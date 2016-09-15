<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>SCE FTP Configuration</title>
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

			<jsp:include page="ftpSetting.jsp" />
			
			<h:panelGroup layout="block" id="client-status-block">
			<rich:tabPanel  selectedTab="DBPEventConfigurationTab" switchType="ajax" >
				<rich:tab label="DBP Event Configuration" id="DBPEventConfigurationTab">
					<jsp:include page="dbpSetting.jsp" />   
				</rich:tab>
				<rich:tab label="Participant Information Upload Configuration" id="ParticipantConfigurationTab" rendered="#{dbpEventDispatch.partConfig.participantsUploadFlag}">
					<jsp:include page="participantSetting.jsp" />
				</rich:tab>
			</rich:tabPanel>
			
			</h:panelGroup>
	
		</rich:panel>
	    </h:form>
        <br/>
		<jsp:include page="../footer.jsp" />		
    </body>
</f:view>
</html>
