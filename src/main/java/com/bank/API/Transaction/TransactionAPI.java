package com.bank.API.Transaction;

import com.bank.BeanClass.Transaction;
import com.bank.DAO.TransactionDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class TransactionAPI extends HttpServlet {
    private final TransactionDAO tDs = TransactionDAO.getInstance();

    public TransactionAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            String param = request.getPathInfo().substring(1);
            com.bank.BeanClass.Transaction transaction = null;
            int trans_id = Integer.parseInt(param);
            transaction = tDs.getTransaction(trans_id);
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            if (transaction != null) {
                JSONObject jo = new JSONObject();
                jo.put("Transaction ID", transaction.getTransId());
                jo.put("Description", transaction.getDescription());
                jo.put("Date", transaction.getDate());
                jo.put("Debit", transaction.getAmtDebit());
                jo.put("Credit", transaction.getAmtCredit());
                jo.put("To", transaction.getToId());
                jo.put("Balance", transaction.getBalance());
                out.print(jo);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                out.print("Transaction not found");
            }
            out.flush();
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
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                PrintWriter out = resp.getWriter();
                resp.setContentType("application/text");
                tDs.updateTransaction(new Transaction(desc, debit, credit, balance,accountNumber, toId));
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

            System.out.println("Couldn't parse the body content in the put request.");
        }
    }
}
