package com.bank.API.Transaction;

import com.bank.BeanClass.Transaction;
import com.bank.DAO.TransactionDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionAPI extends HttpServlet {
    private final TransactionDAO tDs = TransactionDAO.getInstance();

    public TransactionAPI() throws SQLException, ClassNotFoundException {

    }

    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String param = request.getPathInfo().substring(1);
            PrintWriter out = response.getWriter();
            if (isNumeric(param)) {
                com.bank.BeanClass.Transaction t = null;
                int trans_id = Integer.parseInt(param);
                t = tDs.getTransaction(trans_id);
                response.setContentType("application/json");
                JSONArray ja = new JSONArray();
                if (t != null) {
                    JSONObject jo = new JSONObject();
                    jo.put("Transaction ID", t.getTransId());
                    jo.put("Description", t.getDescription());
                    jo.put("Date", t.getDate());
                    jo.put("Debit", t.getAmtDebit());
                    jo.put("Credit", t.getAmtCredit());
                    jo.put("Balance", t.getBalance());
                    jo.put("IsRemoved", t.getIsRemoved());
                    jo.put("Account", t.getAccountNumber());
                    jo.put("ToId", t.getToId());
                    ja.put(jo);
                    out.print(ja);
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("Transaction not found");
                }
                out.flush();
            }
            else {
                ArrayList<Transaction> transactions = tDs.getTransactionsByAccount(param);
                JSONArray ja = new JSONArray();
                for (Transaction t: transactions) {
                    JSONObject jo = new JSONObject();
                    jo.put("Transaction ID", t.getTransId());
                    jo.put("Description", t.getDescription());
                    jo.put("Date", t.getDate());
                    jo.put("Debit", t.getAmtDebit());
                    jo.put("Credit", t.getAmtCredit());
                    jo.put("Balance", t.getBalance());
                    jo.put("IsRemoved", t.getIsRemoved());
                    jo.put("Account", t.getAccountNumber());
                    jo.put("ToId", t.getToId());
                    ja.put(jo);
                }
                response.setContentType("application/json");
                out.print(ja);
            }
        } catch(SQLException | IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int trans_id = Integer.parseInt(req.getPathInfo().substring(1));
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            tDs.deleteTransaction(trans_id);
            out.print("Transaction with id " + trans_id + " is successfully deleted!");
            out.flush();
        } catch (SQLException | IOException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            PrintWriter out = resp.getWriter();
            out.print("Transaction not found");
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        StringBuffer jb = new StringBuffer();
        String line = null;
        int trans_id = Integer.parseInt(req.getPathInfo().substring(1));
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
            try {
                JSONObject jsonObject = new JSONObject(jb.toString());
                String desc = jsonObject.getString("description");
                float debit = jsonObject.getFloat("debit");
                float credit = jsonObject.getFloat("credit");
                float balance = jsonObject.getFloat("balance");
                int toId = jsonObject.getInt("toId");
                String accountNumber = jsonObject.getString("account_num");
                int isRemoved = jsonObject.getInt("isRemoved");
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/text");
                Transaction t = new Transaction(trans_id, desc, debit, credit, balance,accountNumber, toId, isRemoved);
                tDs.updateTransaction(t);
                resp.setStatus(HttpServletResponse.SC_OK);
                out.print("Transaction with id " + trans_id + " is successfully Updated!");
                out.flush();
            } catch (SQLException e) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                PrintWriter out = resp.getWriter();
                out.print("Transaction not found");
                out.flush();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't parse the body content in the put request.");
        }
    }
}
