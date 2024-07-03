package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.stereotype.Component;

@Component
public interface TransferDao {
    public void makeTransfer(Transfer transfer);
}
