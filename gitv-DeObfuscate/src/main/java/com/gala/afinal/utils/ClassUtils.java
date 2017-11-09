package com.gala.afinal.utils;

import com.gala.afinal.annotation.sqlite.Id;
import com.gala.afinal.annotation.sqlite.Table;
import com.gala.afinal.exception.DbException;
import com.gala.imageprovider.p000private.C0127c;
import com.gala.imageprovider.p000private.C0130j;
import com.gala.imageprovider.p000private.C0133h;
import com.gala.imageprovider.p000private.C0134i;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class ClassUtils {
    public static String getTableName(Class<?> clazz) {
        Table table = (Table) clazz.getAnnotation(Table.class);
        if (table == null || table.name().trim().length() == 0) {
            return clazz.getName().replace('.', '_');
        }
        return table.name();
    }

    public static Object getPrimaryKeyValue(Object entity) {
        return FieldUtils.getFieldValue(entity, getPrimaryKeyField(entity.getClass()));
    }

    public static String getPrimaryKeyColumn(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null) {
            Id id = null;
            for (Field field : declaredFields) {
                id = (Id) field.getAnnotation(Id.class);
                if (id != null) {
                    break;
                }
            }
            Field field2 = null;
            if (id != null) {
                String column = id.column();
                if (column == null || column.trim().length() == 0) {
                    return field2.getName();
                }
                return column;
            }
            for (Field name : declaredFields) {
                if ("_id".equals(name.getName())) {
                    return "_id";
                }
            }
            for (Field name2 : declaredFields) {
                if ("id".equals(name2.getName())) {
                    return "id";
                }
            }
            return null;
        }
        throw new RuntimeException("this model[" + clazz + "] has no field");
    }

    public static Field getPrimaryKeyField(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        if (declaredFields != null) {
            for (Field field : declaredFields) {
                if (field.getAnnotation(Id.class) != null) {
                    break;
                }
            }
            Field field2 = null;
            if (field2 == null) {
                for (Field field3 : declaredFields) {
                    if ("_id".equals(field3.getName())) {
                        break;
                    }
                }
            }
            Field field32 = field2;
            if (field32 != null) {
                return field32;
            }
            for (Field field4 : declaredFields) {
                if ("id".equals(field4.getName())) {
                    return field4;
                }
            }
            return field32;
        }
        throw new RuntimeException("this model[" + clazz + "] has no field");
    }

    public static String getPrimaryKeyFieldName(Class<?> clazz) {
        Field primaryKeyField = getPrimaryKeyField(clazz);
        return primaryKeyField == null ? null : primaryKeyField.getName();
    }

    public static List<C0130j> getPropertyList(Class<?> clazz) {
        List<C0130j> arrayList = new ArrayList();
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            for (Field field : declaredFields) {
                if (!(FieldUtils.isTransient(field) || !FieldUtils.isBaseDateType(field) || field.getName().equals(primaryKeyFieldName))) {
                    C0130j c0130j = new C0130j();
                    c0130j.m327a(FieldUtils.getColumnByField(field));
                    field.getName();
                    c0130j.m332b(field.getType());
                    c0130j.m333b(FieldUtils.getPropertyDefaultValue(field));
                    c0130j.m334b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                    c0130j.m329a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                    c0130j.m328a(field);
                    arrayList.add(c0130j);
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<C0133h> getManyToOneList(Class<?> clazz) {
        List<C0133h> arrayList = new ArrayList();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!FieldUtils.isTransient(field) && FieldUtils.isManyToOne(field)) {
                    C0133h c0133h = new C0133h();
                    if (field.getType() == C0127c.class) {
                        Class cls = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];
                        if (cls != null) {
                            c0133h.m338a(cls);
                        }
                    } else {
                        c0133h.m338a(field.getType());
                    }
                    c0133h.m327a(FieldUtils.getColumnByField(field));
                    field.getName();
                    c0133h.m332b(field.getType());
                    c0133h.m334b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                    c0133h.m329a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                    arrayList.add(c0133h);
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<C0134i> getOneToManyList(Class<?> clazz) {
        List<C0134i> arrayList = new ArrayList();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!FieldUtils.isTransient(field) && FieldUtils.isOneToMany(field)) {
                    C0134i c0134i = new C0134i();
                    c0134i.m327a(FieldUtils.getColumnByField(field));
                    field.getName();
                    if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        Class cls;
                        if (parameterizedType.getActualTypeArguments().length == 1) {
                            cls = (Class) parameterizedType.getActualTypeArguments()[0];
                            if (cls != null) {
                                c0134i.m340a(cls);
                            }
                        } else {
                            cls = (Class) parameterizedType.getActualTypeArguments()[1];
                            if (cls != null) {
                                c0134i.m340a(cls);
                            }
                        }
                        c0134i.m332b(field.getType());
                        c0134i.m334b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                        c0134i.m329a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                        arrayList.add(c0134i);
                    } else {
                        throw new DbException("getOneToManyList Exception:" + field.getName() + "'s type is null");
                    }
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
