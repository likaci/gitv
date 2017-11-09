package com.gala.video.lib.share.ifmanager.bussnessIF.openplay;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IOpenapiReporterManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IOpenapiReporterManager {
        public Object getInterface() {
            return this;
        }

        public static IOpenapiReporterManager asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IOpenapiReporterManager)) {
                return null;
            }
            return (IOpenapiReporterManager) wrapper;
        }
    }

    void onAddFavRecord(Album album);

    void onAddPlayRecord(Album album);

    void onDeleteAllFavRecord();

    void onDeleteAllPlayRecord();

    void onDeleteSingleFavRecord(Album album);

    void onDeleteSinglePlayRecord(Album album);
}
