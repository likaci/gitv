package com.tvos.apps.utils;

import com.github.promeg.pinyinhelper.Pinyin;

public class TinyPinyinUtil {
    public static final String RET_PINYIN_TYPE_FULL = "full";
    public static final String RET_PINYIN_TYPE_HEADCHAR = "headChar";

    public static String char2Pinyin(char c) {
        return Pinyin.toPinyin(c);
    }

    public static String str2Pinyin(String src) {
        return str2Pinyin(src, null);
    }

    public static String str2Pinyin(String src, String retType) {
        if (src == null || src.trim().equalsIgnoreCase("")) {
            return null;
        }
        char[] srcChar = src.toCharArray();
        String[] temp = new String[src.length()];
        for (int i = 0; i < srcChar.length; i++) {
            try {
                temp[i] = Pinyin.toPinyin(srcChar[i]);
                if ("headChar".equalsIgnoreCase(retType)) {
                    temp[i] = String.valueOf(temp[i].charAt(0));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StringBuilder pinyinBuilder = new StringBuilder();
        for (String append : temp) {
            pinyinBuilder.append(append);
        }
        return pinyinBuilder.toString();
    }
}
