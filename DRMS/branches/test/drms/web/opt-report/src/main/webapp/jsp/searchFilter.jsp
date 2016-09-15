<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:form>
<a4j:region>
<rich:panel id="filter-by-block" styleClass="panel-container">
<div style="margin-bottom:10px; margin-top:5px" >	
	<span class="hightlight-txt">
	<h:outputText value="Select"/>
	<h:outputLink value="javascript:#{rich:component('select-participants-dlg')}.show();"> 
		<h:outputText value="Participant(s)"/>
	</h:outputLink>
	<h:outputText value="Or"/>
	<h:outputLink value="javascript:#{rich:component('select-event-dlg')}.show();"> 
		<h:outputText value="An Event"/>
	</h:outputLink>
	</span>
	
	<h:inputText value="#{report.searchCriteria.filterByObjectName}" readonly ="true" 
	style="margin-left:10px;width:430px;font-size:14px" 
	required="true" requiredMessage="End time is required"/>
</div>

<rich:panel  id="dynamic-filter-block" style="width:700px; border:none" styleClass="mod-header">
	<SPAN id="dynamic-filter-title" tabindex="0" class="toggle-title" title="Collapse"
				onclick="Element.toggleClassName(this.parentNode,'collapsed'); if(this.getAttribute('title')=='Collapse') this.setAttribute('title','Expand'); else this.setAttribute('title','Collapse');"
				onkeydown="var evt = event || window.event; if(evt.keyCode==13) this.onclick();"
				>
				Advance
	</SPAN>
	<table id="dynamic-filter-body" class="toggle-body">
		<tr>
			<td>
				<h:panelGrid styleClass="form-input" columns="4" id="date-range-block">
					<h:outputLabel value="Start Date: " styleClass="form-field"/>
					
					<h:outputLabel value="Start Time:" styleClass="form-field" rendered="#{report.timeNeeded}"/>
					<h:outputLabel value=""  rendered="#{!report.timeNeeded}"/>
					
					<h:outputLabel value="End Date: " rendered="#{report.endDateNeeded}" styleClass="form-field"/>
					<h:outputLabel value="" rendered="#{!report.endDateNeeded}"/>
					
					<h:outputLabel value="End Time: " styleClass="form-field" rendered="#{report.timeNeeded}"/>
					<h:outputLabel value=""  rendered="#{!report.timeNeeded}"/>
					
						
					<rich:calendar id="startDateCal" readonly="#{report.searchCriteria.searchByEvent}"
										label="Start Date"
										value="#{report.searchCriteria.startDate}" 
										datePattern="#{report.dateFormat}" 
										required="true" requiredMessage="Start Date is required"
										/>	
										
					<h:inputText size="5" readonly="#{report.searchCriteria.searchByEvent}"
									maxlength="5" id="startTime" value="#{report.searchCriteria.startTime}" rendered="#{report.timeNeeded}"
									required="true" requiredMessage="Start time is required">
						<f:converter converterId="HourMinuteConverter"/>
					</h:inputText>
					<h:outputLabel value=""  rendered="#{!report.timeNeeded}"/>
					
					<rich:calendar id="endDateCal"  rendered="#{report.endDateNeeded}" readonly="#{report.searchCriteria.searchByEvent}"
										label="End Date"
										value="#{report.searchCriteria.endDate}"
										datePattern="#{report.dateFormat}"
										required="true" requiredMessage="End Date is required"
										/>
										
					<h:outputLabel value="" rendered="#{!report.endDateNeeded}"/>
					
					<h:inputText size="5" maxlength="5" id="endTime"  readonly="#{report.searchCriteria.searchByEvent}" 
						value="#{report.searchCriteria.endTime}" rendered="#{report.timeNeeded}"
						required="true" requiredMessage="End time is required">
						<f:converter converterId="HourMinuteConverter"/>
					</h:inputText>
					<h:outputLabel value=""  rendered="#{!report.timeNeeded}"/>
				</h:panelGrid>
			</td>
			<td>
				<rich:panel styleClass="panel-container" rendered="#{report.programNeeded}">
					<h:panelGrid styleClass="form-input" columns="1" id="program-block" >
						<h:outputLabel escape="false" value="Program:" styleClass="form-field"/>
						
						<h:selectOneMenu id="programSelection"  value="#{report.searchCriteria.program}"  disabled="#{report.searchCriteria.searchByEvent}" 
								style="width:150px">
								<f:selectItems value="#{report.programOptionItems}"/>
						</h:selectOneMenu>
					</h:panelGrid>
				</rich:panel>
			</td>
		</tr>
	</table>
