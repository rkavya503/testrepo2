<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html:form action="/dbpSimulator" enctype="multipart/form-data">
    <table cellspacing="0">
        <caption>Program DBP/PDP</caption>
        <tbody>
            <tr>
                <td>Itron XML File:</td>
                <td>
                    <html:file property="xmlFile"/>
                </td>
            </tr>
            <tr>
                <td>Server response:</td>
                <td>
                    <bean:write name="DbpSimulatorForm" property="result" filter="true"/>
                </td>
            </tr>
        </tbody>
    </table>
<div style="text-align: center;">
    <input type="submit" value="Create Event"/>
</div>
</html:form>