<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<div id="fm-container">
    <html:form action="/commDevDetail" styleId="fm-form">
        <html:hidden property="userName"/>
        <h3>
            Participant Info <bean:write name="CommDevDetailForm" property="userName"/>
        </h3>
        <input type="hidden" name="dispatch" value=""/>
        <fieldset>
            <legend>
                Account Info
            </legend>
            <div class="fm-req">
                <label for="userName">
                    Participant Name:
                </label>
                <div id="userName">
                    <html:text property="userName" size="40" disabled="true"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="accountNumber">
                    Account Number:
                </label>
                <div id="accountNumber">
                    <html:text property="accountNumber" size="40"/>
                </div>
            </div>
            
            <c:if test="${CommDevDetailForm.secondaryAccountNumberEnabled}">
	            <div class="fm-req">
	                <label for="secondaryAccountNumber">
	                    Secondary Account Number:
	                </label>
	                <div id="secondaryAccountNumber">
	                    <html:text property="secondaryAccountNumber" size="40"/>
	                </div>
	            </div>
            </c:if>
            
        </fieldset>
        <fieldset>
            <legend>
                Contact Info
            </legend>
            <div class="fm-req">
                <label for="firstName">
                    First Name:
                </label>
                <div id="firstName">
                    <html:text property="firstName" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="lastName">
                    Last Name:
                </label>
                <div id="lastName">
                    <html:text property="lastName" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="contact1">
                    Contact Info 1:
                </label>
                <div id="contact1">
                    <html:text property="contact1" size="40"/>
                    <html:select property="contactType1">
                        <html:optionsCollection name="CommDevDetailForm" property="contactTypes" label="name" value="value"/>
                    </html:select>
                </div>
            </div>
            <div class="fm-req">
                <label for="contact2">
                    Contact Info 2:
                </label>
                <div id="contact2">
                    <html:text property="contact2" size="40"/>
                    <html:select property="contactType2">
                        <html:optionsCollection name="CommDevDetailForm" property="contactTypes" label="name" value="value"/>
                    </html:select>
                </div>
            </div>
            <div class="fm-req">
                <label for="contact3">
                    Contact Info 3:
                </label>
                <div id="contact3">
                    <html:text property="contact3" size="40"/>
                    <html:select property="contactType3">
                        <html:optionsCollection name="CommDevDetailForm" property="contactTypes" label="name" value="value"/>
                    </html:select>
                </div>
            </div>
            <div class="fm-req">
                <label for="contact4">
                    Contact Info 4:
                </label>
                <div id="contact4">
                    <html:text property="contact4" size="40"/>
                    <html:select property="contactType4">
                        <html:optionsCollection name="CommDevDetailForm" property="contactTypes" label="name" value="value"/>
                    </html:select>
                </div>
            </div>
            <c:if test="${not empty CommDevDetailForm.externalContacts}">
                <c:forEach items="${CommDevDetailForm.externalContacts}" var="extC">
                    <div class="fm-req">
                        <label for="extContact">
                            External Contact:
                        </label>
                        <div id="extContact">
                            <input type="text" size="40" value="${extC.address}" disabled="true"/> ${extC.type}
                        </div>
                    </div>
                </c:forEach>
            </c:if>
        </fieldset>
        <fieldset>
            <legend>
                Feedback:
            </legend>
            <div class="fm-req">
                <label for="feedback">
                    Feedback:
                </label>
                <div id="feedback" align="left">
                    <html:checkbox property="feedback"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="meterId">
                    Meter ID:
                </label>
                <div id="meterId">
                    <html:text property="meterId" size="40"/>
                </div>
            </div>
        </fieldset>
        <fieldset>
            <legend>
                Location:
            </legend>
            <div class="fm-req">
                <label for="address">
                    Zip Code:
                </label>
                <div id="address">
                    <html:text property="address" size="5"/>
                </div>
            </div>
            <!-- 
            <div class="fm-req">
                <label for="gridLocation">
                    Grid Location:
                </label>
                <div id="gridLocation">
                    <html:text property="gridLocation" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="latitude">
                    Latitude:
                </label>
                <div id="latitude">
                    <html:text property="latitude" size="40"/>
                </div>
            </div>
            <div class="fm-req">
                <label for="longitude">
                    Longitude:
                </label>
                <div id="longitude">
                    <html:text property="longitude" size="40"/>
                </div>
            </div>
             -->
        </fieldset>

    </html:form>
</div>
