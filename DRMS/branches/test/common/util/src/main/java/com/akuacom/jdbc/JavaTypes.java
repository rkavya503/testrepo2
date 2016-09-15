package com.akuacom.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Array;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

public class JavaTypes {
	
	//most used property types in java which can mapped to a sql type
	public static final int T_BOOLEAN                           = 0;
	public static final int T_BYTE                              = 1;
	public static final int T_CHAR                              = 2;
	public static final int T_DOUBLE                            = 3;
	public static final int T_FLOAT                             = 4;
	public static final int T_INT                               = 5;
	public static final int T_JAVA_LANG_BOOLEAN                 = 6;
	public static final int T_JAVA_LANG_BYTE                    = 7;
	public static final int T_JAVA_LANG_CHARACTER               = 8;
	public static final int T_JAVA_LANG_DOUBLE                  = 9;
	public static final int T_JAVA_LANG_FLOAT                   = 10;
	public static final int T_JAVA_LANG_INTEGER                 = 11;
	public static final int T_JAVA_LANG_LONG                    = 12;
	public static final int T_JAVA_LANG_SHORT                   = 13;
	public static final int T_JAVA_LANG_STRING                  = 14;
	public static final int T_JAVA_MATH_BIGDECIMAL              = 15;
	public static final int T_JAVA_MATH_BIGINTEGER              = 16;
	public static final int T_JAVA_SQL_ARRAY                    = 17;
	public static final int T_JAVA_SQL_DATE                     = 18;
	public static final int T_JAVA_SQL_TIME                     = 19;
	public static final int T_JAVA_SQL_TIMESTAMP                = 20;
	public static final int T_JAVA_UTIL_DATE                    = 21;
	public static final int T_LONG                              = 22;
	public static final int T_SHORT                             = 23;
	
	private static final String[] classNames= new String[] {
		int.class.getName(),
		Integer.class.getName(),
		long.class.getName(),
		Long.class.getName(),
		String.class.getName(),
		boolean.class.getName(),
		Boolean.class.getName(),
		short.class.getName(),
		Short.class.getName(),
		byte.class.getName(),
		Byte.class.getName(),
		double.class.getName(),
		Double.class.getName(),
		float.class.getName(),
		Float.class.getName(),
		char.class.getName(),
		Character.class.getName(),
		BigDecimal.class.getName(),
		BigInteger.class.getName(),
		Array.class.getName(),
		Date.class.getName(),
		java.sql.Date.class.getName(),
		Timestamp.class.getName(),
		Time.class.getName(),
	};
	
	static {
		Arrays.sort(classNames);
	}
	
	public static int getTypeId(Class<?> type){
		return Arrays.binarySearch(classNames,type.getName());
	}
	
	public static int getTypeId(String className){
		return Arrays.binarySearch(classNames,className);
	}
	
	public static boolean isAutoBoxing(Class<?> from, Class<?> to){
		if(from.isPrimitive() && !to.isPrimitive()){
			int fromId = getTypeId(from);
			int toId  = getTypeId(to);
			switch(fromId){
			case T_LONG:
				return toId == T_JAVA_LANG_LONG;
			case T_INT:
				return toId == T_JAVA_LANG_INTEGER;
			case T_SHORT:
				return toId== T_JAVA_LANG_SHORT;
			case T_DOUBLE:
				return toId == T_JAVA_LANG_DOUBLE;
			case T_BYTE:
				return toId == T_JAVA_LANG_BYTE;
			case T_BOOLEAN:
				return toId == T_JAVA_LANG_BOOLEAN;
			case T_FLOAT:
				return toId == T_JAVA_LANG_FLOAT;
			case T_CHAR:
				return toId == T_JAVA_LANG_CHARACTER;
			}
			
		}else if(!from.isPrimitive() && to.isPrimitive()){
			return isAutoBoxing(to,from);
		}
		return false;
	}
	
	
	public static boolean isCollection(Object value){
		if(value==null) return false;
		return Collection.class.isInstance(value);
	}
	
	public static boolean isArray(Object value){
		if(value==null) return false;
		return value.getClass().isArray();
	}
	
