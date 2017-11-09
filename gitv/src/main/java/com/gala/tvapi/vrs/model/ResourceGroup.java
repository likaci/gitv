package com.gala.tvapi.vrs.model;

import java.util.List;

public class ResourceGroup extends Model {
    private static final long serialVersionUID = 1;
    public int channelId;
    public GroupKvs groupKvs;
    public String id;
    public List<ChannelLabel> items;
}
