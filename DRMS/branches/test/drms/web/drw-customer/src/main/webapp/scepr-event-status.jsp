<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.drw.util.DRWUtil" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<f:view>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script src="js/scriptaculous.js" type="text/javascript"></script>
<script src="js/dragdrop.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<title>Commercial - Demand Response Event Status</title>

<link href="css/template.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite.css" rel="stylesheet" type="text/css" />
<link href="css/mobile_app.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var refreshButtonSDPA;
	var refreshButtonSDPS;
	var refreshButtonSAIA;
	var refreshButtonSAIS;
	var refreshButtonAPIA;
	var refreshButtonAPIS;
	var refreshButtonBIPA;
	var refreshButtonBIPS;
	var refreshButtonCBPA;
	var refreshButtonCBPS;
	var refreshButtonDBPA;
	var refreshButtonDBPS;

	
	var lastTimeStampSDPA=-1;
	var lastTimeStampSDPS=-1;
	var lastTimeStampSAIA=-1;
	var lastTimeStampSAIS=-1;
	var lastTimeStampAPIA=-1;
	var lastTimeStampAPIS=-1;
	var lastTimeStampBIPA=-1;
	var lastTimeStampBIPS=-1;
	var lastTimeStampCBPA=-1;
	var lastTimeStampCBPS=-1;
	var lastTimeStampDBPA=-1;
	var lastTimeStampDBPS=-1;
	
	function initialize() {
		refreshButtonSDPA = document.getElementById("form:refreshButton_SDPA");
		refreshButtonSDPS = document.getElementById("form:refreshButton_SDPS");
		refreshButtonSAIA = document.getElementById("form:refreshButton_SAIA");
		refreshButtonSAIS = document.getElementById("form:refreshButton_SAIS");
		refreshButtonAPIA = document.getElementById("form:refreshButton_APIA");
		refreshButtonAPIS = document.getElementById("form:refreshButton_APIS");
		refreshButtonBIPA = document.getElementById("form:refreshButton_BIPA");
		refreshButtonBIPS = document.getElementById("form:refreshButton_BIPS");
		refreshButtonCBPA = document.getElementById("form:refreshButton_CBPA");
		refreshButtonCBPS = document.getElementById("form:refreshButton_CBPS");
		refreshButtonDBPA = document.getElementById("form:refreshButton_DBPA");
		refreshButtonDBPS = document.getElementById("form:refreshButton_DBPS");
    
        downloadUrl("getMapData.cachemonitor", function(data) {
        
            var xml = data.responseText; 
			//alert(xml);
			var obj = eval('('+xml+')');
			var lastTimeStampSDPAXML = obj.ACT_SDPCOME_EVENT;
			var lastTimeStampSDPSXML = obj.SCHE_SDPCOME_EVENT;
			var lastTimeStampSAIAXML = obj.SAI_COMMERCIAL_ACTIVE;
			var lastTimeStampSAISXML = obj.SAI_COMMERCIAL_SCHEDULED;
			var lastTimeStampAPIAXML = obj.ACT_API_EVENT;
			var lastTimeStampAPISXML = obj.SCHE_API_EVENT;
			var lastTimeStampBIPAXML = obj.ACT_BIP_EVENT;
			var lastTimeStampBIPSXML = obj.SCHE_BIP_EVENT;
			var lastTimeStampCBPAXML = obj.ACT_CBP_EVENT;
			var lastTimeStampCBPSXML = obj.SCHE_CBP_EVENT;
			var lastTimeStampDBPAXML = obj.DBP_COMMERCIAL_ACTIVE;
			var lastTimeStampDBPSXML = obj.DBP_COMMERCIAL_SCHEDULED;
			var systemTime = obj.systemTime;
			document.getElementById('form:outputText_systemTime').innerHTML="Updated "+systemTime;
        
			if(lastTimeStampSDPAXML != lastTimeStampSDPA){
                lastTimeStampSDPA = lastTimeStampSDPAXML;
				autoRefresh(refreshButtonSDPA);
            }
			if(lastTimeStampSDPSXML != lastTimeStampSDPS){
                lastTimeStampSDPS = lastTimeStampSDPSXML;
				autoRefresh(refreshButtonSDPS);
            }
			if(lastTimeStampSAIAXML != lastTimeStampSAIA){
                lastTimeStampSAIA = lastTimeStampSAIAXML;
				autoRefresh(refreshButtonSAIA);
            }
			if(lastTimeStampSAISXML != lastTimeStampSAIS){
                lastTimeStampSAIS = lastTimeStampSAISXML;
				autoRefresh(refreshButtonSAIS);
            }
			if(lastTimeStampAPIAXML != lastTimeStampAPIA){
				//alert(lastTimeStampAPIA +" should refresh button is API active: "+lastTimeStampAPIAXML);
                lastTimeStampAPIA = lastTimeStampAPIAXML;
				autoRefresh(refreshButtonAPIA);
            }
			if(lastTimeStampAPISXML != lastTimeStampAPIS){
				//alert(lastTimeStampAPIS +" should refresh button is API scheduled: "+lastTimeStampAPISXML);
                lastTimeStampAPIS = lastTimeStampAPISXML;
				autoRefresh(refreshButtonAPIS);
            }
			if(lastTimeStampBIPAXML != lastTimeStampBIPA){
                lastTimeStampBIPA = lastTimeStampBIPAXML;
				autoRefresh(refreshButtonBIPA);
            }
			if(lastTimeStampBIPSXML != lastTimeStampBIPS){
                lastTimeStampBIPS = lastTimeStampBIPSXML;
				autoRefresh(refreshButtonBIPS);
            }
			if(lastTimeStampCBPAXML != lastTimeStampCBPA){
                lastTimeStampCBPA = lastTimeStampCBPAXML;
				autoRefresh(refreshButtonCBPA);
            }
			if(lastTimeStampCBPSXML != lastTimeStampCBPS){
                lastTimeStampCBPS = lastTimeStampCBPSXML;
				autoRefresh(refreshButtonCBPS);
            }
			if(lastTimeStampDBPAXML != lastTimeStampDBPA){
                lastTimeStampDBPA = lastTimeStampDBPAXML;
				autoRefresh(refreshButtonDBPA);
            }
			if(lastTimeStampDBPSXML != lastTimeStampDBPS){
                lastTimeStampDBPS = lastTimeStampDBPSXML;
				autoRefresh(refreshButtonDBPS);
            }
        
        });//end of downloadUrl
		
		//adjustBIP();
    }//end of initialize method

	
	function downloadUrl(url, callback) {
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
            params = "keyValue=ACT_SDPCOME_EVENT,SCHE_SDPCOME_EVENT,SAI_COMMERCIAL_ACTIVE,SAI_COMMERCIAL_SCHEDULED,ACT_API_EVENT,SCHE_API_EVENT,ACT_BIP_EVENT,SCHE_BIP_EVENT,ACT_CBP_EVENT,SCHE_CBP_EVENT,DBP_COMMERCIAL_ACTIVE,DBP_COMMERCIAL_SCHEDULED";
        request.send(params); 
    }
	
	function autoRefresh(refreshButton){
		if(refreshButton!=null){
			refreshButton.onclick();
		}
	}
    function doNothing(){
    }

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
        initialize();
        t=setTimeout("timedCount()",<%=DRWUtil.getSystemManager().getPss2Properties().getDrCacheInterval()%>); // refresh intervel 10 sec
    }
    
    function stopCount()
    {
        clearTimeout(t);
        timer_is_on=0;
    }	


	function overlay() {
		el = document.getElementById("overlay");
		el.style.visibility = (el.style.visibility == "visible") ? "hidden" : "visible";
	}
	
	function overlayShow() {
		el = document.getElementById("overlay");
		el.style.visibility = "visible";	
		
		el2 = document.getElementById("RTPForecastDIV");
		el2.style.visibility = "visible";
		el2.style.display = "";		
	}
	function overlayHide() {
		el = document.getElementById("overlay");
		el.style.visibility = "hidden";	
		el2.style.display = "none";	
		
		el2 = document.getElementById("RTPForecastDIV");
		el2.style.visibility = "hidden";
	}

	function switchDisplaySDPMaxToMin(){
		document.getElementById("form:SDPOpenPanelGroup").style.display="none";
		document.getElementById("form:SDPClosePanelGroup").style.display="";
		document.getElementById("minIconSDP").focus();
	}
	function switchDisplaySDPMinToMax(){
		document.getElementById("form:SDPClosePanelGroup").style.display="none";
		document.getElementById("form:SDPOpenPanelGroup").style.display="";
		document.getElementById("maxIconSDP").focus();
	}

	function switchDisplayAPIMaxToMin(){
		document.getElementById("form:APIOpenPanelGroup").style.display="none";
		document.getElementById("form:APIClosePanelGroup").style.display="";
		document.getElementById("minIconAPI").focus();
	}
	function switchDisplayAPIMinToMax(){
		document.getElementById("form:APIClosePanelGroup").style.display="none";
		document.getElementById("form:APIOpenPanelGroup").style.display="";
		document.getElementById("maxIconAPI").focus();
	}
	
	
	
	function switchDisplayBIPMaxToMin(){
		document.getElementById("form:BIPOpenPanelGroup").style.display="none";
		document.getElementById("form:BIPClosePanelGroup").style.display="";
		document.getElementById("minIconBIP").focus();
	}
	function switchDisplayBIPMinToMax(){
		document.getElementById("form:BIPOpenPanelGroup").style.display="";
		document.getElementById("form:BIPClosePanelGroup").style.display="none";
		document.getElementById("maxIconBIP").focus();
		
	}
	
	function switchDisplayCBPMaxToMin(){
		document.getElementById("form:CBPOpenPanelGroup").style.display="none";
		document.getElementById("form:CBPClosePanelGroup").style.display="";
		document.getElementById("minIconCBP").focus();
	}
	function switchDisplayCBPMinToMax(){
		document.getElementById("form:CBPClosePanelGroup").style.display="none";
		document.getElementById("form:CBPOpenPanelGroup").style.display="";
		document.getElementById("maxIconCBP").focus();
	}
	
	
	function switchDisplaySAIMaxToMin(){
		document.getElementById("form:SAIOpenPanelGroup").style.display="none";
		document.getElementById("form:SAIClosePanelGroup").style.display="";
		document.getElementById("minIconSAI").focus();
	}
	function switchDisplaySAIMinToMax(){
		document.getElementById("form:SAIClosePanelGroup").style.display="none";
		document.getElementById("form:SAIOpenPanelGroup").style.display="";
		document.getElementById("maxIconSAI").focus();
	}	
	
	function switchDisplayDBPMaxToMin(){
		document.getElementById("form:DBPOpenPanelGroup").style.display="none";
		document.getElementById("form:DBPClosePanelGroup").style.display="";
		document.getElementById("minIconDBP").focus();
	}
	function switchDisplayDBPMinToMax(){
		document.getElementById("form:DBPClosePanelGroup").style.display="none";
		document.getElementById("form:DBPOpenPanelGroup").style.display="";
		document.getElementById("maxIconDBP").focus();
	}
	
	
	
	function switchDisplayDRCMaxToMin(){
		document.getElementById("form:DRCOpenPanelGroup").style.display="none";
		document.getElementById("form:DRCClosePanelGroup").style.display="";
		document.getElementById("minIconDRC").focus();
	}
	function switchDisplayDRCMinToMax(){
		document.getElementById("form:DRCClosePanelGroup").style.display="none";
		document.getElementById("form:DRCOpenPanelGroup").style.display="";
		document.getElementById("maxIconDRC").focus();
	}
	
	function switchScheduleSDPMaxToMin(){
		document.getElementById("form:SDPOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:SDPClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleSDPMinToMax(){
		document.getElementById("form:SDPOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:SDPClosedScheduleEventPanel").style.display="none";
	}
	function switchScheduleSAIMaxToMin(){
		document.getElementById("form:SAIOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:SAIClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleSAIMinToMax(){
		document.getElementById("form:SAIOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:SAIClosedScheduleEventPanel").style.display="none";
	}
	function switchScheduleAPIMaxToMin(){
		document.getElementById("form:APIOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:APIClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleAPIMinToMax(){
		document.getElementById("form:APIOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:APIClosedScheduleEventPanel").style.display="none";
	}
	function switchScheduleBIPMaxToMin(){
		document.getElementById("form:BIPOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:BIPClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleBIPMinToMax(){
		document.getElementById("form:BIPClosedScheduleEventPanel").style.display="none";
		document.getElementById("form:BIPOpenedScheduleEventPanel").style.display="";
	}
	function switchScheduleCBPMaxToMin(){
		document.getElementById("form:CBPOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:CBPClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleCBPMinToMax(){
		document.getElementById("form:CBPOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:CBPClosedScheduleEventPanel").style.display="none";
	}
	function switchScheduleDBPMaxToMin(){
		document.getElementById("form:DBPOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:DBPClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleDBPMinToMax(){
		document.getElementById("form:DBPOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:DBPClosedScheduleEventPanel").style.display="none";
	}
	function switchScheduleDRCMaxToMin(){
		document.getElementById("form:DRCOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:DRCClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleDRCMinToMax(){
		document.getElementById("form:DRCOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:DRCClosedScheduleEventPanel").style.display="none";
	}
	function initialComponent(){
		var APIEventEmptyFlagA = document.getElementById("form:APIEventEmptyFlagA").innerText;
		var BIPEventEmptyFlagA = document.getElementById("form:BIPEventEmptyFlagA").innerText;
		var CBPEventEmptyFlagA = document.getElementById("form:CBPEventEmptyFlagA").innerText;
		var DBPEventEmptyFlagA = document.getElementById("form:DBPEventEmptyFlagA").innerText;
		var SAIEventEmptyFlagA = document.getElementById("form:SAIEventEmptyFlagA").innerText;
		var SDPEventEmptyFlagA = document.getElementById("form:SDPEventEmptyFlagA").innerText;
		
		var APIEventEmptyFlagS = document.getElementById("form:APIEventEmptyFlagS").innerText;
		var BIPEventEmptyFlagS = document.getElementById("form:BIPEventEmptyFlagS").innerText;
		var CBPEventEmptyFlagS = document.getElementById("form:CBPEventEmptyFlagS").innerText;
		var DBPEventEmptyFlagS = document.getElementById("form:DBPEventEmptyFlagS").innerText;
		var SAIEventEmptyFlagS = document.getElementById("form:SAIEventEmptyFlagS").innerText;
		var SDPEventEmptyFlagS = document.getElementById("form:SDPEventEmptyFlagS").innerText;
		
		if(APIEventEmptyFlagA == "false" || APIEventEmptyFlagS == "false"){
			//has active events;
			switchDisplayAPIMinToMax();
		}else{
			//active events empty;
			switchDisplayAPIMaxToMin();
		}
		if(BIPEventEmptyFlagA == "false" || BIPEventEmptyFlagS == "false"){
			//has active events;
			switchDisplayBIPMinToMax();
		}else{
			//active events empty;
			switchDisplayBIPMaxToMin();
		}
		if(CBPEventEmptyFlagA == "false" || CBPEventEmptyFlagS == "false"){
			//has active events;
			switchDisplayCBPMinToMax();
		}else{
			//active events empty;
			switchDisplayCBPMaxToMin();
		}
		if(DBPEventEmptyFlagA == "false" || DBPEventEmptyFlagS == "false"){
			//has active events;
			switchDisplayDBPMinToMax();
		}else{
			//active events empty;
			switchDisplayDBPMaxToMin();
		}
		if(SAIEventEmptyFlagA == "false" || SAIEventEmptyFlagS == "false"){
			//has active events;
			switchDisplaySAIMinToMax();
		}else{
			//active events empty;
			switchDisplaySAIMaxToMin();
		}
		if(SDPEventEmptyFlagA == "false" || SDPEventEmptyFlagS == "false"){
			//has active events;
			switchDisplaySDPMinToMax();
		}else{
			//active events empty;
			switchDisplaySDPMaxToMin();
		}
		
		document.getElementById("commercialTabLink").focus();
	 }
	 
	 function accessibilityForTab(){
	 	if(document.getElementById("form:APIOpenPanelGroup").style.display=="none"){
			var min = document.getElementById("minIconAPI");
			
			if(min){
				min.focus();
			}
		}else if(document.getElementById("form:APIClosePanelGroup").style.display=="none"){
			var max = document.getElementById("maxIconAPI");
		
			if(max){
				max.focus();
			}
		}
	 }
	 
	function evictCookie(name) {
	    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	}
	
		
</script>
<style type="text/css">

	#overlay {
		 visibility: hidden;
		 position: absolute;
		 left: 0px;
		 top: 0px;
		 width:100%;
		 height:100%;
		 _height:1000px;
		 text-align:center;
		 z-index: 10;
		 background-image:url(images/transparentBG.png);
		 _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=scale, src=images/transparentBG.png,) alpha(opacity=50); 
	}

	#RTPForecastDIV {
		visibility: hidden;
		position:absolute;
		z-index:20;
		left:36%;
		top: 88px;
	}
h3 {	
	font-family:Arial, Helvetica, sans-serif;
	font-size:14px;
	font-weight: bold;
	color:#417300;
	margin-top: 0px;
	}
.activeTitle{
	color: #009245;
    font-size: 11px;
	 margin-left: 15px;
}		
</style>
</head>
<body onload="startTimer();initialComponent();hideMobileApp();">
<a4j:region>
<a4j:keepAlive beanName = "drFrame"/>
<jsp:include page="jsp/component/component_RTP.jsp" />
<h:inputHidden id="hideMob" value="#{drFrame.mobBadge}"/>
<div id="templatemo_wrapper_outer">
  <div id="templatemo_wrapper_inner" style="margin: 0 auto;">
   
    <div class="pageHeader">
      <div class="nav">
    
        <div class="logoDiv"> <a href="http://www.sce.com/default.htm" title="Southern California Edison"> <img src="images/pagelogo.gif" width="150" height="53" class="pagelogo" alt="SCE logo" title="SCE logo"/> </a> </div>
    
        <div class="wrapper">
          <div class="topmenu">
            <ul>
              <li>
                <h4><a href="https://www.sce.com/wps/portal/home/customer-service/billing-payment/pay-my-bill">Pay Your Bill</a></h4>
              </li>
              <li>
                <h4><a href="https://www.sce.com/wps/portal/home/customer-service/move-center">Turn On/Turn Off Service</a></h4>
              </li>
              <li>
                <h4><a href="https://www.sce.com/wps/portal/home/outage-center">Outage Center</a></h4>
              </li>
              <li>
                <h4><a href="https://www.sce.com/wps/portal/home/safety">Your Safety</a></h4>
              </li>
              <li>
                <h4><a href="https://www.sce.com/wps/portal/home/customer-service/contact-us">Contact Us</a></h4>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
	
    <div id="mainTemplateWrapper">
      <h1><h:outputText value="#{drTextContext.titleEventStatusPage}"/></h1>
      <p class="pageTitleDescription">
		<h:outputText value="#{drTextContext.copyrightActiveEventTitle}"/>
	  </p>
      <div id="eventStatusTabWrap">
        <ul id="eventStatusTab" role="tablist">
          <li id="eventStatusTab1" role="tab"><a href="scepr-event-status-resi.jsf"><h2>Residential</h2></a></li>
          <li id="eventStatusTab2" role="tab"><a id="commercialTabLink" href="scepr-event-status.jsf" class="on" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}"><h2>Commercial</h2></a></li>
          <li id="eventStatusTab3" role="tab"><a href="scepr-event-history.jsf"><h2>Event History</h2></a></li>
        </ul>
      </div>
      <div id="innerContentPanel" role="tabpanel">
		<h:form id="form">
		
			<a4j:commandButton id="refreshButton_SDPA" style="display:none">
				<a4j:support event="onclick" reRender="panelSDPExpandNoEvents,panelSDPExpandEvents,panelSDPCollapseNoEvents,panelSDPCollapseEvents,SDPInformationPanel,SDPOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_SDPS" style="display:none">
				<a4j:support event="onclick" reRender="panelSDPScheduleNoEvents,panelSDPScheduleEventsMin,panelSDPScheduleEventsMax,SDPInformationPanel"></a4j:support>
			</a4j:commandButton>
			
			<a4j:commandButton id="refreshButton_SAIA" style="display:none">
				<a4j:support event="onclick" reRender="panelSAIExpandNoEvents,panelSAIExpandEvents,panelSAICollapseNoEvents,panelSAICollapseEvents,SAIOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_SAIS" style="display:none">
				<a4j:support event="onclick" reRender="panelSAIScheduleNoEvents,panelSAIScheduleEventsMin,panelSAIScheduleEventsMax"></a4j:support>
			</a4j:commandButton>
			
			<a4j:commandButton id="refreshButton_APIA" style="display:none">
				<a4j:support event="onclick" reRender="panelAPIExpandNoEvents,panelAPIExpandEvents,panelAPICollapseNoEvents,panelAPICollapseEvents,APIInformationPanel,APIOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_APIS" style="display:none">
				<a4j:support event="onclick" reRender="panelAPIScheduleNoEvents,panelAPIScheduleEventsMin,panelAPIScheduleEventsMax,APIInformationPanel"></a4j:support>
			</a4j:commandButton>
			
			<a4j:commandButton id="refreshButton_BIPA" style="display:none">
				<a4j:support event="onclick" reRender="panelBIPExpandNoEvents,panelBIPExpandEvents,panelBIPCollapseNoEvents,panelBIPCollapseEvents,BIPInformationPanel,BIPOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_BIPS" style="display:none">
				<a4j:support event="onclick" reRender="panelBIPScheduleNoEvents,panelBIPScheduleEventsMin,panelBIPScheduleEventsMax,BIPInformationPanel"></a4j:support>
			</a4j:commandButton>
			
			<a4j:commandButton id="refreshButton_CBPA" style="display:none">
				<a4j:support event="onclick" reRender="panelCBPExpandNoEvents,panelCBPExpandEvents,panelCBPCollapseNoEvents,panelCBPCollapseEvents,CBPOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_CBPS" style="display:none">
				<a4j:support event="onclick" reRender="panelCBPScheduleNoEvents,panelCBPScheduleEventsMin,panelCBPScheduleEventsMax"></a4j:support>
			</a4j:commandButton>
			
			<a4j:commandButton id="refreshButton_DBPA" style="display:none">
				<a4j:support event="onclick" reRender="panelDBPExpandNoEvents,panelDBPExpandEvents,panelDBPCollapseNoEvents,panelDBPCollapseEvents,DBPOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_DBPS" style="display:none">
				<a4j:support event="onclick" reRender="panelDBPScheduleNoEvents,panelDBPScheduleEventsMin,panelDBPScheduleEventsMax"></a4j:support>
			</a4j:commandButton>
			
		
			
        <div class="ConventEventLeft">
			<div class="EventHeader">Active Events <span class="datetime"><h:outputText id="outputText_systemTime" value="Updated #{drFrame.currentSystemTime}"/></span></div>
			<jsp:include page="jsp/component/component_API_comm.jsp" />
			<jsp:include page="jsp/component/component_CBP_comm.jsp" />			
			<jsp:include page="jsp/component/component_DBP_comm.jsp" />
			<jsp:include page="jsp/component/component_DRC_comm.jsp" />
			<jsp:include page="jsp/component/component_SAI_comm.jsp" />
			<jsp:include page="jsp/component/component_SDP_comm.jsp" />
			<jsp:include page="jsp/component/component_BIP_comm.jsp" />
		</div>
		</h:form>
        <div class="EventSidebar">
          <div class="tempHeader">Real Time Pricing</div>
		  <div class="realtimeTemperature">
            <div id="tempDatetime">
              <div class="tempDatetimeBg">
                <div><span style="font-size:10px;">Estimated Pricing Category:</span></div>
              </div>
              <div class="tempDatetimeMiddle">
                <div class="dailyTemp" >
                  <div>
					<h:outputText value="#{drFrame.commercialUIBackingBean.currentRTP.weekdayString}"/>
					<br />
					<h:outputText value="#{drFrame.commercialUIBackingBean.currentRTP.dateString}" />
				  </div>
                </div>
                <div class="tempDatetimeBottom">
                  <div><h:outputText value="#{drFrame.commercialUIBackingBean.currentRTP.pricingCategoryWithAsterisk}"/></div>
                </div>
              </div>
              <div class="tempDatetimeBottomBg"></div>
            </div>
          </div>
		  <div class="forecastButton">
            <a href="#"><img src="images/buttons/estimates.jpg" onclick="overlayShow();" alt="View 5-Day Estimates" title="View 5-Day Estimates" border="0" /></a>
          </div>
          <div class="forcastLegal2">
            <h:outputText value="#{drTextContext.copyrightRTP}" escape="false"/>
          </div>
          <!-- 
          <div class="EventQuestTitle">
            <div class="EventRightTitle">Questions?</div>
            <div class="contactInfo">Contact Us:<h:outputText value="#{drTextContext.contactNo}"/></div>
          </div>
          <div class="dividerLine"></div>
           -->
          <div class="EventSideLink">
            <div class="EventRightTitle">Related Links</div>
            <ul>
              <li><a href="https://www.sce.com/wps/portal/home/business/savings-incentives/demand-response/?from=adr_demand">SCE Demand Response Programs</a></li>
			  <li><a href="https://www.sce.com/wps/portal/home/outage-center/rotating-outages/?from=adr_outage">SCE Rotating Outage Status</a></li>
              <li><a href="https://www.sce.com/wps/portal/home/regulatory/tariff-books">SCE Tariff Book</a></li>
              <li><a href="http://www.caiso.com/Pages/TodaysOutlook.aspx">California ISO - Today's Outlook</a></li>
			  <li>
				<h:outputLink target="_blank" value="/dr.website/scepr-event-blockview.jsf">
					<f:verbatim>SCE Block View</f:verbatim>
				</h:outputLink>
			  </li>
			  <li class="mobileSite" id="mobileSite"><a href="../m.dr.website" target="blank" onclick="evictCookie('FULL_SITE_ON_MOBILE');" >Mobile Site</a></li>
            </ul>
          </div>

								<div class="EventSideLink" id = "mobileApp">
									<div id = "sceDRAlerts">
										<div class="mobileApp">
											<div><span>SCE DR Alerts App</span></div>
										</div>
										
										<div class="trackAndManage">
												Download, set up &
												stay informed with
												custom Demand
												Response (DR) Alerts.
											</br>
											<div align="center">
												<img src="images/DR_Alerts_App_Icon.png" />
											</div>
											</br>
											<div class="downloadHeader" align="center">
												<a href="http://on.sce.com/drwebsitealerts">Download Now</a>
											</div>
										</div>
										
										<div class="mobileAppBottomBg">
										</div>
									</div>
									
								</div>

							</div>
        <div class="clearfix"></div>
      </div>
    </div>
    <div class="FooterWrapper">
      <div class="footerNav">
        <ul>
          <li><a href="https://www.sce.com/wps/portal/home/about-us">About</a> </li>
          <li><a href="https://www.sce.com/wps/portal/home/mysce/sitemap">Site Map</a></li>
          <li><a href="https://www.sce.com/wps/portal/home/privacy">Privacy</a></li>
          <li><a href="https://www.sce.com/wps/portal/home/regulatory">Regulatory</a></li>
          <li><a href="http://www.edison.com/">Edison International</a></li>
		  <li class="mobileSiteLi" id="mobileSiteLi"><a href="../m.dr.website" target="blank" onclick="evictCookie('FULL_SITE_ON_MOBILE');" >Mobile Site</a></li>
        </ul>
        <p/> 
      </div>
      <div class="footerCopyRight"><h:outputText value="Copyright @ 2014 Southern California Edison. All Rights Reserved"/></div>
    </div>
  </div>
</div>
<div class="margin_bottom_10"></div>
</a4j:region>
<script>
var isMobile = {
    Android: function() {
        return navigator.userAgent.match(/Android/i);
    },
    BlackBerry: function() {
        return navigator.userAgent.match(/BlackBerry/i);
    },
    iOS: function() {
        return navigator.userAgent.match(/iPhone|iPad|iPod/i);
    },
    Opera: function() {
        return navigator.userAgent.match(/Opera Mini/i);
    },
    Windows: function() {
        return navigator.userAgent.match(/IEMobile/i);
    },
    any: function() {
        return (isMobile.Android() || isMobile.BlackBerry() || isMobile.iOS() || isMobile.Opera() || isMobile.Windows());
    }
};

var $j=jQuery.noConflict();
if( !isMobile.any() ){
	$j('#mobileSiteLi').css("display", "none");
	$j('#mobileSite').css("display", "none");
	if($j('#mobileSiteLi').prev()){
		$j('#mobileSiteLi').prev().css("border","none");
	}
}
function hideMobileApp()
{
	var mm = document.getElementById("hideMob").value;
	if(mm=='true'){
	document.getElementById("mobileApp").style.display = "none";
	}else{
		document.getElementById("mobileApp").style.display = "block";
	}
	
}
</script>
</body>
</f:view>
</html>
