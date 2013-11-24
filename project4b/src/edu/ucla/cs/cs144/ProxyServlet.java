package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
	
        String query = request.getParameter("q");
		URLConnection connection = new URL("http://google.com/complete/search?output=toolbar&q=" + query).openConnection();
		//connection.setRequestProperty("Accept-Charset", charset);
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String xml="";
		String temp="";
		while((temp = in.readLine()) != null)
			xml+=temp;
		in.close();
		//response.setAttribute("result",xml);
		request.setAttribute("result",xml);
		//response.setContentType("text/xml");
		request.getRequestDispatcher("./suggest.jsp").forward(request, response);
		
    }
}
