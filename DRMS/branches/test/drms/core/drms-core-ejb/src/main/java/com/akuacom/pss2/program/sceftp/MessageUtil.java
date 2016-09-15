/**
 * 
 */
package com.akuacom.pss2.program.sceftp;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
import javax.transaction.TransactionRolledbackException;

import com.akuacom.common.exception.AppServiceException;
import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;

/**
 * helper for exception message parser
 *  
 */
public class MessageUtil {

    public static String getErrorMessage(Exception e) {
    	String errorMessage = null;
        if (e instanceof TransactionRolledbackException) {
            errorMessage = getErrorMessage((Exception) e.getCause());
        } else if (e instanceof RemoteException) {
            errorMessage = getErrorMessage((Exception) e.getCause());
        } else if (e instanceof EJBException) {
            final Exception ee = ((EJBException) e).getCausedByException();
            if (ee != null) {
                errorMessage = getErrorMessage(ee);
            } else {
                errorMessage = e.getMessage();
            }
        } else if (e instanceof ProgramValidationException) {
            final List<ProgramValidationMessage> programValidatationMessages = ((ProgramValidationException) e).getErrors();
            StringBuilder sb = new StringBuilder();
            for (ProgramValidationMessage message : programValidatationMessages) {
                sb.append(message.getParameterName());
                sb.append(": ");
                sb.append(message.getDescription());
                sb.append(";\n");
            }
            errorMessage = sb.toString();
        } else if (e instanceof AppServiceException){
        	errorMessage = ((AppServiceException)e).getMessage();
        } else {
            errorMessage = e.getMessage();
        }
        if(errorMessage == null)
        {
        	return "internal error";
        }
        return errorMessage; 
    }
    
    public static List<String> getDisplayMessage(Exception e) {
    	List<String> errorMessages=new ArrayList<String>();
        if (e instanceof TransactionRolledbackException) {
        	errorMessages = getDisplayMessage((Exception) e.getCause());
        } else if (e instanceof RemoteException) {
        	errorMessages = getDisplayMessage((Exception) e.getCause());
        } else if (e instanceof EJBException) {
            final Exception ee = ((EJBException) e).getCausedByException();
            if (ee != null) {
            	errorMessages = getDisplayMessage(ee);
            } else {
            	errorMessages.add(e.getMessage());
            }
        } else if (e instanceof ProgramValidationException) {
            final List<ProgramValidationMessage> programValidatationMessages = ((ProgramValidationException) e).getErrors();
            for (ProgramValidationMessage message : programValidatationMessages) {
                StringBuilder sb = new StringBuilder();
                sb.append(message.getParameterName());
                sb.append(": ");
                sb.append(message.getDescription());
                sb.append(";\n");
                errorMessages.add(sb.toString());
            }
        } else {
        	errorMessages.add(e.getMessage());
        }
        if(errorMessages.size() == 0)
        {
        	errorMessages.add("internal error");
        }
    	
    	return errorMessages;
    }
    
    public static Exception getCauseException(Exception e) {
    	Exception clause =e;
        if (e instanceof TransactionRolledbackException) {
        	clause = getCauseException((Exception) e.getCause());
        } else if (e instanceof RemoteException) {
        	clause = getCauseException((Exception) e.getCause());
        } else if (e instanceof EJBException) {
            clause = ((EJBException) e).getCausedByException();
        } 
    	
    	return clause;
    }
}
