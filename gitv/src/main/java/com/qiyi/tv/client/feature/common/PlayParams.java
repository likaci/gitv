package com.qiyi.tv.client.feature.common;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.gala.video.lib.share.common.configs.IntentConfig2;

public class PlayParams implements Parcelable {
    public static final Creator<PlayParams> CREATOR = new Creator<PlayParams>() {
        public final PlayParams createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(PlayParams.class.getClassLoader());
            bundle.setClassLoader(PlayParams.class.getClassLoader());
            PlayParams playParams = new PlayParams();
            playParams.readBundle(bundle);
            return playParams;
        }

        public final PlayParams[] newArray(int size) {
            return new PlayParams[size];
        }
    };
    private boolean a = true;

    public boolean isContinuePlay() {
        return this.a;
    }

    public void setContinuePlay(boolean isContinuePlay) {
        this.a = isContinuePlay;
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putBoolean(IntentConfig2.FROM_CONTINUE, this.a);
    }

    protected void readBundle(Bundle bundle) {
        this.a = bundle.getBoolean(IntentConfig2.FROM_CONTINUE);
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
