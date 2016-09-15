package com.akuacom.utils.lang;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class ClassUtil {
    private static Logger log = Logger.getLogger(ClassUtil.class);

    public static Map<String, Object> toMap(Object o)
            throws IllegalArgumentException, IllegalAccessException {
        log.info("called with " + o);
        Map<String, Object> map = new HashMap<String, Object>();
        Class<?>  objectClass = o.getClass();
        int mods;
        for (Field f : FieldUtil.getLinealFields(objectClass)) {
            mods = f.getModifiers();
            if (Modifier.isFinal(mods) || Modifier.isStatic(mods)
                    || Modifier.isTransient(mods)) {
                continue;
            }
            map.put(f.getName(), f.get(o));
        }
        return map;
    }
    
    public static String toJson(Object o) {
        StringBuffer sb;
        Class<?> cl =o.getClass();
        if(cl.isArray()) {
            sb = new StringBuffer("[");
            Object[] ary = (Object[]) o;
            for(Object ob : ary) {
                sb.append(toJson(ob));
            }
            sb.append("]");
        } else if(cl.isPrimitive()) {
            return "" + o;
        } else {
            sb = new StringBuffer("{");
            Map<String, Object> map = Collections.EMPTY_MAP;
            try {
                map = toMap(o);
            }catch(Exception e) {
                e.printStackTrace();
            }
            Object v;
            for(String key : map.keySet()) {
                sb.append(key).append(":");
                v = map.get(key);
                if(v == null) {
                    sb.append("null,");
                    continue;
                }
                cl = v.getClass();
                if(cl.isArray() ) {
                    sb.append("[");
                    Object[]ar = (Object[]) v;
                    if(ar.length > 0) {
                        for(Object ob : ar) {
                            sb.append(toJson(ob));
                            sb.append(",");
                        }
                        sb.setLength(sb.length()-1);
                    }
                    sb.append("],");
                } else if(Collection.class.isAssignableFrom(cl)) {
                    sb.append("[");
                    Collection c = (Collection) v;
                    if(!c.isEmpty()) {
                        for(Object ob : c) {
                            sb.append(toJson(ob));
                            sb.append(",");
                        }
                        sb.setLength(sb.length()-1);
                    }
                    sb.append("],");
                } else if (cl.isPrimitive() || v instanceof String) {
                    if(v instanceof String) {
                        sb.append("\"").append(v).append("\",");
                    } else {
                        sb.append(v).append(",");
                    }
                } else {
                    sb.append(toJson(v)).append(",");
                }
            }
            sb.setLength(sb.length()-1);
            sb.append("}");
        }
           
        return sb.toString();
    }
        

    private static Field byName(List<Field> fs, String name) {
        for (Field f : fs) {
            if (f.getName().equals(name)) {
                return f;
            }
        }
        throw new IllegalArgumentException("no field " + name + " in "
                + Dbg.oS(fs));
    }

    public static void fromMap(Object o, Map<String, Object> map) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if(o == null || map == null) {
            throw new IllegalArgumentException( "object " + o + " or map " + Dbg.oS(map) + " is null");
        }
        Class<?> c = o.getClass();
        List<Field> fs = FieldUtil.getLinealFields(c);
        for (String k : map.keySet()) {
            byName(fs, k).set(o, map.get(k));
        }
    }
    
    public static String dumpIfs(Object o) {
        
        if( o == null) return "";
        
        StringBuffer sb = new StringBuffer(o.toString() + " has ifs\n" );
        
        Class<?> c=  o.getClass();
        for(Class<?> i : c.getInterfaces()) {
            sb.append(i.getName() );
            sb.append("\n");
        }
        
        return sb.toString();
    }
    
    public static String dumpClasses(Object o) {
        StringBuffer sb = new StringBuffer(o.toString() + " has classes\n" );
        
        Class<?> c=  o.getClass();
        for(Class<?> i : c.getClasses()) {
            sb.append(i.getName() );
            sb.append("\n");
        }
        sb.append("super ");
        sb.append(c.getSuperclass() != null ? c.getSuperclass().getName():"");
        return sb.toString();
    }
    
    /**
     * @param c
     * @return String representation of class hierarchy from c up
     */
    public static String hierarchy(Class<?> c) {
        Class<?> s = c;
        StringBuffer sb = new StringBuffer(s.getSimpleName());
        while(s.getSuperclass() != null) {
            sb.append("->");
            s = s.getSuperclass();
            sb.append(s.getSimpleName());
        }
        return sb.toString();
    }
    
    /**
     * @param theClass
     * @return list of classes in hierarchy from theClass up
     */
    public static List<Class<?>> lineage(Class<?> theClass) {
        List<Class<?>> lineage = new ArrayList<Class<?>>();
        lineage.add(theClass);
        Class<?> s = theClass;
        while (s.getSuperclass() != null) {
            lineage.add(0,s.getSuperclass());
            s = s.getSuperclass();
        }
        log.debug(theClass.getSimpleName() + " ::=> " + Dbg.oS(lineage));
        return lineage;
    }

}
