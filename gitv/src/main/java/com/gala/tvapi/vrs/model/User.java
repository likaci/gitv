package com.gala.tvapi.vrs.model;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PackageType;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.type.TimePeriod;
import com.gala.tvapi.type.UserType;

public class User extends Model {
    private static final long serialVersionUID = 1;
    public String authcookie = "";
    public String birthday = null;
    public String cion;
    public String city = null;
    public String cookie_qencry = null;
    public String edu = null;
    public String email = null;
    public String gender = null;
    public String icon = null;
    public String industry = null;
    public int insecure_account = 0;
    public String login_state = null;
    public String personal_url = null;
    public String phone = null;
    public PPSVipInfo pps_vip = null;
    public String province = null;
    public String real_name = null;
    public String redirect = null;
    public String self_intro = null;
    public GalaVipInfo tv_vip_info = null;
    public String uid;
    public String uname = null;
    public String user_nick;
    public CommonUserInfo userinfo = null;
    public GalaVipInfo vip_info = null;
    public String work = null;

    public void setVip_Info(GalaVipInfo info) {
        this.vip_info = info;
    }

    public GalaVipInfo getGalaVipInfo() {
        if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN || this.tv_vip_info == null) {
            return this.vip_info;
        }
        return this.tv_vip_info;
    }

    public void setPPS_Vip_Info(PPSVipInfo info) {
        this.pps_vip = info;
    }

    public PPSVipInfo getPPSVipInfo() {
        return this.pps_vip;
    }

    public void setUserInfo(CommonUserInfo info) {
        this.userinfo = info;
    }

    public CommonUserInfo getUserInfo() {
        return this.userinfo;
    }

    public String getUid() {
        if (this.userinfo == null || this.userinfo.uid == null || this.userinfo.uid.isEmpty()) {
            return this.uid;
        }
        return this.userinfo.uid;
    }

    public String getCookie() {
        if (this.authcookie == null || this.authcookie.isEmpty()) {
            return this.cookie_qencry;
        }
        return this.authcookie;
    }

    public UserType getUserType() {
        UserType userType;
        UserType userType2 = null;
        PlatformType platform = TVApiBase.getTVApiProperty().getPlatform();
        if (platform == PlatformType.TAIWAN || platform == PlatformType.VR_ANDROID_ALLINONE || platform == PlatformType.ANDROID_PHONE || this.tv_vip_info == null) {
            userType = null;
        } else {
            userType = this.tv_vip_info.getUserType();
        }
        if (this.vip_info != null) {
            userType2 = this.vip_info.getUserType();
            if (userType != null) {
                if (userType.isLitchi()) {
                    userType2.setLitchi(true);
                }
                if (userType.isLitchiOverdue()) {
                    userType2.setLitchiOverdue(true);
                }
            }
        }
        if (!(this.userinfo == null || this.userinfo.vip_info == null || r1 != null)) {
            userType2 = this.userinfo.vip_info.getUserType();
            if (userType != null) {
                if (userType.isLitchi()) {
                    userType2.setLitchi(true);
                }
                if (userType.isLitchiOverdue()) {
                    userType2.setLitchiOverdue(true);
                }
            }
        }
        if (userType2 != null) {
            return userType2;
        }
        if (userType != null) {
            return userType;
        }
        return new UserType();
    }

    public PackageType getPackageType() {
        if (this.vip_info == null) {
            return PackageType.NO_PACKAGE;
        }
        return this.vip_info.getPackageType();
    }

    public TimePeriod getTimePeriod() {
        if (this.vip_info == null || this.vip_info.type.equals("0")) {
            return TimePeriod.INVALID;
        }
        if (this.vip_info.status.equals("1")) {
            return TimePeriod.VALID;
        }
        if (this.vip_info.status.equals("2")) {
            return TimePeriod.LOCKED;
        }
        if (this.vip_info.status.equals("3")) {
            return TimePeriod.OVERDUE;
        }
        return TimePeriod.INVALID;
    }

    public boolean isInsecureAccount() {
        return this.insecure_account == 1;
    }
}
