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
				
				
				function mouseOverListener(obj){	
					$j(obj).addClass('activeClass');
				}
				
						
				function mouseOutListener(obj){	
					if ( $j(obj).hasClass('activeClass') ){
						$j(obj).removeClass('activeClass');
					}		
				}
 
				function init(){	
				$j('#navForm:hours_view tr').each(function(){				
					$j(this).find('td').each(function(){
						if("High"==$j(this).html()){
							$j(this).addClass('highlight_high');
						}else if("Moderate"==$j(this).html()){
							$j(this).addClass('highlight_moderate');
						}else if("Normal"==$j(this).html()){
							$j(this).addClass('highlight_normal');
						}
					})
				})}
				
				function validateGenerateCriteria(obj){
				if("View"==$j(obj).val()){
					var r=confirm("Unsaved data will be lost- Do you want to continue?")
					if (r==true) { return true;}
					else { 
						$j('input:radio[name=navForm:radioButtonStrategy]:nth(1)').attr('checked',true);						
						return false; 
					}
				}
				return true;
				}
				
				function showConfirm(obj){
					var r=confirm("Unsaved data will be lost- Do you want to continue?")
					if (r==true) { return true;}
					else {
						//$j('input:radio[name=navForm:radioButtonStrategy]:nth(1)').attr('checked',true);						
						return false; 
					}
				}

			//]]>
			</script>
		</head>
		<body>
		
		<h:form id="navForm">
			<jsp:include page="header.jsp" />														
			<rich:panel styleClass="content-panel" id="content_body">			
				<f:facet name="header">
					<h:outputText 
						value="Edit RTP Shed Strategy for Client #{client.name} in Program #{clientProgram.programName}" />
				</f:facet>
				
				<h:messages layout="table" globalOnly="true" 
					infoClass="global-message-info" warnClass="global-message-warn" 
					errorClass="global-message-error" fatalClass="global-message-fatal"/>	
					
				<a4j:region >
					<fieldset>
						<legend ><b>Display mode</b></legend>
						<h:selectOneRadio id="radioButtonStrategy" value="#{clientProgram.scertp2013BackingBean.dispalyModel}" 
							styleClass="radio-buttons" disabled="#{!header1.editShed}">
							<a4j:support event="onclick" reRender="table_content" 
								actionListener="#{clientProgram.scertp2013BackingBean.typeListener}"
								   onsubmit="if(!validateGenerateCriteria(this)) {return false;}" 
								/>
							<f:selectItems value="#{clientProgram.scertp2013BackingBean.availableTypes}"/>
						</h:selectOneRadio>
					</fieldset>
					
					<rich:spacer height="20px" width="700px" />
				</a4j:region>
				
				<h:panelGroup  layout="block" id="table_content">																	
					<h:panelGroup  layout="block" id="table_content1">	
					<rich:dataTable id="hours_view" rows="24" rendered="#{clientProgram.scertp2013BackingBean.viewModel}"
						value="#{clientProgram.scertp2013BackingBean.rtpStrategies}" var="entry" 
						styleClass="content-tab-table">
						
						<f:facet name="header">
						<rich:columnGroup>
							
							<rich:column rowspan="2" breakBefore="true">
								<h:outputText value="Time Block" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="EXTREMELY HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="VERY HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="MODERATE SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="MILD SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HIGH COST WINTER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="LOW COST WINTER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HIGH COST WEEKEND" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="LOW COST WEEKEND" />
							</rich:column>		                        
							<rich:column colspan="1" breakBefore="true">								
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[0]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[1]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[2]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[3]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[4]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[5]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[6]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[7]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[8]}" />
							</rich:column>								
						</rich:columnGroup>
					</f:facet>						
						<rich:column width="10%">
							<h:outputText value="#{entry.timeBockString}" style="font-weight:bold"/>
						</rich:column>
						<rich:column width="10%">							
							<h:outputText value="#{entry.value0}" />	
						</rich:column>
						<rich:column width="10%">							
							<h:outputText value="#{entry.value1}" />
						</rich:column>						
						<rich:column width="10%">												
							<h:outputText value="#{entry.value2}" />
						</rich:column>						
						<rich:column width="10%">									
							<h:outputText value="#{entry.value3}" />	
						</rich:column>						
						<rich:column width="10%">									
							<h:outputText value="#{entry.value4}" />		
						</rich:column>						
						<rich:column width="10%">											
							<h:outputText value="#{entry.value5}" />
						</rich:column>						
						<rich:column width="10%">							
							<h:outputText value="#{entry.value6}" />			
						</rich:column>						
						<rich:column width="10%">							
							<h:outputText value="#{entry.value7}" />				
						</rich:column>						
						<rich:column width="10%">							
							<h:outputText value="#{entry.value8}" />
						</rich:column>						
					</rich:dataTable>
				</h:panelGroup>
				<!-- ******************************************************************************* -->
				<h:panelGroup  layout="block" id="table_content2">							
					<a4j:status id="waitStatus" forceId="true"
						onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
						onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />					
					<rich:dataTable id="hours" rows="24" rendered="#{!clientProgram.scertp2013BackingBean.viewModel}"					
						value="#{clientProgram.scertp2013BackingBean.rtpStrategies}" var="entry" 
						onRowMouseOver="mouseOverListener(this);"
						onRowMouseOut="mouseOutListener(this);"
						styleClass="content-tab-table">
						<f:facet name="header">
						<rich:columnGroup>
							<rich:column rowspan="2" breakBefore="true">
								<h:outputText value="Time Block" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="EXTREMELY HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="VERY HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HOT SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="MODERATE SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="MILD SUMMER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HIGH COST WINTER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="LOW COST WINTER WEEKDAY" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="HIGH COST WEEKEND" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="LOW COST WEEKEND" />
							</rich:column>		      
							<rich:column rowspan="2">
								<h:outputText value="Actions" />
							</rich:column>									
							<rich:column colspan="1" breakBefore="true">								
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[0]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[1]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[2]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[3]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[4]}" />
							</rich:column>
							<rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[5]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[6]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[7]}" />
							</rich:column>
							 <rich:column colspan="1">
								<h:outputText value="#{clientProgram.scertp2013BackingBean.temperatureCategories[8]}" />
							</rich:column>															
						</rich:columnGroup>
					</f:facet>
						<rich:column width="10%" styleClass="#{entry.style}">							
							<h:outputText value="#{entry.timeBockString}" style="font-weight:bold"/>							
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">							
							<h:panelGroup>
								 <h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value0}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value1}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value2}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								 <h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value3}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value4}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value5}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value6}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								 <h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value7}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" styleClass="#{entry.style}">
							<h:panelGroup>						
								<h:selectOneMenu  style="padding-left:5px;border: 1px solid;" value="#{entry.value8}">
									<f:selectItem  itemLabel="Normal" itemValue="Normal" />
									<f:selectItem itemLabel="Moderate" itemValue="Moderate" />
									<f:selectItem  itemLabel="High" itemValue="High" />
								</h:selectOneMenu>
							</h:panelGroup>
						</rich:column>
						<rich:column width="10%" rendered="true" styleClass="#{entry.style}">
							<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton value="Copy" title="Copy"   status="waitStatus"
												action="#{clientProgram.scertp2013BackingBean.copyEntryAction}"
												reRender="hours">
												<a4j:actionparam name="timeBlockString"  
													value="#{entry.timeBockString}" 
													assignTo="#{clientProgram.scertp2013BackingBean.timeBlockString}"/>
											</a4j:commandButton> 
										</h3>
									</td>
									<td>
										<h3>
											<a4j:commandButton value="Paste" title="Paste" status="waitStatus"
												action="#{clientProgram.scertp2013BackingBean.pasteEntryAction}"
												reRender="hours">
												<a4j:actionparam name="timeBlockString"
													value="#{entry.timeBockString}" 
													assignTo="#{clientProgram.scertp2013BackingBean.timeBlockString}"/>
											</a4j:commandButton> 
										</h3>
									</td>
								</tr>
							</table>
						</rich:column>
					</rich:dataTable>
				</h:panelGroup>
				<script type="text/javascript">
					init();
				</script>
				<div id="fm-submit">
					<table border="0">
							<tr>
								<td>
									<h:panelGroup  layout="block" rendered="#{!clientProgram.scertp2013BackingBean.viewModel}">
									<h3>
										<a4j:commandButton value="Update Strategy" title="Update Strategy"
											oncomplete="#{rich:component('confirm_panel')}.show();" reRender="confirm_panel" rendered="#{client.batchUpdateEnabled}"
											 />
									</h3>
									</h:panelGroup>
								</td>
								<td>
									<h:panelGroup  layout="block" rendered="#{!clientProgram.scertp2013BackingBean.viewModel}">
									<h3>
										<h:commandButton value="Cancel" title="Cancel" 
											action="#{clientProgram.scertp2013BackingBean.cancelRtpShedStrategiesAction}" 
											 onclick="if(!showConfirm(this)) {return false;}" 
											immediate="true"/>
									</h3>
									</h:panelGroup>
									<h:panelGroup  layout="block" rendered="#{clientProgram.scertp2013BackingBean.viewModel}">
									<h3>
										<h:commandButton value="Cancel" title="Cancel" 
											action="#{clientProgram.scertp2013BackingBean.cancelRtpShedStrategiesAction}" 											
											immediate="true"/>
									</h3>
									</h:panelGroup>
								</td>
							</tr>
						</table>	
					</div>
			</h:panelGroup>
			</rich:panel>
			<rich:modalPanel id="waitModalPanel" autosized="true" width="200"
				height="120" moveable="false" resizeable="false">
				<f:facet name="header">
					<h:outputText value="Processing" />
				</f:facet>
				<h:outputText value="Please wait..." />
				<center>
					<h:graphicImage value="/images/ajax-loader.gif" />
				</center>
			</rich:modalPanel>
			
			
			
			
						
				<rich:modalPanel id="confirm_panel" autosized="true"
	                    keepVisualState="false"
	                    width="315"
	                    height="150"
	                    >
	                    <div id="deleteForm">
	                    
             			<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
	            		<rich:panel id="messages"  >
	            			<a id="confirmBox" href="javascript:void(0);">
	                			<h:outputText value="This action will apply to the following clients. "/>
	                		</a>
	            		</rich:panel>
						<div style="height:150px;overflow-y:auto;overflow-x:hidden">
						
						<rich:dataTable id="clients_descendantClients" style="margin-left: 0px;margin-right: 0px;width:100%"
								value="#{clientProgram.descendantClients}" var="client"
								styleClass="content-tab-table">
								<rich:column width="50%">
									<f:facet name="header">
										<h:outputText value="Name" title="Name"/>
									</f:facet>
									<h:outputText value="#{client.participantName}"/>
								</rich:column>								
							</rich:dataTable>
						</div>			            		
							<table border="0">
							<tr>
								<td>
									<h3>
										<a4j:commandButton id="yes" value="Yes" 
										   action="#{clientProgram.scertp2013BackingBean.saveRtpShedStrategiesAction}"
										   oncomplete="#{rich:component('confirm_panel')}.hide();"
										   title="Save"/>
									</h3>
								</td>
								<td>
									<h3>
										<a4j:commandButton id="no" value="No" oncomplete="#{rich:component('confirm_panel')}.hide();" title="Cancel Clients"/>					
									</h3>
								</td>
							</tr>
							</table>
						
						
	            		</div>
	 				</rich:modalPanel>
			</h:form>
			<jsp:include page="footer.jsp" />
		</body>
	</f:view>
</html>