package retrofit;

import retrofit.client.Response;

public abstract class ResponseCallback implements Callback<Response> {
    public abstract void success(Response response);

    public void success(Response response, Response response2) {
        success(response);
    }
}
