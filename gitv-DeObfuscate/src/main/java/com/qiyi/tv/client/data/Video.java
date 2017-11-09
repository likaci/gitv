package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class Video extends Media implements Serializable {
    public static final Creator<Video> CREATOR = new C19881();
    private static final String KEY_ALBUM_ID = "video.album_id";
    private static final String KEY_CHANNEL_ID = "video.channel_id";
    private static final String KEY_COMMENT = "video.comment";
    private static final String KEY_FOCUS = "video.focus";
    private static final String KEY_HISTORY_ADD_TIME = "video.history_add_time";
    private static final String KEY_IS_SERIES = "video.is_series";
    private static final String KEY_PLAY_ORDER = "video.play_order";
    private static final String KEY_PLAY_TIME = "video.play_time";
    private static final String KEY_START_TIME = "video.start_time";
    private static final String KEY_TIME = "video.time";
    private static final String KEY_TOTAL_TIME = "video.play_total_time";
    private static final long serialVersionUID = 1;
    private String mAlbumId = "";
    private int mChannelId = 0;
    private String mComment = "";
    private String mFocus = "";
    private String mHistoryAddTime = "";
    private boolean mIsSeries = false;
    private int mPlayOrder = -1;
    private int mPlayTime = -1;
    private int mStartTime = 0;
    private String mTime = "";
    private long mTotalTime = 0;

    static class C19881 implements Creator<Video> {
        C19881() {
        }

        public final Video createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(Video.class.getClassLoader());
            bundle.setClassLoader(Video.class.getClassLoader());
            Video video = new Video();
            video.readBundle(bundle);
            return video;
        }

        public final Video[] newArray(int size) {
            return new Video[size];
        }
    }

    public Video() {
        super(1);
    }

    public int getChannelId() {
        return this.mChannelId;
    }

    public String getAlbumId() {
        return this.mAlbumId;
    }

    public boolean isSeries() {
        return this.mIsSeries;
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    public void setStartTime(int seconds) {
        this.mStartTime = seconds;
    }

    public int getPlayOrder() {
        return this.mPlayOrder;
    }

    public String getComment() {
        return this.mComment;
    }

    public void setComment(String comment) {
        this.mComment = comment;
    }

    public int describeContents() {
        return 0;
    }

    protected void readBundle(Bundle bundle) {
        super.readBundle(bundle);
        this.mChannelId = bundle.getInt(KEY_CHANNEL_ID);
        this.mAlbumId = bundle.getString(KEY_ALBUM_ID);
        this.mComment = bundle.getString(KEY_COMMENT);
        this.mIsSeries = bundle.getBoolean(KEY_IS_SERIES);
        this.mStartTime = bundle.getInt(KEY_START_TIME);
        this.mPlayOrder = bundle.getInt(KEY_PLAY_ORDER);
        this.mPlayTime = bundle.getInt(KEY_PLAY_TIME);
        this.mTotalTime = bundle.getLong(KEY_TOTAL_TIME);
        this.mFocus = bundle.getString(KEY_FOCUS);
        this.mHistoryAddTime = bundle.getString(KEY_HISTORY_ADD_TIME);
        this.mTime = bundle.getString(KEY_TIME);
    }

    protected void writeBundle(Bundle bundle) {
        super.writeBundle(bundle);
        bundle.putInt(KEY_CHANNEL_ID, this.mChannelId);
        bundle.putString(KEY_ALBUM_ID, this.mAlbumId);
        bundle.putString(KEY_COMMENT, this.mComment);
        bundle.putBoolean(KEY_IS_SERIES, this.mIsSeries);
        bundle.putInt(KEY_START_TIME, this.mStartTime);
        bundle.putInt(KEY_PLAY_ORDER, this.mPlayOrder);
        bundle.putInt(KEY_PLAY_TIME, this.mPlayTime);
        bundle.putLong(KEY_TOTAL_TIME, this.mTotalTime);
        bundle.putString(KEY_FOCUS, this.mFocus);
        bundle.putString(KEY_HISTORY_ADD_TIME, this.mHistoryAddTime);
        bundle.putString(KEY_TIME, this.mTime);
    }

    public void writeToParcel(Parcel dest, int i) {
        Parcelable bundle = new Bundle();
        writeBundle(bundle);
        dest.writeParcelable(bundle, 0);
    }

    public void setChannelId(int channelId) {
        this.mChannelId = channelId;
    }

    public void setAlbumId(String albumId) {
        this.mAlbumId = albumId;
    }

    public void setIsSeries(boolean isSeries) {
        this.mIsSeries = isSeries;
    }

    public void setPlayOrder(int playOrder) {
        this.mPlayOrder = playOrder;
    }

    public int getPlayTime() {
        return this.mPlayTime;
    }

    public void setPlayTime(int mPlayTime) {
        this.mPlayTime = mPlayTime;
    }

    public long getTotalTime() {
        return this.mTotalTime;
    }

    public void setTotalTime(long mTotalTime) {
        this.mTotalTime = mTotalTime;
    }

    public String getFocus() {
        return this.mFocus;
    }

    public void setFocus(String mFocus) {
        this.mFocus = mFocus;
    }

    public String getHistoryAddTime() {
        return this.mHistoryAddTime;
    }

    public void setHistoryAddTime(String historyAddTime) {
        this.mHistoryAddTime = historyAddTime;
    }

    public String getTime() {
        return this.mTime;
    }

    public void setTime(String time) {
        this.mTime = time;
    }
}
