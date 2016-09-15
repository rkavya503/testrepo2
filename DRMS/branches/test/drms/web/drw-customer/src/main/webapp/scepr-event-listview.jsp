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
<title>List View - Demand Response Event Status</title>
<link href="css/poputemplate_2013_v1.css" rel="stylesheet" type="text/css" />
<link href="css/drwebsite_2013_v1.css" rel="stylesheet" type="text/css" />
<script src="js/jquery.js"></script>



<style type="text/css">

.listViewWarningbox{
    background:url(images/bg-evtsResult.gif) no-repeat 10px 7px;
    padding-left:56px;
    padding-right:25px;padding-top:8px;
    font:normal 7pt arial;
    background-color:#C30;
    color:#58595b;
    background-color:#f1f7e6;
    _padding-top:0px;
    margin-top:0px;
    min-height:40px;
    _height:40px;

}
.EvtListTable{
    color:#58595b;
    width:504px;
    margin: 10px 0px 10px 35px;
    /*margin-bottom:5px;*/
}
.locationWrapper
{
    width:504px;
    margin:0 0 0 35px;
    padding:0;
}

.locationWrapper div.countyWrapper, .locationWrapper div.cityWrapper, .locationWrapper div.zipWrapper
{
    float:left;
    width:135px;
    padding:0 20px 0 13px;
    margin:0;
}

h3 {	
	font-family:Arial, Helvetica, sans-serif;
	font-size:13px;
	font-weight:normal;
	color:#417300;
	}
