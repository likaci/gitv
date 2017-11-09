package retrofit;

import retrofit.client.Response;

final class ResponseWrapper {
    final Response response;
    final Object responseBody;

    ResponseWrapper(Response response, Object responseBody) {
        this.response = response;
        this.responseBody = responseBody;
    }
}
