package com.gala.imageprovider.p000private;

import com.gala.afinal.utils.FieldUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

public class C0130j {
    private Class<?> f555a;
    private String f556a;
    private Field f557a;
    private Method f558a;
    private String f559b;
    private Method f560b;

    public final void m326a(Object obj, Object obj2) {
        Date date = null;
        if (this.f560b == null || obj2 == null) {
            try {
                this.f557a.setAccessible(true);
                this.f557a.set(obj, obj2);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            if (this.f555a == String.class) {
                this.f560b.invoke(obj, new Object[]{obj2.toString()});
            } else if (this.f555a == Integer.TYPE || this.f555a == Integer.class) {
                int intValue;
                r1 = this.f560b;
                r2 = new Object[1];
                if (obj2 == null) {
                    Integer num = null;
                    intValue = num.intValue();
                } else {
                    intValue = Integer.parseInt(obj2.toString());
                }
                r2[0] = Integer.valueOf(intValue);
                r1.invoke(obj, r2);
            } else if (this.f555a == Float.TYPE || this.f555a == Float.class) {
                float floatValue;
                r1 = this.f560b;
                r2 = new Object[1];
                if (obj2 == null) {
                    Float f = null;
                    floatValue = f.floatValue();
                } else {
                    floatValue = Float.parseFloat(obj2.toString());
                }
                r2[0] = Float.valueOf(floatValue);
                r1.invoke(obj, r2);
            } else if (this.f555a == Double.TYPE || this.f555a == Double.class) {
                double doubleValue;
                r2 = this.f560b;
                r3 = new Object[1];
                if (obj2 == null) {
                    Double d = null;
                    doubleValue = d.doubleValue();
                } else {
                    doubleValue = Double.parseDouble(obj2.toString());
                }
                r3[0] = Double.valueOf(doubleValue);
                r2.invoke(obj, r3);
            } else if (this.f555a == Long.TYPE || this.f555a == Long.class) {
                long longValue;
                r2 = this.f560b;
                r3 = new Object[1];
                if (obj2 == null) {
                    Long l = null;
                    longValue = l.longValue();
                } else {
                    longValue = Long.parseLong(obj2.toString());
                }
                r3[0] = Long.valueOf(longValue);
                r2.invoke(obj, r3);
            } else if (this.f555a == Date.class || this.f555a == java.sql.Date.class) {
                r1 = this.f560b;
                r2 = new Object[1];
                if (obj2 != null) {
                    date = FieldUtils.stringToDateTime(obj2.toString());
                }
                r2[0] = date;
                r1.invoke(obj, r2);
            } else if (this.f555a == Boolean.TYPE || this.f555a == Boolean.class) {
                boolean booleanValue;
                r1 = this.f560b;
                r2 = new Object[1];
                if (obj2 == null) {
                    Boolean bool = null;
                    booleanValue = bool.booleanValue();
                } else {
                    booleanValue = "1".equals(obj2.toString());
                }
                r2[0] = Boolean.valueOf(booleanValue);
                r1.invoke(obj, r2);
            } else {
                this.f560b.invoke(obj, new Object[]{obj2});
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public final <T> T m324a(Object obj) {
        if (!(obj == null || this.f558a == null)) {
            try {
                return this.f558a.invoke(obj, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final String mo652a() {
        return this.f556a;
    }

    public final void m327a(String str) {
        this.f556a = str;
    }

    public final String m331b() {
        return this.f559b;
    }

    public final void m333b(String str) {
        this.f559b = str;
    }

    public final Class<?> m330b() {
        return this.f555a;
    }

    public final void m332b(Class<?> cls) {
        this.f555a = cls;
    }

    public final void m329a(Method method) {
        this.f558a = method;
    }

    public final void m334b(Method method) {
        this.f560b = method;
    }

    public final void m328a(Field field) {
        this.f557a = field;
    }
}
