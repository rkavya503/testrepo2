<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Operator Email Setting</title>
		<jsp:include page="../head.jsp" />

    </head>

    <body>
	<jsp:include page="../header_options.jsp" />
		<h:form>
			<a4j:keepAlive beanName="contactBackingBean" />
			<rich:dataTable style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Operator Email Setting" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			<rich:dataTable value="#{contactBackingBean.contacts}" var="item" style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="name" escape="false" /></f:facet>
					<h:outputText value="#{item.firstName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="address" escape="false" /></f:facet>
					<h:outputText value="#{item.address}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="opt out <br/>Client Test Email Digest " escape="false" /></f:facet>
					<h:selectBooleanCheckbox value="#{item.optOutDigest}" title="opt out" disabled ="#{contactBackingBean.userAuthorized}">
                    </h:selectBooleanCheckbox>
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="opt out <br/>Undelivered Report" escape="false" /></f:facet>
					<h:selectBooleanCheckbox value="#{item.optOutUndeliveredReport}" title="opt out Undelivered Report" disabled ="#{contactBackingBean.userAuthorized}">
                    </h:selectBooleanCheckbox>
				</rich:column>
			</rich:dataTable>
			<h:commandButton value="Update Contact" title="Update Contact" action="#{contactBackingBean.updateContactAction}" rendered = "#{!contactBackingBean.userAuthorized}" />
			
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>