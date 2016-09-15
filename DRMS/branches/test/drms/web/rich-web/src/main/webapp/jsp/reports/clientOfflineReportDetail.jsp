<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Client Offline Report Daily Detail</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<h:form>
	<jsp:include page="../header_reports.jsp" />
		
			<a4j:keepAlive beanName="clientOfflineReport" />
			<rich:dataTable style="min-width: 800px;width:expression(document.body.clientWidth < 800 ? '800px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Client Offline Daily Report Detail" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			
			<h:panelGrid columns="1" style="min-width: 800px;width:expression(document.body.clientWidth < 800 ? '800px' : '100%' );">
				<h:panelGroup>
					<a4j:commandButton style="position:relative;float:right;" value="Back" action="#{clientOfflineReport.cancel}" immediate="true" title="Back"/>
					<h:commandButton style="position:relative;float:right;" value="Export" title="Export" action="#{clientOfflineReport.exportToExcel}" />
				</h:panelGroup>
			</h:panelGrid>
			
			<rich:dataTable value="#{clientOfflineReport.detail}" var="item" style="min-width: 800px;width:expression(document.body.clientWidth < 800 ? '800px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="Client Name" escape="false" /></f:facet>
					<h:outputText value="#{item.clientName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Participant Name" escape="false" /></f:facet>
					<h:outputText value="#{item.participantName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Account Number" escape="false" /></f:facet>
					<h:outputText value="#{item.accountNumber}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Last Contact" escape="false" /></f:facet>
					<h:outputText value="#{item.lastContact}" >
						<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/>
					</h:outputText>
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Offline Duration" escape="false" /></f:facet>
					<h:outputText value="#{item.offline}" />
				</rich:column>
			</rich:dataTable>
			
			<br/>
			
			<h:panelGrid columns="4" styleClass="event-panel-grid" style="display:">
				<h:panelGroup>
					<p align="left">Generate Date:</p>
				</h:panelGroup>
				<h:panelGroup>
					<h:outputText value="#{clientOfflineReport.report.generateTime}" >
						<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/>
					</h:outputText>	
					
				</h:panelGroup>
			</h:panelGrid>
			
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>