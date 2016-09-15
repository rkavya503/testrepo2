
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="DBPPanelGroup">
 <h:outputText id="DBPEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}"/>  
<h:outputText id="DBPEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleDBPPREventGroup.eventEmptyFlag}"/>   
		  <div id="form:DBPOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true" id="maxIconDBP" name="maxIconDBP" href="#minIconDBP" style="cursor: pointer;" title="collapse" onclick="switchDisplayDBPMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="DBPOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsOpenedTitle"><h3>Demand Bidding Program (DBP)</h3></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
            </div>
			</h:panelGroup>
            <div class="EventsOpenContDiv">
				<!--1.1 Active Events--> 
				<h:panelGroup layout="block" id="panelDBPExpandNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>
				<h:panelGroup id="panelDBPExpandEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}">	
				<div class="activeTitle">Active Events</div>
				<richext:dataTable 
					value="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}"
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
						<h:outputText  value="#{item.longProgramName}">
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
				<!--1.2 Scheduled Events-->
				<h:panelGroup layout="block" id="panelDBPScheduleNoEvents">		
				<!-- ## commented for to resolve the bug DRMS-8639
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.scheduleDBPPREventGroup.scheduledEventEmptyFlag}">		
					<div class="noEvenInfo"><img src="images/icons/info.gif" align="middle" alt="no scheduled events" /> DBP scheduled events will appear after 4:30 PM </div>
				</h:panelGroup>
				[END] DRMS-8639 -->
				</h:panelGroup>
					
					<div id="form:DBPOpenedScheduleEventPanel" style="display:">
						<h:panelGroup layout="block" id="panelDBPScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleDBPPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:DBPScheduleMinToMaxLink"  id="form:DBPScheduleMaxToMinLink" name="form:DBPScheduleMaxToMinLink"   style="cursor: pointer;" onclick="switchScheduleDBPMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable 
							value="#{drFrame.commercialUIBackingBean.scheduleDBPPREventGroup.events}" 
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
								<h:outputText  value="#{item.longProgramName}">
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
					<div id="form:DBPClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelDBPScheduleEventsMin">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleDBPPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:DBPScheduleMaxToMinLink" id="form:DBPScheduleMinToMaxLink" name="form:DBPScheduleMinToMaxLink"  style="cursor: pointer;" onclick="switchScheduleDBPMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
						</h:panelGroup> 
						</h:panelGroup>
					</div>
					<div class="viewEventAreas">
						For event information, please visit <a href="http://www.sce.com/myaccount">www.sce.com/myaccount</a> or call 1-866-334-7827.
					</div>  
				
              <div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  	
          <div id="form:DBPClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"  id="minIconDBP" name="minIconDBP" href="#maxIconDBP"  style="cursor: pointer;" title="expand" onclick="switchDisplayDBPMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsClosedTitle"><h3>Demand Bidding Program (DBP)</h3></div> 
				
				<h:panelGroup layout="block" id="panelDBPCollapseNoEvents">
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div>No Events in Progress</div>
					</div>
				</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelDBPCollapseEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeDBPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
				</h:panelGroup>				
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</h:panelGroup>		  

