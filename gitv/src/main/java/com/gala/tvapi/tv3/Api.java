package com.gala.tvapi.tv3;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.gala.tvapi.tv3.cache.ApiDataCache;
import com.gala.tvapi.tv3.result.RegisterResult;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public abstract class Api<T extends ApiResult> implements IApi<T> {
    private static int a = 0;
    protected static String f506a = null;
    protected Class<T> f507a;

    public Api(Class<T> clazz) {
        this.f507a = clazz;
    }

    protected static String a(String str, String[] strArr) {
        if (strArr.length == 0) {
            return str;
        }
        String[] strArr2 = new String[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            strArr2[i] = a(strArr[i]);
        }
        return String.format(str, (Object[]) strArr2);
    }

    private static String a(String str) {
        if (str != null) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    protected static void m91a() {
        if (!ApiDataCache.getRegisterDataCache().isRegisterCacheAvailable()) {
            ITVApi.registerApi().callSync(new IApiCallback<RegisterResult>() {
                public void onSuccess(RegisterResult registerResult) {
                }

                public void onException(ApiException apiException) {
                }
            }, new String[0]);
        }
    }

    protected static String m90a() {
        if (f506a == null || f506a.isEmpty()) {
            f506a = ApiDataCache.getRegisterDataCache().getAuthorization();
        }
        return "Authorization:" + f506a;
    }

    protected static int a() {
        if (a == Integer.MAX_VALUE) {
            a = 0;
        }
        int i = a + 1;
        a = i;
        return i;
    }

    protected final T m92a(String str) {
        try {
            return (ApiResult) JSON.parseObject(str, this.f507a);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
