<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>

<div id="subpage_report_logs">
	<%@include file="log-sub-tabs.jsp"%>
</div>

<c:if test="${requestScope.admin}">
    <form action="logDetail.do" method="post" id="fm-form">
        <input type="hidden" name="dispatch" value="quick"/>
        <fieldset>
            <legend>
                Quick Reference
            </legend>
            <div class="fm-req">
                <label for="id">
                    <span style=";color: red;">*</span>ID:
                </label>
                <div id="id">
                    <input type="text" name="id"/> <input type="submit" value="Go"/>
                </div>
            </div>
        </fieldset>
    </form>
</c:if>
<!-- link calendar resources -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/tcal.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/tcal.js"></script> 
<html:form method="post" action="/logList" styleId="submitQuery">
<input type="hidden" name="dispatch" value="list"/>
<html:hidden property="page"/>
<html:hidden property="objectsPerPage"/>
<html:hidden property="serverTime"/>

<table cellspacing="1">
    <caption>Filter Query</caption>
    <tr>
        <td style="text-align: right;">
            Start Date:
        </td>
        <td>
            <html:text property="startDate" styleId="startDate" size="16" readonly="true" styleClass="tcal"/>
           
        </td>
        <td style="text-align: right;">
            Start Time:
        </td>
        <td>
            <html:text property="startTime" styleId="startTime" size="16" onchange="updateOptions(this, 'startHour', 'startMinute', 'startSecond')"/>
            <html:select property="startHour"
                    styleId="startHour"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'h', 'startTime')">
                <html:optionsCollection property="hourList" label="name"/>
            </html:select>
            <html:select property="startMinute"
                    styleId="startMinute"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'm', 'startTime')">
                <html:optionsCollection property="minList" label="name"/>
            </html:select>
            <html:select property="startSecond"
                    styleId="startSecond"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 's', 'startTime')">
                <html:optionsCollection property="minList" label="name"/>
            </html:select></td>
    </tr>
    <tr class="alt">
        <td style="text-align: right;">
            End Date:
        </td>
       
        <td>
            <html:text property="endDate" styleId="endDate" size="16" readonly="true" styleClass="tcal"/>
        </td>
        <td style="text-align: right;">
            End Time:
        </td>
        <td>
            <html:text property="endTime" styleId="endTime" size="16" onchange="updateOptions(this, 'endHour', 'endMinute', 'endSecond')"/>
            <html:select property="endHour"
                    styleId="endHour"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'h', 'endTime')">
                <html:optionsCollection property="hourList" label="name"/>
            </html:select>
            <html:select property="endMinute"
                    styleId="endMinute"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'm', 'endTime')">
                <html:optionsCollection property="minList" label="name"/>
            </html:select>
            <html:select property="endSecond"
                    styleId="endSecond"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 's', 'endTime')">
                <html:optionsCollection property="minList" label="name"/>
            </html:select>
            <input type="submit"
                    value="Now" onclick="this.form.dispatch.value = 'now'"/>
        </td>
    </tr>
    <tr>
        <td style="text-align: right;">
            Level:
        </td>
        <td>
            <html:select property="logLevel">
                <html:optionsCollection property="logLevels" label="name" value="value"/>
            </html:select>
        </td>
        <td style="text-align: right;">
            User Name:
        </td>
        <td>
            <html:text property="userName" styleId="userName" size="16"/>
        </td>
    </tr>
    <tr class="alt">
        <td style="text-align: right;">
            Category:
        </td>
        <td colspan="3">
            <html:radio property="categoryRadio" value="builtin" onclick="this.checked=true;flipContrl('catBuiltin', 'catCustom')"/>
            <html:select property="builtInCategory" styleId="catBuiltin" title="click the radio button to enable">
                <html:option value="--"/>
                <html:optionsCollection property="categoryOptions" label="name"/>
            </html:select>
            <html:radio property="categoryRadio" value="costom" onclick="this.checked=true;flipContrl('catCustom', 'catBuiltin')"/>
            <html:text property="category" styleId="catCustom" size="16" disabled="true" title="click the radio button to enable"/>
        </td>
    </tr>
    <tr>
        <td style="text-align: right;">
            Program:
        </td>
        <td>
            <html:select property="program">
                <html:option value="--"/>
                <html:optionsCollection property="programOptions" label="name"/>
            </html:select>
        </td>
        <td style="text-align: right;">
            Description Word:
        </td>
        <td>
            <html:text property="descriptionWord" styleId="descriptionWord" size="16"/>
        </td>
    </tr>
    <tr class="alt">
        <td colspan="2" align="left">
            <input type="button" value="Clear Query" onclick="this.form.reset();onClickNowEndTime('endHour', 'endMinute', 'endSecond', 'endTime' );"/>
        </td>
        <td colspan="2"
            align="right">
            <input type="submit"
                    value="Run Query" onclick="this.form.page.value = 1"/>
        </td>
    </tr>