</rich:panel>
</rich:panel>
</a4j:region>
<br>
<div style="width:700px; border-top:1px solid #BBBBBB">
	<rich:messages layout="table" globalOnly="false" id="msesage-block"
		infoClass="global-message-info" warnClass="global-message-warn"
		errorClass="global-message-error" fatalClass="global-message-fatal" />
	</h:form>
	<a4j:commandButton  
                type="submit" style="width:60px; margin-top:10px;margin-left: 640px; padding:0 8px" styleClass="lighting-button"
                value="Search"  title="Search"
				action="#{report.searchAction}"
				status="waitStatus"
				reRender="msesage-block,event-events-block,event-participation-block,client-status-block,usage-block"/>
</div>
			
<rich:modalPanel id="select-participants-dlg" keepVisualState="false" height="510" width="400">
	<f:facet name="header">
            <h:outputText value="Select Participant(s)"/>
	</f:facet>	  
	<h:form  id="participant-form"> 
	<table width="360px">
		<tr>
			<td width="30%"> <h:outputLabel style="font-size:11px" value="Participant Name: " styleClass="form-field"/> </td>
			<td width="40%"> <h:inputText   value="#{report.partSelection.participant.participantName}"></h:inputText>
			<td width="30%" align="right"> 
				<a4j:commandButton  value="Search" type="submit" title="Search" styleClass="dlg-button"
				action="#{report.partSelection.participant.searchAction}"
				limitToList="true"
				reRender="participantList"/>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<richext:treeTable id="participantList" value="#{report.partSelection.participant}" var="item" rows="1000" height="400px" width="100%" selectionMode="multiple">
						<richext:treeColumn sortBy="#{participantName}" sortable="true" width="100%">
							<f:facet name="header">
								   <h:outputText value="Participant Name" escape="false"/>
							   </f:facet>				
							<h:outputText/>
							<h:outputText value="#{item.participantName}"></h:outputText>
						</richext:treeColumn>
				</richext:treeTable>
			</td>
		</tr>
		<tr>
			<td  colspan="3" align="right">
			 <a4j:commandButton id="part_cancel" value="Cancel"  styleClass="dlg-button"
                          oncomplete="#{rich:component('select-participants-dlg')}.hide();"/>
			
			 <a4j:commandButton value="OK" oncomplete="#{rich:component('select-participants-dlg')}.hide();" styleClass="dlg-button"
					action="#{report.partSelection.okAction}" limitToList="true" reRender="filter-by-block,dynamic-filter-block">
					</a4j:commandButton>
			</td>
		</tr>
	</table>
</h:form>
</rich:modalPanel>

