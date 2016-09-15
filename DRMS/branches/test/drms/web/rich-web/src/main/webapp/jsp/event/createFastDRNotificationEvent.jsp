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
        <h:form id="form">
            <!-- When this page initialize invoke the JSF backing bean model refresh -->
           
            
            <!-- Validation Message Component -->
            <rich:spacer height="5px" width="700px" />
            <h:messages layout="table" globalOnly="true"
                        infoClass="global-message-info" warnClass="global-message-warn"
                        errorClass="global-message-error" fatalClass="global-message-fatal"/>

            <rich:spacer height="5px" width="700px" />
            <!-- Event Basic Information Component -->
            <rich:panel styleClass="event-rich-panel">Program <h:outputText value="#{eventDataModel.programName}"/></rich:panel>

            <rich:spacer height="20px" width="700px" />

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
                    <h:selectBooleanCheckbox value="#{item.select}" disabled="#{item.nonselectable}"/>
                </rich:column>
                <rich:column sortable="false" >
                    <f:facet name="header">
                        <h:outputText value="Participant" escape="false"  />
                    </f:facet>
                    <h:outputText  value="#{item.participant.participantName}">
                    </h:outputText>
                </rich:column>
            </rich:dataTable>

            <h:commandButton value="Select All" action="#{eventDataModel.selectedAllParticipants}" immediate="true" title="Select All participants" />
            <h:commandButton value="Select None" action="#{eventDataModel.selectedNoneParticipants}" immediate="true" title="Select None participants" />

            <rich:spacer height="30px" width="700px" />

            <div style="text-align: center;">
                <h:commandButton value="Issue Event" action="#{eventDataModel.submitFastDRToDB}" title="Submit and Issue Event"/>
                <h:commandButton value="Cancel" action="#{eventDataModel.cancel}" immediate="true" title="Cancel Submit and Issue Event"/>
            </div>

        </h:form>
        <jsp:include page="/jsp/footer.jsp" />
    </f:view>
</jsp:root>