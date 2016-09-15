<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<h:panelGroup layout="block" id="APIPanelGroup">
<h:outputText id="APIEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}"/>
<h:outputText id="APIEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleAPIPREventGroup.eventEmptyFlag}"/>
          <div id="form:APIOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true" id="maxIconAPI" name="maxIconAPI" href="#minIconAPI"  style="cursor: pointer;" title="collapse" onclick="switchDisplayAPIMaxToMin()"> <img src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="APIOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsOpenedTitle"><h3>Agricultural &amp; Pumping Interruptible Program (AP-I)</h3></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
            </div>
			</h:panelGroup>
            <div class="EventsOpenContDiv">
				
				<h:panelGroup layout="block" id="panelAPIExpandNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>
				<h:panelGroup layout="block" id="panelAPIExpandEvents">	
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">	
				<div class="activeTitle">Active Events</div>
				<richext:dataTable 
					value="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}"
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
					<rich:column sortable="false" style="width:160px;">
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
							<f:param name="productName" value="A" />
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
							<f:param name="productName" value="A" />
							<f:param name="eventKey" value="#{item.key}" />
							<f:param name="isActive" value="true" />
						</h:outputLink>						
					</rich:column>
				</richext:dataTable>
				</h:panelGroup>
				</h:panelGroup>
              <div class="clearfix"></div>
              
				<h:panelGroup layout="block" id="panelAPIScheduleNoEvents">
				 
				</h:panelGroup>		
					
					<div id="form:APIOpenedScheduleEventPanel" style="display:">
						<h:panelGroup layout="block" id="panelAPIScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleAPIPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a href="#form:APIScheduleMinToMaxLink"  id="form:APIScheduleMaxToMinLink" name="form:APIScheduleMaxToMinLink"  style="cursor: pointer;" onclick="switchScheduleAPIMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable 
							value="#{drFrame.commercialUIBackingBean.scheduleAPIPREventGroup.scheduledEvents}" 
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
							<rich:column sortable="false" style="width:160px;">
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
									<f:param name="productName" value="A" />
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
									<f:param name="productName" value="A" />
									<f:param name="eventKey" value="#{item.key}" />
									<f:param name="isActive" value="false" />
								</h:outputLink>						
							</rich:column>
						</richext:dataTable>
						</h:panelGroup> 
						</h:panelGroup>
					</div>
					<div id="form:APIClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelAPIScheduleEventsMin">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleAPIPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a  href="#form:APIScheduleMaxToMinLink" id="form:APIScheduleMinToMaxLink" name="form:APIScheduleMinToMaxLink" style="cursor: pointer;" onclick="switchScheduleAPIMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
						</h:panelGroup> 
						</h:panelGroup>
					</div>
				
              
				<h:panelGroup layout="block" id="APIInformationPanel" >
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleAPIPREventGroup.scheduledEventEmptyFlag||!drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">
                            <div class="viewEventAreas">
                                <h:outputText value="#{drTextContext.disclaimerAPICommercial}"/>
                            </div>
                        </h:panelGroup>
				</h:panelGroup> 
				<div class="viewEventAreas"><h:outputText value="#{drTextContext.contactInfoApiCom}"/></div>  
				<div class="clearfix"></div>
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
		  
		  
          <div id="form:APIClosePanelGroup" >
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"   id="minIconAPI" name="minIconAPI" href="#maxIconAPI" style="cursor: pointer;" title="expand" onclick="switchDisplayAPIMinToMax()"> <img src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsClosedTitle"><h3>Agricultural &amp; Pumping Interruptible Program (AP-I)</h3></div>
				
				<h:panelGroup layout="block" id="panelAPICollapseNoEvents">
				<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div>No Events in Progress</div>
					</div>
				</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelAPICollapseEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventEmptyFlag}">
					<div class="NoEventStatus">
					  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeAPIPREventGroup.eventSize}"/> Event(s) in Progress</div>
					</div>		
				</h:panelGroup>
				</h:panelGroup>
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
</h:panelGroup>		  

