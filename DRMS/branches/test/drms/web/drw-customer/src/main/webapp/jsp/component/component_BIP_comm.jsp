<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:outputText id="BIPEventEmptyFlagA" style="display:none" value="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}"/>
<h:outputText id="BIPEventEmptyFlagS" style="display:none" value="#{drFrame.commercialUIBackingBean.scheduleBIPPREventGroup.eventEmptyFlag}"/>
          <div id="form:BIPOpenPanelGroup">
          <div class="EventWrapper_OpenPanels" style="clear:both">
            <div class="EventOpenedUpperHeader">
              <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="true" id="maxIconBIP" name="maxIconBIP" href="#minIconBIP"  style="cursor: pointer;" title="collapse" onclick="switchDisplayBIPMaxToMin()"> <img  src="images/icons/collapseIcon.png" alt="collapse" title="collapse" /> </a> </div>
            </div>
			<h:panelGroup id="BIPOpenTitle" layout="block">
            <div class="EventsOpenedHeaderTitleBg">
              <div class="EventsOpenedTitle"><h3>Base Interruptible Program (BIP)</h3></div>
			  <h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>		
				</h:panelGroup>
            </div>
			</h:panelGroup>
            <div class="EventsOpenContDiv">
              <!--1.1 Active Events--> 
				<h:panelGroup layout="block" id="panelBIPExpandNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">	
						<table class="NoEventsDateTable">
							<tr>
							  <td><img src="images/icons/NoEventIcon.png" alt="no events in progress" align="middle" class="NoEvenIcon" /> No Events in Progress</td>
							</tr>
						</table>					
					</h:panelGroup>	
				</h:panelGroup>
				<h:panelGroup layout="block" id="panelBIPExpandEvents">
				<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">	
				<div class="activeTitle">Active Events</div>
				<richext:dataTable id="bipActiveEventsTable"
					value="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.events}" 
					rendered="#{!drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}"
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
							<f:param name="productName" value="B" />
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
							<f:param name="productName" value="B" />
							<f:param name="eventKey" value="#{item.key}" />
							<f:param name="isActive" value="true" />
						</h:outputLink>						
					</rich:column>
				</richext:dataTable>

				</h:panelGroup>
				</h:panelGroup>
              <div class="clearfix"></div>
				
				<h:panelGroup layout="block" id="panelBIPScheduleNoEvents">
					
				</h:panelGroup>	
					
					
					
					<div>
					<div id="form:BIPOpenedScheduleEventPanel" style="display:">
						<h:panelGroup layout="block" id="panelBIPScheduleEventsMax">
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleBIPPREventGroup.scheduledEventEmptyFlag}">
						<div class="viewScheduledEvents" ><a href="#form:BIPScheduleMinToMaxLink"  id="form:BIPScheduleMaxToMinLink" name="form:BIPScheduleMaxToMinLink" style="cursor: pointer;" onclick="switchScheduleBIPMaxToMin()"><img src="images/icons/downArrow.png" align="middle" alt="Hide Scheduled Events" class="rightArrow" /> Hide Scheduled Events</a></div>
						<richext:dataTable id="bipScheduledEventsTable"
							value="#{drFrame.commercialUIBackingBean.scheduleBIPPREventGroup.scheduledEvents}" 
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
									<f:param name="productName" value="B" />
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
									<f:param name="productName" value="B" />
									<f:param name="eventKey" value="#{item.key}" />
									<f:param name="isActive" value="false" />
								</h:outputLink>						
							</rich:column>
						</richext:dataTable>
						</h:panelGroup> 
						</h:panelGroup>
					</div>
					</div>
					
					<div>
					<div id="form:BIPClosedScheduleEventPanel" style="display:none">
						<h:panelGroup layout="block" id="panelBIPScheduleEventsMin">
							<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleBIPPREventGroup.scheduledEventEmptyFlag}">
								<div class="viewScheduledEvents" ><a  href="#form:BIPScheduleMaxToMinLink" id="form:BIPScheduleMinToMaxLink" name="form:BIPScheduleMinToMaxLink" style="cursor: pointer;" onclick="switchScheduleBIPMinToMax()"><img src="images/icons/leftGreenArrow.png" align="middle" alt="View Scheduled Events" class="rightArrow" /> View Scheduled Events</a></div>			
							</h:panelGroup> 
						</h:panelGroup>
					</div>
					</div>
				
				<div class="clearfix"></div>
				<h:panelGroup layout="block" id="BIPInformationPanel" >
						<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.scheduleBIPPREventGroup.scheduledEventEmptyFlag||!drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">
                            <div class="viewEventAreas"  >
                                <h:outputText value="#{drTextContext.disclaimerBIPCommercial}"/>
                            </div>
                        </h:panelGroup>
				</h:panelGroup> 
				
				<div class="viewEventAreas"><h:outputText value="#{drTextContext.contactInfoBipCom}"/></div>  
              <div class="clearfix"></div>
			  
            </div>
            <div class="EventsOpenFooterBg"></div>
          </div>
          </div>
          <div id="form:BIPClosePanelGroup" style="display:none;">
            <div class="EventWrapper_ClosePanels">
              <div class="EventOpenedUpperHeader">
                <div class="EventControlIcon" style="float:right; margin:0px"> <a  aria-expanded="false"   id="minIconBIP" name="minIconBIP" href="#maxIconBIP" style="cursor: pointer;" title="expand" onclick="switchDisplayBIPMinToMax()"> <img  src="images/icons/expandIcon.png" alt="expand" title="expand" /> </a> </div>
              </div>
              <div class="EventsCloseHeaderTitleBg">
                <div class="EventsClosedTitle"><h3>Base Interruptible Program (BIP)</h3></div>
				
				<h:panelGroup layout="block" id="panelBIPCollapseNoEvents">
					<h:panelGroup layout="block" rendered="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">
						<div class="NoEventStatus">
						  <div>No Events in Progress</div>
						</div>
					</h:panelGroup>
				</h:panelGroup>
				
				<h:panelGroup layout="block" id="panelBIPCollapseEvents">
					<h:panelGroup layout="block" rendered="#{!drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventEmptyFlag}">
						<div class="NoEventStatus">
						  <div><h:outputText value="#{drFrame.commercialUIBackingBean.activeBIPPREventGroup.eventSize}"/> Event(s) in Progress</div>
						</div>		
					</h:panelGroup>
				</h:panelGroup>
              </div>
              <div class="EventsCloseFooterBg"></div>
            </div>
          </div>
