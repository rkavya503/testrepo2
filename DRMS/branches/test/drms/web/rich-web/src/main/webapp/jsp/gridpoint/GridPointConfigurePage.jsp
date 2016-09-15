<jsp:root xmlns:jsp="http://java.sun.com/JSP/Page"
	xmlns:f="http://java.sun.com/jsf/core"
	xmlns:a4j="http://richfaces.org/a4j"
	xmlns:rich="http://richfaces.org/rich"
	xmlns:h="http://java.sun.com/jsf/html" version="2.1">
	<jsp:directive.page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" />
<head>
	<jsp:include page="/jsp/head.jsp" />
</head>
<script>

function displayAction(){
	
	var fixScopeEnabledCheckbox = document.getElementById("form:fixScopeEnabledCheckbox");
	var fixScopeValueInputText = document.getElementById("form:fixScopeValueInputText");
	var fixScopeValueOutputText = document.getElementById("form:fixScopeValueOutputText");
	var dateBackScopeOutputText = document.getElementById("form:dateBackScopeOutputText");
	var dateBackScopeInputText = document.getElementById("form:dateBackScopeInputText");
	
	if(fixScopeEnabledCheckbox.checked){
		fixScopeValueInputText.style.display = "";
		fixScopeValueOutputText.style.display = "";
		dateBackScopeInputText.style.display = "none";
		dateBackScopeOutputText.style.display = "none";
	}else{
		fixScopeValueInputText.style.display = "none";
		fixScopeValueOutputText.style.display = "none";
		dateBackScopeInputText.style.display = "";
		dateBackScopeOutputText.style.display = "";
		//dateBackScopeInputText.disabled="false";
	}
}

function defualtDisplay(){

        var fixScopeEnabledRadioVar = document.getElementById("form:fixScopeEnabledRadio:0");
       // fixScopeEnabledRadioVar.checked = true;
        
        var fixBackScopeEnabledRadioVar = document.getElementById("form:backScopeEnabledRadio:0");
       // fixBackScopeEnabledRadioVar.checked = false;
        
        var fixScopeEnabledCheckbox = document.getElementById("form:fixScopeEnabledCheckbox");
        var fixScopeValueInputText =  document.getElementById("form:fixScopeValueInputText");
        var fixScopeValueOutputText = document.getElementById("form:fixScopeValueOutputText");
        var dateBackScopeOutputText = document.getElementById("form:dateBackScopeOutputText");
        var dateBackScopeInputText =  document.getElementById("form:dateBackScopeInputText");

        fixScopeValueInputText.style.display = "";
        fixScopeValueOutputText.style.display = "";
        dateBackScopeInputText.style.display = "none";
        dateBackScopeOutputText.style.display = "none";
}

function fixedScopeAction(){

        var fixScopeEnabledCheckbox = document.getElementById("form:fixScopeEnabledCheckbox");
        var fixScopeValueInputText =  document.getElementById("form:fixScopeValueInputText");
        var fixScopeValueOutputText = document.getElementById("form:fixScopeValueOutputText");
        var dateBackScopeOutputText = document.getElementById("form:dateBackScopeOutputText");
        var dateBackScopeInputText =  document.getElementById("form:dateBackScopeInputText");

        fixScopeValueInputText.style.display = "";
		fixScopeValueOutputText.style.display = "";
		dateBackScopeInputText.style.display = "none";
		dateBackScopeOutputText.style.display = "none";
          var fixScopeEnabledRadioVar = document.getElementById("form:fixScopeEnabledRadio:0");
        //fixScopeEnabledRadioVar.checked = true;
        var fixBackScopeEnabledRadioVar = document.getElementById("form:backScopeEnabledRadio:0");
       // fixBackScopeEnabledRadioVar.checked = false;
}

