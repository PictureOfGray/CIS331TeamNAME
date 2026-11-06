/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for a user's saved payment plan
 * @disclaimer 
 */
public class PaymentPlan {          // ← class opens here

    // fields
    private int auctionID;          // auction this pays for, or -1 if typed in manually
    private double price;           // amount being financed
    private double annualRate;      // yearly interest as a percent, e.g. 1.5
    private int months;             // 12, 24, or 36
    private double extraPayment;    // extra paid above the minimum each month (0 if none)

    // constructor
    public PaymentPlan(int auctionID, double price, double annualRate, int months, double extraPayment) {
        this.auctionID = auctionID;
        this.price = price;
        this.annualRate = annualRate;
        this.months = months;
        this.extraPayment = extraPayment;
    }

    // getters
    public int getAuctionID() {
        return auctionID;
    }

    public double getPrice() {
        return price;
    }

    public double getAnnualRate() {
        return annualRate;
    }

    public int getMonths() {
        return months;
    }

    public double getExtraPayment() {
        return extraPayment;
    }

    // calculations

    // yearly percent -> monthly decimal (6% -> 0.005)
    public double getMonthlyRate() {
        return annualRate / 100 / 12;
    }

    // smallest payment that pays it off in exactly `months` months
    public double getMinimumPayment() {
        double r = getMonthlyRate();
        if (r == 0) {
            return price / months;   // no interest: just split it evenly
        }
        return price * r / (1 - Math.pow(1 + r, -months));
    }

    // what the user actually pays each month
    public double getMonthlyPayment() {
        return getMinimumPayment() + extraPayment;
    }

}