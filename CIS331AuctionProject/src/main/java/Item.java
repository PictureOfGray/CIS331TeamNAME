/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for Items
 * @disclaimer 
 */
public class Item {
    private int itemID;
    private String itemName;
    private String itemDescr;
    
    // Constructor
    public Item(int itemID, String itemName, String itemDescr) {
        this.itemID = itemID;
        this.itemName = itemName;
        this.itemDescr = itemDescr;
    }

    public int getItemID() {
        return itemID;
    }

    public String getItemName() {
        return itemName;
    }
    
    public String getItemDescr() {
        return itemDescr;
    }

    public void setItemName(int itemID) {
        this.itemID = itemID;
    }

    public void setItemDescr(String itemDescr) {
        this.itemDescr = itemDescr;
    }
}
