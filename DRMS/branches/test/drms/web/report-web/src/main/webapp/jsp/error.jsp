<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
<head>
<title>DRAS Participant</title>
</head>

<script type="text/javascript">
function closeWindow(){
	 if(navigator.appVersion.indexOf("MSIE") != -1){
	 	 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
		if (version == "8" || version == "7") { 
			window.open('', '_self', ''); 
			window.close(); 
		} else if (version == "6") { 
			window.opener = null; 
			window.close(); 
		} else { 
			window.opener = ''; 
			window.close(); // attempt to close window first, show user warning message if fails 
		} 
	}else{
		window.opener = null; 
		window.close(); 
	}
}
</script>

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
	background: #DAE0D2 url("../images/layout/header_background-wide.jpg")
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
	text-align: right;
	font-size: 0.7em;
	color: #93abbe;
	letter-spacing: 1px;
}

#page_footer a img {
	border-width: 0;
}
</style>

<div id="header">
<h1>Demand Response Automation Server</h1>
</div>

<div id="error-body"><strong>Your session timed out.</strong>
<br/>
<br/>
Please <a href="/pss2.website/Login">login</a> and try again.</div>

<jsp:include page="/jsp/logo.jsp" />

</body>
</f:view>
</html>
