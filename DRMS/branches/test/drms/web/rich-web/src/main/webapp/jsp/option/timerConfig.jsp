<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>Client Offline Configuration</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
		<jsp:include page="../header_options.jsp" />
        <h:form>
	    <a4j:keepAlive beanName="timerConfigBackingBean" />
		<rich:panel
            styleClass="about-content-panel; font:normal 9px arial"
            style="align: left">
			<rich:messages id="dispatchMessages" layout="table" globalOnly="false" 
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />
			
            <f:facet name="header">
                <h:outputText value="Client Offline Configuration" />
            </f:facet>			
			<h:panelGroup layout="block" id="client-status-block">
			<rich:tabPanel  selectedTab="reportTab" switchType="ajax" >
				<rich:tab label="Client Offline Report Configuration" id="reportTab">
					<jsp:include page="clientOfflineReportSetting.jsp" />   
				</rich:tab>
				<rich:tab label="Client Offline Notification Configuration" id="notificationTab">
					<jsp:include page="clientOfflineNotificationSetting.jsp" />
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
