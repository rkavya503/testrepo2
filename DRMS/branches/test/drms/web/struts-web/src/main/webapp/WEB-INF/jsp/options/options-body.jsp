<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<logic:notEmpty name="popUpMessage">
    <script type="text/javascript">
        alert("<bean:message key="${popUpMessage}"/>");
        window.location="index.do";
    </script>
</logic:notEmpty>
<div id="subpage_options_password">
	<%@include file="options-sub-links.jsp"%>
</div>
<br/>
<html:form action="/options">
    <input type="hidden" name="dispatch" value="update"/>
    <div class="tablestyle">
        <table cellspacing="0">
            <caption>Change Password</caption>
            <tbody>
                <tr class="alt">
                    <td>UserName</td>
                    <td>
                        <bean:write name="OptionsForm" property="userName"/>
                    </td>
                </tr>
                <tr>
                    <td>Current Password</td>
                    <td>
                        <html:password property="oldPassword" size="40"/>
                    </td>
                </tr>
                <tr class="alt">
                    <td>New Password</td>
                    <td>
                        <html:password property="password" size="40"/>
                    </td>
                </tr>
                <tr>
                    <td>Confirm New Password</td>
                    <td>
                        <html:password property="password2" size="40"/>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
    <div id="fm-submit" class="fm-req">
        <input type="submit" value="Save"/>
        <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel'"/>
    </div>
</html:form>
