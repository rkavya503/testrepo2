package com.honeywell.drms.logger;

import org.apache.log4j.helpers.PatternConverter;
import org.apache.log4j.helpers.PatternParser;

public class LogEntryPatternParser extends PatternParser {

	public LogEntryPatternParser(String pattern) {
		super(pattern);
	}

	protected void finalizeConverter(char c) {
		PatternConverter pc = null;
		switch (c) {
		case 'e':
			pc = new LogEntryConverter(formattingInfo);
			currentLiteral.setLength(0);
			break;
		default:
			super.finalizeConverter(c);
			return;
		}
		addConverter(pc);
	}
}
	