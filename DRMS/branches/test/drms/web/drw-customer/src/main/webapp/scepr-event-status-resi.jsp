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
<title>Residential - Demand Response Event Status</title>
<link href="css/template.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite.css" rel="stylesheet" type="text/css" />
<link href="css/mobile_app.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript">
	var refreshButtonSDPA;
	var refreshButtonSDPS;
	var refreshButtonSAIA;
	var refreshButtonSAIS;
	var refreshButtonSPDA;
	var refreshButtonSPDS;
	
	var lastTimeStampSDPA=-1;
	var lastTimeStampSDPS=-1;
	var lastTimeStampSAIA=-1;
	var lastTimeStampSAIS=-1;
	var lastTimeStampSPDA=-1;
	var lastTimeStampSPDS=-1;
	
    function initialize() {
		refreshButtonSDPA = document.getElementById("form:refreshButton_SDPA");
		refreshButtonSDPS = document.getElementById("form:refreshButton_SDPS");
		refreshButtonSAIA = document.getElementById("form:refreshButton_SAIA");
		refreshButtonSAIS = document.getElementById("form:refreshButton_SAIS");
		refreshButtonSPDA = document.getElementById("form:refreshButton_SPDA");
		refreshButtonSPDS = document.getElementById("form:refreshButton_SPDS");
    
        downloadUrl("getMapData.cachemonitor", function(data) {
        
            var xml = data.responseText; 
			//alert(xml);
			var obj = eval('('+xml+')');
			var lastTimeStampSDPAXML = obj.ACT_SDPRESI_EVENT;
			var lastTimeStampSDPSXML = obj.SCHE_SDPRESI_EVENT;
			var lastTimeStampSAIAXML = obj.SAI_RESIDENTIAL_ACTIVE;
			var lastTimeStampSAISXML = obj.SAI_RESIDENTIAL_SCHEDULED;
			var lastTimeStampSPDAXML = obj.SPD_RESIDENTIAL_ACTIVE;
			var lastTimeStampSPDSXML = obj.SPD_RESIDENTIAL_SCHEDULED;
			
			var systemTime = obj.systemTime;
			document.getElementById('form:outputText_systemTime').innerHTML="Updated "+systemTime;
        
			//alert(lastTimeStampSDPAXML);
           if(lastTimeStampSDPAXML != lastTimeStampSDPA){
				//alert(lastTimeStampSDPA +"should refresh button is SDPA: "+lastTimeStampSDPAXML);
                lastTimeStampSDPA = lastTimeStampSDPAXML;
				autoRefresh(refreshButtonSDPA);
            }
			if(lastTimeStampSDPSXML != lastTimeStampSDPS){
				//alert(lastTimeStampSDPS +"should refresh button is SDPS"+lastTimeStampSDPSXML);
                lastTimeStampSDPS = lastTimeStampSDPSXML;
				autoRefresh(refreshButtonSDPS);
            }
			if(lastTimeStampSAIAXML != lastTimeStampSAIA){
				//alert("should refresh button is SAIA"+lastTimeStampSAIAXML);
                lastTimeStampSAIA = lastTimeStampSAIAXML;
				autoRefresh(refreshButtonSAIA);
            }
			if(lastTimeStampSAISXML != lastTimeStampSAIS){
				//alert("should refresh button is SAIS"+lastTimeStampSAISXML);
                lastTimeStampSAIS = lastTimeStampSAISXML;
				autoRefresh(refreshButtonSAIS);
            }
			
			if(lastTimeStampSPDAXML != lastTimeStampSPDA){
                lastTimeStampSPDA = lastTimeStampSPDAXML;
				autoRefresh(refreshButtonSPDA);
            }
			if(lastTimeStampSPDSXML != lastTimeStampSPDS){
                lastTimeStampSPDS = lastTimeStampSPDSXML;
				autoRefresh(refreshButtonSPDS);
            }
        
        });//end of downloadUrl
    }//end of initialize method

    function autoRefresh(refreshButton){
		if(refreshButton!=null){
			refreshButton.onclick();
			//alert("refresh button is "+refreshButton);
		}
	}
  
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
     
            params = "keyValue=ACT_SDPRESI_EVENT,SCHE_SDPRESI_EVENT,SAI_RESIDENTIAL_ACTIVE,SAI_RESIDENTIAL_SCHEDULED,SPD_RESIDENTIAL_ACTIVE,SPD_RESIDENTIAL_SCHEDULED";
        request.send(params)      
 
    }// end of downloadUrl
    
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
	
	function switchDisplaySPDMaxToMin(){
		document.getElementById("form:SPDOpenPanelGroup").style.display="none";
		document.getElementById("form:SPDClosePanelGroup").style.display="";
		document.getElementById("minIconSPD").focus();
	}
	function switchDisplaySPDMinToMax(){
		document.getElementById("form:SPDClosePanelGroup").style.display="none";
		document.getElementById("form:SPDOpenPanelGroup").style.display="";
		document.getElementById("maxIconSPD").focus();
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
	
	function switchScheduleSPDMaxToMin(){
		document.getElementById("form:SPDOpenedScheduleEventPanel").style.display="none";
		document.getElementById("form:SPDClosedScheduleEventPanel").style.display="";
	}
	function switchScheduleSPDMinToMax(){
		document.getElementById("form:SPDOpenedScheduleEventPanel").style.display="";
		document.getElementById("form:SPDClosedScheduleEventPanel").style.display="none";
	}
	
	function initialComponent(){
		var SAIEventEmptyFlagA = document.getElementById("form:SAIEventEmptyFlagA").innerText;
		var SAIEventEmptyFlagS = document.getElementById("form:SAIEventEmptyFlagS").innerText;
		
		var SDPEventEmptyFlagA = document.getElementById("form:SDPEventEmptyFlagA").innerText;
		var SDPEventEmptyFlagS = document.getElementById("form:SDPEventEmptyFlagS").innerText;
		
		var SPDEventEmptyFlagA = document.getElementById("form:SPDEventEmptyFlagA").innerText;
		var SPDEventEmptyFlagS = document.getElementById("form:SPDEventEmptyFlagS").innerText;
		
		if(SAIEventEmptyFlagA == "false" || SAIEventEmptyFlagS == "false" ){
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
		if(SPDEventEmptyFlagA == "false" || SPDEventEmptyFlagS == "false"){
			//has active events;
			switchDisplaySPDMinToMax();
		}else{
			//active events empty;
			switchDisplaySPDMaxToMin();
		}
		document.getElementById("residentialTabLink").focus();
	 }
	 
	 function accessibilityForTab(){
	 
		if(document.getElementById("form:SDPOpenPanelGroup").style.display=="none"){
			var min = document.getElementById("minIconSDP");
			
			if(min){
				min.focus();
			}
		}else if(document.getElementById("form:SDPClosePanelGroup").style.display=="none"){
			var max = document.getElementById("maxIconSDP");
		
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
          <li id="eventStatusTab1" role="tab">
			<a 	id="residentialTabLink" href="scepr-event-status-resi.jsf" class="on" 
				onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}"
			><h2>Residential</h2></a></li>
          <li id="eventStatusTab2" role="tab"><a href="scepr-event-status.jsf"><h2>Commercial</h2></a></li>
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
			<a4j:commandButton id="refreshButton_SPDA" style="display:none">
				<a4j:support event="onclick" reRender="panelSPDExpandNoEvents,panelSPDExpandEvents,panelSPDCollapseNoEvents,panelSPDCollapseEvents,SPDOpenTitle"></a4j:support>
			</a4j:commandButton>
			<a4j:commandButton id="refreshButton_SPDS" style="display:none">
				<a4j:support event="onclick" reRender="panelSPDScheduleNoEvents,panelSPDScheduleEventsMin,panelSPDScheduleEventsMax"></a4j:support>
			</a4j:commandButton>
			
		<div class="ConventEventLeft">	
			<div class="EventHeader">Active Events <span class="datetime"><h:outputText id="outputText_systemTime" value="Updated #{drFrame.currentSystemTime}"/></span></div>
			<jsp:include page="jsp/component/component_SDP_resi.jsp" />
			<jsp:include page="jsp/component/component_SPD_resi.jsp" />
			<jsp:include page="jsp/component/component_SAI_resi.jsp" />
			
		</div>
		
		
		
		
		</h:form>
        <div class="EventSidebar">
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
      <div class="footerCopyRight"><h:outputText value="Copyright @ 2014 Southern California Edison. All Rights Reserved" style="font-size: 11px;"/></div>
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
