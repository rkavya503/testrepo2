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
		<h:form id="navForm">
			<jsp:include page="header.jsp" />

			<a4j:region>
				<a4j:keepAlive beanName="demandLimitingParticipantSettingsEditor" />
				<rich:panel>
					<f:facet name="header">
						<h:outputText
							value="Mock Meter for #{demandLimitingMockMeter.participantName}" />
					</f:facet>
				</rich:panel>

				<h:messages layout="table" globalOnly="true"
					infoClass="global-message-info" warnClass="global-message-warn"
					errorClass="global-message-error" fatalClass="global-message-fatal" />
				<rich:panel>
					<Label for="intervalLoad">Interval Load </Label>
					<rich:spacer width="60px" />
					<h:inputText value="#{demandLimitingMockMeter.intervalLoad}"
						id="intervalLoad" />
					<rich:spacer width="10px" />kW

				</rich:panel>
				<rich:toolBar height="34" itemSeparator="line">
					<rich:toolBarGroup>
						
						
						<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton action="#{demandLimitingMockMeter.sendIntervalLoad}" value="Send" />
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton action="#{demandLimitingMockMeter.cancel}" value="Cancel" />
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