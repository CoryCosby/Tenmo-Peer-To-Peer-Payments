package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDto;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{
    private final JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate, AccountDao accountDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = accountDao;
    }

    @Override
    public void makeTransfer(Transfer transfer) {
        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES(?,?,?,?,?);";
        jdbcTemplate.update(sql, transfer.getTransferTypeId(), transfer.getTransferStatusId(), transfer.getAccountFrom(),
                            transfer.getAccountTo(), transfer.getAmount());


    }

    @Override
    public void transferFunds(Transfer transfer) {
        Account accountFrom = accountDao.getAccountFromAccountId(transfer.getAccountFrom());
        Account accountTo = accountDao.getAccountFromAccountId(transfer.getAccountTo());
        double balanceFrom = accountDao.getBalanceByUserId(accountFrom.getUserId());
        double balanceTo = accountDao.getBalanceByUserId(accountFrom.getUserId());
        double transferAmount = transfer.getAmount();
        if (accountFrom.getAccountId() == accountTo.getAccountId()) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "YOUR TRANSFER CANNOT BE COMPLETED");
        }
        if (transferAmount > balanceFrom || transferAmount <= 0 ) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,"YOUR TRANSFER CANNOT BE COMPLETED") ;
        } else {
            accountDao.addBalance(transferAmount, accountTo.getAccountId());
            accountDao.subtractBalance(transferAmount, accountFrom.getAccountId());
        }
    }

    @Override
    public List<String> getAllUsers() {
        List<String> users = new ArrayList<>();
        String sql = "SELECT username, user_id FROM tenmo_user";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while (results.next()) {
            users.add(results.getString("username" + " user_id"));
        }
        return users;
    }

    @Override
    public Transfer getTransferByTransferId(int transferId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer " +
                "WHERE transfer_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
        if (results.next()) {
            return mapRowToTransfer(results);
        } else {
        return null;
        }
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        Transfer transfer = null;
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount " +
                "FROM transfer t " +
                "JOIN account  a ON a.account_id = t.account_from OR a.account_id = t.account_to " +
                "WHERE a.user_id = ?";

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        while (results.next()) {
            transfer = mapRowToTransfer(results);
            transfers.add(transfer);
        }
        return transfers;
    }
    @Override
    public Transfer processTransfer(TransferDto transferDto){

        String sql = "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) VALUES (?,?,?,?,?);";
        int transferTypeId = 2;
        Account accountFrom = accountDao.getAccountFromAccountId(transferDto.getAccountFrom());

        if (transferDto.getAccountFrom() == transferDto.getAccountTo()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED, "YOUR TRANSFER CANNOT BE COMPLETED");
        }
        if (accountFrom.getBalance() < transferDto.getAmount()){
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED,"YOUR TRANSFER CANNOT BE COMPLETED") ;
        }
        int transferStatusId = 2;

        jdbcTemplate.update(sql, transferTypeId, transferStatusId, transferDto.getAccountFrom(),
                            transferDto.getAccountTo(), transferDto.getAmount());

        String sql2 = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                        " FROM transfer ORDER BY transfer_id DESC LIMIT 1;";

        Transfer transfer = null;

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql2);
        if(results.next()){
            transfer = mapRowToTransfer(results);
        }


        return transfer;
    }
@Override
    public List<Transfer> listPendingTransfers(String username) {
    List<Transfer> transfers = new ArrayList<>();
    String sql = "SELECT  transfer_id ,transfer_type_id, transfer_status_id, account_from, account_to, amount  FROM transfer t " +
            "JOIN account a ON t.account_to = a.account_id OR t.account_from = a.account_id " +
            "JOIN tenmo_user tu on tu.user_id = a.user_id "+
            "WHERE username LIKE ? AND transfer_status_id = 1;";

    SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
    while(results.next()){
        transfers.add(mapRowToTransfer(results));
    }
    return transfers;
}

    @Override
    public void changeTransferStatus(Transfer transfer) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transfer.getTransferStatusId(), transfer.getTransferId());
    }



    private Transfer mapRowToTransfer(SqlRowSet rowSet) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(rowSet.getInt("transfer_id"));
        transfer.setTransferTypeId(rowSet.getInt("transfer_type_id"));
        transfer.setTransferStatusId(rowSet.getInt("transfer_status_id"));
        transfer.setAccountFrom(rowSet.getInt("account_from"));
        transfer.setAccountTo(rowSet.getInt("account_to"));
        transfer.setAmount(rowSet.getDouble("amount"));
        return transfer;
    }
}
