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
	</head>

	<f:view>



		<h:form id="settings">
			<a4j:region>
				<jsp:include page="/jsp/header_program.jsp" />
				<rich:spacer height="1px" width="700px" />
				<rich:messages layout="table" globalOnly="false"
					infoClass="global-message-info" warnClass="global-message-warn"
					errorClass="global-message-error" fatalClass="global-message-fatal" />
				<rich:dataTable
					value="#{demandLimitingProgramSettingsEditor.settings}"
					var="setting" width="100%">
					<f:facet name="header">
						<h:outputText value="Demand Limiting Program Settings" />
					</f:facet>
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
						<h:outputText value="#{setting.threshold}"  rendered="#{setting.showMode}" />
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
						<h:inputText value="#{setting.value}" />
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
							<h:commandButton
								action="#{demandLimitingProgramSettingsEditor.saveSettings}"
								value="Save" />
							<h:commandButton
								action="#{demandLimitingProgramSettingsEditor.cancel}"
								value="Cancel" />
						</rich:panel>
					</f:facet>
				</rich:dataTable>
				<jsp:include page="/jsp/footer.jsp" />
			</a4j:region>
		</h:form>



	</f:view>
</jsp:root>