<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Communication Emails</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<h:form>
	<jsp:include page="../header_reports.jsp" />
		
			<a4j:keepAlive beanName="communicationEmails" />
			<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
				<tr>
					<td><h:outputText value="From:"/></td>
					<td>
						<rich:calendar value="#{communicationEmails.searchFromDate}" 
						   cellHeight="22px" style="width:80px" 
						   datePattern="#{applicationScope.dateFormat}"/>
						<h:selectOneMenu value="#{communicationEmails.searchFromHour}">
							<f:selectItems value="#{communicationEmails.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{communicationEmails.searchFromMin}">
							<f:selectItems value="#{communicationEmails.minList}"/>
						</h:selectOneMenu>
					</td>
					
				</tr>
				<tr>
					<td><h:outputText value="To:"/></td>
					<td>
						<rich:calendar value="#{communicationEmails.searchToDate}" 
						   cellHeight="22px" style="width:100px" 
						   datePattern="#{applicationScope.dateFormat}"/>
						<h:selectOneMenu value="#{communicationEmails.searchToHour}">
							<f:selectItems value="#{communicationEmails.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{communicationEmails.searchToMin}">
							<f:selectItems value="#{communicationEmails.minList}"/>
						</h:selectOneMenu>
						<h:commandButton value="Search" title="Search" action="#{communicationEmails.search}" />
						<h:commandButton value="Export" title="Export" action="#{communicationEmails.exportToExcel}" />
					</td>
				</tr>
			</table>
			
			<BR/>
			
			<rich:dataTable style="min-width: 800px;width:expression(document.body.clientWidth < 800 ? '800px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Communication Emails" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			<rich:dataTable value="#{communicationEmails.emails}" var="item" style="min-width: 800px;width:expression(document.body.clientWidth < 800 ? '800px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="Creation Time" escape="false" /></f:facet>
					<h:outputText value="#{item.creationTime}" >
						<f:convertDateTime pattern="#{applicationScope.fullDateTimeFormat}"/>
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Contact Name" escape="false" /></f:facet>
					<h:outputText value="#{item.contactName}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Address" escape="false" /></f:facet>
					<h:outputText value="#{item.emailAddress}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Subject" escape="false" /></f:facet>
					<h:outputText value="#{item.subject}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Participant Name" escape="false" /></f:facet>
					<h:outputText value="#{item.participantName}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Client Name" escape="false" /></f:facet>
					<h:outputText value="#{item.clientName}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Status" escape="false" /></f:facet>
					<h:outputText value="#{item.statusExpress}" >
					</h:outputText>	
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Sent Time" escape="false" /></f:facet>
					<h:outputText value="#{item.sendTime}" >
					</h:outputText>	
				</rich:column>
			</rich:dataTable>
			<h:outputText value="#{communicationEmails.displayNothing}" style="font-size:15px;"/>
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>