package com.bank.BeanClass;

public class Customer {
    private int cust_id;
    private String name;
    private String email;
    private String password;
    private String accountNumber;

    public Customer() {
        this.name = null;
        this.email = null;
        this.password = null;
        this.accountNumber = null;
    }
    public Customer(String name, String email, String password, String accountNumber) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountNumber = accountNumber;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public int getId() {
        return this.cust_id;
    }
    public void setId(int id) {
        this.cust_id = id;
    }

    public String toString() {
        return "Customer [cust_id=" + cust_id + ", name=" + name + ", email=" + email + ", password=" + password
                + ", accountNumber=" + accountNumber + "]";
    }
}
