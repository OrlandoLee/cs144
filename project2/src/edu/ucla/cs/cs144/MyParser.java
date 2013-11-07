/* CS144
 *
 * Parser skeleton for processing item-???.xml files. Must be compiled in
 * JDK 1.5 or above.
 *
 * Instructions:
 *
 * This program processes all files passed on the command line (to parse
 * an entire diectory, type "java MyParser myFiles/*.xml" at the shell).
 *
 * At the point noted below, an individual XML file has been parsed into a
 * DOM Document node. You should fill in code to process the node. Java's
 * interface for the Document Object Model (DOM) is in package
 * org.w3c.dom. The documentation is available online at
 *
 * http://java.sun.com/j2se/1.5.0/docs/api/index.html
 *
 * A tutorial of Java's XML Parsing can be found at:
 *
 * http://java.sun.com/webservices/jaxp/
 *
 * Some auxiliary methods have been written for you. You may find them
 * useful.
 */

package edu.ucla.cs.cs144;

import java.io.*;
import java.text.*;
import java.util.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.ErrorHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;



class MyParser {
    
    static final String columnSeparator = "|*|";
    static DocumentBuilder builder;
    
    static final String[] typeName = {
	"none",
	"Element",
	"Attr",
	"Text",
	"CDATA",
	"EntityRef",
	"Entity",
	"ProcInstr",
	"Comment",
	"Document",
	"DocType",
	"DocFragment",
	"Notation",
    };
    
    static class MyErrorHandler implements ErrorHandler {
        
        public void warning(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void error(SAXParseException exception)
        throws SAXException {
            fatalError(exception);
        }
        
        public void fatalError(SAXParseException exception)
        throws SAXException {
            exception.printStackTrace();
            System.out.println("There should be no errors " +
                               "in the supplied XML files.");
            System.exit(3);
        }
        
    }
    
    /* Non-recursive (NR) version of Node.getElementsByTagName(...)
     */
    static Element[] getElementsByTagNameNR(Element e, String tagName) {
        Vector< Element > elements = new Vector< Element >();
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
            {
                elements.add( (Element)child );
            }
            child = child.getNextSibling();
        }
        Element[] result = new Element[elements.size()];
        elements.copyInto(result);
        return result;
    }
    
    /* Returns the first subelement of e matching the given tagName, or
     * null if one does not exist. NR means Non-Recursive.
     */
    static Element getElementByTagNameNR(Element e, String tagName) {
        Node child = e.getFirstChild();
        while (child != null) {
            if (child instanceof Element && child.getNodeName().equals(tagName))
                return (Element) child;
            child = child.getNextSibling();
        }
        return null;
    }
    
    /* Returns the text associated with the given element (which must have
     * type #PCDATA) as child, or "" if it contains no text.
     */
    static String getElementText(Element e) {
        if (e.getChildNodes().getLength() == 1) {
            Text elementText = (Text) e.getFirstChild();
            return elementText.getNodeValue();
        }
        else
            return "";
    }
    
    /* Returns the text (#PCDATA) associated with the first subelement X
     * of e with the given tagName. If no such X exists or X contains no
     * text, "" is returned. NR means Non-Recursive.
     */
    static String getElementTextByTagNameNR(Element e, String tagName) {
        Element elem = getElementByTagNameNR(e, tagName);
        if (elem != null)
            return getElementText(elem);
        else
            return "";
    }
    
    /* Returns the amount (in XXXXX.xx format) denoted by a money-string
     * like $3,453.23. Returns the input if the input is an empty string.
     */
    static String strip(String money) {
        if (money.equals(""))
            return money;
        else {
            double am = 0.0;
            NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
            try { am = nf.parse(money).doubleValue(); }
            catch (ParseException e) {
                System.out.println("This method should work for all " +
                                   "money values you find in our data.");
                System.exit(20);
            }
            nf.setGroupingUsed(false);
            return nf.format(am).substring(1);
        }
    }

