package com.akuacom.pss2.data.usage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.akuacom.pss2.data.PDataEntry;
import com.akuacom.utils.lang.DateUtil;

/**
 * Class for handle history data
 * 
 * 
 */
public class EntryDataUtil {

	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Map<String, List<PDataEntry>> generateModel() throws Exception {

		return generateModel("dataentry.sql",true,"\\'\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2}\\',\\d+\\.?\\d*\\,");
	}
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, List<PDataEntry>> generateModel(String fileName, boolean isWeeklyTemplate, String model) throws Exception {
		InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
		EntryDataUtil util = new EntryDataUtil();
		
		List<String[]> result = util.readFile(is,model);

		@SuppressWarnings("rawtypes")
		List dataList = util.popuplateData(result, PDataEntry.class);

		HashMap<String, List<PDataEntry>> map = null;
		
		if(isWeeklyTemplate){
			map = EntryDataUtil.generateTempalte(dataList);
		}else{
			map = generateMockDataEntry(dataList);
		}

		return map;
	}

	List<String[]> readFile(InputStream in,String patternStr) {
		List<String[]> result = new ArrayList<String[]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line = null;
		try {
			while (((line = br.readLine()) != null)) {

				Pattern pattern = Pattern.compile(patternStr);
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {

					String text = matcher.group();

					String[] tokens = text.split(",");
					if (tokens.length == 2) {
						result.add(tokens);
					}
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;

	}
	
	List<String[]> readFile(InputStream in) {
		List<String[]> result = new ArrayList<String[]>();

		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		String line = null;
		try {
			while (((line = br.readLine()) != null)) {
				String str = "\\d{4}\\-\\d{2}\\-\\d{2}\\s\\d{2}\\:\\d{2}\\:\\d{2},\\d+\\.?\\d*\\,";

				Pattern pattern = Pattern.compile(str);
				Matcher matcher = pattern.matcher(line);

				if (matcher.find()) {

					String text = matcher.group();

					String[] tokens = text.split(",");
					if (tokens.length == 2) {
						result.add(tokens);
					}
				}
			}
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return result;

	}

	/**
	 * 
	 * @param in
	 * @param clazz
	 * @param field1
	 *            time
	 * @param field2
	 *            value
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List popuplateData(List<String[]> in, Class clazz) throws Exception, Exception {
		List result = new ArrayList();
		Iterator item = in.iterator();
		while (item.hasNext()) {
			String[] tokens = (String[]) item.next();

			tokens[0] = tokens[0].replaceAll("'", "");

			Object obj = null;
			try {
				obj = clazz.newInstance();
				Field time = clazz.getDeclaredField("time");
				time.setAccessible(true);
				time.set(obj, generateDate(tokens[0]));

				Field value = clazz.getDeclaredField("value");
				value.setAccessible(true);
				value.set(obj, Double.valueOf(tokens[1]));

				result.add(obj);
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

		}

		return result;
	}

	public static Date generateDate(String value, String pattern) throws ParseException {

		SimpleDateFormat ss = new SimpleDateFormat();

		ss.applyPattern(pattern);

		return ss.parse(value);

	}

	public static Date generateDate(String value) throws ParseException {
		return generateDate(value, "yyyy-MM-dd HH:mm:SS");

	}

	public static String formatDate(Date value, String pattern) throws ParseException {

		SimpleDateFormat ss = new SimpleDateFormat();

		ss.applyPattern(pattern);

		return ss.format(value);

	}

	public static String formatDate(Date value) throws ParseException {
		return formatDate(value, "yyyy-MM-dd HH:mm:SS");

	}

	public static HashMap<String, List<PDataEntry>> generateTempalte(List<PDataEntry> dataList) {
		HashMap<String, List<PDataEntry>> map = new HashMap<String, List<PDataEntry>>();

		List<PDataEntry> sunData = new ArrayList<PDataEntry>();
		List<PDataEntry> monData = new ArrayList<PDataEntry>();
		List<PDataEntry> tueData = new ArrayList<PDataEntry>();
		List<PDataEntry> wedData = new ArrayList<PDataEntry>();
		List<PDataEntry> thuData = new ArrayList<PDataEntry>();
		List<PDataEntry> friData = new ArrayList<PDataEntry>();
		List<PDataEntry> staData = new ArrayList<PDataEntry>();

		map.put("sunday", sunData);
		map.put("monday", monData);
		map.put("tuesday", tueData);
		map.put("wednesday", wedData);
		map.put("thursday", thuData);
		map.put("friday", friData);
		map.put("saturday", staData);

		Iterator<PDataEntry> item = dataList.iterator();

		while (item.hasNext()) {
			PDataEntry temp = item.next();

			Date date = temp.getTime();
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);

			switch (cal.get(Calendar.DAY_OF_WEEK)) {
			case Calendar.SUNDAY:
				addToList(map.get("sunday"), temp);
			case Calendar.MONDAY:
				addToList(map.get("monday"), temp);
			case Calendar.TUESDAY:
				addToList(map.get("tuesday"), temp);
			case Calendar.WEDNESDAY:
				addToList(map.get("wednesday"), temp);
			case Calendar.THURSDAY:
				addToList(map.get("thursday"), temp);
			case Calendar.FRIDAY:
				addToList(map.get("friday"), temp);
			case Calendar.SATURDAY:
				addToList(map.get("saturday"), temp);
			default:
			}

		}

		return map;

	}
	
	public static HashMap<String, List<PDataEntry>> generateMockDataEntry(List<PDataEntry> dataList) {

		HashMap<String, List<PDataEntry>> logMap = new HashMap<String, List<PDataEntry>>();
	        for (PDataEntry de : dataList) {
	          
	            Date date = DateUtil.stripTime(de.getTime());
	            List<PDataEntry> dl;
	            if (logMap.get(date.toString()) == null) {
	                dl = new ArrayList<PDataEntry>();
	                logMap.put(date.toString(), dl);
	            } else {
	                dl = logMap.get(date.toString());
	            }
	           dl.add(de);
	        }

		return logMap;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void addToList(List in, Object item) {
		if (in.size() >= 96) {
			return;
		}
		in.add(item);

	}

}
