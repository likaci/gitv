package com.gala.tvapi.vrs.result;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.PlatformType;
import com.gala.tvapi.vrs.model.Package;
import com.gala.video.api.ApiResult;
import java.util.List;

public class ApiResultPackageContent extends ApiResult {
    private String f1353a = "";
    private String f1354b = "";
    private String f1355c = "";
    private String f1356d = "";
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
                    this.f1353a = packageR.price;
                    this.f1354b = packageR.code;
                    this.f1355c = packageR.periodUnit;
                    this.f1356d = packageR.period;
                    return true;
                }
            }
        }
        return false;
    }

    public String getBroadcastPrice() {
        if (this.f1353a.isEmpty()) {
            isCanBuyBroadcast();
        }
        return this.f1353a;
    }

    public String getBroadcastCode() {
        if (this.f1354b.isEmpty()) {
            isCanBuyBroadcast();
        }
        return this.f1354b;
    }

    public String getBroadcastPeriod() {
        if (this.f1355c.isEmpty() || this.f1356d.isEmpty()) {
            return "";
        }
        String str = this.f1355c;
        String str2 = this.f1356d;
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
