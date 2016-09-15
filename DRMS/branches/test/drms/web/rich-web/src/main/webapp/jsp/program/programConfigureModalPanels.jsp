<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>


	<rich:modalPanel id="deleteSeasonWarnPanel" autosized="false" keepVisualState="false" width="360" height="80">
	            <rich:panel>
	                <h:outputText value="You are about to delete the season(s). Please confirm."/>
	            </rich:panel>
	            <a4j:commandButton value="Confirm Delete" 
					action="#{programConfigureDataModel.seasonConfigureDataModel.removeLastSeason}"
					oncomplete="#{rich:component('deleteSeasonWarnPanel')}.hide();"
					reRender="seasonConfigureDataTable">
	            </a4j:commandButton>
	            <a4j:commandButton value="Cancel" oncomplete="#{rich:component('deleteSeasonWarnPanel')}.hide();"/>
	</rich:modalPanel>	
	<rich:modalPanel id="deleteHolidayWarnPanel" autosized="false" keepVisualState="false" width="360" height="80">
	            <rich:panel>
	                <h:outputText value="You are about to delete the holiday(s). Please confirm."/>
	            </rich:panel>
	            <a4j:commandButton value="Confirm Delete" 
					action="#{programConfigureDataModel.seasonConfigureDataModel.removeLastHoliday}"
					oncomplete="#{rich:component('deleteHolidayWarnPanel')}.hide();"
					reRender="holidayConfigureDataTable">
	            </a4j:commandButton>
	            <a4j:commandButton value="Cancel" oncomplete="#{rich:component('deleteHolidayWarnPanel')}.hide();"/>
	</rich:modalPanel>	
	<rich:modalPanel id="saveWarnPanel" autosized="false" keepVisualState="false" width="315" height="80">
	    <h:form id="saveWarnForm">
	            <rich:panel>
	                <h:outputText value="Press 'Ok' to save the program profile."/>
	            </rich:panel>
	            <a4j:commandButton value="OK" action="#{programConfigureDataModel.saveProgramConfigure}" oncomplete="#{rich:component('saveWarnPanel')}.hide();">
	            </a4j:commandButton>
	            <a4j:commandButton value="Cancel" oncomplete="#{rich:component('saveWarnPanel')}.hide();"/>
	     </h:form>
	</rich:modalPanel>