</table>
<br>
<display:table name="${LogFilterForm.logPageList}" id="element" cellspacing="0" cellpadding="0"
    requestURI="logList.do" export="true">
    <display:setProperty name="basic.show.header" value="false"/>
    <display:setProperty name="basic.empty.showtable" value="false"/>
    <display:caption>
        <thead>
            <tr>
                <th>Date and Time</th>
                <th>ID</th>
                <th>Level</th>
                <th>User</th>
                <th>Category</th>
                <th>Program</th>
                <th>Description</th>
            </tr>
        </thead>
    </display:caption>
    <display:column media="html">
        <a href="logDetail.do?id=${element.id}&index=${element_rowNum - 1}">
            <bean:write name="element" property="logDate" format="${applicationScope.millsTimeFormat}"/>
        </a>
    </display:column>
    <display:column title="Date and Time" media="excel">
        <bean:write name="element" property="logDate" format="${applicationScope.millsTimeFormat}"/>
    </display:column>
    <display:column property="userParam3"/>
    <display:column property="strLogLevel"/>
    <display:column media="html">
        <bean:write name="element" property="userName"/>&nbsp;
    </display:column>
    <display:column title="User" media="excel">
        <bean:write name="element" property="userName"/>
    </display:column>
    <display:column property="category"/>
    <display:column media="html">
        <bean:write name="element" property="userParam1"/>&nbsp;
    </display:column>
    <display:column title="Program" media="excel">
        <bean:write name="element" property="userParam1"/>
    </display:column>
    <display:column property="description"/>
</display:table>
</html:form>
<script type='text/javascript'>
    function onClickNowEndTime(hour, min, sec, dest) {
        var date = new Date();
        date.setTime(document.getElementById('serverTime'));
        updateOption(hour, date.getHours());
        updateOption(min, date.getMinutes());
        updateOption(sec, date.getSeconds());
        var endH = document.getElementById('endHour');
        endH.value = date.getHours();
        endH.options[date.getHours()].selected = true;
        var endM = document.getElementById('endMinute');
        endM.value = date.date.getMinutes();
        endM.options[date.date.getMinutes()].selected = true;
        var endS = document.getElementById('endSecond');
        endS.value = date.getSeconds();
        endS.options[date.getSeconds()].selected = true;
        var o = document.getElementById(dest);
        o.value = pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds());
    }

    function updateOption(ctrl, index) {
        var o = document.getElementById(ctrl);
        o.options[index].selected = true;
    }

    function updateOptions(src, hour, min, sec) {
        var arr = src.value.split(":");
        updateOption(hour, arr[0]);
        updateOption(min, arr[1]);
        updateOption(sec, arr[2]);
    }
    
    onClickNowEndTime('endHour', 'endMinute', 'endSecond', document.getElementById('endTime'));
    updateOptions(document.getElementById('startTime'), 'startHour', 'startMinute', 'startSecond');
    updateOptions(document.getElementById('endTime'), 'endHour', 'endMinute', 'endSecond');


    function pad(value) {
        return value < 10? "0"+value : value;
    }

    function updateTimeText(val, type, dest) {
        var str = document.getElementById(dest).value;
        if( type == 'h') {
            str = str.replace(/^\d\d/, pad(val));
        } else if( type == 'm' ) {
            str = str.replace(/:\d\d:/, ":" + pad(val) + ":");
        } else if( type == 's' ) {
            str = str.replace(/\d\d$/, pad(val));
        }
        document.getElementById(dest).value = str;
    }

    function flipContrl(s1, s2) {
        var current = document.getElementById(s1);
        current.disabled = false;
        var other = document.getElementById(s2);
        other.disabled = true;
    }

</script>
