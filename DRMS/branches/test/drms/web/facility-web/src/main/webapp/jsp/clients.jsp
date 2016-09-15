<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.facdash.*" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US" >
<jsp:include page="clear-session.jsp" />
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
			<script type="text/javascript">
        		function handleEvent(){
        			document.getElementById("dialogTitle").focus();
        		}
        	</script>
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
				
            <rich:panel styleClass="content-panel" id="clientsPanel">
            <a4j:status id="waitStatus" forceId="true"
						onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
						onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
						
				<a4j:region>
                    <a4j:keepAlive beanName="clients"/>					
                <rich:messages layout="table" globalOnly="true"
                    infoClass="global-message-info" warnClass="global-message-warn"
                    errorClass="global-message-error" fatalClass="global-message-fatal"/>

            <rich:dataTable id="clients" rows="20"
                value="#{clients.clients}" var="client"
                reRender="ds" styleClass="content-table" >
                <rich:column width="2%">
                	<h:selectBooleanCheckbox value="#{client.delete}" disabled="#{clients.deleteChecked}"/>
                </rich:column>
                <rich:column width="14%" sortBy="#{client.name}">
                    <f:facet name="header">
                        <h:outputText value="Name" title="Name" />
                    </f:facet>
                    <richext:commandLink value="#{client.name}"
                        action="#{clients.editClientAction}"
                        actionListener="#{clients.editClientListener}"
                        headingLevel="h3">
                        <f:param name="clientName" value="#{client.name}"/>
                    </richext:commandLink>
                </rich:column>
                <rich:column width="14%" sortBy="#{client.eventsString}">
                    <f:facet name="header">
                        <h:outputText value="Events" title="Events" />
                    </f:facet>
                    <h:outputText value="#{client.eventsString}"
                        title="#{client.eventsString}" />
                </rich:column>
                <rich:column width="14%" sortBy="#{client.programsString}">
                    <f:facet name="header">
                        <h:outputText value="Programs" title="Programs"/>
                    </f:facet>
                    <h:outputText value="#{client.programsString}"
                        title="#{client.programsString}" />
                </rich:column>
                <rich:column id="pending"  width="14%" sortBy="#{client.eventStatus}">
                    <f:facet name="header">
                        <h:outputText value="Pending" title="Pending"/>
                    </f:facet>
                    <h:outputText value="#{client.eventStatus}" />
                </rich:column>
                <rich:column width="14%" sortBy="#{client.mode}">
                    <f:facet name="header">
                        <h:outputText value="Mode" title="Mode"/>
                    </f:facet>
                    <h:outputText value="#{client.mode}" />
                </rich:column>
                <rich:column width="14%" sortBy="#{client.lastContact}">
                    <f:facet name="header">
                        <h:outputText value="Last Contact" title="Last Contact"/>
                    </f:facet>
	                <h:outputText value="#{client.lastContact}">
 	 	 		 	    <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
 	 	 		 	</h:outputText>
                </rich:column>
                <rich:column width="14%" sortBy="#{client.commStatus}">
                    <f:facet name="header">
                        <h:outputText value="Comm Status" title="Comm Status"/>
                    </f:facet>
                    <h:outputText value="#{client.commStatus}" />
                </rich:column>
                
                
                <rich:column width="14%" sortBy="#{client.lastUsageContact}" rendered="#{clients.dataUsageEnabled}">
                    <f:facet name="header">
                        <h:outputText value="Obix Last Contact" title="Obix Last Contact"/>
                    </f:facet>
                    <h:outputText value="#{client.lastUsageContact}">
                        <f:convertDateTime pattern="#{applicationScope.shortDateTimeFormat}"/>
                    </h:outputText>
                </rich:column>
                <rich:column width="14%" sortBy="#{client.usageCommStatus}" rendered="#{clients.dataUsageEnabled}">
                    <f:facet name="header">
                        <h:outputText value="Obix Comm Status" title="Comm Status"/>
                    </f:facet>
                    <h:outputText value="#{client.usageCommStatus}" />
                </rich:column>
                
                             <%--
                             <rich:column width="25%" >
                                    <f:facet name="header">
                                            <h:outputText value="Actions"  title="Actions"/>
                                    </f:facet>
                                    <div>
                                        <rich:messages
                                            style="text-align:right;font-size: 15px; "
                                            layout="table" globalOnly="true"
                                            infoClass="global-message-info" warnClass="global-message-warn"
                                            errorClass="global-message-error" fatalClass="global-message-fatal"
                                        />
                                    </div>
                                    <h:selectOneRadio
                                        id="optStatus"
                                        value="#{client.optStatus}"
                                        styleClass="radio-buttons"
                                       >
                                       <a4j:support
                                           action="#{client.updateOptControl}"
                                                    event="onclick"
                                                    reRender="c-panel"
                                                    ajaxSingle="true"
                                        >
                                       </a4j:support>
                                       <f:selectItems  value="#{client.optValues}" />
                                    </h:selectOneRadio>
                             </rich:column>
                                    --%>

				            <f:facet name="footer">
				                <rich:datascroller id="ds"></rich:datascroller>
				            </f:facet>
						</rich:dataTable>
                     
						
						<rich:spacer height="2px" />
		
						<div id="fm-submit">
							<table border="0">
							<tr>
								<td>
									<h3>
										<h:commandButton title="New Client" value="New Client" action="#{clients.newClientAction}" disabled="#{clients.addClient}" />
									</h3>
								</td>
								<td>
									<h3>
										<a4j:commandButton
											oncomplete="#{rich:component('panel')}.show();handleEvent();" reRender="panel"
											value="Delete Clients"
											title="Delete Clients" disabled="#{clients.deleteClinet}">
										</a4j:commandButton>						
									</h3>
								</td>
							</tr>
							</table>
						</div>
						</a4j:region>
					</rich:panel>						
					<rich:modalPanel id="panel" autosized="true"
	                    keepVisualState="false"
	                    width="315"
	                    height="150"
	                    >
	                    <div id="deleteForm">
	                    
             			<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
	            		<rich:panel id="messages"  >
	            			<a id="confirmBox" href="javascript:void(0);">
	                			<h:outputText value="Are you sure you want to delete the clients you had checked? "/>
	                		</a>
	            		</rich:panel>
	            		
	            		
						
							<table border="0">
							<tr>
								<td>
									<h3>
										<a4j:commandButton id="yes" value="Yes" reRender="clients"
										   action="#{clients.deleteClientsAction}"
										   oncomplete="#{rich:component('panel')}.hide();"
										   title="Delete Clients"/>
									</h3>
								</td>
								<td>
									<h3>
										<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('panel')}.hide();" title="Cancel Clients"/>					
									</h3>
								</td>
							</tr>
							</table>
						
						
	            		</div>
	 				</rich:modalPanel>
				</h:form>
				
				<rich:modalPanel id="waitModalPanel" autosized="true" width="200"
					height="120" moveable="false" resizeable="false">
					<f:facet name="header">
						<h:outputText value="Processing" />
					</f:facet>
					<h:outputText value="Please wait..." />
					<center>
						<h:graphicImage value="/images/ajax-loader.gif" />
					</center>
				</rich:modalPanel>
			<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
