package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import android.content.Context;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.model.type.Action;
import com.gala.multiscreen.dmr.util.ContextProfile;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnFlingEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnKeyChanged;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnNotifyEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener.OnPushVideoEvent;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomOperate;
import java.util.List;

class GalaMSWrapper extends GalaMSWrapperBase implements IGalaMSExpand, IMSGalaCustomOperate {
    private static final String TAG = "TvMultiScreen";

    public void onKeyEvent(KeyKind kind) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onKeyEvent(" + kind + ")");
        }
        new MsSendKeyUtils().sendSysKeyWithApp(ContextProfile.getContext(), kind);
    }

    public void onSeekEvent(KeyKind keyKind) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onSeekEvent() keyKind=" + keyKind + ", isMSEnable=" + isMSEnable());
        }
        if (isMSEnable()) {
            this.mMSListener.onSeekEvent(keyKind);
        }
    }

    public void onFlingEvent(KeyKind keyKind) {
        String packageName;
        Context context = ContextProfile.getContext();
        String buildCfgPkgName = BuildCache.getInstance().getString(BuildConstance.APK_PACKAGE_NAME, AppEnvConstant.DEF_PKG_NAME);
        if (StringUtils.isEmpty(buildCfgPkgName.trim())) {
            packageName = AppEnvConstant.DEF_PKG_NAME;
        } else {
            packageName = buildCfgPkgName;
        }
        if (DeviceUtils.isAppForeground(context, packageName)) {
            if (isMSEnable()) {
                this.mMSListener.onFlingEvent(keyKind);
            }
        } else if (this.mOnFlingList.isEmpty()) {
            new MsSendKeyUtils().sendSysKey(context, keyKind);
        } else {
            for (OnFlingEvent listener : this.mOnFlingList) {
                listener.onEvent(keyKind);
            }
        }
    }

    public void onPushVideoEvent(PushVideo pushVideo) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onPushVideoEvent receive phone key is '" + pushVideo.key + "'" + ", openForOversea is '" + pushVideo.open_for_oversea + "'");
        }
        if (isMSEnable()) {
            this.mMSListener.onPushVideoEvent(pushVideo);
        }
        for (OnPushVideoEvent listener : this.mOnPushVideoList) {
            listener.onEvent(pushVideo);
        }
    }

    public Notify onPhoneSync() {
        Notify notify = null;
        if (this.mMSListener != null) {
            notify = this.mMSListener.onPhoneSync();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onPhoneSync() return=" + (notify == null ? null : notify.toString()));
        }
        return notify;
    }

    public boolean onActionChanged(Action action) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onActionChanged() event=" + action.toString());
        }
        boolean ret = false;
        if (isMSEnable()) {
            ret = this.mMSListener.onActionChanged(action);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onActionChanged() return=" + ret);
        }
        return ret;
    }

    public boolean onKeyChanged(int keycode) {
        boolean ret = false;
        if (isMSEnable()) {
            ret = this.mMSListener.onKeyChanged(keycode);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onKeyChanged() return=" + ret);
        }
        for (OnKeyChanged listener : this.mOnKeyChangedList) {
            listener.onEvent(keycode);
        }
        return ret;
    }

    public long getPlayPosition() {
        long ret = 0;
        if (isMSEnable()) {
            ret = this.mMSListener.getPlayPosition();
        }
        if (LogUtils.mIsDebug) {
            MSLog.log("getPlayPosition() return=" + ret, LogType.MS_TO_PHONE);
        }
        return ret;
    }

    public boolean onResolutionChanged(String newResolution) {
        boolean ret = false;
        if (isMSEnable()) {
            ret = this.mMSListener.onResolutionChanged(newResolution);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onResolutionChanged() return=" + ret);
        }
        return ret;
    }

    public boolean onSeekChanged(long newPosition) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onSeekChanged() newPosition=" + newPosition);
        }
        boolean ret = false;
        if (isMSEnable()) {
            ret = this.mMSListener.onSeekChanged(newPosition);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onSeekChanged() return=" + ret);
        }
        return ret;
    }

    public void onPushPlayList(List<Video> playlist) {
        this.mPlayList = playlist;
        LogUtils.m1568d(TAG, "onPushPlayList = " + this.mPlayList);
        doPushoPlayList();
    }

    private boolean doPushoPlayList() {
        boolean ret = false;
        LogUtils.m1568d(TAG, "doPushoPlayList() isAdStart = " + this.isAdStart + ",mPlayList =" + this.mPlayList);
        if (this.mPlayList != null && this.isAdStart) {
            if (isMSEnable()) {
                ret = this.mMSListener.onPushPlayList(this.mPlayList);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "onPushPlayList() return=" + ret);
            }
            this.mPlayList = null;
            this.isAdStart = false;
        }
        return ret;
    }

    public void onAdStart() {
        LogUtils.m1568d(TAG, "onAdStart()");
        this.isAdStart = true;
        doPushoPlayList();
    }

    public void onNotifyEvent(RequestKind requestKind, String message) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onNotifyEvent(" + requestKind + ", " + message + ")" + " mMSListener=" + this.mMSListener);
        }
        for (OnNotifyEvent listener : this.mOnNotifyList) {
            listener.onEvent(requestKind, message);
        }
        if (isMSEnable()) {
            this.mMSListener.onNotifyEvent(requestKind, message);
        }
    }
}
