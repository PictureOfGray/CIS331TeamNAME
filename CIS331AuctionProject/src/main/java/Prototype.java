
import java.util.*; // to use Scanner & ArrayLists
import java.io.*;   // for Java File I/O

/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler
 * Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is a console-based prototype of an auctioneering system
 * @disclaimer
 */
public class Prototype {

    public static Scanner userInput = new Scanner(System.in);
    public static int intent = -1;

    // Declare class variables for use throughout the program
    public static ArrayList<Item> items = new ArrayList<>();
    public static int nextItemSlot = 0;

    public static ArrayList<Auction> auctions = new ArrayList<>();
    public static int nextAuctionSlot = 0;

    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<PotentialSeller> potentialSellers = new ArrayList<>();

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
                case 1 ->
                    sellerMenu();
                case 2 ->
                    buyerMenu();
                case 3 ->
                    adminMenu();
                case 4 ->
                    potentialSellerMenu();
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
        System.out.println("What type of user are you?\n 1 = Seller, 2 = Buyer, 3 = Admin, 4 = Refer Potential Seller. Enter 0 to exit: ");
        return getValidatedInt(0, 4, "Invalid input!\n Input 0 to exit, 1 for Seller, 2 for Buyer, 3 for Admin, or 4 to Refer Potential Seller: ");
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
            System.out.println("Welcome, Seller! What do you want to do?\n Input 1 to create an item, 2 to edit an existing item, 3 to create an auction. Enter 0 to logout: ");
            intent = getValidatedInt(0, 3, "Invalid input!\n Enter 1 to create an item, 2 to edit an existing item, 3 to create an auction, or 0 to logout: ");

