package com.gala.tvapi.vrs;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.api.IApiUrlBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.cybergarage.xml.XML;

public class BaseHelper {
    protected static TVApiTool a = new TVApiTool();

    public static final class a implements IApiUrlBuilder {
        private String a = null;

        public a(String str) {
            this.a = str;
        }

        public final String build(String... params) {
            int i = 0;
            if (this.a.contains("m=%s") || this.a.contains("deviceId=%s")) {
                if (params == null || params.length <= 0) {
                    return BaseHelper.b(this.a, TVApiBase.getTVApiProperty().getAuthId());
                }
                String[] strArr = new String[(params.length + 1)];
                while (i < params.length) {
                    strArr[i] = params[i];
                    i++;
                }
                strArr[params.length] = TVApiBase.getTVApiProperty().getAuthId();
                return BaseHelper.b(this.a, strArr);
            } else if (params != null && params.length > 0) {
                return BaseHelper.b(this.a, params);
            } else {
                TVApiTool tVApiTool = BaseHelper.a;
                return TVApiTool.parseLicenceUrl(this.a);
            }
        }

        public final List<String> header() {
            return null;
        }
    }

    protected static String a(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                com.gala.tvapi.log.a.b("URL Encode", e);
            }
        }
        return "";
    }

    protected static String b(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = a(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr);
    }
}
