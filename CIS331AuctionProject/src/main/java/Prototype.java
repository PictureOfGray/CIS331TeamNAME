/* TODO
* Implement "logout" feature so that a seller can return to the role select menu, etc.
* Implement "go back" feature so that is a buyer selects Edit Item, they can input a key to cancel the action
* Export. Make it a CSV but instead of a comma it's a ` lol
* Refactor instructsions in-app so they appear on multiple lines when they get a bit too long. For example:
*   "Welcome, Buyer! What do you want to do?
*   Input 1 to browse items, 2 to place a bid on an item. Enter 0 to exit:"
* Add whitespace so it's *not* like: 
*    "What type of user are you? 1 = Seller, 2 = Buyer, 3 = Admin. Enter 0 to exit: 
*    2
*    Welcome"
*/

import java.util.*; // to use Scanner
import java.io.*;   // for Java File I/O

/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 10/16/2026
 * @purpose This is a console-based prototype of an auctioneering system
 * @disclaimer
 */
public class Prototype {
    public static Scanner userInput = new Scanner(System.in);
    public static int intent = -1;
    
    // Declare class variables for use throughout the program
    
    
    // items structure: [item id, item name, item decr]
    public static String[][] items = new String[256][3];
    public static int nextItemSlot = 0;
    // auctions structure: [auction id, item id, current bid, date opnened, closing date, open (should be bool)]
    public static String[][] auctions = new String[256][6];
    public static int nextAuctionSlot = 0;


    public static void main(String[] args) {
        /*
        * General Flow of the application:
        * Data automatically loads
        * User sees the main menu, and can select Seller, Buyer, or Admin
        * Then the user goes to respective sub-menu, and can select different tasks
        * Or log out and choose a different sub-menu
        * Once finished, data is automatically saved and program ends.
        */
        loadData();
        boolean loop = true;
        while (loop) { // infinite loop allows us to logout of a role and keep the program running
            int userType = login();
            switch (userType) {
                case 1 -> sellerMenu();
                case 2 -> buyerMenu();
                case 3 -> adminMenu();
                case 0 -> {
                    loop = false;
                    break;
                }
            }
        }

        saveData();
    }
    
