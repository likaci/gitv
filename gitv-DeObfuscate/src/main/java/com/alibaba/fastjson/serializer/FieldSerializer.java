package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.util.FieldInfo;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import org.cybergarage.soap.SOAP;

public class FieldSerializer implements Comparable<FieldSerializer> {
    private final String double_quoted_fieldPrefix;
    protected int features;
    protected BeanContext fieldContext;
    public final FieldInfo fieldInfo;
    private String format;
    private RuntimeSerializerInfo runtimeInfo;
    private String single_quoted_fieldPrefix;
    private String un_quoted_fieldPrefix;
    protected boolean writeEnumUsingName = false;
    protected boolean writeEnumUsingToString = false;
    protected final boolean writeNull;

    static class RuntimeSerializerInfo {
        ObjectSerializer fieldSerializer;
        Class<?> runtimeFieldClass;

        public RuntimeSerializerInfo(ObjectSerializer fieldSerializer, Class<?> runtimeFieldClass) {
            this.fieldSerializer = fieldSerializer;
            this.runtimeFieldClass = runtimeFieldClass;
        }
    }

    public FieldSerializer(Class<?> beanType, FieldInfo fieldInfo) {
        int i = 0;
        this.fieldInfo = fieldInfo;
        this.fieldContext = new BeanContext(beanType, fieldInfo);
        fieldInfo.setAccessible();
        this.double_quoted_fieldPrefix = '\"' + fieldInfo.name + "\":";
        boolean writeNull = false;
        JSONField annotation = fieldInfo.getAnnotation();
        if (annotation != null) {
            SerializerFeature feature;
            for (SerializerFeature feature2 : annotation.serialzeFeatures()) {
                if (feature2 == SerializerFeature.WriteMapNullValue) {
                    writeNull = true;
                    break;
                }
            }
            this.format = annotation.format();
            if (this.format.trim().length() == 0) {
                this.format = null;
            }
            SerializerFeature[] serialzeFeatures = annotation.serialzeFeatures();
            int length = serialzeFeatures.length;
            while (i < length) {
                feature2 = serialzeFeatures[i];
                if (feature2 == SerializerFeature.WriteEnumUsingToString) {
                    this.writeEnumUsingToString = true;
                } else if (feature2 == SerializerFeature.WriteEnumUsingName) {
                    this.writeEnumUsingName = true;
                }
                i++;
            }
            this.features = SerializerFeature.of(annotation.serialzeFeatures());
        }
        this.writeNull = writeNull;
    }

    public void writePrefix(JSONSerializer serializer) throws IOException {
        SerializeWriter out = serializer.out;
        if (!out.quoteFieldNames) {
            if (this.un_quoted_fieldPrefix == null) {
                this.un_quoted_fieldPrefix = this.fieldInfo.name + SOAP.DELIM;
            }
            out.write(this.un_quoted_fieldPrefix);
        } else if (out.useSingleQuotes) {
            if (this.single_quoted_fieldPrefix == null) {
                this.single_quoted_fieldPrefix = '\'' + this.fieldInfo.name + "':";
            }
            out.write(this.single_quoted_fieldPrefix);
        } else {
            out.write(this.double_quoted_fieldPrefix);
        }
    }

    public Object getPropertyValue(Object object) throws InvocationTargetException, IllegalAccessException {
        return this.fieldInfo.get(object);
    }

    public int compareTo(FieldSerializer o) {
        return this.fieldInfo.compareTo(o.fieldInfo);
    }

    public void writeValue(JSONSerializer serializer, Object propertyValue) throws Exception {
        if (this.format != null) {
            serializer.writeWithFormat(propertyValue, this.format);
            return;
        }
        Class<?> runtimeFieldClass;
        if (this.runtimeInfo == null) {
            if (propertyValue == null) {
                runtimeFieldClass = this.fieldInfo.fieldClass;
            } else {
                runtimeFieldClass = propertyValue.getClass();
            }
            this.runtimeInfo = new RuntimeSerializerInfo(serializer.getObjectWriter(runtimeFieldClass), runtimeFieldClass);
        }
        RuntimeSerializerInfo runtimeInfo = this.runtimeInfo;
        int fieldFeatures = this.fieldInfo.serialzeFeatures;
        if (propertyValue == null) {
            runtimeFieldClass = runtimeInfo.runtimeFieldClass;
            SerializeWriter out = serializer.out;
            if (Number.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(this.features, SerializerFeature.WriteNullNumberAsZero.mask);
                return;
            } else if (String.class == runtimeFieldClass) {
                out.writeNull(this.features, SerializerFeature.WriteNullStringAsEmpty.mask);
                return;
            } else if (Boolean.class == runtimeFieldClass) {
                out.writeNull(this.features, SerializerFeature.WriteNullBooleanAsFalse.mask);
                return;
            } else if (Collection.class.isAssignableFrom(runtimeFieldClass)) {
                out.writeNull(this.features, SerializerFeature.WriteNullListAsEmpty.mask);
                return;
            } else {
                ObjectSerializer fieldSerializer = runtimeInfo.fieldSerializer;
                if (out.isEnabled(SerializerFeature.WriteMapNullValue) && (fieldSerializer instanceof JavaBeanSerializer)) {
                    out.writeNull();
                    return;
                }
                fieldSerializer.write(serializer, null, this.fieldInfo.name, this.fieldInfo.fieldType, fieldFeatures);
                return;
            }
        }
        if (this.fieldInfo.isEnum) {
            if (this.writeEnumUsingName) {
                serializer.out.writeString(((Enum) propertyValue).name());
                return;
            } else if (this.writeEnumUsingToString) {
                serializer.out.writeString(((Enum) propertyValue).toString());
                return;
            }
        }
        Class<?> valueClass = propertyValue.getClass();
        if (valueClass == runtimeInfo.runtimeFieldClass) {
            runtimeInfo.fieldSerializer.write(serializer, propertyValue, this.fieldInfo.name, this.fieldInfo.fieldType, fieldFeatures);
            return;
        }
        serializer.getObjectWriter(valueClass).write(serializer, propertyValue, this.fieldInfo.name, this.fieldInfo.fieldType, fieldFeatures);
    }
}
