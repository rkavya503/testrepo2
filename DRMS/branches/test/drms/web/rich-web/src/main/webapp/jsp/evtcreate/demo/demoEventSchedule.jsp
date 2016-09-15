<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:form id="demoSchedule">
		
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title"> Event Timing </span>
</rich:panel> 

<rich:panel styleClass="content-panel">
	<div>
		<jsp:include page="demoEvtTiming.jsp"/>
	</div>
	<div style="width:900px; height:395px; margin-top:15px; margin-left:18px">
		<div class="table-title" style="margin-bottom:5px"> Signals </div>
		<jsp:include page="demoEvtSignals.jsp"/>
	</div>
</rich:panel>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD">
	<a4j:commandButton value="Next  >" styleClass="wizard-btn" style="margin-left:580px"
			action="#{evtAdvisor.eventModel.nextPage}" title="Next Step"
			oncomplete="#{rich:component('comfirmpanel')}.show();"
			limitToList="true" reRender="event-creation-wizard,msg-block,comfirmpanel">
			<f:setPropertyActionListener
					value="nextPage"
					target="#{evtAdvisor.eventModel.nextPage}"/>
	</a4j:commandButton>		
			
	<a4j:commandButton value="Issue Event" styleClass="wizard-btn" 
			action="#{evtAdvisor.eventModel.goToConfirm}"
			oncomplete="#{rich:component('comfirmpanel')}.show();"
			reRender="event-creation-wizard,msg-block,comfirmpanel"
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
