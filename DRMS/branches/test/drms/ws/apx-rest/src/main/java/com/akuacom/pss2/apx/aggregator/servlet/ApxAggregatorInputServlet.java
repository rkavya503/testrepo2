package com.akuacom.pss2.apx.aggregator.servlet;

import java.io.File;
import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.akuacom.pss2.apx.common.ApxInputServletHelper;
import com.akuacom.pss2.program.apx.aggregator.ApxAggregatorManager;
import com.akuacom.pss2.program.apx.parser.APXXmlParser;
import com.akuacom.pss2.program.sceftp.MessageUtil;

/**
 * @author Ram Pandey
 * 
 */
public class ApxAggregatorInputServlet  extends HttpServlet {

	private static final long serialVersionUID = -7871415881511526264L;
	private static final Logger log = Logger.getLogger(ApxAggregatorInputServlet.class);
	
	@EJB
	private ApxAggregatorManager.L apxAggregatorManager;
	
	public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 handleRequest(request,response);
	 	}
	 public void doPost(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
			try{
				log.info(" Servlet request ");
				Thread.sleep(1000);	
				doPut(request, response);
			}catch(Exception ee){
		 	}
		}
	 private void handleRequest(HttpServletRequest request,HttpServletResponse response) throws IOException {
		 	int errors=0;
	        String errMessage="";
		 	
	        try {
	        	ServletInputStream inputStream = request.getInputStream();
		        File file=ApxInputServletHelper.saveTempFile(inputStream);
		        APXXmlParser parser=new APXXmlParser(file);
				apxAggregatorManager.processApxRequest(parser);
				
			} catch (Exception e) {
				errors=1;
	        	errMessage=MessageUtil.getErrorMessage(e);
	        	log.error(e);
			}
	        String resMessage = ApxInputServletHelper.getApxResponseMessage(errors, errMessage);
		    ServletOutputStream out = response.getOutputStream();
		    out.println(resMessage);
		    out.flush();
	 }

}
