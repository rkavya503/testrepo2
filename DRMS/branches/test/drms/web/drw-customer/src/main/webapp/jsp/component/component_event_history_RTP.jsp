
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
	border-top-width: 2px;
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
	border-top-style: solid;

	border-bottom-color: #009245;
	border-bottom-width:2px;
	border-bottom-style: solid;
}
</style>
<script type="text/javascript">

function markPages(){
		
		var result = document.getElementById("eventHistoryForm:totalResult12").value;
		var pages = document.getElementById("eventHistoryForm:PageNumber1").value;
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
		
		document.getElementById("eventHistoryForm:resultIndexFromLabel2").innerHTML =start;
		document.getElementById("eventHistoryForm:resultIndexToLabel2").innerHTML =end;
}
</script>
<h:panelGroup layout="block" id="eventHistoryResultsPanelGroup_GenerateResults" rendered="#{drFrame.historyUIBackingBean.historyRTPEventsFlag}">	
	<div class="DivHistrySearch">
		<div class="searchResult-Title"><h:outputText value="#{drFrame.historyUIBackingBean.currentSelectProgram}"/></div>
        
		<div class="searchResultCtrl">
			<div class="exportCSV2">
				<h:commandLink action="#{drFrame.historyUIBackingBean.exportCSV_EventHistory_RTP}" title="Export CSV" value="Export CSV" rendered="#{drFrame.historyUIBackingBean.exportCSVDispalyFlag}"/>
				<h:commandButton action="#{drFrame.historyUIBackingBean.exportCSV_EventHistory_RTP}"  title="Export CSV" alt="Export CSV" value="Export CSV" image="images/icons/leftGreenArrow.png" rendered="#{drFrame.historyUIBackingBean.exportCSVDispalyFlag}"/>	
			</div>
        </div>        
		
		<div class="searchResultList">
			<richext:dataTable id="rtpHistoryEventsDataTable"
							value="#{drFrame.historyUIBackingBean.historyRTPEvents.rtpList}" 
							var="item" 
							rows="50"
							width="100%" 
							styleClass="evtSearchTable" 
							cellpadding="0" 
							cellspacing="0"
							summary="table with two columns indicated date and price of category"
							rowClasses="EvtTableCell,EvtTableCell_nobg">		
						<rich:column sortable="false" styleClass="rich-table-cell-first">
								<f:facet name="header" >
									<h:outputText value="#{drTextContext.tblcolDateOfUsage}" escape="false" />
								</f:facet>
								<h:outputText  value="#{item.dateString}">
								</h:outputText>
						</rich:column> 
						<rich:column sortable="false" styleClass="rich-table-cell-last">
								<f:facet name="header">
									<h:outputText value="#{drTextContext.tblcolPricingCategoryHistory}" escape="false"  />
								</f:facet>
								<h:outputText  value="#{item.pricingCategoryString}">
								</h:outputText>
						</rich:column>
			</richext:dataTable>
		</div>
        <br>
		<div class="searchResultCtrl">
                <div class="searchResult_navi">
                  <ul>
                    <li style="margin-top:3px">
						<rich:datascroller 	for="rtpHistoryEventsDataTable" 
											id="dc2" 
											reRender="resultIndexFromLabel2,resultIndexToLabel2,PageNumber1" 
											style="width:150px;display:#{drFrame.historyUIBackingBean.displaySearchDataScroller}" 
											page="#{drFrame.historyUIBackingBean.scrollerPage}"
											align="left" 
											maxPages="5"
											boundaryControls="hide"
											fastControls="show"
											rendered="true"
											oncomplete="markPages()"
											/>
					</li>
                    <li class="resultSum"> 
                            <h:inputHidden id="PageNumber1" value="#{drFrame.historyUIBackingBean.scrollerPage}" />
						    <h:inputHidden id="totalResult12" value="#{drFrame.historyUIBackingBean.totalResults}" />
						
							<h:outputText id="resultIndexFromLabel2" value="#{drFrame.historyUIBackingBean.resultIndexFrom}"/>
							-
							<h:outputText id="resultIndexToLabel2" value="#{drFrame.historyUIBackingBean.resultIndexTo}"/>
							of
							<h:outputText value="#{drFrame.historyUIBackingBean.totalResults}"/>
							Results
						
					</li>                            
					</ul>
                </div>
        </div>		
        <div class="searchResult_declare clearfix">
			<h:outputText value="#{drTextContext.copyrightHistoryRTPSearch}" escape="false" />
		</div>
		
    </div>
 </h:panelGroup>