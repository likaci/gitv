package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public class CollectionCodec implements ObjectSerializer, ObjectDeserializer {
    public static final CollectionCodec instance = new CollectionCodec();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull(SerializerFeature.WriteNullListAsEmpty);
            return;
        }
        Type elementType = null;
        if (out.isEnabled(SerializerFeature.WriteClassName) && (fieldType instanceof ParameterizedType)) {
            elementType = ((ParameterizedType) fieldType).getActualTypeArguments()[0];
        }
        Collection<?> collection = (Collection) object;
        SerialContext context = serializer.context;
        serializer.setContext(context, object, fieldName, 0);
        if (out.isEnabled(SerializerFeature.WriteClassName)) {
            if (HashSet.class == collection.getClass()) {
                out.append((CharSequence) "Set");
            } else if (TreeSet.class == collection.getClass()) {
                out.append((CharSequence) "TreeSet");
            }
        }
        out.append('[');
        int i = 0;
        for (Object item : collection) {
            try {
                int i2 = i + 1;
                if (i != 0) {
                    out.append(',');
                }
                if (item == null) {
                    out.writeNull();
                    i = i2;
                } else {
                    try {
                        Class<?> clazz = item.getClass();
                        if (clazz == Integer.class) {
                            out.writeInt(((Integer) item).intValue());
                            i = i2;
                        } else if (clazz == Long.class) {
                            out.writeLong(((Long) item).longValue());
                            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                                out.write(76);
                                i = i2;
                            } else {
                                i = i2;
                            }
                        } else {
                            serializer.getObjectWriter(clazz).write(serializer, item, Integer.valueOf(i2 - 1), elementType, 0);
                            i = i2;
                        }
                    } catch (Throwable th) {
                        Throwable th2 = th;
                    }
                }
            } catch (Throwable th3) {
                th2 = th3;
                i2 = i;
            }
        }
        out.append(']');
        serializer.context = context;
        return;
        serializer.context = context;
        throw th2;
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (parser.lexer.token() == 8) {
            parser.lexer.nextToken(16);
            return null;
        } else if (type == JSONArray.class) {
            Collection array = new JSONArray();
            parser.parseArray(array);
            return array;
        } else {
            T list = TypeUtils.createCollection(type);
            Type itemType = null;
            if (type instanceof ParameterizedType) {
                itemType = ((ParameterizedType) type).getActualTypeArguments()[0];
            } else {
                if (type instanceof Class) {
                    Class<?> clazz = (Class) type;
                    if (!clazz.getName().startsWith("java.")) {
                        Type superClass = clazz.getGenericSuperclass();
                        if (superClass instanceof ParameterizedType) {
                            itemType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
                        }
                    }
                }
                if (itemType == null) {
                    itemType = Object.class;
                }
            }
            parser.parseArray(itemType, list, fieldName);
            return list;
        }
    }

    public int getFastMatchToken() {
        return 14;
    }
}
