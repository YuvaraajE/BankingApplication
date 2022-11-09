package com.bank.API.Customer;

import com.bank.DAO.CustomerDAO;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class CustomerAPI extends HttpServlet {
    private final CustomerDAO cDs = CustomerDAO.getInstance();

    public CustomerAPI() throws SQLException, ClassNotFoundException {

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            int cust_id = Integer.parseInt(request.getPathInfo().substring(1));
            com.bank.BeanClass.Customer customer = cDs.getCustomerByCustID(cust_id);
            JSONObject jo = new JSONObject();
            jo.put("id", customer.getId());
            jo.put("email", customer.getEmail());
            jo.put("password", customer.getPassword());
            jo.put("accountNumber", customer.getAccountNumber());
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print(jo);
            out.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int cust_id = Integer.parseInt(req.getPathInfo().substring(1));
            cDs.deleteCustomer(cust_id);
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            out.print("Customer with id " + cust_id + " is successfully deleted!");
            out.flush();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StringBuffer jb = new StringBuffer();
        String line = null;
        int cust_id = Integer.parseInt(req.getPathInfo().substring(1));
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) {
            System.out.println("Couldn't find body in the post request.");
        }
        try {
            JSONObject jsonObject = new JSONObject(jb.toString());
            String userName = jsonObject.getString("username");
            String email = jsonObject.getString("email");
            String password = jsonObject.getString("password");
            String accountNumber = jsonObject.getString("accountNumber");
            cDs.updateCustomer(cust_id, userName, email, password, accountNumber);
            resp.setContentType("application/text");
            PrintWriter out = resp.getWriter();
            out.print("Customer with id " + cust_id + " is successfully Updated!");
            out.flush();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
