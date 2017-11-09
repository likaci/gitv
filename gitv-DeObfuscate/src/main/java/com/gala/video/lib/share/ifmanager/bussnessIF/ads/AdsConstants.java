package com.gala.video.lib.share.ifmanager.bussnessIF.ads;

import com.gala.sdk.plugin.PluginType;

public class AdsConstants {
    public static final String AD_BANNER_KEY = "url";
    public static final String AD_ID_KEY = "ad_id";
    public static final int AD_IMAGE_FOCUS_IMAGE_LIMIT_SIZE = 10;
    public static final String AD_IMAGE_RENDER = "image";
    public static final int AD_IMAGE_RESOURCE_LIMIT_SIZE = 10;
    public static final String AD_INFO_FILE = "ads_info";
    public static final String AD_MIXER_KEY = "mixer_result";
    public static final String AD_SCREEN_LANDSCAPE_URL = "landScapeUrl";
    public static final String AD_SCREEN_NEED_AD_BADGE_ = "needAdBadge";
    public static final String AD_SCREEN_RENDER_TYPE = "renderType";
    public static final String AD_SCREEN_SAVER_DURATION = "duration";
    public static final String AD_SCREEN_SAVER_IMAGE_URL = "imgUrl";
    public static final String AD_SCREEN_SAVER_NEEDADBADGE = "needAdBadge";
    public static final String AD_SCREEN_SAVER_NEED_QR = "needQR";
    public static final String AD_SCREEN_SAVER_QR_DESC = "qrDescription";
    public static final String AD_SCREEN_SAVER_QR_POS = "qrPosition";
    public static final String AD_SCREEN_SAVER_QR_TITLE = "qrTitle";
    public static final String AD_THROUGH_CAROUSEL = "carousel";
    public static final String AD_THROUGH_H5 = "h5";
    public static final String AD_THROUGH_IMAGE = "image";
    public static final String AD_THROUGH_VIDEO = "video";
    public static final String AD_URL_KEY = "image_url";
    public static final String AD_VIDEO_BADGE = "needAdBadge";
    public static final String AD_VIDEO_DURATION = "duration";
    public static final String AD_VIDEO_DYNAMIC_URL = "dynamicUrl";
    public static final String AD_VIDEO_NEED_QR = "needQR";
    public static final String AD_VIDEO_QRPOSITION = "qrPosition";
    public static final String AD_VIDEO_QRSCALE = "qrHeightScale";
    public static final String AD_VIDEO_QR_DESCRIPTION = "qrDescription ";
    public static final String AD_VIDEO_QR_TITTLE = "qrTitle";
    public static final String AD_VIDEO_RENDER = "video";
    public static final String AD_VIDEO_SKIPPABLE = "isSkippable";
    public static final int BANNER_AD_HEIGHT_DEFAULT = 150;
    public static final int BANNER_AD_WIDTH_DEFAULT = 1830;
    public static final String EXIT_AD_IMAGE_URL = "imgUrl";
    public static final String EXIT_AD_NEED_QR = "needQR";
    public static final String EXIT_AD_QR_DESC = "qrDescription";
    public static final String EXIT_AD_QR_TITLE = "qrTitle";
    public static final String HOME_FOCUS_IMAGE_AD_HEIGHT = "height";
    public static final int HOME_FOCUS_IMAGE_AD_HEIGHT_DEFAULT = 470;
    public static final String HOME_FOCUS_IMAGE_AD_IMAGE_URL = "imgUrl";
    public static final String HOME_FOCUS_IMAGE_AD_NEED_BADGE = "needAdBadge";
    public static final String HOME_FOCUS_IMAGE_AD_TITLE = "title";
    public static final String HOME_FOCUS_IMAGE_AD_WIDTH = "width";
    public static final int HOME_FOCUS_IMAGE_AD_WIDTH_DEFAULT = 950;
    public static final int IMAGE_MAX_HEIGHT = 1080;
    public static final int IMAGE_MAX_WIGTH = 1920;
    public static final String JSON_TEMPLATE_TYPE_VALUE_EXIT = "exit";
    public static final String JSON_TEMPLATE_TYPE_VALUE_INTERSTITIAL = "interstitial";
    public static final int NO_AD_ID = -1;
    public static final String PLAYER_ID = "qc_100001_100145";
    public static final String START_SCREEN_IMAGE_NAME = "start_screen_image.jpg";

    public enum AdClickType {
        NONE("none"),
        PLAY_LIST("play_list"),
        VIDEO("video"),
        H5(AdsConstants.AD_THROUGH_H5),
        IMAGE("image"),
        DEFAULT(PluginType.DEFAULT_TYPE),
        VIDEO_ILLEGAL("video_illegal"),
        CAROUSEL("carousel"),
        CAROUSEL_ILLEGAL("carousel_illegal");
        
        private String value;

        private AdClickType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum AdType {
        START_AD("start_ad"),
        FOCUS_AD("focus_ad"),
        SCREEN_SAVER_AD("screen_saver_ad"),
        EXIT_AD("exit_ad"),
        NONE("none");
        
        private String value;

        private AdType(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
