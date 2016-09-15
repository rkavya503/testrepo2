<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="/jsp/head.jsp" />
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
	             	<rich:panel styleClass="panel-style">

							<h:panelGrid columns="2" styleClass="panel-style">
								<h:outputLabel for="start" value="Start Time (hh:mm):" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="start"
										value="#{rule.rule.start}"
										required="true"
										requiredMessage="Start Time is required"
										label="#{rule.rule.start}"
										title="Start Time"
										converterMessage="Start Time must be of the form hh:mm (e.g. 12:34)">
										<f:convertDateTime pattern="HH:mm"/>
									</h:inputText>
									<h:message for="start" errorClass="message-error" />
								</h:panelGroup>

								<h:outputLabel for="end" value="End Time (hh:mm):" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="end"
										value="#{rule.rule.end}"
										required="true"
										requiredMessage="End Time is required"
										label="#{rule.rule.end}"
										title="End Time"
										converterMessage="End Time must be of the form hh:mm (e.g. 12:34)">
										<f:convertDateTime pattern="HH:mm"/>
									</h:inputText>
									<h:message for="end" errorClass="message-error" />
								</h:panelGroup>
<%--	
								<h:outputLabel for="selectOneMenu_mode" value="Mode:" style="font-weight:bold"/>

								<h:selectOneMenu id="selectOneMenu_mode" value="#{rule.rule.mode}"
									title="Mode">
									<f:selectItems value="#{rule.availableModes}"/>
								</h:selectOneMenu>

								<h:outputLabel for="variable" value="Variable:" style="font-weight:bold"/>
								<h:selectOneMenu id="variable" value="#{rule.rule.variable}"
									disabled="#{rule.variableValueEditable}"
									title="Variable">
									<f:selectItems value="#{rule.availableVariables}"/>
								</h:selectOneMenu>

								<h:outputLabel for="name" value="Operator:" style="font-weight:bold"/>
								<h:selectOneMenu id="name" value="#{rule.operator}" title="Operator">
									<f:selectItems value="#{rule.availableOperators}"/>
									<a4j:support event="onchange"
										reRender="variable, value"
										action="#{rule.operatorAction}"/>
								</h:selectOneMenu>
--%>

								<h:outputLabel for="value" value="Value:" style="font-weight:bold"/>
								<h:panelGroup>
									<h:inputText id="value" value="#{rule.rule.value}"
										required="true" label="#{rule.rule.value}"
										disabled="#{rule.variableValueEditable}"
										title="Value"/>
									<h:message for="value" errorClass="errors" />
								</h:panelGroup>
							</h:panelGrid>

							<div id="fm-submit">
								<h:commandButton value="Create Rule"
                                                 title="Create Rule"
									action="#{rules.createRuleAction}"/>
								<rich:spacer width="5px" />
								<h:commandButton value="Cancel" title="Cancel"
									action="#{rules.cancelRuleAction}"
									immediate="true"/>
							</div>
						</rich:panel>
				</h:form>
			
			</div>
		</body>
	</f:view>
</html>
