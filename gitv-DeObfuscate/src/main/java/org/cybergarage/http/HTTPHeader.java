package org.cybergarage.http;

import java.io.LineNumberReader;
import java.io.StringReader;
import org.cybergarage.util.Debug;

public class HTTPHeader {
    private static int MAX_LENGTH = 1024;
    private String name;
    private String value;

    public HTTPHeader(String name, String value) {
        setName(name);
        setValue(value);
    }

    public HTTPHeader(String lineStr) {
        setName("");
        setValue("");
        if (lineStr != null) {
            int colonIdx = lineStr.indexOf(58);
            if (colonIdx >= 0) {
                String name = lineStr.substring(0, colonIdx);
                String value = lineStr.substring(colonIdx + 1, lineStr.length());
                setName(name.trim());
                setValue(value.trim());
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public boolean hasName() {
        if (this.name == null || this.name.length() <= 0) {
            return false;
        }
        return true;
    }

    private static String getValueFromLine(String line, String name) {
        if (line == null) {
            return "";
        }
        int index = line.indexOf(58);
        if (index < 0) {
            return "";
        }
        if (line.substring(0, index).toUpperCase().trim().equals(name.toUpperCase().trim())) {
            return line.substring(index + 1, line.length()).trim();
        }
        return "";
    }

    public static final String getValue(LineNumberReader reader, String name) {
        try {
            String lineStr = reader.readLine();
            while (lineStr != null && lineStr.length() > 0) {
                String result = getValueFromLine(lineStr, name);
                if (!result.equals("")) {
                    return result;
                }
                lineStr = reader.readLine();
            }
            return "";
        } catch (Exception e) {
            Debug.warning(e);
            return "";
        }
    }

    public static final String getValue(String data, String name) {
        return getValue(new LineNumberReader(new StringReader(data), Math.min(data.length(), MAX_LENGTH)), name);
    }

    public static final String getValue(byte[] data, String name) {
        return getValue(new String(data), name);
    }

    public static final int getIntegerValue(String data, String name) {
        try {
            return Integer.parseInt(getValue(data, name));
        } catch (Exception e) {
            return 0;
        }
    }

    public static final int getIntegerValue(byte[] data, String name) {
        try {
            return Integer.parseInt(getValue(data, name));
        } catch (Exception e) {
            return 0;
        }
    }
}
