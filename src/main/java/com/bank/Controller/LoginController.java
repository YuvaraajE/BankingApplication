package com.bank.Controller;

import com.bank.BeanClass.Account;
import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;
import com.bank.DAO.CustomerDAO;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LoginController extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("login.jsp");
        view.forward(request, response);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String enteredPassword = req.getParameter("password");
        try {
            Customer customer = EditCustomerController.getCustomer(req, resp, email);
            if (customer == null) {
                RequestDispatcher view = req.getRequestDispatcher("login.jsp");
                req.setAttribute("error", "true");
                view.forward(req, resp);
            }
            else {
                String correctPassword = customer.getPassword();
                if (correctPassword.equals(enteredPassword)) {
                    redirectToIndex(req, resp, customer);
                } else {
                    RequestDispatcher view = req.getRequestDispatcher("login.jsp");
                    req.setAttribute("error", "true");
                    view.forward(req, resp);
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            out.print("Customer not found");
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
    }

    private static void redirectToIndex(HttpServletRequest req, HttpServletResponse resp, Customer customer) throws SQLException, ClassNotFoundException, IOException {
        com.bank.BeanClass.Account account = AccountController.getAccount(req, resp, customer.getAccountNumber());
        ArrayList<Transaction> transactions = TransactionController.getTranactionsByAccount(req, resp, customer.getAccountNumber());
        Set<Customer> custSet = new HashSet<Customer>();
        Customer cust = null;
        for (Transaction transaction : transactions) {
            if (transaction.getToId() != -1) {
                cust = EditCustomerController.getCustomer(req, resp, String.valueOf(transaction.getToId()));
            }
            if ((transaction.getToId() > 0 && cust != null) || transaction.getToId() == -1)
            {
                if (transaction.getToId() == -1) {
                    custSet.add(customer);
                }
                else {
                    custSet.add(cust);
                }
            }
        }
        ArrayList<Customer> customers = new ArrayList<>(custSet);
        HttpSession session = req.getSession();
        session.setAttribute("customer", customer);
        session.setAttribute("customers", customers);
        session.setAttribute("account", account);
        session.setAttribute("transactions", transactions);
        session.setAttribute("customerDAO", CustomerDAO.getInstance());
        resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
    }
}
