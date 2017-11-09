package com.gala.tvapi.vrs.b;

import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.a;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public abstract class i implements a {
    static {
        TVApiTool tVApiTool = new TVApiTool();
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

    protected static String a(String str, String... strArr) {
        if (strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = a(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr);
    }
}
