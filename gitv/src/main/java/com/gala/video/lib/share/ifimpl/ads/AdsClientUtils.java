package com.gala.video.lib.share.ifimpl.ads;

import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.AdsClient;

public class AdsClientUtils {

    private static class SingletonHelper {
        private static AdsClient instance = new AdsClient(TVApiBase.getTVApiProperty().getPassportDeviceId(), Project.getInstance().getBuild().getVersionString(), DeviceUtils.getMd5FormatMacAddr(), Project.getInstance().getBuild().getVrsUUID());

        private SingletonHelper() {
        }
    }

    public static AdsClient getInstance() {
        return SingletonHelper.instance;
    }
}
