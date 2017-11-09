package com.gala.android.dlna.sdk.controlpoint.qimohttpserver;

import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response;
import com.gala.android.dlna.sdk.controlpoint.qimohttpserver.NanoHTTPD.Response.Status;
import java.io.ByteArrayInputStream;
import java.util.Map;

public class InternalRewrite extends Response {
    private final Map<String, String> headers;
    private final String uri;

    public InternalRewrite(Map<String, String> headers, String uri) {
        super(Status.OK, "text/html", new ByteArrayInputStream(new byte[0]), 0);
        this.headers = headers;
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getUri() {
        return this.uri;
    }
}
