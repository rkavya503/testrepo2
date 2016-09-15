<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
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
<div class="tablestyle">
<table cellspacing="0">
      <caption>
          Log Entry Detail
          <fmt:formatDate value="${requestScope.fireLogEntryDetail.logDate}"
                          type="both"
                          pattern="${applicationScope.millsTimeFormat}" />
      </caption>
      <tr>
      	<th width = "30%" >Item</th>
      	<th>Detail</th>
      </tr>
      <tr><td>ID:</td>
      	<td>
              <c:out value="${requestScope.fireLogEntryDetail.userParam3}"/>
          </td>
      </tr>
      <tr>
      	<td>Log Level:</td>
      	<td><c:out value="${requestScope.fireLogEntryDetail.strLogLevel}"/>&nbsp;</td>
      </tr>
      <tr>
      	<td>User:</td>
      	<td><c:out value="${requestScope.fireLogEntryDetail.userName}"/>&nbsp;</td>
      </tr>
      <tr>
      	<td>Category:</td>
      	<td><c:out value="${requestScope.fireLogEntryDetail.category}"/>&nbsp;</td>
      </tr>
      <tr>
      	<td>Program:</td>
      	<td><c:out value="${requestScope.fireLogEntryDetail.userParam1}"/>&nbsp;</td>
      </tr>
    <c:choose>
        <c:when test="${requestScope.userrole == 'Operator'}">
            <tr>
                <td>Description:</td>
                <td>
                    <c:out value="${requestScope.fireLogEntryDetail.description}"/>&nbsp;
                </td>
            </tr>
            <tr>
                <td colspan="2">Long Description:</td>
            </tr>
            <c:if test="${! empty fireLogEntryDetail.longDescr}">
                <tr>
                    <td colspan="2" ><bean:write name="fireLogEntryDetail" property="longDescr" filter="false"/></td>
                </tr>
            </c:if>
        </c:when>
        <c:when test="${requestScope.userrole == 'Admin'}">
            <tr>
                <td>File Name:</td>
                <td><c:out value="${requestScope.fireLogEntryDetail.fileName}"/>&nbsp;</td>
            </tr>
            <tr>
                <td>Line Number:</td>
                <td><c:out value="${requestScope.fireLogEntryDetail.lineNum}"/>&nbsp;</td>
            </tr>
            <tr>
                <td>Class Name:</td>
                <td><c:out value="${requestScope.fireLogEntryDetail.className}"/>&nbsp;</td>
            </tr>
            <tr>
                <td>Method Name:</td>
                <td><c:out value="${requestScope.fireLogEntryDetail.methodName}"/>&nbsp;</td>
            </tr>
            <tr>
                <td>Short Description:</td>
                <td><c:out value="${requestScope.fireLogEntryDetail.description}"/>&nbsp;</td>
            </tr>
            <tr>
                <td colspan="2">Long Description:</td>
            </tr>
            <tr>
                <td colspan="2" ><bean:write name="fireLogEntryDetail" property="longDescr" filter="false"/></td>
            </tr>
        </c:when>
    </c:choose>
</table>
</div>
<div id="fm-submit" class="fm-req">
    <html:form action="/logDetail">
        <input type="hidden" name="dispatch" value="go"/>
        <html:hidden property="index"/>
        <c:if test="${! requestScope.NO_PREVIOUS}">
            <input type="submit" value="Up" onclick="form['index'].value = parseInt(form['index'].value) - 1" />
        </c:if>
        <input type="submit" value="Back" onclick="form.dispatch.value='back'" />
        <c:if test="${! requestScope.NO_NEXT}">
            <input type="submit" value="Down" onclick="form['index'].value = parseInt(form['index'].value) + 1" />
        </c:if>
    </html:form>
</div>
