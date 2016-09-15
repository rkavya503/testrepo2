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
	<h:form id="form">
		<!-- When this page initialize invoke the JSF backing bean model refresh -->
		<div style="display:none;">
			<h:commandButton id="renewModelButton" value="renew" onclick="#{eventDataModel.renew}"/>
			<script type="text/javascript">
				var relativedComponent = document.getElementById("form:renewModelButton");
				if(relativedComponent){
					relativedComponent.onclick();
				}
			</script>
		</div>
		
		<!-- Validation Message Component -->
		<rich:spacer height="5px" width="700px" />
		<h:messages layout="table" globalOnly="true" 
			infoClass="global-message-info" warnClass="global-message-warn" 
			errorClass="global-message-error" fatalClass="global-message-fatal"/>	
							
		<rich:spacer height="5px" width="700px" />
		<!-- Event Basic Information Component -->
		<rich:panel styleClass="event-rich-panel">Program <h:outputText value="#{eventDataModel.programName}"/></rich:panel>
		
		<rich:spacer height="20px" width="700px" />
		<!-- Participant List waiting for choise which are the program contained -->
		<rich:dataTable id="participants" value="#{eventDataModel.allParticipantsInProgram}" var="item" width="100%" styleClass="event-rich-table ">		
			<f:facet name="header">
                <h:outputText value="Participants"></h:outputText>
            </f:facet>

			<rich:column sortable="false" >
				<f:facet name="header">
                    <h:outputText value="Participant" escape="false"  />
                </f:facet>
				<h:outputText  value="#{item.participant.participantName}">
				</h:outputText>
			</rich:column> 
			<rich:column sortable="false" >
                <f:facet name="header">
                    <h:outputText value="Account#" escape="false"/>
                </f:facet>
				<h:outputText value="#{item.participant.accountNumber}" />
			</rich:column>
        </rich:dataTable>

		<rich:spacer height="30px" width="700px" />
		
		<div style="text-align: center;">        
			<h:commandButton value="Confirm" action="#{eventDataModel.confirm}" title="Submit and Issue Event"/>
			<h:commandButton value="Cancel" action="#{eventDataModel.cancel}" immediate="true" title="Cancel Submit and Issue Event"/>
		</div>

	</h:form>
   <jsp:include page="/jsp/footer.jsp" />
</f:view>
</jsp:root>