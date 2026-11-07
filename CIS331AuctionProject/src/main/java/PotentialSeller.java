/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for Potential Sellers
 * @disclaimer 
 */
public class PotentialSeller {
    private int potentialSellerID;
    private String name;
    private String phoneNumber;
    private String emailAddress;
    private String notes;

    public PotentialSeller(int potentialSellerID, String name, String phoneNumber, String emailAddress, String notes) {
        this.potentialSellerID = potentialSellerID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
    }

    public int getPotentialSellerID() {
        return potentialSellerID;
    }

    public String getName() {
        return name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getNotes() {
        return notes;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
