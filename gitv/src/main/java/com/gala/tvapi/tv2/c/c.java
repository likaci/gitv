package com.gala.tvapi.tv2.c;

import android.annotation.SuppressLint;
import com.alibaba.fastjson.JSON;
import com.gala.tvapi.log.TVApiRecordLog;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.a.a;
import com.gala.tvapi.tv2.a.b;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import com.gala.video.api.ICommonApi;
import java.util.Date;
import java.util.List;

public class c<T extends ApiResult> implements com.gala.tvapi.tv2.a.c<T> {
    private static Date a = new Date();
    protected a f489a = null;
    protected ICommonApi f490a = ApiFactory.getCommonApi();
    protected String[] f491a;

    public void a(boolean z, String str, String str2, String str3) {
        if (!TVApiBase.getTVApiProperty().isSendLogRecord()) {
            return;
        }
        if (z && str != null && (str.contains("devRegister") || str.contains("playCheck"))) {
            TVApiRecordLog.addTVApiLogRecordLog(str, b() + "-" + str2 + "-" + str3);
        } else if (!z) {
            TVApiRecordLog.addTVApiLogRecordLog(str, b() + "-" + str2 + "-" + str3);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    protected static String b() {
        a.setTime(TVApiBase.getTVApiProperty().getCurrentTime());
        return DateLocalThread.formatYH(a);
    }

    public final boolean m89a() {
        if (this.f489a == null) {
            return true;
        }
        return this.f489a.a();
    }

    public T a(String str, Class<T> cls) {
        if (!(this.f489a == null || str == null)) {
            this.f489a.a(str);
        }
        if (str == null) {
            return (ApiResult) JSON.parseObject(this.f489a.a(), (Class) cls);
        }
        return (ApiResult) JSON.parseObject(str, (Class) cls);
    }

    public final String a() {
        if (this.f489a != null) {
            this.f489a.a();
        }
        return "";
    }

    public boolean a(String str, String... strArr) {
        return true;
    }

    public static String b(String str) {
        return TVApiTool.parseLicenceUrl(str);
    }

    protected static void m87b() {
        com.gala.tvapi.log.a.a("TVApiProcessor", "preprocessor-device-check");
        TVApi.deviceCheck.callSync(new IApiCallback<ApiResultDeviceCheck>() {
            public final /* synthetic */ void onSuccess(Object obj) {
                ApiResultDeviceCheck apiResultDeviceCheck = (ApiResultDeviceCheck) obj;
                if (apiResultDeviceCheck != null && apiResultDeviceCheck.data != null) {
                    TVApiBase.getTVApiProperty().setAuthId(apiResultDeviceCheck.data.authId);
                    TVApiBase.getTVApiProperty().setHideString(apiResultDeviceCheck.data.hide);
                    TVApiBase.getTVApiProperty().setApiKey(apiResultDeviceCheck.data.apiKey);
                }
            }

            public final void onException(ApiException apiException) {
                TVApiBase.getTVApiProperty().initAuthIdAndApiKey();
            }
        }, new String[0]);
    }

    public void a(boolean z, b bVar) {
        bVar.a();
    }

    public List<String> a(List<String> list) {
        if (list != null) {
            int i = 0;
            while (i < list.size()) {
                TVApiBase.getTVApiProperty();
                if (((String) list.get(i)).equals("apiKey:") || ((String) list.get(i)).equals("apiKey:APIKEY")) {
                    list.remove(i);
                    list.add("apiKey:" + TVApiBase.getTVApiProperty().getApiKey());
                    break;
                }
                i++;
            }
        }
        return list;
    }

    public final void m88a() {
        if (this.f489a != null) {
            this.f489a.a(null);
        }
    }

    public String a(String str) {
        String replace;
        String parseLicenceUrl = TVApiTool.parseLicenceUrl(str);
        if (!TVApiBase.getTVApiProperty().checkAuthIdAndApiKeyAvailable()) {
            b();
        }
        TVApiBase.getTVApiProperty();
        if (parseLicenceUrl.contains("APIKEY")) {
            TVApiBase.getTVApiProperty();
            replace = parseLicenceUrl.replace("APIKEY", TVApiBase.getTVApiProperty().getApiKey());
            com.gala.tvapi.log.a.a("TVApiProcessor", "replace url is " + replace);
        } else {
            replace = parseLicenceUrl;
        }
        TVApiBase.getTVApiProperty();
        if (!parseLicenceUrl.contains("AUTHID")) {
            return replace;
        }
        TVApiBase.getTVApiProperty();
        replace = parseLicenceUrl.replace("AUTHID", TVApiBase.getTVApiProperty().getAuthId());
        com.gala.tvapi.log.a.a("TVApiProcessor", "replace url is " + replace);
        return replace;
    }
}
