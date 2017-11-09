package com.gala.imageprovider.private;

import com.gala.afinal.exception.DbException;
import com.gala.afinal.utils.ClassUtils;
import com.gala.afinal.utils.FieldUtils;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public final class k {
    private static final HashMap<String, k> d = new HashMap();
    private f a;
    private String f18a;
    public final HashMap<String, j> f19a = new HashMap();
    private boolean f20a;
    public final HashMap<String, i> b = new HashMap();
    public final HashMap<String, h> c = new HashMap();

    private k() {
    }

    public static k a(Class<?> cls) {
        if (cls == null) {
            throw new DbException("table info get error,because the clazz is null");
        }
        k kVar = (k) d.get(cls.getName());
        if (kVar == null) {
            k kVar2 = new k();
            kVar2.f18a = ClassUtils.getTableName(cls);
            cls.getName();
            Field primaryKeyField = ClassUtils.getPrimaryKeyField(cls);
            if (primaryKeyField != null) {
                f fVar = new f();
                fVar.a(FieldUtils.getColumnByField(primaryKeyField));
                primaryKeyField.getName();
                fVar.b(FieldUtils.getFieldSetMethod((Class) cls, primaryKeyField));
                fVar.a(FieldUtils.getFieldGetMethod((Class) cls, primaryKeyField));
                fVar.b(primaryKeyField.getType());
                kVar2.a = fVar;
                List<j> propertyList = ClassUtils.getPropertyList(cls);
                if (propertyList != null) {
                    for (j jVar : propertyList) {
                        if (jVar != null) {
                            kVar2.f19a.put(jVar.a(), jVar);
                        }
                    }
                }
                List<h> manyToOneList = ClassUtils.getManyToOneList(cls);
                if (manyToOneList != null) {
                    for (h hVar : manyToOneList) {
                        if (hVar != null) {
                            kVar2.c.put(hVar.a(), hVar);
                        }
                    }
                }
                List<i> oneToManyList = ClassUtils.getOneToManyList(cls);
                if (oneToManyList != null) {
                    for (i iVar : oneToManyList) {
                        if (iVar != null) {
                            kVar2.b.put(iVar.a(), iVar);
                        }
                    }
                }
                d.put(cls.getName(), kVar2);
                kVar = kVar2;
            } else {
                throw new DbException("the class[" + cls + "]'s idField is null , \n you can define _id,id property or use annotation @id to solution this exception");
            }
        }
        if (kVar != null) {
            return kVar;
        }
        throw new DbException("the class[" + cls + "]'s table is null");
    }

    public final String m4a() {
        return this.f18a;
    }

    public final f a() {
        return this.a;
    }

    public final boolean m6a() {
        return this.f20a;
    }

    public final void m5a() {
        this.f20a = true;
    }
}
