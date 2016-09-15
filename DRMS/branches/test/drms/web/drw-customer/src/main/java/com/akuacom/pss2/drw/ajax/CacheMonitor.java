package com.akuacom.pss2.drw.ajax;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.model.CacheCategory;
import com.akuacom.pss2.drw.model.StatusCache;
import com.akuacom.utils.lang.DateUtil;

public class CacheMonitor extends HttpServlet {
	private static String PARAM_NAME = "keyValue";

	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(req, response);
		
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse response)
			throws ServletException, IOException {
		String param = req.getParameter(PARAM_NAME);
		String[] params = param.split(",");
		if(params.length<=1){
			Date date = StatusCache.getInstance().getStatusMap().get(param);
			long result = -1L;
			if(date != null){
				result = date.getTime();
			}
			
			response.setContentType("text/xml");
			response.getWriter().write(String.valueOf(result));
			
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append("systemTime"+":" +"\"");
		sb.append(DateUtil.format(new Date(), "MM/dd/yyyy hh:mma")+"\"");
		for(String paraName : params){
			try{
				CacheCategory paraEnum = Enum.valueOf(CacheCategory.class, paraName);
				Date date = StatusCache.getInstance().getStatusMap().get(paraEnum);
				long result = -1L;
				if(date != null){
					result = date.getTime();
				}
				sb.append(",");
				sb.append(paraName+":");
				sb.append(result);
				
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		sb.append("}");
		
		response.setContentType("text/xml");
		response.getWriter().write(sb.toString());
	}
}
