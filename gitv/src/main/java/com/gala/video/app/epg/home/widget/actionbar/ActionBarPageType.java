package com.gala.video.app.epg.home.widget.actionbar;

public enum ActionBarPageType {
    HOME_PAGE("home_page"),
    EPG_PAGE("epg_list_page"),
    MSG_CENTER_PAGE("message_center_page");
    
    private String value;

    private ActionBarPageType(String value) {
        this.value = "";
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
