<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ page import="com.akuacom.pss2.richsite.Footer"%>
<%@ page import="com.akuacom.utils.config.RuntimeSwitches" %>

<body onload="init()">
  <a4j:region >
  <script type="text/javascript">
      A4J.AJAX.onExpired = function(loc, expiredMsg){
        if(window.confirm("Due to inactivity, your session has timed out. Please log in again.")){
            loc = "../pss2.website/Login";
            return loc
            } else {
             return false;
            }
        }
/*
        A4J.AJAX.onError = function(req,status,message){
            window.alert("Custom onError handler "+message);
        }
*/
	function init(){
		var obj = document.getElementById("logoGraphicImage");
		var obj_src = obj.src;
		var index = obj_src.indexOf("sce_logo.gif");
	 
		if(index<0){
			obj.alt="Logo";
			obj.title="Logo";
		}
	}
  </script>
 </a4j:region>

  <jsp:include page="/jsp/logo.jsp" />
  
 </body>