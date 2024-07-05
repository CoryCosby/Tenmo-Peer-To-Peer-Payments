package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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
    public double addBalance(double amount, int userId) {
        Account account = getAccountFromUserId(userId);
        double newBalance = account.getBalance() + amount;
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, userId);
        } catch (DataAccessException e) {
            System.out.println("Cannot Access");
        }
        return account.getBalance();
    }

    @Override
    public double subtractBalance(double amount, int userId) {
        Account account = getAccountFromUserId(userId);
        double newBalance = account.getBalance() - amount;
        String sql = "UPDATE accounts SET balance = ? WHERE user_id = ?";
        try {
            jdbcTemplate.update(sql, newBalance, userId);
        } catch (DataAccessException e) {
            System.out.println("Cannot Access");
        }
        return account.getBalance();
    }
     public Account getAccountFromUserId(int userId) {
        Account account = null;
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
        if (results.next()) {
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
