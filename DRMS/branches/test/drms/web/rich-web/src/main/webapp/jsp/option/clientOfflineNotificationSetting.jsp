<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<a4j:region>	
<h:panelGroup layout="block">
	<fieldset>
		<legend><b>Client Offline Summer Notification Setting:</b></legend>			
			<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
				<tr>
					<td><h:outputText value="Notification Time:"/></td>
					<td>
						<h:selectOneMenu value="#{timerConfigBackingBean.summerConfig.invokeHour}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}" >
							<f:selectItems value="#{timerConfigBackingBean.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{timerConfigBackingBean.summerConfig.invokeMin}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}" >
							<f:selectItems value="#{timerConfigBackingBean.minList}"/>
						</h:selectOneMenu>
					</td>
				</tr>
				<tr>
					<td><h:outputText value="Summer Start From:"/></td>
					<td>
						<h:selectOneMenu 	style="width:40px;"
									title=""
									id="selectOneMenu_summerStartMonth"
									value="#{timerConfigBackingBean.summerConfig.startMonth}"
									onkeydown="if((event || window.event).keyCode==13) {this.onchange();return false}"
									valueChangeListener="#{timerConfigBackingBean.startMonthChange}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}" >                     
									<f:selectItems value="#{timerConfigBackingBean.startMonthItems}"/> 
									<a4j:support event="onchange" reRender="selectOneMenu_summerStartDay" ajaxSingle="true" limitToList="true"/> 
						</h:selectOneMenu>
						month
						<h:selectOneMenu 	style="width:40px;"
											id="selectOneMenu_summerStartDay" 
											value="#{timerConfigBackingBean.summerConfig.startDay}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">                     
											<f:selectItems value="#{timerConfigBackingBean.startDayItems}"/> 
						</h:selectOneMenu> 						
						day
					</td>
				</tr>
				<tr>
					<td><h:outputText value="Summer End To:"/></td>
					<td>
						<h:selectOneMenu 	style="width:40px;"
									title=""
									id="selectOneMenu_summerEndMonth"
									value="#{timerConfigBackingBean.summerConfig.endMonth}"
									valueChangeListener="#{timerConfigBackingBean.endMonthChange}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">                     
									<f:selectItems value="#{timerConfigBackingBean.endMonthItems}"/> 
									<a4j:support event="onchange" reRender="selectOneMenu_summerEndDay" ajaxSingle="true" limitToList="true"/> 
						</h:selectOneMenu>
						month
						<h:selectOneMenu 	style="width:40px;"
											id="selectOneMenu_summerEndDay" 
											value="#{timerConfigBackingBean.summerConfig.endDay}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">                     
											<f:selectItems value="#{timerConfigBackingBean.endDayItems}"/> 
						</h:selectOneMenu> 						
						day
					</td>
				</tr>
				<tr>
					<td><h:outputText value="Thresholds:"/></td>
					<td>
						<h:inputText style="width:40px;" value="#{timerConfigBackingBean.summerThreshold}" required="true" requiredMessage="Thresholds for the summer is required. Please enter a valid value." disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">
						</h:inputText> hour(s)
					</td>
				</tr>
			</table>
	</fieldset>
	<fieldset>
		<legend><b>Client Offline Winter Notification Setting:</b></legend>
			<table style="font-family: Arial, Verdana, sans-serif;font-size: 11px;font-style: normal;font-variant: normal;font-weight: bold;">
				<tr>
					<td><h:outputText value="Notification Time:"/></td>
					<td>
						<h:selectOneMenu value="#{timerConfigBackingBean.winterConfig.invokeHour}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">
							<f:selectItems value="#{timerConfigBackingBean.hourList}"/>
						</h:selectOneMenu>
						:
						<h:selectOneMenu value="#{timerConfigBackingBean.winterConfig.invokeMin}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">
							<f:selectItems value="#{timerConfigBackingBean.minList}"/>
						</h:selectOneMenu>
					</td>
				</tr>
				<tr>
					<td><h:outputText value="Notification Day:"/></td>
					<td>
						<h:selectOneMenu value="#{timerConfigBackingBean.winterConfig.day}" disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}">
							<f:selectItems value="#{timerConfigBackingBean.dayList}"/>
						</h:selectOneMenu>
					</td>
				</tr>
				<tr>
					<td><h:outputText value="Thresholds:"/></td>
					<td>
						<h:inputText style="width:40px;" value="#{timerConfigBackingBean.winterThreshold}" required="true" requiredMessage="Thresholds for the winter is required. Please enter a valid value." disabled="#{!timerConfigBackingBean.clientOfflineSummerNotificationEnabled}"/> hour(s)
					</td>
				</tr>
			</table>
	</fieldset>
	<h:commandButton value="Save" action="#{timerConfigBackingBean.updateNotificationAction}" rendered="#{timerConfigBackingBean.clientOfflineSummerNotificationEnabled}"/>	
</h:panelGroup>
</a4j:region>