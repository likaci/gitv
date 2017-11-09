package org.xbill.DNS;

import java.util.HashMap;

class Mnemonic {
    static final int CASE_LOWER = 3;
    static final int CASE_SENSITIVE = 1;
    static final int CASE_UPPER = 2;
    private static Integer[] cachedInts = new Integer[64];
    private String description;
    private int max = Integer.MAX_VALUE;
    private boolean numericok;
    private String prefix;
    private HashMap strings = new HashMap();
    private HashMap values = new HashMap();
    private int wordcase;

    static {
        for (int i = 0; i < cachedInts.length; i++) {
            cachedInts[i] = new Integer(i);
        }
    }

    public Mnemonic(String description, int wordcase) {
        this.description = description;
        this.wordcase = wordcase;
    }

    public void setMaximum(int max) {
        this.max = max;
    }

    public void setPrefix(String prefix) {
        this.prefix = sanitize(prefix);
    }

    public void setNumericAllowed(boolean numeric) {
        this.numericok = numeric;
    }

    public static Integer toInteger(int val) {
        if (val < 0 || val >= cachedInts.length) {
            return new Integer(val);
        }
        return cachedInts[val];
    }

    public void check(int val) {
        if (val < 0 || val > this.max) {
            throw new IllegalArgumentException(new StringBuffer().append(this.description).append(" ").append(val).append("is out of range").toString());
        }
    }

    private String sanitize(String str) {
        if (this.wordcase == 2) {
            return str.toUpperCase();
        }
        if (this.wordcase == 3) {
            return str.toLowerCase();
        }
        return str;
    }

    private int parseNumeric(String s) {
        try {
            int val = Integer.parseInt(s);
            if (val >= 0 && val <= this.max) {
                return val;
            }
        } catch (NumberFormatException e) {
        }
        return -1;
    }

    public void add(int val, String str) {
        check(val);
        Integer value = toInteger(val);
        str = sanitize(str);
        this.strings.put(str, value);
        this.values.put(value, str);
    }

    public void addAlias(int val, String str) {
        check(val);
        Integer value = toInteger(val);
        this.strings.put(sanitize(str), value);
    }

    public void addAll(Mnemonic source) {
        if (this.wordcase != source.wordcase) {
            throw new IllegalArgumentException(new StringBuffer().append(source.description).append(": wordcases do not match").toString());
        }
        this.strings.putAll(source.strings);
        this.values.putAll(source.values);
    }

    public String getText(int val) {
        check(val);
        String str = (String) this.values.get(toInteger(val));
        if (str != null) {
            return str;
        }
        str = Integer.toString(val);
        return this.prefix != null ? new StringBuffer().append(this.prefix).append(str).toString() : str;
    }

    public int getValue(String str) {
        str = sanitize(str);
        Integer value = (Integer) this.strings.get(str);
        if (value != null) {
            return value.intValue();
        }
        if (this.prefix != null && str.startsWith(this.prefix)) {
            int val = parseNumeric(str.substring(this.prefix.length()));
            if (val >= 0) {
                return val;
            }
        }
        if (this.numericok) {
            return parseNumeric(str);
        }
        return -1;
    }
}
