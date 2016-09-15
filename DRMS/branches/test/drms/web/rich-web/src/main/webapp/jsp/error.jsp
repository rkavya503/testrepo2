<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
<f:view>
<head>
<title>DR Automation Server: Price Server System</title>
</head>
<body>

<style>
#header {
	float: left;
	width: 100%;
	font-size: .8em;
	line-height: normal;
	text-align: left;
	overflow: auto;
	margin: 0 0 8px 0;
	background: #DAE0D2 url("/facdash/images/layout/header_background-wide.jpg")
		repeat-x center bottom;
}

body {
	color: #000000;
	text-align: left;
	margin: 0px;
	font-family: verdana, arial, sans-serif;
	font-size: small;
	background-color: #FFFFFF;
}

#page_footer {
	background-color: #fff;
	clear: both;
	border-top: solid #5d819d 1px;
	margin: 10px 10px 20px 10px;
}

#page_footer .akua {
	padding-top: 5px;
	padding-bottom: 10px;
	margin-bottom: 0px;
	text-align: right;
	font-size: 0.9em;
	color: #93abbe;
	letter-spacing: 1px;
}
</style>

<div id="header">
<h1>Demand Response Automation Server</h1>
</div>

<div id="error-body"><strong>Your session timed out.</strong>
<br>
<br>
Please <a href="/pss2.website/Login">login</a> and try again.</div>
<jsp:include page="/jsp/logo.jsp" />
</body>
</f:view>
</html>
