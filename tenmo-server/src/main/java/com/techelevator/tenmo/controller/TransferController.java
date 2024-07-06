package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class TransferController {
    private TransferDao transferDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.userDao = userDao;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public void makeTransfer(@Valid @RequestBody Transfer transfer) {
        transferDao.makeTransfer(transfer);
    }

    @RequestMapping(path = "/my_transfers", method = RequestMethod.GET)
    public List<Transfer> getAllTransfers(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getUserByUsername(userName);
        List<Transfer> transfers = transferDao.getTransfersByUserId(user.getId());
        return transfers;
    }

    @RequestMapping(path = "my_transfers/{id}", method = RequestMethod.GET)
    public Transfer getTransfer(@PathVariable ("id") int transferId) {
        Transfer transfer = transferDao.getTransferByTransferId(transferId);
        return transfer;
    }

    @RequestMapping(path = "transfer/complete", method = RequestMethod.PUT)
    public String completeTransfer(@RequestBody Transfer transfer) {
        transferDao.transferFunds(transfer);
        return "Completed";
    }
}
