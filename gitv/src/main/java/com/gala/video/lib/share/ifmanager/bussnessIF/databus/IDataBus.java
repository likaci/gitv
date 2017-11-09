package com.gala.video.lib.share.ifmanager.bussnessIF.databus;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface IDataBus extends IInterfaceWrapper {
    public static final String APP_DOWNLOAD_COMPLETE = "app_download_complete";
    public static final String BUILD_FIRST_PAGE_FINISHED = "build_first_page_event";
    public static final String CHECK_UPGRADE_EVENT = "check_upgrade_event";
    public static final String DEVICE_CHECK_FISNISHED_EVENT = "device_check_event_finished";
    public static final String DYNAMIC_REQUEST_FINISHED_EVENT = "dynamic_request_finished";
    public static final String FOCUS_IMAGE_AD_DOWNLOAD_COMPLETE = "focus_image_ad_DownLoad_complete";
    public static final String HISTORY_CLOUD_SYNC_COMPLETED = "history_cloud_sync_finished";
    public static final String HISTORY_DB_RELOAD_COMPLETED = "history_db_reload_finished";
    public static final String HISTORY_DELETE = "history_delete";
    public static final String HOME_INIT_FINISH_EVENT = "home_init_finished_event";
    public static final String PLAY_PLUGIN_LOAD_SUCCESS = "play_plugin_load_success";
    public static final String SHOW_PREVIEW_COMPLETED = "show_preview_completed";
    public static final String STARTUP_ERROR_EVENT = "start_up_error_event";
    public static final String STARTUP_UPGRADE_EVENT = "start_up_upgrade_event";
    public static final String UPDADE_ACTION_BAR = "update_action_bar";
    public static final String UPDATE_PROMOTION_APP = "update_promotion_app";

    public interface MyObserver {
        void update(String str);
    }

    public static abstract class Wrapper implements IDataBus {
        public Object getInterface() {
            return this;
        }

        public static IDataBus asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IDataBus)) {
                return null;
            }
            return (IDataBus) wrapper;
        }
    }

    void postEvent(String str);

    void postEvent(String str, int i);

    void postStickyEvent(String str);

    void registerStickySubscriber(String str, MyObserver myObserver);

    void registerSubscriber(String str, MyObserver myObserver);

    void unRegisterSubscriber(String str, MyObserver myObserver);
}
