<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			<div id="frame" >
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
				</h:form>
				<rich:panel styleClass="content-panel">
					<iframe src="${newsInfo.newsUrl}" 
							frameborder="0" width="100%" height="400" scrolling="auto">
							<p>Your browser does not support iframes.</p>
					h:		
					</iframe>	
				</rich:panel>		
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
