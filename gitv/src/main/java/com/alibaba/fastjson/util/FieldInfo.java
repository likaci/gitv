package com.alibaba.fastjson.util;

import com.alibaba.fastjson.annotation.JSONField;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public class FieldInfo implements Comparable<FieldInfo> {
    public final Class<?> declaringClass;
    public final Field field;
    public final boolean fieldAccess;
    private final JSONField fieldAnnotation;
    public final Class<?> fieldClass;
    public final boolean fieldTransient;
    public final Type fieldType;
    public final boolean getOnly;
    public final boolean isEnum;
    public final String label;
    public final Method method;
    private final JSONField methodAnnotation;
    public final String name;
    public final char[] name_chars;
    private int ordinal = 0;
    public final int serialzeFeatures;

    public FieldInfo(String name, Class<?> declaringClass, Class<?> fieldClass, Type fieldType, Field field, int ordinal, int serialzeFeatures) {
        this.name = name;
        this.declaringClass = declaringClass;
        this.fieldClass = fieldClass;
        this.fieldType = fieldType;
        this.method = null;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;
        this.isEnum = fieldClass.isEnum();
        if (field != null) {
            boolean z;
            int modifiers = field.getModifiers();
            if ((modifiers & 1) != 0 || this.method == null) {
                z = true;
            } else {
                z = false;
            }
            this.fieldAccess = z;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldTransient = false;
            this.fieldAccess = false;
        }
        this.name_chars = genFieldNameChars();
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        this.label = "";
        this.fieldAnnotation = null;
        this.methodAnnotation = null;
        this.getOnly = false;
    }

    public FieldInfo(String name, Method method, Field field, Class<?> clazz, Type type, int ordinal, int serialzeFeatures, JSONField fieldAnnotation, JSONField methodAnnotation, String label) {
        Class<?> fieldClass;
        Type fieldType;
        Type genericFieldType;
        if (field != null) {
            String fieldName = field.getName();
            if (fieldName.equals(name)) {
                name = fieldName;
            }
        }
        this.name = name;
        this.method = method;
        this.field = field;
        this.ordinal = ordinal;
        this.serialzeFeatures = serialzeFeatures;
        this.fieldAnnotation = fieldAnnotation;
        this.methodAnnotation = methodAnnotation;
        if (field != null) {
            int modifiers = field.getModifiers();
            boolean z = (modifiers & 1) != 0 || method == null;
            this.fieldAccess = z;
            this.fieldTransient = Modifier.isTransient(modifiers);
        } else {
            this.fieldAccess = false;
            this.fieldTransient = false;
        }
        if (label == null || label.length() <= 0) {
            this.label = "";
        } else {
            this.label = label;
        }
        this.name_chars = genFieldNameChars();
        if (method != null) {
            TypeUtils.setAccessible(method);
        }
        if (field != null) {
            TypeUtils.setAccessible(field);
        }
        boolean getOnly = false;
        if (method != null) {
            Class<?>[] types = method.getParameterTypes();
            if (types.length == 1) {
                fieldClass = types[0];
                fieldType = method.getGenericParameterTypes()[0];
            } else {
                fieldClass = method.getReturnType();
                fieldType = method.getGenericReturnType();
                getOnly = true;
            }
            this.declaringClass = method.getDeclaringClass();
        } else {
            fieldClass = field.getType();
            fieldType = field.getGenericType();
            this.declaringClass = field.getDeclaringClass();
            getOnly = Modifier.isFinal(field.getModifiers());
        }
        this.getOnly = getOnly;
        if (clazz != null && fieldClass == Object.class && (fieldType instanceof TypeVariable)) {
            genericFieldType = getInheritGenericType(clazz, (TypeVariable) fieldType);
            if (genericFieldType != null) {
                this.fieldClass = TypeUtils.getClass(genericFieldType);
                this.fieldType = genericFieldType;
                this.isEnum = fieldClass.isEnum();
                return;
            }
        }
        genericFieldType = fieldType;
        if (!(fieldType instanceof Class)) {
            genericFieldType = getFieldType(clazz, type, fieldType);
            if (genericFieldType != fieldType) {
                if (genericFieldType instanceof ParameterizedType) {
                    fieldClass = TypeUtils.getClass(genericFieldType);
                } else if (genericFieldType instanceof Class) {
                    fieldClass = TypeUtils.getClass(genericFieldType);
                }
            }
        }
        this.fieldType = genericFieldType;
        this.fieldClass = fieldClass;
        this.isEnum = fieldClass.isEnum();
    }

    protected char[] genFieldNameChars() {
        int nameLen = this.name.length();
        char[] name_chars = new char[(nameLen + 3)];
        this.name.getChars(0, this.name.length(), name_chars, 1);
        name_chars[0] = '\"';
        name_chars[nameLen + 1] = '\"';
        name_chars[nameLen + 2] = ':';
        return name_chars;
    }

    public <T extends Annotation> T getAnnation(Class<T> annotationClass) {
        if (annotationClass == JSONField.class) {
            return getAnnotation();
        }
        T annotatition = null;
        if (this.method != null) {
            annotatition = this.method.getAnnotation(annotationClass);
        }
        if (annotatition != null || this.field == null) {
            return annotatition;
        }
        return this.field.getAnnotation(annotationClass);
    }

    public static Type getFieldType(Class<?> clazz, Type type, Type fieldType) {
        if (clazz == null || type == null) {
            return fieldType;
        }
        if (fieldType instanceof GenericArrayType) {
            Type componentType = ((GenericArrayType) fieldType).getGenericComponentType();
            Type componentTypeX = getFieldType(clazz, type, componentType);
            if (componentType != componentTypeX) {
                return Array.newInstance(TypeUtils.getClass(componentTypeX), 0).getClass();
            }
            return fieldType;
        } else if (!TypeUtils.isGenericParamType(type)) {
            return fieldType;
        } else {
            TypeVariable<?> typeVar;
            TypeVariable<?>[] typeVariables;
            int i;
            if (fieldType instanceof TypeVariable) {
                ParameterizedType paramType = (ParameterizedType) TypeUtils.getGenericParamType(type);
                typeVar = (TypeVariable) fieldType;
                typeVariables = TypeUtils.getClass(paramType).getTypeParameters();
                for (i = 0; i < typeVariables.length; i++) {
                    if (typeVariables[i].getName().equals(typeVar.getName())) {
                        return paramType.getActualTypeArguments()[i];
                    }
                }
            }
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedFieldType = (ParameterizedType) fieldType;
                Type[] arguments = parameterizedFieldType.getActualTypeArguments();
                boolean changed = false;
                typeVariables = null;
                Type[] actualTypes = null;
                for (i = 0; i < arguments.length; i++) {
                    Type feildTypeArguement = arguments[i];
                    if (feildTypeArguement instanceof TypeVariable) {
                        typeVar = (TypeVariable) feildTypeArguement;
                        if (type instanceof ParameterizedType) {
                            if (typeVariables == null) {
                                typeVariables = clazz.getTypeParameters();
                            }
                            for (int j = 0; j < typeVariables.length; j++) {
                                if (typeVariables[j].getName().equals(typeVar.getName())) {
                                    if (actualTypes == null) {
                                        actualTypes = ((ParameterizedType) type).getActualTypeArguments();
                                    }
                                    arguments[i] = actualTypes[j];
                                    changed = true;
                                }
                            }
                        }
                    }
                }
                if (changed) {
                    return new ParameterizedTypeImpl(arguments, parameterizedFieldType.getOwnerType(), parameterizedFieldType.getRawType());
                }
            }
            return fieldType;
        }
    }

    public static Type getInheritGenericType(Class<?> clazz, TypeVariable<?> tv) {
        Type gd = tv.getGenericDeclaration();
        Type type;
        do {
            type = clazz.getGenericSuperclass();
            if (type == null) {
                return null;
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType ptype = (ParameterizedType) type;
                if (ptype.getRawType() == gd) {
                    TypeVariable<?>[] tvs = gd.getTypeParameters();
                    Type[] types = ptype.getActualTypeArguments();
                    for (int i = 0; i < tvs.length; i++) {
                        if (tvs[i] == tv) {
                            return types[i];
                        }
                    }
                    return null;
                }
            }
            clazz = TypeUtils.getClass(type);
        } while (type != null);
        return null;
    }

    public String toString() {
        return this.name;
    }

    public Member getMember() {
        if (this.method != null) {
            return this.method;
        }
        return this.field;
    }

    protected Class<?> getDeclaredClass() {
        if (this.method != null) {
            return this.method.getDeclaringClass();
        }
        if (this.field != null) {
            return this.field.getDeclaringClass();
        }
        return null;
    }

    public int compareTo(FieldInfo o) {
        if (this.ordinal < o.ordinal) {
            return -1;
        }
        if (this.ordinal > o.ordinal) {
            return 1;
        }
        int result = this.name.compareTo(o.name);
        if (result != 0) {
            return result;
        }
        boolean isSampeType;
        boolean oSameType;
        Class<?> thisDeclaringClass = getDeclaredClass();
        Class<?> otherDeclaringClass = o.getDeclaredClass();
        if (!(thisDeclaringClass == null || otherDeclaringClass == null || thisDeclaringClass == otherDeclaringClass)) {
            if (thisDeclaringClass.isAssignableFrom(otherDeclaringClass)) {
                return -1;
            }
            if (otherDeclaringClass.isAssignableFrom(thisDeclaringClass)) {
                return 1;
            }
        }
        if (this.field == null || this.field.getType() != this.fieldClass) {
            isSampeType = false;
        } else {
            isSampeType = true;
        }
        if (o.field == null || o.field.getType() != o.fieldClass) {
            oSameType = false;
        } else {
            oSameType = true;
        }
        if (isSampeType && !oSameType) {
            return 1;
        }
        if (oSameType && !isSampeType) {
            return -1;
        }
        if (o.fieldClass.isPrimitive() && !this.fieldClass.isPrimitive()) {
            return 1;
        }
        if (this.fieldClass.isPrimitive() && !o.fieldClass.isPrimitive()) {
            return -1;
        }
        if (o.fieldClass.getName().startsWith("java.") && !this.fieldClass.getName().startsWith("java.")) {
            return 1;
        }
        if (!this.fieldClass.getName().startsWith("java.") || o.fieldClass.getName().startsWith("java.")) {
            return this.fieldClass.getName().compareTo(o.fieldClass.getName());
        }
        return -1;
    }

    public JSONField getAnnotation() {
        if (this.fieldAnnotation != null) {
            return this.fieldAnnotation;
        }
        return this.methodAnnotation;
    }

    public String getFormat() {
        JSONField annotation = getAnnotation();
        if (annotation == null) {
            return null;
        }
        String format = annotation.format();
        if (format.trim().length() == 0) {
            return null;
        }
        return format;
    }

    public Object get(Object javaObject) throws IllegalAccessException, InvocationTargetException {
        if (this.method != null) {
            return this.method.invoke(javaObject, new Object[0]);
        }
        return this.field.get(javaObject);
    }

    public void set(Object javaObject, Object value) throws IllegalAccessException, InvocationTargetException {
        if (this.method != null) {
            this.method.invoke(javaObject, new Object[]{value});
            return;
        }
        this.field.set(javaObject, value);
    }

    public void setAccessible() throws SecurityException {
        if (this.method != null) {
            TypeUtils.setAccessible(this.method);
        } else {
            TypeUtils.setAccessible(this.field);
        }
    }
}
