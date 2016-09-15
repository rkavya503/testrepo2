<%@ page import="java.util.Date"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<rich:dataTable value="#{client.endPoints}" var="resourceEndPoints" style="display:none"/>

<rich:dataTable id="clients" rows="1"
    rendered="#{!client.dataUsageEnabled}"
	value="#{client.clients}" var="aClient"
	styleClass="content-client-table">
	
	<rich:column width="17%" >
		<f:facet name="header">
			<h:outputText value="Client" title="Client" />
		</f:facet>
	<h3>
	<h:outputText value="#{aClient.name}" />
	</h3>
	</rich:column>
	<rich:column width="16%">
		<f:facet name="header">
			<h:outputText value="Type" title="Type" />
		</f:facet>
		<h:outputText value="#{aClient.clientType}" />
	</rich:column>
	<rich:column width="16%">
		<f:facet name="header">
			<h:outputText value="Pending" title="Pending" />
		</f:facet>
		<h:outputText value="#{aClient.eventStatus}" />
	</rich:column>
	<rich:column width="17%">
		<f:facet name="header">
			<h:outputText value="Mode" title="Mode" />
		</f:facet>
		<h:outputText value="#{aClient.mode}" />
	</rich:column>
	<rich:column width="17%">
		<f:facet name="header">
			<h:outputText value="Last Contact" title="Last Contact" />
		</f:facet>
		<h:outputText value="#{aClient.lastContact}">
			<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
		</h:outputText>
	</rich:column>
	<rich:column width="17%">
		<f:facet name="header">
			<h:outputText value="Comm Status" title="Comm Status" />
		</f:facet>
		<h:outputText value="#{aClient.commStatus}" />
	</rich:column>
	<rich:column width="17%">
		<f:facet name="header">
			<h:outputText value="Telemetry History" title="Telemetry History" />
		</f:facet>
		<a4j:commandButton onclick="jQuery('.resourceVenId').val('#{resourceEndPoints.venId}');jQuery('.resourceName').val('#{aClient.name}');#{rich:component('telemetry_history_dialog')}.show();return false;" value="View"> </a4j:commandButton>
	</rich:column>		
</rich:dataTable>

<rich:dataTable id="clients2" rows="1"
    rendered="#{client.dataUsageEnabled}"
    value="#{client.clients}" var="aClient"
    styleClass="content-client-table">	
    <rich:column  >
        <f:facet name="header">
            <h:outputText value="Client" title="Client" />
        </f:facet>
    <h3>
    <h:outputText value="#{aClient.name}" />
    </h3>
    </rich:column>
    <rich:column>
        <f:facet name="header">
            <h:outputText value="Type" title="Type" />
        </f:facet>
        <h:outputText value="#{aClient.clientType}" />
    </rich:column>
    <rich:column>
        <f:facet name="header">
            <h:outputText value="Pending" title="Pending" />
        </f:facet>
        <h:outputText value="#{aClient.eventStatus}" />
    </rich:column>
    <rich:column >
        <f:facet name="header">
            <h:outputText value="Mode" title="Mode" />
        </f:facet>
        <h:outputText value="#{aClient.mode}" />
    </rich:column>
    <rich:column >
        <f:facet name="header">
            <h:outputText value="Last Contact" title="Last Contact" />
        </f:facet>
        <h:outputText value="#{aClient.lastContact}">
            <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
        </h:outputText>
    </rich:column>
    <rich:column>
        <f:facet name="header">
            <h:outputText value="Comm Status" title="Comm Status" />
        </f:facet>
        <h:outputText value="#{aClient.commStatus}" />
    </rich:column>
    <rich:column >
        <f:facet name="header">
            <h:outputText value="Obix Last Contact" title="Last Contact" />
        </f:facet>
        <h:outputText value="#{aClient.lastUsageContact}">
            <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
        </h:outputText>
    </rich:column>
    <rich:column>
        <f:facet name="header">
            <h:outputText value="Obix Comm Status" title="Comm Status" />
        </f:facet>
        <h:outputText value="#{aClient.usageCommStatus}" />
    </rich:column>
    <rich:column width="17%">
		<f:facet name="header">
			<h:outputText value="Telemetry History" title="Telemetry History" />
		</f:facet>
		<a4j:commandButton onclick="jQuery('.resourceVenId').val('#{resourceEndPoints.venId}');jQuery('.resourceName').val('#{aClient.name}');#{rich:component('telemetry_history_dialog')}.show();return false;" value="View"> </a4j:commandButton>
	</rich:column>	
