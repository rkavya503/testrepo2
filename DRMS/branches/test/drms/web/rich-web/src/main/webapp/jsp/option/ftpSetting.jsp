<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<h:panelGroup layout="block" id="ftp-config-block">
	<fieldset>
		<legend>
			<b>FTP Server Configuration</b>
		</legend>
		<h:panelGrid columns="4" width="85%" border="0">
			<h:outputText value="FTP Server Host:" />
			<h:inputText value="#{dbpEventDispatch.host}" required="true"
				requiredMessage="FTP server host is required. Please enter a valid IP address." />

			<h:outputText value="Port: " />
			<h:inputText value="#{dbpEventDispatch.port}" required="true"
				requiredMessage="FTP server port is required. Please enter a valid port number.">
			</h:inputText>

			<h:outputText value="Username: " />
			<h:inputText value="#{dbpEventDispatch.username}" />

			<h:outputText value="Password: " />
			<h:inputSecret value="#{dbpEventDispatch.password}"
				redisplay="true" />
		</h:panelGrid>
	</fieldset>
</h:panelGroup>
