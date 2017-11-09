package com.gala.tvapi.vrs.model;

import java.util.List;

public class PlayListQipu extends Model {
    private static final long serialVersionUID = 1;
    public ChannelTable channel;
    public String imageStyle = "";
    public int pages = 0;
    public List<PlayAlbum> plst;
    public int size = 0;
    public String tvBackgroundUrl = "";
}
