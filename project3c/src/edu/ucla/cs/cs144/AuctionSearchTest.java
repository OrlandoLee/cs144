package edu.ucla.cs.cs144;

import java.util.Calendar;
import java.util.Date;

import edu.ucla.cs.cs144.AuctionSearch;
import edu.ucla.cs.cs144.SearchResult;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.FieldName;

public class AuctionSearchTest {
	public static void main(String[] args1)
	{
		AuctionSearch as = new AuctionSearch();

		String message = "Test message";
		String reply = as.echo(message);
		System.out.println("Reply: " + reply);
		
		String query = "star trek";
		SearchResult[] basicResults = as.basicSearch(query, 0, 20);
		System.out.println("Basic Seacrh Query: " + query);
		System.out.println("Received " + basicResults.length + " results");
		for(SearchResult result : basicResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
//		SearchConstraint constraint =
		    new SearchConstraint(FieldName.BuyPrice, "5.99"); 
		SearchConstraint constraint1 = new SearchConstraint(FieldName.EndTime,"Dec-14-01 21:00:05");
	//	SearchConstraint constraint = new SearchConstraint(FieldName.ItemName,"Precious Moments");
		//SearchConstraint constraint = new SearchConstraint(FieldName.BidderId,"potzer12");
	//	SearchConstraint constraint1 = new SearchConstraint(FieldName.SellerId,"waltera317a");
	//	SearchConstraint constraint1 = new SearchConstraint(FieldName.Category,"kitchenware");
		SearchConstraint[] constraints = {constraint1};
		SearchResult[] advancedResults = as.advancedSearch(constraints, 0, 20);
		System.out.println("Advanced Seacrh");
		System.out.println("Received " + advancedResults.length + " results");
		for(SearchResult result : advancedResults) {
			System.out.println(result.getItemId() + ": " + result.getName());
		}
		
		//String itemId = "1497595357";
		String itemId = "1497497054";
		String item = as.getXMLDataForItemId(itemId);
		System.out.println("XML data for ItemId: " + itemId);
		System.out.println(item);

		// Add your own test here
	}
}
