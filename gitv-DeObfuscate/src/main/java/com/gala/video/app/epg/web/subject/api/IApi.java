package com.gala.video.app.epg.web.subject.api;

import com.gala.video.api.ApiException;

public interface IApi {

    public interface IExceptionCallback {
        void getStateResult(ApiException apiException);
    }

    public interface IStatusCallback {
        void onStatus(int i);
    }
}
