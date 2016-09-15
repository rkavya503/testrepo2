<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<h:panelGroup layout="block">
				<h:panelGrid columns="4" width="85%" border="0">

					<h:outputText value="Event File Path: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.sourcePath}"
						title="Backup File Path" />
						
					<h:outputText value="Processed File Path: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.backupPath}"
						title="Backup File Path" />
						
					<h:outputText value="Disabled File Path: " />
					<h:inputText value="#{dbpEventDispatch.dbpConfig.config.appendixPath}"
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
						
						
					<h:outputText value="File Name Template: " />
					<h:outputText id="outputName"
						value="#{dbpEventDispatch.dbpConfig.config.filenameTemplate}" />
					<h:panelGroup />

					<h:outputText value="Automated Dispatch Programs:" />
					<h:selectBooleanCheckbox id="saiEnable" value="SAI"/>
					<h:outputLabel value="SAI" for="saiEnable" />
					<h:selectBooleanCheckbox id="dbpEnable" value="DBP DA"/>
					<h:outputLabel value="DBP DA" for="dbpEnable" />
					<h:selectBooleanCheckbox id="sdpEnable" value="SDP"/>
					<h:outputLabel value="SDP" for="sdpEnable" />
					<h:selectBooleanCheckbox id="apiEnable" value="API"/>
					<h:outputLabel value="API" for="apiEnable" />
					<h:selectBooleanCheckbox id="bipEnable" value="TOU-BIP"/>
					<h:outputLabel value="TOU-BIP" for="bipEnable" />
					<h:selectBooleanCheckbox id="sdpEnable" value="SDP"/>
					<h:outputLabel value="SDP" for="sdpEnable" />
					
				</h:panelGrid>
				
							<h:panelGrid columns="2" width="15%" border="0">
				<h:commandButton action="#{dbpEventDispatch.dbpConfig.testConnection}"
			                 value="Test Connection" />
			             <h:commandButton action="#{dbpEventDispatch.dbpConfig.save}"
			                 value="Save"/>
			</h:panelGrid>
			
				
</h:panelGroup>
