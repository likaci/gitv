package com.gala.video.lib.framework.core.storage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class ExternalStorageWatcher {
    private static ExternalStorageWatcher gInstance;
    private ExternalStorageWatcherCallback mCallback = new C16051();
    private Context mContext;
    private BroadcastReceiver mReceiver = new C16062();

    public interface ExternalStorageWatcherCallback {
        void onSdcardConnected();

        void onSdcardDisconnected();

        void onUsbConnected();

        void onUsbDisconnected();
    }

    class C16051 implements ExternalStorageWatcherCallback {
        C16051() {
        }

        public void onSdcardConnected() {
        }

        public void onUsbConnected() {
        }

        public void onSdcardDisconnected() {
        }

        public void onUsbDisconnected() {
        }
    }

    class C16062 extends BroadcastReceiver {
        C16062() {
        }

        public void onReceive(Context context, Intent intent) {
            String path = intent.getData().toString().substring("file://".length());
            String action = intent.getAction();
            boolean issdcard = ExternalStorageWatcher.this.isSdcard(path);
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                if (issdcard) {
                    ExternalStorageWatcher.this.mCallback.onSdcardConnected();
                } else {
                    ExternalStorageWatcher.this.mCallback.onUsbConnected();
                }
            } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                if (issdcard) {
                    ExternalStorageWatcher.this.mCallback.onSdcardDisconnected();
                } else {
                    ExternalStorageWatcher.this.mCallback.onUsbDisconnected();
                }
            } else if ("android.intent.action.MEDIA_REMOVED".equals(action)) {
                if (issdcard) {
                    ExternalStorageWatcher.this.mCallback.onSdcardDisconnected();
                } else {
                    ExternalStorageWatcher.this.mCallback.onUsbDisconnected();
                }
            } else if (!"android.intent.action.MEDIA_BAD_REMOVAL".equals(action)) {
            } else {
                if (issdcard) {
                    ExternalStorageWatcher.this.mCallback.onSdcardDisconnected();
                } else {
                    ExternalStorageWatcher.this.mCallback.onUsbDisconnected();
                }
            }
        }
    }

    public static synchronized void setup(Context context, ExternalStorageWatcherCallback callback) {
        synchronized (ExternalStorageWatcher.class) {
            if (gInstance == null) {
                gInstance = new ExternalStorageWatcher(context, callback);
            }
        }
    }

    private ExternalStorageWatcher(Context context, ExternalStorageWatcherCallback callback) {
        this.mContext = context.getApplicationContext();
        if (callback != null) {
            this.mCallback = callback;
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_REMOVED");
        intentFilter.addAction("android.intent.action.MEDIA_BAD_REMOVAL");
        intentFilter.addDataScheme("file");
        this.mContext.registerReceiver(this.mReceiver, intentFilter);
    }

    private boolean isSdcard(String path) {
        if (path.contains("sdcard")) {
            return true;
        }
        return false;
    }

    public static void release() {
        if (gInstance != null) {
            gInstance.mContext.unregisterReceiver(gInstance.mReceiver);
            gInstance.mContext = null;
            gInstance.mCallback = null;
            gInstance = null;
        }
    }
}
