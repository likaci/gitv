package com.push.pushservice.utils;

import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.http.NameValuePair;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class SecretKeyUtils {
    public static String sign(Map<String, String> params, String secretKey) {
        if (params == null || params.size() == 0 || TextUtils.isEmpty(secretKey)) {
            return null;
        }
        SortedMap<String, String> sortedParams = new TreeMap(params);
        sortedParams.remove("sign");
        StringBuilder sb = new StringBuilder();
        for (String key : sortedParams.keySet()) {
            String val = (String) sortedParams.get(key);
            if (TextUtils.isEmpty(val)) {
                val = "";
            }
            sb.append(key).append(SearchCriteria.EQ).append(val).append("|");
        }
        return PushUtils.encodeMD5(sb.append(secretKey).toString());
    }

    public static String sign(List<NameValuePair> params, String secretKey) {
        if (params == null || params.size() == 0 || TextUtils.isEmpty(secretKey)) {
            return null;
        }
        Map paramMap = new HashMap();
        for (NameValuePair item : params) {
            paramMap.put(item.getName(), item.getValue());
        }
        return sign(paramMap, secretKey);
    }
}
