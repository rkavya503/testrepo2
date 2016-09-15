<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<%@ page import="com.akuacom.pss2.facdash.*" %>
<html xmlns="http://www.w3.org/1999/xhtml" lang="en-US" xml:lang="en-US" >
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
			<style>
			
			TR.row-current .rich-table-cell a:link {
				color: white;
			}			
			
			.rich-table-cell a:link {
				color: #0072BC;
			}
			
			.activeClass{
				background:#A9E2F3;
			}
			
			.scroll-body-table TR.row-Selected {
				background:#A9E2F3;
			}
			
			.scroll-body-table TR.row-Selected TD {
				color: black;
			}
			
			TR.row-current {
				background:#A9E2F3;
				font-weight: bold;
			}
			
			a:link {
				text-decoration: underline;
			}
				
			TR.row-category {
			font-weight: bold;
			background: white;
			}
			
			TR.row-category .rich-table-cell{
				color:black;
				font-style:italic;
				font-weight:bold;
			}
			
			TR.row-current {
				background: #3399FF;
				font-weight:bold;
			}

			TR.row-current .rich-table-cell{
				color:white;
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
			//]]>
			</script>
		</head>
		<body>
			<div id="frame">
			
				<h:form id="navForm">
					<a4j:keepAlive beanName="aggregationTree"/>
					
					<h:panelGroup layout="block" id='boy_section'> 
					
					<jsp:include page="header.jsp" />
	
					<rich:panel styleClass="about-content-panel" id="treepanel">
						
						<a4j:status id="waitStatus" forceId="true"
						onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
						onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
						
						<h:panelGroup layout="block" > 												
							<div style="text-align: left;">
								<h:outputText value="Aggregated Participants" styleClass="form-field" ></h:outputText>
								<table width="800px">       
								<tr><td id="enrolled-table-col" width="800px">							
									<richext:treeTable 
									 onRowMouseOver="mouseOverListener(this);"
									 onRowMouseOut="mouseOutListener(this);"
									 status="waitStatus" width="100%" height="400px" id="aggTree" value="#{aggregationTree.aggTree}" var="item" selectionMode="none">       	
										<richext:treeColumn width="400px">
											<f:facet name="header">
													<h:outputText value="Participant Name/Program Name" escape="false"  />
											</f:facet>
											<h4>
											 <a4j:commandLink value="#{item.participantName}"
												status="waitStatus"
												action="#{aggregationTree.rowSwitchAction}"
												reRender="boy_section">										
												<f:param name="selectedParticipantName" value="#{item.participantName}" />
												<f:param name="selectedProgramName" value="#{item.programName}" />
											</a4j:commandLink>
											<h:outputText value="#{item.participantName!=''?'':item.consolidationProgramName}"  />
											
											
											</h4>																					
										</richext:treeColumn>								
										<rich:column width="400px">
											<f:facet name="header">
												<a4j:region>
													<h:outputText value="Account #" escape="false"  />
												</a4j:region>
											</f:facet>
											<h:outputText value="#{item.accountNo}" />
										</rich:column>
										<rich:column width="400px">
											<f:facet name="header">
												<a4j:region>
													<h:outputText value="Secondary Account #" escape="false"  />
												</a4j:region>
											</f:facet>
											<h:outputText value="#{item.secondaryAccountNo}" />
										</rich:column>
									</richext:treeTable>	
								 </td></tr>
								</table>
							</div>																																							
						</h:panelGroup>						
					</rich:panel>
					<br/>
							<table>
								<tr>
									<td style="background: #3399FF; width:20px; height:15px"></td><td>Current selected participant</td>
								</tr>								
							</table>
					</h:panelGroup> 
				</h:form>

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
				
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>