package edu.ucla.cs.cs144;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProxyServlet extends HttpServlet implements Servlet {
       
    public ProxyServlet() {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        // your codes here
    	String query = request.getParameter("q");
    	URL url = new URL("http://google.com/complete/search?output=toolbar&q=" + query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        int length;
		byte[] buffer = new byte[2048];
		while ((length = connection.getInputStream().read(buffer)) > 0)
        {
        	response.getOutputStream().write(buffer, 0, length);
        }
		response.setContentType("text/xml");
		connection.getInputStream().close();
		response.getOutputStream().close();
    }
}
