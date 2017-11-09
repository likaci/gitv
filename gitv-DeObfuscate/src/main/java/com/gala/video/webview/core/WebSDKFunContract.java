package com.gala.video.webview.core;

public interface WebSDKFunContract {

    public interface IFunBase {
        void finish();

        String getParams();

        String getSupportMethodList(String str);

        String getUserInfoParams(String str);
    }

    public interface IFunLoad {
        void onLoadCompleted();

        void onLoadFailed(String str);
    }

    public interface IFunUser {
        void onLoginSuccess(String str);

        void setActivityResult(String str, int i);
    }
}
