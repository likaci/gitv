package com.gala.video.lib.framework.core.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UrlUtils {
    private static final String CHAR_SET = "utf-8";

    public static String urlFormat(String url, Object... params) {
        if (params == null) {
            return url;
        }
        for (int i = 0; i < params.length; i++) {
            params[i] = urlEncode((String) params[i]);
        }
        return String.format(url, params);
    }

    public static String urlEncode(String param) {
        if (!StringUtils.isEmpty((CharSequence) param)) {
            try {
                return URLEncoder.encode(param, "utf-8");
            } catch (UnsupportedEncodingException e) {
                LogUtils.m1571e("urlEncode", e.getMessage());
            }
        }
        return "";
    }
}
