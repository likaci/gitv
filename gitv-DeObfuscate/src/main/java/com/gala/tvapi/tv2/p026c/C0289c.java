package com.gala.tvapi.tv2.p026c;

import android.annotation.SuppressLint;
import com.alibaba.fastjson.JSON;
import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.log.TVApiRecordLog;
import com.gala.tvapi.tools.DateLocalThread;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.p024a.C0281b;
import com.gala.tvapi.tv2.p024a.C0285a;
import com.gala.tvapi.tv2.p024a.C0286c;
import com.gala.tvapi.tv2.result.ApiResultDeviceCheck;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiFactory;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import com.gala.video.api.ICommonApi;
import java.util.Date;
import java.util.List;

public class C0289c<T extends ApiResult> implements C0286c<T> {
    private static Date f976a = new Date();
    protected C0285a f977a = null;
    protected ICommonApi f978a = ApiFactory.getCommonApi();
    protected String[] f979a;

    class C02941 implements IApiCallback<ApiResultDeviceCheck> {
        C02941() {
        }

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
    }

    public void mo855a(boolean z, String str, String str2, String str3) {
        if (!TVApiBase.getTVApiProperty().isSendLogRecord()) {
            return;
        }
        if (z && str != null && (str.contains("devRegister") || str.contains("playCheck"))) {
            TVApiRecordLog.addTVApiLogRecordLog(str, C0289c.mo865b() + "-" + str2 + "-" + str3);
        } else if (!z) {
            TVApiRecordLog.addTVApiLogRecordLog(str, C0289c.mo865b() + "-" + str2 + "-" + str3);
        }
    }

    @SuppressLint({"SimpleDateFormat"})
    protected static String mo865b() {
        f976a.setTime(TVApiBase.getTVApiProperty().getCurrentTime());
        return DateLocalThread.formatYH(f976a);
    }

    public final boolean m695a() {
        if (this.f977a == null) {
            return true;
        }
        return this.f977a.mo846a();
    }

    public T mo850a(String str, Class<T> cls) {
        if (!(this.f977a == null || str == null)) {
            this.f977a.mo848a(str);
        }
        if (str == null) {
            return (ApiResult) JSON.parseObject(this.f977a.mo846a(), (Class) cls);
        }
        return (ApiResult) JSON.parseObject(str, (Class) cls);
    }

    public final String mo851a() {
        if (this.f977a != null) {
            this.f977a.mo846a();
        }
        return "";
    }

    public boolean mo856a(String str, String... strArr) {
        return true;
    }

    public static String m686b(String str) {
        return TVApiTool.parseLicenceUrl(str);
    }

    protected static void m687b() {
        C0262a.m629a("TVApiProcessor", "preprocessor-device-check");
        TVApi.deviceCheck.callSync(new C02941(), new String[0]);
    }

    public void mo854a(boolean z, C0281b c0281b) {
        c0281b.mo836a();
    }

    public List<String> mo853a(List<String> list) {
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

    public final void m692a() {
        if (this.f977a != null) {
            this.f977a.mo848a(null);
        }
    }

    public String mo852a(String str) {
        String replace;
        String parseLicenceUrl = TVApiTool.parseLicenceUrl(str);
        if (!TVApiBase.getTVApiProperty().checkAuthIdAndApiKeyAvailable()) {
            C0289c.mo865b();
        }
        TVApiBase.getTVApiProperty();
        if (parseLicenceUrl.contains("APIKEY")) {
            TVApiBase.getTVApiProperty();
            replace = parseLicenceUrl.replace("APIKEY", TVApiBase.getTVApiProperty().getApiKey());
            C0262a.m629a("TVApiProcessor", "replace url is " + replace);
        } else {
            replace = parseLicenceUrl;
        }
        TVApiBase.getTVApiProperty();
        if (!parseLicenceUrl.contains("AUTHID")) {
            return replace;
        }
        TVApiBase.getTVApiProperty();
        replace = parseLicenceUrl.replace("AUTHID", TVApiBase.getTVApiProperty().getAuthId());
        C0262a.m629a("TVApiProcessor", "replace url is " + replace);
        return replace;
    }
}
