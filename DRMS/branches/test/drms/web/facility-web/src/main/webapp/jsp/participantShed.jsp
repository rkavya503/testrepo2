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
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
					<rich:panel styleClass="content-panel" id="content-panel">
						<rich:dataTable id="hours" rows="24" value="#{participantShed.entries}" var="entry" styleClass="content-table" style="width:80%;">
										<rich:column width="50%">
											<f:facet name="header">
												<h:outputText value="Time Block"  title="Time Block"/>
											</f:facet>
											<h:outputText value="#{entry.timeBockString}" style="font-weight:bold"/>
										</rich:column>
										<rich:column width="50%">
											<f:facet name="header">
												<h:outputText value="Shed Value(KW)" title="Shed Value"/>
											</f:facet>
											<h:outputText value="#{entry.value}" style="font-weight:bold"/>
										</rich:column>
						</rich:dataTable>
					</rich:panel>
					
				</h:form>
	
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
