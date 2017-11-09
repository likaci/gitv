package com.gala.tvapi.vrs.p032b;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.tools.TVApiTool;
import com.gala.tvapi.vrs.C0354a;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public abstract class C0355i implements C0354a {
    static {
        TVApiTool tVApiTool = new TVApiTool();
    }

    protected static String m780a(String str) {
        if (!(str == null || str.isEmpty())) {
            try {
                return URLEncoder.encode(str, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                C0262a.m634b("URL Encode", e);
            }
        }
        return "";
    }

    protected static String m781a(String str, String... strArr) {
        if (strArr.length <= 0) {
            return TVApiTool.parseLicenceUrl(str);
        }
        for (int i = 0; i < strArr.length; i++) {
            strArr[i] = C0355i.m780a(strArr[i]);
        }
        return String.format(TVApiTool.parseLicenceUrl(str), (Object[]) strArr);
    }
}
