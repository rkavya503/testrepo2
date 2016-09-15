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
			<div id="frame">
				<h:form id="navForm"> 
					<jsp:include page="header.jsp" />

					<rich:panel styleClass="content-panel">
						<jsp:include page="client-header.jsp" />

						<rich:panel styleClass="content-panel">
							<f:facet name="header">
								<h:outputText
									value="Rules for Event #{event.name}" title="Rules for Event"/>
							</f:facet>

							<rich:dataTable id="list" value="#{eventRules.jsfRules}" 
								var="rule" >
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="Start" title="Start" />
									</f:facet>
									<h:outputText value="#{rule.rule.start}">
										<f:convertDateTime pattern="HH:mm" />
									</h:outputText>
								</rich:column>
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="End Time" title="End Time" />
									</f:facet>
									<h:outputText value="#{rule.rule.end}">
										<f:convertDateTime pattern="HH:mm" />
									</h:outputText>
								</rich:column>
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="Mode" title="Mode" />
									</f:facet>
									<h:outputText value="#{rule.rule.mode}" />
								</rich:column>
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="Variable" title="Variable" />
									</f:facet>
									<h:outputText value="#{rule.rule.variable}" />
								</rich:column>
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="Operator" title="Operator" />
									</f:facet>
									<h:outputText value="#{rule.operator}" />
								</rich:column>
								<rich:column width="100">
									<f:facet name="header">
										<h:outputText value="Value" title="Value" />
									</f:facet>
									<h:outputText value="#{rule.rule.value}" />
								</rich:column>
							</rich:dataTable>

							<rich:spacer height="10px" />

							<div id="fm-submit">
								<h3><h:commandButton value="Cancel" title="Cancel" action="#{eventRules.cancelRulesAction}" /></h3>
							</div>
						</rich:panel>
					</rich:panel>
				</h:form>

				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
