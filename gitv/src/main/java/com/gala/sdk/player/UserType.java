package com.gala.sdk.player;

import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

public class UserType {
    private int mGalaUserType;
    private int mLitchiUserType;

    public static final class GalaUserType {
        public static final int NOT_VIP = 1;
        public static final int TAIWAN_VIP_GOLD = 7;
        public static final int VIP_BANNED = 2;
        public static final int VIP_GOLD = 5;
        public static final int VIP_LEGAL = 3;
        public static final int VIP_PLATINUM = 6;
        public static final int VIP_SILVER = 4;
    }

    public static final class LitchiUserType {
        public static final int LEGAL = 2;
        public static final int NOT_VIP = 1;
        public static final int OVERDURE = 3;
    }

    public void setLitchiUserType(int litchiUserType) {
        this.mLitchiUserType = litchiUserType;
    }

    public void setGalaUserType(int GalaUserType) {
        this.mGalaUserType = GalaUserType;
    }

    public int getLitchiUserType() {
        return this.mLitchiUserType;
    }

    public int getGalaUserType() {
        return this.mGalaUserType;
    }

    public String toJsonString() {
        boolean z;
        boolean z2 = true;
        Map hashMap = new HashMap();
        hashMap.put("litchi", Boolean.valueOf(this.mLitchiUserType == 2));
        String str = "isLitchiOverdue";
        if (this.mLitchiUserType == 3) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        str = "expire";
        if (this.mGalaUserType == 2) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        str = "member";
        if (this.mGalaUserType == 3) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        str = LoginConstant.CLICK_RESEAT_GETGOLD;
        if (this.mGalaUserType == 5) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        str = "silver";
        if (this.mGalaUserType == 5) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        str = "platinum";
        if (this.mGalaUserType == 6) {
            z = true;
        } else {
            z = false;
        }
        hashMap.put(str, Boolean.valueOf(z));
        String str2 = "tw_vip";
        if (this.mGalaUserType != 7) {
            z2 = false;
        }
        hashMap.put(str2, Boolean.valueOf(z2));
        return new JSONObject(hashMap).toString();
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UserType@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("mLitchiUserType=").append(this.mLitchiUserType);
        stringBuilder.append(", mGalaUserType=").append(this.mGalaUserType);
        return stringBuilder.toString();
    }
}
