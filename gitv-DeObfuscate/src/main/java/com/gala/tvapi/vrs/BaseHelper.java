package com.gala.tvapi.vrs;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.api.IApiUrlBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import org.cybergarage.xml.XML;

public class BaseHelper {
    protected static TVApiTool f1195a = new TVApiTool();

    public static final class C0328a implements IApiUrlBuilder {
        private String f1197a = null;

        public C0328a(String str) {
            this.f1197a = str;
        }

        public final String build(String... params) {
            int i = 0;
            if (this.f1197a.contains("m=%s") || this.f1197a.contains("deviceId=%s")) {
                if (params == null || params.length <= 0) {
                    return BaseHelper.m757b(this.f1197a, TVApiBase.getTVApiProperty().getAuthId());
                }
                String[] strArr = new String[(params.length + 1)];
                while (i < params.length) {
                    strArr[i] = params[i];
                    i++;
                }
                strArr[params.length] = TVApiBase.getTVApiProperty().getAuthId();
                return BaseHelper.m757b(this.f1197a, strArr);
            } else if (params != null && params.length > 0) {
                return BaseHelper.m757b(this.f1197a, params);
            } else {
                TVApiTool tVApiTool = BaseHelper.f1195a;
                return TVApiTool.parseLicenceUrl(this.f1197a);
            }
        }

        public final List<String> header() {
            return null;
        }
    }

    protected static String m756a(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                C0262a.m634b("URL Encode", e);
            }
        }
        return "";
    }

    protected static String m757b(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = m756a(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr);
    }
}
