/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.akuacom.pss2.openadr2.report;


public class DataSetDefEnums {
    
    public static enum DataSetOwnerType {
        PROGRAM,
        PARTY,
        RESOURCE,
        ENDPOINT,
        GROUP,
        OTHER,
        EVENT,
        EVENTRESOURCE,
        SIGNAL
    };
    
    public static DataSetOwnerType ownerTypeFromString(String typeName) {        
        if (typeName.equalsIgnoreCase("program"))  {  return DataSetOwnerType.PROGRAM;   }
        if (typeName.equalsIgnoreCase("party"))    {  return DataSetOwnerType.PARTY;   }
        if (typeName.equalsIgnoreCase("resource")) {  return DataSetOwnerType.RESOURCE;   }
        if (typeName.equalsIgnoreCase("endpoint")) {  return DataSetOwnerType.ENDPOINT;   }
        if (typeName.equalsIgnoreCase("group")) {  return DataSetOwnerType.GROUP;   }
        if (typeName.equalsIgnoreCase("other")) {  return DataSetOwnerType.OTHER;   }
        if (typeName.equalsIgnoreCase("event")) {  return DataSetOwnerType.EVENT;   }
        if (typeName.equalsIgnoreCase("eventresource")) {  return DataSetOwnerType.EVENTRESOURCE;   }
        if (typeName.equalsIgnoreCase("signal")) {  return DataSetOwnerType.SIGNAL;   }
        return null;
    }
    
    
    public enum DataType {
        INTEGER(0),
        DOUBLE(1),
        BOOLEAN(2),
        ENUM(3),
        EVENT(4),
        STRING(5);
        
        private int value;
        DataType(int x) { this.value=x; }
        public int toInt() {  return value; }
    }
    
    public enum InterpolationType {
        NOT_ALLOWED,
        LINEAR,
        STEP,
    }
    
    public static enum BooleanCombinationType {
        AND,
        OR,
        XOR
    };
    
}
