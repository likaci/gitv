package com.gala.video.lib.share.common.configs;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.MemoryLevelInfo;

public class HomeDataConfig {
    public static final int CARD_MAX_LENGTH = 1830;
    public static final int CARD_SPACE_LENGTH = 24;
    public static final int CARD_SPACE_LENGTH_CIRCLE = 68;
    public static final String DAY_THEME_APK_NAME = "day_theme.zip";
    public static final String HOME_APP_OPERATOR_LIST_DIR = "home/home_cache/app_operator.dem";
    public static final String HOME_CAROUSEL_CHANNEL_LIST_DIR = "home/home_cache/carousel_channel_list.dem";
    public static final String HOME_CHANNEL_LIST_DIR = "home/home_cache/channel_list.dem";
    public static final String HOME_DAILY_NEWS_DIR = "home/home_cache/daily_news.dem";
    public static final String HOME_DATA_CACHE = "home/home_cache/";
    public static final String HOME_DAY_THEME_CACHE = "home/home_cache/day_theme_channel_icons.dem";
    public static final String HOME_EXIT_OPERATE_DIR = "home/home_cache/exit_operate.dem";
    public static final String HOME_IP_LOCAL_DIR = "home/home_cache/ip_local.dem";
    public static final String HOME_MENU_DIR = "home/home_cache/home_menu.dem";
    public static final String HOME_MENU_NAME_DIR = "home/home_cache/home_menu_name.dem";
    public static final String HOME_PAGE_LIST_DIR = "home/home_cache/home_page_list";
    public static final String HOME_PROMOTION_APP = "home/home_cache/promotion_app.dem";
    public static final String HOME_RECOMMEND_LIST_QUIT_DIR = "home/home_cache/recommend_list_quit_apk.dem";
    public static final String HOME_REFRESH_PERIODISM_FILENAME = "home/v6.0/home_refresh_periodism.dem";
    public static final String HOME_SCREEN_SAVER_DIR = "home/home_cache/screen_saver.dem";
    public static final String HOME_START_OPERATE_DIR = "home/home_cache/start_operate.dem";
    public static final String HOME_TAB_HIDE_INFO_DIR = "home/home_cache/home_tab_hide_info.dem";
    public static final String HOME_TAB_INFO_DIR = "home/home_cache/home_tab_info.dem";
    public static boolean LOW_PERFORMANCE_DEVICE = false;
    public static final int MAX_CARD_NUMBER = 10;
    public static final int MAX_CARD_NUMBER_LOWEST_MEMORY = 4;
    public static final int MAX_CARD_NUMBER_LOW_MEMORY = 5;
    public static final int MAX_ITEM_NUMBER = 20;
    public static final int MAX_ITEM_NUMBER_LOW_MEMORY = 10;
    public static final int MAX_OPERATE_FROM_RESOURCE_NUMBER = 15;
    public static final long MENU_REQUEST_DELAY = 180000;
    public static final long PLUGIN_UPGRADE_DELAY = 180000;
    public static final long REFRESH_INTELLIGENT_SEARCH_INTERVAL = 86400000;
    public static final long REFRESH_REFRESH_TIME_INTERVAL = 86400000;
    public static final long REFRESH_TAB_INFO_INTERVAL = 86400000;
    public static final int RESOURCE_GROUP_TYPE = 7;
    public static final String TAG = "HomeDataCenter";
    public static final long THEME_REQUEST_DELAY = 120000;
    public static final int TV_TAG_ALL_NAME_LENGTH = 3;
    public static boolean sIsBuildUIComplete = false;
    public static boolean sIsDebug = false;
    public static boolean sIsFlatBuffer = false;

    public static final class BuildUIFlag {
        public static final int INVALID = 0;
        public static final int UNKNOWN = -1;
        public static final int VALID = 1;
    }

