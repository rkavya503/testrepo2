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
	<h:form>
		<jsp:include page="/jsp/header_events.jsp" />
		<h:messages layout="table" globalOnly="true" 
			infoClass="global-message-info" warnClass="global-message-warn" 
			errorClass="global-message-error" fatalClass="global-message-fatal"/>
		<rich:spacer height="20px" width="700px" />
		<rich:panel styleClass="event-rich-panel" >
			Pending Event:

			<h:panelGrid columns="2" styleClass="event-panel-grid" border="0">
			<h:panelGroup>
				<p align="left">Program Name: </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{dbpNoBidEventDataModel.programName}"/></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Event Name:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{dbpNoBidEventDataModel.eventName}"/></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Issued at:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText  value="#{dbpNoBidEventDataModel.issuedTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText></p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Starts at:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{dbpNoBidEventDataModel.startTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText> </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left">Ends at: </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{dbpNoBidEventDataModel.endTime}">
				<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/> </h:outputText> </p>
			</h:panelGroup>

			<h:panelGroup>
				<p align="left">Status:  </p>
			</h:panelGroup>
			
			<h:panelGroup>
				<p align="left"><h:outputText value="#{dbpNoBidEventDataModel.state}"/></p>
			</h:panelGroup>			
			</h:panelGrid>

		</rich:panel>
		
		<rich:spacer height="20px" width="700px" />
		
		<rich:dataTable id="bids" value="#{dbpNoBidEventDataModel.currentBids}" var="item" width="100%" cellspacing="0" cellpadding="0"
		 styleClass="event-rich-table">		
			<f:facet name="header">
                <h:outputText value="Current Bid Status"></h:outputText>
            </f:facet>
			<rich:column sortable="true" width="120px" sortBy="#{item.participantName}">
				<f:facet name="header">
                    <h:outputText value="Participant" escape="false"  />
                </f:facet>
				<h:outputText  value="#{item.participantName}">
				</h:outputText>
			</rich:column>
			<rich:column sortable="true" width="120px" sortBy="#{item.accountNumber}">
                <f:facet name="header">
                    <h:outputText value="Account#" escape="false"/>
                </f:facet>
				<h:outputText value="#{item.accountNumber}" />
			</rich:column>
			
			<rich:columns var="blockTime" index="index" value="#{dbpNoBidEventDataModel.blockTimes}" width="100px">

					<f:facet name="header">

						<h:outputText value="#{blockTime}" />

					</f:facet>
					
					<h:outputText value="#{item.reductions[index]}" />
					
			</rich:columns>
			
			<f:facet name="footer" >
                <rich:dataTable value="#{dbpNoBidEventDataModel}" var="model" width="100%">
					<rich:column sortable="false" width="119px">
						<h:outputText  value="Total KW"/>
					</rich:column>
					<rich:column sortable="false" width="120px">
						<h:outputText  value=""/>
					</rich:column>
					<rich:columns var="blockTime" index="index" value="#{dbpNoBidEventDataModel.blockTimes}" width="100px">
						<h:outputText value="#{model.reductionTotals[index]}" />
					</rich:columns>
				</rich:dataTable>
            </f:facet>
        </rich:dataTable>

		<rich:spacer height="20px" width="700px" />  
		
		<rich:dataTable id="clients" value="#{dbpNoBidEventDataModel.clients}" var="item" width ="100%"
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
			<h:commandButton value="Confirm" action="#{dbpNoBidEventDataModel.submitToDB}" title="Submit and Issue Event"/>
			<h:commandButton value="Cancel" action="#{dbpNoBidEventDataModel.cancel}" title="Cancel Submit and Issue Event"/>	
		</div>
	</h:form>
   <jsp:include page="/jsp/footer.jsp" />
</f:view>
</jsp:root>