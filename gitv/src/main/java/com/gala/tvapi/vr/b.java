package com.gala.tvapi.vr;

import android.os.SystemClock;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.TVApiHeader;
import com.gala.tvapi.tv2.ITVApiServer;
import com.gala.tvapi.vr.a.a;
import com.gala.tvapi.vr.result.DeviceCheckVRResult;
import com.gala.video.api.ApiException;
import com.gala.video.api.ApiResult;
import com.gala.video.api.IApiCallback;
import com.gala.video.api.http.IHttpCallback;
import com.mcto.ads.internal.net.PingbackConstants;
import java.util.List;

public final class b<T extends ApiResult> implements ITVApiServer<T> {
    private a a = a.a();
    private a f537a;
    private Class<T> f538a;
    private String f539a;
    private boolean f540a;

    public b(a aVar, Class<T> cls, String str, boolean z) {
        this.f537a = aVar;
        this.f538a = cls;
        this.f539a = str;
        this.f540a = z;
    }

    public final void callSync(IApiCallback<T> callback, String... strings) {
        a((IApiCallback) callback);
        a(true, callback, strings);
    }

    public final void call(IApiCallback<T> callback, String... strings) {
        a((IApiCallback) callback);
        a(false, callback, strings);
    }

    public final void callSync(IApiCallback<T> callback, TVApiHeader tVApiHeader, String... strArr) {
        a((IApiCallback) callback);
    }

    public final void call(IApiCallback<T> callback, TVApiHeader tVApiHeader, String... strArr) {
        a((IApiCallback) callback);
    }

    private static void a(IApiCallback<T> iApiCallback) {
        if (iApiCallback == null) {
            throw new NullPointerException("A callback is needed for TVApi");
        }
    }

    private void a(boolean z, final IApiCallback<T> iApiCallback, String... strArr) {
        final String a = this.f537a.a(strArr);
        String a2 = this.f537a.a();
        IHttpCallback anonymousClass1 = new IHttpCallback(this) {
            private /* synthetic */ b a;
            private String f542a = a;

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
                    ApiResult a = this.a.a(response);
                    elapsedRealtime2 = SystemClock.elapsedRealtime();
                    if (a != null) {
                        Class cls = a.getClass();
                        if (cls == DeviceCheckVRResult.class) {
                            DeviceCheckVRResult deviceCheckVRResult = (DeviceCheckVRResult) a;
                            if (deviceCheckVRResult.data != null) {
                                VRApi.setApiKey(deviceCheckVRResult.data.apiKey);
                            }
                        }
                        if (cls == this.a.f538a) {
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
                                            a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
                                        }
                                        apiException = new ApiException(str2, str, httpCode, this.f542a);
                                        apiException.setDetailMessage(this.f542a + "\n" + response);
                                        apiException.setRequestTimes(requestTimes);
                                        apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                                        a(apiException);
                                        return;
                                    } catch (Exception e3) {
                                        e3.printStackTrace();
                                        a(httpCode, response, 0, requestTimes);
                                        return;
                                    }
                                }
                            }
                            str = str3;
                        } else {
                            str = null;
                        }
                        try {
                            apiException = new ApiException(null, str, httpCode, this.f542a);
                            apiException.setDetailMessage(this.f542a + "\n" + response);
                            apiException.setParseTime((int) (elapsedRealtime2 - elapsedRealtime));
                            a(apiException);
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
                            a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
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
                    a(httpCode, response, (int) (elapsedRealtime2 - elapsedRealtime), requestTimes);
                }
            }

            private void a(String str, String str2, int i, List<Integer> list) {
                ApiException apiException = new ApiException("", "-100", str, this.f542a, JSONException.class.toString());
                apiException.setDetailMessage(this.f542a + "\n" + str2 + "\n");
                apiException.setParseTime(i);
                apiException.setRequestTimes(list);
                a(apiException);
            }

            public final void onException(Exception e, String httpCode, String str, List<Integer> times) {
                ApiException apiException;
                if (e != null) {
                    apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f542a, e.getClass().toString());
                    apiException.setDetailMessage(this.f542a + "\n" + e.getMessage());
                } else {
                    apiException = new ApiException("", "HTTP_ERR_" + httpCode, httpCode, this.f542a, "");
                    apiException.setDetailMessage(this.f542a + "\n");
                }
                apiException.setRequestTimes(times);
                a(apiException);
            }

            private void a(ApiException apiException) {
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
            this.a.b(a, this.f537a.a(), anonymousClass1, this.f540a, this.f539a, a2);
        } else {
            this.a.a(a, this.f537a.a(), anonymousClass1, this.f540a, this.f539a, a2);
        }
    }

    protected final ApiResult a(String str) throws Exception {
        return (ApiResult) JSON.parseObject(str, this.f538a);
    }
}
