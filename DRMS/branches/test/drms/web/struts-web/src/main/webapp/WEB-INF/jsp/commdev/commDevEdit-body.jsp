<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ page import="com.akuacom.pss2.program.participant.ProgramParticipant" %>
<%@ page import="java.util.*" %>



<style type="text/css">

#participantDetailTable {
position: relative;
top: -4px;
left: -4px;
width: 100%;
border: solid 0px ;
background: white;
}
#participantDetailTable tr {
background: #FFFFFF;
}
#participantDetailTable td {
margin: 0;
padding: 4px 20px;
text-align: left;
border-bottom: 0px solid;
}

#multibox-fieldset{
font-weight:normal;
}

#multibox-fieldset input{
width: 13px;
}
</style>

<body onload="init()">
<div id="fm-container">
	<script type="text/javascript" src="ckeditor/ckeditor.js"></script>
<script>

function passDisable() {
	if(document.CommDevDetailForm.passEnable.checked)
	{
		document.CommDevDetailForm.password.blur()
		document.CommDevDetailForm.password.disabled=true
		document.CommDevDetailForm.password2.blur()
		document.CommDevDetailForm.password2.disabled=true
	}else{
	document.CommDevDetailForm.password.disabled=false
	document.CommDevDetailForm.password2.disabled=false
	}
}


function setEventOptoutDisable()
{
	if(document.CommDevDetailForm.useDefaultFeatureValue.checked)
	{
		document.CommDevDetailForm.partEventOptoutEnabled.disabled=true;
		document.CommDevDetailForm.partEventOptoutEnabled.checked=document.CommDevDetailForm.defaultEventOptoutEnabled.value=='true';
	}else{
		document.CommDevDetailForm.partEventOptoutEnabled.disabled=false;
	}
}


function serviceSiteIdDivAction(){	
	var obj = document.getElementById("dataEnablerInput");
	var serviceSiteIdDiv = document.getElementById("serviceSiteIdDiv");
	if(obj != null && obj.checked) {
		serviceSiteIdDiv.style.display = "";
	}else {
		serviceSiteIdDiv.style.display = "none";
	} 
}

function userRoleDisbaleAction() {	
	var byName = document.getElementsByName("userRoleEnabled")[0].value;	
	if(byName == 'true') {
		  var inputs = document.getElementsByTagName('input');
		  for(var i = 0; i < inputs.length; i++) {
		      inputs[i].disabled=true;
		  }
		  var selects = document.getElementsByTagName('select');	
		  for(var i = 0; i < selects.length; i++) {
			  selects[i].disabled=true;
		  }				  
		  var anchors = document.getElementById('resetPsw');
		  anchors.onclick=function() {
		  alert('You are not authorized to change password');
		  return false;
		  };
	}
}

function init(){
	userRoleDisbaleAction();	
	setEventOptoutDisable();
    serviceSiteIdDivAction();
}
</script>

   <h3>
        Participant Info <bean:write name="CommDevDetailForm" property="userName"/>
   </h3>


