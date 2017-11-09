package com.gala.android.dlna.sdk.controlpoint.qimohttpserver;

public interface WebServerPluginInfo {
    String[] getIndexFilesForMimeType(String str);

    String[] getMimeTypes();

    WebServerPlugin getWebServerPlugin(String str);
}
