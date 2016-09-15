<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.akuacom.pss2.facdash.Header1" %>
<%@ page import="com.akuacom.pss2.facdash.FDUtils" %>
<%@ page import="java.util.Date" %>
<%@ page import="com.akuacom.utils.BuildProperties" %> 

<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<html lang="en-US" xml:lang="en-US">
	<f:view>
		<head>
			<jsp:include page="head.jsp" />
			<script type="text/javascript">
			//<![CDATA[
				
				var tomorrowEnabled = <%=FDUtils.isTelemetryTomorrowEnabled()%>;
				
				
				function isSameDay(date1, date2){
					return ((date1.getFullYear()==date2.getFullYear())&&(date1.getMonth()==date2.getMonth())&&(date1.getDate()==date2.getDate()));
				}
				var startDate = new Date();
					startDate.setFullYear(1900+Number(<h:outputText value="#{irrUsage.dateRange.startTime.year}" />) ,
										  <h:outputText value="#{irrUsage.dateRange.startTime.month}" />,
										  <h:outputText value="#{irrUsage.dateRange.startTime.date}" />);
					startDate.setHours(0);
					startDate.setMinutes(0);
					startDate.setSeconds(0);
					startDate.setMilliseconds(0);
										  
					var endDate = new Date();
					endDate.setFullYear(1900+Number(<h:outputText value="#{irrUsage.dateRange.endTime.year}" />) ,
										<h:outputText value="#{irrUsage.dateRange.endTime.month}" />,
										<h:outputText value="#{irrUsage.dateRange.endTime.date}" />);
					endDate.setHours(23);
					endDate.setMinutes(59);
					endDate.setSeconds(59);
					endDate.setMilliseconds(999);
								
				function disablementFunction(day){
					
					//enable today
					
					var today = new Date();
					if(isSameDay(day.date,today)) return true;
					
					if(tomorrowEnabled){
						var tomorrow = new Date(+new Date()+24*3600000);
						if(isSameDay(day.date,tomorrow)) return true;
					}

					var flag = true;
					var today = new Date();

					if (day.date<startDate || day.date>endDate)
					{
						flag = false;
					}				

					// disable when return false
					return flag;
				}
				
				function disabledClassesProv(day){
					
					var today = new Date();
					if(isSameDay(day.date,today)) return;
					
					if(tomorrowEnabled){
                        var tomorrow = new Date(+new Date()+24*3600000);
                        if(isSameDay(day.date,tomorrow)) return;
                    }
					
					
					if (day.date<startDate||day.date>endDate)
					{
						return 'rich-calendar-boundary-dates';
					}
				}
				//]]>
			</script>
		</head>
		<body>
			<div id="frame">
			<h:form id="form">
		<jsp:include page="header.jsp" />
		<h:inputHidden id="showRawData" value="#{irrUsage.showRawData}">	</h:inputHidden>
		<h:inputHidden id="disableAggregatedData" value="#{irrUsage.disableAggregatedData}">				
		</h:inputHidden>
		
		<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton id="refreshButton" style="display:none;">
													<a4j:support event='onclick' actionListener="#{irrUsage.getReports}"
													 reRender="dayAvgBase,dayTotalBase,dayAvgActual,dayTotalActual,dayAvgShed,dayTotalShed"></a4j:support>
											</a4j:commandButton>
										</h3>
									</td>
								</tr>
		</table>
		 <span style="margin: 1em 0;padding-left: 0.8em;color: #16557F;font-size: 2em;font-weight: bold;">
                <h:outputText value="#{irrUsage.participantName}" /> 
            </span>
		<br/>
		<rich:spacer height="30px" width="100%" />		
	<h:panelGroup layout="block" id="usageBodyBlock" >
	<a4j:region>	
		<fieldset>
			<legend><b>Data Time Range</b></legend>
				<h:outputLabel value="Date: " styleClass="form-field" />&nbsp;&nbsp;&nbsp;
				<rich:calendar popup="true" id="datepicker" value="#{irrUsage.date}" datePattern="#{applicationScope.dateFormat}" reRender="usageBodyBlock" 
					  isDayEnabled="disablementFunction" dayStyleClass="disabledClassesProv" >
					<a4j:support event="onchanged" actionListener="#{irrUsage.refresh}" reRender="usageBodyBlock"></a4j:support>
				</rich:calendar>								
				&nbsp;&nbsp;&nbsp;
				&nbsp;
								
		</fieldset>
	</a4j:region>
	<br/>

	<rich:panel styleClass="usage-table">
		<rich:dataTable width="100%" id="usage" title="Entire Day "
			value="#{irrUsage.reports}" var="p" >
			<f:facet name="header4">
				<h:outputText value="Entire Day " />
				<f:facet name="header">
					<rich:columnGroup>
						<rich:column colspan="1">
						</rich:column>
						<rich:column colspan="2">
							<h:outputText value="Baseline" />
						</rich:column>
						<rich:column colspan="2">
							<h:outputText value="Telemetry" />
						</rich:column>
						<rich:column colspan="2">
							<h:outputText value="Shed" />
						</rich:column>
					</rich:columnGroup>
				</f:facet>
			</f:facet>
			<rich:column width="100px" colspan="1" styleClass="center-column" >
				<h:outputText value="#{p.name}"
					style="font-weight:bold" />
			</rich:column>
			<rich:column width="100px" styleClass="right-column" colspan="1">
				<f:facet name="header">
					<h:outputText value="Avg(KW)" title="Avg" />
				</f:facet>
				<h:outputText id="dayAvgBase" value="#{p.baseAvg==0?'':p.baseAvg}" >
					 <f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>				
			<rich:column width="100px" colspan="1" styleClass="right-column">
				<f:facet name="header">
					<h:outputText value="Total(KWH)" title="total" />
				</f:facet>
				<h:outputText id="dayTotalBase" value="#{p.baseAvg==0?'':p.baseTotal}" >
					<f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>
			<rich:column width="100px"  styleClass="right-column">
				<f:facet name="header">
					<h:outputText value="Avg(KW)" title="Avg" />
				</f:facet>
				<h:outputText id="dayAvgActual" value="#{p.actualAvg==0?'':p.actualAvg}" >
					<f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>				
			<rich:column width="100px" styleClass="right-column">
				<f:facet name="header">
					<h:outputText value="Total(KWH)" title="total" />
				</f:facet>
				<h:outputText id="dayTotalActual" value="#{p.actualAvg==0?'':p.actualTotal}">
					 <f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>
			<rich:column width="100px" styleClass="right-column">
				<f:facet name="header">
					<h:outputText value="Avg(KW)" title="Avg" />
				</f:facet>
				<h:outputText id="dayAvgShed" value="#{(p.actualAvg==0||p.baseAvg==0)?'':p.shedAvg}">
				 <f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>
			<rich:column width="100px"  styleClass="right-column">
				<f:facet name="header">
					<h:outputText value="Total(KWH)" title="total" />
				</f:facet>
				<h:outputText id="dayTotalShed" value="#{(p.actualAvg==0||p.baseAvg==0)?'':p.shedTotal}">
				 <f:convertNumber pattern = "#,##0.00"/>
				</h:outputText>
			</rich:column>
		</rich:dataTable>
	</rich:panel>
	<rich:spacer></rich:spacer>
	<br/>
	<rich:panel styleClass="content-panel">
		<script type="text/javascript" src="swfobject.js"></script>
		<script type="text/javascript">     
		var flashvars = {
				  pdWsURL: "<h:outputText value="#{irrUsage.pdWsURL}" />",
				  participantName: "<h:outputText value="#{irrUsage.participantName}" />",
				  selectedDate: "<h:outputText value="#{irrUsage.selectDate}" />",
				  isIndividual: '<h:outputText value="#{irrUsage.individualparticipant}" />'
				};   
			var params = {};            
            params.wmode = "transparent"; 
				
			swfobject.embedSWF("IRRDemo.swf", "usageCurve", "98%", "700", "9.0.0", null, flashvars, params, null);
			function refreshShowRawData(showRawData){
				document.getElementById("form:showRawData").value = showRawData;
				document.getElementById("form:refreshButton").onclick();
			}
			function refreshShowAggData(disableAggregatedData){
				document.getElementById("form:disableAggregatedData").value = disableAggregatedData;
				document.getElementById("form:refreshButton").onclick();
			}
			
			
		</script>		
		<div id="usageCurve"></div>	
        <div>
            <hr/>
             <h:outputText value="#{irrUsage.disclaimer}" styleClass="panel-style"/>
            <br/>
            <h:outputText value="* indicates a participant not attend this activity" styleClass="panel-style"/>
            <br/>
        </div>
		
	</rich:panel>	
					</h:panelGroup>
				</h:form>
				<jsp:include page="footer.jsp" />
			</div>
		</body>
	</f:view>
</html>
