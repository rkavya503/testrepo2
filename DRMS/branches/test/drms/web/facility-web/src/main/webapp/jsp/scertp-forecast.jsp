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

				<rich:panel id="forecast-panel" styleClass="content-panel">

					<rich:dataTable id="strategies" rows="24"
						value="#{sceRTPForecast.hours}" var="hour" 
						styleClass="content-tab-table">
						<f:facet name="header">
	                    	<rich:columnGroup>
		                    	<rich:column colspan="13">
									<h:outputText value="Forecast for #{client.name} in #{clientProgram.programName}" />
		                        </rich:column>
	                        	<rich:column rowspan="4" breakBefore="true">
	                            	<rich:spacer/>
	                        	</rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.dateStrings[5]}" />
		                        </rich:column>
		                        <rich:column colspan="2" breakBefore="true">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.seasonStrings[5]}" />
		                        </rich:column>
		                        <rich:column colspan="2" breakBefore="true">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="2">
		                        	<h:outputText value="#{sceRTPForecast.highTempStrings[5]}" />
		                        </rich:column>
		                        <rich:column breakBefore="true">
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="price" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
	                    	</rich:columnGroup>
						</f:facet>

						<rich:column width="16%">
							<h:outputText value="#{hour.timeBlockString}"/>
						</rich:column>
						
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[0].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[0].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[1].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[1].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[2].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[2].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[3].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[3].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[4].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[4].mode}"/>
						</rich:column>
						
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[5].price}"/>
						</rich:column>
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[5].mode}"/>
						</rich:column>
					</rich:dataTable>
					
					<rich:spacer height="10px" />

					<div id="fm-submit">
						<h3><h:commandButton value="Back" action="#{sceRTPForecast.doneAction}" /></h3>
					</div> 
				</rich:panel>
			</h:form>

			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
