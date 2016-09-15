<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page contentType="text/html" pageEncoding="UTF-8"/>
	<head>
		<jsp:include page="/jsp/head.jsp" />
		<script type="text/javascript">
			 function closeWindow(){
				if(navigator.appVersion.indexOf("MSIE") != -1){
					 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
					if (version == "9" || version == "8" || version == "7") { 
						window.open('', '_self', ''); 
						window.close(); 
					} else if (version == "6") { 
						window.opener = null; 
						window.close();
					} else { 
						window.opener = ''; 
						window.close(); 
					} 
				}else{
					window.opener = null; 	
					window.close();
				}
			 }
		</script>
	</head>
    <f:view>
		<a4j:keepAlive beanName="shedEditBean" />
		
		<h:form>
		
		<rich:panel>
			
			<rich:tabPanel  width="90%" tabClass="tab-titles" styleClass="content-tab" immediate="true">
				
                    <rich:tab label="Edit Shed Value ">
						<h:messages layout="table" globalOnly="true" infoClass="global-message-info" warnClass="global-message-warn" errorClass="global-message-error" fatalClass="global-message-fatal"/>			
			
						<fieldset>
							<legend ><b>Participant Shed Types</b></legend>
							<h:selectOneMenu 	style="width:180px;"
											title="Participant Shed Types Selection"
											id="selectOneMenu_program"
											value="#{shedEditBean.participantShedType}">                     
											<f:selectItems value="#{shedEditBean.availableTypes}"/> 
											<a4j:support event="onchange" reRender="hours" actionListener="#{shedEditBean.typeListener}"/>
							</h:selectOneMenu>	
						</fieldset>
						
						<rich:spacer height="20px" width="700px" />
						<rich:dataTable id="hours" rows="24" value="#{shedEditBean.entries}" var="entry" styleClass="content-tab-table" style="width:600px;">
									<rich:column width="50%">
										<f:facet name="header">
											<h:outputText value="Time Block"  title="Time Block"/>
										</f:facet>
										<h:outputText value="#{entry.timeBockString}" style="font-weight:bold"/>
									</rich:column>
									<rich:column width="50%">
										<f:facet name="header">
											<h:outputText value="Shed Value(KW)" title="Shed Value"/>
										</f:facet>
										<h:panelGroup>						
											<h:inputText id="shedEntryValue" value="#{entry.value}" label="#{entry.value}" validatorMessage="value must >=0" title="Shed Value">
												<f:validateDoubleRange minimum="0.0"/>
											</h:inputText>
											<h:message for="shedEntryValue" errorClass="message-error" />
										</h:panelGroup>
									</rich:column>
									<!--<rich:column width="20%" rendered="#{shedEditBean.showCopyPaste}">
										<table border="0">
											<tr>
												<td>
													<h3>
														<a4j:commandButton value="Copy" title="Copy" action="#{shedEditBean.copyEntryAction}">
															<a4j:actionparam name="timeBlockString" value="#{entry.timeBockString}" assignTo="#{shedEditBean.timeBlockString}"/>
														</a4j:commandButton> 
													</h3>
												</td>
												<td>
													<h3>
														<a4j:commandButton value="Paste" title="Paste" action="#{shedEditBean.pasteEntryAction}" reRender="hours">
															<a4j:actionparam name="timeBlockString" value="#{entry.timeBockString}" assignTo="#{shedEditBean.timeBlockString}"/>
														</a4j:commandButton> 
													</h3>
												</td>
											</tr>
										</table>
									</rich:column>-->
						</rich:dataTable>
						
						<div>
							<table border="0">
										<tr>
											<td>
												<h:panelGroup  layout="block">
												<h3>
													<h:commandButton value="Save" title="Save" action="#{shedEditBean.updateAction}" />
												</h3>
												</h:panelGroup>
											</td>
											<td>
												<h:panelGroup  layout="block">
												<h3>
													<h:commandButton value="Close" title="Close" immediate="true" onclick="closeWindow();"/>
												</h3>
												</h:panelGroup>
											</td>
										</tr>
							</table>	
						</div>    
                    </rich:tab>
                    <rich:tab label="Upload Shed Value">
						<h:messages layout="table" globalOnly="true" infoClass="global-message-info" warnClass="global-message-warn" errorClass="global-message-error" fatalClass="global-message-fatal"/>	
							<h:outputText style="color:red" id="uploadFileWarningMessage" value="#{shedEditBean.uploadFileWarning}" rendered ="true"/>
							<rich:fileUpload id="uploadFileComponent"
								fileUploadListener="#{shedEditBean.uploadListener}"
								immediateUpload="true"
								autoclear="true"
								listHeight="0px" listWidth="80px"> 
								<a4j:support event="onuploadcomplete" reRender="uploadFileName,uploadFileWarningMessage,uploadFileComponent" />
							</rich:fileUpload>
							<h:panelGroup>
								<h:outputText  id="uploadFileName" value="#{shedEditBean.uploadFileName}"/>
							</h:panelGroup>
							<div>
								<table border="0">
											<tr>
												<td>
													<h:panelGroup  layout="block">
													<h3>
														<h:commandButton value="Clear" action="#{shedEditBean.clearUploadFilesAction}" immediate="true" title="Clear the upload file"/>
													</h3>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block">
													<h3>
														<h:commandButton value="Save" title="Save" action="#{shedEditBean.uploadAction}" />
													</h3>
													</h:panelGroup>
												</td>
												<td>
													<h:panelGroup  layout="block">
													<h3>
														<h:commandButton value="Close" title="Close" immediate="true" onclick="closeWindow();"/>
													</h3>
													</h:panelGroup>
												</td>
											</tr>
								</table>	
							</div>  
                    </rich:tab>
            </rich:tabPanel>
			
			
		</rich:panel>
		
		</h:form>
		
	</f:view>

</jsp:root>