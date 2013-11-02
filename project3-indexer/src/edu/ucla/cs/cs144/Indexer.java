package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
 
    public void rebuildIndexes()throws SQLException
	{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}

	
	Statement stmt = conn.createStatement();
	Statement stmt1 = conn.createStatement();
	
	String itemId = "";	
	String name = "";
	String description = "";
	String fullSearchableText = "";
	String category = ""; //from item category table
	
	ResultSet rs = stmt.executeQuery("select * from item");

//	String index_directory = System.getenv("LUCENE_INDEX");
//	IndexWriter indexWriter = new IndexWriter( index_directory + "/index1", new StandardAnalyzer(), true);
//	Document doc = new Document();


	while(rs.next()){
		fullSearchableText = "";
		itemId = rs.getString("item_id");
		name = rs.getString ("name");
		description = rs.getString ("description");		
		
		ResultSet rs_category = stmt1.executeQuery("select * from id_category where item_id =" + itemId);
	
		while(rs_category.next())
		{

			itemId = rs_category.getString("item_id");
			category = rs_category.getString("category");
			//doc.add(new Field("category", category, Field.Store.YES, Field.Index.TOKENIZED));
			fullSearchableText = fullSearchableText+" "+category+" ";
		}

		fullSearchableText = fullSearchableText + name + " "+ description+" ";
	
		rs_category.close();
		
   	} 


	rs.close();
	stmt.close();
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
   
	}
	
    public static void main(String args[]) throws SQLException{
        Indexer idx = new Indexer();
		try{
			idx.rebuildIndexes();
			}
		catch(SQLException ex){
		            System.out.println("SQLException caught");
		            System.out.println("---");
		            while ( ex != null ){
		                System.out.println("Message   : " + ex.getMessage());
		                System.out.println("SQLState  : " + ex.getSQLState());
		                System.out.println("ErrorCode : " + ex.getErrorCode());
		                System.out.println("---");
		                ex = ex.getNextException();
		            }
		       }
		
    }   
}
