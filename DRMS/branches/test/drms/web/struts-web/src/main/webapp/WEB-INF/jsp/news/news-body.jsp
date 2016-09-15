<%@ taglib prefix="bean" uri="http://struts.apache.org/tags-bean"%>
<%@ taglib prefix="html" uri="http://struts.apache.org/tags-html"%>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>



<html:form action="/news" >
<body onload="initalizeFrame()">
<iframe id="newsFrame"  src="" frameborder="0" width="100%" height="1200" scrolling="auto"></iframe>
<html:hidden  property = "newsUrl" />
</body>
</html:form>

<script type='text/javascript'>

function initalizeFrame ()
{

 var byName = document.getElementsByName("newsUrl")[0].value;
 document.getElementById("newsFrame").src = byName;
 
}

</script>





