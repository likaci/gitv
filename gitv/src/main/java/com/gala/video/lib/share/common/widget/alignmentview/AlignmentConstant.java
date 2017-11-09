package com.gala.video.lib.share.common.widget.alignmentview;

import android.graphics.Paint;
import com.alibaba.fastjson.asm.Opcodes;
import java.lang.Character.UnicodeBlock;
import org.xbill.DNS.WKSRecord.Service;

public class AlignmentConstant {
    public static String replaceBreakLineToSpace(String str) {
        return str.replaceAll("\n", " ");
    }

    public static boolean isLetterOfEnglish(char c) {
        char count = c;
        if (count >= 'A' && count <= 'Z') {
            return true;
        }
        if (count >= 'a' && count <= 'z') {
            return true;
        }
        if (count < '0' || count > '9') {
            return false;
        }
        return true;
    }

    public static boolean isHalfPunctuation(char c) {
        char count = c;
        if (count >= '!' && count <= '/') {
            return true;
        }
        if (count >= ':' && count <= '@') {
            return true;
        }
        if (count >= '[' && count <= '`') {
            return true;
        }
        if (count < Service.NTP || count > Opcodes.IAND) {
            return false;
        }
        return true;
    }

    public static boolean isPunctuation(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        try {
            if (ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
                return true;
            }
            return false;
        } catch (NoSuchFieldError e) {
            e.printStackTrace();
            return false;
        }
    }

    public static int getWidthofString(String str, Paint paint) {
        if (str == null || str.equals("") || paint == null) {
            return 0;
        }
        int strLength = str.length();
        int result = 0;
        float[] widths = new float[strLength];
        paint.getTextWidths(str, widths);
        for (int i = 0; i < strLength; i++) {
            result = (int) (((float) result) + widths[i]);
        }
        return result;
    }

    public static boolean isLeftPunctuation(char c) {
        char count = c;
        if (count == '“' || count == '《' || count == '（' || count == '【' || count == '(' || count == '<' || count == '[' || count == Service.NTP) {
            return true;
        }
        return false;
    }

    public static boolean isRightPunctuation(char c) {
        char count = c;
        if (count == '”' || count == '》' || count == '）' || count == '】' || count == ')' || count == '>' || count == ']' || count == Service.LOCUS_MAP) {
            return true;
        }
        return false;
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        int i = 0;
        while (i < c.length) {
            if (isPunctuation(c[i])) {
                if (c[i] == '　') {
                    c[i] = ' ';
                } else if (c[i] > '＀' && c[i] < '｟') {
                    c[i] = (char) (c[i] - 65248);
                }
            }
            i++;
        }
        return new String(c);
    }
}
