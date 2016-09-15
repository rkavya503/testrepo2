<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
          xmlns:f="http://java.sun.com/jsf/core"
          xmlns:h="http://java.sun.com/jsf/html"
          xmlns:rich="http://richfaces.org/rich"
		  xmlns:a4j="http://richfaces.org/a4j"
          version="2.1">
    <jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
    <head>
        <jsp:include page="/jsp/head.jsp" />
    </head>

    <f:view>
		<a4j:keepAlive beanName="eventDataModel"/>
		<jsp:include page="/jsp/header_events.jsp" />
		
            <rich:spacer height="5px" width="700px" />
			<rich:messages layout="table" globalOnly="true" id="msg-block"
			infoClass="global-message-info" warnClass="global-message-warn"
			errorClass="global-message-error" fatalClass="global-message-fatal"/>
			
            <rich:spacer height="5px" width="700px" />
            <!-- Event Basic Information Component -->
		
		<h:panelGroup id="event-creation-wizard-panel">
        <h:form id="form">   
		<h:panelGroup id = "mainPanel" layout="block" >
            <!-- When this page initialize invoke the JSF backing bean model refresh -->

            
            <!-- Validation Message Component -->
			
			<rich:panel styleClass="event-rich-panel">Program <h:outputText value="#{eventDataModel.programName}"/></rich:panel>
			
            <h:panelGrid columns="3" styleClass="event-panel-grid">

                <h:panelGroup>
                    <p align="left">Now:</p>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectBooleanCheckbox title="instantEvent" value="#{eventDataModel.instantEvent}"
                        id="instantEvent" onclick="flipControls(this)"/>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

                <h:panelGroup>
                    <p align="left">Event Date:</p>
                </h:panelGroup>
                <h:panelGroup>
                    <rich:calendar value="#{eventDataModel.eventDate}" id="eventDateCalendar"
                                   required="true" requiredMessage="Required"  popup="true" showApplyButton="false"
                                   datePattern="#{applicationScope.dateFormat}" cellWidth="24px" cellHeight="22px"
                                   style="width:180px"/>
                </h:panelGroup>
                <h:message for="eventDateCalendar" errorClass="message-error"  />


                <h:panelGroup>
                    <p align="left">Start Time:</p>
                </h:panelGroup>
                <h:panelGroup>
                    <h:selectOneMenu value="#{eventDataModel.startHour}" id="startHour">
                        <f:selectItems value="#{eventDataModel.hourList}"/>
                    </h:selectOneMenu>
                    :
                    <h:selectOneMenu value="#{eventDataModel.startMin}" id="startMin">
                        <f:selectItems value="#{eventDataModel.minList}"/>
                    </h:selectOneMenu>
                    :
                    <h:selectOneMenu value="#{eventDataModel.startSec}" id="startSec">
                        <f:selectItems value="#{eventDataModel.secList}"/>
                    </h:selectOneMenu>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

                <h:panelGroup>
                    <p align="left">Duration (minute):</p>
                </h:panelGroup>
                <h:panelGroup>
                    <h:inputText 	value="#{eventDataModel.duration}"
									onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}">
					<a4j:support event="onchange" action="#{eventDataModel.durationChangeAction}" reRender="mainPanel" limitToList="true"/> 
                    </h:inputText>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

                <h:panelGroup>
                    <p align="left">End Time (approx):</p>
                </h:panelGroup>
                <h:panelGroup>
                    <h:outputText value="#{eventDataModel.endTime}">
                        <f:convertDateTime pattern="HH:mm:ss" />
                    </h:outputText>
                </h:panelGroup>
                <h:panelGroup>
                </h:panelGroup>

            </h:panelGrid>

            <div style="display:none;">
                <script type="text/javascript">
                    function flipControls(obj) {
                        document.getElementById("form:eventDateCalendar").disabled = obj.checked;
                        document.getElementById("form:startHour").disabled = obj.checked;
                        document.getElementById("form:startMin").disabled = obj.checked;
                        document.getElementById("form:startSec").disabled = obj.checked;
                    }
                    var obj = document.getElementById("form:instantEvent");
                    flipControls(obj);
                </script>
            </div>

            <rich:spacer height="30px" width="700px" />

            <!-- Participant List waiting for choise which are the program contained -->
			
            <rich:dataTable id="participants" value="#{eventDataModel.allParticipantsInProgram}" var="item" width="100%" styleClass="event-rich-table ">
                <f:facet name="header">
                    <h:outputText value="Participants"/>
                </f:facet>

                <rich:column sortable="false" >
                    <f:facet name="header">
                        <h:outputText value="Selected" escape="false"/>
                    </f:facet>
                    <h:selectBooleanCheckbox value="#{item.select}" disabled="#{item.nonselectable}">
						<a4j:support event="onclick" reRender="mainPanel" limitToList="true"/> 
					</h:selectBooleanCheckbox>
                </rich:column>
                <rich:column sortable="false" >
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participant.participantName}">
                    </h:outputText>
                </rich:column>
				<rich:column sortable="false" >
                    <f:facet name="header">
                        <h:outputText value="Register Shed (KW)" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participantRegistedShed}">
                    </h:outputText>
                </rich:column>
				<rich:column sortable="false" >
                    <f:facet name="header">
                        <h:outputText value="Available Shed (KW)" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participantAvilableShed}">
                    </h:outputText>
                </rich:column>
            </rich:dataTable>
			<table width="100%">
				<tr>
					<td align="right" width="80%"></td>
					<td align="right" class="shed" nowrap="nowrap"><h:outputText style="font-size: 12px;" styleClass="form-field" value="Total Register Shed:"/></td>
					<td align="right"  class="shed" nowrap="nowrap">
						<h:outputText id='sel-total-register' style="font-size: 12px;" styleClass="form-field" value="#{eventDataModel.totalRegisterShed} kw"/>
					</td>
				</tr>
				<tr>
					<td align="right" width="80%"></td>
					<td align="right" class="shed" nowrap="nowrap"><h:outputText style="font-size: 12px;" styleClass="form-field" value="Total Available Shed:"/></td>
					<td align="right"  class="shed" nowrap="nowrap">
						<h:outputText id='sel-total-available' style="font-size: 12px;" styleClass="form-field" value="#{eventDataModel.totalAvilabeShed} kw"/>
					</td>
				</tr>
			</table>
            <h:commandButton value="Select All" action="#{eventDataModel.selectedAllParticipants}" immediate="true" title="Select All participants" />
            <h:commandButton value="Select None" action="#{eventDataModel.selectedNoneParticipants}" immediate="true" title="Select None participants" />

            <rich:spacer height="30px" width="700px" />

            <div style="text-align: center;">
                <a4j:commandButton value="Issue Event" action="#{eventDataModel.submitFastDRToDB}" 
                		oncomplete="#{rich:component('comfirmpanel')}.show();" title="Submit and Issue Event"
                		reRender="event-creation-wizard-panel,msg-block,comfirmpanel">
						<f:setPropertyActionListener
								value="false"
								target="#{eventDataModel.nextPageNoValidation}"/>
                			
                </a4j:commandButton>
                <h:commandButton value="Cancel" action="#{eventDataModel.cancel}" immediate="true" title="Cancel Submit and Issue Event"/>
            </div>
		</h:panelGroup>
        </h:form>
        <rich:modalPanel id="comfirmpanel" autosized="false"
	      keepVisualState="false" width="315" height="150" rendered="#{eventDataModel.warnMsgConfirm}">
		    <h:form onsubmit="" id="creationForm">
		          <rich:panel id="messages">
		              <h:outputText value="#{eventDataModel.warnMsg}"/>
		              <h:outputText value=". Are you sure you want to create the event? " />
		          </rich:panel>
			<a4j:commandButton reRender="event-creation-wizard-panel"
							   id="yes" value="Yes" action="#{eventDataModel.submitFastDRToDB}"
							   oncomplete="#{rich:component('comfirmpanel')}.hide();">
						<f:setPropertyActionListener
								value="true"
								target="#{eventDataModel.nextPageNoValidation}"/>
			</a4j:commandButton>
			<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('comfirmpanel')}.hide();"/>
	          
		    </h:form>
		</rich:modalPanel>
        </h:panelGroup>
        <jsp:include page="/jsp/footer.jsp" />
    </f:view>
</jsp:root>