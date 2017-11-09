package retrofit;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.cybergarage.http.HTTP;
import retrofit.RequestInterceptor.RequestFacade;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.converter.Converter;
import retrofit.mime.FormUrlEncodedTypedOutput;
import retrofit.mime.MultipartTypedOutput;
import retrofit.mime.TypedOutput;
import retrofit.mime.TypedString;

final class RequestBuilder implements RequestFacade {
    private final String apiUrl;
    private TypedOutput body;
    private String contentTypeHeader;
    private final Converter converter;
    private final FormUrlEncodedTypedOutput formBody;
    private List<Header> headers;
    private final boolean isObservable;
    private final boolean isSynchronous;
    private final MultipartTypedOutput multipartBody;
    private final String[] paramNames;
    private final ParamUsage[] paramUsages;
    private StringBuilder queryParams;
    private String relativeUrl;
    private final String requestMethod;

    private static class MimeOverridingTypedOutput implements TypedOutput {
        private final TypedOutput delegate;
        private final String mimeType;

        MimeOverridingTypedOutput(TypedOutput delegate, String mimeType) {
            this.delegate = delegate;
            this.mimeType = mimeType;
        }

        public String fileName() {
            return this.delegate.fileName();
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return this.delegate.length();
        }

        public void writeTo(OutputStream out) throws IOException {
            this.delegate.writeTo(out);
        }
    }

    RequestBuilder(String apiUrl, RestMethodInfo methodInfo, Converter converter) {
        this.apiUrl = apiUrl;
        this.converter = converter;
        this.paramNames = methodInfo.requestParamNames;
        this.paramUsages = methodInfo.requestParamUsage;
        this.requestMethod = methodInfo.requestMethod;
        this.isSynchronous = methodInfo.isSynchronous;
        this.isObservable = methodInfo.isObservable;
        if (methodInfo.headers != null) {
            this.headers = new ArrayList(methodInfo.headers);
        }
        this.contentTypeHeader = methodInfo.contentTypeHeader;
        this.relativeUrl = methodInfo.requestUrl;
        String requestQuery = methodInfo.requestQuery;
        if (requestQuery != null) {
            this.queryParams = new StringBuilder().append('?').append(requestQuery);
        }
        switch (methodInfo.requestType) {
            case FORM_URL_ENCODED:
                this.formBody = new FormUrlEncodedTypedOutput();
                this.multipartBody = null;
                this.body = this.formBody;
                return;
            case MULTIPART:
                this.formBody = null;
                this.multipartBody = new MultipartTypedOutput();
                this.body = this.multipartBody;
                return;
            case SIMPLE:
                this.formBody = null;
                this.multipartBody = null;
                return;
            default:
                throw new IllegalArgumentException("Unknown request type: " + methodInfo.requestType);
        }
    }

