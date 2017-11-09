package com.gala.video.lib.share.ifmanager.bussnessIF.background;

import android.graphics.drawable.Drawable;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IBackgroundManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IBackgroundManager {
        public Object getInterface() {
            return this;
        }

        public static IBackgroundManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IBackgroundManager)) {
                return null;
            }
            return (IBackgroundManager) wrapper;
        }
    }

    Drawable getBackgroundDrawable();

    void setBackgroundDrawable(String str);
}
