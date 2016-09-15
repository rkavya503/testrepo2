package com.akuacom.pss2.web.util;

import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

public class ValidateTool {
//    private static final String INPUT_DATE_FORMAT = "MM/dd/yyyy";
//    /** The Constant INPUT_DATE_PARSER. */
//    public static final SimpleDateFormat INPUT_DATE_PARSER = new SimpleDateFormat(INPUT_DATE_FORMAT);
    public static final String[] SPECIAL_CHAR_TO_VALIDATE = {"*", "_" ,"!","@","#","$","%","^","&","*","()","_","+","[]","{}",":",";","'",".","/","<>","?","~","`","*"};

    public static SimpleDateFormat getDateFormat(String format){
        return new SimpleDateFormat(format);
    }
    
    public static void validateDateField(ActionErrors errors, String dateString, String messageKey, String fieldDisplayString, String format) {
        if (dateString == null || dateString.length() == 0) {
            errors.add(messageKey, new ActionMessage("pss2.common.empty", fieldDisplayString));
        } else {
            try {
            	getDateFormat(format).parse(dateString);
            } catch (ParseException e) {
                errors.add(messageKey, new ActionMessage("pss2.common.date.invalid", fieldDisplayString, format));
            }
        }
    }

    public static String specialCharValidator(String field,boolean isPart){
                if (!isPart) {
                    field = field.replaceAll("\\s", " ");
                }
                for (int i=0;i<SPECIAL_CHAR_TO_VALIDATE.length; i++){
                    if (field.contains(SPECIAL_CHAR_TO_VALIDATE[i])){
                           field = field.replaceAll("\\"+SPECIAL_CHAR_TO_VALIDATE[i], "");
                    }
                }
        return field;
    }


//    private static final String INPUT_TIME_FORMAT = "HH:mm";
//    /** The Constant INPUT_TIME_PARSER. */
//    public static final SimpleDateFormat INPUT_TIME_PARSER = new SimpleDateFormat(INPUT_TIME_FORMAT);
//
//    public static void validateTimeField(ActionErrors errors, String timeString, String messageKey, String fieldDisplayString) {
//        if (timeString == null || timeString.length() == 0) {
//            errors.add(messageKey, new ActionMessage("pss2.common.empty", fieldDisplayString));
//        } else {
//            try {
//                INPUT_TIME_PARSER.parse(timeString);
//            } catch (ParseException e) {
//                errors.add(messageKey, new ActionMessage("pss2.common.time.invalid", fieldDisplayString, INPUT_TIME_FORMAT));
//            }
//        }
//    }
}
