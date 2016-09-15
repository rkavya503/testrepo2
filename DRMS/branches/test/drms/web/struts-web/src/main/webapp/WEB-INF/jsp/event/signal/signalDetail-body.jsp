<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<script type="text/javascript">
    var optionMap = <%=request.getAttribute("js")%>;
    function updateLevel(signal) {
        var form = signal.form;
        var opts = form["level"].options;
        for (var opt1 in opts) {
            opts.remove(opt1.index);
        }
        var a = optionMap[signal.selectedIndex];
        for (var i = 0; i < a.length; i++) {
            var opt = document.createElement('option');
            opt.text = a[i];
            opt.value = a[i];
            opts.add(opt);
        }
    }
</script>
<html:form action="/signalDetail">
    <html:hidden property="programName"/>
    <html:hidden property="eventName"/>
    <html:hidden property="participantName"/>
    <input type="hidden" name="dispatch" value="save"/>
    <table style="width:600px">
        <caption>
            Add Signal Entry for <br/>
            Program: ${SignalDetailForm.programName},
            Event: ${SignalDetailForm.eventName},
            Comm Dev: ${SignalDetailForm.participantName}
        </caption>
        <tr>
            <td>Date</td>
            <td>
                <html:text property="date"/>
                <script type=text/javascript>
                    var cal = new CalendarPopup();
                </script>
                <img align='middle' border='0' hspace='0' vspace='0'
                     src='<%=request.getContextPath()%>/secure/images/cal.gif'
                     id="EVENT_START_DATE_t" style="cursor:pointer;"
                     onclick="cal.select(document.forms['SignalDetailForm'].date,'EVENT_START_DATE_t','MM/dd/yyyy');"/>
            </td>
        </tr>
        <tr>
            <td>
                Time
            </td>
            <td>
                <html:select property="hour">
                    <html:optionsCollection property="hourList" label="name"/>
                </html:select>
                <html:select property="min">
                    <html:optionsCollection property="minList" label="name"/>
                </html:select>
                <html:select property="sec">
                    <html:optionsCollection property="secList" label="name"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>Signal</td>
            <td>
                <html:select property="signal" onblur="updateLevel(this)" onchange="updateLevel(this)">
                    <html:optionsCollection property="signals" label="name"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>Level</td>
            <td>
                <html:select property="level"></html:select>
                <html:text property="price"/>
            </td>
        </tr>
    </table>
    <div id="fm-submit" class="fm-req">
        <input type="submit" value="Save"/>
    </div>
</html:form>