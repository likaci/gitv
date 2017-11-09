package com.tvos.apps.utils;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.tvos.apps.utils.db.Constraints;
import com.tvos.apps.utils.db.ConstraintsAnnotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DBUtil {
    private static final String TAG = DBUtil.class.getSimpleName();

    public static ContentValues getContentValues(Object obj, boolean needTransferInnerObj) {
        ContentValues values = new ContentValues();
        transferObjToValues(obj, values, needTransferInnerObj);
        return values;
    }

    public static String createTableSql(Class<?> clazz) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CREATE TABLE IF NOT EXISTS " + clazz.getSimpleName() + "(");
        Field[] fields = clazz.getDeclaredFields();
        if (fields.length == 0) {
            return "";
        }
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    String className = field.getType().getSimpleName();
                    stringBuilder.append(field.getName());
                    if (className.equalsIgnoreCase("String")) {
                        stringBuilder.append(" TEXT");
                    } else if (className.equalsIgnoreCase("int")) {
                        stringBuilder.append(" INTEGER");
                    } else if (className.equalsIgnoreCase("long")) {
                        stringBuilder.append(" INTEGER");
                    } else if (className.equalsIgnoreCase("boolean")) {
                        stringBuilder.append(" INTEGER");
                    } else if (className.equalsIgnoreCase("short")) {
                        stringBuilder.append(" INTEGER");
                    } else if (className.equalsIgnoreCase("float")) {
                        stringBuilder.append(" REAL");
                    } else if (className.equalsIgnoreCase("double")) {
                        stringBuilder.append(" REAL");
                    } else if (className.equalsIgnoreCase("byte")) {
                        stringBuilder.append(" INTEGER");
                    } else if (className.equalsIgnoreCase("byte[]")) {
                        stringBuilder.append(" BLOB");
                    }
                    if (field.isAnnotationPresent(ConstraintsAnnotation.class)) {
                        ConstraintsAnnotation constraintsAnnotation = (ConstraintsAnnotation) field.getAnnotation(ConstraintsAnnotation.class);
                        Constraints[] nullParamConstraints = constraintsAnnotation.nullParamsConstraints();
                        if (!(nullParamConstraints == null || nullParamConstraints.length == 0)) {
                            for (Constraints constraints : nullParamConstraints) {
                                stringBuilder.append(" ");
                                stringBuilder.append(constraints.getName());
                            }
                        }
                        String checkConstraint = constraintsAnnotation.checkIn();
                        if (!(checkConstraint == null || checkConstraint.isEmpty())) {
                            stringBuilder.append(" CHECK ");
                            stringBuilder.append(checkConstraint);
                        }
                        String defaultValueConstraint = constraintsAnnotation.defaultValue();
                        if (!(defaultValueConstraint == null || defaultValueConstraint.isEmpty())) {
                            stringBuilder.append(" DEFAULT ");
                            stringBuilder.append(checkConstraint);
                        }
                    }
                    if (i != fields.length - 1) {
                        stringBuilder.append(" ,");
                    }
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        stringBuilder.append(")");
        Log.d(TAG, stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static String[] getTableColumns(Class<?> tableClass) {
        Field[] fields = tableClass.getDeclaredFields();
        if (fields.length == 0) {
            return null;
        }
        String[] columns = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            try {
                field.setAccessible(true);
                if (!Modifier.isStatic(field.getModifiers())) {
                    columns[i] = field.getName();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return columns;
    }

    public static synchronized void upgradeDB(SQLiteDatabase db, Class<?> tableClass) {
        synchronized (DBUtil.class) {
            Log.d(TAG, "onUpgrade");
            String tableName = tableClass.getSimpleName();
            String createTableSql = createTableSql(tableClass);
            String[] columns = getTableColumns(tableClass);
            String tempTableName = "temp_" + tableName;
            db.execSQL("ALTER TABLE " + tableName + " RENAME TO " + tempTableName);
            Log.d(TAG, "rename");
            db.execSQL(createTableSql);
            Log.d(TAG, "onCreate");
            String[] columnNames = db.query(tempTableName, null, null, null, null, null, null).getColumnNames();
            List<String> matchedColumns = new ArrayList();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < columnNames.length; i++) {
                int j = 0;
                while (j < columns.length) {
                    if (columnNames[i].equals(columns[j])) {
                        if (!matchedColumns.contains(columnNames[i])) {
                            matchedColumns.add(columnNames[i]);
                        }
                    } else {
                        j++;
                    }
                }
            }
            for (int k = 0; k < matchedColumns.size(); k++) {
                if (k != matchedColumns.size() - 1) {
                    builder.append(new StringBuilder(String.valueOf((String) matchedColumns.get(k))).append(", ").toString());
                } else {
                    builder.append(new StringBuilder(String.valueOf((String) matchedColumns.get(k))).append(" ").toString());
                }
            }
            db.execSQL("INSERT INTO " + tableName + "(" + builder.toString() + ") SELECT " + builder.toString() + " FROM " + tempTableName);
            Log.d(TAG, "copy data");
            db.execSQL("DROP TABLE IF EXISTS " + tempTableName + ";");
            Log.d(TAG, "delete tmp");
        }
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
                        field.setAccessible(true);
                        if (!Modifier.isStatic(field.getModifiers())) {
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
                    field.setAccessible(true);
                    if (!(Modifier.isStatic(field.getModifiers()) || setValueFromContentValues(field, obj, values) || !needTransferInnerObj)) {
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
            return false;
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
