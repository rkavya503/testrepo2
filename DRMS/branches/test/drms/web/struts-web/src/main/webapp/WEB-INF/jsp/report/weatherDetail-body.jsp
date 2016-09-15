<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<div id="subpage_report_weather">
	<%@include file="../log/log-sub-tabs.jsp"%>
</div>

<div id="fm-container">
    <h3>
        Edit Weather
    </h3>
    <html:form action="/weatherDetail" styleId="fm-form">
        <input type="hidden" name="dispatch" value=""/>
        <fieldset>
            <legend>
                Weather Info
            </legend>
            <div class="fm-req">
                <label for="date">
                   Date:
                </label>
                <div id="date">
                    <html:text property="date" size="40" disabled="true">
                         <bean:write name="WeatherDetailForm" property="date" format="${applicationScope.dateFormat}"/>
                    </html:text>
                </div>
            </div>
            <div class="fm-req">
                <label for="final">
                    Final:
                </label>
                <div id="final">
                    <html:text property="final" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="final">
                    Reporting Station:
                </label>
                <div id="reportingStation">
                    <html:text property="reportingStation" size="8"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="high">
                    High:
                </label>
                <div id="high">
                    <html:text property="high" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh0">
                    Forecast 0:
                </label>
                <div id="forecastHigh0">
                    <html:text property="forecastHigh0" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh1">
                    Forecast 1:
                </label>
                <div id="forecastHigh1">
                    <html:text property="forecastHigh1" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh2">
                    Forecast 2:
                </label>
                <div id="forecastHigh2">
                    <html:text property="forecastHigh2" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh3">
                    Forecast 3:
                </label>
                <div id="forecastHigh3">
                    <html:text property="forecastHigh3" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh4">
                    Forecast 4:
                </label>
                <div id="forecastHigh4">
                    <html:text property="forecastHigh4" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="forecastHigh5">
                    Forecast 5:
                </label>
                <div id="forecastHigh5">
                    <html:text property="forecastHigh5" size="40"/>
                </div>
            </div>
        </fieldset>
        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Update" onclick="this.form.dispatch.value='update'"/>
            <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel'"/>
        </div>
    </html:form>
</div>
