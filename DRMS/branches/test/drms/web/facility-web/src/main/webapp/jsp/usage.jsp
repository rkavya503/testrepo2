<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.akuacom.pss2.facdash.Header1" %>
<%@ page import="com.akuacom.pss2.facdash.FDUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.akuacom.utils.BuildProperties" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%
	String userName = FDUtils.getParticipantName();
	int timezoneOffset = (new Date()).getTimezoneOffset()/60;
    long serverTime = (new Date()).getTime();
    BuildProperties buildProperties = new BuildProperties();

%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />

		</head>
		<body>
			<div id="frame">
			<h:form id="form">
		<jsp:include page="header.jsp" />
			<h:inputHidden id="date" value="#{usageTable.date}"  />
			<h3><a4j:commandButton id="refreshButton" style="display:none;">
				<a4j:support event='onclick' actionListener="#{usageTable.getUsageByDate}"
				 ></a4j:support>
			</a4j:commandButton></h3>


   <rich:panel styleClass="content-panel">
           <script src="AC_OETags.js" language="javascript"></script>
           <script language="JavaScript" type="text/javascript">
<!--
// -----------------------------------------------------------------------------
// Globals
// Major version of Flash required
var requiredMajorVersion = 9;
// Minor version of Flash required
var requiredMinorVersion = 0;
// Minor version of Flash required
var requiredRevision = 28;

// Version check for the Flash Player that has the ability to start Player Product Install (6.0r65)
var hasProductInstall = DetectFlashVer(6, 0, 65);

// Version check based upon the values defined in globals
var hasRequestedVersion = DetectFlashVer(requiredMajorVersion, requiredMinorVersion, requiredRevision);

if ( hasProductInstall && !hasRequestedVersion ) {
	// DO NOT MODIFY THE FOLLOWING FOUR LINES
	// Location visited after installation is complete if installation is required
	var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
	var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

	AC_FL_RunContent(
		"src", "playerProductInstall",
		"FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
		"width", "100%",
		"height", "100%",
		"align", "middle",
		"id", "pss2",
		"quality", "high",
		"bgcolor", "#869ca7",
		"name", "pss2",
		"allowScriptAccess","sameDomain",
		"type", "application/x-shockwave-flash",
		"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
} else if (hasRequestedVersion) {
	// if we've detected an acceptable version
	// embed the Flash Content SWF when all tests are passed
	var pname = '<h:outputText value="#{usageTable.participantName}" />';
	var individualparticipant = '<h:outputText value="#{usageTable.individualparticipant}" />';
	AC_FL_RunContent(
			"src", "DRASFlexTelemetryPlot",
			"width", "100%",
			"height", "680",
			"align", "middle",
			"id", "pss2",
			"quality", "high",
			"bgcolor", "#869ca7",
			"name", "DRASParticipants",
 			"FlashVars", "pdWsURL=../../pss2-drms-core-ejb-<%=buildProperties.getVersion()%>/UsageDataServicerBean&participantName="+pname+"&isIndividual="+individualparticipant+"&serverTime=<%=serverTime%>&timezoneOffset=<%=timezoneOffset%>&plotLabel=&hideControls=false&showRawData=true&dataSet1=Projected Normal Usage,Projected Normal Usage&dataSet2=Current Demand,Current Demand&isReport=true&isDemo=false",
 			"allowScriptAccess","sameDomain",
            "wmode", "opaque",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);

	function refreshByDate(date_param){
		document.getElementById("form:date").value = date_param;
		document.getElementById("form:refreshButton").onclick();
	} 

  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
  -->
						</script>
					</rich:panel>
				</h:form>
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
