/**
 * @authors Dorian Crawford, Laura Elmore, Daniel Haddadin, Mason Noufal, Tyler Ryu, Qiuxuan Xiong, Yuyao Yang
 * @date 11/8/2026
 * @purpose This is the Class Definition File for Auctions
 * @disclaimer 
 */
public class Auction {
    private int auctionID;
    private int itemID;
    private double currentBid;
    private int winningBuyerID;
    private String dateOpened;
    private String closingDate;
    private boolean open;

    public Auction(int auctionID, int itemID, double currentBid, int winningBuyerID, String dateOpened, String closingDate, boolean open) {
        this.auctionID = auctionID;
        this.itemID = itemID;
        this.currentBid = currentBid;
        this.winningBuyerID = winningBuyerID;
        this.dateOpened = dateOpened;
        this.closingDate = closingDate;
        this.open = open;
    }

    public int getAuctionID() {
        return auctionID;
    }

    public int getItemID() {
        return itemID;
    }

    public double getCurrentBid() {
        return currentBid;
    }

    public int getWinningBuyerID() {
        return winningBuyerID;
    }

    public String getDateOpened() {
        return dateOpened;
    }

    public String getClosingDate() {
        return closingDate;
    }
    
    public boolean getClosedStatus() {
        return open;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setCurrentBid(double currentBid) {
        this.currentBid = currentBid;
    }

    public void setWinningBuyerID(int winningBuyerID) {
        this.winningBuyerID = winningBuyerID;
    }

    public void setDateOpened(String dateOpened) {
        this.dateOpened = dateOpened;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
