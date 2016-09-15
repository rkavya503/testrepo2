<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.drw.util.DRWUtil" %>
<%@ page import="java.util.Date" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<f:view>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Map View - Demand Response Event Status</title>
<link href="css/poputemplate_2013_v1.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite_2013_v1.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="<%=DRWUtil.getSystemManager().getPss2Properties().getGmapURL()%>?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false"></script>
<script type="text/javascript" src="js/geoxml3_v1.js"></script>
<script type="text/javascript" src="js/progressBar.js"></script>

<script type="text/javascript">
  var $j=jQuery.noConflict();
    
    if (typeof String.prototype.startsWith != 'function') {
    	  // see below for better implementation!
    	  String.prototype.startsWith = function (str){
    	    return this.indexOf(str) == 0;
    	  };
    }
  
    var drawingHoleEnable = <%=DRWUtil.getSystemManager().getPss2Properties().getGmapDrawingholeEnable()%>;
    var infowindow;
    var firstCome =0;   
    var geoXml = null;
    var request;
    var service;
    var map = null;
    var pb = null;
    var lb = null; // data load process button
    var geocoder = null;
    var toggleState = 1;
    var infowindow = null;
    var marker = null;
    var geoXmlDoc = null;
    var keyword;
    var markersArray = [];    
    var existPolygons = [];
    var cur = 0;
    var lastSdprTimeStamp = <%=new Date().getTime()%>;
    var lastSdpcTimeStamp = <%=new Date().getTime()%>;
    var lastApiTimeStamp =  <%=new Date().getTime()%>;
	var lastBipTimeStamp =  <%=new Date().getTime()%>;
	
	var lastLegendTimeStamp =  <%=new Date().getTime()%>;
    var defaultBounds = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
	var active = false;
    function createMyPolygon(placemark) {
        // Overlay handling code goes here
        if (1==1) {
            cur = cur+1;
        //  alert(cur);
            pb.updateBar(1);
            return;
            //geoXml.createPolygon(placemark);
            //geoXml.createOverlay(placemark);
        }
      };
    
    function initialize() {
		// clear exist map
    	existPolygons = [];
    	if(geoXmlDoc && geoXmlDoc.gpolygons){
    	    for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
    	    	geoXmlDoc.gpolygons[i].setMap(null);     
    	    }
    	    geoXmlDoc.gpolygons = [];
    	}
    	//	
        geocoder = new google.maps.Geocoder();
        infowindow = new google.maps.InfoWindow({size: new google.maps.Size(150,50) }); 
        var latlng = new google.maps.LatLng(35.0139, -117.7033);

        var myOptions = {
            zoom: 10,
            center: latlng,
            mapTypeControl: true,
            mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
            navigationControl: true,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        map = new google.maps.Map(document.getElementById("map"),
            myOptions);
        map.fitBounds(defaultBounds);
            
        pb = new progressBar();
        map.controls[google.maps.ControlPosition.RIGHT].push(pb.getDiv());
        
        
        lb = new progressBar();
        map.controls[google.maps.ControlPosition.RIGHT].push(lb.getDiv());
        lb.start(1);
        downloadUrl("getMapData.ajax", function(data) {
			
            geoXml = new geoXML3.parser({
                    map: map,
                    singleInfoWindow: true,
                    createPolygon:  createMyPolygon,
                    drawingHoles: drawingHoleEnable,
                  // createOverlay: addMyOverlay,
                   // createMarker: createMyPolygon,
                    afterParse: useTheData
                    });
            cur = 0;        
            var xml = data.responseText;
            var result = xml.split("^", 2);
            lb.hide();
            pb.start(result[0]);        
            geoXml.parseKmlString(result[1]);
        
        });//end of downloadUrl
    }//end of initialize method

    function showAddress(address) {
        var contentString = "Outside Area";
        geocoder.geocode( { 'address': address}, function(results, status) {

        if (status == google.maps.GeocoderStatus.OK) {
            var point = results[0].geometry.location;
            map.setCenter(point);
            if (marker && marker.setMap) marker.setMap(null);
              marker = new google.maps.Marker({
                  map: map, 
                  position: point
              });
            for (var i=0; i<geoXml.docs[0].gpolygons.length; i++) {
              if (geoXml.docs[0].gpolygons[i].Contains(point)) {
                contentString = address+"<br>is in District "+geoXml.docs[0].placemarks[i].name;
                contentString += "<br>"+point+"<br>polygon#"+i;
                break;
              }
            }
            google.maps.event.addListener(marker, 'click', function() {
            infowindow.setContent(contentString); 
            infowindow.open(map,marker);
            });
            google.maps.event.trigger(marker,"click");
        } else {
            alert("Geocode was not successful for the following reason: " + status);
          }
        });
    }// end of show address

    function kmlShowPoly(obj,formElement, poly, eventKey,panelId) {
	

	
	//poly is no longer useful, need to set identifier for each event
		if("ALL"==eventKey){
			//erase polygons on the screen
			var bounds = new google.maps.LatLngBounds();
         	var gpolygons  = [];
			for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {
			 //title category+eventId, if id=all, remove all polys in this category
	         	if (geoXmlDoc.gpolygons[i].title.startsWith(poly)) {
	                geoXmlDoc.gpolygons[i].setMap(null);            
	            } else {
	            	bounds.union(geoXmlDoc.gpolygons[i].bounds);  
	            	gpolygons.push(geoXmlDoc.gpolygons[i]);             
	            }
	            //alert(geoXmlDoc.gpolygons[i].getMap());
	         }//end for loop
	         
			map.fitBounds(bounds);
	        geoXmlDoc.gpolygons = [];
	        geoXmlDoc.gpolygons = gpolygons;
	        if(geoXmlDoc.gpolygons.length<1){
	            // no any polygon
	            map.fitBounds(defaultBounds);
	        }
		}
	
	
        var addPolygon = obj.checked;
        var params= "";
        if("sdp-r"==formElement||"sdp-r-s"==formElement){     
			if(addPolygon){
				
				if(document.getElementById("mapform:sdp-r-s")){
					document.getElementById("mapform:sdp-r-s").checked =addPolygon;
				}
				
				if(document.getElementById("mapform:sdp-r")){
					document.getElementById("mapform:sdp-r").checked =addPolygon;
				}
				
			}
			if(document.getElementById("mapform:sdp-r")){
			params="sdpr="+document.getElementById("mapform:sdp-r").checked
            		+"&eventKey="+eventKey;
			}else{
			params="sdpr="+document.getElementById("mapform:sdp-r-s").checked
            		+"&eventKey="+eventKey;
			}
            
            lastSdprTimeStamp = <%=new Date().getTime()%>;
        }else if("sdp-c"==formElement||"sdp-c-s"==formElement){   
			if(addPolygon){
				if(document.getElementById("mapform:sdp-c-s")){
					document.getElementById("mapform:sdp-c-s").checked = addPolygon;
				}
				
				if(document.getElementById("mapform:sdp-c")){
					document.getElementById("mapform:sdp-c").checked = addPolygon;
				}
			}
			if(document.getElementById("mapform:sdp-c")){
			 params="sdpc="+document.getElementById("mapform:sdp-c").checked
            +"&eventKey="+eventKey;
			}else{
			 params="sdpc="+document.getElementById("mapform:sdp-c-s").checked
            +"&eventKey="+eventKey;
			}
           
            lastSdpcTimeStamp = <%=new Date().getTime()%>;
        }else if("api"==formElement||"api-s"==formElement){    
			if(addPolygon){
				if(document.getElementById("mapform:api-s")){
				document.getElementById("mapform:api-s").checked =addPolygon; 		
				}
				
				if(document.getElementById("mapform:api")){
				document.getElementById("mapform:api").checked =addPolygon; 		
				}
			}   
			if(document.getElementById("mapform:api")){
			params="api="+document.getElementById("mapform:api").checked
            +"&eventKey="+eventKey;
			}else{
			params="api="+document.getElementById("mapform:api-s").checked
            +"&eventKey="+eventKey;
			}
            
            lastApiTimeStamp =  <%=new Date().getTime()%>;
        }else if("bip"==formElement||"bip-s"==formElement){      
			if(addPolygon){
				if(document.getElementById("mapform:bip-s")){
					document.getElementById("mapform:bip-s").checked =addPolygon;
				}
				if(document.getElementById("mapform:bip")){
					document.getElementById("mapform:bip").checked =addPolygon;
				}
			}	
			if(document.getElementById("mapform:bip")){
			params="bip="+document.getElementById("mapform:bip").checked
            +"&eventKey="+eventKey;
			}else{
			params="bip="+document.getElementById("mapform:bip-s").checked
            +"&eventKey="+eventKey;
			}			
            
            lastBipTimeStamp =  <%=new Date().getTime()%>;
        }else if("cbp"==formElement||"cbp-s"==formElement){      
			if(addPolygon){
				if(document.getElementById("mapform:cbp-s")){
					document.getElementById("mapform:cbp-s").checked =addPolygon;
				}
				
				if(document.getElementById("mapform:cbp")){
					document.getElementById("mapform:cbp").checked =addPolygon;
				}
			}	
			if(document.getElementById("mapform:cbp")){
			params="cbp="+document.getElementById("mapform:cbp").checked
            +"&eventKey="+eventKey;
			}else{
			params="cbp="+document.getElementById("mapform:cbp-s").checked
            +"&eventKey="+eventKey;
			}			
            
            lastBipTimeStamp =  <%=new Date().getTime()%>;
        }
              
        if(addPolygon){
            oneProductChange(params);
        }else{
         var bounds = new google.maps.LatLngBounds();
         var gpolygons  = [];
         for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {
		 //title category+eventId, if id=all, remove all polys in this category
         if (geoXmlDoc.gpolygons[i].title == (poly+":"+eventKey)) {
                geoXmlDoc.gpolygons[i].setMap(null);            
            } else {
             bounds.union(geoXmlDoc.gpolygons[i].bounds);  
             gpolygons.push(geoXmlDoc.gpolygons[i]);             
            }
            //alert(geoXmlDoc.gpolygons[i].getMap());
         }//end for loop
        map.fitBounds(bounds);
        geoXmlDoc.gpolygons = [];
        geoXmlDoc.gpolygons = gpolygons;
        if(geoXmlDoc.gpolygons.length<1){
            // no any polygon
            map.fitBounds(defaultBounds);
        }
        }
        return;
    
    }
    
     function oneProductChange(params) {
        existPolygons = resetMap();
        cur = 0;
        lb.start(1);
        pb.hide();
        
        downloadUrl("getMapData.ajax", function(data) {         
        geoXml = new geoXML3.parser({
                map: map,
                singleInfoWindow: true,
                drawingHoles: drawingHoleEnable,
                createPolygon:  createMyPolygon,
                afterParse: useTheData
            });
        lb.hide();
        
        var xml = data.responseText;
        
        var result = xml.split("^", 2);

        pb.start(result[0]);
        geoXml.parseKmlString(result[1]);
        
        },params);     
    }

    function useTheData(doc){
      // Geodata handling goes here, using JSON properties of the doc object
      geoXmlDoc = doc[0];
      pb.hide();
      if(existPolygons.length>0){        
        //alert(existPolygons);   
         for (var i=0;i<existPolygons.length;i++) {      
           geoXmlDoc.gpolygons.push(existPolygons[i]);     
         }
         var bounds = new google.maps.LatLngBounds();
         for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
            if(!geoXmlDoc.gpolygons[i].getMap()){
                geoXmlDoc.gpolygons[i].setMap(map);                         
            }
            bounds.union(geoXmlDoc.gpolygons[i].bounds);    
         }
         map.fitBounds(bounds);
      }
      if(geoXmlDoc.gpolygons.length<1){
        // no any polygon
        var latlng = new google.maps.LatLng(35.0139, -117.7033);
        map.setCenter(latlng);
      }
      //enable all checkboxes
	  enableAllCheckBox();
      //var sdpr = document.getElementById("mapform:sdp-r");
      //var sdpc = document.getElementById("mapform:sdp-c");
     // var api = document.getElementById("mapform:api");  
	 // var bip = document.getElementById("mapform:bip");        
     // sdpr.disabled = false;
     // sdpc.disabled = false;
     // api.disabled = false;       
	 // bip.disabled = false;       
    //  alert(cur);
    //var latlng = new google.maps.LatLng(35.0139, -117.7033);
    // map.setCenter(marker.getPosition());
    };

    function hide_markers_kml(){
        geoXml.hideDocument();  
    }

    function unhide_markers_kml(){
        geoXml.showDocument();  
    }
    
	function isActive(){
		var sels = document.getElementsByName('mapform:eventStateSelectRadio');
		var activeStatus;
		for(i=0;i<sels.length;i++){
			if(sels[i].checked){
			activeStatus =sels[i].value;
			break;
			}
		}
		var active;
		if(activeStatus=="ActiveEvents"){
		active=true;
		}else{
		active=false;
		} 
		
		return active;
	
	}
    
    function downloadUrl(url, callback, param) {
    //1.disable all check box before ajax request sended
    //2. will be enabled after map rendering	
		var sels = document.getElementsByName('mapform:eventStateSelectRadio');
		var activeStatus;
		
		var active = isActive();
       
        
		if(document.getElementById("mapform:sdp-r")){
			var sdpr = document.getElementById("mapform:sdp-r");
		}else{
			var sdpr = document.getElementById("mapform:sdp-r-s");
		}
        
		if(document.getElementById("mapform:sdp-c")){
		var sdpc = document.getElementById("mapform:sdp-c");
		}else{
		var sdpc = document.getElementById("mapform:sdp-c-s");
		}
        
		
		if(document.getElementById("mapform:api")){
		var api = document.getElementById("mapform:api");
		}else{
		var api = document.getElementById("mapform:api-s");
		}
		
		if(document.getElementById("mapform:bip")){
		var bip = document.getElementById("mapform:bip");
		}else{
		var bip = document.getElementById("mapform:bip-s");
		}
		
		if(document.getElementById("mapform:cbp")){
		var cbp = document.getElementById("mapform:cbp");
		}else{
		var cbp = document.getElementById("mapform:cbp-s");
		}
		//alert(bip);
        var params="";   
		var eventKeys="";		
		
        if(!param){
            params = "sdpr="+sdpr.checked+"&sdpc="+sdpc.checked+"&api="+api.checked+"&bip="+bip.checked+"&cbp="+cbp.checked;
			
			var c = document.getElementById('mapform').getElementsByTagName('input');
		for (var i = 0; i < c.length; i++) {
			//&&c[i].checked&&c[i].id!="mapform:sdp-r"&&c[i].id!="mapform:sdp-c"&&c[i].id!="mapform:api"&&c[i].id!="mapform:bip"
			if (c[i].type == 'checkbox'&&c[i].checked&&!c[i].id.startsWith("mapform:sdp-r")&&!c[i].id.startsWith("mapform:sdp-c")&&!c[i].id.startsWith("mapform:api")&&!c[i].id.startsWith("mapform:bip") &&!c[i].id.startsWith("mapform:cbp") ) {
					var id = c[i].id+"-value";
					if(document.getElementById(id)){
						if(eventKeys==""){
							eventKeys = document.getElementById(id).value;						
						}else{
							eventKeys = eventKeys+","+document.getElementById(id).value;						
						}					    					
					}
			}
		}
			params = params+"&eventKey="+eventKeys;						
			//params = "sdpr="+sdpr.checked+"&sdpc="+sdpc.checked+"&api="+api.checked;
        }else{
        	
            params = param;
        }
		params = params+"&active="+active;
		disableAllCheckBox();
        //sdpr.disabled = true;
       // sdpc.disabled = true;
       // api.disabled = true;
		//bip.disabled = true;
        
        var request = window.ActiveXObject ?
            new ActiveXObject('Microsoft.XMLHTTP') :
            new XMLHttpRequest;

        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                request.onreadystatechange = doNothing;
                callback(request, request.status);
            }
        };

        request.open('POST', url, true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");

        request.send(params)      
    }// end of downloadUrl

    function doNothing() {}

    function productChange() {
        //resetMap();
        cur = 0;
        lb.start(1);
        pb.hide();
        downloadUrl("getMapData.ajax", function(data) {         
        geoXml = new geoXML3.parser({
                map: map,
                singleInfoWindow: true,
                drawingHoles: drawingHoleEnable,
                createPolygon:  createMyPolygon,
                afterParse: useTheData
            });
        lb.hide();
        
        var xml = data.responseText;
        
        var result = xml.split("^", 2);
        pb.start(result[0]);
        geoXml.parseKmlString(result[1]);
        
        });     
    }

    function resetMap() {
        var polygons = geoXmlDoc.gpolygons;
       for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
           geoXmlDoc.gpolygons[i].setMap(null);     
       }
       geoXmlDoc.gpolygons  = [];
       
       return polygons;
    }
	
	function callCodeAddress(event){
		if((event||window.event).keyCode==13) {			
			codeAddress(event);
		}		
	}

    function codeAddress(event) {
		var theEvent = event||window.event||arguments.callee.caller.arguments[0]; 		
        Event.stop(theEvent);
		//theEvent.stopPropagation();
    
        clearOverlays();
        address = document.getElementById("mapform:address").value;
        keyword=address;
        
        if($j.trim(keyword)=="") return;//don't send any request, just clear the current markers
        
        geocoder.geocode( { 'address': address}, function(results, status) {

        if (status == google.maps.GeocoderStatus.OK) {
            //alert(results[1].geometry.location);
            var indexOfComa= parseFloat(String(results[0].geometry.location).indexOf(',',0));
            var indexOfLastParenthesis= parseFloat(String(results[0].geometry.location).indexOf(')',0));
            inputLat = String(results[0].geometry.location).slice(1,indexOfComa);
            inputLong = String(results[0].geometry.location).slice(indexOfComa+1,indexOfLastParenthesis);
            address=results[0].geometry.location;
            map.setCenter(results[0].geometry.location);
            map.setZoom(11);
            // callSearch();
            //ReadXML();
            //alert(results[1].geometry.location);      
            marker = new google.maps.Marker({
                map: map,
                position: results[0].geometry.location                          
            });         
            
            marker.setIcon('images/icons/home.png');
            markersArray.push(marker);
        } else {
            alert("Geocode was not successful for the following reason: " + status);
        }
        });
    }

    function clearOverlays() {
      if (markersArray) {
        for (var i = 0; i < markersArray.length; i++ ) {
          markersArray[i].setMap(null);
        }
      }
    }
    
    //*****periodcal refresh codes segment
    var t;  
    var timer_is_on=0;
    function startTimer(){
       if (!timer_is_on)
         {
             timer_is_on=1;
             setTimeout("timedCount()",<%=DRWUtil.getSystemManager().getPss2Properties().getDrCacheInterval()%>); // run timeCount() function in 10 s     
         }
    }
    function timedCount()
    {
        monitorCache();
        t=setTimeout("timedCount()",<%=DRWUtil.getSystemManager().getPss2Properties().getDrCacheInterval()%>); // refresh intervel 10 sec
    }
    
    //**** monitor cache changes codes:
    function monitorCache() {
    	  var active = isActive();
          var params ;
          if(active){
        	  params= "active=true";
          }else{
        	  params= "active=false";
          }
          
          
        
          downloadUrl("getMapData.legendmonitor", function(data) {
          //enable check box
		  enableAllCheckBox();
          //sdpr.disabled = false;
          //sdpc.disabled = false;
          //api.disabled = false;
		 // bip.disabled = false;
          
          var xml = data.responseText;           
          var legend_time  = xml;         
          if((legend_time > lastLegendTimeStamp)){
              lastLegendTimeStamp = legend_time;
              document.getElementById("mapform:refreshButton").onclick();              
          }
        
          },params);//end of downloadUjrl
      }//end of initialize method
      
      function clearPolygon(polyTitle){
          for(var j=0;j<polyTitle.length;j++){

              var poly = polyTitle[j];
              var gpolygons  = [];
              for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {          
                  if (geoXmlDoc.gpolygons[i].title == poly) {
                         geoXmlDoc.gpolygons[i].setMap(null);                                             
                     }else{
                        gpolygons.push(geoXmlDoc.gpolygons[i]);
                     }
                     //alert(geoXmlDoc.gpolygons[i].getMap());
               }//end for inner loop
               geoXmlDoc.gpolygons = [];
               geoXmlDoc.gpolygons = gpolygons;   
          }//end of outter loop
         
          
      }
    function accessibilityForTab(){
		var obj = document.getElementById("mapform:address");
		if(obj){
			obj.focus();
		}
		//ie
		event.cancelBubble=true;
		event.returnValue = false;
	 }
	 function initFocus(){
		// document.getElementById("tabLink").focus();
	 }
	 
	 function closeWindow(){
		if(window.opener==null){
			if(navigator.appVersion.indexOf("MSIE") != -1){
				 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
				if (version == "9" || version == "8" || version == "7") { 
						window.open('', '_self', ''); 
						window.close(); 
				} else if (version == "6") { 
						window.close();
				} else { 
						
						window.close(); 
				} 
			}else{
				window.open('', '_self', '');
				window.close();
			}
		}else{
			if(navigator.appVersion.indexOf("MSIE") != -1){
				 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
				if (version == "9" || version == "8" || version == "7") { 
						window.open('', '_self', ''); 
						window.close(); 
				} else if (version == "6") { 
						window.opener = null; 
						window.close();
				} else { 
					
						window.opener = ''; 
						window.close(); 
				} 
			}else{
			
					window.opener = null; 
					window.open('', '_self', '');					
					window.close();
			}
		}
		
	 }
	 
	 function checkAllHandler(obj,from,poly){
		
		var classname = obj.id;
		var elements = document.getElementsByClassName(classname);
		
		for(i=0;i<elements.length;i++){
			elements[i].checked=obj.checked;
		}
		
		kmlShowPoly(obj,from,poly,'ALL');
	 
	 }
	 
	 function disableAllCheckBox() {

		var c = document.getElementById('mapform').getElementsByTagName('input');
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].disabled = true;
			}
		}
	}	
	function enableAllCheckBox() {
	 
		var c = document.getElementById('mapform').getElementsByTagName('input');
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].disabled = false;
			}
		}
	}	
	
	function checkboxCheck(panelId,checkBoxId){
		if(panelId){
			var c = document.getElementById(checkBoxId);
			var allSubCheckboxs = document.getElementById(panelId).getElementsByTagName('input');
			var checkFlag=false;
			for (var i = 0; i < allSubCheckboxs.length; i++) {
				if (allSubCheckboxs[i].type == 'checkbox') {
					if(allSubCheckboxs[i].checked){
						checkFlag = true;
					}
				}
			}
			if(!checkFlag){
				c.checked = false;
			}
		}
	}
