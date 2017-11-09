package com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting;

import android.content.Context;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface INetworkProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements INetworkProvider {
        public Object getInterface() {
            return this;
        }

        public static INetworkProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof INetworkProvider)) {
                return null;
            }
            return (INetworkProvider) wrapper;
        }
    }

    String getNetworkAction();

    GlobalDialog makeDialogAsNetworkError(Context context, String str);
}
