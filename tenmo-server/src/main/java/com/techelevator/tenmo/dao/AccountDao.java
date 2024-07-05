package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.security.Principal;


public interface AccountDao {

   double getBalanceByUserId(int userId);

   Account mapRowToAccount(SqlRowSet results);

   double addBalance(double amountToAdd, int id);

   double subtractBalance(double amountToSubtract, int id);

   Account getAccountFromUserId(int userId);

  Account getAccountFromUsername(Principal principal);
}
