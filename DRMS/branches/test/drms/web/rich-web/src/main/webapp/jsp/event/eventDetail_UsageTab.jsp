<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ page import="com.akuacom.utils.BuildProperties" %> 

<%
    BuildProperties buildProperties = new BuildProperties();
%>


<f:view>
			<a4j:commandButton id="refreshButton" style="display:none;">
				<a4j:support event='onclick' actionListener="#{eventUsageDataModel.getMatrix}"
				 reRender="usagePanel"></a4j:support>
			</a4j:commandButton>
	<rich:panel styleClass="event-rich-panel">
		<h:outputText value="Usage Graph" style="font-weight:bold"/>
	</rich:panel>
	 <a4j:outputPanel  ajaxRendered="true" id="usagePanel">
	<rich:dataTable
			id="reports"
			width="100%" value="#{eventUsageDataModel}"
				var="p" rows="2"
			styleClass="event-rich-table ">

		<rich:spacer height="30px" width="700px" />
		
		<rich:column >
			<f:facet name="header">
			</f:facet>
			<h:outputText  value="#{eventUsageDataModel.titleLabel}" />
		</rich:column>
		
		<rich:column styleClass="right-column">
			<f:facet name="header">
			   <h:outputText value="Average Baseline(KW)" />
			</f:facet>
			<h:outputText value="#{eventUsageDataModel.baseValue}" >
				<f:convertNumber pattern = "#,##0.000"/>
			</h:outputText>
		</rich:column>
		
		<rich:column styleClass="right-column">
			<f:facet name="header">
				<h:outputText value="Average Usage(KW)" />
			</f:facet>
			<h:outputText value="#{eventUsageDataModel.usageValue}" >
				<f:convertNumber pattern = "#,##0.000"/>
			</h:outputText>
		</rich:column>
		
		 <rich:column styleClass="right-column">
			<f:facet name="header">
			   <h:outputText value="Average Shed(KW)" />
			</f:facet>
			<h:outputText value="#{eventUsageDataModel.shedValue}" >
				<f:convertNumber pattern = "#,##0.000"/>
			</h:outputText>
		</rich:column>
	</rich:dataTable>
		</a4j:outputPanel>
	<rich:spacer></rich:spacer>
	<BR>
	<BR>
	<h:panelGroup layout="block" id="usage-block" >
		<rich:panel styleClass="content-panel">
			<script type="text/javascript" src="swfobject.js"></script>
			<script type="text/javascript">     
				var flashvars = {
				  pdWsURL: "../../pss2-drms-core-ejb-<%=buildProperties.getVersion()%>/UsageDataServicerBean",
				  eventName:"<h:outputText value="#{EventDetailDataModel.event.eventName}" />",
				  isIndividual:"<h:outputText value="#{EventDetailDataModel.individualparticipant}" />",
				  plotLabel: "",
				  showRawData:"true",
				  hideControls: "false",
				  dataSet1: "Baseline,Baseline",
				  dataSet2: "Current Demand,Current Demand",
				  isReport:"false"
				};          
				var params = {};            
				params.wmode = "transparent"; 
				
				swfobject.embedSWF("DRASFlexTelemetryPlot.swf", "usageCurve", "98%", "380", "9.0.0", null, flashvars, params, null);
				
				function refreshByDate(date_param){
					
					document.getElementById("form:refreshButton").onclick();
				} 
			</script>		
			<div id="usageCurve"></div>	
		</rich:panel>	
	</h:panelGroup>
</f:view>
