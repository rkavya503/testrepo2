<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<h:panelGroup layout="block">
				<h:panelGrid columns="4" width="85%" border="0">

					<h:outputText value="File Name Template: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.formattedFilename}">
						<a4j:support event="onchange" reRender="outputName" />
					</h:inputText>
					<h:outputText id="outputName"
						value="#{dbpEventDispatch.dbpConfig.filenameTemplate}" />
					<h:panelGroup />

					<h:outputText value="Backup File Path: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.backupPath}"
						title="Backup File Path" />

					<h:outputText value="Scan Start Time: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.scanStartTime}"
						label="Scan Start Time" title="Scan Start Time"
						validator="#{dbpEventDispatch.validateTime}"
						valueChangeListener="#{dbpEventDispatch.dbpConfig.valueChanged}">
						<f:attribute name="format" value="HH:mm" />
					</h:inputText>

					<h:outputText value="Scan End Time: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.scanEndTime}" id="endTime"
						validator="#{dbpEventDispatch.validateTime}"
						valueChangeListener="#{dbpEventDispatch.dbpConfig.valueChanged}">
						<f:attribute name="format" value="HH:mm" />
					</h:inputText>

					<h:outputText value="Scan Interval: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.scanInterval}"
						label="Scan Interval" title="Scan Interval" 
						valueChangeListener="#{dbpEventDispatch.dbpConfig.valueChanged}"/>

					<h:outputText value="Email Notification Required:" />
					<h:selectBooleanCheckbox title="Email Notification Required"
						value="#{dbpEventDispatch.dbpConfig.config.required}" />
				</h:panelGrid>
				
							<h:panelGrid columns="2" width="15%" border="0">
				<h:commandButton action="#{dbpEventDispatch.dbpConfig.testConnection}"
			                 value="Test Connection" />
			             <h:commandButton action="#{dbpEventDispatch.dbpConfig.save}"
			                 value="Save"/>
			</h:panelGrid>
			
				
</h:panelGroup>
