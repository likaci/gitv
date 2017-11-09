package com.gala.tvapi.tv2.property;

import com.gala.tvapi.p023c.C0253a;
import org.cybergarage.xml.XML;

public class ITVApiProperty {
    protected static String m702a(String str) {
        try {
            return C0253a.m614a(str.getBytes(XML.CHARSET_UTF8)).toString().trim();
        } catch (Exception e) {
            return "";
        }
    }
}
