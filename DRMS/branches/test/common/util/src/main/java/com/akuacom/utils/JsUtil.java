package com.akuacom.utils;

import java.util.Collection;
import java.util.Iterator;

import scala.Product;

public class JsUtil {
    
    private static String value(Object o) {
        if(o instanceof String) {
            return "\""+o +"\"";
        } else if ( o instanceof Double|| o instanceof Float) {
            return o == null ? null : NumberFormats.dec2( o instanceof Double ? (Double) o : (Float) o + .0);
        }
        return o == null ? null : o.toString();
    }
    
    public static String toJsArray(Collection c) {
        StringBuffer sb = new StringBuffer("[");
        if (c.isEmpty()) {
            sb.append("[]");
            return sb.toString();
        }
        Iterator i = c.iterator();

        while (i.hasNext()) {
            Object o = i.next();
            if (o instanceof Collection) {
                sb.append(toJsArray((Collection) o));
            } else if (o != null && o.getClass().isArray()) {
                sb.append(toJsArray((Object[]) o));
            } else {
                sb.append(value(o));
            }
            if(i.hasNext()) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String toJsArray(Object[] ob) {
        return toJsArry(ob);
    }
    
    public static String toJsArray(double[] ob) {
        if (ob == null || ob.length==0) {
            return "[]";
        }
        StringBuffer sb = new StringBuffer("[");
        for(double d : ob) {
            sb.append(NumberFormats.dec2(d)).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append("]");
        return sb.toString();
    }
    
    public static String toJsArry(Object ar) {
        StringBuffer sb = new StringBuffer("[");
        Object[]a = (Object[]) ar;
        if (a == null || a.length == 0) {
            sb.append("]");
            return sb.toString();
        }

        for(int i = 0; i < a.length; i++ ) {
            Object o = a[i];
            if (o instanceof Collection) {
                sb.append(toJsArray((Collection) o));
            } else if (o != null && o.getClass().isArray()) {
                sb.append(toJsArray((Object[]) o));
            } else {
                sb.append(value(o));
            }
            if(i < a.length -1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static <P extends Product> String toJson(Collection ps, P fieldNames) {
        
        StringBuffer sb = new StringBuffer("[");
        if (ps.isEmpty()) {
            sb.append("]");
            return sb.toString();
        }
        Iterator<P>i= ps.iterator();
        Product p;
        while(i.hasNext()) {
            p = i.next();
            if(p.productArity() != fieldNames.productArity()) {
                throw new IllegalArgumentException("p.productArity() != fieldNames.productArity()");
            }
            sb.append("{");
            for(int idx = 0; idx < p.productArity(); idx++) {
                sb.append(fieldNames.productElement(idx));
                sb.append(": ");
                sb.append(value(p.productElement(idx)));
                if(idx < p.productArity()-1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            if(i.hasNext()) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String asJSVar(String src) {
        src = src.trim();
        src = src.replace(" ", "");
        src = src.replace("-", "");
        return src;
    }

}
