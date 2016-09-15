<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<f:view>
<head>
	<jsp:include page="/jsp/head.jsp" />
</head>
<body>
	<a4j:keepAlive beanName="bipEventAdvisor"/>
	<richext:set beanName="evtAdvisor" value="#{bipEventAdvisor}"/>
	<richext:set beanName="demoEvent" value="#{evtAdvisor.eventModel.event}"/>
	<h:form id="navForm">
	<jsp:include page="/jsp/header.jsp" />
	</h:form>
	<rich:spacer height="5px" width="80px"/>
	<rich:dataTable width="945px" styleClass="wizard-header" style="border-bottom:1px solid #DDDDDD">
		<f:facet name="header4">
			<f:facet name="header">
				<rich:columnGroup>
					<rich:column  style="text-align:left;font-size: 13px;">
						<h:outputText value="#{evtAdvisor.eventModel.programName}"/>
					</rich:column>
					<rich:column width="21px">
						<a4j:status>
							<f:facet name="start">
								<h:graphicImage style="margin:0px;"  value="/images/small-ajax-loader.gif"/>
							</f:facet>
						</a4j:status>
					</rich:column>
				</rich:columnGroup>
			</f:facet>
		</f:facet>
	</rich:dataTable>

	<rich:panel id="event-creation-wizard" styleClass="wizard-panel">
		<rich:messages layout="table"  id="msg-block"
			infoClass="global-message-info" warnClass="global-message-warn"
			errorClass="global-message-error" fatalClass="global-message-fatal"/>
		
		<!-- don't draw tab header(showHeader=false) and use next and back button 
		to nagivate among pages,so that it looks like a wizard -->
		<rich:tabPanel selectedTab="#{evtAdvisor.eventModel.activePage}" 
					   switchType="ajax"
					   headerClass="wizard-header">
			<richext:tab label="Event Timing" id="drEventSchedule" status ="waitStatus" showHeader="false"> 
				<jsp:include page="eventSchedule.jsp"/>
			</richext:tab>
			<richext:tab label="Confirm"  id="confirmation" status ="waitStatus" showHeader="false">
				<jsp:include page="confirmation.jsp"/>
			</richext:tab>
		</rich:tabPanel>
	</rich:panel>	
	<jsp:include page="/jsp/footer.jsp" />
</body>	
</f:view>