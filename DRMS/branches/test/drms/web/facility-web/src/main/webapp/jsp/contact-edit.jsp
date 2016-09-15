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
				<script type="text/javascript">
				function commDisable() {
					if(document.getElementById('navForm:comm').checked){
						document.getElementById('navForm:on').disabled=false;
						document.getElementById('navForm:off').disabled=false;
					}else{
						document.getElementById('navForm:on').disabled=true;
						document.getElementById('navForm:off').disabled=true;
					}
				} 
				</script>
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
					<rich:panel styleClass="content-panel">
						<jsp:include page="client-header.jsp" />
						
						<h:message for="navForm" errorClass="message-error" />
						
						<rich:panel styleClass="panel-style">
							<f:facet name="header">
								<h:outputLabel value="Contact for Client #{client.name} " />
							</f:facet>
							<div>
								<h:outputLabel value="(" styleClass="form-field"/>
								<h:graphicImage alt="*" title="*"  url="/images/layout/required.jpg" />
								<h:outputLabel value="denotes a required field)" styleClass="form-field"/>
							</div>
							<h:panelGrid columns="3" styleClass="panel-style" style="position:relative;">
							
								<h:outputLabel for="selectOneMenu_type" value="Type:" style="font-weight:bold"/>
								<h:selectOneMenu id="selectOneMenu_type" value="#{contact.type}">
									<f:selectItems value="#{contact.availableTypes}"/>
									<a4j:support event="onchange" reRender="addressText" ajaxSingle="true"/>
								</h:selectOneMenu>
								
								
								<h:message for="address" errorClass="message-error" styleClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:12px;"/>
								<h:panelGroup>
									<h:graphicImage id="requiredAddress" alt="required" title="required" url="/images/layout/required.jpg" />
									<h:outputLabel for="address"  id="addressText" value="#{contact.addressText}:"
										styleClass="form-field"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:inputText 
										id="address" 
										value="#{contact.address}" 
										label="#{contact.address}" 
										required="true"
										requiredMessage="#{contact.addressText} is required"
										
                                        validator="#{contact.validateEmail}"/>
								</h:panelGroup>
								
								<h:message for="name" errorClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:12px;"/>
								<h:panelGroup>
									<h:graphicImage id="requiredName" alt="required" title="required" url="/images/layout/required.jpg" />
									<h:outputLabel for="name"  value="Name:" 
										styleClass="form-field"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:inputText 
										id="name" 
										value="#{contact.name}" 
										label="#{contact.name}" 
										required="true"
										requiredMessage="Name is required"
										
										/>
								</h:panelGroup>	
								
								<h:message for="comm" errorClass="errors" style="position:absolute; left:290px;width:200px;margin-top:12px;"/>
						 		<h:outputLabel for="comm"  value="Comm Notifications:" style="font-weight:bold"/>
								<h:panelGroup>
									<h:selectBooleanCheckbox 
											id="comm" 
											value="#{contact.commNotification}"
											required="true" 
											label="#{contact.commNotification}" 
											onclick="commDisable()" 
											/>
									
								</h:panelGroup>
								
								<h:message for="on" errorClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:12px;"/>
								<h:panelGroup>
									<h:graphicImage id="requiredOnSN" alt="required" title="required" url="/images/layout/required.jpg" />
									<h:outputLabel for="on"  value="On Season Notify:" 
										styleClass="form-field"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:panelGroup>
										<h:inputText 
											id="on" 
											value="#{contact.onSeasonNotification}"
											label="#{contact.onSeasonNotification}"
											required="true" 
											requiredMessage="On Season Notify is required"
											validatorMessage="On Season Notify must be between 0.05 and 100.0"
											>
											<f:validateDoubleRange minimum="0.05" maximum="100.0"/>
										</h:inputText>
										<h:outputLabel value=" hours" 	styleClass="form-field"/>
									</h:panelGroup>
								</h:panelGroup>
								
								<h:message for="off" errorClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:12px;"/>
								<h:panelGroup>
									<h:graphicImage id="requiredSeasonNOff" alt="required" title="required" url="/images/layout/required.jpg" />
									<h:outputLabel for="off"  value="Off Season Notify:" 
										styleClass="form-field"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:panelGroup>
										<h:inputText 
											id="off" 
											value="#{contact.offSeasonNotification}"
											label="#{contact.offSeasonNotification}"
											required="true"
											requiredMessage="Off Season Notify is required"
											validatorMessage="Off Season Notify must be between 0.05 and 100.0"
											>
											<f:validateDoubleRange minimum="0.05"
												maximum="100.0"
											 />
										</h:inputText>
										<h:outputLabel value=" hours" 
											styleClass="form-field"/>
									</h:panelGroup>
								</h:panelGroup>
								
								<h:outputLabel value=""/>
								<h:outputLabel for="selectOneMenu_eventNotification" value="Event Notifications:" style="font-weight:bold"/>
								<h:panelGroup>
									<h:selectOneMenu id="selectOneMenu_eventNotification" value="#{contact.eventNotification}" >
                                        <f:selectItems value="#{contact.availableEventNotificationTypes}"/>
                                    </h:selectOneMenu>
								</h:panelGroup>
								
								
							</h:panelGrid>	
							
							<h:panelGrid columns="1" styleClass="panel-style">
								<h:outputLabel value="Event Notification Definitions" style="font-weight:bold"/> 
                                <h:outputLabel value="No Notifications: No event notifications will be sent to the corresponding contact during event dispatched." />
                                <h:outputLabel value="All Notifications: All event notifications will be sent to the corresponding contact during event dispatched." />
                                <h:outputLabel value="Strategy Initiated Notifications: Only notifications where a shed strategy other than normal is initiated during event dispatched." />
							</h:panelGrid>
				<a4j:region>
							<h:panelGrid columns="1" styleClass="panel-style"
								id="demandLimitingNotificationSettings" rendered="#{contact.participantDemandLimitingEnabled}">
								<h:panelGroup>
									<h:outputLabel for="demandLimitingNotifications"
										value="Demand Limiting Notifications:"
										style="font-weight:bold" />
									<h:selectBooleanCheckbox id="demandLimitingNotifications"
										value="#{contact.demandLimitingNotifications}"
										required="false" label="Demand Limiting Notifications:"
										immediate="true" style="font-weight:bold">
										<a4j:support event="onchange"
											reRender="demandLimitingNotificationSettings" />
									</h:selectBooleanCheckbox>
									<h:message for="demandLimitingNotifications"
										errorClass="errors" />
								</h:panelGroup>
							</h:panelGrid>



							<h:panelGrid columns="2" styleClass="panel-style">
							<h:outputLabel for="defaultMsgThreshold"  value="Use default message threshold:" style="font-weight:bold"/>
								<h:panelGroup>
									<h:selectBooleanCheckbox 
										id="defaultMsgThreshold" 
										value="#{contact.defaultMsgThreshold}"
										required="true" 
										label="#{contact.defaultMsgThreshold}" 
										immediate="true" >
                                        	<a4j:support event="onchange"  reRender="threshold"/>										
									</h:selectBooleanCheckbox>
								</h:panelGroup>						
						</h:panelGrid>													
						<h:panelGrid columns="2" styleClass="panel-style">
							
							<h:outputLabel for="threshold"  value="Message Threshold:" style="font-weight:bold"/>
								<h:panelGroup>								
									<h:inputText 
										id="threshold" 
										value="#{contact.msgThreshold}"
										label="#{contact.msgThreshold}"
										requiredMessage="Message Threshold is required"
										disabled="#{contact.defaultMsgThreshold}"
										immediate="true" >
										<f:converter converterId="javax.faces.Integer"/>
									</h:inputText>
									<h:message for="threshold" errorClass="message-error" />
								</h:panelGroup>								
						</h:panelGrid>
						
						<h:panelGrid columns="1" styleClass="panel-style">
                                <h:outputLabel value="Threshold is maximum email messages with same priority which will be sent per day" />
                        </h:panelGrid>	
                        
                        					
						</a4j:region>																
							<div id="fm-submit">
								
								<table border="0">
									<tr>
										<td>
											<h:panelGroup  layout="block"  rendered="#{!contact.edit}" >
											<h3>
																				
												<a4j:commandButton
												 value="Create Contact" title="Create Contact"
													oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel"
													rendered="#{(!contact.edit)&&client.batchUpdateEnabled}"
												/>
												
												
												<h:commandButton value="Create Contact" title="Create Contact"
													action="#{client.createContactAction}" 
													rendered="#{(!contact.edit)&&(!client.batchUpdateEnabled)}" />
											</h3>
											</h:panelGroup>
										</td>
										<td>
											<h:panelGroup  layout="block"  rendered="#{contact.edit}" >
											<h3>
												<a4j:commandButton
												 value="Save Contact" title="Save Contact"
													oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel"
													rendered="#{contact.edit&&client.batchUpdateEnabled}"
												>
												</a4j:commandButton>
												
												<h:commandButton value="Save Contact" title="Save Contact"
													action="#{client.updateContactAction}" 
													rendered="#{contact.edit&&(!client.batchUpdateEnabled)}" />
										
											</h3>
											</h:panelGroup>
										</td>
										<td>
											<h3>
												<h:commandButton value="Cancel" title="Cancel" 
													action="#{client.cancelContactAction}" 
													immediate="true" />
											</h3>
										</td>
									</tr>
								</table>	
							</div>
						</rich:panel>
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
								value="#{client.descendantClients}" var="client"
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
										   action="#{client.createContactAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   rendered="#{!contact.edit}"
										   title="Create Contact"/>
									
										<a4j:commandButton id="yesSave" value="Yes" 
										   action="#{client.updateContactAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   rendered="#{contact.edit}"
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
			</div>
		</body>
	</f:view>
</html>
