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
		String q = request.getParameter("q");
		String numResultsToSkip = request.getParameter("numResultsToSkip");
		String numResultsToReturn = request.getParameter("numResultsToReturn");
		int int_numResultsToSkip = 0;
		int int_numResultsToReturn = 0;
		if (numResultsToSkip != null)
			int_numResultsToSkip = Integer.valueOf(numResultsToSkip);
		if(numResultsToReturn !=null)
			int_numResultsToReturn = Integer.valueOf(numResultsToReturn);
	
		SearchResult[] results = AuctionSearchClient.basicSearch(q, int_numResultsToSkip, int_numResultsToReturn);
		request.setAttribute("results",results);
		
		request.getRequestDispatcher("./search.jsp").forward(request, response);
    }
}
