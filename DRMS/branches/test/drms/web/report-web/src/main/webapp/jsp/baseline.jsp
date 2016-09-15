  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
<f:view>
	<a4j:keepAlive beanName="baseline" />
	<head>
	<jsp:include page="head.jsp" />
	</head>
	<body>
	<br/>
	<div id="frame">
		<div  style="margin-left:18px; margin-top:5px" width="80%">
			<jsp:include page="bsearchFilter.jsp"/>
		</div>
	
		<h:form id="form">
		<jsp:include page="footer.jsp" />
		</div>
		</h:form> 
	</body>
</f:view>
</html>