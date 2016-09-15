<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:form id="demoSchedule">
		
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title">BIP - Event Detail</span>
</rich:panel> 

<rich:panel styleClass="content-panel">
	<div>
		<jsp:include page="eventTiming.jsp"/>
	</div>

</rich:panel>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD">
	<a4j:commandButton value="Issue Event" styleClass="wizard-btn" style="margin-left:15px"
			action="#{evtAdvisor.eventModel.goToConfirm}" title="Issue Event"			
			limitToList="true" reRender="event-creation-wizard,msg-block"/>
				
	<a4j:commandButton value="Cancel" styleClass="wizard-btn" 
			immediate="true" reRender="event-creation-wizard" action="#{evtAdvisor.eventModel.cancel}"
			title="Cancel creation of event"/>
			
	
</rich:panel>
</h:form>