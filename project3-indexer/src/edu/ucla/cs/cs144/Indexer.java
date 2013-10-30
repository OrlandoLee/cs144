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
 
    public void rebuildIndexes() {

        Connection conn = null;

        // create a connection to the database to retrieve Items from MySQL
	try {
	    conn = DbManager.getConnection(true);
	} catch (SQLException ex) {
	    System.out.println(ex);
	}


	Statment stmt = conn.createStatement();
	
	String itemId = "";	
	String name = "";
	String discription = "";
	String fullSearchableText = "";
	String category = ""; //from item category table
	
	
	ResultSet rs = stmt.executeQuery("select * from item");
	ResultSet rs_category;
	
	String index_directory = System.getenv("LUCENE_INDEX");
	IndexWriter indexWriter = new IndexWriter( index_directory + "/index1", new StandartAnalyzer(), true);
	Document doc = new Document();

	while(rs.next()){
		
		fullSearchableString = "";
		
		itemId = rs.getString("item_id");
		name = rs.getString ("name");
		discription = rs.getString ("description");
		
		rs_category = stmt.executeQuery("select * from id_category where item_id = "+itemId);//?????""
		while(rs_category.next())
		{
			category = rs_category.getString("category");
			doc.add(new Field("category", category, Field.Store.YES, Filed.Index.YES));
			fullSearchableString+ = category+" ";
		}
		
		fullSearchableString+ = name + " "+ discription;
		doc.add(new Field("itemId", itemId, Field.Store.YES, Filed.Index.NO ));
		doc.add(new Field("name", name, Field.Store.YES, Filed.Index.TOKENIZED ));
		doc.add(new Field("description", discription, Field.Store.YES, Filed.Index.TOKENIZED ));
		doc.add(new Field("content", fullSearchableText, Field.Store.NO, Filed.Index.TOKENIZED ));
		writer.addDocument(doc);
		System.out.println(itemId + ","+fullSearchableText);
	}
	


	
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
        idx.rebuildIndexes();
    }   
}