    public void addHeader(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("Header name must not be null.");
        } else if (HTTP.CONTENT_TYPE.equalsIgnoreCase(name)) {
            this.contentTypeHeader = value;
        } else {
            List<Header> headers = this.headers;
            if (headers == null) {
                headers = new ArrayList(2);
                this.headers = headers;
            }
            headers.add(new Header(name, value));
        }
    }

    public void addPathParam(String name, String value) {
        addPathParam(name, value, true);
    }

    public void addEncodedPathParam(String name, String value) {
        addPathParam(name, value, false);
    }

    private void addPathParam(String name, String value, boolean urlEncodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Path replacement name must not be null.");
        } else if (value == null) {
            throw new IllegalArgumentException("Path replacement \"" + name + "\" value must not be null.");
        } else if (urlEncodeValue) {
            try {
                this.relativeUrl = this.relativeUrl.replace("{" + name + "}", URLEncoder.encode(String.valueOf(value), "UTF-8").replace("+", "%20"));
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException("Unable to convert path parameter \"" + name + "\" value to UTF-8:" + value, e);
            }
        } else {
            this.relativeUrl = this.relativeUrl.replace("{" + name + "}", String.valueOf(value));
        }
    }

    public void addQueryParam(String name, String value) {
        addQueryParam(name, value, true);
    }

    public void addEncodedQueryParam(String name, String value) {
        addQueryParam(name, value, false);
    }

    private void addQueryParam(String name, String value, boolean urlEncodeValue) {
        if (name == null) {
            throw new IllegalArgumentException("Query param name must not be null.");
        } else if (value == null) {
            throw new IllegalArgumentException("Query param \"" + name + "\" value must not be null.");
        } else {
            if (urlEncodeValue) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("Unable to convert query parameter \"" + name + "\" value to UTF-8: " + value, e);
                }
            }
            StringBuilder queryParams = this.queryParams;
            if (queryParams == null) {
                queryParams = new StringBuilder();
                this.queryParams = queryParams;
            }
            queryParams.append(queryParams.length() > 0 ? '&' : '?');
            queryParams.append(name).append('=').append(value);
        }
    }

    void setArguments(Object[] args) {
        if (args != null) {
            int count = args.length;
            if (!(this.isSynchronous || this.isObservable)) {
                count--;
            }
            for (int i = 0; i < count; i++) {
                String name = this.paramNames[i];
                Object value = args[i];
                ParamUsage paramUsage = this.paramUsages[i];
                boolean urlEncodeValue;
                int arrayLength;
                int x;
                Object arrayValue;
                Object entryValue;
                switch (paramUsage) {
                    case PATH:
                        if (value != null) {
                            addPathParam(name, value.toString());
                            break;
                        }
                        throw new IllegalArgumentException("Path parameter \"" + name + "\" value must not be null.");
                    case ENCODED_PATH:
                        if (value != null) {
                            addEncodedPathParam(name, value.toString());
                            break;
                        }
                        throw new IllegalArgumentException("Path parameter \"" + name + "\" value must not be null.");
                    case QUERY:
                    case ENCODED_QUERY:
                        if (value != null) {
                            urlEncodeValue = paramUsage == ParamUsage.QUERY;
                            if (!(value instanceof Iterable)) {
                                if (!value.getClass().isArray()) {
                                    addQueryParam(name, value.toString(), urlEncodeValue);
                                    break;
                                }
                                arrayLength = Array.getLength(value);
                                for (x = 0; x < arrayLength; x++) {
                                    arrayValue = Array.get(value, x);
                                    if (arrayValue != null) {
                                        addQueryParam(name, arrayValue.toString(), urlEncodeValue);
                                    }
                                }
                                break;
                            }
                            for (Object iterableValue : (Iterable) value) {
                                if (iterableValue != null) {
                                    addQueryParam(name, iterableValue.toString(), urlEncodeValue);
                                }
                            }
                            break;
                        }
                        break;
                    case QUERY_MAP:
                    case ENCODED_QUERY_MAP:
                        if (value != null) {
                            urlEncodeValue = paramUsage == ParamUsage.QUERY_MAP;
                            for (Entry<?, ?> entry : ((Map) value).entrySet()) {
                                entryValue = entry.getValue();
                                if (entryValue != null) {
                                    addQueryParam(entry.getKey().toString(), entryValue.toString(), urlEncodeValue);
                                }
                            }
                            break;
                        }
                        break;
                    case HEADER:
                        if (value == null) {
                            break;
                        }
                        addHeader(name, value.toString());
                        break;
                    case FIELD:
                        if (value != null) {
                            if (!(value instanceof Iterable)) {
                                if (!value.getClass().isArray()) {
                                    this.formBody.addField(name, value.toString());
                                    break;
                                }
                                arrayLength = Array.getLength(value);
                                for (x = 0; x < arrayLength; x++) {
                                    arrayValue = Array.get(value, x);
                                    if (arrayValue != null) {
                                        this.formBody.addField(name, arrayValue.toString());
                                    }
                                }
                                break;
                            }
                            for (Object iterableValue2 : (Iterable) value) {
                                if (iterableValue2 != null) {
                                    this.formBody.addField(name, iterableValue2.toString());
                                }
                            }
                            break;
                        }
                        break;
                    case FIELD_MAP:
                        if (value == null) {
                            break;
                        }
                        for (Entry<?, ?> entry2 : ((Map) value).entrySet()) {
                            entryValue = entry2.getValue();
                            if (entryValue != null) {
                                this.formBody.addField(entry2.getKey().toString(), entryValue.toString());
                            }
                        }
                        break;
                    case PART:
                        if (value != null) {
                            if (!(value instanceof TypedOutput)) {
                                if (!(value instanceof String)) {
                                    this.multipartBody.addPart(name, this.converter.toBody(value));
                                    break;
                                } else {
                                    this.multipartBody.addPart(name, new TypedString((String) value));
                                    break;
                                }
                            }
                            this.multipartBody.addPart(name, (TypedOutput) value);
                            break;
                        }
                        break;
                    case PART_MAP:
                        if (value == null) {
                            break;
                        }
                        for (Entry<?, ?> entry22 : ((Map) value).entrySet()) {
                            String entryName = entry22.getKey().toString();
                            entryValue = entry22.getValue();
                            if (entryValue != null) {
                                if (entryValue instanceof TypedOutput) {
                                    this.multipartBody.addPart(entryName, (TypedOutput) entryValue);
                                } else if (entryValue instanceof String) {
                                    this.multipartBody.addPart(entryName, new TypedString((String) entryValue));
                                } else {
                                    this.multipartBody.addPart(entryName, this.converter.toBody(entryValue));
                                }
                            }
                        }
                        break;
                    case BODY:
                        if (value != null) {
                            if (!(value instanceof TypedOutput)) {
                                this.body = this.converter.toBody(value);
                                break;
                            } else {
                                this.body = (TypedOutput) value;
                                break;
                            }
                        }
                        throw new IllegalArgumentException("Body parameter value must not be null.");
                    default:
                        throw new IllegalArgumentException("Unknown parameter usage: " + paramUsage);
                }
            }
        }
    }

    Request build() throws UnsupportedEncodingException {
        if (this.multipartBody == null || this.multipartBody.getPartCount() != 0) {
            String apiUrl = this.apiUrl;
            StringBuilder url = new StringBuilder(apiUrl);
            if (apiUrl.endsWith("/")) {
                url.deleteCharAt(url.length() - 1);
            }
            url.append(this.relativeUrl);
            StringBuilder queryParams = this.queryParams;
            if (queryParams != null) {
                url.append(queryParams);
            }
            TypedOutput body = this.body;
            if (!(body == null || this.contentTypeHeader == null)) {
                body = new MimeOverridingTypedOutput(body, this.contentTypeHeader);
            }
            return new Request(this.requestMethod, url.toString(), this.headers, body);
        }
        throw new IllegalStateException("Multipart requests must contain at least one part.");
    }
}
