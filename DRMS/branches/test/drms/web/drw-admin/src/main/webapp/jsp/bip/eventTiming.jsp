<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<div id="evt-timing">
<h:panelGrid  id="timing" columns="2" >
<richext:set beanName="localSelection" value="#{evtAdvisor.eventModel.locationSelection}"/>
<richext:set beanName="evtTiming" value="#{evtAdvisor.eventModel.eventTiming}"/>
	<h:panelGroup >
		<h:graphicImage id="requiredName" alt="required" title="required"  url="/images/layout/required.jpg" />
		<h:outputText style="text-align: left;font-size:11px"   value="Start Date & Time:"/>		
	</h:panelGroup>	 

	<!-- Enter key press will trigger submit of form by default, onkeydown handler will just 
		trigger ajax handler for  value change -->
	 <a4j:region>
        <h:panelGroup>
            <rich:calendar id="startTime"
                  value="#{evtTiming.startDate}"  
                  datePattern="MM-d-yyyy"
                  popup="true">
                  <a4j:support event="onchanged"  reRender="msg-block" limitToList="true"/> 
            </rich:calendar>
            <h:panelGroup>
                <h:selectOneMenu value="#{evtTiming.startHour}">
                    <f:selectItems value="#{evtTiming.hourList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
                :
                <h:selectOneMenu value="#{evtTiming.startMin}">
                    <f:selectItems value="#{evtTiming.minList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
                :
                <h:selectOneMenu value="#{evtTiming.startSec}">
                    <f:selectItems value="#{evtTiming.secList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
            </h:panelGroup>
        </h:panelGroup>
    </a4j:region>  
	<!-- start time-->
	
	<h:panelGroup >
		<h:graphicImage id="required" alt="required" title="required"  url="/images/layout/required.jpg" />
		<h:outputText style="text-align: left;font-size:11px"   value="Issued Date & Time:"/>		
	</h:panelGroup>	 

	 <a4j:region>
        <h:panelGroup>
            <rich:calendar id="issueTime"
                  value="#{evtTiming.issuedDate}"  
                  datePattern="MM-d-yyyy"
                  popup="true">
                  <a4j:support event="onchanged"  reRender="msg-block" limitToList="true"/> 
            </rich:calendar>
            <h:panelGroup>
                <h:selectOneMenu value="#{evtTiming.issuedHour}">
                    <f:selectItems value="#{evtTiming.hourList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
                :
                <h:selectOneMenu value="#{evtTiming.issuedMin}">
                    <f:selectItems value="#{evtTiming.minList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
                :
                <h:selectOneMenu value="#{evtTiming.issuedSec}">
                    <f:selectItems value="#{evtTiming.secList}"/>
                    <a4j:support  event="onchange" reRender="msg-block"/>
                </h:selectOneMenu>
            </h:panelGroup>
        </h:panelGroup>
    </a4j:region>  
	
	<h:outputLabel style="text-align: left;font-size:11px" for="endTime" value="End Date & Time:"/>
	<h:panelGroup >
	 <a4j:region>
        <rich:calendar id="endTime"  value="#{evtTiming.endDate}"           
            datePattern="MM-d-yyyy"
            popup="true" >
            <a4j:support event="onchanged"  reRender="msg-block" limitToList="true"/> 
        </rich:calendar>
        <h:panelGroup>
            <h:selectOneMenu value="#{evtTiming.endHour}">
                <f:selectItems value="#{evtTiming.hourList}"/>
                <a4j:support  event="onchange" action="#{evtAdvisor.eventModel.updateEndDate}" reRender="msg-block,endTime" limitToList="true"/>
            </h:selectOneMenu>
            :
            <h:selectOneMenu value="#{evtTiming.endMin}">
                <f:selectItems value="#{evtTiming.minList}"/>
                <a4j:support  event="onchange" action="#{evtAdvisor.eventModel.updateEndDate}" reRender="msg-block,endTime" limitToList="true"/>
            </h:selectOneMenu>
            :
            <h:selectOneMenu value="#{evtTiming.endSec}">
                <f:selectItems value="#{evtTiming.secList}"/>
                <a4j:support  event="onchange" action="#{evtAdvisor.eventModel.updateEndDate}" reRender="msg-block,endTime" limitToList="true"/>
            </h:selectOneMenu>
        </h:panelGroup>
    </a4j:region>
	&nbsp;
	 <h:selectBooleanCheckbox
         value="#{demoEvent.estimated}"
         title="Estimated"
        >						
     </h:selectBooleanCheckbox>
	 
	<h:outputText style="text-align: left;font-size:11px" value="Estimated"/>
	</h:panelGroup>	
	
	<h:panelGroup >
		<h:graphicImage id="requiredBlockss" alt="required" title="required"  url="/images/layout/required.jpg" />
		<h:outputText style="text-align: left;font-size:11px"   value="Blocks:"/>	
	</h:panelGroup>	 
	
	<h:selectManyCheckbox value="#{demoEvent.block}" id="block_radio" styleClass="selfDefinedFontRadio">
		<f:selectItems value="#{demoEvent.blockList}"/>
		 <a4j:support  event="onchange" reRender="msg-block"/>
	</h:selectManyCheckbox>
	
	
	<h:outputLabel style="text-align: left;font-size:11px" for="comments" value="Comments:"/>
	
	<h:inputTextarea id="comments" value="#{demoEvent.comments}" cols="30" rows="3">
	 <a4j:support  event="onchange" reRender="msg-block"/>
    </h:inputTextarea>
	
</h:panelGrid>
</div>
