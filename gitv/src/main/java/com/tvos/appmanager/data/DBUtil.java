package com.tvos.appmanager.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class DBUtil {
    private static final String TAG = "DBUtil";

    public static ContentValues getContentValues(Object obj, boolean needTransferInnerObj) {
        ContentValues values = new ContentValues();
        transferObjToValues(obj, values, needTransferInnerObj);
        return values;
    }

    public static void getObject(Object obj, Cursor values, boolean needTransferInnerObj) {
        transferValuesToObj(obj, values, needTransferInnerObj);
    }

    private static void transferObjToValues(Object obj, ContentValues values, boolean needTransferInnerObj) {
        if (obj != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            if (fields.length != 0) {
                for (Field field : fields) {
                    try {
                        if (!Modifier.isStatic(field.getModifiers())) {
                            field.setAccessible(true);
                            Object fieldValue = field.get(obj);
                            if (!putValueToContentValues(field.getName(), fieldValue, values) && needTransferInnerObj) {
                                transferObjToValues(fieldValue, values, needTransferInnerObj);
                            }
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e2) {
                        e2.printStackTrace();
                    }
                }
            }
        }
    }

    private static void transferValuesToObj(Object obj, Cursor values, boolean needTransferInnerObj) {
        if (obj != null && values != null) {
            Field[] fields = obj.getClass().getDeclaredFields();
            if (fields.length != 0) {
                for (Field field : fields) {
                    if (!Modifier.isStatic(field.getModifiers())) {
                        field.setAccessible(true);
                        if (!setValueFromContentValues(field, obj, values) && needTransferInnerObj) {
                            try {
                                Object innerObject = field.getType().newInstance();
                                transferValuesToObj(innerObject, values, needTransferInnerObj);
                                field.set(obj, innerObject);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e2) {
                                e2.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean putValueToContentValues(String fieldName, Object value, ContentValues values) {
        if (value == null || values == null) {
            return true;
        }
        String className = value.getClass().getSimpleName();
        if (className.equalsIgnoreCase("String")) {
            values.put(fieldName, (String) value);
            return true;
        } else if (className.equalsIgnoreCase("Integer")) {
            values.put(fieldName, (Integer) value);
            return true;
        } else if (className.equalsIgnoreCase("Long")) {
            values.put(fieldName, (Long) value);
            return true;
        } else if (className.equalsIgnoreCase("Boolean")) {
            values.put(fieldName, (Boolean) value);
            return true;
        } else if (className.equalsIgnoreCase("Short")) {
            values.put(fieldName, (Short) value);
            return true;
        } else if (className.equalsIgnoreCase("Float")) {
            values.put(fieldName, (Float) value);
            return true;
        } else if (className.equalsIgnoreCase("Double")) {
            values.put(fieldName, (Double) value);
            return true;
        } else if (className.equalsIgnoreCase("Byte")) {
            values.put(fieldName, (Byte) value);
            return true;
        } else if (!className.equalsIgnoreCase("byte[]")) {
            return false;
        } else {
            values.put(fieldName, (byte[]) value);
            return true;
        }
    }

    private static boolean setValueFromContentValues(Field field, Object obj, Cursor values) {
        if (obj == null || values == null) {
            return true;
        }
        String fieldName = field.getName();
        String className = field.getType().getSimpleName();
        boolean result = false;
        Object value = null;
        int index = values.getColumnIndex(fieldName);
        if (index == -1) {
            Log.d(TAG, new StringBuilder(String.valueOf(fieldName)).append(" does't exist").toString());
            return true;
        }
        if (className.equalsIgnoreCase("String")) {
            value = values.getString(index);
            result = true;
        } else if (className.equalsIgnoreCase("int")) {
            value = Integer.valueOf(values.getInt(index));
            result = true;
        } else if (className.equalsIgnoreCase("long")) {
            value = Long.valueOf(values.getLong(index));
            result = true;
        } else if (className.equalsIgnoreCase("boolean")) {
            if (values.getInt(index) == 0) {
                value = Boolean.valueOf(false);
            } else {
                value = Boolean.valueOf(true);
            }
            result = true;
        } else if (className.equalsIgnoreCase("short")) {
            value = Short.valueOf(values.getShort(index));
            result = true;
        } else if (className.equalsIgnoreCase("float")) {
            value = Float.valueOf(values.getFloat(index));
            result = true;
        } else if (className.equalsIgnoreCase("double")) {
            value = Double.valueOf(values.getDouble(index));
            result = true;
        } else if (className.equalsIgnoreCase("byte")) {
            value = Byte.valueOf((byte) (values.getInt(index) & 255));
            result = true;
        } else if (className.equalsIgnoreCase("byte[]")) {
            value = values.getBlob(index);
            result = true;
        }
        if (!result) {
            return result;
        }
        try {
            field.set(obj, value);
            return result;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return result;
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
            return result;
        }
    }
}
