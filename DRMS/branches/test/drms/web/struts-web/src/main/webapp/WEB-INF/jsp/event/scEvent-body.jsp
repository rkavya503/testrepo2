<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html:form action="/scEventDetail">
<html:hidden property="programName"/>
<input type="hidden" name="dispatch" value="save"/>
    <table>
        <caption>
            Program <bean:write name="SCEventDatailForm" property="programName"/>
        </caption>
        <tr>
            <td valign='top' width='300'>
                Event Date:
            </td>
            <td>
                <html:text property="eventDate" size="12"/>
                <script type=text/javascript>
                    var cal = new CalendarPopup();
                </script>
                <img align='middle' border='0' hspace='0' vspace='0'
                     src='<%=request.getContextPath()%>/secure/images/cal.gif'
                     id="EVENT_START_DATE_t" style="cursor:pointer;"
                     onclick="cal.select(document.forms['SCEventDatailForm'].eventDate,'EVENT_START_DATE_t','MM/dd/yyyy');"/>
            </td>
        </tr>
        <tr>
            <td>
                Start Time:
            </td>
            <td>
                <html:select property="startHour">
                    <html:optionsCollection property="hourList" label="name"/>
                </html:select>:
                <html:select property="startMin">
                    <html:optionsCollection property="minList" label="name"/>
                </html:select>:
                <html:select property="startSec">
                    <html:optionsCollection property="secList" label="name"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>End Time:</td>
            <td>
                <html:select property="endHour">
                    <html:optionsCollection property="hourList" label="name"/>
                </html:select>:
                <html:select property="endMin">
                    <html:optionsCollection property="minList" label="name"/>
                </html:select>:
                <html:select property="endSec">
                    <html:optionsCollection property="secList" label="name"/>
                </html:select>
            </td>
        </tr>
        <tr>
            <td>Percentage:</td>
            <td><html:text property="shedAmount"/></td>
        </tr>
        <tr>
            <td>Baseline Strategy:</td>
            <td>
                <html:select property="shedStrategy">
                    <html:option value="THREE_TEN"/>
                    <html:option value="THREE_TEN_MA"/>
                </html:select>
            </td>
        </tr>
    </table>
    <div style="text-align: center;">
        <input type="submit" value="Create Event" onclick="onSave();"/>
        <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel';"/>
    </div>
</html:form>
