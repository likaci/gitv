package com.gala.tvapi.vrs.model;

public class CommonUserInfo extends Model {
    private static final long serialVersionUID = 1;
    public String accountType = "";
    public String birthday = "";
    public int city = 0;
    public String email = "";
    public String icon = "";
    public String nickname = "";
    public String phone = "";
    public String real_name = "";
    public String regip = "";
    public String suid = "";
    public String uid = "";
    public String user_name = "";
    public GalaVipInfo vip_info = null;

    public void setVip_Info(GalaVipInfo info) {
        this.vip_info = info;
    }

    public GalaVipInfo getVipInfo() {
        return this.vip_info;
    }
}
