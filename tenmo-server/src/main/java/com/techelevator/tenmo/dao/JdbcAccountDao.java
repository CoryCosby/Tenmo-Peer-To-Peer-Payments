package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.boot.autoconfigure.quartz.QuartzProperties;
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
    public double getBalance(String name) {
        double balance = 0;
        String sql = "SELECT balance FROM account a \n" +
                        "JOIN tenmo_user tu ON  a.user_id = tu.user_id WHERE username = ?";

        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, name);
        if(result.next()) {
            balance = result.getDouble("balance");
        }
    return balance;
    }


}
