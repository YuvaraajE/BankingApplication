package com.bank.BeanClass;

public class Customer {
    private int cust_id;
    private String name;
    private String email;
    private String password;
    private String accountNumber;

    private String PANNumber;

    public Customer() {
        this.name = null;
        this.email = null;
        this.password = null;
        this.accountNumber = null;
        this.PANNumber = null;
    }
    public Customer(String name, String email, String password, String accountNumber, String pan) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.accountNumber = accountNumber;
        this.PANNumber = pan;
    }
    public Customer(int id, String name, String email, String password, String accountNumber, String pan) {
        this(name, email, password, accountNumber, pan);
        this.cust_id = id;
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

    public String getPANNumber() {
        return this.PANNumber;
    }
    public void setPANNumber(String panNumber) {
        this.PANNumber = panNumber;
    }

    @Override
    public int hashCode() {
        return 2;
    }
    @Override
    public boolean equals(Object o) {
        if (o instanceof Customer) {
            return this.getId() == ((Customer) o).getId();
        }
        return false;
    }
    @Override
    public String toString() {
        return "Customer [cust_id=" + cust_id + ", name=" + name + ", email=" + email + ", password=" + password
                + ", accountNumber=" + accountNumber + "]";
    }
}
