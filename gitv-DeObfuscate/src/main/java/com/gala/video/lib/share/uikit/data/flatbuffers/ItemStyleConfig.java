package com.gala.video.lib.share.uikit.data.flatbuffers;

import android.util.Log;
import com.gala.cloudui.utils.CuteUtils;
import com.gala.cloudui.utils.CuteUtils.IInitCallback;

public class ItemStyleConfig {
    private static final String TAG = "ItemStyleConfig";
    private static final IInitCallback sInitCallback = new C18031();
    private static final Object sLock = new Object();

    static class C18031 implements IInitCallback {
        C18031() {
        }

        public void onFail() {
            Log.e(ItemStyleConfig.TAG, "sInitCallback,onFail, INIT_OK=false, main thread run parseItemStyle()");
            ItemStyleConfig.parseItemStyle();
        }
    }

    public static void initItemStyle() {
        Log.e(TAG, "initItemStyle,init_ok? =" + CuteUtils.isInitOk());
        CuteUtils.setInitOk(false);
        CuteUtils.setInitCallback(sInitCallback);
    }

    private static void parseItemStyle() {
        synchronized (sLock) {
            if (CuteUtils.isInitOk()) {
                Log.e(TAG, "parseItemStyle,INIT_OK=true, return");
                return;
            }
            long l = System.currentTimeMillis();
            new ItemStyleFlatBufferParser().initItemStyle();
            Log.e(TAG, "bufferParser consume=" + (System.currentTimeMillis() - l));
            CuteUtils.setInitOk(true);
        }
    }
}
