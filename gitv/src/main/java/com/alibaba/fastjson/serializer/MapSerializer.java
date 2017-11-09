package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.xbill.DNS.WKSRecord.Service;

public class MapSerializer extends SerializeFilterable implements ObjectSerializer {
    public static MapSerializer instance = new MapSerializer();

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        Map<?, ?> map = (Map) object;
        if (serializer.containsReference(object)) {
            serializer.writeReference(object);
            return;
        }
        SerialContext parent = serializer.context;
        serializer.setContext(parent, object, fieldName, 0);
        try {
            out.write((int) Service.NTP);
            serializer.incrementIndent();
            Class<?> preClazz = null;
            ObjectSerializer preWriter = null;
            boolean first = true;
            if (out.isEnabled(SerializerFeature.WriteClassName)) {
                boolean containsKey;
                Class<?> mapClass = map.getClass();
                if (mapClass == JSONObject.class || mapClass == HashMap.class || mapClass == LinkedHashMap.class) {
                    if (map.containsKey(JSON.DEFAULT_TYPE_KEY)) {
                        containsKey = true;
                        if (!containsKey) {
                            out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                            out.writeString(object.getClass().getName());
                            first = false;
                        }
                    }
                }
                containsKey = false;
                if (containsKey) {
                    out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
                    out.writeString(object.getClass().getName());
                    first = false;
                }
            }
            for (Entry entry : map.entrySet()) {
                Object value = entry.getValue();
                Object entryKey = entry.getKey();
                List<PropertyPreFilter> preFilters = serializer.propertyPreFilters;
                if (preFilters != null && preFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        if (!serializer.applyName(this, object, (String) entryKey)) {
                        }
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        if (!serializer.applyName(this, object, JSON.toJSONString(entryKey))) {
                        }
                    }
                }
                List<PropertyFilter> propertyFilters = serializer.propertyFilters;
                if (propertyFilters != null && propertyFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        if (!serializer.apply(this, object, (String) entryKey, value)) {
                        }
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        if (!serializer.apply(this, object, JSON.toJSONString(entryKey), value)) {
                        }
                    }
                }
                List<NameFilter> nameFilters = serializer.nameFilters;
                if (nameFilters != null && nameFilters.size() > 0) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        entryKey = serializer.processKey(this, object, (String) entryKey, value);
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        entryKey = serializer.processKey(this, object, JSON.toJSONString(entryKey), value);
                    }
                }
                List<ValueFilter> valueFilters = serializer.valueFilters;
                List<ContextValueFilter> contextValueFilters = serializer.contextValueFilters;
                if ((valueFilters != null && valueFilters.size() > 0) || (contextValueFilters != null && contextValueFilters.size() > 0)) {
                    if (entryKey == null || (entryKey instanceof String)) {
                        value = serializer.processValue(this, null, object, (String) entryKey, value);
                    } else if (entryKey.getClass().isPrimitive() || (entryKey instanceof Number)) {
                        value = serializer.processValue(this, null, object, JSON.toJSONString(entryKey), value);
                    }
                }
                if (value == null) {
                    if (!out.isEnabled(SerializerFeature.WriteMapNullValue)) {
                    }
                }
                if (entryKey instanceof String) {
                    String key = (String) entryKey;
                    if (!first) {
                        out.write(44);
                    }
                    if (out.isEnabled(SerializerFeature.PrettyFormat)) {
                        serializer.println();
                    }
                    out.writeFieldName(key, true);
                } else {
                    if (!first) {
                        out.write(44);
                    }
                    if (!out.isEnabled(SerializerFeature.BrowserCompatible)) {
                        if (!out.isEnabled(SerializerFeature.WriteNonStringKeyAsString)) {
                            if (!out.isEnabled(SerializerFeature.BrowserSecure)) {
                                serializer.write(entryKey);
                                out.write(58);
                            }
                        }
                    }
                    serializer.write(JSON.toJSONString(entryKey));
                    out.write(58);
                }
                first = false;
                if (value == null) {
                    out.writeNull();
                } else {
                    Class<?> clazz = value.getClass();
                    if (clazz == preClazz) {
                        preWriter.write(serializer, value, entryKey, null, 0);
                    } else {
                        preClazz = clazz;
                        preWriter = serializer.getObjectWriter(clazz);
                        preWriter.write(serializer, value, entryKey, null, 0);
                    }
                }
            }
            serializer.decrementIdent();
            if (out.isEnabled(SerializerFeature.PrettyFormat) && map.size() > 0) {
                serializer.println();
            }
            out.write((int) Service.LOCUS_MAP);
        } finally {
            serializer.context = parent;
        }
    }
}
