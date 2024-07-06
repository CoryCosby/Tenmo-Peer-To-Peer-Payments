package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.security.Principal;

@Component
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public double getBalanceByUserId(int userId) {
        double balance = 0;
        String sql = "SELECT balance FROM account WHERE user_id = ?";

        try {
            SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
            if(result.next()) {
                balance = result.getDouble("balance");
            }
        } catch (DataAccessException e) {
            System.out.println("Cannot Access");
        }
        return balance;
    }
    @Override
    public double addBalance(double amount, int accountTo) {
        Account account = getAccountFromAccountId(accountTo);
        double newBalance = account.getBalance() + amount;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, accountTo);
        } catch (DataAccessException e) {
            System.out.println("Cannot Access");
        }
        return account.getBalance();
    }

    @Override
    public double subtractBalance(double amount, int accountId) {
        Account account = getAccountFromAccountId(accountId);
        double newBalance = account.getBalance() - amount;
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, accountId);
        } catch (DataAccessException e) {
            System.out.println("Cannot Access");
        }
        return account.getBalance();
    }
     public Account getAccountFromAccountId(int accountId) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
        if (results.next()) {
            account = mapRowToAccount(results);
        }
        return account;
    }

    @Override
    public Account getAccountFromUsername(Principal principal){
        Account account = null;
        String sql = "SELECT  account_id, a.user_id, balance FROM account a JOIN tenmo_user tu ON a.user_id = tu.user_id WHERE username = ?;";
        String username = principal.getName();;
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);
        if (results.next()){
            account = mapRowToAccount(results);
        }
        return account;
    }
    @Override
    public Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setBalance(result.getDouble("balance"));
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        return account;
    }

}
