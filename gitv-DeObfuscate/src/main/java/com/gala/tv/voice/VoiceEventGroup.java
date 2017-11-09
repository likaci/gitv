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
    public static final Creator<VoiceEventGroup> CREATOR = new C02001();
    private int f801a;
    private Bundle f802a;
    private String f803a;
    private final ArrayList<VoiceEvent> f804a;

    static class C02001 implements Creator<VoiceEventGroup> {
        C02001() {
        }

        public final VoiceEventGroup createFromParcel(Parcel parcel) {
            return new VoiceEventGroup(parcel);
        }

        public final VoiceEventGroup[] newArray(int i) {
            return new VoiceEventGroup[i];
        }
    }

    private VoiceEventGroup(Parcel parcel) {
        this.f804a = new ArrayList();
        this.f802a = parcel.readBundle(VoiceEvent.class.getClassLoader());
        if (this.f802a != null) {
            this.f803a = this.f802a.getString("KEY_GROUP_ID", "");
            this.f801a = this.f802a.getInt("KEY_GROUP_PRIORITY");
            Collection parcelableArrayList = this.f802a.getParcelableArrayList("KEY_GROUP_EVENTS");
            if (parcelableArrayList != null) {
                this.f804a.addAll(parcelableArrayList);
            }
        }
        Log.m525d("VoiceGroup", "VoiceGroup() " + toString());
    }

    public VoiceEventGroup(List<VoiceEvent> list) {
        this.f804a = new ArrayList();
        if (list != null) {
            this.f804a.addAll(list);
        }
    }

    public boolean isEmpty() {
        return this.f804a.isEmpty();
    }

    public String getGroupId() {
        return this.f803a;
    }

    public int getPriority() {
        return this.f801a;
    }

    public List<VoiceEvent> getEvents() {
        return this.f804a;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.setClassLoader(VoiceEvent.class.getClassLoader());
        bundle.putString("KEY_GROUP_ID", this.f803a);
        bundle.putInt("KEY_GROUP_PRIORITY", this.f801a);
        bundle.putParcelableArrayList("KEY_GROUP_EVENTS", this.f804a);
        parcel.writeBundle(bundle);
    }
}
