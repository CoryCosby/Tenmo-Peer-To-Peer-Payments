package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Account {

    private int accountId;

    private int userId;

    private double balance;


    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Account(){

    }
    public Account (int accountId, int userId, double balance){

        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }
    public int getUserId() {
        return userId;
    }

    public void setId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }
    public int getAccountId() {
        return accountId;
    }





}
