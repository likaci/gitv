package retrofit.android;

import android.net.http.AndroidHttpClient;
import retrofit.client.ApacheClient;

public final class AndroidApacheClient extends ApacheClient {
    public AndroidApacheClient() {
        super(AndroidHttpClient.newInstance("Retrofit"));
    }
}
