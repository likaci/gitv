package retrofit.client;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class OkClient extends UrlConnectionClient {
    private final OkUrlFactory okUrlFactory;

    private static OkHttpClient generateDefaultOkHttp() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(15000, TimeUnit.MILLISECONDS);
        client.setReadTimeout(20000, TimeUnit.MILLISECONDS);
        return client;
    }

    public OkClient() {
        this(generateDefaultOkHttp());
    }

    public OkClient(OkHttpClient client) {
        this.okUrlFactory = new OkUrlFactory(client);
    }

    protected HttpURLConnection openConnection(Request request) throws IOException {
        return this.okUrlFactory.open(new URL(request.getUrl()));
    }
}
