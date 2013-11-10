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
import java.util.Set;
import java.util.HashSet;

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
            //	System.out.println("size should be 72");
            System.out.println(hits.length());
            int size = Math.min(hits.length(),numResultsToReturn+numResultsToSkip);
            r = new SearchResult[size];
            for(int i = numResultsToSkip,j=0; i < size; i++,j++) {
                Document doc = hits.doc(i);
                String itemId = doc.get("itemId");
                String name = doc.get("name");
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
        Connection conn = null;
        SearchResult[] result = new SearchResult[0];
        
        try{
            SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy H:m:s");
            SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            
            
            HashMap<String, String> name_constraints = new HashMap<String,String>();
            HashMap<String, String> temp_result = new HashMap<String,String>();
            HashSet <String> luceneSet = new HashSet<String>();
            HashSet <String> mysqlSet = new HashSet<String>();
            
            name_constraints.put(FieldName.ItemName, "name");
            name_constraints.put(FieldName.Category, "category");
            name_constraints.put(FieldName.SellerId, "seller");
            name_constraints.put(FieldName.BuyPrice, "buy_price");
            name_constraints.put(FieldName.BidderId, "bidder");//where it was used? another table
            name_constraints.put(FieldName.EndTime, "ends");
            name_constraints.put(FieldName.Description, "description");
            
            String mysqlQuery = "";
            String luceneQuery = "";
	    	int bidindex = -1;
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
                            luceneQuery = second + " : " + value  ;// value needs to be escaped
                        else
                            luceneQuery = luceneQuery +" AND " + second + " : " + value ; //also value needs to be escaped
                    }
                    else
                    {
                        if(second.equals("ends"))
                        {
				            Date parsed = format.parse(value);
				            value = newformat.format(parsed);
                            
                        }
			if(second.equals("bidder"))
			{
			 bidindex = i;
			}
			else
			{
                        if(mysqlQuery.equals(""))
                            mysqlQuery = "select item_id,name from item where " + second + "=" +"\"" + value + "\"";//needs to be escaped
                        else
                            mysqlQuery = mysqlQuery + " AND " + second + "=" +"\"" + value + "\"";//needs to be escaped
                        }
			//mysql query
                    }
		}
		}
			if(bidindex!=-1)
			{
				String bidder = " \"" + constraints[bidindex].getValue() + "\" ";
				if(mysqlQuery.equals(""))
				     {
					mysqlQuery = "select bids.item_id,name from item as A join bids on A.item_id = bids.item_id where bids.user_id = "+ bidder;
				     }
				else
				{
				mysqlQuery = "select A.item_id, name from (" +mysqlQuery + ")  as A join bids on A.item_id = bids.item_id where user_id = "+ bidder;
                		}
			}   
                
				
           
			System.out.println("mysql query: " + mysqlQuery);
			System.out.println("lucene query: "+ luceneQuery);
			
			//lucene result
			
            
 	    if(!luceneQuery.equals(""))           
           {
	     Query parsedQuery = contentParser.parse(luceneQuery);
            Hits hits = searcher.search(parsedQuery);
            //System.out.println(hits.length());
            int size = hits.length();
            for(int ii = 0; ii < size; ii++) {
                Document doc = hits.doc(ii);
                String itemId = doc.get("itemId");
                String name = doc.get("name");
                temp_result.put(itemId,name);
                luceneSet.add(itemId);
				// remember star trek means star OR trek which is the same for parse
            }
	   }		
			//mysql result
			
            if(!mysqlQuery.equals(""))
	{
            // create a connection to the database to retrieve Items from MySQL
            try {
                conn = DbManager.getConnection(true);
            } catch (SQLException ex) {
                System.out.println(ex);
            }
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(mysqlQuery);
            
            while(rs.next())
            {  
                mysqlSet.add(rs.getString("item_id"));
            	temp_result.put(rs.getString("item_id"),rs.getString("name"));
	    	}
	}
            if(!mysqlQuery.equals("")&&!luceneQuery.equals("")) 
            luceneSet.retainAll(mysqlSet);
	    else
	    luceneSet.addAll(mysqlSet);
	   
            int finalSize = Math.min( (luceneSet.size()-numResultsToSkip), numResultsToReturn);
	    result = new SearchResult[finalSize];
            Iterator<String> iterator = luceneSet.iterator();
            int index = 0;
	    int count = 0; 
           while(iterator.hasNext())
            {
		if(count>=numResultsToSkip && count<finalSize+numResultsToSkip)
		{
                String itemId = iterator.next();
		if (temp_result.get(itemId)!=null)
                  {
                    String name = temp_result.get(itemId);
		    		result[index] =  new SearchResult(itemId, name);
                    index++;
                  }
		else
		{
		  System.out.println("ERROR!!!");	
		}
		}
		else
		  iterator.next();

		count++;
            }

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } //catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
           // e.printStackTrace();
        //} 
	catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		//1 include in FieldName(done)
		//2 connect to mysql to find out item_id of some part using  AND
		//3 connect to lucene and search using OR???
		//4 Final result should intersect 3 and 2
		return result;
	}
    
	public String getXMLDataForItemId(String itemId) {
		// TODO: Your code here!
		Connection conn = null;
		String xml=null;
		try
		{
			SimpleDateFormat newformat = new SimpleDateFormat("MMM-dd-yy H:m:s");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");

            conn = DbManager.getConnection(true);
            Statement stmt = conn.createStatement();
			String query = "SELECT * FROM item JOIN user ON item.seller = user.user_id WHERE item_id = " +  "\"" +itemId + "\""; //item & user table
            ResultSet rs = stmt.executeQuery(query);
            
			HashMap<String, String> tag_table = new HashMap<String,String>();
			tag_table.put("name", "Name");
			tag_table.put("category","Category");
			tag_table.put("currently","Currently");
			tag_table.put("buy_price","Buy_Price");
			tag_table.put("first_bid","First_Bid");
			tag_table.put("number_of_bids","Number_of_Bids");
			tag_table.put("bids","Bids");
			tag_table.put("bid","Bid");
			tag_table.put("bidder","Bidder");
			tag_table.put("location","Location");
			tag_table.put("country","Country");
			tag_table.put("time","Time");
			tag_table.put("amount","Amount");
			tag_table.put("started","Started");
			tag_table.put("ends","Ends");
			tag_table.put("description","Description");
			//TO-DO FILL OUT THE TABLE
			
            while(rs.next())
            {  
			   	xml += "<Item ItemID=\"" + itemId + "\">\n";
				xml += "  " + getXMLLine(tag_table.get("name"), rs);
				//category 
				Statement category_stmt = conn.createStatement();
				String category_query = "Select * from id_category where item_id = " + "\"" + itemId +"\"";
				ResultSet category_rs = category_stmt.executeQuery(category_query);
				while(category_rs.next())
				{
					xml + = "  " + getXMLLine(tag_table.get("category"), category_rs);
				}
				category_rs.close();
				category_stmt.close();
				
				xml += "  " + getXMLLine(tag_table.get("currently"), rs);
				
				if (!getXMLLine(tag_table.get("buy_price"), result).equals("<Buy_Price>$0.00</Buy_Price>\n"))
			         xml += "  " + getXMLLine(tag_table.get("buy_price"), rs); 
			                               
			 	xml += "  " + getXMLLine(tag_table.get("first_bid"), rs);                // Parse First Bid tag
			 	xml += "  " + getXMLLine(tag_table.get("number_of_bids"), rs);   // Parse Number of Bids tag
			  
				Statement bids_stmt = conn.createStatement();
				String bids_query = "SELECT * FROM bids JOIN user ON bids.user_id = user.user_id WHERE item_id = " +  "\"" +itemId + "\"";
				ResultSet bids_rs = bids_stmt.executeQuery(bids_query);
				xml += "  " + "<" + tag_table.get("bids") + ">\n";
				
				while (bids_result.next()) {
			                                 	xml += "    <" + tag_table.get("bid") + ">\n";
			                                    xml += "      <" + tag_table.get("bidder") + " UserID=\"" + bids_rs.getString("user_id") + "\" ";
			                                    xml += "Rating=\"" + bids_rs.getString("rating") + "\">\n";
	                                            xml += "        " + getXMLLine(tag_table.get("location"), bids_rs);     // Find Bidder's Location if present
			                        			xml += "        " + getXMLLine(tag_table.get("country"), bids_rs);      // Find Bidder's Country if present
			                                    xml += "      </" + tag_table.get("bidder") + ">\n";
												Date bid_parsed = format.parse(bids_rs.getString("time"));
										        bid_value = newformat.format(parsed);
			                                    xml += "      <" + tag_table.get("time") + ">" + bid_value + "</" + tag_table.get("time") + ">\n";
			                                    xml += "      " + getXMLLine(tag_table.get("amount"), bids_rs);
			                                    xml += "    </" + tag_table.get("bid") + ">\n";
			                      
			                                }
                 xml += "  </" + tag_table.get("bids") + ">\n";
			     bids_rs.close();
		         bids_stmt.close();
		
				 xml += "  " + getXMLLine(tag_table.get("location"), rs); 
				 xml += "  " + getXMLLine(tag_table.get("country"), rs);
 				 xml += "  " + getXMLLine(tag_table.get("started"), rs);                  // Parse Started tag
				 xml += "  " + getXMLLine(tag_table.get("ends"), rs);                             // Parse Ends tag
  				 xml += "  <" + tag_table.get("tag") + " ";                                                // Parse Seller tag
				 xml += "UserID" + "=\"" + rs.getString("seller") + "\" ";
				 xml += "Rating=\"" + rs.getString("rating") + "\"/>\n";
				 xml += "  " + getXMLLine(tag_table.get("description"), rs);              // Parse Description tag
 				 xml += "</" + "Item" + ">";
	    	}
				rs.close();
				stmt.close();
				conn.close();
			
		} catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CorruptIndexException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } 
		  catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	
		return xml;
	}
	
	private String getXMLLine(String tag, ResultSet r) throws SQLException {
	                String xml = "<" + tag + ">";
	                if (tag.equals("Currently") || tag.equals("Buy_Price") || tag.equals("First_Bid")  || tag.equals("Amount"))
	                        xml += "$";
	                if (tag.equals("Started") || tag.equals("Ends") || tag.equals("Time"))
	                       { 
							 	Date parsed = format.parse(r.getString(tag));
					            value = newformat.format(parsed);
								xml += value;
							}
	                else
	                        xml += escape(r.getString(tag));
	                xml += "</" + tag + ">\n";
	                return xml;
	        }
	private String escape(String s) {
		                return s.replaceAll("\"", "quot;").replaceAll("\'", "&apos;").replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
		        }
	
	
	public String echo(String message) {
		return message;
	}
    
}
