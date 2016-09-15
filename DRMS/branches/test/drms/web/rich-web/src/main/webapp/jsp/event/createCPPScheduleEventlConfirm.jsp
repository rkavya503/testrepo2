<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<head>
	<jsp:include page="/jsp/head.jsp" />
</head>

<f:view>
	<jsp:include page="/jsp/header_events.jsp" />
	<h:form>
		
		<rich:spacer height="20px" width="700px" />
		<h:messages layout="table" globalOnly="true" 
			infoClass="global-message-info" warnClass="global-message-warn" 
			errorClass="global-message-error" fatalClass="global-message-fatal"/>
		<rich:panel styleClass="event-rich-panel" >
			Pending Event:

			<h:panelGrid columns="2" styleClass="event-panel-grid" border="0">
			<h:panelGroup>
				<p align="left">Program Name: </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{eventDataModel.programName}"/></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Event Name:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{eventDataModel.eventName}"/></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Issued at:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText  value="#{eventDataModel.issuedTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Starts at:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{eventDataModel.startTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText> </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left">Ends at: </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{eventDataModel.endTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText> </p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Status:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{eventDataModel.state}"/></p>
			</h:panelGroup>			
			</h:panelGrid>

		</rich:panel>
		
		
		

		<rich:spacer height="20px" width="700px" />
        <rich:dataTable id="participants" value="#{eventDataModel.allParticipantsInProgram}" var="item" width="100%" styleClass="event-rich-table ">
			<f:facet name="header">
                <h:outputText value="Participants"></h:outputText>
            </f:facet>
			<rich:column sortable="true" width="50%" sortBy="#{item.participant.participantName}">
				<f:facet name="header">
                    <h:outputText value="Participant" escape="false"  />
                </f:facet>
				<h:outputText  value="#{item.participant.participantName}">
				</h:outputText>
			</rich:column>			
			<rich:column sortable="false" width="40%" >
                <f:facet name="header">
                    <h:outputText value="Account#" escape="false"/>
                </f:facet>
				<h:outputText value="#{item.participant.accountNumber}" />
			</rich:column>
        </rich:dataTable>

		<rich:spacer height="20px" width="700px" /> 
		
        <rich:dataTable id="clients" value="#{eventDataModel.clients}" var="item" width ="100%"
		styleClass="event-rich-table ">		
			
			<f:facet name="header">
                <h:outputText value="Clients"></h:outputText>
            </f:facet>
			
			<rich:column sortable="true" width="40%" sortBy="#{item.accountNumber}">
                <f:facet name="header">
                    <h:outputText value="Client" escape="false"/>
                </f:facet>
				<h:outputText value="#{item.accountNumber}" />
			</rich:column>
			
			<rich:column sortable="false" width="50%" >
				<f:facet name="header">
                    <h:outputText value="Participant" escape="false"  />
                </f:facet>
				<h:outputText  value="#{item.parent}">
				</h:outputText>
			</rich:column>
              
        </rich:dataTable>
		
		<rich:spacer height="30px" width="700px" />
		<div style="text-align: center;">   
			<h:commandButton value="Confirm" action="#{eventDataModel.submitToDB}"  title="Submit and Issue Event"/>
			<h:commandButton value="Cancel" action="#{eventDataModel.cancel}" title="Cancel Submit and Issue Event"/>	
		</div>
	</h:form>
   <jsp:include page="/jsp/footer.jsp" />
</f:view>
</jsp:root>