	public static boolean isBlob(Object value){
		if(value==null) return false;
		return Blob.class.isInstance(value);
	}
	
	public static <T> T  forceCast(Object object, Class<T> expectType){
		if(object == null) return null;
		
		Class<?> type = object.getClass();
		int toId  = getTypeId(expectType);
		//both are numbers
		if(isNumber(object) && isNumber(expectType)){
			Number number = (Number) object;
			int expectTypeId = Arrays.binarySearch(classNames,expectType.getName());
			switch(expectTypeId){
			case T_LONG:
			case T_JAVA_LANG_LONG:
				return (T) (Long) number.longValue();
			case T_INT:
			case T_JAVA_LANG_INTEGER:
				return (T) (Integer) number.intValue();
				
			case T_FLOAT:
			case T_JAVA_LANG_FLOAT:
				return (T) (Float) number.floatValue();
			case T_SHORT:
			case T_JAVA_LANG_SHORT:
				return (T)  (Short) number.shortValue();
			case T_DOUBLE:
			case T_JAVA_LANG_DOUBLE:
				return (T) (Double) number.doubleValue();
			case T_BYTE:
			case T_JAVA_LANG_BYTE:
				return (T) (Byte)number.byteValue();
			case T_JAVA_MATH_BIGDECIMAL:
				return (T) new BigDecimal(number.toString());
			default:
				return (T) object;
			}
		}
		
		if(isNumber(object) && (toId == T_BOOLEAN || toId== T_JAVA_LANG_BOOLEAN)){
			Number number = (Number) object;
			if(number.intValue()==0)
				return (T) Boolean.FALSE;
			else
				return (T) Boolean.TRUE;
		}
		
		if(expectType.isEnum()){
			Class<? extends Enum> type1 = (Class<? extends Enum>) expectType;
			int actualTypeId = Arrays.binarySearch(classNames,object.getClass().getName());
			switch(actualTypeId){
			case T_JAVA_LANG_STRING:
				 String str= (String)object;
				 return (T) Enum.valueOf(type1, str.trim());
			case T_INT:
			case T_JAVA_LANG_INTEGER:
				int ordinal = (Integer)object;
				for(Enum instance:type1.getEnumConstants()){
					if(instance.ordinal() == ordinal)
						return  (T)instance;
				}
				throw new IllegalArgumentException("No enum found for ordinal "+ordinal);
			default:
				throw new IllegalArgumentException("can not convert type "+type +" to "+expectType);
			}
		}
		
		if(type.isEnum()){
			int expectTypeId = Arrays.binarySearch(classNames,expectType.getName());
			switch(expectTypeId){
			case T_JAVA_LANG_STRING:
				 return (T) ((Enum)object).name();
			case T_INT:
			case T_JAVA_LANG_INTEGER:
				 return (T) (Integer) ((Enum)object).ordinal();
			default:
				throw new IllegalArgumentException("can not convert type "+type +" to "+expectType);
			}
		}
		throw new IllegalArgumentException("can not convert type "+type +" to "+expectType);
	}
	
	public static int  sizeOfCollectionOrArray(Object value){
		return JavaTypes.isCollection(value)? ((Collection<?>)value).size():
			((Object[])value).length;
	}
	
	public static Object[]  valuesOfCollectionOrArray(Object value){
		if(isArray(value))
			return (Object[])value;
		if(isCollection(value))
			return ((Collection<?>)value).toArray();
		return null;
	}
	
