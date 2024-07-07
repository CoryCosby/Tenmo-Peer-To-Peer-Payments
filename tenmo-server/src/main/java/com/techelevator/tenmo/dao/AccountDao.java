package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.security.Principal;


public interface AccountDao {

   double getBalanceByUserId(int userId);

   Account mapRowToAccount(SqlRowSet results);

   double addBalance(double amountToAdd, int id);

   double subtractBalance(double amountToSubtract, int id);

   Account getAccountFromAccountId(int accountId);
   Account getAccountFromUserId(int userId);
   User getUserFromAccountId(int accountId);
  Account getAccountFromUsername(Principal principal);
}
