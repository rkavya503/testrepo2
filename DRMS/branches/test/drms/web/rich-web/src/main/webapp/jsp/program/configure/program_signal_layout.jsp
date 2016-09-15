<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>

<h:panelGroup styleClass="program_property_content" layout="block" >
	<rich:dataTable id="programSignalsDataTable" value="#{programConfigureDataModel.signalConfigureDataModel.signalDefDataModelList}" var="item" width="100%" styleClass="event-rich-table ">		
			
			<rich:column sortable="false" width="100px">
                <f:facet name="header">
                    <h:outputText value="Selected" escape="false"/>
                </f:facet>				
				<h:selectBooleanCheckbox value="#{item.select}" disabled="#{!ProgramDataModel.programSignalEnabled}"/>
			</rich:column>
			
			<rich:column sortable="false" >
				<f:facet name="header">
                    <h:outputText value="Signal Name" escape="false"  />
                </f:facet>
				<h:outputText  value="#{item.signalDef.signalName}">
				</h:outputText>
			</rich:column> 
    </rich:dataTable>	
		
	<a4j:commandButton value="Select All" action="#{programConfigureDataModel.signalConfigureDataModel.selectedAll}" immediate="true" title="Select All" reRender="programSignalsDataTable" disabled="#{!ProgramDataModel.programSignalEnabled}"/>
	<a4j:commandButton value="Select None" action="#{programConfigureDataModel.signalConfigureDataModel.selectedNone}" immediate="true" title="Select None" reRender="programSignalsDataTable" disabled="#{!ProgramDataModel.programSignalEnabled}"/>
	
	
	<div style="clear: both"></div>
                    
                   <div class="programEditSeasonButtonStyle">
                    <a4j:commandButton 
                     title="Save program configuration" action="#{programConfigureDataModel.saveProgramSignalsConfigure}"  value="Save" immediate="false" disabled="#{!ProgramDataModel.programSignalEnabled}"/>
                     
                     <a4j:commandButton
                                       value="Cancel" action="#{programConfigureDataModel.dispatchToProgramPage}" immediate="true" title="Cancel Submit" />
                    </div>  				
</h:panelGroup>

