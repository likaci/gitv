package com.gala.imageprovider.private;

import com.gala.afinal.utils.FieldUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;

public class j {
    private Class<?> a;
    private String f21a;
    private Field f22a;
    private Method f23a;
    private String b;
    private Method f24b;

    public final void a(Object obj, Object obj2) {
        Date date = null;
        if (this.f24b == null || obj2 == null) {
            try {
                this.f22a.setAccessible(true);
                this.f22a.set(obj, obj2);
                return;
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            if (this.a == String.class) {
                this.f24b.invoke(obj, new Object[]{obj2.toString()});
            } else if (this.a == Integer.TYPE || this.a == Integer.class) {
                int intValue;
                r1 = this.f24b;
                r2 = new Object[1];
                if (obj2 == null) {
                    Integer num = null;
                    intValue = num.intValue();
                } else {
                    intValue = Integer.parseInt(obj2.toString());
                }
                r2[0] = Integer.valueOf(intValue);
                r1.invoke(obj, r2);
            } else if (this.a == Float.TYPE || this.a == Float.class) {
                float floatValue;
                r1 = this.f24b;
                r2 = new Object[1];
                if (obj2 == null) {
                    Float f = null;
                    floatValue = f.floatValue();
                } else {
                    floatValue = Float.parseFloat(obj2.toString());
                }
                r2[0] = Float.valueOf(floatValue);
                r1.invoke(obj, r2);
            } else if (this.a == Double.TYPE || this.a == Double.class) {
                double doubleValue;
                r2 = this.f24b;
                r3 = new Object[1];
                if (obj2 == null) {
                    Double d = null;
                    doubleValue = d.doubleValue();
                } else {
                    doubleValue = Double.parseDouble(obj2.toString());
                }
                r3[0] = Double.valueOf(doubleValue);
                r2.invoke(obj, r3);
            } else if (this.a == Long.TYPE || this.a == Long.class) {
                long longValue;
                r2 = this.f24b;
                r3 = new Object[1];
                if (obj2 == null) {
                    Long l = null;
                    longValue = l.longValue();
                } else {
                    longValue = Long.parseLong(obj2.toString());
                }
                r3[0] = Long.valueOf(longValue);
                r2.invoke(obj, r3);
            } else if (this.a == Date.class || this.a == java.sql.Date.class) {
                r1 = this.f24b;
                r2 = new Object[1];
                if (obj2 != null) {
                    date = FieldUtils.stringToDateTime(obj2.toString());
                }
                r2[0] = date;
                r1.invoke(obj, r2);
            } else if (this.a == Boolean.TYPE || this.a == Boolean.class) {
                boolean booleanValue;
                r1 = this.f24b;
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
                this.f24b.invoke(obj, new Object[]{obj2});
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public final <T> T a(Object obj) {
        if (!(obj == null || this.f23a == null)) {
            try {
                return this.f23a.invoke(obj, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final String a() {
        return this.f21a;
    }

    public final void a(String str) {
        this.f21a = str;
    }

    public final String m7b() {
        return this.b;
    }

    public final void b(String str) {
        this.b = str;
    }

    public final Class<?> b() {
        return this.a;
    }

    public final void b(Class<?> cls) {
        this.a = cls;
    }

    public final void a(Method method) {
        this.f23a = method;
    }

    public final void b(Method method) {
        this.f24b = method;
    }

    public final void a(Field field) {
        this.f22a = field;
    }
}
