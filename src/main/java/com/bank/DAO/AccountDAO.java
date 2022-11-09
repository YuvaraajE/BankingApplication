package com.bank.DAO;

import com.bank.BeanClass.Account;
import com.bank.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AccountDAO {
    private static AccountDAO obj;
    private java.sql.Connection con;
    public AccountDAO() throws SQLException, ClassNotFoundException {
        this.con = Connection.getInstance().getConnection();
    }
    public static AccountDAO getInstance() throws SQLException, ClassNotFoundException {
        if (obj == null) {
            obj = new AccountDAO();
        }
        return obj;
    }

    public Account getAccount(String accountNumber) throws SQLException {
        String query="select * from account where account_num=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setString(1, accountNumber);
        ResultSet rs=ps.executeQuery();
        Account a = null;
        if (rs.next()) {
            a=new Account();
            a.setAccountNumber(rs.getString("account_num"));
            a.setBalance(rs.getFloat("balance"));
        }
        return a;
    }

    public boolean createAccount(Account a) throws SQLException {
        String query = "insert into account(account_num, balance) values(?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, a.getAccountNumber());
        ps.setFloat(2, a.getBalance());
        return ps.execute();
    }

    public boolean updateAccount(String accountNumber, float balance) throws SQLException {
        String query = "update account set balance=? where account_num=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setFloat(1, balance);
        ps.setString(2, accountNumber);
        return ps.execute();
    }

    public boolean deleteAccount(String accountNum) throws SQLException {
        String query = "delete from account where account_num=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, accountNum);
        return ps.execute();
    }

    public ArrayList<Account> getAccounts() throws SQLException {
        String query = "select * from account";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ArrayList<Account> a = new ArrayList<Account>();
        while (rs.next()) {
            Account a1 = new Account();
            a1.setAccountNumber(rs.getString("account_num"));
            a1.setBalance(rs.getFloat("balance"));
            a.add(a1);
        }
        return a;
    }
}
