package com.bank.Controller;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jasypt.util.text.BasicTextEncryptor;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Base64;


public class RegisterController extends HttpServlet{

    public static BasicTextEncryptor textEncryptor = null;
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher view = request.getRequestDispatcher("register.jsp");
        view.forward(request, response);
    }public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = new String(Base64.getDecoder().decode(req.getParameter("password")));
        String accountNumber = req.getParameter("accNum");
        String panNumber = req.getParameter("panNumber");
        float balance = 0;
        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse res = EditCustomerController.CreateCustomer(name, email, password, accountNumber, panNumber,httpClient);
        if(200 == res.getStatusLine().getStatusCode()) {
            resp.sendRedirect("http://localhost:8080/Bank/login");
        }
        else {
            HttpSession session = req.getSession();
            session.setAttribute("error", "true");
            session.setAttribute("message", "Could not Register!");
            resp.sendRedirect("http://localhost:8080/Bank/register");
        }
    }

    public static BasicTextEncryptor getEncryptor() {
        if (textEncryptor == null) {
            textEncryptor = new BasicTextEncryptor();
            String key = "X2#23432rSKJKSL12";
            textEncryptor.setPasswordCharArray(key.toCharArray());
        }
        return textEncryptor;
    }
}