    /*
    * Function to give the user the choice of which role they want to be (Seller, Buyer, or Admin)
    */
    public static int login() {
        System.out.println("What type of user are you? 1 = Seller, 2 = Buyer, 3 = Admin. Enter 0 to exit: ");
        return getValidatedInt(0, 3, "Invalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
    }

    
    /*
    * Sub-menu for Seller. Actions available to the Seller:
    * Create an item
    * Edit existing item
    * Create auction sale with 1+ items
    */
    public static void sellerMenu() {

        intent = -1;
        
        // this whole workflow is in a while loop so the seller can keep selection actions until done
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Seller! What do you want to do? Input 1 to create an item, 2 to edit an existing item, 3 to create an auction. Enter 0 to exit: ");
            intent = getValidatedInt(0, 3, "Invalid input! Enter 1 to create an item, 2 to edit an existing item, 3 to create an auction, or 0 to exit: ");

            switch (intent) { // process user choice
                case 1 -> { // create an item
                    addItem(nextItemSlot);
                    System.out.println("Item made!");
                }
                case 2 -> { // editing existing item
                    // display existing items
                    displayItems();

                    int selectedIndex;
                    while (true) { // validate user input to make sure it's an int in index range
                        System.out.println("What item do you want to edit? Input id: ");

                        if (userInput.hasNextInt()) {
                            selectedIndex = userInput.nextInt();

                            if (selectedIndex >= 0 &&
                                selectedIndex < nextItemSlot &&
                                items[selectedIndex] != null) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or if it is and isn't in the range of item indices
                        System.out.println("Invalid item ID!");
                    }
                    addItem(selectedIndex);
                    System.out.println("Item edited!");
                }
                case 3 -> { // create an auction
                    displayItems();

                    int selectedItem;
                    while (true) { // validate user input
                        System.out.println("What item do you want to auction? Input item ID:");

                        if (userInput.hasNextInt()) { // validate for int and valid ID
                            selectedItem = userInput.nextInt();

                            if (selectedItem >= 0 &&
                                selectedItem < nextItemSlot &&
                                items[selectedItem] != null) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or if it is and isn't in range of the items indices, print
                        System.out.println("Invalid item ID!");
                    }

                    userInput.nextLine(); // eat any newlines that may be present

                    double openingBid;
                    while (true) { // validate opening bid is a double
                        System.out.println("Enter opening bid:");

                        if (userInput.hasNextDouble()) {
                            openingBid = userInput.nextDouble();

                            if (openingBid >= 0) {
                                userInput.nextLine(); // consume newline
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input ins't a double, print this:
                        System.out.println("Opening bid must be a valid number!");
                    }

                    String openingDate;
                    while (true) { // validate user inputs date
                        System.out.println("Enter opening date (MM-DD-YYYY):");
                        openingDate = userInput.nextLine().trim();

                        if (!openingDate.equals("")) {
                            break;
                        }
                        // if input is blank, print this:
                        System.out.println("Date cannot be blank!");
                    }

                    String closingDate;

                    while (true) { // validate user inputs date
                        System.out.println("Enter closing date (MM-DD-YYYY):");
                        closingDate = userInput.nextLine().trim();

                        if (!openingDate.isEmpty()) {
                            break;
                        }
                        // if input is blank, print this:
                        System.out.println("Date cannot be blank!");
                    }

                    auctions[nextAuctionSlot] = new String[] {
                        String.valueOf(nextAuctionSlot), // auction id
                        items[selectedItem][0],          // item id
                        String.valueOf(openingBid),      // opening bid
                        openingDate,
                        closingDate,
                        "true"                           // open?
                    };

                    System.out.printf(
                        "Auction #%s created for item '%s'.\n", auctions[nextAuctionSlot][0], items[selectedItem][1]
                    );

                    nextAuctionSlot++;
                    System.out.println("Auction made!");
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
        
    }
    
    
    /*
    * Sub-menu for Buyer. User has the option to:
    * Browse items
    * Place bid on item
    */
    public static void buyerMenu() {

        intent = -1;
        
        // this whole workflow is in a while loop so the buyer can keep doing buyer things after selecting
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Buyer! What do you want to do? Input 1 to browse items, 2 to place a bid on an item. Enter 0 to exit: ");
            intent = getValidatedInt(0, 2, "Invalid input! Enter 1 to browse items, 2 to place a bid, or 0 to exit: ");

            switch (intent) { // handles which action the buyer inputs
                case 1 -> { // browse items
                    displayItems();
                }
                case 2 -> { // place bid on item
                    System.out.println("Here are all the open auctions:");
                    for (int i = 0; i < nextAuctionSlot; i++) {
                        if (auctions[i][5].equals("true")) {
                            System.out.printf("AUCTION # %s:\n", auctions[i][0]);
                            System.out.printf("--ITEM: %s\n", items[Integer.parseInt(auctions[i][1])][1]);
                            System.out.printf("--DESCRIPTION: %s\n", items[Integer.parseInt(auctions[i][1])][2]);
                            System.out.printf("--CLOSING DATE: %s\n", auctions[i][4]);
                            System.out.printf("--CURRENT HIGHEST BID: %s\n", auctions[i][2]);
                            System.out.println();
                        }   
                    }
                    
                    System.out.println("Which item do you want to bid on? Input id: ");
                    int selectedIndex;

                    while (true) { // validate int and proper index is inputted
                        System.out.println("Which auction do you want to bid on?");

                        if (userInput.hasNextInt()) {
                            selectedIndex = userInput.nextInt();

                            if (selectedIndex >= 0 &&
                                selectedIndex < nextAuctionSlot &&
                                auctions[selectedIndex] != null &&
                                auctions[selectedIndex][5].equals("true")) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or is and isn't in the index/auction's closed
                        System.out.println("Invalid auction ID! Please input an auction id: ");
                    }
                    
                    System.out.println("What is your bid? ");
                    double userBid;
                    while (true) { // validate user's bid
                        if (userInput.hasNextDouble()) {
                            userBid = userInput.nextDouble();

                            if (userBid > 0) {
                                if (userBid > Double.parseDouble(auctions[selectedIndex][5])) {
                                    auctions[selectedIndex][5] = String.valueOf(userBid);
                                    break;
                                } else { // bid is a double but less than current bid
                                    System.out.println("Please place a higher bid: ");
                                }
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't a double, print:
                        System.out.println("Bid must be greater than 0! Please input again: ");
                    }
                    System.out.println("Bid placed!");
                    
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
    }
    
    
    /*
    * Sub-menu for Admins. Has the following actions:
    * Close/end sale before its date, ending sale for all auction items
    * Edit existing items
    * Run reports
    */
    public static void adminMenu() {
        intent = -1;
        
        // this whole workflow is in a while loop so the admin can keep doing admin things after an action
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Admin! What do you want to do? Input 1 to close a sale early, 2 to edit an existing item, 3 to run reports. Enter 0 to exit: ");
            intent = getValidatedInt(0, 3, "Invalid input! Enter 1 to close a sale early, 2 to edit existing items, 3 to run reports, or 0 to exit: ");

            switch (intent) { // looks at input and runs respective action
                case 1 -> { // close a sale early
                    // display all open auctions
                    System.out.println("Open Auctions: ");
                    for (int i = 0; i < nextAuctionSlot; i++) {
                        if (auctions[i] != null &&
                            auctions[i][5].equals("true")) {

                            System.out.printf(
                                "Auction #%s: %s (Current Bid: %s)\n", auctions[i][0], items[Integer.parseInt(auctions[i][1])][1] , auctions[i][2]
                            );
                        }
                    }

                    int selectedAuction;
                    while (true) { // validate input for int and valid ID
                        System.out.println("Which auction do you want to close? Input ID: ");

                        if (userInput.hasNextInt()) {
                            selectedAuction = userInput.nextInt();

                            if (selectedAuction >= 0 &&
                                selectedAuction < nextAuctionSlot &&
                                auctions[selectedAuction] != null &&
                                auctions[selectedAuction][5].equals("true")) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, print this:
                        System.out.println("Invalid auction ID!");
                    }

                    auctions[selectedAuction][5] = "false"; // change open field to false, i.e., closed

                    System.out.printf("Auction #%d has been closed!\n", selectedAuction);
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
                    System.out.println("Item edited!");
                }
                case 3 -> { // run reports
                    generateReport();
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
    }

    /* Looks at auctions.csv and items.csv and loads it into memory (arrays)
    *  Files are like a CSV, except instead of a comma separating values it's a ` (backtick)
    */    
    public static void loadData() {
        try { // this is in a try statement in case there's an error
            Scanner itemsReader = new Scanner(new File("src/main/java/items.csv"));
            itemsReader.nextLine(); // eat the first line of the csv, since we don't need it
            
            // while there's still content, get a line, break it up into pieces of data, and save to array
            while(itemsReader.hasNextLine()) {
                String row = itemsReader.nextLine();
                String[] itemInfo = row.split("`"); // split the CSV line into its parts
                
                int itemIndex = Integer.parseInt(itemInfo[0]); // yeah the index is based off the id and that's bad but w/e
                if (itemIndex >= items.length) { // if we have 256 items already, don't load the next one
                    System.out.println("Max number of items loaded!");
                    break;
                }
                
                items[itemIndex][0] = itemInfo[0]; // item ID
                items[itemIndex][1] = itemInfo[1]; // item name
                items[itemIndex][2] = itemInfo[2]; // item description
                nextItemSlot++;
            }
        } catch (FileNotFoundException ex) {
            System.getLogger(Prototype.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        // Read the auctions.cs file and put it in a global array.
        try {
            Scanner auctionReader = new Scanner(new File("src/main/java/auctions.csv"));
            auctionReader.nextLine(); // eat the first line of the csv, since we don't need it

            while(auctionReader.hasNextLine()) {
                String row = auctionReader.nextLine();
                String[] auctionInfo = row.split("`"); // split the CSV line into its parts
                int auctionIndex = Integer.parseInt(auctionInfo[0]); // yeah the index is based off the id and that's bad but w/e

                if (auctionIndex >= items.length) {
                    System.out.println("Max number of auctions loaded!");
                    break;
                }

                auctions[auctionIndex][0] = auctionInfo[0]; // auction ID
                auctions[auctionIndex][1] = auctionInfo[1]; // item ID
                auctions[auctionIndex][2] = auctionInfo[2]; // current bid
                auctions[auctionIndex][3] = auctionInfo[3]; // date opened
                auctions[auctionIndex][4] = auctionInfo[4]; // closing date
                auctions[auctionIndex][5] = auctionInfo[5]; // open (true/false)
                nextAuctionSlot++;
            }
        } catch (FileNotFoundException ex) {
            System.getLogger(Prototype.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
        }
        
        
        System.out.println("All data loaded!");
    }
    
    /* Overwrites both auctions.csv and items.csv with everything currently in memory
    *  Saves like a CSV, except instead of a comma separating values it's a ` (backtick)
    */
    public static void saveData() {
        System.out.println("Data saved. Goodbye!");
    }

    // Helper function to generate reports for the ADmin
    public static void generateReport() {
        System.out.println("Which report would you like to run?");
        System.out.println("1: Live Auctions Report");
        System.out.println("2: Items For Sale Report");
        System.out.println("3: Price Histogram");
        System.out.println("0: Return");
        System.out.println("Please input a number: ");

        int reportType = getValidatedInt(0, 3, "Invalid input! Please select option 1, 2, 3, or 0 to return:");

        switch (reportType) { // generate a report based on which input was given

            case 1 -> { // Live auctions grouped by auction
                System.out.println("\nLIVE AUCTIONS REPORT");
                
                for (int i = 0; i < nextAuctionSlot; i++) {

                    if (auctions[i] != null && auctions[i][5].equals("true")) { // open auctions only
                        System.out.printf("AUCTION # %s:\n", auctions[i][0]);
                        System.out.printf("--Item: %s\n", items[Integer.parseInt(auctions[i][1])][1]);
                        System.out.printf("--Description: %s\n", items[Integer.parseInt(auctions[i][1])][2]);
                        System.out.printf("--Current Highest Bid: %s\n", auctions[i][2]);
                        System.out.printf("--Date Opened: %s\n", auctions[i][3]);
                        System.out.printf("--Closing Date: %s\n", auctions[i][4]);
                        System.out.println();
                    }
                }
                System.out.println();
            }

            case 2 -> { // Items currently for sale with price filter
                System.out.println("Enter a price to show items whose current bid is at least that: ");
                double minimumPrice;
                
                while (true) { // validate user is entering a double
                    if (userInput.hasNextDouble()) {
                        minimumPrice = userInput.nextDouble();
                        if (minimumPrice >= 0) {
                            break;
                        }
                    } else {
                        userInput.next();
                    }
                    // if input isn't a double or a negative minimum price, print this:
                    System.out.println("Please enter a valid non-negative price:");
                }

                System.out.printf("\nITEMS FOR SALE ($%.2f AND UP)\n", minimumPrice);

                for (int i = 0; i < nextAuctionSlot; i++) {
                    if (auctions[i] != null && auctions[i][5].equals("true")) {

                        double price = Double.parseDouble(auctions[i][2]);

                        if (price >= minimumPrice) {
                            System.out.printf( "Auction #%s | %s | Current Bid: $%.2f\n",
                                auctions[i][0],
                                items[Integer.parseInt(auctions[i][1])][1],
                                price
                            );
                        }
                    }
                }

                System.out.println();
            }

            case 3 -> { // Histogram report
                int[] bins = new int[5];

                // add auctions' prices to respective bins
                for (int i = 0; i < nextAuctionSlot; i++) {
                    if (auctions[i] != null && auctions[i][5].equals("true")) {

                        double price = Double.parseDouble(auctions[i][2]);

                        if (price <= 5) {
                            bins[0]++;
                        }
                        else if (price <= 10) {
                            bins[1]++;
                        }
                        else if (price <= 20) {
                            bins[2]++;
                        }
                        else if (price <= 50) {
                            bins[3]++;
                        }
                        else {
                            bins[4]++;
                        }
                    }
                }

                int maxCount = 0;

                // Finds the max value among the bins. Thanks GeeksForGeeks!
                for (int count : bins) {
                    if (count > maxCount) {
                        maxCount = count;
                    }
                }

                // Print out report
                String[] labels = {"$1 - $5", "$6 - $10", "$11 - $20", "$21 - $50", "$51+"};

                System.out.println("\nPRICE HISTOGRAM");

                for (int i = 0; i < bins.length; i++) {

                    int barLength = 0;

                    if (maxCount > 0) {
                        barLength = (bins[i] * 10) / maxCount;
                    }

                    System.out.printf( "%-12s |", labels[i]);

                    for (int j = 0; j < barLength; j++) {
                        System.out.print("=");
                    }

                    System.out.printf(" (%d)\n", bins[i]);
                }

                System.out.println();
            }

            case 0 -> {
                return;
            }
        }
        
    }
    
    
    // Helper function to add items for Seller
    public static void addItem(int itemIndex) {
        // if we're using the next blank element in the items array, advance by one
        if (itemIndex == nextItemSlot) {
            nextItemSlot++;
        }
        
        userInput.nextLine(); // eats any newlines before prompting
        
        System.out.println("What is the name of your item? ");
        String itemName = userInput.nextLine();
        
        System.out.println("What is the description of your item? ");
        String itemDescr = userInput.nextLine();
        
        items[itemIndex] = new String[] {String.valueOf(itemIndex), itemName, itemDescr};
    }
    
    // Helper function used to display all items
    public static void displayItems() {
        System.out.println("Here are all the items:");
        int i = 0;
 
        while (i < items.length && items[i][0] != null) {
            System.out.printf("ITEM ID %s, %s: %s\n", items[i][0], items[i][1], items[i][2]);
            System.out.println();
            i++;
        }
    }

    // Helper function. User inputs something, check if it's an int within the range
    public static int getValidatedInt(int min, int max, String errorMessage) {
        while (true) {
            if (userInput.hasNextInt()) {
                int value = userInput.nextInt();

                if (value >= min && value <= max) {
                    return value;
                }
            } else {
                userInput.next();
            }
            // if input isn't an int, or doesn't fall within specified range, print this:
            System.out.println(errorMessage);
        }
    }

}