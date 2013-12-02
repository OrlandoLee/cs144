<html>
<head>
<%@ page import="java.util.*" %>
<%@ page import="edu.ucla.cs.cs144.*" %>
</head>
<body>
<br>ItemID:  <%=session.getAttribute("itemId")%> </br>
<br>ItemName: <%=session.getAttribute("name")%></br>
<br>Buy Price: <%=session.getAttribute("buyPrice")%></br>
<br>
<%String secureURL = "https://"+request.getServerName()+":8443"+request. getContextPath()+"/display.jsp";%>
<form method="Post" Action=<%=secureURL%>>
Credit Card: <input type="text" name="creditCard">
<input type="submit" value="submit">
</form>
</br>
</body>
</html>
