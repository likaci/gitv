package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnFlingEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnKeyChanged;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnNotifyEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnPushVideoEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GalaMSWrapperBase {
    protected boolean isAdStart = false;
    protected IMSGalaCustomListener mMSListener;
    protected boolean mMsEnable = false;
    protected Set<OnFlingEvent> mOnFlingList = new HashSet();
    protected Set<OnKeyChanged> mOnKeyChangedList = new HashSet();
    protected Set<OnNotifyEvent> mOnNotifyList = new HashSet();
    protected Set<OnPushVideoEvent> mOnPushVideoList = new HashSet();
    protected List<Video> mPlayList = null;

    GalaMSWrapperBase() {
    }

    public void setMSListener(IMSGalaCustomListener callback) {
        if (LogUtils.mIsDebug) {
            MSLog.log("setMSListener(" + callback + ")", LogType.PARAMETER);
        }
        this.mMSListener = callback;
    }

    public void setMSEnable(boolean enable) {
        if (!enable) {
            this.isAdStart = false;
            this.mPlayList = null;
        }
        this.mMsEnable = enable;
    }

    public void registerOnFlingEvent(OnFlingEvent listener) {
        this.mOnFlingList.add(listener);
    }

    public void unRegisterOnFlingEvent(OnFlingEvent listener) {
        this.mOnFlingList.remove(listener);
    }

    public void registerOnNotifyEvent(OnNotifyEvent listener) {
        this.mOnNotifyList.add(listener);
    }

    public void unRegisterOnNotifyEvent(OnNotifyEvent listener) {
        this.mOnNotifyList.remove(listener);
    }

    public void registerOnPushVideoEvent(OnPushVideoEvent listener) {
        this.mOnPushVideoList.add(listener);
    }

    public void unRegisterOnPushVideoEvent(OnPushVideoEvent listener) {
        this.mOnPushVideoList.remove(listener);
    }

    public void registerOnKeyChangedEvent(OnKeyChanged listener) {
        this.mOnKeyChangedList.add(listener);
    }

    public void unRegisterOnKeyChangedEvent(OnKeyChanged listener) {
        this.mOnKeyChangedList.remove(listener);
    }

    protected boolean isMSEnable() {
        if (LogUtils.mIsDebug) {
            MSLog.log("isMSEnable(" + this.mMSListener + ", msEnable=" + this.mMsEnable + ")", LogType.PARAMETER);
        }
        return this.mMSListener != null && this.mMsEnable;
    }
}
