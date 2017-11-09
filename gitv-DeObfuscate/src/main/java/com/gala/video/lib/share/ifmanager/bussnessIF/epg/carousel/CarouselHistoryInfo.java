package com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel;

import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;

public class CarouselHistoryInfo implements Serializable {
    private static final long ONE_DAY_TIME = 86400000;
    private static final long ONE_MINUTE_TIME = 60000;
    private static final String TAG = "CarouselHistoryInfo";
    private static final long serialVersionUID = 1;
    private float mActiveFactor = 0.0f;
    private long mCarouselChannelEndTime = 0;
    private String mCarouselChannelId = "";
    private String mCarouselChannelName = "";
    private String mCarouselChannelNo = "";
    private long mCarouselChannelStartTime = 0;
    private int mEffectiveFactor = 0;
    private long mPlayAllTime = 0;
    private long mPreference = 0;
    private float mUpdateTime = 0.0f;

    public CarouselHistoryInfo(String channelId, String channelNo, String channelName) {
        this.mCarouselChannelName = channelName;
        this.mCarouselChannelNo = channelNo;
        this.mCarouselChannelId = channelId;
    }

    public String getCarouselChannelName() {
        return this.mCarouselChannelName;
    }

    public void setCarouselChannelName(String carouselChannelName) {
        this.mCarouselChannelName = carouselChannelName;
    }

    public String getCarouselChannelNo() {
        return this.mCarouselChannelNo;
    }

    public void setCarouselChannelNo(String carouselChannelNo) {
        this.mCarouselChannelNo = carouselChannelNo;
    }

    public String getCarouselChannelId() {
        return this.mCarouselChannelId;
    }

    public void setCarouselChannelId(String carouselChannelId) {
        this.mCarouselChannelId = carouselChannelId;
    }

    public long getCarouselChannelStartTime() {
        return this.mCarouselChannelStartTime;
    }

    public void setStartTime(long startTime) {
        if (this.mCarouselChannelEndTime > 0) {
            this.mUpdateTime = (float) ((startTime - this.mCarouselChannelEndTime) / 86400000);
        } else {
            this.mUpdateTime = 0.0f;
        }
        this.mCarouselChannelStartTime = startTime;
    }

    public void setEndTime(long endTime) {
        this.mCarouselChannelEndTime = endTime;
        Log.d(TAG, "mPlayAllTime before = " + this.mPlayAllTime);
        Log.d(TAG, "mCarouselChannelStartTime = " + this.mCarouselChannelStartTime);
        Log.d(TAG, "mCarouselChannelEndTime = " + this.mCarouselChannelEndTime);
        Log.d(TAG, "current play time ms = " + (this.mCarouselChannelEndTime - this.mCarouselChannelStartTime));
        Log.d(TAG, "current play time min = " + (((double) ((this.mCarouselChannelEndTime - this.mCarouselChannelStartTime) / ONE_MINUTE_TIME)) * 1.0d));
        this.mPlayAllTime += (this.mCarouselChannelEndTime - this.mCarouselChannelStartTime) / ONE_MINUTE_TIME;
        Log.d(TAG, "mPlayAllTime after = " + this.mPlayAllTime);
    }

    public long getPlayAllTime() {
        return this.mPlayAllTime;
    }

    public long getCarouselChannelEndTime() {
        return this.mCarouselChannelEndTime;
    }

    public long getPreference() {
        this.mEffectiveFactor = this.mPlayAllTime > 10 ? 1 : 0;
        this.mActiveFactor = (float) (1.0d / (Math.pow(2.718281828459045d, (double) ((2.0f * this.mUpdateTime) - 7.0f)) + 1.0d));
        this.mPreference = (long) (((float) (this.mPlayAllTime * ((long) this.mEffectiveFactor))) * this.mActiveFactor);
        return this.mPreference;
    }

    public String toString() {
        return "CarouselHistoryInfo [mCarouselChannelName = " + this.mCarouselChannelName + ", mCarouselChannelNo = " + this.mCarouselChannelNo + ", mCarouselChannelId = " + this.mCarouselChannelId + ", mCarouselChannelStartTime = " + this.mCarouselChannelStartTime + ", mCarouselChannelEndTime = " + this.mCarouselChannelEndTime + ", mEffectiveFactor = " + this.mEffectiveFactor + ", mActiveFactor = " + this.mActiveFactor + ", mPlayAllTime = " + this.mPlayAllTime + ", mPreference = " + this.mPreference + ", mUpdateTime = " + this.mUpdateTime + AlbumEnterFactory.SIGN_STR;
    }
}
