package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class DefaultFieldDeserializer extends FieldDeserializer {
    private ObjectDeserializer fieldValueDeserilizer;

    public DefaultFieldDeserializer(ParserConfig mapping, Class<?> clazz, FieldInfo fieldInfo) {
        super(clazz, fieldInfo);
    }

    public ObjectDeserializer getFieldValueDeserilizer(ParserConfig config) {
        if (this.fieldValueDeserilizer == null) {
            this.fieldValueDeserilizer = config.getDeserializer(this.fieldInfo.fieldClass, this.fieldInfo.fieldType);
        }
        return this.fieldValueDeserilizer;
    }

    public void parseField(DefaultJSONParser parser, Object object, Type objectType, Map<String, Object> fieldValues) {
        if (this.fieldValueDeserilizer == null) {
            this.fieldValueDeserilizer = parser.getConfig().getDeserializer(this.fieldInfo);
        }
        if (objectType instanceof ParameterizedType) {
            parser.getContext().type = objectType;
        }
        Object value = this.fieldValueDeserilizer.deserialze(parser, this.fieldInfo.fieldType, this.fieldInfo.name);
        if (parser.getResolveStatus() == 1) {
            ResolveTask task = parser.getLastResolveTask();
            task.fieldDeserializer = this;
            task.ownerContext = parser.getContext();
            parser.setResolveStatus(0);
        } else if (object == null) {
            fieldValues.put(this.fieldInfo.name, value);
        } else {
            setValue(object, value);
        }
    }

    public int getFastMatchToken() {
        if (this.fieldValueDeserilizer != null) {
            return this.fieldValueDeserilizer.getFastMatchToken();
        }
        return 2;
    }
}
