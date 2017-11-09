package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class Channel implements Parcelable, Serializable {
    public static final Creator<Channel> CREATOR = new C19841();
    public static final int ID_1080P = 104;
    public static final int ID_3D = 103;
    public static final int ID_4K = 105;
    public static final int ID_CAR = 17;
    public static final int ID_CARTOON = 3;
    public static final int ID_CINEMA = 102;
    public static final int ID_DOCUMENTARY = 15;
    public static final int ID_DUBY = 106;
    public static final int ID_EDUCATION = 8;
    public static final int ID_EMOTION = 1007;
    public static final int ID_ENCODE = 1009;
    public static final int ID_ENTERTAINMENT = 6;
    public static final int ID_EPISODE = 2;
    public static final int ID_FASHION = 11;
    public static final int ID_FILM = 1;
    public static final int ID_FINANCE = 14;
    public static final int ID_FUNNY = 12;
    public static final int ID_H265 = 107;
    public static final int ID_HEALTH = 1008;
    public static final int ID_HOT = 20;
    public static final int ID_INFANT = 29;
    public static final int ID_KIDS = 16;
    public static final int ID_LATEST = 108;
    public static final int ID_LAW = 1006;
    public static final int ID_LIFE = 13;
    public static final int ID_LIYUANCUN = 10011;
    public static final int ID_MILITARY = 19;
    public static final int ID_MUSIC = 4;
    public static final int ID_MUSIC_MV = 10013;
    public static final int ID_NEWS = 18;
    public static final int ID_NG = 7;
    public static final int ID_OPUSCULUM = 10010;
    public static final int ID_SPORTS = 9;
    public static final int ID_SQUARE = 10012;
    public static final int ID_TRAVEL = 10;
    public static final int ID_VARIETY = 5;
    public static final int ID_VIP = 109;
    private static final String KEY_ICON_URL = "channel.icon_url";
    private static final String KEY_ID = "channel.id";
    private static final String KEY_NAME = "channel.name";
    private static final String KEY_PICURL = "channel.picurl";
    private static final String KEY_USER_TAGS = "channel.user_tags";
    private static final long serialVersionUID = 1;
    private String mIconUrl;
    private int mId;
    private String mName;
    private String mPicUrl;
    private final UserTags mUserTags = new UserTags();

    static class C19841 implements Creator<Channel> {
        C19841() {
        }

        public final Channel createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(Channel.class.getClassLoader());
            bundle.setClassLoader(Channel.class.getClassLoader());
            Channel channel = new Channel();
            channel.readBundle(bundle);
            return channel;
        }

        public final Channel[] newArray(int size) {
            return new Channel[size];
        }
    }

    public int getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public Picture getImage() {
        return new Picture(this.mPicUrl);
    }

    public Picture getIcon() {
        return new Picture(this.mIconUrl);
    }

    public String getPicUrl() {
        return this.mPicUrl;
    }

    public String getIconUrl() {
        return this.mIconUrl;
    }

    public UserTags getUserTags() {
        return this.mUserTags;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setPicUrl(String url) {
        this.mPicUrl = url;
    }

    public void setIconUrl(String iconUrl) {
        this.mIconUrl = iconUrl;
    }

    public int describeContents() {
        return 0;
    }

    protected void readBundle(Bundle bundle) {
        this.mId = bundle.getInt(KEY_ID);
        this.mName = bundle.getString(KEY_NAME);
        this.mPicUrl = bundle.getString(KEY_PICURL);
        this.mUserTags.copy((UserTags) bundle.getParcelable(KEY_USER_TAGS));
        this.mIconUrl = bundle.getString(KEY_ICON_URL);
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putInt(KEY_ID, this.mId);
        bundle.putString(KEY_NAME, this.mName);
        bundle.putString(KEY_PICURL, this.mPicUrl);
        bundle.putParcelable(KEY_USER_TAGS, this.mUserTags);
        bundle.putString(KEY_ICON_URL, this.mIconUrl);
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }
}
