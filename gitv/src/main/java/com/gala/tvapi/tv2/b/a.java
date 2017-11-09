package com.gala.tvapi.tv2.b;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.property.TVApiProperty;

public final class a implements com.gala.tvapi.tv2.a.a {
    private long a = 1000;
    private com.gala.tvapi.a.a.a f488a = new com.gala.tvapi.a.a.a();

    public final String a() {
        return com.gala.tvapi.a.a.a.a();
    }

    public final boolean m86a() {
        boolean isCacheDeviceCheck;
        TVApiProperty tVApiProperty = TVApiBase.getTVApiProperty();
        if (tVApiProperty != null) {
            isCacheDeviceCheck = tVApiProperty.isCacheDeviceCheck();
        } else {
            isCacheDeviceCheck = false;
        }
        if (!isCacheDeviceCheck || this.f488a == null) {
            return true;
        }
        String a = com.gala.tvapi.a.a.a.a();
        com.gala.tvapi.log.a.a("isUpdateData", "content=" + a);
        long a2;
        if (a == null || a.equals("")) {
            a2 = com.gala.tvapi.a.a.a().a("tvapi_db", "net_request_time");
            this.a <<= 1;
            if (this.a >= 3600000) {
                this.a = 2000;
            }
            com.gala.tvapi.log.a.a("isUpdateData", "Real Request Duration = " + this.a);
            if (System.currentTimeMillis() - a2 <= this.a) {
                return false;
            }
            return true;
        }
        a2 = com.gala.tvapi.a.a.a().a("tvapi_db", "apikey_update_time");
        com.gala.tvapi.log.a.a("isUpdateData", "Api key update Time = " + a2);
        if (System.currentTimeMillis() - a2 <= 14400000) {
            return false;
        }
        return true;
    }

    public final void a(String str) {
        com.gala.tvapi.a.a.a().a("tvapi_db", "device_check_content", str);
    }

    public final void a(long j) {
        com.gala.tvapi.a.a.a().a("tvapi_db", "apikey_update_time", j);
    }

    public final void b(long j) {
        com.gala.tvapi.a.a.a().a("tvapi_db", "net_request_time", j);
    }
}
