package com.gala.video.app.epg.home.data.hdata;

public enum HomeDataType {
    DEVICE_REGISTER("devRegister"),
    APP_CONFIG("dynamicQ"),
    APP_UPDATE("moduleUpdate"),
    TAB_INFO("tabInfo"),
    HOME_DATA("groupDetail"),
    START_ERROR("start_error"),
    CHANNEL("channel"),
    APP_STORE("appStore"),
    APP_OPERATOR("appOperator"),
    DAILY_INFO("dailyInfo"),
    CAROUSEL_CHANNEL("carousel_channel"),
    THEME("theme"),
    HOME_MENU("home_menu"),
    NONE("none");
    
    private String mKey;

    private HomeDataType(String key) {
        this.mKey = key;
    }

    public String getKey() {
        return this.mKey;
    }
}
