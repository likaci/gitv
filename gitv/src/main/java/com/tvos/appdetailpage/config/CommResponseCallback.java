package com.tvos.appdetailpage.config;

import android.util.Log;
import retrofit.ResponseCallback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class CommResponseCallback extends ResponseCallback {
    private final String CLIENT_TAG;
    private final String LOG_TAG;
    private final String METHOD_TAG;

    public CommResponseCallback(String logtag, String methodtag, String clienttag) {
        this.LOG_TAG = logtag;
        this.METHOD_TAG = methodtag;
        this.CLIENT_TAG = clienttag;
    }

    public void failure(RetrofitError arg0) {
        Log.e(this.LOG_TAG, this.LOG_TAG + ">>>>>>>>>>>>>>>>>>Pingback " + this.METHOD_TAG + " " + this.CLIENT_TAG + " Failure" + arg0.getMessage() + " ---  " + arg0.getUrl());
    }

    public void success(Response arg0) {
        Log.d(this.LOG_TAG, this.LOG_TAG + ">>>>>>>>>>>>>>>>>>Pingback " + this.METHOD_TAG + " " + this.CLIENT_TAG + "  Success");
    }
}
