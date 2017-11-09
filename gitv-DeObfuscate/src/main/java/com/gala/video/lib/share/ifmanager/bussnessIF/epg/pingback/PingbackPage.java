package com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback;

public enum PingbackPage {
    HomePage("home_page"),
    AlbumDetail("album_detail"),
    MultiSubject("multi_subject"),
    Ucenter("Ucenter"),
    SoloTab("solotab"),
    DetailAll("DetailAll");
    
    private String value;

    private PingbackPage(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
