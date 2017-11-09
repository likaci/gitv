package com.alibaba.fastjson.support.spring;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import java.io.IOException;
import java.lang.reflect.Type;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

public class FastJsonHttpMessageConverter4 extends AbstractGenericHttpMessageConverter<Object> {
    private FastJsonConfig fastJsonConfig = new FastJsonConfig();

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public FastJsonHttpMessageConverter4() {
        super(MediaType.ALL);
    }

    protected boolean supports(Class<?> cls) {
        return true;
    }

    public Object read(Type type, Class<?> cls, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), type, this.fastJsonConfig.getFeatures());
    }

    protected void writeInternal(Object obj, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        outputMessage.getHeaders().setContentLength((long) JSON.writeJSONString(outputMessage.getBody(), this.fastJsonConfig.getCharset(), obj, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures()));
    }

    protected Object readInternal(Class<? extends Object> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return JSON.parseObject(inputMessage.getBody(), this.fastJsonConfig.getCharset(), (Type) clazz, this.fastJsonConfig.getFeatures());
    }
}
