package com.bank.Controller;

import com.bank.BeanClass.Account;
import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jasypt.util.text.BasicTextEncryptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TransactionController extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        String type = req.getPathInfo().substring(1);
        String description = req.getParameter("description");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        ArrayList<Customer> customers = (ArrayList<Customer>) session.getAttribute("customers");
        BasicTextEncryptor textEncryptor = RegisterController.getEncryptor();
        String correctPasswordEncrypted = customer.getPassword();
        String decryptedData = textEncryptor.decrypt(correctPasswordEncrypted);
        if (password == null || description == null) {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Description/Password cannot be empty!");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        else if (!password.equals(decryptedData)) {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Password is incorrect");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        else {
            if (type.equals("deposit")) {
                float credit = Float.parseFloat(req.getParameter("depositMoney"));
                if (credit <= 0) {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Deposit amount should be greater than zero!");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else {
                    float debit = 0;
                    Account account = (Account) session.getAttribute("account");
                    float balance = account.getBalance();
                    balance = balance + credit;
                    Transaction t = new Transaction(description, debit, credit, balance, account.getAccountNumber(), -1);
                    int id = createTransaction(req, resp, t);
                    t.setTransId(id);
                    ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                    transactions.add(t);
                    account.setBalance(t.getBalance());
                    AccountController.UpdateAccount(account.getAccountNumber(), account.getBalance(), httpClient);
                    session.setAttribute("account", account);
                    session.setAttribute("transactions", transactions);
                    updateCustomersSession(session, customer, customers);
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
            }
            else if (type.equals("withdraw")) {
                float debit = Float.parseFloat(req.getParameter("withdrawMoney"));
                Account account = (Account) session.getAttribute("account");
                if (debit <= 0) {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Withdraw amount should be greater than zero!");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else if (account.getBalance() < debit) {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Not enough balance to complete the transaction");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else {
                    float credit = 0;
                    float balance = account.getBalance();
                    balance = balance - debit;
                    Transaction t = new Transaction(description, debit, credit, balance, account.getAccountNumber(), -1);
                    int id = createTransaction(req, resp, t);
                    t.setTransId(id);
                    ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                    transactions.add(t);
                    account.setBalance(t.getBalance());
                    AccountController.UpdateAccount(account.getAccountNumber(), account.getBalance(), httpClient);
                    session.setAttribute("account", account);
                    session.setAttribute("transactions", transactions);
                    updateCustomersSession(session, customer, customers);
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
            }
            else if (type.equals("transfer")) {
                float debit1 = Float.parseFloat(req.getParameter("transferMoney")), debit2 = 0;
                float credit1 = 0, credit2 = Float.parseFloat(req.getParameter("transferMoney"));
                Account account = (Account) session.getAttribute("account");
                Account account2 = AccountController.getAccount(req, resp, req.getParameter("transferAccount"));
                if (debit1 <= 0) {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Transfer amount should be greater than zero!");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else if(account2 == null){
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Account Number does not exists!");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else if (account.getBalance() <= debit1) {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Not enough balance to make transaction!");
                    resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                }
                else {
                    Customer cust2 = EditCustomerController.getCustomer(req, resp, account2.getAccountNumber());
                    if (customer.getId() == cust2.getId()) {
                        float balance1 = 0;
                        float balance2 = 0;
                        Transaction t1, t2;
                        balance1 = account.getBalance() - debit1;
                        t1 = new Transaction(description, debit1, credit1, balance1, account.getAccountNumber(), -1);
                        int id1 = TransactionController.createTransaction(req, resp, t1);
                        t1.setTransId(id1);
                        account.setBalance(t1.getBalance());
                        AccountController.UpdateAccount(account.getAccountNumber(), account.getBalance(), httpClient);
                        balance2 = account.getBalance() + credit2;
                        t2 = new Transaction(description, debit2, credit2, balance2, account2.getAccountNumber(), -1);
                        int id2 = TransactionController.createTransaction(req, resp, t2);
                        t2.setTransId(id2);
                        account.setBalance(t2.getBalance());
                        AccountController.UpdateAccount(account.getAccountNumber(), account.getBalance(), httpClient);
                        ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                        transactions.add(t1);
                        transactions.add(t2);
                        session.setAttribute("account", account);
                        session.setAttribute("transactions", transactions);
                        updateCustomersSession(session, cust2, customers);
                        resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                    }
                    else {
                        float balance1 = account.getBalance(); balance1 = balance1 - debit1;
                        float balance2 = account2.getBalance(); balance2 = balance2 + credit2;
                        Transaction t1, t2;
                        t1 = new Transaction(description, debit1, credit1, balance1, account.getAccountNumber(), cust2.getId());
                        t2 = new Transaction(description, debit2, credit2, balance2, account2.getAccountNumber(), customer.getId());
                        ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                        int id1 = TransactionController.createTransaction(req, resp, t1);
                        int id2 = TransactionController.createTransaction(req, resp, t2);
                        t1.setTransId(id1);
                        t2.setTransId(id2);
                        account.setBalance(t1.getBalance());
                        account2.setBalance(t2.getBalance());
                        AccountController.UpdateAccount(account.getAccountNumber(), account.getBalance(), httpClient);
                        AccountController.UpdateAccount(account2.getAccountNumber(), account2.getBalance(), httpClient);
                        transactions.add(t1);
                        session.setAttribute("account", account);
                        session.setAttribute("transactions", transactions);
                        updateCustomersSession(session, cust2, customers);
                        resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                    }
                }
            }
        }
    }

    static Transaction getTransaction(HttpServletRequest req, HttpServletResponse resp, int trans_id) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get;
        get = new HttpGet("http://localhost:8080/Bank/api/transaction/" + trans_id);
        HttpResponse response = httpClient.execute(get);
        int internResponseStatus = response.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        Transaction transaction = null;
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONArray obj = new JSONArray(result.toString());
                for(int i = 0; i < obj.length(); i++)
                {
                    JSONObject jObj = obj.getJSONObject(i);
                    transaction = new Transaction(jObj.getInt("Transaction ID"), jObj.getString("Description"), jObj.getFloat("Debit"), jObj.getFloat("Credit"), jObj.getFloat("Balance"), jObj.getString("Account"), jObj.getInt("ToId"), jObj.getInt("IsRemoved"));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
//        else {
//            session.setAttribute("error", "true");
//            session.setAttribute("message", "Something problem with customer API!");
//            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
//        }
        return transaction;
    }
    static ArrayList<Transaction> getTranactionsByAccount(HttpServletRequest req, HttpServletResponse resp, String accountNumber) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get;
        get = new HttpGet("http://localhost:8080/Bank/api/transaction/" + accountNumber);
        HttpResponse response = httpClient.execute(get);
        int internResponseStatus = response.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        ArrayList<Transaction> transactions = new ArrayList<Transaction>();
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONArray obj = new JSONArray(result.toString());
                for(int i = 0; i < obj.length(); i++)
                {
                    JSONObject jObj = obj.getJSONObject(i);
                    transactions.add(new Transaction(jObj.getInt("Transaction ID"), jObj.getString("Description"), jObj.getFloat("Debit"), jObj.getFloat("Credit"), jObj.getFloat("Balance"), accountNumber, jObj.getInt("ToId"), jObj.getInt("IsRemoved")));
                }
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
//        else {
//            session.setAttribute("error", "true");
//            session.setAttribute("message", "Something problem with customer API!");
//            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
//        }
        return transactions;
    }

    static void updateTransaction(HttpServletRequest req, HttpServletResponse resp, Transaction transaction) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPut put = new HttpPut("http://localhost:8080/Bank/api/transaction/" + transaction.getTransId());
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("transId", transaction.getTransId());
        jo.put("description", transaction.getDescription());
        jo.put("debit", transaction.getAmtDebit());
        jo.put("credit", transaction.getAmtCredit());
        jo.put("balance", transaction.getBalance());
        jo.put("toId", transaction.getToId());
        jo.put("account_num", transaction.getAccountNumber());
        jo.put("isRemoved", transaction.getIsRemoved());
        StringEntity stringEntity = new StringEntity(jo.toString());
        put.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(put);
    }

    static int createTransaction(HttpServletRequest req, HttpServletResponse resp, Transaction transaction) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost post = new HttpPost("http://localhost:8080/Bank/api/transaction");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("description", transaction.getDescription());
        jo.put("debit", transaction.getAmtDebit());
        jo.put("credit", transaction.getAmtCredit());
        jo.put("balance", transaction.getBalance());
        jo.put("toId", transaction.getToId());
        jo.put("account_num", transaction.getAccountNumber());
        StringEntity stringEntity = new StringEntity(jo.toString());
        post.setEntity(stringEntity);
        HttpResponse response = httpClient.execute(post);
        String result = convertStreamToString(response.getEntity().getContent());
        System.out.println("result: " + result);
        return new JSONObject(result).getInt("trans_id");
    }

    private static void updateCustomersSession(HttpSession session, Customer cust2, List<Customer> customers) {
        boolean flag = true;
        for (Customer customer : customers) {
            if (customer.getId() == cust2.getId()) {
                flag = false;
                break;
            }
        }
        if (flag) {
            customers.add(cust2);
            session.setAttribute("customers", customers);
        }
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
}