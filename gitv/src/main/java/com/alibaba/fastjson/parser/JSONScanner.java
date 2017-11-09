package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.util.ASMUtils;
import com.alibaba.fastjson.util.IOUtils;
import com.mcto.ads.internal.net.SendFlag;
import java.util.Calendar;
import java.util.TimeZone;

public final class JSONScanner extends JSONLexerBase {
    public static final int ISO8601_LEN_0 = "0000-00-00".length();
    public static final int ISO8601_LEN_1 = "0000-00-00T00:00:00".length();
    public static final int ISO8601_LEN_2 = "0000-00-00T00:00:00.000".length();
    private final int len;
    private final String text;

    public JSONScanner(String input) {
        this(input, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(String input, int features) {
        super(features);
        this.text = input;
        this.len = this.text.length();
        this.bp = -1;
        next();
        if (this.ch == '﻿') {
            next();
        }
    }

    public final char charAt(int index) {
        if (index >= this.len) {
            return JSONLexer.EOI;
        }
        return this.text.charAt(index);
    }

    public final char next() {
        char c;
        int index = this.bp + 1;
        this.bp = index;
        if (index >= this.len) {
            c = JSONLexer.EOI;
        } else {
            c = this.text.charAt(index);
        }
        this.ch = c;
        return c;
    }

    public JSONScanner(char[] input, int inputLength) {
        this(input, inputLength, JSON.DEFAULT_PARSER_FEATURE);
    }

    public JSONScanner(char[] input, int inputLength, int features) {
        this(new String(input, 0, inputLength), features);
    }

    protected final void copyTo(int offset, int count, char[] dest) {
        this.text.getChars(offset, offset + count, dest, 0);
    }

    static boolean charArrayCompare(String src, int offset, char[] dest) {
        int destLen = dest.length;
        if (destLen + offset > src.length()) {
            return false;
        }
        for (int i = 0; i < destLen; i++) {
            if (dest[i] != src.charAt(offset + i)) {
                return false;
            }
        }
        return true;
    }

    public final boolean charArrayCompare(char[] chars) {
        return charArrayCompare(this.text, this.bp, chars);
    }

    public final int indexOf(char ch, int startIndex) {
        return this.text.indexOf(ch, startIndex);
    }

    public final String addSymbol(int offset, int len, int hash, SymbolTable symbolTable) {
        return symbolTable.addSymbol(this.text, offset, len, hash);
    }

    public byte[] bytesValue() {
        return IOUtils.decodeBase64(this.text, this.np + 1, this.sp);
    }

    public final String stringVal() {
        if (this.hasSpecial) {
            return new String(this.sbuf, 0, this.sp);
        }
        return subString(this.np + 1, this.sp);
    }

    public final String subString(int offset, int count) {
        if (!ASMUtils.IS_ANDROID) {
            return this.text.substring(offset, offset + count);
        }
        if (count < this.sbuf.length) {
            this.text.getChars(offset, offset + count, this.sbuf, 0);
            return new String(this.sbuf, 0, count);
        }
        char[] chars = new char[count];
        this.text.getChars(offset, offset + count, chars, 0);
        return new String(chars);
    }

    public final char[] sub_chars(int offset, int count) {
        if (!ASMUtils.IS_ANDROID || count >= this.sbuf.length) {
            char[] chars = new char[count];
            this.text.getChars(offset, offset + count, chars, 0);
            return chars;
        }
        this.text.getChars(offset, offset + count, this.sbuf, 0);
        return this.sbuf;
    }

    public final String numberString() {
        char chLocal = charAt((this.np + this.sp) - 1);
        int sp = this.sp;
        if (chLocal == 'L' || chLocal == 'S' || chLocal == 'B' || chLocal == 'F' || chLocal == 'D') {
            sp--;
        }
        return subString(this.np, sp);
    }

    public boolean scanISO8601DateIfMatch() {
        return scanISO8601DateIfMatch(true);
    }

    public boolean scanISO8601DateIfMatch(boolean strict) {
        int rest = this.len - this.bp;
        if (!strict && rest > 13) {
            char c0 = charAt(this.bp);
            char c1 = charAt(this.bp + 1);
            char c2 = charAt(this.bp + 2);
            char c3 = charAt(this.bp + 3);
            char c4 = charAt(this.bp + 4);
            char c5 = charAt(this.bp + 5);
            char c_r0 = charAt((this.bp + rest) - 1);
            char c_r1 = charAt((this.bp + rest) - 2);
            if (c0 == '/' && c1 == 'D' && c2 == 'a' && c3 == 't' && c4 == 'e' && c5 == '(' && c_r0 == '/' && c_r1 == ')') {
                int plusIndex = -1;
                for (int i = 6; i < rest; i++) {
                    char c = charAt(this.bp + i);
                    if (c != '+') {
                        if (c < '0' || c > '9') {
                            break;
                        }
                    } else {
                        plusIndex = i;
                    }
                }
                if (plusIndex == -1) {
                    return false;
                }
                int offset = this.bp + 6;
                long millis = Long.parseLong(subString(offset, plusIndex - offset));
                this.calendar = Calendar.getInstance(this.timeZone, this.locale);
                this.calendar.setTimeInMillis(millis);
                this.token = 5;
                return true;
            }
        }
        char y0;
        char y1;
        char y2;
        char y3;
        char M0;
        char M1;
        char d0;
        char d1;
        char h0;
        char h1;
        char m0;
        char m1;
        char s0;
        char s1;
        char S0;
        char S1;
        char S2;
        int millis2;
        if (rest == 8 || rest == 14 || rest == 17) {
            if (strict) {
                return false;
            }
            y0 = charAt(this.bp);
            y1 = charAt(this.bp + 1);
            y2 = charAt(this.bp + 2);
            y3 = charAt(this.bp + 3);
            M0 = charAt(this.bp + 4);
            M1 = charAt(this.bp + 5);
            d0 = charAt(this.bp + 6);
            d1 = charAt(this.bp + 7);
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            int hour;
            int minute;
            int seconds;
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            if (rest != 8) {
                h0 = charAt(this.bp + 8);
                h1 = charAt(this.bp + 9);
                m0 = charAt(this.bp + 10);
                m1 = charAt(this.bp + 11);
                s0 = charAt(this.bp + 12);
                s1 = charAt(this.bp + 13);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                if (rest == 17) {
                    S0 = charAt(this.bp + 14);
                    S1 = charAt(this.bp + 15);
                    S2 = charAt(this.bp + 16);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    if (S1 < '0' || S1 > '9') {
                        return false;
                    }
                    if (S2 < '0' || S2 > '9') {
                        return false;
                    }
                    millis2 = (((S0 - 48) * 100) + ((S1 - 48) * 10)) + (S2 - 48);
                } else {
                    millis2 = 0;
                }
                hour = ((h0 - 48) * 10) + (h1 - 48);
                minute = ((m0 - 48) * 10) + (m1 - 48);
                seconds = ((s0 - 48) * 10) + (s1 - 48);
            } else {
                hour = 0;
                minute = 0;
                seconds = 0;
                millis2 = 0;
            }
            this.calendar.set(11, hour);
            this.calendar.set(12, minute);
            this.calendar.set(13, seconds);
            this.calendar.set(14, millis2);
            this.token = 5;
            return true;
        } else if (rest < ISO8601_LEN_0) {
            return false;
        } else {
            if (charAt(this.bp + 4) != '-') {
                return false;
            }
            if (charAt(this.bp + 7) != '-') {
                return false;
            }
            y0 = charAt(this.bp);
            y1 = charAt(this.bp + 1);
            y2 = charAt(this.bp + 2);
            y3 = charAt(this.bp + 3);
            M0 = charAt(this.bp + 5);
            M1 = charAt(this.bp + 6);
            d0 = charAt(this.bp + 8);
            d1 = charAt(this.bp + 9);
            if (!checkDate(y0, y1, y2, y3, M0, M1, d0, d1)) {
                return false;
            }
            setCalendar(y0, y1, y2, y3, M0, M1, d0, d1);
            char t = charAt(this.bp + 10);
            int i2;
            if (t == 'T' || (t == ' ' && !strict)) {
                if (rest < ISO8601_LEN_1) {
                    return false;
                }
                if (charAt(this.bp + 13) != ':') {
                    return false;
                }
                if (charAt(this.bp + 16) != ':') {
                    return false;
                }
                h0 = charAt(this.bp + 11);
                h1 = charAt(this.bp + 12);
                m0 = charAt(this.bp + 14);
                m1 = charAt(this.bp + 15);
                s0 = charAt(this.bp + 17);
                s1 = charAt(this.bp + 18);
                if (!checkTime(h0, h1, m0, m1, s0, s1)) {
                    return false;
                }
                setTime(h0, h1, m0, m1, s0, s1);
                char dot = charAt(this.bp + 19);
                String[] timeZoneIDs;
                if (dot != '.') {
                    this.calendar.set(14, 0);
                    i2 = this.bp + 19;
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.token = 5;
                    if (dot == 'Z' && this.calendar.getTimeZone().getRawOffset() != 0) {
                        timeZoneIDs = TimeZone.getAvailableIDs(0);
                        if (timeZoneIDs.length > 0) {
                            this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
                        }
                    }
                    return true;
                } else if (rest < ISO8601_LEN_2) {
                    return false;
                } else {
                    S0 = charAt(this.bp + 20);
                    if (S0 < '0' || S0 > '9') {
                        return false;
                    }
                    millis2 = S0 - 48;
                    int millisLen = 1;
                    S1 = charAt(this.bp + 21);
                    if (S1 >= '0' && S1 <= '9') {
                        millis2 = (millis2 * 10) + (S1 - 48);
                        millisLen = 2;
                    }
                    if (millisLen == 2) {
                        S2 = charAt(this.bp + 22);
                        if (S2 >= '0' && S2 <= '9') {
                            millis2 = (millis2 * 10) + (S2 - 48);
                            millisLen = 3;
                        }
                    }
                    this.calendar.set(14, millis2);
                    int timzeZoneLength = 0;
                    char timeZoneFlag = charAt((this.bp + 20) + millisLen);
                    if (timeZoneFlag == '+' || timeZoneFlag == '-') {
                        char t0 = charAt(((this.bp + 20) + millisLen) + 1);
                        if (t0 < '0' || t0 > '1') {
                            return false;
                        }
                        char t1 = charAt(((this.bp + 20) + millisLen) + 2);
                        if (t1 < '0' || t1 > '9') {
                            return false;
                        }
                        char t2 = charAt(((this.bp + 20) + millisLen) + 3);
                        if (t2 == ':') {
                            if (charAt(((this.bp + 20) + millisLen) + 4) != '0') {
                                return false;
                            }
                            if (charAt(((this.bp + 20) + millisLen) + 5) != '0') {
                                return false;
                            }
                            timzeZoneLength = 6;
                        } else if (t2 != '0') {
                            timzeZoneLength = 3;
                        } else if (charAt(((this.bp + 20) + millisLen) + 4) != '0') {
                            return false;
                        } else {
                            timzeZoneLength = 5;
                        }
                        setTimeZone(timeZoneFlag, t0, t1);
                    } else if (timeZoneFlag == 'Z') {
                        timzeZoneLength = 1;
                        if (this.calendar.getTimeZone().getRawOffset() != 0) {
                            timeZoneIDs = TimeZone.getAvailableIDs(0);
                            if (timeZoneIDs.length > 0) {
                                this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
                            }
                        }
                    }
                    char end = charAt(this.bp + ((millisLen + 20) + timzeZoneLength));
                    if (end != '\u001a' && end != '\"') {
                        return false;
                    }
                    i2 = this.bp + ((millisLen + 20) + timzeZoneLength);
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.token = 5;
                    return true;
                }
            } else if (t == '\"' || t == '\u001a') {
                this.calendar.set(11, 0);
                this.calendar.set(12, 0);
                this.calendar.set(13, 0);
                this.calendar.set(14, 0);
                i2 = this.bp + 10;
                this.bp = i2;
                this.ch = charAt(i2);
                this.token = 5;
                return true;
            } else if (t != '+' && t != '-') {
                return false;
            } else {
                if (this.len != 16) {
                    return false;
                }
                if (charAt(this.bp + 13) != ':' || charAt(this.bp + 14) != '0' || charAt(this.bp + 15) != '0') {
                    return false;
                }
                setTime('0', '0', '0', '0', '0', '0');
                this.calendar.set(14, 0);
                setTimeZone(t, charAt(this.bp + 11), charAt(this.bp + 12));
                return true;
            }
        }
    }

    protected void setTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        int minute = ((m0 - 48) * 10) + (m1 - 48);
        int seconds = ((s0 - 48) * 10) + (s1 - 48);
        this.calendar.set(11, ((h0 - 48) * 10) + (h1 - 48));
        this.calendar.set(12, minute);
        this.calendar.set(13, seconds);
    }

    protected void setTimeZone(char timeZoneFlag, char t0, char t1) {
        int timeZoneOffset = ((((t0 - 48) * 10) + (t1 - 48)) * 3600) * 1000;
        if (timeZoneFlag == '-') {
            timeZoneOffset = -timeZoneOffset;
        }
        if (this.calendar.getTimeZone().getRawOffset() != timeZoneOffset) {
            String[] timeZoneIDs = TimeZone.getAvailableIDs(timeZoneOffset);
            if (timeZoneIDs.length > 0) {
                this.calendar.setTimeZone(TimeZone.getTimeZone(timeZoneIDs[0]));
            }
        }
    }

    private boolean checkTime(char h0, char h1, char m0, char m1, char s0, char s1) {
        if (h0 == '0') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 == '1') {
            if (h1 < '0' || h1 > '9') {
                return false;
            }
        } else if (h0 != '2' || h1 < '0') {
            return false;
        } else {
            if (h1 > '4') {
                return false;
            }
        }
        if (m0 < '0' || m0 > '5') {
            if (m0 != '6') {
                return false;
            }
            if (m1 != '0') {
                return false;
            }
        } else if (m1 < '0' || m1 > '9') {
            return false;
        }
        if (s0 < '0' || s0 > '5') {
            if (s0 != '6') {
                return false;
            }
            if (s1 != '0') {
                return false;
            }
        } else if (s1 < '0' || s1 > '9') {
            return false;
        }
        return true;
    }

