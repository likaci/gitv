package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.io.Serializable;

public class Album extends Media implements Serializable {
    public static final Creator<Album> CREATOR = new C19821();
    private static final String KEY_CHANNEL_ID = "album.channel_id";
    private static final String KEY_COMMENT = "album.comment";
    private static final String KEY_FOCUS = "album.focus";
    private static final String KEY_HISTORY_ADD_TIME = "album.history_add_time";
    private static final String KEY_IS_SERIES = "album.is_series";
    private static final String KEY_PLAY_ORDER = "album.play_order";
    private static final String KEY_PLAY_TIME = "album.play_time";
    private static final String KEY_START_TIME = "album.start_time";
    private static final String KEY_TIME = "album.time";
    private static final String KEY_TOTAL_TIME = "album.play_total_time";
    private static final String KEY_TV_COUNT = "album.tv_count";
    private static final String KEY_VIDEO_ID = "album.video_id";
    private static final long serialVersionUID = 1;
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
    private int mTvCount = 0;
    private String mVideoId = "";

    static class C19821 implements Creator<Album> {
        C19821() {
        }

        public final Album createFromParcel(Parcel parcel) {
            Bundle bundle = (Bundle) parcel.readParcelable(Album.class.getClassLoader());
            bundle.setClassLoader(Album.class.getClassLoader());
            Album album = new Album();
            album.readBundle(bundle);
            return album;
        }

        public final Album[] newArray(int size) {
            return new Album[size];
        }
    }

    public Album() {
        super(2);
    }

    public int getChannelId() {
        return this.mChannelId;
    }

    public String getVideoId() {
        return this.mVideoId;
    }

    public int getPlayOrder() {
        return 0;
    }

    public String getComment() {
        return this.mComment;
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

    public int describeContents() {
        return 0;
    }

    protected void readBundle(Bundle bundle) {
        super.readBundle(bundle);
        this.mChannelId = bundle.getInt(KEY_CHANNEL_ID);
        this.mVideoId = bundle.getString(KEY_VIDEO_ID);
        this.mComment = bundle.getString(KEY_COMMENT);
        this.mIsSeries = bundle.getBoolean(KEY_IS_SERIES);
        this.mStartTime = bundle.getInt(KEY_START_TIME);
        this.mPlayOrder = bundle.getInt(KEY_PLAY_ORDER);
        this.mPlayTime = bundle.getInt(KEY_PLAY_TIME);
        this.mTotalTime = bundle.getLong(KEY_TOTAL_TIME);
        this.mFocus = bundle.getString(KEY_FOCUS);
        this.mTvCount = bundle.getInt(KEY_TV_COUNT);
        this.mHistoryAddTime = bundle.getString(KEY_HISTORY_ADD_TIME);
        this.mTime = bundle.getString(KEY_TIME);
    }

    protected void writeBundle(Bundle bundle) {
        super.writeBundle(bundle);
        bundle.putInt(KEY_CHANNEL_ID, this.mChannelId);
        bundle.putString(KEY_VIDEO_ID, this.mVideoId);
        bundle.putString(KEY_COMMENT, this.mComment);
        bundle.putBoolean(KEY_IS_SERIES, this.mIsSeries);
        bundle.putInt(KEY_START_TIME, this.mStartTime);
        bundle.putInt(KEY_PLAY_ORDER, this.mPlayOrder);
        bundle.putInt(KEY_PLAY_TIME, this.mPlayTime);
        bundle.putLong(KEY_TOTAL_TIME, this.mTotalTime);
        bundle.putString(KEY_FOCUS, this.mFocus);
        bundle.putInt(KEY_TV_COUNT, this.mTvCount);
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

    public void setVideoId(String videoId) {
        this.mVideoId = videoId;
    }

    public void setComment(String comment) {
        this.mComment = comment;
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

    public int getTvCount() {
        return this.mTvCount;
    }

    public void setTvCount(int count) {
        this.mTvCount = count;
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
