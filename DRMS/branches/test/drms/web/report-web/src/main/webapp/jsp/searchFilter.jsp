<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<h:panelGroup id="searchFilterForm-block">
<h:form id="searchFilterForm">


	<a4j:region>
		<h:panelGroup id="message-block">
			<h:messages 	
							layout="table" globalOnly="false" id="message" showDetail="true" showSummary="false"
							infoClass="global-message-info" warnClass="global-message-warn"
							errorClass="global-message-error" fatalClass="global-message-fatal" />	
		</h:panelGroup>		
		
		<rich:panel id="filter-by-block"  styleClass="panel-container">
			<rich:panel rendered="#{report.aggParticipation}"  id="agg-root-block" style="width:700px; border:none" styleClass="">
				<span class="hightlight-txt">
					<h:outputText  value="Aggregation Level:"/>
					<rich:spacer width="5px" />
					<h:outputText id="aggName" value="#{report.aggTree.aggDisplay} " style="font-weight:bold"/>
					
					<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton style="margin-left:10px; margin-top:3px"
												oncomplete="#{rich:component('aggModelPanel')}.show()"
												reRender="aggModelPanel,program_panel"
												value=" Change Aggregation Level ">
												<a4j:actionparam name="currParticipant"
													value="#{report.context.parentParticipant}"
													assignTo="#{report.aggTree.currParticipant}"
													/>
											</a4j:commandButton>
										</h3>
									</td>
								</tr>
							</table>
				</span>
			</rich:panel>
			<rich:panel id="dynamic-filter-block" style="width:700px; border:none" styleClass="mod-header" >
				<a tabindex="0" onclick="Element.toggleClassName(this.parentNode,'collapsed');" onkeydown="var evt = event || window.event; if(evt.keyCode==13) this.onclick();">
					<span id="dynamic-filter-title"  class="toggle-title" title="Collapse"
					onclick="if(this.getAttribute('title')=='Collapse') this.setAttribute('title','Expand'); else this.setAttribute('title','Collapse');"
					onkeydown="var evt = event || window.event; if(evt.keyCode==13) this.onclick();"
					>
						Filter 
						
					</span>
				</a>
				
				<table id="dynamic-filter-body" class="toggle-body">
					<tr>
						<td>
							<h:panelGrid styleClass="form-input" columns="1" >
								<h:panelGroup>
									<h:outputLabel  for="startDateCalInputDate" value="Start Date: " styleClass="form-field"/>
									<br>
									<rich:calendar id="startDateCal"
													label="Start Date"
													value="#{report.searchCriteria.startDate}" 
													datePattern="#{report.dateFormat}" 
													enableManualInput="true"
													required="true" requiredMessage="Start Date is required"
													/>
								</h:panelGroup>
							</h:panelGrid>
						</td>
						<td>
							<h:panelGrid styleClass="form-input" columns="1" >
								<h:panelGroup>
									<h:outputLabel  for="startTime" value="Start Time:" styleClass="form-field" rendered="#{report.timeNeeded}"/>
									<h:outputText  value=""  rendered="#{!report.timeNeeded}"/>
									<br>
									<h:inputText size="5"
										maxlength="5" id="startTime" value="#{report.searchCriteria.startTime}" rendered="#{report.timeNeeded}"
										required="true" requiredMessage="Start time is required">
										<f:converter converterId="HourMinuteConverter"/>
									</h:inputText>
									<h:outputText value=""  rendered="#{!report.timeNeeded}"/>
								</h:panelGroup>
							</h:panelGrid>
						</td>
						<td>
							<h:panelGrid styleClass="form-input" columns="1" >
								<h:panelGroup>
									<h:outputLabel for="endDateCalInputDate" value="End Date: " rendered="#{report.endDateNeeded}" styleClass="form-field"/>
									<h:outputText value="" rendered="#{!report.endDateNeeded}"/>
									<br>
									<rich:calendar id="endDateCal"  rendered="#{report.endDateNeeded}"
													label="End Date"
													value="#{report.searchCriteria.endDate}"
													datePattern="#{report.dateFormat}"
													enableManualInput="true"
													required="true" requiredMessage="End Date is required"
													/>
									<h:outputText value="" rendered="#{!report.endDateNeeded}"/>
								</h:panelGroup>
							</h:panelGrid>
						</td>
						<td>
							<h:panelGrid styleClass="form-input" columns="1" >
								<h:panelGroup>
									<h:outputLabel for="endTime" value="End Time: " styleClass="form-field" rendered="#{report.timeNeeded}"/>
									<h:outputText value=""  rendered="#{!report.timeNeeded}"/>
									<br>
									<h:inputText size="5" maxlength="5" id="endTime" 
										value="#{report.searchCriteria.endTime}" rendered="#{report.timeNeeded}"
										required="true" requiredMessage="End time is required">
										<f:converter converterId="HourMinuteConverter"/>
									</h:inputText>
									<h:outputText value=""  rendered="#{!report.timeNeeded}"/>
								</h:panelGroup>
							</h:panelGrid>
						</td>
						<td>
							<rich:panel styleClass="panel-container" rendered="#{report.programNeeded}">
								<h:panelGrid styleClass="form-input" columns="1" id="program-block" >
									<h:panelGroup>
										<h:outputLabel for="programSelection" escape="false" value="Program:" styleClass="form-field"/>
										<br>
										<h:selectOneMenu id="programSelection"  value="#{report.searchCriteria.program}" style="width:150px">
											<f:selectItems value="#{report.programOptionItems}"/>
										</h:selectOneMenu>
									</h:panelGroup>
								</h:panelGrid>
							</rich:panel>
						</td>			
					</tr>
				</table>
			</rich:panel>
		</rich:panel>
	</a4j:region>
	<br/>
	<div style="width:700px; border-top:1px solid #BBBBBB">
		
		
		<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton  
												type="submit" style="width:60px; margin-top:10px;margin-left: 640px; padding:0 8px" styleClass="lighting-button"
												value="Search"  title="Search"
												action="#{report.searchAction}"
												status="waitStatus"
												reRender="searchFilterForm-block,event-events-block,event-participation-block,client-status-block,usage-block"/>
										</h3>
									</td>
								</tr>
							</table>	
	</div>
