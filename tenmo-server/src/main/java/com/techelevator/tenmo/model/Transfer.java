package com.techelevator.tenmo.model;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;

public class Transfer {

    private int transferId;
    @Min(value = 1, message = "transfer type id is required")
    private int transferTypeId;
    @Min(value = 1, message = "transfer status id is required")
    private int transferStatusId;
    @Min(value = 2000, message = "account from is required")
    private int accountFrom;
    @Min(value = 2000, message = "account to is required")
    private int accountTo;
    @DecimalMin(value = ".01", message = "you must send a minimum of $0.01")
    private double amount;



    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
