package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public final class ListSerializer implements ObjectSerializer {
    public static final ListSerializer instance = new ListSerializer();

    public final void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        ObjectSerializer itemSerializer;
        Throwable th;
        boolean writeClassName = serializer.out.isEnabled(SerializerFeature.WriteClassName);
        SerializeWriter out = serializer.out;
        Type elementType = null;
        if (writeClassName && (fieldType instanceof ParameterizedType)) {
            elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        }
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        List<?> list = (List) object;
        if (list.size() == 0) {
            out.append((CharSequence) "[]");
            return;
        }
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        try {
            int i;
            Object item;
            ObjectSerializer objectSerializer;
            if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                out.append('[');
                serializer.incrementIndent();
                i = 0;
                for (Object item2 : list) {
                    if (i != 0) {
                        out.append(',');
                    }
                    serializer.println();
                    if (item2 == null) {
                        serializer.out.writeNull();
                    } else if (serializer.containsReference(item2)) {
                        serializer.writeReference(item2);
                    } else {
                        itemSerializer = serializer.getObjectWriter(item2.getClass());
                        try {
                            serializer.context = new SerialContext(context, object, fieldName, 0, 0);
                            itemSerializer.write(serializer, item2, Integer.valueOf(i), elementType, 0);
                            objectSerializer = itemSerializer;
                        } catch (Throwable th2) {
                            th = th2;
                            objectSerializer = itemSerializer;
                        }
                    }
                    i++;
                }
                serializer.decrementIdent();
                serializer.println();
                out.append(']');
                serializer.context = context;
                return;
            }
            out.append('[');
            i = 0;
            int size = list.size();
            itemSerializer = null;
            while (i < size) {
                item2 = list.get(i);
                if (i != 0) {
                    out.append(',');
                }
                if (item2 == null) {
                    out.append((CharSequence) "null");
                    objectSerializer = itemSerializer;
                } else {
                    Class<?> clazz = item2.getClass();
                    if (clazz == Integer.class) {
                        out.writeInt(((Integer) item2).intValue());
                        objectSerializer = itemSerializer;
                    } else if (clazz == Long.class) {
                        long val = ((Long) item2).longValue();
                        if (writeClassName) {
                            out.writeLong(val);
                            out.write(76);
                        } else {
                            out.writeLong(val);
                        }
                        objectSerializer = itemSerializer;
                    } else {
                        if (!out.disableCircularReferenceDetect) {
                            serializer.context = new SerialContext(context, object, fieldName, 0, 0);
                        }
                        if (serializer.containsReference(item2)) {
                            serializer.writeReference(item2);
                            objectSerializer = itemSerializer;
                        } else {
                            objectSerializer = serializer.getObjectWriter(item2.getClass());
                            objectSerializer.write(serializer, item2, Integer.valueOf(i), elementType, 0);
                        }
                    }
                }
                i++;
                itemSerializer = objectSerializer;
            }
            out.append(']');
            serializer.context = context;
            return;
        } catch (Throwable th3) {
            th = th3;
        }
        serializer.context = context;
        throw th;
    }
}
