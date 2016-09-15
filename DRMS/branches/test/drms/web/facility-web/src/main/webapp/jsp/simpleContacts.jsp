<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
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
					
					<rich:panel styleClass="content-panel">
						<rich:messages layout="table" for="navForm"
							infoClass="global-message-info" warnClass="global-message-warn" 
							errorClass="global-message-error" fatalClass="global-message-fatal"/>
						
                        <rich:dataTable id="clients_contacts" rows="20" width="800px"
                                    value="#{client.contacts}" var="contact"
                                    reRender="ds" styleClass="content-tab-table">
                            <rich:column width="2%">
                                <h:selectBooleanCheckbox value="#{contact.delete}" title="Select Client for Delete" />
                            </rich:column>        
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="Name"  title="Name" />
                                </f:facet>
                                <h:outputText value="#{contact.name}" />
                            </rich:column>
                            <rich:column>
                                <f:facet name="header">
                                    <h:outputText value="Email Address" title="Email Address" />
                                </f:facet>
                                <h:outputText value="#{contact.address}" />
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
											 <a4j:commandButton id="newSC" value="Add Contact" 
												action="#{client.clearTemps}"
												reRender="scModelPanel"
												oncomplete="#{rich:component('scModelPanel')}.show();"/>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton
												oncomplete="#{rich:component('panel')}.show();handleEvent();"
												value="Delete Contacts">
											</a4j:commandButton>
										</h3>
									</td>
								</tr>
							</table>
                        </div>
                        
					</rich:panel>
				</h:form>
                
                <rich:modalPanel id="panel" width="315" height="150">
                  <h:form id="deleteForm">
                  <a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
                    <rich:panel id="messages"  >
                    <a id="confirmBox" href="javascript:void(0);">
                        <h:outputText value="Are you sure you want to delete the contacts you had checked? "/>
                    </a>
                    </rich:panel>
					<table border="0">
								<tr>
									<td>
										<h3>
											 <a4j:commandButton id="yes" value="Yes" reRender="clients_contacts"
											   action="#{client.deleteContactsAction}"
											   oncomplete="#{rich:component('panel')}.hide();"/>
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('panel')}.hide();"/>
										</h3>
									</td>
								</tr>
							</table>
                  </h:form>
                </rich:modalPanel>
                
                <rich:modalPanel id="scModelPanel" width="450" autosized="true">
                    <f:facet name="header">
                        <h:outputText value="New Email Contact" />
                    </f:facet>
            
                    <h:form id="scForm">
                        <rich:panel>
                            <h:panelGrid columns="2">
                                <h:outputText value="Name" />
                                <h:inputText value="#{client.tempName}" id="tempName">
                                </h:inputText>
            
                                <h:outputText value="Email" />
                                <h:inputText value="#{client.tempEmail}" id="tempEmail" >
                                </h:inputText>
                            </h:panelGrid>
                        </rich:panel>
                        <rich:spacer height="10px" />
                        <h:panelGroup layout="block">
							<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton id="scCancel" value="Cancel"
												oncomplete="#{rich:component('scModelPanel')}.hide();" />
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton id="scSave" value="Save" 
												reRender="clients_contacts"
												oncomplete="#{rich:component('scModelPanel')}.hide();"
												action="#{client.createSimpleContactAction}" />  
										</h3>
									</td>
								</tr>
							</table>
                        </h:panelGroup>
                    </h:form>
                </rich:modalPanel>
	
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
