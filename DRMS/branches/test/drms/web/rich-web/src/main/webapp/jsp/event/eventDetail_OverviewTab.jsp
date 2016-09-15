<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <h:form>
        <h:panelGrid columns="2" styleClass="event-panel-grid" cellpadding="5">
            <h:outputText>Event Name</h:outputText>
            <h:outputText value="#{EventDetailDataModel.event.eventName}"/>
            <h:outputText>Program Name</h:outputText>
            <h:outputText value="#{EventDetailDataModel.event.programName}"/>
            <h:outputText>Estimated Shed</h:outputText>
            <h:outputText value="#{EventDetailDataModel.estimatedShed}"/>
            <h:outputText>Current Shed</h:outputText>
            <h:outputText value="#{EventDetailDataModel.currentShed}"/>
            <h:outputText>Issued At</h:outputText>
            <h:outputText value="#{EventDetailDataModel.issuedTime}">
				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
            </h:outputText>

            <h:outputText rendered="#{EventDetailDataModel.dbp}">Participant Respond By</h:outputText>
            <h:outputText value="#{EventDetailDataModel.drasRespondBy}">
				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
            </h:outputText>
            <h:outputText rendered="#{EventDetailDataModel.dbp}">Operator Respond By</h:outputText>
            <h:outputText value="#{EventDetailDataModel.respondBy}">
				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
            </h:outputText>

            <h:outputText>Starts At</h:outputText>
            <h:outputText value="#{EventDetailDataModel.startTime}">
				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
            </h:outputText>
            <h:outputText>Ends At</h:outputText>
            <h:outputText value="#{EventDetailDataModel.endTime}">
				<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
            </h:outputText>
            <h:outputText>Status</h:outputText>
            <h:outputText value="#{EventDetailDataModel.state}"/>
			<h:outputText>Shed Amount</h:outputText>
            <h:outputText value="#{EventDetailDataModel.shedAmount}"/>
        </h:panelGrid>
     </h:form>
        
</jsp:root>