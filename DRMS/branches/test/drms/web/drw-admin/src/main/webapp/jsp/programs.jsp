<!doctype html public "-//w3c//dtd html 4.0 transitional//en">  
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<html lang="en-US" xml:lang="en-US">
<f:view>
    <head>
    <jsp:include page="head.jsp" />
    
   	<style>
		.inactive {
				   pointer-events: none;
				   cursor: default;
				   color:gray;
				   text-decoration:line-through;
				}
				
		a.inactive:visited{
					color:gray;
					text-decoration:line-through;
				}
		a.inactive:link{
					color:gray;
					text-decoration:line-through;
				}

	</style>

    </head>
    <body>
    <div id="frame">
        
        
        
        <h:form id="form">
        <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
                <td><jsp:include page="header.jsp"/></td>
            </tr>
        </table>
            <rich:dataTable width="70%" value="" var="item">
                <f:facet name="header">
                    <rich:columnGroup>
                        <rich:column  style="text-align:left;font-size: 13px;">
                            <h:outputText value="Programs"/>
                        </rich:column>
                    </rich:columnGroup>
                </f:facet>
            </rich:dataTable>
            <rich:dataTable id="programsDataTable" value="#{programs.programList}" var="program" width="70%" rows="2">

                <rich:column>
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="No." escape="false"  />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputText value="#{program.index}" />
                </rich:column>
                
                <rich:column>
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Program Name" escape="false"  />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputText value="#{program.programName}" />
                </rich:column>  
                
                <rich:column>
                    <f:facet name="header">
                        <a4j:region>
                            <h:outputText value="Program Class" escape="false"  />
                            <br />
                        </a4j:region>
                    </f:facet>
                    <h:outputText value="#{program.programClass}" />
                </rich:column>  

                <rich:column>
                    <f:facet name="header">
                        <h:outputText value="Actions" />
                    </f:facet>
                    <h:outputLink styleClass="#{program.eventURLStyle}" value="#{program.eventURL}" disabled="#{!programs.programAddEnabled}">
                        <f:verbatim>Add Event</f:verbatim>
                    </h:outputLink>
                    |
                    <h:outputLink  styleClass="#{program.activeEventURLStyle}" value="#{program.activeEventURL}" disabled="#{!programs.programViewEnabled}">
                        <f:verbatim>Active Events</f:verbatim>
                    </h:outputLink>    
                    <a4j:region rendered="#{!program.bipProgram}">           
	                    |
	                    <a>
	                    <h:outputLink  styleClass="#{program.historyEventURLStyle}" value="#{program.historyEventURL}" disabled="#{!programs.programViewEnabled}">
	                        <f:verbatim>History Events</f:verbatim>
	                    </h:outputLink> 
	                    </a>
                    </a4j:region>
                    
                    <a4j:region rendered="#{program.bipProgram}">
	                     |                      
	                    <a>
	                    <h:outputLink   value="/drw.admin/jsp/bip/historyEvt/historyEvents.jsf?programName=BIP" disabled="#{!programs.programViewEnabled}">
	                        <f:verbatim>History Events(Before 2013)</f:verbatim>
	                    </h:outputLink> 
	                    </a>
	                     |
	                    <a>
	                    <h:outputLink  value="/drw.admin/jsp/bip2013/historyEvt/historyEvents.jsf?programName=BIP" disabled="#{!programs.programViewEnabled}">
	                        <f:verbatim>History Events(After 2013)</f:verbatim>
	                    </h:outputLink> 
                   		 </a>
                     
                     </a4j:region>
                </rich:column>
                
            </rich:dataTable>
        <rich:spacer height="15px" width="80px"/>
        <jsp:include page="footer.jsp" />
        
        </h:form> 
        </div>
    </body>
</f:view>
</html>