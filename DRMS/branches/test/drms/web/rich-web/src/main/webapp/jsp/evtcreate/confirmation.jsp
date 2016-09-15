<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<richext:set beanName="targetParticipants" value="#{evtAdvisor.eventModel.selectedParticipants}"/>
<richext:set beanName="targetClients" value="#{evtAdvisor.eventModel.selectedClients}"/>

<h:form id="confirmForm">
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title"> Confirm </span>
</rich:panel> 
<div  style="margin-left:22px">
<span style="font-weight:bold">Pending Event </span>
<table class="confirm-panel-grid" style="margin-left:5px">
	<tr>
		<td>Program Name:</td>
		<td><h:outputText value="#{evtAdvisor.eventModel.programName}"/></td>
	</tr>
	<tr>
		<td>Event Name:</td>
		<td><h:outputText value="#{evtAdvisor.eventModel.eventName}"/></td>
	</tr>
	<tr>
		<td>Issued At:</td>
		<td><h:outputText value="#{evtAdvisor.eventModel.issueTime}">
				<f:convertDateTime type="date" pattern="#{applicationScope.dateFormat} HH:mm"/>
			</h:outputText>
		</td>
	</tr>
	<tr>
		<td>Starts At:</td>
		<td><h:outputText value="#{evtAdvisor.eventModel.startTime}">
				<f:convertDateTime type="date" pattern="#{applicationScope.dateFormat} HH:mm"/>
			</h:outputText>
		</td>
	</tr>
	<tr>
		<td>Ends At: </td>
		<td><h:outputText value="#{evtAdvisor.eventModel.endTime}">
				<f:convertDateTime type="date" pattern="#{applicationScope.dateFormat} HH:mm"/>
			</h:outputText></td>
	</tr>
	<tr>
		<td>Status:</td>
		<td></td>
	</tr>
</table>
<rich:spacer height="10px" width="50px"/>
<rich:panel rendered="#{evtAdvisor.eventModel.cbpProgramEJB}">
<h:outputText value="Locations: " style="font-weight:bold"></h:outputText>
<h:outputText value="#{evtAdvisor.eventModel.locations}"></h:outputText>
</rich:panel>
<rich:spacer height="10px" width="50px"/>
<table>
	<tr>
		<td>
			<div class="table-title" style="margin-bottom:5px"><h:outputText value="Participants:"/></div>
			<richext:treeTable id="confirmParticipantList" value="#{targetParticipants}" var="item" rows="10000" 
					height="380px" width="455px">
				<richext:treeColumn sortBy="#{participantName}"  width="50%">
						<f:facet name="header">
							   <h:outputText value="Participant Name" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.participantName}"></h:outputText>
				</richext:treeColumn>
				<rich:column sortBy="#{account}"  width="50%">
						<f:facet name="header">
							   <h:outputText value="Account#" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.account}"></h:outputText>
				</rich:column>
			</richext:treeTable>
		</td>
		<td>
			<div class="table-title" style="margin-bottom:5px"><h:outputText value="Clients:"/></div>
			<richext:treeTable id="confirmClienttList" value="#{targetClients}" var="item" rows="10000" 
					height="380px" width="455px">
				<richext:treeColumn sortBy="#{clientName}" width="50%">
						<f:facet name="header">
							   <h:outputText value="Client Name" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.clientName}"></h:outputText>
				</richext:treeColumn>
				<rich:column sortBy="#{parent}" width="50%">
						<f:facet name="header">
							   <h:outputText value="Participant" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.parent}"></h:outputText>
				</rich:column>
			</richext:treeTable>
		</td>
	</tr>
</table>
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
	<a4j:commandButton value="< Back" styleClass="wizard-btn" style="margin-left:490px"
			action="#{evtAdvisor.eventModel.backPage}" title="step back"	
			limitToList="true" reRender="event-creation-wizard"/>
	<a4j:commandButton value="Confirm" styleClass="wizard-btn" 
			action="#{evtAdvisor.eventModel.createEvent}" title="Create event"
			limitToList="true" reRender="event-creation-wizard"/>
	<a4j:commandButton value="Cancel" action="#{evtAdvisor.eventModel.cancel}" immediate="true" title="Cancel"/>
</rich:panel>
</h:form>