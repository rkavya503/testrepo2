<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>DBPEventDispatchConfiguration</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
        <h:form>
	    <jsp:include page="../header_options.jsp" />
		<rich:panel
            styleClass="about-content-panel; font:normal 9px arial"
            style="align: left">
			<rich:messages id="dispatchMessages" layout="table" globalOnly="false" 
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />
			<BR></BR>
            <f:facet name="header">
                <h:outputText value="Configuration Parameters" />
            </f:facet>
	    
	        <h:panelGrid columns="4" width="85%" border="0">
				<!--h:panelGroup-->
				<h:outputText value="FTP Server Host:" />		
				<h:inputText value="#{dbpEventDispatch.host}" required="true"
				requiredMessage="FTP server host is required. Please enter a valid IP address."/>
				<!--/h:panelGroup-->
				
				<h:outputText value="Port: "/>		
				<h:inputText value="#{dbpEventDispatch.port}"  required="true"
				requiredMessage="FTP server port is required. Please enter a valid port number.">
				</h:inputText>
				
				<h:outputText value="Username: "/>		
				<h:inputText value="#{dbpEventDispatch.username}"/>
				
				<h:outputText value="Password: "/>		
				<h:inputSecret value="#{dbpEventDispatch.password}" redisplay="true"/>
				
				<h:outputText value="File Name Template: "/>		
				<h:inputText value="#{dbpEventDispatch.formattedFilename}">
				<a4j:support event="onchange" reRender="outputName"/>
				</h:inputText>
				<h:outputText id="outputName" value="#{dbpEventDispatch.filenameTemplate}"/>
				<h:panelGroup/>
				
				<h:outputText value="Backup File Path: "/>		
				<h:inputText value="#{dbpEventDispatch.path}" title="Backup File Path"/>
				
				<h:outputText value="Scan Start Time: "/>		
				<h:inputText value="#{dbpEventDispatch.startTime}" 
					label="Scan Start Time" title="Scan Start Time" 
					converterMessage="Scan start time should be defined as HH:mm (HH indicates hour in day (0-23)).">
					<f:convertDateTime type="time" pattern="HH:mm"/>				
				</h:inputText>
				
				<h:outputText value="Scan End Time: "/>		
				<h:inputText value="#{dbpEventDispatch.endTime}" id="endTime" 
					label="Scan End Time" title="Scan End Time"
					converterMessage="Scan end time should be defined as HH:mm (HH indicates hour in day (0-23)).">
					<f:convertDateTime type="time" pattern="HH:mm"/>
				</h:inputText>
				
				<h:outputText value="Scan Interval: "/>		
				<h:inputText value="#{dbpEventDispatch.interval}" label="Scan Interval" title="Scan Interval"/>
				
				<h:outputText value="Email Notification Required:"/>
				<h:selectBooleanCheckbox title="Email Notification Required" value="#{dbpEventDispatch.required}"/>	
			</h:panelGrid>
	    <br/>
			<rich:separator height="1px"/>
        <br/>
	        <h:panelGrid columns="2" width="15%" border="0">
				<h:commandButton action="#{dbpEventDispatch.testConnection}"
                    value="Test Connection" />
                <h:commandButton action="#{dbpEventDispatch.save}"
                    value="Save"/>
			</h:panelGrid>
				
		</rich:panel>
	    </h:form>
        <br/>
		<jsp:include page="../footer.jsp" />		
    </body>
</f:view>
</html>
