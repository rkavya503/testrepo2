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

						<rich:panel styleClass="panel-style">
							<f:facet name="header">
								<h:outputLabel value="Add Event Rule in Event #{event.name}"  title="Add Event Rule in Event" />
							</f:facet>
							<h:panelGrid columns="2" styleClass="panel-style">
								<h:outputLabel for="start" value="Start Time (hh:mm):" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="start" value="#{eventRule.rule.start}" required="true"
										label="#{eventRule.rule.start}"
										title="Start Time">
										<f:convertDateTime pattern="HH:mm"/>
									</h:inputText>
									<h:message for="start" errorClass="errors" />
								</h:panelGroup>
		
								<h:outputLabel for="end" value="End Time (hh:mm):" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="end" value="#{eventRule.rule.end}" required="true"
										label="#{eventRule.rule.end}"
										title="End Time">
										<f:convertDateTime pattern="HH:mm"/>
									</h:inputText>
									<h:message for="end" errorClass="errors" />
								</h:panelGroup>
		
								<h:outputLabel for="selectOneMenu_mode" value="Mode:" style="font-weight:bold"/>
								<h:selectOneMenu id="selectOneMenu_mode" value="#{eventRule.rule.mode}"
									title="Mode">
									<f:selectItems value="#{eventRule.availableModes}"/>
								</h:selectOneMenu>
		
								<h:outputLabel for="selectOneMenu_variable" value="Variable:" style="font-weight:bold"/>
								<h:selectOneMenu id="selectOneMenu_variable" value="#{eventRule.rule.variable}"
									title="Variable">
									<f:selectItems value="#{eventRule.availableVariables}"/>
								</h:selectOneMenu>
		
								<h:outputLabel for="name" value="Operator:" style="font-weight:bold"/>
								<h:selectOneMenu id="name" value="#{eventRule.operator}"
									title="Operator">
									<f:selectItems value="#{eventRule.availableOperators}"/>
								</h:selectOneMenu>
		
								<h:outputLabel for="value" value="Value:" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="value" value="#{eventRule.rule.value}"
										required="true" label="#{eventRule.rule.value}" 
										title="Value"/>
									<h:message for="value" errorClass="errors" />
								</h:panelGroup>
							</h:panelGrid>
							
							<div id="fm-submit">
								<table border="0">
									<tr>
										<td>
											<h3>
												<h:commandButton value="Create Rule" title="Create Rule" action="#{eventRules.createRuleAction}" />
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Cancel" title="Cancel" action="#{eventRules.cancelRuleAction}"/>
											</h3>
										</td>
									</tr>
								</table>
							</div>
						</rich:panel>
					</rich:panel>
				</h:form>
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
