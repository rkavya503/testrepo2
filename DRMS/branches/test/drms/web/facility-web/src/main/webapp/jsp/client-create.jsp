<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.ejb.client.EJB3Factory" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%
	SystemManager systemManager = (SystemManager)EJB3Factory.getBean(SystemManager.class);
	boolean supportNonAutoClient = systemManager.getPss2Features().isNonAutoClientEnabled();
	
%>

<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
	
					<rich:panel>
						<f:facet name="header">
							<h:outputText value="Create Client" />
						</f:facet>

						<h:messages layout="table" globalOnly="true" 
							infoClass="global-message-info" warnClass="global-message-warn" 
							errorClass="global-message-error" fatalClass="global-message-fatal"/>
						<div>
							<h:outputLabel value="(" styleClass="form-field"/>
							<h:graphicImage alt="*" title="*"  url="/images/layout/required.jpg" />
							<h:outputLabel value="denotes a required field)" styleClass="form-field"/>
						</div>	
						<h:panelGrid columns="3" styleClass="panel-style" style="position:relative;">
							
							<%
                           if(supportNonAutoClient)
                           {
                            %>
							<h:outputText value=""/>
							<h:outputLabel for="selectOneMenu_clientType" value="Type:" style="font-weight:bold"/>
							<h:selectOneMenu id="selectOneMenu_clientType" value="#{client.clientType}">
								<f:selectItem itemValue="AUTO" itemLabel="AUTO" />
								<f:selectItem itemValue="MANUAL" itemLabel="MANUAL" />
							</h:selectOneMenu>
							
                            <%
                            }    
                            %>
							
							
							<h:message for="name" errorClass="message-error" style="position:absolute; left:310px;width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredName" alt="required" title="required"  url="/images/layout/required.jpg" />
								<h:outputLabel for="name" value="Name:" styleClass="form-field"/>
								<h:outputText value="#{header1.participantName}." title="#{header1.participantName}." style="float:right"/>
							</h:panelGroup>
							<h:panelGroup>
								
								
								<h:inputText 
									id="name" 
									value="#{client.name}" 
									label="#{client.name}" 
									required="true"
									requiredMessage="Name is required"
									title="Client Name"/>
							</h:panelGroup>
							
							<h:message for="password" errorClass="message-error"  styleClass="password_Message" style="position:absolute; left:310px; width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredPassword" alt="required" title="required" url="/images/layout/required.jpg" />
								<h:outputLabel for="password" value="Password:" 
									styleClass="form-field"/>
							</h:panelGroup>
							<h:inputSecret id="password" 
								value="#{client.password}"
								label="#{client.password}" 
								required="true"
								requiredMessage="Password is required"
								title="Password"/>
							

							<h:message for="confirm" errorClass="message-error"  styleClass="confirm_Message" style="position:absolute; left:310px;width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredConfirm" alt="required" title="required" url="/images/layout/required.jpg" />
								<h:outputLabel for="confirm" value="Confirm Password:" 
									styleClass="form-field"/>
							</h:panelGroup>
							<h:inputSecret 
								id="confirm"
								value="#{client.confirmPassword}"
								label="#{client.confirmPassword}"
								required="true"  
								requiredMessage="Confirm New Password is required" 
								title="Confirm Password"/>
							
							<h:outputText/>
							<h:panelGroup rendered="#{header1.deviceTypeVisible}">
								<h:outputLabel for="deviceTypeInputText" value="Device Type:" styleClass="form-field"/>
							</h:panelGroup>
							<h:panelGroup rendered="#{header1.deviceTypeVisible}">
								<h:inputText 
									id="deviceTypeInputText" 
									value="#{client.deviceType}" 
									label="#{client.deviceType}"
									title="Device Type"/>
							</h:panelGroup>
							<h:outputText/>
							<h:panelGroup rendered="#{header1.deviceTypeVisible}">
								<h:outputLabel for="venList" value="VEN ID's:" styleClass="form-field"/>
							</h:panelGroup>							
							<h:panelGroup rendered="#{header1.deviceTypeVisible}">		
							<a4j:region>					
							<h:selectOneMenu id="venList" value="#{client.endpoints.venId}" title="Select Ven for 2b profile mapping">                     
									<f:selectItems value="#{client.endpoints.vens}"/>									
									<a4j:support event="onchange" reRender="venDetails"  action="#{client.endpoints.changeVenID}"/>
							</h:selectOneMenu>																
							</a4j:region>							
							<a4j:outputPanel id="venDetails">
								<h:outputText/>	
								<h:panelGroup>
								<h:outputLabel for="venNameLable" value="VEN Name:"  styleClass="form-field"/>
								</h:panelGroup>	
								<h:inputText id="vName" label="venNameLable" value="#{client.endpoints.venName}" readonly="true"/>
								<h:outputText/>
								<h:panelGroup>
								<h:outputLabel id="cDateLable" value="Created Date:" styleClass="form-field"/>							
								<h:inputText id="vcDate" label="cDateLable" value="#{client.endpoints.createdDate}" readonly="true"/>
								</h:panelGroup>
							</a4j:outputPanel>		
							</h:panelGroup>				
													
						</h:panelGrid>
	
						<div id="fm-submit">
							<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton value="Create Client" title="Create Client" action="#{clients.createClientAction}" />
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton value="Cancel" title="Cancel" 
												action="#{clients.cancelClientAction}" 
												immediate="true" />
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
