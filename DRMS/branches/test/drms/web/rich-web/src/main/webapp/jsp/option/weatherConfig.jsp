<!doctype html public "-//w3c//dtd html 4.0 transitional//en">
<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>

<html>
<f:view>
    <head>
    <title>Weather Configuration</title>
    <jsp:include page="../head.jsp" />
    </head>
    <body>
		<jsp:include page="../header_options.jsp" />
        <h:form>
	        
	        <rich:panel
	            styleClass="about-content-panel; font:normal 9px arial"
	            style="align: left">
	            <rich:messages id="dispatchMessages" layout="table" globalOnly="false" 
	                       infoClass="global-message-info" warnClass="global-message-warn"
	                       errorClass="global-message-error" fatalClass="global-message-fatal" />
	            <BR></BR>
	            <f:facet name="header">
	                <h:outputText value="Configuration Parameters" />
	            </f:facet>
	
		        <h:panelGroup layout="block" id="country-config-block">
		             <fieldset>
		                <legend>
		                    <b>Weather Configuration</b>
		                </legend>
		                <h:panelGrid columns="1" width="85%" border="0">
		
		                    <h:panelGroup>
		                         <h:outputText value="Country: " />
		                        <h:panelGroup>
		                            <h:selectOneMenu value="#{weatherConfig.country}">
		                                <f:selectItems value="#{weatherConfig.countries}"/>
		                                <a4j:support  event="onchange" action="#{weatherConfig.update}" reRender="detail_block"/>
		                            </h:selectOneMenu>                          
		                        </h:panelGroup>
		                    </h:panelGroup>
		                        
		                   </h:panelGrid>
		                </fieldset>
		        </h:panelGroup>
		        
		        <h:panelGroup  id="detail_block">
			        <h:panelGroup layout="block" id="aus-config-block" rendered="#{weatherConfig.showAus}">
			             <fieldset>
			                <legend>
			                    <b>AUS Server Configuration</b>
			                </legend>
			                <h:panelGrid columns="4" width="85%" border="0">
			                    <h:panelGroup>
			                         <h:outputText value="State/territory: " />
			                        <h:panelGroup>
			                            <h:selectOneMenu value="#{weatherConfig.state}">
			                                <f:selectItems value="#{weatherConfig.states}"/>
			                            </h:selectOneMenu>                          
			                        </h:panelGroup>
			                    </h:panelGroup>
			                    
			                     <h:panelGroup>
			                        <h:outputText value="City: " />
			                        <h:inputText value="#{weatherConfig.city}" />
			                     </h:panelGroup>
			
			                   </h:panelGrid>
			                </fieldset>
			        </h:panelGroup>
			        
			        <h:panelGroup layout="block" id="us-config-block" rendered="#{weatherConfig.showUs}">
			             <fieldset>
			                <legend>
			                    <b>US Server Configuration</b>
			                </legend>
			                <h:panelGrid columns="1" width="85%" border="0">
			                     <h:panelGroup>
			                        <h:outputText value="Zipcode: " />
			                        <h:inputText value="#{weatherConfig.zipcode}" />
			                     </h:panelGroup>
			
			                   </h:panelGrid>
			                </fieldset>
			        </h:panelGroup>
					
					<h:panelGroup layout="block" id="uk-config-block" rendered="#{weatherConfig.showUk}">
			             <fieldset>
			                <legend>
			                    <b>UK Server Configuration</b>
			                </legend>
			                <h:panelGrid columns="1" width="85%" border="0">
								<h:panelGroup>
			                        <h:outputText value="City Display Name: " />
			                        <h:inputText value="#{weatherConfig.displayCityNameOfUK}" />
			                    </h:panelGroup>
								<h:panelGroup>
			                        <h:outputText value="Selected Forecast Name: " />
									<h:outputText value="#{weatherConfig.selectedForecastName}" />
			                    </h:panelGroup>
								<h:panelGroup>
			                        <h:outputText value="Matched Observation Name: " />
									<h:outputText value="#{weatherConfig.matchedObservationName}" />
			                    </h:panelGroup>
			                     <h:panelGroup>
			                        <h:outputText value="Forecast Search: " />
			                        <h:inputText value="#{weatherConfig.ukCitySearch}" />
									<h:commandButton action="#{weatherConfig.searchUKCity}" value="Search"/>
			                     </h:panelGroup>
								<h:panelGroup>
			                        <h:outputText value="Search Result: " />
			                        <h:panelGroup>
			                            <h:selectOneMenu value="#{weatherConfig.selectedUKCity}">
			                                <f:selectItems value="#{weatherConfig.searchUKCityResult}"/>
			                            </h:selectOneMenu>                          
			                        </h:panelGroup>
			                    </h:panelGroup>
			                   </h:panelGrid>
			                </fieldset>
			        </h:panelGroup>
					
					
		        </h:panelGroup>
		        
		        <h:panelGrid columns="1" width="15%" border="0">
		                         <h:commandButton action="#{weatherConfig.save}"
		                             value="Save"/>
		        </h:panelGrid>
	            
	        </rich:panel>
        </h:form>
        <br/>
        <jsp:include page="../footer.jsp" />        
    </body>
</f:view>
</html>
