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
        Are you sure you want to delete those programs ? 
   </h3>

    <html:form action="/uoProgram" styleId="fm-form"  >
        <input type="hidden" name="dispatch" value="delete"/>
        <html:hidden property="dispatch" value="delete" />
        <html:hidden property="function" value="delete" />

 

            <display:table name="${EventListForm.programList}" id="program"
                       cellspacing="0" cellpadding="0"
               requestURI="uoProgram.do"  >
            <display:column title="Programs to delete" media="html" >
                ${program.programName}
           </display:column>
        </display:table>

           <input type="submit" value="Delete" />
       

           <a href="uoProgram.do?dispatch=cancel" title="Cancel"> Cancel </a>

          <!--
              <a href="uoProgram.do?dispatch=delete" title="Delete" onClick="javascript:document.EventListForm.submit();">
          Delete </a>
          -->


     </html:form>

           



    