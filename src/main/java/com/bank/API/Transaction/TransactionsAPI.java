package com.bank.API.Transaction;

import com.bank.BeanClass.Transaction;
import com.bank.DAO.TransactionDAO;
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

public class TransactionsAPI extends HttpServlet {
    private final TransactionDAO tDs = TransactionDAO.getInstance();
    public TransactionsAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            ArrayList<Transaction> transactions = tDs.getTransactions();
            JSONArray ja = new JSONArray();
            for (Transaction transaction : transactions) {
                JSONObject jo = new JSONObject();
                jo.put("Transaction ID", transaction.getTransId());
                jo.put("Description", transaction.getDescription());
                jo.put("Date", transaction.getDate());
                jo.put("Debit", transaction.getAmtDebit());
                jo.put("Credit", transaction.getAmtCredit());
                jo.put("Balance", transaction.getBalance());
                jo.put("IsRemoved", transaction.getIsRemoved());
                jo.put("Account", transaction.getAccountNumber());
                jo.put("ToId", transaction.getToId());
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
            String desc = jsonObject.getString("description");
            float debit = jsonObject.getFloat("debit");
            float credit = jsonObject.getFloat("credit");
            float balance = jsonObject.getFloat("balance");
            int toId = jsonObject.getInt("toId");
            String accountNumber = jsonObject.getString("account_num");
            TransactionDAO tDs = TransactionDAO.getInstance();
            int trans_id = tDs.createTransaction(new Transaction(desc, debit, credit, balance, accountNumber, toId, 0));
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            JSONObject jo = new JSONObject();
            jo.put("trans_id", trans_id);
            out.print(jo);
            out.flush();
        }
        catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
        }
    }
}
