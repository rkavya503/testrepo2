<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="CBPPanelGroup">
<h:outputText id="CBPEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}"/>
<h:outputText id="CBPEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleCBPPREventGroup.eventEmptyFlag}"/>
          <div id="form:CBPOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true" id="maxIconCBP" name="maxIconCBP" href="#minIconCBP"  style="cursor: pointer;" title="collapse" onclick="switchDisplayCBPMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="CBPOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsOpenedTitle"><h3>Capacity Bidding Program (CBP)</h3></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
            </div>
			</h:panelGroup>
            <div class="EventsOpenContDiv">
				
				<h:panelGroup layout="block" id="panelCBPExpandNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>
				<h:panelGroup layout="block" id="panelCBPExpandEvents">	
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}">	
				<div class="activeTitle">Active Events</div>
				<richext:dataTable 
					value="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}"
					var="item" 
					width="100%" 
					styleClass="EvtTable" 
					cellpadding="0" 
					cellspacing="0"
					summary="table with six columns indicated product, start date, end date, start time, end time and block for each activity"
					rowClasses="EvtTableCell,EvtTableCell_nobg">		
					<rich:column sortable="false" styleClass="rich-table-cell-first" >
						<f:facet name="header" >
							<h:outputText value="Product" escape="false" />
						</f:facet>
						<h:outputText  value="#{item.event.product}">
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
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="End Time" escape="false"  />
						</f:facet>
						<h:outputText  value="#{item.tbdEndTimeString}">
						</h:outputText>
					</rich:column>
					<rich:column sortable="false" style="width:120px;">
						<f:facet name="header" >
									<h:outputText value="Blocks" escape="false" />
						</f:facet>
						<h:outputText  value="#{item.blockString}">
						</h:outputText>
					</rich:column>
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Map" escape="false"  />
						</f:facet>
						<h:outputLink target="_blank" value="/dr.website/scepr-event-mapview.jsf">
							<f:verbatim>View</f:verbatim>
							<f:param name="productName" value="P" />
							<f:param name="eventKey" value="#{item.key}" />
							<f:param name="isActive" value="true" />
						</h:outputLink>
					</rich:column>
					<rich:column sortable="false" styleClass="rich-table-cell-last">
						<f:facet name="header">
							<h:outputText value="List" escape="false"  />
						</f:facet>
						<h:outputLink target="_blank" value="/dr.website/scepr-event-listview.jsf">
							<f:verbatim>View</f:verbatim>
							<f:param name="productName" value="P" />
							<f:param name="eventKey" value="#{item.key}" />
							<f:param name="isActive" value="true" />
						</h:outputLink>						
					</rich:column>
				</richext:dataTable>
				</h:panelGroup>
				</h:panelGroup>
              <div class="clearfix"></div>
              
				<h:panelGroup layout="block" id="panelCBPScheduleNoEvents">
				 
				</h:panelGroup>		
					
					<div id="form:CBPOpenedScheduleEventPanel" style="display:">
						<h:panelGroup layout="block" id="panelCBPScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleCBPPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a href="#form:CBPScheduleMinToMaxLink"  id="form:CBPScheduleMaxToMinLink" name="form:CBPScheduleMaxToMinLink"  style="cursor: pointer;" onclick="switchScheduleCBPMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable 
							value="#{drFrame.commercialUIBackingBean.scheduleCBPPREventGroup.scheduledEvents}" 
							rendered="true"
							var="item" 
							width="100%" 
							styleClass="EvtTable" 
							cellpadding="0" 
							cellspacing="0"
							summary="table with six columns indicated product, start date, end date, start time, end time and block for each activity"
							rowClasses="EvtTableCell,EvtTableCell_nobg">		
							<rich:column sortable="false" styleClass="rich-table-cell-first" >
								<f:facet name="header" >
									<h:outputText value="Product" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.event.product}">
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
							<rich:column sortable="false"> 
								<f:facet name="header">
									<h:outputText value="End Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndTimeString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" style="width:120px;">
								<f:facet name="header" >
									<h:outputText value="Blocks" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.blockString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" >
								<f:facet name="header">
									<h:outputText value="Map" escape="false"  />
								</f:facet>
								<h:outputLink target="_blank" value="/dr.website/scepr-event-mapview.jsf">
									<f:verbatim>View</f:verbatim>
									<f:param name="productName" value="P" />
									<f:param name="eventKey" value="#{item.key}" />
									<f:param name="isActive" value="false" />
								</h:outputLink>
							</rich:column>
							<rich:column sortable="false" styleClass="rich-table-cell-last">
								<f:facet name="header">
									<h:outputText value="List" escape="false"  />
								</f:facet>
								<h:outputLink target="_blank" value="/dr.website/scepr-event-listview.jsf">
									<f:verbatim>View</f:verbatim>
									<f:param name="productName" value="P" />
									<f:param name="eventKey" value="#{item.key}" />
									<f:param name="isActive" value="false" />
								</h:outputLink>						
							</rich:column>
						</richext:dataTable>
						</h:panelGroup> 
						</h:panelGroup>
					</div>
					<div id="form:CBPClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelCBPScheduleEventsMin">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleCBPPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:CBPScheduleMaxToMinLink" id="form:CBPScheduleMinToMaxLink" name="form:CBPScheduleMinToMaxLink" style="cursor: pointer;" onclick="switchScheduleCBPMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
						</h:panelGroup> 
						</h:panelGroup>
					</div>
				
              
				<h:panelGroup layout="block" id="CBPInformationPanel" >
                    <div class="viewEventAreas">This web site displays CBP events activated by SCE. <br/><br/> Check with your CBP Aggregator for details of your participation in CBP events.</div>
				</h:panelGroup> 
				<div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  
          <div id="form:CBPClosePanelGroup" >
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"   id="minIconCBP" name="minIconCBP" href="#maxIconCBP" style="cursor: pointer;" title="expand" onclick="switchDisplayCBPMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsClosedTitle"><h3>Capacity Bidding Program (CBP)</h3></div>
				
				<h:panelGroup layout="block" id="panelCBPCollapseNoEvents">
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div>No Events in Progress</div>
					</div>
				</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelCBPCollapseEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeCBPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
				</h:panelGroup>
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</h:panelGroup>		  

