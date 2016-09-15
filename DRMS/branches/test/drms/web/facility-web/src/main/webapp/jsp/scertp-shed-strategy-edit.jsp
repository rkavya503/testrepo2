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

				<rich:panel styleClass="content-panel">
					<f:facet name="header">
						<h:outputText 
							value="Add RTP Shed Strategy for Client #{client.name} in Program #{clientProgram.programName}" />
					</f:facet>
					
					<h:messages layout="table" globalOnly="true" 
						infoClass="global-message-info" warnClass="global-message-warn" 
						errorClass="global-message-error" fatalClass="global-message-fatal"/>

					<a4j:region renderRegionOnly="true">
						<h:panelGrid columns="2" styleClass="panel-style">
								<h:panelGroup>
									<h:graphicImage id="requiredName" alt="Required" url="/images/layout/required.jpg" />
									<h:outputLabel for="name" value="Name:" style="font-weight:bold"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:inputText id="name" 
										value="#{rtpShedStrategy.strategy.name}" 
										required="true"
										label="#{rtpShedStrategy.strategy.name}"
										disabled="#{rtpShedStrategy.edit}"
										title="Name"
										requiredMessage="Name is required">
										<a4j:support event="onblur"/>
									</h:inputText>
									<h:message for="name" errorClass="message-error" />
								</h:panelGroup>
						</h:panelGrid>
					</a4j:region>
						
					<a4j:region renderRegionOnly="true">
						<fieldset>
							<legend ><b>RTP Shed Strategy Types</b></legend>
							<h:selectOneRadio id="radioButtonStrategy" value="#{rtpShedStrategy.strategy.type}" 
								styleClass="radio-buttons">
								<a4j:support event="onclick" reRender="hours" 
									actionListener="#{rtpShedStrategy.typeListener}"/>
								<f:selectItems value="#{rtpShedStrategy.availableTypes}"/>
							</h:selectOneRadio>
						</fieldset>
						
						<rich:spacer height="20px" width="700px" />
					
						<rich:dataTable id="hours" rows="24"
							value="#{rtpShedStrategy.entries}" var="entry" 
							styleClass="content-tab-table">
							<rich:column width="30%">
								<f:facet name="header">
									<h:outputText value="Time Block"  title="Time Block"/>
								</f:facet>
								<h:outputText value="#{entry.timeBockString}"
									style="font-weight:bold"/>
							</rich:column>
							<rich:column width="30%">
								<f:facet name="header">
									<h:outputText value="Moderate Price"  title="Moderate Price"/>
								</f:facet>
								<h:panelGroup>						
									<h:inputText id="moderate" value="#{entry.moderatePrice}"
										label="#{entry.moderatePrice}" 
										validatorMessage="must be blank or a price"
										title="Moderate Price">
										<f:validateDoubleRange minimum="0.0" maximum="1000.0"/>
									</h:inputText>
									<h:message for="moderate" errorClass="message-error" />
								</h:panelGroup>
							</rich:column>
							<rich:column width="30%">
								<f:facet name="header">
									<h:outputText value="High Price" title="High Price"/>
								</f:facet>
								<h:panelGroup>						
									<h:inputText id="high" value="#{entry.highPrice}"
										label="#{entry.highPrice}" 
										validatorMessage="must be blank or a price"
										title="High Price">
										<f:validateDoubleRange minimum="0.0" maximum="1000.0"/>
									</h:inputText>
									<h:message for="high" errorClass="message-error" />
								</h:panelGroup>
							</rich:column>
							<rich:column width="10%" 
								rendered="#{rtpShedStrategy.showCopyPaste}">
								
								<table border="0">
									<tr>
										<td>
											<h3>
												<a4j:commandButton value="Copy" title="Copy" 
													action="#{rtpShedStrategy.copyEntryAction}">
													<a4j:actionparam name="timeBlockString"
														value="#{entry.timeBockString}" 
														assignTo="#{rtpShedStrategy.timeBlockString}"/>
												</a4j:commandButton> 
											</h3>
										</td>
										<td>
											<h3>
												<a4j:commandButton value="Paste" title="Paste" 
													action="#{rtpShedStrategy.pasteEntryAction}"
													reRender="hours">
													<a4j:actionparam name="timeBlockString"
														value="#{entry.timeBockString}" 
														assignTo="#{rtpShedStrategy.timeBlockString}"/>
												</a4j:commandButton> 
											</h3>
										</td>
									</tr>
								</table>
							</rich:column>
						</rich:dataTable>
					</a4j:region>

					<div id="fm-submit">
						<table border="0">
								<tr>
									<td>
										<h:panelGroup  layout="block"  rendered="#{!rtpShedStrategy.edit}">
										<h3>
											<h:commandButton value="Create Strategy" title="Create Strategy"
												action="#{rtpShedStrategies.createStrategyAction}" 
												rendered="#{!rtpShedStrategy.edit}"/>
										</h3>
										</h:panelGroup>
									</td>
									<td>
										<h:panelGroup  layout="block"  rendered="#{rtpShedStrategy.edit}">
										<h3>
											<h:commandButton value="Update Strategy" title="Update Strategy"
												action="#{rtpShedStrategies.updateStrategyAction}" 
												rendered="#{rtpShedStrategy.edit}"/>
										</h3>
										</h:panelGroup>
									</td>
									<td>
										<h:panelGroup  layout="block">
										<h3>
											<h:commandButton value="Cancel" title="Cancel" 
												action="#{rtpShedStrategies.cancelStrategyAction}" 
												immediate="true"/>
										</h3>
										</h:panelGroup>
									</td>
								</tr>
							</table>	
					</div>

				</rich:panel>
			</h:form>
			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
