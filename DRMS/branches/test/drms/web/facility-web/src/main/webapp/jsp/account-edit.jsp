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

<script type="text/javascript">		
</script>
		<body>
			<div id="frame">
				<h:form id="navForm">
					<jsp:include page="header.jsp" />
	
					<rich:panel styleClass="content-panel">

						<rich:panel>
							<f:facet name="header">
								<h:outputText value="Sub Account" />
							</f:facet>
							
							<h:messages layout="table" globalOnly="true" 
								infoClass="global-message-info" warnClass="global-message-warn" 
								errorClass="global-message-error" fatalClass="global-message-fatal"/>
								
							<h:panelGrid columns="2" styleClass="panel-style">
								<h:panelGroup>
									<h:graphicImage id="requiredAccountNumber" alt="Required" url="/images/layout/required.jpg" />
									<h:outputLabel id="accountNumberText" for="name"
										value="Account Number:" 
										styleClass="form-field"/>
								</h:panelGroup>
								<h:panelGroup>
									<h:inputText
										id="name" title="Account Number"
										value="#{account.name}" 
										label="#{account.name}" 
										required="true"
										requiredMessage="Account Number is required"
										disabled="#{account.edit}"/>
									<h:message for="name" errorClass="message-error"/>
									
								</h:panelGroup>
		
								<h:outputLabel id="premiseText" for="premise"
									value="Premise Number:" 
									styleClass="form-field"/>
								<h:panelGroup>
									<h:inputText 
										id="premise"  title="Premise Number"
										value="#{account.premise}" 
										label="#{account.premise}" />
									<h:message for="premise" errorClass="message-error" />
								</h:panelGroup>
		
		                       <h:outputLabel id="enrollmentDateText"  for="enrollment"
                                    value="Enrollment Date:" 
                                    styleClass="form-field"/>
                                <h:panelGroup>
                                    <a4j:outputPanel layout="block">
                                        <rich:calendar 
                                            id="enrollment"  
                                            value="#{account.enrollmentDate}"
                                            popup="true"
                                            datePattern="M/d/yy"
                                            cellWidth="24px" 
                                            cellHeight="22px" 
                                            style="width:200px"/>
                                    </a4j:outputPanel>
                                    <h:message for="enrollment" errorClass="message-error" />  
		                        </h:panelGroup>
								<h:outputLabel id="startDateText" for="start"
									value="Start Date:" 
									styleClass="form-field"/>
								<h:panelGroup>
									<a4j:outputPanel layout="block">
				                    	<rich:calendar 
				                    		id="start" 
				                    		value="#{account.startDate}"
					                        popup="true"
					                        datePattern="M/d/yy"
					                        cellWidth="24px" 
					                        cellHeight="22px" 
					                        style="width:200px"/>
				           			</a4j:outputPanel>
									<h:message for="start" errorClass="message-error" />
								</h:panelGroup>
		
								<h:outputLabel value="Deactive Date:" styleClass="form-field" for="deactivate"/>
								<h:panelGroup>
									<a4j:outputPanel  layout="block">
				                    	<rich:calendar 
				                    		id="deactivate"
				                    		value="#{account.deactiveDate}"
					                        popup="true"
					                        datePattern="M/d/yy"
					                        cellWidth="24px" 
					                        cellHeight="22px" 
					                        style="width:200px" />
				           			</a4j:outputPanel>
									<h:message for="deactivate" errorClass="message-error" />
								</h:panelGroup>
		
								<h:outputLabel value="Comment:" styleClass="form-field" for="comment"/>
								<h:panelGroup>
									<h:inputText id="comment" value="#{account.comment}" title="Comment"
										label="#{account.comment}" />
									<h:message for="comment" errorClass="message-error" />
								</h:panelGroup>
		
							</h:panelGrid>
		
							<div id="fm-submit">
								<table border="0">
								<tr>
									<td>
										<h3>
											<h:commandButton 	value="Create Account" title="Create Account"
																action="#{subAccounts.createAccountAction}" rendered="#{!account.edit}"
											 />
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton 	value="Save Account" title="Save Account"
																action="#{subAccounts.updateAccountAction}" rendered="#{account.edit}"
											 />				
										</h3>
									</td>
									<td>
										<h3>
											<h:commandButton 	value="Cancel" title="Cancel"  
																action="#{subAccounts.cancelAccountAction}" 
																immediate="true"
											 />			
										</h3>
									</td>
								</tr>
								</table>	 
							</div>

						</rich:panel>
					</rich:panel>
				</h:form>
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
