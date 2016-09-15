<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c" %>

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
</style>

<div id="fm-container">
    <h3>
        Create Participant
    </h3>
    <html:form action="/commDevDetail" styleId="fm-form">
        <input type="hidden" name="dispatch" value=""/>
        <fieldset>
            <legend>
                Account Info
            </legend>

			<div style="width:500px;">
			<table id="participantDetailTable" style="font-family: Arial, Verdana, sans-serif;font-size: 12px;font-weight: bold;">
				<tr>
					<td align="right" style="width:200px;">
						<label for="userName" style="font-weight:bold;">
							<span style=";color: red;">*</span>Participant Name:
						</label>
					</td>
					<td align="right" style="width:200px;">
						<html:text property="userName" size="40"/>
					</td>					
                </tr>
				<tr>
					<td align="right" style="width:200px">
						 <label for="password" style="font-weight:bold;">
							<span style=";color: red;">*</span>Password:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:password property="password" size="40"/>
					</td>					
                </tr>
				<tr>
					<td align="right" style="width:200px">
						<label for="password2" style="font-weight:bold;">
							<span style=";color: red;">*</span>Confirm Password:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:password property="password2" size="40"/>
					</td>					
                </tr>
				<tr>
					<td align="right" style="width:200px">
						<label for="accountNumber" style="font-weight:bold;">
							<span style=";color: red;">*</span>Account Number:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:text property="accountNumber" size="40"/>
					</td>					
                </tr>
                
                <c:if test="${CommDevDetailForm.secondaryAccountNumberEnabled}">
                  <tr>
					<td align="right" style="width:200px">
						<label for="secondaryAccountNumber" style="font-weight:bold;">
							Secondary Account Number:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:text property="secondaryAccountNumber" size="40"/>
					</td>					
                </tr>                	          
	          </c:if>
	          <c:if test="${CommDevDetailForm.applicationIdEnabled}">
	         	 <tr>
					<td align="right" style="width:200px">
						<label for="applicationId" style="font-weight:bold;">
							Application Id:
						</label>
					</td>
					<td align="right" style="width:300px">
						<html:text property="applicationId" size="40"/>
					</td>					
                </tr>
                </c:if>
			</table>
			</div>
        </fieldset>
        <div id="fm-submit" class="fm-req">
            <input type="submit" value="Save" onclick="this.form.dispatch.value='save';"/>
            <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel';"/>
        </div>
    </html:form>
</div>
