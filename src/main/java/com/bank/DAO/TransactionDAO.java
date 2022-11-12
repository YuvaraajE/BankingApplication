package com.bank.DAO;

import com.bank.BeanClass.Transaction;
import com.bank.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TransactionDAO {
    private static TransactionDAO obj;
    private java.sql.Connection con;
    public TransactionDAO() throws SQLException, ClassNotFoundException {
        this.con = Connection.getInstance().getConnection();
    }
    public static TransactionDAO getInstance() throws SQLException, ClassNotFoundException {
        if (obj == null) {
            obj = new TransactionDAO();
        }
        return obj;
    }

    public Transaction getTransaction(int trans_id) throws SQLException {
        String query="select * from Transaction where trans_id=?";
        PreparedStatement ps=con.prepareStatement(query);
        ps.setInt(1, trans_id);
        ResultSet rs=ps.executeQuery();
        Transaction t = null;
        if (rs.next()) {
            t=new Transaction(rs.getString("description"), rs.getFloat("debit"), rs.getFloat("credit"), rs.getFloat("balance"), rs.getString("account_num"));
            t.setTransId(trans_id);
        }
        return t;
    }

    public boolean createTransaction(Transaction t) throws SQLException {
        String query = "insert into Transaction(date_time,description,debit,credit,balance,account_num) values (?,?,?,?,?,?)";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setDate(1, t.getDate());
        ps.setString(2, t.getDescription());
        ps.setFloat(3, t.getAmtDebit());
        ps.setFloat(4, t.getAmtCredit());
        ps.setFloat(5, t.getBalance());
        ps.setString(6, t.getAccountNumber());
        return ps.execute();
    }

    public boolean updateTransaction(String desc, float debit, float credit, float balance, int trans_id, String accountNumber) throws SQLException {
        String query = "update Transaction set description=?,debit=?,credit=?,balance=?,account_num=? where trans_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, desc);
        ps.setFloat(2, debit);
        ps.setFloat(3, credit);
        ps.setFloat(4, balance);
        ps.setString(5, accountNumber);
        ps.setInt(6, trans_id);
        return ps.execute();
    }

    public boolean deleteTransaction(int trans_id) throws SQLException {
        String query = "delete from Transaction where trans_id=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, trans_id);
        return ps.execute();
    }

    public ArrayList<Transaction> getTransactions() throws SQLException {
        String query = "select * from Transaction";
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        ArrayList<Transaction> a = new ArrayList<Transaction>();
        while (rs.next()) {
            Transaction t1 = new Transaction(rs.getString("description"), rs.getFloat("debit"), rs.getFloat("credit"), rs.getFloat("balance"), rs.getString("account_num"));
            t1.setTransId(rs.getInt("trans_id"));
            a.add(t1);
        }
        return a;
    }

    public ArrayList<Transaction> getTransactionsByAccount(String account_num) throws SQLException {
        String query = "select * from Transaction where account_num=?";
        PreparedStatement ps = con.prepareStatement(query);
        ps.setString(1, account_num);
        ResultSet rs = ps.executeQuery();
        ArrayList<Transaction> a = new ArrayList<Transaction>();
        while (rs.next()) {
            Transaction t1 = new Transaction(rs.getString("description"), rs.getFloat("debit"), rs.getFloat("credit"), rs.getFloat("balance"), rs.getString("account_num"));
            t1.setTransId(rs.getInt("trans_id"));
            a.add(t1);
        }
        return a;
    }
}
