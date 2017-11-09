package com.gala.video.lib.share.ifmanager.bussnessIF.imsg;

public class MsgDialogParams {
    public IMsgContent[] contents;
    public String imgUrl;
    public int position;
    public String showName;
    public int style;

    public MsgDialogParams(int position, String imgUrl, String showName, int style, IMsgContent[] contents) {
        this.position = position;
        this.imgUrl = imgUrl;
        this.showName = showName;
        this.style = style;
        this.contents = contents;
    }
}
