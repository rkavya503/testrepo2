<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:panelGroup layout="block" >
	<h:outputText value="Signal Detail for Event: #{report.events.selectedEventName} for #{report.events.selectedClientName}" styleClass="form-field"></h:outputText>
	<BR><BR>
	
	<richext:dataTable status="waitStatus" id="reportSignalDetailTable" value="#{report.events.eventSignal}" var="item" width="95%" >

		<rich:column sortable="true" sortBy="#{signalTime}">
			<f:facet name="header">
			    <h:outputText value="Time" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.signalTime}"><f:convertDateTime pattern="#{report.dateTimeFormat}"/></h:outputText>
		</rich:column>
		<rich:column rendered="#{report.events.bidColDisplay}" sortable="true" sortBy="#{bid}">
			<f:facet name="header">
			    <h:outputText value="Bid" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.bid}"></h:outputText>
		</rich:column>
		<rich:column rendered="#{report.events.modeColDisplay}" sortable="true" sortBy="#{mode}">
			<f:facet name="header">
			    <h:outputText value="Mode" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.mode}"></h:outputText>
		</rich:column>
		<rich:column rendered="#{report.events.priceColDisplay}" sortable="true" sortBy="#{price}">
			<f:facet name="header">
			    <h:outputText value="Price" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.price}"></h:outputText>
		</rich:column>

		<rich:column rendered="#{report.events.cppPriceColDisplay}" sortable="true" sortBy="#{CPPPrice}">
			<f:facet name="header">
			    <h:outputText value="CPP Price" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.CPPPrice}"></h:outputText>
		</rich:column>

		<rich:column rendered="#{report.events.pendingColDisplay}" sortable="true" sortBy="#{pending}">
			<f:facet name="header">
			    <h:outputText value="Status" escape="false"/>
			</f:facet>
			<h:outputText value="#{item.pending}"></h:outputText>			
		</rich:column>
    </richext:dataTable>

	<br>
	<h:panelGroup layout="block" >
		<h:commandButton  value="Export Data" title="Export Data" 
			action="#{report.events.exportEventSignalAction}" 
			disabled="#{report.events.exportSignalDisable}"
			style="border: 1px solid #2BB0FF;"/>
	</h:panelGroup>	
	
	<h:outputText value="No Signals Received for the client" styleClass="not-found-msg" 
		rendered="#{report.events.eventSignal.totalRowCount==0}"/>

</h:panelGroup>

