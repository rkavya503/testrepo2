<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://akuacom.com/richext" prefix="richext"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
    <head>
    <jsp:include page="head.jsp" />
	
	<style>
		.headerTable {
			width:635px;
		}
		.rich-table-headercell a{
			color:#fff;
		}
	</style>
	<!--[if IE]>
		<link href="css/drw-IE.css" type="text/css" rel="stylesheet" />
	<![endif]-->
    </head>
    <body>
    <div id="frame">
        
        <h:form id="upForm">
            <table width="100%" cellpadding="0" cellspacing="0" border="0">
	            <tr>
	                <td><jsp:include page="header.jsp"/></td>
	            </tr>
        	</table>
			<div id="table" style="padding-left:20px;">
				<rich:dataTable  style="width:835px" styleClass="headerTable" value="" var="item">
	                <f:facet name="header">
	                    <rich:columnGroup>
	                        <rich:column  style="text-align:left;font-size: 13px;">
	                            <h:outputText value="Geographic Configuration"/>
	                        </rich:column>
	                        
	                        <rich:column style="text-align:right;padding-right:41px" >
									<h:commandLink  value="Export" action="#{locationKmlBean.locationKmls.exportHtmlTableToExcel}" />									
                               </rich:column>
	                    </rich:columnGroup>
	                </f:facet>
	            </rich:dataTable>
				<richext:treeTable id="locationKml" value="#{locationKmlBean.locationKmls}" var="item" rows="10000" height="380px	" width="830px" >
					<rich:column width="100px" >
						<f:facet name="header">
							   <h:outputText value="Dispatch Type" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.type}">
						</h:outputText>
					</rich:column>
					<rich:column width="200px" >
						<f:facet name="header">
							   <h:outputText value="Location Name" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.name}">
						</h:outputText>
					</rich:column>
					<rich:column width="90px" >
						<f:facet name="header">
							   <h:outputText value="Location#" escape="false"/>
						   </f:facet>				
						<h:outputText value="#{item.number}"></h:outputText>
					</rich:column>
					<rich:column width="120px" >
						<f:facet name="header">
							   <h:outputText value="KML File Available" escape="false"/>
						   </f:facet>               
						<h:outputText value="#{item.kmlAvailable}" />                                                    
					</rich:column>
					
					<rich:column width="40px" >
						<f:facet name="header">
							   <h:outputText value="Size" escape="false"/>
						   </f:facet>               
						<h:outputText value="#{item.strSize}" />                                                    
					</rich:column>
					
					<rich:column width="180px" >
						<f:facet name="header">
							   <h:outputText value="Load Time" escape="false"/>
						   </f:facet>               
						<h:outputText value="#{item.strCreationTime}" />                                                    
					</rich:column>
				</richext:treeTable>			
			</div>
    			
            <rich:spacer height="15px" width="80px"/>
            <jsp:include page="footer.jsp" />
        </h:form>    
        </div>
    </body>
</f:view>
</html>