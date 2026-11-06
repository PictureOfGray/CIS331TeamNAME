/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for User
 * @disclaimer 
 */
public class User {
    
    //fields
    private int userID;
    private String userName;
    private PaymentPlan paymentPlan; // null until payment info is entered

//Constructor
public User(int userID, String userName, PaymentPlan paymentPlan) {
        this.userID = userID;
        this.userName = userName;
        this.paymentPlan = paymentPlan;
    }

// Getters
    public int getUserID() {
        return userID;
    }

    public String getUserName() {
        return userName;
    }

    public PaymentPlan getPaymentPlan() {
        return paymentPlan;
    }

    public boolean hasPaymentPlan() {
        return paymentPlan != null;
    }

    // setters
    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPaymentPlan(PaymentPlan paymentPlan) {
        this.paymentPlan = paymentPlan;
    }

}

