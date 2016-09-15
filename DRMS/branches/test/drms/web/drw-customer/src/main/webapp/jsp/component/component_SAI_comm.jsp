<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

 <h:outputText id="SAIEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}"/>  
<h:outputText id="SAIEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleSAIPREventGroup.eventEmptyFlag}"/>   
		  <div id="form:SAIOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true"  id="maxIconSAI" name="maxIconSAI" href="#minIconSAI" style="cursor: pointer;" title="collapse" onclick="switchDisplaySAIMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="SAIOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsMainTitle"><h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">Summer Advantage Incentive (SAI)</h3><div style="margin-top:-14px;"><label class="EventsSubTitle" for="panelSAIExpandNoEvents">(Also known as Critical Peak Pricing)</label></div></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>
				</h:panelGroup>	
            </div>
			</h:panelGroup>	
            <div class="EventsOpenContDiv">
				<!--1.1 Active Events--> 
				<h:panelGroup layout="block" id="panelSAIExpandNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>
				<h:panelGroup id="panelSAIExpandEvents">
				<h:panelGroup layout="block"  rendered="#{!drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">
				<div class="activeTitle">Active Events</div>
				<richext:dataTable 
					value="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}"
					var="item" 
					width="100%" 
					styleClass="EvtTable" 
					cellpadding="0" 
					cellspacing="0"
					summary="table with five columns indicated product, start date, end date, start time and end time for each activity"
					rowClasses="EvtTableCell,EvtTableCell_nobg">		
					<rich:column sortable="false" styleClass="rich-table-cell-first" >
						<f:facet name="header" >
							<h:outputText value="Product" escape="false" />
						</f:facet>
						<h:outputText  value="SAI">
						</h:outputText>
					</rich:column> 
					<rich:column sortable="false" >
						<f:facet name="header" >
							<h:outputText value="Start Date" escape="false" />
						</f:facet>
						<h:outputText  value="#{item.startDateString}">
						</h:outputText>
					</rich:column>
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="End Date" escape="false"  />
						</f:facet>
						<h:outputText  value="#{item.tbdEndDateString}">
						</h:outputText>
					</rich:column>
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Start Time" escape="false"  />
						</f:facet>
						<h:outputText  value="#{item.eventStartTimeString}">
						</h:outputText>
					</rich:column>
					<rich:column sortable="false" styleClass="rich-table-cell-last">
						<f:facet name="header">
							<h:outputText value="End Time" escape="false"  />
						</f:facet>
						<h:outputText  value="#{item.tbdEndTimeString}">
						</h:outputText>
					</rich:column>
				</richext:dataTable>
				</h:panelGroup>		
				</h:panelGroup>	
				<h:panelGroup layout="block" id="panelSAIScheduleNoEvents">		
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.scheduleSAIPREventGroup.scheduledEventEmptyFlag}">		
						<div class="noEvenInfo"><img src="images/icons/info.gif" align="middle" alt="no scheduled events"/> No events are currently scheduled for this program</div>
					</h:panelGroup> 
				</h:panelGroup>
					
					<div id="form:SAIOpenedScheduleEventPanel" style="display:">
						<h:panelGroup layout="block" id="panelSAIScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleSAIPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:SAIScheduleMinToMaxLink"  id="form:SAIScheduleMaxToMinLink" name="form:SAIScheduleMaxToMinLink"  style="cursor: pointer;" onclick="switchScheduleSAIMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable 
							value="#{drFrame.commercialUIBackingBean.scheduleSAIPREventGroup.scheduledEvents}" 
							rendered="true"
							var="item" 
							width="100%" 
							styleClass="EvtTable" 
							cellpadding="0" 
							cellspacing="0"
							summary="table with five columns indicated product, start date, end date, start time and end time for each activity"
							rowClasses="EvtTableCell,EvtTableCell_nobg">		
							<rich:column sortable="false" styleClass="rich-table-cell-first" >
								<f:facet name="header" >
									<h:outputText value="Product" escape="false" />
								</f:facet>
								<h:outputText  value="SAI">
								</h:outputText>
							</rich:column> 
							<rich:column sortable="false" >
								<f:facet name="header" >
									<h:outputText value="Start Date" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.startDateString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" >
								<f:facet name="header">
									<h:outputText value="End Date" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndDateString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" >
								<f:facet name="header">
									<h:outputText value="Start Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventStartTimeString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" styleClass="rich-table-cell-last">
								<f:facet name="header">
									<h:outputText value="End Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndTimeString}">
								</h:outputText>
							</rich:column>
						</richext:dataTable>
						</h:panelGroup> 
						</h:panelGroup>
					</div>
					
					<div id="form:SAIClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelSAIScheduleEventsMin">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleSAIPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:SAIScheduleMaxToMinLink" id="form:SAIScheduleMinToMaxLink" name="form:SAIScheduleMinToMaxLink"  style="cursor: pointer;" onclick="switchScheduleSAIMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
						</h:panelGroup> 
						</h:panelGroup>
					</div>
				    <div class="viewEventAreas"><h:outputText escape="false" value="#{drTextContext.contactInfoSaiCom}"/></div>  
              <div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  	
          <div id="form:SAIClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"  id="minIconSAI" name="minIconSAI" href="#maxIconSAI"  style="cursor: pointer;" title="expand" onclick="switchDisplaySAIMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsMainTitle"><h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">Summer Advantage Incentive (SAI)</h3><div style="margin-top:-14px;"><label class="EventsSubTitle" for="panelSAICollapseNoEvents">(Also known as Critical Peak Pricing)</label></div></div>
				
				<h:panelGroup layout="block" id="panelSAICollapseNoEvents">
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div>No Events in Progress</div>
					</div>
				</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelSAICollapseEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeSAIPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>
				</h:panelGroup>			
				</h:panelGroup>				
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
  

