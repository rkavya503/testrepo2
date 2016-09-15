
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<style>
.rich-dtascroller-table {
	border-color: #BED6F8;
	background-color: white;
	border-width: 0px;
	border-style: solid;
}

.rich-datascr-button, .rich-datascr-ctrls-separator {
	background-color: #FFFFFF;
	border-color: #FFFFFF;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	border-width: 0px;
	border-style: solid;
	width: 25px;
	cursor: pointer;
	text-align: center;
}

.rich-datascr-inact {
	border-top-color: #BED6F8;
	color: #BED6F8;
	font-family: Arial,Verdana,sans-serif;
	font-size: 11px;
	font-color: #009245;
	border-top-width: 0px;
	border-top-style: none; 
	width: 25px;
	cursor: pointer;
	text-align: center;
}

.rich-datascr-act {
	color: #009245;
	font-family: Arial,Verdana,sans-serif;
	font-size: 11px;
	font-color: #009245;
	width: 25px;
	text-align: center;

	border-top-color: #009245;
	border-top-width: 0px;
	border-top-style: none;

	border-bottom-color: #009245;
	border-bottom-width:2px;
	border-bottom-style: solid;
}
</style>
<script type="text/javascript">

function markPages(){
		
		var result = document.getElementById("eventHistoryForm:totalResult1").value;
		var pages = document.getElementById("eventHistoryForm:PageNumber").value;
		var tempstart = (pages-1)*50;
		var start ;
		var temp = pages*50;
		var end;
		
		if(tempstart==0){
		 	start = 1;
		} else {
		 	start = tempstart
		}
		
		if(temp<result){
		 	end = temp;
		} else {
			end = result
		}
		
		document.getElementById("eventHistoryForm:resultIndexFromLabelG2").innerHTML =start;
		document.getElementById("eventHistoryForm:resultIndexToLabelG2").innerHTML =end;
}
</script>
<h:panelGroup layout="block" id="eventHistoryResultsPanelGroup_RTPResults" rendered="#{!drFrame.historyUIBackingBean.historyRTPEventsFlag}">	
	<div class="DivHistrySearch">
		<div class="searchResult-Title"><h:outputText value="#{drFrame.historyUIBackingBean.currentSelectProgram}"/></div>      
		
		<div class="searchResultCtrl">
			<div class="exportCSV2">
				<h:commandLink 	rendered="#{drFrame.historyUIBackingBean.exportCSVDispalyFlag}"
								action="#{drFrame.historyUIBackingBean.exportCSV_EventHistory_Normal}" 
								value="Export CSV" />
				<h:commandButton 	action="#{drFrame.historyUIBackingBean.exportCSV_EventHistory_Normal}"  
									title="Export CSV" alt="Export CSV" value="Export CSV" image="images/icons/leftGreenArrow.png" 
									rendered="#{drFrame.historyUIBackingBean.exportCSVDispalyFlag}"/>	
			</div>
        </div>
		<div class="searchResultList">
			<richext:dataTable id="generalHistoryEventsDataTable" 
							value="#{drFrame.historyUIBackingBean.historyBaseEvents.events}" 
							var="item" 
							rows="50"
							width="100%" 
							styleClass="evtSearchTable" 
							cellpadding="0" 
							cellspacing="0"
							summary="table with columns indicated product, start date, end date, start time and end time for each activity, column block is only for the API and BIP events"
							rowClasses="EvtTableCell,EvtTableCell_nobg">		
						<rich:column sortable="false" styleClass="rich-table-cell-first" style="width:70px;">
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
								<h:outputText  value="#{item.endDateString}">
								</h:outputText>
							</rich:column>
							
							<rich:column sortable="false" rendered="#{!drFrame.historyUIBackingBean.blockColumnDisplayFlag}" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolStartTime}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventStartTimeString}">
								</h:outputText>
							</rich:column>
							
							<rich:column sortable="false" styleClass="rich-table-cell-last" rendered="#{!drFrame.historyUIBackingBean.blockColumnDisplayFlag}" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolEndTime}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventEndTimeString}">
								</h:outputText>
							</rich:column>
							
							<rich:column sortable="false" rendered="#{drFrame.historyUIBackingBean.blockColumnDisplayFlag}" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolStartTime}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventStartTimeString}">
								</h:outputText>
							</rich:column>
							
							<rich:column sortable="false" rendered="#{drFrame.historyUIBackingBean.blockColumnDisplayFlag}" style="width:60px;">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolEndTime}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.eventEndTimeString}">
								</h:outputText>
							</rich:column>
							
							<rich:column sortable="false" styleClass="rich-table-cell-last" rendered="#{drFrame.historyUIBackingBean.blockColumnDisplayFlag}" style="width:120px;">
										<f:facet name="header" >
											<h:outputText value="Block" escape="false" />
										</f:facet>
										<h:outputText  value="#{item.blockString}">
										</h:outputText>
							</rich:column>
			</richext:dataTable>
		</div>
        <br>

		
		<div class="searchResultCtrl">
                <div class="searchResult_navi">
                  <ul>
                    <li style="margin-top:3px">
						<rich:datascroller 	for="generalHistoryEventsDataTable" 
											id="dc22" 
											reRender="resultIndexFromLabelG2,resultIndexToLabelG2,PageNumber" 
											style="width:150px;display:#{drFrame.historyUIBackingBean.displaySearchDataScroller}" 
											page="#{drFrame.historyUIBackingBean.scrollerPage}"
											align="left" 
											maxPages="5"
											boundaryControls="hide"
											fastControls="show"
											rendered="true"
											oncomplete="markPages()"/>
											
					</li>
                    <li class="resultSum"> 
                   
						    <h:inputHidden id="PageNumber" value="#{drFrame.historyUIBackingBean.scrollerPage}" />
						    <h:inputHidden id="totalResult1" value="#{drFrame.historyUIBackingBean.totalResults}" />
						    
							<h:outputText id="resultIndexFromLabelG2" value="#{drFrame.historyUIBackingBean.resultIndexFrom}"/>
							-
							<h:outputText id="resultIndexToLabelG2" value="#{drFrame.historyUIBackingBean.resultIndexTo}"/>
							of
							<h:outputText id="totalResult" value="#{drFrame.historyUIBackingBean.totalResults}"/>
							Results
						
					</li>                           
					</ul>
                </div>
        </div>
        <div class="searchResult_declare clearfix">
			
				<h:outputText value="#{drTextContext.copyrightHistoryRTPSearch} ZIP Codes listed above indicate some customers within the area were impacted by an event. Not all customers in a listed ZIP Code were necessarily impacted. SCE makes this data available as a courtesy only and cannot guarantee complete accuracy in the information provided." escape="false" />
			
		</div>
		
    </div>
 </h:panelGroup>