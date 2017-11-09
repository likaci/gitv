package com.gala.video.lib.share.ifmanager.bussnessIF.ads;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.model.CupidAdModel;
import com.gala.video.lib.share.pingback.HomeAdPingbackModel;
import com.gala.video.lib.share.utils.TraceEx;
import com.mcto.ads.CupidAd;

public interface IAdProcessingUtils extends IInterfaceWrapper {

    public static abstract class Wrapper implements IAdProcessingUtils {
        public Object getInterface() {
            return this;
        }

        public static IAdProcessingUtils asInterface(Object wrapper) {
            TraceEx.beginSection("IAdProcessingUtils.asInterface");
            if (wrapper == null || !(wrapper instanceof IAdProcessingUtils)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IAdProcessingUtils) wrapper;
        }
    }

    void downloadImage(String str);

    void onClickAd(Context context, CupidAdModel cupidAdModel, HomeAdPingbackModel homeAdPingbackModel);

    void onClickForNotOpenAdData(Context context);

    void parseAdRawData(CupidAd cupidAd, CupidAdModel cupidAdModel);
}