.radioSelectionsBG
{
	margin-left:15px;
	margin-right:15px;
	margin-bottom:15px;
	margin-top:-20px;
	border-right: solid 1px #9d9d9d;
	border-bottom: solid 1px #9d9d9d;
	border-left: solid 1px #9d9d9d;
	border-top: solid 1px #9d9d9d;
	-moz-border-radius: 5px;
	border-radius: 5px;
	height:50px;
}
.radioHolder
{
	padding-left:20px;
	padding-top:12px;
	height:30px;
	vertical-align:text-top;
	font-size:12px;
	color:#000;
	font-weight:bold;
}	
</style>
<script type="text/javascript">
	var $j=jQuery.noConflict();
	var refreshButtonSDPR;
	var refreshButtonSDPC;
	var refreshButtonAPI;
	var refreshButtonBIP;

	
	var lastTimeStampSDPR=-1;
	var lastTimeStampSDPC=-1;
	var lastTimeStampAPI=-1;
	var lastTimeStampBIP=-1;
	
	var lastLegendTimeStamp =  <%=new Date().getTime()%>;
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
        //initialize();
		//document.getElementById("form:refreshButton").onclick();  
		monitorCache();		
        t=setTimeout("timedCount()",<%=DRWUtil.getSystemManager().getPss2Properties().getDrCacheInterval()%>); // refresh intervel 10 sec
    }
   
    function stopCount()
    {
        clearTimeout(t);
        timer_is_on=0;
    }
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
		  var interval = legend_time - lastLegendTimeStamp;		  
          if(interval>0){
				//alert('invoke: legend_time is '+legend_time+' and invoke: lastLegendTimeStamp is '+lastLegendTimeStamp + " interval is"+ interval);
              lastLegendTimeStamp = legend_time;
              document.getElementById("form:refreshButton").onclick();  
				
          }
        
          },params);//end of downloadUjrl
      }//end of initialize method
	  
	function isActive(){
		var isActive = document.getElementById('form:eventStateSelectRadio:0').checked;
		var active;
		if(isActive){
			active=true;
		}else{
			active=false;
		} 
		return active;
	}
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
	function countyChangeListener(){
		var obj = document.getElementById("form:inputText_zipcode");
		obj.value="";
		var obj = document.getElementById("form:selectOneMenu_city");
		obj.value="Select";
	}
	function cityChangeListener(){
		var obj = document.getElementById("form:inputText_zipcode");
		obj.value="";
		var obj = document.getElementById("form:selectOneMenu_county");
		obj.value="Select";
	}
	function zipcodeChangeListener(){
		var obj = document.getElementById("form:selectOneMenu_county");
		obj.value="Select";
		var obj = document.getElementById("form:selectOneMenu_city");
		obj.value="Select";
	}
	function searchComplete(){

		var obj = document.getElementById("form:message").firstChild.firstChild.firstChild.firstChild;
		if(obj){
			obj.focus();
		}
		
	}
	function initFocus(){
		//document.getElementById("form:tabLink").focus();
	 }
	 function accessibilityForTab(){
		document.getElementById("form:checkBoxSDPR").focus();
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

function disableAllCheckBox() {

	var c = document.getElementById('form').getElementsByTagName('input');
    for (var i = 0; i < c.length; i++) {
        if (c[i].type == 'checkbox') {
            c[i].disabled = true;
        }
    }
}	
function enableAllCheckBox() {
 
	var c = document.getElementById('form').getElementsByTagName('input');
    for (var i = 0; i < c.length; i++) {
        if (c[i].type == 'checkbox') {
            c[i].disabled = false;
        }
    }
}	
function checkAllCheckBox() {

	var c = document.getElementById('form').getElementsByTagName('input');
    for (var i = 0; i < c.length; i++) {
        if (c[i].type == 'checkbox') {
            c[i].checked = true;
        }
    }
}	
function uncheckAllCheckBox() {
	uncheckAllCheckBoxFunction('form:sdp-r','form:sdprLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:sdp-rs','form:sdprLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:sdp-c','form:sdpcLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:sdp-cs','form:sdpcLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:api','form:apiLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:apis','form:apiLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:bip','form:bipLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:bips','form:bipLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:cbp','form:cbpLegendPanelGroup');
	uncheckAllCheckBoxFunction('form:cbps','form:cbpLegendPanelGroup');
}
function uncheckAllCheckBoxFunction(checkboxID,legendPanelID) {
	var obj = document.getElementById(checkboxID);
	if(obj){		
		obj.checked = false;
		if(document.getElementById(legendPanelID)){
			var c = document.getElementById(legendPanelID).getElementsByTagName('input');
			for (var i = 0; i < c.length; i++) {
				if (c[i].type == 'checkbox') {
					c[i].checked =false;			
				}
			}
		}
	}
}
	 function checkAllHandler(obj,legendPanelID){
		var checked = obj.checked;
		var c = document.getElementById(legendPanelID).getElementsByTagName('input');
		for (var i = 0; i < c.length; i++) {
			if (c[i].type == 'checkbox') {
				c[i].checked =checked;	
			}
		}
		
	 }  
	 function checkEventHandler(obj,checAllCheckBoxId,panelId){
		var checked = obj.checked; 
		var c = document.getElementById(checAllCheckBoxId);
		if(checked){
			c.checked = checked;
		}
		if(panelId){
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
	 
	 function eventPanelDisplayByID(panelID,legendCheckObjID){
		var legendCheckObj = document.getElementById(legendCheckObjID);
		eventPanelDisplay(panelID,legendCheckObj);
	 }
	 function eventPanelDisplay(panelID,legendCheckObj){
		var checked = legendCheckObj.checked;
		if(checked){
			document.getElementById(panelID).style.display = "";
		}else{
			document.getElementById(panelID).style.display = "none";
		}
	 }
	 function initialize(){
		//alert('initialize');
		var isActive = document.getElementById('form:eventStateSelectRadio:0').checked;
		if(isActive){
			var checked = document.getElementById('form:cbp').checked;
			if(checked){
				document.getElementById('form:CBPPanel').style.display = "";
			}else{
				document.getElementById('form:CBPPanel').style.display = "none";
			}
			var checked = document.getElementById('form:api').checked;
			if(checked){
				document.getElementById('form:APIPanel').style.display = "";
			}else{
				document.getElementById('form:APIPanel').style.display = "none";
			}
			var checked = document.getElementById('form:bip').checked;
			if(checked){
				document.getElementById('form:BIPPanel').style.display = "";
			}else{
				document.getElementById('form:BIPPanel').style.display = "none";
			}
			var checked = document.getElementById('form:sdp-r').checked;
			if(checked){
				document.getElementById('form:SDPRPanel').style.display = "";
			}else{
				document.getElementById('form:SDPRPanel').style.display = "none";
			}
			var checked = document.getElementById('form:sdp-c').checked;
			if(checked){
				document.getElementById('form:SDPCPanel').style.display = "";
			}else{
				document.getElementById('form:SDPCPanel').style.display = "none";
			}
		}else{
			var checked = document.getElementById('form:cbps').checked;
			if(checked){
				document.getElementById('form:CBPPanel').style.display = "";
			}else{
				document.getElementById('form:CBPPanel').style.display = "none";
			}
			var checked = document.getElementById('form:apis').checked;
			if(checked){
				document.getElementById('form:APIPanel').style.display = "";
			}else{
				document.getElementById('form:APIPanel').style.display = "none";
			}
			var checked = document.getElementById('form:bips').checked;
			if(checked){
				document.getElementById('form:BIPPanel').style.display = "";
			}else{
				document.getElementById('form:BIPPanel').style.display = "none";
			}
			var checked = document.getElementById('form:sdp-rs').checked;
			if(checked){
				document.getElementById('form:SDPRPanel').style.display = "";
			}else{
				document.getElementById('form:SDPRPanel').style.display = "none";
			}
			var checked = document.getElementById('form:sdp-cs').checked;
			if(checked){
				document.getElementById('form:SDPCPanel').style.display = "";
			}else{
				document.getElementById('form:SDPCPanel').style.display = "none";
			}
		}
		
	 }
function searchCompleteAction(){
	switchSearchResult("form:sdpcActiveEventsDisplayPanel","form:sdpcSearchNoEventsPanel","form:sdpcLegendPanelGroup");
	switchSearchResult("form:sdprActiveEventsDisplayPanel","form:sdprSearchNoEventsPanel","form:sdprLegendPanelGroup");
	switchSearchResult("form:apiActiveEventsDisplayPanel","form:apiSearchNoEventsPanel","form:apiLegendPanelGroup");
	switchSearchResult("form:cbpActiveEventsDisplayPanel","form:cbpSearchNoEventsPanel","form:cbpLegendPanelGroup");
	switchSearchResult("form:bipActiveEventsDisplayPanel","form:bipSearchNoEventsPanel","form:bipLegendPanelGroup");
	
	
	
	
}	 
function switchSearchResult(displayPanelID,searchNoEventsPanelID,legendPanelID){
	if(document.getElementById(displayPanelID)){
		var table = document.getElementById(displayPanelID).getElementsByTagName('table');
		var obj = document.getElementById(searchNoEventsPanelID);
		if(table.length>0&&(obj!=null)){
			obj.style.display="none";
		}else if(table.length<=0&&(obj!=null)){
			obj.style.display="";
		}
		
		
		var c = document.getElementById(legendPanelID).getElementsByTagName('input');
		var hasEventsFlag = false;
		for (var i = 0; i < c.length; i++) {
				if (c[i].type == 'checkbox') {
					if(c[i].checked){
						hasEventsFlag = true;
					}
				}
		}
		if(hasEventsFlag){
			//do nothing
		}else{
			obj.style.display="none";
		}
		
	}
	
	
	
}
</script>
</head>
<body onload="startTimer();initFocus();initialize()">
<a4j:region>

<a4j:keepAlive beanName="drListView" />
<h:form id="form">
<a4j:commandButton id="refreshButton" style="display:none;">
		<a4j:support event='onclick' oncomplete="initialize();" actionListener="#{drListView.refreshLegend}" reRender="listViewLegendPanel,eventListViewPanel"></a4j:support>
</a4j:commandButton>
<div id="popupWrapper" style="margin: 0 auto;">
  <div class="popupHeader">
    <h1><a href="#" title="Southern California Edison">Southern California Edison</a></h1>
  </div>





  <div class="innerWrapper">
    <div class="contentWrapper">
      <div id="tabViewWrapper">
        <ul id="tabView" role="tablist">
            <li id="tabView1"  role="tab">
				<h:commandLink action="#{drListView.dispatchToMapViewPage}" id="navigateLink">
				<h2>Map View</h2>
				</h:commandLink>
            </li>
            <li id="tabView2"  role="tab">
                <h:commandLink action="#{drListView.dispatchToListViewPage}" styleClass="on" id="tabLink" onkeydown="if((event || window.event).keyCode==9) {accessibilityForTab();return false;}">
				<h2>List View</h2>
				</h:commandLink>
            </li>
        </ul>
      </div>









      




      <div id="tabContentPanel" role="tabpanel">  
	  <div class="radioSelectionsBG">
        	<div class="radioHolder">
				<h:selectOneRadio value="#{drListView.eventStateSelect}"  id="eventStateSelectRadio"  styleClass="radio-buttons" style="vertical-align: middle; margin: 0px;">
					<a4j:support event="onclick" onsubmit="disableAllCheckBox();" oncomplete="uncheckAllCheckBox();enableAllCheckBox();initialize();" action="#{drListView.eventSwitchListener}" reRender="eventListViewPanel,listViewLegendPanel"  ajaxSingle="true"/>
					<f:selectItem itemValue="ActiveEvents" itemLabel="Active Events" id="activeEventsSelectItem" />
					<f:selectItem itemValue="ScheduledEvents" itemLabel="Scheduled Events" id="scheduledEventsSelectItem" />
				</h:selectOneRadio>
			</div>
			
     </div>
	 
      <h:panelGroup id="eventListViewPanel" layout="block"  >   
		<jsp:include page="jsp/component/component_SDPR_listview.jsp" />
		<jsp:include page="jsp/component/component_SDPC_listview.jsp" />
		<jsp:include page="jsp/component/component_API_listview.jsp" />
		<jsp:include page="jsp/component/component_CBP_listview.jsp" />
		<jsp:include page="jsp/component/component_BIP_listview.jsp" />
      </h:panelGroup> 
        
      </div>
     






    </div>




    <div id="sideBar">
      <jsp:include page="jsp/component/component_list_legend.jsp" />




      <div class="sideFooterBg"></div>
      
      <div class="sideHeader" style="margin-top:20px;"></div>
      <h:panelGroup id="filterPanel" layout="block" >
      <div class="sideMiddleBg">
        <div class="selectionTitle"><h:outputLabel value="County" for="selectOneMenu_county"/></div>
        <div class="selectionWrap">
            <h:selectOneMenu    styleClass="sideDropdown" 
                            title="county selection"
                            id="selectOneMenu_county"
                            value="#{drListView.searchCounty}"
                            onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
							immediate="true"
							onchange="countyChangeListener();"
                            valueChangeListener="#{drListView.countyChange}">                     
                            <f:selectItems value="#{drListView.countyItems}"/> 
                            <a4j:support event="onchange"/> 
            </h:selectOneMenu>
        </div>
        
        <div class="clearfix"></div>
        <div class="selectionTitle"><h:outputLabel value="City" for="selectOneMenu_city"/></div>
		
		
        <div class="selectionWrap">
            <h:selectOneMenu    styleClass="sideDropdown"
                                title="city type selection"
                                id="selectOneMenu_city" 
								immediate="true"
								onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
								onchange="cityChangeListener();"
                                value="#{drListView.searchCity}">                     
                                <f:selectItems value="#{drListView.cityItems}"/> 
								 <a4j:support event="onchange"/> 
            </h:selectOneMenu>
            
        </div>
        <div class="clearfix"></div>
        

        <div class="selectionTitle"><h:outputLabel value="Zip Code" for="inputText_zipcode"/></div>
        <div class="selectionWrap">
            <span class="search">
				<h:inputText onchange="zipcodeChangeListener();" style="width:115px;" value="#{drListView.searchZipCode}" id="inputText_zipcode" styleClass="sideZipBox" />
				
            </span>
            <a4j:commandButton  style="float:none;vertical-align:middle;" 
                                title="Search" 
                                alt="Search" 
                                action ="#{drListView.searchFilterAction}" 
                                image="images/icons/sideBarSearch.jpg"
                                reRender="innerCBPPanel,innerAPIPanel,innerBIPPanel,innerSDPRPanel,innerSDPCPanel,warningPanel"
                                ignoreDupResponses="true"
                                limitToList="true"
                                requestDelay="200"
								oncomplete="searchCompleteAction();searchComplete();">
				
            </a4j:commandButton>
            
        </div>

        
        <div style="text-align:right; padding-right:20px;">




            <a4j:commandButton id="clearButton"  reRender="filterPanel,innerAPIPanel,innerBIPPanel,innerSDPRPanel,innerSDPCPanel,warningPanel" style="float:none;vertical-align:middle;" title="Clear All" alt="Clear All" action ="#{drListView.dispatchToListViewPage}" image="images/buttons/clearAll2.jpg">
                
            </a4j:commandButton>
        </div>
        <br/>
        <h:panelGroup id = "warningPanel" layout="block" >
        <h:panelGroup layout="block" rendered="#{drListView.validateErrorFlag}"> 
        <div class="listViewWarning" style="width:228px;">                
            <div class="listViewWarningbox">                            
                <h:messages  id="message" style="font-size: 8px;"
                                globalOnly="false"
                                layout="table"
                                infoClass="global-message-info" 
                                warnClass="global-message-warn" 
                                errorClass="global-message-error" 
                                fatalClass="global-message-fatal"/>                                                         

            </div>          

        </div> 
        </h:panelGroup>





        </h:panelGroup>



        <div class="clearfix"></div>
      </div>      

      </h:panelGroup>
      <div class="sideFooterBg">
      </div>
    </div>

  </div>
  <div class="clearfix"></div>
  		
  <div class="FooterLegalWrapper">
    <p>ZIP Codes listed above indicate some customers within the area are impacted by an event. Not all customers in a listed ZIP Code will necessarily be impacted. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided.</p>
  </div>
  
	<br/>

	
	<a title="close" style="cursor: pointer;float:right" onclick="closeWindow()">Close</a>
	<br/>
	<br/>	
</div>
 </h:form>


</a4j:region>
</body>
</f:view>
</html>
