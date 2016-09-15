<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:form id="confirmEndForm">
<a4j:keepAlive beanName="activeEvents"/>
<richext:set beanName="evtTiming" value="#{activeEvents.eventTiming}"/>
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title">BIP - End Active Events</span>
</rich:panel> 
<div  style="margin-left:22px">

<rich:spacer height="10px" width="50px"/>
<table width="900px">
	<tr>
		<td>
			<h:panelGrid styleClass="filter-form-block" columns="2" id="reject-filter-block" style="width:100%">
				<h:panelGroup >
					<h:graphicImage id="requiredName" alt="required" title="required"  url="/images/layout/required.jpg" />
					<h:outputText style="text-align: left;font-size:11px"   value="Actual End Date & Time:"/>
					    <a4j:region>
		                        <h:panelGroup>
		                            <rich:calendar id="startTime"
		                                  value="#{evtTiming.startDate}"  
		                                  datePattern="MM-d-yyyy"
		                                  popup="true">
		                            </rich:calendar>
		                            <h:panelGroup>
		                                <h:selectOneMenu id="hour" value="#{evtTiming.startHour}">
		                                    <f:selectItems value="#{evtTiming.hourList}"/>
		                                </h:selectOneMenu>
		                                :
		                                <h:selectOneMenu id="minute" value="#{evtTiming.startMin}">
		                                    <f:selectItems value="#{evtTiming.minList}"/>
		                                </h:selectOneMenu>
		                                :
		                                <h:selectOneMenu id="second" value="#{evtTiming.startSec}">
		                                    <f:selectItems value="#{evtTiming.secList}"/>
		                                </h:selectOneMenu>
		                            </h:panelGroup>
		                        </h:panelGroup>
		                </a4j:region>   					
				</h:panelGroup>	 
				<input type="button" value="Clear Date" onclick="clearDate();" style="margin-left:15px" title="Clear Date" class="wizard-btn" />
					
				</h:panelGrid>
		</td>
	</tr>
	<tr>
		<td>
			<richext:treeTable id="confirmParticipantList" value="#{activeEvents.selectedParticipants}" var="item" rows="10000" 
					height="380px" width="100%">
				
				<rich:column width="5%">
					<f:facet name="header">
							<h:outputText value="No." escape="false"  />
					</f:facet>
					   <h:outputText value="#{item.rowIndex+1}"/>  
				</rich:column>
				<rich:column sortBy="#{event.startTime}" width="15%" >
						<f:facet name="header">
							   <h:outputText value="Start Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.startTime}">
							<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column sortBy="#{estimatedEndTime}" width="15%" >
						<f:facet name="header">
							   <h:outputText value="Estimated End<br/>Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.estimatedEndTime!=null?item.estimatedEndTime:'-'}">
							<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column sortBy="#{actualEndTime}" width="15%" >
						<f:facet name="header">
							   <h:outputText value="Actual End<br/> Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.actualEndTime!=null?item.actualEndTime:'-'}">
							<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column width="10%">
						<f:facet name="header">
							   <h:outputText value="Dispatch<br/>Type" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.allLocationType==null?item.location.type:item.allLocationType}"></h:outputText>
				</rich:column>
				
			    <rich:column width="10%">
                        <f:facet name="header">
                               <h:outputText value="Location#" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.allLocationType==null?item.location.number:'-'}" title="#{item.allLocationType==null?item.location.number:'-'}" />                                                    
                </rich:column>
				
				<rich:column  >
						<f:facet name="header">
							   <h:outputText value="Dispatch Location" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.allLocationType==null?item.location.name:'All'}" title="#{item.allLocationType==null?item.location.name:'All'}">
                            <richext:subStringConverterTag length="20"></richext:subStringConverterTag>
                         </h:outputText>
				</rich:column>	
				<rich:column  width="25%"  >
						<f:facet name="header">
							   <h:outputText value="Comments" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.comment}" title="#{item.event.comment}"><richext:subStringConverterTag length="50"></richext:subStringConverterTag></h:outputText>
				</rich:column>
			</richext:treeTable>
		</td>
		
	</tr>
</table>
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
	<a4j:commandButton value="Submit" styleClass="wizard-btn" style="margin-left:15px"
			action="#{activeEvents.doEnd}" title="Submit"	
			limitToList="true" reRender="event-creation-wizard"/>
	<a4j:commandButton value="Cancel" reRender="event-creation-wizard" action="#{activeEvents.doCancel}" 
	   oncomplete="resetButtonStatus();"
	   immediate="true" title="Cancel" styleClass="wizard-btn"/>
	<a4j:commandButton value="Exit" reRender="event-creation-wizard" action="#{activeEvents.doExit}" immediate="true" title="Exit" styleClass="wizard-btn"/>
</rich:panel>
</h:form>