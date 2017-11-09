package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.model.Package;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultPackageContent extends ApiResult {
    private String a = "";
    private String b = "";
    private String c = "";
    private String d = "";
    public List<Package> data = null;
    public String supportVodCoupon = "";

    public void setData(List<Package> ps) {
        this.data = ps;
    }

    public List<Package> getPackages() {
        return this.data;
    }

    public boolean isCanBuyPlatinumPackage() {
        if (this.data != null && this.data.size() > 0) {
            for (Package packageR : this.data) {
                if (packageR.type.equals("1")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCanBuyBroadcast() {
        if (this.data != null && this.data.size() > 0) {
            for (Package packageR : this.data) {
                if (packageR.type.equals("0")) {
                    this.a = packageR.price;
                    this.b = packageR.code;
                    this.c = packageR.periodUnit;
                    this.d = packageR.period;
                    return true;
                }
            }
        }
        return false;
    }

    public String getBroadcastPrice() {
        if (this.a.isEmpty()) {
            isCanBuyBroadcast();
        }
        return this.a;
    }

    public String getBroadcastCode() {
        if (this.b.isEmpty()) {
            isCanBuyBroadcast();
        }
        return this.b;
    }

    public String getBroadcastPeriod() {
        if (this.c.isEmpty() || this.d.isEmpty()) {
            return "";
        }
        String str = this.c;
        String str2 = this.d;
        if ((TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN ? 1 : null) != null) {
            if (str.equals("1")) {
                return str2 + "天";
            }
            if (str.equals("2")) {
                return str2 + "個月";
            }
            if (str.equals("3")) {
                return str2 + "小時";
            }
        } else if (str.equals("1")) {
            return str2 + "天";
        } else {
            if (str.equals("2")) {
                return str2 + "个月";
            }
            if (str.equals("3")) {
                return str2 + "小时";
            }
        }
        return null;
    }
}
