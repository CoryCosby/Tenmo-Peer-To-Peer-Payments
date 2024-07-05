package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;


public interface AccountDao {

   double getBalanceByUserId(int userId);

   Account mapRowToAccount(SqlRowSet results);

   double addBalance(double amountToAdd, int id);

   double subtractBalance(double amountToSubtract, int id);

   Account getAccountFromUserId(int userId);
}
