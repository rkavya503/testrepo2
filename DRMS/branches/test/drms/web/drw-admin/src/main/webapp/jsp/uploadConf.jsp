<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
    <a4j:keepAlive beanName="confUploadBean" />
    <head>
    <jsp:include page="head.jsp" />
    <style>
        .top {
            vertical-align: top;
            
        }
        .info {
            height: 202px;
            overflow: auto;
        }
        .rich-messages-label {
            font-size:12px;
        }
    </style>
    </head>
    <body>
   
    <div id="frame">
        
        <h:form id="upForm">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td><jsp:include page="header.jsp"/></td>
            </tr>
        </table>
            <h:panelGrid id="upload" columns="2" columnClasses="top,top">
                
                <rich:fileUpload fileUploadListener="#{confUploadBean.listener}"
                    maxFilesQuantity="#{confUploadBean.uploadsAvailable}"
                    id="uploadItem"
                    immediateUpload="#{confUploadBean.autoUpload}"
                    acceptedTypes="csv" allowFlash="false">
                    
                    <a4j:support 
                        event="onuploadcomplete" 
                        action="#{confUploadBean.fileChoosenAction}"
                        reRender="progressPanel,msg-block"
                        />
                    
                     <f:facet name="label">
                         <h:outputText value="{_KB}KB from {KB}KB uploaded --- {mm}:{ss}" />
                     </f:facet>
                    
                </rich:fileUpload>
                <h:panelGroup id="progressPanel">     
                <rich:messages layout="table"  id="msg-block"
                    infoClass="global-message-info" warnClass="global-message-warn"
                    errorClass="global-message-error" fatalClass="global-message-fatal"/>
                                                
                </h:panelGroup>
                 <a4j:commandButton action="#{confUploadBean.clearUploadData}"
                    reRender="upload" value="Reset"
                    rendered="true" />
                
            </h:panelGrid>

            <rich:spacer height="15px" width="80px"/>
            <jsp:include page="footer.jsp" />
        </h:form>    
        </div>
    </body>
</f:view>
</html>