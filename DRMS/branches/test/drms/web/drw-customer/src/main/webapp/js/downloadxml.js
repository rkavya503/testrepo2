

//<![CDATA[
      // this variable will collect the html which will eventually be placed in the side_bar 
      var side_bar_html = ""; 
    
      // arrays to hold copies of the markers and html used by the side_bar 
      // because the function closure trick doesnt work there 
      var gmarkers = []; 

     // global "map" variable
      var map = null;
	  var markersArray = [];
	  var service;
	  var geocoder;
	  var inputLat;
	  var inputLong;
	  var distanceCheck;
// A function to create the marker and set up the event window function 
function createMarker(latlng, name, html) {
    var contentString = html;
    var marker = new google.maps.Marker({
        position: latlng,
        map: map,
        zIndex: Math.round(latlng.lat()*-100000)<<5
        });
	marker.setIcon('images/greenMarker.png');
    google.maps.event.addListener(marker, 'click', function() {
        infowindow.setContent(contentString); 
        infowindow.open(map,marker);
        });
		
    // save the info we need to use later for the side_bar
    gmarkers.push(marker);
    // add a line to the side_bar html
   // side_bar_html += '<a href="javascript:myclick(' + (gmarkers.length-1) + ')">' + name + '<\/a><br>';
}
 
// This function picks up the click and opens the corresponding info window
function myclick(i) {
  google.maps.event.trigger(gmarkers[i], "click");
}

function initialize() {
	
	geocoder = new google.maps.Geocoder();
  // create the map
  var myOptions = {
    zoom: 4,
    center: new google.maps.LatLng(37.6922361, -97.3375448),
    mapTypeControl: true,
    mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
    navigationControl: true,
    mapTypeId: google.maps.MapTypeId.ROADMAP
  }
  map = new google.maps.Map(document.getElementById("map_canvas"),
                                myOptions);
 
  google.maps.event.addListener(map, 'click', function() {
        infowindow.close();
		
        });
		
		}
function ReadXML(){
	//alert(markersArray.length);
	
	$('#listHolder').remove(); 
	$('#pageWrapper').append('<div id="listHolder"></div>');
	
      // Read the data from example.xml
      downloadUrl("xml/HE_markers.xml", function(doc) {
        var xmlDoc = xmlParse(doc);
		var dealerName = xmlDoc.getElementsByTagName("title");
		var dealerPhone = xmlDoc.getElementsByTagName("phone");
		var dealerAddress1 = xmlDoc.getElementsByTagName("address");
		var dealerAddress2 = xmlDoc.getElementsByTagName("address2");
		var dealerLat = xmlDoc.getElementsByTagName("latitude");
		var dealerLong = xmlDoc.getElementsByTagName("longitude");
       // alert(dealerLong[70].firstChild.nodeValue);
		var i;
		//alert(distanceCheck);
        for (i = 0; i < dealerName.length; i++) {
          // obtain the attribues of each marker
          var lat = parseFloat(dealerLat[i].firstChild.nodeValue);
          var lng = parseFloat(dealerLong[i].firstChild.nodeValue);
          var point = new google.maps.LatLng(lat,lng);
          var html = "<span id='dealerName'>"+dealerName[i].firstChild.nodeValue +"</span><br/>" +dealerPhone[i].firstChild.nodeValue +"<br/>" +dealerAddress1[i].firstChild.nodeValue +"<br/>" +dealerAddress2[i].firstChild.nodeValue;
		  var label = dealerPhone[i].firstChild.nodeValue;
          //var label = markers[i].getAttribute("label");
          // create the marker
		  
		  distanceCheck=distance(parseFloat(inputLat), parseFloat(inputLong), parseFloat(dealerLat[i].firstChild.nodeValue), parseFloat(dealerLong[i].firstChild.nodeValue));
		 if(distanceCheck <50)
		  {
			 
		 $('#listHolder').append('<span id="listTitle">'+dealerName[i].firstChild.nodeValue+'</span><br /><span id="listDescription">'+dealerPhone[i].firstChild.nodeValue+'<br />'+dealerAddress1[i].firstChild.nodeValue+'<br />'+dealerAddress2[i].firstChild.nodeValue+'<br /><br /></span>');	  
          var marker = createMarker(point,label,html);
		  //marker.setIcon('images/greenMarker.png');
		  markersArray.push(marker);

		  }
		// alert(i);
		 //var marker = createMarker(point,label,html);
		 
		  
        }
		
        // put the assembled side_bar_html contents into the side_bar div
        //document.getElementById("side_bar").innerHTML = side_bar_html;
      });
}
// Deletes all markers in the array by removing references to them
function deleteOverlays() {
  if (markersArray) {
    for (i in markersArray) {
      markersArray[i].setMap(null);
    }
    markersArray.length = 0;
  }
}    
 
