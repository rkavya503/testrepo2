<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_weather">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<p></p>
<!-- link calendar resources -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/tcal.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/tcal.js"></script> 
<html:form method="post" action="/weatherReport" styleId="fm-form">
    <input type="hidden" name="dispatch" value="list"/>
    <fieldset>
        <legend>Weather</legend>
        <div class="fm-req">      
            <label for="startDate">
                Start Date:
            </label>
            <div id="startDate">
                <html:text property="startDate" size="40" readonly="true" styleClass="tcal"/>				               
            </div>
            <label for="endDate">
                End Date:
            </label>
            <div id="endDate">
                <html:text property="endDate" size="40" styleClass="tcal"/>              
            </div>
        </div>
        <input type="submit" value="Search"/> 
    </fieldset>
    <div class="tablestyle">
        <display:table name="WeatherForm.weatherList" id="weather" pagesize="20" cellspacing="0" cellpadding="0"
            requestURI="weatherReport.do" sort="list" export="true">
            <display:caption title="" media="html">Weather</display:caption>
            <display:column title="Date" sortable="true" media="html">
            	<c:choose>
					<c:when test="${requestScope.admin}">
			            <html:link action="/weatherDetail" paramId="date"
			                paramName="weather" paramProperty="date">
			                <bean:write name="weather" property="date" format="${applicationScope.dateFormat}"/>
			            </html:link>
			        </c:when>
			        <c:otherwise>
			            <bean:write name="weather" property="date" format="${applicationScope.dateFormat}"/>
			        </c:otherwise>
		        </c:choose>
            </display:column>
            <display:column title="Date" sortable="true" media="excel">
                <bean:write name="weather" property="date" format="${applicationScope.dateFormat}"/>
            </display:column>
            <display:column title="High" property="high" sortable="true"/>
            <display:column title="Station" property="reportingStation" sortable="true"/>
            <display:column title="Final" property="isFinal" sortable="true"/>
            <display:column title="Forecast 0" property="forecastHigh0" sortable="true"/>
            <display:column title="Forecast 1" property="forecastHigh1" sortable="true"/>
            <display:column title="Forecast 2" property="forecastHigh2" sortable="true"/>
            <display:column title="Forecast 3" property="forecastHigh3" sortable="true"/>
            <display:column title="Forecast 4" property="forecastHigh4" sortable="true"/>
            <display:column title="Forecast 5" property="forecastHigh5" sortable="true"/>
            <display:setProperty name="paging.banner.item_name" value="Entry"/>
            <display:setProperty name="paging.banner.items_name" value="Entries"/>
        </display:table>

              <html:link 
                      action="../pss2.website/weatherReport.do?dispatch=exportWeather" >
                       Export
              </html:link>

    </div>

  
</html:form>

                  <c:if test="${WeatherForm.nooaWeatherLink}">
                  <br><br><br>

                  <a target="_blank" href="http://www.wrh.noaa.gov/mesowest/getobext.php?table=1&wfo=lox&sid=KCQT" />
                                           Primary:<img src="../pss2.website/images/logos/noaaLogo.png" />
                                        </a>
                                  &nbsp;&nbsp;&nbsp;
                    <br><br>
                  


                 </c:if>