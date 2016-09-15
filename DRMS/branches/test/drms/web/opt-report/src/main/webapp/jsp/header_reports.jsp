<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="java.util.Date"%>
<style>

a.viewscorecardlink, a.viewscorecardlink:visited {
color: black;
}
a.viewscorecardlink, a.viewscorecardlink:visited {
color: black;
}
a:visited {
color: #5D819D;
text-decoration: none;
}
a:link {
color: #27577D;
text-decoration: none;
}
.viewscorecardlink {
right: 18px;
position: absolute;
font-weight: bold;
font-family: Arial, Verdana, sans-serif;
font-size: 11px;
cursor: hand;
}

#nav a {
float: none;
font-size: 10px;
}


#nav li {
float: left;
margin: 0;
padding: 0 0 0 9px;
}
user agent stylesheetli {
display: list-item;
text-align: -webkit-match-parent;
}
Inherited from ul#nav
ul#nav {
list-style: none;
}
user agent stylesheetul, menu, dir {
list-style-type: disc;
}
Inherited from div.header
.testserverutilityoperator .header {
font-size: .8em;
line-height: normal;
}

#page_wrapper {
text-align: left;
}
.productionserverutilityoperator, .testserverutilityoperator {
text-align: left;
}

body {
font-family: verdana, arial, sans-serif;
}

#nav #current a {
font-size: 10px;
}
</style>
<a4j:keepAlive beanName="report" />
<a4j:status id="waitStatus" forceId="true"
          onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
           onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
<h:panelGroup styleClass="#{report.headerStyle}" layout="block" id="page_wrapper">
	<div class="header"><br>
<h1>Demand Response Automation Server</h1>

<ul id="nav">

	<li id="programs"><h:outputLink value="/pss2.website/uoProgram.do">
		<f:verbatim> Programs </f:verbatim>
	</h:outputLink></li>

	<li id="participants"><h:outputLink
		value="/pss2.website/participantsMap.do">
		<f:verbatim> Participants </f:verbatim>
	</h:outputLink></li>

	<li id="clients"><h:outputLink value="/pss2.website/clientsMap.do">
		<f:verbatim> Clients </f:verbatim>
	</h:outputLink></li>

	<li id="events"><h:outputLink value="/pss2.website/uoEvent.do">
		<f:verbatim> Events </f:verbatim>
	</h:outputLink></li>

	<a4j:region rendered="#{footer.newsEnabled}">
		<li id="news"><h:outputLink value="/pss2.website/news.do">
			<f:verbatim> News & Info </f:verbatim>
		</h:outputLink></li>
	</a4j:region>

	<li id="current"><h:outputLink value="/pss2.website/logList.do">
		<f:verbatim> Reports </f:verbatim>
	</h:outputLink></li>

	<li id="options"><h:outputLink value="/pss2.website/options.do">
		<f:verbatim> Options </f:verbatim>
	</h:outputLink></li>

	<li id="about"><h:outputLink value="/pss2.website/about.do">
		<f:verbatim> About </f:verbatim>
	</h:outputLink></li>
</ul>

      <h:outputLink id="scorecardlink" target="_blank"
          rendered="#{report.scorecardEnabled}"
          styleClass="viewScorecardLink"
          value="/pss2.utility/jsp/reports/scoreCard.jsf">
            <f:verbatim>Scorecard</f:verbatim>
			<a4j:support ajaxSingle="true" process="scorecardlink" event="onclick" reRender="scorecardlink"/>			
        </h:outputLink>
        
				
<ul id="navright">
	<li>
		<h:commandLink action="#{report.logoutAndCleanSession}" style="font-weight: normal;">
			<f:verbatim>Logout</f:verbatim>
		</h:commandLink>
	</li>
	<li><a href="#"><h:outputText
		value="Welcome #{report.context.welcomeName}" /> </a></li>
	
	<a4j:region rendered="#{report.displayPrice}">
			<li>
				<h:outputText value="#{report.austrialiaPrice}" title="#{report.priceInformation}"/>
			</li>
	</a4j:region>
			
	<li><a href="#"><h:outputText value="#{report.context.serverTime}">
		<f:convertDateTime pattern="#{report.headerDateTimeFormat}" />
	</h:outputText> </a></li>
</ul>
</div>
</h:panelGroup>