    public static final class CardType {
        public static final String CARD_APP = "-1";
        public static final String CARD_BANNER_IMAGE_AD = "-3";
        public static final String CARD_BASE_250 = "307";
        public static final String CARD_BASE_360 = "306";
        public static final String CARD_BASE_410 = "305";
        public static final String CARD_CAROUSEL = "311";
        public static final String CARD_CAROUSEL_TAG = "315";
        public static final String CARD_CHANNEL_LIST = "314";
        public static final String CARD_HORIZONTAL = "309";
        public static final String CARD_PRISMATIC = "312";
        public static final String CARD_RECOMMEND_APP = "316";
        public static final String CARD_ROUND_230 = "302";
        public static final String CARD_SETTING = "-2";
        public static final String CARD_SQUARE = "313";
        public static final String CARD_TO_BE_ONLINE = "310";
        public static final String CARD_TWO_ROW_360 = "304";
        public static final String CARD_TWO_ROW_410 = "303";
        public static final String CARD_VERTICAL = "308";
        public static final String COVER_FLOW_410 = "301";
    }

    public static final class ChCardId {
        public static final int CAROUSEL_CHANNEL = 3;
        public static final int CAROUSEL_HISTORY = 4;
        public static final int CHANNEL = 2;
        public static final int NONE = 1;
    }

    public static final class ImageSize {
        public static final String IMAGE_SIZE_230_230 = "_230_230";
        public static final String IMAGE_SIZE_260_360 = "_260_360";
        public static final String IMAGE_SIZE_300_300 = "_300_300";
        public static final String IMAGE_SIZE_354_490 = "_354_490";
        public static final String IMAGE_SIZE_480_270 = "_480_270";
        public static final String IMAGE_SIZE_480_360 = "_480_360";
    }

    public static final class ItemSize {
        public static final int ITEM_118 = 118;
        public static final int ITEM_120 = 120;
        public static final int ITEM_200 = 200;
        public static final int ITEM_204 = 204;
        public static final int ITEM_226 = 226;
        public static final int ITEM_230 = 230;
        public static final int ITEM_250 = 250;
        public static final int ITEM_260 = 260;
        public static final int ITEM_270 = 270;
        public static final int ITEM_285 = 285;
        public static final int ITEM_300 = 300;
        public static final int ITEM_302 = 302;
        public static final int ITEM_347 = 347;
        public static final int ITEM_354 = 354;
        public static final int ITEM_355 = 355;
        public static final int ITEM_360 = 360;
        public static final int ITEM_402 = 402;
        public static final int ITEM_410 = 410;
        public static final int ITEM_480 = 480;
        public static final int ITEM_490 = 490;
        public static final int ITEM_494 = 494;
        public static final int ITEM_731 = 731;
        public static final int ITEM_80 = 80;
    }

    public static final class Judge {
        public static final int NO = 2;
        public static final int YES = 1;
    }

    public static final class LCardId {
        public static final int DEFAULT = 1;
    }

    public static final class PCardId {
        public static final int NONE = 2;
        public static final int TITLE = 1;
    }

    public enum PageType {
        HOME,
        EPG,
        PLAYER,
        NONE
    }

    public static final class TCardId {
        public static final int NONE = 1;
        public static final int RANK = 3;
        public static final int TIME = 2;
    }

    public static final class TaskAction {
        public static final int PARALELL = 0;
        public static final int SERIAL = 1;
    }

    public static final class TaskLevel {
        public static final int REFRESH_FAST = 1;
        public static final int REFRESH_FIXED = -2;
        public static final int REFRESH_LOW = 3;
        public static final int REFRESH_NEVER = -1;
        public static final int REFRESH_NORMAL = 2;
    }

    public static final class TaskLimitBehavior {
        public static final int FIRST_EXECUTION_LIMIT = 1;
        public static final int NO_LIMIT = 0;
    }

    static {
        boolean z = false;
        if (Runtime.getRuntime().availableProcessors() <= 2 || MemoryLevelInfo.isLowMemoryDevice()) {
            z = true;
        }
        LOW_PERFORMANCE_DEVICE = z;
        LogUtils.m1568d(TAG, "is low performance device ? " + LOW_PERFORMANCE_DEVICE);
    }
}
