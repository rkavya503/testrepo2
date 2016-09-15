<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="com.akuacom.pss2.facdash.*,javax.faces.context.*" %>
<html>
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
		</head>
		<body>
			
				<h:form>
					<jsp:include page="header.jsp" />
	
                                        <rich:panel styleClass="about-content-panel" style="align: left">
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
                                                
						
                                               
					</rich:panel>
	
				</h:form>

				<jsp:include page="footer.jsp" />
			
		</body>
	</f:view>
</html>
