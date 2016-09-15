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
					
					<rich:panel styleClass="content-panel">
						<h:messages layout="table" globalOnly="true" 
							infoClass="global-message-info" warnClass="global-message-warn" 
							errorClass="global-message-error" fatalClass="global-message-fatal"/>
						<div>
							<h:outputLabel value="(" styleClass="form-field"/>
							<h:graphicImage alt="*" title="*"  url="/images/layout/required.jpg" />
							<h:outputLabel value="denotes a required field)" styleClass="form-field"/>
						</div>
						<h:panelGrid columns="3" styleClass="panel-style" style="position:relative;">
						
							<h:message for="currentPW" errorClass="message-error" style="position:absolute; left:300px;width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredCurrentPW" alt="required" title="required"  url="/images/layout/required.jpg" />
								<h:outputLabel for="currentPW"  value="Current Password:" 
									styleClass="form-field"/>
							</h:panelGroup>
							<h:inputSecret id="currentPW" 
								value="#{options.currentPW}" 
								required="true" label="#{options.currentPW}"
								requiredMessage="Current Password is required"
								title="Current Password"/>
							
							<h:message for="newPW" errorClass="message-error" style="position:absolute; left:300px;width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredNewPW" alt="required" title="required"  url="/images/layout/required.jpg" />
								<h:outputLabel for="newPW" value="New Password:" styleClass="form-field"/>
							</h:panelGroup>
							<h:inputSecret id="newPW"
								value="#{options.newPW}" 
								required="true" label="#{options.newPW}"
								requiredMessage="New Password is required"
								title="New Password"/>
							
							<h:message for="confirmPW" errorClass="message-error" style="position:absolute; left:300px;width:200px;margin-top:-8px;"/>
							<h:panelGroup>
								<h:graphicImage id="requiredConfirmPW" alt="required" title="required"  url="/images/layout/required.jpg" />
								<h:outputLabel for="confirmPW"  value="Confirm New Password:" 
									styleClass="form-field"/>
							</h:panelGroup>
							<h:inputSecret id="confirmPW" 
								value="#{options.confirmPW}" 
								required="true" label="#{options.confirmPW}"
								requiredMessage="Confirm New Password is required"
								title="Confirm New Password"/>
							
						</h:panelGrid>
						
						
						<div id="fm-submit">
							<h3><h:commandButton value="Update Password" title="Update Password" action="#{options.updatePasswordAction}"/></h3>
						</div>
					</rich:panel>
				</h:form>
	
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
