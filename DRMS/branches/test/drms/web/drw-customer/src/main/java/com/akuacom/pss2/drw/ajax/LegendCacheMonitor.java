package com.akuacom.pss2.drw.ajax;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.model.CacheCategory;
import com.akuacom.pss2.drw.model.StatusCache;

public class LegendCacheMonitor extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req, response);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		Boolean active = Boolean.valueOf(req.getParameter("active"));
		
		Date latest = null;
		Date[] dates = new Date[5];
		if(active){
			dates[0]=StatusCache.getInstance().getStatusMap().get(CacheCategory.ACT_SDPRESI_EVENT);
			dates[1]=StatusCache.getInstance().getStatusMap().get(CacheCategory.ACT_SDPCOME_EVENT);
			dates[2]=StatusCache.getInstance().getStatusMap().get(CacheCategory.ACT_API_EVENT);
			dates[3]=StatusCache.getInstance().getStatusMap().get(CacheCategory.ACT_BIP_EVENT);
			dates[4]=StatusCache.getInstance().getStatusMap().get(CacheCategory.ACT_CBP_EVENT);
		}else{
			dates[0]=StatusCache.getInstance().getStatusMap().get(CacheCategory.SCHE_SDPRESI_EVENT);
			dates[1]=StatusCache.getInstance().getStatusMap().get(CacheCategory.SCHE_SDPCOME_EVENT);
			dates[2]=StatusCache.getInstance().getStatusMap().get(CacheCategory.SCHE_API_EVENT);
			dates[3]=StatusCache.getInstance().getStatusMap().get(CacheCategory.SCHE_BIP_EVENT);
			dates[4]=StatusCache.getInstance().getStatusMap().get(CacheCategory.SCHE_CBP_EVENT);
		}
		
		for(Date d:dates){
			if(latest==null||d.after(latest)) 
				latest =d;
		}
		
		long result = latest.getTime();
		
		response.setContentType("text/xml");
		response.getWriter().write(String.valueOf(result));
		
		return;
	}
}
