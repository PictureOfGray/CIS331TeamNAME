package CIS331TeamNAME;

import java.util.Scanner;

/**
 * * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 10/16/2026
 * @purpose This is a console-based prototype of an auctioneering system
 * @disclaimer
 */
public class Prototype {
    public static Scanner userInput = new Scanner(System.in);

    public static void main(String[] args) {
        /*
        * General Flow of the application:
        * Data is automatically loads
        * User sees the main menu, selects a user type, and does user things
        * Data is automatically saved and program ends.
        */
        loadData();
        mainMenu();
        saveData();
    }
    
    public static void mainMenu() {
        int userType = login();
        switch (userType) {
            case 1 -> sellerMenu();
            case 2 -> buyerMenu();
            case 3 -> adminMenu();
            case 0 -> {
                return;
            }
        }
    }
    
    public static void sellerMenu() {
        /*
        * Actions available to the Seller:
        * Create an item
        * Edit existing item
        * Create auction sale with 1+ items
        */
        int intent = -1;
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Seller! What do you want to do? Input 1 to create an item, 2 to edit an existing item, 3 to create an auction. Enter 0 to exit: ");
            while (!userInput.hasNextInt() && (intent < 0 || intent > 3)) { // ensures only ints are entered
                userInput.next();
                System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
            }
            intent = userInput.nextInt();

            switch (intent) {
                case 1 -> { // create an item
                }
                case 2 -> { // editing existing item
                }
                case 3 -> { // create an auction
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
        
    }
    
    public static void buyerMenu() {
        /*
        * Browse items
        * Place bid on item
        */
        int intent = -1;
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Buyer! What do you want to do? Input 1 to browse items, 2 to place a bid on an item. Enter 0 to exit: ");
            while (!userInput.hasNextInt() && (intent < 0 || intent > 2)) { // ensures only ints are entered
                userInput.next();
                System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
            }
            intent = userInput.nextInt();

            switch (intent) {
                case 1 -> { // browse items
                }
                case 2 -> { // place bid on item
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
    }
    
    public static void adminMenu() {
        /*
        * Close/end sale before its date, ending sale for all auction items
        * Edit existing items
        * Run reports
        */
        int intent = -1;
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Admin! What do you want to do? Input 1 to close a sale early, 2 to edit an existing item, 3 to run reports. Enter 0 to exit: ");
            while (!userInput.hasNextInt() && (intent < 0 || intent > 3)) { // ensures only ints are entered
                userInput.next();
                System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
            }
            intent = userInput.nextInt();

            switch (intent) {
                case 1 -> { // close a sale early
                }
                case 2 -> { // edit an existing item
                }
                case 3 -> { // run reports
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
    }
    
    public static int login() {
        int userType = -1;
        
        System.out.println("What type of user are you? 1 = Seller, 2 = Buyer, 3 = Admin. Enter 0 to exit: ");
        while (!userInput.hasNextInt() && (userType < 0 || userType > 3)) { // ensures only ints are entered
            userInput.next();
            System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
        }
        return userInput.nextInt();
    }
    
    //TODO
    public static void loadData() {
        return;
    }
    
    //TODO
    public static void saveData() {
        return;
    }
    
    public static void generateReport() {
        /*
        * Report on live auctions and their items for sale, grouped by auction
        * Report showing all items for sale with a price filter
        * Charting report that produces a histogram of price ranges. Length of bars is proportional to overall total of items
        */
    }

}
