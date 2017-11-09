package com.gala.tvapi.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.a;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import java.io.Serializable;

public class UserType implements Serializable {
    private boolean a = false;
    private boolean b = false;
    private boolean c = false;
    private boolean d = false;
    private boolean e = false;
    private boolean f = false;
    private boolean g = false;
    private boolean h = false;
    private boolean i = false;

    public void setExpire(boolean isExpire) {
        this.a = isExpire;
    }

    public void setMember(boolean isMember) {
        this.b = isMember;
    }

    public void setGold(boolean isGold) {
        this.c = isGold;
    }

    public void setSilver(boolean isSilver) {
        this.d = isSilver;
    }

    public void setPlatinum(boolean isPlatinum) {
        this.e = isPlatinum;
    }

    public void setPhoneMonth(boolean isPhoneMonth) {
        this.f = isPhoneMonth;
    }

    public void setLitchi(boolean isLitchi) {
        this.g = isLitchi;
    }

    public void setLitchiOverdue(boolean isOverdue) {
        this.i = isOverdue;
    }

    public boolean isExpire() {
        return this.a;
    }

    public boolean isMember() {
        return this.b;
    }

    public boolean isGold() {
        return this.c;
    }

    public boolean isSilver() {
        return this.d;
    }

    public boolean isPlatinum() {
        return this.e;
    }

    public boolean isPhoneMonth() {
        return this.f;
    }

    public boolean isLitchi() {
        return this.g;
    }

    public boolean isLitchiOverdue() {
        return this.i;
    }

    public String toJsonString() {
        JSONObject jSONObject = new JSONObject();
        if (this.a) {
            jSONObject.put("expire", Boolean.valueOf(true));
        } else {
            jSONObject.put("expire", Boolean.valueOf(false));
        }
        if (this.b) {
            jSONObject.put("member", Boolean.valueOf(true));
        } else {
            jSONObject.put("member", Boolean.valueOf(false));
        }
        if (this.c) {
            jSONObject.put(LoginConstant.CLICK_RESEAT_GETGOLD, Boolean.valueOf(true));
        } else {
            jSONObject.put(LoginConstant.CLICK_RESEAT_GETGOLD, Boolean.valueOf(false));
        }
        if (this.d) {
            jSONObject.put("silver", Boolean.valueOf(true));
        } else {
            jSONObject.put("silver", Boolean.valueOf(false));
        }
        if (this.e) {
            jSONObject.put("platinum", Boolean.valueOf(true));
        } else {
            jSONObject.put("platinum", Boolean.valueOf(false));
        }
        if (this.f) {
            jSONObject.put("phonemonth", Boolean.valueOf(true));
        } else {
            jSONObject.put("phonemonth", Boolean.valueOf(false));
        }
        if (this.g) {
            jSONObject.put("litchi", Boolean.valueOf(true));
        } else {
            jSONObject.put("litchi", Boolean.valueOf(false));
        }
        if (this.h) {
            jSONObject.put("twvip", Boolean.valueOf(true));
        } else {
            jSONObject.put("twvip", Boolean.valueOf(false));
        }
        if (this.i) {
            jSONObject.put("isLitchiOverdue", Boolean.valueOf(true));
        } else {
            jSONObject.put("isLitchiOverdue", Boolean.valueOf(false));
        }
        return jSONObject.toJSONString();
    }

    public static UserType parseString(String s) {
        a.a("UserType", "json=" + s);
        UserType userType = new UserType();
        JSONObject parseObject = JSON.parseObject(s);
        if (parseObject != null) {
            Boolean bool = parseObject.getBoolean("expire");
            if (bool != null && bool.booleanValue()) {
                userType.setExpire(true);
            }
            bool = parseObject.getBoolean("member");
            if (bool != null && bool.booleanValue()) {
                userType.setMember(true);
            }
            bool = parseObject.getBoolean(LoginConstant.CLICK_RESEAT_GETGOLD);
            if (bool != null && bool.booleanValue()) {
                userType.setGold(true);
            }
            bool = parseObject.getBoolean("silver");
            if (bool != null && bool.booleanValue()) {
                userType.setSilver(true);
            }
            bool = parseObject.getBoolean("platinum");
            if (bool != null && bool.booleanValue()) {
                userType.setPlatinum(true);
            }
            bool = parseObject.getBoolean("phonemonth");
            if (bool != null && bool.booleanValue()) {
                userType.setPhoneMonth(true);
            }
            bool = parseObject.getBoolean("litchi");
            if (bool != null && bool.booleanValue()) {
                userType.setLitchi(true);
            }
            bool = parseObject.getBoolean("twvip");
            if (bool != null && bool.booleanValue()) {
                userType.setTWVIP(true);
            }
            Boolean bool2 = parseObject.getBoolean("isLitchiOverdue");
            if (bool2 != null && bool2.booleanValue()) {
                userType.setLitchiOverdue(true);
            }
        }
        return userType;
    }

    public boolean isTWVIP() {
        return this.h;
    }

    public void setTWVIP(boolean mIsTWVIP) {
        this.h = mIsTWVIP;
    }
}
