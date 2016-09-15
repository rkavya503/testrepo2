<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<script type="text/javascript">
	function _selChange(wholeTable,button){
		var eles =wholeTable.getElementsByClassName('checked');
		if(eles.length>0){
			button.removeAttribute("disabled");
		}else{
			button.setAttribute("disabled",true);
		}
	}
</script>
<h:form id="selected-part-form" onkeypress="var evt=(event||window.event); if(evt.keyCode==13) {#{rich:element('apply-select-filter')}.click();Event.stop(evt);}">
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title"> Enroll Participants </span>
</rich:panel> 

<rich:panel styleClass="content-panel" id ="participant-selection-panel" style="margin-left:10px">
	<richext:set beanName="partSelection" value="#{evtAdvisor.eventModel.participantSelection}"/>
	
	<table width="920px" class="filter-table">
		<tr><td>
			<richext:set beanName="selectedFilter" value="#{partSelection.selectedProvider.filter}"/>
			<h:panelGrid styleClass="form-input filter-form-block" columns="8" id="select-filter-block">
					<!-- first row -->
					<h:outputText value="Filter By" styleClass="form-field"/>	
					<a4j:region>
						<h:selectOneMenu value="#{selectedFilter.filterBy}" styleClass="form-field" style="margin-left:10px">
							<f:selectItems value="#{selectedFilter.filterByList}"/>
							 <a4j:support  event="onchange" reRender="select-filter-block"/>
						</h:selectOneMenu>
					</a4j:region>
					
					<rich:spacer height="20px" width="30px"/>
					
					<h:outputText value="#{selectedFilter.searchByLabel}" styleClass="form-field" rendered="#{!selectedFilter.byShed}"/>
					<h:inputText   value="#{selectedFilter.searchByValue}" style="margin-left:10px" styleClass="form-field" rendered="#{!selectedFilter.byShed}" />
					<h:outputText value="" rendered="#{!selectedFilter.byShed}"/>
					<h:outputText value="" rendered="#{!selectedFilter.byShed}"/>
					<h:outputText value="" rendered="#{!selectedFilter.byShed}"/>
					
					<!-- row for shed -->
					<h:outputText value="Min. Shed" styleClass="form-field" rendered="#{selectedFilter.byShed}"/>	
					<rich:inputNumberSlider  value="#{selectedFilter.searchMinShed}" width="200" rendered="#{selectedFilter.byShed}"
                                             maxValue="#{selectedFilter.maxShed}"    minValue="#{selectedFilter.minShed}"
                                             showArrows="true"  step="#{selectedFilter.step}" showToolTip="true"
                                             immediate="true" id="r-minslider" enableManualInput="false"/>
                    <rich:spacer height="20px" width="50px" rendered="#{selectedFilter.byShed}"/>
                    <h:outputText value="Max. Shed" styleClass="form-field" rendered="#{selectedFilter.byShed}"/>	
					<rich:inputNumberSlider  value="#{selectedFilter.searchMaxShed}" width="200" rendered="#{selectedFilter.byShed}"
                                             maxValue="#{selectedFilter.maxShed}"    minValue="#{selectedFilter.minShed}"
                                             showArrows="true"  step="#{selectedFilter.step}" showToolTip="true"
                                             immediate="true" id="r-maxslider" enableManualInput="false" 
											 />                       
			</h:panelGrid>
		</td></tr>
		<tr><td>
			<table width="100%">
					<tr>
						<td align="left">
							<a4j:commandButton id="apply-select-filter" value="Filter" styleClass="cmd-button"
							  action="#{partSelection.selectedProvider.applyFilter}" title="filter in enrolled participants"
							  limitToList="true"
							  reRender="participantList,clear-select-filter,sel-participant-count,sel-total-register,sel-total-available"/>
							  
							<a4j:commandButton id="clear-select-filter" value="View All Enrolled" styleClass="cmd-button"
							  action="#{partSelection.selectedProvider.clearFilter}" title="View all enroll participants"
							  disabled="#{partSelection.selectedProvider.emptyFilter}"
							  limitToList="true" 
							  reRender="participantList,select-filter-block,clear-select-filter,sel-participant-count,sel-total-register,sel-total-available"/>
							
							<a4j:commandButton value="Remove Selected" id="remove-selected-btn"  style="width:120px" styleClass="cmd-button"
								action="#{partSelection.removeParticipants}" title="Remove selected participants from enrolled list"
								oncomplete="#{rich:element('remove-selected-btn')}.setAttribute('disabled',true);"
								limitToList="true" 
								reRender="participantList,enroll-more-button,sel-participant-count,sel-total-register,sel-total-available"/>
							
							<a4j:commandButton type="button" id="enroll-more-button" value="Enroll More" styleClass="cmd-button"
								onclick="#{rich:component('select-participants-dlg')}.show();"
								title="Add More Participants" disabled="#{partSelection.rejectedProvider.allCount<=0}"
								oncomplete="_selChange($('rejected-part-col'),#{rich:element('enroll-selected-btn')});"
								limitToList="true" reRender="participant-rejection-panel"/>
								
							<a4j:commandButton type="button" id="view-ineligible-button" value="Ineligible Participants" style="width:150px" styleClass="cmd-button"
								onclick="#{rich:component('ineligible-participants-dlg')}.show();"
								title="view all ineligible participants" disabled="#{partSelection.ineligibleProvider.allCount<=0}"
								limitToList="true" reRender="participant-ineligible-panel"/>
						</td>
					</tr>
			</table>
		</td></tr>
		<tr><td align="left">
			<table width="100%">
			<tr>
				<td align="right" width="80%"></td>
				<td align="right" class="shed" nowrap="nowrap"><h:outputText styleClass="form-field" value="Total Register Shed:"/></td>
				<td align="right"  class="shed" nowrap="nowrap">
					<h:outputText  id='sel-total-register' styleClass="form-field" value="#{partSelection.selectedProvider.totalRegisterShed} kw"/></td>
			</tr>
			<tr>
				<td align="left"><div class="table-title">
					<h:outputText value="Enrolled Event Participants"/> 
					<h:outputText id='sel-participant-count' styleClass="important-tip" value="[#{partSelection.selectedProvider.totalRowCount}]"/>
					<h:outputText value=":"/> 
				<span class="table-sub-title">(All eligible program participants are enrolled by default)</span></div>
				</td>
				<td align="right" class="shed" nowrap="nowrap">
					<h:outputText styleClass="form-field" value="Total Available Shed:"/></td>
				<td align="right" class="shed" nowrap="nowrap">
					<h:outputText id="sel-total-available" styleClass="form-field" value="#{partSelection.selectedProvider.totalAvailableShed} kw"/>
				</td>
			</tr>
			<tr><td id="enrolled-table-col" colspan="3">
			<richext:treeTable id="participantList" value="#{partSelection.selectedProvider}" var="item" rows="10000" 
					height="420px" width="100%" selectionMode="multiple"
					onSelection="_selChange($('enrolled-table-col'),#{rich:element('remove-selected-btn')});">
				<richext:treeColumn sortBy="#{participantName}" sortable="true" width="30%">
						<f:facet name="header">
							   <h:outputText value="Participant Name" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.participantName}"></h:outputText>
				</richext:treeColumn>
				<rich:column sortBy="#{account}" sortable="true" width="30%">
						<f:facet name="header">
							   <h:outputText value="Account#" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.account}"></h:outputText>
				</rich:column>
				<rich:column sortBy="#{registerShed}" sortable="true" width="20%">
						<f:facet name="header">
							   <h:outputText value="Register Shed (KW)" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.registerShed}"></h:outputText>
				</rich:column>
				<rich:column sortBy="#{availableShed}" sortable="true" width="20%">
						<f:facet name="header">
							   <h:outputText value="Available Shed (KW)" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.availableShed}"></h:outputText>
				</rich:column>
			</richext:treeTable>
			</td></tr>
			</table>
		</td></tr>
	</table>
