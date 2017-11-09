package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONScanner;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.tvos.apps.utils.DateUtil;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessControlException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TypeUtils {
    public static boolean compatibleWithJavaBean;
    private static ConcurrentMap<String, Class<?>> mappings = new ConcurrentHashMap();
    private static Class<?> optionalClass;
    private static boolean optionalClassInited = false;
    private static Method oracleDateMethod;
    private static boolean oracleDateMethodInited = false;
    private static Method oracleTimestampMethod;
    private static boolean oracleTimestampMethodInited = false;
    private static Class<?> pathClass;
    private static boolean pathClass_error = false;
    private static boolean setAccessibleEnable = true;

    static {
        compatibleWithJavaBean = false;
        try {
            String prop = System.getProperty("fastjson.compatibleWithJavaBean");
            if ("true".equals(prop)) {
                compatibleWithJavaBean = true;
            } else if ("false".equals(prop)) {
                compatibleWithJavaBean = false;
            }
        } catch (Throwable th) {
        }
        addBaseClassMappings();
    }

    public static String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }

    public static Byte castToByte(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Byte.valueOf(((Number) value).byteValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            return Byte.valueOf(Byte.parseByte(strVal));
        }
        throw new JSONException("can not cast to byte, value : " + value);
    }

    public static Character castToChar(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Character) {
            return (Character) value;
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            if (strVal.length() == 1) {
                return Character.valueOf(strVal.charAt(0));
            }
            throw new JSONException("can not cast to char, value : " + value);
        }
        throw new JSONException("can not cast to char, value : " + value);
    }

    public static Short castToShort(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            return Short.valueOf(Short.parseShort(strVal));
        }
        throw new JSONException("can not cast to short, value : " + value);
    }

    public static BigDecimal castToBigDecimal(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof BigInteger) {
            return new BigDecimal((BigInteger) value);
        }
        String strVal = value.toString();
        if (strVal.length() == 0) {
            return null;
        }
        return new BigDecimal(strVal);
    }

    public static BigInteger castToBigInteger(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof BigInteger) {
            return (BigInteger) value;
        }
        if ((value instanceof Float) || (value instanceof Double)) {
            return BigInteger.valueOf(((Number) value).longValue());
        }
        String strVal = value.toString();
        if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
            return null;
        }
        return new BigInteger(strVal);
    }

    public static Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Float.valueOf(Float.parseFloat(strVal));
        }
        throw new JSONException("can not cast to float, value : " + value);
    }

    public static Double castToDouble(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Double.valueOf(((Number) value).doubleValue());
        }
        if (value instanceof String) {
            String strVal = value.toString();
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Double.valueOf(Double.parseDouble(strVal));
        }
        throw new JSONException("can not cast to double, value : " + value);
    }

    public static Date castToDate(Object value) {
        Date date = null;
        if (value == null) {
            return null;
        }
        if (value instanceof Date) {
            return (Date) value;
        }
        if (value instanceof Calendar) {
            return ((Calendar) value).getTime();
        }
        long longValue = -1;
        if (value instanceof Number) {
            return new Date(((Number) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            JSONScanner dateLexer = new JSONScanner(strVal);
            try {
                if (dateLexer.scanISO8601DateIfMatch(false)) {
                    date = dateLexer.getCalendar().getTime();
                    return date;
                }
                dateLexer.close();
                if (strVal.startsWith("/Date(") && strVal.endsWith(")/")) {
                    strVal = strVal.substring(6, strVal.length() - 2);
                }
                if (strVal.indexOf(45) != -1) {
                    String format;
                    if (strVal.length() == JSON.DEFFAULT_DATE_FORMAT.length()) {
                        format = JSON.DEFFAULT_DATE_FORMAT;
                    } else if (strVal.length() == 10) {
                        format = DateUtil.PATTERN_STANDARD10H;
                    } else if (strVal.length() == DateUtil.PATTERN_STANDARD19H.length()) {
                        format = DateUtil.PATTERN_STANDARD19H;
                    } else {
                        format = "yyyy-MM-dd HH:mm:ss.SSS";
                    }
                    SimpleDateFormat dateFormat = new SimpleDateFormat(format, JSON.defaultLocale);
                    dateFormat.setTimeZone(JSON.defaultTimeZone);
                    try {
                        return dateFormat.parse(strVal);
                    } catch (ParseException e) {
                        throw new JSONException("can not cast to Date, value : " + strVal);
                    }
                } else if (strVal.length() == 0) {
                    return null;
                } else {
                    longValue = Long.parseLong(strVal);
                }
            } finally {
                dateLexer.close();
            }
        }
        if (longValue >= 0) {
            return new Date(longValue);
        }
        Class<?> clazz = value.getClass();
        if ("oracle.sql.TIMESTAMP".equals(clazz.getName())) {
            if (oracleTimestampMethod == null && !oracleTimestampMethodInited) {
                try {
                    oracleTimestampMethod = clazz.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException e2) {
                } finally {
                    oracleTimestampMethodInited = true;
                }
            }
            try {
                return (Date) oracleTimestampMethod.invoke(value, new Object[0]);
            } catch (Exception e3) {
                throw new JSONException("can not cast oracle.sql.TIMESTAMP to Date", e3);
            }
        } else if ("oracle.sql.DATE".equals(clazz.getName())) {
            if (oracleDateMethod == null && !oracleDateMethodInited) {
                try {
                    oracleDateMethod = clazz.getMethod("toJdbc", new Class[0]);
                } catch (NoSuchMethodException e4) {
                } finally {
                    oracleDateMethodInited = true;
                }
            }
            try {
                return (Date) oracleDateMethod.invoke(value, new Object[0]);
            } catch (Exception e32) {
                throw new JSONException("can not cast oracle.sql.DATE to Date", e32);
            }
        } else {
            throw new JSONException("can not cast to Date, value : " + value);
        }
    }

    public static java.sql.Date castToSqlDate(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Date) {
            return (java.sql.Date) value;
        }
        if (value instanceof Date) {
            return new java.sql.Date(((Date) value).getTime());
        }
        if (value instanceof Calendar) {
            return new java.sql.Date(((Calendar) value).getTimeInMillis());
        }
        long j = 0;
        if (value instanceof Number) {
            j = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            j = Long.parseLong(strVal);
        }
        if (j > 0) {
            return new java.sql.Date(j);
        }
        throw new JSONException("can not cast to Date, value : " + value);
    }

    public static Timestamp castToTimestamp(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return new Timestamp(((Calendar) value).getTimeInMillis());
        }
        if (value instanceof Timestamp) {
            return (Timestamp) value;
        }
        if (value instanceof Date) {
            return new Timestamp(((Date) value).getTime());
        }
        long j = 0;
        if (value instanceof Number) {
            j = ((Number) value).longValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            j = Long.parseLong(strVal);
        }
        if (j > 0) {
            return new Timestamp(j);
        }
        throw new JSONException("can not cast to Date, value : " + value);
    }

    public static Long castToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            return Long.valueOf(((Number) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            try {
                return Long.valueOf(Long.parseLong(strVal));
            } catch (NumberFormatException e) {
                JSONScanner dateParser = new JSONScanner(strVal);
                Calendar calendar = null;
                if (dateParser.scanISO8601DateIfMatch(false)) {
                    calendar = dateParser.getCalendar();
                }
                dateParser.close();
                if (calendar != null) {
                    return Long.valueOf(calendar.getTimeInMillis());
                }
            }
        }
        throw new JSONException("can not cast to long, value : " + value);
    }

    public static Integer castToInt(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer) value;
        }
        if (value instanceof Number) {
            return Integer.valueOf(((Number) value).intValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if (strVal.indexOf(44) != 0) {
                strVal = strVal.replaceAll(",", "");
            }
            return Integer.valueOf(Integer.parseInt(strVal));
        } else if (value instanceof Boolean) {
            return Integer.valueOf(((Boolean) value).booleanValue() ? 1 : 0);
        } else {
            throw new JSONException("can not cast to int, value : " + value);
        }
    }

    public static byte[] castToBytes(Object value) {
        if (value instanceof byte[]) {
            return (byte[]) value;
        }
        if (value instanceof String) {
            return IOUtils.decodeBase64((String) value);
        }
        throw new JSONException("can not cast to int, value : " + value);
    }

    public static Boolean castToBoolean(Object value) {
        boolean z = true;
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Number) {
            if (((Number) value).intValue() != 1) {
                z = false;
            }
            return Boolean.valueOf(z);
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
            if ("true".equalsIgnoreCase(strVal) || "1".equals(strVal)) {
                return Boolean.TRUE;
            }
            if ("false".equalsIgnoreCase(strVal) || "0".equals(strVal)) {
                return Boolean.FALSE;
            }
        }
        throw new JSONException("can not cast to boolean, value : " + value);
    }

    public static <T> T castToJavaBean(Object obj, Class<T> clazz) {
        return cast(obj, (Class) clazz, ParserConfig.getGlobalInstance());
    }

    public static <T> T cast(Object obj, Class<T> clazz, ParserConfig config) {
        if (obj == null) {
            return null;
        }
        if (clazz == null) {
            throw new IllegalArgumentException("clazz is null");
        } else if (clazz == obj.getClass()) {
            return obj;
        } else {
            if (!(obj instanceof Map)) {
                if (clazz.isArray()) {
                    if (obj instanceof Collection) {
                        Collection<Object> collection = (Collection) obj;
                        int index = 0;
                        Object array = Array.newInstance(clazz.getComponentType(), collection.size());
                        for (Object item : collection) {
                            Array.set(array, index, cast(item, clazz.getComponentType(), config));
                            index++;
                        }
                        return array;
                    } else if (clazz == byte[].class) {
                        return castToBytes(obj);
                    }
                }
                if (clazz.isAssignableFrom(obj.getClass())) {
                    return obj;
                }
                if (clazz == Boolean.TYPE || clazz == Boolean.class) {
                    return castToBoolean(obj);
                }
                if (clazz == Byte.TYPE || clazz == Byte.class) {
                    return castToByte(obj);
                }
                if (clazz == Short.TYPE || clazz == Short.class) {
                    return castToShort(obj);
                }
                if (clazz == Integer.TYPE || clazz == Integer.class) {
                    return castToInt(obj);
                }
                if (clazz == Long.TYPE || clazz == Long.class) {
                    return castToLong(obj);
                }
                if (clazz == Float.TYPE || clazz == Float.class) {
                    return castToFloat(obj);
                }
                if (clazz == Double.TYPE || clazz == Double.class) {
                    return castToDouble(obj);
                }
                if (clazz == String.class) {
                    return castToString(obj);
                }
                if (clazz == BigDecimal.class) {
                    return castToBigDecimal(obj);
                }
                if (clazz == BigInteger.class) {
                    return castToBigInteger(obj);
                }
                if (clazz == Date.class) {
                    return castToDate(obj);
                }
                if (clazz == java.sql.Date.class) {
                    return castToSqlDate(obj);
                }
                if (clazz == Timestamp.class) {
                    return castToTimestamp(obj);
                }
                if (clazz.isEnum()) {
                    return castToEnum(obj, clazz, config);
                }
                if (Calendar.class.isAssignableFrom(clazz)) {
                    Calendar calendar;
                    Date date = castToDate(obj);
                    if (clazz == Calendar.class) {
                        calendar = Calendar.getInstance(JSON.defaultTimeZone, JSON.defaultLocale);
                    } else {
                        try {
                            calendar = (Calendar) clazz.newInstance();
                        } catch (Exception e) {
                            throw new JSONException("can not cast to : " + clazz.getName(), e);
                        }
                    }
                    calendar.setTime(date);
                    return calendar;
                }
                if (obj instanceof String) {
                    String strVal = (String) obj;
                    if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                        return null;
                    }
                    if (clazz == Currency.class) {
                        return Currency.getInstance(strVal);
                    }
                }
                throw new JSONException("can not cast to : " + clazz.getName());
            } else if (clazz == Map.class) {
                return obj;
            } else {
                Map map = (Map) obj;
                if (clazz != Object.class || map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                    return castToJavaBean((Map) obj, clazz, config);
                }
                return obj;
            }
        }
    }

    public static <T> T castToEnum(Object obj, Class<T> clazz, ParserConfig mapping) {
        try {
            if (obj instanceof String) {
                String name = (String) obj;
                if (name.length() == 0) {
                    return null;
                }
                return Enum.valueOf(clazz, name);
            }
            if (obj instanceof Number) {
                int ordinal = ((Number) obj).intValue();
                Object[] values = clazz.getEnumConstants();
                if (ordinal < values.length) {
                    return values[ordinal];
                }
            }
            throw new JSONException("can not cast to : " + clazz.getName());
        } catch (Exception ex) {
            throw new JSONException("can not cast to : " + clazz.getName(), ex);
        }
    }

    public static <T> T cast(Object obj, Type type, ParserConfig mapping) {
        if (obj == null) {
            return null;
        }
        if (type instanceof Class) {
            return cast(obj, (Class) type, mapping);
        }
        if (type instanceof ParameterizedType) {
            return cast(obj, (ParameterizedType) type, mapping);
        }
        if (obj instanceof String) {
            String strVal = (String) obj;
            if (strVal.length() == 0 || "null".equals(strVal) || "NULL".equals(strVal)) {
                return null;
            }
        }
        if (type instanceof TypeVariable) {
            return obj;
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static <T> T cast(Object obj, ParameterizedType type, ParserConfig mapping) {
        Type rawTye = type.getRawType();
        if (rawTye == Set.class || rawTye == HashSet.class || rawTye == TreeSet.class || rawTye == List.class || rawTye == ArrayList.class) {
            Type itemType = type.getActualTypeArguments()[0];
            if (obj instanceof Iterable) {
                T collection;
                if (rawTye == Set.class || rawTye == HashSet.class) {
                    collection = new HashSet();
                } else if (rawTye == TreeSet.class) {
                    collection = new TreeSet();
                } else {
                    collection = new ArrayList();
                }
                for (Object item : (Iterable) obj) {
                    collection.add(cast(item, itemType, mapping));
                }
                return collection;
            }
        }
        if (rawTye == Map.class || rawTye == HashMap.class) {
            Type keyType = type.getActualTypeArguments()[0];
            Type valueType = type.getActualTypeArguments()[1];
            if (obj instanceof Map) {
                T map = new HashMap();
                for (Entry entry : ((Map) obj).entrySet()) {
                    map.put(cast(entry.getKey(), keyType, mapping), cast(entry.getValue(), valueType, mapping));
                }
                return map;
            }
        }
        if ((obj instanceof String) && ((String) obj).length() == 0) {
            return null;
        }
        if (type.getActualTypeArguments().length == 1 && (type.getActualTypeArguments()[0] instanceof WildcardType)) {
            return cast(obj, rawTye, mapping);
        }
        throw new JSONException("can not cast to : " + type);
    }

    public static <T> T castToJavaBean(Map<String, Object> map, Class<T> clazz, ParserConfig config) {
        try {
            if (clazz == StackTraceElement.class) {
                int lineNumber;
                String declaringClass = (String) map.get("className");
                String methodName = (String) map.get("methodName");
                String fileName = (String) map.get("fileName");
                Number value = (Number) map.get("lineNumber");
                if (value == null) {
                    lineNumber = 0;
                } else {
                    lineNumber = value.intValue();
                }
                return new StackTraceElement(declaringClass, methodName, fileName, lineNumber);
            }
            Object iClassObject = map.get(JSON.DEFAULT_TYPE_KEY);
            if (iClassObject instanceof String) {
                String className = (String) iClassObject;
                Class<?> loadClazz = loadClass(className);
                if (loadClazz == null) {
                    throw new ClassNotFoundException(className + " not found");
                } else if (!loadClazz.equals(clazz)) {
                    return castToJavaBean(map, loadClazz, config);
                }
            }
            if (clazz.isInterface()) {
                JSONObject object;
                if (map instanceof JSONObject) {
                    object = (JSONObject) map;
                } else {
                    object = new JSONObject((Map) map);
                }
                return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, object);
            }
            if (config == null) {
                config = ParserConfig.getGlobalInstance();
            }
            JavaBeanDeserializer javaBeanDeser = null;
            ObjectDeserializer deserizer = config.getDeserializer((Type) clazz);
            if (deserizer instanceof JavaBeanDeserializer) {
                javaBeanDeser = (JavaBeanDeserializer) deserizer;
            }
            if (javaBeanDeser != null) {
                return javaBeanDeser.createInstance((Map) map, config);
            }
            throw new JSONException("can not get javaBeanDeserializer");
        } catch (Exception e) {
            throw new JSONException(e.getMessage(), e);
        }
    }

    private static void addBaseClassMappings() {
        mappings.put("byte", Byte.TYPE);
        mappings.put("short", Short.TYPE);
        mappings.put("int", Integer.TYPE);
        mappings.put("long", Long.TYPE);
        mappings.put("float", Float.TYPE);
        mappings.put("double", Double.TYPE);
        mappings.put("boolean", Boolean.TYPE);
        mappings.put("char", Character.TYPE);
        mappings.put("[byte", byte[].class);
        mappings.put("[short", short[].class);
        mappings.put("[int", int[].class);
        mappings.put("[long", long[].class);
        mappings.put("[float", float[].class);
        mappings.put("[double", double[].class);
        mappings.put("[boolean", boolean[].class);
        mappings.put("[char", char[].class);
        mappings.put(HashMap.class.getName(), HashMap.class);
    }

    public static void clearClassMapping() {
        mappings.clear();
        addBaseClassMappings();
    }

    public static Class<?> loadClass(String className) {
        return loadClass(className, null);
    }

    public static boolean isPath(Class<?> clazz) {
        if (pathClass == null && !pathClass_error) {
            try {
                pathClass = Class.forName("java.nio.file.Path");
            } catch (Throwable th) {
                pathClass_error = true;
            }
        }
        if (pathClass != null) {
            return pathClass.isAssignableFrom(clazz);
        }
        return false;
    }

    public static Class<?> loadClass(String className, ClassLoader classLoader) {
        if (className == null || className.length() == 0) {
            return null;
        }
        Class<?> clazz = (Class) mappings.get(className);
        if (clazz != null) {
            return clazz;
        }
        if (className.charAt(0) == '[') {
            return Array.newInstance(loadClass(className.substring(1), classLoader), 0).getClass();
        }
        if (className.startsWith("L") && className.endsWith(";")) {
            return loadClass(className.substring(1, className.length() - 1), classLoader);
        }
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(className);
                mappings.put(className, clazz);
                return clazz;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                clazz = contextClassLoader.loadClass(className);
                mappings.put(className, clazz);
                return clazz;
            }
        } catch (Throwable th) {
        }
        try {
            clazz = Class.forName(className);
            mappings.put(className, clazz);
            return clazz;
        } catch (Throwable th2) {
            return clazz;
        }
    }

    public static List<FieldInfo> computeGetters(Class<?> clazz, JSONType jsonType, Map<String, String> aliasMap, boolean sorted) {
        Map<String, FieldInfo> fieldInfoMap = new LinkedHashMap();
        for (Method method : clazz.getMethods()) {
            String propertyName;
            Field field;
            JSONField jSONField;
            String methodName = method.getName();
            int ordinal = 0;
            int serialzeFeatures = 0;
            String label = null;
            if (!(Modifier.isStatic(method.getModifiers()) || method.getReturnType().equals(Void.TYPE) || method.getParameterTypes().length != 0 || method.getReturnType() == ClassLoader.class || (method.getName().equals("getMetaClass") && method.getReturnType().getName().equals("groovy.lang.MetaClass")))) {
                String str;
                JSONField annotation = (JSONField) method.getAnnotation(JSONField.class);
                if (annotation == null) {
                    annotation = getSupperMethodAnnotation(clazz, method);
                }
                if (annotation != null) {
                    if (annotation.serialize()) {
                        ordinal = annotation.ordinal();
                        serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                        if (annotation.name().length() != 0) {
                            propertyName = annotation.name();
                            if (aliasMap != null) {
                                propertyName = (String) aliasMap.get(propertyName);
                                if (propertyName == null) {
                                }
                            }
                            fieldInfoMap.put(propertyName, new FieldInfo(propertyName, method, null, clazz, null, ordinal, serialzeFeatures, annotation, null, null));
                        } else if (annotation.label().length() != 0) {
                            label = annotation.label();
                        }
                    }
                }
                if (methodName.startsWith("get")) {
                    if (methodName.length() >= 4) {
                        if (!methodName.equals("getClass")) {
                            char c3 = methodName.charAt(3);
                            if (Character.isUpperCase(c3) || c3 > 'Ȁ') {
                                if (compatibleWithJavaBean) {
                                    propertyName = decapitalize(methodName.substring(3));
                                } else {
                                    propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                                }
                            } else if (c3 == '_') {
                                propertyName = methodName.substring(4);
                            } else if (c3 == 'f') {
                                propertyName = methodName.substring(3);
                            } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                                propertyName = decapitalize(methodName.substring(3));
                            }
                            if (!isJSONTypeIgnore(clazz, propertyName)) {
                                field = ParserConfig.getField(clazz, propertyName);
                                jSONField = null;
                                if (field != null) {
                                    jSONField = (JSONField) field.getAnnotation(JSONField.class);
                                    if (jSONField != null) {
                                        if (jSONField.serialize()) {
                                            ordinal = jSONField.ordinal();
                                            serialzeFeatures = SerializerFeature.of(jSONField.serialzeFeatures());
                                            if (jSONField.name().length() != 0) {
                                                propertyName = jSONField.name();
                                                if (aliasMap != null) {
                                                    propertyName = (String) aliasMap.get(propertyName);
                                                    if (propertyName == null) {
                                                    }
                                                }
                                            }
                                            if (jSONField.label().length() != 0) {
                                                label = jSONField.label();
                                                str = propertyName;
                                                if (aliasMap != null) {
                                                    propertyName = (String) aliasMap.get(str);
                                                    if (propertyName != null) {
                                                        str = propertyName;
                                                    }
                                                }
                                                fieldInfoMap.put(str, new FieldInfo(str, method, field, clazz, null, ordinal, serialzeFeatures, annotation, jSONField, label));
                                            }
                                        }
                                    }
                                }
                                str = propertyName;
                                if (aliasMap != null) {
                                    propertyName = (String) aliasMap.get(str);
                                    if (propertyName != null) {
                                        str = propertyName;
                                    }
                                }
                                fieldInfoMap.put(str, new FieldInfo(str, method, field, clazz, null, ordinal, serialzeFeatures, annotation, jSONField, label));
                            }
                        }
                    }
                }
                if (methodName.startsWith("is") && methodName.length() >= 3) {
                    char c2 = methodName.charAt(2);
                    if (Character.isUpperCase(c2)) {
                        if (compatibleWithJavaBean) {
                            propertyName = decapitalize(methodName.substring(2));
                        } else {
                            propertyName = Character.toLowerCase(methodName.charAt(2)) + methodName.substring(3);
                        }
                    } else if (c2 == '_') {
                        propertyName = methodName.substring(3);
                    } else if (c2 == 'f') {
                        propertyName = methodName.substring(2);
                    }
                    field = ParserConfig.getField(clazz, propertyName);
                    if (field == null) {
                        field = ParserConfig.getField(clazz, methodName);
                    }
                    jSONField = null;
                    if (field != null) {
                        jSONField = (JSONField) field.getAnnotation(JSONField.class);
                        if (jSONField != null) {
                            if (jSONField.serialize()) {
                                ordinal = jSONField.ordinal();
                                serialzeFeatures = SerializerFeature.of(jSONField.serialzeFeatures());
                                if (jSONField.name().length() != 0) {
                                    propertyName = jSONField.name();
                                    if (aliasMap != null) {
                                        propertyName = (String) aliasMap.get(propertyName);
                                        if (propertyName == null) {
                                        }
                                    }
                                }
                                if (jSONField.label().length() != 0) {
                                    label = jSONField.label();
                                    str = propertyName;
                                    if (aliasMap != null) {
                                        propertyName = (String) aliasMap.get(str);
                                        if (propertyName != null) {
                                            str = propertyName;
                                        }
                                    }
                                    fieldInfoMap.put(str, new FieldInfo(str, method, field, clazz, null, ordinal, serialzeFeatures, annotation, jSONField, label));
                                }
                            }
                        }
                    }
                    str = propertyName;
                    if (aliasMap != null) {
                        propertyName = (String) aliasMap.get(str);
                        if (propertyName != null) {
                            str = propertyName;
                        }
                    }
                    fieldInfoMap.put(str, new FieldInfo(str, method, field, clazz, null, ordinal, serialzeFeatures, annotation, jSONField, label));
                }
            }
        }
        for (Field field2 : clazz.getFields()) {
            if (!Modifier.isStatic(field2.getModifiers())) {
                jSONField = (JSONField) field2.getAnnotation(JSONField.class);
                ordinal = 0;
                serialzeFeatures = 0;
                propertyName = field2.getName();
                label = null;
                if (jSONField != null) {
                    if (jSONField.serialize()) {
                        ordinal = jSONField.ordinal();
                        serialzeFeatures = SerializerFeature.of(jSONField.serialzeFeatures());
                        if (jSONField.name().length() != 0) {
                            propertyName = jSONField.name();
                        }
                        if (jSONField.label().length() != 0) {
                            label = jSONField.label();
                        }
                    }
                }
                if (aliasMap != null) {
                    propertyName = (String) aliasMap.get(propertyName);
                    if (propertyName == null) {
                    }
                }
                if (!fieldInfoMap.containsKey(propertyName)) {
                    fieldInfoMap.put(propertyName, new FieldInfo(propertyName, null, field2, clazz, null, ordinal, serialzeFeatures, null, jSONField, label));
                }
            }
        }
        List<FieldInfo> fieldInfoList = new ArrayList();
        boolean containsAll = false;
        String[] orders = null;
        JSONType annotation2 = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation2 != null) {
            orders = annotation2.orders();
            if (orders == null || orders.length != fieldInfoMap.size()) {
                containsAll = false;
            } else {
                containsAll = true;
                for (String item : orders) {
                    if (!fieldInfoMap.containsKey(item)) {
                        containsAll = false;
                        break;
                    }
                }
            }
        }
        if (containsAll) {
            for (String item2 : orders) {
                fieldInfoList.add((FieldInfo) fieldInfoMap.get(item2));
            }
        } else {
            for (FieldInfo fieldInfo : fieldInfoMap.values()) {
                fieldInfoList.add(fieldInfo);
            }
            if (sorted) {
                Collections.sort(fieldInfoList);
            }
        }
        return fieldInfoList;
    }

    public static JSONField getSupperMethodAnnotation(Class<?> clazz, Method method) {
        Class<?>[] interfaces = clazz.getInterfaces();
        if (interfaces.length > 0) {
            Class<?>[] types = method.getParameterTypes();
            for (Class<?> interfaceClass : interfaces) {
                for (Method interfaceMethod : interfaceClass.getMethods()) {
                    Class<?>[] interfaceTypes = interfaceMethod.getParameterTypes();
                    if (interfaceTypes.length == types.length && interfaceMethod.getName().equals(method.getName())) {
                        boolean match = true;
                        for (int i = 0; i < types.length; i++) {
                            if (!interfaceTypes[i].equals(types[i])) {
                                match = false;
                                break;
                            }
                        }
                        if (match) {
                            JSONField annotation = (JSONField) interfaceMethod.getAnnotation(JSONField.class);
                            if (annotation != null) {
                                return annotation;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
        }
        return null;
    }

    private static boolean isJSONTypeIgnore(Class<?> clazz, String propertyName) {
        JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
        if (jsonType != null) {
            String[] fields = jsonType.includes();
            if (fields.length > 0) {
                for (Object equals : fields) {
                    if (propertyName.equals(equals)) {
                        return false;
                    }
                }
                return true;
            }
            fields = jsonType.ignores();
            for (Object equals2 : fields) {
                if (propertyName.equals(equals2)) {
                    return true;
                }
            }
        }
        if (clazz.getSuperclass() == Object.class || clazz.getSuperclass() == null || !isJSONTypeIgnore(clazz.getSuperclass(), propertyName)) {
            return false;
        }
        return true;
    }

    public static boolean isGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (!(type instanceof Class)) {
            return false;
        }
        Type superType = ((Class) type).getGenericSuperclass();
        if (superType != Object.class) {
            return isGenericParamType(superType);
        }
        return false;
    }

    public static Type getGenericParamType(Type type) {
        if (!(type instanceof ParameterizedType) && (type instanceof Class)) {
            return getGenericParamType(((Class) type).getGenericSuperclass());
        }
        return type;
    }

    public static Type unwrapOptional(Type type) {
        if (!optionalClassInited) {
            try {
                optionalClass = Class.forName("java.util.Optional");
            } catch (Exception e) {
            } finally {
                optionalClassInited = true;
            }
        }
        if (!(type instanceof ParameterizedType)) {
            return type;
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        if (parameterizedType.getRawType() == optionalClass) {
            return parameterizedType.getActualTypeArguments()[0];
        }
        return type;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getClass(((ParameterizedType) type).getRawType());
        }
        if (type instanceof TypeVariable) {
            return (Class) ((TypeVariable) type).getBounds()[0];
        }
        return Object.class;
    }

    public static Field getField(Class<?> clazz, String fieldName, Field[] declaredFields) {
        for (Field field : declaredFields) {
            if (fieldName.equals(field.getName())) {
                return field;
            }
        }
        Class<?> superClass = clazz.getSuperclass();
        if (superClass == null || superClass == Object.class) {
            return null;
        }
        return getField(superClass, fieldName, superClass.getDeclaredFields());
    }

    public static int getSerializeFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return SerializerFeature.of(annotation.serialzeFeatures());
    }

    public static int getParserFeatures(Class<?> clazz) {
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation == null) {
            return 0;
        }
        return Feature.of(annotation.parseFeatures());
    }

    public static String decapitalize(String name) {
        if (name == null || name.length() == 0) {
            return name;
        }
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char[] chars = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }

    static void setAccessible(AccessibleObject obj) {
        if (setAccessibleEnable && !obj.isAccessible()) {
            try {
                obj.setAccessible(true);
            } catch (AccessControlException e) {
                setAccessibleEnable = false;
            }
        }
    }

    public static Class<?> getCollectionItemClass(Type fieldType) {
        if (!(fieldType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type actualTypeArgument = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        if (actualTypeArgument instanceof Class) {
            Class<?> cls = (Class) actualTypeArgument;
            if (Modifier.isPublic(cls.getModifiers())) {
                return cls;
            }
            throw new JSONException("can not create ASMParser");
        }
        throw new JSONException("can not create ASMParser");
    }

    public static Collection createCollection(Type type) {
        Class<?> rawClass = getRawClass(type);
        if (rawClass == AbstractCollection.class || rawClass == Collection.class) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(HashSet.class)) {
            return new HashSet();
        }
        if (rawClass.isAssignableFrom(LinkedHashSet.class)) {
            return new LinkedHashSet();
        }
        if (rawClass.isAssignableFrom(TreeSet.class)) {
            return new TreeSet();
        }
        if (rawClass.isAssignableFrom(ArrayList.class)) {
            return new ArrayList();
        }
        if (rawClass.isAssignableFrom(EnumSet.class)) {
            Type itemType;
            if (type instanceof ParameterizedType) {
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                itemType = Object.class;
            }
            return EnumSet.noneOf((Class) itemType);
        }
        try {
            return (Collection) rawClass.newInstance();
        } catch (Exception e) {
            throw new JSONException("create instane error, class " + rawClass.getName());
        }
    }

    public static Class<?> getRawClass(Type type) {
        if (type instanceof Class) {
            return (Class) type;
        }
        if (type instanceof ParameterizedType) {
            return getRawClass(((ParameterizedType) type).getRawType());
        }
        throw new JSONException("TODO");
    }
}
