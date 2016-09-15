/*
 * www.akuacom.com - Automating Demand Response
 * 
 * com.akuacom.pss2.web.report.AccountsReportAction.java - Copyright(c)1994 to 2010 by Akuacom . All rights reserved. 
 * Redistribution and use in source and binary forms, with or without modification, is prohibited.
 *
 */
package com.akuacom.pss2.web.report;

import com.akuacom.pss2.report.ReportManager;
import com.akuacom.pss2.report.entities.Account;
import com.akuacom.pss2.web.commdev.CommDevMapAction;
import com.akuacom.pss2.web.commdev.CommDevVO;
import com.akuacom.pss2.web.util.DisplayTagUtil;
import com.akuacom.pss2.web.util.EJBFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The Class AccountsReportAction.
 */
public class AccountsReportAction extends Action {
    
    /* (non-Javadoc)
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        AccountsReportForm form = (AccountsReportForm) actionForm;
        ReportManager report = EJBFactory.getBean(ReportManager.class);
        final List<Account> list = report.getAccounts();
        //DRMS-6825
        List<Account> refactorylist=refactoryList(list);
        form.setAccounts(refactorylist);

        if (DisplayTagUtil.isExport(httpServletRequest)) {
            return actionMapping.findForward("export");
        } else {
            return actionMapping.findForward("success");
        }
    }
    
    //DRMS-6825
    private List<Account> refactoryList(List<Account> list){
    	Map<String,List<Account>> map = new HashMap<String,List<Account>>();
    	for(int i=0;i<list.size();i++){
    		Account account = list.get(i);
    		String accountNumber = account.getAccountNumber();
    		if(map.containsKey(accountNumber)){
    			List<Account> tmp =map.get(accountNumber);
    			tmp.add(account);
    			map.put(accountNumber, tmp);
    		}else{
    			List<Account> tmp = new ArrayList<Account>();
    			tmp.add(account);
    			map.put(accountNumber, tmp);
    		}
    	}
    	List<Account> result = new ArrayList<Account>();
    	Iterator<String> iterator = map.keySet().iterator();
    	while(iterator.hasNext()){
    		String key =iterator.next();
    		List<Account> keySameAccounts = map.get(key);
    		Account newAccount = buildAccount(keySameAccounts);
    		if(newAccount!=null){
    			result.add(newAccount);
    		}
    	}
    	return result;
    }
    //DRMS-6825
    private Account buildAccount(List<Account> list){
    	Account account = null;
    	if(list.size()>0){
    		account = list.get(0);
    		for(int i=1;i<list.size();i++){
        		Account tmp = list.get(i);
        		account.setProgramNames(account.getProgramNames()+","+tmp.getProgramNames());
    		}	
    	}
    	return account;
    }
}
