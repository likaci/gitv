package com.gala.video.lib.share.ifmanager.bussnessIF.dynamic;

import com.gala.tvapi.tv2.model.DeviceCheck;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IDynamicQDataProvider extends IInterfaceWrapper {

    public interface ILoadDynamicDataCallback {
        void onSuccess();
    }

    public static abstract class Wrapper implements IDynamicQDataProvider {
        public Object getInterface() {
            return this;
        }

        public static IDynamicQDataProvider asInterface(Object wrapper) {
            TraceEx.beginSection("IDynamicQDataProvider.asInterface");
            if (wrapper == null || !(wrapper instanceof IDynamicQDataProvider)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IDynamicQDataProvider) wrapper;
        }
    }

    void checkImageURLUpdate(int i);

    IDynamicResult getDynamicQDataModel();

    boolean isLogResident();

    boolean isSupportVip();

    void loadDynamicQData(DeviceCheck deviceCheck, ILoadDynamicDataCallback iLoadDynamicDataCallback);
}
