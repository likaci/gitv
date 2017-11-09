package com.alibaba.fastjson.util;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.annotation.JSONCreator;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONPOJOBuilder;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class JavaBeanInfo {
    public final Method buildMethod;
    public final Class<?> builderClass;
    public final Class<?> clazz;
    public final Constructor<?> creatorConstructor;
    public final Constructor<?> defaultConstructor;
    public final int defaultConstructorParameterSize;
    public final Method factoryMethod;
    public final FieldInfo[] fields;
    public final JSONType jsonType;
    public final int parserFeatures;
    public final FieldInfo[] sortedFields;
    public final String typeName;

    public JavaBeanInfo(Class<?> clazz, Class<?> builderClass, Constructor<?> defaultConstructor, Constructor<?> creatorConstructor, Method factoryMethod, Method buildMethod, JSONType jsonType, List<FieldInfo> fieldList) {
        int i = 0;
        this.clazz = clazz;
        this.builderClass = builderClass;
        this.defaultConstructor = defaultConstructor;
        this.creatorConstructor = creatorConstructor;
        this.factoryMethod = factoryMethod;
        this.parserFeatures = TypeUtils.getParserFeatures(clazz);
        this.buildMethod = buildMethod;
        this.jsonType = jsonType;
        if (jsonType != null) {
            String typeName = jsonType.typeName();
            if (typeName.length() != 0) {
                this.typeName = typeName;
            } else {
                this.typeName = clazz.getName();
            }
        } else {
            this.typeName = clazz.getName();
        }
        this.fields = new FieldInfo[fieldList.size()];
        fieldList.toArray(this.fields);
        FieldInfo[] sortedFields = new FieldInfo[this.fields.length];
        System.arraycopy(this.fields, 0, sortedFields, 0, this.fields.length);
        Arrays.sort(sortedFields);
        if (Arrays.equals(this.fields, sortedFields)) {
            sortedFields = this.fields;
        }
        this.sortedFields = sortedFields;
        if (defaultConstructor != null) {
            i = defaultConstructor.getParameterTypes().length;
        }
        this.defaultConstructorParameterSize = i;
    }

    private static FieldInfo getField(List<FieldInfo> fieldList, String propertyName) {
        for (FieldInfo item : fieldList) {
            if (item.name.equals(propertyName)) {
                return item;
            }
        }
        return null;
    }

    static boolean add(List<FieldInfo> fieldList, FieldInfo field) {
        int i = fieldList.size() - 1;
        while (i >= 0) {
            FieldInfo item = (FieldInfo) fieldList.get(i);
            if (!item.name.equals(field.name) || (item.getOnly && !field.getOnly)) {
                i--;
            } else {
                if (item.fieldClass.isAssignableFrom(field.fieldClass)) {
                    fieldList.remove(i);
                } else if (item.compareTo(field) >= 0) {
                    return false;
                } else {
                    fieldList.remove(i);
                }
                fieldList.add(field);
                return true;
            }
        }
        fieldList.add(field);
        return true;
    }

    public static JavaBeanInfo build(Class<?> clazz, Type type) {
        Class cls;
        JSONType jsonType = (JSONType) clazz.getAnnotation(JSONType.class);
        Class<?> builderClass = getBuilderClass(jsonType);
        Field[] declaredFields = clazz.getDeclaredFields();
        Method[] methods = clazz.getMethods();
        if (builderClass == null) {
            cls = clazz;
        } else {
            Class<?> cls2 = builderClass;
        }
        Constructor<?> defaultConstructor = getDefaultConstructor(cls);
        Method buildMethod = null;
        List<FieldInfo> fieldList = new ArrayList();
        Class<?>[] types;
        JSONField fieldAnnotation;
        if (defaultConstructor != null || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
            int ordinal;
            int serialzeFeatures;
            JSONField annotation;
            String methodName;
            Field field;
            String propertyName;
            if (defaultConstructor != null) {
                TypeUtils.setAccessible(defaultConstructor);
            }
            if (builderClass != null) {
                String withPrefix = null;
                JSONPOJOBuilder builderAnno = (JSONPOJOBuilder) builderClass.getAnnotation(JSONPOJOBuilder.class);
                if (builderAnno != null) {
                    withPrefix = builderAnno.withPrefix();
                }
                if (withPrefix == null || withPrefix.length() == 0) {
                    withPrefix = "with";
                }
                for (Method method : builderClass.getMethods()) {
                    if (!Modifier.isStatic(method.getModifiers()) && method.getReturnType().equals(builderClass)) {
                        ordinal = 0;
                        serialzeFeatures = 0;
                        annotation = (JSONField) method.getAnnotation(JSONField.class);
                        if (annotation == null) {
                            annotation = TypeUtils.getSupperMethodAnnotation(clazz, method);
                        }
                        if (annotation != null) {
                            if (annotation.deserialize()) {
                                ordinal = annotation.ordinal();
                                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                                if (annotation.name().length() != 0) {
                                    add(fieldList, new FieldInfo(annotation.name(), method, null, clazz, type, ordinal, serialzeFeatures, annotation, null, null));
                                }
                            }
                        }
                        methodName = method.getName();
                        if (methodName.startsWith(withPrefix) && methodName.length() > withPrefix.length()) {
                            char c0 = methodName.charAt(withPrefix.length());
                            if (Character.isUpperCase(c0)) {
                                StringBuilder stringBuilder = new StringBuilder(methodName.substring(withPrefix.length()));
                                stringBuilder.setCharAt(0, Character.toLowerCase(c0));
                                add(fieldList, new FieldInfo(stringBuilder.toString(), method, null, clazz, type, ordinal, serialzeFeatures, annotation, null, null));
                            }
                        }
                    }
                }
                if (builderClass != null) {
                    JSONPOJOBuilder builderAnnotation = (JSONPOJOBuilder) builderClass.getAnnotation(JSONPOJOBuilder.class);
                    String buildMethodName = null;
                    if (builderAnnotation != null) {
                        buildMethodName = builderAnnotation.buildMethod();
                    }
                    if (buildMethodName == null || buildMethodName.length() == 0) {
                        buildMethodName = "build";
                    }
                    try {
                        buildMethod = builderClass.getMethod(buildMethodName, new Class[0]);
                    } catch (NoSuchMethodException e) {
                    } catch (SecurityException e2) {
                    }
                    if (buildMethod == null) {
                        try {
                            buildMethod = builderClass.getMethod("create", new Class[0]);
                        } catch (NoSuchMethodException e3) {
                        } catch (SecurityException e4) {
                        }
                    }
                    if (buildMethod == null) {
                        throw new JSONException("buildMethod not found.");
                    }
                    TypeUtils.setAccessible(buildMethod);
                }
            }
            for (Method method2 : methods) {
                ordinal = 0;
                serialzeFeatures = 0;
                methodName = method2.getName();
                if (methodName.length() >= 4 && !Modifier.isStatic(method2.getModifiers()) && (method2.getReturnType().equals(Void.TYPE) || method2.getReturnType().equals(clazz))) {
                    types = method2.getParameterTypes();
                    if (types.length == 1) {
                        annotation = (JSONField) method2.getAnnotation(JSONField.class);
                        if (annotation == null) {
                            annotation = TypeUtils.getSupperMethodAnnotation(clazz, method2);
                        }
                        if (annotation != null) {
                            if (annotation.deserialize()) {
                                ordinal = annotation.ordinal();
                                serialzeFeatures = SerializerFeature.of(annotation.serialzeFeatures());
                                if (annotation.name().length() != 0) {
                                    add(fieldList, new FieldInfo(annotation.name(), method2, null, clazz, type, ordinal, serialzeFeatures, annotation, null, null));
                                }
                            }
                        }
                        if (methodName.startsWith("set")) {
                            String decapitalize;
                            char c3 = methodName.charAt(3);
                            if (Character.isUpperCase(c3) || c3 > 'Ȁ') {
                                if (TypeUtils.compatibleWithJavaBean) {
                                    decapitalize = TypeUtils.decapitalize(methodName.substring(3));
                                } else {
                                    decapitalize = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                                }
                            } else if (c3 == '_') {
                                decapitalize = methodName.substring(4);
                            } else if (c3 == 'f') {
                                decapitalize = methodName.substring(3);
                            } else if (methodName.length() >= 5 && Character.isUpperCase(methodName.charAt(4))) {
                                decapitalize = TypeUtils.decapitalize(methodName.substring(3));
                            }
                            field = TypeUtils.getField(clazz, decapitalize, declaredFields);
                            if (field == null && types[0] == Boolean.TYPE) {
                                field = TypeUtils.getField(clazz, "is" + Character.toUpperCase(decapitalize.charAt(0)) + decapitalize.substring(1), declaredFields);
                            }
                            fieldAnnotation = null;
                            if (field != null) {
                                fieldAnnotation = (JSONField) field.getAnnotation(JSONField.class);
                                if (fieldAnnotation != null) {
                                    ordinal = fieldAnnotation.ordinal();
                                    serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                                    if (fieldAnnotation.name().length() != 0) {
                                        add(fieldList, new FieldInfo(fieldAnnotation.name(), method2, field, clazz, type, ordinal, serialzeFeatures, annotation, fieldAnnotation, null));
                                    }
                                }
                            }
                            add(fieldList, new FieldInfo(decapitalize, method2, field, clazz, type, ordinal, serialzeFeatures, annotation, fieldAnnotation, null));
                        }
                    }
                }
            }
            for (Field field2 : clazz.getFields()) {
                if (!Modifier.isStatic(field2.getModifiers())) {
                    boolean contains = false;
                    for (FieldInfo item : fieldList) {
                        if (item.name.equals(field2.getName())) {
                            contains = true;
                            break;
                        }
                    }
                    if (!contains) {
                        ordinal = 0;
                        serialzeFeatures = 0;
                        propertyName = field2.getName();
                        fieldAnnotation = (JSONField) field2.getAnnotation(JSONField.class);
                        if (fieldAnnotation != null) {
                            ordinal = fieldAnnotation.ordinal();
                            serialzeFeatures = SerializerFeature.of(fieldAnnotation.serialzeFeatures());
                            if (fieldAnnotation.name().length() != 0) {
                                propertyName = fieldAnnotation.name();
                            }
                        }
                        add(fieldList, new FieldInfo(propertyName, null, field2, clazz, type, ordinal, serialzeFeatures, null, fieldAnnotation, null));
                    }
                }
            }
            for (Method method22 : clazz.getMethods()) {
                methodName = method22.getName();
                if (methodName.length() >= 4 && !Modifier.isStatic(method22.getModifiers())) {
                    if (methodName.startsWith("get") && Character.isUpperCase(methodName.charAt(3)) && method22.getParameterTypes().length == 0 && (Collection.class.isAssignableFrom(method22.getReturnType()) || Map.class.isAssignableFrom(method22.getReturnType()) || AtomicBoolean.class == method22.getReturnType() || AtomicInteger.class == method22.getReturnType() || AtomicLong.class == method22.getReturnType())) {
                        annotation = (JSONField) method22.getAnnotation(JSONField.class);
                        if (annotation == null || annotation.name().length() <= 0) {
                            propertyName = Character.toLowerCase(methodName.charAt(3)) + methodName.substring(4);
                        } else {
                            propertyName = annotation.name();
                        }
                        if (getField(fieldList, propertyName) == null) {
                            add(fieldList, new FieldInfo(propertyName, method22, null, clazz, type, 0, 0, annotation, null, null));
                        }
                    }
                }
            }
            return new JavaBeanInfo(clazz, builderClass, defaultConstructor, null, null, buildMethod, jsonType, fieldList);
        }
        Constructor<?> creatorConstructor = getCreatorConstructor(clazz);
        int i;
        if (creatorConstructor != null) {
            TypeUtils.setAccessible(creatorConstructor);
            types = creatorConstructor.getParameterTypes();
            if (types.length > 0) {
                Annotation[][] paramAnnotationArrays = creatorConstructor.getParameterAnnotations();
                for (i = 0; i < types.length; i++) {
                    fieldAnnotation = null;
                    for (Annotation paramAnnotation : paramAnnotationArrays[i]) {
                        if (paramAnnotation instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation;
                            break;
                        }
                    }
                    if (fieldAnnotation == null) {
                        throw new JSONException("illegal json creator");
                    }
                    add(fieldList, new FieldInfo(fieldAnnotation.name(), clazz, types[i], creatorConstructor.getGenericParameterTypes()[i], TypeUtils.getField(clazz, fieldAnnotation.name(), declaredFields), fieldAnnotation.ordinal(), SerializerFeature.of(fieldAnnotation.serialzeFeatures())));
                }
            }
            return new JavaBeanInfo(clazz, builderClass, null, creatorConstructor, null, null, jsonType, fieldList);
        }
        Method factoryMethod = getFactoryMethod(clazz, methods);
        if (factoryMethod != null) {
            TypeUtils.setAccessible(factoryMethod);
            types = factoryMethod.getParameterTypes();
            if (types.length > 0) {
                paramAnnotationArrays = factoryMethod.getParameterAnnotations();
                for (i = 0; i < types.length; i++) {
                    fieldAnnotation = null;
                    for (Annotation paramAnnotation2 : paramAnnotationArrays[i]) {
                        if (paramAnnotation2 instanceof JSONField) {
                            fieldAnnotation = (JSONField) paramAnnotation2;
                            break;
                        }
                    }
                    if (fieldAnnotation == null) {
                        throw new JSONException("illegal json creator");
                    }
                    add(fieldList, new FieldInfo(fieldAnnotation.name(), clazz, types[i], factoryMethod.getGenericParameterTypes()[i], TypeUtils.getField(clazz, fieldAnnotation.name(), declaredFields), fieldAnnotation.ordinal(), SerializerFeature.of(fieldAnnotation.serialzeFeatures())));
                }
            }
            return new JavaBeanInfo(clazz, builderClass, null, null, factoryMethod, null, jsonType, fieldList);
        }
        throw new JSONException("default constructor not found. " + clazz);
    }

    static Constructor<?> getDefaultConstructor(Class<?> clazz) {
        if (Modifier.isAbstract(clazz.getModifiers())) {
            return null;
        }
        Constructor<?> defaultConstructor = null;
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            if (constructor.getParameterTypes().length == 0) {
                defaultConstructor = constructor;
                break;
            }
        }
        if (defaultConstructor != null || !clazz.isMemberClass() || Modifier.isStatic(clazz.getModifiers())) {
            return defaultConstructor;
        }
        for (Constructor<?> constructor2 : constructors) {
            Class<?>[] types = constructor2.getParameterTypes();
            if (types.length == 1 && types[0].equals(clazz.getDeclaringClass())) {
                return constructor2;
            }
        }
        return defaultConstructor;
    }

    public static Constructor<?> getCreatorConstructor(Class<?> clazz) {
        Constructor<?> creatorConstructor = null;
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (((JSONCreator) constructor.getAnnotation(JSONCreator.class)) != null) {
                if (creatorConstructor != null) {
                    throw new JSONException("multi-JSONCreator");
                }
                creatorConstructor = constructor;
            }
        }
        return creatorConstructor;
    }

    private static Method getFactoryMethod(Class<?> clazz, Method[] methods) {
        Method factoryMethod = null;
        for (Method method : methods) {
            if (Modifier.isStatic(method.getModifiers()) && clazz.isAssignableFrom(method.getReturnType()) && ((JSONCreator) method.getAnnotation(JSONCreator.class)) != null) {
                if (factoryMethod != null) {
                    throw new JSONException("multi-JSONCreator");
                }
                factoryMethod = method;
            }
        }
        return factoryMethod;
    }

    public static Class<?> getBuilderClass(JSONType type) {
        if (type == null) {
            return null;
        }
        Class<?> builderClass = type.builder();
        if (builderClass == Void.class) {
            return null;
        }
        return builderClass;
    }
}
