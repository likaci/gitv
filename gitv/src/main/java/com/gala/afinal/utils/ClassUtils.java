package com.gala.afinal.utils;

import com.gala.afinal.annotation.sqlite.Id;
import com.gala.afinal.annotation.sqlite.Table;
import com.gala.afinal.exception.DbException;
import com.gala.imageprovider.private.c;
import com.gala.imageprovider.private.h;
import com.gala.imageprovider.private.i;
import com.gala.imageprovider.private.j;
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

    public static List<j> getPropertyList(Class<?> clazz) {
        List<j> arrayList = new ArrayList();
        try {
            Field[] declaredFields = clazz.getDeclaredFields();
            String primaryKeyFieldName = getPrimaryKeyFieldName(clazz);
            for (Field field : declaredFields) {
                if (!(FieldUtils.isTransient(field) || !FieldUtils.isBaseDateType(field) || field.getName().equals(primaryKeyFieldName))) {
                    j jVar = new j();
                    jVar.a(FieldUtils.getColumnByField(field));
                    field.getName();
                    jVar.b(field.getType());
                    jVar.b(FieldUtils.getPropertyDefaultValue(field));
                    jVar.b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                    jVar.a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                    jVar.a(field);
                    arrayList.add(jVar);
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<h> getManyToOneList(Class<?> clazz) {
        List<h> arrayList = new ArrayList();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!FieldUtils.isTransient(field) && FieldUtils.isManyToOne(field)) {
                    h hVar = new h();
                    if (field.getType() == c.class) {
                        Class cls = (Class) ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[1];
                        if (cls != null) {
                            hVar.a(cls);
                        }
                    } else {
                        hVar.a(field.getType());
                    }
                    hVar.a(FieldUtils.getColumnByField(field));
                    field.getName();
                    hVar.b(field.getType());
                    hVar.b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                    hVar.a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                    arrayList.add(hVar);
                }
            }
            return arrayList;
        } catch (Throwable e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public static List<i> getOneToManyList(Class<?> clazz) {
        List<i> arrayList = new ArrayList();
        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (!FieldUtils.isTransient(field) && FieldUtils.isOneToMany(field)) {
                    i iVar = new i();
                    iVar.a(FieldUtils.getColumnByField(field));
                    field.getName();
                    if (field.getGenericType() instanceof ParameterizedType) {
                        ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                        Class cls;
                        if (parameterizedType.getActualTypeArguments().length == 1) {
                            cls = (Class) parameterizedType.getActualTypeArguments()[0];
                            if (cls != null) {
                                iVar.a(cls);
                            }
                        } else {
                            cls = (Class) parameterizedType.getActualTypeArguments()[1];
                            if (cls != null) {
                                iVar.a(cls);
                            }
                        }
                        iVar.b(field.getType());
                        iVar.b(FieldUtils.getFieldSetMethod((Class) clazz, field));
                        iVar.a(FieldUtils.getFieldGetMethod((Class) clazz, field));
                        arrayList.add(iVar);
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
