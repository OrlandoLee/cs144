<html>
<head>
<meta name="viewport" content="width=device-width">
	<%@ page import="edu.ucla.cs.cs144.*" %>
	<% SearchResult[] rs = (SearchResult[])request.getAttribute("results"); %>
	<%
   String query = request.getParameter("q"); 
   String numResultsToSkipString = request.getParameter("numResultsToSkip");
   int numResultsToSkip = 0;
   if (numResultsToSkipString != null)
       numResultsToSkip = Integer.valueOf(numResultsToSkipString);
   String numResultsToReturnString = request.getParameter("numResultsToReturn");
   int numResultsToReturn = 10;
   if (numResultsToReturnString != null)
       numResultsToReturn = Integer.valueOf(numResultsToReturnString); %>
</head>
<body>
	<form method="Get" ACTION = "./search">
		<br>
		Key word:
		<input type ="text" name="q">
		<input type ="hidden" name="numResultsToSkip" value="0">
		<br>
		<input type ="hidden" name="numResultsToReturn" value="10">
		<input type="submit" name="search">
	</form>
	
	
	<%for (SearchResult r : rs) { %>
	  name: <%=r.getName()%>
	  itemId:<a href ="/eBay/item?id=<%=r.getItemId()%>"> <%=r.getItemId()%></a>
	<br>
	<% }%>
	
	<% if (numResultsToSkip > 0) { %>
	<a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numResultsToSkip - 10 >= 0 ? numResultsToSkip - 10 : 0 %>&numResultsToReturn=10">Prev</a>
	<% } 
	if (rs.length == 10) {%>
	<a href="/eBay/search?q=<%= query %>&numResultsToSkip=<%= numResultsToSkip + 10 %>&numResultsToReturn=10">Next</a>
	<% } %>
</body>
</html>
