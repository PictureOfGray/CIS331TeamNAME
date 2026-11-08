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
    private int referringUserID;

    public PotentialSeller(int potentialSellerID, String name, String phoneNumber, String emailAddress, String notes, int referringUserID) {
        this.potentialSellerID = potentialSellerID;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.notes = notes;
        this.referringUserID = referringUserID;
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
   public int getReferringUserID() {
       return referringUserID;
   }
    public String getNotes() {
        return notes;
    }

    public String getEmailAddress() {
        return emailAddress;
    }
}
