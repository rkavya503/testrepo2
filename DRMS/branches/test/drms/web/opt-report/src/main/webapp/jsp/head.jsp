<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>">
<title>DRAS Operator Report</title>
<link href="css/facdash.css" type="text/css" rel="stylesheet" />
<link href="css/report.css" type="text/css" rel="stylesheet" />


