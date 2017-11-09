package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;
import java.util.ArrayList;

public class Playlist extends Media implements Serializable {
    public static final Creator<Playlist> CREATOR = new C19861();
    private static final String KEY_DETAIL = "media.detail";
    private static final long serialVersionUID = 1;
    private ArrayList<Media> mPlaylistDetail;

    static class C19861 implements Creator<Playlist> {
        C19861() {
        }

        public final Playlist createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(Playlist.class.getClassLoader());
            bundle.setClassLoader(Playlist.class.getClassLoader());
            Playlist playlist = new Playlist();
            playlist.readBundle(bundle);
            return playlist;
        }

        public final Playlist[] newArray(int size) {
            return new Playlist[size];
        }
    }

    public Playlist() {
        super(3);
    }

    public int describeContents() {
        return 0;
    }

    protected void readBundle(Bundle bundle) {
        this.mPlaylistDetail = bundle.getParcelableArrayList(KEY_DETAIL);
        super.readBundle(bundle);
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putParcelableArrayList(KEY_DETAIL, this.mPlaylistDetail);
        super.writeBundle(bundle);
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }

    public ArrayList<Media> getPlaylistDetail() {
        return this.mPlaylistDetail;
    }

    public void setPlaylistDetail(ArrayList<Media> mPlaylistDetail) {
        this.mPlaylistDetail = mPlaylistDetail;
    }
}
