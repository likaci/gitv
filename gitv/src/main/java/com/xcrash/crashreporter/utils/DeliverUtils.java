package com.xcrash.crashreporter.utils;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.xcrash.crashreporter.CrashReporter;
import com.xcrash.crashreporter.anno.HideAnnotation;
import com.xcrash.crashreporter.anno.MessageAnnotation;
import com.xcrash.crashreporter.generic.CrashReportParams;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.zip.GZIPOutputStream;
import org.cybergarage.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class DeliverUtils {
    public static final String TAG = "DeliverUtils";

    public static String constructUrl(Context context, Object object) {
        if (object == null) {
            return null;
        }
        String requestUrl = null;
        boolean isEncode = true;
        String name = "";
        if (object.getClass().isAnnotationPresent(MessageAnnotation.class)) {
            MessageAnnotation anno = (MessageAnnotation) object.getClass().getAnnotation(MessageAnnotation.class);
            if (anno != null) {
                isEncode = anno.isEncode();
                requestUrl = anno.requestUrl();
                name = anno.name();
            }
        }
        HashMap<String, String> hashmap = new HashMap();
        Field[] field = object.getClass().getDeclaredFields();
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < field.length; i++) {
            try {
                field[i].setAccessible(true);
                if (!field[i].isAnnotationPresent(HideAnnotation.class)) {
                    String value = (String) field[i].get(object);
                    if (value == null) {
                        value = "";
                    } else {
                        sb.append(" , " + field[i].getName() + " = " + value);
                    }
                    if (isEncode) {
                        if ("d".equals(field[i].getName())) {
                            value = encodingUTF8(value);
                        } else {
                            value = encoding(value);
                        }
                    }
                    hashmap.put(field[i].getName(), value);
                }
            } catch (Exception e) {
                Log.i(TAG, "构建url异常 = " + e.getMessage());
                e.printStackTrace();
            }
        }
        sb.append(AlbumEnterFactory.SIGN_STR);
        Log.d(TAG, sb.toString());
        if (name.equals("dragon_qos")) {
            addDragonPublicParams(context, hashmap);
        } else if (name.equals("mirror_qos")) {
            addMirrorPublicParams(context, hashmap);
        }
        return hashmapToUrl(requestUrl, hashmap);
    }

    public static void addDragonPublicParams(Context context, HashMap<String, String> hashmap) {
        if (hashmap != null && context != null) {
            DebugLog.log(TAG, "addDragonPublicParams");
            CrashReportParams commonParams = CrashReporter.getInstance().getReportParams();
            String pf = commonParams.getPf() == null ? "" : commonParams.getPf();
            String p = commonParams.getP() == null ? "" : commonParams.getP();
            String p1 = commonParams.getP1() == null ? "" : commonParams.getP1();
            String pu = commonParams.getPu() == null ? "" : commonParams.getPu();
            String mkey = commonParams.getMkey() == null ? "" : commonParams.getMkey();
            String v = commonParams.getV() == null ? Utility.getVersionName(context) : commonParams.getV();
            String u = commonParams.getU() == null ? Utility.getIMEI(context) : commonParams.getU();
            String qyid = commonParams.getQyid();
            String aqyid = commonParams.getAqyid();
            String qyidv2 = commonParams.getQyidv2();
            hashmap.put("pf", pf);
            hashmap.put("p", p);
            hashmap.put("p1", p1);
            hashmap.put("u", u);
            hashmap.put("pu", pu);
            hashmap.put("mkey", mkey);
            hashmap.put("v", v);
            hashmap.put("os", Utility.getOSVersionInfo());
            hashmap.put("brand", encoding(Build.BRAND));
            hashmap.put("ua", encoding(Utility.getMobileModel()));
            if (Utility.isTvGuo()) {
                hashmap.put("net", "1");
            } else {
                hashmap.put("net", NetworkUtil.getNetWorkType(context));
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("qyid", qyid);
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("aqyid", aqyid);
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("qyidv2", qyidv2);
            }
        }
    }

    public static void addMirrorPublicParams(Context context, HashMap<String, String> hashmap) {
        if (hashmap != null && context != null) {
            DebugLog.log(TAG, "addMirrorPublicParams");
            CrashReportParams commonParams = CrashReporter.getInstance().getReportParams();
            String pf = commonParams.getPf() == null ? "" : commonParams.getPf();
            String p = commonParams.getP() == null ? "" : commonParams.getP();
            String p1 = commonParams.getP1() == null ? "" : commonParams.getP1();
            String pu = commonParams.getPu() == null ? "" : commonParams.getPu();
            String mkey = commonParams.getMkey() == null ? "" : commonParams.getMkey();
            String v = commonParams.getV() == null ? Utility.getVersionName(context) : commonParams.getV();
            String u = commonParams.getU() == null ? Utility.getIMEI(context) : commonParams.getU();
            String pNew = pf + "_" + p + "_" + p1;
            String qyid = commonParams.getQyid();
            String aqyid = commonParams.getAqyid();
            String qyidv2 = commonParams.getQyidv2();
            hashmap.put("p1", pNew);
            hashmap.put("u", u);
            hashmap.put("pu", pu);
            hashmap.put("mkey", mkey);
            hashmap.put("v", v);
            hashmap.put("brand", encoding(Build.BRAND));
            hashmap.put("os", Utility.getOSVersionInfo());
            hashmap.put("ua_model", encoding(Utility.getMobileModel()));
            if (Utility.isTvGuo()) {
                hashmap.put("net_work", "1");
            } else {
                hashmap.put("net_work", NetworkUtil.getNetWorkType(context));
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("qyid", qyid);
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("aqyid", aqyid);
            }
            if (!TextUtils.isEmpty(qyid)) {
                hashmap.put("qyidv2", qyidv2);
            }
        }
    }

    public static void addMirrorPublicParamsToBody(Context context, JSONObject json, Object object) {
        if (json != null && context != null) {
            Field[] field = object.getClass().getDeclaredFields();
            boolean isEncode = true;
            if (object.getClass().isAnnotationPresent(MessageAnnotation.class)) {
                MessageAnnotation anno = (MessageAnnotation) object.getClass().getAnnotation(MessageAnnotation.class);
                if (anno != null) {
                    isEncode = anno.isEncode();
                }
            }
            for (int i = 0; i < field.length; i++) {
                try {
                    field[i].setAccessible(true);
                    if (!field[i].isAnnotationPresent(HideAnnotation.class)) {
                        String value = (String) field[i].get(object);
                        if (value == null) {
                            value = "";
                        }
                        if (isEncode) {
                            if ("d".equals(field[i].getName())) {
                                value = encodingUTF8(value);
                            } else {
                                value = encoding(value);
                            }
                        }
                        json.put(field[i].getName(), value);
                    }
                } catch (Exception e) {
                    Log.i(TAG, "构建url异常 = " + e.getMessage());
                    e.printStackTrace();
                }
            }
            try {
                CrashReportParams commonParams = CrashReporter.getInstance().getReportParams();
                String pf = commonParams.getPf() == null ? "" : commonParams.getPf();
                String p = commonParams.getP() == null ? "" : commonParams.getP();
                String p1 = commonParams.getP1() == null ? "" : commonParams.getP1();
                String pu = commonParams.getPu() == null ? "" : commonParams.getPu();
                String mkey = commonParams.getMkey() == null ? "" : commonParams.getMkey();
                String v = commonParams.getV() == null ? Utility.getVersionName(context) : commonParams.getV();
                String u = commonParams.getU() == null ? Utility.getIMEI(context) : commonParams.getU();
                String pNew = pf + "_" + p + "_" + p1;
                String qyid = commonParams.getQyid();
                String aqyid = commonParams.getAqyid();
                String qyidv2 = commonParams.getQyidv2();
                json.put("p1", pNew);
                json.put("u", u);
                json.put("pu", pu);
                json.put("mkey", mkey);
                json.put("v", v);
                json.put("brand", encoding(Build.BRAND));
                json.put("os", Utility.getOSVersionInfo());
                json.put("ua_model", encoding(Utility.getMobileModel()));
                json.put("net_work", NetworkUtil.getNetWorkType(context));
                if (!TextUtils.isEmpty(qyid)) {
                    json.put("qyid", qyid);
                }
                if (!TextUtils.isEmpty(qyid)) {
                    json.put("aqyid", aqyid);
                }
                if (!TextUtils.isEmpty(qyid)) {
                    json.put("qyidv2", qyidv2);
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
    }

    public static String hashmapToUrl(String url, HashMap<String, String> map) {
        if (map == null) {
            return url;
        }
        StringBuilder sb = new StringBuilder(url);
        if (url.contains("?")) {
            sb.append('&');
        } else {
            sb.append('?');
        }
        for (Entry<String, String> entry : map.entrySet()) {
            String value;
            String key = ((String) entry.getKey()).toString();
            if (TextUtils.isEmpty((CharSequence) entry.getValue())) {
                value = "";
            } else {
                value = ((String) entry.getValue()).toString();
            }
            sb.append(key).append('=').append(value).append('&');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    public static String encodingUTF8(String str) {
        try {
            return isEmpty(str) ? "" : URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str) || "null".equals(str)) {
            return true;
        }
        if (str.length() > 4) {
            return false;
        }
        return str.equalsIgnoreCase("null");
    }

    public static String encoding(String str) {
        return isEmpty(str) ? "" : URLEncoder.encode(str);
    }

    public static boolean postWithGzip(JSONObject json, String urlStr) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod(HTTP.POST);
            conn.setDoOutput(true);
            conn.setRequestProperty(HTTP.CONTENT_TYPE, "application/octet-stream");
            conn.setRequestProperty("Content-Encoding", "gzip");
            OutputStream os = new GZIPOutputStream(conn.getOutputStream());
            os.write(json.toString().getBytes("UTF-8"));
            os.flush();
            os.close();
            if (conn.getResponseCode() == 200) {
                DebugLog.log(TAG, "send report:success");
                return true;
            }
            DebugLog.log(TAG, "send report:fail ", Integer.valueOf(conn.getResponseCode()));
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
