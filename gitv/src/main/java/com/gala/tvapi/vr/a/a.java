package com.gala.tvapi.vr.a;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.api.http.IHttpCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.cybergarage.http.HTTP;
import org.cybergarage.soap.SOAP;

public final class a {
    private static final a a = new a();
    private long f496a = 0;
    private String f497a = "";
    private final ExecutorService f498a = Executors.newFixedThreadPool(1, new ThreadFactory() {
        private AtomicInteger a = new AtomicInteger(0);

        public final Thread newThread(Runnable runnable) {
            return new Thread(runnable, "VR:" + this.a.incrementAndGet() + "/1");
        }
    });

    private a() {
    }

    public static a a() {
        return a;
    }

    public final void a(String str, List<String> list, IHttpCallback iHttpCallback, boolean z, String str2, String str3) {
        final String str4 = str;
        final List<String> list2 = list;
        final IHttpCallback iHttpCallback2 = iHttpCallback;
        final boolean z2 = z;
        final String str5 = str2;
        final String str6 = str3;
        this.f498a.execute(new Runnable(this) {
            private /* synthetic */ a a;

            public final void run() {
                this.a.b(str4, list2, iHttpCallback2, z2, str5, str6);
            }
        });
    }

    public final void b(String str, List<String> list, IHttpCallback iHttpCallback, boolean z, String str2, String str3) {
        long j;
        long j2;
        if (this.f496a == IOpenApiCommandHolder.OAA_NO_LIMIT) {
            this.f496a = 0;
            j = 0;
        } else {
            j2 = this.f496a + 1;
            this.f496a = j2;
            j = j2;
        }
        com.gala.tvapi.log.a.a("id=" + j + "," + str2, "url = " + str);
        com.gala.tvapi.log.a.a("id=" + j + "," + str2, "post content = " + str3);
        if (list != null && list.size() > 0) {
            for (String str4 : list) {
                com.gala.tvapi.log.a.a("id=" + j + "," + str2, "header = " + str4);
            }
        }
        j2 = System.currentTimeMillis();
        try {
            String a = a(str, list, z, str3);
            com.gala.tvapi.log.a.a("id=" + j + ", " + str2 + ", response_time = " + (System.currentTimeMillis() - j2) + "ms", "data = " + a);
            iHttpCallback.onSuccess(a, this.f497a, null);
        } catch (Exception e) {
            com.gala.tvapi.log.a.a("id=" + j + "," + str2, "Exception = " + e.toString());
            iHttpCallback.onException(e, this.f497a, null, null);
        } catch (Error e2) {
            com.gala.tvapi.log.a.a("id=" + j + "," + str2, "Error = " + e2.getClass().toString());
            iHttpCallback.onException(new Exception(e2.fillInStackTrace()), this.f497a, null, null);
        }
    }

