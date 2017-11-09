package com.gala.video.app.epg.ui.search;

import com.gala.video.lib.share.utils.TagKeyUtil;

public interface ISearchConstant {
    public static final String CHANNEL_ID = "channel_id";
    public static final String CHANNEL_NAME = "channel_name";
    public static final char[] EXPAND_RELATION_DEFAULT = new char[7];
    public static final String IS_FROM_OPENAPI = "from_openapi";
    public static final int KEYBOARD_CLEAR = 0;
    public static final int KEYBOARD_EXPAND = 2;
    public static final int KEYBOARD_FULL = 0;
    public static final int KEYBOARD_OPER_TAG_KEY = TagKeyUtil.generateTagKey();
    public static final int KEYBOARD_REMOVE = 1;
    public static final int KEYBOARD_T9 = 1;
    public static final int KEYBOARD_TYPE_TAG_KEY = TagKeyUtil.generateTagKey();
    public static final int SEARCH_HISTORY = 1;
    public static final int SEARCH_HISTORY_COUNT_MAX = 6;
    public static final int SEARCH_HOTWORDS = 0;
    public static final int SEARCH_HOT_COUNT_FILTER = 6;
    public static final int SEARCH_HOT_COUNT_MAX = 10;
    public static final int SEARCH_SUGGEST = 2;
    public static final int SEARCH_TYPE_TAG_KEY = TagKeyUtil.generateTagKey();
    public static final int SEARCH_VIP = 3;
    public static final String SUGGEST_TYPE_PERSON = "person";
    public static final int TAGKEY_SUGGEST_QPID = TagKeyUtil.generateTagKey();
    public static final int TAGKEY_SUGGEST_TYPE = TagKeyUtil.generateTagKey();
    public static final String TVSRCHSOURCE = "tvsrchsource";
    public static final String TVSRCHSOURCE_MSG = "消息";
    public static final String TVSRCHSOURCE_OTHER = "other";
    public static final String TVSRCHSOURCE_TAB = "tab";
}
