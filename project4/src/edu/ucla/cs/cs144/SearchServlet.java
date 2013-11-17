package edu.ucla.cs.cs144;

import java.io.IOException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchServlet extends HttpServlet implements Servlet {
       
    public SearchServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
		String q = request.getAttribute("q");
		String numResultsToSkip = request.getAttribute("numResultsToSkip");
		String numResultsToReturn = request.getAttribute("numResultsToReturn");
		int int_numResultsToSkip = 0;
		int int_numResultsToReturn = 0;
		if (numberResultsToSkip != null)
			int_numResultsToSkip = Integer.valueOf(numberResultsToSkip);
		if(numberResultsToReturn !=null)
			int_numResultsToReturn = Integer.valueOf(numberResultsToReturn);
			
		SearchResult[] results = AuctionSearchClient.basicSearch(q, int_numResultsToSkip, int_numResultsToReturn);
        request.setAttribute("results",results);
		
		request.getRequestDispatcher("./search.jsp").forward(request, response);
    }
}
