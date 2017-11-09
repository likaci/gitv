package com.gala.video.lib.share.ifmanager.bussnessIF.player;

import android.content.Context;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IPlayerFeatureProxy extends IInterfaceWrapper {
    public static final int LogType_CupId = 2;
    public static final int LogType_Puma = 1;

    public interface OnStateChangedListener {
        void onCanceled();

        void onFailed();

        void onLoading();

        void onSuccess();
    }

    public static abstract class Wrapper implements IPlayerFeatureProxy {
        public Object getInterface() {
            return this;
        }

        public static IPlayerFeatureProxy asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IPlayerFeatureProxy)) {
                return null;
            }
            return (IPlayerFeatureProxy) wrapper;
        }
    }

    void enableHCDNPreDeploy(boolean z);

    String getLog(int i);

    IPlayerFeature getPlayerFeature();

    IPlayerFeature getPlayerFeatureOnlyInitJava();

    String getPlayerModulesVersion();

    void initailize(Context context);

    boolean isPlayerAlready();

    void loadPlayerFeatureAsync(Context context, OnStateChangedListener onStateChangedListener, boolean z);

    void setHCDNCleanAvailable(boolean z);

    void updateAuthorization(String str);

    void updateDeviceCheckInfo(String str, String str2);
}
