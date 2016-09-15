<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Map"%>
<%@ page import="com.akuacom.pss2.facdash.FDUtils"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.akuacom.pss2.system.SystemManager"%>
<%@ page import=" com.akuacom.ejb.client.EJB3Factory"%>
<%@ page import=" com.akuacom.pss2.system.property.PSS2Features"%>
<%@ page import="com.akuacom.pss2.facdash.SimpleDashboard"%>
<%@ page import="java.util.List"%>
<%@ page import="java.util.Iterator"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="com.akuacom.utils.BuildProperties"%>
<%@ page import="javax.faces.context.FacesContext"%>
<%@ page import="com.akuacom.pss2.facdash.WeatherWidget"%>
<%@ page import="com.akuacom.pss2.facdash.JSFEvent"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<jsp:include page="clear-session.jsp" />

<html lang="en-US" xml:lang="en-US">
<f:view>
	<head>
		<jsp:include page="head.jsp" />

		<%
			String path = request.getContextPath();
			String basePath = request.getScheme() + "://"
								+ request.getServerName() + ":"
									+ request.getServerPort() + path + "/";
			SystemManager systemManager = (SystemManager)EJB3Factory.getBean(SystemManager.class);
	        PSS2Features features = systemManager.getPss2Features();
	        Boolean enableWeather = Boolean.valueOf(features.isSimpleDashBoardWeatherEnabled());
		%>		
		<link href="css/dashboard.css" rel="stylesheet" type="text/css"/>

		<script type="text/javascript">
		   // the message used in modelPanel confirm box
			   var message = "";
			   function optoutEvent( programName, eventName){
					   message = "Are you sure you want to Opt Out of "+ programName +" event?";
					   document.getElementById('todayMessageBox').innerHTML= message;
					   document.getElementById('deleteForm:eventName').value= eventName;
					   Richfaces.showModalPanel('panel');
					   handleEvent();
					   //:RichFaces.hideModalPanel('form:panel'
			   }
			   function hideOptout(){
					   Richfaces.hideModalPanel('panel');
			   }
			   function handleEvent(){
				   document.getElementById("dialogTitle").focus();
			   }
		</script>

	</head>
	
	<body>
		<div id="frame">
			<h:form id="navForm">
				<a4j:poll id="poll"
					  interval="30000"
					  enabled="true"
					  reRender="weatherDate,poll,currentTemperature,currentHumTemperature,currentHighTemperature,nextWeather,nextHigh,nextLow,currentTempUnit,nextHighUnit,nextLowUnit" />
                <div id="dashboad-container">
				    <jsp:include page="header.jsp" />
				</div>

				<%
				Map m = FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap();
					String ua = (String)m.get("User-Agent");
					boolean isFirefox = ( ua != null && ua.indexOf( "Firefox/") != -1 );
					boolean isMSIE = ( ua != null && ua.indexOf( "MSIE" ) != -1 );

					String partName = FDUtils.getParticipantName();
					int timezoneOffset = (new Date()).getTimezoneOffset() / 60;
					long serverTime = (new Date()).getTime();
					BuildProperties buildProperties = new BuildProperties();

					SimpleDashboard simpleDashboard = (SimpleDashboard) FacesContext
							  .getCurrentInstance().getExternalContext().getRequestMap()
							  .get("simpleDashboard");

					if (simpleDashboard == null) {
						  simpleDashboard = new SimpleDashboard();
						  FacesContext.getCurrentInstance().getExternalContext()
								  .getRequestMap()
								  .put("simpleDashboard", simpleDashboard);
					}

					String dailyEventlistulClass =
					simpleDashboard.getDailyEventlistulClass();
					String dailyEventlistulliClass =
					simpleDashboard.getDailyEventlistulliClass();
					String todayWeatherIcon = null;
                    String tomorrowWeatherIcon = null;
				    if(enableWeather.booleanValue()){
						WeatherWidget weatherWidget = (WeatherWidget) FacesContext
								  .getCurrentInstance().getExternalContext().getRequestMap()
								  .get("weatherWidget");
	
						if (weatherWidget == null) {
							  weatherWidget = new WeatherWidget();
							  FacesContext.getCurrentInstance().getExternalContext()
									  .getRequestMap().put("weatherWidget", weatherWidget);
						}
	
						todayWeatherIcon =weatherWidget.getCurrentWeatherIcon() + ".png";
						tomorrowWeatherIcon =weatherWidget.getNextWeatherIcon() + ".png";
				    }
				%>

				<% if( isFirefox ){ %>
					<br><br><br><br><br>
				<% } %>
				
				<table align="center" border="0">
					<tr>
						<td align="left" >


         
							<!-- *************************************************************** -->

							<rich:toolTip value="#{simpleDashboard.commtime}" for="deviceOffline" />
							<rich:toolTip value="#{simpleDashboard.commtime}" for="deviceOnline" />

							<a4j:keepAlive beanName = "simpleDashboard"/>
							<!--/* Event header begin*/-->
							<h:panelGrid id="simpledash" columns="3" cellpadding="0" cellspacing="0">
								<h:panelGroup styleClass="#{simpleDashboard.hdrLftCss}"	layout="block" id="messageHeaderL">
								</h:panelGroup>
								<h:panelGroup styleClass="#{simpleDashboard.hdrContCss}" layout="block" id="messageHeaderC">
									<rich:spacer height="50px" />
									<span id="events_title" class="Hdr_txt">
										<h:outputText value="#{simpleDashboard.titleMessage}"></h:outputText>
									</span>
									<h:panelGroup styleClass="devicestatus" layout="block"
											id="deviceOnline"
											rendered="#{simpleDashboard.deviceOnLine}"
											style="background: url(images/deviceonline.png) 0
											0  no-repeat;height: 36px;width:144px;_background:none;_FILTER:
											progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/deviceonline.png',
											sizingMethod='crop')">
									</h:panelGroup>
									<h:panelGroup styleClass="devicestatus" layout="block"
											id="deviceOffline"
											rendered="#{simpleDashboard.deviceOnLine==false}"
											style="background: url(images/deviceoffline.png) 0
											0  no-repeat;height: 35px;width:148px;_background:none;_FILTER:
											progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/deviceoffline.png',
											sizingMethod='crop')">
									</h:panelGroup>
								</h:panelGroup>
								<h:panelGroup styleClass="#{simpleDashboard.hdrRightCss}" layout="block" id="messageHeaderR">
								</h:panelGroup>
							</h:panelGrid>
							<!--/* Event header end*/-->

							<!--EventMain content     -->
							<div id="maincont">
								<!--weather module-->
								<%if(enableWeather.booleanValue()){ %>
								<div class="weather">
									<!--weather title bar-->
									<div id="wthertitle_bar">
										<div class="blocktitle">
											<h:outputText value="#{weatherWidget.zipCode}"/>
										</div>
									</div>
									<!-- weather info-->
									<!--today's weather info-->
									<div id="weatherinfo">
										<ul>
											<li class="tday">Today</li>
											<li><h:outputText id="weatherDate" value="#{weatherWidget.currentDate}" /></li>
										</ul>
										<div id="wtherIcon">
											<div id="tdayIcon"
												style="background: url(images/<%=todayWeatherIcon%>)
												10px 0  no-repeat;_background:none;_FILTER:
												progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/<%=todayWeatherIcon%>',
												sizingMethod='crop')">
												<div class="temp">
													<h:outputText id="currentTemperature"  value="#{weatherWidget.currentTemperature}" /><h:outputText id="currentTempUnit" escape="false"  value="#{weatherWidget.unit}" />
												</div>
												<!--updated by Harlin on Feb.15 tag location changed-->
												<div id="tdayWtherInfo">
													<ul>
														<li><h:outputText id="currentHighTemperature" value="#{weatherWidget.currentWeather}" /></li>
														<li>High 
															<h:outputText value="#{weatherWidget.currentHigh}" /> 
															&nbsp Low
															<h:outputText id="currentHumTemperature" value="#{weatherWidget.currentLow}" />
														</li>
														<li>Humidity: <h:outputText value="#{weatherWidget.currenthumidity}" />%</li>
													</ul>
												</div>
											</div>
											<div id="spaceline"><img src="images/spacer.gif" width="1" height="1" alt="space line"/></div>
											<div id="wther_tomorrow">
												<ul>
													<li>Tomorrow</li>
													<li><h:outputText value="#{weatherWidget.nextDate}" /></li>
													<li class="tomorrow"
														style="background:
														url(images/<%=tomorrowWeatherIcon%>) 0 0  no-repeat;height:
														60px;width:80px;_background:none;_FILTER:
														progid:DXImageTransform.Microsoft.AlphaImageLoader(src='../images/<%=tomorrowWeatherIcon%>',
														sizingMethod='crop')"></li>
													<li class="Small"><h:outputText id="nextWeather"
														value="#{weatherWidget.nextWeather}" /></li>
													<li class="Small">High:<h:outputText id="nextHigh"
														value="#{weatherWidget.nextHigh}" /><h:outputText id="nextHighUnit"  escape="false" 
                                                        value="#{weatherWidget.unit}" /></li>
													<li class="Small">Low:<h:outputText id="nextLow"
														value="#{weatherWidget.nextLow}" /><h:outputText id="nextLowUnit"  escape="false" 
                                                        value="#{weatherWidget.unit}" /></li>
												</ul>
											</div>
										</div>
									</div>
								</div>      
								<%} %>
								<!-- Today's usage module-->
								<h:panelGroup id="usage" layout="block" styleClass="usage" rendered="#{simpleDashboard.showUsageGraph}">
									<!-- Today's usage title bar-->
									<div id="usage_bar">
										<div class="blocktitle">Today's Usage</div>
										<script type="text/javascript" src="swfobject.js"></script>
										<script type="text/javascript">
											var flashvars = {
													pdWsURL:"../../pss2-drms-core-ejb-<%=buildProperties.getVersion()%>/UsageDataServicerBean",
													participantName: "<%=partName%>",
													serverTime: "<%=serverTime%>",
													timezoneOffset: "<%=timezoneOffset%>",
													isIndividual:"true",
													plotLabel: "",
													hideControls: "true",
													dataSet1: "Baseline,Baseline",
													dataSet2: "Current Demand,Current Demand"
											};
											swfobject.embedSWF("DRASFlexTelemetryPlot.swf","usageCurve", "290", "280", "9.0.0", null, flashvars, null, null);
										</script>
									</div>
									<div id="usageCurve"></div>
									<!--today's estimated saving block-->
									<h:panelGroup id="estimateSave" layout="block" styleClass="estimateSave" rendered="#{simpleDashboard.showSaving}">
										<div id="estSaving_lft"></div>
										<div id="estSaving_rgt"></div>
										<div id="estSaving_mid">
											<div class="estSavingtext1">Estimated Energy Savings During Today's Event</div>
											<h:panelGroup id="estSavingLabel" layout="block" styleClass="estSavingtext2">
												<h:outputText value="#{simpleDashboard.savingEnergy}">
													<f:convertNumber pattern="#,##0.#" />
												</h:outputText>
												<span class="estSavingtext3">kWh</span>
											</h:panelGroup>
										</div>
									</h:panelGroup>
								</h:panelGroup>
								<!--Events module-->
								<h:panelGroup id="eventlist" layout="block" styleClass="#{simpleDashboard.eventListClass}">
									<div id="evtstitle_bar1">
										<div class="blocktitle">Today's Events</div>							
										<h:panelGroup
							                id="activeBtn"
							                layout="block"
							                rendered="#{simpleDashboard.active}"
							                styleClass="activeBtn">
							                <img src="images/active_new.png" alt="Active Event "
							                    width="21"
							                    height="21"
							                />
							            </h:panelGroup>
							            <h:panelGroup
							                id="pendingBtn"
							                layout="block"
							                rendered="#{simpleDashboard.pending}"
							                styleClass="activeBtn">
							                <img src="images/pending_new.png" alt="Pending Event "
							                    width="21"
							                    height="21"
							                />
							            </h:panelGroup>
									</div>
									<!-- today's events list  todayList-->
									<a4j:keepAlive beanName = "simpleDashboard"/>
									<a4j:keepAlive beanName = "events"/>
									<h:panelGroup id="tdayEventlistWithOptoutButton" layout="block" rendered="#{simpleDashboard.hasTodayList}"  styleClass="#{simpleDashboard.dailyEventlistClass}">
										<ul class="<%=dailyEventlistulClass%>">
											<a4j:repeat value="#{simpleDashboard.todayEvents}" var="eventItem">
												<li class="<%=dailyEventlistulliClass%>">
													<h:outputText value="#{eventItem.programName}"/>
													<h:outputText value=" event #{eventItem.dateTimeForEvent}"/>
												</li>
												<a4j:region rendered="#{simpleDashboard.optout}" >
													<li class="<%=dailyEventlistulliClass%>">
														<h3><a4j:commandButton
																  reRender="simpledash,tdayEventlistWithOptoutButton"
																  onclick="optoutEvent('#{eventItem.programName}','#{eventItem.name}')"
																  styleClass="optBtn"
																  value="Opt-out from this event" title="Opt-out from this event"
																  disabled="#{!simpleDashboard.eventOptoutEnabled}">
														</a4j:commandButton></h3>
													</li>    
												</a4j:region>					
											</a4j:repeat>
										</ul>                 		
									</h:panelGroup>

									<h:panelGroup id="tdayEventlistNoData" layout="block" rendered="#{simpleDashboard.hasTodayList==false}" styleClass="#{simpleDashboard.dailyEventlistClass}">
										<ul class="<%=dailyEventlistulClass%>">
											<li class="<%=dailyEventlistulliClass%>"><h:outputText value="No Data..." /></li>
										</ul>
									</h:panelGroup>
									<!-- Tomorrow's events-->
									<div id="evtstitle_bar2" class="upperline">
										<div class="blocktitle">Tomorrow's Events</div>
										<h:panelGroup id="activeBtnTom" layout="block" rendered="#{simpleDashboard.hasTomorrowList}" styleClass="activeBtn">
											<img src="images/active.gif" alt="Pending Event " width="30" height="21"/>
										</h:panelGroup>
									</div>
									<!--tomorrow's events list-->
									<h:panelGroup id="tomorrowEventlistWithOptoutButton" layout="block" rendered="#{simpleDashboard.hasTomorrowList}" styleClass="#{simpleDashboard.dailyEventlistClass}">
										<ul class="<%=dailyEventlistulClass%>">
											<a4j:repeat value="#{simpleDashboard.tomorrowEvents}" var="eventItem">
												<li class="<%=dailyEventlistulliClass%>">
													<h:outputText value="#{eventItem.programName}"/>
													<h:outputText value=" event #{eventItem.dateTimeForEvent}"/>
												</li>
												<a4j:region rendered="#{simpleDashboard.optout}" >
													<li class="<%=dailyEventlistulliClass%>">
														<h3><a4j:commandButton
																  reRender="simpledash,tomorrowEventlistWithOptoutButton"
																  onclick="optoutEvent('#{eventItem.programName}','#{eventItem.name}')"
																  styleClass="optBtn"
																  value="Opt-out from this event" title="Opt-out from this event"
																  disabled="#{!simpleDashboard.eventOptoutEnabled}">
														</a4j:commandButton></h3>
													</li>  
												</a4j:region>                    
											</a4j:repeat>
										</ul>                 		
									</h:panelGroup>

									<h:panelGroup id="todayEventlistNoData" layout="block" rendered="#{simpleDashboard.hasTomorrowList==false}" styleClass="#{simpleDashboard.dailyEventlistClass}">
										<ul class="<%=dailyEventlistulClass%>">
											<li class="<%=dailyEventlistulliClass%>"><h:outputText value="No Data..." /></li>
										</ul>
									</h:panelGroup>
								</h:panelGroup>
								<!-- configuratable link/RSS feed-->
								<div class="footer_lgt">
									<h:panelGroup id="footerlink" layout="block" rendered="#{simpleDashboard.linkEnable}" styleClass="footerlink">
										<ul class="footerlink_ul">
											<li class="footerlink_ul_li_title">Configurable link</li>
											<li class="footerlink_ul_li">
												<h:outputLink styleClass="footerlink_ul_li_a" value="#{simpleDashboard.link1}">
													<h:outputText	value="#{simpleDashboard.link1Name}" />
												</h:outputLink>
											</li>
											<li class="footerlink_ul_li">
												<h:outputLink styleClass="footerlink_ul_li_a" value="#{simpleDashboard.link2}">
													<h:outputText value="#{simpleDashboard.link2Name}" />
												</h:outputLink>
											</li>
										</ul>
									</h:panelGroup>
									<h:panelGroup id="rssLink" layout="block" rendered="#{simpleDashboard.rssFeedEnable}" styleClass="rsslink">
										<ul class="rsslink_ul">
											<li class="title">Rss Feed</li>
											<li class="rsslink_ul_li"><a class="rsslink_ul_li_a" href="#">Rss link1</a></li>
										</ul>
									</h:panelGroup>
									<div style="clear: both"></div>
								</div>

								<!--trademark icon/copyright-->
								<div  class="footer_lgt" ><jsp:include page="footer.jsp" /></div>
							</div>	
						</td>
					</tr>
				</table>
          
				<!--EventMain end-->

				<!-- *************************************************************** -->

			</h:form>
		</div>
 
		<rich:modalPanel 	id="panel" autosized="false" left="auto" top="auto" overlapEmbedObjects="true" keepVisualState="false" width="315" height="150">
			<h:form id="deleteForm">
				<a id="dialogTitle" href="javascript:void(0);" style="font-weight: bold;">Confirmation dialog</a>
				<rich:panel id="messages">
					<a id="confirmBox" href="javascript:void(0);">
						<span id="todayMessageBox"></span>
						<h:inputHidden id="eventName" value="#{simpleDashboard.eventName}"></h:inputHidden>
					</a>
				</rich:panel>
				
				
				<table border="0">
								<tr>
									<td>
										<h3>
											<a4j:commandButton
													reRender="simpledash,tdayEventlistWithOptoutButton,tomorrowEventlistWithOptoutButton"
													id="yes" value="Yes"
													action="#{simpleDashboard.optoutAction}"
													oncomplete="#{rich:component('panel')}.hide();">
											</a4j:commandButton>
										</h3>
									</td>
									<td>
										<h3>
											<input type="button" value="No" onclick="hideOptout();"/>
										</h3>
									</td>
								</tr>
							</table>
			</h:form>
		</rich:modalPanel>

	</body>
</f:view>
</html>