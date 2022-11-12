package com.bank.API.Account;

import com.bank.DAO.AccountDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class AccountAPI extends HttpServlet {
    private final AccountDAO cDs = AccountDAO.getInstance();

    public AccountAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String account_num = request.getPathInfo().substring(1);
            com.bank.BeanClass.Account acc = cDs.getAccount(account_num);
            JSONObject jo = new JSONObject();
            jo.put("accountNumber", acc.getAccountNumber());
            jo.put("Balance", acc.getBalance());
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jo);
            out.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            String account_num = req.getPathInfo().substring(1);
            cDs.deleteAccount(account_num);
            resp.setContentType("application/text");
            out.print("Account with acc_num: " + account_num + " is successfully deleted!");
            out.flush();
        } catch (SQLException e) {
            out.print("Account could not be deleted!");
            out.flush();
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        String account_num = req.getPathInfo().substring(1);
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println("Couldn't find body in the post request.");
        }
        try {
            JSONObject jsonObject = new JSONObject(jb.toString());
            String accountNumber = jsonObject.getString("accountNumber");
            float balance = jsonObject.getFloat("balance");
            cDs.updateAccount(accountNumber, balance);
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            out.print("Account with acc_num: " + accountNumber + " is successfully Updated!");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
