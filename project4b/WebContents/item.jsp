<html>
<head>
	<%@ page import="java.util.*" %>
	<%@ page import="edu.ucla.cs.cs144.*" %>
	<%Item item = (Item) request.getAttribute("item");
	String itemId = item.getitemId();
    String name = item.getname();
    String sellerID= item.getsellerID();
    String sellerRating= item.getsellerRating();
    String sellerLocation= item.getsellerLocation() ;
    String sellerCountry= item.getsellerCountry();
    String description= item.getdescription();
    String firstBid= item.getfirstBid() ;
    String buyPrice= item.getbuyPrice();
    Date startTime= item.getStartTime();
    Date endTime= item.getEndTime();
    Bid[] bidArray= item.getBids();
    String[] categoryArray= item.getCategories();
	%>
	
	<script type="text/javascript" 
	    src="http://maps.google.com/maps/api/js?sensor=false"> 
	</script> 
	<script type="text/javascript"> 
	<!--
	  function initialize() { 
		var geocoder = new google.maps.Geocoder();
	    var latlng = new google.maps.LatLng(34.063509,-118.44541); 
	    var myOptions = { 
	      zoom: 1, // default is 8  
	      center: latlng, 
	      mapTypeId: google.maps.MapTypeId.ROADMAP 
	    }; 
	    var map = new google.maps.Map(document.getElementById("map_canvas"), 
	        myOptions); 
		var address = "<%=sellerLocation%> , <%=sellerCountry%>"//Los Angeles succeeded
		geocoder.geocode( { 'address': address}, function(results, status) {
		      if (status == google.maps.GeocoderStatus.OK) {
		        map.setCenter(results[0].geometry.location);
				map.setZoom(14);
		      } else {
		        map.setZoom(1);
		      }
		    });
	  } 
	-->
	</script>
</head>
<body onload="initialize()"> 
  <div id="map_canvas" style="width:100%; height:50%"></div> 
	<form method="Get" ACTION = "./item">
		<br>
		itemID:
		<input type ="text" name="id">
		<input type="submit" name="search">
	</form>
	
	<%=name%><br/>
	Seller: <%=sellerID%>(<%=sellerRating%>)<br/>
	<%=sellerLocation%>,<%=sellerCountry%><br/>
	Current Price: <%= bidArray.length > 0 ? bidArray[bidArray.length-1].getbidAmount() : firstBid %><br/>
	false
        Buy Now Price: <%= buyPrice %><br/>

        Start Time: <%= startTime %><br/>
        End Time: <%= endTime %><br/>
        <br/>
		 Categories:
	        <table>
	            <% for (String category : categoryArray) { %>
	            <tr><td><%= category %></td></tr>
	            <% } %>
	        </table><br/>
        Description:
		<%= description %><br/>
        <br/>
 		<%= bidArray.length %> Bid<% if (bidArray.length != 1) { %>s<% } %><% if (bidArray.length > 0) { %><br/>
			<table border="1px solid blue" cellpadding="0" cellspacing="0">
	            <tr><td>Bidder</td><td>Bid Time</td><td>Bid Amount</td></tr>
	            <% for (Bid bid : bidArray) { %>
	            <tr><td><%= bid.getbidderID() %> (<%= bid.getbidderRating() %>)</td><td><%= bid.getTime() %></td><td><%= bid.getbidAmount() %></td></tr> 
	            <% } %>
	        </table>
	        <% } %>
</body>
</html>
