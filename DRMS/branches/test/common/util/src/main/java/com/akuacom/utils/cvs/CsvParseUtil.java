package com.akuacom.utils.cvs;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * CsvParseUtil is a java utility Class which is use to parse standard CSV File  
 * 
 * @author Yang Liu
 *
 */
public class CsvParseUtil {
	/**
	 * <p>
	 * Parse the CSV file content from file name
	 * @param fileName	CSV file name
	 * 
	 * @return CSV file content by String format
	 * 
	 * @throws Exception
	 */
	public static synchronized String parseFile(String fileName) throws Exception {
		
		File file = new File(fileName);
		
		InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
		
		return parseFile(inputStream);
	}
	/**
	 * <p>
	 * Parse the CSV file content from InputStream
	 * 
	 * @param inputStream InputStream 
	 * 
	 * @return CSV file content by String format
	 * 
	 * @throws Exception
	 */
	public static synchronized String parseFile(InputStream inputStream) throws Exception {
		BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
		StringBuilder sb = new StringBuilder();
		String line;
		while ((line = in.readLine()) != null) {
			sb.append(line);
			sb.append('\n');
		}
		return sb.toString();
	}
	/**
	 * Transfer the csv content to Map which the key is the column number and the value is the array of String result.
	 * 
	 * @param content csv 
	 * 
	 * @param stripQuotesFlag 
	 * 
	 * @return CSV content map which the key is the column number and the value is the array of String result.
	 * 		   Key is start at 0.
	 * 
	 * @throws Exception
	 */
	public static synchronized Map<Integer, String[]> transferCsvStringToMap(String content,
			boolean stripQuotesFlag) throws Exception {
		Map<Integer, String[]> map = new HashMap<Integer, String[]>();
		BufferedReader br = new BufferedReader(new StringReader(content));
		String lineContent;
		int index = 0;
		while ((lineContent = br.readLine()) != null) {
			if (lineContent.trim().length() == 0) {
				continue;
			}
			String[] lineContents = lineContent.split(",");
			if (stripQuotesFlag) {
				String[] lineContentsNew = new String[lineContents.length]; 
				for (int i = 0; i < lineContents.length; i++) {
					lineContentsNew[i] = stripQuotes(lineContents[i]);
				}
				map.put(index, lineContentsNew);
			}else{
				map.put(index, lineContents);	
			}
			
			index++;
		}

		return map;
	}
	/**
	 * Remove the \" from csv content
	 * 
	 * @param input
	 * 
	 * @return
	 */
	private static synchronized String stripQuotes(String input) {
		if (input.charAt(0)=='"'){
			return input.substring(1, input.length() - 1);
		}
		return input;
	}
	/**
	 * Transfer the csv content to List which sort by order.
	 * @param content
	 * @param stripQuotesFlag
	 * @return
	 * @throws Exception
	 */
	public static synchronized List<String[]> transferCsvStringToList(String content,
			boolean stripQuotesFlag) throws Exception {
		List<String[]> list = new ArrayList<String[]>();
		BufferedReader br = new BufferedReader(new StringReader(content));
		String lineContent;
		while ((lineContent = br.readLine()) != null) {
			if (lineContent.trim().length() == 0) {
				continue;
			}
			String[] lineContents = lineContent.split(",");
			if (stripQuotesFlag) {
				String[] lineContentsNew = new String[lineContents.length]; 
				for (int i = 0; i < lineContents.length; i++) {
					lineContentsNew[i] = stripQuotes(lineContents[i]);
				}
				list.add(lineContentsNew);
			}else{
				list.add(lineContents);
			}
		}

		return list;
	}	
	/**
	 * Print function for csv content
	 * @param content
	 * @return
	 */
	public static synchronized String printCsvContent(List<String[]> content){
		StringBuilder sb = new StringBuilder();
		if(!content.isEmpty()){		
			for(int i=0;i<content.size();i++){
				String[] contents = content.get(i);
				StringBuffer line = new StringBuffer();
				for(int j=0;j<contents.length;j++){
					line.append(contents[j]+",");
				}
				if(line.length()>=1){
					StringBuffer s = new StringBuffer(line.substring(0, line.length()-1));
					s.append("\n");
					sb.append(s);
				}
			}		
			System.out.println(sb.toString());
		}
		return sb.toString();		
	}
	/**
	 * Print function for csv content
	 * @param content
	 * @return
	 */
	public static synchronized String printCsvContent(Map<Integer, String[]> content){
		StringBuilder sb = new StringBuilder();
		if(!content.isEmpty()){			
			Set<Integer> keySet = content.keySet();
			Iterator<Integer> i = keySet.iterator();
			while(i.hasNext()){
				int index = i.next();
				String[] contents = content.get(index);
				StringBuffer line = new StringBuffer();
				for(int j=0;j<contents.length;j++){
					line.append(contents[j]+",");
				}
				if(line.length()>=1){
					StringBuffer s = new StringBuffer(line.substring(0, line.length()-1));
					s.append("\n");
					sb.append(s);
				}
			}		
			System.out.println(sb.toString());
		}
		return sb.toString();
	}


}
