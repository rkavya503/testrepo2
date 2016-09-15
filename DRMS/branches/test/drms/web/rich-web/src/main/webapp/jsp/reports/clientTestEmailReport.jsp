<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Client Test Email Report</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<h:form>
	<jsp:include page="../header_reports.jsp" />
		
			<a4j:keepAlive beanName="emailReportBean" />
			<h:panelGrid columns="4" styleClass="event-panel-grid">
				<h:panelGroup>
					<p align="left">Start Date:</p>
				</h:panelGroup>
				<h:panelGroup>
					<rich:calendar value="#{emailReportBean.startDate}" 
						   cellHeight="22px" style="width:180px" 
						   datePattern="#{applicationScope.dateFormat}"/>
				</h:panelGroup>
				<h:panelGroup>
					<p align="left">End Date:</p>
				</h:panelGroup>
				<h:panelGroup>
					<rich:calendar value="#{emailReportBean.endDate}" 
						   cellHeight="22px" style="width:180px" 
						   datePattern="#{applicationScope.dateFormat}"/>
				</h:panelGroup>
				<h:commandButton value="Search" title="Search" action="#{emailReportBean.search}" />
				<h:commandButton value="Export" title="Export" action="#{emailReportBean.export}" />
			</h:panelGrid>
			<rich:dataTable style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Client Test Email Report" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			<rich:dataTable value="#{emailReportBean.entities}" var="item" style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="Participant Name" escape="false" /></f:facet>
					<h:outputText value="#{item.participantName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Client Name" escape="false" /></f:facet>
					<h:outputText value="#{item.clientName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Programs Name" escape="false" /></f:facet>
					<h:outputText value="#{item.programName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Contact Name" escape="false" /></f:facet>
					<h:outputText value="#{item.contactName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Contact Email" escape="false" /></f:facet>
					<h:outputText value="#{item.contactAddress}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Creation Time" escape="false" /></f:facet>
					<h:outputText value="#{item.createTime}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Client Test Emails List" escape="false" /></f:facet>
					<h:outputText value="#{item.subjectList}" />
				</rich:column>
			</rich:dataTable>
			
			
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>