	static String preprocess(String s)
	{
		//return "\""+s.replace("\\","\\\\").replace("\"","\\\"")+"\"";
		//return "\""+s.replaceAll("\"","\\\\\"")+"\"";
		return "\""+s.replaceAll("\"","&quote;")+"\"";
	}
    
    /* Process one items-???.xml file.
     */
    static void processFile(File xmlFile) {
        Document doc = null;
        try {
            doc = builder.parse(xmlFile);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(3);
        }
        catch (SAXException e) {
            System.out.println("Parsing error on file " + xmlFile);
            System.out.println("  (not supposed to happen with supplied XML files)");
            e.printStackTrace();
            System.exit(3);
        }
        
        /* At this point 'doc' contains a DOM representation of an 'Items' XML
         * file. Use doc.getDocumentElement() to get the root Element. */
        System.out.println("Successfully parsed - " + xmlFile);

try {
	////////////////
	//file writer for user bids id_category item.
	///////////////
	File file_user =new File("user.tmp");
	File file_bids =new File("bids.dat");
	File file_id_category =new File("id_category.tmp");
	File file_item =new File("item.dat");

	if(!file_user.exists()){
	 	file_user.createNewFile();
	   }
	if(!file_bids.exists()){
	    file_bids.createNewFile();
	   }
	if(!file_id_category.exists()){
	    file_id_category.createNewFile();
	   }
	if(!file_item.exists()){
	   	file_item.createNewFile();
	   }
	PrintWriter writer_user = new PrintWriter(new FileOutputStream(file_user,true));
	PrintWriter writer_bids = new PrintWriter(new FileOutputStream(file_bids,true));
	PrintWriter writer_id_category = new PrintWriter(new FileOutputStream(file_id_category,true));
	PrintWriter writer_item = new PrintWriter(new FileOutputStream(file_item,true));
	
	
	SimpleDateFormat format = new SimpleDateFormat("MMM-dd-yy H:m:s");
	SimpleDateFormat newformat = new SimpleDateFormat("yyyy-MM-dd HH:m:s");

 	org.w3c.dom.NodeList nList = doc.getElementsByTagName("Item");

	for (int i = 0; i < nList.getLength(); i++) {
		Node nNode = nList.item(i);
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {	
					Element eElement = (Element) nNode;
					String item_id = "";
					String name = "";
					String currently = "";
					String buy_price = "0.00";
					String first_bid = "";
					String number_of_bids = "";
					String discription = "";
					String started = "";
					String ends = "";
					String seller = "";
					String location = "";
					String country = "";
					String rating = "";
					String bidder = "";
					String amount = "";
					String time = "";
					
					
				    item_id = preprocess(eElement.getAttribute("ItemID"));
				    name = preprocess(eElement.getElementsByTagName("Name").item(0).getTextContent());
				    currently = preprocess(eElement.getElementsByTagName("Currently").item(0).getTextContent().replace("$","").replace(",",""));
				    if (eElement.getElementsByTagName("Buy_Price").getLength()==1)
				    {
					buy_price = preprocess(eElement.getElementsByTagName("Buy_Price").item(0).getTextContent().replace("$","").replace(",",""));
					}
					else
					{buy_price = "0.00";}	
					first_bid = preprocess(eElement.getElementsByTagName("First_Bid").item(0).getTextContent().replace("$","").replace(",",""));
					number_of_bids = eElement.getElementsByTagName("Number_of_Bids").item(0).getTextContent();
					discription = eElement.getElementsByTagName("Description").item(0).getTextContent();
					if(discription.length() > 4000)
					{
						discription = discription.substring(0,4000);
					}				
					discription = preprocess(discription);
					started = eElement.getElementsByTagName("Started").item(0).getTextContent();
					ends = eElement.getElementsByTagName("Ends").item(0).getTextContent();
					
					Element subeElement = (Element) eElement.getElementsByTagName("Seller").item(0);	
					
					seller = preprocess(subeElement.getAttribute("UserID"));
					rating = preprocess(subeElement.getAttribute("Rating"));
                                        int countrylen = eElement.getElementsByTagName("Country").getLength();
					int locationlen = eElement.getElementsByTagName("Location").getLength();
					country = preprocess(eElement.getElementsByTagName("Country").item(countrylen - 1).getTextContent());
					location = preprocess(eElement.getElementsByTagName("Location").item(locationlen - 1).getTextContent());
					writer_user.println(seller+","+rating+","+country+","+location);
					try {
			            Date parsed = format.parse(started);
			            started = newformat.format(parsed);
			        }
			        catch(ParseException pe) {
			            System.out.println("ERROR: Cannot parse \"" + started + "\"");
			        }
					
					try {
			            Date parsed = format.parse(ends);
			            ends = newformat.format(parsed);
			        }
			        catch(ParseException pe) {
			            System.out.println("ERROR: Cannot parse \"" + ends + "\"");
			        }
					writer_item.println(item_id+","+name+","+currently+","+buy_price+","+first_bid+","+number_of_bids+","+discription+","+started+","+ends+","+seller);
				
					/*ItemID Category********************************************************************/
					
					int length = eElement.getElementsByTagName("Category").getLength();
					int temp = 0;
					String category = "";
					while(temp<length)
					{
						category = eElement.getElementsByTagName("Category").item(temp).getTextContent();
						category = preprocess(category);
						writer_id_category.println(item_id+ "," +category);
						temp++;
					}
					
		
						/*Bids********************************************************************/
						
						org.w3c.dom.NodeList bidList = getElementByTagNameNR(eElement,"Bids").getElementsByTagName("Bid");
						for (int bid_i = 0; bid_i < bidList.getLength(); bid_i++) {
								Node bid_nNode = bidList.item(bid_i);
								Element bid_eElement = (Element) bid_nNode;
								Element bid_subeElement = (Element) bid_eElement.getElementsByTagName("Bidder").item(0);
								
								time = bid_eElement.getElementsByTagName("Time").item(0).getTextContent();
								try {
						            Date parsed = format.parse(time);
						            time = newformat.format(parsed);
						        }
						        catch(ParseException pe) {
						            System.out.println("ERROR: Cannot parse \"" + time + "\"");
						        }
						bidder = bid_subeElement.getAttribute("UserID");
						bidder = preprocess(bidder);
						amount = bid_eElement.getElementsByTagName("Amount").item(0).getTextContent().replace("$","").replace(",","");
						writer_bids.println(item_id+","+bidder+","+time+","+amount);
					   			
					/*Bidder********************************************************************/ 	
					
					
						if(bid_eElement.getElementsByTagName("Country").getLength()==1)
							{
								country = bid_eElement.getElementsByTagName("Country").item(0).getTextContent();
								country = preprocess(country);
							}
						else
							country = "";
						if(bid_eElement.getElementsByTagName("Location").getLength()==1)
						    {
								location = bid_eElement.getElementsByTagName("Location").item(0).getTextContent();
								location = preprocess(location);
							}
						else
							location = "";
						rating = bid_subeElement.getAttribute("Rating");
						rating = preprocess(rating);
						writer_user.println(bidder+","+rating+","+country+","+location);
							}
							
								
				}
		}
		

		writer_user.close();
		writer_bids.close();
		writer_id_category.close();
		writer_item.close();
	} catch (IOException ex){
	  // report
	}	
        
    }
    
    public static void main (String[] args) {
        if (args.length == 0) {
            System.out.println("Usage: java MyParser [file] [file] ...");
            System.exit(1);
        }
        
        /* Initialize parser. */
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);      
            builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new MyErrorHandler());
        }
        catch (FactoryConfigurationError e) {
            System.out.println("unable to get a document builder factory");
            System.exit(2);
        } 
        catch (ParserConfigurationException e) {
            System.out.println("parser was unable to be configured");
            System.exit(2);
        }
        
        /* Process all files listed on command line. */
        for (int i = 0; i < args.length; i++) {
            File currentFile = new File(args[i]);
            processFile(currentFile);
        }
    }
}
