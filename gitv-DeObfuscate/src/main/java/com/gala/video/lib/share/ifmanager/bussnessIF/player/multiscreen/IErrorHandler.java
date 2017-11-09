package com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen;

public interface IErrorHandler {

    public enum ErrorType {
        VIP,
        COMMON
    }

    void showError(ErrorType errorType, String str);
}
