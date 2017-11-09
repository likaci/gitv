package com.gala.tvapi.tools;

import com.gala.tvapi.c.d;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tv3.cache.ApiDataCache;

public class ITVApiDataProvider {
    private static final ITVApiDataProvider a = new ITVApiDataProvider();

    private ITVApiDataProvider() {
    }

    public static ITVApiDataProvider getInstance() {
        return a;
    }

    public String getAuthorization() {
        return ApiDataCache.getRegisterDataCache().getAuthorization();
    }

    public String getEncryptKey() {
        String b = d.b(ApiDataCache.getRegisterDataCache().getSecret(), ApiDataCache.getRegisterDataCache().getPublicKey());
        a.a("ITVApiDataProvider", "key1=" + b);
        return b;
    }
}
