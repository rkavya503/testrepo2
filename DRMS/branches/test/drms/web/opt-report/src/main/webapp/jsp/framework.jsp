<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
	<head>
	<jsp:include page="head.jsp" />
	<style type="text/css">

	.scroll-body-table TR.row-Selected TD{
		
		filter:alpha(opacity=90); 

	}
	</style>
	
	</head>
	<body>
	<div id="frame">
	<h:form id="header_form">
		<table width="100%" cellpadding="0" cellspacing="0" border="0">
			<tr>
				<td><jsp:include page="header_reports.jsp" /></td>
			</tr>
		</table>
	</h:form>
		<div  style="margin-left:18px; margin-top:5px" width="80%">
			<jsp:include page="searchFilter.jsp"/>
		</div>
	
		<h:form id="form">
		<%-- Rich Tab body block starts--%>
		<table width="100%">
			<tr>
				<td style="padding-left: 10px">
				<rich:tabPanel selectedTab="#{report.selectedTab}" switchType="ajax">
					<rich:tab label="Usage"  status ="waitStatus" rendered="#{report.usageEnable}" id="usageTab" reRender="dynamic-filter-block">
						<jsp:include page="usage.jsp" />
					</rich:tab>
					<rich:tab  status ="waitStatus" label="Client Status" id="clientStatusTab" reRender="dynamic-filter-block">
						<jsp:include page="clientStatus.jsp" />
					</rich:tab>
					<rich:tab label="Events" status ="waitStatus" id="eventsTab" reRender="dynamic-filter-block">
						<jsp:include page="events.jsp" />
					</rich:tab>
					<rich:tab label="Participation"  status ="waitStatus" id="participationTab" reRender="dynamic-filter-block">
						<jsp:include page="participation.jsp" />
					</rich:tab>
				</rich:tabPanel></td>
			</tr>
		</table>
			
		
		<jsp:include page="footer.jsp" />
		</div>
		</h:form> 
	</body>
</f:view>
</html>