package com.gala.video.lib.share.ifmanager.bussnessIF.ads;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.utils.TraceEx;

public interface IAdApi extends IInterfaceWrapper {

    public static abstract class Wrapper implements IAdApi {
        public Object getInterface() {
            return this;
        }

        public static IAdApi asInterface(Object wrapper) {
            TraceEx.beginSection("IAdApi.asInterface");
            if (wrapper == null || !(wrapper instanceof IAdApi)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IAdApi) wrapper;
        }
    }

    String fetchBannerAd(String str, String str2);

    String fetchBannerAd(String str, String str2, String str3, String str4);

    String getAdImageResourceJSON();

    String getExitAppDialogAds(String str);

    String getHomeFocusImageAds(String str);

    String getScreenAd(String str);

    String getScreenSaverAds(String str);

    String getScreenVideoDownLoadUrl(String str);
}
