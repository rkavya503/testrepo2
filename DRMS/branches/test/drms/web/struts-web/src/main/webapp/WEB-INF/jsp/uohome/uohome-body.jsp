<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/tld/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/tld/fn.tld" prefix="fn"%>

<script type="text/javascript">
   function send(frm){
          document.EventListForm.dispatch.value="deleteEdit";
          document.EventListForm.submit();
    }

   function viewEvent(pr, ev) {
        document.ScheduleFormALL.programName.value = pr;
        document.ScheduleFormALL.eventName.value = ev;
        document.ScheduleFormALL.submit();
    }

   function confirmDelete(frm) {
        var s = "";
        var j = 0;
        var np = 0;

        for (var i=0;i<frm.length;i++) {
            var e = frm.elements[i];
            if (e.name == 'programNames') {
                if( e.checked ) {
                    if( j>0 ) {
                        s += ", "
                    }
                    s += e.value;
                    j++;
                  // num of participants
                    np = ( (e.value).split("#")[1] );
                 //*** Start of part eval
                    if (np > 0){
                        alert( "Program(s) "+ (e.value).split("#")[0] + " has/have "+(e.value).split("#")[1]+" participation(s), try to un-enroll the participation before deleting "  );
                        s = "";
                        document.EvenListForm.programNames.focus();
                        return false;
                    }else{
                         if (s == "") return false;
                         var conf = confirm("Are you sure you want to delete program " + (e.value).split("#")[0] + "?");
                         if (conf == true ){
                              document.EventListForm.submit()
                              return true;
                        }else{
                             document.EvenListForm.programNames.focus();
                              return false;
                        }
                    }
                 //*** End of part eval
                }
            }
        }
    }

</script>

<form action="uoProgram.do" name="ScheduleFormALL" method="post">
    <input type="hidden" name="dispatch" value="view"/>
    <input type="hidden" name="eventName" value=""/>
    <input type="hidden" name="programName" value=""/>
</form>

<html:form action="/uoProgram"  >

     <input type="hidden" name="dispatch" value=""/>
<!-- Table Style -->
<p/>

<div class="tablestyle">
    <display:table name="${EventListForm.programList}" id="program" cellspacing="0" cellpadding="0"
                   requestURI="uoProgram.do"  export="true">
         <display:caption media="html">
         
                <div class="edit_tools">
                    <a href="program.do?dispatch=create" title="Create Program">
                        <img src="secure/images/add_device.gif"/>
                    </a>
                     <a href="#" title="Delete Selected Items"  onclick="send(document.EventListForm);" >
                        <img src="secure/images/delete_device.gif" />
                    </a>
                </div>
        
        Programs
        </display:caption>
        <logic:equal name="readonly" value="false">
                 <c:set var="partTotal" value="${0}" />
                 <c:set var="clientTotal" value="${0}" />
                   <c:set var="count" value="${0}" />
                 <c:forEach  items="${EventListForm.participantInProgram}" var="mapEntry" varStatus="status">
                       <c:choose>
                       <c:when test="${(mapEntry.key == program.programName)}">
                            <%-- <c:set var="partTotal" value="${mapEntry.value}" /> --%>

                                <c:forEach  items="${mapEntry.value}" var="partItems" varStatus="loop">
                                     <c:choose>
                                      <c:when test="${loop.count == 1}">
                                           <c:set var="clientTotal" value="${partItems}" />
                                          </c:when>
                                        <c:otherwise>
                                               <c:set var="partTotal" value="${partItems}" />
                                         </c:otherwise>

                                        </c:choose>
                                    
                                </c:forEach>
                        </c:when>
                        </c:choose>
                 </c:forEach>
        <display:column title="Delete" media="html">
            <c:if test="${partTotal <= 0}">
                  <input name="programNames" type="checkbox"
                   value="${program.programName}#<c:out value="${partTotal}" />"/>
            </c:if>
            <%--
            <c:if test="${partTotal > 0}">
                  [<c:out value="${partTotal}" />]
            </c:if>
            --%>
        </display:column>
        </logic:equal>

	<!-- Create Clone Program -->
		        <display:column title="Clone" media="html" class="edit_item">
		            <logic:equal name="readonly" value="false">
		                <a href="program.do?dispatch=cloneEdit&programName=${program.programName}">
		                <img src="secure/images/add_device.gif" />
		                </a>
		            </logic:equal>
		        </display:column>
	<!--  End of Clone -->


        <display:column title="Name" media="html" class="edit_item">
            <logic:equal name="readonly" value="false">
                <a href="program.do?dispatch=edit&programName=${program.programName}">
                  ${program.programName}
                </a>
            </logic:equal>
            <logic:equal name="readonly" value="true">
                ${program.programName}
            </logic:equal>
        </display:column>

        <display:column title="name" media="excel">
            ${program.programName}
        </display:column>

        <display:column title="Action" media="html">
            <c:if test="${program.manualCreatable}">
                <!-- <a href="eventDetail.do?dispatch=create&programName=${program.programName}">Add Event</a> -->
                <a href="dispatchEventDetail.do?dispatch=create&programName=${program.programName}">Add Event</a>
            </c:if>
            <c:if test="${not program.manualCreatable}">
                &nbsp;
            </c:if>
        </display:column>
        <display:column title="Participation" media="html">
            <a href="uoParticipant.do?programName=${program.programName}">View Participants  
                [ <c:out value="${partTotal}" /> : <c:out value="${clientTotal}" /> :
                 ] </a>
        </display:column>
        <%--
        <display:column title="Shed Potential">
            <a href="uoParticipant.do?programName=${program.programName}">100</a>
        </display:column>
        <display:column title="Pending Events">
            <a href="uoParticipant.do?programName=${program.programName}">None</a>
        </display:column>
        --%>
    </display:table>
</div>

</html:form>