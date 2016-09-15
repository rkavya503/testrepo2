<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">

     <head>
        <script>
            function resetClearElements(){
                document.forms[1].elements[1].value = "";
            }
        </script>
    </head>

	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

    <body onload="resetClearElements()" >
    <h:form id="clientsForm" onkeydown="if((event||window.event).keyCode==13) {#{rich:element('FilterButton')}.click(); Event.stop(event || window.event);}">
        
		<rich:messages layout="table" globalOnly="false" infoClass="global-message-info" warnClass="global-message-warn" errorClass="global-message-error" fatalClass="global-message-fatal" />

		<rich:dataTable width="100%">
			<f:facet name="header4">
				<f:facet name="header">
					<rich:columnGroup>
	                    <rich:column  style="text-align:left;font-size: 15px; " colspan="9">
							<h:outputText id="title" value="Clients" />
	                    </rich:column>
                        <rich:column colspan="1" width="200px">
                            <a4j:commandLink oncomplete="#{rich:component('panel')}.show()" reRender="panel" value="" rendered="#{clientDataModel.canDeleteClient}">
								<h:graphicImage value="/images/layout/delete_device.gif" />
							</a4j:commandLink>
						</rich:column>
					</rich:columnGroup>
				</f:facet>
			</f:facet>
		</rich:dataTable>

		<rich:dataTable id="headerGrid" width="100%">
			<f:facet name="header4">
				<f:facet name="caption">
					<rich:columnGroup>
						<rich:column id="queryRegion">
							<table width="100%" style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold;">
								<tr>
									<td align="right" width="8%"><h:outputText value="Search : " /></td>
									<td align="left" width="15%">
										<h:panelGroup id="filterConditionArea">
											<h:inputText immediate="true" id="filterCondition" value="#{clientDataModel.searchCondition.value}" style="border:1px solid; width:150px;" rendered="#{clientDataModel.filtedByTextInput}">
												<a4j:support event="onkeyup" reRender="outtext" /> 
											</h:inputText>
											<h:outputText id="outtext" value="#{clientDataModel.searchCondition.value}" style="display:none"/>
											<h:selectOneMenu id="filterConditionType" value="#{clientDataModel.searchCondition.clientType}"  rendered="#{clientDataModel.filterByClientType}" style="width:150px">
												<f:selectItems value="#{clientDataModel.clientTypes}" />
											</h:selectOneMenu>
											<h:selectOneMenu id="filterConditionEventStatus" value="#{clientDataModel.searchCondition.eventStatus}" rendered="#{clientDataModel.filtedByEventStatus}" style="width:150px">
												<f:selectItems value="#{clientDataModel.eventStatusOptions}" />
											</h:selectOneMenu>
											<h:selectOneMenu id="filterConditionStatus" value="#{clientDataModel.searchCondition.commsStatus}" rendered="#{clientDataModel.filtedByCommsStatus}" style="width:150px">
											    <f:selectItems value="#{clientDataModel.commsStatusOptions}" />
											</h:selectOneMenu>
										</h:panelGroup>
									</td>	
									<td align="right" width="8%"><h:outputText value="Filter By : " /></td>
									<td align="left" width="7%">
										<a4j:region>
											<h:selectOneMenu immediate="true" id="filterOptions" style="padding-left:5px;border: 1px solid;" value="#{clientDataModel.searchCondition.param}"  valueChangeListener="#{clientDataModel.filterTypeChanged}" >
												<f:selectItems value="#{clientDataModel.filterOptions}" />
												<a4j:support event="onchange" reRender="filterConditionArea"/>
											</h:selectOneMenu>
										</a4j:region>
									</td>
									<td align="left" width="6%"><a4j:commandButton  oncomplete="resetClearElements()"  id="FilterButton" type="submit" value="   Search   " title="Search Clients" reRender="clients, searchHistoryURL" action="#{clientDataModel.filterAction}" style="border: 1px solid #2BB0FF;" disabled="#{clientDataModel.canSearchClient}"></a4j:commandButton></td>
									<td align="right" width="2%"><h:selectBooleanCheckbox value="#{clientDataModel.searchInResult}" disabled="#{clientDataModel.searchInResults}"></h:selectBooleanCheckbox></td>
									<td align="left" width="15%"><h:outputText value="Search in Result " /></td>
									<td align="left"><h:commandButton value="     Clear     " title="Clear Query Conditions and Results" action="#{clientDataModel.clearAction}" style="border: 1px solid #2BB0FF;" /></td>
									<td align="left"><h:commandButton value="     Export    " title="Export Result"   action="#{clientDataModel.exportHtmlTableToExcel}" style="border: 1px solid #2BB0FF;" disabled="#{clientDataModel.canExport}" /></td>
									<td align="left"><h:commandButton value="Export Contacts" title="Export Contacts" action="#{clientDataModel.exportContactsAction}" style="border: 1px solid #2BB0FF;" disabled="#{clientDataModel.canExportContacts}" /></td>
									<td width="20%"></td>
                                </tr>
							</table>
							<br />
							<table>
								<tr>
									<td align="center" valign="middle" style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold;">
										<h:outputText value="Search By : " />
										<a4j:commandLink action="#{clientDataModel.searchAllAction}" value="All" reRender="clients,searchHistoryURL">
											<f:param name="all" value="all" />
										</a4j:commandLink>
									</td>
									<td>
										<h:panelGrid id="searchHistoryURL">
											<a4j:repeat value="#{clientDataModel.searchHistory.indexedConditions}"  var="item">
												>>
												<a4j:commandLink action="#{clientDataModel.historySearchAction}" value="#{item.label}" 
													reRender="clients,searchHistoryURL" 
													style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold; color:#{clientDataModel.searchHistory.maxWorkingIndex &gt;= item.sequence? '#27577D':'#B4C8E6'}">
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
		
		<richext:dataTable width="100%" id="clients" value="#{clientDataModel}" var="item" rows="20">
            <rich:column width="50px">
				<f:facet name="header"><h:outputText value="Delete" escape="false" /><br /></f:facet>
				<h:selectBooleanCheckbox disabled="#{(clientDataModel.deleteCheckBoxDisable) || (item.disabled)}" value="#{item.selected}"  />
				<h:outputText rendered="false" value="Delete"></h:outputText>
			</rich:column>
			<rich:column sortBy="#{participantName}" width="100px" sortable="true">
				<f:facet name="header"><h:outputText value="Client Name" escape="false"  /></f:facet>
				<h:outputText id="name" value="#{item.name}" />
			</rich:column>
			<rich:column sortBy="#{parent}" width="100px" sortable="true">
				<f:facet name="header"><h:outputText value="Participant" escape="false"  /></f:facet>
				<h:outputText  value="#{item.participantName}"></h:outputText>
			</rich:column>
			<rich:column sortBy="#{type}" width="80px" sortable="true">
				<f:facet name="header"><h:outputText value="Type" escape="false"  /></f:facet>
				<h:outputText  value="#{item.type}"></h:outputText>
			</rich:column>
			<rich:column width="200px">
				<f:facet name="header"><h:outputText value="Programs" /></f:facet>
				<a4j:repeat value="#{item.programsParticipation}" var="program">											
					<h:outputText  style="color:#{program.state==1?'black':'gray'}; margin-right:5px" value="#{program.programName}  "/>
				</a4j:repeat>
			</rich:column>
			<rich:column width="100px">
				<f:facet name="header"><h:outputText value= "Event Status"/></f:facet>
				<h:outputText style="color:#{item.event_status_color}; white-space:nowrap;" value="#{item.eventStatus}" />
			</rich:column>
			<rich:column width="90px">
				<f:facet name="header"><h:outputText value="Mode" /></f:facet>
				<h:outputText value="#{item.mode}" style="white-space:nowrap;"/>
			</rich:column>
			<rich:column width="180px" sortBy="#{lastContact}" sortable="true">
				<f:facet name="header"><h:outputText value="Last Contact" /></f:facet>
				<h:outputText value="#{item.lastContact}"><f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/></h:outputText>
			</rich:column>
			<rich:column width="100px" sortBy="#{status}" sortable="true">
				<f:facet name="header"><h:outputText value="Comm Status" /></f:facet>
                <h:outputText style="color:#{item.comm_status_color}" value="#{item.status}" />
			</rich:column>
			<rich:column width="100px" sortBy="#{deviceType}" sortable="true">
				<f:facet name="header"><h:outputText value="Device Type" /></f:facet>
				<h:outputText value="#{item.deviceType}"></h:outputText>
			</rich:column>	
			<rich:column width="100px" rendered="#{clientDataModel.sceFlag}">
				<f:facet name="header"><h:outputText value="Participant Type" /></f:facet>
				<h:outputText value="#{item.participantType}"></h:outputText>
			</rich:column>
			<rich:column width="100px" rendered="#{clientDataModel.sceFlag}">
				<f:facet name="header"><h:outputText value="ABank" /></f:facet>
				<h:outputText value="#{item.ABank}"></h:outputText>
			</rich:column>		
            <rich:column width="100px" rendered="#{clientDataModel.sceFlag}">
				<f:facet name="header"><h:outputText value="Lead Accounts" /></f:facet>
				<h:outputText value="#{item.leadAccountString}"></h:outputText>
			</rich:column>
			<rich:column width="200px" >
				<f:facet name="header"><h:outputText value="Action" /></f:facet>
				<h:outputLink value="/facdash/jsp/client-edit.jsf?user=#{item.participantName}&amp;client=#{item.name}&amp;aggDisplayMode=reset">
					<f:verbatim> Customer View </f:verbatim>
				</h:outputLink>
                |
                 <h:outputLink value="/facdash/jsp/client-edit.jsf?user=#{item.participantName}&amp;client=#{item.name}&amp;subTab=controlTab&amp;aggDisplayMode=reset">
					<f:verbatim> Change Control Mode </f:verbatim>
				</h:outputLink>
                |
                <h:outputLink value="/facdash/jsp/client-edit.jsf?user=#{item.participantName}&amp;client=#{item.name}&amp;subTab=client_programs&amp;aggDisplayMode=reset">
					<f:verbatim> Strategies </f:verbatim>
				</h:outputLink>
                |
                 <h:outputLink value="/facdash/jsp/client-edit.jsf?user=#{item.participantName}&amp;client=#{item.name}&amp;subTab=contactTab&amp;aggDisplayMode=reset">
					<f:verbatim> Contacts </f:verbatim>
				</h:outputLink>
			</rich:column>
			<f:facet name="footer">
                <rich:datascroller id="paging" limitToList="true" immediate="true" rendered="true" for="clients"  maxPages="#{clientDataModel.totalRowCount}" />
			</f:facet>
		</richext:dataTable>
    </h:form>
</body>
        
		<rich:modalPanel id="panel" autosized="false" keepVisualState="false" width="380" height="200">
			<h:form id="deleteForm">
				<rich:panel id="messages">
					<h:outputText value="You are about to delete client(s): " />
					<h:outputText value="#{clientDataModel.selectedParticipant} "/>
				</rich:panel>
				<a4j:commandButton id="yes" value="Yes" reRender="clients" action="#{clientDataModel.participantDeleteAction}" oncomplete="#{rich:component('panel')}.hide();" />
				<a4j:commandButton id="no" value="No" ajaxSingle="true"	action="#{clientDataModel.participantCancelAction}" oncomplete="#{rich:component('panel')}.hide();" />
			</h:form>
		</rich:modalPanel>
		
</jsp:root>
        
        
