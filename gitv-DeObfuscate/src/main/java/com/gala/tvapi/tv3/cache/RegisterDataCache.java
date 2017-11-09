package com.gala.tvapi.tv3.cache;

import com.gala.tvapi.p008b.C0214a;
import com.gala.tvapi.tv3.TVApiConfig;

public class RegisterDataCache extends ApiCache {
    public void putSecret(String secret) {
        ApiCache.m732a("secret", secret);
    }

    public String getSecret() {
        return ApiCache.m729a("secret");
    }

    public void putExpiredln(long expiredln) {
        ApiCache.m731a("expiredln", expiredln);
    }

    public long getExpiredln() {
        return ApiCache.m729a("expiredln");
    }

    public void putUniqueId(String uniqueId) {
        ApiCache.m732a("uniqueId", uniqueId);
    }

    public String getUniqueId() {
        return ApiCache.m729a("uniqueId");
    }

    public void putRequestTime(long requestTime) {
        ApiCache.m731a("requesttime", requestTime);
    }

    public long getRequestTime() {
        return ApiCache.m729a("requesttime");
    }

    public void putAuthorization(String authorization) {
        ApiCache.m732a("Authorization", authorization);
    }

    public String getAuthorization() {
        return ApiCache.m729a("Authorization");
    }

    public void putPublicKeyMd5(String md5) {
        ApiCache.m732a("publickeymd5", md5);
    }

    public String getPublicKeyMd5() {
        return ApiCache.m729a("publickeymd5");
    }

    public String getPublicKey() {
        return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLxoJ3Gi6bJjDlrik9WVNMMbuydTRlm3sMuUMMGyH/K/YF4jiqdqBhV1vcraoJTGdqmLeANwY+/QLKfrzBcXqBgxd5T4rQJJaUCcJsEcdtpHeuu75C+sFOgeeCR46SwKA5J/u4anc+IpA/UfbUf/nKaEHjI4np+CWWM/N/UWpDbwIDAQAB";
    }

    public void putApkVersion(String version) {
        ApiCache.m732a("apkversion", version);
    }

    public String getApkVersion() {
        return ApiCache.m729a("apkversion");
    }

    public void putUUID(String uuid) {
        ApiCache.m732a("uuid", uuid);
    }

    public String getUUID() {
        return ApiCache.m729a("uuid");
    }

    public boolean isRegisterCacheAvailable() {
        long requestTime = getRequestTime();
        long expiredln = getExpiredln();
        long serviceTime = ApiDataCache.getTimeDataCache().getServiceTime();
        long deviceTime = ApiDataCache.getTimeDataCache().getDeviceTime();
        String secret = getSecret();
        String uniqueId = getUniqueId();
        String publicKeyMd5 = getPublicKeyMd5();
        String a = C0214a.m580a("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLxoJ3Gi6bJjDlrik9WVNMMbuydTRlm3sMuUMMGyH/K/YF4jiqdqBhV1vcraoJTGdqmLeANwY+/QLKfrzBcXqBgxd5T4rQJJaUCcJsEcdtpHeuu75C+sFOgeeCR46SwKA5J/u4anc+IpA/UfbUf/nKaEHjI4np+CWWM/N/UWpDbwIDAQAB");
        String authorization = getAuthorization();
        String uuid = getUUID();
        if (publicKeyMd5 == null) {
            return false;
        }
        if (!publicKeyMd5.equals(a) || requestTime == 0 || expiredln == 0 || secret == null || uniqueId == null || authorization == null || (serviceTime + ((System.currentTimeMillis() / 1000) - deviceTime)) - requestTime >= expiredln * 60 || !TVApiConfig.get().getApkVersion().equals(getApkVersion()) || !TVApiConfig.get().getUuid().equals(uuid)) {
            return false;
        }
        return true;
    }
}
