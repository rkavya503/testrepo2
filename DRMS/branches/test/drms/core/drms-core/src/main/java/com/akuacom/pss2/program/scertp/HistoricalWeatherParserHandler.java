package com.akuacom.pss2.program.scertp;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;

import org.apache.log4j.Logger;

public class HistoricalWeatherParserHandler extends HTMLEditorKit.ParserCallback {
    private static final Logger log = Logger.getLogger(HistoricalWeatherParserHandler.class);

    private boolean processTR = false;
    private boolean processTD = false;
    private boolean handleText = false;

    private int rowCount = 0;
    private int colCount = 0;

    private List<HistoricalWeatherEntry> list = new ArrayList<HistoricalWeatherEntry>();
    private HistoricalWeatherEntry entry;

    @Override
    public void handleEndOfLineString(String s) {
        super.handleEndOfLineString(s);
    }

    @Override
    public void handleEndTag(HTML.Tag tag, int i) {
        if (HTML.Tag.TABLE.equals(tag)) {
            rowCount = 0;
            if (processTR) {
                log.debug("========== end processing");
            }
            processTR = false;
        } else if (HTML.Tag.TR.equals(tag)) {
            if (processTR) {
                if (rowCount >= 3) {
                    list.add(entry);
                }
            }
            processTD = false;
            colCount = 0;
            rowCount++;
        } else if (HTML.Tag.TD.equals(tag)) {
            handleText = false;
            colCount++;
        }
    }

    @Override
    public void handleError(String s, int i) {
        super.handleError(s, i);
    }

    @Override
    public void handleSimpleTag(HTML.Tag tag, MutableAttributeSet mutableAttributeSet, int i) {
        super.handleSimpleTag(tag, mutableAttributeSet, i);
    }

    @Override
    public void handleStartTag(HTML.Tag tag, MutableAttributeSet attributeSet, int i) {
        if (HTML.Tag.TABLE.equals(tag)) {
            final Enumeration<?> attributeNames = attributeSet.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                final HTML.Attribute name = (HTML.Attribute) attributeNames.nextElement();
                final Object value = attributeSet.getAttribute(name);
                if (HTML.Attribute.CELLSPACING.equals(name) && !"0".equals(value)) {
                    log.debug("========== start processing");
                    processTR = true;
                }
//                System.out.println(name.toString() + "," + value.toString());
            }
        } else if (HTML.Tag.TR.equals(tag)) {
            if (processTR) {
                if (rowCount < 3) {
//                    rowCount++;
//                    System.out.println("in the header, igonre");
                } else {
//                    System.out.println("in the content, gether value");
                    processTD = true;
                    entry = new HistoricalWeatherEntry();
                }
            }
        } else if (HTML.Tag.TD.equals(tag)) {
            if (processTD) {
                handleText = true;
            }
        }
        super.handleStartTag(tag, attributeSet, i);
    }

    @Override
    public void handleText(char[] chars, int i) {
        if (handleText) {
            final String s = new String(chars);
//            System.out.println(rowCount + ", " + colCount + ", " + s);
            if (colCount == 0) {
                entry.setDate(s);
            } else if (colCount == 1) {
                entry.setTime(s);
            } else if (colCount == 6) {
                try {
                    entry.setHigh(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    // ignore
                }
            } else if (colCount == 8) {
                try {
                    entry.setSixHourHigh(Integer.parseInt(s));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
        }
    }

    public List<HistoricalWeatherEntry> getList() {
        return list;
    }
}
