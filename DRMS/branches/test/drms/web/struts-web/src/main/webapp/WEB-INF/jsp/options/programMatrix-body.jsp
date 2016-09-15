<%@ page import="com.akuacom.pss2.program.matrix.ProgramMatrixTrig" %>
<%@ page import="com.akuacom.pss2.web.program.ProgramMatrixForm" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.akuacom.pss2.program.matrix.ProgramMatrix" %>
<%@ page import="com.akuacom.pss2.program.testProgram.TestProgram" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.ArrayList" %>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<%
    ProgramMatrixTrig pmt = (ProgramMatrixTrig)request.getAttribute("ProgramMatrixTrig");
    HashMap progs = pmt.getPrograms();
    
    //remove client test 
    if (progs.containsValue(TestProgram.PROGRAM_NAME)){
    	Iterator allkeys = progs.keySet().iterator();
    	while (allkeys.hasNext()){
    		String key=(String)allkeys.next();
    		if (progs.get(key).equals(TestProgram.PROGRAM_NAME)){
    			progs.remove(key);
    			break;
    		}
    	}
    }
    
    List<ProgramMatrix> pms = pmt.getProgramMatrix();
    Iterator keys = progs.keySet().iterator();                                                  
%>
<div id="subpage_options_pmatrix">
	<%@include file="options-sub-links.jsp"%>
</div>
<br/>
<form name="ProgramMatrixForm" method="post" action="/pss2.website/programMatrix.do">
    <input type="hidden" name="dispatch" value=""/>

<div class="tablestyle">

<table id="matrix" cellpadding="0" cellspacing="0">
<caption>
    Program Matrix
</caption>
<thead>
    <tr>
        <th></th>

        <%
        while(keys.hasNext())
        {
            String key = (String)keys.next();
            String name = (String) progs.get(key);
        %>
        <th><%=name%></th>
        <%
        }

        %>
        
    </tr>
</thead>
<tbody>
    <%
        keys = progs.keySet().iterator();
        List<String> pastKeys = new ArrayList<String>();
        while(keys.hasNext())
        {
            String key = (String)keys.next();
            String name = (String) progs.get(key);

    %>
<tr>
    <td><%=name%></td>
    <%
        Iterator key1s = progs.keySet().iterator();
        while(key1s.hasNext())
        {
            String key1 = (String)key1s.next();
            String name1 = (String) progs.get(key1);
            String disabled = "";
            if(! pastKeys.contains(key1))
            {
                String checkedString = "";
                
                if(pmt.coexist(key, key1) || key.equals(key1))
                {
                    checkedString="checked";
					
                }
                if(key.equals(key1))
				{
					disabled = "DISABLED";
				}
				else
				{
					disabled = "";	
				}
               
    %>
        <td>
            <input name="matrixCells" type="checkbox" <%=checkedString%> value='<%=key+"_" + key1%>' <%=disabled%>>
        </td>
    <%
            }
            else
            {
    %>
        <td>
            
        </td>
    <%
            }
        }
        pastKeys.add(key);
    %>
</tr>
    <%
        }
    %>
</tbody>
</table>
</div>
    <div id="fm-submit" class="fm-req">
            <input type="submit" value="Update" onclick="this.form.dispatch.value='update'"/>
            <input type="submit" value="Cancel" onclick="this.form.dispatch.value='cancel'"/>
    </div>
</form>
