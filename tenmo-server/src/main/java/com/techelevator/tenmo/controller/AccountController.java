package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

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
}
