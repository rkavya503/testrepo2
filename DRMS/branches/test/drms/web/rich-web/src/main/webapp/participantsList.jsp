<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:c="http://java.sun.com/jsp/jstl/core"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">

	<head>
<script>
            function resetClearElements(){
                document.forms[1].elements[1].value = "";
            }
        </script>
        
        <style>
		.rich-mpnl-shadow {background-color: #f0f0f0; border-color: #ffffff; border-width: 0px}  
		.center {
			text-align: center;
			font-size:12px;
		}
</style>
		
	</head>
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

	<body onload="resetClearElements()">
		<h:form
			onkeydown="if((event||window.event).keyCode==13) {#{rich:element('FilterButton')}.click(); Event.stop(event || window.event);}">
			<!--<a4j:keepAlive beanName="participantDataModel" />-->
			<rich:messages layout="table" globalOnly="false"
				infoClass="global-message-info" warnClass="global-message-warn"
				errorClass="global-message-error" fatalClass="global-message-fatal" />

			<rich:dataTable width="100%">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Participants" />
							</rich:column>

							<rich:column colspan="1" width="200px">
								<h:outputLink
									value="/pss2.website/commDevDetail.do?dispatch=create"
									rendered="#{participantDataModel.canCreateParticipant}">
									<h:graphicImage value="/images/layout/add_device.gif" />
								</h:outputLink>

								<a4j:commandLink oncomplete="#{rich:component('panel')}.show()"
									reRender="panel" value=""
									rendered="#{participantDataModel.canDeleteParticipant}">
									<h:graphicImage value="/images/layout/delete_device.gif" />
								</a4j:commandLink>
								<h:outputLink value=""
									rendered="#{participantDataModel.canCreateParticipant and (participantDataModel.sce)}">
									<a href="/pss2.website/commDevDetail.do?dispatch=viewarq"
										target="_blank"><h:graphicImage
											value="/images/layout/user.png" style="float:right"
											alt="Approve Participants" title="Approve Participants" /></a>
								</h:outputLink>
							</rich:column>
						</rich:columnGroup>

					</f:facet>
				</f:facet>
			</rich:dataTable>


			<rich:dataTable width="100%">
				<f:facet name="header4">
					<f:facet name="caption">
						<rich:columnGroup>
							<rich:column id="queryRegion">
								<a4j:region>

									<table width="100%"
										style="font-family: Arial, Verdana, sans-serif; font-size: 12px; font-weight: bold;">
										<tr>
											<td align="right" width="8%"><h:outputText
													value="Search : " /></td>
											<td align="left" width="15%"><h:inputText
													id="FilterCondition"
													value="#{participantDataModel.searchCondition.value}"
													style="border: 1px solid;">

												</h:inputText></td>
											<td align="right" width="8%"><h:outputText
													value="Filter By : " /></td>
											<td align="left" width="7%"><a4j:region>
													<h:selectOneMenu id="filterOptions"
														style="padding-left:5px;border: 1px solid;"
														value="#{participantDataModel.searchCondition.param}"
														valueChangeListener="#{participantDataModel.filterTypeChanged}">
														<f:selectItems
															value="#{participantDataModel.filterOptions}" />
													</h:selectOneMenu>
												</a4j:region></td>
											<td align="left" width="6%"><a4j:commandButton
													disabled="#{participantDataModel.canSearchPaticipants}"
													oncomplete="resetClearElements()" id="FilterButton"
													value="   Search   " title="Search Participant"
													reRender="participants, searchHistoryURL"
													action="#{participantDataModel.filterAction}"
													style="border: 1px solid #2BB0FF;"></a4j:commandButton></td>
											<td align="right" width="2%"><h:selectBooleanCheckbox
													value="#{participantDataModel.searchInResult}"></h:selectBooleanCheckbox>
											</td>
											<td align="left" width="10%"><h:outputText
													value="Search in Result " /></td>
											<!--   <td align="left"><h:outputLink value="/pss2.website/participantsMap.do"><f:verbatim> Clear </f:verbatim></h:outputLink></td>-->
											<td align="left"><h:commandButton
													value="     Clear     "
													title="Clear Query Conditions and Results"
													action="#{participantDataModel.clearAction}"
													style="border: 1px solid #2BB0FF;"
													disabled="#{participantDataModel.canClearParticipants}" />



											</td>
											<td align="left"><h:commandButton
													disabled="#{participantDataModel.canExportParticipants}"
													value="     Export    " title="Export Result"
													action="#{participantDataModel.exportHtmlTableToExcel}"
													style="border: 1px solid #2BB0FF;" /></td>
											<td align="left" width="20%">
												<!--
                                                <a4j:commandButton
													value="     Import    " title="Import Participants"
													style="border: 1px solid #2BB0FF;" 
													action="#{participantImport.fileUploadStartAction}"
													oncomplete="#{rich:component('importModalPanel')}.show()"
													reRender="importModalPanel"/>
                                                -->
											</td>

										</tr>
									</table>
								</a4j:region>
								<br />

								<table>
									<tr>
										<td align="center" valign="middle"
											style="font-family: Arial, Verdana, sans-serif; font-size: 12px; font-weight: bold;">
											<h:outputText value="Search By : " /> <a4j:commandLink
												action="#{participantDataModel.searchAllAction}" value="All"
												reRender="participants,searchHistoryURL">
												<f:param name="all" value="all" />
											</a4j:commandLink>
										</td>

										<td
											style="font-family: Arial, Verdana, sans-serif; font-size: 12px; font-weight: bold;">
											<h:panelGrid id="searchHistoryURL">
												<a4j:repeat
													value="#{participantDataModel.searchHistory.indexedConditions}"
													var="item">
												>>
												<a4j:commandLink
														action="#{participantDataModel.historySearchAction}"
														value="#{item.label}"
														reRender="participants,searchHistoryURL"
														style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold; color:#{participantDataModel.searchHistory.maxWorkingIndex &gt;= item.sequence? '#27577D':'#B4C8E6'}">
														<f:param name="linkSequence" value="#{item.sequence}" />
													</a4j:commandLink>
												</a4j:repeat>
											</h:panelGrid>
										</td>
									</tr>
								</table>

							</rich:column>
						</rich:columnGroup>

					</f:facet>
				</f:facet>
			</rich:dataTable>

			<richext:dataTable id="participants" value="#{participantDataModel}"
				var="item" rows="20" width="100%">
				<!--<a4j:keepAlive beanName="participantDataModel" />-->
				<rich:column width="50px">
					<f:facet name="header">
						<a4j:region>
							<h:outputText value="Delete" escape="false" />
							<br />
						</a4j:region>
					</f:facet>
					<h:selectBooleanCheckbox
						disabled="#{(participantDataModel.canDeleteChecked) || (item.disabled)}"
						value="#{item.selected}" />
					<h:outputText rendered="false" value="Delete"></h:outputText>
				</rich:column>

				<rich:column sortBy="#{name}" width="100px" sortable="true">
					<f:facet name="header">

						<h:outputText value="Name" escape="false" />

					</f:facet>
					<h:outputText value="#{item.name}"></h:outputText>
				</rich:column>

				<!-- Bcd rep names -->

				<rich:column sortBy="#{bcdRepName}" width="100px" sortable="true"
					rendered="#{participantDataModel.participantInfoEnable}">
					<f:facet name="header">

						<h:outputText value="BCD Rep Name" escape="false" />

					</f:facet>
					<h:outputText value="#{item.bcdRepName}"></h:outputText>
				</rich:column>
				<!--  -->

				<rich:column width="100px" sortBy="#{accountNumber}" sortable="true">
					<f:facet name="header">
						<h:outputText value="Account #" escape="false" />
					</f:facet>
					<h:outputText value="#{item.accountNumber}"></h:outputText>
				</rich:column>
				<!-- AutoDR Profile date -->
				<rich:column sortBy="#{autoDrProfileStartDate}" width="100px"
					sortable="true"
					rendered="#{participantDataModel.participantInfoEnable}">
					<f:facet name="header">
						<h:outputText value="AutoDR Profile date" escape="false" />
					</f:facet>
					<h:outputText value="#{item.autoDrProfileStartDate}">
						<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
					</h:outputText>
				</rich:column>

				<!--  -->
				<!--                     <rich:column width="100px" sortBy="#{item.programsParticipation}">-->
				<rich:column width="200px">
					<f:facet name="header">
						<h:outputText value="Programs" escape="false" />
					</f:facet>
					<a4j:repeat value="#{item.programsParticipation}" var="program">
						<h:outputText
							style="color:#{program.optOut==0?'black':'gray'}; margin-right:5px"
							value="#{program.programName}  " />
					</a4j:repeat>
				</rich:column>

				<rich:column width="200px">
					<f:facet name="header">
						<h:outputText value="Actions" />
					</f:facet>
					<h:outputLink rendered="#{participantDataModel.editParticipant}"
						value="/pss2.website/commDevDetail.do?dispatch=edit&amp;userName=#{item.name}">
						<f:verbatim> Edit </f:verbatim>
					</h:outputLink>

					<h:outputLink rendered="#{participantDataModel.viewParticipant}"
						value="/pss2.website/commDevDetail.do?dispatch=edit&amp;userName=#{item.name}">
						<f:verbatim> View </f:verbatim>
					</h:outputLink>

					<h:graphicImage value="../pss2.website/images/note.png"
						rendered="#{item.hasNotes}" alt="Notes attached" />
                               |
                     <h:outputLink
						value="#{item.defaultFacdashPage}?user=#{item.name}&amp;installer=#{item.installer}&amp;role=#{item.userType}&amp;aggDisplayMode=reset">
						<f:verbatim> Customer View </f:verbatim>
					</h:outputLink>

					<h:outputText value="|"
						rendered="#{participantDataModel.canUserAddClient}" />
					<h:outputLink rendered="#{participantDataModel.canUserAddClient}"
						value="#{item.defaultFacdashPage}?user=#{item.name}&amp;installer=#{item.installer}&amp;role=#{item.userType}&amp;aggDisplayMode=reset&amp;createClient=true">
						<f:verbatim> Add Client </f:verbatim>
					</h:outputLink>

					<a4j:commandLink
						oncomplete="#{rich:component('aggModelPanel')}.show()"
						reRender="aggModelPanel"
						rendered="#{(participantDataModel.aggregationEnabled) and (header1.aggregationEnabled)}"
						value=" | Aggregation">
						<a4j:actionparam name="currParticipant" value="#{item.name}"
							assignTo="#{aggTree.loadParticipant}" />
					</a4j:commandLink>
					
					<h:outputText
						rendered="#{(participantDataModel.aggregationEnabled) and (header1.aggregationEnabled)}"
						value=" [ #{item.aggPartCount} ]" id="aggCount" />
					
					
					<h:outputText value=" | " rendered="#{participantDataModel.sce and (item.requestId ne null) and participantDataModel.canCreateParticipant}" />
					<h:graphicImage value="/images/layout/requestId.png" rendered="#{participantDataModel.sce and (item.requestId ne null) and participantDataModel.canCreateParticipant}"
                                                 onclick="#{rich:component('request_id_dialog')}.show();return false;"/>
						
					<rich:modalPanel  
					id="request_id_dialog" domElementAttachment="form" keepVisualState="false" height="85" width="400">	
						<f:facet name="header">
							<h:outputText value="SSM Request Id" title="SSM RequestId" />
						</f:facet>
						<f:facet name="controls">
						 
							 <h:commandLink value="Close" style="cursor:pointer" onclick="Richfaces.hideModalPanel('request_id_dialog')" />
						
						 </f:facet>
						<br/>
						 <div class="center">
						<h:outputText value="#{item.requestId}" />			</div>
                    </rich:modalPanel>                                   
                                                
                                                
                                                
				</rich:column>

				<f:facet name="footer">
					<rich:datascroller id="paging" limitToList="true" immediate="true"
						rendered="true" for="participants"
						maxPages="#{participantDataModel.totalRowCount}" />
				</f:facet>
			</richext:dataTable>
		</h:form>

	</body>

	<rich:modalPanel id="panel" autosized="false" keepVisualState="false"
		width="315" height="150">
		<h:form id="deleteForm">
			<rich:panel id="messages">
				<h:outputText value="You are about to delete participant(s): " />
				<h:outputText value="#{participantDataModel.selectedParticipant} ">
				</h:outputText>
			</rich:panel>
			<a4j:commandButton id="yes" value="Yes" reRender="participants"
				action="#{participantDataModel.participantDeleteAction}"
				oncomplete="#{rich:component('panel')}.hide();">
			</a4j:commandButton>
			<a4j:commandButton id="no" value="No" ajaxSingle="true"
				action="#{participantDataModel.participantCancelAction}"
				oncomplete="#{rich:component('panel')}.hide();" />
		</h:form>
	</rich:modalPanel>

	<rich:modalPanel id="aggModelPanel" keepVisualState="false"
		autosized="false" height="700" width="600"
		trimOverlayedElements="false">
		<f:facet name="header">
			<h:outputText value="Aggregation Group Editor" />
		</f:facet>

		<a4j:status id="waitStatus" forceId="true"
			onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
			onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />

		<rich:messages layout="table" globalOnly="false"
			infoClass="global-message-info" warnClass="global-message-warn"
			errorClass="global-message-error" fatalClass="global-message-fatal" />

		<rich:dragIndicator id="indicator" />

		<h:form id="aggForm">
			<center>
				<h3 align="center">
					<h:outputText
						value="Participants Aggregated by Program for #{aggTree.parentParticipant}" />
				</h3>
				<h:outputText value="Current Participant" />
				<rich:spacer width="5px" />
				<h:outputText id="aggName" value="#{ header1.participantName }"
					style="font-weight:bold" />
				<h:outputText value="Changes are saved as they are made." />
			</center>

			<h:panelGrid columns="2"
				columnClasses="vertical-align:top;width:280px;
				height:420px">
				<rich:panel style="width:280px;height:420px;"
					header="Aggregation by Program"
					bodyClass="height:420px;overflow:auto;">

					<rich:tree id="aggTreeView" value="#{aggTree.treeNode}"
						var="aggNode"
						reRender="availableParts,selectedParticipant,switch,partHeader"
						ajaxSubmitSelection="true" dragIndicator="indicator"
						status="waitStatus" toggleOnClick="false"
						dropListener="#{aggTree.dropListener}"
						changeExpandListener="#{aggTree.processExpansion}"
						nodeSelectListener="#{aggTree.processSelection}"
						adviseNodeSelected="#{aggTree.adviseNodeSelected}"
						style="width:260px;height:400px;vertical-align:top;overflow:auto;">

						<rich:treeNode acceptedTypes="#{aggNode}">
							<h:outputText value="#{aggNode}" />
						</rich:treeNode>
					</rich:tree>
				</rich:panel>

				<rich:panel style="width:280px;height:420px;" id="partHeader"
					header="Available Participants for #{aggTree.displayProgram}"
					bodyClass="height:420px;overflow:auto;">
					<rich:extendedDataTable height="380px" width="80%"
						id="availableParts" selectionMode="multi"
						selection="#{aggTree.selection}"
						value="#{aggTree.availableParticipantList}" var="partS">
						<rich:column sortable="false" style="border-style:none">
							<f:facet name="header">
								<h:outputText value="Participants" id="flag" />
							</f:facet>
							<rich:dragSupport dragIndicator=":indicator"
								dragType="#{aggTree.currProgram}" dragValue="#{partS}">
								<rich:dndParam name="label" value="#{partS}" />
							</rich:dragSupport>
							<h:outputText value="#{partS}"></h:outputText>
						</rich:column>
					</rich:extendedDataTable>
					<a4j:commandButton value="List All" reRender="availableParts"
						action="#{aggTree.showParticipantList}">
					</a4j:commandButton>
				</rich:panel>
			</h:panelGrid>

			<rich:spacer height="10px" />
			<h:panelGrid columns="2">
				<a4j:commandButton id="done" value="Done" reRender="participants"
					oncomplete="#{rich:component('aggModelPanel')}.hide();" />
				<a4j:commandButton id="switch" value="Remove"
					reRender="aggTreeView,availableParts,selectedParticipant,switch"
					status="waitStatus"
					disabled="#{'' ==  aggTree.selectedParticipantName}">
					<a4j:actionparam name="switchName"
						value="#{aggTree.selectedParticipantName}"
						assignTo="#{aggTree.removingParticipant}" />
				</a4j:commandButton>
			</h:panelGrid>
			<rich:spacer height="10px" />
			<h:outputText value="Selected Participant:" />
			<rich:spacer width="5px" />
			<h:outputText escape="false"
				value="#{aggTree.selectedParticipantName}" style="font-weight:bold"
				id="selectedParticipant" />
		</h:form>
	</rich:modalPanel>

	<rich:modalPanel id="waitModalPanel" autosized="true" width="200"
		height="120" moveable="false" resizeable="false">
		<f:facet name="header">
			<h:outputText value="Processing" />
		</f:facet>
		<h:outputText value="Please wait..." />
		<center>
			<h:graphicImage value="/images/ajax-loader.gif" />
		</center>
	</rich:modalPanel>
</jsp:root>
