package com.bank.API.Customer;

import com.bank.BeanClass.Customer;
import com.bank.DAO.CustomerDAO;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomersAPI extends HttpServlet {
    private final CustomerDAO cDs = CustomerDAO.getInstance();
    public CustomersAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response){
        try{
            ArrayList<Customer> customers = cDs.getCustomers();
            JSONArray ja = new JSONArray();
            for (Customer customer : customers) {
                JSONObject jo = new JSONObject();
                jo.put("id", customer.getId());
                jo.put("email", customer.getEmail());
                jo.put("password", customer.getPassword());
                jo.put("accountNumber", customer.getAccountNumber());
                jo.put("panNumber", customer.getPANNumber());
                ja.put(jo);
            }
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(ja);
            out.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println("Couldn't find body in the post request.");
        }
        try {
            JSONObject jsonObject =  new JSONObject(jb.toString());
            String userName = jsonObject.getString("username");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String accountNumber = jsonObject.getString("accountNumber");
            String panNumber = jsonObject.getString("panNumber");
            CustomerDAO cDs = CustomerDAO.getInstance();
            cDs.createCustomer(new Customer(userName, email, password, accountNumber, panNumber));
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            out.print("New customer is successfully Created!");
            out.flush();
        } catch (SQLException | ClassNotFoundException e) {
            if (e.getMessage().contains("Duplicate")) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                resp.setContentType("application/text");
                PrintWriter out = resp.getWriter();
                out.print("User with that email already exists!");
                out.flush();
            }
        }
    }
}
