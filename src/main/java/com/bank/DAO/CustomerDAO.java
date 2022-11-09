package com.bank.DAO;

import com.bank.BeanClass.Customer;
import com.bank.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CustomerDAO {
    public static CustomerDAO obj = null;
    private java.sql.Connection con = null;
    public CustomerDAO() throws SQLException, ClassNotFoundException {
        this.con = Connection.getInstance().getConnection();
    }

    public static CustomerDAO getInstance() throws SQLException, ClassNotFoundException {
        if (obj == null) {
            obj = new CustomerDAO();
        }
        return obj;
    }
    public ArrayList<Customer> getCustomers() throws SQLException {
        ArrayList<Customer> list = new ArrayList<Customer>();
        String query="select * from Customer";
        PreparedStatement ps=con.prepareStatement(query);
        ResultSet rs=ps.executeQuery();
        Customer c = null;
        while (rs.next()) {
            list.add(new Customer(rs.getString("name"), rs.getString("email"), rs.getString("password"), rs.getString("account_num")));
        }
        return list;
    }

    public Customer getCustomerByCustID(int cust_id) throws SQLException {
        String query="select * from Customer where cust_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1, cust_id);
        ResultSet rs=ps.executeQuery();
        Customer c = null;
        if (rs.next()) {
            c=new Customer();
            c.setName(rs.getString("name"));
            c.setEmail(rs.getString("email"));
            c.setPassword(rs.getString("password"));
            c.setAccountNumber(rs.getString("account_num"));
        }
        return c;
    }

    public boolean createCustomer(Customer c) throws SQLException {
        String query = "insert into Customer(name,email,password,account_num) values(?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, c.getName());
        ps.setString(2, c.getEmail());
        ps.setString(3, c.getPassword());
        ps.setString(4, c.getAccountNumber());
        System.out.println(c);
        return ps.execute();
    }

    public boolean updateCustomer(int cust_id, String name, String email, String password, String accountNumber) throws SQLException {
        String query = "update Customer set name=?,email=?,password=? account_num=? where cust_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, accountNumber);
        ps.setInt(5, cust_id);
        return ps.execute();
    }

    public boolean deleteCustomer(int cust_id) throws SQLException {
        String query = "delete from Customer where cust_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, cust_id);
        return ps.execute();
    }
}
