package com.bank.Controller;

import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class RemoveTransaction extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int trans_id = Integer.parseInt(request.getPathInfo().substring(1));
        Transaction transaction = TransactionController.getTransaction(request, response, trans_id);
        transaction.setIsRemoved();
        TransactionController.updateTransaction(request, response, transaction);
        HttpSession session = request.getSession();
        ArrayList<Transaction> transactions;
        transactions = TransactionController.getTranactionsByAccount(request, response, ((Customer)session.getAttribute("customer")).getAccountNumber());
        session.setAttribute("transactions", transactions);
        response.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
    }
}