</rich:panel>
	<jsp:include page="buttonsOnPartSelPage.jsp"/>
</h:form>

<a4j:region>
	<rich:modalPanel id="select-participants-dlg" keepVisualState="false" height="520" width="770">
		<f:facet name="header">
				<h:outputText styleClass="dlg-title" value="Enroll Participants"/>
		</f:facet>
		<f:facet name="controls">
            <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('select-participants-dlg')"/>
		</f:facet>
		<h:form  id="participant-form" onkeypress="var evt=(event||window.event); if(evt.keyCode==13) {#{rich:element('apply-reject-filter')}.click();Event.stop(evt);}"> 
		<rich:panel styleClass="content-panel" id ="participant-rejection-panel">
		<table width="720px" class="filter-table">
			<tr><td>
				<h:panelGrid styleClass="filter-form-block" columns="8" id="reject-filter-block">
					<richext:set beanName="rejectFilter" value="#{partSelection.rejectedProvider.filter}"/>
					<!-- first row -->
					<h:outputText value="Filter By" styleClass="form-field"/>	
					<a4j:region>
						<h:selectOneMenu value="#{rejectFilter.filterBy}" styleClass="form-field" style="margin-left:10px">
							<f:selectItems value="#{rejectFilter.filterByList}"/>
							 <a4j:support  event="onchange" reRender="reject-filter-block"/>
						</h:selectOneMenu>
					</a4j:region>
					
					<rich:spacer height="20px" width="20px"/>
					
					<h:outputText value="#{rejectFilter.searchByLabel}" styleClass="form-field" rendered="#{!rejectFilter.byShed}"/>
					<h:inputText   value="#{rejectFilter.searchByValue}" style="margin-left:10px" styleClass="form-field" rendered="#{!rejectFilter.byShed}" />
					<h:outputText value="" rendered="#{!rejectFilter.byShed}"/>
					<h:outputText value="" rendered="#{!rejectFilter.byShed}"/>
					<h:outputText value="" rendered="#{!rejectFilter.byShed}"/>
					
					<!-- row for shed -->
					<h:outputText value="Min. Shed" styleClass="form-field" rendered="#{rejectFilter.byShed}"/>	
					<rich:inputNumberSlider  value="#{rejectFilter.searchMinShed}" width="200" rendered="#{rejectFilter.byShed}"
                                             maxValue="#{rejectFilter.maxShed}"    minValue="#{rejectFilter.minShed}"
                                             showArrows="true"  step="#{rejectFilter.step}" showToolTip="true"
                                             immediate="true" id="r-minslider" enableManualInput="false"/>
                    <rich:spacer height="20px" width="20px" rendered="#{rejectFilter.byShed}"/>
                    <h:outputText value="Max. Shed" styleClass="form-field" rendered="#{rejectFilter.byShed}"/>	
					<rich:inputNumberSlider  value="#{rejectFilter.searchMaxShed}" width="200" rendered="#{rejectFilter.byShed}"
                                             maxValue="#{rejectFilter.maxShed}"    minValue="#{rejectFilter.minShed}"
                                             showArrows="true"  step="#{rejectFilter.step}" showToolTip="true"
                                             immediate="true" id="r-maxslider" enableManualInput="false"/>                       
				</h:panelGrid>
			</td></tr>
			<tr><td>
				<table width="100%">
					<tr>
						<td>
							<a4j:commandButton id="apply-reject-filter" value="Filter" style="width:120px"  styleClass="dlg-button"
							  action="#{partSelection.rejectedProvider.applyFilter}"
							  title="Filter participants in rejection list"
							  limitToList="true" reRender="rejectedParticipantList,clear-reject-filter,rej-participant-count,rej-total-register,rej-total-available"/>
							<rich:spacer height="20px" width="50px"/>
							<a4j:commandButton id="clear-reject-filter" value="View All Rejected" style="width:120px"  styleClass="dlg-button"
							  action="#{partSelection.rejectedProvider.clearFilter}"
							  disabled="#{partSelection.rejectedProvider.emptyFilter}"
							  title="View all rejected participants"
							  limitToList="true" 
							  reRender="rejectedParticipantList,reject-filter-block,clear-reject-filter,rej-participant-count,rej-total-register,rej-total-available"/>
							  <rich:spacer height="20px" width="50px"/>
							  <a4j:commandButton value="Enroll Selected" id="enroll-selected-btn"
								title="add checked participants to selection list"
								oncomplete="#{rich:component('select-participants-dlg')}.hide();" styleClass="dlg-button"
								action="#{partSelection.addParticipants}"  style="width:100px"
								limitToList="true"
								reRender="participant-rejection-panel,participantList,enroll-more-button,sel-participant-count,sel-total-register,sel-total-available">
							</a4j:commandButton>
						</td>
					</tr>
				</table>
			</td></tr>
			<tr><td>
				<table width="100%">
					<tr>
						<td align="left" width="70%"></td>
						<td align="right" class="shed" nowrap="nowrap" >
							<h:outputText styleClass="form-field" value="Total Register Shed:"/> </td>
						<td align="right"  class="shed"  nowrap="nowrap">
							<h:outputText id="rej-total-register" styleClass="form-field" 	value="#{partSelection.rejectedProvider.totalRegisterShed} kw"/></td>
					</tr>
					<tr>
						<td align="left"><div class="table-title">
							<h:outputText value="Rejected Event Participants"/>
							<h:outputText id='rej-participant-count' styleClass="important-tip" value="[#{partSelection.rejectedProvider.totalRowCount}]"/>
							<h:outputText value=":"/>
							</div></td>
						<td align="right" class="shed" nowrap="nowrap">
							<h:outputText  styleClass="shed form-field" value="Total Available Shed:"/>
						</td>
						<td align="right"  class="shed" nowrap="nowrap">
							<h:outputText id="rej-total-available" styleClass="shed form-field" value="#{partSelection.rejectedProvider.totalAvailableShed} kw"/>
						</td>
					</tr>
					<tr>
						<td colspan="3" id="rejected-part-col" align="right">
							<richext:treeTable id="rejectedParticipantList" 
								value="#{partSelection.rejectedProvider}" var="item" 
									rows="10000" height="300px" width="100%" selectionMode="multiple"
									onSelection="_selChange($('rejected-part-col'),#{rich:element('enroll-selected-btn')});">
								<richext:treeColumn sortBy="#{participantName}" sortable="true" width="25%">
									<f:facet name="header">
										   <h:outputText value="Participant Name" escape="false"/>
									   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.participantName}"></h:outputText>
								</richext:treeColumn>
								<rich:column sortBy="#{account}" sortable="true" width="25%">
									<f:facet name="header">
									   <h:outputText value="Account#" escape="false"/>
								   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.account}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{registerShed}" sortable="true" width="25%">
										<f:facet name="header">
											   <h:outputText value="Register Shed (KW)" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{item.registerShed}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{availableShed}" sortable="true" width="25%">
										<f:facet name="header">
											   <h:outputText value="Available Shed (KW)" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{item.availableShed}"></h:outputText>
								</rich:column>
							</richext:treeTable>
						</td>
					</tr>
				</table>
			<td></tr>
			<tr>
				<td align="right">
				 <a4j:commandButton id="part_cancel" value="Cancel"  styleClass="dlg-button" style="width:100px"
							  oncomplete="#{rich:component('select-participants-dlg')}.hide();"/>
				
				</td>
			</tr>
		</table>
		</rich:panel>
		</h:form> 
	</rich:modalPanel>
