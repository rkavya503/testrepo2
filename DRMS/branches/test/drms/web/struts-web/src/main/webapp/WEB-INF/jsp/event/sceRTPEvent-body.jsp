<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html:form action="/sceRTPEventDetail">
<html:hidden property="programName"/>
<input type="hidden" name="dispatch" value="save"/>
    <table>
        <caption>
            Program <bean:write name="SCERTPEventDetailForm" property="programName"/>
        </caption>
     </table>
    <div style="text-align: center;">
        <input type="submit" value="Create Event" onclick="onSave();"/>
        <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel';"/>
    </div>
</html:form>
