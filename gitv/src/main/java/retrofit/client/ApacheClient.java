package retrofit.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.cybergarage.http.HTTP;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedOutput;

public class ApacheClient implements Client {
    private final HttpClient client;

    private static class GenericEntityHttpRequest extends HttpEntityEnclosingRequestBase {
        private final String method;

        GenericEntityHttpRequest(Request request) {
            this.method = request.getMethod();
            setURI(URI.create(request.getUrl()));
            for (Header header : request.getHeaders()) {
                addHeader(new BasicHeader(header.getName(), header.getValue()));
            }
            setEntity(new TypedOutputEntity(request.getBody()));
        }

        public String getMethod() {
            return this.method;
        }
    }

    private static class GenericHttpRequest extends HttpRequestBase {
        private final String method;

        public GenericHttpRequest(Request request) {
            this.method = request.getMethod();
            setURI(URI.create(request.getUrl()));
            for (Header header : request.getHeaders()) {
                addHeader(new BasicHeader(header.getName(), header.getValue()));
            }
        }

        public String getMethod() {
            return this.method;
        }
    }

    static class TypedOutputEntity extends AbstractHttpEntity {
        final TypedOutput typedOutput;

        TypedOutputEntity(TypedOutput typedOutput) {
            this.typedOutput = typedOutput;
            setContentType(typedOutput.mimeType());
        }

        public boolean isRepeatable() {
            return true;
        }

        public long getContentLength() {
            return this.typedOutput.length();
        }

        public InputStream getContent() throws IOException {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.typedOutput.writeTo(out);
            return new ByteArrayInputStream(out.toByteArray());
        }

        public void writeTo(OutputStream out) throws IOException {
            this.typedOutput.writeTo(out);
        }

        public boolean isStreaming() {
            return false;
        }
    }

    private static HttpClient createDefaultClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 15000);
        HttpConnectionParams.setSoTimeout(params, 20000);
        return new DefaultHttpClient(params);
    }

    public ApacheClient() {
        this(createDefaultClient());
    }

    public ApacheClient(HttpClient client) {
        this.client = client;
    }

    public Response execute(Request request) throws IOException {
        return parseResponse(request.getUrl(), execute(this.client, createRequest(request)));
    }

    protected HttpResponse execute(HttpClient client, HttpUriRequest request) throws IOException {
        return client.execute(request);
    }

    static HttpUriRequest createRequest(Request request) {
        if (request.getBody() != null) {
            return new GenericEntityHttpRequest(request);
        }
        return new GenericHttpRequest(request);
    }

    static Response parseResponse(String url, HttpResponse response) throws IOException {
        StatusLine statusLine = response.getStatusLine();
        int status = statusLine.getStatusCode();
        String reason = statusLine.getReasonPhrase();
        List<Header> headers = new ArrayList();
        String contentType = "application/octet-stream";
        for (Header header : response.getAllHeaders()) {
            String name = header.getName();
            String value = header.getValue();
            if (HTTP.CONTENT_TYPE.equalsIgnoreCase(name)) {
                contentType = value;
            }
            headers.add(new Header(name, value));
        }
        TypedByteArray body = null;
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            body = new TypedByteArray(contentType, EntityUtils.toByteArray(entity));
        }
        return new Response(url, status, reason, headers, body);
    }
}
