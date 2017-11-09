package com.gala.imageprovider.p000private;

import com.gala.afinal.exception.DbException;
import com.gala.afinal.utils.ClassUtils;
import com.gala.afinal.utils.FieldUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public final class C0135k {
    private static final HashMap<String, C0135k> f565d = new HashMap();
    private C0131f f566a;
    private String f567a;
    public final HashMap<String, C0130j> f568a = new HashMap();
    private boolean f569a;
    public final HashMap<String, C0134i> f570b = new HashMap();
    public final HashMap<String, C0133h> f571c = new HashMap();

    private C0135k() {
    }

    public static C0135k m341a(Class<?> cls) {
        if (cls == null) {
            throw new DbException("table info get error,because the clazz is null");
        }
        C0135k c0135k = (C0135k) f565d.get(cls.getName());
        if (c0135k == null) {
            C0135k c0135k2 = new C0135k();
            c0135k2.f567a = ClassUtils.getTableName(cls);
            cls.getName();
            Field primaryKeyField = ClassUtils.getPrimaryKeyField(cls);
            if (primaryKeyField != null) {
                C0131f c0131f = new C0131f();
                c0131f.m327a(FieldUtils.getColumnByField(primaryKeyField));
                primaryKeyField.getName();
                c0131f.m334b(FieldUtils.getFieldSetMethod((Class) cls, primaryKeyField));
                c0131f.m329a(FieldUtils.getFieldGetMethod((Class) cls, primaryKeyField));
                c0131f.m332b(primaryKeyField.getType());
                c0135k2.f566a = c0131f;
                List<C0130j> propertyList = ClassUtils.getPropertyList(cls);
                if (propertyList != null) {
                    for (C0130j c0130j : propertyList) {
                        if (c0130j != null) {
                            c0135k2.f568a.put(c0130j.mo652a(), c0130j);
                        }
                    }
                }
                List<C0133h> manyToOneList = ClassUtils.getManyToOneList(cls);
                if (manyToOneList != null) {
                    for (C0133h c0133h : manyToOneList) {
                        if (c0133h != null) {
                            c0135k2.f571c.put(c0133h.mo652a(), c0133h);
                        }
                    }
                }
                List<C0134i> oneToManyList = ClassUtils.getOneToManyList(cls);
                if (oneToManyList != null) {
                    for (C0134i c0134i : oneToManyList) {
                        if (c0134i != null) {
                            c0135k2.f570b.put(c0134i.mo652a(), c0134i);
                        }
                    }
                }
                f565d.put(cls.getName(), c0135k2);
                c0135k = c0135k2;
            } else {
                throw new DbException("the class[" + cls + "]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception");
            }
        }
        if (c0135k != null) {
            return c0135k;
        }
        throw new DbException("the class[" + cls + "]'s table is null");
    }

    public final String m343a() {
        return this.f567a;
    }

    public final C0131f m342a() {
        return this.f566a;
    }

    public final boolean m345a() {
        return this.f569a;
    }

    public final void m344a() {
        this.f569a = true;
    }
}
