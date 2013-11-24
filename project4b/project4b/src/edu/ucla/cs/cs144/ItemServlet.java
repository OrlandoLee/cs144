package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import org.xml.sax.InputSource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;


public class ItemServlet extends HttpServlet implements Servlet {
       
    public ItemServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
	try
	{
		String itemId = request.getParameter("id");
		String xml = AuctionSearchClient.getXMLDataForItemId(itemId);
		
		
    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);      
        DocumentBuilder builder = factory.newDocumentBuilder();
		StringReader reader = new StringReader(xml);
	    Document doc = builder.parse(new InputSource(reader));
	    Element itemElement = doc.getDocumentElement();		
		
		
		String name = getElementTextByTagNameNR(itemElement,"Name");
		String sellerLocation = getElementTextByTagNameNR(itemElement,"Location");
		String sellerCountry = getElementTextByTagNameNR(itemElement,"Country");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMM-dd-yy HH:mm:ss");
		String startTimeString = getElementTextByTagNameNR(itemElement, "Started");
		Date startTime = dateFormat.parse(startTimeString);
		String endTimeString = getElementTextByTagNameNR(itemElement, "Ends");
    	Date endTime = dateFormat.parse(endTimeString);
		
		String description = getElementTextByTagNameNR(itemElement, "Description");
		
		String firstBid = getElementTextByTagNameNR(itemElement, "First_Bid");
		String buyPrice = getElementTextByTagNameNR(itemElement, "Buy_Price");
		
		//seller
		Element sellerElement = getElementByTagNameNR(itemElement, "Seller");
		String sellerID = sellerElement.getAttribute("UserID");
	    String sellerRating = sellerElement.getAttribute("Rating");
	
	    Element categoryElements[] = getElementsByTagNameNR(itemElement,"Category");
		String[] categoryArray = new String[categoryElements.length];
		
		int i=0;
		for (Element category : categoryElements)
		{
			String categoryName = getElementText(category);
			categoryArray[i++] = categoryName;
		}
		Arrays.sort(categoryArray, String.CASE_INSENSITIVE_ORDER);
		
		// Parse bids
		Element bidsParent = getElementByTagNameNR(itemElement, "Bids");
	    Element bidElements[] = getElementsByTagNameNR(bidsParent, "Bid");
		Bid[] bidArray = new Bid[bidElements.length];//implement later
		
		int j=0;
		for(Element bidElement : bidElements)
		{
			String bidTimeString = getElementTextByTagNameNR(bidElement, "Time");
		    Date bidTime = dateFormat.parse(bidTimeString);
		    String bidAmount = getElementTextByTagNameNR(bidElement, "Amount");
			
			Element bidderElement = getElementByTagNameNR(bidElement, "Bidder");
			String bidderID = bidderElement.getAttribute("UserID");
			String bidderRating = bidderElement.getAttribute("Rating");
			String bidderLocation = getElementTextByTagNameNR(bidderElement, "Location");
			String bidderCountry = getElementTextByTagNameNR(bidderElement, "Country");	
			
			Bid bid = new Bid(bidTime, bidAmount, bidderID, bidderRating, bidderLocation, bidderCountry);
			bidArray[j++] = bid;	    
		}
		
		Arrays.sort(bidArray, Bid.BidTimeComparator);
		
		Item item = new Item(itemId, name, sellerID, sellerRating, sellerLocation, sellerCountry, description, firstBid, buyPrice, startTime, endTime, bidArray, categoryArray);
    	
    	request.setAttribute("item", item);
    	request.getRequestDispatcher("/item.jsp").forward(request, response);

		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {}
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
}
