<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>">
<title> DR Automation Server: Price Server System</title>
<link href="css/facdash.css" type="text/css" rel="stylesheet" />
<link href="css/forms.css" type="text/css" rel="stylesheet" />

