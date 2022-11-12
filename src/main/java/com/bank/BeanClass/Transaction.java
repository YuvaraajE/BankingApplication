package com.bank.BeanClass;
import java.sql.Date;

public class Transaction {
    private int transId;
    private Date time;
    private String description;
    private float amtDebit;
    private float amtCredit;
    private float balance;
    private String accountNumber;

    public Transaction(String description, float amtDebit, float amtCredit, float balance, String accountNumber) {
        this.time = new Date(System.currentTimeMillis());
        this.description = description;
        this.amtDebit = amtDebit;
        this.amtCredit = amtCredit;
        this.balance = balance;
        this.accountNumber = accountNumber;
    }
    public int getTransId() {
        return transId;
    }
    public void setTransId(int transId) {
        this.transId = transId;
    }
    public Date getDate() {
        return time;
    }
    public void setTime(Date time) {
        this.time = time;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public float getAmtDebit() {
        return amtDebit;
    }
    public void setAmtDebit(float amtDebit) {
        this.amtDebit = amtDebit;
    }
    public float getAmtCredit() {
        return amtCredit;
    }
    public void setAmtCredit(float amtCredit) {
        this.amtCredit = amtCredit;
    }
    public float getBalance() {
        return balance;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }
}
