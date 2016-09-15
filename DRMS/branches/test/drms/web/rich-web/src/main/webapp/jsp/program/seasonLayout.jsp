<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>


	<div>
			<h:panelGroup styleClass="programEditSeasonPartInfo" layout="block" rendered="true">
				<rich:dataTable width="100%" styleClass="event-rich-table ">		
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column>
								<h:outputText value="Configure Season for #{programConfigureDataModel.programName}"></h:outputText>
							</rich:column>
							<rich:column colspan="1" width="60px" rendered="#{ProgramDataModel.programSeasonEnabled}">
								<a4j:commandButton image="/images/layout/add_device.gif"
									action="#{programConfigureDataModel.seasonConfigureDataModel.addLastSeason}"
									reRender="seasonConfigureDataTable"/>
								<a4j:commandButton reRender="deleteSeasonWarnPanel"
									oncomplete="#{rich:component('deleteSeasonWarnPanel')}.show()" immediate="true"
									image="/images/layout/delete_device.gif"/>
							</rich:column>
						</rich:columnGroup>
					</f:facet>
				</rich:dataTable>
				
				<rich:dataTable id="seasonConfigureDataTable" value="#{programConfigureDataModel.seasonConfigureDataModel.seasons}" var="season" width="100%" styleClass="event-rich-table ">
					<rich:column sortable="false" >
	                    <f:facet name="header">
	                        <a4j:region>
	                            <h:outputText value="Delete" escape="false" />
	                            <br />
	                        </a4j:region>
	                    </f:facet>
	                    <h:selectBooleanCheckbox value="#{season.deleted}" title="Delete Season"/>
					</rich:column> 
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Name" escape="false"  />
						</f:facet>
						<h:selectOneMenu value="#{season.name}">   
							<f:selectItems value="#{season.seasonNameTypes}"/>
						</h:selectOneMenu>
					</rich:column> 
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Start Date" escape="false"/>
						</f:facet>
						<rich:calendar value="#{season.startDate}" required="true" 
										requiredMessage="Required"  popup="true" showApplyButton="false" 
										datePattern="#{programConfigureDataModel.dateFormat}"/>	
					</rich:column>
                    	<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="End Date" escape="false"/>
						</f:facet>
						<rich:calendar value="#{season.endDate}" required="true" 
										requiredMessage="Required"  popup="true" showApplyButton="false" 
										datePattern="#{programConfigureDataModel.dateFormat}"/>
					</rich:column>
				</rich:dataTable>
			
			<h:panelGroup styleClass="programEditSeasonHonlidayInnerPartInfo" layout="block" rendered="true">
			</h:panelGroup>
		
				<rich:dataTable width="100%" styleClass="event-rich-table ">		
					<f:facet name="header">
						<rich:columnGroup>
							<rich:column>
								<h:outputText value="Configure Holiday for #{programConfigureDataModel.programName}"></h:outputText>
							</rich:column>
							<rich:column colspan="1" width="60px" rendered="#{ProgramDataModel.programSeasonEnabled}">
								<a4j:commandButton image="/images/layout/add_device.gif"
									action="#{programConfigureDataModel.seasonConfigureDataModel.addLastHoliday}"
									reRender="holidayConfigureDataTable"/>
								<a4j:commandButton reRender="deleteHolidayWarnPanel,holidayConfigureDataTable"
									oncomplete="#{rich:component('deleteHolidayWarnPanel')}.show()"
									image="/images/layout/delete_device.gif"/>
							</rich:column>
						</rich:columnGroup>
					</f:facet>
				</rich:dataTable>
				
				
				<rich:dataTable id="holidayConfigureDataTable" value="#{programConfigureDataModel.seasonConfigureDataModel.holidays}" 
				var="holiday" width="100%" styleClass="event-rich-table ">
					<rich:column sortable="false" >
	                    <f:facet name="header">
	                        <a4j:region>
	                            <h:outputText value="Delete" escape="false" />
	                            <br />
	                        </a4j:region>
	                    </f:facet>
	                    <h:selectBooleanCheckbox value="#{holiday.deleted}" title="Delete Holiday"/>
					</rich:column> 
				
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Name" escape="false"  />
						</f:facet>
						<h:inputText value="#{holiday.name}" style="width:95%"/>
					</rich:column> 
					<rich:column sortable="false" >
						<f:facet name="header">
							<h:outputText value="Start Date" escape="false"/>
						</f:facet>
						<rich:calendar value="#{holiday.startDate}" required="true" 
										requiredMessage="Required"  popup="true" showApplyButton="false" 
										datePattern="#{programConfigureDataModel.dateFormat}"/>	
					</rich:column>
				</rich:dataTable>
                    <br><br>
                    <div class="programEditSeasonButtonStyle">
                    <a4j:commandButton value="Save" action="#{programConfigureDataModel.seasonConfigureDataModel.saveSeasonConfigure}" disabled="#{!ProgramDataModel.programSeasonEnabled}"/>
                    </div>
			</h:panelGroup>
		

	</div>
 

        