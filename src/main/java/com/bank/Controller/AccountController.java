package com.bank.Controller;

import com.bank.BeanClass.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AccountController {
    static Account getAccount(HttpServletRequest req, HttpServletResponse resp, String accountNumber) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet get;
        get = new HttpGet("http://localhost:8080/Bank/api/account/" + accountNumber);
        HttpResponse response = httpClient.execute(get);
        int internResponseStatus = response.getStatusLine().getStatusCode();
        HttpSession session = req.getSession();
        Account account = null;
        if(200 == internResponseStatus) {
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer result = new StringBuffer();
            String line = "";
            while ((line = rd.readLine()) != null) {
                result.append(line);
            }
            try {
                JSONObject obj = new JSONObject(result.toString());
                account = new Account(obj.getString("accountNumber"), obj.getFloat("Balance"));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }
//        else {
//            session.setAttribute("error", "true");
//            session.setAttribute("message", "Something problem with customer API!");
//            resp.sendRedirect("http://localhost:8080/Bank/?customersOp=0");
//        }
        return account;
    }

    static HttpResponse UpdateAccount(String accountNumber, float balance,HttpClient httpClient) throws IOException {
        HttpResponse response;
        HttpPut put = new HttpPut("http://localhost:8080/Bank/api/account/" + accountNumber);
        put.setHeader("Accept", "application/json");
        put.setHeader("Content-type", "application/json");
        JSONObject jo = new JSONObject();
        jo.put("accountNumber", accountNumber);
        jo.put("balance", balance);
        StringEntity stringEntity = new StringEntity(jo.toString());
        put.setEntity(stringEntity);
        response = httpClient.execute(put);
        return response;
    }
}
