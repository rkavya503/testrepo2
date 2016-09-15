<%
	String path = "/drw.admin";
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";	
%>
<base href="<%=basePath%>">
<title>Demand Response Automation Server</title>
<link href="css/facdash.css" type="text/css" rel="stylesheet" />
<link href="css/drw.css" type="text/css" rel="stylesheet" />


