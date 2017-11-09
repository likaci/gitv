package com.gala.video.app.epg.ui.albumlist.enums;

public class IFootEnum {
    public static final String FAVOURITE_STR = "我的收藏";
    public static final String PLAY_HISTORY_ALL_STR = "我的记录";
    public static final String PLAY_HISTORY_LONG_STR = "长视频记录";
    public static final String SUBSCRIBE_STR = "我的预约";

    public enum FootLeftRefreshPage {
        NONE(-1),
        PLAY_HISTORY_ALL(0),
        PLAY_HISTORY_LONG(1),
        SUBSCRIBE(2),
        FAVOURITE(3);
        
        private int value;

        private FootLeftRefreshPage(int value) {
            this.value = 0;
            this.value = value;
        }

        public String valueName() {
            switch (this.value) {
                case 0:
                    return IFootEnum.PLAY_HISTORY_ALL_STR;
                case 1:
                    return IFootEnum.PLAY_HISTORY_LONG_STR;
                case 2:
                    return IFootEnum.SUBSCRIBE_STR;
                case 3:
                    return IFootEnum.FAVOURITE_STR;
                default:
                    return null;
            }
        }
    }

    public static FootLeftRefreshPage valueOf(String name) {
        if (PLAY_HISTORY_ALL_STR.equals(name)) {
            return FootLeftRefreshPage.PLAY_HISTORY_ALL;
        }
        if (PLAY_HISTORY_LONG_STR.equals(name)) {
            return FootLeftRefreshPage.PLAY_HISTORY_LONG;
        }
        if (SUBSCRIBE_STR.equals(name)) {
            return FootLeftRefreshPage.SUBSCRIBE;
        }
        if (FAVOURITE_STR.equals(name)) {
            return FootLeftRefreshPage.FAVOURITE;
        }
        return null;
    }
}
