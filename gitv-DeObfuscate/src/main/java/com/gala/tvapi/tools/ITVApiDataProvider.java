package com.gala.tvapi.tools;

import com.gala.tvapi.log.C0262a;
import com.gala.tvapi.p023c.C0256d;
import com.gala.tvapi.tv3.cache.ApiDataCache;

public class ITVApiDataProvider {
    private static final ITVApiDataProvider f934a = new ITVApiDataProvider();

    private ITVApiDataProvider() {
    }

    public static ITVApiDataProvider getInstance() {
        return f934a;
    }

    public String getAuthorization() {
        return ApiDataCache.getRegisterDataCache().getAuthorization();
    }

    public String getEncryptKey() {
        String b = C0256d.m623b(ApiDataCache.getRegisterDataCache().getSecret(), ApiDataCache.getRegisterDataCache().getPublicKey());
        C0262a.m629a("ITVApiDataProvider", "key1=" + b);
        return b;
    }
}
