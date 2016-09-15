+<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<script type="text/javascript">
document.getElementById('selected-part-form:remove-selected-btn').disabled = true;
	
	function _selChange(wholeTable,button){
		var eles =wholeTable.getElementsByClassName('checked');
		if(eles.length>0){
			button.removeAttribute("disabled");
		}else{
			button.setAttribute("disabled",true);
		}
	}
	
	function selectAll_btn()
	{
		if(document.getElementById('selected-part-form:select-all-btn').disabled == true)
				document.getElementById('selected-part-form:select-all-btn').disabled = false;
		else
			document.getElementById('selected-part-form:select-all-btn').disabled = true;
	}
	
</script>
<h:form id="selected-part-form" onkeypress="var evt=(event||window.event); if(evt.keyCode==13) {#{rich:element('apply-select-filter')}.click();Event.stop(evt);}">
<rich:panel styleClass="wizard-title-container">
	<span class="wizard-title"> Enroll Locations </span>
</rich:panel> 

<rich:panel styleClass="content-panel" id ="participant-selection-panel" style="margin-left:10px">
	<richext:set beanName="groupSelection" value="#{evtAdvisor.eventModel.eventEnrollment}"/>
		
	<table width="920px" class="filter-table">
		<tr><td>
			<table width="100%">
					<tr>
						<td align="right">
							<a4j:commandButton value="Select All" id="select-all-btn"  style="width:120px" styleClass="cmd-button"
								action="#{groupSelection.addAll}" title="Select All" onclick = "selectAll_btn();"
								limitToList="true" 
								reRender="participantList,enroll-more-button,sel-participant-count,sel-total-register,sel-total-available"/>
								
							<a4j:commandButton value="Remove Selected" id="remove-selected-btn"  style="width:120px" styleClass="cmd-button"
								action="#{groupSelection.removeEnrollment}" title="Remove selected"
								oncomplete="#{rich:element('remove-selected-btn')}.setAttribute('disabled',true);" onclick = "selectAll_btn();"
								limitToList="true"
								reRender="participantList,enroll-more-button,sel-participant-count,sel-total-register,sel-total-available,event-creation-wizard"/>
							
							<a4j:commandButton type="button" id="enroll-more-button" value="Enroll More" styleClass="cmd-button"
								action="#{groupSelection.removeEnrollmentAll}"
								title="Add More"  
								oncomplete="_selChange($('rejected-part-col'),#{rich:element('enroll-selected-btn')},#{rich:component('select-participants-dlg')}.show());"
								limitToList="true" reRender="participant-rejection-panel"/>
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
					<h:outputText  id='sel-total-register' styleClass="form-field" value="#{groupSelection.selectedProvider.totalRegisterShed} kw"/></td>
			</tr>
			<tr>
				<td align="left"><div class="table-title">
					<h:outputText value="Enrolled Locations"/> 
					<h:outputText id='sel-participant-count' styleClass="important-tip" value="[#{groupSelection.selectedProvider.totalRowCount}]"/>
					<h:outputText value=":"/> 
				<span class="table-sub-title">( All Abanks are NOT enrolled by default.)</span></div>
				</td>
				<td align="right" class="shed" nowrap="nowrap">
					<h:outputText styleClass="form-field" value="Total Available Shed:"/></td>
				<td align="right" class="shed" nowrap="nowrap">
					<h:outputText id="sel-total-available" styleClass="form-field" value="#{groupSelection.selectedProvider.totalAvailableShed} kw"/>
				</td>
			</tr>
			<tr><td id="enrolled-table-col" colspan="3">
			<richext:treeTable id="participantList" value="#{groupSelection.selectedProvider}" var="item" rows="10000" 
					height="420px" width="100%" selectionMode="multiple"
					onSelection="_selChange($('enrolled-table-col'),#{rich:element('remove-selected-btn')});">
				<richext:treeColumn sortBy="#{groupName}" sortable="true" width="20%">
						<f:facet name="header">
							   <h:outputText value="Location" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.groupName}"></h:outputText>
				</richext:treeColumn>
				<rich:column sortBy="#{groupId}" sortable="true" width="20%">
						<f:facet name="header">
							   <h:outputText value="Location Number" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.groupId}"></h:outputText>
				</rich:column>
        <rich:column sortBy="#{type}" sortable="true" width="15%">
						<f:facet name="header">
							   <h:outputText value="Type" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.type}"></h:outputText>
				</rich:column>
        <rich:column sortBy="#{participantCount}" sortable="true" width="10%">
						<f:facet name="header">
							   <h:outputText value="Participants" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.participantCount}"></h:outputText>
				</rich:column>
				<rich:column sortBy="#{registerShed}" sortable="true" width="15%">
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
	<jsp:include page="../buttonsOnPartSelPage.jsp"/>
