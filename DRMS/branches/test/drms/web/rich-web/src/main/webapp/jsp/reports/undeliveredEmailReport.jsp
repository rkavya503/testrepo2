<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>

<f:view>
    <head>
		<title>Undelivered Email Report</title>
		<jsp:include page="../head.jsp" />
		
		<style>
		
			select {
				border-color: #C0C0C0;
				font-size: 11px;
				font-family: Arial,Verdana,sans-serif;
				color: #000;
				background-color: #fff;
				zoom:0.8;
				}
		
		</style>
		
		<script type="text/javascript">
		
		var curDt = new Date();

        function disablementFunction(day){
           
			if(curDt.getTime()-day.date.getTime() < 0 ){
				return false;
			} 
			if(curDt.getTime()-day.date.getTime() > 1000*60*60*24*90){
				return false;
			}
			
			return true;
        }
        //function disabledClassesProv(day){
        //    if (curDt.getTime() - day.date.getTime() &gt;= 1000*60*60*24*365*3) return 'rich-calendar-boundary-dates';
		//	if(curDt.getTime()-day.date.getTime() &lt; 0 )return 'rich-calendar-boundary-dates';
        //}
		function disabledClassesProv(day){
			
			if(curDt.getTime()-day.date.getTime() < 0 ){
				return 'rich-calendar-boundary-dates';
			} 
			if(curDt.getTime()-day.date.getTime() > 1000*60*60*24*90){
				return 'rich-calendar-boundary-dates';
			}
			
        }
		
    </script>

    </head>

    <body>
	<h:form>
	<jsp:include page="../header_reports.jsp" />
		
			<a4j:keepAlive beanName="undeliveredEmailReportBean" />
			<h:panelGrid columns="4" styleClass="event-panel-grid">
				<h:panelGroup>
					<p align="left">Start Time:</p>
				</h:panelGroup>						
				<h:panelGroup>
		            <rich:calendar id="startTime"
		                  value="#{undeliveredEmailReportBean.eventTiming.startDate}"  
		                  isDayEnabled="disablementFunction" 
						  dayStyleClass="disabledClassesProv"
		                  datePattern="#{applicationScope.dateFormat}"
		                  popup="true">
		            </rich:calendar>
		            <h:panelGroup>
		                <h:selectOneMenu value="#{undeliveredEmailReportBean.eventTiming.startHour}">
		                    <f:selectItems value="#{undeliveredEmailReportBean.eventTiming.hourList}"/>
		                </h:selectOneMenu>
		                :
		                <h:selectOneMenu value="#{undeliveredEmailReportBean.eventTiming.startMin}">
		                    <f:selectItems value="#{undeliveredEmailReportBean.eventTiming.minList}"/>
		                </h:selectOneMenu>
		            </h:panelGroup>
		        </h:panelGroup>
		        <h:panelGroup>
					<p align="left">End Time:</p>
				</h:panelGroup>	
		        <h:panelGroup>
		            <rich:calendar id="endTime"
		                  value="#{undeliveredEmailReportBean.eventTiming.endDate}"  
		                  isDayEnabled="disablementFunction" 
						  dayStyleClass="disabledClassesProv"
		                  datePattern="#{applicationScope.dateFormat}"
		                  popup="true">
		            </rich:calendar>
		            <h:panelGroup>
		                <h:selectOneMenu value="#{undeliveredEmailReportBean.eventTiming.endHour}">
		                    <f:selectItems value="#{undeliveredEmailReportBean.eventTiming.hourList}"/>
		                </h:selectOneMenu>
		                :
		                <h:selectOneMenu value="#{undeliveredEmailReportBean.eventTiming.endMin}">
		                    <f:selectItems value="#{undeliveredEmailReportBean.eventTiming.minList}"/>
		                </h:selectOneMenu>
		            </h:panelGroup>
		        </h:panelGroup>
		        
		        
				<h:commandButton value="Search" title="Search" action="#{undeliveredEmailReportBean.search}" />
				<h:commandButton value="Export" title="Export" action="#{undeliveredEmailReportBean.export}" />
				<!--<h:commandButton value="Email" title="Email" action="#{undeliveredEmailReportBean.testEmail}" />-->
			</h:panelGrid>
			<rich:dataTable style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );">
				<f:facet name="header4">
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column style="text-align:left;font-size: 15px;">
								<h:outputText value="Undelivered Email Report" />
							</rich:column>
						</rich:columnGroup>		
					</f:facet>
				</f:facet>
			</rich:dataTable>
			<rich:dataTable value="#{undeliveredEmailReportBean.entities}" var="item" style="min-width: 1200px;width:expression(document.body.clientWidth < 1200 ? '1200px' : '100%' );table-layout:fixed;">	 		
				<rich:column >
					<f:facet name="header"><h:outputText value="Time of Email" escape="false" /></f:facet>
					<h:outputText value="#{item.createTime}">
						<f:convertDateTime pattern="MM/dd/yyyy HH:mm:ss" />
					</h:outputText>
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Subject Line" escape="false" /></f:facet>
					<h:outputText value="#{item.subject}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Contact Name" escape="false" /></f:facet>
					<h:outputText value="#{item.contactName}" />
				</rich:column>
				<rich:column style="max-width: 50px;word-wrap: break-word;white-space: -moz-pre-wrap;white-space: pre-wrap; ">
					<f:facet name="header"><h:outputText value="Email Address" escape="false" /></f:facet>
					<h:outputText value="#{item.contactAddress}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Participant Name" escape="false" /></f:facet>
					<h:outputText value="#{item.participantName}" />
				</rich:column>
				<rich:column >
					<f:facet name="header"><h:outputText value="Client Name" escape="false" /></f:facet>
					<h:outputText value="#{item.clientName}" />
				</rich:column>
				
			</rich:dataTable>
			
		</h:form>
		<!--------------------------------------------------- Client Information Table End  --------------------------------------------------->
			<jsp:include page="../footer.jsp" />
    </body>
</f:view>
</html>