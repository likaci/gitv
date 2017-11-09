package com.gala.video.app.player.config.configWriter;

public interface IConfigWriter {

    public interface OnConfigWriteListener {
        void onFailed(Throwable th);

        void onSuccess();
    }

    void writeConfigAsync(String str, OnConfigWriteListener onConfigWriteListener);

    boolean writeConfigSync(String str);
}
