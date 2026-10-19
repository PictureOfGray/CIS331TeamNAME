package CIS331TeamNAME;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
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
    public static int nextAuctionSlot = 0;
    
// Files are read from the programs working directory
// We use "|" instead of "," as the seperator so commas inside names/descriptions dont break a line apart
public static final String ITEMS_FILE = "items.txt";
public static final String AUCTIONS_FILE = "auctions.txt";
public static final String DELIMITER = "|";


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
            intent = getValidatedInt(0, 3, "Invalid input! Enter 1 to create an item, 2 to edit an existing item, 3 to create an auction, or 0 to exit: ");

            switch (intent) {
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

                        System.out.println("Invalid item ID!");
                    }
                    addItem(selectedIndex);
                    System.out.println("Item edited!");
                }
                case 3 -> { // create an auction
                    displayItems();

                    int selectedItem;
                    while (true) {
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

                        System.out.println("Opening bid must be a valid number!");
                    }

                    String openingDate;
                    while (true) { // validate user inputs date
                        System.out.println("Enter opening date (MM-DD-YYYY):");
                        openingDate = userInput.nextLine().trim();

                        if (!openingDate.equals("")) {
                            break;
                        }

                        System.out.println("Date cannot be blank!");
                    }

                    String closingDate;

                    while (true) { // validate user inputs date
                        System.out.println("Enter closing date (MM-DD-YYYY):");
                        closingDate = userInput.nextLine().trim();

                        if (!openingDate.isEmpty()) {
                            break;
                        }

                        System.out.println("Date cannot be blank!");
                    }

                    auctions[nextAuctionSlot] = new String[] {
                        String.valueOf(nextAuctionSlot), // auction id
                        items[selectedItem][0],          // item id
                        items[selectedItem][1],          // item name
                        items[selectedItem][2],          // item description
                        String.valueOf(openingBid),
                        openingDate,
                        closingDate,
                        "true"
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
    
    public static void buyerMenu() {
        /*
        * Browse items
        * Place bid on item
        */
        intent = -1;
        
        // this whole workflow is in a while loop so the buyer can keep doing buyer things
        while (intent > 0 || intent == -1) {
            System.out.println("Welcome, Buyer! What do you want to do? Input 1 to browse items, 2 to place a bid on an item. Enter 0 to exit: ");
            intent = getValidatedInt(0, 2, "Invalid input! Enter 1 to browse items, 2 to place a bid, or 0 to exit: ");

            switch (intent) {
                case 1 -> { // browse items
                    displayItems();
                }
                case 2 -> { // place bid on item
                    System.out.println("Here are all the open auctions:");
                    for (int i = 0; i < auctions.length && auctions[i] != null; i++) {
                        if (auctions[i][7].equals("true")) {
                            System.out.printf("AUCTION # %s:\n", auctions[i][0]);
                            System.out.printf("--ITEM: %s\n", auctions[i][2]);
                            System.out.printf("--DESCRIPTION: %s\n", auctions[i][3]);
                            System.out.printf("--CLOSING DATE: %s\n", auctions[i][6]);
                            System.out.printf("--CURRENT HIGHEST BID: %s\n", auctions[i][4]);
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
                                auctions[selectedIndex][7].equals("true")) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }

                        System.out.println("Invalid auction ID! Please input an auction id: ");
                    }
                    
                    System.out.println("What is your bid? ");
                    double userBid;
                    while (true) {
                        

                        if (userInput.hasNextDouble()) {
                            userBid = userInput.nextDouble();

                            if (userBid > 0) {
                                if (userBid > Double.parseDouble(auctions[selectedIndex][4])) {
                                    auctions[selectedIndex][4] = String.valueOf(userBid);
                                    break;
                                } else {
                                    System.out.println("Please place a higher bid: ");
                                }
                            }
                        } else {
                            userInput.next();
                        }

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
            intent = getValidatedInt(0, 3, "Invalid input! Enter 1 to close a sale early, 2 to edit existing items, 3 to run reports, or 0 to exit: ");

            switch (intent) {
                case 1 -> { // close a sale early
                    System.out.println("Open Auctions: ");
                    for (int i = 0; i < nextAuctionSlot; i++) {
                        if (auctions[i] != null &&
                            auctions[i][7].equals("true")) {

                            System.out.printf(
                                "Auction #%s: %s (Current Bid: %s)\n", auctions[i][0], auctions[i][2], auctions[i][4]
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
                                auctions[selectedAuction][7].equals("true")) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }

                        System.out.println("Invalid auction ID!");
                    }

                    auctions[selectedAuction][7] = "false";

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
    
    public static int login() {
        System.out.println("What type of user are you? 1 = Seller, 2 = Buyer, 3 = Admin. Enter 0 to exit: ");
        return getValidatedInt(0, 3, "Invalid input! Input 0 to exit, 1 for Seller, 2 for Buyer, or 3 for Admin: ");
    }
    
    // AI Assisted (Claude): loadData() and saveData() written with AI help.
    // Team: 
    //
    // File format: one record per line, fields separated by "|", in the same order as the arrays.
    //   items.txt:    id|name|description
    //   auctions.txt: auctionId|itemId|itemName|itemDescription|currentBid|openDate|closeDate|open
    //
    // ID scheme: the rest of the program assumes a record's ID equals its array index, so each loaded
    // record is stored at the index matching its ID. The next free slot (and next new ID) is then
    // the highest loaded ID + 1, so new records never reuse an existing ID.
    public static void loadData() {
        // ---------- Load items ----------
        try {
            Scanner fileIn = new Scanner(new File(ITEMS_FILE));
            int lineNumber = 0;

            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine();
                lineNumber++;

                if (line.trim().isEmpty()) { // skip blank lines
                    continue;
                }

                // split on "|" (it has to be escaped as "\\|" because split() takes a regex)
                // the -1 keeps empty fields, e.g. an item with a blank description
                String[] fields = line.split("\\|", -1);

                if (fields.length != 3) {
                    System.out.printf("Skipping bad line %d in %s (expected 3 fields)\n", lineNumber, ITEMS_FILE);
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());

                    if (id < 0 || id >= items.length) {
                        System.out.printf("Skipping line %d in %s (ID out of range)\n", lineNumber, ITEMS_FILE);
                        continue;
                    }

                    items[id] = new String[] {String.valueOf(id), fields[1], fields[2]};

                    if (id >= nextItemSlot) { // next ID = highest ID so far + 1
                        nextItemSlot = id + 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.printf("Skipping line %d in %s (ID is not a number)\n", lineNumber, ITEMS_FILE);
                }
            }

            fileIn.close();
            System.out.printf("Loaded items from %s.\n", ITEMS_FILE);
        } catch (FileNotFoundException e) {
            // first run, or the file was deleted: just start with no items
            System.out.printf("%s not found. Starting with no items.\n", ITEMS_FILE);
        }

        // ---------- Load auctions ----------
        try {
            Scanner fileIn = new Scanner(new File(AUCTIONS_FILE));
            int lineNumber = 0;

            while (fileIn.hasNextLine()) {
                String line = fileIn.nextLine();
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] fields = line.split("\\|", -1);

                if (fields.length != 8) {
                    System.out.printf("Skipping bad line %d in %s (expected 8 fields)\n", lineNumber, AUCTIONS_FILE);
                    continue;
                }

                try {
                    int id = Integer.parseInt(fields[0].trim());
                    Double.parseDouble(fields[4].trim()); // make sure the bid is a number now, so reports don't crash later

                    if (id < 0 || id >= auctions.length) {
                        System.out.printf("Skipping line %d in %s (ID out of range)\n", lineNumber, AUCTIONS_FILE);
                        continue;
                    }

                    // anything other than "true" counts as closed
                    String open = fields[7].trim().equalsIgnoreCase("true") ? "true" : "false";

                    auctions[id] = new String[] {
                        String.valueOf(id), fields[1].trim(), fields[2], fields[3],
                        fields[4].trim(), fields[5].trim(), fields[6].trim(), open
                    };

                    if (id >= nextAuctionSlot) {
                        nextAuctionSlot = id + 1;
                    }
                } catch (NumberFormatException e) {
                    System.out.printf("Skipping line %d in %s (ID or bid is not a number)\n", lineNumber, AUCTIONS_FILE);
                }
            }

            fileIn.close();
            System.out.printf("Loaded auctions from %s.\n", AUCTIONS_FILE);
        } catch (FileNotFoundException e) {
            System.out.printf("%s not found. Starting with no auctions.\n", AUCTIONS_FILE);
        }

        System.out.printf("%d item slot(s) and %d auction slot(s) in use.\n\n", nextItemSlot, nextAuctionSlot);
    }
    
    // Overwrites both data files with everything currently in memory
    public static void saveData() {
        // ---------- Save items ----------
        try {
            PrintWriter fileOut = new PrintWriter(ITEMS_FILE);

            for (int i = 0; i < nextItemSlot; i++) {
                if (items[i][0] != null) { // skip any empty slots
                    fileOut.println(toLine(items[i]));
                }
            }

            fileOut.close(); // close() is what actually writes everything to disk
        } catch (FileNotFoundException e) {
            System.out.printf("Could not save %s: %s\n", ITEMS_FILE, e.getMessage());
        }

        // ---------- Save auctions ----------
        try {
            PrintWriter fileOut = new PrintWriter(AUCTIONS_FILE);

            for (int i = 0; i < nextAuctionSlot; i++) {
                if (auctions[i][0] != null) {
                    fileOut.println(toLine(auctions[i]));
                }
            }

            fileOut.close();
        } catch (FileNotFoundException e) {
            System.out.printf("Could not save %s: %s\n", AUCTIONS_FILE, e.getMessage());
        }

        System.out.println("Data saved. Goodbye!");
    }

    // Joins one record's fields into a single "|"-separated line.
    // If a user typed "|" into a name or description, it's swapped for "/" so the line still splits correctly when loaded.
    public static String toLine(String[] record) {
        String line = "";

        for (int i = 0; i < record.length; i++) {
            String field = (record[i] == null) ? "" : record[i].replace(DELIMITER, "/");
            line += field;

            if (i < record.length - 1) {
                line += DELIMITER;
            }
        }

        return line;
    }
    
    public static void generateReport() {
        System.out.println("Which report would you like to run?");
        System.out.println("1: Live Auctions Report");
        System.out.println("2: Items For Sale Report");
        System.out.println("3: Price Histogram");
        System.out.println("0: Return");
        System.out.println("Please input a number: ");

        int reportType = getValidatedInt(0, 3, "Invalid input! Please select option 1, 2, 3, or 0 to return:");

        switch (reportType) {

            case 1 -> { // Live auctions grouped by auction
                System.out.println("\nLIVE AUCTIONS REPORT");
                for (int i = 0; i < nextAuctionSlot; i++) {

                    if (auctions[i] != null && auctions[i][7].equals("true")) {
                        System.out.printf("\nAUCTION #%s\n", auctions[i][0]);
                        System.out.printf("Item: %s\n", auctions[i][2]);
                        System.out.printf("Description: %s\n", auctions[i][3]);
                        System.out.printf("Current Bid: $%s\n", auctions[i][4]);
                        System.out.printf("Closing Date: %s\n", auctions[i][6]);
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

                    System.out.println("Please enter a valid non-negative price:");
                }

                System.out.printf("\nITEMS FOR SALE ($%.2f AND UP)\n", minimumPrice);

                for (int i = 0; i < nextAuctionSlot; i++) {
                    if (auctions[i] != null && auctions[i][7].equals("true")) {

                        double price = Double.parseDouble(auctions[i][4]);

                        if (price >= minimumPrice) {
                            System.out.printf( "Auction #%s | %s | Current Bid: $%.2f\n",
                                auctions[i][0],
                                auctions[i][2],
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
                    if (auctions[i] != null && auctions[i][7].equals("true")) {

                        double price = Double.parseDouble(auctions[i][4]);

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
    
    public static void displayItems() {
        System.out.println("Here are all the items:");
        int i = 0;
        while (i < items.length && items[i][0] != null) {
            System.out.printf("ITEM ID %s, %s: %s\n", items[i][0], items[i][1], items[i][2]);
            System.out.println();
            i++;
        }
    }

    // User inputs something, check if it's an int within the range
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

            System.out.println(errorMessage);
        }
    }

}