<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<style>

</style>
<f:view>
    <head>
		<title>Client Info</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
		<h:form id="clientInfoForm"  style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
			<jsp:include page="../header_reports.jsp" />
			<!--------------------------------------------------- Client Information Table Begin--------------------------------------------------->
			<rich:dataTable style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Client Info" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			
			<rich:dataTable id="clientInfoTable" value="#{clientInfoTableBackingBean.clientInfos}" var="item" style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="Client" escape="false" /></f:facet>
					<h:outputText value="#{item.client}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Participant" escape="false" /></f:facet>
					<h:outputText value="#{item.participant}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Account<br/>Number" escape="false" /></f:facet>
					<h:outputText value="#{item.accountNumber}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Parent" escape="false" /></f:facet>
					<h:outputText value="#{item.parent}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Configured Programs - Enabled" escape="false" /></f:facet>
					<h:outputText value="#{item.activeClientPrograms}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Event<br/>Status" escape="false" /></f:facet>
					<h:outputText value="#{item.eventStatus}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Event<br/>Mode" escape="false" /></f:facet>
					<h:outputText value="#{item.mode}" />
				</rich:column>				
				<rich:column >
					<f:facet name="header"><h:outputText value="Comm<br/>Status" escape="false" /></f:facet>
					<h:outputText value="#{item.commStatus}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Last<br/>Contact" escape="false" /></f:facet>
					<h:outputText value="#{item.lastContact}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Client<br/>Type" escape="false" /></f:facet>
					<h:outputText value="#{item.clientType}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Device<br/>Type" escape="false" /></f:facet>
					<h:outputText value="#{item.deviceType}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Contacts" escape="false" /></f:facet>
					<h:outputText value="#{item.contacts}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Participant Type" escape="false" /></f:facet>
					<h:outputText value="#{item.participantType}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Opt Out" escape="false" /></f:facet>
					<h:outputText value="#{item.eventOptOutDisplay}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="Lead Accounts" escape="false" /></f:facet>
					<h:outputText value="#{item.leadAccountDisplay}" />
				</rich:column>
				<rich:column>
					<f:facet name="header"><h:outputText value="ABank" escape="false" /></f:facet>
					<h:outputText value="#{item.ABank}" />
				</rich:column>
			</rich:dataTable>
			
			<h:commandLink 	value="Export To Excel" title="Export To Excel" action="#{clientInfoTableBackingBean.exportToExcel}" />
			<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
		</h:form>
    </body>
</f:view>
</html>