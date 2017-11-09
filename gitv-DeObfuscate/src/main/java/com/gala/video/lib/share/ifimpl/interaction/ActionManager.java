package com.gala.video.lib.share.ifimpl.interaction;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.util.ArrayMap;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.interaction.IActionManager.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.interaction.IActivityStateCallback;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;

public class ActionManager extends Wrapper {
    private static final String TAG = "ActionManager";
    private ArrayMap<String, IActivityStateCallback> mMap = new ArrayMap();

    private enum ASTATE {
        CREATE,
        START,
        RESUME,
        PAUSE,
        STOP,
        DESTROY
    }

    ActionManager() {
    }

    public void startActivity(Context context, String action) {
        startActivity(context, action, -1, null);
    }

    public void startActivity(Context context, String action, int requestCode) {
        startActivity(context, action, requestCode, null);
    }

    public void startActivity(Context context, String action, IActivityStateCallback cb) {
        startActivity(context, action, -1, cb);
    }

    public void startActivity(Context context, String action, int requestCode, IActivityStateCallback cb) {
        startActivity(context, new Intent().setAction(action), requestCode, cb);
    }

    public void startActivity(Context context, Intent intent) {
        startActivity(context, intent, -1, null);
    }

    public void startActivity(Context context, Intent intent, int requestCode) {
        startActivity(context, intent, requestCode, null);
    }

    public void startActivity(Context context, Intent intent, IActivityStateCallback cb) {
        startActivity(context, intent, -1, cb);
    }

    public void startActivity(Context context, Intent intent, int requestCode, IActivityStateCallback cb) {
        LogUtils.m1568d(TAG, "startActivity context = " + context + ", intent = " + intent + ", requestCode = " + requestCode + ", IActivityStateCallback = " + cb);
        if (context != null) {
            if (!(context instanceof Activity)) {
                intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
            }
            checkIntent(intent);
            checkMap(intent.getAction(), cb);
            checkRequestCode(context, intent, requestCode);
        }
    }

    private void checkIntent(Intent intent) {
        if (intent == null) {
            Throwable th = new Throwable("ActionManager:: Intent is null !!!");
        }
        if (StringUtils.isEmpty(intent.getAction())) {
            ComponentName cn = intent.getComponent();
            if (cn.getShortClassName() != null) {
                String[] array = cn.getShortClassName().split("\\.");
                if (array.length > 0) {
                    intent.setAction(IntentUtils.getActionName(ActionSetTool.getActionByKey(array[array.length - 1])));
                } else {
                    LogUtils.m1571e(TAG, "checkIntent array = " + array);
                }
            }
        } else {
            intent.setAction(IntentUtils.getActionName(intent.getAction()));
        }
        LogUtils.m1568d(TAG, "checkIntent = " + intent.getAction());
    }

    private void checkMap(String action, IActivityStateCallback cb) {
        if (cb != null && !StringUtils.isEmpty((CharSequence) action) && !this.mMap.containsKey(action)) {
            this.mMap.put(action, cb);
            LogUtils.m1568d(TAG, "checkMap = " + this.mMap.size() + ", put cb(" + action + "," + cb + ")");
        }
    }

    private void checkRequestCode(Context context, Intent intent, int requestCode) {
        if (requestCode < 0) {
            try {
                context.startActivity(intent);
                if (context instanceof Activity) {
                    PageIOUtils.activityInTransition((Activity) context);
                }
            } catch (ActivityNotFoundException e) {
                LogUtils.m1575i(TAG, "checkRequestCode", e);
            }
        } else if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
            PageIOUtils.activityInTransition((Activity) context);
        } else {
            Throwable th = new Throwable("ActionManager::startActivityForResult context must be Activity !!!");
        }
    }

    public void onActivityCreate(String action) {
        onActivityState(ASTATE.CREATE, action);
    }

    public void onActivityStart(String action) {
        onActivityState(ASTATE.START, action);
    }

    public void onActivityResume(String action) {
        onActivityState(ASTATE.RESUME, action);
    }

    public void onActivityPause(String action) {
        onActivityState(ASTATE.PAUSE, action);
    }

    public void onActivityStop(String action) {
        onActivityState(ASTATE.STOP, action);
    }

    public void onActivityDestory(String action) {
        onActivityState(ASTATE.DESTROY, action);
    }

    private void onActivityState(ASTATE state, String action) {
        if (!StringUtils.isEmpty((CharSequence) action) && !this.mMap.isEmpty()) {
            IActivityStateCallback stateCallback = (IActivityStateCallback) this.mMap.get(action);
            if (stateCallback != null) {
                LogUtils.m1568d(TAG, "onActivityState = " + state + ", action = " + action);
                switch (state) {
                    case CREATE:
                        stateCallback.onCreate();
                        return;
                    case START:
                        stateCallback.onStart();
                        return;
                    case RESUME:
                        stateCallback.onResume();
                        return;
                    case PAUSE:
                        stateCallback.onPause();
                        return;
                    case STOP:
                        stateCallback.onStop();
                        return;
                    case DESTROY:
                        stateCallback.onDestory();
                        this.mMap.remove(action);
                        return;
                    default:
                        return;
                }
            }
        }
    }
}
