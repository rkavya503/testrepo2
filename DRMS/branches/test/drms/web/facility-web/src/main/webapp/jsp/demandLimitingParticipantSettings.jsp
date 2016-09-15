<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java"
		contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
	<head>
<jsp:include page="/jsp/head.jsp" />
<style type="text/css">
.rich-calendar-input {
	width: 200px;
}
</style>

	</head>



	<f:view>
		<h:form id="navForm">
			<jsp:include page="header.jsp" />

			<a4j:region>
				<a4j:keepAlive beanName="demandLimitingParticipantSettingsEditor" />
				<rich:panel>
					<f:facet name="header">
						<h:outputText
							value="Demand Limiting Settings for #{demandLimitingParticipantSettingsEditor.participantName}" />
					</f:facet>
				</rich:panel>

				<h:messages layout="table" globalOnly="true"
					infoClass="global-message-info" warnClass="global-message-warn"
					errorClass="global-message-error" fatalClass="global-message-fatal" />
				<rich:tabPanel switchType="client" width="100%">
					<rich:tab label="Participant Settings">

						<rich:dataTable
							value="#{demandLimitingParticipantSettingsEditor.settings}"
							var="setting" width="100%">
							<rich:column sortBy="#{setting.sortOrder}" sortOrder="ASCENDING">
								<f:facet name="header">
									<h:outputText value="Sequence" />
								</f:facet>
								<h:outputText value="#{setting.sortOrder}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Category" />
								</f:facet>
								<h:outputText value="#{setting.source}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Threshold" />
								</f:facet>
								<h:outputText value="#{setting.threshold}"
									rendered="#{setting.showMode}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Variable" />
								</f:facet>
								<h:outputText value="#{setting.variable}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Operator" />
								</f:facet>
								<h:outputText value="#{setting.operatorStr}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Value" />
								</f:facet>
								<h:inputText value="#{setting.value}"
									disabled="#{!setting.editable}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Unit of Measure" />
								</f:facet>
								<h:outputText value="#{setting.variableUom}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Mode" />
								</f:facet>
								<h:outputText value="#{setting.mode}"
									rendered="#{setting.showMode}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Signal?" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{setting.signalAction}"
									disabled="true" rendered="#{setting.showMode}" />
							</rich:column>
							<rich:column>
								<f:facet name="header">
									<h:outputText value="Notify?" />
								</f:facet>
								<h:selectBooleanCheckbox value="#{setting.notifyAction}"
									disabled="true" rendered="#{setting.showMode}" />
							</rich:column>
							<f:facet name="footer">
								<rich:panel>
									<table border="0">
										<tr>
											<td>
												<h3>
													<h:commandButton action="#{demandLimitingParticipantSettingsEditor.validate}" value="Validate Settings" />
												</h3>
											</td>
											<td>
												<h3>
													<h:commandButton action="#{demandLimitingParticipantSettingsEditor.calculate}" value="Calculate Thresholds" />
												</h3>
											</td>
											<td>
												<h3>
													<h:commandButton action="#{demandLimitingParticipantSettingsEditor.reset}" value="Reset Settings" />
												</h3>
											</td>
										</tr>
									</table>	
								</rich:panel>
							</f:facet>

						</rich:dataTable>
					</rich:tab>
					<rich:tab label="OptOut Period">
						<h:panelGrid columns="2" styleClass="panel-style">
							<h:outputLabel for="demandLimitingBlockadeStart"
								value="Demand limiting OptOut start: " />
							<h:panelGroup>
								<rich:calendar
									value="#{demandLimitingParticipantSettingsEditor.optOutStart}"
									id="demandLimitingBlockadeStart" locale="en/US" popup="true"
									datePattern="M/dd/yy hh:mm a" required="false"
									requiredMessage="When client has opted out of Demand Limiting Program, opt out start date is required"
									showApplyButton="true" cellWidth="60px" cellHeight="26px"
									style="width:200px" inputSize="30" />
								<h:message for="demandLimitingBlockadeStart"
									errorClass="message-error" />
							</h:panelGroup>


							<h:outputLabel for="demandLimitingBlockadeEnd"
								value="Demand limiting OptOut end: " />
							<h:panelGroup>
								<rich:calendar
									value="#{demandLimitingParticipantSettingsEditor.optOutEnd}"
									id="demandLimitingBlockadeEnd" locale="en/US" popup="true"
									datePattern="M/dd/yy hh:mm a" required="false"
									requiredMessage="When client has opted out of Demand Limiting Program, opt out end date is required"
									showApplyButton="true" cellWidth="60px" cellHeight="26px"
									style="width:200px" inputSize="30" />
								<h:message for="demandLimitingBlockadeEnd"
									errorClass="message-error" />
							</h:panelGroup>
						</h:panelGrid>

						<rich:spacer height="40px" />

						<h3><h:commandButton
							action="#{demandLimitingParticipantSettingsEditor.resetOptOutPeriod}"
							value="Reset OptOut Period" /></h3>


					</rich:tab>
				</rich:tabPanel>
				<rich:toolBar height="34" itemSeparator="line">
					<rich:toolBarGroup>
						
						
						<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton
												action="#{demandLimitingParticipantSettingsEditor.saveSettings}"
												value="Save" />
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton
												action="#{demandLimitingParticipantSettingsEditor.cancel}"
												value="Cancel" />
										</h3>
									</td>
								</tr>
							</table>	
					</rich:toolBarGroup>
				</rich:toolBar>
				<jsp:include page="footer.jsp" />

			</a4j:region>


		</h:form>

	</f:view>
</jsp:root>