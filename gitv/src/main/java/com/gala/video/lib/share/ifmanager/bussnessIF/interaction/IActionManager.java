package com.gala.video.lib.share.ifmanager.bussnessIF.interaction;

import android.content.Context;
import android.content.Intent;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IActionManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IActionManager {
        public Object getInterface() {
            return this;
        }

        public static IActionManager asInterface(Object wrapper) {
            TraceEx.beginSection("IGalaAccountManager.asInterface");
            if (wrapper == null || !(wrapper instanceof IActionManager)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IActionManager) wrapper;
        }
    }

    void onActivityCreate(String str);

    void onActivityDestory(String str);

    void onActivityPause(String str);

    void onActivityResume(String str);

    void onActivityStart(String str);

    void onActivityStop(String str);

    void startActivity(Context context, Intent intent);

    void startActivity(Context context, Intent intent, int i);

    void startActivity(Context context, Intent intent, int i, IActivityStateCallback iActivityStateCallback);

    void startActivity(Context context, Intent intent, IActivityStateCallback iActivityStateCallback);

    void startActivity(Context context, String str);

    void startActivity(Context context, String str, int i);

    void startActivity(Context context, String str, int i, IActivityStateCallback iActivityStateCallback);

    void startActivity(Context context, String str, IActivityStateCallback iActivityStateCallback);
}
