package com.qa.Report;


import java.io.*;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TestReport  {
	
	String Filename= "";
	String Build= "";
	String Date = "";
	String Server = "";
	String comma = ",";
	String space2 = "  ";
	String space4 = "    ";
	FileOutputStream out; 
	PrintStream ps; 
	Calendar currentDate = Calendar.getInstance();
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MMM/dd HH:mm:ss");
	
	public TestReport(String f, String b, String s){
		Filename = f;
		Build = b;
		Server = s;
		
		Date = formatter.format(currentDate.getTime());
		
		
	}
public void openReport () {
	
//	FileOutputStream out; 
	try {
		
		out = new FileOutputStream(Filename, true);
		ps= new PrintStream(out);
		ps.append("Start Date and Time" + comma + Date + "\r\n");
		ps.append("Build"  + comma + Build + "\r\n" );
		ps.append("Server"  + comma + Server + "\r\n");
		ps.append("\r\n");
		ps.append("Component"  + comma + "Test"  + comma + "Result"  + comma + "Details");	
		ps.append("\r\n");
	} 
	catch (Exception e){
		  System.err.println ("Error in initializing. File was probably open.");
	}
}

public void report (String component, String test, String result, String details  ) {
	
	try {
		
		ps.append(component + comma + test + comma + result + comma + details + comma);	
		ps.append("\r\n");
	
	} 
		catch (Exception e){
		  System.err.println ("Error in writing");
	}
	
	
}

public void closeReport () {
	
	try {
		currentDate = Calendar.getInstance();
		Date = formatter.format(currentDate.getTime());
		ps.append("\r\n");
		ps.append("End Date and Time" + comma + Date + comma + "\r\n");
		ps.append("Build" + comma + Build + "\r\n");
		ps.append("Server" + comma + Server + "\r\n");
		ps.append("\r\n");
		 ps.close();
	} 
		catch (Exception e){
		  System.err.println ("Error in writing");
	}
	
	
}


}
