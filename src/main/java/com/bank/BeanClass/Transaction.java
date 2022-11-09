package com.bank.BeanClass;
import java.time.LocalDateTime;

public class Transaction {
    private int transId;
    private LocalDateTime time;
    private String description;
    private float amtDebit;
    private float amtCredit;
    private float balance;
    private String accountNumber;

    public Transaction(int transId, String description, float amtDebit, float amtCredit, float balance, String accountNumber) {
        this.transId = transId;
        this.description = description;
        this.amtDebit = amtDebit;
        this.amtCredit = amtCredit;
        this.balance = balance;
        this.accountNumber = accountNumber;
        if (this.amtCredit != 0) {
            this.balance += this.amtCredit;
        }
        else {
            this.balance -= this.amtDebit;
        }
    }
    public int getTransId() {
        return transId;
    }

    public LocalDateTime getTime() {
        return time;
    }
    public void setTime(LocalDateTime time) {
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
        return accountNumber;
    }
}
