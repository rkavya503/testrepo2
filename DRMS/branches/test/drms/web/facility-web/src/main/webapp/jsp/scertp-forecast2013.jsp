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
			
				<style type="text/css">
				.rich-inplace-select-view {
					background-color:transparent;
					border-bottom-style: none;
				}
				
				.rich-inplace-select-font {
					zoom: 1;
					font-family: Arial, Verdana, sans-serif;
					font-size: 11px;
				}
				.activeClass{
					background:#90EE90;
				}
				.copySelected{
					background:#A9E2F3;
				}
				
				.highlight_high{
					background:url(images/redHBg.gif) 0 0 no-repeat;
					padding: 4px 4px 4px 30px;
					
					
				}
				.highlight_moderate{
					background:url(images/orangeHBg.gif) 0 0 no-repeat;
					padding: 4px 4px 4px 30px;
				}
				.highlight_normal{
					background:url(images/greenHBg.gif) 0 0 no-repeat;
					padding: 4px 4px 4px 30px;
				}			
				fieldset {
					margin-left: 20px;
					width:300px;
				}
			</style>
			
			
				 <script type="text/javascript">
			//<![CDATA[
				var $j=jQuery.noConflict();				
				
 
				function init(){	
				$j('#navForm:strategies tr').each(function(){				
					$j(this).find('td').each(function(){
						if("High".toUpperCase()==$j(this).html()){
							$j(this).addClass('highlight_high');
						}else if("Moderate".toUpperCase()==$j(this).html()){
							$j(this).addClass('highlight_moderate');
						}else if("Normal".toUpperCase()==$j(this).html()){
							$j(this).addClass('highlight_normal');
						}
					})
				})}
				

			//]]>
			</script>
		</head>
		<body>
			<h:form id="navForm">
				<jsp:include page="header.jsp" />

				<rich:panel id="forecast-panel" styleClass="content-panel">

					<rich:dataTable id="strategies" rows="24"
						value="#{sceRTPForecast2013.hours}" var="hour" 
						styleClass="content-tab-table">
						<f:facet name="header">
	                    	<rich:columnGroup>
		                    	<rich:column colspan="7">
									<h:outputText value="Forecast for #{client.name} in #{clientProgram.programName}" />
		                        </rich:column>
	                        	<rich:column rowspan="4" breakBefore="true">
	                            	<rich:spacer/>
	                        	</rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.dateStrings[5]}" />
		                        </rich:column>
		                        <rich:column colspan="1" breakBefore="true">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.seasonStrings[5]}" />
		                        </rich:column>
		                        <rich:column colspan="1" breakBefore="true">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[0]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[1]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[2]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[3]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[4]}" />
		                        </rich:column>
		                        <rich:column colspan="1">
		                        	<h:outputText value="#{sceRTPForecast2013.highTempStrings[5]}" />
		                        </rich:column>
		                        <rich:column breakBefore="true">
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
		                        </rich:column>
		                        <rich:column>
		                        	<h:outputText value="mode" />
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
							<h:outputText value="#{hour.entries[0].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[1].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[2].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[3].mode}"/>
						</rich:column>

						<rich:column width="7%">
							<h:outputText value="#{hour.entries[4].mode}"/>
						</rich:column>
						
						<rich:column width="7%">
							<h:outputText value="#{hour.entries[5].mode}"/>
						</rich:column>
					</rich:dataTable>
					
					<script type="text/javascript">
					init();
					</script>
					<rich:spacer height="10px" />

					<div id="fm-submit">
						<h3><h:commandButton value="Back" action="#{sceRTPForecast2013.doneAction}" /></h3>
					</div> 
				</rich:panel>
			</h:form>

			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>