<!-- link calendar resources -->
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/tcal.css" />
	<script type="text/javascript" src="<%=request.getContextPath()%>/tcal.js"></script> 
   <html:form action="/commDevDetail" styleId="fm-form">
		<html:hidden property = "userRoleEnabled" />
        <html:hidden property="userName"/>

        <table border="0">
               <tr class="alt"><td>
                        <div class="fm-req" align="left">
                           <label for="passwordConf">
                                <jsp:useBean id="CommDevDetailForm" scope="request"
                                             class="com.akuacom.pss2.web.commdev.CommDevDetailForm" />
                                  <jsp:getProperty name="CommDevDetailForm" property="passwordConf"/>

                           </label>
                        </div>
		   </td></tr>
        	  <tr class="alt"><td>
                        <div class="fm-req">
                           <label for="userName">
                               Participant Name:
                           </label>
                           <div id="userName">
                               <html:text property="userName" size="40" disabled="true"/>
                            </div>
                        </div>
		   </td>
                  </tr>


                <tr class="alt"><td>
            	<div class="fm-req">
               <label for="password">
                  
               </label>
            <a href="commDevDetailReset.do?dispatch=editPassword&userName=<bean:write name="CommDevDetailForm" property="userName"/>" id="resetPsw">
                       <b>Reset Password</b>
            </a>
            </div>
            </td></tr>

           


        <html:hidden property="userName"/>

        <input type="hidden" name="dispatch" value=""/>
        <fieldset>
		
            <tr class="alt"><td>
	               	<div class="fm-req">
	                <label for="accountNumber">
	                    Account Number:
	                </label>
	                <div id="accountNumber">
	                    <html:text property="accountNumber" size="40"/>
                        <%--<a href="sceAccounts.do?userName=${CommDevDetailForm.userName}">Sub Accounts</a>--%>
	                </div>
	              </div>
            </td></tr>
            
             <c:if test="${CommDevDetailForm.secondaryAccountNumberEnabled}">
	            <tr class="alt"><td>
		               	<div class="fm-req">
		                <label for="secondaryAccountNumber">
		                    Secondary Account Number:
		                </label>
		                <div id="secondaryAccountNumber">
		                    <html:text property="secondaryAccountNumber" size="40"/>
		                </div>
		              </div>
	            </td></tr>
	            
	            <html:hidden property="secondaryAccountNumberEnabled"/>
	          </c:if>
	          
	           <c:if test="${CommDevDetailForm.applicationIdEnabled}">
	            <tr class="alt"><td>
		               	<div class="fm-req">
		                <label for="applicationId">
		                    Application Id:
		                </label>
		                <div id="applicationId">
		                    <html:text property="applicationId" size="40"/>
		                </div>
		              </div>
	            </td></tr>	            
	            <html:hidden property="applicationIdEnabled"/>
	          </c:if>
            
            </fieldset>
       </table>

        
       
        
        	<c:if test="${CommDevDetailForm.featureParticipantInfo}">
		        <fieldset>
		            <legend>
		                Participant Information:
		            </legend>
		            <div class="fm-opt">
		                <label for="customerName">
		                    Customer Name:
		                </label>
					<html:text property="customerName" size="60" readonly="true"/> 
		                <!-- <div id="customerName">
							<c:out value="${CommDevDetailForm.customerName}"/>	                  
		                </div>-->  
					</div>
		            <br/>
		                <div class="fm-opt">
		                <label for=bcdRepName>
		                    BCD Rep Name:
		                </label>
							<html:text property="bcdRepName" size="60" readonly="true"/> 
						</div>
		            <br/>
		            <div class="fm-opt">
		                <label for="serviceStreetAddress">
		                    Service Street Address:
		                </label>
		                <html:text property="serviceStreetAddress" size="60" readonly="true" /> 
		                <!-- <div id="serviceStreetAddress">
							<c:out value="${CommDevDetailForm.serviceStreetAddress}"/>	                    
		                </div>-->
		            </div>
		            <br/>
		            <div class="fm-opt">
		                <label for="serviceCityName">
		                    Service City Name:
		                </label>
		                <html:text property="serviceCityName" size="60" readonly="true" />
		                <!-- <div id="serviceCityName">
							<c:out value="${CommDevDetailForm.serviceCityName}"/>	                    
		                </div> -->
		            </div>
		            <br/>
		            <div class="fm-opt">
		                <label for="zip">
		                    Zip:
		                </label>
		                <html:text property="zip" size="10" readonly="true" /> 
		                <!-- <div id="zip">
							<c:out value="${CommDevDetailForm.zip}"/>	                    
		                </div>-->
		            </div>
		            <br/>
		            <div class="fm-opt">
		                <label for="ABank">
		                    ABank:
		                </label>
		                <html:text property="ABank" size="60" readonly="true" /> 
		                <!-- <div id="ABank">
							<c:out value="${CommDevDetailForm.ABank}"/>	                    
		                </div>-->
		            </div>
		            <br/>
		            <div class="fm-opt">
		                <label for="slap">
		                    SLAP:
		                </label>
					<html:text property="slap" size="60" readonly="true" /> 
		                <!-- <div id="slap">
							<c:out value="${CommDevDetailForm.slap}"/>	                    
		                </div>-->
				</div>
		            <br/>
		            <div class="fm-opt">
		                <label for="PNode">
		                    PNode:
		                </label>
		                <html:text property="PNode" size="60" readonly="true" /> 
		                <!-- <div id="PNode">
							<c:out value="${CommDevDetailForm.PNode}"/>	                    
		                </div>-->
		            </div>
					
					<br/>
		            <div class="fm-opt">
		                <label for="substation">
		                    Substation:
		                </label>
		                <html:text property="substation" size="60" readonly="true" /> 
		            </div>
					<br/>
		            <div class="fm-opt">
		                <label for="blockNumber">
		                    Block Number:
		                </label>
		                <html:text property="blockNumber" size="60" readonly="true" /> 
		            </div>
					<br/>
		            <div class="fm-opt">
		                <label for="programOption">
		                    Program Option:
		                </label>
		                <html:text property="programOption" size="60" readonly="true" /> 
		            </div>
		            <br/>
		            <div class="fm-opt">
		                <label for="servicePlan">
		                    Service Plan:
		                </label>
		                <html:text property="servicePlan" size="60" readonly="true" /> 
		                <!-- <div id="servicePlan">
							<c:out value="${CommDevDetailForm.servicePlan}"/>	                    
		                </div>-->
		            </div>
		            <br/>
		            <div class="fm-opt">            				
            				
		                <label for="rateEffectiveDateStr">
		                    Rate Effective Date:
		                </label>
		                    	<html:text property="rateEffectiveDateStr" styleId="rateEffectiveDateStr" size="16" readonly="true" /> 
		            </div>
		            <br/>
		            <div class="fm-opt">            				
            				
		                <label for="autoDrProfileStartDateStr">
		                    Auto DR Profile Start Date:
		                </label>
		                    	<html:text property="autoDrProfileStartDateStr" styleId="autoDrProfileStartDateStr" size="16" readonly="true" /> 
		            </div>
		             
		            <br/>
		            <div class="fm-opt">
		                <label for="directAccessParticipant">
		                    Direct Access Participant:
		                </label>
					<html:checkbox property="directAccessParticipant" disabled="true" />
				</div>
		            <br/>
		           
	               	<div class="fm-opt">
		                <label for="premiseNumber">
		                    Premise Number:
		                </label>
		                <div id="premiseNumber">
		                    <html:text property="premiseNumber" size="40"/> 
		                </div>
		            </div>
		            <br/>
		            <div class="fm-opt">
            				
			                <label for="startDateStr">
			                    Start Date:
			                </label>
	                    	<html:text property="startDateStr" styleId="startDateStr" size="16" readonly="true" styleClass="tcal" /> 
           					
		            </div>
		            <br/>
		            <div class="fm-opt">
            				
			                <label for="deactivateDateStr">
			                    Deactivate Date:
			                </label>
	                    	<html:text property="deactivateDateStr" styleId="deactivateDateStr" size="16" readonly="true" styleClass="tcal" /> 
           					
		            </div>
                    <br/>
                   <div class="fm-opt">

			                <label for="enrolledDateStr">
			                    Enrollment Date:
			                </label>
	                    	<html:text property="enrolledDateStr" styleId="enrolledDateStr" size="16" readonly="true" styleClass="tcal" />
           					
		             </div>
                     <br/>

                     <div class="fm-opt">
		                <label for="comments">
		                    Comments:
		                </label>
		                <div id="comments">
		            
                            <html:textarea property="comments"  cols="10" rows="4" />
		                </div>
		            </div>
					<br/>
					<div class="fm-opt">
						<label for="items">
		                    Participant type:
		                </label>	
						<div id="multibox-fieldset">						
						<logic:iterate id="item" name="CommDevDetailForm" property="items">
						    <bean:write name="item"/>
							<html:multibox  property="selectedItems">
							    <bean:write name="item"/>
						    </html:multibox>
							&nbsp;&nbsp;&nbsp;&nbsp;
						</logic:iterate>
						</div>
		            </div>		            		            
		            <br/>

		        </fieldset>
		        
        	</c:if>
        	<c:if test="${CommDevDetailForm.featureLocation}">
		        <fieldset>
		            <legend>
		                Location:
		            </legend>
		            <div class="fm-req">
		                <label for="address">
		                    Zip Code:
		                </label>
		                <div id="address">
		                    <html:text property="address" size="5"/>
		                </div>
		            </div>
                    <!-- 
		            <div class="fm-req">
		                <label for="gridLocation">
		                    Grid Location:
		                </label>
		                <div id="gridLocation">
		                    <html:text property="gridLocation" size="40"/>
		                </div>
		            </div>
		            <div class="fm-req">
		                <label for="latitude">
		                    Latitude:
		                </label>
		                <div id="latitude">
		                    <html:text property="latitude" size="40"/>
		                </div>
		            </div>
		            <div class="fm-req">
		                <label for="longitude">
		                    Longitude:
		                </label>
		                <div id="longitude">
		                    <html:text property="longitude" size="40"/>
		                </div>
		            </div>
                     -->
		        </fieldset>
        	</c:if>
        	<c:if test="${CommDevDetailForm.featureParticipantsMapView}">
		        <fieldset>
		            <legend>
		                Geographic Location:
		            </legend>
		            <div class="fm-opt">
		                <label for="latitude">
		                    Latitude:
		                </label>
		                <div id="latitude">
		                    <html:text property="latitude" size="20"/>
		                </div>
		            </div>
		            <div class="fm-opt">
		                <label for="longitude">
		                    Longitude:
		                </label>
		                <div id="longitude">
		                    <html:text property="longitude" size="20"/>
		                </div>
		            </div>
		        </fieldset>
        	</c:if>
        <c:if test="${CommDevDetailForm.adminUser}">
        	        
        	<c:if test="${CommDevDetailForm.featureFeedback}">
		        <fieldset>
		            <legend>
		                Feedback:
		            </legend>
		            <div class="fm-req">
		                <label for="feedback">
		                    Feedback:
		                </label>
		                <div id="feedback" align="left">
		                    <html:checkbox property="feedback"/>
		                </div>
		            </div>
		            <div class="fm-req">
		                <label for="meterId">
		                    Meter ID:
		                </label>
		                <div id="meterId">
		                    <html:text property="meterId" size="40"/>
		                </div>
		            </div>
		        </fieldset>
        	</c:if>
			<c:if test="${CommDevDetailForm.featureClientOfflineNotification}">
		        <fieldset>
		            <legend>
		                Client Offline Notification Setting:
		            </legend>
		            <div class="fm-opt">
		                <label for="optOutClientOfflineNotification">
		                    Opt out :
		                </label>
						<html:checkbox property="optOutClientOfflineNotification"/>
					</div>
					<br/>
					<div class="fm-opt">
		                <label for="clientOfflineNotificationEnable">
		                    Override system thresholds :
		                </label>
						<html:checkbox property="clientOfflineNotificationEnable"/>
					</div>
					<br/>
					<br/>
					<div class="fm-opt">
		                <label for="clientOfflineNotificationAggEnable">
		                    Send Aggregated Client Offline Emails :
		                </label>
						<html:checkbox property="clientOfflineNotificationAggEnable"/>
					</div>
					<br/>
					<br/>
					<br/>
					<div class="fm-opt">
		                <label for="thresholdsSummer">
		                    Thresholds for the summer:
		                </label>
		                <html:text property="thresholdsSummer" size="3" /> hour(s)
					</div>
					<br/>
					<div class="fm-opt">
		                <label for="thresholdsUnSummer">
		                    Thresholds for the winter:
		                </label>
		                <html:text property="thresholdsUnSummer" size="3" /> hour(s)
					</div>
		        </fieldset>
        	</c:if>
        	<c:if test="${CommDevDetailForm.featureShedInfo}">
		        <fieldset>
		            <legend>
		                Shed Info:
		            </legend>
		            <div>
		                <div id="shedPerHourKW">
							<a target="_blank" href="../pss2.utility/shedEdit.jsf?participantName=<bean:write name="CommDevDetailForm" property="userName"/>">
								<b>View & Edit Shed</b>
							</a>
		                </div>
		            </div>
		        </fieldset>
        	</c:if>
        </c:if>
        <c:if test="${CommDevDetailForm.adminUser}">
            <html:hidden property="feedback"/>
            <html:hidden property="meterId"/>
            <html:hidden property="address"/>
            <html:hidden property="gridLocation"/>
            <html:hidden property="latitude"/>
            <html:hidden property="longitude"/>
            <html:hidden property="shedPerHourKW"/>
        </c:if>

        <div class="tablestyle">

            <display:table name="CommDevDetailForm.allPrograms" id="program" cellspacing="0" cellpadding="0">
                <display:caption>
                    Programs for <bean:write name="CommDevDetailForm" property="userName"/>
                </display:caption>
                <!-- program check box  -->
                <display:column title="Program">
                    <input type="checkbox" name="programs" value="${program.name}"
                     <c:if test="${program.value}">checked</c:if>/>
                 </display:column>

                 <!--  client config. check box -->
                 <display:column title="Client Config.">
                  <input type="checkbox" name="clientConfig" value="${program.name}"
                     <c:forEach var="pConfig" items="${CommDevDetailForm.clientsConfig}" varStatus="status">
                         <c:choose>
                                <c:when test="${(pConfig.programName == program.name) && (pConfig.clientConfig == 1) && !(pConfig.participant.client)}">
                                        checked />

                                </c:when>
                                        <c:when test="${ ((pConfig.programName == program.name) && (pConfig.clientConfig == 0) ) && !(pConfig.participant.client) }">
                                                 />
                                  </c:when>
                                <c:otherwise>
                        </c:otherwise>
                        </c:choose>
                     </c:forEach>
                  </display:column>
               <display:column property="name" title="Program Name"/>
           </display:table>

        </div>
		

        <fieldset>
			<div style="width:500px;">
			<table id="participantDetailTable" style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold;">
				<tr>
					<td align="right" style="width:180px;">
						<label style="font-weight:bold;">
							User Type:
						</label>
					</td>
					<td align="right" style="width:180px;">
						<select name="userType">
						   <c:forEach var="item" items="${CommDevDetailForm.userTypeList}" >
										<c:choose>
											<c:when test="${(item == CommDevDetailForm.userType)}">
												 <option selected>
												   <c:out value="${item}" />
												</option>
											</c:when>
											 <c:otherwise>
												<option>
												  <c:out value="${item}" />
												</option>
											  </c:otherwise>
										</c:choose>       
							</c:forEach>
						</select>
					</td>					
                </tr>
				<tr>
					<td align="right" style="width:180px">
						 <label style="font-weight:bold;">
							Installer: 
						</label>
					</td>
					<td align="right" style="width:300px">
						<input type="checkbox" name="installer"  <c:if test="${CommDevDetailForm.installer}">checked</c:if>/>
					</td>					
                </tr>
		
                <tr>
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Use default event opt out setting: 
						</label>
					</td>
					<td align="right" style="width:300px">
						<input type="checkbox" id="useDefaultFeatureValueInput" name="useDefaultFeatureValue" onclick="setEventOptoutDisable();"  
						<c:if test="${CommDevDetailForm.useDefaultFeatureValue}">checked</c:if>/>
					    <input type='hidden' name="defaultEventOptoutEnabled" value="${CommDevDetailForm.defaultEventOptoutEnabled}"/>
					</td>					
                </tr>
                <tr>
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Enable event optout: 
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:checkbox property="partEventOptoutEnabled"/>
					</td>					
                </tr>
                
                 <tr>
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Retained: 
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:checkbox property="retained"/>
					</td>					
                </tr>
                
				<c:choose>
					<c:when test="${CommDevDetailForm.usageData== true}">
				<tr>
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Enable Client Data:
						</label>
					</td>
					<td align="right" style="width:300px">
						<input id="dataEnablerInput" onclick="serviceSiteIdDivAction();"  type="checkbox" name="dataEnabler"  <c:if test="${CommDevDetailForm.dataEnabler}">checked</c:if>/>
					</td>					
                </tr>
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${CommDevDetailForm.usageData== true && CommDevDetailForm.enableDataService== true}">
				<tr id="serviceSiteIdDiv">
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Service Site ID:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:text property="siteID" size="30"/>
					</td>					
                </tr>
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>

				<c:choose>
					<c:when test="${CommDevDetailForm.featureDemandLimiting== true}">
				<tr id="serviceSiteIdDiv">
					<td align="right" style="width:180px">
						<label style="font-weight:bold;">
							Enable Demand Limiting:
						</label>
					</td>
					<td align="right" style="width:300px">
						<input  type="checkbox" name="demandLimiting"  <c:if test="${CommDevDetailForm.demandLimiting}">checked</c:if>/>
					</td>					
                </tr>
					</c:when>
					<c:otherwise></c:otherwise>
				</c:choose>
				</table>
			</div>
        </fieldset>			

		
		
		
        	<c:if test="${CommDevDetailForm.featureParticipantNotes}">
		        <fieldset>
		            <legend>
		                Participant Notes:
		            </legend>
						<html:textarea property="notes"/>
						<script type="text/javascript">
							CKEDITOR.config.removePlugins = 'elementspath';
							CKEDITOR.replace( 'notes',
									{
								skin	:	'office2003',
								toolbar :
								[
									{ name: 'document', items : [ 'Preview','Print','Maximize'] },
									{ name: 'clipboard', items : [ 'Cut','Copy','Paste','PasteText','PasteFromWord','-','Undo','Redo' ] },
									{ name: 'editing', items : [ 'Find','Replace','-','SelectAll'] },
									{ name: 'insert', items : [ 'HorizontalRule','SpecialChar','PageBreak'] },
									'/',
									{ name: 'basicstyles', items : [ 'Bold','Italic','Underline','Strike','Subscript','Superscript','-','RemoveFormat' ] },
									{ name: 'paragraph', items : [ 'NumberedList','BulletedList','-','Outdent','Indent','-','Blockquote','CreateDiv','-','JustifyLeft','JustifyCenter','JustifyRight','JustifyBlock','-','BidiLtr','BidiRtl' ] },
									'/',
									{ name: 'styles', items : [ 'Styles','Format','Font','FontSize' ] },
									{ name: 'colors', items : [ 'TextColor','BGColor' ] },
								]
							});
						</script>
		                <!--  <label for="notesLastUpdateStr">
		                    Notes were last updated
		                </label>-->
		                <c:if test="${CommDevDetailForm.displayParticipantNotesUpdateInfo}">
		                	<div id="notesLastUpdateStr">
								Notes were last updated on <c:out value="${CommDevDetailForm.notesLastUpdateStr}"/>	 by <c:out value="${CommDevDetailForm.notesAuthor}"/>.	                    
		                	</div>
		                </c:if>
				</fieldset>
			</c:if>
        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Update" onclick="this.form.dispatch.value='update'"/>
            <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel'"/>
        </div>
		

    </html:form>
</div>
</body>