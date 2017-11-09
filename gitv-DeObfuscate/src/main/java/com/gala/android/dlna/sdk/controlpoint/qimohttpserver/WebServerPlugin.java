package com.gala.android.dlna.sdk.controlpoint.qimohttpserver;

import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.IHTTPSession;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response;
import java.io.File;
import java.util.Map;

public interface WebServerPlugin {
    boolean canServeUri(String str, File file);

    void initialize(Map<String, String> map);

    Response serveFile(String str, Map<String, String> map, IHTTPSession iHTTPSession, File file, String str2);
}
