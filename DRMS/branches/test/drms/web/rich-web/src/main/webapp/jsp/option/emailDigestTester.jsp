<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Test Client Email Digest Tester</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<jsp:include page="../header_options.jsp" />
		<h:form>
			<a4j:keepAlive beanName="emailDigestTester" />
			<h:panelGrid columns="4" styleClass="event-panel-grid">
				<h:panelGroup>
					<p align="left">Start Date:</p>
				</h:panelGroup>
				<h:panelGroup>
					<rich:calendar value="#{emailDigestTester.startDate}" 
						   cellHeight="22px" style="width:180px" 
						   datePattern="#{applicationScope.dateFormat}"/>
				</h:panelGroup>
				<h:panelGroup>
					<p align="left">End Date:</p>
				</h:panelGroup>
				<h:panelGroup>
					<rich:calendar value="#{emailDigestTester.endDate}" 
						   cellHeight="22px" style="width:180px" 
						   datePattern="#{applicationScope.dateFormat}"/>
				</h:panelGroup>
				<h:commandButton value="Generate" title="Generate Digest Email" action="#{emailDigestTester.digestAction}" />
				<h:commandButton value="Search" title="Search Digest Email" action="#{emailDigestTester.retrieveDigestAction}" />
			</h:panelGrid>
			<rich:dataTable style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Test Client Email Digest Tester" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			<rich:dataTable value="#{emailDigestTester.results}" var="item" style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="contact address" escape="false" /></f:facet>
					<h:outputText value="#{item.contactName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="create time" escape="false" /></f:facet>
					<h:outputText value="#{item.createTime}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="subject" escape="false" /></f:facet>
					<h:outputText value="#{item.subjectList}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="content" escape="false" /></f:facet>
					<h:outputText value="#{item.content}" />
				</rich:column>
			</rich:dataTable>
			
			
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>