</a4j:region>

<a4j:region>
	<rich:modalPanel id="ineligible-participants-dlg" keepVisualState="false" height="520" width="770">
		<f:facet name="header">
				<h:outputText styleClass="dlg-title" value="Ineligible Participants"/>
		</f:facet>
		 <f:facet name="controls">
            <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('ineligible-participants-dlg')"/>
		</f:facet>
		<h:form  id="ineligible-participant-form"> 
		<rich:panel styleClass="content-panel" id ="participant-ineligible-panel">
		<table width="750px" class="filter-table">
			<tr><td>
				<table width="100%">
					<tr>
						<td align="left" width="80%"></td>
						<td align="right" class="shed" nowrap="nowrap" ></td>
						<td align="right"  class="shed"  nowrap="nowrap"></td>
					</tr>
					<tr>
						<td align="left"><div class="table-title">
							<h:outputText value="Ineligible Participants"/>
							<h:outputText id='ine-participant-count' styleClass="important-tip" value="[#{partSelection.ineligibleProvider.totalRowCount}]"/>
							<h:outputText value=":"/>
							</div ></td>
						<td align="right" class="shed" nowrap="nowrap"></td>
						<td align="right"  class="shed" nowrap="nowrap"></td>
					</tr>
					<tr>
						<td colspan="3" align="right">
							<richext:treeTable id="ineligibleParticipantList" 
								value="#{partSelection.ineligibleProvider}" var="item" 
									rows="10000" height="400px" width="100%">
								<richext:treeColumn sortBy="#{participantName}" sortable="true" width="19%">
									<f:facet name="header">
										   <h:outputText value="Participant Name" escape="false"/>
									   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.participantName}"></h:outputText>
								</richext:treeColumn>
								<rich:column sortBy="#{account}" sortable="true" width="19%">
									<f:facet name="header">
									   <h:outputText value="Account#" escape="false"/>
								   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.account}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{registerShed}" sortable="true" width="19%">
										<f:facet name="header">
											   <h:outputText value="Register Shed (KW)" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{item.registerShed}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{availableShed}" sortable="true" width="19%">
										<f:facet name="header">
											   <h:outputText value="Available Shed (KW)" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{item.availableShed}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{comments}" sortable="true" width="24%">
										<f:facet name="header">
											   <h:outputText value="Comments" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{partSelection.ineligibleProvider.rejectedReason}"></h:outputText>
								</rich:column>
							</richext:treeTable>
						</td>
					</tr>
				</table>
			<td></tr>
			<tr>
				<td align="right">
				 <rich:spacer height="20px" width="30px"/>
				 <a4j:commandButton id="ineligible_part_close" value="Close"  styleClass="dlg-button" style="width:100px"
							  oncomplete="#{rich:component('ineligible-participants-dlg')}.hide();"/>
				</td>
			</tr>
		</table>
		</rich:panel>
		</h:form> 
	</rich:modalPanel>
</a4j:region>