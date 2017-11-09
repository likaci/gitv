package com.gala.tvapi.vr;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.tv2.ITVApiServer;
import com.gala.tvapi.vr.p030a.C0320a;
import com.gala.tvapi.vr.result.DeviceCheckVRResult;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import com.gala.video.api.http.IHttpCallback;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class C0322b<T extends ApiResult> implements ITVApiServer<T> {
    private C0320a f1185a = C0320a.m746a();
    private C0315a f1186a;
    private Class<T> f1187a;
    private String f1188a;
    private boolean f1189a;

    public C0322b(C0315a c0315a, Class<T> cls, String str, boolean z) {
        this.f1186a = c0315a;
        this.f1187a = cls;
        this.f1188a = str;
        this.f1189a = z;
    }

    public final void callSync(IApiCallback<T> callback, String... strings) {
        C0322b.m753a((IApiCallback) callback);
        m754a(true, callback, strings);
    }

    public final void call(IApiCallback<T> callback, String... strings) {
        C0322b.m753a((IApiCallback) callback);
        m754a(false, callback, strings);
    }

    public final void callSync(IApiCallback<T> callback, TVApiHeader tVApiHeader, String... strArr) {
        C0322b.m753a((IApiCallback) callback);
    }

    public final void call(IApiCallback<T> callback, TVApiHeader tVApiHeader, String... strArr) {
        C0322b.m753a((IApiCallback) callback);
    }

    private static void m753a(IApiCallback<T> iApiCallback) {
        if (iApiCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void m754a(boolean z, final IApiCallback<T> iApiCallback, String... strArr) {
        final String a = this.f1186a.mo864a(strArr);
        String a2 = this.f1186a.mo863a();
        IHttpCallback c03211 = new IHttpCallback(this) {
            private /* synthetic */ C0322b f1181a;
            private String f1183a = a;

            public final void onSuccess(String response, String httpCode, List<Integer> requestTimes) {
                long elapsedRealtime;
                long elapsedRealtime2;
                Exception e;
                String str;
                JSONObject parseObject;
                ApiException apiException;
                String str2 = null;
                try {
                    elapsedRealtime = SystemClock.elapsedRealtime();
                    ApiResult a = this.f1181a.m755a(response);
                    elapsedRealtime2 = SystemClock.elapsedRealtime();
                    if (a != null) {
                        Class cls = a.getClass();
                        if (cls == DeviceCheckVRResult.class) {
                            DeviceCheckVRResult deviceCheckVRResult = (DeviceCheckVRResult) a;
                            if (deviceCheckVRResult.data != null) {
                                VRApi.setApiKey(deviceCheckVRResult.data.apiKey);
                            }
                        }
                        if (cls == this.f1181a.f1187a) {
                            String str3 = a.code;
                            if (str3 != null) {
                                try {
                                    if (str3.equals("N000")) {
                                        a.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                        iApiCallback.onSuccess(a);
                                        return;
                                    }
                                } catch (Exception e2) {
                                    e = e2;
                                    str = str3;
                                    e.printStackTrace();
                                    try {
                                        elapsedRealtime = SystemClock.elapsedRealtime();
                                        parseObject = JSON.parseObject(response);
                                        if (parseObject != null) {
                                            str = parseObject.getString(PingbackConstants.CODE);
                                            str2 = parseObject.getString("msg");
                                        }
                                        elapsedRealtime2 = SystemClock.elapsedRealtime();
                                        if (str != null || str.equals("")) {
                                            m751a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
                                        }
                                        apiException = new ApiException(str2, str, httpCode, this.f1183a);
                                        apiException.setDetailMessage(this.f1183a + "\n" + response);
                                        apiException.setRequestTimes(requestTimes);
                                        apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                        m750a(apiException);
                                        return;
                                    } catch (Exception e3) {
                                        e3.printStackTrace();
                                        m751a(httpCode, response, 0, requestTimes);
                                        return;
                                    }
                                }
                            }
                            str = str3;
                        } else {
                            str = null;
                        }
                        try {
                            apiException = new ApiException(null, str, httpCode, this.f1183a);
                            apiException.setDetailMessage(this.f1183a + "\n" + response);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            m750a(apiException);
                        } catch (Exception e4) {
                            e3 = e4;
                            e3.printStackTrace();
                            elapsedRealtime = SystemClock.elapsedRealtime();
                            parseObject = JSON.parseObject(response);
                            if (parseObject != null) {
                                str = parseObject.getString(PingbackConstants.CODE);
                                str2 = parseObject.getString("msg");
                            }
                            elapsedRealtime2 = SystemClock.elapsedRealtime();
                            if (str != null) {
                            }
                            m751a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
                        }
                    }
                } catch (Exception e5) {
                    e3 = e5;
                    str = null;
                    e3.printStackTrace();
                    elapsedRealtime = SystemClock.elapsedRealtime();
                    parseObject = JSON.parseObject(response);
                    if (parseObject != null) {
                        str = parseObject.getString(PingbackConstants.CODE);
                        str2 = parseObject.getString("msg");
                    }
                    elapsedRealtime2 = SystemClock.elapsedRealtime();
                    if (str != null) {
                    }
                    m751a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
                }
            }

            private void m751a(String str, String str2, int i, List<Integer> list) {
                ApiException apiException = new ApiException("", "-100", str, this.f1183a, JSONException.class.toString());
                apiException.setDetailMessage(this.f1183a + "\n" + str2 + "\n");
                apiException.setParseTime(i);
                apiException.setRequestTimes(list);
                m750a(apiException);
            }

            public final void onException(Exception e, String httpCode, String str, List<Integer> times) {
                ApiException apiException;
                if (e != null) {
                    apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1183a, e.getClass().toString());
                    apiException.setDetailMessage(this.f1183a + "\n" + e.getMessage());
                } else {
                    apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f1183a, "");
                    apiException.setDetailMessage(this.f1183a + "\n");
                }
                apiException.setRequestTimes(times);
                m750a(apiException);
            }

            private void m750a(ApiException apiException) {
                apiException.printStackTrace();
                iApiCallback.onException(apiException);
            }

            public final String onCalling(String url) {
                return url;
            }

            public final List<String> onHeader(List<String> header) {
                return header;
            }
        };
        if (z) {
            this.f1185a.m749b(a, this.f1186a.mo863a(), c03211, this.f1189a, this.f1188a, a2);
        } else {
            this.f1185a.m748a(a, this.f1186a.mo863a(), c03211, this.f1189a, this.f1188a, a2);
        }
    }

    protected final ApiResult m755a(String str) throws Exception {
        return (ApiResult) JSON.parseObject(str, this.f1187a);
    }
}
