package com.alibaba.fastjson.support.jaxrs;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.util.IOUtils;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

@Consumes({"*/*"})
@Produces({"*/*"})
@Provider
public class FastJsonProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    @Deprecated
    protected Charset charset;
    private Class<?>[] clazzes;
    @Deprecated
    protected String dateFormat;
    private FastJsonConfig fastJsonConfig;
    @Deprecated
    protected SerializerFeature[] features;
    @Deprecated
    protected SerializeFilter[] filters;
    @Context
    UriInfo uriInfo;

    public FastJsonConfig getFastJsonConfig() {
        return this.fastJsonConfig;
    }

    public void setFastJsonConfig(FastJsonConfig fastJsonConfig) {
        this.fastJsonConfig = fastJsonConfig;
    }

    public FastJsonProvider() {
        this.charset = IOUtils.UTF8;
        this.features = new SerializerFeature[0];
        this.filters = new SerializeFilter[0];
        this.fastJsonConfig = new FastJsonConfig();
        this.clazzes = null;
    }

    public FastJsonProvider(Class<?>[] clazzes) {
        this.charset = IOUtils.UTF8;
        this.features = new SerializerFeature[0];
        this.filters = new SerializeFilter[0];
        this.fastJsonConfig = new FastJsonConfig();
        this.clazzes = null;
        this.clazzes = clazzes;
    }

    @Deprecated
    public FastJsonProvider(String charset) {
        this.charset = IOUtils.UTF8;
        this.features = new SerializerFeature[0];
        this.filters = new SerializeFilter[0];
        this.fastJsonConfig = new FastJsonConfig();
        this.clazzes = null;
        this.fastJsonConfig.setCharset(Charset.forName(charset));
    }

    @Deprecated
    public Charset getCharset() {
        return this.fastJsonConfig.getCharset();
    }

    @Deprecated
    public void setCharset(Charset charset) {
        this.fastJsonConfig.setCharset(charset);
    }

    @Deprecated
    public String getDateFormat() {
        return this.fastJsonConfig.getDateFormat();
    }

    @Deprecated
    public void setDateFormat(String dateFormat) {
        this.fastJsonConfig.setDateFormat(dateFormat);
    }

    @Deprecated
    public SerializerFeature[] getFeatures() {
        return this.fastJsonConfig.getSerializerFeatures();
    }

    @Deprecated
    public void setFeatures(SerializerFeature... features) {
        this.fastJsonConfig.setSerializerFeatures(features);
    }

    @Deprecated
    public SerializeFilter[] getFilters() {
        return this.fastJsonConfig.getSerializeFilters();
    }

    @Deprecated
    public void setFilters(SerializeFilter... filters) {
        this.fastJsonConfig.setSerializeFilters(filters);
    }

    protected boolean isValidType(Class<?> type, Annotation[] classAnnotations) {
        if (type == null) {
            return false;
        }
        if (this.clazzes == null) {
            return true;
        }
        for (Class<?> cls : this.clazzes) {
            if (cls == type) {
                return true;
            }
        }
        return false;
    }

    protected boolean hasMatchingMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return true;
        }
        String subtype = mediaType.getSubtype();
        if (SettingConstants.ACTON_TYPE_JSON.equalsIgnoreCase(subtype) || subtype.endsWith("+json") || "javascript".equals(subtype) || "x-javascript".equals(subtype) || "x-json".equals(subtype) || "x-www-form-urlencoded".equalsIgnoreCase(subtype) || subtype.endsWith("x-www-form-urlencoded")) {
            return true;
        }
        return false;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (hasMatchingMediaType(mediaType)) {
            return isValidType(type, annotations);
        }
        return false;
    }

    public long getSize(Object t, Class<?> cls, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return -1;
    }

    public void writeTo(Object obj, Class<?> cls, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, Object> multivaluedMap, OutputStream entityStream) throws IOException, WebApplicationException {
        SerializerFeature[] serializerFeatures = this.fastJsonConfig.getSerializerFeatures();
        if (this.uriInfo != null && this.uriInfo.getQueryParameters().containsKey("pretty")) {
            if (serializerFeatures == null) {
                serializerFeatures = new SerializerFeature[]{SerializerFeature.PrettyFormat};
            } else {
                List<SerializerFeature> featureList = new ArrayList(Arrays.asList(serializerFeatures));
                featureList.add(SerializerFeature.PrettyFormat);
                serializerFeatures = (SerializerFeature[]) featureList.toArray(serializerFeatures);
            }
            this.fastJsonConfig.setSerializerFeatures(serializerFeatures);
        }
        JSON.writeJSONString(entityStream, this.fastJsonConfig.getCharset(), obj, this.fastJsonConfig.getSerializeConfig(), this.fastJsonConfig.getSerializeFilters(), this.fastJsonConfig.getDateFormat(), JSON.DEFAULT_GENERATE_FEATURE, this.fastJsonConfig.getSerializerFeatures());
        entityStream.flush();
    }

    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        if (hasMatchingMediaType(mediaType)) {
            return isValidType(type, annotations);
        }
        return false;
    }

    public Object readFrom(Class<Object> cls, Type genericType, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream entityStream) throws IOException, WebApplicationException {
        return JSON.parseObject(entityStream, this.fastJsonConfig.getCharset(), genericType, this.fastJsonConfig.getFeatures());
    }
}
