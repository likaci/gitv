package retrofit;

import java.io.IOException;
import java.lang.reflect.Type;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.Converter;
import retrofit.mime.TypedInput;

public class RetrofitError extends RuntimeException {
    private final Converter converter;
    private final boolean networkError;
    private final Response response;
    private final Type successType;
    private final String url;

    public static RetrofitError networkError(String url, IOException exception) {
        return new RetrofitError(exception.getMessage(), url, null, null, null, true, exception);
    }

    public static RetrofitError conversionError(String url, Response response, Converter converter, Type successType, ConversionException exception) {
        return new RetrofitError(exception.getMessage(), url, response, converter, successType, false, exception);
    }

    public static RetrofitError httpError(String url, Response response, Converter converter, Type successType) {
        return new RetrofitError(response.getStatus() + " " + response.getReason(), url, response, converter, successType, false, null);
    }

    public static RetrofitError unexpectedError(String url, Throwable exception) {
        return new RetrofitError(exception.getMessage(), url, null, null, null, false, exception);
    }

    RetrofitError(String message, String url, Response response, Converter converter, Type successType, boolean networkError, Throwable exception) {
        super(message, exception);
        this.url = url;
        this.response = response;
        this.converter = converter;
        this.successType = successType;
        this.networkError = networkError;
    }

    public String getUrl() {
        return this.url;
    }

    public Response getResponse() {
        return this.response;
    }

    public boolean isNetworkError() {
        return this.networkError;
    }

    public Object getBody() {
        return getBodyAs(this.successType);
    }

    public Type getSuccessType() {
        return this.successType;
    }

    public Object getBodyAs(Type type) {
        Object obj = null;
        if (this.response != null) {
            TypedInput body = this.response.getBody();
            if (body != null) {
                try {
                    obj = this.converter.fromBody(body, type);
                } catch (ConversionException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return obj;
    }
}
