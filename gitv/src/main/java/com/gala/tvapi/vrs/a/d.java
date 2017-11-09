package com.gala.tvapi.vrs.a;

import com.gala.tvapi.b.b;
import com.gala.tvapi.b.c;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.vrs.model.Vid;
import com.gala.tvapi.vrs.result.ApiResultM3u8;
import com.gala.video.api.ApiResult;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.net.MalformedURLException;
import java.net.URL;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class d<T extends ApiResult> extends k<T> {
    protected String a = null;
    private boolean f546a = false;

    public T a(String str, Class<T> cls) {
        if (!(super.a(str, cls) instanceof ApiResultM3u8)) {
            return super.a(str, cls);
        }
        ApiResultM3u8 apiResultM3u8 = (ApiResultM3u8) super.a(str, cls);
        if (apiResultM3u8 == null || apiResultM3u8.data == null) {
            return apiResultM3u8;
        }
        apiResultM3u8.data.m3utx = a(this.a, apiResultM3u8.data.m3utx);
        if (apiResultM3u8.data.vidl == null || apiResultM3u8.data.vidl.size() <= 0) {
            return apiResultM3u8;
        }
        for (Vid vid : apiResultM3u8.data.vidl) {
            if (!(vid.m3utx == null || vid.m3utx.equals(""))) {
                vid.m3utx = a(this.a, vid.m3utx);
            }
        }
        return apiResultM3u8;
    }

    public final boolean a(String str, String... strArr) {
        try {
            String query = new URL(str).getQuery();
            if (!(query == null || query.isEmpty())) {
                this.a = query.split("&");
                if (this.a != null && this.a.length > 0) {
                    for (String str2 : this.a) {
                        a.a("checkParams", "param=" + str2);
                        String[] split = str2.split(SearchCriteria.EQ);
                        if (split != null && split.length > 0) {
                            if (split[0].equals(Keys.T) && (split.length != 2 || split[1].length() != 13)) {
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
        return super.a(str, strArr);
    }

    public final void a(String str, boolean z) {
        this.a = str;
        this.f546a = z;
    }

    protected final String a(String str, String str2) {
        b a = c.a(TVApiBase.getTVApiProperty().getPlatform());
        if (str2 == null) {
            return str2;
        }
        if (this.f546a) {
            if (str2.endsWith("m3u8")) {
                return str2 + "?qypid=" + str + "_5201&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId() + "&src=" + a.a();
            }
            return str2 + "&qypid=" + str + "_5201&qyid=" + TVApiBase.getTVApiProperty().getPassportDeviceId() + "&src=" + a.a();
        } else if (str2.endsWith("m3u8")) {
            return str2 + "?src=" + a.a();
        } else {
            return str2 + "&src=" + a.a();
        }
    }
}
