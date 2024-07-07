package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

public class AccountService {

    private final String baseurl = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();


    public Account getAccount(AuthenticatedUser currentUser){
        Account account = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Account> response = restTemplate.exchange(baseurl + "/account", HttpMethod.GET, entity, Account.class);
            account = response.getBody();

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    public Account accountByUserId (AuthenticatedUser currentUser, int userId){

        Account account = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Account> response = restTemplate.exchange(baseurl + "/account/" + userId, HttpMethod.GET, entity, Account.class);
            account = response.getBody();

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;

    }

    public Account accountByAccountId (AuthenticatedUser currentUser, int accountId){

        Account account = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Account> response = restTemplate.exchange(baseurl + "/account/get/" + accountId, HttpMethod.GET, entity, Account.class);
            account = response.getBody();

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return account;

    }

    public User userFromAccountId(AuthenticatedUser currentUser, int accountId){

        User user = null;
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<User> response = restTemplate.exchange(baseurl + "/account/get/" + accountId, HttpMethod.GET, entity, User.class);
            user = response.getBody();

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        return user;

    }

    public User[] getUsers (AuthenticatedUser currentUser){
        User[] users = null;

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<User[]> response = restTemplate.exchange(baseurl + "/users",
                    HttpMethod.GET, entity, User[].class);

            users = response.getBody();
        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }

        return users;
    }


}
