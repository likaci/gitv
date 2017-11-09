package com.gala.video.lib.share.common.configs;

import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;

public class ServerConfig {
    public static final int[] INIT_CHANNEL_IDS = new int[]{29, 2, 6, 1, 7, 4, 26, 16, 24, 18, 10, 60, 5, 17, 15};
    public static final int[] LOOP_CHANNEL_IDS = new int[]{23, 27};
    public static final int[] RECOMMEND_CHANNEL_IDS = new int[]{22, 23, 24};
    public static final int[] TILE_INDEX_CHANNEL_ID = new int[]{31, 33, 34, 35, 36, 37, 38, 39};
    public static final String TU_SERVER = "http://data.itv.ptqy.gitv.tv/";
    private static final boolean TVAPI_DEBUG_ENABLED = true;
    private static final boolean USE_MASTER = true;
    private static final boolean USE_TEST_SERVER = false;

    public static String getChannelTabUrlForHome(String url, boolean isBigImage) {
        return PicSizeUtils.getUrlWithSize(isBigImage ? PhotoSize._470_230 : PhotoSize._230_230, url);
    }

    public static String getOtherTabUrlForHome(String imageUrl) {
        return getChannelTabUrlForHome(imageUrl, false);
    }

    public static boolean isUseTestServer() {
        return false;
    }

    public static boolean isUseMaster() {
        return true;
    }

    public static boolean isTVApiDebugEnabled() {
        return true;
    }
}
