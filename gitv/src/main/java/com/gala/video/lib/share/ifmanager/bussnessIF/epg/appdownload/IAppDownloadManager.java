package com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IAppDownloadManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IAppDownloadManager {
        public Object getInterface() {
            return this;
        }

        public static IAppDownloadManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IAppDownloadManager)) {
                return null;
            }
            return (IAppDownloadManager) wrapper;
        }
    }

    boolean isComplete();

    void post();

    void startInstall();
}
