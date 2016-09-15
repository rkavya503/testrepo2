package com.akuacom.utils.lang;

import java.io.File;
import java.util.NoSuchElementException;


public class ClassPathUtil {
    public final static String LOOK_IN_CLASSPATH_PREFIX = "^";

	/**
	 * Looks in each class path entry (assumed to be a directory) for an file named resource
	 * @param resource the file to seek (leading ^ character is stripped, see TestRequiresDb for explanation)
	 * @return the absolute path of the file or null
	 */
	public static String resolve(String resource) {
		if(resource.startsWith("^")) {
			resource = resource.substring(1);
		}
		
		
		String cpProp =System.getProperty("java.class.path");
		String[] cp = cpProp.split(File.pathSeparator);
		File f;
		for(String p : cp) {
			f = new File(p + File.separator + resource);
			if(f.exists()) {
				return f.getAbsolutePath();
			}
		}
		throw new NoSuchElementException(resource);
	}
}
