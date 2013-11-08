package edu.ucla.cs.cs144;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.io.IOException;

import org.apache.lucene.document.Field;
import org.apache.lucene.document.Document;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class Indexer {
    
    /** Creates a new instance of Indexer */
    public Indexer() {
    }
    private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            indexWriter = new IndexWriter(System.getenv("LUCENE_INDEX") + "/index1",
                                          new StandardAnalyzer(),
                                          create);
        }
        return indexWriter;
    }
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
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
        ResultSet rs_category = stmt1.executeQuery("select * from id_category");
        HashMap<String, String> category_map = new HashMap<String, String>();
        
        while(rs_category.next())
        {
            string key = rs_category.getString("item_id");
            if (category_map.containsKey(key))
            {
                category_map.put(key, category_map.get(key) + " " + rs_category.getString("category"));
            }
            else
            {
                category_map.put(key, rs_category.getString("category"));
                
            }
            
        }
        rs_category.close();
        
        
        int count = 0;
        
        try{
            getIndexWriter(true);
            while(rs.next()){
                IndexWriter writer = getIndexWriter(false);
                
                Document doc = new Document();
                fullSearchableText = "";
                itemId = rs.getString("item_id");
                System.out.println(count++);
                //System.out.println(itemId);
                name = rs.getString ("name");
                description = rs.getString ("description");
                
                
                doc.add(new Field("itemId", itemId, Field.Store.YES, Field.Index.NO ));
                doc.add(new Field("name", name, Field.Store.YES, Field.Index.TOKENIZED ));
                doc.add(new Field("description", description, Field.Store.YES, Field.Index.TOKENIZED ));
                
                category = category_map.get(itemId);
                
                doc.add(new Field("category", category, Field.Store.YES, Field.Index.TOKENIZED));
                fullSearchableText = name + " "+ description+" " + category;
                doc.add(new Field("content", fullSearchableText, Field.Store.NO, Field.Index.TOKENIZED ));
                
                writer.addDocument(doc);
                
            }
            
            
            
            rs.close();
            stmt.close();
            stmt1.close();
            
            closeIndexWriter();
        }catch (Exception e)
        {
            System.out.println("Exception caught.\n");
        }
        
        
        
        
        
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
	}
	
    public static void main(String args[]){
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