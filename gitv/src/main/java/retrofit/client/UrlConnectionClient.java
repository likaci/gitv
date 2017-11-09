package retrofit.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import org.cybergarage.http.HTTP;
import retrofit.mime.TypedInput;
import retrofit.mime.TypedOutput;

public class UrlConnectionClient implements Client {
    private static final int CHUNK_SIZE = 4096;

    private static class TypedInputStream implements TypedInput {
        private final long length;
        private final String mimeType;
        private final InputStream stream;

        private TypedInputStream(String mimeType, long length, InputStream stream) {
            this.mimeType = mimeType;
            this.length = length;
            this.stream = stream;
        }

        public String mimeType() {
            return this.mimeType;
        }

        public long length() {
            return this.length;
        }

        public InputStream in() throws IOException {
            return this.stream;
        }
    }

    public Response execute(Request request) throws IOException {
        HttpURLConnection connection = openConnection(request);
        prepareRequest(connection, request);
        return readResponse(connection);
    }

    protected HttpURLConnection openConnection(Request request) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(request.getUrl()).openConnection();
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(20000);
        return connection;
    }

    void prepareRequest(HttpURLConnection connection, Request request) throws IOException {
        connection.setRequestMethod(request.getMethod());
        connection.setDoInput(true);
        for (Header header : request.getHeaders()) {
            connection.addRequestProperty(header.getName(), header.getValue());
        }
        TypedOutput body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HTTP.CONTENT_TYPE, body.mimeType());
            long length = body.length();
            if (length != -1) {
                connection.setFixedLengthStreamingMode((int) length);
                connection.addRequestProperty(HTTP.CONTENT_LENGTH, String.valueOf(length));
            } else {
                connection.setChunkedStreamingMode(4096);
            }
            body.writeTo(connection.getOutputStream());
        }
    }

    Response readResponse(HttpURLConnection connection) throws IOException {
        InputStream stream;
        int status = connection.getResponseCode();
        String reason = connection.getResponseMessage();
        if (reason == null) {
            reason = "";
        }
        List<Header> headers = new ArrayList();
        for (Entry<String, List<String>> field : connection.getHeaderFields().entrySet()) {
            String name = (String) field.getKey();
            for (String value : (List) field.getValue()) {
                headers.add(new Header(name, value));
            }
        }
        String mimeType = connection.getContentType();
        int length = connection.getContentLength();
        if (status >= 400) {
            stream = connection.getErrorStream();
        } else {
            stream = connection.getInputStream();
        }
        return new Response(connection.getURL().toString(), status, reason, headers, new TypedInputStream(mimeType, (long) length, stream));
    }
}
