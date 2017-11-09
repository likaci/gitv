package com.gala.video.lib.share.ifmanager.bussnessIF.skin;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface ISkinManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements ISkinManager {
        public Object getInterface() {
            return this;
        }

        public static ISkinManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof ISkinManager)) {
                return null;
            }
            return (ISkinManager) wrapper;
        }
    }

    IThemeProvider getIThemeProvider();

    IThemeZipHelper getIThemeZipHelper();
}
