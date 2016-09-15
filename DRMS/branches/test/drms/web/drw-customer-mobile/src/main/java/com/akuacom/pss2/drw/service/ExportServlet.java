package com.akuacom.pss2.drw.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akuacom.pss2.drw.EventDAO;
import com.akuacom.pss2.drw.EventDAOImpl;
import com.akuacom.pss2.drw.entry.HistoryResultEntry;
import com.akuacom.pss2.drw.value.EventValue;
import com.akuacom.pss2.drw.value.WeatherValue;


public class ExportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	EventDAO evtDao = new EventDAOImpl();
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String filename = "EventsHistory.csv";
		//String contentType = "application/vnd.ms-excel";
		String contentType = "application/csv; charset=UTF-8";
		PrintWriter out = response.getWriter();
		response.setHeader("Content-disposition", "attachment; filename=" + filename);
        response.setContentType(contentType);
        String fileContent = buildCSVContent(request);       
        out.print(fileContent);
        out.close();
	}
	
	private String buildCSVContent(HttpServletRequest request){
		Date startDate=null;
		Date endDate=null;
		try {
			Boolean isRTP = Boolean.valueOf(request.getParameter("isRTP"));
			String program = request.getParameter("program");
			String product = request.getParameter("product");
			String start = request.getParameter("start");
			String end = request.getParameter("end");
			String zipcodes = request.getParameter("zipcodes");
			startDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(start);
			endDate = (new SimpleDateFormat("MM/dd/yyyy")).parse(end);
			if(isRTP){
				List<WeatherValue> result = evtDao.getHistoryTems(startDate, endDate);
				List<String> header = new ArrayList<String>();
				header.add("Date");
				header.add("Pricing Category");
				
				List<List<String>> contents = new ArrayList<List<String>>();
				for(WeatherValue weather:result){
					List<String> content = new ArrayList<String>();
					content.add("\""+(new SimpleDateFormat("MMMM dd, yyyy")).format(weather.getDate())+"\"");
					String rtpPricing="";
					if(weather.getPricingCategory()!=null&&(!weather.getPricingCategory().equalsIgnoreCase(""))){
						rtpPricing=weather.getPricingCategory();
					}else{
						rtpPricing="N/A";
					}
					content.add(rtpPricing);
					contents.add(content);
				}
				return buildCSVContent(header,contents);
				
			}else{
				List<String> zipList = new ArrayList<String>();
				if(zipcodes!=null&&(!"".equalsIgnoreCase(zipcodes))){
					zipList = Arrays.asList(zipcodes.split(";"));	
				}
				List<EventValue> result = evtDao.getHistoryEvents(program,product,zipList,start,end);
				List<String> header = new ArrayList<String>();
				header.add("Product");
				header.add("Start Date");
				header.add("End Date");
				header.add("Start Time");
				header.add("End Time");
				boolean isBlock = false;
				if("API".equalsIgnoreCase(program)||"BIP".equalsIgnoreCase(program)||"CBP".equalsIgnoreCase(program)){
					isBlock=true;
					header.add("Block");
				}
				List<List<String>> contents = new ArrayList<List<String>>();
				
				for(EventValue ev:result){
					if("BIP".equalsIgnoreCase(program)){
						ev.setProduct("BIP");
					}
					HistoryResultEntry entry = new HistoryResultEntry();
					entry.setEventValue(ev);
					List<String> content = new ArrayList<String>();
					content.add(entry.getProduct());
					content.add("\""+entry.getStartDate()+"\"");
					content.add("\""+entry.getEndDate()+"\"");
					content.add(entry.getStartTime());
					content.add(entry.getEndTime());
					if(isBlock){
						content.add("\""+entry.getBlocks()+"\"");
					}
					contents.add(content);
				}
				return buildCSVContent(header,contents);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	} 
	
	private String buildCSVContent(List<String> header,List<List<String>> contents){
		StringBuffer result=new StringBuffer();
		
		for(String headerCell:header){
			result.append(headerCell).append(",");
		}
		result.replace(result.length()-1, result.length(),"\n");
		
		for(List<String> contentRow:contents){
			for(String contentCell:contentRow){
				result.append(contentCell).append(",");
			}
			result.replace(result.length()-1, result.length(),"\n");
		}
		
		result.replace(result.length()-1, result.length(),"\n");
		
		return result.toString();
	}
}
