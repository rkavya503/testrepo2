<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>
<rich:panel styleClass="content-panel">
	<table class="panel-style" width="100%">
		<tr>
			<td valign="top" colspan="2" align="left">
				<table width="100%">
					<tr> 
					<td align="left" width="32%" valign="top">
						<jsp:include page="demoEvtTiming.jsp"/>
					</td>
					<td valign="top" width="68%">
						<jsp:include page="demoEvtSignals.jsp"/>
					</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</rich:panel>