function dataBackScopeAction(){

        var fixBackScopeEnabledRadioVar = document.getElementById("form:backScopeEnabledRadio:0");

        if (fixBackScopeEnabledRadioVar.checked){
                var fixScopeEnabledCheckbox = document.getElementById("form:fixScopeEnabledCheckbox");
                var fixScopeValueInputText =  document.getElementById("form:fixScopeValueInputText");
                var fixScopeValueOutputText = document.getElementById("form:fixScopeValueOutputText");
                var dateBackScopeOutputText = document.getElementById("form:dateBackScopeOutputText");
                var dateBackScopeInputText =  document.getElementById("form:dateBackScopeInputText");

                fixScopeValueInputText.style.display = "";
                fixScopeValueOutputText.style.display = "";
                dateBackScopeInputText.style.display = "none";
                dateBackScopeOutputText.style.display = "none";
        }else{
               var fixScopeEnabledCheckbox = document.getElementById("form:fixScopeEnabledCheckbox");
               var fixScopeValueInputText =  document.getElementById("form:fixScopeValueInputText");
               var fixScopeValueOutputText = document.getElementById("form:fixScopeValueOutputText");
               var dateBackScopeOutputText = document.getElementById("form:dateBackScopeOutputText");
               var dateBackScopeInputText =  document.getElementById("form:dateBackScopeInputText");

               fixScopeValueInputText.style.display = "none";
               fixScopeValueOutputText.style.display = "none";
               dateBackScopeInputText.style.display = "";
               dateBackScopeOutputText.style.display = "";
        }
}


function init(){
     defualtDisplay();
}


