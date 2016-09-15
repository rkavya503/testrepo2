<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<div id="fm-container">
    <html:form action="/sceAccounts" styleId="fm-form">
        <html:hidden property="userName"/>
        <h3>
            Sub Accounts for <bean:write name="sceAccountForm" property="userName"/>
        </h3>
        <input type="hidden" name="dispatch" value=""/>
        <fieldset>
            <legend>
                Add a New Account
            </legend>
            <div class="fm-req">
                <label for="Account">
                    Account Name:
                </label>
                <div id="Account">
                    <html:text property="sceAccount" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="Comment">
                    Comment:
                </label>
                <div id="Comment">
                    <html:text property="comment" size="40"/>
                </div>
            </div>
        </fieldset>

        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Add" onclick="this.form.dispatch.value='add'"/>
            <input type="submit" value="Done" onclick="this.form.dispatch.value='cancel'"/>
        </div>

        <p/>

        <div class="tablestyle">
            <display:table name="accounts" id="account" cellspacing="0" cellpadding="0" export="true">
                <display:caption>
                    Accounts
                </display:caption>
                <display:column title="">
                    <input type="checkbox" name="accounts" value="${account.subAccountId}"/>
                </display:column>
                <display:column property="subAccountId" title="Account Name"/>
                <display:column property="comment" title="Comment"/>
            </display:table>
        </div>
        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Delete" onclick="this.form.dispatch.value='delete'"/>
        </div>
    </html:form>
</div>
