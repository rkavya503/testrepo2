<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGrid  id="timing">
<h:panelGrid  columns="4" style="font-size:11px">	
	<h:outputLabel style="font-size:11px" for="notificationTime"  value="Notification Time:"/>
	<!-- Enter key press will trigger submit of form by default, onkeydown handler will just 
		trigger ajax handler for  value change -->
	<a4j:region>
		<h:inputText id="notificationTime"  styleClass="time-input" style="width:90px;"
			value="#{demoEvent.notificationTimeStr}" 
            disabled="#{demoEvent.nowUsed}"
			required="true" label="#{demoEvent.notificationTimeStr}"
			requiredMessage="notification time is required"
			title="notification time"
			onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
			valueChangeListener="#{demoEvent.notificationTimeChange}">
			<a4j:support event="onchange" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					limitToList="true"/> 
			<f:converter converterId="HourMinuteConverter"/>
		</h:inputText>
	</a4j:region>
	<a4j:region>
		<rich:calendar id="notificationTimeCal" label="notification Date"
			  value="#{demoEvent.notificationDateOnly}"  
              disabled="#{demoEvent.nowUsed}"
			  valueChangeListener="#{demoEvent.notificationDateChange}"
			  datePattern="#{applicationScope.dateFormat}"
			  popup="true">
			<a4j:support event="onchanged" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					 limitToList="true"/> 
		</rich:calendar>
	</a4j:region>
    <a4j:region >
        <h:selectBooleanCheckbox id="isNowUsed" value="#{demoEvent.nowUsed}">
               <a4j:support event="onclick" action="#{demoEvent.nowNotification}" reRender="message,timing" limitToList="true"/>
        </h:selectBooleanCheckbox>
		Now
    </a4j:region>
</h:panelGrid>
<h:selectOneRadio value="#{demoEvent.selectedCreationType}" id="controlStateSelectRadio"
										layout="lineDirection"
										style="font-size:11px;text-align: center;">
										<a4j:support event="onclick"
											actionListener="#{demoEvent.typeListener}"
											reRender="timing"
											ajaxSingle="true"/>
										<a4j:support event="onchange" reRender="timing" actionListener="#{demoEvent.typeListener}"/>	
										<f:selectItem itemValue="TIME" itemLabel="TIME" id="autoSelectItem"/>
										<f:selectItem itemValue="DURATION" itemLabel="DURATION" id="manualSelectItem"/>
									</h:selectOneRadio>	

<h:panelGrid  columns="4" style="font-size:11px">	
	
	<!-- notice-->
	<h:outputLabel style="font-size:11px" for="notice" value="Notice(min):"/>
	<a4j:region>
		<h:inputText id="notice"  styleClass="time-input" style="width:90px;"
			value="#{demoEvent.notice}" 
			disabled="#{!demoEvent.renderedDurationComponent}"
			required="true" label="#{demoEvent.notice}"
			requiredMessage="Notice time span is required"
			title="Notice time span"
			onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
			valueChangeListener="#{demoEvent.noticeTimeChange}">
			<a4j:support event="onchange" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					limitToList="true"/> 
			<f:converter converterId="javax.faces.Integer"/>
		</h:inputText>
	</a4j:region>
	<h:outputLabel style="width:1px"/>
	<h:outputLabel style="width:1px"/>
	
	
	<!-- start time-->
	<h:outputLabel style="font-size:11px;" for="startTime" value="Start Time:"/>
	<a4j:region>
		<h:inputText id="startTime" styleClass="time-input" style="width:90px;"
			value="#{demoEvent.startTimeStr}" 
			disabled="#{!demoEvent.renderedTimingComponent}"
			required="true" label="#{demoEvent.startTimeStr}"
			requiredMessage="start time is required"
			title="Event start time" 
			onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
			valueChangeListener="#{demoEvent.startTimeChange}">
			<a4j:support event="onchange" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					 limitToList="true"/> 
			<f:converter converterId="HourMinuteConverter"/>
		</h:inputText>
	</a4j:region>
	<a4j:region>
		<rich:calendar id="startTimeCal"  value="#{demoEvent.startDateOnly}"  label="Event Start date" 
			disabled="#{!demoEvent.renderedTimingComponent}"
			valueChangeListener="#{demoEvent.startDateChange}"
			datePattern="#{applicationScope.dateFormat}"
			popup="true">
			<a4j:support event="onchanged" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					limitToList="true"/> 
		</rich:calendar>
	</a4j:region>
	<h:outputLabel style="width:1px"/>
	
	<!-- duration-->
	<h:outputLabel style="font-size:11px" for="duration" value="Duration (min):" />
	<a4j:region>
		<h:inputText id="duration" styleClass="time-input" style="width:90px;"
			value="#{demoEvent.duration}" 
			disabled="#{!demoEvent.renderedDurationComponent}"
			required="true" label="#{demoEvent.duration}"
			requiredMessage="Event duration is required"
			title="Event duration"
			onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
			valueChangeListener="#{demoEvent.durationTimeChange}">
			<a4j:support event="onchange" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					 limitToList="true"/> 
			<f:converter converterId="javax.faces.Integer"/>
		</h:inputText>
	</a4j:region>
	<h:outputLabel/>
	<h:outputLabel/>
	
	
	
	<!-- end time -->
	<h:outputLabel style="font-size:11px" for="endTime" value="End Time:"/>
	<a4j:region>
		<h:inputText id="endTime"  styleClass="time-input" style="width:90px;"
			value="#{demoEvent.endTimeStr}" 
			disabled="#{!demoEvent.renderedTimingComponent}"
			required="true" label="#{demoEvent.endTimeStr}"
			requiredMessage="Event End Time is required"
			title="Event End Time"
			onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
			valueChangeListener="#{demoEvent.endTimeChange}">
			<a4j:support event="onchange" action="#{demoEvent.updateModel}" reRender="messageForm-block,timing,events" 
					limitToList="true"/> 
			<f:converter converterId="HourMinuteConverter"/>
		</h:inputText>
	</a4j:region>
	<h:outputLabel/>
	<h:outputLabel/>
</h:panelGrid>
</h:panelGrid>