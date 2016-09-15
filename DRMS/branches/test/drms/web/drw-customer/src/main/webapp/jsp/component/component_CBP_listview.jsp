<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGroup id="CBPRefreshPanel" layout="block">
<h:panelGroup  layout="block"  id="CBPPanel">	
		<div class="planCBPHeaderBg"><h3>Capacity Bidding Program (CBP)</h3></div>
	<h:panelGroup  layout="block"  id="innerCBPPanel">
		<h:panelGroup  layout="block"  rendered="#{drListView.groupCBP.eventEmptyFlag}">	
		<div class="dividerGrayLine"></div>
			<table class="resultTable2">
			  <tr>
				<td><img src="images/icons/NoEventIcon.png" align="middle" class="inEvenIcon" alt="No Events in Progress"/> No Events in Progress</td>
			  </tr>
			</table>	
		<div class="dividerGrayLine"></div>	
		</h:panelGroup>

		<h:panelGroup  layout="block"  rendered="#{!drListView.groupCBP.eventEmptyFlag}" id="cbpActiveEventsPanel">	
			<h:panelGroup  layout="block"  id="cbpSearchNoEventsPanel" style="display:none">	
			<div class="dividerGrayLine"></div>
				<table class="resultTable2">
				  <tr>
					<td><img src="images/icons/NoEventIcon.png" align="middle" class="inEvenIcon" alt="No Events in Progress"/> The search found no events in the area you requested.</td>
				  </tr>
				</table>	
			<div class="dividerGrayLine"></div>	
			</h:panelGroup>
			<h:panelGroup id="cbpActiveEventsDisplayPanel"  layout="block">	
				<rich:dataList var="event" value="#{drListView.groupCBP.events}" rows="100" style="list-style-type:none;">	
					<div class="dividerGrayLine2013"></div>
						
						<richext:dataTable value="#{event}" var="item" 
							style="margin: 10px 0px 20px 0px;" 
							width="100%" 
							rows="50"
							styleClass="EvtListTable" 
							cellpadding="0" 
							cellspacing="0"
							summary="table with six columns indicated product, start date, end date, start time, end time and block for each activity"
							rowClasses="EvtTableCell,EvtTableCell_nobg">
							<rich:column sortable="false" styleClass="rich-table-cell-first" style="width:60px;">
								<f:facet name="header" >
									<h:outputText value="Product" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.event.product}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" style="width:60px;">
								<f:facet name="header" >
									<h:outputText value="Start Date" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.startDateString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="End Date" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndDateString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="Start Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventStartTimeString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="End Time" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.tbdEndTimeString}">
								</h:outputText>
							</rich:column>
							<rich:column sortable="false" styleClass="rich-table-cell-last" style="width:120px;">
								<f:facet name="header">
									<h:outputText value="Blocks" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.blockString}">
								</h:outputText>
							</rich:column>		
						</richext:dataTable >
					<div class="dividerGrayLine2013"></div>
					
					<div style="width:504px;margin:0 0 0 5px;padding:0;">
					  <div class="countyWrapper" style="float:left;width:135px;padding:0 20px 0 13px;margin:0;word-wrap:break-word;">
						<div style="font-family:Arial, Helvetica, sans-serif;font-size:13px;font-weight:normal;color:#417300;">County</div>
						
						<p sytle="font-family:Arial, Helvetica, sans-serif;font-size:11px;color:#000000;line-height:18px;padding-bottom:15px;">
							<h:outputText value="#{event.countyString}" >
						</h:outputText>
						</p>
					  </div>
					  <div class="cityWrapper" style="float:left;width:135px;padding:0 20px 0 13px;margin:0;word-wrap:break-word;">
						
						<div style="font-family:Arial, Helvetica, sans-serif;font-size:13px;font-weight:normal;color:#417300;">City</div>
						<p sytle="font-family:Arial, Helvetica, sans-serif;font-size:11px;color:#000000;line-height:18px;padding-bottom:15px;">
							<h:outputText value="#{event.cityString}" >
							</h:outputText>
						</p>
					  </div>
					  <div class="zipWrapper" style="float:left;width:135px;padding:0 20px 0 13px;margin:0;word-wrap:break-word;">
						
						<div style="font-family:Arial, Helvetica, sans-serif;font-size:13px;font-weight:normal;color:#417300;">ZIP Code</div>
						<p sytle="font-family:Arial, Helvetica, sans-serif;font-size:11px;color:#000000;line-height:18px;padding-bottom:15px;">
							<h:outputText value="#{event.zipCodeString}" >
							</h:outputText>
						</p>
					  </div>
					</div>
					
					
					<div class="clearfix"></div>				
				</rich:dataList>
			
				<div class="clearfix"></div> 
				
			</h:panelGroup>
			
			<h:panelGroup layout="block" rendered="#{!drListView.groupCBP.eventEmptyFlag}" >
                <div class="viewEventAreas" style="color: #666;margin-left:10px;" >
                    <h:outputText value="This web site displays CBP events activated by SCE. Check with your CBP Aggregator for details of your participation in CBP events."/>
                </div>
            </h:panelGroup>
			
			<br/>
			<br/>
			
		</h:panelGroup>
	</h:panelGroup>	
</h:panelGroup>    
</h:panelGroup>    		
