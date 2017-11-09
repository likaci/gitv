package com.tvos.apps.utils.thirdparty;

import com.squareup.okhttp.OkHttpClient;
import java.util.concurrent.TimeUnit;
import retrofit.RestAdapter.Builder;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.OkClient;

public class RetrofitUtils {
    private static final long HTTP_CONNECT_TIMEOUT_INMILLIS_DEF = 10000;
    private static final long HTTP_READ_TIMEOUT_INMILLIS_DEF = 10000;

    public static <T> T createRest(String endpoint, Class<T> restCls) {
        OkHttpClient okClient = new OkHttpClient();
        okClient.setConnectTimeout(10000, TimeUnit.MILLISECONDS);
        okClient.setReadTimeout(10000, TimeUnit.MILLISECONDS);
        return new Builder().setEndpoint(endpoint).setLogLevel(LogLevel.FULL).setClient(new OkClient(okClient)).build().create(restCls);
    }
}
