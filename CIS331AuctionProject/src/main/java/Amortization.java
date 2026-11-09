/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for a user's saved payment plan
 * @disclaimer 
 */
public class Amortization {        

    public static void printSchedule(User user) {
        PaymentPlan plan = user.getPaymentPlan();
        if (plan == null) {
            System.out.println("This user has no payment information saved.");
            return;
        }

        // pull everything from the saved plan
        double monthlyRate = plan.getMonthlyRate();
        double payment = plan.getMonthlyPayment();
        double balance = plan.getPrice();
        double totalInterest = 0;
        double totalPaid = 0;
        int month = 0;

        // Print summary
        System.out.printf("\nAMORTIZATION SCHEDULE FOR %s (USER #%d)\n", user.getUserName(), user.getUserID());
        System.out.printf("Amount financed: $%,.2f | Interest: %.2f%% per year | Term: %d months\n",
                plan.getPrice(), plan.getAnnualRate(), plan.getMonths());
        System.out.printf("Minimum payment: $%,.2f | Extra per month: $%,.2f | You pay: $%,.2f/month\n\n",
                plan.getMinimumPayment(), plan.getExtraPayment(), payment);

        // keep going until it's paid off (extra payments can end it early)
        while (balance > 0.005 && month < plan.getMonths()) {

            if (month % 12 == 0) { // start of a new year block, like Part 1
                System.out.println("-=+YEAR " + (month / 12 + 1) + "+=-");
                System.out.printf("%-10s| %16s | %10s | %10s | %10s | %17s\n",
                        "MONTH", "STARTING BALANCE", "INTEREST", "PRINCIPAL", "PAYMENT", "REMAINING BALANCE");
            }
            month++;

            double startingBalance = balance;
            double interest = startingBalance * monthlyRate;
            double thisPayment = payment;

            // last payment: just pay off what's left
            if (thisPayment > startingBalance + interest || month == plan.getMonths()) {
                thisPayment = startingBalance + interest;
            }

            double principal = thisPayment - interest;
            balance = startingBalance - principal;   // only principal lowers the balance
            if (balance < 0.005) {
                balance = 0;
            }

            totalInterest += interest;
            totalPaid += thisPayment;

            System.out.printf("%-10s| %,16.2f | %,10.2f | %,10.2f | %,10.2f | %,17.2f\n",
                    String.format("%2d %s", month, monthName(month)),
                    startingBalance, interest, principal, thisPayment, balance);

            if (month % 12 == 0) {
                System.out.println(); // blank line between years
            }
        }

        // totals at the bottom
        System.out.printf("\nPaid off in %d months. Total paid: $%,.2f (interest: $%,.2f)\n",
                month, totalPaid, totalInterest);

        if (plan.getExtraPayment() > 0) {
            double minimumTotal = plan.getMinimumPayment() * plan.getMonths();
            System.out.printf("Paying extra saves %d month(s) and about $%,.2f compared with the minimum.\n",
                    plan.getMonths() - month, minimumTotal - totalPaid);
        }
        System.out.println();
    }

    // replaces the old PrintMonth switch: month 1 = JAN, 13 = JAN again, etc.
    static String monthName(int month) {
        String[] names = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN",
                          "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        return names[(month - 1) % 12];
    }

}