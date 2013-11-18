package edu.ucla.cs.cs144;

import java.math.BigDecimal;
import java.util.Date;

public class Item {
	String itemId;
    String name;
    String sellerID;
    String sellerRating;
    String sellerLocation;
    String sellerCountry;
    String description;
    String firstBid;
    String buyPrice;
    Date startTime;
    Date endTime;
    Bid[] bidArray;
    String[] categoryArray;

	public Item(String itemId,String name,String sellerID,String sellerRating,String sellerLocation,String sellerCountry, String description, String firstBid,String buyPrice,Date startTime,
			Date endTime, Bid[] bidArray, String[] categoryArray) {
		super();
        this.itemId = itemId;
		this.name = name;
		this.sellerID = sellerID;
        this.sellerRating = sellerRating;
        this.sellerLocation = sellerLocation;
        this.sellerCountry = sellerCountry;
        this.description = description;
		this.firstBid = firstBid;
		this.buyPrice = buyPrice;
		this.startTime = startTime;
		this.endTime = endTime;
		this.bidArray = bidArray;
		this.categoryArray = categoryArray;
	}

	public String getitemId() {
		return itemId;
	}
    public String getname() {
		return name;
	}
    public String getsellerID() {
		return sellerID;
	}
    public String getsellerRating() {
		return sellerRating;
	}
    public String getsellerLocation() {
		return sellerLocation;
	}
    public String getsellerCountry() {
		return sellerCountry;
	}
    public String getdescription() {
		return description;
	}
    public String getfirstBid() {
		return firstBid;
	}
    public String getbuyPrice() {
		return buyPrice;
	}
  
	public Bid[] getBids() {
		return bidArray;
	}

	
	public String[] getCategories() {
		return categoryArray;
	}
    
	public Date getStartTime() {
		return startTime;
	}
	public Date getEndTime() {
		return endTime;
	}

	
}
