package com.gala.video.app.epg.voice.function;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OpenAppListener extends VoiceListener {
    private static final String TAG = "OpenAppListener";
    private Map<String, Runnable> mMap = new HashMap();
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenAppListener.TAG, "receive intent = " + intent);
            }
            new Thread8K(new Runnable() {
                public void run() {
                    OpenAppListener.this.prepare();
                }
            }).start();
        }
    };

    private class AppRunnable implements Runnable {
        private final String mPackageName;

        AppRunnable(String packageName) {
            this.mPackageName = packageName;
        }

        public void run() {
            try {
                Intent intent = OpenAppListener.this.mContext.getPackageManager().getLaunchIntentForPackage(this.mPackageName);
                intent.setFlags(ClientDefaults.MAX_MSG_SIZE);
                PageIOUtils.activityIn(OpenAppListener.this.mContext, intent);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public OpenAppListener(Context context, int priority) {
        super(context, priority);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PACKAGE_ADDED");
        filter.addAction("android.intent.action.PACKAGE_REMOVED");
        filter.addAction("android.intent.action.PACKAGE_REPLACED");
        filter.addDataScheme("package");
        context.registerReceiver(this.mReceiver, filter);
    }

    public void prepare() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "prepare");
        }
        try {
            Intent main = new Intent("android.intent.action.MAIN");
            main.addCategory("android.intent.category.LAUNCHER");
            List<ResolveInfo> packages = this.mContext.getPackageManager().queryIntentActivities(main, 0);
            synchronized (this.mMap) {
                this.mMap.clear();
                for (int i = 0; i < packages.size(); i++) {
                    ResolveInfo packageInfo = (ResolveInfo) packages.get(i);
                    String appName = packageInfo.activityInfo.loadLabel(this.mContext.getPackageManager()).toString();
                    String packageName = packageInfo.activityInfo.packageName;
                    if (!TextUtils.isEmpty(appName)) {
                        this.mMap.put(appName.toUpperCase(Locale.CHINESE), new AppRunnable(packageName));
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "prepare() appName=" + appName + ", packageName=" + packageName);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected List<AbsVoiceAction> doOpenAction() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "do open action");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            for (final String finalAppName : this.mMap.keySet()) {
                actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(4, finalAppName)) {
                    protected boolean dispatchVoiceEvent(VoiceEvent event) {
                        PingBackUtils.setTabSrc("其他");
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(OpenAppListener.TAG, "OpenAppHelper/dispatchVoiceEvent()");
                        }
                        Runnable runnable = (Runnable) OpenAppListener.this.mMap.get(finalAppName);
                        if (runnable != null) {
                            new Thread8K(runnable, OpenAppListener.TAG).start();
                        }
                        return true;
                    }
                });
            }
        } catch (Exception e) {
            LogUtils.e(TAG, "do open action exception = ", e);
        }
        return actions;
    }
}
