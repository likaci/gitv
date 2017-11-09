package com.qiyi.tv.client.feature.account;

public class UserInfo {
    private int a;
    private long f823a;
    private String f824a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;

    public UserInfo(String uid, String token, String nickName, long expire) {
        this.f824a = uid;
        this.c = token;
        this.e = nickName;
        this.f823a = expire;
    }

    public String getUid() {
        return this.f824a;
    }

    public void setUid(String uid) {
        this.f824a = uid;
    }

    public String getAuthCookie() {
        return this.b;
    }

    public void setAuthCookie(String authCookie) {
        this.b = authCookie;
    }

    public String getToken() {
        return this.c;
    }

    public void setToken(String token) {
        this.c = token;
    }

    public String getName() {
        return this.d;
    }

    public void setName(String name) {
        this.d = name;
    }

    public String getNickName() {
        return this.e;
    }

    public void setNickName(String nickName) {
        this.e = nickName;
    }

    public int getGender() {
        return this.a;
    }

    public void setGender(int gender) {
        this.a = gender;
    }

    public long getExpire() {
        return this.f823a;
    }

    public void setExpire(long expire) {
        this.f823a = expire;
    }

    public String getIconUrl() {
        return this.f;
    }

    public void setIconUrl(String iconUrl) {
        this.f = iconUrl;
    }

    public String getRefreshToken() {
        return this.g;
    }

    public void setRefreshToken(String refreshToken) {
        this.g = refreshToken;
    }

    public String getTokenSecret() {
        return this.h;
    }

    public void setTokenSecret(String tokenSecret) {
        this.h = tokenSecret;
    }
}
