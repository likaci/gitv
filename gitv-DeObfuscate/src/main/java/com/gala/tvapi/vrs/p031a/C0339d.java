package com.gala.tvapi.vrs.p031a;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p008b.C0213b;
import com.gala.tvapi.p008b.C0218c;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.model.Vid;
import com.gala.tvapi.vrs.result.ApiResultM3u8;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class C0339d<T extends ApiResult> extends C0336k<T> {
    protected String f1214a = null;
    private boolean f1215a = false;

    public T mo850a(String str, Class<T> cls) {
        if (!(super.mo850a(str, cls) instanceof ApiResultM3u8)) {
            return super.mo850a(str, cls);
        }
        ApiResultM3u8 apiResultM3u8 = (ApiResultM3u8) super.mo850a(str, cls);
        if (apiResultM3u8 == null || apiResultM3u8.data == null) {
            return apiResultM3u8;
        }
        apiResultM3u8.data.m3utx = m767a(this.f1214a, apiResultM3u8.data.m3utx);
        if (apiResultM3u8.data.vidl == null || apiResultM3u8.data.vidl.size() <= 0) {
            return apiResultM3u8;
        }
        for (Vid vid : apiResultM3u8.data.vidl) {
            if (!(vid.m3utx == null || vid.m3utx.equals(""))) {
                vid.m3utx = m767a(this.f1214a, vid.m3utx);
            }
        }
        return apiResultM3u8;
    }

    public final boolean mo856a(String str, String... strArr) {
        try {
            String query = new URL(str).getQuery();
            if (!(query == null || query.isEmpty())) {
                this.a = query.split("&");
                if (this.a != null && this.a.length > 0) {
                    for (String str2 : this.a) {
                        C0262a.m629a("checkParams", "param=" + str2);
                        String[] split = str2.split(SearchCriteria.EQ);
                        if (split != null && split.length > 0) {
                            if (split[0].equals(Keys.f2035T) && (split.length != 2 || split[1].length() != 13)) {
                                return false;
                            }
                            if (split[0].equals("sc") && (split.length != 2 || split[1].isEmpty())) {
                                return false;
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
        }
        return super.mo856a(str, strArr);
    }

    public final void m768a(String str, boolean z) {
        this.f1214a = str;
        this.f1215a = z;
    }

    protected final String m767a(String str, String str2) {
        C0213b a = C0218c.m605a(TVApiBase.getTVApiProperty().getPlatform());
        if (str2 == null) {
            return str2;
        }
        if (this.f1215a) {
            if (str2.endsWith("m3u8")) {
                return str2 + "?qypid=" + str + "_5201&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId() + "&src=" + a.mo829a();
            }
            return str2 + "&qypid=" + str + "_5201&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId() + "&src=" + a.mo829a();
        } else if (str2.endsWith("m3u8")) {
            return str2 + "?src=" + a.mo829a();
        } else {
            return str2 + "&src=" + a.mo829a();
        }
    }
}
