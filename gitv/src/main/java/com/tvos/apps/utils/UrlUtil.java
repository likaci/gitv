package com.tvos.apps.utils;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.gala.video.lib.share.common.configs.WebConstants;
import java.util.Map;
import java.util.regex.Pattern;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class UrlUtil {
    private static final String TAG = "UrlUtil";

    public static boolean isUrlValid(String url) {
        if (!TextUtils.isEmpty(url)) {
            return Pattern.compile("^(https?|ftp|file|pps)://.*").matcher(url).matches();
        }
        Log.d(TAG, "isUrlValid, url is empty");
        return false;
    }

    public static String createUrlParam(Map<String, String> params) {
        if (params == null || params.size() == 0) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (String key : params.keySet()) {
            sb.append(key).append(SearchCriteria.EQ).append((String) params.get(key)).append("&");
        }
        sb.deleteCharAt(sb.length() - 1).toString();
        return sb.toString();
    }

    public static String combineMediaUrl(String url, int imageWidth, int imageHeight) {
        try {
            StringBuilder urlBuilder = new StringBuilder();
            int preIndex = url.lastIndexOf("_");
            int proIndex = url.lastIndexOf(".");
            if (!(preIndex == -1 || proIndex == -1 || url.substring(preIndex + 1, proIndex).split(WebConstants.PARAM_KEY_X).length <= 1)) {
                urlBuilder.append(url.substring(0, preIndex + 1));
                urlBuilder.append(new StringBuilder(String.valueOf(imageWidth)).append("_").append(imageHeight).toString());
                urlBuilder.append(url.substring(proIndex));
                url = urlBuilder.toString();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String encodeUrl(String url) {
        int i;
        StringBuilder urlBuilder = new StringBuilder();
        StringBuilder paramBuilder = new StringBuilder();
        String[] urlArray = url.split("\\?");
        if (urlArray.length > 1) {
            paramBuilder.append("?");
            String[] paramArray = urlArray[1].split("&");
            if (paramArray.length > 0) {
                for (i = 0; i < paramArray.length; i++) {
                    String[] array = paramArray[i].split(SearchCriteria.EQ);
                    if (array.length > 1) {
                        String keyStr = Uri.encode(array[0]);
                        String valueStr = Uri.encode(array[1]);
                        paramBuilder.append(keyStr);
                        paramBuilder.append(SearchCriteria.EQ);
                        paramBuilder.append(valueStr);
                    }
                    if (i != paramArray.length - 1) {
                        paramBuilder.append("&");
                    }
                }
            }
        }
        String[] prefixArray = urlArray[0].split("/");
        if (prefixArray.length > 1) {
            urlBuilder.append(prefixArray[0]);
            urlBuilder.append("/");
            for (i = 1; i < prefixArray.length; i++) {
                if (prefixArray[i].contains(SOAP.DELIM)) {
                    String[] colobArray = prefixArray[i].split(SOAP.DELIM);
                    StringBuilder colobBuilder = new StringBuilder();
                    colobBuilder.append(Uri.encode(colobArray[0]));
                    if (colobArray.length > 1) {
                        for (int j = 1; j < colobArray.length; j++) {
                            colobBuilder.append(SOAP.DELIM);
                            colobBuilder.append(Uri.encode(colobArray[j]));
                        }
                    }
                    urlBuilder.append(colobBuilder.toString());
                } else {
                    urlBuilder.append(Uri.encode(prefixArray[i]));
                }
                if (i != prefixArray.length - 1) {
                    urlBuilder.append("/");
                }
            }
        }
        urlBuilder.append(paramBuilder.toString());
        if (urlBuilder.toString().equals("")) {
            return url;
        }
        return urlBuilder.toString();
    }
}
