  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>


<h:form>
<rich:panel id="filter-by-block" styleClass="panel-container">
<div style="margin-bottom:10px; margin-top:5px" >	
	<span class="hightlight-txt">
	<h:outputText value="Select  " style="color:black"/>
	<h:outputLink value="javascript:#{rich:component('select-participants-dlg')}.show();"> 
		<h:outputText value="Participant(s)"/>
	</h:outputLink>
	</span>
	
	<h:inputText value="#{baseline.searchCriteria.filterByObjectName}" readonly ="true" 
	style="margin-left:10px;width:300px;font-size:14px" 
	required="true" requiredMessage="pelease select participants">
		<a4j:support event="onchange" reRender="progressPanel" />
	</h:inputText>
</div>

<rich:panel  id="dynamic-filter-block" style="width:700px; border:none">
	<table>
		<tr>
			<td>
				<fieldset>
					<legend><b>Date/Time Range</b></legend>
					<h:panelGrid styleClass="form-input" columns="4" id="date-range-block">
						<h:outputLabel value="Start Date: " styleClass="form-field"/>
						
						<h:outputLabel value="Start Time:" styleClass="form-field" rendered="#{baseline.timeNeeded}"/>
						<h:outputLabel value=""  rendered="#{!baseline.timeNeeded}"/>
						
						<h:outputLabel value="End Date: " rendered="#{baseline.endDateNeeded}" styleClass="form-field"/>
						<h:outputLabel value="" rendered="#{!baseline.endDateNeeded}"/>
						
						<h:outputLabel value="End Time: " styleClass="form-field" rendered="#{baseline.timeNeeded}"/>
						<h:outputLabel value=""  rendered="#{!baseline.timeNeeded}"/>
						
							
						<rich:calendar id="startDateCal"
											label="Start Date"
											value="#{baseline.searchCriteria.startDate}" 
											datePattern="#{baseline.dateFormat}" 
											required="true" requiredMessage="Start Date is required"
											>	
											<a4j:support event="onchanged"  reRender="progressPanel,confirmpanel"/>
						</rich:calendar>
											
						<h:inputText size="5"
										maxlength="5" id="startTime" value="#{baseline.searchCriteria.startTime}" rendered="#{baseline.timeNeeded}"
										required="true" requiredMessage="Start time is required">
							<f:converter converterId="HourMinuteConverter"/>
						</h:inputText>
						<h:outputLabel value=""  rendered="#{!baseline.timeNeeded}"/>
						
						<rich:calendar id="endDateCal"  rendered="#{baseline.endDateNeeded}" 
											label="End Date"
											value="#{baseline.searchCriteria.endDate}"
											datePattern="#{baseline.dateFormat}"
											required="true" requiredMessage="End Date is required"
											>
								<a4j:support event="onchanged"  reRender="progressPanel,confirmpanel"/>
						</rich:calendar>					
						<h:outputLabel value="" rendered="#{!baseline.endDateNeeded}"/>
						
						<h:inputText size="5" maxlength="5" id="endTime" 
							value="#{baseline.searchCriteria.endTime}" rendered="#{baseline.timeNeeded}"
							required="true" requiredMessage="End time is required">
							<f:converter converterId="HourMinuteConverter"/>
						</h:inputText>
						<h:outputLabel value=""  rendered="#{!baseline.timeNeeded}"/>
					</h:panelGrid>
				</fieldset>
			</td>
			<td>
				<rich:panel styleClass="panel-container" rendered="#{baseline.programNeeded}">
					<fieldset>
						<legend><b>Program</b></legend>
						<h:panelGrid styleClass="form-input" columns="1" id="program-block" >
							<h:outputLabel escape="false" value="Program:" styleClass="form-field"/>
							
							<h:selectOneMenu id="programSelection"  value="#{baseline.searchCriteria.program}"  disabled="#{baseline.searchCriteria.searchByEvent}" 
									style="width:150px">
									<f:selectItems value="#{baseline.programOptionItems}"/>
							</h:selectOneMenu>
						</h:panelGrid>
					</fieldset>
				</rich:panel>
			</td>
		</tr>
	</table>
</rich:panel>

