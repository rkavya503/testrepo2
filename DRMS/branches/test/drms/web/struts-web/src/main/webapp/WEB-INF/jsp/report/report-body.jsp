<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>

<html:form method="post" action="/logList" styleId="submitQuery">
<input type="hidden" name="dispatch" value="list"/>
<html:hidden property="page"/>
<html:hidden property="objectsPerPage"/>
<table cellspacing="1">
    <caption>Report Filter</caption>
    <tr>
        <td style="text-align: right;">
            Start Date:
        </td>
        <td>
            <html:text property="startDate" styleId="startDate" size="16"/>
            <script type=text/javascript>
                var cal = new CalendarPopup();
            </script>
            <img align='middle' border='0' hspace='0' vspace='0'
                 src='<%=request.getContextPath()%>/secure/images/cal.gif'
                 id="popupStartDate" style="cursor:pointer;"
                 onclick="cal.select(document.forms['LogFilterForm'].startDate,'popupStartDate','${applicationScope.dateFormat}');"/>
        </td>
        <td style="text-align: right;">
            Start Time:
        </td>
        <td>
            <html:text property="startTime" styleId="startTime" size="16" onchange="updateOptions(this, 'startHour', 'startMinute', 'startSecond')"/>
            <select name="startHour"
                    id="startHour"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'h', 'startTime')">
                <c:forEach var="i" begin="0" end="23" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
            </select>
            <select name="startMinute"
                    id="startMinute"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'm', 'startTime')">
                <c:forEach var="i" begin="0" end="59" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
            </select>
            <select name="startSecond"
                    id="startSecond"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 's', 'startTime')">
                <c:forEach var="i" begin="0" end="59" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
        </select></td>
    </tr>
    <tr class="alt">
        <td style="text-align: right;">
            End Date:
        </td>
        <td>
            <html:text property="endDate" styleId="endDate" size="16"/>
            <script type=text/javascript>
                var cal2 = new CalendarPopup();
            </script>
            <img align='middle' border='0' hspace='0' vspace='0'
                 src='<%=request.getContextPath()%>/secure/images/cal.gif'
                 id="popupEndDate" style="cursor:pointer;"
                 onclick="cal2.select(document.forms['LogFilterForm'].endDate,'popupEndDate','${applicationScope.dateFormat}');"/>
        </td>
        <td style="text-align: right;">
            End Time:
        </td>
        <td>
            <html:text property="endTime" styleId="endTime" size="16" onchange="updateOptions(this, 'endHour', 'endMinute', 'endSecond')"/>
            <select name="endHour"
                    id="endHour"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'h', 'endTime')">
                <c:forEach var="i" begin="0" end="23" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
            </select>
            <select name="endMinute"
                    id="endMinute"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 'm', 'endTime')">
                <c:forEach var="i" begin="0" end="59" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
            </select>
            <select name="endSecond"
                    id="endSecond"
                    size="1"
                    onchange="updateTimeText(this.selectedIndex, 's', 'endTime')">
                <c:forEach var="i" begin="0" end="59" step="1">
                    <option>
                        <bean:write name="i" format="##"/>
                    </option>
                </c:forEach>
            </select>
            <input type="button"
                   value="..."
                   onclick="onClickNowEndTime('endHour', 'endMinute', 'endSecond', 'endTime' );"/>
        </td>
    </tr>

    <tr class="alt">

        <td colspan="3"
            align="right">
            <input type="button"
                    value="Program Report" onclick="return switchReport('reportProgram')"/>
            <input type="button"
                    value="Participant Report" onclick="switchReport('reportParticipant')"/>
            <input type="button"
                    value="Event Report" onclick="switchReport('reportEvent')"/>
        </td>
    </tr>
</table>
<br>

</html:form>

<div id="reportProgram" name="reportProgram" style="display: none;">
   <img id="reportProgramImg"  src="/pss2.website/images/mockup/program_report_mock.gif"/>
</div>
<div id="reportParticipant" name="reportParticipant" style="display: none;">
   <img id="reportParticipantImg"  src="/pss2.website/images/mockup/participant_report_mock.gif"/>
</div>
<div id="reportEvent" name="reportEvent" style="display: none;">
   <img id="reportEventImg"  src="/pss2.website/images/mockup/event_report_mock.gif"/>
</div>



<script type='text/javascript'>

    function switchReport(obj)
    {

	    if(document.getElementById(obj).style.display == 'none'){
	    document.getElementById(obj).style.display = 'block';
	    }
	   if(obj != 'reportProgram')
	   {
	   	document.getElementById('reportProgram').style.display = 'none';
	   }
	   if(obj != 'reportParticipant')
	   	   {
	   	   	document.getElementById('reportParticipant').style.display = 'none';
	   }
	   if(obj != 'reportEvent')
	   	   {
	   	   	document.getElementById('reportEvent').style.display = 'none';
	   }

    }

    function onClickNowEndTime(hour, min, sec, dest) {
        var date = new Date();
        updateOption(hour, date.getHours());
        updateOption(min, date.getMinutes());
        updateOption(sec, date.getSeconds());
        var o = document.getElementById(dest);
        o.value = pad(date.getHours()) + ":" + pad(date.getMinutes()) + ":" + pad(date.getSeconds());
    }

    function updateOption(ctrl, index) {
        var o = document.getElementById(ctrl);
        o.options[index].selected = true;

    }

    function updateOptions(src, hour, min, sec) {
        var arr = src.value.split(":")
        updateOption(hour, arr[0]);
        updateOption(min, arr[1]);
        updateOption(sec, arr[2]);
    }

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
