package com.akuacom.pss2.richsite.util;

import java.util.Comparator;

import com.akuacom.pss2.report.entities.ClientOfflineReportEntity;

public class ClientOfflineReportEntityComparator implements Comparator<ClientOfflineReportEntity> {


	@Override
	public int compare(ClientOfflineReportEntity arg0, ClientOfflineReportEntity arg1) {
		
		int flag = 0;
		if(arg0==null||arg1==null||arg0.getStartTime()==null||arg1.getStartTime()==null){
			return flag;
		}
		if(arg0.getStartTime()!=null && arg1.getStartTime()!=null){
			flag = arg0.getStartTime().compareTo(arg1.getStartTime());
		}else if(arg0.getStartTime()!=null && arg1.getStartTime() ==null){
			flag = 1;
		}else if(arg0.getStartTime()==null && arg1.getStartTime() !=null){
			flag = -1;
		}else{
			flag = 0;
		}
		return flag;
	}

}
