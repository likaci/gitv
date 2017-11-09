package com.gala.imageprovider.p000private;

import com.gala.afinal.utils.FieldUtils;
import java.util.Date;

public final class C0132g {
    private Object f561a;
    private String f562a;

    public C0132g(String str, Object obj) {
        this.f562a = str;
        this.f561a = obj;
    }

    public final String m336a() {
        return this.f562a;
    }

    public final Object m335a() {
        if ((this.f561a instanceof Date) || (this.f561a instanceof java.sql.Date)) {
            return FieldUtils.SDF.format(this.f561a);
        }
        return this.f561a;
    }
}
