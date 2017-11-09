package com.gala.tvapi.tv2.p025b;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p006a.C0211a;
import com.gala.tvapi.p006a.p007a.C0210a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p024a.C0285a;
import com.gala.tvapi.tv2.property.TVApiProperty;

public final class C0288a implements C0285a {
    private long f974a = 1000;
    private C0210a f975a = new C0210a();

    public final String mo846a() {
        return C0210a.m565a();
    }

    public final boolean m683a() {
        boolean isCacheDeviceCheck;
        TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
        if (tVApiProperty != null) {
            isCacheDeviceCheck = tVApiProperty.isCacheDeviceCheck();
        } else {
            isCacheDeviceCheck = false;
        }
        if (!isCacheDeviceCheck || this.f975a == null) {
            return true;
        }
        String a = C0210a.m565a();
        C0262a.m629a("isUpdateData", "content=" + a);
        long a2;
        if (a == null || a.equals("")) {
            a2 = C0211a.m566a().m567a("tvapi_db", "net_request_time");
            this.f974a <<= 1;
            if (this.f974a >= 3600000) {
                this.f974a = 2000;
            }
            C0262a.m629a("isUpdateData", "Real Request Duration = " + this.f974a);
            if (System.currentTimeMillis() - a2 <= this.f974a) {
                return false;
            }
            return true;
        }
        a2 = C0211a.m566a().m567a("tvapi_db", "apikey_update_time");
        C0262a.m629a("isUpdateData", "Api key update Time = " + a2);
        if (System.currentTimeMillis() - a2 <= 14400000) {
            return false;
        }
        return true;
    }

    public final void mo848a(String str) {
        C0211a.m566a().m571a("tvapi_db", "device_check_content", str);
    }

    public final void mo847a(long j) {
        C0211a.m566a().m570a("tvapi_db", "apikey_update_time", j);
    }

    public final void mo849b(long j) {
        C0211a.m566a().m570a("tvapi_db", "net_request_time", j);
    }
}
