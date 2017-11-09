package com.gala.afinal.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class AjaxParams {
    private static String ENCODING = "UTF-8";
    protected ConcurrentHashMap<String, FileWrapper> fileParams;
    protected ConcurrentHashMap<String, String> urlParams;

    static class FileWrapper {
        public String contentType;
        public String fileName;
        public InputStream inputStream;

        public FileWrapper(InputStream inputStream, String fileName, String contentType) {
            this.inputStream = inputStream;
            this.fileName = fileName;
            this.contentType = contentType;
        }

        public String getFileName() {
            if (this.fileName != null) {
                return this.fileName;
            }
            return "nofilename";
        }
    }

    public AjaxParams() {
        init();
    }

    public AjaxParams(Map<String, String> source) {
        init();
        for (Entry entry : source.entrySet()) {
            put((String) entry.getKey(), (String) entry.getValue());
        }
    }

    public AjaxParams(String key, String value) {
        init();
        put(key, value);
    }

    public AjaxParams(Object... keysAndValues) {
        init();
        int length = keysAndValues.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Supplied arguments must be even");
        }
        for (int i = 0; i < length; i += 2) {
            put(String.valueOf(keysAndValues[i]), String.valueOf(keysAndValues[i + 1]));
        }
    }

    public void put(String key, String value) {
        if (key != null && value != null) {
            this.urlParams.put(key, value);
        }
    }

    public void put(String key, File file) throws FileNotFoundException {
        put(key, new FileInputStream(file), file.getName());
    }

    public void put(String key, InputStream stream) {
        put(key, stream, null);
    }

    public void put(String key, InputStream stream, String fileName) {
        put(key, stream, fileName, null);
    }

    public void put(String key, InputStream stream, String fileName, String contentType) {
        if (key != null && stream != null) {
            this.fileParams.put(key, new FileWrapper(stream, fileName, contentType));
        }
    }

    public void remove(String key) {
        this.urlParams.remove(key);
        this.fileParams.remove(key);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry entry : this.urlParams.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry.getKey());
            stringBuilder.append(SearchCriteria.EQ);
            stringBuilder.append((String) entry.getValue());
        }
        for (Entry entry2 : this.fileParams.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append((String) entry2.getKey());
            stringBuilder.append(SearchCriteria.EQ);
            stringBuilder.append("FILE");
        }
        return stringBuilder.toString();
    }

    public HttpEntity getEntity() {
        if (this.fileParams.isEmpty()) {
            try {
                return new UrlEncodedFormEntity(getParamsList(), ENCODING);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            }
        }
        HttpEntity multipartEntity = new MultipartEntity();
        for (Entry entry : this.urlParams.entrySet()) {
            multipartEntity.addPart((String) entry.getKey(), (String) entry.getValue());
        }
        int size = this.fileParams.entrySet().size() - 1;
        int i = 0;
        for (Entry entry2 : this.fileParams.entrySet()) {
            FileWrapper fileWrapper = (FileWrapper) entry2.getValue();
            if (fileWrapper.inputStream != null) {
                boolean z = i == size;
                if (fileWrapper.contentType != null) {
                    multipartEntity.addPart((String) entry2.getKey(), fileWrapper.getFileName(), fileWrapper.inputStream, fileWrapper.contentType, z);
                } else {
                    multipartEntity.addPart((String) entry2.getKey(), fileWrapper.getFileName(), fileWrapper.inputStream, z);
                }
            }
            i++;
        }
        return multipartEntity;
    }

    private void init() {
        this.urlParams = new ConcurrentHashMap();
        this.fileParams = new ConcurrentHashMap();
    }

    protected List<BasicNameValuePair> getParamsList() {
        List<BasicNameValuePair> linkedList = new LinkedList();
        for (Entry entry : this.urlParams.entrySet()) {
            linkedList.add(new BasicNameValuePair((String) entry.getKey(), (String) entry.getValue()));
        }
        return linkedList;
    }

    public String getParamString() {
        return URLEncodedUtils.format(getParamsList(), ENCODING);
    }
}
