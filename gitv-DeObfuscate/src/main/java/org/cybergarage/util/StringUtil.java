package org.cybergarage.util;

public final class StringUtil {
    public static final boolean hasData(String value) {
        if (value != null && value.length() > 0) {
            return true;
        }
        return false;
    }

    public static final int toInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            Debug.warning(e);
            return 0;
        }
    }

    public static final long toLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            Debug.warning(e);
            return 0;
        }
    }

    public static final int findOf(String str, String chars, int startIdx, int endIdx, int offset, boolean isEqual) {
        if (offset == 0) {
            return -1;
        }
        int charCnt = chars.length();
        int idx = startIdx;
        while (true) {
            char strc;
            int noEqualCnt;
            int n;
            char charc;
            if (offset > 0) {
                if (endIdx < idx) {
                    break;
                }
                strc = str.charAt(idx);
                noEqualCnt = 0;
                for (n = 0; n < charCnt; n++) {
                    charc = chars.charAt(n);
                    if (isEqual) {
                        if (strc != charc) {
                            noEqualCnt++;
                        }
                        if (noEqualCnt == charCnt) {
                            return idx;
                        }
                    } else if (strc != charc) {
                        return idx;
                    }
                }
                idx += offset;
            } else {
                if (idx < endIdx) {
                    break;
                }
                strc = str.charAt(idx);
                noEqualCnt = 0;
                for (n = 0; n < charCnt; n++) {
                    charc = chars.charAt(n);
                    if (isEqual) {
                        if (strc != charc) {
                            noEqualCnt++;
                        }
                        if (noEqualCnt == charCnt) {
                            return idx;
                        }
                    } else if (strc != charc) {
                        return idx;
                    }
                }
                idx += offset;
            }
        }
        return -1;
    }

    public static final int findFirstOf(String str, String chars) {
        return findOf(str, chars, 0, str.length() - 1, 1, true);
    }

    public static final int findFirstNotOf(String str, String chars) {
        return findOf(str, chars, 0, str.length() - 1, 1, false);
    }

    public static final int findLastOf(String str, String chars) {
        return findOf(str, chars, str.length() - 1, 0, -1, true);
    }

    public static final int findLastNotOf(String str, String chars) {
        return findOf(str, chars, str.length() - 1, 0, -1, false);
    }

    public static final String trim(String trimStr, String trimChars) {
        int spIdx = findFirstNotOf(trimStr, trimChars);
        if (spIdx < 0) {
            return trimStr;
        }
        String trimStr2 = trimStr.substring(spIdx, trimStr.length());
        spIdx = findLastNotOf(trimStr2, trimChars);
        if (spIdx < 0) {
            return trimStr2;
        }
        return trimStr2.substring(0, spIdx + 1);
    }
}
