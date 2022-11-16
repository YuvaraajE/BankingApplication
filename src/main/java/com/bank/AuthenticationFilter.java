package com.bank;

import com.bank.BeanClass.Account;
import com.bank.BeanClass.Customer;
import com.bank.BeanClass.Transaction;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class AuthenticationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();
        String loginURI = request.getContextPath() + "/login";
        String registerURI = request.getContextPath() + "/register";
        boolean loggedIn = session != null && session.getAttribute("customer") != null;
        boolean isAPI = request.getRequestURI().contains(request.getContextPath() + "/api");
        boolean loginRequest = request.getRequestURI().equals(loginURI);
        boolean registerRequest = request.getRequestURI().equals(registerURI);
        boolean isStaticFile = request.getRequestURI().contains(request.getContextPath() + "/scripts");

        if (loggedIn || loginRequest || registerRequest || isAPI || isStaticFile) {
            if (isAPI && isValidated(request, response)) {
                filterChain.doFilter(request, response);
            }
            else if (!isAPI) {
                filterChain.doFilter(request, response);
            }
            else {
                session.setAttribute("error", "true");
                session.setAttribute("message", "Unauthorized Access");
                response.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
            }
        } else {
            response.sendRedirect(loginURI);
        }
    }

    private boolean isValidated(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        Customer curr_cust = (Customer) session.getAttribute("customer");
        Account cur_acc = (Account) session.getAttribute("account");
        ArrayList<Transaction> transactions = (ArrayList<Transaction>) session.getAttribute("transactions");
        String[] urlTokens = req.getRequestURL().toString().split("/");
        String type = urlTokens[urlTokens.length - 2];
        String value = urlTokens[urlTokens.length - 1];
        return true;
    }
}
