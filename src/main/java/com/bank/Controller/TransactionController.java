package com.bank.Controller;

import com.bank.BeanClass.Account;
import com.bank.BeanClass.Transaction;
import com.bank.DAO.TransactionDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        //TODO: Deposit not working properly
        String type = req.getPathInfo().substring(1);
        if (type.equals("deposit")) {
            float credit = Float.parseFloat(req.getParameter("depositMoney"));
            float debit = 0;
            String description = req.getParameter("description");
            String password = req.getParameter("password");
            HttpSession session = req.getSession();
            Account account = (Account) session.getAttribute("account");
            String accountNumber = account.getAccountNumber();
            float balance = account.getBalance();
            balance = balance + credit;
            TransactionDAO tDs = null;
            try {
                tDs = TransactionDAO.getInstance();
                Transaction t = new Transaction(description, debit ,credit, balance, accountNumber);
                tDs.createTransaction(t);
                ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                transactions.add(t);
                account.setBalance(t.getBalance());
                session.setAttribute("account", account);
                session.setAttribute("transactions", transactions);
                resp.sendRedirect("http://localhost:8080/Bank/");
            } catch (SQLException | ClassNotFoundException e) {
                resp.setContentType("application/text");
                PrintWriter out = resp.getWriter();
                out.print("Transaction is not created!");
                out.flush();
                throw new RuntimeException(e);
            }
        }
    }
}