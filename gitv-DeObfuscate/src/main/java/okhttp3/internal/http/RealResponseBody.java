package okhttp3.internal.http;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import org.cybergarage.http.HTTP;

public final class RealResponseBody extends ResponseBody {
    private final Headers headers;
    private final BufferedSource source;

    public RealResponseBody(Headers headers, BufferedSource source) {
        this.headers = headers;
        this.source = source;
    }

    public MediaType contentType() {
        String contentType = this.headers.get(HTTP.CONTENT_TYPE);
        return contentType != null ? MediaType.parse(contentType) : null;
    }

    public long contentLength() {
        return HttpHeaders.contentLength(this.headers);
    }

    public BufferedSource source() {
        return this.source;
    }
}
