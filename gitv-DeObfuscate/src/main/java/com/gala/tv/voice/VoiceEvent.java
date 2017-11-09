package com.gala.tv.voice;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.gala.tv.voice.core.Log;
import java.util.ArrayList;

public class VoiceEvent implements Parcelable {
    public static final Creator<VoiceEvent> CREATOR = new C01991();
    public static final int TYPE_ALL = -1;
    public static final int TYPE_EPISODE_DIRECTION = 15;
    public static final int TYPE_EPISODE_INDEX = 10;
    public static final int TYPE_KEYEVENT = 13;
    public static final int TYPE_KEYWORDS = 4;
    public static final int TYPE_NUMBER = 5;
    public static final int TYPE_PAGE_DIRECTION = 14;
    public static final int TYPE_PAGE_INDEX = 7;
    public static final int TYPE_PLAY = 16;
    public static final int TYPE_POSITION = 6;
    public static final int TYPE_SEARCH = 3;
    public static final int TYPE_SEEK_OFFSET = 2;
    public static final int TYPE_SEEK_TO = 1;
    public static final int TYPE_UNKNOWN = 0;
    private int f797a;
    private final Bundle f798a;
    private String f799a;
    private String f800b;

    static class C01991 implements Creator<VoiceEvent> {
        C01991() {
        }

        public final VoiceEvent createFromParcel(Parcel parcel) {
            return new VoiceEvent(parcel);
        }

        public final VoiceEvent[] newArray(int i) {
            return new VoiceEvent[i];
        }
    }

    private VoiceEvent(Parcel parcel) {
        this.f798a = new Bundle();
        Bundle readBundle = parcel.readBundle();
        if (readBundle != null) {
            this.f797a = readBundle.getInt("KEY_TYPE", 0);
            if (readBundle.containsKey("KEY_KEYWORDS")) {
                ArrayList stringArrayList = readBundle.getStringArrayList("KEY_KEYWORDS");
                if (stringArrayList != null && stringArrayList.size() > 0) {
                    this.f800b = (String) stringArrayList.get(0);
                }
            } else {
                this.f800b = readBundle.getString("KEY_KEYWORD");
            }
            this.f799a = readBundle.getString("KEY_ORIGNAL");
            Bundle bundle = readBundle.getBundle("KEY_EXTRAS");
            if (bundle != null) {
                this.f798a.putAll(bundle);
            }
        }
        Log.m525d("VoiceEvent", "VoiceEvent() " + toString());
    }

    VoiceEvent(int i, String str) {
        this.f798a = new Bundle();
        this.f797a = i;
        this.f800b = str;
    }

    public int getType() {
        return this.f797a;
    }

    public String getKeyword() {
        return this.f800b;
    }

    public String getOrignal() {
        return this.f799a;
    }

    public void setOrignal(String str) {
        this.f799a = str;
    }

    public void putExtras(Bundle bundle) {
        this.f798a.putAll(bundle);
    }

    public Bundle getExtras() {
        return new Bundle(this.f798a);
    }

    public String toString() {
        return "VoiceEvent(mType=" + this.f797a + ", mKeyword=" + this.f800b + ", orignal=" + this.f799a + ", mBundle=" + this.f798a + ")";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_TYPE", this.f797a);
        bundle.putString("KEY_KEYWORD", this.f800b);
        if (!TextUtils.isEmpty(this.f799a)) {
            bundle.putString("KEY_ORIGNAL", this.f799a);
        }
        if (!this.f798a.isEmpty()) {
            bundle.putBundle("KEY_EXTRAS", this.f798a);
        }
        parcel.writeBundle(bundle);
    }
}
