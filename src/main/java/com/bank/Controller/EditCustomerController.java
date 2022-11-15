package com.bank.Controller;

import com.bank.BeanClass.Customer;
import com.bank.DAO.CustomerDAO;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class EditCustomerController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int cust_id = Integer.parseInt(req.getParameter("cust_id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String accountNumber = req.getParameter("accountNumber");
        try {
            CustomerDAO cust_ds = CustomerDAO.getInstance();
            Customer customer = cust_ds.getCustomerByCustID(cust_id);
            HttpSession session = req.getSession();
            if ((!customer.getEmail().equals(email)) || (!customer.getAccountNumber().equals(accountNumber))) {
                session.setAttribute("error", "true");
                session.setAttribute("message", "Email / Account Number cannot be edited!");
                resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
            }
            else {
                cust_ds.updateCustomer(cust_id, name, email, password, accountNumber);
                Customer updatedCustomer = cust_ds.getCustomerByCustID(cust_id);
                session.setAttribute("customer", updatedCustomer);
                resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
            }
        }
        catch (SQLException | ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }
}