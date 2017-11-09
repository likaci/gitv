package retrofit;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import retrofit.Profiler.RequestInformation;
import retrofit.client.Client;
import retrofit.client.Client.Provider;
import retrofit.client.Header;
import retrofit.client.Request;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.MimeUtil;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class RestAdapter {
    static final String IDLE_THREAD_NAME = "Retrofit-Idle";
    static final String THREAD_PREFIX = "Retrofit-";
    final Executor callbackExecutor;
    private final Provider clientProvider;
    final Converter converter;
    final ErrorHandler errorHandler;
    final Executor httpExecutor;
    final Log log;
    volatile LogLevel logLevel;
    private final Profiler profiler;
    final RequestInterceptor requestInterceptor;
    private RxSupport rxSupport;
    final Endpoint server;
    private final Map<Class<?>, Map<Method, RestMethodInfo>> serviceMethodInfoCache;

    public interface Log {
        public static final Log NONE = new C22371();

        static class C22371 implements Log {
            C22371() {
            }

            public void log(String message) {
            }
        }

        void log(String str);
    }

    public static class Builder {
        private Executor callbackExecutor;
        private Provider clientProvider;
        private Converter converter;
        private Endpoint endpoint;
        private ErrorHandler errorHandler;
        private Executor httpExecutor;
        private Log log;
        private LogLevel logLevel = LogLevel.NONE;
        private Profiler profiler;
        private RequestInterceptor requestInterceptor;

        public Builder setEndpoint(String endpoint) {
            if (endpoint == null || endpoint.trim().length() == 0) {
                throw new NullPointerException("Endpoint may not be blank.");
            }
            this.endpoint = Endpoints.newFixedEndpoint(endpoint);
            return this;
        }

        public Builder setEndpoint(Endpoint endpoint) {
            if (endpoint == null) {
                throw new NullPointerException("Endpoint may not be null.");
            }
            this.endpoint = endpoint;
            return this;
        }

        public Builder setClient(final Client client) {
            if (client != null) {
                return setClient(new Provider() {
                    public Client get() {
                        return client;
                    }
                });
            }
            throw new NullPointerException("Client may not be null.");
        }

        public Builder setClient(Provider clientProvider) {
            if (clientProvider == null) {
                throw new NullPointerException("Client provider may not be null.");
            }
            this.clientProvider = clientProvider;
            return this;
        }

        public Builder setExecutors(Executor httpExecutor, Executor callbackExecutor) {
            if (httpExecutor == null) {
                throw new NullPointerException("HTTP executor may not be null.");
            }
            if (callbackExecutor == null) {
                callbackExecutor = new SynchronousExecutor();
            }
            this.httpExecutor = httpExecutor;
            this.callbackExecutor = callbackExecutor;
            return this;
        }

        public Builder setRequestInterceptor(RequestInterceptor requestInterceptor) {
            if (requestInterceptor == null) {
                throw new NullPointerException("Request interceptor may not be null.");
            }
            this.requestInterceptor = requestInterceptor;
            return this;
        }

        public Builder setConverter(Converter converter) {
            if (converter == null) {
                throw new NullPointerException("Converter may not be null.");
            }
            this.converter = converter;
            return this;
        }

        public Builder setProfiler(Profiler profiler) {
            if (profiler == null) {
                throw new NullPointerException("Profiler may not be null.");
            }
            this.profiler = profiler;
            return this;
        }

        public Builder setErrorHandler(ErrorHandler errorHandler) {
            if (errorHandler == null) {
                throw new NullPointerException("Error handler may not be null.");
            }
            this.errorHandler = errorHandler;
            return this;
        }

        public Builder setLog(Log log) {
            if (log == null) {
                throw new NullPointerException("Log may not be null.");
            }
            this.log = log;
            return this;
        }

        public Builder setLogLevel(LogLevel logLevel) {
            if (logLevel == null) {
                throw new NullPointerException("Log level may not be null.");
            }
            this.logLevel = logLevel;
            return this;
        }

        public RestAdapter build() {
            if (this.endpoint == null) {
                throw new IllegalArgumentException("Endpoint may not be null.");
            }
            ensureSaneDefaults();
            return new RestAdapter(this.endpoint, this.clientProvider, this.httpExecutor, this.callbackExecutor, this.requestInterceptor, this.converter, this.profiler, this.errorHandler, this.log, this.logLevel);
        }

        private void ensureSaneDefaults() {
            if (this.converter == null) {
                this.converter = Platform.get().defaultConverter();
            }
            if (this.clientProvider == null) {
                this.clientProvider = Platform.get().defaultClient();
            }
            if (this.httpExecutor == null) {
                this.httpExecutor = Platform.get().defaultHttpExecutor();
            }
            if (this.callbackExecutor == null) {
                this.callbackExecutor = Platform.get().defaultCallbackExecutor();
            }
            if (this.errorHandler == null) {
                this.errorHandler = ErrorHandler.DEFAULT;
            }
            if (this.log == null) {
                this.log = Platform.get().defaultLog();
            }
            if (this.requestInterceptor == null) {
                this.requestInterceptor = RequestInterceptor.NONE;
            }
        }
    }

    public enum LogLevel {
        NONE,
        BASIC,
        HEADERS,
        FULL;

        public boolean log() {
            return this != NONE;
        }
    }

    private class RestHandler implements InvocationHandler {
        private final Map<Method, RestMethodInfo> methodDetailsCache;

        RestHandler(Map<Method, RestMethodInfo> methodDetailsCache) {
            this.methodDetailsCache = methodDetailsCache;
        }

        public Object invoke(Object proxy, Method method, final Object[] args) throws Throwable {
            if (method.getDeclaringClass() == Object.class) {
                return method.invoke(this, args);
            }
            final RestMethodInfo methodInfo = RestAdapter.getMethodInfo(this.methodDetailsCache, method);
            if (methodInfo.isSynchronous) {
                try {
                    return invokeRequest(RestAdapter.this.requestInterceptor, methodInfo, args);
                } catch (RetrofitError error) {
                    Throwable newError = RestAdapter.this.errorHandler.handleError(error);
                    if (newError == null) {
                        throw new IllegalStateException("Error handler returned null for wrapped exception.", error);
                    }
                    throw newError;
                }
            } else if (RestAdapter.this.httpExecutor == null || RestAdapter.this.callbackExecutor == null) {
                throw new IllegalStateException("Asynchronous invocation requires calling setExecutors.");
            } else if (methodInfo.isObservable) {
                if (RestAdapter.this.rxSupport == null) {
                    if (Platform.HAS_RX_JAVA) {
                        RestAdapter.this.rxSupport = new RxSupport(RestAdapter.this.httpExecutor, RestAdapter.this.errorHandler, RestAdapter.this.requestInterceptor);
                    } else {
                        throw new IllegalStateException("Observable method found but no RxJava on classpath.");
                    }
                }
                return RestAdapter.this.rxSupport.createRequestObservable(new Invoker() {
                    public ResponseWrapper invoke(RequestInterceptor requestInterceptor) {
                        return (ResponseWrapper) RestHandler.this.invokeRequest(requestInterceptor, methodInfo, args);
                    }
                });
            } else {
                final RequestInterceptorTape interceptorTape = new RequestInterceptorTape();
                RestAdapter.this.requestInterceptor.intercept(interceptorTape);
                Callback<?> callback = args[args.length - 1];
                final Object[] objArr = args;
                RestAdapter.this.httpExecutor.execute(new CallbackRunnable(callback, RestAdapter.this.callbackExecutor, RestAdapter.this.errorHandler) {
                    public ResponseWrapper obtainResponse() {
                        return (ResponseWrapper) RestHandler.this.invokeRequest(interceptorTape, methodInfo, objArr);
                    }
                });
                return null;
            }
        }

        private Object invokeRequest(RequestInterceptor requestInterceptor, RestMethodInfo methodInfo, Object[] args) {
            ExceptionCatchingTypedInput exceptionCatchingTypedInput;
            String url = null;
            Response response;
            Type type;
            try {
                methodInfo.init();
                String serverUrl = RestAdapter.this.server.getUrl();
                RequestBuilder requestBuilder = new RequestBuilder(serverUrl, methodInfo, RestAdapter.this.converter);
                requestBuilder.setArguments(args);
                requestInterceptor.intercept(requestBuilder);
                Request request = requestBuilder.build();
                url = request.getUrl();
                if (!methodInfo.isSynchronous) {
                    Thread.currentThread().setName(RestAdapter.THREAD_PREFIX + url.substring(serverUrl.length()));
                }
                if (RestAdapter.this.logLevel.log()) {
                    request = RestAdapter.this.logAndReplaceRequest("HTTP", request);
                }
                Object profilerObject = null;
                if (RestAdapter.this.profiler != null) {
                    profilerObject = RestAdapter.this.profiler.beforeCall();
                }
                long start = System.nanoTime();
                response = RestAdapter.this.clientProvider.get().execute(request);
                long elapsedTime = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
                int statusCode = response.getStatus();
                if (RestAdapter.this.profiler != null) {
                    RestAdapter.this.profiler.afterCall(RestAdapter.getRequestInfo(serverUrl, methodInfo, request), elapsedTime, statusCode, profilerObject);
                }
                if (RestAdapter.this.logLevel.log()) {
                    response = RestAdapter.this.logAndReplaceResponse(url, response, elapsedTime);
                }
                type = methodInfo.responseObjectType;
                if (statusCode < 200 || statusCode >= 300) {
                    throw RetrofitError.httpError(url, Utils.readBodyToBytesIfNecessary(response), RestAdapter.this.converter, type);
                }
                Object responseWrapper;
                if (type.equals(Response.class)) {
                    if (!methodInfo.isStreaming) {
                        response = Utils.readBodyToBytesIfNecessary(response);
                    }
                    if (methodInfo.isSynchronous) {
                        if (!methodInfo.isSynchronous) {
                            Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                        }
                        return response;
                    }
                    responseWrapper = new ResponseWrapper(response, response);
                    if (methodInfo.isSynchronous) {
                        return responseWrapper;
                    }
                    Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                    return responseWrapper;
                }
                TypedInput body = response.getBody();
                if (body != null) {
                    exceptionCatchingTypedInput = new ExceptionCatchingTypedInput(body);
                    responseWrapper = RestAdapter.this.converter.fromBody(exceptionCatchingTypedInput, type);
                    if (!methodInfo.isSynchronous) {
                        ResponseWrapper responseWrapper2 = new ResponseWrapper(response, responseWrapper);
                        if (!methodInfo.isSynchronous) {
                            Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                        }
                        return responseWrapper2;
                    } else if (methodInfo.isSynchronous) {
                        return responseWrapper;
                    } else {
                        Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                        return responseWrapper;
                    }
                } else if (!methodInfo.isSynchronous) {
                    responseWrapper = new ResponseWrapper(response, null);
                    if (methodInfo.isSynchronous) {
                        return responseWrapper;
                    }
                    Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                    return responseWrapper;
                } else if (methodInfo.isSynchronous) {
                    return null;
                } else {
                    Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                    return null;
                }
            } catch (ConversionException e) {
                if (exceptionCatchingTypedInput.threwException()) {
                    throw exceptionCatchingTypedInput.getThrownException();
                }
                throw RetrofitError.conversionError(url, Utils.replaceResponseBody(response, null), RestAdapter.this.converter, type, e);
            } catch (ConversionException e2) {
                try {
                    throw e2;
                } catch (Throwable th) {
                    if (!methodInfo.isSynchronous) {
                        Thread.currentThread().setName(RestAdapter.IDLE_THREAD_NAME);
                    }
                }
            } catch (IOException e3) {
                if (RestAdapter.this.logLevel.log()) {
                    RestAdapter.this.logException(e3, url);
                }
                throw RetrofitError.networkError(url, e3);
            } catch (Throwable t) {
                if (RestAdapter.this.logLevel.log()) {
                    RestAdapter.this.logException(t, url);
                }
                RetrofitError unexpectedError = RetrofitError.unexpectedError(url, t);
            }
        }
    }

    private RestAdapter(Endpoint server, Provider clientProvider, Executor httpExecutor, Executor callbackExecutor, RequestInterceptor requestInterceptor, Converter converter, Profiler profiler, ErrorHandler errorHandler, Log log, LogLevel logLevel) {
        this.serviceMethodInfoCache = new LinkedHashMap();
        this.server = server;
        this.clientProvider = clientProvider;
        this.httpExecutor = httpExecutor;
        this.callbackExecutor = callbackExecutor;
        this.requestInterceptor = requestInterceptor;
        this.converter = converter;
        this.profiler = profiler;
        this.errorHandler = errorHandler;
        this.log = log;
        this.logLevel = logLevel;
    }

    public void setLogLevel(LogLevel loglevel) {
        if (this.logLevel == null) {
            throw new NullPointerException("Log level may not be null.");
        }
        this.logLevel = loglevel;
    }

    public LogLevel getLogLevel() {
        return this.logLevel;
    }

    public <T> T create(Class<T> service) {
        Utils.validateServiceClass(service);
        return Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new RestHandler(getMethodInfoCache(service)));
    }

    Map<Method, RestMethodInfo> getMethodInfoCache(Class<?> service) {
        Map<Method, RestMethodInfo> methodInfoCache;
        synchronized (this.serviceMethodInfoCache) {
            methodInfoCache = (Map) this.serviceMethodInfoCache.get(service);
            if (methodInfoCache == null) {
                methodInfoCache = new LinkedHashMap();
                this.serviceMethodInfoCache.put(service, methodInfoCache);
            }
        }
        return methodInfoCache;
    }

    static RestMethodInfo getMethodInfo(Map<Method, RestMethodInfo> cache, Method method) {
        RestMethodInfo methodInfo;
        synchronized (cache) {
            methodInfo = (RestMethodInfo) cache.get(method);
            if (methodInfo == null) {
                methodInfo = new RestMethodInfo(method);
                cache.put(method, methodInfo);
            }
        }
        return methodInfo;
    }

    Request logAndReplaceRequest(String name, Request request) throws IOException {
        this.log.log(String.format("---> %s %s %s", new Object[]{name, request.getMethod(), request.getUrl()}));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : request.getHeaders()) {
                this.log.log(header.toString());
            }
            String bodySize = "no";
            TypedOutput body = request.getBody();
            if (body != null) {
                String bodyMime = body.mimeType();
                if (bodyMime != null) {
                    this.log.log("Content-Type: " + bodyMime);
                }
                long bodyLength = body.length();
                bodySize = bodyLength + "-byte";
                if (bodyLength != -1) {
                    this.log.log("Content-Length: " + bodyLength);
                }
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!request.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        request = Utils.readBodyToBytesIfNecessary(request);
                        body = request.getBody();
                    }
                    this.log.log(new String(((TypedByteArray) body).getBytes(), MimeUtil.parseCharset(body.mimeType())));
                }
            }
            this.log.log(String.format("---> END %s (%s body)", new Object[]{name, bodySize}));
        }
        return request;
    }

    private Response logAndReplaceResponse(String url, Response response, long elapsedTime) throws IOException {
        this.log.log(String.format("<--- HTTP %s %s (%sms)", new Object[]{Integer.valueOf(response.getStatus()), url, Long.valueOf(elapsedTime)}));
        if (this.logLevel.ordinal() >= LogLevel.HEADERS.ordinal()) {
            for (Header header : response.getHeaders()) {
                this.log.log(header.toString());
            }
            long bodySize = 0;
            TypedInput body = response.getBody();
            if (body != null) {
                bodySize = body.length();
                if (this.logLevel.ordinal() >= LogLevel.FULL.ordinal()) {
                    if (!response.getHeaders().isEmpty()) {
                        this.log.log("");
                    }
                    if (!(body instanceof TypedByteArray)) {
                        response = Utils.readBodyToBytesIfNecessary(response);
                        body = response.getBody();
                    }
                    byte[] bodyBytes = ((TypedByteArray) body).getBytes();
                    bodySize = (long) bodyBytes.length;
                    this.log.log(new String(bodyBytes, MimeUtil.parseCharset(body.mimeType())));
                }
            }
            this.log.log(String.format("<--- END HTTP (%s-byte body)", new Object[]{Long.valueOf(bodySize)}));
        }
        return response;
    }

    void logException(Throwable t, String url) {
        Log log = this.log;
        String str = "---- ERROR %s";
        Object[] objArr = new Object[1];
        if (url == null) {
            url = "";
        }
        objArr[0] = url;
        log.log(String.format(str, objArr));
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        this.log.log(sw.toString());
        this.log.log("---- END ERROR");
    }

    private static RequestInformation getRequestInfo(String serverUrl, RestMethodInfo methodDetails, Request request) {
        long contentLength = 0;
        String contentType = null;
        TypedOutput body = request.getBody();
        if (body != null) {
            contentLength = body.length();
            contentType = body.mimeType();
        }
        return new RequestInformation(methodDetails.requestMethod, serverUrl, methodDetails.requestUrl, contentLength, contentType);
    }
}
