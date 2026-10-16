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
            case 1:
                sellerMenu();
                break;
            case 2:
                buyerMenu();
                break;
            case 3:
                adminMenu();
                break;
            case 0:
                return;
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

}
