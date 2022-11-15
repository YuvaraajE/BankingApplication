package com.bank.Controller;

import com.bank.BeanClass.Account;
import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;
import com.bank.DAO.AccountDAO;
import com.bank.DAO.CustomerDAO;
import com.bank.DAO.TransactionDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TransactionController extends HttpServlet {
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String type = req.getPathInfo().substring(1);
        String description = req.getParameter("description");
        String password = req.getParameter("password");
        HttpSession session = req.getSession();
        Customer customer = (Customer) session.getAttribute("customer");
        ArrayList<Customer> customers = (ArrayList<Customer>) session.getAttribute("customers");
        if (password == null || description == null) {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Description/Password cannot be empty!");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        else if (!password.equals(customer.getPassword())) {
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
                    TransactionDAO tDs = null;
                    try {
                        Transaction t = new Transaction(description, debit, credit, balance, account.getAccountNumber(), -1);
                        TransactionDAO.getInstance().createTransaction(t);
                        ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                        transactions.add(t);
                        account.setBalance(t.getBalance());
                        AccountDAO.getInstance().updateAccount(account.getAccountNumber(), account.getBalance());
                        session.setAttribute("account", account);
                        session.setAttribute("transactions", transactions);
                        updateCustomers(session, customer, customers);
                        resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
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
                    try {
                        Transaction t = new Transaction(description, debit, credit, balance, account.getAccountNumber(), -1);
                        TransactionDAO.getInstance().createTransaction(t);
                        ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                        transactions.add(t);
                        account.setBalance(t.getBalance());
                        AccountDAO.getInstance().updateAccount(account.getAccountNumber(), account.getBalance());
                        session.setAttribute("account", account);
                        session.setAttribute("transactions", transactions);
                        updateCustomers(session, customer, customers);
                        resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else if (type.equals("transfer")) {
                float debit1 = Float.parseFloat(req.getParameter("transferMoney")), debit2 = 0;
                float credit1 = 0, credit2 = Float.parseFloat(req.getParameter("transferMoney"));
                Account account2 = null;
                Account account = (Account) session.getAttribute("account");
                try {
                    account2 = AccountDAO.getInstance().getAccount(req.getParameter("transferAccount"));
                } catch (SQLException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
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
                    try {
                        Customer cust2 = CustomerDAO.getInstance().getCustomerByAcccountNumber(account2.getAccountNumber());
                        if (customer.getId() == cust2.getId()) {
                            float balance1 = 0;
                            float balance2 = 0;
                            Transaction t1, t2;
                            balance1 = account.getBalance() - debit1;
                            t1 = new Transaction(description, debit1, credit1, balance1, account.getAccountNumber(), cust2.getId());
                            TransactionDAO.getInstance().createTransaction(t1);
                            account.setBalance(t1.getBalance());
                            AccountDAO.getInstance().updateAccount(account.getAccountNumber(), account.getBalance());
                            balance2 = account.getBalance() + credit2;
                            t2 = new Transaction(description, debit2, credit2, balance2, account2.getAccountNumber(), customer.getId());
                            TransactionDAO.getInstance().createTransaction(t2);
                            account.setBalance(t2.getBalance());
                            AccountDAO.getInstance().updateAccount(account.getAccountNumber(), account.getBalance());
                            ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                            transactions.add(t1);
                            transactions.add(t2);
                            session.setAttribute("account", account);
                            session.setAttribute("transactions", transactions);
                            updateCustomers(session, cust2, customers);
                            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                        }
                        else {
                            float balance1 = account.getBalance(); balance1 = balance1 - debit1;
                            float balance2 = account2.getBalance(); balance2 = balance2 + credit2;
                            Transaction t1, t2;
                            t1 = new Transaction(description, debit1, credit1, balance1, account.getAccountNumber(), cust2.getId());
                            t2 = new Transaction(description, debit2, credit2, balance2, account2.getAccountNumber(), customer.getId());
                            ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
                            TransactionDAO.getInstance().createTransaction(t1);
                            TransactionDAO.getInstance().createTransaction(t2);
                            account.setBalance(t1.getBalance());
                            account2.setBalance(t2.getBalance());
                            AccountDAO.getInstance().updateAccount(account.getAccountNumber(), account.getBalance());
                            AccountDAO.getInstance().updateAccount(account2.getAccountNumber(), account2.getBalance());
                            transactions.add(t1);
                            session.setAttribute("account", account);
                            session.setAttribute("transactions", transactions);
                            updateCustomers(session, cust2, customers);
                            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
                        }
                    } catch (SQLException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    private static void updateCustomers(HttpSession session, Customer cust2, List<Customer> customers) {
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
}