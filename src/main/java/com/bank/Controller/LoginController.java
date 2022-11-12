package com.bank.Controller;

import com.bank.BeanClass.Transaction;
import com.bank.DAO.AccountDAO;
import com.bank.DAO.CustomerDAO;
import com.bank.DAO.TransactionDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoginController extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("login.jsp");
        view.forward(request, response);
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String email = req.getParameter("email");
        String enteredPassword = req.getParameter("password");
        try {
            CustomerDAO cust_ds = CustomerDAO.getInstance();
            com.bank.BeanClass.Customer customer = cust_ds.getCustomerByEmail(email);
            if (customer == null) {
                RequestDispatcher view = req.getRequestDispatcher("login.jsp");
                req.setAttribute("error", "true");
                view.forward(req, resp);
            }
            else {
                String correctPassword = customer.getPassword();
                if (correctPassword.equals(enteredPassword)) {
                    AccountDAO accountDAO = AccountDAO.getInstance();
                    com.bank.BeanClass.Account account = accountDAO.getAccount(customer.getAccountNumber());
                    ArrayList<Transaction> transactions = TransactionDAO.getInstance().getTransactionsByAccount(customer.getAccountNumber());
                    HttpSession session = req.getSession();
                    session.setAttribute("customer", customer);
                    session.setAttribute("account", account);
                    session.setAttribute("transactions", transactions);
                    resp.sendRedirect("http://localhost:8080/Bank/");
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
}
