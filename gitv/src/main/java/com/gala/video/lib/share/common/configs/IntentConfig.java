package com.gala.video.lib.share.common.configs;

import com.gala.video.lib.framework.core.env.AppEnvConstant;

public class IntentConfig {
    public static final String ALBUM_EXCLUSIVE = "album_exclusive";
    public static final String ALBUM_ID = "albumId";
    public static final String ALBUM_NAME = "album_name";
    public static final String ALBUM_VIP = "albumvip";
    public static final String CHANNELNAME = "channelName";
    public static final String CHN_ID = "chnId";
    public static final String CHN_NAME = "chnName";
    public static final String DISABLE_START_PREVIEW = "disable_start_preview";
    public static final String INTENT_PARAM_FROM_WHERE = "fromWhere";
    public static final String IS_MULTISCREEN = "action_is_multiscreen";
    public static final String OPENAPI_HOME_TARGET_PAGE = "home_target_page";
    public static final String PLAY_HISTORY = "history";
    public static final String PUSH_AUTH_COOKIE = "push_auth_cookie";
    public static final String PUSH_AUTH_PLATFORM = "push_auth_platform";
    public static final String PUSH_AUTH_VID = "push_auth_vid";
    public static final String PUSH_OPEN_FOR_OVERSEA = "open_for_oversea";
    public static final String SOURCE = "source";
    public static final String START_TIME = "startTime";
    public static final String URI_AUTH = AppEnvConstant.DEF_PKG_NAME;
    public static final String URL_PATH_PLAY = "/play";
    public static final String VRS_ALBUM_ID = "vrsAlbumId";
    public static final String VRS_CHN_ID = "vrsChnId";
    public static final String VRS_TVID = "vrsTvId";
    public static final String VRS_VID = "vrsVid";

    public interface ActionSuffix {
        public static final String ACION_DETAIL_SUFFIX = ".action.ACTION_DETAIL";
        public static final String ACTION_ALBUMLIST_SUFFIX = ".action.ACTION_ALBUMLIST";
        public static final String ACTION_HOME_SUFFIX = ".action.ACTION_HOME";
        public static final String ACTION_PERSON_CENTER = ".action.ACTION_PERSON_CENTER";
        public static final String ACTION_PLAY_VIDEO_SUFFIX = ".action.ACTION_PLAYVIDEO";
        public static final String ACTION_PURCHASE_SUFFIX = ".action.ACTION_PURCHASE";
        public static final String ACTION_SEARCHRESULT_SUFFIX = ".action.ACTION_SEARCHRESULT";
        public static final String ACTION_SEARCH_SUFFIX = ".action.ACTION_SEARCH";
        public static final String ACTION_SUBJECT_SUFFIX = ".action.ACTION_SUBJECT";
        public static final String ACTION_WEEKEND = ".action.ACTION_WEEKEND";
    }

    public interface BroadcastAction {
        public static final String ACION_DETAIL = "ACTION_DETAIL";
        public static final String ACTION_ALBUMLIST = "ACTION_ALBUMLIST";
        public static final String ACTION_HOME = "ACTION_HOME";
        public static final String ACTION_PERSON_CENTER = "ACTION_PERSON_CENTER";
        public static final String ACTION_PLAY_VIDEO = "ACTION_PLAYVIDEO";
        public static final String ACTION_PURCHASE = "ACTION_PURCHASE";
        public static final String ACTION_SEARCH = "ACTION_SEARCH";
        public static final String ACTION_SEARCHRESULT = "ACTION_SEARCHRESULT";
        public static final String ACTION_SUBJECT = "ACTION_SUBJECT";
        public static final String ACTION_WEEKEND = "ACTION_WEEKEND";
    }
}