    private void setCalendar(char y0, char y1, char y2, char y3, char M0, char M1, char d0, char d1) {
        this.calendar = Calendar.getInstance(this.timeZone, this.locale);
        int month = (((M0 - 48) * 10) + (M1 - 48)) - 1;
        int day = ((d0 - 48) * 10) + (d1 - 48);
        this.calendar.set(1, ((((y0 - 48) * 1000) + ((y1 - 48) * 100)) + ((y2 - 48) * 10)) + (y3 - 48));
        this.calendar.set(2, month);
        this.calendar.set(5, day);
    }

    static boolean checkDate(char y0, char y1, char y2, char y3, char M0, char M1, int d0, int d1) {
        if ((y0 != '1' && y0 != '2') || y1 < '0' || y1 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9') {
            return false;
        }
        if (M0 == '0') {
            if (M1 < '1' || M1 > '9') {
                return false;
            }
        } else if (M0 != '1') {
            return false;
        } else {
            if (!(M1 == '0' || M1 == '1' || M1 == '2')) {
                return false;
            }
        }
        if (d0 == 48) {
            if (d1 < 49 || d1 > 57) {
                return false;
            }
        } else if (d0 == 49 || d0 == 50) {
            if (d1 < 48) {
                return false;
            }
            if (d1 > 57) {
                return false;
            }
        } else if (d0 != 51) {
            return false;
        } else {
            if (!(d1 == 48 || d1 == 49)) {
                return false;
            }
        }
        return true;
    }

