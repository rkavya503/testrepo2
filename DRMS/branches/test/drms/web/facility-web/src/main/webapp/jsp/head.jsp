<%@ page import="com.akuacom.pss2.system.SystemManager" %>
<%@ page import="com.akuacom.ejb.client.EJB3Factory" %>
<%
	String path = request.getContextPath();

    SystemManager systemManager = (SystemManager)EJB3Factory.getBean(SystemManager.class);

    String hostName = systemManager.getPss2Properties().getResponseHost();
    String proxy = request.getParameter("proxy");

    if("true".equalsIgnoreCase(proxy))
    {
        request.getSession().putValue("proxy", proxy);
    }
    

    proxy = (String) request.getSession().getValue("proxy");
   
    if(hostName == null || hostName.isEmpty() || "true".equalsIgnoreCase(proxy))
    {
        hostName = request.getServerName();
    }

	String basePath = request.getScheme() + "://"
		+ hostName + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>"/>
<title>DRAS Participant</title>
<link href="css/facdash.css" type="text/css" rel="stylesheet" />

