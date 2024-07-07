package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.sql.SQLOutput;
import java.util.Scanner;

public class TransferService {


    private final String baseurl = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;
    private final AccountService accountService = new AccountService();

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public Transfer getTransferById(AuthenticatedUser currentUser, int transferId){
        Transfer transfer = null;
        User[] users = null;
        String sender = "";
        String receiver = "";
        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Transfer> response = restTemplate.exchange(baseurl + "/my_transfers/" + transferId, HttpMethod.GET, entity, Transfer.class);
            transfer = response.getBody();

            users = accountService.getUsers(currentUser);

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        for (int i = 0; i < users.length; i++){
            Account account = accountService.accountByUserId(currentUser, users[i].getId());

            if (account.getAccountId() == transfer.getAccountTo()){
                receiver = users[i].getUsername();
            }if (account.getAccountId() == transfer.getAccountFrom()){
                sender = users[i].getUsername();
            }
        }
        System.out.println("Transfer ID     From     To     Amount");
        System.out.println(transfer.getTransferId() + "            " + sender +"            "+receiver+"          $" + transfer.getAmount());

        return transfer;
    }

    public Transfer[] getTransferHistory(AuthenticatedUser currentUser) {
        Transfer[] transfers = null;
        User[] users = null;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseurl + "/my_transfers",
                    HttpMethod.GET, entity, Transfer[].class);

            transfers = response.getBody();

            users = accountService.getUsers(currentUser);

        } catch (ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (transfers.length == 0) {
            System.out.println("You have no account activity");
        } else {
            System.out.println("Transfer ID     Name     Amount");
            for (int i = 0; i < transfers.length; i++) {
                String sender = null;
                String receiver = null;
                for (int j = 0; j< users.length; j++){
                    Account account = accountService.accountByUserId(currentUser, users[j].getId());
                    Account myaccount = accountService.accountByUserId(currentUser, currentUser.getUser().getId());
                    if(transfers[i].getAccountTo() == account.getAccountId() && transfers[i].getAccountFrom() == myaccount.getAccountId()){
                       receiver = users[j].getUsername();
                        System.out.println(transfers[i].getTransferId() + "      from " + receiver +"     $"+ transfers[i].getAmount());

                    }
                    if(transfers[i].getAccountFrom() == account.getAccountId() && transfers[i].getAccountTo() == myaccount.getAccountId()){
                        sender = users[j].getUsername();
                        System.out.println(transfers[i].getTransferId() + "       to" + sender +"     $" + transfers[i].getAmount());

                    }
                }
            }
        }
        return transfers;
    }

    public Transfer createTransfer(Transfer newTransfer, AuthenticatedUser currentUser) {
        Transfer returnedTransfer = null;

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(newTransfer, headers);

            returnedTransfer = restTemplate.postForObject(baseurl + "/transfer", entity,Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log("Error creating transfer"+ e.getMessage());
        }

        return returnedTransfer;
    }
    public boolean sendMoney(Transfer returnedTransfer, AuthenticatedUser currentUser) {
        boolean success = false;
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Transfer> entity = new HttpEntity<>(returnedTransfer, headers);

            restTemplate.put(baseurl + "/transfer/complete",
                    entity);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (success){
            User user = accountService.userFromAccountId(currentUser, returnedTransfer.getAccountTo());
            System.out.println("Your transfer of $"+ returnedTransfer.getAmount() + " to " +
                   user.getUsername()  + " was successful." );
        }else {
            System.out.println("Failed to process your transfer.");
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }

    public Transfer promptForTransferData(Account account, AuthenticatedUser currentUser) {
        Transfer newTransfer = null;
        while (newTransfer == null) {
            System.out.println("--------------------------------------------");
            System.out.println("Enter Transfer data as a comma separated list containing:");
            System.out.println("User To, Amount");
            System.out.println("Example 1002, 50.00");
            System.out.println("--------------------------------------------");
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            newTransfer = makeTransfer(scanner.nextLine(), account, currentUser);
            if (newTransfer == null) {
                System.out.println("Invalid entry. Please try again.");
            }
        }

        return newTransfer;
    }

    private Transfer makeTransfer(String csv, Account account,AuthenticatedUser currentUser) {
        Transfer transfer = null;
        String[] parsed = csv.split(",");
        if (parsed.length == 2) {
            try {
                Account accountTo = accountService.accountByUserId(currentUser, Integer.parseInt(parsed[0].trim()));
                transfer = new Transfer();
                transfer.setAccountFrom(account.getAccountId());
                transfer.setAccountTo(accountTo.getAccountId());
                transfer.setAmount(Double.parseDouble(parsed[1].trim()));

            } catch (NumberFormatException e) {
                transfer = null;
            }
        }
        return transfer;
    }

}
