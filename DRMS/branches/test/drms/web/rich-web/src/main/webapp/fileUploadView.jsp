<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:richext="http://akuacom.com/richext"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1"
    >

	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />

        <h:form id="importModalForm">

           <a4j:region renderRegionOnly="true">

			<h:panelGrid columns="1">
				<rich:fileUpload
					addControlLabel="Choose file..."
					fileUploadListener="#{participantImport.fileUploadListener}"
					maxFilesQuantity="1" 
					id="importModalFile" 
					disabled="#{participantImport.fileUploadDisabled}"
					immediateUpload="true"
					acceptedTypes="csv" 
					allowFlash="true"
					autoclear="true"
					listHeight="1px" 
					listWidth="475px"
					stopButtonClass="display: none;"
					stopButtonClassDisabled = "display: none;"
					rendered="#{participantImport.fileUploadEnabled}">
					<a4j:support 
						event="onuploadcomplete" 
						action="#{participantImport.fileChoosenAction}"
						reRender="importModalButtons"
                        />
				</rich:fileUpload>
				<rich:progressBar 
					id="importModalProgress"
					label="#{participantImport.pass}"
					value="#{participantImport.lineNum}"  
					minValue="1"  
					maxValue="#{participantImport.totalLineNum}"
					interval="667"
					reRenderAfterComplete="importModalProgress,importModalErrors,importModalButtons">
				    <f:facet name="initial">
						<h:outputText value="" />
				    </f:facet>
				    <f:facet name="complete">
						<h:outputText value="upload complete" />
				    </f:facet>
					<a4j:support 
						event="oncomplete" 
						reRender="importModalButtons"/>
				</rich:progressBar>
				<rich:extendedDataTable id="importModalErrors"
					width="475px" 
					height="195px"
					value="#{participantImport.fileUploadOutput}" 
					var="error">
					<rich:column width="476px">
						<h:outputText value="#{error}"></h:outputText>
					</rich:column>
				</rich:extendedDataTable>
				<h:panelGroup id="importModalButtons">
					<a4j:commandButton
						disabled="#{participantImport.importModalCancel1}" 
						id="importModalCancel1" 
						value="Cancel" 
						rendered="#{participantImport.renderCancel1}"
						/>
					<a4j:commandButton 
						id="importModalCancel2" 
						value="Cancel" 
						rendered="#{!participantImport.renderCancel1}"
						action="#{participantImport.fileUploadCancelAction}"
						disabled="#{!participantImport.uploadInProgress}"/>
					<a4j:commandButton 
						id="importModalDone" 
						value="Done" 
						reRender="participants"
						disabled="#{participantImport.uploadInProgress}"
						action="#{participantImport.fileUploadDoneAction}"
						/>
				</h:panelGroup>
			</h:panelGrid>
    
        </a4j:region>
		</h:form>

</jsp:root>