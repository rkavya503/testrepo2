<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:form id="timing-form">
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title"> Event Timing </span>
</rich:panel> 

<rich:panel styleClass="content-panel" id ="event_timing_panel" style="margin-left:10px">
	<richext:set beanName="evtTiming" value="#{evtAdvisor.eventModel.eventTiming}"/>
	<h:panelGrid columns="3" styleClass="event-panel-grid">
		 <h:panelGroup>
               <p align="left">Issue Date:</p>
		</h:panelGroup>
		<h:panelGroup>
			<rich:calendar value="#{evtTiming.issuedTime}"  id="issuedDateCalendar"
						   cellHeight="22px" style="width:180px" 
						   datePattern="#{applicationScope.dateFormat}"/>
		</h:panelGroup>
		<h:outputText style="color: #3C78B5; font-size:11px;font-weight:normal" value="Leave issue date blank for immediate issue"/>
		
		<h:panelGroup>
			<p align="left">Issue Time:</p>
		</h:panelGroup>
		<h:panelGroup>
		   <a4j:outputPanel>
					<h:selectOneMenu value="#{evtTiming.issuedHour}">
						<f:selectItems value="#{evtTiming.hourList}"/>
					</h:selectOneMenu>
					:
					<h:selectOneMenu value="#{evtTiming.issuedMin}">
						<f:selectItems value="#{evtTiming.minList}"/>
					</h:selectOneMenu>
					:
					<h:selectOneMenu value="#{evtTiming.issuedSec}">
						<f:selectItems value="#{evtTiming.secList}"/>
					</h:selectOneMenu>
		  </a4j:outputPanel>
		</h:panelGroup>

		<rich:spacer height="30px" width="100px" />
		
		<h:panelGroup>
      <h:panelGroup rendered="#{not evtAdvisor.eventModel.spanDays}">
        <p align="left">Event Date:</p>
      </h:panelGroup>
      <h:panelGroup rendered="#{evtAdvisor.eventModel.spanDays}">
        <p align="left">Start Date:</p>
      </h:panelGroup>
		</h:panelGroup>
		<h:panelGroup>
			<rich:calendar value="#{evtTiming.eventDate}" id="eventDateCalendar"
						   required="true" requiredMessage="Required"  popup="true"
						   showApplyButton="false" datePattern="#{applicationScope.dateFormat}" cellWidth="24px"
						   cellHeight="22px" style="width:180px"
						   enableManualInput="true"
						   />
		</h:panelGroup>
		<h:message for="eventDateCalendar" errorClass="message-error"  />


		<h:panelGroup>
			<p align="left">Start Time:</p>
		</h:panelGroup>
		<h:panelGroup>
			<h:selectOneMenu value="#{evtTiming.startHour}">
				<f:selectItems value="#{evtTiming.hourList}"/>
			</h:selectOneMenu>
			:
			<h:selectOneMenu value="#{evtTiming.startMin}">
				<f:selectItems value="#{evtTiming.minList}"/>
			</h:selectOneMenu>
			:
			<h:selectOneMenu value="#{evtTiming.startSec}">
				<f:selectItems value="#{evtTiming.secList}"/>
			</h:selectOneMenu>
		</h:panelGroup>
    <h:panelGroup>
    </h:panelGroup>
    
		<h:panelGroup rendered="#{evtAdvisor.eventModel.spanDays}">
        <p align="left">End Date:</p>
		</h:panelGroup>
    <h:panelGroup rendered="#{evtAdvisor.eventModel.spanDays}">
			<rich:calendar value="#{evtTiming.endDate}" id="endDateCalendar"
						   popup="true"
						   showApplyButton="false" datePattern="#{applicationScope.dateFormat}" cellWidth="24px"
						   cellHeight="22px" style="width:180px"
						   enableManualInput="true"
						   />
		</h:panelGroup>
		<h:outputText rendered="#{evtAdvisor.eventModel.spanDays}" 
      style="color: #3C78B5; font-size:11px;font-weight:normal" value="End date can be empty"/>
    
		<h:panelGroup>
			<p align="left">End Time:</p>
		</h:panelGroup>
		<h:panelGroup>
			<h:selectOneMenu value="#{evtTiming.endHour}">
				<f:selectItems value="#{evtTiming.hourList}"/>
			</h:selectOneMenu>
			:
			<h:selectOneMenu value="#{evtTiming.endMin}">
				<f:selectItems value="#{evtTiming.minList}"/>
			</h:selectOneMenu>
			:
			<h:selectOneMenu value="#{evtTiming.endSec}">
				<f:selectItems value="#{evtTiming.secList}"/>
			</h:selectOneMenu>
		</h:panelGroup>
		<h:panelGroup>

		</h:panelGroup>
	</h:panelGrid>
</rich:panel>

