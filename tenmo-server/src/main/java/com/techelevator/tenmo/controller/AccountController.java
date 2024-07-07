package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
@PreAuthorize("isAuthenticated()")
@RestController
public class AccountController {
    private AccountDao accountDao;
    private UserDao userDao;
    public AccountController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }


    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public double getBalance(Principal principal) {
        String userName = principal.getName();
        User user = userDao.getUserByUsername(userName);
        double balance = accountDao.getBalanceByUserId(user.getId());
        return balance;
    }
    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> getUserList(){
        List<User> users = userDao.getUsers();
        return users;
    }

    @RequestMapping(path = "/account/{id}", method = RequestMethod.GET)
    public Account accountByUserId(@PathVariable ("id") int userId){
        Account account = accountDao.getAccountFromUserId(userId);
        return account;
    }

    @RequestMapping(path = "/account", method = RequestMethod.GET)
    public Account getAccount (Principal principal){
        Account account = accountDao.getAccountFromUsername(principal);

        return account;
    }

    @RequestMapping(path = "/account/get/{id}", method = RequestMethod.GET)
        public User userByAccountId(@PathVariable ("id") int accountId){
            User user = accountDao.getUserFromAccountId(accountId);
            return user;
        }



    @RequestMapping(path = "/checkuser", method = RequestMethod.GET)
    public String getCurrentAccount (Principal principal){

        return principal.getName();
    }
}
