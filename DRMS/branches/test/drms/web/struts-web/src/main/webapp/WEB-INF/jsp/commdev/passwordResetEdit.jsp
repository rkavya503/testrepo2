<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>

   <h3>
        Participant Info <bean:write name="CommDevDetailForm" property="userName"/>
   </h3>

<html:form action="/commDevDetailReset" styleId="fm-form">

        <html:hidden property="userName"/>
        <html:hidden property="accountNumber"/>


        <input type="hidden" name="dispatch" value="reset"/>
        <html:hidden property="dispatch" value="reset" />
        <html:hidden property="function" value="reset" />

        <table border="0">
               <tr class="alt"><td>
                        <div class="fm-req" align="left">
                           <label for="passwordConf">
                                <jsp:useBean id="CommDevDetailForm" scope="request"
                                             class="com.akuacom.pss2.web.commdev.CommDevDetailForm" />
 
                           </label>
                        </div>
		   </td></tr>
          <html:hidden property="programs"/>

        	  <tr class="alt"><td>
                        <div class="fm-req">
                           <label for="userName">
                               Participant Name:
                           </label>
                           <div id="userName">
                               <html:text property="userName" size="40" disabled="true"/>
                            </div>
                        </div>
		   </td></tr>


        		<tr class="alt"><td>
            	<div class="fm-req">
               <label for="password">
                    Participant Password:
               </label>
                     <html:password property="password" size="40" disabled="false" />
            </div>
            </td></tr>

            <tr class="alt" > <td>
            	<div class="fm-req">
                <label for="password">
                    Confirm Password :
                </label>
                     <html:password property="password2" size="40" disabled="false" />
            </div>
            </td></tr>

         <tr class="alt" > <td>
	         <div id="fm-submit" class="fm-req">
	        	 <input type="submit" value="Reset" onclick="this.form.dispatch.value='rest'"/>
	   		</div>
         </td></tr>

        </table>
    </html:form>