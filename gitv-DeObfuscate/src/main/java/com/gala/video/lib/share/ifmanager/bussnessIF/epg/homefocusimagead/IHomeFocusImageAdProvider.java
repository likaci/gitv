package com.gala.video.lib.share.ifmanager.bussnessIF.epg.homefocusimagead;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeFocusImageAdModel;
import java.util.List;

public interface IHomeFocusImageAdProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHomeFocusImageAdProvider {
        public Object getInterface() {
            return this;
        }

        public static IHomeFocusImageAdProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IHomeFocusImageAdProvider)) {
                return null;
            }
            return (IHomeFocusImageAdProvider) wrapper;
        }
    }

    List<HomeFocusImageAdModel> getFocusAdModelList();
}
