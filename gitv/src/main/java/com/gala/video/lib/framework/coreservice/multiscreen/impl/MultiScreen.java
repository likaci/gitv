package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import android.content.Context;
import com.gala.android.dlna.sdk.DeviceName;
import com.gala.multiscreen.dmr.MultiScreenHelper;
import com.gala.multiscreen.dmr.logic.MSIcon;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.DlnaMessage;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.cache.DynamicCache;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomOperate;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSStandardOperate;
import com.gala.video.lib.framework.coreservice.multiscreen.IMultiScreen;
import java.util.List;

public class MultiScreen extends MultiScreenBase implements IMultiScreen {
    private static final String TAG = "TvMultiScreen";
    private static MultiScreen gMultiScreen = null;

    public /* bridge */ /* synthetic */ boolean isPhoneConnected() {
        return super.isPhoneConnected();
    }

    public /* bridge */ /* synthetic */ boolean isPhoneKey() {
        return super.isPhoneKey();
    }

    public /* bridge */ /* synthetic */ void onSeekFinish() {
        super.onSeekFinish();
    }

    public /* bridge */ /* synthetic */ void sendMessage(DlnaMessage dlnaMessage) {
        super.sendMessage(dlnaMessage);
    }

    public /* bridge */ /* synthetic */ void setDeviceName(String str) {
        super.setDeviceName(str);
    }

    public /* bridge */ /* synthetic */ void setDlnaLogEnabled(boolean z) {
        super.setDlnaLogEnabled(z);
    }

    public /* bridge */ /* synthetic */ void setIsPhoneKey(boolean z) {
        super.setIsPhoneKey(z);
    }

    public /* bridge */ /* synthetic */ void setTvVersion(String str) {
        super.setTvVersion(str);
    }

    private MultiScreen() {
        this.mGalaMSWrapper = new GalaMSWrapper();
        this.mStandardMSWrapper = new StandardMSWrapper();
        this.mGalaMSWrapper.registerOnNotifyEvent(this.mOnNotifyListener);
        this.mGalaMSWrapper.registerOnKeyChangedEvent(this.mOnKeyChangedListener);
    }

    public static IMultiScreen get() {
        if (gMultiScreen == null) {
            gMultiScreen = new MultiScreen();
        }
        return gMultiScreen;
    }

    public void start(Context context, String name, String deviceId, String pkgName, List<MSIcon> iconList) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "start(" + isSupportMS() + ")");
        }
        if (isSupportMS()) {
            if (!MultiScreenHelper.getInstance().isStartDlnaServer()) {
                this.mHelper = MultiScreenHelper.getInstance();
                this.mHelper.registerGalaMSCallback(this.mGalaMSWrapper);
                this.mHelper.registerStandardMSCallback(this.mStandardMSWrapper);
                this.mHelper.setName(name);
                this.mHelper.setDeviceId(deviceId);
                this.mHelper.setPackageName(pkgName);
                this.mHelper.setGalaDevice(DeviceName.IGALA_BOX);
                this.mHelper.setDeviceType(MultiScreenHelper.DEVICE_TYPE_ONLY_QIMO);
                this.mHelper.setDlnaLogEnabled(this.mDlnaDebugEnabled);
                this.mHelper.setTvVersionString(this.mTvVersion);
                List<MSIcon> list = iconList;
                if (list != null) {
                    for (MSIcon icon : list) {
                        this.mHelper.addIcon(icon);
                    }
                }
                this.mHelper.startAsync(context);
            } else {
                return;
            }
        }
        MSHttpServer.startServer(context, this.mGalaMSWrapper);
    }

    public void stop() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "stop(" + isSupportMS() + ")");
        }
        if (MultiScreenHelper.getInstance().isStartDlnaServer() && isSupportMS()) {
            if (this.mHelper == null) {
                this.mHelper = MultiScreenHelper.getInstance();
            }
            this.mHelper.unregisterGalaMSCallback();
            this.mHelper.unregisterStandardMSCallback();
            this.mHelper.stop();
        }
    }

    public IMSGalaCustomOperate getGalaCustomOperator() {
        return this.mGalaMSWrapper;
    }

    public void sendSysKey(Context context, KeyKind kind) {
        new MsSendKeyUtils().sendSysKey(context, kind);
    }

    public boolean isSupportMS() {
        boolean isSupportMultiScreen;
        String dynamicValue = DynamicCache.get().getString("mulCtr", "");
        if (dynamicValue == null || "1".equals(dynamicValue)) {
            isSupportMultiScreen = true;
        } else {
            isSupportMultiScreen = false;
        }
        LogUtils.d(TAG, "is support multiScreen in dynamic ? " + isSupportMultiScreen);
        boolean buildValue = getBoolean(BuildCache.getInstance().getString(BuildConstance.APK_SUPPORT_MULTISCREEN, "true"));
        LogUtils.d(TAG, "is support multiScreen in appcfg ? " + buildValue);
        if (isSupportMultiScreen && buildValue) {
            return true;
        }
        return false;
    }

    private boolean getBoolean(String name) {
        return "true".equalsIgnoreCase(name);
    }

    public IMSStandardOperate getStandardOperator() {
        return this.mStandardMSWrapper;
    }
}
