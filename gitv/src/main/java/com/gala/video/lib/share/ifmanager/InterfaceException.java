package com.gala.video.lib.share.ifmanager;

import android.util.AndroidRuntimeException;

class InterfaceException {
    InterfaceException() {
    }

    public static void throwRuntimeException(String msg) {
        InterfaceManager.get().printMap();
        throw new AndroidRuntimeException(msg);
    }
}
