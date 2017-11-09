package com.qiyi.tv.client.feature.account;

public class UserInfo {
    private int f2067a;
    private long f2068a;
    private String f2069a;
    private String f2070b;
    private String f2071c;
    private String f2072d;
    private String f2073e;
    private String f2074f;
    private String f2075g;
    private String f2076h;

    public UserInfo(String uid, String token, String nickName, long expire) {
        this.f2069a = uid;
        this.f2071c = token;
        this.f2073e = nickName;
        this.f2068a = expire;
    }

    public String getUid() {
        return this.f2069a;
    }

    public void setUid(String uid) {
        this.f2069a = uid;
    }

    public String getAuthCookie() {
        return this.f2070b;
    }

    public void setAuthCookie(String authCookie) {
        this.f2070b = authCookie;
    }

    public String getToken() {
        return this.f2071c;
    }

    public void setToken(String token) {
        this.f2071c = token;
    }

    public String getName() {
        return this.f2072d;
    }

    public void setName(String name) {
        this.f2072d = name;
    }

    public String getNickName() {
        return this.f2073e;
    }

    public void setNickName(String nickName) {
        this.f2073e = nickName;
    }

    public int getGender() {
        return this.f2067a;
    }

    public void setGender(int gender) {
        this.f2067a = gender;
    }

    public long getExpire() {
        return this.f2068a;
    }

    public void setExpire(long expire) {
        this.f2068a = expire;
    }

    public String getIconUrl() {
        return this.f2074f;
    }

    public void setIconUrl(String iconUrl) {
        this.f2074f = iconUrl;
    }

    public String getRefreshToken() {
        return this.f2075g;
    }

    public void setRefreshToken(String refreshToken) {
        this.f2075g = refreshToken;
    }

    public String getTokenSecret() {
        return this.f2076h;
    }

    public void setTokenSecret(String tokenSecret) {
        this.f2076h = tokenSecret;
    }
}
