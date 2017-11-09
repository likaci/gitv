package com.gala.tv.voice;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.gala.tv.voice.core.Log;
import java.util.ArrayList;

public class VoiceEvent implements Parcelable {
    public static final Creator<VoiceEvent> CREATOR = new Creator<VoiceEvent>() {
        public final VoiceEvent createFromParcel(Parcel parcel) {
            return new VoiceEvent(parcel);
        }

        public final VoiceEvent[] newArray(int i) {
            return new VoiceEvent[i];
        }
    };
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
    private int a;
    private final Bundle f398a;
    private String f399a;
    private String b;

    private VoiceEvent(Parcel parcel) {
        this.f398a = new Bundle();
        Bundle readBundle = parcel.readBundle();
        if (readBundle != null) {
            this.a = readBundle.getInt("KEY_TYPE", 0);
            if (readBundle.containsKey("KEY_KEYWORDS")) {
                ArrayList stringArrayList = readBundle.getStringArrayList("KEY_KEYWORDS");
                if (stringArrayList != null && stringArrayList.size() > 0) {
                    this.b = (String) stringArrayList.get(0);
                }
            } else {
                this.b = readBundle.getString("KEY_KEYWORD");
            }
            this.f399a = readBundle.getString("KEY_ORIGNAL");
            Bundle bundle = readBundle.getBundle("KEY_EXTRAS");
            if (bundle != null) {
                this.f398a.putAll(bundle);
            }
        }
        Log.d("VoiceEvent", "VoiceEvent() " + toString());
    }

    VoiceEvent(int i, String str) {
        this.f398a = new Bundle();
        this.a = i;
        this.b = str;
    }

    public int getType() {
        return this.a;
    }

    public String getKeyword() {
        return this.b;
    }

    public String getOrignal() {
        return this.f399a;
    }

    public void setOrignal(String str) {
        this.f399a = str;
    }

    public void putExtras(Bundle bundle) {
        this.f398a.putAll(bundle);
    }

    public Bundle getExtras() {
        return new Bundle(this.f398a);
    }

    public String toString() {
        return "VoiceEvent(mType=" + this.a + ", mKeyword=" + this.b + ", orignal=" + this.f399a + ", mBundle=" + this.f398a + ")";
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putInt("KEY_TYPE", this.a);
        bundle.putString("KEY_KEYWORD", this.b);
        if (!TextUtils.isEmpty(this.f399a)) {
            bundle.putString("KEY_ORIGNAL", this.f399a);
        }
        if (!this.f398a.isEmpty()) {
            bundle.putBundle("KEY_EXTRAS", this.f398a);
        }
        parcel.writeBundle(bundle);
    }
}
