package com.gala.video.lib.share.ifmanager.bussnessIF.web;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IJSConfigDataProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IJSConfigDataProvider {
        public Object getInterface() {
            return this;
        }

        public static IJSConfigDataProvider asInterface(Object wrapper) {
            TraceEx.beginSection("IJSConfigDataProvider.asInterface");
            if (wrapper == null || !(wrapper instanceof IJSConfigDataProvider)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IJSConfigDataProvider) wrapper;
        }
    }

    IJSConfigResult getJSConfigResult();

    int getMemoryLevel();

    void loadData();

    void setMemoryLevel(int i);
}
