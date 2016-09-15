
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGroup layout="block" id="SDPPanelGroup">
<h:outputText id="SDPEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}"/>
<h:outputText id="SDPEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleSDPPREventGroup.eventEmptyFlag}"/>
		  <div id="form:SDPOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true"  id="maxIconSDP" name="maxIconSDP" href="#minIconSDP" style="cursor: pointer;" title="collapse" onclick="switchDisplaySDPMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="SDPOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsOpenedTitle"><h3>Summer Discount Plan (SDP) &#8212; Commercial</h3></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
			</h:panelGroup>
            </div>
			</h:panelGroup>
            <div class="EventsOpenContDiv">
				<!--1.1 Active Events-->
				<h:panelGroup layout="block" id="panelSDPExpandNoEvents">	
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">	
					<table class="NoEventsDateTable">
						<tr>
						  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
						</tr>
					</table>					
				</h:panelGroup>
				</h:panelGroup>
				<h:panelGroup layout="block" id="panelSDPExpandEvents">	
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">
					<div class="EventAlertTitle">Air Conditioners Are Being Cycled-Off</div>
					<div class="EventAlertTag">Click on Map - View or List - View below to see if your area is impacted.</div>	
					<div class="activeTitle">Active Events</div>				
				<richext:dataTable 
					value="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}"
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
									<h:outputText value="Estimated End Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndTimeString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" >
								<f:facet name="header">
									<h:outputText value="Map" escape="false"  />
								</f:facet>
								<h:outputLink target="_blank" value="/dr.website/scepr-event-mapview.jsf">
									<f:verbatim>View</f:verbatim>
									<f:param name="productName" value="C" />
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
									<f:param name="productName" value="C" />
									<f:param name="eventKey" value="#{item.key}" />
									<f:param name="isActive" value="true" />
								</h:outputLink>						
							</rich:column>
				</richext:dataTable>
              
				</h:panelGroup>
				</h:panelGroup>
              <div class="clearfix"></div>
			  <!--1.2 Scheduled Events-->
				<h:panelGroup layout="block" id="panelSDPScheduleNoEvents">
				
				</h:panelGroup>	
				
					
					<div id="form:SDPOpenedScheduleEventPanel" style="display:inline">
						<h:panelGroup layout="block" id="panelSDPScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleSDPPREventGroup.scheduledEventEmptyFlag}">		
						<div class="viewScheduledEvents" ><a  href="#form:SDPScheduleMinToMaxLink"  id="form:SDPScheduleMaxToMinLink" name="form:SDPScheduleMaxToMinLink"  style="cursor: pointer;" onclick="switchScheduleSDPMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable 
							value="#{drFrame.commercialUIBackingBean.scheduleSDPPREventGroup.scheduledEvents}" 
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
											<h:outputText value="Estimated End Time" escape="false"  />
										</f:facet>
										<h:outputText  value="#{item.tbdEndTimeString}">
										</h:outputText>
									</rich:column>
									<rich:column sortable="false" >
										<f:facet name="header">
											<h:outputText value="Map" escape="false"  />
										</f:facet>
										<h:outputLink target="_blank" value="/dr.website/scepr-event-mapview.jsf">
											<f:verbatim>View</f:verbatim>
											<f:param name="productName" value="C" />
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
											<f:param name="productName" value="C" />
											<f:param name="eventKey" value="#{item.key}" />
											<f:param name="isActive" value="false" />
										</h:outputLink>						
									</rich:column>
						</richext:dataTable>
						</h:panelGroup>
						</h:panelGroup>
					</div>
					
					<div id="form:SDPClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelSDPScheduleEventsMin">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleSDPPREventGroup.scheduledEventEmptyFlag}">		
						<div class="viewScheduledEvents" ><a  href="#form:SDPScheduleMaxToMinLink" id="form:SDPScheduleMinToMaxLink" name="form:SDPScheduleMinToMaxLink"  style="cursor: pointer;" onclick="switchScheduleSDPMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
						</h:panelGroup>
						</h:panelGroup>
					</div>
              
				<h:panelGroup layout="block" id="SDPInformationPanel" >
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleSDPPREventGroup.eventEmptyFlag||!drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">
                            <div class="viewEventAreas" >
                                <h:outputText value="#{drTextContext.disclaimerSDPCommercial}"/>
                            </div>
                        </h:panelGroup>
				</h:panelGroup> 
				<div class="viewEventAreas"><h:outputText value="For event information, please call 1-800-497-2813."/></div>  
				<div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  
          <div id="form:SDPClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"  id="minIconSDP" name="minIconSDP" href="#maxIconSDP"  style="cursor: pointer;" title="expand" onclick="switchDisplaySDPMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsClosedTitle"><h3>Summer Discount Plan (SDP) &#8212; Commercial</h3></div>
				
				<h:panelGroup layout="block" id="panelSDPCollapseNoEvents">
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div>No Events in Progress</div>
					</div>
				</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelSDPCollapseEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeSDPPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
				</h:panelGroup>
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</h:panelGroup>

