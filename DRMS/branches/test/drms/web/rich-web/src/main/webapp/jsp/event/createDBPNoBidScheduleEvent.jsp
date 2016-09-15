<?xml version="1.0" encoding="UTF-8" ?>
<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<head>
	<jsp:include page="/jsp/head.jsp" />
</head>

<f:view>
	<jsp:include page="/jsp/header_events.jsp" />
	<h:form id="form">
		<!-- When this page initialize invoke the JSF backing bean model refresh -->
		<div style="display:none;">
			<h:commandButton id="renewModelButton" value="renew" onclick="#{dbpNoBidEventDataModel.renew}"/>
			<script type="text/javascript">
				var relativedComponent = document.getElementById("form:renewModelButton");
				if(relativedComponent){
					relativedComponent.onclick();
				}
			</script>
		</div>
		
		<!-- Validation Message Component -->
		<rich:spacer height="5px" width="700px" />
		<h:messages layout="table" globalOnly="true" 
			infoClass="global-message-info" warnClass="global-message-warn" 
			errorClass="global-message-error" fatalClass="global-message-fatal"/>	
							
		<rich:spacer height="5px" width="700px" />
		<!-- Event Basic Information Component -->
		<rich:panel styleClass="event-rich-panel">
			Program DBP
		</rich:panel>
		
		<h:panelGrid columns="3" styleClass="event-panel-grid">
			<h:panelGroup>
				<p align="right">Program Type:</p>
			</h:panelGroup>
			<h:panelGroup>
				<p align="center"><h:outputText value="DBP DA"/></p>
			</h:panelGroup>
			<h:panelGroup/>	
		
			<h:panelGroup>
				<p align="right">Bid File:</p>
			</h:panelGroup>
			<h:panelGroup>
				<table>
					<tr>
						<td>
							<rich:fileUpload
								id="uploadFileComponent"
								fileUploadListener="#{dbpNoBidEventDataModel.listener}"
								
								immediateUpload="true"
								autoclear="true"
								
								listHeight="0px" listWidth="80px"> 
								<a4j:support event="onuploadcomplete" reRender="uploadFileName,uploadFileWarningMessage,uploadFileComponent" />
							</rich:fileUpload>
						</td>
						<td><h:commandButton value="Clear" action="#{dbpNoBidEventDataModel.clearUploadFiles}" immediate="true" title="Clear the upload file"/></td>
					</tr>
				</table>
	
			</h:panelGroup>
			<h:panelGroup>
				<h:outputText style="color:red" id="uploadFileWarningMessage" value="#{dbpNoBidEventDataModel.uploadFileWarning}" rendered ="true"/>
				<h:outputText  id="uploadFileName" value="#{dbpNoBidEventDataModel.uploadFileName}"/>
			</h:panelGroup>
			<h:panelGroup/>	
			
			
			<h:panelGroup>
				<h:outputText  value="* Please upload csv file." />
			</h:panelGroup>
		
			
		</h:panelGrid>
	
	
		
		<rich:spacer height="30px" width="700px" />

		
		<div style="text-align: center;">        
			<h:commandButton value="Confirm" action="#{dbpNoBidEventDataModel.confirm}" title="Submit and Confirm Event"/>
			<h:commandButton value="Cancel" action="#{dbpNoBidEventDataModel.cancel}" immediate="true" title="Cancel Submit and Confirm Event"/>
		</div>

	</h:form>
   <jsp:include page="/jsp/footer.jsp" />
</f:view>
</jsp:root>