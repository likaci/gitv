package com.gala.tvapi;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.video.api.IApiUrlBuilder;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public abstract class C0212a implements IApiUrlBuilder {
    static {
        TVApiTool tVApiTool = new TVApiTool();
    }

    protected static String m573a(String str, String... strArr) {
        if (strArr == null || strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = C0212a.m572a(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr);
    }

    protected static String m572a(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                C0262a.m634b("URL Encode", e);
            }
        }
        return "";
    }
}
