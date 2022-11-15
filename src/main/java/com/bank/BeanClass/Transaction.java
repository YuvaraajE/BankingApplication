package com.bank.BeanClass;
import java.sql.Timestamp;

public class Transaction {
    private int transId;
    private Timestamp time;
    private String description;
    private float amtDebit;
    private float amtCredit;
    private float balance;
    private String accountNumber;
    private int toId;
    private int isRemoved;

    public Transaction(String description, float amtDebit, float amtCredit, float balance, String accountNumber, int toId) {
        this.time = new Timestamp(System.currentTimeMillis());
        this.description = description;
        this.amtDebit = amtDebit;
        this.amtCredit = amtCredit;
        this.balance = balance;
        this.accountNumber = accountNumber;
        this.isRemoved = 0;
        this.toId = toId;
    }
    public Transaction(String description, float amtDebit, float amtCredit, float balance, String accountNumber, int toId,int isRemoved) {
        this(description, amtDebit, amtCredit, balance, accountNumber, toId);
        this.isRemoved = isRemoved;
    }
    public int getTransId() {
        return transId;
    }
    public void setTransId(int transId) {
        this.transId = transId;
    }
    public Timestamp getDate() {
        return time;
    }
    public void setTime(Timestamp time) {
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
    public int getIsRemoved() {
        return this.isRemoved;
    }
    public void setIsRemoved() {
        this.isRemoved = 1;
    }
    public int getToId() {
        return this.toId;
    }
    public void setToId(int toId) {
        this.toId = toId;
    }

    public String toString() {
        return "Transaction [transId=" + transId + ", time=" + time + ", description=" + description
                + ", amtDebit=" + amtDebit + ", amtCredit=" + amtCredit + ", balance=" + balance + ", accountNumber=" + accountNumber + ", To ID:" + toId + ", isRemoved=" + isRemoved + "]";
    }
}
