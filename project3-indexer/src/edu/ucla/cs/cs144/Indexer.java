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
 
    public void rebuildIndexes()throws SQLException, org.apache.lucene.index.CorruptIndexException, org.apache.lucene.store.LockObtainFailedException, java.io.IOException
	{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}

	
	Statement stmt = conn.createStatement();

	String itemId = "";	
	String name = "";
	String description = "";
	String fullSearchableText = "";
	String category = ""; //from item category table
	
	ResultSet rs = stmt.executeQuery("select * from item");
	ResultSet rs_category = null;
	String index_directory = System.getenv("LUCENE_INDEX");
	IndexWriter indexWriter = new IndexWriter( index_directory + "/index1", new StandardAnalyzer(), true);
	Document doc = new Document();


	while(rs.next()){
		
		fullSearchableText = "";
		itemId = rs.getString("item_id");
		name = rs.getString ("name");
		description = rs.getString ("description");
		
		System.out.println(itemId + " item_id" + name + "name" + description + "description");
    	} 
        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
   
	}
	
    public static void main(String args[]) throws SQLException{
        Indexer idx = new Indexer();
		idx.rebuildIndexes();
    }   
}
