package edu.ucla.cs.cs144;

import java.lang.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hit;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import java.util.Date;
import java.util.Iterator;
import java.text.SimpleDateFormat;

import edu.ucla.cs.cs144.DbManager;
import edu.ucla.cs.cs144.SearchConstraint;
import edu.ucla.cs.cs144.SearchResult;

import java.util.HashMap;

public class AuctionSearch implements IAuctionSearch {

	/* 
         * You will probably have to use JDBC to access MySQL data
         * Lucene IndexSearcher class to lookup Lucene index.
         * Read the corresponding tutorial to learn about how to use these.
         *
         * Your code will need to reference the directory which contains your
	 * Lucene index files.  Make sure to read the environment variable 
         * $LUCENE_INDEX with System.getenv() to build the appropriate path.
	 *
	 * You may create helper functions or classes to simplify writing these
	 * methods. Make sure that your helper functions are not public,
         * so that they are not exposed to outside of this class.
         *
         * Any new classes that you create should be part of
         * edu.ucla.cs.cs144 package and their source files should be
         * placed at src/edu/ucla/cs/cs144.
         *
         */
	private IndexSearcher searcher = null;
	private QueryParser contentParser = null;
	
	public AuctionSearch() {
		try{
			searcher = new IndexSearcher(System.getenv("LUCENE_INDEX") + "/index1");
			contentParser = new QueryParser("content", new StandardAnalyzer());
		}
		catch (Exception e)
		{
			System.out.println(e);
		}
	}		
	public SearchResult[] basicSearch(String query, int numResultsToSkip, 
			int numResultsToReturn) {
		SearchResult[] r = new SearchResult[0];
		try{
		Query parsedQuery = contentParser.parse(query);
		Hits hits = searcher.search(parsedQuery);
		System.out.println("size should be 72");
		System.out.println(hits.length());
		int size = Math.min(hits.length(),numResultsToReturn+numResultsToSkip);
		r = new SearchResult[size];
		for(int i = numResultsToSkip,j=0; i < size; i++,j++) {
		   Document doc = hits.doc(i);
		   String itemId = doc.get("itemId");
		   String name = doc.get("name");
		   //System.out.println(itemId + ":" + name);
		   r[j] = new SearchResult(itemId,name);
		//1 space may allocated wrong. 2 remember star trek means star OR trek which is the same for parse
		 }
		}
		catch (Exception e)
		{
			System.out.println("Exeception caught in basic search");
		}
		
		return r;
	}

	public SearchResult[] advancedSearch(SearchConstraint[] constraints, 
			int numResultsToSkip, int numResultsToReturn) {
		// TODO: Your code here!
		SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy H:m:s");
		SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
		
		
		HashMap<String, String> name_constraints = new HashMap<String,String>();
		name_constraints.put(FieldName.ItemName, "name");
		name_constraints.put(FieldName.Category, "category");
		name_constraints.put(FieldName.SellerId, "seller");
		name_constraints.put(FieldName.BuyPrice, "buy_price");
		name_constraints.put(FieldName.BidderId, "bidder");//where it was used? another table
		name_constraints.put(FieldName.EndTime, "ends");
		name_constraints.put(FieldName.Description, "description");
		
		String mysqlQuery = "";
		String luceneQuery = "";
		for(int i=0; i<constraints.length; i++)
		{
			String constrainsName = constraints[i].getFieldName();
			if(name_constraints.containsKey(constrainsName))
			{
				String value = constraints[i].getValue();
				
				//seems need to be escaped
				//then construct the string
				String second = name_constraints.get(constrainsName);
				if(second.equals("category") || second.equals("name") || second.equals("description"))
				{
					if(luceneQuery.equals(""))
						luceneQuery = second + ":" + value  ;// value needs to be escaped
					else
						luceneQuery = luceneQuery +"AND" + second + ":" + value ; //also value needs to be escaped
				}
				else
				{
					if(second.equals("ends"))
					{
						/*
						try {
				            Date parsed = format.parse(value);
				            value = newformat.format(parsed);
				        	}
				        catch(ParseException pe) {
				            System.out.println("ERROR: Cannot parse \"" + value + "\"");
				        }
						*/
					}
					if(mysqlQuery.equals(""))
						mysqlQuery = second + "=" +"\"" + value + "\"";//needs to be escaped
					else
						mysqlQuery = mysqlQuery + "AND" + second + "=" +"\"" + value + "\"";//needs to be escaped
					//mysql query
				}
				
				
			}
			System.out.println("mysql query: " + mysqlQuery);
			System.out.println("lucene query: "+ luceneQuery);
		}
		//1 include in FieldName(done)
		//2 connect to mysql to find out item_id of some part using  AND
		//3 connect to lucene and search using OR???
		//4 Final result should intersect 3 and 2
		return new SearchResult[0];
	}

	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		return null;
	}
	
	public String echo(String message) {
		return message;
	}

}
