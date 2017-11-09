package com.gala.tvapi.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.gala.tvapi.log.a;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class TVImageTool {
    public Bitmap downloadImage(String strUrl) throws IOException {
        HttpURLConnection httpURLConnection;
        Exception exception;
        Throwable th;
        HttpURLConnection httpURLConnection2 = null;
        a.a("TVImageTool", "url=" + strUrl);
        InputStream inputStream;
        try {
            Bitmap decodeStream;
            Object obj;
            Object obj2;
            if (com.gala.tvapi.b.a.a(strUrl)) {
                obj = httpURLConnection2;
                obj2 = httpURLConnection2;
            } else {
                HttpURLConnection httpURLConnection3 = (HttpURLConnection) new URL(strUrl).openConnection();
                HttpURLConnection httpURLConnection4;
                try {
                    httpURLConnection3.setConnectTimeout(3000);
                    httpURLConnection3.setReadTimeout(5000);
                    httpURLConnection3.connect();
                    inputStream = httpURLConnection3.getInputStream();
                    if (inputStream != null) {
                        try {
                            httpURLConnection4 = httpURLConnection3;
                            decodeStream = BitmapFactory.decodeStream(inputStream);
                            httpURLConnection2 = httpURLConnection4;
                        } catch (Exception e) {
                            Exception exception2 = e;
                            httpURLConnection = httpURLConnection3;
                            exception = exception2;
                            try {
                                exception.printStackTrace();
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (httpURLConnection != null) {
                                    return httpURLConnection2;
                                }
                                httpURLConnection.disconnect();
                                return httpURLConnection2;
                            } catch (Throwable th2) {
                                th = th2;
                                httpURLConnection2 = httpURLConnection;
                                if (inputStream != null) {
                                    inputStream.close();
                                }
                                if (httpURLConnection2 != null) {
                                    httpURLConnection2.disconnect();
                                }
                                throw th;
                            }
                        } catch (Throwable th3) {
                            Throwable th4 = th3;
                            httpURLConnection2 = httpURLConnection3;
                            th = th4;
                            if (inputStream != null) {
                                inputStream.close();
                            }
                            if (httpURLConnection2 != null) {
                                httpURLConnection2.disconnect();
                            }
                            throw th;
                        }
                    }
                    httpURLConnection4 = httpURLConnection3;
                    obj2 = httpURLConnection2;
                    httpURLConnection2 = httpURLConnection4;
                } catch (Exception e2) {
                    obj = httpURLConnection2;
                    httpURLConnection4 = httpURLConnection3;
                    exception = e2;
                    httpURLConnection = httpURLConnection4;
                    exception.printStackTrace();
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection != null) {
                        return httpURLConnection2;
                    }
                    httpURLConnection.disconnect();
                    return httpURLConnection2;
                } catch (Throwable th5) {
                    obj = httpURLConnection2;
                    httpURLConnection2 = httpURLConnection3;
                    th = th5;
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (httpURLConnection2 != null) {
                        httpURLConnection2.disconnect();
                    }
                    throw th;
                }
            }
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection2 == null) {
                return decodeStream;
            }
            httpURLConnection2.disconnect();
            return decodeStream;
        } catch (Exception e3) {
            exception = e3;
            httpURLConnection = httpURLConnection2;
            inputStream = httpURLConnection2;
            exception.printStackTrace();
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection != null) {
                return httpURLConnection2;
            }
            httpURLConnection.disconnect();
            return httpURLConnection2;
        } catch (Throwable th6) {
            th = th6;
            inputStream = httpURLConnection2;
            if (inputStream != null) {
                inputStream.close();
            }
            if (httpURLConnection2 != null) {
                httpURLConnection2.disconnect();
            }
            throw th;
        }
    }
}
