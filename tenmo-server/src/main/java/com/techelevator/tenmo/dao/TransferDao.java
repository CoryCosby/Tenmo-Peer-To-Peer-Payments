package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.List;

@Component
public interface TransferDao {
    void makeTransfer(Transfer transfer);

    List<String> getAllUsers();

    Transfer getTransferByTransferId(int userId);

    List<Transfer> getTransfersByUserId(int userId);

    void transferFunds(Transfer transfer);

    Transfer processTransfer(TransferDto transferDto);

}