<br/>
<h:panelGrid columns="3">
	<a4j:outputPanel id="progressPanel">
            <rich:progressBar value="#{baseline.currentValue}"
                interval="1000" label="#{baseline.currentValue}%"
                enabled="#{baseline.enabled}" minValue="0" maxValue="100"
                reRenderAfterComplete="progressPanel">
                <f:facet name="initial">
                    <h3><a4j:commandButton action="#{baseline.generationAction}"
                        value="Generate baseline" reRender="progressPanel"
                        rendered="#{baseline.buttonRendered}"
						disabled="#{!baseline.scopeChange}"
                        style="margin: 9px 0px 5px;" /></h3>
                </f:facet>
                <f:facet name="complete">
                    <h3><a4j:commandButton action="#{baseline.generationAction}"
                        value="Generate baseline" reRender="progressPanel"
                        rendered="#{baseline.buttonRendered}"
						disabled="#{!baseline.scopeChange}"
                        style="margin: 9px 0px 5px;" /></h3>
                </f:facet>
            </rich:progressBar>
    </a4j:outputPanel>
    <h:panelGrid/>
	<rich:panel style="border-style:none">
		<h3><a4j:commandButton 
			value="Clear Usage Data" 
			style="margin: 9px 0px 5px;" >
	            <a4j:support
	                     event="onclick"
	                     oncomplete="#{rich:component('confirmpanel')}.show()"
	                     ajaxSingle="true"
	                     >
	            </a4j:support>  
			</a4j:commandButton>
			</h3>
	</rich:panel>
</h:panelGrid>
</rich:panel>

<rich:messages layout="table" globalOnly="false" id="msesage-block"
	infoClass="global-message-info" warnClass="global-message-warn"
	errorClass="global-message-error" fatalClass="global-message-fatal" />
</h:form>
	

<rich:modalPanel id="select-participants-dlg" keepVisualState="false" height="510" width="400">
	<f:facet name="header">
            <h:outputText value="Select Participant(s)"/>
	</f:facet>	  
	<h:form  id="participant-form"> 
	<table width="360px">
		<tr>
			<td width="30%"> <h:outputLabel style="font-size:11px" value="Participant Name: " styleClass="form-field"/> </td>
			<td width="40%"> <h:inputText   value="#{baseline.partSelection.participant.participantName}"></h:inputText>
			<td width="30%" align="right"> 
				<h3><a4j:commandButton  value="Search" type="submit" title="Search" styleClass="dlg-button"
				action="#{baseline.partSelection.participant.searchAction}"
				limitToList="true"
				reRender="participantList"/></h3>
			</td>
		</tr>
		<tr>
			<td colspan="3" align="right">
				<richext:treeTable id="participantList" value="#{baseline.partSelection.participant}" var="item" rows="1000" height="400px" width="100%" selectionMode="multiple">
						<richext:treeColumn sortBy="#{participantName}" sortable="true" width="100%">
							<f:facet name="header">
								   <h:outputText value="Participant Name" escape="false"/>
							   </f:facet>				
							<h:outputText/>
							<h:outputText value="#{item.participantName}"></h:outputText>
						</richext:treeColumn>
				</richext:treeTable>
			</td>
		</tr>
		<tr>
			<td  colspan="3" align="right">
				<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton id="part_cancel" value="Cancel"  styleClass="dlg-button"
												oncomplete="#{rich:component('select-participants-dlg')}.hide();"/>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton value="OK" oncomplete="#{rich:component('select-participants-dlg')}.hide();" styleClass="dlg-button"
												action="#{baseline.partSelection.okAction}" limitToList="true" reRender="filter-by-block,dynamic-filter-block">
											</a4j:commandButton>
										</h3>
									</td>
								</tr>
							</table>	
			</td>
		</tr>
	</table>
</h:form>
</rich:modalPanel>

 <rich:modalPanel id="confirmpanel"
                            autosized="false"
                            keepVisualState="false"
                            width="315"
                            height="100">
    <h:form id="deleteForm">
            <rich:panel id="messages"  >
                <h:outputText value="Are you sure to remove the usage data between "/>
                <h:outputText value="#{baseline.searchCriteria.startDate}">
                <f:convertDateTime pattern="#{baseline.dateFormat}"/>  
                </h:outputText>
                <h:outputText value=" and "/>
                <h:outputText value="#{baseline.searchCriteria.endDate}">
                <f:convertDateTime pattern="#{baseline.dateFormat}"/>  
                </h:outputText>
             </rich:panel>
				<table border="0">
								<tr>
									<td>
										<h3>
											 <a4j:commandButton  id="yes" value="Yes" reRender="date-range-block"
																action="#{baseline.clearDirtyData}"
																oncomplete="#{rich:component('confirmpanel')}.hide();">
												   <a4j:support
																action="#{baseline.clearDirtyData}"
																event="onchange"
																oncomplete="#{rich:component('confirmpanel')}.show()"
																ajaxSingle="true"
																>
												   </a4j:support>
											</a4j:commandButton>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton reRender="date-range-block" id="no" value="No" oncomplete="#{rich:component('confirmpanel')}.hide();"/>
										</h3>
									</td>
								</tr>
							</table>
     </h:form>
</rich:modalPanel>
