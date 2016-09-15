<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
      "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="com.akuacom.pss2.system.*,
                 com.akuacom.ejb.client.*,
                 com.akuacom.pss2.participant.*,
                 com.akuacom.pss2.util.UserType,
                 javax.faces.context.ExternalContext,
                 javax.faces.context.FacesContext"
%>
<%
	SystemManager systemManager = (SystemManager)EJB3Factory.getBean(SystemManager.class);
	String defaultPage = systemManager.getPss2Features().getFeatureFacdashDefaultPage();
	Boolean newEnabled = systemManager.getPss2Features().isNewsEnabled();
    boolean gotoSimplifiedDashboard = false;

    // this is here to avoid loggin out someone else? see LogoutAction in struts-web
    String uid = (String) request.getSession().getAttribute("uid");
    if(uid == null)
    {
        ExternalContext context =
        FacesContext.getCurrentInstance().getExternalContext();
        uid = context.getUserPrincipal().getName();

        ParticipantManager pManager = (ParticipantManager)EJB3Factory.getBean(ParticipantManager.class);

        Participant loggedUser = pManager.getParticipantOnly(uid);


        if (loggedUser != null) {
            gotoSimplifiedDashboard = UserType.SIMPLE.equals(loggedUser.getUserType());
        }
    }
    
    if (gotoSimplifiedDashboard) {
    	response.sendRedirect("simpleDashboard.jsf");
    }
    else if(newEnabled.booleanValue() & "news".equals(defaultPage))
	{
		response.sendRedirect("news.jsf?uid=" + uid);
	}
	else
	{
		response.sendRedirect("clients.jsf?uid=" + uid);
	}
%>
<html>
  <head>
   </head>
  <body>
  </body>
</html>