    public boolean isEOF() {
        return this.bp == this.len || (this.ch == JSONLexer.EOI && this.bp + 1 == this.len);
    }

    public int scanFieldInt(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            boolean negative = false;
            if (ch == '-') {
                index = index2 + 1;
                ch = charAt(index2);
                negative = true;
            } else {
                index = index2;
            }
            if (ch < '0' || ch > '9') {
                this.matchStat = -1;
                return 0;
            }
            int value = ch - 48;
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch >= '0' && ch <= '9') {
                    value = (value * 10) + (ch - 48);
                    index = index2;
                }
            }
            if (ch == '.') {
                this.matchStat = -1;
                return 0;
            } else if (value < 0) {
                this.matchStat = -1;
                return 0;
            } else {
                if (ch == ',' || ch == '}') {
                    this.bp = index2 - 1;
                }
                int i;
                if (ch == ',') {
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                    this.matchStat = 3;
                    this.token = 16;
                    if (negative) {
                        return -value;
                    }
                    return value;
                }
                if (ch == '}') {
                    int i2 = this.bp + 1;
                    this.bp = i2;
                    ch = charAt(i2);
                    if (ch == ',') {
                        this.token = 16;
                        i = this.bp + 1;
                        this.bp = i;
                        this.ch = charAt(i);
                    } else if (ch == ']') {
                        this.token = 15;
                        i = this.bp + 1;
                        this.bp = i;
                        this.ch = charAt(i);
                    } else if (ch == '}') {
                        this.token = 13;
                        i = this.bp + 1;
                        this.bp = i;
                        this.ch = charAt(i);
                    } else if (ch == JSONLexer.EOI) {
                        this.token = 20;
                    } else {
                        this.bp = startPos;
                        this.ch = startChar;
                        this.matchStat = -1;
                        return 0;
                    }
                    this.matchStat = 4;
                }
                if (negative) {
                    return -value;
                }
                return value;
            }
        }
        this.matchStat = -2;
        return 0;
    }

    public String scanFieldString(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int length = this.bp + fieldName.length;
            int index = length + 1;
            if (charAt(length) != '\"') {
                this.matchStat = -1;
                return stringDefaultValue();
            }
            int startIndex = index;
            int endIndex = indexOf('\"', startIndex);
            if (endIndex == -1) {
                throw new JSONException("unclosed str");
            }
            String stringVal = subString(startIndex, endIndex - startIndex);
            if (stringVal.indexOf(92) != -1) {
                while (true) {
                    int slashCount = 0;
                    int i = endIndex - 1;
                    while (i >= 0 && charAt(i) == '\\') {
                        slashCount++;
                        i--;
                    }
                    if (slashCount % 2 == 0) {
                        break;
                    }
                    endIndex = indexOf('\"', endIndex + 1);
                }
                int chars_len = endIndex - ((this.bp + fieldName.length) + 1);
                stringVal = JSONLexerBase.readString(sub_chars((this.bp + fieldName.length) + 1, chars_len), chars_len);
            }
            char ch = charAt(endIndex + 1);
            if (ch == ',' || ch == '}') {
                this.bp = endIndex + 1;
                this.ch = ch;
                String strVal = stringVal;
                int i2;
                if (ch == ',') {
                    i2 = this.bp + 1;
                    this.bp = i2;
                    this.ch = charAt(i2);
                    this.matchStat = 3;
                    return strVal;
                } else if (ch == '}') {
                    i2 = this.bp + 1;
                    this.bp = i2;
                    ch = charAt(i2);
                    if (ch == ',') {
                        this.token = 16;
                        i2 = this.bp + 1;
                        this.bp = i2;
                        this.ch = charAt(i2);
                    } else if (ch == ']') {
                        this.token = 15;
                        i2 = this.bp + 1;
                        this.bp = i2;
                        this.ch = charAt(i2);
                    } else if (ch == '}') {
                        this.token = 13;
                        i2 = this.bp + 1;
                        this.bp = i2;
                        this.ch = charAt(i2);
                    } else if (ch == JSONLexer.EOI) {
                        this.token = 20;
                    } else {
                        this.bp = startPos;
                        this.ch = startChar;
                        this.matchStat = -1;
                        return stringDefaultValue();
                    }
                    this.matchStat = 4;
                    return strVal;
                } else {
                    this.matchStat = -1;
                    return stringDefaultValue();
                }
            }
            this.matchStat = -1;
            return stringDefaultValue();
        }
        this.matchStat = -2;
        return stringDefaultValue();
    }

    public String scanFieldSymbol(char[] fieldName, SymbolTable symbolTable) {
        this.matchStat = 0;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            if (charAt(index) != '\"') {
                this.matchStat = -1;
                return null;
            }
            char ch;
            int start = index2;
            int hash = 0;
            index = index2;
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch == '\"') {
                    break;
                }
                hash = (hash * 31) + ch;
                if (ch == '\\') {
                    this.matchStat = -1;
                    return null;
                }
                index = index2;
            }
            this.bp = index2;
            ch = charAt(this.bp);
            this.ch = ch;
            String strVal = symbolTable.addSymbol(this.text, start, (index2 - start) - 1, hash);
            int i;
            if (ch == ',') {
                i = this.bp + 1;
                this.bp = i;
                this.ch = charAt(i);
                this.matchStat = 3;
                return strVal;
            } else if (ch == '}') {
                int i2 = this.bp + 1;
                this.bp = i2;
                ch = charAt(i2);
                if (ch == ',') {
                    this.token = 16;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == ']') {
                    this.token = 15;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '}') {
                    this.token = 13;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == JSONLexer.EOI) {
                    this.token = 20;
                } else {
                    this.matchStat = -1;
                    return null;
                }
                this.matchStat = 4;
                return strVal;
            } else {
                this.matchStat = -1;
                return null;
            }
        }
        this.matchStat = -2;
        return null;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Collection<java.lang.String> scanFieldStringArray(char[] r17, java.lang.Class<?> r18) {
        /*
        r16 = this;
        r14 = 0;
        r0 = r16;
        r0.matchStat = r14;
        r0 = r16;
        r14 = r0.text;
        r0 = r16;
        r15 = r0.bp;
        r0 = r17;
        r14 = charArrayCompare(r14, r15, r0);
        if (r14 != 0) goto L_0x001c;
    L_0x0015:
        r14 = -2;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
    L_0x001b:
        return r9;
    L_0x001c:
        r14 = java.util.HashSet.class;
        r0 = r18;
        r14 = r0.isAssignableFrom(r14);
        if (r14 == 0) goto L_0x0062;
    L_0x0026:
        r9 = new java.util.HashSet;
        r9.<init>();
    L_0x002b:
        r0 = r16;
        r14 = r0.bp;
        r0 = r17;
        r15 = r0.length;
        r7 = r14 + r15;
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r14 = 91;
        if (r1 != r14) goto L_0x015b;
    L_0x0040:
        r7 = r8 + 1;
        r0 = r16;
        r1 = r0.charAt(r8);
        r8 = r7;
    L_0x0049:
        r14 = 34;
        if (r1 != r14) goto L_0x00e0;
    L_0x004d:
        r12 = r8;
        r14 = 34;
        r0 = r16;
        r5 = r0.indexOf(r14, r12);
        r14 = -1;
        if (r5 != r14) goto L_0x0084;
    L_0x0059:
        r14 = new com.alibaba.fastjson.JSONException;
        r15 = "unclosed str";
        r14.<init>(r15);
        throw r14;
    L_0x0062:
        r14 = java.util.ArrayList.class;
        r0 = r18;
        r14 = r0.isAssignableFrom(r14);
        if (r14 == 0) goto L_0x0072;
    L_0x006c:
        r9 = new java.util.ArrayList;
        r9.<init>();
        goto L_0x002b;
    L_0x0072:
        r9 = r18.newInstance();	 Catch:{ Exception -> 0x0079 }
        r9 = (java.util.Collection) r9;	 Catch:{ Exception -> 0x0079 }
        goto L_0x002b;
    L_0x0079:
        r4 = move-exception;
        r14 = new com.alibaba.fastjson.JSONException;
        r15 = r4.getMessage();
        r14.<init>(r15, r4);
        throw r14;
    L_0x0084:
        r14 = r5 - r12;
        r0 = r16;
        r13 = r0.subString(r12, r14);
        r14 = 92;
        r14 = r13.indexOf(r14);
        r15 = -1;
        if (r14 == r15) goto L_0x00b9;
    L_0x0095:
        r10 = 0;
        r6 = r5 + -1;
    L_0x0098:
        if (r6 < 0) goto L_0x00a9;
    L_0x009a:
        r0 = r16;
        r14 = r0.charAt(r6);
        r15 = 92;
        if (r14 != r15) goto L_0x00a9;
    L_0x00a4:
        r10 = r10 + 1;
        r6 = r6 + -1;
        goto L_0x0098;
    L_0x00a9:
        r14 = r10 % 2;
        if (r14 != 0) goto L_0x00d5;
    L_0x00ad:
        r3 = r5 - r12;
        r0 = r16;
        r2 = r0.sub_chars(r12, r3);
        r13 = com.alibaba.fastjson.parser.JSONLexerBase.readString(r2, r3);
    L_0x00b9:
        r7 = r5 + 1;
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r9.add(r13);
    L_0x00c6:
        r14 = 44;
        if (r1 != r14) goto L_0x0137;
    L_0x00ca:
        r7 = r8 + 1;
        r0 = r16;
        r1 = r0.charAt(r8);
        r8 = r7;
        goto L_0x0049;
    L_0x00d5:
        r14 = 34;
        r15 = r5 + 1;
        r0 = r16;
        r5 = r0.indexOf(r14, r15);
        goto L_0x0095;
    L_0x00e0:
        r14 = 110; // 0x6e float:1.54E-43 double:5.43E-322;
        if (r1 != r14) goto L_0x0100;
    L_0x00e4:
        r0 = r16;
        r14 = r0.text;
        r15 = "ull";
        r14 = r14.startsWith(r15, r8);
        if (r14 == 0) goto L_0x0100;
    L_0x00f1:
        r7 = r8 + 3;
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r14 = 0;
        r9.add(r14);
        goto L_0x00c6;
    L_0x0100:
        r14 = 93;
        if (r1 != r14) goto L_0x012f;
    L_0x0104:
        r14 = r9.size();
        if (r14 != 0) goto L_0x012f;
    L_0x010a:
        r7 = r8 + 1;
        r0 = r16;
        r1 = r0.charAt(r8);
    L_0x0112:
        r0 = r16;
        r0.bp = r7;
        r14 = 44;
        if (r1 != r14) goto L_0x017d;
    L_0x011a:
        r0 = r16;
        r14 = r0.bp;
        r0 = r16;
        r14 = r0.charAt(r14);
        r0 = r16;
        r0.ch = r14;
        r14 = 3;
        r0 = r16;
        r0.matchStat = r14;
        goto L_0x001b;
    L_0x012f:
        r14 = -1;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
        goto L_0x001b;
    L_0x0137:
        r14 = 93;
        if (r1 != r14) goto L_0x0153;
    L_0x013b:
        r7 = r8 + 1;
        r0 = r16;
        r1 = r0.charAt(r8);
    L_0x0143:
        r14 = com.alibaba.fastjson.parser.JSONLexerBase.isWhitespace(r1);
        if (r14 == 0) goto L_0x0112;
    L_0x0149:
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r7 = r8;
        goto L_0x0143;
    L_0x0153:
        r14 = -1;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
        goto L_0x001b;
    L_0x015b:
        r0 = r16;
        r14 = r0.text;
        r15 = "ull";
        r14 = r14.startsWith(r15, r8);
        if (r14 == 0) goto L_0x0175;
    L_0x0168:
        r7 = r8 + 3;
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r9 = 0;
        r7 = r8;
        goto L_0x0112;
    L_0x0175:
        r14 = -1;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
        goto L_0x001b;
    L_0x017d:
        r14 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r1 != r14) goto L_0x021d;
    L_0x0181:
        r0 = r16;
        r14 = r0.bp;
        r0 = r16;
        r1 = r0.charAt(r14);
    L_0x018b:
        r14 = 44;
        if (r1 != r14) goto L_0x01b0;
    L_0x018f:
        r14 = 16;
        r0 = r16;
        r0.token = r14;
        r0 = r16;
        r14 = r0.bp;
        r14 = r14 + 1;
        r0 = r16;
        r0.bp = r14;
        r0 = r16;
        r14 = r0.charAt(r14);
        r0 = r16;
        r0.ch = r14;
    L_0x01a9:
        r14 = 4;
        r0 = r16;
        r0.matchStat = r14;
        goto L_0x001b;
    L_0x01b0:
        r14 = 93;
        if (r1 != r14) goto L_0x01cf;
    L_0x01b4:
        r14 = 15;
        r0 = r16;
        r0.token = r14;
        r0 = r16;
        r14 = r0.bp;
        r14 = r14 + 1;
        r0 = r16;
        r0.bp = r14;
        r0 = r16;
        r14 = r0.charAt(r14);
        r0 = r16;
        r0.ch = r14;
        goto L_0x01a9;
    L_0x01cf:
        r14 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r1 != r14) goto L_0x01ee;
    L_0x01d3:
        r14 = 13;
        r0 = r16;
        r0.token = r14;
        r0 = r16;
        r14 = r0.bp;
        r14 = r14 + 1;
        r0 = r16;
        r0.bp = r14;
        r0 = r16;
        r14 = r0.charAt(r14);
        r0 = r16;
        r0.ch = r14;
        goto L_0x01a9;
    L_0x01ee:
        r14 = 26;
        if (r1 != r14) goto L_0x01fd;
    L_0x01f2:
        r14 = 20;
        r0 = r16;
        r0.token = r14;
        r0 = r16;
        r0.ch = r1;
        goto L_0x01a9;
    L_0x01fd:
        r11 = 0;
    L_0x01fe:
        r14 = com.alibaba.fastjson.parser.JSONLexerBase.isWhitespace(r1);
        if (r14 == 0) goto L_0x0213;
    L_0x0204:
        r8 = r7 + 1;
        r0 = r16;
        r1 = r0.charAt(r7);
        r0 = r16;
        r0.bp = r8;
        r11 = 1;
        r7 = r8;
        goto L_0x01fe;
    L_0x0213:
        if (r11 != 0) goto L_0x018b;
    L_0x0215:
        r14 = -1;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
        goto L_0x001b;
    L_0x021d:
        r14 = -1;
        r0 = r16;
        r0.matchStat = r14;
        r9 = 0;
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.JSONScanner.scanFieldStringArray(char[], java.lang.Class):java.util.Collection<java.lang.String>");
    }

    public long scanFieldLong(char[] fieldName) {
        this.matchStat = 0;
        int startPos = this.bp;
        char startChar = this.ch;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            boolean negative = false;
            if (ch == '-') {
                index = index2 + 1;
                ch = charAt(index2);
                negative = true;
            } else {
                index = index2;
            }
            if (ch < '0' || ch > '9') {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return 0;
            }
            long value = (long) (ch - 48);
            while (true) {
                index2 = index + 1;
                ch = charAt(index);
                if (ch >= '0' && ch <= '9') {
                    value = (10 * value) + ((long) (ch - 48));
                    index = index2;
                }
            }
            if (ch == '.') {
                this.matchStat = -1;
                return 0;
            }
            if (ch == ',' || ch == '}') {
                this.bp = index2 - 1;
            }
            if (value < 0) {
                this.bp = startPos;
                this.ch = startChar;
                this.matchStat = -1;
                return 0;
            } else if (ch == ',') {
                r8 = this.bp + 1;
                this.bp = r8;
                this.ch = charAt(r8);
                this.matchStat = 3;
                this.token = 16;
                if (negative) {
                    return -value;
                }
                return value;
            } else if (ch == '}') {
                r8 = this.bp + 1;
                this.bp = r8;
                ch = charAt(r8);
                if (ch == ',') {
                    this.token = 16;
                    r8 = this.bp + 1;
                    this.bp = r8;
                    this.ch = charAt(r8);
                } else if (ch == ']') {
                    this.token = 15;
                    r8 = this.bp + 1;
                    this.bp = r8;
                    this.ch = charAt(r8);
                } else if (ch == '}') {
                    this.token = 13;
                    r8 = this.bp + 1;
                    this.bp = r8;
                    this.ch = charAt(r8);
                } else if (ch == JSONLexer.EOI) {
                    this.token = 20;
                } else {
                    this.bp = startPos;
                    this.ch = startChar;
                    this.matchStat = -1;
                    return 0;
                }
                this.matchStat = 4;
                if (negative) {
                    return -value;
                }
                return value;
            } else {
                this.matchStat = -1;
                return 0;
            }
        }
        this.matchStat = -2;
        return 0;
    }

    public boolean scanFieldBoolean(char[] fieldName) {
        this.matchStat = 0;
        if (charArrayCompare(this.text, this.bp, fieldName)) {
            boolean value;
            int index = this.bp + fieldName.length;
            int index2 = index + 1;
            char ch = charAt(index);
            if (ch == 't') {
                index = index2 + 1;
                if (charAt(index2) != 'r') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'u') {
                    this.matchStat = -1;
                    return false;
                }
                index = index2 + 1;
                if (charAt(index2) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                this.bp = index;
                ch = charAt(this.bp);
                value = true;
            } else if (ch == 'f') {
                index = index2 + 1;
                if (charAt(index2) != 'a') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'l') {
                    this.matchStat = -1;
                    return false;
                }
                index = index2 + 1;
                if (charAt(index2) != 's') {
                    this.matchStat = -1;
                    return false;
                }
                index2 = index + 1;
                if (charAt(index) != 'e') {
                    this.matchStat = -1;
                    return false;
                }
                this.bp = index2;
                ch = charAt(this.bp);
                value = false;
                index = index2;
            } else {
                this.matchStat = -1;
                return false;
            }
            int i;
            if (ch == ',') {
                i = this.bp + 1;
                this.bp = i;
                this.ch = charAt(i);
                this.matchStat = 3;
                this.token = 16;
                return value;
            } else if (ch == '}') {
                int i2 = this.bp + 1;
                this.bp = i2;
                ch = charAt(i2);
                if (ch == ',') {
                    this.token = 16;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == ']') {
                    this.token = 15;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == '}') {
                    this.token = 13;
                    i = this.bp + 1;
                    this.bp = i;
                    this.ch = charAt(i);
                } else if (ch == JSONLexer.EOI) {
                    this.token = 20;
                } else {
                    this.matchStat = -1;
                    return false;
                }
                this.matchStat = 4;
                return value;
            } else {
                this.matchStat = -1;
                return false;
            }
        }
        this.matchStat = -2;
        return false;
    }

    public final int scanInt(char expectNext) {
        boolean negative;
        this.matchStat = 0;
        int offset = this.bp;
        int offset2 = offset + 1;
        char chLocal = charAt(offset);
        if (chLocal == '-') {
            negative = true;
        } else {
            negative = false;
        }
        if (negative) {
            offset = offset2 + 1;
            chLocal = charAt(offset2);
        } else {
            offset = offset2;
        }
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            return 0;
        }
        int value = chLocal - 48;
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (value * 10) + (chLocal - 48);
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            offset = offset2;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            offset = offset2;
            return 0;
        } else {
            while (chLocal != expectNext) {
                if (JSONLexerBase.isWhitespace(chLocal)) {
                    offset = offset2 + 1;
                    chLocal = charAt(offset2);
                    offset2 = offset;
                } else {
                    this.matchStat = -1;
                    if (negative) {
                        value = -value;
                    }
                    offset = offset2;
                    return value;
                }
            }
            this.bp = offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                value = -value;
            }
            offset = offset2;
            return value;
        }
    }

    public long scanLong(char expectNextChar) {
        this.matchStat = 0;
        int offset = this.bp;
        int offset2 = offset + 1;
        char chLocal = charAt(offset);
        boolean negative = chLocal == '-';
        if (negative) {
            offset = offset2 + 1;
            chLocal = charAt(offset2);
        } else {
            offset = offset2;
        }
        if (chLocal < '0' || chLocal > '9') {
            this.matchStat = -1;
            return 0;
        }
        long value = (long) (chLocal - 48);
        while (true) {
            offset2 = offset + 1;
            chLocal = charAt(offset);
            if (chLocal >= '0' && chLocal <= '9') {
                value = (10 * value) + ((long) (chLocal - 48));
                offset = offset2;
            }
        }
        if (chLocal == '.') {
            this.matchStat = -1;
            offset = offset2;
            return 0;
        } else if (value < 0) {
            this.matchStat = -1;
            offset = offset2;
            return 0;
        } else {
            while (chLocal != expectNextChar) {
                if (JSONLexerBase.isWhitespace(chLocal)) {
                    offset = offset2 + 1;
                    chLocal = charAt(offset2);
                    offset2 = offset;
                } else {
                    this.matchStat = -1;
                    offset = offset2;
                    return value;
                }
            }
            this.bp = offset2;
            this.ch = charAt(this.bp);
            this.matchStat = 3;
            this.token = 16;
            if (negative) {
                value = -value;
            }
            offset = offset2;
            return value;
        }
    }

    protected final void arrayCopy(int srcPos, char[] dest, int destPos, int length) {
        this.text.getChars(srcPos, srcPos + length, dest, destPos);
    }

    public String info() {
        String str;
        StringBuilder append = new StringBuilder().append("pos ").append(this.bp).append(", json : ");
        if (this.text.length() < SendFlag.FLAG_KEY_PINGBACK_ST) {
            str = this.text;
        } else {
            str = this.text.substring(0, SendFlag.FLAG_KEY_PINGBACK_ST);
        }
        return append.append(str).toString();
    }
}
