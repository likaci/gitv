package com.gala.video.lib.share.ifmanager.bussnessIF.epg;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.uikit.action.data.CommonAdData;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;

public interface IActionJump extends IInterfaceWrapper {

    public static abstract class Wrapper implements IActionJump {
        public Object getInterface() {
            return this;
        }

        public static IActionJump asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IActionJump)) {
                return null;
            }
            return (IActionJump) wrapper;
        }
    }

    void onItemClick(Context context, BaseActionModel baseActionModel);

    void onItemClickAD(Context context, CommonAdData commonAdData);
}
