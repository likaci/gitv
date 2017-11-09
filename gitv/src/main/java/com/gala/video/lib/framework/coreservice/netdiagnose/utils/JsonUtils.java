package com.gala.video.lib.framework.coreservice.netdiagnose.utils;

import android.util.Log;
import org.xbill.DNS.WKSRecord.Service;

public final class JsonUtils {
    public static String formatJson(String jsonStr) {
        if (jsonStr == null || "".equals(jsonStr)) {
            Log.v("JsonUtils", "jsonStr is null");
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char current = '\u0000';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            char last = current;
            current = jsonStr.charAt(i);
            switch (current) {
                case ',':
                    sb.append(current);
                    if (last == '\\') {
                        break;
                    }
                    sb.append('\n');
                    addIndentBlank(sb, indent);
                    break;
                case Service.MIT_DOV /*91*/:
                case Service.NTP /*123*/:
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                case Service.DCP /*93*/:
                case Service.LOCUS_MAP /*125*/:
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                default:
                    sb.append(current);
                    break;
            }
        }
        return sb.toString();
    }

    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }
}
