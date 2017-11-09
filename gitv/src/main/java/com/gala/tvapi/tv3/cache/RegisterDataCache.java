package com.gala.tvapi.tv3.cache;

import com.gala.tvapi.b.a;
import com.gala.tvapi.tv3.TVApiConfig;

public class RegisterDataCache extends ApiCache {
    public void putSecret(String secret) {
        ApiCache.a("secret", secret);
    }

    public String getSecret() {
        return ApiCache.a("secret");
    }

    public void putExpiredln(long expiredln) {
        ApiCache.a("expiredln", expiredln);
    }

    public long getExpiredln() {
        return ApiCache.a("expiredln");
    }

    public void putUniqueId(String uniqueId) {
        ApiCache.a("uniqueId", uniqueId);
    }

    public String getUniqueId() {
        return ApiCache.a("uniqueId");
    }

    public void putRequestTime(long requestTime) {
        ApiCache.a("requesttime", requestTime);
    }

    public long getRequestTime() {
        return ApiCache.a("requesttime");
    }

    public void putAuthorization(String authorization) {
        ApiCache.a("Authorization", authorization);
    }

    public String getAuthorization() {
        return ApiCache.a("Authorization");
    }

    public void putPublicKeyMd5(String md5) {
        ApiCache.a("publickeymd5", md5);
    }

    public String getPublicKeyMd5() {
        return ApiCache.a("publickeymd5");
    }

    public String getPublicKey() {
        return "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLxoJ3Gi6bJjDlrik9WVNMMbuydTRlm3sMuUMMGyH/K/YF4jiqdqBhV1vcraoJTGdqmLeANwY+/QLKfrzBcXqBgxd5T4rQJJaUCcJsEcdtpHeuu75C+sFOgeeCR46SwKA5J/u4anc+IpA/UfbUf/nKaEHjI4np+CWWM/N/UWpDbwIDAQAB";
    }

    public void putApkVersion(String version) {
        ApiCache.a("apkversion", version);
    }

    public String getApkVersion() {
        return ApiCache.a("apkversion");
    }

    public void putUUID(String uuid) {
        ApiCache.a("uuid", uuid);
    }

    public String getUUID() {
        return ApiCache.a("uuid");
    }

    public boolean isRegisterCacheAvailable() {
        long requestTime = getRequestTime();
        long expiredln = getExpiredln();
        long serviceTime = ApiDataCache.getTimeDataCache().getServiceTime();
        long deviceTime = ApiDataCache.getTimeDataCache().getDeviceTime();
        String secret = getSecret();
        String uniqueId = getUniqueId();
        String publicKeyMd5 = getPublicKeyMd5();
        String a = a.a("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLxoJ3Gi6bJjDlrik9WVNMMbuydTRlm3sMuUMMGyH/K/YF4jiqdqBhV1vcraoJTGdqmLeANwY+/QLKfrzBcXqBgxd5T4rQJJaUCcJsEcdtpHeuu75C+sFOgeeCR46SwKA5J/u4anc+IpA/UfbUf/nKaEHjI4np+CWWM/N/UWpDbwIDAQAB");
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
