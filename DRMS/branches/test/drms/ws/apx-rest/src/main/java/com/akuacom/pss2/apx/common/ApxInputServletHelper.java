package com.akuacom.pss2.apx.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.akuacom.pss2.util.Configuration;
import com.akuacom.pss2.util.MemorySequence;

public class ApxInputServletHelper {
	private static final Logger log = Logger.getLogger(ApxInputServletHelper.class);
	
	
	public static String getApxResponseMessage(int errorCount , String errorMessage){
		StringBuilder builder=new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\" ?>\n");
		builder.append("<Response>\n    <Errors>");
		builder.append(errorCount);
		builder.append("</Errors>\n");
		if (errorCount>0) {
			builder.append("    <ErrorDescription>");
			builder.append(errorMessage);
			builder.append("</ErrorDescription>\n");
			
		}
		builder.append("</Response>");
		return builder.toString();
	}
	
	public static File saveTempFile(InputStream in) {
		File result = null;
		Configuration conf=new Configuration();
		String tempFolder = conf.getLogPath();
		try {
			SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd_HHmmss");
			String filename="APX_interface_request_"+format.format(new Date())+"_"+MemorySequence.getNextSequenceId()+".xml";
			result = new File(tempFolder, filename);
//			result = File.createTempFile(prefix, ".xml", new File(tempFolder));

			BufferedWriter w1 = new BufferedWriter(new FileWriter(result));
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String line;
			while ((line = reader.readLine()) != null) {
				w1.write(line);
				w1.newLine();
			}
			reader.close();
			w1.close();
			log.debug("APX request is written to " + result.getName());
		} catch (IOException e) {
			log.warn("Failed to create temp file for sce apx request", e);
		}
		return result;
	}

}