</rich:dataTable>
<rich:modalPanel id="telemetry_history_dialog" domElementAttachment="form" keepVisualState="false" height="300" width="450">
      	<f:facet name="header">
			<h:outputText value="Telemetry History" title="Telemetry History" />
		</f:facet>
        <f:facet name="controls">
         <h:commandLink action = "#{ResourceStatusModel.resetAll}">
             <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('telemetry_history_dialog')"/>
         </h:commandLink>
         </f:facet>
		<table id="dynamic-filter-body" class="toggle-body">
			<tr>
				<td>
				
					<h:panelGrid id = "resourceStartDt" styleClass="form-input" columns="1" >
						<h:panelGroup>
						<h:inputText  styleClass="resourceName" value="#{ResourceStatusModel.resourceName}" style="display:none"/>
						<h:inputText  styleClass="resourceVenId" value="#{ResourceStatusModel.resourceVenId}" style="display:none"/>
							<h:outputLabel  value="Start Date: " styleClass="form-field"/>
							<br>
	                   		<rich:calendar id="startDateCalRes" popup="true" cellHeight="22px" inputStyle="width:80px;margin-left:5px;"
							     value="#{ResourceStatusModel.startDate}" showApplyButton="false" cellWidth="24px" enableManualInput="true"
							         >
							     <a4j:support event="onchanged" ajaxSingle="true" reRender="startDateCalRes" /> 
							</rich:calendar>							
						</h:panelGroup>
					</h:panelGrid>
				</td>
				<td>
					<h:panelGrid id = "resourceEndDt" styleClass="form-input" columns="1" >
						<h:panelGroup>
							<h:outputLabel value="End Date: " styleClass="form-field"/>
							<br>
							<rich:calendar id="endDateCalRes" popup="true" cellHeight="22px" inputStyle="width:80px;margin-left:5px;"
							     value="#{ResourceStatusModel.endDate}" showApplyButton="false" cellWidth="24px" enableManualInput="true"
							         >
							     <a4j:support event="onchanged" ajaxSingle="true" reRender="endDateCalRes" /> 
							</rich:calendar>
						</h:panelGroup>
					</h:panelGrid>
				</td>
				<td>
				   <a4j:commandButton id="showHistory" style="float:right;margin:5px" value="Show History" action="#{ResourceStatusModel.getResourceHistory}" reRender="telemetry_history_table">
	                </a4j:commandButton>
				</td>	
				<td>
				<a4j:commandButton id="currentStatus" style="float:right;margin:5px" value="Current Status" action="#{ResourceStatusModel.getLatestStatus}" reRender="telemetry_history_table">
	                </a4j:commandButton>
	                </td>			
			</tr>
		</table>
		<rich:dataTable id="telemetry_history_table"  value="#{ResourceStatusModel.resourceHistoryList}" var="resource" columns="2" styleClass="content-client-table">
			<rich:column width="50%" >
			<f:facet name="header">
				<h:outputText value="Status" title="Status" />
			</f:facet>
			<h3>
			<h:outputText value="#{ resource.onlineStatus ? 'ONLINE': 'OFFLINE'}" />
			</h3>
			</rich:column>
			<rich:column width="50%">
			<f:facet name="header">
				<h:outputText value="Timestamp" title="Timestamp" />
			</f:facet>
			<h:outputText value="#{resource.reportedTime}" />
			</rich:column>
		</rich:dataTable>
</rich:modalPanel>
