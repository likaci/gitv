package com.qiyi.tv.client.feature.account;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class VipInfo implements Parcelable, Serializable {
    public static final Creator<VipInfo> CREATOR = new Creator<VipInfo>() {
        public final VipInfo createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(VipInfo.class.getClassLoader());
            bundle.setClassLoader(VipInfo.class.getClassLoader());
            VipInfo vipInfo = new VipInfo();
            vipInfo.readBundle(bundle);
            return vipInfo;
        }

        public final VipInfo[] newArray(int size) {
            return new VipInfo[size];
        }
    };
    private static final String KEY_ISVIP = "vip.isvip";
    private static final String KEY_VIPDATE = "vip.date";
    private static final long serialVersionUID = 1;
    private boolean mIsVip = false;
    private String mVipDate;

    public VipInfo(String mVipDate, boolean mIsVip) {
        this.mVipDate = mVipDate;
        this.mIsVip = mIsVip;
    }

    public boolean isVip() {
        return this.mIsVip;
    }

    public void setIsVip(boolean mIsVip) {
        this.mIsVip = mIsVip;
    }

    public String getVipDate() {
        return this.mVipDate;
    }

    public void setVipDate(String mVipDate) {
        this.mVipDate = mVipDate;
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putString(KEY_VIPDATE, this.mVipDate);
        bundle.putBoolean(KEY_ISVIP, this.mIsVip);
    }

    protected void readBundle(Bundle bundle) {
        this.mVipDate = bundle.getString(KEY_VIPDATE);
        this.mIsVip = bundle.getBoolean(KEY_ISVIP);
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }
}
