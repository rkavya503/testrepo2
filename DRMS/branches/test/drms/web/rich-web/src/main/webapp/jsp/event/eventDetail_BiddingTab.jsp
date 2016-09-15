<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich"
    xmlns:h="http://java.sun.com/jsf/html" version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <h:form>
        <a4j:outputPanel  ajaxRendered="true" id="biddingPanel">
            <rich:dataTable
                id="bids"
                value="#{EventDetailDataModel.biddingParticipantsInEvent}"
                var="eventParticipantDataModel"
                width="100%"
                styleClass="content-tab-table">

            <rich:spacer height="30px" width="700px" />
                <rich:column width="2%" visible="#{EventDetailDataModel.drasBidding}">
                	<h:selectBooleanCheckbox value="#{eventParticipantDataModel.bidSelected}"/>
                </rich:column>
                <rich:column sortBy="#{eventParticipantDataModel.participant.participantName}" sortOrder="ASCENDING">
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{eventParticipantDataModel.participant.participantName}"/>
                </rich:column>
                <rich:column sortBy="#{eventParticipantDataModel.participant.accountNumber}">
                    <f:facet name="header">
                        <h:outputText value="Account#" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{eventParticipantDataModel.participant.accountNumber}" />
                </rich:column>
                <rich:columns value="#{EventDetailDataModel.bidBlocks}" var="bid" index="ind">
	                <f:facet name="header">
	                    <h:outputText value="#{bid}" />
	                </f:facet>
                    <h:outputText value="#{eventParticipantDataModel.bids[ind]}" />
                </rich:columns>
                <rich:column sortBy="#{eventParticipantDataModel.bidStatus}">
                    <f:facet name="header">
                        <h:outputText value="Status" escape="false"/>
                    </f:facet>
                    <h:outputText value="#{eventParticipantDataModel.bidStatus}" />
                </rich:column>
                <rich:column >
                    <f:facet name="header">
                        <h:outputText value="Actions" escape="false"/>
                    </f:facet>
					<a4j:commandButton 
						value="Edit Bids"
						title="Edit Bids"
						oncomplete="#{rich:component('editBidsPanel')}.show()"
						reRender="editBidsPanel"
						rendered="#{EventDetailDataModel.bidsEditable}"
						style="border: 1px solid #2BB0FF;"
						disabled="#{!EventDetailDataModel.canEditBid}">
						<a4j:actionparam 
							name="bids"
							value="#{eventParticipantDataModel.participant.participantName}"
							assignTo="#{EventDetailDataModel.editParticipantName}" />
					</a4j:commandButton>
                </rich:column>
                <f:facet name="footer">
                    <rich:columnGroup>
                        <rich:column>
                            <h:outputText value="Total" escape="false"/>
                        </rich:column>
                        <rich:column rendered="#{EventDetailDataModel.drasBidding}"/>
                        <rich:column/>
		                <rich:columns value="#{EventDetailDataModel.bidBlocks}" var="bid" index="ind">
		                	<h:outputText value="#{EventDetailDataModel.reductionTotals[ind]}" escape="false"  />
		                </rich:columns>
	                    <rich:column/>
	                    <rich:column/>
                    </rich:columnGroup>
	        	</f:facet>        
            </rich:dataTable>
			<rich:spacer height="2px" />

			<div id="fm-submit">
				<h:commandButton title="Send" value="Send" 
					action="#{EventDetailDataModel.sendBidsAction}" 
					rendered="#{EventDetailDataModel.drasBidding}"/>
				<rich:spacer width="20px" />
				<h:commandButton title="Accept" value="Accept" 
					action="#{EventDetailDataModel.acceptBidsAction}"
					rendered="#{EventDetailDataModel.drasBidding}" />
				<rich:spacer width="20px" />
				<h:commandButton title="Accept All" value="Accept All" 
					action="#{EventDetailDataModel.acceptAllBidsAction}" 
					rendered="#{EventDetailDataModel.drasBidding}"/>
				<rich:spacer width="20px" />
				<h:commandButton title="Reject" value="Reject" 
					action="#{EventDetailDataModel.rejectBidsAction}" 
					rendered="#{EventDetailDataModel.drasBidding}"/>
				<rich:spacer width="20px" />
				<h:commandButton title="Reject All" value="Reject All" 
					action="#{EventDetailDataModel.rejectAllBidsAction}" 
					rendered="#{EventDetailDataModel.drasBidding}"/>
				<rich:spacer width="20px" />
				<h:commandButton title="Next State" value="Next State" 
					action="#{EventDetailDataModel.nextStateBidsAction}" 
					rendered="#{EventDetailDataModel.drasBidding}"/>
			</div>
        </a4j:outputPanel>
    </h:form>
    
	<rich:modalPanel id="editBidsPanel" autosized="false" keepVisualState="false"
		width="450" height="#{EventDetailDataModel.bidsPanelHeight}">
		<f:facet name="header">
			<h:outputText value="Edit Bids" />
		</f:facet>
		<h:form>
           <rich:dataTable
                id="bids"
                value="#{EventDetailDataModel.editableBids}"
                var="editableBid"
                styleClass="content-tab-table">
                <rich:column>
                    <f:facet name="header">
                        <h:outputText value="Time" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{editableBid.timeBlock}" />
                </rich:column>
                <rich:column>
                    <f:facet name="header">
                        <h:outputText value="Bid" escape="false"/>
                    </f:facet>
					<h:inputText 
						id="newBid" 
						value="#{editableBid.reduction}"
						label="#{editableBid.reduction}"
						maxlength="10" size="10">
					</h:inputText>
					<h:message for="newBid" errorClass="message-error" />
                </rich:column>
            </rich:dataTable>
            <br/>
 			<a4j:commandButton value="Save"
				action="#{EventDetailDataModel.editBidsAction}"
				oncomplete="#{rich:component('editBidsPanel')}.hide();" 
				style="border: 1px solid #2BB0FF;"/>
			<a4j:commandButton value="Cancel"
				onclick="#{rich:component('editBidsPanel')}.hide(); return false" 
				style="border: 1px solid #2BB0FF;"/>
		</h:form>
	</rich:modalPanel>

</jsp:root>