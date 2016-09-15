<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.akuacom.pss2.facdash.FDUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.akuacom.utils.BuildProperties" %> 

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<jsp:include page="clear-session.jsp" />
<%
    String partName = FDUtils.getParticipantName();
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
	<div id="frame"><h:form id="navForm">
		<jsp:include page="header.jsp" />
		<rich:panel styleClass="content-panel">
			<h:panelGrid id="panel" columns="2">
				<h:panelGroup>
					<a4j:region>
						<a4j:poll interval="60000" reRender="events" enabled="true" />
						<rich:extendedDataTable id="events" rows="5"
							value="#{events.events}" var="event" reRender="ds"
							styleClass="content-table-narrow" height="210px" width="390px">
							<f:facet name="header">
								<h:outputText value="Active and Upcoming Events.." />
							</f:facet>
							<rich:column width="90px" sortBy="#{event.name}">
								<f:facet name="header">
									<h:outputText value="Name" title="Name" />
								</f:facet>
								<h:outputText value="#{event.name}" />
							</rich:column>
							<rich:column width="90px" sortBy="#{event.programName}">
								<f:facet name="header">
									<h:outputText value="Program" title="Program" />
								</f:facet>
								<h:outputText value="#{event.programName}" />
							</rich:column>
							<rich:column width="85px" sortBy="#{event.start}">
								<f:facet name="header">
									<h:outputText value="Start" title="Start" />
								</f:facet>
								<h:outputText value="#{event.start}">
									<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}" />
								</h:outputText>
							</rich:column>
							<rich:column width="60px" sortBy="#{event.status}">
								<f:facet name="header">
									<h:outputText value="Status" title="Status" />
								</f:facet>
								<h:outputText value="#{event.status}" />
							</rich:column>
							<f:facet name="footer">
								<rich:datascroller id="ds"></rich:datascroller>
							</f:facet>
							<rich:column width="65px">
								<f:facet name="header">
									<h:outputText value="Actions" title="Actions" />
								</f:facet>
								<h3><h:commandButton value="Opt-out"
									action="#{status.optoutAction}"
									actionListener="#{status.optoutListener}">
									<f:attribute name="eventName" value="#{event.name}" />
								</h:commandButton></h3>
							</rich:column>
						</rich:extendedDataTable>
					</a4j:region>



					<rich:spacer height="10px" />

					<a4j:region>
						<a4j:poll interval="60000" reRender="clients" enabled="true" />
						<rich:extendedDataTable id="clients" rows="5"
							value="#{clients.clients}" var="client" reRender="ds"
							styleClass="content-table-narrow" height="210px" width="390px">
							<f:facet name="header">
								<h:outputText value="Clients" title="Clients" />
							</f:facet>
							<rich:column width="100px" sortBy="#{client.name}">
								<f:facet name="header">
									<h:outputText value="Name" title="Name" />
								</f:facet>
								<h:outputText value="#{client.name}" title="#{client.name}" />
							</rich:column>
							<rich:column width="100px" sortBy="#{client.eventStatus}">
								<f:facet name="header">
									<h:outputText value="Pending" title="Pending" />
								</f:facet>
								<h:outputText value="#{client.eventStatus}" />
							</rich:column>
							<rich:column width="100px" sortBy="#{client.mode}">
								<f:facet name="header">
									<h:outputText value="Mode" title="Mode" />
								</f:facet>
								<h:outputText value="#{client.mode}" />
							</rich:column>
							<rich:column width="90px" sortBy="#{client.commStatus}">
								<f:facet name="header">
									<h:outputText value="Comm Status" title="Comm Status" />
								</f:facet>
								<h:outputText value="#{client.commStatus}" />
							</rich:column>
							<f:facet name="footer">
								<rich:datascroller id="ds"></rich:datascroller>
							</f:facet>
						</rich:extendedDataTable>
					</a4j:region>

				</h:panelGroup>
				<rich:panel styleClass="internal-panel-narrow">
					<f:facet name="header">
						<h:outputText value="Today's Usage" title="Today's Usage" />
					</f:facet>
					<script src="AC_OETags.js" language="javascript"></script>
					<script language="JavaScript" type="text/javascript">
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

	AC_FL_RunContent(
			"src", "DRASFlexTelemetryPlot",
			"width", "390",
			"height", "400",
			"align", "middle",
			"id", "pss2",
			"quality", "high",
			"bgcolor", "#869ca7",
			"name", "DRASParticipants",
            "FlashVars", "pdWsURL=../../pss2-drms-core-ejb-<%=buildProperties.getVersion()%>/UsageDataServicerBean&participantName=<%=partName%>&serverTime=<%=serverTime%>&timezoneOffset=<%=timezoneOffset%>&plotLabel=&hideControls=true&dataSet1=Projected Normal Usage,Projected Normal Usage&dataSet2=Current Usage,Current Usage",
			"allowScriptAccess","sameDomain",
			"type", "application/x-shockwave-flash",
			"pluginspage", "http://www.adobe.com/go/getflashplayer"
	);
	
  } else {  // flash is too old or we can't detect the plugin
    var alternateContent = 'Alternate HTML content should be placed here. '
  	+ 'This content requires the Adobe Flash Player. '
   	+ '<a href=http://www.adobe.com/go/getflash/>Get Flash</a>';
    document.write(alternateContent);  // insert non-flash content
  }
  -->
								</script>
				</rich:panel>
			</h:panelGrid>


		</rich:panel>
	</h:form> <jsp:include page="footer.jsp" /></div>
	</body>
</f:view>
</html>
