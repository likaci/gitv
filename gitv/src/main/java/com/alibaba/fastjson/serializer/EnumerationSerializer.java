package com.alibaba.fastjson.serializer;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Enumeration;

public class EnumerationSerializer implements ObjectSerializer {
    public static EnumerationSerializer instance = new EnumerationSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        Throwable th;
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        Type elementType = null;
        if (out.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType)) {
            elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        }
        Enumeration<?> e = (Enumeration) object;
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        try {
            out.append('[');
            int i = 0;
            while (e.hasMoreElements()) {
                int i2;
                try {
                    Object item = e.nextElement();
                    i2 = i + 1;
                    if (i != 0) {
                        out.append(',');
                    }
                    if (item == null) {
                        out.writeNull();
                        i = i2;
                    } else {
                        serializer.getObjectWriter(item.getClass()).write(serializer, item, Integer.valueOf(i2 - 1), elementType, 0);
                        i = i2;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    i2 = i;
                }
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
