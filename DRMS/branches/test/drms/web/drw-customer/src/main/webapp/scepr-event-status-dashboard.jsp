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
<title>Kiosk Presentation Mode - Demand Response Event Status</title>

<link href="css/kiostemplate.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="js/geoxml3_v1.js"></script>
<script type="text/javascript" src="js/progressBar.js"></script>
<script type="text/javascript" src="<%=DRWUtil.getSystemManager().getPss2Properties().getGmapURL()%>?key=<%=DRWUtil.getSystemManager().getPropertyByName("mapKey").getStringValue()%>&sensor=false"></script>

<link href="css/poputemplate_2013_v1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">

	//-----------------------------------------------------------[BEGIN]google javascript--------------------------------------------------------------------------
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
	var lastCbpTimeStamp =  <%=new Date().getTime()%>;
    var defaultBounds = new google.maps.LatLngBounds(new google.maps.LatLng(33.198609, -120.52905399999997), new google.maps.LatLng(38.405354, -114.13122199999998));
	
	   function createMyPolygon(placemark) {
        // Overlay handling code goes here
        if (1==1) {
            cur = cur+1;
        
            pb.updateBar(1);
            return;
            //geoXml.createPolygon(placemark);
            //geoXml.createOverlay(placemark);
        }
      };
	  
	function initMap() {
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
        
		var retriveparam = "ALL_ACT_API=true&ALL_ACT_BIP=true&ALL_ACT_CBP=true&ALL_ACT_SDPR=true&ALL_ACT_SDPC=true";
        
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
            var result = xml.split("^", 2);
            lb.hide();
            pb.start(result[0]);
            geoXml.parseKmlString(result[1]);
        
        },retriveparam);//end of downloadUrl
	}
	
		
	function resetMap() {
        var polygons = geoXmlDoc.gpolygons;
       for (var i=0;i<geoXmlDoc.gpolygons.length;i++) {      
           geoXmlDoc.gpolygons[i].setMap(null);     
       }
       geoXmlDoc.gpolygons  = [];              
    }
	
	 function useTheData(doc){
      // Geodata handling goes here, using JSON properties of the doc object
      geoXmlDoc = doc[0];
      pb.hide();

    };
	
	function downloadUrl(url, callback, param) {
	//1.disable all check box before ajax request sended
	//2. will be enabled after map rendering				
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

        request.send(param)      
    }// end of downloadUrl
	//-----------------------------------------------------------[END]google javascript--------------------------------------------------------------------------
	
	
	
	



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
        //initializeEvents();
		//monitorCache();
		monitorCacheUpdate();
        t=setTimeout("timedCount()",<%=DRWUtil.getSystemManager().getPss2Properties().getDrCacheInterval()%>); // refresh intervel 10 sec
    }
    function startTimerForRoll(){
		roll();
		setTimeout("startTimerForRoll()",200); 
    }
	
	var lastHeight_active;
	var lastHeight_scheduled;
	function roll(){
		var activeEventList=null;
		var scheduledEventList=null;
		var obj_active=null;
		var obj_scheduled=null;
		activeEventList = document.getElementById("form:activeEventList");
		scheduledEventList = document.getElementById("form:scheduledEventList");
		if(activeEventList!=null){
			obj_active = document.getElementById("form:activeEventList:scb");
			lastHeight_active = obj_active.scrollTop;
			obj_active.scrollTop=obj_active.scrollTop+1;
					//alert("scrollTop is "+ obj.scrollTop+";lastHeight is "+lastHeight);
			if(obj_active.scrollTop-lastHeight_active==0){
						//alert(obj.scrollTop+"; to the buttom.Now return to the top");
				obj_active.scrollTop=0;
			}
		}
		if(scheduledEventList!=null){
			obj_scheduled = document.getElementById("form:scheduledEventList:scb");
			lastHeight_scheduled = obj_scheduled.scrollTop;
			obj_scheduled.scrollTop=obj_scheduled.scrollTop+1;
			if(obj_scheduled.scrollTop-lastHeight_scheduled==0){
					//alert(obj.scrollTop+"; to the buttom.Now return to the top");
				obj_scheduled.scrollTop=0;
			}
		}		
	}
	var refreshButton;

	
	var lastTimeStampA=<%=new Date().getTime()%>;
	var lastTimeStampS=<%=new Date().getTime()%>;
	

	
	function downloadUrlForCache(url, callback) {
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
        var params="";
            params = "keyValue=KIOSK_ACTIVE,KIOSK_SCHEDULED";
        request.send(params); 
    }
	
	function autoRefresh(refreshButton){
		if(refreshButton!=null){
			refreshButton.onclick();
		}
	}
	
		function doNothing(){
    }
	
	
	function adjustCss(){
		
		var obj_active = document.getElementById("form:activeEventList:scb");
		//alert(obj_active.style.overflowX);
		//alert(1 +obj_active.style.overflowX);
		obj_active.style.overflowX="hidden";
		//alert(2 +obj_active.style.overflowX);
		
		var obj_active = document.getElementById("form:scheduledEventList:scb");
		//alert(obj_active.style.overflowX);
		obj_active.style.overflowX="hidden";
		
		
	}

	
	function monitorCacheUpdate(){
		//1 check active time stamp
		//1.1 update active events panel
		//1.2 update active events map zone
		//2 check schedule time stamp
		//2.1 update schedule events panel
		//refreshButtonA = document.getElementById("form:refreshButtonA");
		//refreshButtonS = document.getElementById("form:refreshButtonS");
		refreshButton = document.getElementById("form:refreshButton");
        downloadUrlForCache("getMapData.cachemonitor", function(data) {
        
            var xml = data.responseText; 
			
			var obj = eval('('+xml+')');
			var lastTimeStampAXML = obj.KIOSK_ACTIVE;
			var lastTimeStampSXML = obj.KIOSK_SCHEDULED;
			var systemTime = obj.systemTime;
			document.getElementById('form:outputText_ScheduledEventsTime').innerHTML="Updated "+systemTime;
			document.getElementById('form:outputText_ActiveEventsTime').innerHTML="Updated "+systemTime;
			var flag = false;
            if(lastTimeStampAXML > lastTimeStampA){
                lastTimeStampA = lastTimeStampAXML;
				//autoRefresh(refreshButtonA);
				initMap();
				//alert("update active");
				flag=true;
            }
			if(lastTimeStampSXML > lastTimeStampS){
                lastTimeStampS = lastTimeStampSXML;
				//autoRefresh(refreshButtonS);
				//alert("update schedule");
				flag=true;
            }
			if(flag){
				autoRefresh(refreshButton);
			}
        });//end of downloadUrl
		
	}
