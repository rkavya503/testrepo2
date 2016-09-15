package com.akuacom.pss2.drw.jsf.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.akuacom.pss2.drw.model.RTPEventDataModel;


public class RTPEventGroupBackingBean implements Serializable{
	
	private static final long serialVersionUID = -2817235777863433356L;
	
	private List<RTPEventDataModel> rtpList = new ArrayList<RTPEventDataModel>();
	
	public void setRtpList(List<RTPEventDataModel> rtpList) {
		this.rtpList = rtpList;
	}
	
	public List<RTPEventDataModel> getRtpList() {
		if(rtpList==null || rtpList.size()==0){
			return Collections.emptyList();
		}else{
			return rtpList;
		}
//		return rtpList.subList(1,rtpList.size());
	}
	
	public RTPEventGroupBackingBean(List<RTPEventDataModel> rtpList){
		this.rtpList = rtpList;
	}

	public RTPEventGroupBackingBean(){
		super();
	}
}
