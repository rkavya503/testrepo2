package com.honeywell.drms.logger;

import org.apache.log4j.PatternLayout;
import org.apache.log4j.helpers.PatternParser;

public class LogEntryPatternLayout extends PatternLayout {

	 protected PatternParser createPatternParser(String pattern)
	  {
	    return new LogEntryPatternParser(pattern);
	  }

}
