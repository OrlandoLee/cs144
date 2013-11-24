package edu.ucla.cs.cs144;

import java.util.Comparator;
import java.util.Date;

public class Bid {
	Date bidTime;
	String bidAmount;
    String bidderID;
    String bidderRating;
    String bidderLocation;
	String bidderCountry;
    

	public Bid(Date bidTime, String bidAmount,String bidderID,String bidderRating,String bidderLocation,String bidderCountry) {
		super();
		this.bidTime = bidTime;
		this.bidAmount = bidAmount;
		this.bidderID = bidderID;
		this.bidderRating = bidderRating;
		this.bidderLocation = bidderLocation;
        this.bidderCountry = bidderCountry;
	}


    public String getbidAmount() {
		return bidAmount;
	}
    public String getbidderID() {
		return bidderID;
	}
    public String getbidderRating() {
		return bidderRating;
	}
    public String getbidderLocation() {
		return bidderLocation;
	}
	public String getbidderCountry() {
		return bidderCountry;
	}	
	public Date getTime() {
		return bidTime;
	}

	/**
     * Compare current bid with given bid using their bid times
     */
	public static Comparator<Bid> BidTimeComparator = new Comparator<Bid>() {
		public int compare(Bid bid1, Bid bid2) 
		{
			Date time1 = bid1.getTime();
		    Date time2 = bid2.getTime();
		    return time1.compareTo(time2);
		}
	};
	
}