	public static void bindParameter(int pos, Object paramValue, 
					PreparedStatement ps) throws SQLException {
		if(paramValue==null){
			ps.setNull(pos,java.sql.Types.JAVA_OBJECT);
			return ;
		}
		
		Class<?> type=paramValue.getClass();
		int typeId = JavaTypes.getTypeId(type);
		
		if(TypedParameter.class.isInstance(paramValue)){
			TypedParameter typedParam = (TypedParameter) paramValue;
			ps.setObject(pos, typedParam.getValue(),typedParam.getSqlType());
			return;
		}
		if(JavaTypes.isBlob(paramValue)){
			ps.setBlob(pos, (Blob)paramValue);
			return;
		}
		switch(typeId){
			case JavaTypes. T_JAVA_LANG_STRING:
				ps.setString(pos,(String)paramValue);
				break;
			case JavaTypes. T_INT:
			case JavaTypes. T_JAVA_LANG_INTEGER:
				ps.setInt(pos,(Integer)paramValue );
				break;
			case JavaTypes. T_BOOLEAN:
			case JavaTypes. T_JAVA_LANG_BOOLEAN:
				ps.setBoolean(pos,(Boolean)paramValue);
				break;
			case JavaTypes. T_LONG:
			case JavaTypes. T_JAVA_LANG_LONG:
				ps.setLong(pos, (Long)paramValue);
				break;
			case JavaTypes. T_SHORT:
			case JavaTypes. T_JAVA_LANG_SHORT:
				ps.setShort(pos, (Short)paramValue);
				break;
			case JavaTypes. T_BYTE:
			case JavaTypes. T_JAVA_LANG_BYTE:
				ps.setByte(pos, (Byte)paramValue);
				break;
			case JavaTypes. T_JAVA_UTIL_DATE:
				ps.setTimestamp(pos, JavaTypes.dateToSqlTimeStamp((Date)paramValue));
				break;
			case JavaTypes. T_JAVA_SQL_TIMESTAMP:
				ps.setTimestamp(pos, (Timestamp)paramValue);
				break;
			case JavaTypes. T_JAVA_SQL_TIME:
				ps.setTime(pos,(Time)paramValue);
				break;
			case JavaTypes. T_JAVA_SQL_DATE:
				ps.setDate(pos, (java.sql.Date)paramValue);
				break;
			case JavaTypes. T_JAVA_MATH_BIGDECIMAL:
				ps.setBigDecimal(pos, (BigDecimal)paramValue);
				break;
			case JavaTypes. T_DOUBLE:
			case JavaTypes. T_JAVA_LANG_DOUBLE:
				ps.setDouble(pos, (Double)paramValue);
				break;
			case JavaTypes. T_FLOAT:
			case JavaTypes. T_JAVA_LANG_FLOAT:
				ps.setFloat(pos, (Float)paramValue);
				break;
			case JavaTypes. T_JAVA_SQL_ARRAY:
				ps.setArray(pos, (Array)paramValue);
				break;
			default:
				ps.setObject(pos, paramValue);
				break;
		}
	}
	
	public static boolean isNumber(Object object){
		return Number.class.isInstance(object);
	}
	
	public static boolean isNumber(Class<?> type){
		return Number.class.isAssignableFrom(type) ||
			type.isPrimitive() && type!=boolean.class && type!=char.class && type!=void.class;
	}
	
	public static java.sql.Date dateToSqlDate(Date date){
		if(date instanceof java.sql.Date){
			return (java.sql.Date) date;
		}
		return new java.sql.Date(date.getTime());
	}
	
	public static java.sql.Timestamp dateToSqlTimeStamp(Date date){
		if(date instanceof Timestamp){
			return  (Timestamp) date;
		}
		return new java.sql.Timestamp(date.getTime());
	}
	
	public static java.sql.Time dateToSqlTime(Date date){
		if(date instanceof java.sql.Time){
			return  (Time) date;
		}
		return new java.sql.Time(date.getTime());
	}
	
	
	 
	// ------------ helper methods to generate java type code ----//
	public static void main(String[] args) {
		printTypesDeclaration();
		byte[] b = new byte[]{};
		System.out.println(b.getClass().isArray());
		System.out.println(b.getClass().getComponentType());
	}
	
	protected static void printTypesDeclaration() {
		Arrays.sort(classNames);
		int maxlen = 35;
		for (int i = 0; i < classNames.length; i++) {
			String typeName = "T_" + classNames[i];
			typeName = typeName.toUpperCase().replace('.', '_');
			StringBuffer buffer = new StringBuffer();
			buffer.append("public static final int " + typeName);
			for (int j = 0; j < maxlen - typeName.length(); j++) {
				buffer.append(" ");
			}
			buffer.append(" = " + i + ";");
			System.out.println(buffer);
		}
	}
}