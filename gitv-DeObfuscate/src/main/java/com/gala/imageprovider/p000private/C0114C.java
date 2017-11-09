package com.gala.imageprovider.p000private;

import android.net.Uri;
import com.gala.download.base.FileRequest;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants;
import com.gala.video.lib.share.common.widget.QToast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;

public class C0114C implements Serializable, Runnable {
    private int f518a = 0;
    public FileRequest f519a;
    private final String f520a = ("DownLoader/HttpTask@" + Integer.toHexString(hashCode()));
    private int f521b = 0;
    private int f522c = 0;
    private int f523d;

    C0114C(FileRequest fileRequest) {
        this.f519a = fileRequest;
        this.f521b = 8000;
        this.f522c = QToast.LENGTH_4000;
        this.f523d = fileRequest.getLimitSize();
    }

    protected final FileRequest m266a() {
        return this.f519a;
    }

    public void run() {
        Throwable e;
        Object obj = 1;
        Object obj2 = null;
        if (FileRequest.checkRequestValid(this.f519a)) {
            C0142q.m363a().m366a((Runnable) this);
            try {
                Object obj3;
                FileRequest fileRequest = this.f519a;
                String a = C0138o.m350a().m352a(fileRequest);
                if (a == null || !new File(a).exists()) {
                    obj3 = null;
                } else {
                    mo632a(a);
                    if (C0123G.f541a) {
                        C0123G.m279a("ImageProvider/HttpTask", ">>>>> loadFile success(exists), 【savePath】-" + a + ", 【url】-" + fileRequest.getUrl());
                    }
                    obj3 = 1;
                }
                if (obj3 == null) {
                    try {
                        a = this.f519a.getUrl();
                        this.f519a.getShouldBeKilled();
                        StringBuilder stringBuilder = new StringBuilder(a);
                        if (Uri.parse(a) != null) {
                            C0126b.m307a(Uri.parse(a).getQuery());
                        }
                        HttpUriRequest httpGet = new HttpGet(stringBuilder.toString());
                        HttpClient defaultHttpClient = new DefaultHttpClient();
                        defaultHttpClient.getParams().setParameter("http.connection.timeout", Integer.valueOf(this.f522c));
                        defaultHttpClient.getParams().setParameter("http.socket.timeout", Integer.valueOf(this.f521b));
                        HttpResponse execute = defaultHttpClient.execute(httpGet);
                        int statusCode = execute.getStatusLine().getStatusCode();
                        if (statusCode != 200) {
                            C0123G.m282b("ImageProvider/HttpTask", ">>>>> 【HttpCode】-" + statusCode + ", 【url】-" + a);
                        }
                        if (statusCode == 200) {
                            byte[] a2 = m265a(execute);
                            if (a2 == null) {
                                C0123G.m282b("ImageProvider/HttpTask", ">>>>> http request return null bytes , 【url】-" + a);
                            } else if (this.f523d > 0 && a2.length / 1024 > this.f523d) {
                                C0123G.m282b("ImageProvider/HttpTask", ">>>>> file size is out of limit size --- " + a);
                            } else if (a2.length != 0) {
                                String a3 = C0138o.m350a().m353a(this.f519a, a2);
                                if (a3 != null) {
                                    mo632a(a3);
                                    if (C0123G.f541a) {
                                        C0123G.m279a("ImageProvider/HttpTask", ">>>>> loadFile success, 【savePath】-" + a3 + ", 【url】-" + a);
                                    }
                                } else {
                                    obj = null;
                                }
                                obj2 = obj;
                            }
                        }
                        defaultHttpClient.getConnectionManager().shutdown();
                    } catch (Exception e2) {
                        e = e2;
                        obj2 = obj3;
                        C0123G.m280a(this.f520a, ">>>>> exception happend:", e);
                        C0142q.m363a().m367b(this);
                        if (obj2 != null) {
                            if (this.f518a < 2) {
                                C0123G.m279a(this.f520a, ">>>>>callRetry: limit reached, failed url=" + this.f519a.getUrl());
                                mo631a(new C0153y("image request fail!"));
                                return;
                            }
                            this.f518a++;
                            switch (this.f518a) {
                                case 1:
                                    this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                                    this.f521b = 15000;
                                    break;
                                case 2:
                                    this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                                    this.f521b = 30000;
                                    break;
                            }
                            C0123G.m279a(this.f520a, ">>>>>onRetryDownload: url=" + this.f519a.getUrl());
                            C0142q.m363a().m365a(this);
                            return;
                        }
                        return;
                    } catch (AssertionError e3) {
                        e = e3;
                        obj2 = obj3;
                        C0123G.m280a(this.f520a, ">>>>> assertion error:", e);
                        C0142q.m363a().m367b(this);
                        if (obj2 != null) {
                            return;
                        }
                        if (this.f518a < 2) {
                            this.f518a++;
                            switch (this.f518a) {
                                case 1:
                                    this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                                    this.f521b = 15000;
                                    break;
                                case 2:
                                    this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                                    this.f521b = 30000;
                                    break;
                            }
                            C0123G.m279a(this.f520a, ">>>>>onRetryDownload: url=" + this.f519a.getUrl());
                            C0142q.m363a().m365a(this);
                            return;
                        }
                        C0123G.m279a(this.f520a, ">>>>>callRetry: limit reached, failed url=" + this.f519a.getUrl());
                        mo631a(new C0153y("image request fail!"));
                        return;
                    }
                }
                obj2 = obj3;
            } catch (Exception e4) {
                e = e4;
                C0123G.m280a(this.f520a, ">>>>> exception happend:", e);
                C0142q.m363a().m367b(this);
                if (obj2 != null) {
                    if (this.f518a < 2) {
                        C0123G.m279a(this.f520a, ">>>>>callRetry: limit reached, failed url=" + this.f519a.getUrl());
                        mo631a(new C0153y("image request fail!"));
                        return;
                    }
                    this.f518a++;
                    switch (this.f518a) {
                        case 1:
                            this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                            this.f521b = 15000;
                            break;
                        case 2:
                            this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                            this.f521b = 30000;
                            break;
                    }
                    C0123G.m279a(this.f520a, ">>>>>onRetryDownload: url=" + this.f519a.getUrl());
                    C0142q.m363a().m365a(this);
                    return;
                }
                return;
            } catch (AssertionError e5) {
                e = e5;
                C0123G.m280a(this.f520a, ">>>>> assertion error:", e);
                C0142q.m363a().m367b(this);
                if (obj2 != null) {
                    return;
                }
                if (this.f518a < 2) {
                    this.f518a++;
                    switch (this.f518a) {
                        case 1:
                            this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                            this.f521b = 15000;
                            break;
                        case 2:
                            this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                            this.f521b = 30000;
                            break;
                    }
                    C0123G.m279a(this.f520a, ">>>>>onRetryDownload: url=" + this.f519a.getUrl());
                    C0142q.m363a().m365a(this);
                    return;
                }
                C0123G.m279a(this.f520a, ">>>>>callRetry: limit reached, failed url=" + this.f519a.getUrl());
                mo631a(new C0153y("image request fail!"));
                return;
            }
            C0142q.m363a().m367b(this);
            if (obj2 != null) {
                return;
            }
            if (this.f518a < 2) {
                this.f518a++;
                switch (this.f518a) {
                    case 1:
                        this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                        this.f521b = 15000;
                        break;
                    case 2:
                        this.f522c = ScreenSaverConstants.ELAPSE_STATIC;
                        this.f521b = 30000;
                        break;
                }
                C0123G.m279a(this.f520a, ">>>>>onRetryDownload: url=" + this.f519a.getUrl());
                C0142q.m363a().m365a(this);
                return;
            }
            C0123G.m279a(this.f520a, ">>>>>callRetry: limit reached, failed url=" + this.f519a.getUrl());
            mo631a(new C0153y("image request fail!"));
            return;
        }
        mo631a(new C0153y("Params is wrong!"));
    }

