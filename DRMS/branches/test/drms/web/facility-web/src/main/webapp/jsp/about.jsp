<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="com.akuacom.pss2.facdash.*,javax.faces.context.*" %>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
	
					<rich:panel styleClass="about-content-panel">
						<h:panelGrid columns="2" styleClass="about-panel-style">
							<h:outputText value="Utility:" style="font-weight:bold"/>
							<h:outputText value="#{about.utility}" />
	
							<h:outputText value="Version:" style="font-weight:bold"/>
							<h:outputText value="#{about.displayVersion}" />
	
							<h:outputText value="Build:" style="font-weight:bold"/>
							<h:outputText value="#{about.version} (#{about.buildTime})" />
	
							<h:outputText value="Server Timezone:" style="font-weight:bold"/>
							<h:outputText value="#{about.timezoneInfo}" />

							<h:outputText value="Contact Website:" style="font-weight:bold"/>
							<h:outputText value="#{about.contactWebsite}" />
	
							<h:outputText value="Contact Phone:" style="font-weight:bold"/>
							<h:outputText value="#{about.contactPhone}" />
	
							<h:outputText value="Contact Email:" style="font-weight:bold"/>
							<h:outputText value="#{about.contactEMail}" />
						</h:panelGrid>
						<%= FacesContext.getCurrentInstance().getExternalContext().
							getSessionMap().get("about") == null ? "" : 
							((About)FacesContext.getCurrentInstance().getExternalContext().
							getSessionMap().get("about")).getInfo()
						%>
					</rich:panel>
	
				</h:form>

				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