</script>

<style type="text/css">

</style>
</head>
<body onload="initialize();startTimer();initFocus();">
<center>


<h:form id="mapform">
<a4j:keepAlive beanName="drMapView"/>
<div id="popupWrapper">
  <div class="popupHeader">
    <h1><a href="#" title="Southern California Edison">Southern California Edison</a></h1>
  </div>
  <div class="innerWrapper">
  	<p style="font-family:Arial, Helvetica, sans-serif; font-size:14px; font-weight:normal; line-height:18px; padding-left:10px; width:420px;">This map shows Demand Response events occuring now (Map may not reflect all programs.) Please enter your street address and city, or ZIP code to see if you are within an impacted area.</p>
    <div class="contentWrapper">
      <div id="tabViewWrapper">
          <ul id="tabView" role="tablist">
          <li id="tabView1"  role="tab">
                 <h:commandLink id="tabLink" action="#{drMapView.dispatchToMapViewPage}"  styleClass="on" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}">
                    <h2> Map View </h2>
                </h:commandLink>
            </li>
            <li id="tabView2"  role="tab">
                <h:commandLink action="#{drMapView.dispatchToListViewPage}" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}">
                    <h2> List View </h2>
                </h:commandLink>
            </li>
        </ul>
      </div>
      <div id="tabContentPanel">
      	<div class="radioSelectionsBG">
        	<div class="radioHolder">
				<h:selectOneRadio value="#{drMapView.eventStateSelect}"  id="eventStateSelectRadio"  styleClass="radio-buttons" style="vertical-align: middle; margin: 0px;">
					<a4j:support event="onclick" onsubmit="disableAllCheckBox()" oncomplete="enableAllCheckBox();initialize();" action="#{drMapView.eventSwitchListener}" reRender="legendPanel,buttonArea"  ajaxSingle="true"/>
					<f:selectItem itemValue="ActiveEvents" itemLabel="Active Events" id="activeEventsSelectItem" />
					<f:selectItem itemValue="ScheduledEvents" itemLabel="Scheduled Events" id="scheduledEventsSelectItem" />
				</h:selectOneRadio>
			</div>
        </div>
      	<div class="searchMapWrapper">
			<div style="padding-bottom:5px; font-family:Arial, Helvetica, sans-serif; font-size:12px;">Search the map</div>
			<h:inputText id="address" value="" styleClass="mapaddress" onkeydown="callCodeAddress(event);" />
				<h:commandButton  id="FilterButton" style="float:none;vertical-align:top;" 
                                title="Search" 
                                alt="Search" 
                                onclick="codeAddress(event);document.getElementById('mapform:address').focus();"
                                image="images/icons/searchBtn.jpg">
            
				</h:commandButton>
        </div>
        <div id="map"></div>
        
        
                
      </div>
    </div>