<rich:panel styleClass="content-panel" id ="location_panel" style="margin-left:10px;border-top: 1px solid #DDDDDD;margin-left:0px" rendered="#{evtAdvisor.eventModel.cbpProgramEJB}">
	<h:panelGroup id ="location_group" >

	    <h:outputLabel  style="color:#3C78B5;font-weight:bold;font-size:15px;text-align:left;width:80px;margin-left:0px">Locations:</h:outputLabel>
	    <rich:dataTable id="location_table" value="#{evtAdvisor.eventModel.locationList}" var="location" 
	                width="410px"
	                style="font-size:11px;text-align:left;margin-left:17px" >
	        <rich:column style="text-align:center;" width="10px">
	            <f:facet name="header">
	            </f:facet>
	            <h:selectBooleanCheckbox id="enrolled" value="#{location.enrolled}"></h:selectBooleanCheckbox>
				<h:message for="enrolled" errorClass="message-error"  />
	        </rich:column>
	        <rich:column style="text-align:left;" width="200px">
	            <f:facet name="header">
	                <h:outputLabel style="text-align:center;width:200px">Location Number</h:outputLabel>
	            </f:facet>
	            <h:outputLabel value="#{location.number}" style="text-align:left;width:90px"></h:outputLabel>
	        </rich:column>
	        <rich:column style="text-align:left;" width="200px">
	            <f:facet name="header">
	                <h:outputLabel  style="text-align:center;width:200px">Location Name</h:outputLabel>
	            </f:facet>
	            <h:outputLabel value="#{location.longName}" style="text-align:left;width:200px"></h:outputLabel>
	        </rich:column>
	    </rich:dataTable>
    </h:panelGroup>
</rich:panel>

<a4j:region>
<rich:panel styleClass="content-panel" id ="upload_timing_panel" style="border-top: 1px solid #DDDDDD;margin-left:0px" rendered="#{evtAdvisor.eventModel.uploadEnable}">
	<richext:set beanName="uploadFile" value="#{evtAdvisor.eventModel.uploadFile}"/>
	<h:panelGrid columns="4">
		<h:panelGroup>
			<span class="wizard-title"> <h:outputText value="Create Event via APX monthly nomination CSV file"/> </span>
		</h:panelGroup>
		<h:panelGroup>
			<rich:fileUpload
				id="uploadFileComponent" maxFilesQuantity="1"
				fileUploadListener="#{uploadFile.uploadChanged}"
				immediateUpload="true"
				autoclear="true"
				listHeight="0px" listWidth="80px"> 
				<a4j:support event="onuploadcomplete" reRender="info,uploadFileComponent,location_group"/>
			</rich:fileUpload>
		</h:panelGroup>
		<h:panelGroup>
			<a4j:commandButton value="Clear" action="#{uploadFile.clearUpload}" title="Clear the upload file" reRender="info,uploadFileComponent,location_group"/>
		</h:panelGroup>
		 <h:panelGroup id="info">
				<h:outputText style="color:red" id="uploadFileWarningMessage" value="#{uploadFile.message}" rendered ="true"/>
				<h:outputText id="uploadFileName" value="#{uploadFile.filename}"/>
		 </h:panelGroup>
	</h:panelGrid>
</rich:panel>
</a4j:region>

<rich:spacer height="5px" width="700px" />

<!-- Participant List waiting for choise which are the program contained -->
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD">
	<a4j:commandButton value="Next  >" styleClass="wizard-btn" style="margin-left:580"
			action="#{evtAdvisor.eventModel.nextPage}" title="Next Step"
			oncomplete="#{rich:component('comfirmpanel')}.show();"
			limitToList="true" reRender="event-creation-wizard,msg-block,comfirmpanel">
			<f:setPropertyActionListener
					value="nextPage"
					target="#{evtAdvisor.eventModel.nextPage}"/>
			</a4j:commandButton>
				
	<a4j:commandButton value="Issue Event" styleClass="wizard-btn" 
			action="#{evtAdvisor.eventModel.goToConfirm}" reRender="event-creation-wizard,msg-block,comfirmpanel"
			oncomplete="#{rich:component('comfirmpanel')}.show();"
			title="Go to confirm page">
			<f:setPropertyActionListener
					value="confirmation"
					target="#{evtAdvisor.eventModel.nextPage}"/>
	</a4j:commandButton>		
	
	<a4j:commandButton value="Cancel" styleClass="wizard-btn" 
			immediate="true" action="#{evtAdvisor.eventModel.cancel}"
			title="Cancel creation of event"/>
</rich:panel>
</h:form>

<rich:modalPanel id="comfirmpanel" autosized="false"
      keepVisualState="false" width="315" height="150" rendered="#{evtAdvisor.eventModel.confirm}">
    <h:form onsubmit="" id="creationForm">
        <rich:panel id="messages">
            <h:outputText value="#{evtAdvisor.eventModel.warnings}" />
            </br>
            <h:outputText value="Are you sure you want to create the event? " />
        </rich:panel>
	<a4j:commandButton reRender="event-creation-wizard"
					   id="yes" value="Yes" action="#{evtAdvisor.eventModel.goToNextNoValidation}"
					   oncomplete="#{rich:component('comfirmpanel')}.hide();">
	</a4j:commandButton>
	<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('comfirmpanel')}.hide();"/>
          
    </h:form>
</rich:modalPanel>
