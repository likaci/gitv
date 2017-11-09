package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig.Source;
import com.push.pushservice.constants.DataConst;

public enum ItemDataType {
    NONE("none"),
    SEARCH("search"),
    RECORD("record"),
    SEARCH_RECORD("searchAndRecord"),
    DAILY("dailyInformation"),
    APP(Source.APPLICATION),
    SETTING(Source.CONFIGURATION),
    CHANNEL(Source.CHANNEL_LIST),
    CAROUSEL("carousel"),
    TV_TAG(SourceTool.TVTAG),
    TV_TAG_ALL("tvtag_all"),
    H5("H5"),
    LIVE("live"),
    PLAY_LIST("play_list"),
    PERSON(ISearchConstant.SUGGEST_TYPE_PERSON),
    ALBUM("album"),
    VIDEO("video"),
    LIVE_CHANNEL("live_channel"),
    FOCUS_IMAGE_AD("focus_image_ad"),
    LOGIN("login"),
    VIP_ATTRIBUTE("vip_attribute"),
    MODE_SWITCH("mode_switch"),
    BACKGROUND(SettingConstants.BACKGROUND),
    PLAY_PROMPT("play_prompt"),
    NETWORK("network"),
    FEEDBACK(InterfaceKey.EPG_FB),
    COMMON_SETTING("common_setting"),
    TAB_MANAGE("tabSetting"),
    HELP_CENTER("help_center"),
    MULTI_SCREEN("multi_screen"),
    SYSTEM_UPGRADE("system_upgrade"),
    ABOUT_DEVICE("about_device"),
    CONCERN_WEIXIN("concern_weixin"),
    RESOURCE_GROUP("RESOURCEGROUP"),
    PLST_GROUP("subject"),
    VIP_BUY("vip_buy"),
    VIP_VIDEO("vip_video"),
    SUBSCRIBE("subscribe"),
    COLLECTION(SourceTool.PLAYLIST_TYPE),
    RECOMMEND("recommend"),
    STAR("star"),
    SUPER_ALBUM(Source.SUPER_ALBUM),
    JUMP_TO_H5("jump_to_h5"),
    BANNER_IMAGE_AD("banner_image_ad"),
    RECOMMEND_APP("thirdpartyapp"),
    SUBSCRIBE_BTN("subscribe_btn"),
    TRAILERS(Source.TRAILERS),
    MSGCENTER(DataConst.EXTRA_PUSH_MESSAGE),
    ENTER_ALL("enterall"),
    UCENTER_RECORD_ALL("ucenter_record_all");
    
    private String value;

    private ItemDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    public static ItemDataType getItemTypeByValue(String value) {
        for (ItemDataType itemDataType : values()) {
            if (itemDataType.getValue().equals(value)) {
                return itemDataType;
            }
        }
        return NONE;
    }
}
