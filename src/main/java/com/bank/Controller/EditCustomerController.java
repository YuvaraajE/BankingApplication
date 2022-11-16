package com.bank.Controller;

import com.bank.BeanClass.Customer;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.jasypt.util.text.BasicTextEncryptor;
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
        String panNumber = req.getParameter("panNumber");
        HttpClient httpClient = HttpClients.createDefault();

        HttpGet get = new HttpGet("http://localhost:8080/Bank/api/customer/" + cust_id);
        HttpResponse response1 = httpClient.execute(get);
        int internResponseStatus = response1.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        Customer customer = null;
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            JSONObject obj = new JSONObject(result.toString());
            customer = new Customer(obj.getInt("id"), obj.getString("name"), obj.getString("email"), obj.getString("password"), obj.getString("accountNumber"), obj.getString("panNumber"));
            if ((!customer.getEmail().equals(email)) || (!customer.getAccountNumber().equals(accountNumber))) {
                session.setAttribute("error", "true");
                session.setAttribute("message", "Email / Account Number cannot be edited!");
            }
            else {
                BasicTextEncryptor textEncryptor = RegisterController.getEncryptor();
                String encryptedPassword = textEncryptor.encrypt(password);
                HttpResponse response2 = UpdateCustomer(cust_id, name, email, encryptedPassword, accountNumber, panNumber, httpClient);
                internResponseStatus = response2.getStatusLine().getStatusCode();
                if (200 == internResponseStatus) {
                    Customer updatedCustomer = new Customer(cust_id, name, email, encryptedPassword, accountNumber, panNumber);
                    session.setAttribute("customer", updatedCustomer);
                } else {
                    session.setAttribute("error", "true");
                    session.setAttribute("message", "Failed to edit customer");
                }
            }
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
        else {
            session.setAttribute("error", "true");
            session.setAttribute("message", "Something problem with customer API!");
            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
        }
    }

    static HttpResponse CreateCustomer(String username, String email, String password, String accountNumber, String panNumber, HttpClient httpClient) throws IOException {
        AccountController.createAccount(accountNumber, 0f, httpClient);
        BasicTextEncryptor textEncryptor = RegisterController.getEncryptor();
        String encryptedPassword = textEncryptor.encrypt(password);
        HttpPost post = new HttpPost("http://localhost:8080/Bank/api/customer");
        post.setHeader("Accept", "application/json");
        post.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("username", username);
        jo.put("email", email);
        jo.put("password", encryptedPassword);
        jo.put("accountNumber", accountNumber);
        jo.put("panNumber", panNumber);
        StringEntity stringEntity = new StringEntity(jo.toString());
        post.setEntity(stringEntity);
        return httpClient.execute(post);
    }

    static HttpResponse UpdateCustomer(int cust_id, String name, String email, String password, String accountNumber, String panNumber, HttpClient httpClient) throws IOException {
        HttpResponse response;
        HttpPut put = new HttpPut("http://localhost:8080/Bank/api/customer/" + cust_id);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("username", name);
        jo.put("email", email);
        jo.put("password", password);
        jo.put("accountNumber", accountNumber);
        jo.put("panNumber", panNumber);
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
            StringBuilder result = new StringBuilder();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONObject obj = new JSONObject(result.toString());
                customer = new Customer(obj.getInt("id"), obj.getString("name"), obj.getString("email"), obj.getString("password"), obj.getString("accountNumber"), obj.getString("panNumber"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
        return customer;
    }
}