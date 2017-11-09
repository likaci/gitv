package com.gala.video.app.player.config.configReader;

public interface IConfigReader {

    public interface OnConfigReadListener {
        void onFailed(Throwable th);

        void onSuccess(String str);
    }

    void readConfigAsync(OnConfigReadListener onConfigReadListener);

    String readConfigSync();
}
