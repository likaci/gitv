package com.tvos.apps.utils.sys;

import android.text.TextUtils;
import android.util.Log;
import com.tvos.apps.utils.MD5Utils;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;
import org.cybergarage.xml.XML;

public class NetHelper {
    public static final String TAG = NetHelper.class.getSimpleName();

    public static String getSign(String secureKey, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            if (!params.isEmpty()) {
                for (Entry<String, String> param : new TreeMap(params).entrySet()) {
                    if (!TextUtils.isEmpty((CharSequence) param.getValue())) {
                        sb.append((String) param.getKey());
                        sb.append(SearchCriteria.EQ);
                        sb.append(URLEncoder.encode((String) param.getValue(), XML.CHARSET_UTF8));
                        sb.append("&");
                    }
                }
                sb.setLength(sb.length() - 1);
            }
            sb.append(secureKey);
            Log.d(TAG, "getSign, string is = " + sb);
            return MD5Utils.MD5(sb.toString());
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSignNVKey(String secureKey, Map<String, String> params) {
        try {
            StringBuilder sb = new StringBuilder();
            if (!params.isEmpty()) {
                for (Entry<String, String> param : new TreeMap(params).entrySet()) {
                    Log.d(TAG, "getSignNVKey, key = " + ((String) param.getKey()) + " , value = " + URLEncoder.encode((String) param.getValue(), XML.CHARSET_UTF8));
                    sb.append((String) param.getKey());
                    sb.append(SearchCriteria.EQ);
                    sb.append(URLEncoder.encode((String) param.getValue(), XML.CHARSET_UTF8));
                }
            }
            sb.append(secureKey);
            Log.d(TAG, "getSignNVKey, string is = " + sb);
            return MD5Utils.MD5(sb.toString());
        } catch (Exception e) {
            return "";
        }
    }

    public static String getSign(String secureKey, Object... params) {
        try {
            StringBuilder sb = new StringBuilder();
            if (params.length > 0) {
                TreeMap<String, String> paramsMap = new TreeMap();
                for (int i = 0; i < params.length; i += 2) {
                    paramsMap.put(params[i].toString(), params[i + 1].toString());
                }
                for (Entry<String, String> param : paramsMap.entrySet()) {
                    sb.append((String) param.getKey());
                    sb.append(SearchCriteria.EQ);
                    sb.append(URLEncoder.encode((String) param.getValue(), XML.CHARSET_UTF8));
                    sb.append("&");
                }
                sb.setLength(sb.length() - 1);
            }
            sb.append(secureKey);
            return MD5Utils.MD5(sb.toString());
        } catch (Exception e) {
            return "";
        }
    }
}
