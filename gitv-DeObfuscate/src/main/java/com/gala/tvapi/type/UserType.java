package com.gala.tvapi.type;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.log.C0262a;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import java.io.Serializable;

public class UserType implements Serializable {
    private boolean f1156a = false;
    private boolean f1157b = false;
    private boolean f1158c = false;
    private boolean f1159d = false;
    private boolean f1160e = false;
    private boolean f1161f = false;
    private boolean f1162g = false;
    private boolean f1163h = false;
    private boolean f1164i = false;

    public void setExpire(boolean isExpire) {
        this.f1156a = isExpire;
    }

    public void setMember(boolean isMember) {
        this.f1157b = isMember;
    }

    public void setGold(boolean isGold) {
        this.f1158c = isGold;
    }

    public void setSilver(boolean isSilver) {
        this.f1159d = isSilver;
    }

    public void setPlatinum(boolean isPlatinum) {
        this.f1160e = isPlatinum;
    }

    public void setPhoneMonth(boolean isPhoneMonth) {
        this.f1161f = isPhoneMonth;
    }

    public void setLitchi(boolean isLitchi) {
        this.f1162g = isLitchi;
    }

    public void setLitchiOverdue(boolean isOverdue) {
        this.f1164i = isOverdue;
    }

    public boolean isExpire() {
        return this.f1156a;
    }

    public boolean isMember() {
        return this.f1157b;
    }

    public boolean isGold() {
        return this.f1158c;
    }

    public boolean isSilver() {
        return this.f1159d;
    }

    public boolean isPlatinum() {
        return this.f1160e;
    }

    public boolean isPhoneMonth() {
        return this.f1161f;
    }

    public boolean isLitchi() {
        return this.f1162g;
    }

    public boolean isLitchiOverdue() {
        return this.f1164i;
    }

    public String toJsonString() {
        JSONObject jSONObject = new JSONObject();
        if (this.f1156a) {
            jSONObject.put("expire", Boolean.valueOf(true));
        } else {
            jSONObject.put("expire", Boolean.valueOf(false));
        }
        if (this.f1157b) {
            jSONObject.put("member", Boolean.valueOf(true));
        } else {
            jSONObject.put("member", Boolean.valueOf(false));
        }
        if (this.f1158c) {
            jSONObject.put(LoginConstant.CLICK_RESEAT_GETGOLD, Boolean.valueOf(true));
        } else {
            jSONObject.put(LoginConstant.CLICK_RESEAT_GETGOLD, Boolean.valueOf(false));
        }
        if (this.f1159d) {
            jSONObject.put("silver", Boolean.valueOf(true));
        } else {
            jSONObject.put("silver", Boolean.valueOf(false));
        }
        if (this.f1160e) {
            jSONObject.put("platinum", Boolean.valueOf(true));
        } else {
            jSONObject.put("platinum", Boolean.valueOf(false));
        }
        if (this.f1161f) {
            jSONObject.put("phonemonth", Boolean.valueOf(true));
        } else {
            jSONObject.put("phonemonth", Boolean.valueOf(false));
        }
        if (this.f1162g) {
            jSONObject.put("litchi", Boolean.valueOf(true));
        } else {
            jSONObject.put("litchi", Boolean.valueOf(false));
        }
        if (this.f1163h) {
            jSONObject.put("twvip", Boolean.valueOf(true));
        } else {
            jSONObject.put("twvip", Boolean.valueOf(false));
        }
        if (this.f1164i) {
            jSONObject.put("isLitchiOverdue", Boolean.valueOf(true));
        } else {
            jSONObject.put("isLitchiOverdue", Boolean.valueOf(false));
        }
        return jSONObject.toJSONString();
    }

    public static UserType parseString(String s) {
        C0262a.m629a("UserType", "json=" + s);
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
        return this.f1163h;
    }

    public void setTWVIP(boolean mIsTWVIP) {
        this.f1163h = mIsTWVIP;
    }
}
