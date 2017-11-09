package com.gala.video.app.player.utils;

import com.gala.video.api.ApiException;

public class DebugApiException extends ApiException {
    private static final long serialVersionUID = 1;

    public DebugApiException(String message, String code, String httpCode, String url) {
        super(message, code, httpCode, url);
    }
}
