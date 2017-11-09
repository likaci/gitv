package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import android.app.Instrumentation;
import android.content.Context;
import com.gala.multiscreen.dmr.MultiScreenHelper;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.video.lib.framework.core.cache.BuildCache;
import com.gala.video.lib.framework.core.env.AppEnvConstant;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;

class MsSendKeyUtils {
    private static final String TAG = "MsSendKeyUtils";
    private static final Instrumentation mInstrumentation = new Instrumentation();

    MsSendKeyUtils() {
    }

    public boolean sendSysKey(Context context, KeyKind kind) {
        boolean handled;
        MultiScreenHelper msHelper = MultiScreenHelper.getInstance();
        if (msHelper.isKeySysEnable()) {
            LogUtils.m1568d(TAG, "sendSysKey(" + context + ", " + kind + ") send system key!");
            msHelper.sendKeyCode(kind);
            handled = true;
        } else {
            LogUtils.m1568d(TAG, "sendSysKey(" + context + ", " + kind + ") send app key!");
            handled = sendSysKeyWithApp(context, kind);
        }
        LogUtils.m1568d(TAG, "sendSysKey(" + kind + ") return " + handled);
        return handled;
    }

    public boolean sendSysKeyWithApp(Context context, KeyKind kind) {
        LogUtils.m1568d(TAG, "sendSysKeyWithApp(" + context + ", " + kind + ")");
        boolean handled = false;
        if (kind == KeyKind.LEFT) {
            handled = sendSimulateKeyCode(21);
        } else if (kind == KeyKind.UP) {
            handled = sendSimulateKeyCode(19);
        } else if (kind == KeyKind.RIGHT) {
            handled = sendSimulateKeyCode(22);
        } else if (kind == KeyKind.DOWN) {
            handled = sendSimulateKeyCode(20);
        } else if (kind == KeyKind.CLICK) {
            handled = sendSimulateKeyCode(23);
        } else if (kind == KeyKind.BACK) {
            handled = sendSimulateKeyCode(4);
        } else if (kind == KeyKind.MENU) {
            handled = sendSimulateKeyCode(82);
        } else if (kind == KeyKind.HOME) {
            handled = sendSimulateKeyCode(3);
        } else if (kind == KeyKind.VOLUME_UP) {
            handled = sendSimulateKeyCode(24);
        } else if (kind == KeyKind.VOLUME_DOWN) {
            handled = sendSimulateKeyCode(25);
        }
        LogUtils.m1568d(TAG, "sendSysKeyWithApp(" + kind + ") return " + handled);
        return handled;
    }

    private boolean sendSimulateKeyCode(final int keyCode) {
        String packageName;
        boolean handled = false;
        String buildCfgPkgName = BuildCache.getInstance().getString(BuildConstance.APK_PACKAGE_NAME, AppEnvConstant.DEF_PKG_NAME);
        if (StringUtils.isEmpty(buildCfgPkgName.trim())) {
            packageName = AppEnvConstant.DEF_PKG_NAME;
        } else {
            packageName = buildCfgPkgName;
        }
        if (DeviceUtils.isAppForeground(AppRuntimeEnv.get().getApplicationContext(), packageName)) {
            LogUtils.m1568d(TAG, "sendSimulateKeyCode(" + keyCode + ") forgound!");
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    try {
                        LogUtils.m1568d(MsSendKeyUtils.TAG, "sendSimulateKeyCode(" + keyCode + ") excute!");
                        MsSendKeyUtils.mInstrumentation.sendKeyDownUpSync(keyCode);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
            handled = true;
        } else {
            LogUtils.m1568d(TAG, "sendSimulateKeyCode(" + keyCode + ") not forgound!");
        }
        LogUtils.m1568d(TAG, "sendSimulateKeyCode(" + keyCode + ") return " + handled);
        return handled;
    }
}
