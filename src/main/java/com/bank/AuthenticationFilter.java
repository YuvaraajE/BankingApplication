package com.bank;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
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
            filterChain.doFilter(request, response);
        } else {
            response.sendRedirect(loginURI);
        }
    }
}
