package com.gala.video.app.epg.home.ads;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants.AdClickType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.project.Project;
import com.mcto.ads.constants.ClickThroughType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BannerAdProcessingUtils {
    private static final String TAG = "ads/AdDataProcessingUtils";

    public static void parseClickInfo(ClickThroughType type, String url, BannerImageAdModel model) {
        AdClickType to = AdClickType.NONE;
        if (model != null) {
            model.setItemType(ItemDataType.BANNER_IMAGE_AD);
            switch (type) {
                case DEFAULT:
                case WEBVIEW:
                    model.setAdClickType(AdClickType.H5);
                    model.setClickThroughInfo(url);
                    return;
                case IMAGE:
                    model.setAdClickType(AdClickType.IMAGE);
                    model.setClickThroughInfo(url);
                    GetInterfaceTools.getIAdProcessingUtils().downloadImage(url);
                    return;
                case VIDEO:
                    Matcher plidMatcher = Pattern.compile("//plid=(.*)").matcher(url);
                    boolean isFindPlid = plidMatcher.find();
                    Matcher videoMatcher = Pattern.compile("//albumId=(.*)&tvId=(.*)").matcher(url);
                    boolean isFindVideo = videoMatcher.find();
                    LogUtils.d(TAG, "group count :" + videoMatcher.groupCount());
                    if (isFindPlid) {
                        model.setPlId(plidMatcher.group(1));
                        model.setAdClickType(AdClickType.PLAY_LIST);
                        return;
                    } else if (isFindVideo) {
                        String albumId = videoMatcher.group(1);
                        String tvId = videoMatcher.group(2);
                        model.setAlbumId(albumId);
                        model.setTvId(tvId);
                        model.setAdClickType(AdClickType.VIDEO);
                        return;
                    } else {
                        LogUtils.w(TAG, "clickThroughType is " + ClickThroughType.VIDEO + "(play a video in the APP)" + "but the info of jumping is illegal, " + "info : " + url);
                        model.setAdClickType(AdClickType.VIDEO_ILLEGAL);
                        return;
                    }
                case CAROUSEL_STATION:
                    model.setAdClickType(AdClickType.CAROUSEL);
                    if (Project.getInstance().getControl().isOpenCarousel()) {
                        Matcher carouselMatcher = Pattern.compile("//carouselId=(.*)&carouselNo=(.*)&carouselName=(.*)").matcher(url);
                        if (carouselMatcher.find()) {
                            String carouselId = carouselMatcher.group(1);
                            String carouselNo = carouselMatcher.group(2);
                            String carouselName = carouselMatcher.group(3);
                            model.setCarouselId(carouselId);
                            model.setCarouselNo(carouselNo);
                            model.setCarouselName(carouselName);
                            model.setAdClickType(AdClickType.CAROUSEL);
                            return;
                        }
                        LogUtils.w(TAG, "clickThroughType is " + ClickThroughType.CAROUSEL_STATION + " but the info of jumping is illegal, info : " + url);
                        model.setAdClickType(AdClickType.CAROUSEL_ILLEGAL);
                        return;
                    }
                    LogUtils.d(TAG, "Dynamic interface , not support carousel, filter carousel ad");
                    return;
                default:
                    LogUtils.d(TAG, "unsupported click type");
                    return;
            }
        }
    }
}
