<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
    <a4j:keepAlive beanName="fileUploadBean" />
    <head>
    <jsp:include page="head.jsp" />
    <script>
        
        function onaddListener(event){        
            var acc=document.getElementsByTagName("input");// document.getElementsByName('upForm:locationType');
            var str="NULL"
            for(var i=0;i<acc.length;i++)
            {
                if(acc[i].type=='radio'){
                    //alert(acc[i].checked);
                    if(acc[i].checked){
                        str = acc[i].value.toUpperCase();
                    }               
                }
            }
            if("NULL"==str) {
                alert("locationType is required.");
                switchUploadControl(true);
                return false;
            }
            
            //var fileName = $('upForm:uploadItem').component.entries[0].fileName.toUpperCase();
            //if(fileName.indexOf(str) ==-1){
            //    alert("location type is not matched with the file.");
            //    switchUploadControl(true);
            //    return false;
            //}
        }
        
        function locationHandler() {
        	if($('upForm:uploadItem').component.entries.length != 0) {
        		switchUploadControl(false);   
        	}
            //var acc=document.getElementsByTagName("input");// document.getElementsByName('upForm:locationType');
            //var str="NULL"
            //for(var i=0;i<acc.length;i++)
            //{
            //    if(acc[i].type=='radio'){
            //        //alert(acc[i].checked);
            //        if(acc[i].checked){
            //            str = acc[i].value.toUpperCase();
            //        }               
             //   }
           // }
            
           //if($('upForm:uploadItem').component.entries.length != 0) {
            //    var fileName = $('upForm:uploadItem').component.entries[0].fileName.toUpperCase();
            //    
            //    if(fileName.indexOf(str) ==-1){
            //        alert("location type is not matched with the file.");
           //        switchUploadControl(true);
           //         return false;
           //     }else{
           //         switchUploadControl(false);             
           //     }           
           // }
        
        }
        
        function switchUploadControl(expanded) {
            var button = $('upForm:uploadItem:upload2');             
            if(expanded) {
                 Element.hide(button.parentNode);
            } else {
                 Element.show(button.parentNode);
            }
        }
    </script>
    </head>
    <body>
    <style>
        .top {
            vertical-align: top;
            
        }
        .info {
            height: 202px;
            overflow: auto;
        }
        </style>
    <div id="frame">
        
        <h:form id="upForm">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td><jsp:include page="header.jsp"/></td>
            </tr>
        </table>
            <h:panelGrid columns="2" columnClasses="top,top">
                <h:panelGroup id="progressPanel2"> 
                    <h:graphicImage id="requiredRates" alt="required" title="required"  url="/images/layout/required.jpg" />
                    <h:outputText style="text-align: left;font-size:11px"   value="LocationType"/>   
                    <h:selectOneRadio id="locationType" value="#{fileUploadBean.locationType}" styleClass="selfDefinedFontRadio" onclick="locationHandler();" >
                       <f:selectItem itemValue="SLAP" itemLabel="SLAP" />
                       <f:selectItem itemValue="ABank" itemLabel="ABank" />
                       <f:selectItem itemValue="Substation" itemLabel="Substation" />
                    </h:selectOneRadio>
                </h:panelGroup>
                
                <h:panelGroup>                  
                </h:panelGroup>
                
                <rich:fileUpload fileUploadListener="#{fileUploadBean.listener}"
                    maxFilesQuantity="#{fileUploadBean.uploadsAvailable}"
                    id="uploadItem"
                    immediateUpload="#{fileUploadBean.autoUpload}"
                    acceptedTypes="txt" allowFlash="false" onadd="onaddListener(event);" >
                    
                    <a4j:support 
                        event="onuploadcomplete" 
                        action="#{fileUploadBean.fileChoosenAction}"
                        reRender="progressPanel"
                        />
                    
                     <f:facet name="label">
                         <h:outputText value="{_KB}KB from {KB}KB uploaded --- {mm}:{ss}" />
                     </f:facet>
                    
                </rich:fileUpload>
                <h:panelGroup id="progressPanel">                
                   <rich:progressBar value="#{fileUploadBean.incValue}"  interval="900" enabled="#{fileUploadBean.enabled}" minValue="0" maxValue="100"
                    reRenderAfterComplete="progressPanel">
                        <h:outputText value="{value}%"/>                   
                         <f:facet name="initial">
                            <h:outputText value="Process not started"/>
                         </f:facet>
                         <f:facet name="complete">
                            <h:outputText value="Process completed"/>
                         </f:facet>
                   </rich:progressBar> 
                </h:panelGroup>
                
            </h:panelGrid>

            <rich:spacer height="15px" width="80px"/>
            <jsp:include page="footer.jsp" />
        </h:form>    
        </div>
    </body>
</f:view>
</html>