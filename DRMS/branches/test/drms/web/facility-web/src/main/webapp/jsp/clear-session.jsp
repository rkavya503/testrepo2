<%@ page import="com.akuacom.pss2.facdash.*" %>
<%
	// if we are coming from struts as substite user, clear the session cache
	if(request.getParameter("user") != null)
	{
		FDUtils.setHeader1(null);
	}
%>
