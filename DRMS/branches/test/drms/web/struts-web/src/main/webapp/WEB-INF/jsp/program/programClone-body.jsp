<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>
<%@ page import="com.akuacom.pss2.program.participant.ProgramParticipant" %>
<%@ page import="java.util.*" %>
<div id="fm-container">
<script>

function validate(thisform){
	var myTextField = document.ProgramParticipantsForm.programNameClone;

	if( (myTextField.value == null) || (myTextField.value == "") ){
		alert("Enter a clone name: ");
		return false;
	}else{
		this.form.dispatch.value='clone';
		return true;	
	}

}

function passDisable() 
	{
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

</script>

   <h3>
        Program Clone Info 
        <bean:write name="ProgramParticipantsForm" property="username"/>
   </h3>
   

    <html:form action="/program" styleId="fm-form"  >

        <html:hidden property="username"/>
        <html:hidden property="programName"/>
        <input type="hidden" name="dispatch" value="clone"/>
        <html:hidden property="dispatch" value="clone" />
        <html:hidden property="function" value="clone" />
        
        <table border="0"> 
         <tr class="alt"><td> 
        	 <bean:write name="ProgramParticipantsForm" property="programCloneStatus"/>
         </td></tr>
        
        	  <tr class="alt"><td> 
					<div class="fm-req">
					   <label for="programName">
					      Program Name:
					   </label>
					   <div id="programName">
					       <html:text property="programName" size="40" disabled="true"/>
					    </div>
					</div>
			    </td></tr>
			    
			    <tr class="alt"><td> 
					<div class="fm-req">
					   <label for="programNameClone">
					      Clone Program Name:
					   </label>
					   <div id="programNameClone">
					       <html:text property="programNameClone" size="40" disabled="false"/>
					    </div>
					</div>
			    </td></tr>

     
        </table>
        
         <div id="fm-submit" class="fm-req">
         <input type="submit" value="clone" />
   
    </html:form>