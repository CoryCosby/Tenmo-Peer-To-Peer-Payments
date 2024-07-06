package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.Scanner;

public class TransferService {


    private final String baseurl = "http://localhost:8080";

    private final RestTemplate restTemplate = new RestTemplate();

    private String authToken = null;

    public void setAuthToken(String authToken){
        this.authToken = authToken;
    }

    public Transfer[] getTransferHistory(AuthenticatedUser currentUser){
        Transfer[] transfers = null;

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(currentUser.getToken());
            HttpEntity<Void> entity = new HttpEntity<>(headers);

            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseurl + "/my_transfers" ,
                    HttpMethod.GET, entity, Transfer[].class);

            transfers = response.getBody();

        }catch (ResourceAccessException e){
            BasicLogger.log(e.getMessage());
        }
        if (transfers.length == 0){
            System.out.println("You have no account activity");
        }else for (Transfer transfer: transfers){
            System.out.println("From Account "+ transfer.getAccountFrom() + " to account " + transfer.getAccountTo() +" $"+ transfer.getAmount());
        }

        return transfers;

    }

    public Transfer createTransfer(Transfer newTransfer) {
        Transfer returnedTransfer = null;

        try {
            returnedTransfer = restTemplate.postForObject(baseurl + "/transfer", makeTransferEntity(newTransfer),Transfer.class);
        }catch (RestClientResponseException | ResourceAccessException e){
            BasicLogger.log("TransferService.makeTransfer() has not been implemented");
        }

        return returnedTransfer;
    }
    public boolean sendMoney(Transfer returnedTransfer) {
        boolean success = false;
        try {
            restTemplate.put(baseurl + "/transfer/complete",
                    makeTransferEntity(returnedTransfer));
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        if (success){
            System.out.println("Your transfer of $"+ returnedTransfer.getAmount() + " to" +
                    returnedTransfer.getAccountTo() + " was successful." );
        }else {
            System.out.println("Failed to process your transfer.");
        }
        return success;
    }

    private HttpEntity<Transfer> makeTransferEntity(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(transfer, headers);
    }

    public Transfer promptForTransferData(Account account) {
        Transfer newTransfer = null;
        while (newTransfer == null) {
            System.out.println("--------------------------------------------");
            System.out.println("Enter Transfer data as a comma separated list containing:");
            System.out.println("  Account To, Amount");
            System.out.println("Example 2002, 50.00");
            System.out.println("--------------------------------------------");
            System.out.println();
            Scanner scanner = new Scanner(System.in);
            newTransfer = makeTransfer(scanner.nextLine(), account);
            if (newTransfer == null) {
                System.out.println("Invalid entry. Please try again.");
            }
        }

        return newTransfer;
    }

    private Transfer makeTransfer(String csv, Account account) {
        Transfer transfer = null;
        String[] parsed = csv.split(",");
        if (parsed.length == 2) {
            try {
                transfer = new Transfer();
                transfer.setAccountFrom(account.getAccountId());
                transfer.setAccountTo(Integer.parseInt(parsed[0].trim()));
                transfer.setAmount(Double.parseDouble(parsed[1].trim()));

            } catch (NumberFormatException e) {
                transfer = null;
            }
        }
        return transfer;
    }

}
