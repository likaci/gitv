package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.parser.deserializer.ASMDeserializerFactory;
import com.alibaba.fastjson.parser.deserializer.ArrayListTypeFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.AutowiredObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.DefaultFieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.EnumDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.JavaObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.Jdk8DateCodec;
import com.alibaba.fastjson.parser.deserializer.MapDeserializer;
import com.alibaba.fastjson.parser.deserializer.NumberDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.OptionalCodec;
import com.alibaba.fastjson.parser.deserializer.SqlDateDeserializer;
import com.alibaba.fastjson.parser.deserializer.StackTraceElementDeserializer;
import com.alibaba.fastjson.parser.deserializer.ThrowableDeserializer;
import com.alibaba.fastjson.parser.deserializer.TimeDeserializer;
import com.alibaba.fastjson.serializer.AtomicCodec;
import com.alibaba.fastjson.serializer.AwtCodec;
import com.alibaba.fastjson.serializer.BigDecimalCodec;
import com.alibaba.fastjson.serializer.BigIntegerCodec;
import com.alibaba.fastjson.serializer.BooleanCodec;
import com.alibaba.fastjson.serializer.CalendarCodec;
import com.alibaba.fastjson.serializer.CharArrayCodec;
import com.alibaba.fastjson.serializer.CharacterCodec;
import com.alibaba.fastjson.serializer.CollectionCodec;
import com.alibaba.fastjson.serializer.DateCodec;
import com.alibaba.fastjson.serializer.FloatCodec;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.MiscCodec;
import com.alibaba.fastjson.serializer.ObjectArrayCodec;
import com.alibaba.fastjson.serializer.ReferenceCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.ASMClassLoader;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.IdentityHashMap;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.ServiceLoader;
import java.io.Closeable;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.AccessControlException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TimeZone;
import java.util.TreeMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Pattern;

public class ParserConfig {
    public static final String DENY_PROPERTY = "fastjson.parser.deny";
    private static boolean awtError = false;
    public static ParserConfig global = new ParserConfig();
    private static boolean jdk8Error = false;
    private boolean asmEnable;
    protected ASMDeserializerFactory asmFactory;
    protected ClassLoader defaultClassLoader;
    private String[] denyList;
    private final IdentityHashMap<Type, ObjectDeserializer> derializers;
    public final SymbolTable symbolTable;

    public static ParserConfig getGlobalInstance() {
        return global;
    }

    public ParserConfig() {
        this(null, null);
    }

    public ParserConfig(ClassLoader parentClassLoader) {
        this(null, parentClassLoader);
    }

    public ParserConfig(ASMDeserializerFactory asmFactory) {
        this(asmFactory, null);
    }

