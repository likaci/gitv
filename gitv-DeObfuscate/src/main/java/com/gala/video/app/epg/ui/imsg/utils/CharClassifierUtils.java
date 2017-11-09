package com.gala.video.app.epg.ui.imsg.utils;

import com.gala.video.lib.framework.core.utils.StringUtils;
import java.lang.Character.UnicodeBlock;

public class CharClassifierUtils {
    private static final String CHINESE_PUNCTUATION_LEFT = "《“‘（【";
    private static final String CHINESE_PUNCTUATION_RIGHT = "》”’）】";
    private static final String ENGLISH_PUNCTUATION_LEFT = "<[{";
    private static final String ENGLISH_PUNCTUATION_LEFT_RIGHT = "'\"";
    private static final String ENGLISH_PUNCTUATION_RIGHT = ">]}";
    private static final String NEWLINE = "\n";
    private static final String PUNCTUATION = "、`~!@#$^&*)=|:;,./?~！@#￥……&*）——|；：'。，、？";
    public static final int TYPE_CHINESE = 1;
    public static final int TYPE_CHINESE_PUNCTUATION = 5;
    public static final int TYPE_CHINESE_PUNCTUATION_LEFT = 3;
    public static final int TYPE_CHINESE_PUNCTUATION_RIGHT = 4;
    public static final int TYPE_ENGLISH = 2;
    public static final int TYPE_ENGLISH_PUNCTUATION = 8;
    public static final int TYPE_ENGLISH_PUNCTUATION_LEFT = 6;
    public static final int TYPE_ENGLISH_PUNCTUATION_LEFT_RIGHT = 9;
    public static final int TYPE_ENGLISH_PUNCTUATION_RIGHT = 7;

    public static String separatedLine(String text, int lineLength) {
        if (StringUtils.isEmpty((CharSequence) text) || lineLength < 1) {
            IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Text is null or lineLength is illegal.");
        }
        StringBuilder sb = new StringBuilder();
        int lineNumber = lineLength * 2;
        char[] chars = text.toCharArray();
        int lineIndex = 0;
        int length = chars.length;
        int index = 0;
        while (index < length) {
            int step;
            if (isChinese(chars[index])) {
                step = 2;
            } else {
                step = 1;
            }
            lineIndex += step;
            int next;
            switch (getType(chars[index])) {
                case 1:
                case 2:
                    if (lineIndex <= lineNumber) {
                        if (lineIndex != lineNumber) {
                            sb.append(chars[index]);
                            break;
                        }
                        sb.append(chars[index]);
                        next = index + 1;
                        if (next < chars.length && isPunctuation(chars[next])) {
                            sb.append(chars[next]);
                            next++;
                            if (next < chars.length && isPunctuation(chars[next])) {
                                sb.append(chars[next]);
                            }
                            index = next;
                        }
                        sb.append(NEWLINE);
                        lineIndex = 0;
                        break;
                    }
                    sb.append(NEWLINE);
                    sb.append(chars[index]);
                    lineIndex = step;
                    break;
                case 3:
                case 6:
                    if (lineIndex < lineNumber) {
                        sb.append(chars[index]);
                        break;
                    }
                    sb.append(NEWLINE);
                    sb.append(chars[index]);
                    lineIndex = 0;
                    break;
                case 4:
                case 7:
                    if (lineIndex < lineNumber) {
                        sb.append(chars[index]);
                        break;
                    }
                    sb.append(chars[index]);
                    next = index + 1;
                    if (next < chars.length && isPunctuation(chars[next])) {
                        sb.append(chars[next]);
                        index = next;
                    }
                    sb.append(NEWLINE);
                    lineIndex = 0;
                    break;
                case 5:
                case 8:
                    if (lineIndex < lineNumber) {
                        sb.append(chars[index]);
                        break;
                    }
                    sb.append(chars[index]);
                    sb.append(NEWLINE);
                    lineIndex = 0;
                    break;
                case 9:
                    if (lineIndex >= lineNumber) {
                        if (!isEven(subCount(sb.toString(), chars[index]))) {
                            sb.append(chars[index]);
                            next = index + 1;
                            if (next < length && isPunctuation(chars[next])) {
                                sb.append(chars[next]);
                                index = next;
                            }
                            sb.append(NEWLINE);
                            lineIndex = 0;
                            break;
                        }
                        sb.append(NEWLINE);
                        sb.append(chars[index]);
                        lineIndex = 0;
                        break;
                    }
                    sb.append(chars[index]);
                    break;
                default:
                    sb.append(chars[index]);
                    break;
            }
            index++;
        }
        return sb.toString();
    }

    public static boolean isEven(int count) {
        if (count % 2 == 0) {
            return true;
        }
        return false;
    }

    public static int subCount(String text, char c) {
        int count = 0;
        for (char aChar : text.toCharArray()) {
            if (aChar == c) {
                count++;
            }
        }
        return count;
    }

    public static int getType(char c) {
        if (isChineseByBlock(c)) {
            return 1;
        }
        if (isChinesePunctuation(c)) {
            if (CHINESE_PUNCTUATION_LEFT.contains(String.valueOf(c))) {
                return 3;
            }
            if (CHINESE_PUNCTUATION_RIGHT.contains(String.valueOf(c))) {
                return 4;
            }
            return 5;
        } else if (isEnglish(c)) {
            return 2;
        } else {
            if (ENGLISH_PUNCTUATION_LEFT.contains(String.valueOf(c))) {
                return 6;
            }
            if (ENGLISH_PUNCTUATION_RIGHT.contains(String.valueOf(c))) {
                return 7;
            }
            if (ENGLISH_PUNCTUATION_LEFT_RIGHT.contains(String.valueOf(c))) {
                return 9;
            }
            return 8;
        }
    }

    public static boolean isChinese(char c) {
        if (isChineseByBlock(c) || isChinesePunctuation(c)) {
            return true;
        }
        return false;
    }

    public static boolean isChineseByBlock(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        if (ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS || ub == UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS_SUPPLEMENT) {
            return true;
        }
        return false;
    }

    public static boolean isChinesePunctuation(char c) {
        UnicodeBlock ub = UnicodeBlock.of(c);
        if (ub == UnicodeBlock.GENERAL_PUNCTUATION || ub == UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub == UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub == UnicodeBlock.CJK_COMPATIBILITY_FORMS) {
            return true;
        }
        return false;
    }

    public static boolean isPunctuation(char c) {
        if (isChinesePunctuation(c) || PUNCTUATION.contains(String.valueOf(c))) {
            return true;
        }
        return false;
    }

    public static boolean isEnglish(char c) {
        return String.valueOf(c).matches("^[a-zA-Z0-9]*");
    }
}
