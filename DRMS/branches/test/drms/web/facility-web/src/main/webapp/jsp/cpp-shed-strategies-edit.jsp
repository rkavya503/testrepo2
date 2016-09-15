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

				<rich:panel>
					<f:facet name="header">
                        <h:outputText value="Shed Strategy for Program #{clientProgram.consolidationProgramName} for Client #{client.name}" />
					</f:facet>
					<rich:dataTable id="clients_programs"
						value="#{clientProgram.cppShedStrategies}" var="strategy"
						styleClass="content-tab-table" >
						<rich:column width="50%">
							<f:facet name="header">
								<h:outputText value="Time Block" title="Time Block"/>
							</f:facet>
							<h:outputText value="#{strategy.timeBlockString}"/>
						</rich:column>
						<rich:column width="50%">
							<f:facet name="header">
								<h:outputText value="Level"  title="Level"/>
							</f:facet>
							<h:selectOneMenu value="#{strategy.mode}" disabled="#{!client.userAuthorized}">
								<f:selectItems value="#{clientProgram.cppShedStrategyAvailableModes}"/>
							</h:selectOneMenu>
						</rich:column>
					</rich:dataTable>

					<div id="fm-submit">
						
						<table border="0">
								<tr>
									<td>
										<h3>
										<a4j:commandButton
											 value="Save" title="Save"
												oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel" rendered="#{client.batchUpdateEnabled and client.userAuthorized}"
												 >
										</a4j:commandButton>	
										
										
<%-- 										<h:commandButton value="Save" title="Save" rendered="#{!client.batchUpdateEnabled and client.userAuthorized}" --%>
<%-- 												action="#{clientProgram.saveCPPShedStrategiesAction}" /> --%>
										
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="Cancel" title="Cancel" 
												action="#{clientProgram.cancelCPPShedStrategiesAction}"  
												immediate="true" />
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
										<a4j:commandButton id="yes" value="Yes" 
										   action="#{clientProgram.saveCPPShedStrategiesAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   title="Save"/>
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