package com.gala.video.lib.share.ifmanager.bussnessIF.epg.upgrade;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IUpdateManager extends IInterfaceWrapper {

    public interface UpdateOperation {
        void cancelUpdate();

        void exitApp();
    }

    public static abstract class Wrapper implements IUpdateManager {
        public Object getInterface() {
            return this;
        }

        public static IUpdateManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IUpdateManager)) {
                return null;
            }
            return (IUpdateManager) wrapper;
        }
    }

    boolean isNeedShowNewIcon();

    boolean isShowingDialog();

    void setLimitNotifyCount(boolean z);

    void showDialogAndStartDownload(Context context, boolean z, UpdateOperation updateOperation);
}
