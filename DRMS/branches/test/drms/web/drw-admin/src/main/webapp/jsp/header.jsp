<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN ">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<script>
function closeWindow(){
     if(navigator.appVersion.indexOf("MSIE") != -1){
         var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
        if (version == "9" || version == "8" || version == "7") { 
            window.open('', '_self', ''); 
            window.close(); 
        } else if (version == "6") { 
            window.opener = null; 
            window.close(); 
        } else { 
            window.opener = ''; 
            window.close(); // attempt to close window first, show user warning message if fails 
        } 
    }else{
        window.opener = null; 
        window.close(); 
    }
}
</script>

<a4j:keepAlive beanName="footer" />
<a4j:status id="waitStatus" forceId="true"
          onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
           onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
<h:panelGroup styleClass="#{footer.headerStyle}" layout="block" id="page_wrapper">
                                
    <div class="header">
        <br>
        <h1>
            Demand Response Automation Server
        </h1>
        <ul id="nav">
            <h:panelGroup rendered="#{programs.programViewEnabled}">
            <li id="${footer.context.programs}">
                <richext:commandLink value="Interruptible Programs" action="#{footer.context.programsAction}" 
                    immediate="true" rendered="#{programs.programViewEnabled}"/>
            </li> 
            </h:panelGroup>
            
             <h:panelGroup rendered="#{programs.uploadGeoEnabled}">
             <li id="${footer.context.upload}">
                <richext:commandLink value="Upload Geographic File" action="#{footer.context.uploadAction}" 
                    immediate="true" rendered="#{programs.uploadGeoEnabled}"/>
            </li> 
            </h:panelGroup>
            
            <h:panelGroup rendered="#{programs.uploadConfEnabled}">
            <li id="${footer.context.uploadConf}">
                <richext:commandLink value="Upload Configuration Data" action="#{footer.context.uploadConfAction}" 
                    immediate="true" rendered="#{programs.uploadConfEnabled}"/>
            </li> 
            </h:panelGroup>
            
            <h:panelGroup rendered="#{programs.geoConfigEnabled}">
            <li id="${footer.context.geographicConfig}">
                <richext:commandLink value="Geographic Configuration" action="#{footer.context.geographicConfAction}" 
                    immediate="true" rendered="#{programs.geoConfigEnabled}"/>
            </li>
            </h:panelGroup> 
        </ul>
        
        <div id="navrightDiv">
            <ul id="navright">
            
                <li class="last"><a href="javascript:closeWindow();">Close</a>
                </li>
                
                <li class="topright"><h:outputText
                    value="Welcome #{footer.context.welcomeName} - (#{footer.context.userRole})" />
                </li>
                
                <li class="topright"><h:outputText
                    value="#{footer.context.serverTime}">
                    <f:convertDateTime pattern="#{footer.headerDateTimeFormat}"/>
                    </h:outputText>
                </li>

            </ul>
        </div>
        
    </div>
</h:panelGroup>
<a name="skipAim" id="skipAim"></a>
