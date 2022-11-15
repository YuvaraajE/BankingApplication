package com.bank.Controller;

import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;
import com.bank.DAO.TransactionDAO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class RemoveTransaction extends HttpServlet{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int trans_id = Integer.parseInt(request.getPathInfo().substring(1));
        try {
            TransactionDAO trans_ds = TransactionDAO.getInstance();
            Transaction transaction = trans_ds.getTransaction(trans_id);
            transaction.setIsRemoved();
            trans_ds.updateTransaction(transaction);
            HttpSession session = request.getSession();
            ArrayList<Transaction> transactions;
            transactions = trans_ds.getTransactionsByAccount(((Customer)session.getAttribute("customer")).getAccountNumber());
            session.setAttribute("transactions", transactions);
            response.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        catch (SQLException | ClassNotFoundException ex) {
            System.out.println("RuntimeException");
            throw new RuntimeException(ex);
        }
    }
}




