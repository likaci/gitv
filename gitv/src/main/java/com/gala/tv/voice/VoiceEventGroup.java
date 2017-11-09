package com.gala.tv.voice;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.gala.tv.voice.core.Log;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class VoiceEventGroup implements Parcelable {
    public static final Creator<VoiceEventGroup> CREATOR = new Creator<VoiceEventGroup>() {
        public final VoiceEventGroup createFromParcel(Parcel parcel) {
            return new VoiceEventGroup(parcel);
        }

        public final VoiceEventGroup[] newArray(int i) {
            return new VoiceEventGroup[i];
        }
    };
    private int a;
    private Bundle f407a;
    private String f408a;
    private final ArrayList<VoiceEvent> f409a;

    private VoiceEventGroup(Parcel parcel) {
        this.f409a = new ArrayList();
        this.f407a = parcel.readBundle(VoiceEvent.class.getClassLoader());
        if (this.f407a != null) {
            this.f408a = this.f407a.getString("KEY_GROUP_ID", "");
            this.a = this.f407a.getInt("KEY_GROUP_PRIORITY");
            Collection parcelableArrayList = this.f407a.getParcelableArrayList("KEY_GROUP_EVENTS");
            if (parcelableArrayList != null) {
                this.f409a.addAll(parcelableArrayList);
            }
        }
        Log.d("VoiceGroup", "VoiceGroup() " + toString());
    }

    public VoiceEventGroup(List<VoiceEvent> list) {
        this.f409a = new ArrayList();
        if (list != null) {
            this.f409a.addAll(list);
        }
    }

    public boolean isEmpty() {
        return this.f409a.isEmpty();
    }

    public String getGroupId() {
        return this.f408a;
    }

    public int getPriority() {
        return this.a;
    }

    public List<VoiceEvent> getEvents() {
        return this.f409a;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(VoiceEvent.class.getClassLoader());
        bundle.putString("KEY_GROUP_ID", this.f408a);
        bundle.putInt("KEY_GROUP_PRIORITY", this.a);
        bundle.putParcelableArrayList("KEY_GROUP_EVENTS", this.f409a);
        parcel.writeBundle(bundle);
    }
}
