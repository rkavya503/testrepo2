<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<richext:set beanName="targetParticipants" value="#{evtAdvisor.eventModel.selectedLocations}"/>

<h:form id="confirmForm">
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title">BIP - Issue Events</span>
</rich:panel> 
<div  style="margin-left:22px">

<rich:spacer height="10px" width="50px"/>
<table width="900px">
	<tr>
		<td>
			<h:outputText style="text-align: left;font-size:11px"   value="Issue Date & Time:"/>&nbsp;&nbsp;
			<h:outputText value="#{evtAdvisor.eventModel.issuedTime!=null?evtAdvisor.eventModel.issuedTime:'-'}" style="text-align: left;font-size:11px">
				<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
			</h:outputText>	
		</td>
		<td><h:graphicImage id="requiredName" alt="required" title="required"  url="/images/layout/required.jpg" />
			<h:outputText style="text-align: left;font-size:11px"   value="Start Date & Time:"/>&nbsp;&nbsp;
			<h:outputText value="#{evtAdvisor.eventModel.startTime}" style="text-align: left;font-size:11px">
				<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
			</h:outputText>	
		</td>
		<td>
			<h:outputText style="text-align: left;font-size:11px"   value="End Date & Time:"/>&nbsp;&nbsp;
            <h:outputText value="#{evtAdvisor.eventModel.endTime!=null?evtAdvisor.eventModel.endTime:'-'}" style="text-align: left;font-size:11px" >
                <f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
            </h:outputText> 
            <h:outputText value="#{evtAdvisor.eventModel.endTime!=null&&demoEvent.estimated==true?'*':''}"  />
		</td>
		</tr>	
		<tr>
		<td colspan="3">
			<table>
			<tr>
			<td><h:outputLabel style="text-align: left;vertical-align: middle;font-size:11px" for="comments" value="Comments:"/></td>
			<td><h:inputTextarea id="comments" value="#{demoEvent.comments}" cols="60" rows="2" readonly="true"/></td>
			</tr>
			</table>
		</td>
	</tr>
</table>
<table width="900px">
	<tr>
		<td>
			<richext:treeTable id="confirmParticipantList" value="#{targetParticipants}" var="item" rows="10000" 
					height="360px" width="100%">
				<rich:column>
					<f:facet name="header">
							<h:outputText value="No." escape="false"/>
					</f:facet>
					 <h:outputText value="#{item.rowIndex+1}"/>  
				</rich:column>				
				<rich:column sortBy="#{location.type}" width="25%">
					<f:facet name="header">
						   <h:outputText value="Dispatch Type" escape="false"/>
					</f:facet>				
					<h:outputText value="#{item.location.type}"></h:outputText>
				</rich:column>
				<rich:column sortBy="#{location.name}" width="50%">
					<f:facet name="header">
						   <h:outputText value="Dispatch Location" escape="false"/>
					</f:facet>				
					<h:outputText value="#{item.location.name}"></h:outputText>
				</rich:column>		
				<rich:column width="20%">
	                <f:facet name="header">
	                       <h:outputText value="Location#" escape="false"/>
	                </f:facet>               
	                <h:outputText value="#{item.location.number}"></h:outputText>
                </rich:column>																				
			</richext:treeTable>
		</td>
	</tr>
</table>
<h:outputText value="* Estimated End Date & Time" />
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
	<a4j:commandButton value="Submit" styleClass="wizard-btn" style="margin-left:15px"
			action="#{evtAdvisor.eventModel.createEvent}" title="Create event"
			limitToList="true" reRender="event-creation-wizard"/>
	<a4j:commandButton value="Edit" styleClass="wizard-btn" 
			action="#{evtAdvisor.eventModel.goToEdit}" title="step back"	
			limitToList="true" reRender="event-creation-wizard"/>
	
	<a4j:commandButton value="Cancel" action="#{evtAdvisor.eventModel.cancel}" immediate="true" title="Cancel"/>
</rich:panel>
</h:form>