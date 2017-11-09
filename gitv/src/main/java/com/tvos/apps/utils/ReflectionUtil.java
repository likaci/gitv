package com.tvos.apps.utils;

import java.lang.reflect.Field;

public class ReflectionUtil {
    public static Object getProperty(Object obj, String fieldname) throws Exception {
        return obj.getClass().getField(fieldname).get(obj);
    }

    public static Object getStaticProperty(String className, String fieldName) throws Exception {
        Class cls = Class.forName(className);
        return cls.getField(fieldName).get(cls);
    }

    public static Object getPrivatePropertyValue(Object obj, String propertyName) throws Exception {
        Field field = obj.getClass().getDeclaredField(propertyName);
        field.setAccessible(true);
        return field.get(obj);
    }

    public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {
        Class cls = owner.getClass();
        Class[] argclass = new Class[args.length];
        int j = argclass.length;
        for (int i = 0; i < j; i++) {
            argclass[i] = args[i].getClass();
        }
        return cls.getMethod(methodName, argclass).invoke(owner, args);
    }

    public static Object invokeStaticMethod(String className, String methodName, Object[] args) throws Exception {
        Class cls = Class.forName(className);
        Class[] argclass = new Class[args.length];
        int j = argclass.length;
        for (int i = 0; i < j; i++) {
            argclass[i] = args[i].getClass();
        }
        return cls.getMethod(methodName, argclass).invoke(null, args);
    }

    public static Object newInstance(String className, Object[] args) throws Exception {
        Class clss = Class.forName(className);
        Class[] argclass = new Class[args.length];
        int j = argclass.length;
        for (int i = 0; i < j; i++) {
            argclass[i] = args[i].getClass();
        }
        return clss.getConstructor(argclass).newInstance(new Object[0]);
    }
}
