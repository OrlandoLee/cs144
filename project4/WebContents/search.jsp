<html>
<head>
	<%@ page import="edu.ucla.cs.cs144.*" %>
	<% SearchResult[] rs = (SearchResult[])request.getAttribute("results") %>
</head>
<body>
	<%for (SearchResult r:rs) { %>
	<%= r.getItemId()%> , <%= r.getName()%>
	<br>
	<% }%>
</body>
</html>