</script>
</head>
<body onload="startTimer();initMap();startTimerForRoll();">
<a4j:region>
<a4j:keepAlive beanName = "drKiosk"/>
<div id="kiosWrapper">
  <div class="kiosHeader">
    <h1><a href="http://www.sce.com/default.htm" title="Southern California Edison">Southern California Edison</a></h1>
  </div>
  <h:form id="form">
			<a4j:commandButton id="refreshButton" style="display:none" oncomplete="adjustCss();" >
				 <a4j:support event='onclick' actionListener="#{drKiosk.refreshEvents}" reRender="activeEventsPanelGroup,scheduledEventsPanelGroup"></a4j:support>
			</a4j:commandButton>
			
	<a4j:poll id="poll" 
			interval="30000" 
			reRender="poll,outputText_ScheduledEventsTime,outputText_ActiveEventsTime"/>		
  <div class="kiosInnerWrapper">
    <div class="kiosLeftContent">
		<jsp:include page="jsp/component/component_kiosk_event_active.jsp" />
		<jsp:include page="jsp/component/component_kiosk_event_scheduled.jsp" />
    </div>
    <div class="kiosRightContent"> 
      	<jsp:include page="jsp/component/component_kiosk_event_legend.jsp" />
		<jsp:include page="jsp/component/component_kiosk_event_map.jsp" />
    </div>
    <div class="clearfix"></div>
  </div>
  </h:form>
  <div class="clearfix"></div>
</div>

</a4j:region>
</body>
</f:view>
</html>
