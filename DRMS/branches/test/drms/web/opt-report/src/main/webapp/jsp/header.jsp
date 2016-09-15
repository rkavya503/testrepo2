<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN ">
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<script>
function closeWindow(){
	 if(navigator.appVersion.indexOf("MSIE") != -1){
	 	 var version =parseFloat(navigator.appVersion.split("MSIE")[1]);
		if (version == "9" || version == "8" || version == "7") { 
			window.open('', '_self', ''); 
			window.close(); 
		} else if (version == "6") { 
			window.opener = null; 
			window.close(); 
		} else { 
			window.opener = ''; 
			window.close(); // attempt to close window first, show user warning message if fails 
		} 
	}else{
		window.opener = null; 
		window.close(); 
	}
}
</script>

<a4j:keepAlive beanName="report" />
<a4j:status id="waitStatus" forceId="true"
          onstart="javascript:Richfaces.showModalPanel('waitModalPanel');"
           onstop="javascript:Richfaces.hideModalPanel('waitModalPanel');" />
<h:panelGroup styleClass="#{report.headerStyle}" layout="block" id="page_wrapper">
								
	<div class="header"><br>
		<h:outputText style="font-weight:bold;font-size:25"	value="DRAS Operator Report" />
		<div id="navrightDiv">
			<ul id="navright">
			
				<li class="last"><a href="javascript:closeWindow();">Close</a>
				</li>
				
				<li class="topright"><h:outputText
					value="Welcome #{report.context.welcomeName}" />
				</li>
				
				<li class="topright"><h:outputText
					value="#{report.context.serverTime}">
					<f:convertDateTime pattern="#{report.headerDateTimeFormat}"/>
					</h:outputText>
				</li>
			</ul>
		</div>
	</div>
</h:panelGroup>
<a name="skipAim" id="skipAim"></a>
