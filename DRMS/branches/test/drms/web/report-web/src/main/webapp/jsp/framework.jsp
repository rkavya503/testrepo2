<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
	<head>
		<jsp:include page="head.jsp" />
	</head>
	
	<body>
		<div id="frame">
			<table width="100%" cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td><jsp:include page="header.jsp"/></td>
				</tr>
			</table>
			
			<div  style="margin-left:18px; margin-top:5px;width:80%" >
				<jsp:include page="searchFilter.jsp"/>
			</div>
		
			<h:form id="form">
				<table width="100%">
					<tr>
						<td style="padding-left: 10px">
							<rich:tabPanel selectedTab="#{report.selectedTab}" switchType="ajax">
								<rich:tab label="Usage" status ="waitStatus" rendered="#{report.usageEnable}" id="usageTab" reRender="dynamic-filter-block" oncomplete="$('usage-tab-header').focus();">
									<f:facet name="label">
										<h:outputText  
											escape="false"  
											value="<h2 class='heading-level2' ><a id='usage-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Usage<span/></a></h2>"/>
									</f:facet>
									<jsp:include page="usage.jsp" />
								</rich:tab>
								<rich:tab  status ="waitStatus" label="Client Status" id="clientStatusTab" reRender="dynamic-filter-block" oncomplete="$('sts-tab-header').focus();">
									<f:facet name="label">
										<h:outputText  escape="false" 
											value="<h2 class='heading-level2' ><a id='sts-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Client Status<span/></a></h2>"/>
									</f:facet>
									<jsp:include page="clientStatus.jsp" />
								</rich:tab>
								<rich:tab label="Events" status ="waitStatus" id="eventsTab" reRender="dynamic-filter-block" oncomplete="$('evt-tab-header').focus();">
									<f:facet name="label">
										<h:outputText  escape="false" 
											value="<h2 class='heading-level2' ><a id='evt-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Events<span/></a></h2>"/>
									</f:facet>
									<jsp:include page="events.jsp" />
								</rich:tab>
								<rich:tab label="Participation"  status ="waitStatus" id="participationTab" reRender="dynamic-filter-block" oncomplete="$('ep-tab-header').focus();">
									<f:facet name="label">
										<h:outputText  escape="false" 
											value="<h2 class='heading-level2' ><a id='ep-tab-header' tabindex='0' onkeydown='if((window.event|| event).keyCode==13) {_findClickHandler(this)()};'>Participation<span/></a></h2>"/>
									</f:facet>
									<jsp:include page="participation.jsp" />
								</rich:tab>
							</rich:tabPanel>
						</td>
					</tr>
				</table>
				<jsp:include page="footer.jsp" />
			</h:form> 
		</div>		
	</body>
</f:view>
</html>