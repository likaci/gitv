package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IWebRoleEntry extends IInterfaceWrapper {

    public static abstract class Wrapper implements IWebRoleEntry {
        public Object getInterface() {
            return this;
        }

        public static IWebRoleEntry asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IWebRoleEntry)) {
                return null;
            }
            return (IWebRoleEntry) wrapper;
        }
    }

    void showRoleInAlbum(Context context);

    void showRoleInVip(Context context);
}
