<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>Ge Interface Configuration</title>
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
			<BR></BR>
            <f:facet name="header">
                <h:outputText value="Configuration Parameters" />
            </f:facet>

		
		<h:panelGroup layout="block" id="ftp-config-block">
			<fieldset>
				<legend>
					<b>GE Interface Configuration</b>
				</legend>
				<h:panelGrid columns="4" width="85%" border="0">
					<h:outputText value="Program Name: " />
					<h:inputText value="#{geConf.programName}" required="true"
						requiredMessage="Program Name is required. Please enter a valid port number.">
					</h:inputText>
				
					<h:outputText value="Service URL:" />
					<h:inputText value="#{geConf.url}" required="true"
						requiredMessage="Service URL is required. Please enter a valid IP address." />
		
					<h:outputText value="Service NameSpace:" />
					<h:inputText value="#{geConf.nameSpace}" required="true"
						requiredMessage="Service Name Space is required. Please enter a valid IP address." />
		
		
					<h:outputText value="Service Method:" />
					<h:inputText value="#{geConf.method}" required="true"
						requiredMessage="Service Method is required. Please enter a valid IP address." />
		
					<h:outputText value="Short Interval: " />
					<h:inputText value="#{geConf.shortInterval}" />
		
					<h:outputText value="Long Interval: " />
					<h:inputText value="#{geConf.longInterval}" />
				</h:panelGrid>
			</fieldset>
		</h:panelGroup>
	   <h:commandButton action="#{geConf.save}"
			                 value="Save"/>
		</rich:panel>
	    </h:form>
        <br/>
		<jsp:include page="../footer.jsp" />		
    </body>
</f:view>
</html>
