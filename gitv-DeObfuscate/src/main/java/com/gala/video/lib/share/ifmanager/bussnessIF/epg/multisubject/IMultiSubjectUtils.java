package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;

public interface IMultiSubjectUtils extends IInterfaceWrapper {

    public static abstract class Wrapper implements IMultiSubjectUtils {
        public Object getInterface() {
            return this;
        }

        public static IMultiSubjectUtils asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IMultiSubjectUtils)) {
                return null;
            }
            return (IMultiSubjectUtils) wrapper;
        }
    }

    void OnItemClick(Context context, DataSource dataSource, IMultiSubjectInfoModel iMultiSubjectInfoModel);

    void resetSubscribeFocusIndex();
}
