package com.bank.Controller;

import com.bank.BeanClass.Customer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EditCustomerController extends HttpServlet {

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int cust_id = Integer.parseInt(req.getParameter("cust_id"));
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String accountNumber = req.getParameter("accountNumber");
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet get = new HttpGet("http://localhost:8080/Bank/api/customer/" + cust_id);
        HttpResponse response = httpClient.execute(get);
        int internResponseStatus = response.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        Customer customer = null;
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONObject obj = new JSONObject(result.toString());
                customer = new Customer(obj.getInt("id"), obj.getString("name"), obj.getString("email"), obj.getString("password"), obj.getString("accountNumber"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        else {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Something problem with customer API!");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        if ((!customer.getEmail().equals(email)) || (!customer.getAccountNumber().equals(accountNumber))) {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Email / Account Number cannot be edited!");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        else {
            response = UpdateCustomer(cust_id, name, email, password, accountNumber, httpClient);
            internResponseStatus = response.getStatusLine().getStatusCode();
            if (200 == internResponseStatus) {
                Customer updatedCustomer = new Customer(cust_id, name, email, password, accountNumber);
                session.setAttribute("customer", updatedCustomer);
                resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
            } else {
                session.setAttribute("error", "true");
                session.setAttribute("message", "Failed to edit customer");
                resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
            }
        }
    }

    static HttpResponse UpdateCustomer(int cust_id, String name, String email, String password, String accountNumber, HttpClient httpClient) throws IOException {
        HttpResponse response;
        HttpPut put = new HttpPut("http://localhost:8080/Bank/api/customer/" + cust_id);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("username", name);
        jo.put("email", email);
        jo.put("password", password);
        jo.put("accountNumber", accountNumber);
        StringEntity stringEntity = new StringEntity(jo.toString());
        put.setEntity(stringEntity);
        response = httpClient.execute(put);
        return response;
    }
    static Customer getCustomer(HttpServletRequest req, HttpServletResponse resp, String email) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get;
        get = new HttpGet("http://localhost:8080/Bank/api/customer/" + email);
        HttpResponse response = httpClient.execute(get);
        int internResponseStatus = response.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        Customer customer = null;
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONObject obj = new JSONObject(result.toString());
                customer = new Customer(obj.getInt("id"), obj.getString("name"), obj.getString("email"), obj.getString("password"), obj.getString("accountNumber"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
//        else {
//            session.setAttribute("error", "true");
//            session.setAttribute("message", "Something problem with customer API!");
//            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
//        }
        return customer;
    }
}