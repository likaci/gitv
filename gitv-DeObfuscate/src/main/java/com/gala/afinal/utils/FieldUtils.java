package com.gala.afinal.utils;

import com.gala.afinal.annotation.sqlite.Id;
import com.gala.afinal.annotation.sqlite.ManyToOne;
import com.gala.afinal.annotation.sqlite.OneToMany;
import com.gala.afinal.annotation.sqlite.Property;
import com.gala.afinal.annotation.sqlite.Transient;
import com.tvos.apps.utils.DateUtil;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FieldUtils {
    public static final SimpleDateFormat SDF = new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H);

    public static Method getFieldGetMethod(Class<?> clazz, Field f) {
        String name = f.getName();
        Method method = null;
        if (f.getType() == Boolean.TYPE) {
            method = getBooleanFieldGetMethod(clazz, name);
        }
        if (method == null) {
            return getFieldGetMethod((Class) clazz, name);
        }
        return method;
    }

    public static Method getBooleanFieldGetMethod(Class<?> clazz, String fieldName) {
        String str = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        if (!isISStart(fieldName)) {
            fieldName = str;
        }
        try {
            return clazz.getDeclaredMethod(fieldName, new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getBooleanFieldSetMethod(Class<?> clazz, Field f) {
        String name = f.getName();
        String str = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
        if (isISStart(f.getName())) {
            str = "set" + name.substring(2, 3).toUpperCase() + name.substring(3);
        }
        try {
            return clazz.getDeclaredMethod(str, new Class[]{f.getType()});
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean isISStart(String fieldName) {
        if (fieldName == null || fieldName.trim().length() == 0 || !fieldName.startsWith("is") || Character.isLowerCase(fieldName.charAt(2))) {
            return false;
        }
        return true;
    }

    public static Method getFieldGetMethod(Class<?> clazz, String fieldName) {
        try {
            return clazz.getDeclaredMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), new Class[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getFieldSetMethod(Class<?> clazz, Field f) {
        String name = f.getName();
        try {
            return clazz.getDeclaredMethod("set" + name.substring(0, 1).toUpperCase() + name.substring(1), new Class[]{f.getType()});
        } catch (NoSuchMethodException e) {
            if (f.getType() == Boolean.TYPE) {
                return getBooleanFieldSetMethod(clazz, f);
            }
            return null;
        }
    }

    public static Method getFieldSetMethod(Class<?> clazz, String fieldName) {
        try {
            return getFieldSetMethod((Class) clazz, clazz.getDeclaredField(fieldName));
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Object getFieldValue(Object entity, Field field) {
        return invoke(entity, getFieldGetMethod(entity.getClass(), field));
    }

    public static Object getFieldValue(Object entity, String fieldName) {
        return invoke(entity, getFieldGetMethod(entity.getClass(), fieldName));
    }

    public static void setFieldValue(Object entity, Field field, Object value) {
        Date date = null;
        try {
            Method fieldSetMethod = getFieldSetMethod(entity.getClass(), field);
            if (fieldSetMethod != null) {
                fieldSetMethod.setAccessible(true);
                Class type = field.getType();
                if (type == String.class) {
                    fieldSetMethod.invoke(entity, new Object[]{value.toString()});
                } else if (type == Integer.TYPE || type == Integer.class) {
                    int intValue;
                    r1 = new Object[1];
                    if (value == null) {
                        Integer num = null;
                        intValue = num.intValue();
                    } else {
                        intValue = Integer.parseInt(value.toString());
                    }
                    r1[0] = Integer.valueOf(intValue);
                    fieldSetMethod.invoke(entity, r1);
                } else if (type == Float.TYPE || type == Float.class) {
                    float floatValue;
                    r1 = new Object[1];
                    if (value == null) {
                        Float f = null;
                        floatValue = f.floatValue();
                    } else {
                        floatValue = Float.parseFloat(value.toString());
                    }
                    r1[0] = Float.valueOf(floatValue);
                    fieldSetMethod.invoke(entity, r1);
                } else if (type == Long.TYPE || type == Long.class) {
                    long longValue;
                    Object[] objArr = new Object[1];
                    if (value == null) {
                        Long l = null;
                        longValue = l.longValue();
                    } else {
                        longValue = Long.parseLong(value.toString());
                    }
                    objArr[0] = Long.valueOf(longValue);
                    fieldSetMethod.invoke(entity, objArr);
                } else if (type == Date.class) {
                    r1 = new Object[1];
                    if (value != null) {
                        date = stringToDateTime(value.toString());
                    }
                    r1[0] = date;
                    fieldSetMethod.invoke(entity, r1);
                } else {
                    fieldSetMethod.invoke(entity, new Object[]{value});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Field getFieldByColumnName(Class<?> clazz, String columnName) {
        if (columnName == null) {
            return null;
        }
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields == null || declaredFields.length <= 0) {
            return null;
        }
        Field primaryKeyField;
        Field field;
        if (columnName.equals(ClassUtils.getPrimaryKeyColumn(clazz))) {
            primaryKeyField = ClassUtils.getPrimaryKeyField(clazz);
        } else {
            primaryKeyField = null;
        }
        if (primaryKeyField == null) {
            for (Field field2 : declaredFields) {
                Property property = (Property) field2.getAnnotation(Property.class);
                if (property != null && columnName.equals(property.column())) {
                    field = field2;
                    break;
                }
                ManyToOne manyToOne = (ManyToOne) field2.getAnnotation(ManyToOne.class);
                if (manyToOne != null && manyToOne.column().trim().length() != 0) {
                    field = field2;
                    break;
                }
            }
        }
        field = primaryKeyField;
        if (field == null) {
            return getFieldByName(clazz, columnName);
        }
        return field;
    }

    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        Field field = null;
        if (fieldName != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e2) {
                e2.printStackTrace();
            }
        }
        return field;
    }

    public static String getColumnByField(Field field) {
        Property property = (Property) field.getAnnotation(Property.class);
        if (property != null && property.column().trim().length() != 0) {
            return property.column();
        }
        ManyToOne manyToOne = (ManyToOne) field.getAnnotation(ManyToOne.class);
        if (manyToOne != null && manyToOne.column().trim().length() != 0) {
            return manyToOne.column();
        }
        OneToMany oneToMany = (OneToMany) field.getAnnotation(OneToMany.class);
        if (oneToMany != null && oneToMany.manyColumn() != null && oneToMany.manyColumn().trim().length() != 0) {
            return oneToMany.manyColumn();
        }
        Id id = (Id) field.getAnnotation(Id.class);
        if (id == null || id.column().trim().length() == 0) {
            return field.getName();
        }
        return id.column();
    }

    public static String getPropertyDefaultValue(Field field) {
        Property property = (Property) field.getAnnotation(Property.class);
        if (property == null || property.defaultValue().trim().length() == 0) {
            return null;
        }
        return property.defaultValue();
    }

    public static boolean isTransient(Field f) {
        return f.getAnnotation(Transient.class) != null;
    }

    private static Object invoke(Object obj, Method method) {
        Object obj2 = null;
        if (!(obj == null || method == null)) {
            try {
                obj2 = method.invoke(obj, new Object[0]);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            } catch (InvocationTargetException e3) {
                e3.printStackTrace();
            }
        }
        return obj2;
    }

    public static boolean isManyToOne(Field field) {
        return field.getAnnotation(ManyToOne.class) != null;
    }

    public static boolean isOneToMany(Field field) {
        return field.getAnnotation(OneToMany.class) != null;
    }

    public static boolean isManyToOneOrOneToMany(Field field) {
        return isManyToOne(field) || isOneToMany(field);
    }

    public static boolean isBaseDateType(Field field) {
        Class type = field.getType();
        return type.equals(String.class) || type.equals(Integer.class) || type.equals(Byte.class) || type.equals(Long.class) || type.equals(Double.class) || type.equals(Float.class) || type.equals(Character.class) || type.equals(Short.class) || type.equals(Boolean.class) || type.equals(Date.class) || type.equals(Date.class) || type.equals(java.sql.Date.class) || type.isPrimitive();
    }

    public static Date stringToDateTime(String strDate) {
        if (strDate != null) {
            try {
                return SDF.parse(strDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
