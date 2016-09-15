package com.akuacom.pss2.richsite.util;

import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.akuacom.pss2.participant.Participant;
import com.akuacom.pss2.program.Program;
import com.akuacom.pss2.richsite.program.configure.rules.RulesConfigureDataModel;
import com.akuacom.pss2.richsite.program.configure.rules.RulesViewItem;

public class AkuacomJSFUtil {
	
	/**
	 * Utility function for add message into FacesContext as error level
	 * @param message
	 */
	public static void addErrorMessage(String message) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,message, message));
	}
	
	public static FacesMessage createMessage(String msg){
		FacesMessage fmsg= new FacesMessage(msg);
		fmsg.setSeverity(FacesMessage.SEVERITY_ERROR);
		return fmsg;
	}

	/**
	 * Adds the msg info.
	 *
	 * @param message the message
	 */
	static public void addMsgInfo(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
			message, message));
	}

	/**
	 * Adds the msg error.
	 *
	 * @param message
	 *            the message
	 */
	static public void addMsgError(String message)
	{
		FacesContext fc = FacesContext.getCurrentInstance();
		fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
			message, message));
	}


    	/**
	 * Gets the rule table.
	 *
	 * @return the rule table
	 */
	static public RulesConfigureDataModel getRuleTable()
	{
		return (RulesConfigureDataModel)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rules");
	}

	/**
	 * Sets the rule table.
	 *
	 * @param rules the new rule table
	 */
	static public void setRuleTable(RulesConfigureDataModel rules)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("rules", rules);
	}



    static public RulesViewItem getJSFRule()
	{
		return (RulesViewItem)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("rule");
	}


	static public void setJSFRule(RulesViewItem rule)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("rule", rule);
	}

	static public Boolean getNeedRuleRefresh()
	{
		return (Boolean)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("programRefresh");
	}


	static public void setNeedRuleRefresh(Boolean refresh)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("programRefresh", refresh);
	}

        static public Program getProgram()
	{
		return (Program)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("program");
	}


	static public void setProgram(Program program)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("program", program);
	}

    static public  Map<String,List<Participant>> getParticipantsInProgram()
	{
		return ( Map<String,List<Participant>>)FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().get("participantsInProgram");
	}


	static public void setParticipantsInProgram( Map<String,List<Participant>> participantsInProgram)
	{
		FacesContext.getCurrentInstance().
				getExternalContext().getSessionMap().put("participantsInProgram", participantsInProgram);
	}

	private static final char QUOTECHAR ='"';
	private static final char ESCAPECHAR ='"';
	private static final char SEPARATOR =',';
	private static final String NULLCHAR ="null";
	private static final int INITIAL_STRING_SIZE = 128;
	
    public static StringBuffer writeNext(StringBuffer sb, String nextElement) {
    	if(nextElement==null||NULLCHAR.equalsIgnoreCase(nextElement.trim())){
    		sb.append(QUOTECHAR);
    		sb.append(QUOTECHAR);
    	    sb.append(SEPARATOR);
    	    
    	    return sb;
    	}
    	sb.append(QUOTECHAR);
        sb.append(stringContainsSpecialCharacters(nextElement) ? processLine(nextElement) : nextElement);
        sb.append(QUOTECHAR);
        sb.append(SEPARATOR);
        
        return sb;
    }
    
	private static boolean stringContainsSpecialCharacters(String line) {
	    return line.indexOf(QUOTECHAR) != -1;
	}
	
	protected static StringBuilder processLine(String nextElement)
	{
		StringBuilder sb = new StringBuilder(INITIAL_STRING_SIZE);
	    for (int j = 0; j < nextElement.length(); j++) {
	        char nextChar = nextElement.charAt(j);
	        if (nextChar == QUOTECHAR) {
	        	sb.append(ESCAPECHAR).append(nextChar);
	        } else {
	            sb.append(nextChar);
	        }
	    }
	    
	    return sb;
	}
}
