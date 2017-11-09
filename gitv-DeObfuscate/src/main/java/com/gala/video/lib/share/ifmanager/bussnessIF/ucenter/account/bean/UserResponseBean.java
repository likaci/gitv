package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean;

import com.gala.tvapi.type.UserType;
import com.gala.video.api.ApiException;

public class UserResponseBean {
    private String account;
    private String cookie;
    private ApiException exception;
    private boolean isInsecureAccount;
    private String nickName;
    private String phone;
    private boolean respResult;
    private String uid;
    private UserType userType;

    public boolean isInsecureAccount() {
        return this.isInsecureAccount;
    }

    public void setInsecureAccount(boolean isInsecureAccount) {
        this.isInsecureAccount = isInsecureAccount;
    }

    public boolean getRespResult() {
        return this.respResult;
    }

    public void setRespResult(boolean respResult) {
        this.respResult = respResult;
    }

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getNickName() {
        return this.nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public UserType getUserType() {
        return this.userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public ApiException getException() {
        return this.exception;
    }

    public void setException(ApiException exception) {
        this.exception = exception;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
