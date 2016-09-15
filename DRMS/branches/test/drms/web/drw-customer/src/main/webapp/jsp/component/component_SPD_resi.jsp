<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<div id="SPDDIV">
 <h:outputText id="SPDEventEmptyFlagA" style="display:none" value="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}"/>      
<h:outputText id="SPDEventEmptyFlagS" style="display:none" value="#{drFrame.residentialUIBackingBean.scheduleSPDPREventGroup.eventEmptyFlag}"/>       
		  <div id="form:SPDOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true"  id="maxIconSPD" name="maxIconSPD" href="#minIconSPD" style="cursor: pointer;" title="collapse" onclick="switchDisplaySPDMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
            <h:panelGroup id="SPDOpenTitle" layout="block">
			<div class="EventsOpenedHeaderTitleBg">
                 <div class="EventsMainTitle"><h3>Save Power Day (SPD)</h3></div> 
				
				<h:panelGroup layout="block" >
					<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>			
					</h:panelGroup>				
				</h:panelGroup>	
            </div>
			</h:panelGroup>	  
            <div class="EventsOpenContDiv">
				
				<h:panelGroup id="panelSPDExpandNoEvents" layout="block">
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}" layout="block">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" align="middle" class="NoEvenIcon" alt="no events in progress" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>	
				<h:panelGroup id="panelSPDExpandEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}">
					<div class="activeTitle">Active Events</div>
					<richext:dataTable 
						value="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.events}" 
						rendered="#{!drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}"
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
							<h:outputText  value="SPD">
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
				<h:panelGroup id="panelSPDScheduleNoEvents" layout="block">
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.scheduleSPDPREventGroup.eventEmptyFlag}" layout="block">
						<div class="noEvenInfo"><img src="images/icons/info.gif" align="middle" alt="no event info"/> No events are currently scheduled for this program</div>
					</h:panelGroup>
				</h:panelGroup>
						
					
					<div id="form:SPDOpenedScheduleEventPanel" style="display:">
						<h:panelGroup id="panelSPDScheduleEventsMax" layout="block">
							<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.scheduleSPDPREventGroup.scheduledEventEmptyFlag}" layout="block" >
								<div class="viewScheduledEvents" ><a  href="#form:SPDScheduleMinToMaxLink"  id="form:SPDScheduleMaxToMinLink" name="form:SPDScheduleMaxToMinLink"   style="cursor: pointer;" onclick="switchScheduleSPDMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
								<richext:dataTable 
									value="#{drFrame.residentialUIBackingBean.scheduleSPDPREventGroup.scheduledEvents}" 
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
										<h:outputText  value="SPD">
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
					<div id="form:SPDClosedScheduleEventPanel" style="display:none">
						<h:panelGroup id="panelSPDScheduleEventsMin" layout="block" >
							<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.scheduleSPDPREventGroup.scheduledEventEmptyFlag}" layout="block" >
								<div class="viewScheduledEvents" ><a   href="#form:SPDScheduleMaxToMinLink" id="form:SPDScheduleMinToMaxLink" name="form:SPDScheduleMinToMaxLink" style="cursor: pointer;" onclick="switchScheduleSPDMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
							</h:panelGroup> 
						</h:panelGroup> 
					</div>
					<div class="viewEventAreas"><h:outputText value="#{drTextContext.contactInfoSpdResi}"/></div>  
              <div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  
          <div id="form:SPDClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"   id="minIconSPD" name="minIconSPD" href="#maxIconSPD"  style="cursor: pointer;" title="expand" onclick="switchDisplaySPDMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                 <div class="EventsMainTitle"><h3>Save Power Day (SPD)</h3></div> 
				
				<h:panelGroup id="panelSPDCollapseNoEvents" layout="block" >
					<h:panelGroup rendered="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div>No Events in Progress</div>
						</div>
					</h:panelGroup>
				</h:panelGroup>	
				
				<h:panelGroup id="panelSPDCollapseEvents" layout="block" >
					<h:panelGroup rendered="#{!drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventEmptyFlag}" layout="block" >
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.residentialUIBackingBean.activeSPDPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>			
					</h:panelGroup>				
				</h:panelGroup>	
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</div>
   