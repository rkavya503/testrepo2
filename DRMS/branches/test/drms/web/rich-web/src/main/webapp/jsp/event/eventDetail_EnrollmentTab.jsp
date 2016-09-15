<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <h:form>
        <a4j:outputPanel  ajaxRendered="true" id="tableEnrollPanel">
        
            <rich:dataTable
                id="bids"
                value="#{EventDetailDataModel.optInParticipants}"
                var="item"
                width="100%"
                styleClass="content-tab-table">

            <rich:spacer height="30px" width="700px" />
		       <rich:column>
					<f:facet name="header">
	                	<h:outputText value="Select" escape="false"  />
					</f:facet>
			        <h:selectBooleanCheckbox value="#{item.delete}" title="Select" disabled="#{!EventDetailDataModel.canEditBid}"/>
		   		</rich:column>
                <rich:column width="2%" visible="#{EventDetailDataModel.drasBidding}">
                	<h:selectBooleanCheckbox value="#{item.bidSelected}" disabled="#{!EventDetailDataModel.canEditBid}"/>
                </rich:column>
                <rich:column sortBy="#{item.participant.participantName}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participant.participantName}"/>
                </rich:column>
                <rich:column sortBy="#{item.participant.accountNumber}">
                    <f:facet name="header">
                        <h:outputText value="Account#" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participant.accountNumber}" />
                </rich:column>
                <rich:column sortBy="#{item.participantRegistedShed}">
                    <f:facet name="header">
                        <h:outputText value="Registered Shed (KW)" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participantRegistedShed}">
                        <f:convertNumber pattern="#0.0"/>
                    </h:outputText>
                </rich:column>
                 <rich:column sortBy="#{item.participantAvilableShed}">
                    <f:facet name="header">
                        <h:outputText value="Available Shed (KW)" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{item.participantAvilableShed}">
                        <f:convertNumber pattern="#0.0"/>
                    </h:outputText>
                </rich:column>
                <rich:columns value="#{EventDetailDataModel.bidBlocks}" var="bid" index="ind" style="width:7%">
	                <f:facet name="header">
	                    <h:outputText value="#{bid}" />
	                </f:facet>
                    <h:inputText value="#{item.bids[ind]}" style="width:90%" converterMessage="Not a valid number" disabled="#{!EventDetailDataModel.canEditBid}"/>
                </rich:columns>
            </rich:dataTable>
			<rich:spacer height="2px" />
			
			<a4j:commandButton value="Add Participants" reRender="tableEnrollPanel"
				action="#{EventDetailDataModel.optInAction}" 
				style="border: 1px solid #2BB0FF;"
				disabled="#{!EventDetailDataModel.canEditBid}"/>
			
        </a4j:outputPanel>
    </h:form>
</jsp:root>