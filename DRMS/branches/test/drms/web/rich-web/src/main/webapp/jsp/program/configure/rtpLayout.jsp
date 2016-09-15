<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<div>
		<!--Warning Message Display Zoon. When using rich fileupload component that the message/messages component is not usable correctly.-->
		<div class="programInfoSubTitle">
          <a4j:outputPanel  ajaxRendered="true" id="errorMessage">
			<h:outputText 
				id="warningMessageText" 
				style="color:red"
				rendered="#{programConfigureDataModel.rtpConfigureDataModel.rtpNotExistWarnFlag}"
				value="#{programConfigureDataModel.rtpConfigureDataModel.warningMessage}" />
          </a4j:outputPanel>
		</div>
						
		<!-- Table Content -->	
		<rich:dataTable id="rtpDataTable" 
                        value="#{programConfigureDataModel.rtpConfigureDataModel.lines}" 
                        var="item" 
                        width="100%" styleClass="event-rich-table ">
            <f:facet name="header">
               <h:outputText value="Configure RTP for '#{programConfigureDataModel.programName}'"></h:outputText>
            </f:facet>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="HOUR ENDING@PST" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.endTimeString}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header0}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[0]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header1}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[1]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header2}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[2]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header3}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[3]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header4}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[4]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header5}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[5]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header6}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[6]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header7}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[7]}" />
			</rich:column>
			<rich:column sortable="false" >
				<f:facet name="header">
					<h:outputText value="#{programConfigureDataModel.rtpConfigureDataModel.header8}" escape="false"/>
				</f:facet>				
				<h:outputText value="#{item.values[8]}" />
			</rich:column>
		</rich:dataTable>
		
        <rich:fileUpload
            id="upload"
            fileUploadListener="#{programConfigureDataModel.rtpConfigureDataModel.listener}"
            immediateUpload="true"
            maxFilesQuantity="10"
            autoclear="true"
            acceptedTypes="csv"
            listHeight="0px" listWidth="0px" disabled="#{!ProgramDataModel.programRtpEnabled}"> 
            <a4j:support event="onuploadcomplete" reRender="rtpDataTable,warningMessageText,errorMessage" />
        </rich:fileUpload>
</div>