<h:panelGroup id="legendPanel" layout="block" >	
    <div id="sideBar">
      <div class="sideHeaderBg">Legend</div>
      <div class="sideMiddleBg">
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">
                <h:selectBooleanCheckbox value="#{drMapView.activeEventLegend.sdprSelected}" id="sdp-r" rendered="#{drMapView.displayActiveFlag}" >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'sdp-r','SDP-R')"  />
                </h:selectBooleanCheckbox>
				
				<h:selectBooleanCheckbox value="#{drMapView.scheEventLegend.sdprSelected}" id="sdp-r-s" rendered="#{!drMapView.displayActiveFlag}" >
                    <a4j:support event="onclick" onsubmit="checkAllHandler(this,'sdp-r','SDP-R')"  />
                </h:selectBooleanCheckbox>
		  </div>
          <div class="sicon"><img src="images/icons/sV2.gif" alt="Summer discount plan for residential" align="absmiddle" /></div>
          <div class="sFtext">Summer Discount Plan (SDP) - Residential</div>
          <div class="activeCheckBoxesHolder">
			<h:panelGroup id="SDPREventsLegendPanel" layout="block" rendered="#{drMapView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drMapView.activeEventLegend.sdprItems}" rows="100">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-rdetail" styleClass="mapform:sdp-r" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'sdp-r','SDP-R','#{event.eventKey}','mapform:SDPREventsLegendPanel');" oncomplete="checkboxCheck('mapform:SDPREventsLegendPanel','mapform:sdp-r')"  reRender="navigateLink">
						</a4j:support>						
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-rdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
          </div>
          <div class="activeCheckBoxesHolder">
          	
			<h:panelGroup id="scheSDPREventsLegendPanel" layout="block" rendered="#{!drMapView.displayActiveFlag}" >
				<rich:dataList var="event" value="#{drMapView.scheEventLegend.sdprItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox value="#{event.selected}" id="sdp-rdetail" styleClass="mapform:sdp-r-s" style="vertical-align: middle; margin: 0px; margin-right:10px;">
						<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'sdp-r-s','SDP-R','#{event.eventKey}','mapform:scheSDPREventsLegendPanel');" oncomplete="checkboxCheck('mapform:scheSDPREventsLegendPanel','mapform:sdp-r-s')" reRender="navigateLink">
						</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-rdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>
          </div>
        </div>
         <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">
		  <h:selectBooleanCheckbox value="#{drMapView.activeEventLegend.sdpcSelected}" id="sdp-c" rendered="#{drMapView.displayActiveFlag}" >
              <a4j:support event="onclick"  onsubmit="checkAllHandler(this,'sdp-c','SDP-C')" />
          </h:selectBooleanCheckbox>
		  
		  <h:selectBooleanCheckbox value="#{drMapView.scheEventLegend.sdpcSelected}" id="sdp-c-s" rendered="#{!drMapView.displayActiveFlag}" >
              <a4j:support event="onclick"  onsubmit="checkAllHandler(this,'sdp-c','SDP-C')" />
          </h:selectBooleanCheckbox>
		  </div>
          <div class="sicon"><img src="images/icons/s_p.gif" alt="S" align="absmiddle" /></div>
          <div class="sFtext">Summer Discount Plan (SDP) - Commercial</div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="SDPCEventsLegendPanel" layout="block" rendered="#{drMapView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drMapView.activeEventLegend.sdpcItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;" value="#{event.selected}" id="sdp-cdetail" styleClass="mapform:sdp-c">
						<a4j:support event="onclick" onsubmit="kmlShowPoly(this,'sdp-c','SDP-C','#{event.eventKey}','mapform:SDPCEventsLegendPanel');" oncomplete="checkboxCheck('mapform:SDPCEventsLegendPanel','mapform:sdp-c')" reRender="SDPCPanel,listViewLegendPanel">							
						</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-cdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>	
          </div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="scheSDPCEventsLegendPanel" layout="block" rendered="#{!drMapView.displayActiveFlag}">
				<rich:dataList var="event" value="#{drMapView.scheEventLegend.sdpcItems}" rows="100" style="list-style-type:none;">
					<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;" value="#{event.selected}" id="sdp-cdetail" styleClass="mapform:sdp-c-s">
						<a4j:support event="onclick" onsubmit="kmlShowPoly(this,'sdp-c-s','SDP-C','#{event.eventKey}','mapform:scheSDPCEventsLegendPanel');" oncomplete="checkboxCheck('mapform:scheSDPCEventsLegendPanel','mapform:sdp-c-s')" reRender="SDPCPanel,listViewLegendPanel">							
						</a4j:support>	
					</h:selectBooleanCheckbox>
					<h:inputHidden id="sdp-cdetail-value" value="#{event.eventKey}" />
					<h:outputText  value="#{event.label}"></h:outputText>
				</rich:dataList>
			</h:panelGroup>	
          </div>
        </div>
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">
		   <h:selectBooleanCheckbox id="api" value="#{drMapView.activeEventLegend.apiSelected}" rendered="#{drMapView.displayActiveFlag}" >
               <a4j:support event="onclick" onsubmit="checkAllHandler(this,'api','API')" reRender="navigateLink" />
               </h:selectBooleanCheckbox>
			   
			<h:selectBooleanCheckbox id="api-s" value="#{drMapView.scheEventLegend.apiSelected}" rendered="#{!drMapView.displayActiveFlag}" >
               <a4j:support event="onclick" onsubmit="checkAllHandler(this,'api-s','API')" reRender="navigateLink" />
               </h:selectBooleanCheckbox>
		  
		  </div>
          <div class="sicon"><img src="images/icons/a.gif" alt="A" align="absmiddle" /></div>
          <div class="sFtext">Agricultural &amp; Pumping Interruptible Program (AP-I)</div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="APIEventsLegendPanel" layout="block" rendered="#{drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.activeEventLegend.apiItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox style="vertical-align: middle; margin: 0px; margin-right:10px;" value="#{event.selected}" id="api-rdetail" styleClass="mapform:api">
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'api','API','#{event.eventKey}','mapform:APIEventsLegendPanel');"  oncomplete="checkboxCheck('mapform:APIEventsLegendPanel','mapform:api')" reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="scheAPIEventsLegendPanel" layout="block" rendered="#{!drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.scheEventLegend.apiItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox  style="vertical-align: middle; margin: 0px; margin-right:10px;" value="#{event.selected}" id="api-rdetail" styleClass="mapform:api-s">
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'api','API','#{event.eventKey}','mapform:scheAPIEventsLegendPanel');" oncomplete="checkboxCheck('mapform:scheAPIEventsLegendPanel','mapform:api-s')"   reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="api-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
        </div>
        
        <!-- -->									
		 <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">
          	   <h:selectBooleanCheckbox id="cbp" value="#{drMapView.activeEventLegend.cbpSelected}" rendered="#{drMapView.displayActiveFlag}" >
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'cbp','CBP')"  reRender="navigateLink"/>
               </h:selectBooleanCheckbox>	

				<h:selectBooleanCheckbox id="cbp-s" value="#{drMapView.scheEventLegend.cbpSelected}" rendered="#{!drMapView.displayActiveFlag}">
					<a4j:support event="onclick" onsubmit="checkAllHandler(this,'cbp','CBP')"  reRender="navigateLink"/>
               </h:selectBooleanCheckbox>			   
		  </div>
          <div class="sicon"><img src="images/icons/c.gif" alt="C" align="absmiddle" /></div>
          <div class="sFtext">Capacity Bidding Program (CBP)</div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="CBPEventsLegendPanel" layout="block" rendered="#{drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.activeEventLegend.cbpItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="cbp-rdetail" styleClass="mapform:cbp" style="vertical-align: middle; margin: 0px; margin-right:10px;" >
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'cbp','CBP','#{event.eventKey}','mapform:CBPEventsLegendPanel');" oncomplete="checkboxCheck('mapform:CBPEventsLegendPanel','mapform:cbp')"  reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="cbp-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="scheCBPEventsLegendPanel" layout="block" rendered="#{!drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.scheEventLegend.cbpItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="cbp-rdetail" styleClass="mapform:cbp-s" style="vertical-align: middle; margin: 0px; margin-right:10px;" >
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'cbp-s','CBP','#{event.eventKey}','mapform:scheCBPEventsLegendPanel');" oncomplete="checkboxCheck('mapform:scheCBPEventsLegendPanel','mapform:cbp-s')"  reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="cbp-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
        </div>
        
        <!--  -->
		
       
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">
		       <h:selectBooleanCheckbox id="bip" value="#{drMapView.activeEventLegend.bipSelected}" rendered="#{drMapView.displayActiveFlag}" >
               <a4j:support event="onclick" onsubmit="checkAllHandler(this,'bip','BIP')" reRender="navigateLink" />
               </h:selectBooleanCheckbox>
			   
			   <h:selectBooleanCheckbox id="bip-s" value="#{drMapView.scheEventLegend.bipSelected}" rendered="#{!drMapView.displayActiveFlag}" >
               <a4j:support event="onclick" onsubmit="checkAllHandler(this,'bip','BIP')" reRender="navigateLink" />
               </h:selectBooleanCheckbox>
		  
		  </div>
          <div class="sicon"><img src="images/icons/b2013.gif" alt="B" align="absmiddle" /></div>
          <div class="sFtext">Base Interruptible Program (BIP)</div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="BIPEventsLegendPanel" layout="block" rendered="#{drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.activeEventLegend.bipItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="bip-rdetail" styleClass="mapform:bip" style="vertical-align: middle; margin: 0px; margin-right:10px;" >
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'bip','BIP','#{event.eventKey}','mapform:BIPEventsLegendPanel');"  
oncomplete="checkboxCheck('mapform:BIPEventsLegendPanel','mapform:bip')"	reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="bip-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
          <div class="activeCheckBoxesHolder">
          	<h:panelGroup id="scheBIPEventsLegendPanel" layout="block" rendered="#{!drMapView.displayActiveFlag}">
					<rich:dataList var="event" value="#{drMapView.scheEventLegend.bipItems}" rows="100" style="list-style-type:none;">
						<h:selectBooleanCheckbox value="#{event.selected}" id="bip-rdetail" styleClass="mapform:bip-s" style="vertical-align: middle; margin: 0px; margin-right:10px;" >
							<a4j:support event="onclick"  onsubmit="kmlShowPoly(this,'bip-s','BIP','#{event.eventKey}','mapform:scheBIPEventsLegendPanel');" oncomplete="checkboxCheck('mapform:scheBIPEventsLegendPanel','mapform:bip-s')"  reRender="navigateLink">
							</a4j:support>	
						</h:selectBooleanCheckbox>
						<h:inputHidden id="bip-rdetail-value" value="#{event.eventKey}" />
						<h:outputText  value="#{event.label}"></h:outputText>
					</rich:dataList>
				</h:panelGroup>	
          </div>
        </div>
        <!--
        
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox"><input type="checkbox" align="absmiddle" /></div>
          <div class="sicon"><img src="images/icons/d.gif" alt="D" align="absmiddle" /></div>
          <div class="sFtext">Demand Bidding Program (DBP)</div>
        </div>
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox"><input type="checkbox" align="absmiddle" /></div>
          <div class="sicon"><img src="images/icons/r.gif" alt="R" align="absmiddle" /></div>
          <div class="sFtext">Demand Response Contracts (DRC)</div>
        </div>
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox"><input type="checkbox" align="absmiddle" /></div>
          <div class="sicon"><img src="images/icons/i.gif" alt="I" align="absmiddle" /></div>
          <div class="sFtext">Summer Advantage Incentive (SAI)</div>
        </div>-->
        <div class="sideFilterItemMapview">
          <div class="sideFCheckbox">&nbsp;</div>
          <div class="sicon"><img src="images/icons/home.png" alt="Home" align="absmiddle" /></div>
          <div class="sFtext" style="padding-top:5px;">Your Location</div>
        </div>
        
        <div class="clearfix"></div>
      </div>
      <div class="sideFooterBg"></div>
    </div>
</h:panelGroup>
  </div>
  <div class="clearfix"></div>
  <div class="FooterLegalWrapper">
    <p>Shaded figures in the map above show an approximation of an event area. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.</p>
  </div>
  <br/>
	<a title="close" style="cursor: pointer;float:right" onclick="closeWindow()">Close</a>
	<br/>
	<br/>
  <script type="text/javascript">
		$j('#address').keypress(function(event){
	 
		var keycode = (event.keyCode ? event.keyCode : event.which);
		if(keycode == '13'){
			codeAddress();
		}
		event.stopPropagation();
	});
	</script>
</div>
</center>
<!--[if IE 7]>
<style>
#tabContentPanel{margin-top:-19px;}
#tabViewWrapper {margin-left:-40px;}
.activeCheckBoxesHolder ul{	margin-left:25px; padding-top:0px;}
.activeCheckBoxesHolder ul{	margin-left:25px; padding-top:0px;}
</style>
<![endif]-->

<a4j:commandButton id="refreshButton" style="display:none;">
	<a4j:support event='onclick' oncomplete="initialize();" actionListener="#{drMapView.refreshLegend}"
	 reRender="legendPanel">
			 
	 </a4j:support>
</a4j:commandButton>

</h:form>
</body>
</f:view>
</html>