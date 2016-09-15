<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
		  
			<h:form id="navForm">
				<jsp:include page="header.jsp" />

				<rich:panel>
					<f:facet name="header">
						<h:outputText value="#{bids.title}" />
					</f:facet>
					
					<h:messages layout="table" globalOnly="true" 
						infoClass="global-message-info" warnClass="global-message-warn" 
						errorClass="global-message-error" fatalClass="global-message-fatal"/>
						
					<rich:dataTable id="clients_programs" rows="24"
						value="#{bids.bids}" var="bid" 
						styleClass="content-tab-table">
						<rich:column width="40%">
							<f:facet name="header">
								<h:outputText value="Time Block" title="Time Block"/>
							</f:facet>
							<h:outputText value="#{bid.timeBlock}"/>
						</rich:column>
						<rich:column width="40%">
							<f:facet name="header">
								<h:outputText value="Reduction (kW)" title="Reduction (kW)"/>
							</f:facet>
							<h:inputText id="reduction" value="#{bid.reductionKW}"
								required="false" label="#{bid.reductionKW}"
                                disabled="#{bids.readOnly}"/>
							<h:message for="reduction" errorClass="errors" />
						</rich:column>
						<rich:column width="20%" rendered="#{bids.displayActive}">
							<f:facet name="header">
								<h:outputText value="Active" title="Active" />
							</f:facet>
							<h:selectBooleanCheckbox 
								value="#{bid.active}" disabled="#{bids.readOnly}"/>
						</rich:column>
					</rich:dataTable>

					<div id="fm-submit">
						<table border="0">

							<tr>
								<td>
									<h3>
										<h:commandButton value="Submit" title="Submit" action="#{bids.saveBidsAction}" rendered="#{!bids.readOnly}"/>
									</h3>
								</td>
								<td>
									<h3>
										<h:commandButton value="Cancel"  title="Cancel" action="#{bids.cancelBidsAction}" immediate="true"/>
									</h3>
								</td>
							</tr>
						</table>	
					</div>

				</rich:panel>
			</h:form>

		<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