</h:form>

<a4j:region>
	<rich:modalPanel id="select-participants-dlg" keepVisualState="false" height="520" width="790">
		<f:facet name="header">
				<h:outputText styleClass="dlg-title" value="Enroll Locations"/>
		</f:facet>
		<f:facet name="controls">
            <h:graphicImage value="/images/close.jpg" style="cursor:pointer" onclick="Richfaces.hideModalPanel('select-participants-dlg')"/>
		</f:facet>
		<h:form  id="participant-form" onkeypress="var evt=(event||window.event); if(evt.keyCode==13) {#{rich:element('apply-reject-filter')}.click();Event.stop(evt);}"> 
		<rich:panel styleClass="content-panel" id ="participant-rejection-panel">
		<table width="780px" class="filter-table">
			<tr><td>
				<table width="100%">
					<tr>
						<td align="left" width="70%"></td>
						<td align="right" class="shed" nowrap="nowrap" >
							<h:outputText styleClass="form-field" value="Total Register Shed:"/> </td>
						<td align="right"  class="shed"  nowrap="nowrap">
							<h:outputText id="rej-total-register" styleClass="form-field" 	value="#{groupSelection.rejectedProvider.totalRegisterShed} kw"/></td>
					</tr>
					<tr>
						<td align="left"><div class="table-title">
							<h:outputText value="Rejected Locations"/>
							<h:outputText id='rej-participant-count' styleClass="important-tip" value="[#{groupSelection.rejectedProvider.totalRowCount}]"/>
							<h:outputText value=":"/>
							</div></td>
						<td align="right" class="shed" nowrap="nowrap">
							<h:outputText  styleClass="shed form-field" value="Total Available Shed:"/>
						</td>
						<td align="right"  class="shed" nowrap="nowrap">
							<h:outputText id="rej-total-available" styleClass="shed form-field" value="#{groupSelection.rejectedProvider.totalAvailableShed} kw"/>
						</td>
					</tr>
					<tr>
						<td colspan="3" id="rejected-part-col" align="right">
							<richext:treeTable id="rejectedParticipantList" 
								value="#{groupSelection.rejectedProvider}" var="item" 
									rows="10000" height="300px" width="100%" selectionMode="multiple"
									onSelection="_selChange($('rejected-part-col'),#{rich:element('enroll-selected-btn')});">
								<richext:treeColumn sortBy="#{groupName}" sortable="true" width="20%">
									<f:facet name="header">
										   <h:outputText value="Location" escape="false"/>
									   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.groupName}"></h:outputText>
								</richext:treeColumn>
								<rich:column sortBy="#{groupId}" sortable="true" width="18%">
									<f:facet name="header">
									   <h:outputText value="Location Number" escape="false"/>
								   </f:facet>				
									<h:outputText/>
									<h:outputText value="#{item.groupId}"></h:outputText>
								</rich:column>
                   <rich:column sortBy="#{type}" sortable="true" width="15%">
                    <f:facet name="header">
                        <h:outputText value="Type" escape="false"/>
                    </f:facet>				
                  <h:outputText value="#{item.type}"></h:outputText>
                </rich:column>
                <rich:column sortBy="#{participantCount}" sortable="true" width="9%">
                    <f:facet name="header">
                         <h:outputText value="Participants" escape="false"/>
                       </f:facet>				
                    <h:outputText value="#{item.participantCount}"></h:outputText>
                </rich:column>
								<rich:column sortBy="#{registerShed}" sortable="true" width="15%">
										<f:facet name="header">
											   <h:outputText value="Register Shed (KW)" escape="false"/>
										   </f:facet>				
										<h:outputText/>
										<h:outputText value="#{item.registerShed}"></h:outputText>
								</rich:column>
								<rich:column sortBy="#{availableShed}" sortable="true" width="23%">
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
        	<rich:spacer height="20px" width="50px"/>
							  <a4j:commandButton value="Enroll Selected" id="enroll-selected-btn"
                  title="add checked locations to selection list"
                  oncomplete="#{rich:component('select-participants-dlg')}.hide();" styleClass="dlg-button"
                  action="#{groupSelection.addEnrollment}"  style="width:100px"
                  limitToList="true"
                  reRender="participant-rejection-panel,participantList,enroll-more-button,sel-participant-count,sel-total-register,sel-total-available">
          </a4j:commandButton>
              
				 <a4j:commandButton id="part_cancel" value="Cancel"  styleClass="dlg-button" style="width:100px"
							  oncomplete="#{rich:component('select-participants-dlg')}.hide();"/>
				
				</td>
			</tr>
		</table>
		</rich:panel>
		</h:form> 
	</rich:modalPanel>
</a4j:region>