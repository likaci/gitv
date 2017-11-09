package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class Detail implements Parcelable, Serializable {
    public static final Creator<Detail> CREATOR = new C19851();
    private static final String KEY_ALBUM_DESC = "detail.album_desc";
    private static final String KEY_ALBUM_PRODUCER = "detail.albumproducer";
    private static final String KEY_FOCUS = "detail.focus";
    private static final String KEY_ISSUE_TIME = "detail.issue_time";
    private static final String KEY_PLAY_COUNT = "detail.play_count";
    private static final String KEY_SCORE = "detail.score";
    private static final String KEY_START_TIME = "detail.start_time";
    private static final String KEY_TAG = "detail.tag";
    private static final String KEY_TV_COUNT = "detail.tv_count";
    private static final String KEY_TV_SET = "detail.tv_set";
    private static final long serialVersionUID = 1;
    protected String mAlbumDesc;
    protected String mAlbumProducer;
    protected String mFocus;
    protected String mIssueTime;
    protected String mPlayCount;
    protected String mScore;
    protected int mStartTime;
    protected String mTag;
    protected int mTvCount;
    protected int mTvSet;

    static class C19851 implements Creator<Detail> {
        C19851() {
        }

        public final Detail createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(Detail.class.getClassLoader());
            bundle.setClassLoader(Detail.class.getClassLoader());
            Detail detail = new Detail();
            detail.readBundle(bundle);
            return detail;
        }

        public final Detail[] newArray(int size) {
            return new Detail[size];
        }
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    public String getFocus() {
        return this.mFocus;
    }

    public String getScore() {
        return this.mScore;
    }

    public String getPlayCount() {
        return this.mPlayCount;
    }

    public String getIssueTime() {
        return this.mIssueTime;
    }

    public String getTag() {
        return this.mTag;
    }

    public String getAlbumProducer() {
        return this.mAlbumProducer;
    }

    public String getDesc() {
        return this.mAlbumDesc;
    }

    public int getTvSet() {
        return this.mTvSet;
    }

    public int getTvCount() {
        return this.mTvCount;
    }

    public int describeContents() {
        return 0;
    }

    protected void readBundle(Bundle bundle) {
        this.mStartTime = bundle.getInt(KEY_START_TIME);
        this.mFocus = bundle.getString(KEY_FOCUS);
        this.mScore = bundle.getString(KEY_SCORE);
        this.mPlayCount = bundle.getString(KEY_PLAY_COUNT);
        this.mIssueTime = bundle.getString(KEY_ISSUE_TIME);
        this.mTag = bundle.getString(KEY_TAG);
        this.mAlbumProducer = bundle.getString(KEY_ALBUM_PRODUCER);
        this.mAlbumDesc = bundle.getString(KEY_ALBUM_DESC);
        this.mTvSet = bundle.getInt(KEY_TV_SET);
        this.mTvCount = bundle.getInt(KEY_TV_COUNT);
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putInt(KEY_START_TIME, this.mStartTime);
        bundle.putString(KEY_FOCUS, this.mFocus);
        bundle.putString(KEY_SCORE, this.mScore);
        bundle.putString(KEY_PLAY_COUNT, this.mPlayCount);
        bundle.putString(KEY_ISSUE_TIME, this.mIssueTime);
        bundle.putString(KEY_TAG, this.mTag);
        bundle.putString(KEY_ALBUM_PRODUCER, this.mAlbumProducer);
        bundle.putString(KEY_ALBUM_DESC, this.mAlbumDesc);
        bundle.putInt(KEY_TV_SET, this.mTvSet);
        bundle.putInt(KEY_TV_COUNT, this.mTvCount);
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }
}
