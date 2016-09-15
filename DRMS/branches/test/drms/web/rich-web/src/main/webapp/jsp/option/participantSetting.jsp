<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<h:panelGroup layout="block">
				<h:panelGrid columns="4" width="85%" border="0">

					<h:outputText value="File Name Template: " />
					<h:inputText value="#{dbpEventDispatch.partConfig.config.formattedFilename}">
						<a4j:support event="onchange" reRender="outputNameParticipant" />
					</h:inputText>
					<h:outputText id="outputNameParticipant"
						value="#{dbpEventDispatch.partConfig.filenameTemplate}" />
					<h:panelGroup />

					<h:outputText value="Backup File Path: " />
					<h:inputText value="#{dbpEventDispatch.partConfig.config.backupPath}"
						title="Backup File Path" />

					<h:outputText value="Scan Start Time: " />
					<h:inputText value="#{dbpEventDispatch.partConfig.config.scanStartTime}"
						label="Scan Start Time" title="Scan Start Time"
						validator="#{dbpEventDispatch.validateTime}"
						valueChangeListener="#{dbpEventDispatch.partConfig.valueChanged}">
						<f:attribute name="format" value="HH:mm" />
					</h:inputText>

					<h:outputText value="Scan End Time: " />
					<h:inputText value="#{dbpEventDispatch.partConfig.config.scanEndTime}"
						label="Scan End Time" title="Scan End Time"
						validator="#{dbpEventDispatch.validateTime}"
						valueChangeListener="#{dbpEventDispatch.partConfig.valueChanged}">
						<f:attribute name="format" value="HH:mm" />
					</h:inputText>

					<h:outputText value="Scan Interval: " />
					<h:inputText value="#{dbpEventDispatch.partConfig.config.scanInterval}"
						label="Scan Interval" title="Scan Interval"
						valueChangeListener="#{dbpEventDispatch.partConfig.valueChanged}"/>

					<h:outputText value="Email Notification Required:" />
					<h:selectBooleanCheckbox title="Email Notification Required"
						value="#{dbpEventDispatch.partConfig.config.required}" />
				</h:panelGrid>
				
							<h:panelGrid columns="2" width="15%" border="0">
				<h:commandButton action="#{dbpEventDispatch.partConfig.testConnection}"
			                 value="Test Connection" />
			             <h:commandButton action="#{dbpEventDispatch.partConfig.save}"
			                 value="Save"/>
			</h:panelGrid>
</h:panelGroup>