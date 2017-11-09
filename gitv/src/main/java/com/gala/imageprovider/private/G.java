package com.gala.imageprovider.private;

import com.gala.afinal.utils.FieldUtils;
import java.util.Date;

public final class g {
    private Object a;
    private String f25a;

    public g(String str, Object obj) {
        this.f25a = str;
        this.a = obj;
    }

    public final String m8a() {
        return this.f25a;
    }

    public final Object a() {
        if ((this.a instanceof Date) || (this.a instanceof java.sql.Date)) {
            return FieldUtils.SDF.format(this.a);
        }
        return this.a;
    }
}
