package com.gala.tvapi.tv2.property;

import com.gala.tvapi.c.a;
import org.cybergarage.xml.XML;

public class ITVApiProperty {
    protected static String a(String str) {
        try {
            return a.a(str.getBytes(XML.CHARSET_UTF8)).toString().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
