<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
 <h:outputText id="SAIEventEmptyFlagA" style="display:none" value="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}"/>
<h:outputText id="SAIEventEmptyFlagS" style="display:none" value="#{drFrame.residentialUIBackingBean.scheduleSAIPREventGroup.eventEmptyFlag}"/> 
		  <div id="form:SAIOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  id="maxIconSAI" aria-expanded="true" name="maxIconSAI" href="#minIconSAI" style="cursor: pointer;" title="collapse" onclick="switchDisplaySAIMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
            <h:panelGroup id="SAIOpenTitle" layout="block">
			<div class="EventsOpenedHeaderTitleBg">
                 <div class="EventsMainTitle"><h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">Summer Advantage Incentive (SAI)</h3><div style="margin-top:-14px;"><label class="EventsSubTitle" for="panelSAICollapseNoEvents">(Also known as Critical Peak Pricing)</label></div></div> 
				
				<h:panelGroup layout="block" >
					<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>			
					</h:panelGroup>				
				</h:panelGroup>	
            </div>
			</h:panelGroup>	  
            <div class="EventsOpenContDiv">
				
				<h:panelGroup id="panelSAIExpandNoEvents" layout="block">
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}" layout="block">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" align="middle" class="NoEvenIcon" alt="no events in progress" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>	
				<h:panelGroup id="panelSAIExpandEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}">
					<div class="activeTitle">Active Events</div>
					<richext:dataTable 
						value="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.events}" 
						rendered="#{!drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}"
						var="item" 
						width="100%" 
						styleClass="EvtTable" 
						cellpadding="0" 
						cellspacing="0"
						title="test"
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
				<h:panelGroup id="panelSAIScheduleNoEvents" layout="block">
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.scheduleSAIPREventGroup.eventEmptyFlag}" layout="block">
						<div class="noEvenInfo"><img src="images/icons/info.gif" align="middle" alt="no event info"/> No events are currently scheduled for this program</div>
					</h:panelGroup>
				</h:panelGroup>
						
					
					<div id="form:SAIOpenedScheduleEventPanel" style="display:">
						<h:panelGroup id="panelSAIScheduleEventsMax" layout="block">
							<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.scheduleSAIPREventGroup.scheduledEventEmptyFlag}" layout="block" >
								<div class="viewScheduledEvents" ><a href="#form:SAIScheduleMinToMaxLink"  id="form:SAIScheduleMaxToMinLink" name="form:SAIScheduleMaxToMinLink"   style="cursor: pointer;" onclick="switchScheduleSAIMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
								<richext:dataTable 
									value="#{drFrame.residentialUIBackingBean.scheduleSAIPREventGroup.scheduledEvents}" 
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
						<h:panelGroup id="panelSAIScheduleEventsMin" layout="block" >
							<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.scheduleSAIPREventGroup.scheduledEventEmptyFlag}" layout="block" >
								<div class="viewScheduledEvents" ><a href="#form:SAIScheduleMaxToMinLink" id="form:SAIScheduleMinToMaxLink" name="form:SAIScheduleMinToMaxLink"  style="cursor: pointer;" onclick="switchScheduleSAIMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
							</h:panelGroup> 
						</h:panelGroup> 
					</div>
					<div class="viewEventAreas"><h:outputText value="#{drTextContext.contactInfoSaiResi}"/></div>  
              <div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  
          <div id="form:SAIClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  id="minIconSAI" aria-expanded="false" name="minIconSAI" href="#maxIconSAI"  style="cursor: pointer;" title="expand" onclick="switchDisplaySAIMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                 <div class="EventsMainTitle"><h3 style="font-family:Arial, Helvetica, sans-serif;font-size:14px;font-weight:bold;color:#417300;margin-top: -2px;position: relative;">Summer Advantage Incentive (SAI)</h3><div style="margin-top:-14px;"><label class="EventsSubTitle" for="panelSAICollapseNoEvents">(Also known as Critical Peak Pricing)</label></div></div> 
				
				<h:panelGroup id="panelSAICollapseNoEvents" layout="block" >
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div>No Events in Progress</div>
						</div>
					</h:panelGroup>
				</h:panelGroup>	
				
				<h:panelGroup id="panelSAICollapseEvents" layout="block" >
					<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.residentialUIBackingBean.activeSAIPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>			
					</h:panelGroup>				
				</h:panelGroup>	
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>

   