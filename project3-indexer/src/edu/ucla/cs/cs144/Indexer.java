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
 
    public void rebuildIndexes()throws org.apache.lucene.index.CorruptIndexException, org.apache.lucene.store.LockObtainFailedException, java.io.IOException
	{

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}

	
	Statement stmt = null;
	try{
	    stmt = conn.createStatement();
	}catch (SQLException ex){
	    System.out.println(ex);
	}
	String itemId = "";	
	String name = "";
	String discription = "";
	String fullSearchableText = "";
	String category = ""; //from item category table
	
	ResultSet rs = null;
	try{
	    rs = stmt.executeQuery("select * from item");
	}catch (SQLException ex){
	    System.out.println(ex.getMessage());
	}
	
	ResultSet rs_category = null;
	
	String index_directory = System.getenv("LUCENE_INDEX");
	IndexWriter indexWriter = new IndexWriter( index_directory + "/index1", new StandardAnalyzer(), true);
	Document doc = new Document();

	try{
	while(rs.next()){
		
		fullSearchableText = "";
		
		try{
		itemId = rs.getString("item_id");
		name = rs.getString ("name");
		discription = rs.getString ("description");
		}catch (SQLException ex){
		    System.out.println(ex);
		}
		try
		{
		while(rs_category.next())
		{
		
			category = rs_category.getString("category");
			doc.add(new Field("category", category, Field.Store.YES, Field.Index.TOKENIZED));
			fullSearchableText = fullSearchableText+category+" ";
		}
		}catch(SQLException ex)
		{
			System.out.println(ex);
		}
		
		fullSearchableText = fullSearchableText + name + " "+ discription;
		System.out.println(fullSearchableText);
		doc.add(new Field("itemId", itemId, Field.Store.YES, Field.Index.NO ));
		doc.add(new Field("name", name, Field.Store.YES, Field.Index.TOKENIZED ));
		doc.add(new Field("description", discription, Field.Store.YES, Field.Index.TOKENIZED ));
		doc.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED ));
		indexWriter.addDocument(doc);
		System.out.println(itemId + ","+fullSearchableText);
	}
	}catch(SQLException ex)
	{
	    System.out.println(ex);
	}
	indexWriter.close();
	/*
	 * Add your code here to retrieve Items using the connection
	 * and add corresponding entries to your Lucene inverted indexes.
         *
         * You will have to use JDBC API to retrieve MySQL data from Java.
         * Read our tutorial on JDBC if you do not know how to use JDBC.
         *
         * You will also have to use Lucene IndexWriter and Document
         * classes to create an index and populate it with Items data.
         * Read our tutorial on Lucene as well if you don't know how.
         *
         * As part of this development, you may want to add 
         * new methods and create additional Java classes. 
         * If you create new classes, make sure that
         * the classes become part of "edu.ucla.cs.cs144" package
         * and place your class source files at src/edu/ucla/cs/cs144/.
	 * 
	 */


        // close the database connection
	try {
	    conn.close();
	} catch (SQLException ex) {
	    System.out.println(ex);
	}
    }    

    public static void main(String args[]) {
        Indexer idx = new Indexer();
        try
	{idx.rebuildIndexes();}
	catch (Exception ex){
	    System.out.println(ex);
	}
    }   
}