<rich:modalPanel id="select-event-dlg" keepVisualState="false" height="560" width="670">
	<f:facet name="header">
		<h:outputText value="Select An Event" />
	</f:facet>
	<h:form  id="event-form">
	<table width="100%">
		<tr>
			<td>
				<fieldset>
					<legend><b>Date/Time Range</b></legend>
					<h:panelGrid styleClass="form-input" columns="4" id="event-date-range-block">
						<h:outputLabel value="Start Date:" styleClass="form-field"/>
						<h:outputLabel value="Start Time:" styleClass="form-field"/>
						<h:outputLabel value="End Date:"  styleClass="form-field"/>
						<h:outputLabel value="End Time: " styleClass="form-field"/>
							
						<rich:calendar id="eventStartDateCal" 
											label="Start Date"
											value="#{report.eventSelection.searchCriteria.startDate}" 
											datePattern="#{report.dateFormat}"/>	
											
						<h:inputText size="5" maxlength="5" id="eventStartTime" value="#{report.eventSelection.searchCriteria.startTime}"></h:inputText>
						<rich:calendar id="eventEndDateCal"
											label="End Date"
											value="#{report.eventSelection.searchCriteria.endDate}"
											datePattern="#{report.dateFormat}"/>
						<h:inputText size="5" maxlength="5" id="eventEndTime" value="#{report.eventSelection.searchCriteria.endTime}"></h:inputText>
					</h:panelGrid>
				</fieldset>
			</td>
			<td>
				<fieldset>
					<legend><b>Program</b></legend>
					<h:panelGrid styleClass="form-input" columns="1" id="event-block" >
						<h:outputLabel escape="false" value="Program:" styleClass="form-field"/>
						
						<h:selectOneMenu id="allProgramSelection"  value="#{report.eventSelection.searchCriteria.program}"  style="width:150px">
								<f:selectItems value="#{report.allProgramOptionItems}"/>
						</h:selectOneMenu>
					</h:panelGrid>
				</fieldset>
			</td>
		</tr>
		<tr>
			<td colspan="2" align="right">
				<div style="margin-top:2px; margin-bottom:2px">
				<a4j:commandButton  value="Search"  type="submit" title="Search" styleClass="dlg-button"
				action="#{report.eventSelection.event.searchAction}"
				limitToList="true"
				reRender="eventList,event-date-range-block"/>
				</div>
			</td>
		</td>
		<tr>
			<td colspan="2" width="100%">
				<richext:treeTable id="eventList" 
					onRowDblClick="#{rich:element('evt-confirm-btn')}.click();"
					value="#{report.eventSelection.event}" var="item" rows="200" height="320" width="100%" selectionMode="single">
						<rich:column sortBy="#{eventName}" sortable="true" width="40%">
							<f:facet name="header">
								   <h:outputText value="Event Name" escape="false"/>
							   </f:facet>				
							<h:outputText/>
							<h:outputText value="#{item.eventName}"></h:outputText>
						</rich:column>
						<rich:column sortBy="#{startTime}" sortable="true" width="30%">
							<f:facet name="header">
								   <h:outputText value="Start Time" escape="false"/>
							   </f:facet>				
							<h:outputText/>
							<h:outputText value="#{item.startTime}">
							       <f:convertDateTime pattern="#{report.fullDateTimeFormat}"/>
							</h:outputText>
						</rich:column>
						<rich:column sortBy="#{endTime}" sortable="true" width="30%">
							<f:facet name="header">
								   <h:outputText value="End Time" escape="false"/>
							   </f:facet>				
							<h:outputText/>
							<h:outputText value="#{item.endTime}">
							       <f:convertDateTime pattern="#{report.fullDateTimeFormat}"/>
							</h:outputText>
						</rich:column>
				</richext:treeTable>
			</td>
		</tr>
	</table>
	<br>
	<table width="100%">
		<tr>
			<td width="80%"> </td>
			<td>
				 <a4j:commandButton id="cancel" value="Cancel"  styleClass="dlg-button"
                          oncomplete="#{rich:component('select-event-dlg')}.hide();"/>
			</td>
			<td>
			<a4j:commandButton id="evt-confirm-btn" styleClass="dlg-button" value="OK" oncomplete="#{rich:component('select-event-dlg')}.hide();"
					action="#{report.eventSelection.okAction}" limitToList="true" reRender="filter-by-block,dynamic-filter-block">
			</a4j:commandButton>
			</td>
		</tr>
	</table>
	</h:form>
</rich:modalPanel>

