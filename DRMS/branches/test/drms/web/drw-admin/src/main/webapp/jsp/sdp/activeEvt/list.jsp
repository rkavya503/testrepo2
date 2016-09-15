<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:form id="confirmForm">
<a4j:keepAlive beanName="activeEvents"/>

<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title">SDP - Active Events</span>
</rich:panel> 
<div  style="margin-left:22px">

<rich:spacer height="10px" width="50px"/>
<table width="900px">
	<tr>
		 <td id="enrolled-table-col">
			
			<richext:treeTable id="confirmParticipantList" value="#{activeEvents.locationProvider}" var="item" rows="10000"  selectionMode="multiple"
					 onSelection="_selChange($('enrolled-table-col'),#{rich:element('deleteButton')});_selChange($('enrolled-table-col'),#{rich:element('endBtn')});_selChange2($('enrolled-table-col'),#{rich:element('updateBtn')});" height="380px" width="100%">
				
				<richext:treeColumn width="1px" >
						
				</richext:treeColumn>								
				<rich:column sortBy="#{event.issuedTime}" width="80px" >
						<f:facet name="header">
							   <h:outputText value="Issue <br/> Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.issuedTime}">
							<f:convertDateTime pattern="MM/dd/yyyy" />
						</h:outputText>
						<br>
						<h:outputText value="#{item.event.issuedTime}">
							<f:convertDateTime pattern="HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column sortBy="#{event.startTime}" width="80px" style="word-wrap: break-word;white-space: normal;">
						<f:facet name="header">
							   <h:outputText value="Start <br/> Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.startTime}">
							<f:convertDateTime pattern="MM/dd/yyyy" />
						</h:outputText>
						<br>
						<h:outputText value="#{item.event.startTime}">
							<f:convertDateTime pattern="HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column sortBy="#{estimatedEndTime}" width="90px" >
						<f:facet name="header">
							   <h:outputText value="Estimated End<br/>Date&Time" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.estimatedEndTime!=null?item.estimatedEndTime:'-'}">
							<f:convertDateTime pattern="MM/dd/yyyy" />
						</h:outputText>
						<br>
						<h:outputText value="#{item.estimatedEndTime!=null?item.estimatedEndTime:''}">
							<f:convertDateTime pattern="HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column sortBy="#{actualEndTime}" width="80px" >
						<f:facet name="header">
							   <h:outputText value="Actual End<br/>Date&Time" escape="false"/>
						   </f:facet>
						<h:outputText styleClass="#{item.actualEndTime!=null?'actualEnd':''}" value="#{item.actualEndTime!=null?item.actualEndTime:'-'}">
							<f:convertDateTime pattern="MM/dd/yyyy" />
						</h:outputText>
						<br>
						<h:outputText value="#{item.actualEndTime!=null?item.actualEndTime:''}">
							<f:convertDateTime pattern="HH:mm:ss" />
						</h:outputText>
				</rich:column>
				<rich:column width="60px">
						<f:facet name="header">
							   <h:outputText value="Dispatch<br/>Type" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.allLocationType==null?item.location.type:item.allLocationType}"></h:outputText>
				</rich:column>
				
                <rich:column width="60px" >
                        <f:facet name="header">
                               <h:outputText value="Location#" escape="false"/>
                           </f:facet>               
                        <h:outputText value="#{item.allLocationType==null?item.location.number:'-'}" title="#{item.allLocationType==null?item.location.number:'-'}" />                                                    
                </rich:column>
				
				<rich:column width="100px">
						<f:facet name="header">
							   <h:outputText value="Dispatch Location" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.allLocationType==null?item.location.name:'All'}" title="#{item.allLocationType==null?item.location.name:'All'}">
                            <richext:subStringConverterTag length="20"></richext:subStringConverterTag>
                         </h:outputText>
				</rich:column>	
				<rich:column sortBy="#{event.product}" width="50px" >
						<f:facet name="header">
							   <h:outputText value="Rate" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.product}"></h:outputText>
				</rich:column>			
				<rich:column >
						<f:facet name="header">
							   <h:outputText value="Comments" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.event.comment}" title="#{item.event.comment}"><richext:subStringConverterTag length="50"></richext:subStringConverterTag></h:outputText>
				</rich:column>
				
			</richext:treeTable>
		</td>
		
	</tr>
</table>
<h:outputText value="* You can't update Estimated End Date / Time for an event with Actual End Date / Time." />
<rich:spacer height="10px" width="50px"/>
</div>
<rich:panel styleClass="wizard-button-container" style="border-top: 1px solid #DDDDDD"> 
	<a4j:commandButton value="End" styleClass="wizard-btn" style="margin-left:15px" id="endBtn"
			action="#{activeEvents.goToEnd}" title="End Event"	
			limitToList="true" reRender="event-creation-wizard" />
	<a4j:commandButton value="Update Estimated End Date & Time" styleClass="wizard-btn" style="width: 180px;" id="updateBtn"
	action="#{activeEvents.goToEdit}" title="Update Estimated End Date & Time"	
	limitToList="true" reRender="event-creation-wizard" />
	<a4j:commandButton value="Delete" styleClass="wizard-btn" id="deleteButton"
             oncomplete="#{rich:component('comfirmpanel')}.show();"
             title="Delete"    
            limitToList="true" reRender="comfirmpanel,event-creation-wizard"/>
	<a4j:commandButton value="Exit" styleClass="wizard-btn" 
			action="#{activeEvents.doExit}" title="Exit"
			limitToList="true" reRender="event-creation-wizard"/>
</rich:panel>
</h:form>