<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="java.util.Date,com.akuacom.pss2.richsite.*"%>

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

<link href="css/facdash.css" type="text/css" rel="stylesheet" />
<div id="page_wrapper">
    <div id="header" style="background: #DAE0D2 url('/pss2.utility/images/layout/header_background-blu.jpg') no-repeat left top;"><br>
        <h1>Demand Response Automation Server</h1>



        <ul id="navright">
            <li class="last"><a href="javascript:closeWindow();">Close</a>
            <li><h:outputText value="Welcome #{header1.welcomeName} - (#{header1.userRole})" /></li>
            <li><h:outputText value="#{header1.serverTime}">
                <f:convertDateTime pattern="#{applicationScope.headerDateTimeFormat}" />
            </h:outputText></li>
        </ul>
    </div>
</div>

