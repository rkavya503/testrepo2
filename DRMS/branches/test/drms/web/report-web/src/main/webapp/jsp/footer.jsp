<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

		<script type="text/javascript">
		//<![CDATA[
			A4J.AJAX.onExpired = function(loc, expiredMsg){
				if(window.confirm("Due to inactivity, your session has timed out. Please log in again.")){
					loc = "../pss2.website/Login";
					return loc
				} else {
					return false;
				}
			}
			
			A4J.AJAX.onError = function(req,status,message){
				window.alert("Due to inactivity, your session has timed out. and an error has occured ");
				window.location = './jsp/error.jsf';
			}
			
			function init(){
				var obj = document.getElementById("form:logoGraphicImage");
				var obj_src = obj.src;
				var index = obj_src.indexOf("sce_logo.gif");
			
				if(index<0){
					obj.alt="Logo";
					obj.title="Logo";
				}
			}
			
			Event.observe(window, 'load', function(){
				init();
			});
		//]]>
		</script>

		<jsp:include page="/jsp/logo.jsp" />
  
		<rich:modalPanel id="waitModalPanel" autosized="true" width="200" height="120" moveable="false" resizeable="false">
            <f:facet name="header">
                <h:outputText value="Processing" />
            </f:facet>
            <h:outputText value="Please wait..." />
				<center>
					<h:graphicImage value="/images/ajax-loader.gif" alt="Please wait..."/>
				</center>
		</rich:modalPanel>
