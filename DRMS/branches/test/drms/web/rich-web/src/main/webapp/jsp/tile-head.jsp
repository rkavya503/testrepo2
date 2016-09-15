<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>">
<title>Akuacom DRAS Facilty Dashboard</title>
<link href="css/style.css" type="text/css" rel="stylesheet" />
<link href="css/nav.css" type="text/css" rel="stylesheet" />
<link href="css/forms.css" type="text/css" rel="stylesheet" />

