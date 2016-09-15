<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html>
<f:view>
	<head>
    <title>Profiling (via aspectj)</title>
	<jsp:include page="../head.jsp" />
	</head>
	<body>
	<jsp:include page="../header.jsp" />
	<h:form id="profilingFrom">
		
		<rich:panel styleClass="about-content-panel" style="align: left">
			<h:panelGrid columns="1" styleClass="about-panel-style">
				<h:commandLink value="reset" action="#{profiling.resetTop100 }" />

				<h:outputText escape="false" value="#{profiling.top100Html}" />
			</h:panelGrid>
		</rich:panel>
	</h:form>
    <br/>
    <br/>
    <br/>
	<jsp:include page="../footer.jsp" />
	</body>
</f:view>
</html>