var infowindow = new google.maps.InfoWindow(
  { 
    size: new google.maps.Size(150,50)
  });
    




function createXmlHttpRequest() {
 try {
   if (typeof ActiveXObject != 'undefined') {
     return new ActiveXObject('Microsoft.XMLHTTP');
   } else if (window["XMLHttpRequest"]) {
     return new XMLHttpRequest();
   }
 } catch (e) {
   changeStatus(e);
 }
 return null;
};

/**
* This functions wraps XMLHttpRequest open/send function.
* It lets you specify a URL and will call the callback if
* it gets a status code of 200.
* @param {String} url The URL to retrieve
* @param {Function} callback The function to call once retrieved.
*/
function downloadUrl(url, callback) {
 var status = -1;
 var request = createXmlHttpRequest();
 if (!request) {
   return false;
 }

 request.onreadystatechange = function() {
   if (request.readyState == 4) {
     try {
       status = request.status;
     } catch (e) {
       // Usually indicates request timed out in FF.
     }
     if ((status == 200) || (status == 0)) {
       callback(request.responseText, request.status);
       request.onreadystatechange = function() {};
     }
   }
 }
 request.open('GET', url, true);
 try {
   request.send(null);
 } catch (e) {
   changeStatus(e);
 }
};

/**
 * Parses the given XML string and returns the parsed document in a
 * DOM data structure. This function will return an empty DOM node if
 * XML parsing is not supported in this browser.
 * @param {string} str XML string.
 * @return {Element|Document} DOM.
 */
function xmlParse(str) {
  if (typeof ActiveXObject != 'undefined' && typeof GetObject != 'undefined') {
    var doc = new ActiveXObject('Microsoft.XMLDOM');
    doc.loadXML(str);
    return doc;
  }

  if (typeof DOMParser != 'undefined') {
    return (new DOMParser()).parseFromString(str, 'text/xml');
  }

  return createElement('div', null);
}

/**
 * Appends a JavaScript file to the page.
 * @param {string} url
 */
function downloadScript(url) {
  var script = document.createElement('script');
  script.src = url;
  document.body.appendChild(script);
}
function codeAddress() {
	
		zipcode();
    var address = document.getElementById("address").value;
    geocoder.geocode( { 'address': address}, function(results, status) {
    if (status == google.maps.GeocoderStatus.OK) {
		//alert(results[1].geometry.location);
		var indexOfComa= parseFloat(String(results[0].geometry.location).indexOf(',',0));
		var indexOfLastParenthesis= parseFloat(String(results[0].geometry.location).indexOf(')',0));
		inputLat = String(results[0].geometry.location).slice(1,indexOfComa);
		inputLong = String(results[0].geometry.location).slice(indexOfComa+1,indexOfLastParenthesis);
		//alert(inputLat);
        map.setCenter(results[0].geometry.location);
		map.setZoom(12);
		
		//ReadXML();
		//alert(results[1].geometry.location);
        var marker = new google.maps.Marker({
            map: map,
            position: results[0].geometry.location
			
			
        });
		marker.setIcon('images/greenMarker.png');
		
      } else {
        alert("Geocode was not successful for the following reason: " + status);
      }
    });
	
}
function validateForm()
{
	var numericExpression = /^[0-9]+$/;
	if(document.getElementById("address").value.match(numericExpression)){
	if(document.getElementById("address").value.length == 0){
		alert("Please Enter zip code");
		document.getElementById("address").focus();
		document.getElementById("address").value=""; // set the focus to this input
		return 0;
		
	}
	else if(document.getElementById("address").value.length < 5){
		alert("Please Enter a valid zip code");
		document.getElementById("address").focus();
		document.getElementById("address").value=""; // set the focus to this input
		return 0;
		
	}
	
	else {
		return 1;
	}
	
	}
	else
	{
		alert("Please Enter a valid zip code");
		document.getElementById("address").focus();
		document.getElementById("address").value="";
		return 0;
	}
	}
function distance(lat1,lon1,lat2,lon2) {
	
    var R = 3958; // km (change this constant to get miles)
    var dLat = (lat2-lat1) * Math.PI / 180;
    var dLon = (lon2-lon1) * Math.PI / 180;
    var a = Math.sin(dLat/2) * Math.sin(dLat/2) +
        Math.cos(lat1 * Math.PI / 180 ) * Math.cos(lat2 * Math.PI / 180 ) *
        Math.sin(dLon/2) * Math.sin(dLon/2);
    var c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    var d = R * c;
	
  /*  if (d>1) return Math.round(d)+"km";
    else if (d<=1) return Math.round(d*1000)+"m";*/
    return d;
	
}