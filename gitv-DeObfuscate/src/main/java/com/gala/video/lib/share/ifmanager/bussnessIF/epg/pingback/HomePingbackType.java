package com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback;

public class HomePingbackType {

    public enum ClickPingback {
        ACTION_BAR_CLICK_PINGBACK("action_bar_click_pingback"),
        EXIT_APP_CLICK_PINGBACK("exit_app_click_pingback"),
        ITEM_CLICK_PINGBACK("item_click_pingback"),
        MENU_FLOAT_LAYER_CLICK_PINGBACK("menu_float_layer_click_pingback"),
        MENU_FLOAT_LAYER_SETTING_PAGE_CLICK_PINGBACK("menu_float_layer_setting_page_click_pingback"),
        PRESS_BACK_KEY_CLICK_PINGBACK("press_back_key_click_pingback"),
        PRESS_MENU_KEY_PINGBACK("press_menu_key_pingback"),
        SCREEN_SAVER_PAGE_CLICK_PINGBACK("screen_saver_page_click_pingback"),
        SKIN_CHANGED_PINGBACK("skin_changed_pingback"),
        START_AD_PAGE_CLICK_PINGBACK("start_ad_page_click_pingback"),
        START_OPERATE_PAGE_CLICK_PINGBACK("start_operate_page_click_pingback"),
        SCENE_INSIDE_GUIDE_CHECK_IN_IMAGE_CLICK_PINGBACK("scene_inside_guide_check_in_image_click_pingback"),
        TAB_BAR_CLICK_PINGBACK("tab_bar_click_pingback"),
        TAB_MANAGER_ACTIVATED_CLICK_PINGBACK("tab_manager_activated_click_pingback"),
        TAB_MANAGER_ADDED_RESULT_ADD_ACTION_PINGBACK("tab_manager_added_result_add_action_pingback"),
        TAB_MANAGER_CANCEL_ACTIVATED_CLICK_PINGBACK("tab_manager_cancel_activated_click_pingback"),
        TAB_MANAGER_EXIT_DIALOG_CLICK_PINGBACK("tab_manager_exit_dialog_click_pingback"),
        TAB_MANAGER_GUIDE_CLICK_PINGBACK("tab_manager_guide_click_pingback"),
        TAB_MANAGER_ADD_REMOVE_CLICK_PINGBACK("tab_manager_add_remove_click_pingback"),
        CHINA_POKER_DOWNLOAD_WINDOW_CLICK_PINGBACK("china_poker_download_window_click_pingback");
        
        private String value;

        private ClickPingback(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum CommonPingback {
        START_PINGBACK("start_pingback"),
        START_UP_PINGBACK("start_up_pingback"),
        DATA_ERROR_PINGBACK("data_error_pingback"),
        AD_DATA_REQUEST_PINGBACK("start_exit_ad_request_pingback"),
        FOCUS_AD_DATA_REQUEST_PINGBACK("focus_ad_request_pingback"),
        LOAD_FINISHED_PINGBACK("LoadFinishPingback"),
        SKIN_CHANGED_RESULT_PINGBACK("skin_changed_result_pingback"),
        CAROUSEL_VALID_CHANNEL_GENERATE_PINGBACK("carousel_valid_channel_generate_pingback"),
        SCREEN_SAVER_PAGE_SHOW_PINGBACK("screen_saver_page_show_pingback"),
        SCREEN_SAVER_AD_PAGE_CLICK_PINGBACK("screen_saver_ad_page_click_pingback"),
        PLAY_HISTORY_UPLOAD_PINGBACK("play_history_upload_pingback"),
        PLAY_HISTORY_DOWNLOAD_PINGBACK("play_history_download_pingback"),
        BANNER_AD_DATA_REQUEST_PINGBACK("banner_ad_data_request_pingback"),
        CHINA_POKER_LOAD_FINISHED_PINGBACK("china_poker_load_finished_pingback"),
        LOCAL_INSTALLED_APP_INFO_PINGBACK("local_installed_app_info_pingback"),
        PLUGIN_UPGRADE_INFO_PINGBACK("plugin_upgrade_info_pingback"),
        PLUGIN_PRE_LOAD_PINGBACK("Plugin_pre_load_pingback");
        
        private String value;

        private CommonPingback(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }

    public enum ShowPingback {
        MENU_FLOAT_LAYER_SETTING_PAGE_SHOW_PINGBACK("menu_float_layer_setting_page_show_pingback"),
        TAB_MANAGER_EXIT_DIALOG_SHOW_PINGBACK("tab_manager_exit_dialog_show_pingback"),
        TAB_MANAGER_GUIDE_SHOW_PINGBACK("tab_manager_guide_show_pingback"),
        TAB_MANAGER_PAGE_SHOW_PINGBACK("tab_manager_page_show_pingback"),
        HOME_CARD_SHOW_PINGBACK("home_card_show_pingback"),
        PAGE_SHOW_PINGBACK("page_show_pingback"),
        TAB_BAR_SHOW_PINGBACK("tab_bar_show_pingback"),
        START_OPERATE_PAGE_SHOW_PINGBACK("start_operate_page_show_pingback"),
        START_AD_PAGE_SHOW_PINGBACK("start_ad_page_show_pingback"),
        CHINA_POKER_ENTRY_SHOW_PINGBACK("china_poker_entry_show_pingback"),
        EXIT_APP_SHOW_PINGBACK("exit_app_show_pingback"),
        MULTISUJECT_CARD_SHOW_PINGBACK("multisubject_card_show_pingback"),
        MULTISUJECT_PAGE_SHOW_PINGBACK("multisubject_page_show_pingback"),
        SOLOTAB_PAGE_SHOW_PINGBACK("solotab_page_show_pingback"),
        UCENTER_CARD_SHOW_PINGBACK("ucenter_card_show_pingback"),
        DETAIL_CARD_SHOW_PINGBACK("detail_card_show_pingback"),
        SCENE_INSIDE_GUIDE_CHECK_IN_IMAGE_SHOW_PINGBACK("scene_inside_guide_check_in_image_show_pingback"),
        CHINA_POKER_DOWNLOAD_WINDOW_SHOW_PINGBACK("china_poker_download_window_show_pingback");
        
        private String value;

        private ShowPingback(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
