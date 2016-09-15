<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:form action="/dataCreator" method="post">
    <input type="hidden" name="dispatch"/>
    <p align="top">Properties: <html:textarea property="props" rows="20" cols="80"/></p>
    <html:submit value="Generate" onclick="form.dispatch.value='generate'"/>
</html:form>