    private String a(String str, List<String> list, boolean z, String str2) throws Exception {
        HttpURLConnection httpURLConnection;
        Exception exception;
        Exception e;
        InputStream inputStream;
        String str3;
        Throwable th;
        InputStream inputStream2 = null;
        TVApiBase.getTVApiProperty();
        int i = 0;
        Exception exception2 = null;
        String str4 = null;
        while (true) {
            int i2;
            try {
                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
                try {
                    InputStream inputStream3;
                    httpURLConnection2.setConnectTimeout(10000);
                    httpURLConnection2.setReadTimeout(15000);
                    httpURLConnection2.setRequestProperty("Charset", "UTF-8");
                    httpURLConnection2.setRequestProperty(HTTP.CONNECTION, HTTP.CLOSE);
                    if (list != null && list.size() > 0) {
                        for (String split : list) {
                            String[] split2 = split.split(SOAP.DELIM);
                            if (split2 != null && split2.length >= 2) {
                                httpURLConnection2.addRequestProperty(split2[0], split2[1]);
                            }
                        }
                    }
                    if (z) {
                        httpURLConnection2.setRequestMethod(HTTP.POST);
                        if (!(str2 == null || str2.isEmpty())) {
                            httpURLConnection2.setDoOutput(true);
                            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection2.getOutputStream());
                            dataOutputStream.writeBytes(str2);
                            dataOutputStream.flush();
                            dataOutputStream.close();
                        }
                    } else {
                        httpURLConnection2.setRequestMethod(HTTP.GET);
                    }
                    int responseCode = httpURLConnection2.getResponseCode();
                    if (responseCode == 200) {
                        this.f497a = ErrorEvent.HTTP_CODE_SUCCESS;
                        inputStream3 = httpURLConnection2.getInputStream();
                        if (inputStream3 != null) {
                            try {
                                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream3, "UTF-8"));
                                StringBuilder stringBuilder = new StringBuilder();
                                while (true) {
                                    String readLine = bufferedReader.readLine();
                                    if (readLine == null) {
                                        break;
                                    }
                                    stringBuilder.append(readLine);
                                }
                                str4 = stringBuilder.toString();
                                bufferedReader.close();
                            } catch (Exception e2) {
                                httpURLConnection = httpURLConnection2;
                                exception = e2;
                                inputStream = inputStream3;
                                try {
                                    this.f497a = "-50";
                                    exception.printStackTrace();
                                    if (inputStream != null) {
                                        try {
                                            inputStream.close();
                                        } catch (IOException e3) {
                                            e3.printStackTrace();
                                        }
                                    }
                                    if (httpURLConnection != null) {
                                        e2 = exception;
                                        str3 = str4;
                                    } else {
                                        httpURLConnection.disconnect();
                                        e2 = exception;
                                        str3 = str4;
                                    }
                                    if (str3 == null) {
                                        return str3;
                                    }
                                    i2 = i + 1;
                                    if (i2 < 2) {
                                        throw e2;
                                    }
                                    i = i2;
                                    str4 = str3;
                                    exception2 = e2;
                                } catch (Throwable th2) {
                                    th = th2;
                                    inputStream2 = inputStream;
                                }
                            } catch (Throwable th3) {
                                inputStream2 = inputStream3;
                                httpURLConnection = httpURLConnection2;
                                th = th3;
                            }
                        }
                    } else if (responseCode == 204) {
                        this.f497a = "204";
                        str4 = "";
                        inputStream3 = null;
                    } else {
                        this.f497a = String.valueOf(responseCode);
                        exception2 = new Exception("http error" + this.f497a);
                        inputStream3 = null;
                    }
                    if (inputStream3 != null) {
                        try {
                            inputStream3.close();
                        } catch (IOException e32) {
                            e32.printStackTrace();
                        }
                    }
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                        e2 = exception2;
                        str3 = str4;
                    } else {
                        e2 = exception2;
                        str3 = str4;
                    }
                } catch (Exception e22) {
                    httpURLConnection = httpURLConnection2;
                    exception = e22;
                    inputStream = null;
                    this.f497a = "-50";
                    exception.printStackTrace();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection != null) {
                        httpURLConnection.disconnect();
                        e22 = exception;
                        str3 = str4;
                    } else {
                        e22 = exception;
                        str3 = str4;
                    }
                    if (str3 == null) {
                        return str3;
                    }
                    i2 = i + 1;
                    if (i2 < 2) {
                        i = i2;
                        str4 = str3;
                        exception2 = e22;
                    } else {
                        throw e22;
                    }
                } catch (Throwable th32) {
                    httpURLConnection = httpURLConnection2;
                    th = th32;
                }
            } catch (Exception e4) {
                exception = e4;
                inputStream = null;
                httpURLConnection = null;
                this.f497a = "-50";
                exception.printStackTrace();
                if (inputStream != null) {
                    inputStream.close();
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                    e22 = exception;
                    str3 = str4;
                } else {
                    e22 = exception;
                    str3 = str4;
                }
                if (str3 == null) {
                    return str3;
                }
                i2 = i + 1;
                if (i2 < 2) {
                    i = i2;
                    str4 = str3;
                    exception2 = e22;
                } else {
                    throw e22;
                }
            } catch (Throwable th4) {
                th = th4;
                httpURLConnection = null;
            }
            if (str3 == null) {
                return str3;
            }
            i2 = i + 1;
            if (i2 < 2) {
                throw e22;
            }
            i = i2;
            str4 = str3;
            exception2 = e22;
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        throw th;
        if (inputStream2 != null) {
            try {
                inputStream2.close();
            } catch (IOException e322) {
                e322.printStackTrace();
            }
        }
        if (httpURLConnection != null) {
            httpURLConnection.disconnect();
        }
        throw th;
    }
}
