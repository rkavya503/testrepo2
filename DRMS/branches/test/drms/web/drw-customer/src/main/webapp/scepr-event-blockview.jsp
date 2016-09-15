<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.drw.util.DRWUtil" %>
<%@ page import="java.util.Date" %>


<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<f:view>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!--<meta http-equiv="Refresh" content="10;scepr-event-listview.jsf" />-->
<title>SCE Block View - Demand Response Event Status</title>
<link href="css/poputemplate2013.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="<%=DRWUtil.getSystemManager().getPss2Properties().getGmapURL()%>?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false"></script>
<!-- <script type="text/javascript" src="http://geoxml3.googlecode.com/svn/branches/polys/geoxml3.js"></script> -->
<script type="text/javascript" src="js/geoxml3_v1.js"></script>
<script type="text/javascript" src="js/progressBar.js"></script>

<script type="text/javascript">
	var $j=jQuery.noConflict();
	var drawingHoleEnable = <%=DRWUtil.getSystemManager().getPss2Properties().getGmapDrawingholeEnable()%>;
    var firstCome =0;   
    var geoXml = null;
    var request;
    var service;
    var map = null;
    var pb = null;
    var lb = null; // data load process button
    var geocoder = null;
    var toggleState = 1;
    var marker = null;
    var geoXmlDoc = null;
    var keyword;
    var markersArray = [];    
    var existPolygons = [];
    var cur = 0;
    var defaultBounds = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
	//-------------------------------------------------------------PAGE JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
  
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
		document.getElementById("tabLink").focus();
		collapseBlock(1);
		collapseBlock(2);
		collapseBlock(3);
		collapseBlock(4);
		collapseBlock(5);
		collapseBlock(6);
	 }

	function closeWindow(){
		if(navigator.appVersion.indexOf("MSIE") != -1){
			 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
			if (version == "9" || version == "8" || version == "7") { 
				if(window.opener.document!=null){
					retrieveParentFocus();
					window.open('', '_self', ''); 
					window.close(); 
				}
			} else if (version == "6") { 
				 
				if(window.opener.document!=null){
					retrieveParentFocus();
					window.opener = null; 
					window.close();
				}
			} else { 
				
				if(window.opener.document!=null){
					retrieveParentFocus();
					window.opener = ''; 
					window.close(); 
				}
			} 
		}else{
			if(window.opener.document!=null){
				retrieveParentFocus();
				window.opener = null; 	
				window.close();
			}
		}
	 }
	 
	function retrieveParentFocus(){
		if(window.opener.document.getElementById("residentialTabLink")){
					window.opener.document.getElementById("residentialTabLink").focus();
				}else if(window.opener.document.getElementById("commercialTabLink")){
					window.opener.document.getElementById("commercialTabLink").focus();
		}
	 }
	//-------------------------------------------------------------PAGE JAVA SCRIPT ZONE[E N D]-------------------------------------------------------------
	//-------------------------------------------------------------LEGEND JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
	
	function collapseBlock(blockNumber){
		switch(blockNumber){
			case 1: $j('.insideBlockViewCheckBoxesHolder1').css('display', 'none');
					$j('.block1Expand').css('display', 'block');
					$j('.block1collapse').css('display', 'none');
					break;
			case 2: $j('.insideBlockViewCheckBoxesHolder2').css('display', 'none');
					$j('.block2Expand').css('display', 'block');
					$j('.block2collapse').css('display', 'none');
					break;
			case 3: $j('.insideBlockViewCheckBoxesHolder3').css('display', 'none');
					$j('.block3Expand').css('display', 'block');
					$j('.block3collapse').css('display', 'none');
					break;
			case 4: $j('.insideBlockViewCheckBoxesHolder4').css('display', 'none');
					$j('.block4Expand').css('display', 'block');
					$j('.block4collapse').css('display', 'none');
					break;				
			case 5: $j('.insideBlockViewCheckBoxesHolder5').css('display', 'none');
					$j('.block5Expand').css('display', 'block');
					$j('.block5collapse').css('display', 'none');
					break;
			case 6: $j('.insideBlockViewCheckBoxesHolder6').css('display', 'none');
					$j('.block6Expand').css('display', 'block');
					$j('.block6collapse').css('display', 'none');
					break;						
		}
	}

	
	function expandBlock(blockNumber){
		switch(blockNumber){
			case 1: $j('.insideBlockViewCheckBoxesHolder1').css('display', 'block');
					$j('.block1Expand').css('display', 'none');
					$j('.block1collapse').css('display', 'block');
					break;
			case 2: $j('.insideBlockViewCheckBoxesHolder2').css('display', 'block');
					$j('.block2Expand').css('display', 'none');
					$j('.block2collapse').css('display', 'block');
					break;
			case 3: $j('.insideBlockViewCheckBoxesHolder3').css('display', 'block');
					$j('.block3Expand').css('display', 'none');
					$j('.block3collapse').css('display', 'block');
					break;
			case 4: $j('.insideBlockViewCheckBoxesHolder4').css('display', 'block');
					$j('.block4Expand').css('display', 'none');
					$j('.block4collapse').css('display', 'block');
					break;
			case 5: $j('.insideBlockViewCheckBoxesHolder5').css('display', 'block');
					$j('.block5Expand').css('display', 'none');
					$j('.block5collapse').css('display', 'block');
					break;
			case 6: $j('.insideBlockViewCheckBoxesHolder6').css('display', 'block');
					$j('.block6Expand').css('display', 'none');
					$j('.block6collapse').css('display', 'block');
					break;							
		}
	}
	function disableAllCheckBox(elementId) {
		var c;
		if(elementId){
			c = document.getElementById(elementId).getElementsByTagName('input');
		}else{
			c = document.getElementById('mapform').getElementsByTagName('input');
		}
		
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].disabled = true;
			}
		}
	}	
	function enableAllCheckBox(elementId) {
		var c;
		if(elementId){
			c = document.getElementById(elementId).getElementsByTagName('input');
		}else{
			c = document.getElementById('mapform').getElementsByTagName('input');
		}
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].disabled = false;
			}
		}
	}
	function selectAllCheckBox(elementId) {
		var c;
		if(elementId){
			c = document.getElementById(elementId).getElementsByTagName('input');
		}else{
			c = document.getElementById('mapform').getElementsByTagName('input');
		}
		
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].checked = true;
			}
		}
	}	
	function selectNoneCheckBox(elementId) {
		var c;
		if(elementId){
			c = document.getElementById(elementId).getElementsByTagName('input');
		}else{
			c = document.getElementById('mapform').getElementsByTagName('input');
		}
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].checked = false;
			}
		}
	}	
	//-------------------------------------------------------------LEGEND JAVA SCRIPT ZONE[E N D]-------------------------------------------------------------
	//-------------------------------------------------------------MAP  JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
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
			
			$j("#yourhomeicon").css("display", "block");
			document.getElementById("mapform:address").value="";
        } else {
            alert("Geocode was not successful for the following reason: " + status);
        }
        });
    }
	function initialize() {
        geocoder = new google.maps.Geocoder(); 
        var latlng = new google.maps.LatLng(35.0139, -117.7033);

        var myOptions = {
            zoom: 10,
            center: latlng,
            mapTypeControl: true,
            mapTypeControlOptions: {style: google.maps.MapTypeControlStyle.DROPDOWN_MENU},
            navigationControl: true,
            mapTypeId: google.maps.MapTypeId.ROADMAP
        }
        map = new google.maps.Map(document.getElementById("map"),myOptions);
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
                    afterParse: useTheData
                    });
            cur = 0;        
            var xml = data.responseText;
			//alert(xml);
            var result = xml.split("^", 2);
            lb.hide();
            pb.start(result[0]);    		
            geoXml.parseKmlString(result[1]);
        
        });//end of downloadUrl
    }//end of initialize method
	function doNothing(){
	
	}
	function downloadUrl(url, callback, param) {
    //1.disable all check box before ajax request sended
    //2. will be enabled after map rendering
        var params="";        
        if(!param){
            //params ="blockFlag=true&SCEC=true";
			params="";
        }else{
            params = param;
        }
        var request = window.ActiveXObject ? new ActiveXObject('Microsoft.XMLHTTP') : new XMLHttpRequest;

        request.onreadystatechange = function() {
            if (request.readyState == 4) {
                request.onreadystatechange = doNothing;
                callback(request, request.status);
            }
        };
        request.open('POST', url, true);
        request.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
        request.send(params);  
		disableAllCheckBox();		
    }// end of downloadUrl

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
      //var scec = document.getElementById("mapform:scecCheckBox");       
      //scec.disabled = false;       
    }

	function createMyPolygon(placemark) {
        // Overlay handling code goes here
        if (1==1) {
            cur = cur+1;
            pb.updateBar(1);
            return;
        }
    }
	function kmlShowPolyBySlab(formElement, checkBoxId, legendPanelGroupId){
		 var params= "";

				var checked = document.getElementById(checkBoxId).checked;
				
				var c = document.getElementById(legendPanelGroupId).getElementsByTagName('input');
				if(checked){
					//clear sub checkbox 
					for (var i = 0; i < c.length; i++) {
						if (c[i].type == 'checkbox') {
							if(c[i].checked){
								c[i].click();	
							}else{
								//do nothing
							}
						}
					}
					//invoke 
					params="blockFlag=true&"+formElement+"=true";
					oneProductChange(params);
					for (var i = 0; i < c.length; i++) {
						if (c[i].type == 'checkbox') {
							c[i].checked = true;
						}
					}
				}else{
					//clear---function ok
					for (var i = 0; i < c.length; i++) {
						if (c[i].type == 'checkbox') {
							if(c[i].checked){
								c[i].click();	
							}else{
								//do nothing
							}
						}
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
	function kmlShowPoly(formElement, poly) {
	
		
	
		//alert(poly);
        //alert("mapform:scecCheckBox");
        var addPolygon = false;
        var params= "";
		var isAbank = true;
		var blockList = null;
		
		if(isAbank){
			addPolygon = formElement.checked;
			var blockNumber = poly;
			poly = "BLOCK_"+poly;
			params="blockFlag=true&blockNumber="+blockNumber;
			//alert('blockNumber is '+blockNumber);
			//alert('poly is '+poly);
		}
        if(addPolygon){
            oneProductChange(params);
        }else{
		
         var bounds = new google.maps.LatLngBounds();
         var gpolygons  = [];
         for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {
         if (geoXmlDoc.gpolygons[i].title == poly) {
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
	//-------------------------------------------------------------MAP  JAVA SCRIPT ZONE[E N D]-------------------------------------------------------------
	
	
    





	

	
     function clearOverlays() {
      if (markersArray) {
        for (var i = 0; i < markersArray.length; i++ ) {
          markersArray[i].setMap(null);
        }
      }
    }

	
	
	//----------------JS for block show-----------------
	
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
	


	function resetMap() {
        var polygons = geoXmlDoc.gpolygons;
       for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
           geoXmlDoc.gpolygons[i].setMap(null);     
       }
       geoXmlDoc.gpolygons  = [];
       
       return polygons;
    }
</script>
    
</head>

<body onload="initialize();initFocus();">

<h:form id="mapform">
<div id="popupWrapper" style="margin: 0 auto;">
  <div class="popupHeader">
    <h1><a href="#" title="Southern California Edison">Southern California Edison</a></h1>
  </div>
  <div class="innerWrapper">
    <p style="font-family:Arial, Helvetica, sans-serif; font-size:14px; font-weight:normal; line-height:18px; padding-left:10px; width:420px;">Please enter your street address and city, or ZIP code and select from the Blocks on the right side to see the Block you are in.</p>
    <div class="contentWrapper">
		<div id="yourhomeicon"><img src="images/icons/home.png" alt="Home" align="absmiddle" />&nbsp;&nbsp;&nbsp;Your Location</div>
      <div id="tabViewWrapper">
		<ul id="tabViewBlock" role="tablist">
          <li id="tabView1Block" role="tab">
			<a id="tabLink" href="scepr-event-blockview.jsf"  class="on" onclick="document.getElementById('mapform:address').focus();" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}"><h2>SCE Block View</h2></a>
		  </li>
		  <li id="tabView1Block2" role="tab">
		  </li>
        </ul>
      </div>
      
    <div id="tabContentPanel" role="tabpanel">
        <div class="searchMapWrapper" style="margin-top:-20px;">
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

    
	<div id="sideBar">
		
		<jsp:include page="jsp/component/component_block_legend.jsp" />
      
      <div class="sideFooterBg"></div>
    </div>
	
	
  </div>
  <div class="clearfix"></div>
  <div class="FooterLegalWrapper">
    <p>Shaded figures in the map above show an approximation of a Block area. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.</p>
  </div>
  
	<br/>
	<a title="close" style="cursor: pointer;float:right" onclick="closeWindow()">Close</a>
	<br/>
	<br/>
</div>

</h:form>

</body>
</f:view>
</html>
