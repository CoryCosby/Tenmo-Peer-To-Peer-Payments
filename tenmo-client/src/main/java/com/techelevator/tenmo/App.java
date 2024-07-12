package com.techelevator.tenmo;

import com.techelevator.tenmo.model.*;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;
import com.techelevator.tenmo.services.TransferService;

import java.util.Scanner;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);

    private AuthenticatedUser currentUser;
    private final TransferService transferService = new TransferService();
    private final AccountService accountService = new AccountService();

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        transferService.setAuthToken(currentUser.getToken());
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
		// TODO Auto-generated method stub
        AccountService accountService = new AccountService();
        Account account = accountService.getAccount(currentUser);
        System.out.println("Your Balance is: $" + account.getBalance());
		
	}

	private void viewTransferHistory() {
		// TODO Auto-generated method stub
        System.out.println("Please choose an option:");
        System.out.println("1: view all transfers");
        System.out.println("2: view a specific transfer");
        Scanner scanner2 = new Scanner(System.in);
        int choice = scanner2.nextInt();
        if( choice == 1) {
            transferService.getTransferHistory(currentUser);
        }
         if (choice == 2){
            System.out.println("Enter your transfer number,ex 3001");
            int transferId = scanner2.nextInt();
            transferService.getTransferById(currentUser, transferId);
        }
	}

	private void viewPendingRequests() {
		// TODO Auto-generated method stub
        int choice=0;
        Transfer[] transfers = transferService.getPendingRequests(currentUser);
        while (choice !=1 || choice != 2 || choice !=3 || choice !=4) {
            System.out.println("Please choose an option:");
            System.out.println("1: view all pending transfers");
            System.out.println("2: approve a request");
            System.out.println("3: reject a request");
            System.out.println("4: return to main menu");
            Scanner scanner2 = new Scanner(System.in);
            choice = scanner2.nextInt();
            if (choice == 1) {
                transferService.getPendingRequests(currentUser);
            }
            if (choice == 2) {
                System.out.println("Enter the transfer id you wish to approve: Ex 3001");
                int id = scanner2.nextInt();
                Transfer transfer = transferService.getTransferById(currentUser, id);
                transferService.sendMoney(transfer, currentUser);
            }
            if (choice == 3) {
                System.out.println("Enter the transfer id you wish to reject: Ex 3001");
                int id = scanner2.nextInt();
                Transfer transfer = transferService.getTransferById(currentUser, id);
                transferService.rejectTransfer(currentUser, transfer);
            }
            if (choice == 4){
                break;
            }
        }
    }

	private void sendBucks() {
		// TODO Auto-generated method stub
       Account account = accountService.getAccount(currentUser);
       User[] users =  accountService.getUsers(currentUser);
        System.out.println("User ID     Name");
       for(User user: users){
           System.out.println(user.getId() +"        "+user.getUsername());
       }
       Transfer transfer =  transferService.promptForTransferData(account, currentUser);
       Transfer createdTransfer = transferService.createTransfer(transfer, currentUser);
        transferService.sendMoney( createdTransfer, currentUser);

    }

	private void requestBucks() {
		// TODO Auto-generated method stub
        Account account = accountService.getAccount(currentUser);
        User[] users =  accountService.getUsers(currentUser);
        System.out.println("User ID     Name");
        for(User user: users){
            System.out.println(user.getId() +"     "+user.getUsername());
        }
        Transfer transfer =  transferService.promptForRequestData(account, currentUser);
        Transfer createdTransfer = transferService.createTransfer(transfer, currentUser);
	}
}