</script>
<body onload="init()">
<f:view>
<jsp:include page="/jsp/header_options.jsp" />
	<h:form id="form">
		

		<rich:spacer height="5px" width="400px"/>
		
		<rich:panel style="background-color: #FFFFFF;border-color: #BED6F8;" id ="content-panel" >
			
			<h:messages layout="table" globalOnly="false" infoClass="global-message-info" warnClass="global-message-warn" errorClass="global-message-error" fatalClass="global-message-fatal"/>
			
			<rich:panel style="background-color: #ECF4FE;border-color: #BED6F8;" >GridPoint Data Service Configuration</rich:panel>
            
			<rich:spacer height="20px" width="400px" />
			
			<h:panelGroup id="gridPanel">
				<table width="700px" cellSpacing="0" cellPadding="0" >
					<tr>
						<td>
							<h:outputLabel value="Authentication " style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: bold;"/>
						</td>
					</tr>
					<tr>
						<td width="20%">
							<h:outputLabel value="*Webservice URL:" style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							<h:inputText style="width:400px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
										 value="#{gridPointConfigureBean.dataModel.authenticationURL}"/>
						</td>
					</tr>
					<tr>
						<td width="20%">
							<h:outputLabel value="*User Name:" style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							<h:inputText style="width:80px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
										 value="#{gridPointConfigureBean.dataModel.username}"/>
						</td>
					</tr>
					<tr>					
						<td width="20%">
							<h:outputLabel value="*Password:" style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							<h:inputSecret style="width:80px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
										 value="#{gridPointConfigureBean.dataModel.password}"
										 redisplay="true"
										 />
						</td>
					</tr>	
					
					<tr/>
					
					<tr>
						<td>
							<h:outputLabel value="Retrieve Data " style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: bold;"/>
						</td>
					</tr>
					<tr>
						<td width="20%">
							<h:outputLabel value="*Webservice URL:" style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							<h:inputText style="width:400px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
										 value="#{gridPointConfigureBean.dataModel.retrieveDataURL}"/>
						</td>
					</tr>
					<tr>
						<td width="20%">
							<h:outputLabel value="*Time Interval(min):" style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							<h:inputText style="width:80px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
 										 value="#{gridPointConfigureBean.timeIntervalString}">
							</h:inputText>
						</td>
					</tr>
                    <tr colspan="2"><td width="90%" colspan="2">
                            <h:selectOneRadio
                                              style="font-size: 11px; font-weight: normal;text-align:left"
                                              value="#{gridPointConfigureBean.controlState}"
                                              onclick="dataBackScopeAction()"
                                              id="backScopeEnabledRadio"
                                             >
                                              <a4j:support event="onclick"
                                                reRender="gridPanel"
                                                ajaxSingle="true"
                                                />
                                    <f:selectItem itemValue="fixed"   itemLabel="*Fix Scope" />
                                    <f:selectItem itemValue="lastValid" itemLabel="*Since last valid" />
                            </h:selectOneRadio>
                    </td></tr>
					<tr id="tr1">
						<td width="20%">
                         
								<h:outputLabel 	id="fixScopeValueOutputText" 
												value="*Fix Scope Value(min):" 
												style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
								<h:inputText 	id="fixScopeValueInputText" 
												style="width:80px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
												value="#{gridPointConfigureBean.fixScopeValueString}">
								</h:inputText>
						</td>
					</tr>
					<tr id="tr2">
						<td width="20%">
							<h:outputLabel 	id="dateBackScopeOutputText"
											value="*Maximum days of meter data backfill:"
											style="width:120px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
						</td>
						<td width="80%" >
							
							<h:inputText  id="dateBackScopeInputText" style="width:80px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
										  value="#{gridPointConfigureBean.dateBackScopeString}"
										  >
							</h:inputText>
							  
						</td>
					</tr>
				</table>
				
				<rich:spacer height="10px" width="100%" />
				
				<h:commandButton value="Save Configuration" title="Save GridPoint Data Service Configuration" 
								style="text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
								action="#{gridPointConfigureBean.mergeConfigureDataModel}"/>
								
				<rich:spacer height="20px" width="100%" />				
				<rich:separator height="1" lineType="solid"/><br/>
				
				<rich:tabPanel  width="80%"
                                selectedTab="authTestTab"
                                tabClass="tab-titles"
                                styleClass="content-tab"
                                immediate="true"
                                switchType="ajax"
                                >
                    <rich:tab  switchType="client"
                                     id="authTestTab" reRender="false"
                                     label="Test Authentication Configuration">
                         
						
						<rich:panel style="background-color: #FFFFFF;border-color: #BED6F8;"  >
							<h:outputLabel value="GridPoint Authentication Result:" style="width:300px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: bold;"/>
							<rich:dataTable width="100%" styleClass="event-rich-table " value="#{gridPointConfigureBean.authenticationResults}" var="item">		
									
									<rich:column sortable="false" >
										<f:facet name="header">
											<h:outputText value="Authentication Result" escape="false"  />
										</f:facet>
										<h:outputText value="#{item}">
										</h:outputText>
									</rich:column>									
							</rich:dataTable>	
							
							<br/>
							
							<h:commandButton value="Test Authentication" title="Test Authentication" 
								style="text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
								action="#{gridPointConfigureBean.testAuthntication}"/>
						</rich:panel>
					</rich:tab>

                   
					<rich:tab  	immediate="true"
                                id="retrieveDataTestTab"
                                reRender="false"
                                switchType="client"
                                label="Test Retrieve Configuration" >
								
                        
						<rich:panel style="background-color: #FFFFFF;border-color: #BED6F8;"  >
							<h:outputLabel value="GridPoint Data Retrieve Result:" style="width:300px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: bold;"/>
							<rich:dataTable width="100%" styleClass="event-rich-table " value="#{gridPointConfigureBean.retrieveDataResults}" var="item">		
									
									<rich:column sortable="false" >
										<f:facet name="header">
											<h:outputText value="Time" escape="false"  />
										</f:facet>
										<h:outputText value="#{item.time}">
										</h:outputText>
									</rich:column>
									
									<rich:column sortable="false" >
										<f:facet name="header">
											<h:outputText value="Value" escape="false"  />
										</f:facet>
										<h:outputText value="#{item.value}">
										</h:outputText>
									</rich:column>									
							</rich:dataTable>	
							
							<br/>
							
							<table width="300px" cellSpacing="0" cellPadding="0">
								<tr>
									<td width="70px">
										<h:outputLabel value="*Site ID:" style="width:60px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
									</td>
									<td width="180px" >
										<h:inputText style="width:160px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
													 value="#{gridPointConfigureBean.testSiteId}"/>
									</td>
								</tr>
								<tr>
									<td width="70px">
										<h:outputLabel value="*End Time:" style="width:60px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>
									</td>
									<td width="180px" >
										<h:inputText style="width:160px; font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"
													 value="#{gridPointConfigureBean.testEndTimeString}"/>
									</td>
									<td >
										<h:outputLabel value="(yyyy-MM-dd'T'HH:mm:ss)" style="width:150px;text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>			 
									</td>
								</tr>
							</table>
							
							<br/>	
							
							<h:commandButton value="Test Retrieve Data" title="Test Retrieve Data" 
							action="#{gridPointConfigureBean.testRetrieveData}"
							style="text-align:left;font-family: Arial, Verdana, sans-serif; font-size: 11px; font-weight: normal;"/>	
						</rich:panel>
					</rich:tab>

                </rich:tabPanel>
				
				
				
				
			</h:panelGroup>
			
        </rich:panel>
    </h:form>
</f:view>
</body>
</jsp:root>
