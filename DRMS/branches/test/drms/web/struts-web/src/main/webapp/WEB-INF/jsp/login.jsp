<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.web.util.EJBFactory" %>
<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Properties" %>
<%@ page import="com.akuacom.pss2.system.property.PSS2Features" %>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<jsp:useBean id="pss2Config" class="com.akuacom.pss2.web.common.Config" scope="request"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</head>
<body>
    <% if(((String)application.getAttribute("utility")).equalsIgnoreCase("sce")){ 
	%>
			<jsp:include page="sce_login.jsp"/>
	<%	}else{ %>
			<jsp:include page="default_login.jsp"/>
	<%  } %>

</body>

</html>