    private ParserConfig(ASMDeserializerFactory asmFactory, ClassLoader parentClassLoader) {
        this.derializers = new IdentityHashMap();
        this.asmEnable = !ASMUtils.IS_ANDROID;
        this.symbolTable = new SymbolTable(4096);
        this.denyList = new String[]{"java.lang.Thread"};
        if (asmFactory == null && !ASMUtils.IS_ANDROID) {
            if (parentClassLoader == null) {
                try {
                    asmFactory = new ASMDeserializerFactory(new ASMClassLoader());
                } catch (ExceptionInInitializerError e) {
                } catch (AccessControlException e2) {
                } catch (NoClassDefFoundError e3) {
                }
            } else {
                asmFactory = new ASMDeserializerFactory(parentClassLoader);
            }
        }
        this.asmFactory = asmFactory;
        if (asmFactory == null) {
            this.asmEnable = false;
        }
        this.derializers.put(SimpleDateFormat.class, MiscCodec.instance);
        this.derializers.put(Timestamp.class, SqlDateDeserializer.instance_timestamp);
        this.derializers.put(Date.class, SqlDateDeserializer.instance);
        this.derializers.put(Time.class, TimeDeserializer.instance);
        this.derializers.put(java.util.Date.class, DateCodec.instance);
        this.derializers.put(Calendar.class, CalendarCodec.instance);
        this.derializers.put(JSONObject.class, MapDeserializer.instance);
        this.derializers.put(JSONArray.class, CollectionCodec.instance);
        this.derializers.put(Map.class, MapDeserializer.instance);
        this.derializers.put(HashMap.class, MapDeserializer.instance);
        this.derializers.put(LinkedHashMap.class, MapDeserializer.instance);
        this.derializers.put(TreeMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentMap.class, MapDeserializer.instance);
        this.derializers.put(ConcurrentHashMap.class, MapDeserializer.instance);
        this.derializers.put(Collection.class, CollectionCodec.instance);
        this.derializers.put(List.class, CollectionCodec.instance);
        this.derializers.put(ArrayList.class, CollectionCodec.instance);
        this.derializers.put(Object.class, JavaObjectDeserializer.instance);
        this.derializers.put(String.class, StringCodec.instance);
        this.derializers.put(StringBuffer.class, StringCodec.instance);
        this.derializers.put(StringBuilder.class, StringCodec.instance);
        this.derializers.put(Character.TYPE, CharacterCodec.instance);
        this.derializers.put(Character.class, CharacterCodec.instance);
        this.derializers.put(Byte.TYPE, NumberDeserializer.instance);
        this.derializers.put(Byte.class, NumberDeserializer.instance);
        this.derializers.put(Short.TYPE, NumberDeserializer.instance);
        this.derializers.put(Short.class, NumberDeserializer.instance);
        this.derializers.put(Integer.TYPE, IntegerCodec.instance);
        this.derializers.put(Integer.class, IntegerCodec.instance);
        this.derializers.put(Long.TYPE, LongCodec.instance);
        this.derializers.put(Long.class, LongCodec.instance);
        this.derializers.put(BigInteger.class, BigIntegerCodec.instance);
        this.derializers.put(BigDecimal.class, BigDecimalCodec.instance);
        this.derializers.put(Float.TYPE, FloatCodec.instance);
        this.derializers.put(Float.class, FloatCodec.instance);
        this.derializers.put(Double.TYPE, NumberDeserializer.instance);
        this.derializers.put(Double.class, NumberDeserializer.instance);
        this.derializers.put(Boolean.TYPE, BooleanCodec.instance);
        this.derializers.put(Boolean.class, BooleanCodec.instance);
        this.derializers.put(Class.class, MiscCodec.instance);
        this.derializers.put(char[].class, new CharArrayCodec());
        this.derializers.put(AtomicBoolean.class, BooleanCodec.instance);
        this.derializers.put(AtomicInteger.class, IntegerCodec.instance);
        this.derializers.put(AtomicLong.class, LongCodec.instance);
        this.derializers.put(AtomicReference.class, ReferenceCodec.instance);
        this.derializers.put(WeakReference.class, ReferenceCodec.instance);
        this.derializers.put(SoftReference.class, ReferenceCodec.instance);
        this.derializers.put(UUID.class, MiscCodec.instance);
        this.derializers.put(TimeZone.class, MiscCodec.instance);
        this.derializers.put(Locale.class, MiscCodec.instance);
        this.derializers.put(Currency.class, MiscCodec.instance);
        this.derializers.put(InetAddress.class, MiscCodec.instance);
        this.derializers.put(Inet4Address.class, MiscCodec.instance);
        this.derializers.put(Inet6Address.class, MiscCodec.instance);
        this.derializers.put(InetSocketAddress.class, MiscCodec.instance);
        this.derializers.put(File.class, MiscCodec.instance);
        this.derializers.put(URI.class, MiscCodec.instance);
        this.derializers.put(URL.class, MiscCodec.instance);
        this.derializers.put(Pattern.class, MiscCodec.instance);
        this.derializers.put(Charset.class, MiscCodec.instance);
        this.derializers.put(JSONPath.class, MiscCodec.instance);
        this.derializers.put(Number.class, NumberDeserializer.instance);
        this.derializers.put(AtomicIntegerArray.class, AtomicCodec.instance);
        this.derializers.put(AtomicLongArray.class, AtomicCodec.instance);
        this.derializers.put(StackTraceElement.class, StackTraceElementDeserializer.instance);
        this.derializers.put(Serializable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Cloneable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Comparable.class, JavaObjectDeserializer.instance);
        this.derializers.put(Closeable.class, JavaObjectDeserializer.instance);
        addDeny("java.lang.Thread");
        configFromPropety(System.getProperties());
    }

