

import java.util.Scanner;

/**
 *
 * @author Dorian Crawford
 */
public class Amortization {

    public static void main(String[] args) {
        Amortize();
    }
    
    public static void Amortize() {
        // COLLECT USER INPUTS
        Scanner userInput = new Scanner(System.in);
        
        String itemTitle = "";
        String description = "";
        double price = 0.0;
        int lengthOfPayment = 0;
        
        System.out.println("Welcome! Please input the name of your item: ");
        itemTitle = userInput.nextLine();
        
        System.out.println("\nInput the description of your item: ");
        description = userInput.nextLine();
        
        System.out.println("\nInput the price of your item: ");
        while (!userInput.hasNextDouble()) { // ensures only doubles are entered
            userInput.next();
            System.out.println("\nInvalid input! Input the price of your item as just a number: ");
        }
        price = userInput.nextDouble();
        
        // This is in a loop because if the user inputs a wrong timeframe we'll prompt them again
        System.out.println("\nDo you want to pay over 12 months, 24 months, or 36 months? Type in the respective number: ");
        while (!userInput.hasNextInt()) { // ensures only ints are entered
            userInput.next();
            System.out.println("\nInvalid input! Please select 12 months, 24 months, or 36 months. Type in the respective number: ");
        }
        
        int selectedTimeframe = userInput.nextInt();
        while (selectedTimeframe != 12 && selectedTimeframe != 24 && selectedTimeframe != 36) {
            System.out.println("Incorrect input! Please select 12 months, 24 months, or 36 months. Type in the respective number: ");
            selectedTimeframe = userInput.nextInt();
        }
        lengthOfPayment = selectedTimeframe;
        
        System.out.println("\n");       
        
        // AMORTIZATION SCHEDULING LOGIC
        int numberOfYears = lengthOfPayment / 12;
        double interestRate = .015; //1.5%
        double monthlyPayment = price / lengthOfPayment;
        double amountLeft = price; // we'll be decrementing this with each month
        
        // amortization formula that we're using:
        // (interest rate * price) + (price / # of months)
        
        // Theoretically you could iterate through and store the values in arrays,
        // then print out the results in a grid, but semantics.
        for (int i = 1; i <= numberOfYears; i++) {
            System.out.println("-=+YEAR " + i + "+=-");
            
            System.out.print("MONTH | ");
            System.out.print("STARTING BALANCE | ");
            System.out.print("INTEREST | ");
            System.out.print("MONTHLY PAYMENT | ");
            System.out.print("TOTAL THIS MONTH\n");
            System.out.print("REMAINING BALANCE\n");
            
            // amountLeft is here so we don't go into the negatives
            for (int j = 1; j <= 12; j++) {
            
                double startingBalance = amountLeft;
                double interestPayment = startingBalance * interestRate;
                double totalDue;
         
                // Assume normal monthly payment
                double currentMonthlyPayment = monthlyPayment;
                
                if (monthlyPayment >= startingBalance) {
                    currentMonthlyPayment = startingBalance - interestPayment;
                }
                totalDue = interestPayment + currentMonthlyPayment;
                
                PrintMonth(j);
                System.out.printf("%,12.2f - ", startingBalance);
                System.out.printf("%,12.2f - ", interestPayment);
                System.out.printf("%,12.2f = ", currentMonthlyPayment);
                System.out.printf("%,12.2f | ", totalDue);
                
                amountLeft -= totalDue;
                
                // Guard against tiny negative values from rounding
                if (amountLeft < 0.01) {
                    amountLeft = 0.00;
                }
                System.out.printf("%,12.2f", amountLeft);
                System.out.println();
            }
            
            System.out.println();
        }
    }
    
    static void PrintMonth(int month) {
        switch (month) {
            case 1 -> {
                System.out.print("JAN: ");
            }
            case 2 -> {
                System.out.print("FEB: ");
            }
            case 3 -> {
                System.out.print("MAR: ");
            }
            case 4 -> {
                System.out.print("APR: ");
            }
            case 5 -> {
                System.out.print("MAY: ");
            }
            case 6 -> {
                System.out.print("JUN: ");
            }
            case 7 -> {
                System.out.print("JUL: ");
            }
            case 8 -> {
                System.out.print("AUG: ");
            }
            case 9 -> {
                System.out.print("SEP: ");
            }
            case 10 -> {
                System.out.print("OCT: ");
            }
            case 11 -> {
                System.out.print("NOV: ");
            }
            case 12 -> {
                System.out.print("DEC: ");
            }
            default -> {
            }
        }
    }
}
