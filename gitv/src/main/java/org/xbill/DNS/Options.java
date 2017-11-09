package org.xbill.DNS;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public final class Options {
    private static Map table;

    static {
        try {
            refresh();
        } catch (SecurityException e) {
        }
    }

    private Options() {
    }

    public static void refresh() {
        String s = System.getProperty("dnsjava.options");
        if (s != null) {
            StringTokenizer st = new StringTokenizer(s, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                int index = token.indexOf(61);
                if (index == -1) {
                    set(token);
                } else {
                    set(token.substring(0, index), token.substring(index + 1));
                }
            }
        }
    }

    public static void clear() {
        table = null;
    }

    public static void set(String option) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(option.toLowerCase(), "true");
    }

    public static void set(String option, String value) {
        if (table == null) {
            table = new HashMap();
        }
        table.put(option.toLowerCase(), value.toLowerCase());
    }

    public static void unset(String option) {
        if (table != null) {
            table.remove(option.toLowerCase());
        }
    }

    public static boolean check(String option) {
        if (table == null || table.get(option.toLowerCase()) == null) {
            return false;
        }
        return true;
    }

    public static String value(String option) {
        if (table == null) {
            return null;
        }
        return (String) table.get(option.toLowerCase());
    }

    public static int intValue(String option) {
        String s = value(option);
        if (s != null) {
            try {
                int val = Integer.parseInt(s);
                if (val > 0) {
                    return val;
                }
            } catch (NumberFormatException e) {
            }
        }
        return -1;
    }
}
