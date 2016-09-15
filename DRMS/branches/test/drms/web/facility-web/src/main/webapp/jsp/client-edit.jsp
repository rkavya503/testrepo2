<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.facdash.*" %>
<%@ page import="java.util.*" %>

<html lang="en-US" xml:lang="en-US">

	<f:view>
		<head>
			<jsp:include page="head.jsp" />
			
			 <script type="text/javascript">
			//<![CDATA[
				var $j=jQuery.noConflict();
				function onChangeListener(){
					var manualObj = document.getElementById('navForm:controlStateSelectRadio:0');
					if(manualObj){
						var manualFlag = manualObj.checked;
						if(manualFlag){
							var pendingMenuObj = document.getElementById('navForm:pendingMenu');
							pendingMenuObj.setAttribute("disabled", "disabled");
							var modeMenuObj = document.getElementById('navForm:modeMenu');
							modeMenuObj.setAttribute("disabled", "disabled");
							var expiresObj = document.getElementById('navForm:expiresPopupButton');
							expiresObj.style.display = "none"
							var expiresObj1 = document.getElementById('navForm:expiresInputDate');
							expiresObj1.setAttribute("disabled", "disabled");
						}else{
							var pendingMenuObj = document.getElementById('navForm:pendingMenu');
							pendingMenuObj.removeAttribute("disabled");
							var modeMenuObj = document.getElementById('navForm:modeMenu');
							modeMenuObj.removeAttribute("disabled");
							var expiresObj = document.getElementById('navForm:expiresPopupButton');
							expiresObj.style.display = ""
							var expiresObj1 = document.getElementById('navForm:expiresInputDate');
							expiresObj1.removeAttribute("disabled");
						}
					}
				}
				Event.observe(window, 'load', function(){
					onChangeListener();
				});
				
				function checkUncheckAll(obj){
						  var check = obj.checked; 
						  var eles = $j('.contact_select');
						  for(var i=0;i<eles.length;i++){
							if(eles[i].disabled){
							  continue;
							}
							eles[i].checked=check; 
							if(check) Element.addClassName(eles[i],'checked'); 
							else Element.removeClassName(eles[i],'checked');
						  }
						}

				
				function loadFunction() {
				
					var checkbox = document.getElementById('navForm:clients_contacts:contact_selectAll');
					if(checkbox){
						checkbox.checked = false;
					}				
				}
				
				
			//]]>
		  </script>
		
		</head>
		<body onload=loadFunction()>
		 
		
			<div id="frame">
                <h:form id="navForm">
					<jsp:include page="header.jsp" />
					<rich:panel styleClass="content-panel" id ="content-panel" >
						<jsp:include page="client-header.jsp" />

						<rich:messages layout="table" globalOnly="true"
							infoClass="global-message-info" warnClass="global-message-warn"
							errorClass="global-message-error" fatalClass="global-message-fatal"/>

                        <rich:tabPanel activeTabClass="client_programs"
                                       selectedTab="#{client.selectedTab}"
                                tabClass="tab-titles" styleClass="content-tab"
                                immediate="true" >
							<richext:tab label="Client Events" headingLevel="h4">

								<rich:dataTable id="events" rows="20"
									value="#{client.events}" var="event"
									reRender="ds" styleClass="content-tab-table">
									<rich:column width="15%" sortBy="#{event.name}">
										<f:facet name="header">
											<h:outputText value="Name" title="Name" />
										</f:facet>
										<h5>
										<h:outputText value="#{event.name}" />
										</h5>
									</rich:column>
									<rich:column width="15%" sortBy="#{event.programName}">
										<f:facet name="header">
											<h:outputText value="Program" title="Program" />
										</f:facet>
										<h:outputText value="#{event.programName}" />
									</rich:column>
									<rich:column width="15%" sortBy="#{event.start}">
										<f:facet name="header">
											<h:outputText value="Start" title="Start" />
										</f:facet>
										<h:outputText value="#{event.start}">
											<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
										</h:outputText>
									</rich:column>
									<rich:column width="15%" sortBy="#{event.end}">
										<f:facet name="header">
											<h:outputText value="End" title="End"/>
										</f:facet>
										<h:outputText value="#{event.end}">
											<f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
										</h:outputText>
									</rich:column>
									<rich:column width="15%" sortBy="#{event.status}">
										<f:facet name="header">
											<h:outputText value="Status" title="Status" />
										</f:facet>
										<h:outputText value="#{event.status}" />
									</rich:column>
									<rich:column width="25%">
										<f:facet name="header">
											<h:outputText value="Actions" title="Actions" />
										</f:facet>
                                        <table border="0">
											<tr>
												<td>
													<h:panelGroup  layout="block"  rendered="#{header1.customRulesEnabled}">
													<h5>
														<h:commandButton value="Rules" title="Rules"
															action="#{client.editEventRulesAction}"
															actionListener="#{client.editEventRulesListener}"
															rendered="#{header1.customRulesEnabled}">
															<f:attribute name="eventName" value="#{event.name}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>  
												</td>
												<td>
													
													<h5>
														<h:commandButton value="View Signals" title="View Signals"
															action="#{client.editClientSignalsAction}"
															actionListener="#{client.editClientSignalsListener}">
															<f:attribute name="eventName" value="#{event.name}"/>
														</h:commandButton>
													</h5>
													
												</td>
											</tr>
										</table>
									</rich:column>
									<f:facet name="footer">
						                <rich:datascroller id="ds"></rich:datascroller>
						            </f:facet>
						</rich:dataTable>

							</richext:tab>

                          <richext:tab   id="client_programs"  reRender="client_programs"
                                         label="Client Programs" headingLevel="h4" immediate="false">
								<rich:dataTable id="clients_programs" rows="20"
									value="#{client.programs}" var="aProgram"
									reRender="ds" styleClass="content-tab-table">
									<rich:column width="34%" sortBy="#{aProgram.consolidationProgramName}">
										<f:facet name="header">
											<h:outputText value="Name" title="Name" />
										</f:facet>
										<h5>
										<h:outputText value="#{aProgram.consolidationProgramName}" />
										</h5>
									</rich:column>
									<rich:column width="33%"
										sortBy="#{aProgram.participating}">
										<f:facet name="header">
											<h:outputText value="Participating" title="Participating" />
										</f:facet>
                                        <h:selectBooleanCheckbox
                                            disabled="#{!aProgram.activated or !client.userAuthorized}"
                                            value="#{aProgram.participating}"
                                            title="Participate Program"
                                            >
                                            <a4j:support  event="onclick"
                                                
                                                action="#{client.programParticipatingAction}">
                                            </a4j:support>
                                        </h:selectBooleanCheckbox>
									</rich:column>
									<rich:column width="33%">
										<f:facet name="header">
											<h:outputText value="Actions" title="Actions" />
										</f:facet>
										<table border="0">
											<tr>
												<td>
													<h:panelGroup  layout="block"  rendered="#{header1.customRulesEnabled}">
													<h5>
														<h:commandButton value="Rules" title="Rules"
															action="#{client.editCustomRulesAction}"
															actionListener="#{client.editCustomRulesListener}"
															rendered="#{header1.customRulesEnabled}">
															<f:attribute name="programName" value="#{aProgram.programName}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block"  rendered="#{aProgram.rtpShedStrategiesExist}">
													<h5>
														<h:commandButton value="Shed Strategies" title="Shed Strategies"
															action="#{client.editRTPShedStrategiesAction}"
															actionListener="#{client.editRTPShedStrategiesListener}"
															rendered="#{aProgram.rtpShedStrategiesExist}">
															<f:attribute name="programName" value="#{aProgram.programName}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block"  rendered="#{aProgram.cppShedStrategiesExist}">
													<h5>
														<h:commandButton value="Shed Strategies" title="Shed Strategies"
															action="#{client.editCPPShedStrategiesAction}"
															actionListener="#{client.editCPPShedStrategiesListener}"
															rendered="#{aProgram.cppShedStrategiesExist}">
															<f:attribute name="programName" value="#{aProgram.programName}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block"  rendered="#{aProgram.forecastExist}">
													<h5>
														<h:commandButton value="Forecast" title="Forecast"
															action="#{client.viewForecastAction}"
															actionListener="#{client.viewForecastListener}"
															rendered="#{aProgram.forecastExist}">
															<f:attribute name="programName" value="#{aProgram.programName}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block"  rendered="#{aProgram.bidMappingExist}">
													<h5>
														<h:commandButton value="Bid Mappings" title="Bid Mappings"
															action="#{client.editBidMappingAction}"
															actionListener="#{client.editBidMappingListener}"
															rendered="#{aProgram.bidMappingExist}">
															<f:attribute name="programName" value="#{aProgram.programName}"/>
														</h:commandButton>
													</h5>
													</h:panelGroup>
												</td>
											</tr>
										</table>
									</rich:column>
						            <f:facet name="footer">
						                <rich:datascroller id="ds"></rich:datascroller>
						            </f:facet>
								</rich:dataTable>
							</richext:tab>



							<richext:tab id="contactTab"  label="Contacts" headingLevel="h4">
								<rich:dataTable id="clients_contacts" rows="20"
									value="#{client.contacts}" var="contact"
									reRender="ds" styleClass="content-tab-table">
									<rich:column width="2%">
										<f:facet name="header">                                                                                                                                                           
											<h:selectBooleanCheckbox id="contact_selectAll" title="Name" onclick="javascript: checkUncheckAll(this);"  disabled="#{!client.userAuthorized}"/>
											<script>
												var checkbox = document.getElementById('navForm:clients_contacts:contact_selectAll');
												if(checkbox){
													checkbox.checked = false;
												}	
											</script>
                                        </f:facet>

										<h:selectBooleanCheckbox value="#{contact.delete}" styleClass="contact_select"
											title="Select Client for Delete" 
											disabled="#{contact.external}"/>
									</rich:column>
									<rich:column width="12%" sortBy="#{contact.name}">
										<f:facet name="header">
											<h:outputText value="Name"  title="Name" />
										</f:facet>
										<richext:commandLink value="#{contact.name}" 
											disabled="#{!client.userAuthorized}"
											title="#{contact.name}"
											action="#{client.editContactAction}"
											actionListener="#{client.contactListen}"
											 headingLevel="h5">
											<f:param name="contactKey" value="#{contact.key}"/>
										</richext:commandLink>
									</rich:column>
									<rich:column width="14%" sortBy="#{contact.address}">
										<f:facet name="header">
											<h:outputText value="Address" title="Address" />
										</f:facet>
										<h:outputText value="#{contact.address}" />
									</rich:column>
									<rich:column width="12%" sortBy="#{contact.type}">
										<f:facet name="header">
											<h:outputText value="Type" title="Type" />
										</f:facet>
										<h:outputText value="#{contact.type}" />
									</rich:column>
									<rich:column width="12%"
										sortBy="#{contact.offSeasonNotification}">
										<f:facet name="header">
											<h:outputText value="Off Season Notify " title="Off Season Notify " />
										</f:facet>
										<h:outputText value="#{contact.offSeasonNotification}" />
									</rich:column>
									<rich:column width="12%"
										sortBy="#{contact.onSeasonNotification}">
										<f:facet name="header">
											<h:outputText value="On Season Notify"  title="On Season Notify"/>
										</f:facet>
										<h:outputText value="#{contact.onSeasonNotification}" />
									</rich:column>
									<rich:column width="12%" sortBy="#{contact.commNotification}">
										<f:facet name="header">
											<h:outputText value="Comm Notification" title="Comm Notification" />
										</f:facet>
										<h:selectBooleanCheckbox
											value="#{contact.commNotification}"
											disabled="true"/>
									</rich:column>
									<rich:column width="12%" sortBy="#{contact.eventNotification}">
										<f:facet name="header">
											<h:outputText value="Event Notification" title="Event Notification" />
										</f:facet>
										<h:outputText value="#{contact.eventNotificationDisplay}" />
									</rich:column>
									<rich:column width="12%" sortBy="#{contact.defaultMsgThreshold}">
										<f:facet name="header">
											<h:outputText value="Default Msg Threshold" title="Default Msg Threshold"/>
										</f:facet>
										<h:selectBooleanCheckbox
											value="#{contact.defaultMsgThreshold}"
											disabled="true"/>
									</rich:column>
									
									<rich:column width="12%" sortBy="#{contact.msgThreshold}">
										<f:facet name="header">
											<h:outputText value="Msg Threshold" title="Msg Threshold"/>
										</f:facet>
										<h:outputText value="#{contact.msgThreshold}" />
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
											<h5>
												<h:commandButton styleClass="fm-submit" id="newClientBtn" value="New Contact" title="New Contact"  rendered="#{client.userAuthorized}" action="#{client.newContactAction}" />
											</h5>
										</td>
										<td>
											<h5>																								
												<a4j:commandButton rendered="#{client.userAuthorized}"
												 value="Delete Contacts" title="Delete Contacts"
													oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel"												
												>
												</a4j:commandButton>	
												
											</h5>
										</td>
									</tr>
									</table>	
								</div>

							</richext:tab>

							<richext:tab id="controlTab" label="Control" headingLevel="h4" >
								<a4j:region renderRegionOnly="true">
								<fieldset>
									<legend><b>Client Control Type</b></legend>
									<h:selectOneRadio value="#{client.controlState}" id="controlStateSelectRadio" disabled="#{!client.userAuthorized}"
										onclick="onChangeListener();"
										styleClass="radio-buttons">
										<a4j:support event="onclick"
											reRender="control-panel"
											ajaxSingle="true" />
										<f:selectItem itemValue="auto" itemLabel="Auto-DR" id="autoSelectItem"/>
										<f:selectItem itemValue="manual" itemLabel="Manual" id="manualSelectItem"/>
									</h:selectOneRadio>
								</fieldset>

									<h:panelGrid columns="3" styleClass="panel-style" id="control-panel">
										<h:outputLabel value="Pending:" for="pendingMenu" style="font-weight:bold"/>
										<h:selectOneMenu id="pendingMenu" value="#{client.controlPending}"
											>
											<f:selectItem itemValue="none" itemLabel="None" />
											<f:selectItem itemValue="far" itemLabel="Far (Pending OFF)" />
											<f:selectItem itemValue="near" itemLabel="Near (Pending ON)" />
											<f:selectItem itemValue="active" itemLabel="Active" />
										</h:selectOneMenu>
										<h:panelGroup/>
										
										<h:outputLabel value="Mode:" for="modeMenu" style="font-weight:bold"/>
										<h:selectOneMenu id="modeMenu" value="#{client.controlMode}"
											>
											<f:selectItem itemValue="normal" itemLabel="NORMAL" />
											<f:selectItem itemValue="moderate" itemLabel="MODERATE" />
											<f:selectItem itemValue="high" itemLabel="HIGH" />
											<f:selectItem itemValue="special" itemLabel="SPECIAL" />
										</h:selectOneMenu>
										<h:panelGroup/>
										
                                        <h:outputLabel id="expires-text"
                                            value="Expires:" for="expiresInputDate"
                                            styleClass="form-field"/>
                                        <h:panelGroup>
											<a4j:outputPanel layout="block">
												<rich:calendar
														label="Expires time" 
														id="expires"
														value="#{client.controlExpiresDate}"
														popup="true"
														datePattern="#{applicationScope.shortDateTimeFormat}"
														style="width:200px"
														enableManualInput="true"
														required="true" requiredMessage="Expires time is required"
														inputStyle="width:110px"/>
											 </a4j:outputPanel>
                                         </h:panelGroup>
										<h:message for="expires" errorClass="message-error" />
									</h:panelGrid>
								</a4j:region>

								<div id="fm-submit">
									
									<table border="0">
										<tr>
											<td>
												<h5>
													<h:commandButton  value="Update Control" title="Update Control" action="#{client.updateControlAction}" rendered="#{client.userAuthorized}" />
												</h5>
											</td>
										</tr>
									</table>	
								</div>
							</richext:tab>

                <!-- Options sub tab panel -->
                        <richext:tab label="Client Options" id="cOptions" headingLevel="h4"  >
                        <rich:tabPanel id="options" activeTabClass="client_programs" 
                                        tabClass="tab-titles" styleClass="content-tab"
                                        immediate="true" >
                            <richext:tab label="Check Password" id="cpassword" headingLevel="h4" rendered="#{client.userAuthorized}" >
                                
									
								<fieldset>
									<legend><b>Password Check </b>
                                      
                                    </legend>
								<div>
									<h:outputLabel value="(" styleClass="form-field"/>
									<h:graphicImage alt="*" title="*"  url="/images/layout/required.jpg" />
									<h:outputLabel value="denotes a required field)" styleClass="form-field"/>
								</div>
								<h:panelGrid columns="3" styleClass="panel-style" id="cPass-panel"  style="position:relative;">
                                    <h:message for="checkPW" errorClass="message-error" style="position:absolute; left:300px;width:200px;margin-top:-8px;"/>
									<h:panelGroup>
                                            <h:graphicImage id="requiredPassword" alt="required" title="required" url="/images/layout/required.jpg" />
                                            <h:outputLabel value=" Password:" for="checkPW" styleClass="form-field"/>
                                    </h:panelGroup>
									
                                    <h:inputSecret   id="checkPW" title="Password"
										value="#{client.checkPW}"
                                        label="#{client.checkPW}"
										required="true"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										valueChangeListener="#{client.checkPasswordListener}"
										requiredMessage="Password is required"/>
									

									<div/>
									
									
												<h:panelGroup  layout="block">
												<h5>
													<h:commandButton value="Check Password" title="Check Password"
															 type="submit"
															 styleClass="fm-submit"
															 action="#{client.checkPasswordAction}"
															 />	
												</h5>
												</h:panelGroup>
															 
                                </h:panelGrid>
                                </fieldset>	
									
                            </richext:tab>

                            <richext:tab label="Update Password" id="password" headingLevel="h4" rendered="#{client.userAuthorized}" >
							
                                <fieldset>
									<legend><b>Password Update </b>
                                      
                                    </legend>
								<h:outputLabel value="Please make sure the physical client device, such as jace, has the same password. Otherwise the communication will fail." 
                                        />
								<div>
									<h:outputLabel value="(" styleClass="form-field"/>
									<h:graphicImage alt="*" title="*"  url="/images/layout/required.jpg" />
									<h:outputLabel value="denotes a required field)" styleClass="form-field"/>
								</div>		
								<h:panelGrid columns="3" styleClass="panel-style" id="uPass-panel" style="position:relative;">
									<h:panelGroup>
                                        
                                    </h:panelGroup>
									<h:panelGroup/>
                                    <h:panelGroup/>
									
									 <h:message for="newPW" errorClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:-8px;"/>
                                    <h:panelGroup>
                                                <h:graphicImage id="requiredNewPW" alt="required" title="required" 
                                                                url="/images/layout/required.jpg" />
                                                <h:outputLabel value="New Password:" for="newPW"
                                                        styleClass="form-field"/>
                                    </h:panelGroup>

                                    <h:inputSecret 
                                                   id="newPW" title="New Password"
                                            value="#{client.newPW}"
                                            label="#{client.newPW}"
                                            required="true"
                                            requiredMessage="New Password is required"/>
											
									<h:message for="confirmPW" errorClass="message-error" style="position:absolute; left:290px;width:200px;margin-top:-8px;"/>	
									<h:panelGroup>
										<h:graphicImage id="requiredConfirmNewPW" alt="required" title="required"  url="/images/layout/required.jpg" />
										<h:outputLabel value="Confirm New Password:" for="confirmPW"
											styleClass="form-field"/>
									</h:panelGroup>
                                         <h:inputSecret   id="confirmPW" title="Confirm New Password"
										value="#{client.confirmPW}"
										label="#{client.confirmPW}"
										required="true"
										onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
										
										requiredMessage="Confirm New Password is required"/>
									

									<div/>
									
							
									
									<h:panelGroup  layout="block">
												<h5>
													<h:commandButton
														value="Update Password" title="Update Password"
														type="submit"
														action="#{client.updatePasswordAction}"
														styleClass="fm-submit Update_Password_CommandButton"
													/>
												</h5>
									</h:panelGroup>

                                </h:panelGrid>
								
                                </fieldset>

								
						</richext:tab>

                       
                       <richext:tab rendered="#{header1.iirClientPush}" label="Client Push" headingLevel="h4">
                             Client Type:
                                  <h:outputLabel  value="#{client.clientType}" for="type" style="font-weight:bold" />
                                  <br>
                                    <fieldset>
									<legend><b>Client Push </b>

                                    </legend>
                                <h:panelGrid columns="3" styleClass="panel-style" id="clientPush-panel">

								   <h:panelGroup>
										<h:graphicImage
                                                        alt="required" title="required"  url="/images/layout/required.jpg" />
										<h:outputLabel  value="Client push:" for="clientPushCheck"
											styleClass="form-field" />
									</h:panelGroup>
                                    <h:selectBooleanCheckbox
                                            id="clientPushCheck"
                                            value="#{client.push}"
                                            title="push"
                                            >
                                            <a4j:support
                                                event="onclick"
                                                reRender="uPass-panel"
                                                action="#{client.updateClient}">
                                            </a4j:support>
                                     </h:selectBooleanCheckbox>
                               <%--
                                   <br>
                                    <h:panelGroup>
                                        <h:graphicImage alt="required" title="required" 
                                                    url="/images/layout/required.jpg" />
                                        <h:outputLabel for="selectOneMenu_clientType" value="Type:"
                                                       style="font-weight:bold"/>
                                     </h:panelGroup>
                                    <h:selectOneMenu id="selectOneMenu_clientType" value="#{client.clientType}">
                                        <f:selectItem itemValue="AUTO" itemLabel="AUTO" />
                                        <f:selectItem itemValue="MANUAL" itemLabel="MANUAL" />
                                    </h:selectOneMenu>
                               --%>

                                    <br>
                                    <h:panelGroup>
									<h:graphicImage  alt="required" title="required" 
                                                    url="/images/layout/required.jpg" />
									<h:outputLabel value="Address:" for="ip"
											styleClass="form-field"/>
									</h:panelGroup>
								   <h:inputText id="ip"  title="Name:pwd@URL:" 
                                        value="#{client.ip}"
										label="#{client.ip}"
										required="true" 
										requiredMessage="IP address is required"
                                        style="width:300px"
                                        />
									<h:message for="ip" errorClass="message-error"/>
									<div id="fm-submit">
										
										<table border="0">
											<tr>
												<td>
													<h5>
														<h:commandButton
															value="Update Client" title="Update Client"
															type="submit"
															action="#{client.updateClient}"
															styleClass="fm-submit Update_Password_CommandButton"
															/>
													</h5>
												</td>
											</tr>
										</table>	
									</div>
                                </h:panelGrid>
                             </fieldset>

                        </richext:tab>


							<richext:tab label="Location" rendered="false" headingLevel="h4">

								<h:panelGrid columns="2">
									<h:outputText value="Address:" />
									<h:panelGroup>
										<h:inputText id="address" value="#{client.address}"
											required="false" label="#{client.address}" />
										<h:message for="name" errorClass="errors" />
									</h:panelGroup>

									<h:outputText value="" />
									
									<table border="0">
										<tr>
											<td>
												<h5>
													<h:commandButton value="Geocode" action="#{client.geocodeAction}" immediate="true" />
												</h5>
											</td>
										</tr>
									</table>
									
									<h:outputText value="Grid Location:" />
									<h:panelGroup>
										<h:inputText id="grid" value="#{client.gridLocation}"
											required="false" label="#{client.gridLocation}" />
										<h:message for="grid" errorClass="errors" />
									</h:panelGroup>

									<h:outputText value="Latitude:" />
									<h:panelGroup>
										<h:inputText id="latitude" value="#{client.latitude}"
											required="false" label="#{client.latitude}" />
										<h:message for="latitude" errorClass="errors" />
									</h:panelGroup>

									<h:outputText value="Longitude:" />
									<h:panelGroup>
										<h:inputText id="longitude" value="#{client.longitude}"
											required="true" label="#{client.longitude}" />
										<h:message for="longitude" errorClass="errors" />
									</h:panelGroup>

								</h:panelGrid>

							</richext:tab>
                            
                          <richext:tab label="Opt Out" headingLevel="h4" rendered="#{header1.adminLogin}" >
                                <h:outputLabel for="isClientAllowedOptOut" value="Enable OpenADR Client Opt-Out:" />
                                <h:selectBooleanCheckbox id="isClientAllowedOptOut" 
                                                         value="#{client.clientAllowedToOptOut}" disabled="#{!client.userAuthorized}">
                                    <a4j:support event="onclick" action="#{client.updateClientIsAllowedToOptOut}"/>
                                </h:selectBooleanCheckbox>
                            </richext:tab>
                            
                            <richext:tab label="Confirmation" headingLevel="h4" rendered="#{header1.adminLogin}" >
                                <h:outputLabel for="canClientFailConfirmation" value="This client can fail REST or SOAP confirmation and still be marked online:" />
                                <h:selectBooleanCheckbox id="canClientFailConfirmation" 
                                                         value="#{client.clientCanFailConfirmation}" disabled="#{!client.userAuthorized}">
                                    <a4j:support event="onclick" action="#{client.updateClientCanFailConfirmation}"/>
                                </h:selectBooleanCheckbox>
                            </richext:tab>
                           <richext:tab label="Client Type" headingLevel="h4" rendered="#{header1.adminLogin}" >
                                <fieldset>
									<legend><b>Select Client Type: </b></legend>
                                    <h:selectOneRadio
                                            value="#{client.clientType}"
										    styleClass="radio-buttons" disabled="#{!client.userAuthorized}"
                                            >
										<a4j:support event="onclick"
											reRender="control-panel"
                                            action="#{client.updateClient}"
											ajaxSingle="true"/>
                                      
										<f:selectItem itemValue="AUTO" itemLabel="Auto" />
										<f:selectItem itemValue="MANUAL" itemLabel="Manual" />
									</h:selectOneRadio>
								</fieldset>


                            </richext:tab>
							
							<richext:tab label="Device Type" headingLevel="h4" rendered="#{header1.deviceTypeVisible}" >
                                <fieldset>
									<legend><b>Device Type: </b></legend>
                                    <h:panelGrid columns="3" styleClass="panel-style">
                                    	<h:outputLabel 	value=" Device Type:" for="deviceTypeInputText" styleClass="form-field"/>
                                    	<h:inputText   	id="deviceTypeInputText" 
                                    					title="Device Type"
														value="#{client.deviceType}"
                                        				label="#{client.deviceType}" readonly="#{!client.userAuthorized}"/>
										
										<table border="0">
											<tr>
												<td>
													<h5>
														<h:commandButton	value="Update Device Type" title="Update Device Type"
															type="submit"
															action="#{client.updateClientDeviceType}" rendered="#{client.userAuthorized}"
															/>	
													</h5>
												</td>
											</tr>
										</table>					
                                	</h:panelGrid>
								</fieldset>
                            </richext:tab>
                            
                             <richext:tab label="VEN Info" headingLevel="h4"  rendered="#{client.userAuthorized}" >
                                                                     
                                <rich:dataTable id="clients_endpoints" rows="20"
									value="#{client.endPoints}" var="endpoint"
									reRender="ds" styleClass="content-tab-table">
									
									<rich:column width="12%">
										<f:facet name="header">
											<h:outputText value="VEN Name "  title="Ven Name" />
										</f:facet>
										<richext:commandLink value="#{endpoint.venName}" 
											 headingLevel="h5">
										</richext:commandLink>
									</rich:column>
									<rich:column width="14%">
										<f:facet name="header">
											<h:outputText value="Ven Id " title="Ven Id" />
										</f:facet>
										<h:outputText value="#{endpoint.venId}" />
									</rich:column>
									<rich:column width="12%">
										<f:facet name="header">
											<h:outputText value="Created Time " title="Created Time" />
										</f:facet>
										<h:outputText value="#{endpoint.createdDate}" />
									</rich:column>
									<rich:column width="12%">
										<f:facet name="header">
											<h:outputText value="Cancelled " title="Is cancelled " />
										</f:facet>
										<h:outputText value="#{endpoint.cancelled}" />
									</rich:column>									
						            <f:facet name="footer">
						                <rich:datascroller id="ds"></rich:datascroller>
						            </f:facet>
								</rich:dataTable>                              
                                
                            </richext:tab>
                            
                    </rich:tabPanel>
                </richext:tab>
                
							<richext:tab label="EventStates" headingLevel="h4">
								<pre><h:outputText value="#{client.eventStatesXML}" /></pre>
							</richext:tab>

						</rich:tabPanel>
						<rich:spacer height="10px" />
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
										<a4j:commandButton id="yes" value="Yes" 
										   action="#{client.deleteContactsAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   reRender="clients_contacts"
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