<!DOCTYPE html> 
<%@ page import="com.akuacom.pss2.drw.util.DRWUtil" %>
<html> 
<head> 
	<title>SCE Demand Response Event Status</title> 
	<meta name="viewport" content="width=device-width, initial-scale=1"> 
	<link rel="stylesheet" href="css/themes/default/jquery.mobile-1.3.0.css" />
    
	<script src="js/jquery.js"></script>
	<script src="js/jquery.mobile-1.3.0.js"></script>
	<script type='text/javascript' src='js/knockout-2.3.0.js'></script>

<script type="text/javascript" src="js/geoxml3_v1.js"></script>
<script type="text/javascript" src="js/progressBar.js"></script>

<script type="text/javascript">
	//unselect all checkbox 	 	
	function addScript( url, callback ) {
	    var script = document.createElement( 'script' );
	    if( callback ) script.onload = callback;
	    script.type = 'text/javascript';
	    script.src = url;
	    document.body.appendChild( script );  
	}

	function loadMapsAPI() {
	var is_chrome = navigator.userAgent.indexOf('Chrome') > -1;
	var is_explorer = navigator.userAgent.indexOf('MSIE') > -1;
	var is_firefox = navigator.userAgent.indexOf('Firefox') > -1;
	var is_safari = navigator.userAgent.indexOf("Safari") > -1;
	var is_Opera = navigator.userAgent.indexOf("Presto") > -1;
	if ((is_chrome)&&(is_safari)) {is_safari=false;}
	if(is_safari){// safari not support https
		addScript('http://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initialize' );
	}else if( window.location.protocol == "http:"){
		addScript('http://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initialize' );
	}else{
		addScript('https://maps.googleapis.com/maps/api/js?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false&callback=initialize' );
	}
	
	}

	var drawingHoleEnable = false;
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
    var defaultBounds;
	//-------------------------------------------------------------PAGE JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
  
	//-------------------------------------------------------------PAGE JAVA SCRIPT ZONE[E N D]-------------------------------------------------------------
	//-------------------------------------------------------------LEGEND JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
	
	function disableAllCheckBox(elementId) {
		
	}	
	function enableAllCheckBox(elementId) {
		
	}
	function selectAllCheckBox(elementId) {
		
	}	
	function selectNoneCheckBox(elementId) {
		
	}	
	//-------------------------------------------------------------LEGEND JAVA SCRIPT ZONE[E N D]-------------------------------------------------------------
	//-------------------------------------------------------------MAP  JAVA SCRIPT ZONE[BEING]-------------------------------------------------------------
	

	function loadScript() {
	

	
	  var script = document.createElement('script');
	  script.type = 'text/javascript';
	  script.src = 'https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=false&' +
	      'callback=initialize';
		  try{
	  document.body.appendChild(script);
	  }catch(exception){
		
		alert(exception);
		}
  }

	
	function initialize() {
		addScript('js/label.js')
        geocoder = new google.maps.Geocoder(); 
	
		defaultBounds = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
        var latlng = new google.maps.LatLng(34.050408, -118.164306);

        var myOptions = {
            zoom: 8,
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
	
	function kmlShowPoly(blockNumber, selected) {
		//alert(poly);
        //alert("mapform:scecCheckBox");
        var addPolygon = selected;
        var params= "";
		var isAbank = true;
		var blockList = null;
		var poly;
		if(isAbank){
			poly = "BLOCK_"+blockNumber;
			params="blockFlag=true&multi=false&blockNumber="+blockNumber;
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
        	// force map refresh though changing zoom level
			var zoom = map.getZoom();
			var center = map.getCenter();
			map.setZoom(zoom+2);
			map.setZoom(zoom);
			map.setCenter(center);
			
			checkCheckbox();
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
     	$('.ui-collapsible-heading-toggle').click();
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
	if(geoXmlDoc&&geoXmlDoc.gpolygons){
		var polygons = geoXmlDoc.gpolygons;
       for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
           geoXmlDoc.gpolygons[i].setMap(null);     
       }
       geoXmlDoc.gpolygons  = [];
       
       return polygons;
	}else{
	 return [];
	}
        
    }
//variable declarations

function codeAddress(event) {
        clearOverlays();
        address = document.getElementById("address").value;
        keyword=address;
        if($.trim(keyword)=="") return;//don't send any request, just clear the current markers
        
        geocoder.geocode( { 'address': address}, function(results, status) {
			if (status == google.maps.GeocoderStatus.OK) {
				var indexOfComa= parseFloat(String(results[0].geometry.location).indexOf(',',0));
				var indexOfLastParenthesis= parseFloat(String(results[0].geometry.location).indexOf(')',0));
				inputLat = String(results[0].geometry.location).slice(1,indexOfComa);
				inputLong = String(results[0].geometry.location).slice(indexOfComa+1,indexOfLastParenthesis);
				address=results[0].geometry.location;
				map.setCenter(results[0].geometry.location);
				map.setZoom(11);     
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

   // google.maps.event.addDomListener(window, 'load', initialize);
	
	function selectSlap(obj,classname){
		console.log(!!obj.checked);
		console.log(classname);
		var temp=[];
		var reset=[];
		 $('input:checkbox.'+classname).map(function () {
		 temp.push(this.value);
		 if(this.checked){
			reset.push(this.value);
		 }
		  this.checked=!!obj.checked;

		});
		console.log(temp);
		console.log(reset);
		
		for(var i=0;i<reset.length;i++){
			kmlShowPoly(reset[i],false);
		}
		var blockNumber="";
		for(var i=0;i<temp.length;i++){
			if(i>0) blockNumber += ",";
			blockNumber = blockNumber+temp[i];
		}
		if(!!obj.checked){
			var params="blockFlag=true&multi=true&blockNumber="+blockNumber;
			oneProductChange(params);			
		}
		
	}
	
	function checkboxListener(obj){
	
	console.log(!!obj.checked);
	console.log(obj.value);
	
	kmlShowPoly(obj.value,!!obj.checked);
	}
	
	function checkCheckbox(){
							//DRMS---8239
							for(key in checkboxInteractionMap){
								var checkFlag=false;
								$.each( $( "input[name='"+checkboxInteractionMap[key]+"']" ), function( index, value ){
									if(value.checked){
										checkFlag = true;
									}
								});
								if(!checkFlag){
									$("input[name='"+key+"']").prop('checked',false).checkboxradio("refresh");
								}
							}
	}	
	var checkboxInteractionMap = {
		'scecCheckbox':'scecEventCheckbox',
		'scenCheckbox':'scenEventCheckbox',
		'scnwCheckbox':'scnwEventCheckbox',
		'scewCheckbox':'scewEventCheckbox',
		'schdCheckbox':'schdEventCheckbox',
		'scldCheckbox':'scldEventCheckbox'
	};
	function callSearch()
		{
			
			clearOverlays();
			
			request = {
          location: address,
          radius: 50000,
		  
          keyword: [keyword]
        };
        infowindow = new google.maps.InfoWindow();
        service = new google.maps.places.PlacesService(map);
        service.search(request, callback);
		}
      function callback(results, status) {
        if (status == google.maps.places.PlacesServiceStatus.OK) {
          for (var i = 0; i < results.length; i++) {
            createMarker(results[i]);
			
          }
        }
      }
function clearOverlays() {
  if (markersArray) {
    for (var i = 0; i < markersArray.length; i++ ) {
      markersArray[i].setMap(null);
    }
  }
}

function getCenterPolygon(coordTemp)
{
	var i=0;
	var centerBounds = new google.maps.LatLngBounds();
	for (i = 0; i < coordTemp.length; i++) {
  			centerBounds.extend(coordTemp[i]);
			}
	centerTempString=centerBounds.getCenter();
	centerTempString=String(centerTempString).replace('(', '');		
	centerTempString=String(centerTempString).replace(')', '');	
	comaIndex=String(centerTempString).indexOf(',');
	centerLat=String(centerTempString).slice(0, comaIndex);
	centerLong=String(centerTempString).slice(comaIndex +1, String(centerTempString).length);
	
}
	  
//function to hide block and show blocks
function hideBlock1()
 	{
		$('.hideblock1').css("display", "none");
	 	$('.showblock1').css("display", "block");
		$('.block100 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock1()
 	{
		$('.hideblock1').css("display", "block");
	 	$('.showblock1').css("display", "none");
		$('.block100 .insideCheckboxesTableBlock').css("display", "block");
	}	
function hideBlock2()
 	{
		$('.hideblock2').css("display", "none");
	 	$('.showblock2').css("display", "block");
		$('.block200 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock2()
 	{
		$('.hideblock2').css("display", "block");
	 	$('.showblock2').css("display", "none");
		$('.block200 .insideCheckboxesTableBlock').css("display", "block");
	}	
function hideBlock3()
 	{
		$('.hideblock3').css("display", "none");
	 	$('.showblock3').css("display", "block");
		$('.block300 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock3()
 	{
		$('.hideblock3').css("display", "block");
	 	$('.showblock3').css("display", "none");
		$('.block300 .insideCheckboxesTableBlock').css("display", "block");
	}	
function hideBlock4()
 	{
		$('.hideblock4').css("display", "none");
	 	$('.showblock4').css("display", "block");
		$('.block400 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock4()
 	{
		$('.hideblock4').css("display", "block");
	 	$('.showblock4').css("display", "none");
		$('.block400 .insideCheckboxesTableBlock').css("display", "block");
	}	
function hideBlock5()
 	{
		$('.hideblock5').css("display", "none");
	 	$('.showblock5').css("display", "block");
		$('.block500 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock5()
 	{
		$('.hideblock5').css("display", "block");
	 	$('.showblock5').css("display", "none");
		$('.block500 .insideCheckboxesTableBlock').css("display", "block");
	}
function hideBlock6()
 	{
		$('.hideblock6').css("display", "none");
	 	$('.showblock6').css("display", "block");
		$('.block600 .insideCheckboxesTableBlock').css("display", "none");
	}	
function showBlock6()
 	{
		$('.hideblock6').css("display", "block");
	 	$('.showblock6').css("display", "none");
		$('.block600 .insideCheckboxesTableBlock').css("display", "block");
	}							
    </script>  

    
</head> 
<body onload="loadMapsAPI();"> 

<div data-role="page"  id="map-view">
	
	<div data-role="header" data-theme="c">
		<div class="logoHolder"><img src="images/sce-logo.png" width="127" height="39"/></div>
        <div class="ui-btn-right" data-role="controlgroup" data-type="horizontal" style="margin-top:15px;">
      <a href="index.html"  data-ajax="false" rel="external"><img src="images/home-btn.png" width="23" height="24"/></a>
    </div>
       
	</div><!-- /header -->

		
		
          
          <div data-role="content"style="clear:both">
          	
            <div class="blockview-direction">Please enter your street address and city, or ZIP code and select from the Blocks in the legend to
see the Block you are in.</div>
            <!-- map search field start here -->
            <div class="blockview-search-wrapper">
            	<table style="width:100%">
                <tr>
                	<td><input data-clear-btn="true" name="address" id="address" value="" type="text" ></td>
                    <td width="30"><a href="#" onClick="codeAddress()" data-role="button"  data-mini="true" data-theme="d" data-corners="false">Search</a></td>
                </tr>
                </table>
           		

            </div>
            <!-- map search field ends here -->
            <!-- legend start here -->
            <div data-role="collapsible" class="collapseHeader" data-inset="false" data-iconpos="right" data-theme="c">
                <h4>Legend</h4>
                <!-- start of checkboxes -->
                	<!-- sdp active checkboxes -->
                <div class="block100">
                	<div class="hideblock1"><a href="#" onClick="hideBlock1()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock1"><a href="#" onClick="showBlock1()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="scecCheckbox"  class="scec" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'scec100')" /></td>
                            <td width="30"><img src="images/icons/blockLegend1.gif" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>100 - 199 (SCEC)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock" data-bind="foreach: scecBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="scec100" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)" name="scecEventCheckbox" /></td>
                            <td><label data-bind="text: $data"></label></td>                         
                        </tr>                                          
                    </table>
                </div>
                <div class="block200">
                	<div class="hideblock2"><a href="#" onClick="hideBlock2()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock2"><a href="#" onClick="showBlock2()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="scenCheckbox"  class="scen" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'scen200')" /></td>
                            <td width="30"><img src="images/icons/blockLegend2.gif" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>200 - 299 (SCHD)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock" data-bind="foreach: schdBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="scen200" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)" name="scenEventCheckbox"  /></td>
                           <td><label data-bind="text: $data"></label></td>                               
                        </tr>                      
                    </table>
                </div>
               <div class="block300">
               		<div class="hideblock3"><a href="#" onClick="hideBlock3()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock3"><a href="#" onClick="showBlock3()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="scnwCheckbox" class="scnw" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'scnw300')" /></td>
                            <td width="30"><img src="images/icons/blockLegend3.gif" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>300 - 399 (SCLD)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock" data-bind="foreach: scldBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="scnw300" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)"  name="scnwEventCheckbox"  /></td>
                            <td><label data-bind="text: $data"></label></td> 
                        </tr>
                        
                    </table>
                </div>
              	<div class="block400">
                	<div class="hideblock4"><a href="#" onClick="hideBlock4()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock4"><a href="#" onClick="showBlock4()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock" >
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="scewCheckbox" class="scew" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'scew400')" /></td>
                            <td width="30"><img src="images/icons/blockLegend4V2.gif" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>400 - 499 (SCEN)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock"  data-bind="foreach: scenBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="scew400" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)"  name="scewEventCheckbox" /></td>
                            <td><label data-bind="text: $data"></label></td> 
                        </tr>                      
                    </table>
                </div>
                <div class="block500">
                	<div class="hideblock5"><a href="#" onClick="hideBlock5()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock5"><a href="#" onClick="showBlock5()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="schdCheckbox" class="schd" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'schd500')"/></td>
                            <td width="30"><img src="images/icons/blockLegend5V2.gif" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>500 - 599 (SCNW)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock"  data-bind="foreach: scnwBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="schd500" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)"  name="schdEventCheckbox"  /></td>
                           <td><label data-bind="text: $data"></label></td> 
                        </tr>                     
                    </table>
                </div>
                <div class="block600">
                	<div class="hideblock6"><a href="#" onClick="hideBlock6()"><img src="images/downGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                    <div class="showblock6"><a href="#" onClick="showBlock6()"><img src="images/leftGreenArrow.png" width="18" height="19" align="texttop" border="0"/></a></div>
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	<td width="50" class="blockViewMainCheckbox"><input name="scldCheckbox" class="scld" type="checkbox"  align="absmiddle" onClick="selectSlap(this,'scld600')" /></td>
                            <td width="30"><img src="images/icons/blockLegend7.png" width="19" height="19" alt="S" align="absmiddle" /></td>
                            <td>600 - 699 (SCEW)</td>
                            
                        </tr>
                    </table>
                    <table class="insideCheckboxesTableBlock" data-bind="foreach: scewBlocks">
                    	<tr>
                        	<td width="40" class="blockViewMainCheckbox"><input class="scld600" type="checkbox"  align="absmiddle" data-bind="value: $data" onclick="checkboxListener(this)"   name="scldEventCheckbox"/></td>
                            <td><label data-bind="text: $data"></label></td>
                        </tr>                      
                    </table>
                </div>
                <!-- end of checkboxes -->
                 <!-- legend for home icon -->
                <div class="homeIconHolderBlock">
                	<table class="outsideCheckboxesTableBlock">
                    	<tr>
                        	
                            <td width="30"><img src="images/icons/home.png" width="24" height="30" alt="Home Icon" align="absmiddle" /></td>
                            <td>Your Location</td>
                            
                        </tr>
                    </table>
                </div>
            </div>

            <!-- legend ends here -->
            <!-- map start here -->
          	<div id="map" style="height:300px;; width:90%; margin-left:5%; margin-right:5%; margin-top:20px;"></div>
            <!-- map ends here -->
          </div>

	<div class="footerLinks" style="text-align:left;font-size:11px;padding:10px;padding-top:20px; font-weight:normal;color:#666 !important;">Shaded figures in the map above show an approximation of a Block area. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.
</div>
<br><br><br>
	
    
</div><!-- /page -->
<script>
var i =0;
$('input:checkbox').map(function () {
	console.log(i++);
	this.checked=false;
});
hideBlock1();
hideBlock2();
hideBlock3();hideBlock4();hideBlock5();hideBlock6();

function addTask(url_value,callback){
	$.ajax({
		url: url_value,
		dataType: "json"
	}).done(function(response) {
		callback(response);
	}).fail(function(){
		alert( "ERROR:Retrieve system service error,can't fetch response data." );
	});
}

function ViewModel() {
	var self = this;
	self.systemUpdateTime = ko.observable();
	
	self.scecBlocks= ko.observableArray();
	self.scenBlocks= ko.observableArray();
	self.scnwBlocks= ko.observableArray();
	self.scewBlocks= ko.observableArray();
	self.scldBlocks= ko.observableArray();
	self.schdBlocks= ko.observableArray();
		
	this.clickItem = function(item) {
       console.log(item);
	   console.log(arguments[1].target.checked);
	   arguments[1].target.checked = arguments[1].target.checked;
    };  
	
	self.sync= function() {
	var url="resteasy/services/getBlocks";
	// Load initial state from server, convert it to Task instances, then populate self.tasks
	addTask(url, function(allData) {
		//self.systemUpdateTime(allData.eventStore.updateTime);
		//------------------ RTP[BEGIN] ------------------
		console.log(allData.slapBlocks.scldBlocks);
		self.scecBlocks(allData.slapBlocks.scecBlocks);
		self.scenBlocks(allData.slapBlocks.scenBlocks);
		self.scnwBlocks(allData.slapBlocks.scnwBlocks);
		self.scewBlocks(allData.slapBlocks.scewBlocks);
		self.scldBlocks(allData.slapBlocks.scldBlocks);
		self.schdBlocks(allData.slapBlocks.schdBlocks);

	
	 }); 
		 return self;
	};
	
}
var vm = new ViewModel().sync();
ko.applyBindings(vm);

</script>

</body>

</html>