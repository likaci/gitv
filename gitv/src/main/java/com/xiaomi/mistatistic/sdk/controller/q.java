package com.xiaomi.mistatistic.sdk.controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import org.apache.http.NameValuePair;
import org.cybergarage.http.HTTP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public abstract class q {
    public static String a(Context context, String str, List list) {
        OutputStream outputStream;
        IOException e;
        BufferedReader bufferedReader;
        Throwable th;
        BufferedReader bufferedReader2 = null;
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("url");
        }
        try {
            HttpURLConnection a = a(context, new URL(str));
            a.setConnectTimeout(10000);
            a.setReadTimeout(15000);
            a.setRequestMethod(HTTP.POST);
            String a2 = a(list);
            if (a2 == null) {
                throw new IllegalArgumentException("nameValuePairs");
            }
            a.setDoOutput(true);
            byte[] bytes = a2.getBytes();
            outputStream = a.getOutputStream();
            try {
                outputStream.write(bytes, 0, bytes.length);
                outputStream.flush();
                outputStream.close();
                OutputStream outputStream2 = null;
                a.getResponseCode();
                BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(new r(a.getInputStream())));
                try {
                    String readLine;
                    StringBuffer stringBuffer = new StringBuffer();
                    String property = System.getProperty("line.separator");
                    for (readLine = bufferedReader3.readLine(); readLine != null; readLine = bufferedReader3.readLine()) {
                        stringBuffer.append(readLine);
                        stringBuffer.append(property);
                    }
                    readLine = stringBuffer.toString();
                    bufferedReader3.close();
                    bufferedReader3 = null;
                    if (null != null) {
                        try {
                            outputStream2.close();
                        } catch (IOException e2) {
                        }
                    }
                    if (null != null) {
                        bufferedReader3.close();
                    }
                    return readLine;
                } catch (IOException e3) {
                    e = e3;
                    bufferedReader = bufferedReader3;
                    outputStream = null;
                    bufferedReader2 = bufferedReader;
                    try {
                        throw e;
                    } catch (Throwable th2) {
                        th = th2;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    bufferedReader = bufferedReader3;
                    outputStream = null;
                    bufferedReader2 = bufferedReader;
                    if (outputStream != null) {
                        try {
                            outputStream.close();
                        } catch (IOException e4) {
                            throw th;
                        }
                    }
                    if (bufferedReader2 != null) {
                        bufferedReader2.close();
                    }
                    throw th;
                }
            } catch (IOException e5) {
                e = e5;
                throw e;
            } catch (Throwable th4) {
                th = th4;
                throw new IOException(th.getMessage());
            }
        } catch (IOException e6) {
            e = e6;
            outputStream = null;
            throw e;
        } catch (Throwable th5) {
            th = th5;
            outputStream = null;
            if (outputStream != null) {
                outputStream.close();
            }
            if (bufferedReader2 != null) {
                bufferedReader2.close();
            }
            throw th;
        }
    }

    public static String a(URL url) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(url.getProtocol()).append("://").append("10.0.0.172").append(url.getPath());
        if (!TextUtils.isEmpty(url.getQuery())) {
            stringBuilder.append("?").append(url.getQuery());
        }
        return stringBuilder.toString();
    }

    public static String a(List list) {
        StringBuffer stringBuffer = new StringBuffer();
        for (NameValuePair nameValuePair : list) {
            try {
                if (nameValuePair.getValue() != null) {
                    stringBuffer.append(URLEncoder.encode(nameValuePair.getName(), "UTF-8"));
                    stringBuffer.append(SearchCriteria.EQ);
                    stringBuffer.append(URLEncoder.encode(nameValuePair.getValue(), "UTF-8"));
                    stringBuffer.append("&");
                }
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }
        return (stringBuffer.length() > 0 ? stringBuffer.deleteCharAt(stringBuffer.length() - 1) : stringBuffer).toString();
    }

    public static HttpURLConnection a(Context context, URL url) {
        if (!"http".equals(url.getProtocol())) {
            return (HttpURLConnection) url.openConnection();
        }
        if (d(context)) {
            return (HttpURLConnection) url.openConnection(new Proxy(Type.HTTP, new InetSocketAddress("10.0.0.200", 80)));
        }
        if (!c(context)) {
            return (HttpURLConnection) url.openConnection();
        }
        HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(a(url)).openConnection();
        httpURLConnection.addRequestProperty("X-Online-Host", url.getHost());
        return httpURLConnection;
    }

    public static boolean a(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                return 1 == activeNetworkInfo.getType();
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static String b(Context context) {
        if (a(context)) {
            return "WIFI";
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return "";
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                return activeNetworkInfo == null ? "" : (activeNetworkInfo.getTypeName() + "-" + activeNetworkInfo.getSubtypeName() + "-" + activeNetworkInfo.getExtraInfo()).toLowerCase();
            } catch (Exception e) {
                return "";
            }
        } catch (Exception e2) {
            return "";
        }
    }

    public static boolean c(Context context) {
        if (!"CN".equalsIgnoreCase(((TelephonyManager) context.getSystemService("phone")).getSimCountryIso())) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3 || extraInfo.contains("ctwap")) ? false : extraInfo.regionMatches(true, extraInfo.length() - 3, "wap", 0, 3);
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }

    public static boolean d(Context context) {
        if (!"CN".equalsIgnoreCase(((TelephonyManager) context.getSystemService("phone")).getSimCountryIso())) {
            return false;
        }
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
            if (connectivityManager == null) {
                return false;
            }
            try {
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo == null) {
                    return false;
                }
                String extraInfo = activeNetworkInfo.getExtraInfo();
                return (TextUtils.isEmpty(extraInfo) || extraInfo.length() < 3) ? false : extraInfo.contains("ctwap");
            } catch (Exception e) {
                return false;
            }
        } catch (Exception e2) {
            return false;
        }
    }
}
