package com.gala.video.lib.share.uikit.loader.data;

import android.util.Log;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.constants.ClickThroughType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannerAdProcessingUtils {
    private static final String TAG = "AdDataProcessingUtils";

    public static void parseClickInfo(ClickThroughType type, String url, BannerAd model) {
        AdClickType to = AdClickType.NONE;
        switch (type) {
            case DEFAULT:
            case WEBVIEW:
                model.adClickType = AdClickType.H5;
                model.clickThroughInfo = url;
                return;
            case IMAGE:
                model.adClickType = AdClickType.IMAGE;
                model.clickThroughInfo = url;
                GetInterfaceTools.getIAdProcessingUtils().downloadImage(url);
                return;
            case VIDEO:
                Matcher plidMatcher = Pattern.compile("//plid=(.*)").matcher(url);
                boolean isFindPlid = plidMatcher.find();
                Matcher videoMatcher = Pattern.compile("//albumId=(.*)&tvId=(.*)").matcher(url);
                boolean isFindVideo = videoMatcher.find();
                LogUtils.m1568d(TAG, "group count :" + videoMatcher.groupCount());
                if (isFindPlid) {
                    model.plId = plidMatcher.group(1);
                    model.adClickType = AdClickType.PLAY_LIST;
                    return;
                } else if (isFindVideo) {
                    String albumId = videoMatcher.group(1);
                    String tvId = videoMatcher.group(2);
                    model.albumId = albumId;
                    model.tvId = tvId;
                    model.adClickType = AdClickType.VIDEO;
                    return;
                } else {
                    Log.w(TAG, "clickThroughType is " + ClickThroughType.VIDEO + "(play a video in the APP)" + "but the info of jumping is illegal, " + "info : " + url);
                    model.adClickType = AdClickType.VIDEO_ILLEGAL;
                    return;
                }
            case CAROUSEL_STATION:
                model.adClickType = AdClickType.CAROUSEL;
                if (Project.getInstance().getControl().isOpenCarousel()) {
                    Matcher carouselMatcher = Pattern.compile("//carouselId=(.*)&carouselNo=(.*)&carouselName=(.*)").matcher(url);
                    if (carouselMatcher.find()) {
                        String carouselId = carouselMatcher.group(1);
                        String carouselNo = carouselMatcher.group(2);
                        String carouselName = carouselMatcher.group(3);
                        model.carouselId = carouselId;
                        model.carouselNo = carouselNo;
                        model.carouselName = carouselName;
                        model.adClickType = AdClickType.CAROUSEL;
                        return;
                    }
                    LogUtils.m1577w(TAG, "clickThroughType is " + ClickThroughType.CAROUSEL_STATION + " but the info of jumping is illegal, info : " + url);
                    model.adClickType = AdClickType.CAROUSEL_ILLEGAL;
                    return;
                }
                LogUtils.m1568d(TAG, "Dynamic interface , not support carousel, filter carousel ad");
                return;
            default:
                LogUtils.m1568d(TAG, "unsupported click type");
                return;
        }
    }
}
