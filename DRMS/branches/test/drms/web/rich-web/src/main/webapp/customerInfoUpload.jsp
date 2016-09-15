<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<head>
	<jsp:include page="/jsp/head.jsp" />
</head>

<body>
<f:view>
	<jsp:include page="/jsp/header_options.jsp" />
	<h:form id="form">
		
		
		<rich:spacer height="5px" width="400px"/>
		
		<rich:panel style="background-color: #FFFFFF;border-color: #BED6F8;" id ="content-panel" >
			
			<rich:panel style="background-color: #ECF4FE;border-color: #BED6F8;" >Participants Information Upload</rich:panel>
            
			<rich:spacer height="20px" width="400px" />
			
			
			
			<h:panelGroup id="gridPanel">
				<table width="700px" cellSpacing="0" cellPadding="0" >
					<tr>
						<td>
							<h:outputText id="messageText" value="#{participantInfoFileUploadBean.message}" style="width:500px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: bold;"/>
						</td>
					</tr>
					<tr>
						<td width="100%">
							<rich:fileUpload
								addControlLabel="Choose file..."
								fileUploadListener="#{participantInfoFileUploadBean.listener}"
								maxFilesQuantity="#{participantInfoFileUploadBean.uploadsAvailable}"
								id="importFileComponent" 
								immediateUpload="true"
								acceptedTypes="csv,xls" 
								allowFlash="true"
								autoclear="true"
								listHeight="80px" 
								listWidth="650px"
								stopButtonClass="display: none;"
								stopButtonClassDisabled = "display: none;">
								
								
								<a4j:support event="onupload" action="#{participantInfoFileUploadBean.uploadAction}" reRender="messageText"/>
								<a4j:support event="onuploadcomplete" action="#{participantInfoFileUploadBean.uploadCompleteAction}" reRender="messageText"/>
								
								<f:facet name="label">
									<h:outputText value="{_KB}KB from {KB}KB uploaded --- {mm}:{ss}" />
								</f:facet>
							</rich:fileUpload>
						</td>
					</tr>
					
				</table>
				
				
				<rich:spacer height="10px" width="100%" />
				
				<h:panelGroup id="importModalButtons">
						<a4j:commandButton 
							id="clearButton" 
							value="Clear" 
							style="text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
							action="#{participantInfoFileUploadBean.clear}" reRender="messageText,importFileComponent"/>
				</h:panelGroup>				
				<rich:spacer height="20px" width="100%" />				
				<rich:separator height="1" lineType="solid"/><br/>
				

				
				
				
				
			</h:panelGroup>
			
        </rich:panel>
    </h:form>
	
	<jsp:include page="/jsp/footer.jsp" />
</f:view>
</body>
</jsp:root>
