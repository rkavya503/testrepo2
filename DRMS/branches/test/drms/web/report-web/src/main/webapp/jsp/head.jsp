<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
		+ request.getServerName() + ":" + request.getServerPort()
		+ path + "/";
%>
<base href="<%=basePath%>"/>
<title>DRAS Customer Report</title>
<link href="css/facdash.css" type="text/css" rel="stylesheet" />
<link href="css/report.css" type="text/css" rel="stylesheet" />
<script src="js/accessibility.js" type="text/javascript"></script>