            switch (intent) { // process user choice
                case 1 -> { // create an item
                    addItem(nextItemSlot);
                    System.out.println("Done!");
                }
                case 2 -> { // editing existing item
                    // display existing items
                    displayItems();

                    int selectedIndex = -1;
                    while (true) { // validate user input to make sure it's an int in index range
                        System.out.println("What item do you want to edit?\n Input id, or -1 to cancel: ");

                        if (userInput.hasNextInt()) {
                            selectedIndex = userInput.nextInt();

                            if (selectedIndex == -1) {
                                System.out.println("Item editing cancelled.");
                                break;
                            }

                            if (selectedIndex >= 0
                                    && selectedIndex < nextItemSlot
                                    && items.get(selectedIndex) != null) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or if it is and isn't in the range of item indices
                        System.out.println("Invalid item ID!");
                    }
                    if (selectedIndex == -1) {
                        break;
                    }

                    addItem(selectedIndex);
                    System.out.println("Item edited!");
                }
                case 3 -> { // create an auction
                    displayItems();

                    int selectedItem = -1;
                    while (true) { // validate user input
                        System.out.println("What item do you want to auction?\n Input item ID, or -1 to cancel:");

                        if (userInput.hasNextInt()) { // validate for int and valid ID
                            selectedItem = userInput.nextInt();

                            if (selectedItem == -1) {
                                System.out.println("Bid cancelled.");
                                break;
                            }

                            if (selectedItem >= 0
                                    && selectedItem < nextItemSlot
                                    && items.get(selectedItem) != null) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }

                        // if input isn't an int, or if it is and isn't in range of the items indices, print
                        System.out.println("Invalid item ID!");
                    }
                    if (selectedItem == -1) {
                        break;
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

                    auctions.add(new Auction(
                            nextAuctionSlot, // auction id
                            items.get(selectedItem).getItemID(), // item id
                            openingBid, // opening bid
                            -1, // winningBuyerID
                            openingDate,
                            closingDate,
                            true // open?
                    ));

                    System.out.printf(
                            "Auction #%s created for item '%s'.\n", auctions.get(nextAuctionSlot).getAuctionID(), items.get(selectedItem).getItemName()
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
            System.out.println("Welcome, Buyer! What do you want to do?\n Input 1 to browse items, 2 to place a bid on an item, 3 for payment plans & amortization tables. Enter 0 to logout: ");
            intent = getValidatedInt(0, 3, "Invalid input!\n Enter 1 to browse items, 2 to place a bid, 3 for payment plans, or 0 to logout: ");

            switch (intent) { // handles which action the buyer inputs
                case 1 -> { // browse items
                    displayItems();
                }
                case 2 -> { // place bid on item
                    System.out.println("Here are all the open auctions:");
                    for (int i = 0; i < nextAuctionSlot; i++) {
                        Auction auction = auctions.get(i);

                        if (auction.getClosedStatus()) {
                            Item item = items.get(auction.getItemID());

                            System.out.printf("AUCTION # %d:\n", auction.getAuctionID());
                            System.out.printf("--ITEM: %s\n", item.getItemName());
                            System.out.printf("--DESCRIPTION: %s\n", item.getItemDescr());
                            System.out.printf("--CLOSING DATE: %s\n", auction.getClosingDate());
                            System.out.printf("--CURRENT HIGHEST BID: %.2f\n", auction.getCurrentBid());
                            System.out.println();
                        }
                    }

                    System.out.println("Which item do you want to bid on?\n Input id: ");

                    int selectedIndex = -1;
                    while (true) { // validate int and proper index is inputted
                        System.out.println("Which auction do you want to bid on? Input -1 to cancel");

                        if (userInput.hasNextInt()) {
                            selectedIndex = userInput.nextInt();

                            if (selectedIndex == -1) {
                                System.out.println("Bid cancelled.");
                                break;
                            }

                            if (selectedIndex >= 0
                                    && selectedIndex < nextAuctionSlot
                                    && auctions.get(selectedIndex) != null
                                    && auctions.get(selectedIndex).getClosedStatus()) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or is and isn't in the index/auction's closed
                        System.out.println("Invalid auction ID!\n Please input an auction id: ");
                    }
                    if (selectedIndex == -1) {
                        break;
                    }

                    // record which user is bidding so the auction can be tied to their payment plan
                    System.out.println("Which user is placing this bid?");
                    int biddingUser = selectUser();
                    if (biddingUser == -1) {
                        System.out.println("Bid cancelled.");
                        break;
                    }

                    System.out.println("What is your bid? ");
                    double userBid;
                    while (true) { // validate user's bid
                        if (userInput.hasNextDouble()) {
                            userBid = userInput.nextDouble();

                            if (userBid > 0) {
                                if (userBid > auctions.get(selectedIndex).getCurrentBid()) {
                                    auctions.get(selectedIndex).setCurrentBid(userBid);
                                    auctions.get(selectedIndex).setWinningBuyerID(users.get(biddingUser).getUserID());
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

                case 3 -> { // payment plans & amortization
                    paymentMenu();
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
            System.out.println("Welcome, Admin! What do you want to do?\n Input 1 to close a sale early, 2 to edit an existing item, 3 to run reports, 4 for user payment plans & amortization tables. Enter 0 to logout: ");
            intent = getValidatedInt(0, 4, "Invalid input!\n Enter 1 to close a sale early, 2 to edit existing items, 3 to run reports, 4 for payment plans, or 0 to logout: ");

            switch (intent) { // looks at input and runs respective action
                case 1 -> { // close a sale early
                    // display all open auctions
                    System.out.println("Here are all the open auctions:");
                    for (int i = 0; i < nextAuctionSlot; i++) {
                        Auction auction = auctions.get(i);

                        if (auction.getClosedStatus()) {
                            Item item = items.get(auction.getItemID());

                            System.out.printf("AUCTION # %d:\n", auction.getAuctionID());
                            System.out.printf("--ITEM: %s\n", item.getItemName());
                            System.out.printf("--DESCRIPTION: %s\n", item.getItemDescr());
                            System.out.printf("--CLOSING DATE: %s\n", auction.getClosingDate());
                            System.out.printf("--CURRENT HIGHEST BID: %.2f\n", auction.getCurrentBid());
                            System.out.println();
                        }
                    }

                    int selectedAuction = -1;
                    while (true) { // validate input for int and valid ID
                        System.out.println("Which auction do you want to close?\n Input ID, or -1 to cancel: ");

                        if (userInput.hasNextInt()) {
                            selectedAuction = userInput.nextInt();

                            if (selectedAuction == -1) {
                                System.out.println("Action cancelled.");
                                break;
                            }

                            if (selectedAuction >= 0
                                    && selectedAuction < nextAuctionSlot
                                    && auctions.get(selectedAuction) != null
                                    && auctions.get(selectedAuction).getClosedStatus()) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, print this:
                        System.out.println("Invalid auction ID!");
                    }
                    if (selectedAuction == -1) {
                        break;
                    }

                    auctions.get(selectedAuction).setOpen(false); // change open field to false, i.e., closed

                    System.out.printf("Auction #%d has been closed!\n", selectedAuction);
                }
                case 2 -> { // edit an existing item
                    // display existing items
                    displayItems();

                    int selectedIndex = -1;
                    while (true) { // validate user input to make sure it's an int in index range
                        System.out.println("What item do you want to edit?\n Input id, or -1 to cancel: ");

                        if (userInput.hasNextInt()) {
                            selectedIndex = userInput.nextInt();

                            if (selectedIndex == -1) {
                                System.out.println("Item editing cancelled.");
                                break;
                            }

                            if (selectedIndex >= 0
                                    && selectedIndex < nextItemSlot
                                    && items.get(selectedIndex) != null) {
                                break;
                            }
                        } else {
                            userInput.next();
                        }
                        // if input isn't an int, or if it is and isn't in the range of item indices
                        System.out.println("Invalid item ID! Enter ID or -1 to cancel");
                    }
                    if (selectedIndex == -1) {
                        break;
                    }

                    addItem(selectedIndex);
                    System.out.println("Item edited!");
                }
                case 3 -> { // run reports
                    generateReport();
                }
                case 4 -> { // payment plans & amortization
                    paymentMenu();
                }
                case 0 -> { // exit
                    return;
                }
            }
        }
    }

    //Actual sales lead management(option 4 during login)
    public static void potentialSellerMenu() {
        while (true) {
            System.out.println("\nPOTENTIAL SELLER LEADS");
            System.out.println("1. Add a potential seller lead");
            System.out.println("2. View leads for a referring user");
            System.out.println("3. View all potential seller leads");
            System.out.println("0. Return to login");
            int choice = getValidatedInt(0, 3, "Enter 1, 2, 3, or 0: ");
            switch (choice) {
                case 1 ->
                    addPotentialSeller();
                case 2 ->
                    viewPotentialSellersForUser();
                case 3 ->
                    viewAllPotentialSellers();
                case 0 -> {
                    return;
                }
            }
        }
    }

    public static void addPotentialSeller() {
        int userIndex = selectUser();
        if (userIndex == -1) {
            System.out.println("Lead creation cancelled. Add a user first if needed.");
            return;
        }

        int referringUserID = users.get(userIndex).getUserID();
        userInput.nextLine();

        System.out.println("Please input potential seller name (-1 to cancel): ");
        String name = userInput.nextLine().trim();
        if (name.isEmpty() || name.equals("-1")) {
            System.out.println("Lead creation cancelled.");
            return;
        }
        System.out.println("\nPlease input phone number:");
        String phone = userInput.nextLine().trim();
        System.out.println("\nPlease input email address:");
        String email = userInput.nextLine().trim();
        System.out.println("\n Please input notes about possible auction items:");
        String notes = userInput.nextLine().trim();

        int newID = 1; // get the latest id for Potential Sellers
        for (PotentialSeller lead : potentialSellers) {
            if (lead.getPotentialSellerID() >= newID) {
                newID = lead.getPotentialSellerID() + 1;
            }
        }

        // all these replaces are so our saving function doesn't break
        potentialSellers.add(new PotentialSeller(newID,
                name.replace("`", "'"), phone.replace("`", "'"),
                email.replace("`", "'"), notes.replace("`", "'"), referringUserID));
        System.out.println("Lead #" + newID + " saved successfully!");
    }

    public static void viewPotentialSellersForUser() {
        int userIndex = selectUser();
        //Exit if no user is selected
        if (userIndex == -1) {
            return;
        }
        int userID = users.get(userIndex).getUserID();
        int count = 0;

        for (PotentialSeller lead : potentialSellers) {
            if (lead.getReferringUserID() == userID) {
                displayPotentialSellers(lead);
                count++;
            }
        }
        if (count == 0) {
            System.out.println("No leads for that user.");
        }
    }

    public static void viewAllPotentialSellers() {
        if (potentialSellers.isEmpty()) {
            System.out.println("No potential sellers found.");
            return;
        }

        System.out.println("ALL POTENTIAL SELLER LEADS");

        for (PotentialSeller lead : potentialSellers) {
            displayPotentialSellers(lead);
        }
    }
    

    /* Looks at the .csvs and loads it into memory (arrays)
    *  Files are like a CSV, except instead of a comma separating values it's a ` (backtick)
     */
    public static void loadData() {
        try { // this is in a try statement in case there's an error
            Scanner itemsReader = new Scanner(new File("src/main/java/items.csv"));
            if (itemsReader.hasNextLine()) {
                itemsReader.nextLine(); // eat header row
            }

            // while there's still content, get a line, break it up into pieces of data, and save to array
            while (itemsReader.hasNextLine()) {
                String row = itemsReader.nextLine();
                String[] itemInfo = row.split("`"); // split the CSV line into its parts

                items.add(new Item(
                        Integer.parseInt(itemInfo[0]),
                        itemInfo[1],
                        itemInfo[2]
                ));
                nextItemSlot++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("items.csv not found. Starting with no items.");
            nextItemSlot = 0;
        }

        // Read the auctions.cs file and put it in a global array.
        try {
            Scanner auctionReader = new Scanner(new File("src/main/java/auctions.csv"));
            if (auctionReader.hasNextLine()) {
                auctionReader.nextLine(); // eat header row
            }

            while (auctionReader.hasNextLine()) {
                String row = auctionReader.nextLine();
                String[] auctionInfo = row.split("`"); // split the CSV line into its parts

                auctions.add(new Auction(
                        Integer.parseInt(auctionInfo[0]),
                        Integer.parseInt(auctionInfo[1]),
                        Double.parseDouble(auctionInfo[2]),
                        Integer.parseInt(auctionInfo[3]),
                        auctionInfo[4],
                        auctionInfo[5],
                        Boolean.parseBoolean(auctionInfo[6])
                ));
                nextAuctionSlot++;
            }
        } catch (FileNotFoundException ex) {
            System.out.println("auctions.csv not found. Starting with no auctions.");
            nextAuctionSlot = 0;
        }

        // Read users.csv: userID`userName`hasPlan`auctionID`price`annualRate`months`extraPayment
        try {
            Scanner userReader = new Scanner(new File("src/main/java/users.csv"));
            if (userReader.hasNextLine()) {
                userReader.nextLine(); // eat header row
            }

            while (userReader.hasNextLine()) {
                String row = userReader.nextLine();
                if (row.isBlank()) {
                    continue;
                }
                String[] userInfo = row.split("`");

                PaymentPlan plan = null;
                if (Boolean.parseBoolean(userInfo[2])) {
                    plan = new PaymentPlan(
                            Integer.parseInt(userInfo[3]),
                            Double.parseDouble(userInfo[4]),
                            Double.parseDouble(userInfo[5]),
                            Integer.parseInt(userInfo[6]),
                            Double.parseDouble(userInfo[7])
                    );
                }
                users.add(new User(Integer.parseInt(userInfo[0]), userInfo[1], plan));
            }
        } catch (FileNotFoundException ex) {
            System.out.println("users.csv not found. Starting with no users.");
        }
        
        
        // Loading potentialSellers.csv
        try {
            Scanner sellerReader = new Scanner(new File("src/main/java/potentialSellers.csv"));
            //am i pulling the right file?
            if (sellerReader.hasNextLine()) {
                sellerReader.nextLine();
            }
            
            while (sellerReader.hasNextLine()){
                String row = sellerReader.nextLine();
                
                if (row.isBlank()){
                    continue;
                }    

                String[] sellerInfo = row.split("`");
            
                if(sellerInfo.length == 6){
                   potentialSellers.add(new PotentialSeller(
                   Integer.parseInt(sellerInfo[0]),
                   sellerInfo[1],
                   sellerInfo[2],
                   sellerInfo[3],
                   sellerInfo[4],
                   Integer.parseInt(sellerInfo[5])));
                }
            }
        } catch (Exception e) {
            System.out.println("potentialSellers.csv not found. Starting with no potential sellers.");
        }

        System.out.println("All data loaded!");
    }

    /* Overwrites both auctions.csv and items.csv with everything currently in memory
    *  Saves like a CSV, except instead of a comma separating values it's a ` (backtick)
     */
    public static void saveData() {
        try { // this is in a try in case there's a file exception
            // This is for saving items.csv
            PrintWriter itemWriter = new PrintWriter("src/main/java/items.csv");

            // Header row because we're still pretending to have CSVs
            itemWriter.println("itemID`itemName`itemDescr");

            for (int i = 0; i < nextItemSlot; i++) {
                if (items.get(i) != null) {
                    itemWriter.println(
                            items.get(i).getItemID() + "`"
                            + items.get(i).getItemName() + "`"
                            + items.get(i).getItemDescr()
                    );
                }
            }

            itemWriter.close();

            // This is for saving auctions.csv
            PrintWriter auctionWriter = new PrintWriter("src/main/java/auctions.csv");

            // Header row
            auctionWriter.println("auctionID`itemID`currentBid`winningBuyerID`dateOpened`closingDate`open");

            for (int i = 0; i < nextAuctionSlot; i++) {
                if (auctions.get(i) != null) {
                    auctionWriter.println(
                            auctions.get(i).getAuctionID() + "`"
                            + auctions.get(i).getItemID() + "`"
                            + auctions.get(i).getCurrentBid() + "`"
                            + auctions.get(i).getWinningBuyerID() + "`"
                            + auctions.get(i).getDateOpened() + "`"
                            + auctions.get(i).getClosingDate() + "`"
                            + auctions.get(i).getClosedStatus()
                    );
                }
            }

            auctionWriter.close();

            // This is for saving users.csv (users + their saved payment plans)
            PrintWriter userWriter = new PrintWriter("src/main/java/users.csv");
            userWriter.println("userID`userName`hasPlan`auctionID`price`annualRate`months`extraPayment");

            for (User user : users) {
                PaymentPlan plan = user.getPaymentPlan();
                if (plan != null) {
                    userWriter.println(user.getUserID() + "`"
                            + user.getUserName()
                            + "`true`"
                            + plan.getAuctionID() + "`"
                            + plan.getPrice() + "`"
                            + plan.getAnnualRate() + "`"
                            + plan.getMonths() + "`"
                            + plan.getExtraPayment());
                } else {
                    userWriter.println(user.getUserID() + "`" + user.getUserName() + "`false`-1`0`0`0`0");
                }
            }
            userWriter.close();
            
            // this is for saving potentialSellers.csv
            PrintWriter sellerWriter = new PrintWriter("src/main/java/potentialSellers.csv");
        
            sellerWriter.println("potentialSellerID`name`phoneNumber`emailAddress`notes`referringUserID");

            for(PotentialSeller lead : potentialSellers) {
                sellerWriter.println(
                lead.getPotentialSellerID() + "`" +
                lead.getName() + "`" +
                lead.getPhoneNumber() + "`" +
                lead.getEmailAddress() + "`" +
                lead.getNotes() + "`" +
                lead.getReferringUserID());
            }
            sellerWriter.close();

            System.out.println("Data saved. Goodbye!");

        } catch (FileNotFoundException ex) {
            System.out.println("Error saving data!");
        }
    }

    // Helper function to generate reports for the ADmin
    public static void generateReport() {
        System.out.println("Which report would you like to run?");
        System.out.println("1: Live Auctions Report");
        System.out.println("2: Items For Sale Report");
        System.out.println("3: Price Histogram");
        System.out.println("0: Return");
        System.out.println("Please input a number: ");

        int reportType = getValidatedInt(0, 3, "Invalid input!\n Please select option 1, 2, 3, or 0 to return:");

        switch (reportType) { // generate a report based on which input was given

            case 1 -> { // Live auctions grouped by auction
                System.out.println("\nLIVE AUCTIONS REPORT");

                for (int i = 0; i < nextAuctionSlot; i++) {
                    Auction auction = auctions.get(i);
                    if (auction.getClosedStatus()) {
                        Item item = items.get(auction.getItemID());

                        System.out.printf("AUCTION # %d:\n", auction.getAuctionID());
                        System.out.printf("--ITEM: %s\n", item.getItemName());
                        System.out.printf("--DESCRIPTION: %s\n", item.getItemDescr());
                        System.out.printf("--CLOSING DATE: %s\n", auction.getClosingDate());
                        System.out.printf("--CURRENT HIGHEST BID: %.2f\n", auction.getCurrentBid());
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
                    if (auctions.get(i) != null && auctions.get(i).getClosedStatus()) {

                        double price = auctions.get(i).getCurrentBid();

                        if (price >= minimumPrice) {
                            System.out.printf("Auction #%s | %s | Current Bid: $%.2f\n",
                                    auctions.get(i).getAuctionID(),
                                    items.get(auctions.get(i).getItemID()).getItemName(),
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
                    if (auctions.get(i) != null && auctions.get(i).getClosedStatus()) {

                        double price = auctions.get(i).getCurrentBid();

                        if (price <= 5) {
                            bins[0]++;
                        } else if (price <= 10) {
                            bins[1]++;
                        } else if (price <= 20) {
                            bins[2]++;
                        } else if (price <= 50) {
                            bins[3]++;
                        } else {
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

                    System.out.printf("%-12s |", labels[i]);

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

        System.out.println("What is the name of your item? Or enter -1 to cancel ");
        String intent = userInput.nextLine();
        if (intent.equals("-1")) {
            return;
        }
        String itemName = intent;

        System.out.println("What is the description of your item? ");
        String itemDescr = userInput.nextLine();

        items.add(itemIndex, new Item(
                itemIndex,
                itemName,
                itemDescr
        ));
    }

    // Helper function used to display all items
    public static void displayItems() {
        System.out.println("Here are all the items:");
        int i = 0;

        while (i < items.size() && items.get(i) != null) {
            System.out.printf("ITEM ID %s, %s: %s\n",
                    items.get(i).getItemID(), items.get(i).getItemName(), items.get(i).getItemDescr());
            System.out.println();
            i++;
        }
    }

    // Sub-menu for payment plans & amortization (reached from Buyer and Admin menus)
    public static void paymentMenu() {
        while (true) {
            System.out.println("PAYMENT PLANS\n Input 1 to add a user, 2 to run amortization tables for a user. Enter 0 to go back: ");
            int choice = getValidatedInt(0, 2, "Invalid input!\n Enter 1 to add a user, 2 to run tables, or 0 to go back: ");

            switch (choice) {
                case 0:
                    return;

                case 1:
                    addUser();
                    continue;

                case 2:
                    int userIndex = selectUser();
                    if (userIndex == -1) {
                        continue;
                    }
                    User user = users.get(userIndex);

                    // no saved info: enter it now
                    if (!user.hasPaymentPlan()) {
                        System.out.println("No payment info saved yet. Let's enter it.");
                        if (!enterPaymentPlan(user)) {
                            continue;
                        }
                    } else {  // saved info: use it or replace it                                      
                        System.out.println("Input 1 to use saved payment info, 2 to enter new info: ");
                        int useSaved = getValidatedInt(1, 2, "Enter 1 or 2: ");
                        if (useSaved == 2 && !enterPaymentPlan(user)) {
                            continue;
                        }
                    }

                    Amortization.printSchedule(user);
            }
        }
    }

    // Helper function to enter (or replace) a user's payment info. Returns false if cancelled.
    public static boolean enterPaymentPlan(User user) {

        int auctionID = -1;
        double price;

        System.out.println("\nPAYMENT PLAN");
        System.out.println("1. Enter amount manually");
        System.out.println("2. Use an existing auction");
        System.out.println("0. Cancel");

        int choice = getValidatedInt(
                0, 2, "Please enter 0, 1, or 2: "
        );

        if (choice == 0 || choice == -1) {
            return false;
        }

        if (choice == 2) {

            if (auctions.isEmpty()) {
                System.out.println("No auctions available.");
                return false;
            }

            System.out.println("\nAvailable Auctions:");

            for (Auction auction : auctions) {
                System.out.printf(
                        "Auction ID: %d | Current Bid: $%.2f%n",
                        auction.getAuctionID(),
                        auction.getCurrentBid()
                );
            }

            System.out.println("Enter Auction ID, or -1 to cancel:");

            Auction selectedAuction = null;

            while (selectedAuction == null) {

                int selectedID = getValidatedInt(
                        0, Integer.MAX_VALUE,
                        "Please enter a valid Auction ID:"
                );

                if (selectedID == -1) {
                    return false;
                }

                for (Auction auction : auctions) {
                    if (auction.getAuctionID() == selectedID) {
                        selectedAuction = auction;
                        break;
                    }
                }

                if (selectedAuction == null) {
                    System.out.println("Auction not found. Try again:");
                }
            }

            auctionID = selectedAuction.getAuctionID();
            price = selectedAuction.getCurrentBid();

            if (price <= 0) {
                System.out.println("Auction price must be greater than 0.");
                return false;
            }

            System.out.printf(
                    "Amount to finance: $%.2f%n", price
            );

        } else {

            System.out.println(
                    "Enter amount to finance, or -1 to cancel:"
            );

            price = getValidatedDouble(
                    0.01, "Amount must be greater than 0:"
            );

            if (price == -1) {
                return false;
            }
        }

        System.out.println(
                "Enter yearly interest rate (%), or -1 to cancel:"
        );

        double rate = getValidatedDouble(
                0, "Interest rate must be 0 or more:"
        );

        if (rate == -1) {
            return false;
        }

        System.out.println(
                "Pay over 12, 24, or 36 months? (-1 to cancel)"
        );

        int months = getValidatedInt(
                12, 36, "Choose 12, 24, or 36:"
        );

        while (months != -1
                && months != 12
                && months != 24
                && months != 36) {

            System.out.println("Choose 12, 24, or 36:");
            months = getValidatedInt(
                    12, 36, "Invalid number of months:"
            );
        }

        if (months == -1) {
            return false;
        }

        System.out.println(
                "Extra monthly payment? Enter 0 for none:"
        );

        double extra = getValidatedDouble(
                0, "Extra payment must be 0 or more:"
        );

        if (extra == -1) {
            return false;
        }

        user.setPaymentPlan(
                new PaymentPlan(auctionID, price, rate, months, extra)
        );

        System.out.println(
                "Payment info saved for " + user.getUserName() + "!"
        );

        return true;
    }

    // Helper function to create a new user
    public static void addUser() {
        userInput.nextLine(); // eat leftover newline
        System.out.println("Enter the new user's name, or -1 to cancel: ");
        String name = userInput.nextLine().trim();
        if (name.equals("-1") || name.isEmpty()) {
            System.out.println("User creation cancelled.");
            return;
        }

        // next ID is one past the highest existing ID
        int newID = 0;
        for (User user : users) {
            if (user.getUserID() >= newID) {
                newID = user.getUserID() + 1;
            }
        }
        users.add(new User(newID, name.replace("`", "'"), null)); // no backticks, they'd break the CSV
        System.out.printf("User #%d '%s' created!\n", newID, name);
    }

    // Helper function to display all users
    public static void displayUsers() {
        System.out.println("Here are all the users:");
        for (User user : users) {

            System.out.printf("USER ID %d, %s | Payment info saved: %s\n",
                    user.getUserID(), user.getUserName(), user.hasPaymentPlan() ? "yes" : "no");
        }
        System.out.println();
    }
    
    // Helper function to display all potential sellers
    public static void displayPotentialSellers(PotentialSeller lead) {
        System.out.println("Lead ID: " + lead.getPotentialSellerID());
        System.out.println("Name: " + lead.getName());
        System.out.println("Phone: " + lead.getPhoneNumber());
        System.out.println("Email: " + lead.getEmailAddress());
        System.out.println("Auction notes: " + lead.getNotes());
        System.out.println("Referring user ID: " + lead.getReferringUserID());
    }

    // Helper function: show users, ask for an ID, return that user's index in the list (or -1 to cancel)
    public static int selectUser() {
        if (users.isEmpty()) {
            System.out.println("There are no users yet. Add one first.");
            return -1;
        }
        displayUsers();

        while (true) {
            System.out.println("Input user ID, or -1 to cancel: ");
            if (userInput.hasNextInt()) {
                int id = userInput.nextInt();
                if (id == -1) {
                    return -1;
                }
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getUserID() == id) {
                        return i;
                    }
                }
            } else {
                userInput.next();
            }
            System.out.println("Invalid user ID!");
        }
    }

    // Helper function. User inputs something, check if its a double past some min
    public static double getValidatedDouble(double min, String errorMessage) {
        while (true) {
            if (userInput.hasNextDouble()) {
                double value = userInput.nextDouble();
                if (value == -1) {
                    return -1;
                }
                if (value >= min) {
                    return value;
                }
            } else {
                userInput.next();
            }
            System.out.println(errorMessage);
        }
    }

    // Helper function. User inputs something, check if it's an int within the range
    public static int getValidatedInt(int min, int max, String errorMessage) {
        while (true) {
            if (userInput.hasNextInt()) {
                int value = userInput.nextInt();

                if (value == -1) {
                    return -1;
                }

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
