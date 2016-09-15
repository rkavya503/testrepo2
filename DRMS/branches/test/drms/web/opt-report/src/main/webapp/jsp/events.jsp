	<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="event-events-block">
<richext:dataTable status="waitStatus" id="reportEventsDataTable" value="#{report.events.events}" var="item" rows="20" width="95%">

		<rich:column sortBy="#{programName}" sortable="true">
			<f:facet name="header">
			    <h:outputText value="Program" escape="false"/>
			</f:facet>				
			<h:outputText value="#{item.programName}"></h:outputText>
		</rich:column>

		<rich:column sortBy="#{eventName}" sortable="true">
			<f:facet name="header">
			    <h:outputText value="Event ID" escape="false"/>
			</f:facet>
            <a4j:commandLink value="#{item.eventName}" 
				status="waitStatus"
				action="#{report.events.eventPerformanceAction}"
                reRender="performSummary,reportEventChildPage,allReportEventPage">
                <f:param name="selectedEventName" value="#{item.eventName}"/>
				<f:param name="selectedProgramName" value="#{item.programName}"/>
            </a4j:commandLink>

		</rich:column>

		<rich:column sortBy="#{startTime}" sortable="true">
			<f:facet name="header">
			    <h:outputText value="Start Date / Time" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.startTime}"><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column>

		<rich:column sortBy="#{endTime}" sortable="true">
			<f:facet name="header">
                <h:outputText value="End Date / Time" escape="false"  />
            </f:facet>
			<h:outputText value="#{item.endTime}"><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column>

		<f:facet name="footer">
			<rich:datascroller
                limitToList="true"
                immediate="true"
                rendered="true"
				status="waitStatus"
                for="reportEventsDataTable"/>		
		</f:facet>
    </richext:dataTable>
	<br>
	<h:panelGroup layout="block" >
		<h:commandButton  value="Export Data" title="Export Data" 
			action="#{report.events.exportEventsAction}" 
			disabled="#{report.events.exportDisable}"
			style="border: 1px solid #2BB0FF;"/>
	</h:panelGroup>	
	<h:outputText value="No Events found" styleClass="not-found-msg" 
		rendered="#{report.events.events.totalRowCount==0}"/>
	
	<br><br>
	<rich:tabPanel height="400px" id="reportEventChildPage" selectedTab="#{report.events.selectedTab}" switchType="ajax">
		<rich:tab label="Performance Summary" id="performanceSummaryTab">
			<jsp:include page="eventPerformSummary.jsp" />
		</rich:tab>
		<rich:tab label="Signal Detail" id="signalDetailTab" rendered="#{report.events.displaySignalDetail}">
			<jsp:include page="eventSignalDetail.jsp" />
		</rich:tab>
	</rich:tabPanel>
	
</h:panelGroup>
