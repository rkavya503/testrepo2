<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<html:form action="/varoliiTest">
    <html:hidden property="submitted"/>
    <table cellspacing="0">
        <caption>Varolii Notification Test</caption>
        <tbody>
            <tr>
                <td>Contact Type:</td>
                <td>
                    <html:select property="contactType">
                        <option value="Email Address">Email Address</option>
                        <option value="Phone Number">Phone Number</option>
                        <option value="Fax Number">Fax Number</option>
                        <option value="Pager">Pager</option> 
                    </html:select>
                </td>
            </tr>
            <tr>
                <td>Contact:</td>
                <td>
                    <html:text property="contact"/>
                </td>
            </tr>
        </tbody>
    </table>
<div style="text-align: center;">
    <input type="submit" value="Send Notification" onclick="this.form.submitted.value='true'"/>
</div>
</html:form>