package com.gala.tvapi.vrs.model;

import java.util.List;

public class IChannelTable extends Model {
    private static final long serialVersionUID = 1;
    public String id = "";
    public String image = "";
    public List<IChannelItem> items = null;
    public int num = 0;

    public void setItems(List<IChannelItem> list) {
        this.items = list;
    }

    public List<IChannelItem> getItems() {
        return this.items;
    }
}
