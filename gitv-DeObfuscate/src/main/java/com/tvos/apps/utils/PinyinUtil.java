package com.tvos.apps.utils;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {
    public static final String RET_PINYIN_TYPE_FULL = "full";
    public static final String RET_PINYIN_TYPE_HEADCHAR = "headChar";

    public static String getPinyin(String str) throws NullPointerException {
        return makeStringByStringSet(str2Pinyin(str));
    }

    private static String makeStringByStringSet(Set<String> stringSet) {
        StringBuilder str = new StringBuilder();
        int i = 0;
        for (String s : stringSet) {
            if (i == stringSet.size() - 1) {
                str.append(s);
            } else {
                str.append(new StringBuilder(String.valueOf(s)).append(",").toString());
            }
            i++;
        }
        return str.toString().toLowerCase(Locale.getDefault());
    }

    private static Set<String> str2Pinyin(String src) {
        return str2Pinyin(src, null);
    }

    private static Set<String> str2Pinyin(String src, String retType) {
        if (src == null || src.trim().equalsIgnoreCase("")) {
            return null;
        }
        char[] srcChar = src.toCharArray();
        HanyuPinyinOutputFormat hanYuPinOutputFormat = new HanyuPinyinOutputFormat();
        hanYuPinOutputFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        hanYuPinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        hanYuPinOutputFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
        String[][] temp = new String[src.length()][];
        for (int i = 0; i < srcChar.length; i++) {
            char c = srcChar[i];
            try {
                temp[i] = PinyinHelper.toHanyuPinyinStringArray(srcChar[i], hanYuPinOutputFormat);
                if (temp[i] == null) {
                    temp[i] = new String[]{String.valueOf(srcChar[i])};
                } else if ("headChar".equalsIgnoreCase(retType)) {
                    String[] temptemp = new String[temp[i].length];
                    for (int j = 0; j < temp[i].length; j++) {
                        temptemp[j] = String.valueOf(temp[i][j].charAt(0));
                    }
                    temp[i] = temptemp;
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        String[] pingyinArray = Exchange(temp);
        Set<String> pinyinSet = new HashSet();
        for (Object add : pingyinArray) {
            pinyinSet.add(add);
        }
        return pinyinSet;
    }

    private static String[] Exchange(String[][] strJaggedArray) {
        return DoExchange(strJaggedArray)[0];
    }

    private static String[][] DoExchange(String[][] strJaggedArray) {
        int len = strJaggedArray.length;
        if (len < 2) {
            return strJaggedArray;
        }
        int i;
        String[] temp = new String[(len1 * len2)];
        int Index = 0;
        for (Object valueOf : strJaggedArray[0]) {
            for (String append : strJaggedArray[1]) {
                temp[Index] = new StringBuilder(String.valueOf(valueOf)).append(append).toString();
                Index++;
            }
        }
        String[][] newArray = new String[(len - 1)][];
        for (i = 2; i < len; i++) {
            newArray[i - 1] = strJaggedArray[i];
        }
        newArray[0] = temp;
        return DoExchange(newArray);
    }
}
