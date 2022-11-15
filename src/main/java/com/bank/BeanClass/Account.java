package com.bank.BeanClass;

import java.text.DecimalFormat;

public class Account {
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private String accountNumber;
    private float balance;

    public Account() {
        this.accountNumber = null;
        this.balance = 0.0f;
    }
    public Account(String accountNumber) {
        this.accountNumber = accountNumber;
        this.balance = 0.0f;
    }

    public Account(String accountNumber, Float balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public float getBalance() {
        return balance;
    }
    public void setBalance(float balance) {
        this.balance = Float.parseFloat(df.format(balance));
    }

    public String toString() {
        return accountNumber + " " + df.format(balance);
    }
}
