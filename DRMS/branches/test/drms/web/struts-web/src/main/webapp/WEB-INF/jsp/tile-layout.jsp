<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.core.EJBFactory" %>

<%
	Object style = session.getAttribute("headerStyle");
	if(style==null){
		SystemManager systemManager = EJBFactory.getBean(SystemManager.class);
        String headerStyle="testServerUtilityOperator";
        if(systemManager.getPss2Features().isProductionServer()){
        	headerStyle = "productionServerUtilityOperator";
        }
        session.setAttribute("headerStyle", headerStyle);
	}
%>  
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title><tiles:getAsString name="title"/></title>
        <tiles:insert attribute="header"/>
    </head>
    <body id="standard">
		<div id="page_wrapper" class="${sessionScope.headerStyle}"> 
    
        <tiles:insert attribute="tabs"/>
        <!--  Content Wrapper -->
        <div id="content_wrapper">
            <html:errors/>
            <tiles:insert attribute="content"/>
        </div>
        <!-- Page Wrapper -->
        <div id="page_footer">
            <tiles:insert attribute="footer"/>
        </div>
    </div>
    </body>
</html>
