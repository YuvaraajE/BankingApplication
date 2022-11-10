package com.bank.API.Account;

import com.bank.BeanClass.Account;
import com.bank.DAO.AccountDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountsAPI extends HttpServlet {
    private final AccountDAO cDs = AccountDAO.getInstance();
    public AccountsAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            ArrayList<Account> accounts = cDs.getAccounts();
            JSONArray ja = new JSONArray();
            for (Account account : accounts) {
                JSONObject jo = new JSONObject();
                jo.put("account_num", account.getAccountNumber());
                jo.put("balance", account.getBalance());
                ja.put(jo);
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(ja);
            out.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e)
        {
            System.out.println("Couldn't find body in the post request.");
        }
        try {
            JSONObject jsonObject =  new JSONObject(jb.toString());
            String accountNumber = jsonObject.getString("account_num");
            Float balance = jsonObject.getFloat("balance");
            cDs.createAccount(new Account(accountNumber, balance));
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            out.print("Account is successfully created!");
            out.flush();
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.setContentType("application/text");
                PrintWriter out = resp.getWriter();
                out.print("Account already exists!");
                out.flush();
            }
        }
    }
}
