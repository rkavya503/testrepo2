<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html" %>
<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fmt.tld" prefix="fmt"%>
<html:form method="post" action="/logList">
    <html:hidden property="page"/>
    <html:hidden property="objectsPerPage"/>
    <html:hidden property="startDate"/>
    <html:hidden property="startTime"/>
    <html:hidden property="endDate"/>
    <html:hidden property="endTime"/>
    <html:hidden property="logLevel"/>
    <html:hidden property="userName"/>
    <html:hidden property="category"/>
    <html:hidden property="program"/>
    <html:hidden property="descriptionWord"/>
    <html:hidden property="logCount"/>
    <html:hidden property="sortBy"/>
    <html:hidden property="sortOrder"/>
    <input type="hidden" name="dispatch" value="list"/>
</html:form>
<script type="text/javascript">
    document.forms["LogFilterForm"].submit();
</script>