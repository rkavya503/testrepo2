<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">

<head>
	<jsp:include page="/jsp/head.jsp" />
</head>
	<f:view>
        	<a4j:keepAlive beanName="demoEvent" />
            <f:phaseListener type="com.akuacom.pss2.richsite.event.demo.DemoEventPhaseListener" />
		<jsp:include page="/jsp/header_events.jsp" />
	<h:form id="form">
	<a4j:region>
		<a4j:keepAlive beanName="demoEvent" />
		
		<!-- Validation Message Component -->
		<rich:spacer height="5px" width="700px" />
		<rich:messages id="message" layout="table"
							infoClass="global-message-info" warnClass="global-message-warn"
							errorClass="global-message-error" fatalClass="global-message-fatal"/>
		<rich:spacer height="5px" width="700px" />

		<div id="demoEventInfo">
			<div class="demoEventBasicInfo">
            	<div id="demoEventInfo_header"><div class="demoEventInfoTitle">Program <h:outputText value="#{demoEvent.programName}"/></div></div>
				<div>
					<table>
						<!--Row1 Notification-->
						<tr>
							<td>
								<h:outputLabel for="notificationTime"  value="Notification Time:" styleClass="rich-panel-field"/>
							</td>
							<td>
								<h:inputText id="notificationTime"  styleClass="event-time-input"
										value="#{demoEvent.notificationTimeStr}"
										required="true" label="#{demoEvent.notificationTimeStr}"
										requiredMessage="notification time is required"
										title="notification time"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{demoEvent.notificationTimeChange}">
										<a4j:support event="onchange" reRender="notificationTime,startTimeCal,message,notice,timeOffset,eventTime"
												ajaxSingle="true" limitToList="true"/>
										<f:converter converterId="HourMinuteConverter"/>
								</h:inputText>
							</td>
							<td>
								<rich:calendar id="notificationTimeCal" label="notification Date"
									styleClass="event-time-calendar" value="#{demoEvent.notificationDateOnly}"
									valueChangeListener="#{demoEvent.notificationDateChange}"
									popup="true">
									<a4j:support event="onchanged" reRender="notificationTimeCal,notice,message,startTimeCal,timeOffset,eventTime"
												ajaxSingle="true" limitToList="true"/>
								</rich:calendar>
							</td>
						</tr>
						<!--Row2 Notice-->
						<tr>
							<td>
								<h:outputLabel for="notice" value="Notice(min):" styleClass="rich-panel-field"/>
							</td>
							<td>
								<h:inputText id="notice"  styleClass="event-time-input"
										value="#{demoEvent.notice}"
										required="true" label="#{demoEvent.notice}"
										requiredMessage="Notice time span is required"
										title="Notice time span"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{demoEvent.noticeTimeChange}">
										<a4j:support event="onchange" reRender="message,notificationTime,notificationTimeCal,timeOffset,eventTime"
												ajaxSingle="true" limitToList="true"/>
										<f:converter converterId="javax.faces.Integer"/>
									</h:inputText>
							</td>
							<td>
								
							</td>
						</tr>
						<!--Row3 Start Time-->
						<tr>
							<td>
								<h:outputLabel for="startTime" value="Start Time:" styleClass="rich-panel-field"/>
							</td>
							<td>
								<h:inputText id="startTime" styleClass="event-time-input"
										value="#{demoEvent.startTimeStr}"
										required="true" label="#{demoEvent.startTimeStr}"
										requiredMessage="start time is required"
										title="Event start time"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{demoEvent.startTimeChange}">
										<a4j:support event="onchange" reRender="startTime,startTimeCal,message,notice,notificationTime,notificationTimeCal,endTime,timeOffset,eventTime"
												ajaxSingle="true" limitToList="true"/>
										<f:converter converterId="HourMinuteConverter"/>
									</h:inputText>
							</td>
							<td>
								<rich:calendar id="startTimeCal"  value="#{demoEvent.startDateOnly}"
									styleClass="event-time-calendar"  label="Event Start date"
									valueChangeListener="#{demoEvent.startDateChange}"
									popup="true">
									<a4j:support event="onchanged" reRender="startTime,startTimeCal,message,notice,notificationTime,notificationTimeCal,endTime,timeOffset,eventTime"
												ajaxSingle="true" limitToList="true"/>
								</rich:calendar>
							</td>
						</tr>
						<!--Row4 Duration-->
						<tr>
							<td>
								<h:outputLabel for="duration" value="Duration (min):" styleClass="rich-panel-field"/>
							</td>
							<td>
								<h:inputText id="duration" styleClass="event-time-input"
										value="#{demoEvent.duration}"
										required="true" label="#{demoEvent.duration}"
										requiredMessage="Event duration is required"
										title="Event duration"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{demoEvent.durationTimeChange}">
										<a4j:support event="onchange" reRender="message,endTime,timeOffset,eventTime,offsetFromNotification,startTimeCal"
												ajaxSingle="true" limitToList="true"/>
										<f:converter converterId="javax.faces.Integer"/>
									</h:inputText>
							</td>
							<td>
								
							</td>
						</tr>
						<!--Row5 End Time-->
						<tr>
							<td>
								<h:outputLabel for="endTime" value="End Time:" styleClass="rich-panel-field"/>
							</td>
							<td>
								<h:inputText id="endTime"  styleClass="event-time-input"
										value="#{demoEvent.endTimeStr}"
										required="true" label="#{demoEvent.endTimeStr}"
										requiredMessage="Event End Time is required"
										title="Event End Time"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{demoEvent.endTimeChange}">
										<a4j:support event="onchange" reRender="endTime,message,duration,timeOffset,eventTime,offsetFromNotification"
												ajaxSingle="true" limitToList="true"/>
										<f:converter converterId="HourMinuteConverter"/>
									</h:inputText>
							</td>
							<td>
								
							</td>
						</tr>
					</table>
				</div>
			</div>

			<div class="demoEventSignalInfo">
                	<a4j:keepAlive beanName="demoEvent" />
            	<!--<div id="demoEventInfo_header"><div class="demoEventInfoTitle">Event Signal</div></div>-->
					<rich:dataTable id="events"  value="#{demoEvent.events}" var="evtinstance" styleClass="event-rich-table grid-input">
						<rich:column width="10%">
							<f:facet name="header">
								<h:outputText value="Action" title="Action" />
							</f:facet>
							<div class="btn-div">
							 <a4j:commandButton alt="Add An Event Time Block"
									actionListener="#{demoEvent.addSingalEntry}"
									rendered="#{evtinstance.addable}"
									styleClass="btn-grid btn-add"
									title="Add An Event Time Block"
									reRender="message,events">
									<f:attribute name="evtid" value="#{evtinstance.id}" />
							 </a4j:commandButton>
							  <a4j:commandButton alt="Remove Event Time Block"
									actionListener="#{demoEvent.removeSingalEntry}"
									rendered="#{evtinstance.removable}"
									styleClass="btn-grid btn-remove"
									title="Remove Event Time Block"
									reRender="message,events">
									<f:attribute name="evtid" value="#{evtinstance.id}" />
							 </a4j:commandButton>
							 </div>
                        </rich:column>
						<rich:column width="12%">
							<f:facet name="header">
								<h:outputText value="Date/Time" title="Date Time" />
							</f:facet>
							<h:outputText id="eventTime" value="#{evtinstance.dateTime}">
								<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}" />
							</h:outputText>
                        </rich:column>
                        <rich:column width="10%">
							<f:facet name="header">
								<h:outputText  styleClass="cls" value="Absolute Offset from Notification (mins)" title="minute offset" />
							</f:facet>
							<h:inputText id="offsetFromNotification"
								value="#{evtinstance.offsetFromNotification}"
								required="true" label="#{evtinstance.offsetFromNotification}"
								title="Absolute offset from notification time"
								styleClass="numInput"
								valueChangeListener="#{demoEvent.eventOffSetChange}"
								onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
								rendered ="#{evtinstance.offsetEditable}">
								<a4j:support event="onchange" reRender="message,notice,startTime,duration,endTime,eventTime"
									ajaxSingle="true" limitToList="true"/>
								<f:converter converterId="javax.faces.Integer"/>
							</h:inputText>
							<h:outputText id="timeOffset" value="#{evtinstance.offsetFromNotification}"
								rendered="#{!evtinstance.offsetEditable}"/>
                        </rich:column>
                        <rich:column width="10%" id="rtime">
							<f:facet name="header">
							   <h:outputText value="Event Status" title="Event Status" />
							</f:facet>
							<h:outputText value="#{evtinstance.eventStatus}" id="eventStatus" />
                        </rich:column>
						<!-- dynamic columns decided by enabled signals, pedding signal is not included -->
						<rich:columns var="column" index="index" 
							value="#{demoEvent.enabledSignalTypes}" width="10%">
								<f:facet name="header">
										<h:outputText value="#{column} <br> #{demoEvent.enabledSignals[index].type}" escape="false"/>
								</f:facet>
								<!-- for mode signal -->
								<h:selectOneMenu id="modeSingal" value="#{evtinstance.signalValues[column]}"
									rendered="#{!evtinstance.readOnly&&column=='mode'}">
									<f:selectItems value="#{evtinstance.signalModes}"/>
								</h:selectOneMenu>
								<!-- for other signal -->
								<h:inputText id="othersignal"  											
									value="#{evtinstance.signalValues[column]}" 
									required="true" label="#{column}"
									title="#{column}"
									styleClass="numInput"
									onkeydown="if((event || window.event).keyCode==13) return false;"
									rendered ="#{!evtinstance.readOnly && column!='mode'}">
									<f:converter converterId="javax.faces.Double"/>
								</h:inputText>
								<!-- readonly, start, end. e.g -->
								<h:outputLabel value="" rendered="#{evtinstance.readOnly}"/>
						</rich:columns>
					</rich:dataTable>
            </div>
		</div>

		<rich:spacer height="20px" width="700px" />
        	<a4j:keepAlive beanName="demoEvent" />
		<!-- Participant List waiting for choise which are the program contained -->

        <%--
        <rich:dataTable id="participants" value="#{demoEvent.allParticipantsInProgram}" var="item" width="100%" styleClass="event-rich-table ">
			<f:facet name="header">
                <h:outputText value="Participants"></h:outputText>
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
			<rich:column sortable="false" >
                <f:facet name="header">
                    <h:outputText value="Account#" escape="false"/>
                </f:facet>
				<h:outputText value="#{item.participant.accountNumber}" />
			</rich:column>
        </rich:dataTable>

		<a4j:commandButton value="Select All" action="#{demoEvent.selectedAllParticipants}" immediate="true" title="Select All participants" reRender="participants"/>
		<a4j:commandButton value="Select None" action="#{demoEvent.selectedNoneParticipants}" immediate="true" title="Select None participants" reRender="participants"/>
        --%>
		<div style="text-align: center;">
			<h:commandButton value="Confirm" action="#{demoEvent.confirm}" title="Submit and Issue Event"/>
			<h:commandButton value="Cancel" action="#{demoEvent.cancel}" immediate="true" title="Cancel Submit and Issue Event"/>
		</div>
	</a4j:region>
	</h:form>
		<jsp:include page="/jsp/footer.jsp" />
	</f:view>
</HTML>
