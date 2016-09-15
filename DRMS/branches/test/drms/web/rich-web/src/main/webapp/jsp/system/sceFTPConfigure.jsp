<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<style>
.configurationLabel {
	font-family: verdana, arial, sans-serif;
	font-size: .8em;
}
.configurationTextInput {
	font-family: verdana, arial, sans-serif;
	font-size: .8em;
}
.configurationBackground {
	background-color: #ececec;
	font-size: .7em;
	
}
</style>
<f:view>
    <head>
    <title>RTP Temperature File FTP Configuration</title>

	<a4j:keepAlive beanName="SCEFTPConfigureBean" />
    
    <jsp:include page="../head.jsp" />
    </head>
    <body>
	<jsp:include page="../header_options.jsp" />
    <h:form>
        
       
		<rich:panel styleClass="about-content-panel; font:normal 9px arial" style="align: left">
           
		<rich:messages layout="table" globalOnly="false"
		               infoClass="global-message-info" warnClass="global-message-warn"
		               errorClass="global-message-error" fatalClass="global-message-fatal" />
		<BR></BR>
		   
            <f:facet name="header">
                <h:outputText value="RTP Temperature File FTP Configuration" />
            </f:facet>

				<table width="85%">

                            <rich:tabPanel
                                width="60%"
                                activeTabClass="userConfig"
                                selectedTab="userConfig"
                                tabClass="tab-titles"
                                styleClass="configurationBackground"
                                immediate="true"
                                switchType="client"
                                >
                           <rich:tab id="userConfig" 
									 reRender="false"
                                     label="User Info " 
									
									 >

                                         <tr>
                                            <td>
                                                <h:outputText value="Scan Start Time" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:inputText title="Scan Start Time" id="starttimeInputText" value="#{SCEFTPConfigureBean.startTime}" styleClass="configurationTextInput"/>
                                            </td>
                                            <td>
                                                <h:outputText value="Scan End Time" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:inputText title="Scan End Time" id="endtimeInputText" value="#{SCEFTPConfigureBean.endTime}" styleClass="configurationTextInput"/>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>
                                                <h:outputText value="Scan Interval" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:inputText title="Scan Interval" id="intervalInputText" value="#{SCEFTPConfigureBean.interval}" styleClass="configurationTextInput"/> &nbsp;&nbsp;minutes
                                            </td>
                                            <td>
                                                <h:outputText value="Email Notification Required" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:selectBooleanCheckbox id="RequiredCheckbox" title="Notification Required" value="#{SCEFTPConfigureBean.required}"/>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td>
                                                <h:outputText value="Min(F)" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:inputText title="Min Temperature" id="minInputText" value="#{SCEFTPConfigureBean.minTemperature}" styleClass="configurationTextInput"/>
                                            </td>
                                            <td>
                                                <h:outputText value="Max(F)" styleClass="configurationLabel"/>
                                            </td>
                                            <td>
                                                <h:inputText title="Max Temperature" id="maxInputText" value="#{SCEFTPConfigureBean.maxTemperature}" styleClass="configurationTextInput"/>
                                            </td>
                                        </tr>

						  </rich:tab>

                          <rich:tab rendered="#{footer.admin}"  id="ftpConfig" reRender="false"
                                     label="FTP Info">
                                 	<tr>
                                        <td width="15%">
                                            <h:outputText value="FTP URL" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputText title="FTP URL" id="ftpURLInputText" value="#{SCEFTPConfigureBean.url}" styleClass="configurationTextInput"/>
                                        </td>
                                        <td width="15%">
                                            <h:outputText value="Port" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputText title="Port" id="portInputText" value="#{SCEFTPConfigureBean.port}" styleClass="configurationTextInput"/>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td width="15%">
                                            <h:outputText value="Path" title="Path for backup processed RTP temperature file" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputText title="Path" id="pathInputText" alt="Path for backup processed RTP temperature file" value="#{SCEFTPConfigureBean.path}" styleClass="configurationTextInput"/>
                                        </td>
                                        <td>
                                            <h:outputText value="File Name" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputText title="File Name" id="filenameInputText" value="#{SCEFTPConfigureBean.fileName}" styleClass="configurationTextInput"/> yyyyMMdd_RTPprices.txt
                                        </td>
                                    </tr>
                                    <tr>
                                        <td>
                                            <h:outputText value="User Name" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputText title="User Name" id="usernameInputText" value="#{SCEFTPConfigureBean.userName}" styleClass="configurationTextInput"/>
                                        </td>
                                        <td>
                                            <h:outputText value="Password" styleClass="configurationLabel"/>
                                        </td>
                                        <td>
                                            <h:inputSecret title="password" id="passwordInputText" value="#{SCEFTPConfigureBean.password}" styleClass="configurationTextInput" redisplay="true" />
                                        </td>
                                    </tr>
						  </rich:tab>
                  </rich:tabPanel>

				
			</table>
							
			<fieldset style="width:100%;align: center">
				<legend><b>Actions</b></legend>
				<a4j:commandButton rendered="#{footer.admin}" value="  Test Connection  " action="#{SCEFTPConfigureBean.testFTPSettings}" reRender="starttimeInputText, endtimeInputText"/>&nbsp; &nbsp;
				<a4j:commandButton value="  Save  " action="#{SCEFTPConfigureBean.saveFTPSettings}" reRender="starttimeInputText, endtimeInputText"/>
			</fieldset>
			
        </rich:panel>
	
        <br />
        <br />
        <br />
        <jsp:include page="../footer.jsp" />
    </h:form>
    </body>
</f:view>
</html>