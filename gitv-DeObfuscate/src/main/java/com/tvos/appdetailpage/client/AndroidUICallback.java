package com.tvos.appdetailpage.client;

import android.content.Context;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public abstract class AndroidUICallback<T> implements Callback<T> {
    private Context context;

    public abstract void failure(RetrofitError retrofitError);

    public abstract void success(T t, Response response);

    public AndroidUICallback(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return this.context;
    }
}
