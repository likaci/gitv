package com.gala.video.lib.share.uikit.utils;

import android.graphics.drawable.Drawable;

public class TypeUtils {
    public static final Boolean castToBoolean(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        if (value instanceof Integer) {
            return Boolean.valueOf(((Integer) value).intValue() > 0);
        } else if (value instanceof String) {
            return Boolean.valueOf((String) value);
        } else {
            throw new RuntimeException("can not cast to Boolean, value : " + value);
        }
    }

    public static final Long castToLong(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Long) {
            return (Long) value;
        }
        if (value instanceof Integer) {
            return Long.valueOf(((Integer) value).longValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            return Long.valueOf(Long.parseLong(strVal));
        }
        throw new RuntimeException("can not cast to Long, value : " + value);
    }

    public static final int castToInt(Object value) {
        if (value == null) {
            return 0;
        }
        if (value instanceof Integer) {
            return ((Integer) value).intValue();
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() != 0) {
                return Integer.parseInt(strVal);
            }
            return 0;
        }
        throw new RuntimeException("can not cast to int, value : " + value);
    }

    public static final Short castToShort(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Short) {
            return (Short) value;
        }
        if (value instanceof Number) {
            return Short.valueOf(((Number) value).shortValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            return Short.valueOf(Short.parseShort(strVal));
        }
        throw new RuntimeException("can not cast to castToShort, value : " + value);
    }

    public static final Float castToFloat(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Float) {
            return (Float) value;
        }
        if (value instanceof Number) {
            return Float.valueOf(((Number) value).floatValue());
        }
        if (value instanceof String) {
            String strVal = (String) value;
            if (strVal.length() == 0) {
                return null;
            }
            return Float.valueOf(Float.parseFloat(strVal));
        }
        throw new RuntimeException("can not cast to castToFloat, value : " + value);
    }

    public static final String castToString(Object value) {
        if (value == null) {
            return null;
        }
        return String.valueOf(value);
    }

    public static final Drawable castToDrawable(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Drawable) {
            return (Drawable) value;
        }
        throw new RuntimeException("can not cast to drawable, value : " + value);
    }
}
