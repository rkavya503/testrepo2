/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.core.ErrorUtil.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.core;

import com.akuacom.pss2.core.ProgramValidationException;
import com.akuacom.pss2.core.ProgramValidationMessage;
import com.akuacom.pss2.core.ValidationException;

import javax.transaction.TransactionRolledbackException;
import javax.ejb.EJBException;
import javax.persistence.EntityExistsException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Helper methods for use with errors..
 */
public class ErrorUtil
{
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
            	//shouldn't show debug/sensitive message to end user for security concern
                //errorMessage = e.getMessage();
                errorMessage = "internal error";
            }
        } else if (e instanceof ProgramValidationException) {
            final List<ProgramValidationMessage> programValidatationMessages = ((ProgramValidationException) e).getErrors();
            StringBuilder sb = new StringBuilder();
            for (int i=0;i< programValidatationMessages.size();i++){
                sb.append(programValidatationMessages.get(i).getDescription());
                if (i< (programValidatationMessages.size()-1))
                    sb.append(" , ");
            }
            /*
            for (ProgramValidationMessage message : programValidatationMessages) {
                sb.append(message.getDescription());
                sb.append(" \n ");
            }
             *
             */
            errorMessage = sb.toString();
        } else {
            //errorMessage = e.getMessage();
        	errorMessage = "internal error";
        }
        if(errorMessage == null)
        {
        	return "internal error";
        }
        return errorMessage; 
    }
    
    public static String getWarningMessage(List<ProgramValidationMessage> programValidatationMessages) {
    	if(programValidatationMessages == null || programValidatationMessages.size() <= 0)
    	{
    		return null;
    	}
        StringBuilder sb = new StringBuilder();
        /*
        for (ProgramValidationMessage message : programValidatationMessages) {
                sb.append(message.getDescription());
                sb.append(" \n ");
        }
        */
        for (int i=0;i< programValidatationMessages.size();i++){
                sb.append(programValidatationMessages.get(i).getDescription());
                if (i< (programValidatationMessages.size()-1))
                    sb.append(" , ");
        }

        return sb.toString(); 
    }

    /**
     * Returns an exception wrapped as a ValidationException.
     * 
     * @param e the exception.  If null, returns null.
     * 
     * @return the validation exception
     */
    public static ValidationException getValidationException(Throwable e)
    {
        if(e == null) return null;
        if(e instanceof ValidationException)
        {
            return (ValidationException) e;
        }
        else
        {
            return getValidationException(e.getCause());
        }
    }
    
    public static ProgramValidationException getProgramValidationException(Throwable e)
    {
        if(e == null) return null;
        if(e instanceof ProgramValidationException)
        {
            return (ProgramValidationException) e;
        }
        else
        {
            return getProgramValidationException(e.getCause());
        }
    }

    /**
     * Returns an exception wrapped as a ValidationException.
     *
     * @param e the exception.  If null, returns null.
     *
     * @return the validation exception
     */
    public static EntityExistsException getEntityExistsException(Throwable e)
    {
        if(e == null) return null;
        if(e instanceof EntityExistsException)
        {
            return (EntityExistsException) e;
        }
        else
        {
            return getEntityExistsException(e.getCause());
        }
    }
}