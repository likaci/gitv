package retrofit;

import retrofit.client.Response;

public interface Callback<T> {
    void failure(RetrofitError retrofitError);

    void success(T t, Response response);
}
