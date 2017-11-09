package com.gala.sdk.player;

public class Account {
    private String mCookie;
    private int mLoginType;
    private String mOpenId;
    private String mToken;
    private String mUserId;
    private UserType mUserType;

    private Account(String cookie, String uid, UserType userType) {
        this.mCookie = cookie;
        this.mUserId = uid;
        this.mUserType = userType;
        this.mLoginType = 2;
    }

    private Account(String openId, String token) {
        this.mOpenId = openId;
        this.mToken = token;
    }

    private Account() {
    }

    public String getOpenId() {
        return this.mOpenId;
    }

    public String getToken() {
        return this.mToken;
    }

    public String getCookie() {
        return this.mCookie;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public UserType getUserType() {
        return this.mUserType;
    }

    public int getLoginType() {
        return this.mLoginType;
    }

    public static Account createSharedAccount(String cookie, String uid, UserType userType) {
        return new Account(cookie, uid, userType);
    }

    public static Account createOutsideAccount(String openId, String token) {
        return new Account(openId, token);
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Account{");
        stringBuilder.append("uid=").append(this.mUserId);
        stringBuilder.append("cookie=").append(this.mCookie);
        stringBuilder.append("openId=").append(this.mOpenId);
        stringBuilder.append("token=").append(this.mToken);
        stringBuilder.append("loginType=").append(this.mLoginType).append("}");
        return stringBuilder.toString();
    }
}