    public void configFromPropety(Properties properties) {
        String property = properties.getProperty(DENY_PROPERTY);
        if (property != null && property.length() > 0) {
            String[] items = property.split(",");
            for (String item : items) {
                addDeny(item);
            }
        }
    }

    public boolean isAsmEnable() {
        return this.asmEnable;
    }

    public void setAsmEnable(boolean asmEnable) {
        this.asmEnable = asmEnable;
    }

    public IdentityHashMap<Type, ObjectDeserializer> getDerializers() {
        return this.derializers;
    }

    public ObjectDeserializer getDeserializer(Type type) {
        ObjectDeserializer derializer = (ObjectDeserializer) this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }
        if (type instanceof Class) {
            return getDeserializer((Class) type, type);
        }
        if (!(type instanceof ParameterizedType)) {
            return JavaObjectDeserializer.instance;
        }
        Type rawType = ((ParameterizedType) type).getRawType();
        if (rawType instanceof Class) {
            return getDeserializer((Class) rawType, type);
        }
        return getDeserializer(rawType);
    }

    public ObjectDeserializer getDeserializer(Class<?> clazz, Type type) {
        ObjectDeserializer derializer = (ObjectDeserializer) this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }
        if (type == null) {
            type = clazz;
        }
        derializer = (ObjectDeserializer) this.derializers.get(type);
        if (derializer != null) {
            return derializer;
        }
        JSONType annotation = (JSONType) clazz.getAnnotation(JSONType.class);
        if (annotation != null) {
            Class<?> mappingTo = annotation.mappingTo();
            if (mappingTo != Void.class) {
                return getDeserializer(mappingTo, mappingTo);
            }
        }
        if ((type instanceof WildcardType) || (type instanceof TypeVariable) || (type instanceof ParameterizedType)) {
            derializer = (ObjectDeserializer) this.derializers.get(clazz);
        }
        if (derializer != null) {
            return derializer;
        }
        String className = clazz.getName();
        for (String deny : this.denyList) {
            className = className.replace('$', '.');
            if (className.startsWith(deny)) {
                throw new JSONException("parser deny : " + className);
            }
        }
        if (className.startsWith("java.awt.") && AwtCodec.support(clazz) && !awtError) {
            try {
                this.derializers.put(Class.forName("java.awt.Point"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Font"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Rectangle"), AwtCodec.instance);
                this.derializers.put(Class.forName("java.awt.Color"), AwtCodec.instance);
            } catch (Throwable th) {
                awtError = true;
            }
            derializer = AwtCodec.instance;
        }
        if (!jdk8Error) {
            try {
                if (className.startsWith("java.time.")) {
                    this.derializers.put(Class.forName("java.time.LocalDateTime"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.LocalDate"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.LocalTime"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.ZonedDateTime"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.OffsetDateTime"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.OffsetTime"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.ZoneOffset"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.ZoneRegion"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.ZoneId"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.Period"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.Duration"), Jdk8DateCodec.instance);
                    this.derializers.put(Class.forName("java.time.Instant"), Jdk8DateCodec.instance);
                    derializer = (ObjectDeserializer) this.derializers.get(clazz);
                } else if (className.startsWith("java.util.Optional")) {
                    this.derializers.put(Class.forName("java.util.Optional"), OptionalCodec.instance);
                    this.derializers.put(Class.forName("java.util.OptionalDouble"), OptionalCodec.instance);
                    this.derializers.put(Class.forName("java.util.OptionalInt"), OptionalCodec.instance);
                    this.derializers.put(Class.forName("java.util.OptionalLong"), OptionalCodec.instance);
                    derializer = (ObjectDeserializer) this.derializers.get(clazz);
                }
            } catch (Throwable th2) {
                jdk8Error = true;
            }
        }
        if (className.equals("java.nio.file.Path")) {
            this.derializers.put(clazz, MiscCodec.instance);
        }
        try {
            for (AutowiredObjectDeserializer autowired : ServiceLoader.load(AutowiredObjectDeserializer.class, Thread.currentThread().getContextClassLoader())) {
                for (Type forType : autowired.getAutowiredFor()) {
                    this.derializers.put(forType, autowired);
                }
            }
        } catch (Exception e) {
        }
        if (derializer == null) {
            derializer = (ObjectDeserializer) this.derializers.get(type);
        }
        if (derializer != null) {
            return derializer;
        }
        if (clazz.isEnum()) {
            derializer = new EnumDeserializer(clazz);
        } else if (clazz.isArray()) {
            derializer = ObjectArrayCodec.instance;
        } else if (clazz == Set.class || clazz == HashSet.class || clazz == Collection.class || clazz == List.class || clazz == ArrayList.class) {
            derializer = CollectionCodec.instance;
        } else if (Collection.class.isAssignableFrom(clazz)) {
            derializer = CollectionCodec.instance;
        } else if (Map.class.isAssignableFrom(clazz)) {
            derializer = MapDeserializer.instance;
        } else if (Throwable.class.isAssignableFrom(clazz)) {
            derializer = new ThrowableDeserializer(this, clazz);
        } else {
            derializer = createJavaBeanDeserializer(clazz, type);
        }
        putDeserializer(type, derializer);
        return derializer;
    }

    public ObjectDeserializer createJavaBeanDeserializer(Class<?> clazz, Type type) {
        boolean asmEnable = this.asmEnable;
        if (asmEnable) {
            JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
            if (!(jsonType == null || jsonType.asm())) {
                asmEnable = false;
            }
            if (asmEnable) {
                Class<?> superClass = JavaBeanInfo.getBuilderClass(jsonType);
                if (superClass == null) {
                    superClass = clazz;
                }
                while (Modifier.isPublic(superClass.getModifiers())) {
                    superClass = superClass.getSuperclass();
                    if (superClass != Object.class) {
                        if (superClass == null) {
                            break;
                        }
                    }
                    break;
                }
                asmEnable = false;
            }
        }
        if (clazz.getTypeParameters().length != 0) {
            asmEnable = false;
        }
        if (asmEnable && this.asmFactory != null && this.asmFactory.classLoader.isExternalClass(clazz)) {
            asmEnable = false;
        }
        if (asmEnable) {
            asmEnable = ASMUtils.checkName(clazz.getName());
        }
        if (asmEnable) {
            if (clazz.isInterface()) {
                asmEnable = false;
            }
            JavaBeanInfo beanInfo = JavaBeanInfo.build(clazz, type);
            if (asmEnable && beanInfo.fields.length > 200) {
                asmEnable = false;
            }
            Constructor<?> defaultConstructor = beanInfo.defaultConstructor;
            if (asmEnable && defaultConstructor == null && !clazz.isInterface()) {
                asmEnable = false;
            }
            FieldInfo[] fieldInfoArr = beanInfo.fields;
            int length = fieldInfoArr.length;
            int i = 0;
            while (i < length) {
                FieldInfo fieldInfo = fieldInfoArr[i];
                if (!fieldInfo.getOnly) {
                    Type fieldClass = fieldInfo.fieldClass;
                    if (Modifier.isPublic(fieldClass.getModifiers())) {
                        if (!fieldClass.isMemberClass() || Modifier.isStatic(fieldClass.getModifiers())) {
                            if (fieldInfo.getMember() != null && !ASMUtils.checkName(fieldInfo.getMember().getName())) {
                                asmEnable = false;
                                break;
                            }
                            JSONField annotation = fieldInfo.getAnnotation();
                            if (annotation == null || ASMUtils.checkName(annotation.name())) {
                                if (fieldClass.isEnum() && !(getDeserializer(fieldClass) instanceof EnumDeserializer)) {
                                    asmEnable = false;
                                    break;
                                }
                                i++;
                            } else {
                                asmEnable = false;
                                break;
                            }
                        }
                        asmEnable = false;
                        break;
                    }
                    asmEnable = false;
                    break;
                }
                asmEnable = false;
                break;
            }
        }
        if (asmEnable && clazz.isMemberClass() && !Modifier.isStatic(clazz.getModifiers())) {
            asmEnable = false;
        }
        if (!asmEnable) {
            return new JavaBeanDeserializer(this, clazz, type);
        }
        try {
            return this.asmFactory.createJavaBeanDeserializer(this, clazz, type);
        } catch (NoSuchMethodException e) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (JSONException e2) {
            return new JavaBeanDeserializer(this, clazz, type);
        } catch (Exception e3) {
            throw new JSONException("create asm deserializer error, " + clazz.getName(), e3);
        }
    }

    public FieldDeserializer createFieldDeserializer(ParserConfig mapping, JavaBeanInfo beanInfo, FieldInfo fieldInfo) {
        Class<?> clazz = beanInfo.clazz;
        Class<?> fieldClass = fieldInfo.fieldClass;
        if (fieldClass == List.class || fieldClass == ArrayList.class) {
            return new ArrayListTypeFieldDeserializer(mapping, clazz, fieldInfo);
        }
        return new DefaultFieldDeserializer(mapping, clazz, fieldInfo);
    }

    public void putDeserializer(Type type, ObjectDeserializer deserializer) {
        this.derializers.put(type, deserializer);
    }

    public ObjectDeserializer getDeserializer(FieldInfo fieldInfo) {
        return getDeserializer(fieldInfo.fieldClass, fieldInfo.fieldType);
    }

    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive() || clazz == Boolean.class || clazz == Character.class || clazz == Byte.class || clazz == Short.class || clazz == Integer.class || clazz == Long.class || clazz == Float.class || clazz == Double.class || clazz == BigInteger.class || clazz == BigDecimal.class || clazz == String.class || clazz == java.util.Date.class || clazz == Date.class || clazz == Time.class || clazz == Timestamp.class || clazz.isEnum();
    }

    public static Field getField(Class<?> clazz, String fieldName) {
        Field field = getField0(clazz, fieldName);
        if (field == null) {
            field = getField0(clazz, "_" + fieldName);
        }
        if (field == null) {
            return getField0(clazz, "m_" + fieldName);
        }
        return field;
    }

    private static Field getField0(Class<?> clazz, String fieldName) {
        for (Field item : clazz.getDeclaredFields()) {
            if (fieldName.equals(item.getName())) {
                return item;
            }
        }
        if (clazz.getSuperclass() == null || clazz.getSuperclass() == Object.class) {
            return null;
        }
        return getField(clazz.getSuperclass(), fieldName);
    }

    public ClassLoader getDefaultClassLoader() {
        return this.defaultClassLoader;
    }

    public void setDefaultClassLoader(ClassLoader defaultClassLoader) {
        this.defaultClassLoader = defaultClassLoader;
    }

    public void addDeny(String name) {
        if (name != null && name.length() != 0) {
            String[] denyList = new String[(this.denyList.length + 1)];
            System.arraycopy(this.denyList, 0, denyList, 0, this.denyList.length);
            denyList[denyList.length - 1] = name;
            this.denyList = denyList;
        }
    }
}
