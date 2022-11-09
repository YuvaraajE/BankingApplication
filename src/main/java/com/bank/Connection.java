package com.bank;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {
        java.sql.Connection con = null;
        private static Connection db = null;

        private Connection() throws ClassNotFoundException, SQLException {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.con= DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/BankAppDB","root","sc=nv3vaESEVN");
            if (this.con.isValid(0)) {
                System.out.println("Connected to database!!");
            }
        }

        public static Connection getInstance() throws SQLException, ClassNotFoundException {
            if (db == null)
                db = new Connection();
            return db;
        }

        public java.sql.Connection getConnection() {
            return this.con;
        }
}