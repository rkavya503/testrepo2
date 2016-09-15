<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
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
					
						<rich:dataTable id="sub_accounts" rows="20"
							value="#{subAccounts.accounts}" var="account" 
							reRender="ds" styleClass="content-tab-table">
							<rich:column width="5%">
								<h:selectBooleanCheckbox value="#{account.selected}" title="Check for Selection"/>
							</rich:column>
							<rich:column width="10%" sortBy="#{account.name}">
								<f:facet name="header">
									<h:outputText value="Account #"  title="Account #"/>
								</f:facet>
								<richext:commandLink value="#{account.name}" 
									action="#{subAccounts.editAccountAction}"
									actionListener="#{subAccounts.accountListen}"
									headingLevel="h3">
									<f:param name="accountName" value="#{account.name}"/>
								</richext:commandLink> 
							</rich:column>
							<rich:column width="10%" sortBy="#{account.premise}">
								<f:facet name="header">
									<h:outputText value="Premise #"  title="Premise #"/>
								</f:facet>
								<h:outputText value="#{account.premise}" />
							</rich:column>
							<rich:column width="10%" sortBy="#{account.enrollmentDate}">
                                <f:facet name="header">
                                    <h:outputText value="Enrollment Date" title="Enrollment Date"/>
                                </f:facet>
                                <h:outputText value="#{account.enrollmentDate}">
                                    <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                                </h:outputText>
                            </rich:column>
							<rich:column width="10%" sortBy="#{account.startDate}">
								<f:facet name="header">
									<h:outputText value="Start Date" title="Start Date"/>
								</f:facet>
								<h:outputText value="#{account.startDate}">
									<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
								</h:outputText>
							</rich:column>
							<rich:column width="10%" sortBy="#{account.deactiveDate}">
								<f:facet name="header">
									<h:outputText value="Deactivate Date" title="Deactivate Date"/>
								</f:facet>
								<h:outputText value="#{account.deactiveDate}">
									<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
								</h:outputText>
							</rich:column>
							<rich:column width="35%">
								<f:facet name="header">
									<h:outputText value="Comment"  title="Comment"/>
								</f:facet>
								<h:outputText value="#{account.comment}" />
							</rich:column>
							<rich:column width="10%" sortBy="#{account.active}">
								<f:facet name="header">
									<h:outputText value="Active"  title="Active"/>
								</f:facet>
								<h:outputText value="#{account.active}" />
							</rich:column>
				            <f:facet name="footer">
				                <rich:datascroller id="ds"></rich:datascroller>
				            </f:facet>
						</rich:dataTable>
						
						<rich:spacer height="10px" /> 

						<div id="fm-submit">
							<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton value="New Account" title="New Account" action="#{subAccounts.newAccountAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="Delete Accounts" title="Delete Selection Accounts" action="#{subAccounts.deleteAccountsAction}"/>
										</h3>
									</td>
								</tr>
							</table>	
						</div>

					</rich:panel>
				</h:form>

				<jsp:include page="footer.jsp" />
				</div>
		</body>
	</f:view>
</html>
