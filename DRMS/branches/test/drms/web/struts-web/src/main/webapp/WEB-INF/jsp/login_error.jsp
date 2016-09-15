<html>
<head>
<meta content="text/html; charset=iso-8859-1" http-equiv="Content-Type">
<title>DR Automation Server: LoginError Page</title>
</head>
<body>
<%
	//save attribute to indicate login error happened
	request.setAttribute("loginerr","true");
%>
<jsp:forward page="/WEB-INF/jsp/login.jsp"/>
</body>
</html>