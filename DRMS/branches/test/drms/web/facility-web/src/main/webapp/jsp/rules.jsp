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
									value="Rules for Program #{clientProgram.programName}" title="Rules for Program"/>
							</f:facet>

                       <rich:messages layout="table" globalOnly="false"
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />

                            <rich:extendedDataTable height="300px" width="65%"  id="list" value="#{rules.jsfRules}"
								var="item"  >
                           <%--
                            <rich:dataTable   id="list" value="#{rules.jsfRules}"
								var="rule"  >
							    <rich:orderingList id="list" value="#{rules.jsfRules}"
								var="rule" listWidth="350" converter="ruleConverter">
                              --%>
                                <rich:column width="60">
									<h:selectBooleanCheckbox value="#{item.delete}" disabled="#{rules.deleteCheckboxEnable}" title="Check for Selection"/>
								</rich:column>
								<rich:column width="60">
									<f:facet name="header">
										<h:outputText value="Start"  title="Start"/>
									</f:facet>
									<h:outputText value="#{item.rule.start}">
										<f:convertDateTime pattern="HH:mm" />
									</h:outputText>
								</rich:column>
								<rich:column width="70">
									<f:facet name="header">
										<h:outputText value="End Time"  title="End Time"/>
									</f:facet>
									<h:outputText value="#{item.rule.end}">
										<f:convertDateTime pattern="HH:mm" />
									</h:outputText>
								</rich:column>
								<rich:column width="80">
									<f:facet name="header">
										<h:outputText value="Mode"  title="Mode"/>
									</f:facet>
									<h:outputText value="#{item.rule.mode}" />
								</rich:column>
								<rich:column width="80">
									<f:facet name="header">
										<h:outputText value="Variable"  title="Variable"/>
									</f:facet>
									<h:outputText value="#{item.rule.variable}"
										rendered="#{!item.variableValueEditable}"/>
								</rich:column>
								<rich:column width="80">
									<f:facet name="header">
										<h:outputText value="Operator"  title="Operator"/>
									</f:facet>
									<h:outputText value="#{item.operator}" />
								</rich:column>
								<rich:column width="80">
									<f:facet name="header">
										<h:outputText value="Value"  title="Value" />
									</f:facet>
									<h:outputText value="#{item.rule.value}"
										rendered="#{!item.variableValueEditable}"/>
								</rich:column>
						<%--
                                </rich:orderingList>
                            </rich:dataTable>
                        --%>
                            </rich:extendedDataTable>
							<rich:spacer height="10px" />

							<div id="fm-submit">
								<table border="0">
									<tr>
										<td>
											<h3>
												<h:commandButton value="New Rule" title="New Rule" action="#{rules.newRuleAction}" rendered="#{rules.canAddRule}"/>
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Save Rules" title="Save Rules" action="#{rules.saveRulesAction}" rendered="#{rules.canSaveRule}" />
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Cancel" title="Cancel" action="#{rules.cancelRulesAction}" />
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Delete Rule" title="Delete Rule" action="#{rules.deleteRulesAction}" rendered="#{rules.canDeleteRule}" />
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Up" title="Cancel" action="#{rules.sortRulesUpAction}" immediate="false" rendered="#{rules.canGoUpRule}"/>
											</h3>
										</td>
										<td>
											<h3>
												<h:commandButton value="Down" title="Cancel" action="#{rules.sortRulesDownAction}" immediate="false" rendered="#{rules.canGoDownRule}"/>
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
