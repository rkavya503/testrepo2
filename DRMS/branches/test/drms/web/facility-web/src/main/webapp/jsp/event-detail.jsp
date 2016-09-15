<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			<h:form id="navForm">
				<jsp:include page="header.jsp" />
			</h:form>

				<rich:panel style="width:500px">
					<f:facet name="header">
						<h:outputText value="Event Info" title="Event Info" />
					</f:facet>
					<h:panelGrid columns="2">
					<h:outputText value="Name:" />
					<h:outputText value="#{event.name}" />

					<h:outputText value="Program:" />
					<h:outputText value="#{event.progamName}" />

					<h:outputText value="Program:" />
					<h:outputText value="#{event.notification}" >
						<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
					</h:outputText>

					<h:outputText value="Start:" />
					<h:outputText value="#{event.start}">
						<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
					</h:outputText>

					<h:outputText value="End:" />
					<h:outputText value="#{event.end}" >
						<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
					</h:outputText>

					<h:outputText value="Status:" />
					<h:outputText value="#{event.status}" />

				</h:panelGrid>
			</rich:panel>

			<rich:extendedDataTable id="events" value="#{events.clients}"
				var="client" width="400px" height="140px">
				<f:facet name="header">
					<h:outputText value="Participants" title="Participants" />
				</f:facet>
				<rich:column width="100" sortBy="#{client.name}">
					<f:facet name="header">
						<h:outputText value="Name" title="Name" />
					</f:facet>
					<h:outputText value="#{client.name}" />
				</rich:column>
				<rich:column width="100" sortBy="#{client.account}">
					<f:facet name="header">
						<h:outputText value="Account" title="Account" />
					</f:facet>
					<h:outputText value="#{client.account}" />
				</rich:column>
				<rich:column width="200" >
					<f:facet name="header">
						<h:outputText value="Actions"  title="Actions"/>
					</f:facet>
						<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton value="constraints" title="constraints" action="#{client.editConstraintsAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="rules" title="rules" action="#{client.editRulesAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="bids" title="bids" action="#{client.editBidsAction}"/>
										</h3>
									</td>
								</tr>
							</table>
				</rich:column>
			</rich:extendedDataTable>
			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
