package CIS331TeamNAME;

import java.util.Scanner;

/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 10/16/2026
 * @purpose This is a console-based prototype of an auctioneering system
 * @disclaimer
 */
public class Prototype {
    public static Scanner userInput = new Scanner(System.in);
    public static int intent = -1;
    
    // We should really be using objects for items & auctions but we're restricted so we're doing arrays
    
    // Arrays are arbitrarily set to 256 items because we're not allowed to use ArrayLists
    // Also we could make Object[] arrays but that feels too advanced for the technical restriction we have so we're making it all String and typecasting
    
    // items structure: [item id, item name, item decr]
    public static String[][] items = new String[256][3];
    public static int nextItemSlot = 0;
    // auctions structure: [auction id, item id, item name, item description, current bid, date opnened, closing date, open (should be bool)]
    public static String[][] auctions = new String[256][8];
    

    public static void main(String[] args) {
        /*
        * General Flow of the application:
        * Data is automatically loads
        * User sees the main menu, selects a user type, and does user things
        * Data is automatically saved and program ends.
        */
        loadData();
        int userType = login();
        switch (userType) {
            case 1 -> sellerMenu();
            case 2 -> buyerMenu();
            case 3 -> adminMenu();
            case 0 -> {
                return;
            }
        }
        saveData();
    }

    public static void sellerMenu() {
        /*
        * Actions available to the Seller:
        * Create an item
        * Edit existing item
        * Create auction sale with 1+ items
        */
        intent = -1;
        
        // this whole workflow is in a while loop so the seller can keep doing seller things
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Seller! What do you want to do? Input 1 to create an item, 2 to edit an existing item, 3 to create an auction. Enter 0 to exit: ");
            while (!userInput.hasNextInt() && (intent < 0 || intent > 3)) { // ensures only ints are entered
                userInput.next();
                System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
            }
            intent = userInput.nextInt();

            switch (intent) {
                case 1 -> { // create an item
                    addItem(nextItemSlot);
                }
                case 2 -> { // editing existing item
                    // display existing items
                    displayItems();

                    System.out.println("What item do you want to edit? Input id: ");
                    while (!userInput.hasNextInt()) { // ensures proper ID is entered
                        userInput.next();
                        System.out.println("\nInvalid input! Input ID of the item you want to edit: ");
                    }
                    int selectedIndex = userInput.nextInt();
                    addItem(selectedIndex);
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
        intent = -1;
        
        // this whole workflow is in a while loop so the buyer can keep doing buyer things
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Buyer! What do you want to do? Input 1 to browse items, 2 to place a bid on an item. Enter 0 to exit: ");
            while (!userInput.hasNextInt() && (intent < 0 || intent > 2)) { // ensures only ints are entered
                userInput.next();
                System.out.println("\nInvalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
            }
            intent = userInput.nextInt();

            switch (intent) {
                case 1 -> { // browse items
                    displayItems();
                }
                case 2 -> { // place bid on item
                    System.out.println("Here are all the open items:");
                    int i = 0;
                    while (!items[i][0].equals("") && items[i][7].equals("true")) {
                        System.out.printf("AUCTION # %s:]n", auctions[i][0]);
                        System.out.printf("--ITEM: %s\n", auctions[i][2]);
                        System.out.printf("--DESCRIPTION: %s\n", auctions[i][3]);
                        System.out.printf("--CLOSING DATE: %s\n", auctions[i][6]);
                        System.out.printf("--CURRENT BID: %s\n", auctions[i][4]);
                        System.out.println();                        
                        i++;
                    }
                    
                    System.out.println("Which item do you want to bid on? Input id: ");
                    while (!userInput.hasNextInt()) { // ensures proper ID is entered
                        userInput.next();
                        System.out.println("\nInvalid input! Input ID of the item you want to bid on: ");
                    }
                    int selectedIndex = userInput.nextInt();
                    
                    System.out.println("What is your bid? ");
                    while (!userInput.hasNextDouble()) { // ensures proper ID is entered
                        userInput.next();
                        System.out.println("\nInvalid input! Input ID of the item you want to bid on: ");
                    }
                    
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
        intent = -1;
        
        // this whole workflow is in a while loop so the admin can keep doing admin things
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
                    // display existing items
                    displayItems();

                    System.out.println("What item do you want to edit? Input id: ");
                    while (!userInput.hasNextInt()) { // ensures proper ID is entered
                        userInput.next();
                        System.out.println("\nInvalid input! Input ID of the item you want to edit: ");
                    }
                    int selectedIndex = userInput.nextInt();
                    addItem(selectedIndex);
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
        items[0] = new String[] {"0", "title", "description"};
        items[1] = new String[] {"1", "lorem", "dolor sit amet"};
        items[2] = new String[] {"2", "Sisyphus's Boulder", "it imagines him happy"};
        items[3] = new String[] {"3", "Where The Sidewalk Ends", "by Shel Silverstein"};

        auctions[0] = new String[] {"0", items[1][0], items[1][1], items[1][2], "19.99", "12-12-2012", "05-02-2013", "false"};
        auctions[0] = new String[] {"1", items[2][0], items[2][1], items[2][2], "200.00", "05-27-2025", "05-28-2025", "true"};
        auctions[0] = new String[] {"2", items[3][0], items[3][0], items[3][2], "500", "11-12-1989", "09-09-2009", "true"};
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
    
    public static void addItem(int itemIndex) {
        // if we're using the next blank element in the items array, advance by one
        if (itemIndex == nextItemSlot) {
            nextItemSlot++;
        }
        
        System.out.println("What is the name of your item? ");
        String itemName = userInput.nextLine();
        
        System.out.println("What is the description of your item? ");
        String itemDescr = userInput.nextLine();
        
        items[itemIndex] = new String[] {String.valueOf(itemIndex), itemName, itemDescr};
    }
    
    public static void displayItems() {
        System.out.println("Here are all the items:");
        int i = 0;
        while (!items[i][0].equals("")) {
            System.out.printf("ITEM ID %s, %s: %s\n", items[i][0], items[i][1], items[i][2]);
            System.out.println();
            i++;
        }
    }

}
