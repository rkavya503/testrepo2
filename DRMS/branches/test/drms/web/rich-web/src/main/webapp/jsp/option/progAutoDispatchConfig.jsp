<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>Program Auto Dispatch Configuration</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
		<jsp:include page="../header_options.jsp" />
		 <h:form>
		<rich:panel
            styleClass="about-content-panel; font:normal 9px arial"
            style="align: left">
			<rich:messages id="dispatchMessages" layout="table" globalOnly="false" 
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />
			
            <f:facet name="header">
                <h:outputText value="Program Auto Dispatch Configuration" />
            </f:facet>			
			<h:panelGroup layout="block">
				<fieldset>
					<legend><b>Program Auto Dispatch FTP Configuration:</b></legend>			
						<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
							<tr>
								<td><h:outputText value="FTP Server Host:"/></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.host}" required="true"
											requiredMessage="FTP server host is required. Please enter a valid IP address." />
								</td>
								<td><h:outputText value="FTP Server Port:"/></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.port}" required="true"
											requiredMessage="FTP server port is required. Please enter a valid port number." />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="User Name:"/></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.username}" />
								</td>
								<td><h:outputText value="Password:"/></td>
								<td>
									<h:inputSecret value="#{progAutoDispDispatch.config.password}" redisplay="true" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Event File Path: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.sourcePath}" title="Backup File Path" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Processed File Path: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.backupPath}" title="Processed File Path" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Disabled File Path: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.appendixPath}" title="Disabled File Path" />
								</td>
							</tr>
						</table>
				</fieldset>
				
				<fieldset>
					<legend><b>Program Auto Dispatch Event Configuration:</b></legend>			
						<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
							<tr>
								<td><h:outputText value="File Name Template: " /></td>
								<td>
									<h:outputText id="outputName" value="#{progAutoDispDispatch.config.filenameTemplate}" />
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Event Scan Start Time: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.scanStartTime}"
										label="Scan Start Time" title="Scan Start Time"
										validator="#{progAutoDispDispatch.validateTime}"
										valueChangeListener="#{progAutoDispDispatch.valueChanged}">
										<f:attribute name="format" value="HH:mm" />
									</h:inputText>
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Event Scan End Time: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.scanEndTime}" id="endTime"
										validator="#{progAutoDispDispatch.validateTime}"
										valueChangeListener="#{progAutoDispDispatch.valueChanged}">
										<f:attribute name="format" value="HH:mm" />
									</h:inputText>
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Event Scan Interval: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.scanInterval}"
										label="Scan Interval" title="Scan Interval" 
										valueChangeListener="#{progAutoDispDispatch.valueChanged}"/>
								</td>
							</tr>
							<tr>
								<td><h:outputText value="Event Delay Interval: " /></td>
								<td>
									<h:inputText value="#{progAutoDispDispatch.config.delayInterval}"
										label="Delay Interval" title="Delay Interval" 
										valueChangeListener="#{progAutoDispDispatch.valueChanged}"/>
								</td>
							</tr>
						</table>
						<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
							<tr>
								<td><h:outputText value="SAI: " /><h:selectBooleanCheckbox id="saiEnable" value="#{progAutoDispDispatch.enableSAI}"/></td>
								<td><h:outputText value="DBP: " /><h:selectBooleanCheckbox id="dbpEnable" value="#{progAutoDispDispatch.enableDBP}"/></td>
								<td><h:outputText value="SPD: " /><h:selectBooleanCheckbox id="spdEnable" value="#{progAutoDispDispatch.enableSPD}"/></td>
								<td><h:outputText value="API: " /><h:selectBooleanCheckbox id="apiEnable" value="#{progAutoDispDispatch.enableAPI}"/></td>
								<td><h:outputText value="BIP: " /><h:selectBooleanCheckbox id="bipEnable" value="#{progAutoDispDispatch.enableBIP}"/></td>
								<td><h:outputText value="SDP: " /><h:selectBooleanCheckbox id="sdpEnable" value="#{progAutoDispDispatch.enableSDP}"/></td>
							</tr>
						</table>
				</fieldset>
				<h:commandButton action="#{progAutoDispDispatch.testConnection}" value="Test Connection" />
				<h:commandButton action="#{progAutoDispDispatch.save}" value="Save"/>
			</h:panelGroup>
		</rich:panel>
	    </h:form>
        <br/>
		<jsp:include page="../footer.jsp" />		
    </body>
</f:view>
</html>
