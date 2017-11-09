package com.gala.android.dlna.sdk.stddmrcontroller.enums;

public enum SERVICE {
    AVTransport("urn:schemas-upnp-org:service:AVTransport:1"),
    RenderingControl("urn:schemas-upnp-org:service:RenderingControl:1");
    
    private String mTag;

    private SERVICE(String tag) {
        this.mTag = tag;
    }

    public String getTag() {
        return this.mTag;
    }
}
