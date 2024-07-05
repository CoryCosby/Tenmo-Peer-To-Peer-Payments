package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
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


}
