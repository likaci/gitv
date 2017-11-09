package retrofit.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import retrofit.mime.TypedInput;

public final class Response {
    private final TypedInput body;
    private final List<Header> headers;
    private final String reason;
    private final int status;
    private final String url;

    public Response(String url, int status, String reason, List<Header> headers, TypedInput body) {
        if (url == null) {
            throw new IllegalArgumentException("url == null");
        } else if (status < 200) {
            throw new IllegalArgumentException("Invalid status code: " + status);
        } else if (reason == null) {
            throw new IllegalArgumentException("reason == null");
        } else if (headers == null) {
            throw new IllegalArgumentException("headers == null");
        } else {
            this.url = url;
            this.status = status;
            this.reason = reason;
            this.headers = Collections.unmodifiableList(new ArrayList(headers));
            this.body = body;
        }
    }

    public String getUrl() {
        return this.url;
    }

    public int getStatus() {
        return this.status;
    }

    public String getReason() {
        return this.reason;
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public TypedInput getBody() {
        return this.body;
    }
}
