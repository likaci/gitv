package com.gala.tvapi.vrs.model;

import com.gala.tvapi.type.IChannelType;

public class IChannelItem extends Model {
    public static final int HORICAONTAL_LAYOUT = 1;
    public static final int VIRTUAL_LAYOUT = 2;
    private static final long serialVersionUID = 1;
    public String channelId = "";
    public String id = "";
    public String image = "";
    public int isTop = 0;
    public String itemImageUrl = "";
    public String plId = "";
    public int style = 0;
    public String title = "";
    public int type = 0;

    public boolean isTop() {
        return this.isTop != 0;
    }

    public IChannelType getIChannelType() {
        return this.type == 1 ? IChannelType.RECOMMEND : IChannelType.PLAYLIST;
    }
}
