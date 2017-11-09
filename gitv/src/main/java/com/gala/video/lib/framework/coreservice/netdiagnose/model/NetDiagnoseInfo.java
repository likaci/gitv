package com.gala.video.lib.framework.coreservice.netdiagnose.model;

import com.gala.video.lib.framework.coreservice.netdiagnose.utils.JsonUtils;
import java.io.Serializable;
import java.text.DecimalFormat;

public class NetDiagnoseInfo implements Serializable {
    private static final long serialVersionUID = -7969712674970573254L;
    private String AVGSPEED;
    private int mAvgSpeed;
    private int mBid;
    private StringBuilder mCdnDiagnoseJsonResult;
    private String mCollectionResult;
    private String mDnsResult;
    private int mNetConnectionState;
    private String mNslookupResult;
    private String mPingResult;
    private String mRever;
    private int mStartTime;
    private String mThirdSpeedTestResult;
    private String mTracerouteResult;
    private String mUserCookie;
    private String mUserId;

    public NetDiagnoseInfo(String userCookie, String userId, int startTime, int bid, String rever) {
        this(userCookie, userId, startTime);
        this.mBid = bid;
        this.mRever = rever;
    }

    public NetDiagnoseInfo(String userCookie, String userId, int startTime) {
        this.mCdnDiagnoseJsonResult = new StringBuilder();
        this.AVGSPEED = "\n\ngala Avgspeed is ";
        this.mUserCookie = userCookie;
        this.mUserId = userId;
        this.mStartTime = startTime;
    }

    public String getNslookupResult() {
        return this.mNslookupResult;
    }

    public void setNslookupResult(String nslookupResult) {
        this.mNslookupResult = nslookupResult;
    }

    public String getDnsResult() {
        return this.mDnsResult;
    }

    public void setDnsResult(String mDnsResult) {
        this.mDnsResult = mDnsResult;
    }

    public String getThirdSpeedTestResult() {
        return this.mThirdSpeedTestResult;
    }

    public void setThirdSpeedTestResult(String thirdSpeedTestResult) {
        this.mThirdSpeedTestResult = thirdSpeedTestResult;
    }

    public String getTracerouteResult() {
        return this.mTracerouteResult;
    }

    public void setTracerouteResult(String tracerouteResult) {
        this.mTracerouteResult = tracerouteResult;
    }

    public String getCollectionResult() {
        return this.mCollectionResult;
    }

    public void setCollectionResult(String mCollectResult) {
        this.mCollectionResult = mCollectResult;
    }

    public void setNetConnDiagnoseResult(int state) {
        this.mNetConnectionState = state;
    }

    public int getNetConnDiagnoseResult() {
        return this.mNetConnectionState;
    }

    public void setCdnDiagnoseResult(String jsonResult, int avgSpeed) {
        this.mCdnDiagnoseJsonResult = this.mCdnDiagnoseJsonResult.append(this.AVGSPEED).append(getSpeedDisplay(avgSpeed)).append("\n").append(JsonUtils.formatJson(jsonResult));
        this.mAvgSpeed = avgSpeed;
    }

    private String getSpeedDisplay(int kb) {
        if (kb <= 1024) {
            return kb + "Kb/s ";
        }
        return new DecimalFormat("0.0").format((double) (((float) kb) / 1024.0f)) + "Mb/s ";
    }

    public String getCdnDiagnoseJsonResult() {
        return this.mCdnDiagnoseJsonResult.toString();
    }

    public int getCdnDiagnoseAvgSpeed() {
        return this.mAvgSpeed;
    }

    public String getUserCookie() {
        return this.mUserCookie;
    }

    public String getUserId() {
        return this.mUserId;
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    public int getBid() {
        return this.mBid;
    }

    public String getRever() {
        return this.mRever;
    }

    public String getPingResult() {
        return this.mPingResult;
    }

    public void setPingResult(String pingResult) {
        this.mPingResult = pingResult;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("NetDiagnoseInfo@").append(hashCode()).append("{");
        builder.append(", cookie=").append(this.mUserCookie);
        builder.append(", uid=").append(this.mUserId);
        builder.append(", startTime=").append(this.mStartTime);
        builder.append(", bid=").append(this.mBid);
        builder.append(",rever=").append(this.mRever);
        builder.append(", netConnState=").append(this.mNetConnectionState);
        builder.append(", cdnResult=").append(this.mCdnDiagnoseJsonResult);
        builder.append("}");
        return builder.toString();
    }
}
