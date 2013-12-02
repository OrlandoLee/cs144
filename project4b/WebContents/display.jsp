<h1>Confirmation Page</h1>
<br>ItemID:  <%=session.getAttribute("itemId")%> </br>
<br>ItemName: <%=session.getAttribute("name")%></br>
<br>Buy Price: <%=session.getAttribute("buyPrice")%></br>
<br>Credit Card: <%=request.getParameter("creditCard")%></br>
<br><%= new java.util.Date() %></br>

