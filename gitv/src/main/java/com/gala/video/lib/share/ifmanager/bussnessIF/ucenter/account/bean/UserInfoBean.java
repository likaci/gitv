package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;

public class UserInfoBean implements Serializable {
    private static final long serialVersionUID = -7422318385671873266L;
    private String account;
    private String cookie;
    private String name;
    private String phone;

    public String getCookie() {
        return this.cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return this.account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String toString() {
        return "LoginUserInfo [cookie=" + this.cookie + ", name=" + this.name + ", account=" + this.account + AlbumEnterFactory.SIGN_STR;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return this.phone;
    }
}
