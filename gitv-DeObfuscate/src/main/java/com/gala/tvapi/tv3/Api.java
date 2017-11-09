package com.gala.tvapi.tv3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.result.RegisterResult;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public abstract class Api<T extends ApiResult> implements IApi<T> {
    private static int f1054a = 0;
    protected static String f1055a = null;
    protected Class<T> f1056a;

    class C02961 implements IApiCallback<RegisterResult> {
        C02961() {
        }

        public void onSuccess(RegisterResult registerResult) {
        }

        public void onException(ApiException apiException) {
        }
    }

    public Api(Class<T> clazz) {
        this.f1056a = clazz;
    }

    protected static String m706a(String str, String[] strArr) {
        if (strArr.length == 0) {
            return str;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = m705a(strArr[i]);
        }
        return String.format(str, (Object[]) strArr2);
    }

    private static String m705a(String str) {
        if (str != null) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    protected static void m707a() {
        if (!ApiDataCache.getRegisterDataCache().isRegisterCacheAvailable()) {
            ITVApi.registerApi().callSync(new C02961(), new String[0]);
        }
    }

    protected static String m704a() {
        if (f1055a == null || f1055a.isEmpty()) {
            f1055a = ApiDataCache.getRegisterDataCache().getAuthorization();
        }
        return "Authorization:" + f1055a;
    }

    protected static int m703a() {
        if (f1054a == Integer.MAX_VALUE) {
            f1054a = 0;
        }
        int i = f1054a + 1;
        f1054a = i;
        return i;
    }

    protected final T m708a(String str) {
        try {
            return (ApiResult) JSON.parseObject(str, this.f1056a);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
