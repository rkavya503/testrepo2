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
			<h:form id="navForm">
				<jsp:include page="header.jsp" />

				<rich:panel id="strategies-panel" styleClass="content-panel">

					<rich:dataTable id="strategies" rows="20"
						value="#{rtpShedStrategies.strategies}" var="strategy" 
						reRender="ds" styleClass="content-tab-table">
						<f:facet name="header">
							<h:outputText value="RTP Shed Strategies for Client #{client.name} in Program #{clientProgram.programName}" />
						</f:facet>
						<rich:column width="5%">
							<h:selectBooleanCheckbox value="#{strategy.delete}" title="Check for Selection"/>
						</rich:column>
						<rich:column width="45%" sortBy="#{strategy.strategy.name}">
							<f:facet name="header">
								<h:outputText value="Name"  title="Name"/>
							</f:facet>
							<richext:commandLink value="#{strategy.strategy.name}" 
								action="#{rtpShedStrategies.editStrategyAction}"
								actionListener="#{rtpShedStrategies.editStrategyListen}"
								 headingLevel="h3">
								<f:param name="strategyName" value="#{strategy.strategy.name}"/>
							</richext:commandLink> 
						</rich:column>
						<rich:column width="20%" sortBy="#{strategy.strategy.type}">
							<f:facet name="header">
								<h:outputText value="Type" title="Type"/>
							</f:facet>
							<h:outputText value="#{strategy.strategy.type}"/>
						</rich:column>
						<rich:column width="10%" sortBy="#{strategy.strategy.summerActive}">
							<f:facet name="header">
								<h:outputText value="Summer" title="Summer"/>
							</f:facet>
							<h:selectBooleanCheckbox value="#{strategy.strategy.summerActive}" 
							title="Summer">
								<a4j:support event="onclick" 
									action="#{rtpShedStrategies.summerActiveAction}"
									reRender="strategies-panel">
									<a4j:actionparam name="summerActiveStrategyName"
										value="#{strategy.strategy.name}" 
										assignTo="#{rtpShedStrategies.summerActiveStrategyName}"/>
								</a4j:support>
							</h:selectBooleanCheckbox>
						</rich:column>
						<rich:column width="10%" sortBy="#{strategy.strategy.winterActive}">
							<f:facet name="header">
								<h:outputText value="Winter" title="Winter"/>
							</f:facet>
							<h:selectBooleanCheckbox value="#{strategy.strategy.winterActive}" 
														title="Winter">
								<a4j:support event="onclick" 
									action="#{rtpShedStrategies.winterActiveAction}"
									reRender="strategies-panel">
									<a4j:actionparam name="winterActiveStrategyName"
										value="#{strategy.strategy.name}" 
										assignTo="#{rtpShedStrategies.winterActiveStrategyName}"/>
								</a4j:support>
							</h:selectBooleanCheckbox>
						</rich:column>
						<rich:column width="10%" sortBy="#{strategy.strategy.weekendActive}">
							<f:facet name="header">
								<h:outputText value="Weekend" title="Weekend"/>
							</f:facet>
							<h:selectBooleanCheckbox value="#{strategy.strategy.weekendActive}" 
							title="Weekend">
								<a4j:support event="onclick" 
									action="#{rtpShedStrategies.weekendActiveAction}"
									reRender="strategies-panel">
									<a4j:actionparam name="weekendActiveStrategyName"
										value="#{strategy.strategy.name}" 
										assignTo="#{rtpShedStrategies.weekendActiveStrategyName}"/>
								</a4j:support>
							</h:selectBooleanCheckbox>
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
											<h:commandButton value="New Strategy" title="New Strategy" action="#{rtpShedStrategies.newStrategyAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton rendered="#{client.batchUpdateEnabled}"
											 value="Delete Strategies" title="Delete Selection Strategies"
											 oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel">
												 <a4j:actionparam name="deleteStrategy" value="true" assignTo="#{rtpShedStrategies.deleteStrategy}"/>
											</a4j:commandButton>
											
											<h:commandButton rendered="#{!client.batchUpdateEnabled}" value="Delete Strategies" title="Delete Selection Strategies" action="#{rtpShedStrategies.deleteStrategiesAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton rendered="#{client.batchUpdateEnabled}"
											 value="Save Strategies" title="Save Strategies"
											 oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel">
												 <a4j:actionparam name="deleteStrategy" value="false" assignTo="#{rtpShedStrategies.deleteStrategy}"/>
											</a4j:commandButton>
											
											<h:commandButton rendered="#{!client.batchUpdateEnabled}" value="Save Strategies"  title="Save Strategies" action="#{rtpShedStrategies.saveStrategiesAction}"/>
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="Cancel" title="Cancel" action="#{rtpShedStrategies.cancelStrategiesAction}"/>
										</h3>
									</td>
								</tr>
							</table>	
					</div>
				</rich:panel>
				
				
				<rich:modalPanel id="confirm_panel" autosized="true"
                    keepVisualState="false"
                    width="315"
                    height="150"
                    >
                    <div id="deleteForm">
                    
            			<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
            		<rich:panel id="messages"  >
            			<a id="confirmBox" href="javascript:void(0);">
                			<h:outputText value="This action will apply to the following clients. "/>
                		</a>
            		</rich:panel>
					<div style="height:150px;overflow-y:auto;overflow-x:hidden">
					
					<rich:dataTable id="clients_descendantClients" style="margin-left: 0px;margin-right: 0px;width:100%"
							value="#{clientProgram.descendantClients}" var="client"
							styleClass="content-tab-table">
							<rich:column width="50%">
								<f:facet name="header">
									<h:outputText value="Name" title="Name"/>
								</f:facet>
								<h:outputText value="#{client.participantName}"/>
							</rich:column>								
						</rich:dataTable>
					</div>			            		
						<table border="0">
						<tr>
							<td>
								<h3>																
									<a4j:commandButton id="yesCreate" value="Yes" 
										   action="#{rtpShedStrategies.saveStrategiesAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   rendered="#{!rtpShedStrategies.deleteStrategy}"
										   reRender="strategies"
										   title="Create Contact"/>
									
									<a4j:commandButton id="DeleteStrategies" value="Yes" 
										   action="#{rtpShedStrategies.deleteStrategiesAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   rendered="#{rtpShedStrategies.deleteStrategy}"
										   reRender="strategies"
										   title="Delete Selection Strategies"/>								
								</h3>
							</td>
							<td>
								<h3>
									<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('confirm_panel')}.hide();" title="Cancel Clients"/>					
								</h3>
							</td>
						</tr>
						</table>
					
					
            		</div>
 				</rich:modalPanel>
			</h:form>

			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
