package com.gala.download.base;

public interface IFileCallback {
    void onFailure(FileRequest fileRequest, Exception exception);

    void onSuccess(FileRequest fileRequest, String str);
}
