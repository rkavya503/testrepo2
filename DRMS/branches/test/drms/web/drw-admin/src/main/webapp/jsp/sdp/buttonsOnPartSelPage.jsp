<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
	<a4j:commandButton value="Previous" styleClass="wizard-btn" style="margin-left:15px"
			action="#{evtAdvisor.eventModel.backPage}" title="step back"
			limitToList="true" reRender="event-creation-wizard"/>	
	<a4j:commandButton value="Issue Events" styleClass="wizard-btn" 
			action="#{evtAdvisor.eventModel.goToConfirm}" title="Go to confirm page"
			limitToList="true" reRender="event-creation-wizard"/>
	<a4j:commandButton value="Cancel" reRender="event-creation-wizard" action="#{evtAdvisor.eventModel.cancel}" immediate="true" title="Cancel"/>
</rich:panel>