</h:form>
</h:panelGroup>	


<rich:modalPanel id="aggModelPanel" keepVisualState="false" height="550" width="366">
	<f:facet name="header">
		<h:outputText value="Aggregated Participants" />
	</f:facet>

	<h:form id="aggForm">
		<rich:panel style="width:340px;height:420px;overflow:auto;display:block">
			<f:facet name="header">
				<h:outputText value="Select a Program Participant" />
			</f:facet>
			<rich:tree 	id="aggTreeView" value="#{report.aggTree.multiTreeNode}"
						var="aggNode" ajaxSubmitSelection="true"
						reRender="selectedParticipant,switch"
						nodeSelectListener="#{report.aggTree.processSelection}">

				<rich:treeNode acceptedTypes="#{aggNode}">
					<h:outputText value="#{aggNode}" />
				</rich:treeNode>
			</rich:tree>
		</rich:panel>
		<rich:spacer height="10px" />
		<h:panelGrid columns="2">
			<a4j:commandButton 	id="cancel" value="Cancel" oncomplete="#{rich:component('aggModelPanel')}.hide();" />
			<a4j:commandButton 	id="switch" value="Switch" title="switch aggregator"
								action="#{report.switchParticipantAction}" reRender="aggName,programSelection"
								disabled="#{'' ==report.aggTree.selectedParticipantName}"
								oncomplete="#{rich:component('aggModelPanel')}.hide();">
			</a4j:commandButton>
		</h:panelGrid>
		<rich:spacer height="10px" />
		<h:outputText value="Switch To:" />
		<rich:spacer width="5px" />
		<h:outputText escape="false" value="#{report.aggTree.aggDisplay}" style="font-weight:bold" id="selectedParticipant" />
	</h:form>
</rich:modalPanel>
	


