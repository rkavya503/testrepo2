<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US">
<f:view>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Event History - Demand Response Event Status</title>
<link href="css/template.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite.css" rel="stylesheet" type="text/css" />
<link href="css/mobile_app.css" rel="stylesheet" type="text/css" />
<style type="text/css">

.obscure {
  position: absolute !important;
  clip: rect(1px 1px 1px 1px); /* IE6, IE7 */
  clip: rect(1px, 1px, 1px, 1px);
}

</style>
<script type="text/javascript">
	function evictCookie(name) {
	    document.cookie = name + '=;expires=Thu, 01 Jan 1970 00:00:01 GMT;';
	}
	
	function accessibilityForTab(){

		var obj = document.getElementById("eventHistoryForm:selectOneMenu_program");
		if(obj){
			obj.focus();
		}
	 }
	 function initialComponent(){
		document.getElementById("historyTabLink").focus();
	 }
</script>


</head>
<body onload="initialComponent();hideMobileApp();">
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
      <h1><h:outputText value="#{drTextContext.titleEventHistoryPage}" /></h1>
      <p class="pageTitleDescription"><h:outputText value="#{drTextContext.copyrightHistoryEventTitle}" /></p>
      <div id="eventStatusTabWrap">
        <ul id="eventStatusTab" role="tablist">
          <li id="eventStatusTab1" role="tab"><a href="scepr-event-status-resi.jsf"><h2>Residential</h2></a></li>
          <li id="eventStatusTab2" role="tab"><a href="scepr-event-status.jsf"><h2>Commercial</h2></a></li>
          <li id="eventStatusTab3" role="tab"><a href="scepr-event-history.jsf" id="historyTabLink" class="on" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}" onclick="focusForTab()"><h2>Event History</h2></a></li>
        </ul>
      </div>
      <div id="innerContentPanel" role="tabpanel">
    
        <jsp:include page="jsp/component/component_event_history_panel.jsp" />
    
    
        <div class="EventSidebar" style="margin-top:10px;">
        <!-- 
          <div class="EventQuestTitle">
            <div class="EventRightTitle">Questions?</div>
            <div class="contactInfo">Contact Us:1-866-334-7827</div>
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
      <div class="footerCopyRight">Copyright @ 2014 Southern California Edison. All Rights Reserved</div>
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