    private byte[] m265a(HttpResponse httpResponse) throws IOException {
        BufferedInputStream bufferedInputStream;
        Throwable e;
        HttpEntity entity = httpResponse.getEntity();
        try {
            bufferedInputStream = new BufferedInputStream(entity.getContent());
            try {
                byte[] bArr = new byte[((int) entity.getContentLength())];
                int i = 0;
                int length = bArr.length;
                while (true) {
                    int read = bufferedInputStream.read(bArr, i, length - i);
                    if (read > 0) {
                        i += read;
                    } else {
                        try {
                            bufferedInputStream.close();
                            return bArr;
                        } catch (Exception e2) {
                            return bArr;
                        }
                    }
                }
            } catch (Exception e3) {
                e = e3;
                try {
                    C0123G.m280a(this.f520a, ">>>>>readStream exception:", e);
                    if (bufferedInputStream != null) {
                        return null;
                    }
                    try {
                        bufferedInputStream.close();
                        return null;
                    } catch (Exception e4) {
                        return null;
                    }
                } catch (Throwable th) {
                    e = th;
                    if (bufferedInputStream != null) {
                        try {
                            bufferedInputStream.close();
                        } catch (Exception e5) {
                        }
                    }
                    throw e;
                }
            }
        } catch (Exception e6) {
            e = e6;
            bufferedInputStream = null;
            C0123G.m280a(this.f520a, ">>>>>readStream exception:", e);
            if (bufferedInputStream != null) {
                return null;
            }
            bufferedInputStream.close();
            return null;
        } catch (Throwable th2) {
            e = th2;
            bufferedInputStream = null;
            if (bufferedInputStream != null) {
                bufferedInputStream.close();
            }
            throw e;
        }
    }

    public void mo631a(Exception exception) {
    }

    public void mo632a(String str) {
    }
}
