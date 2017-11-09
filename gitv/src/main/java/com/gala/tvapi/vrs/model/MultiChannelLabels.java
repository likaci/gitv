package com.gala.tvapi.vrs.model;

public class MultiChannelLabels extends Model {
    private static final long serialVersionUID = 1;
    public ChannelLabels data;

    public void setData(ChannelLabels labels) {
        this.data = labels;
    }

    public ChannelLabels getChannelLabels() {
        return this.data;
    }
}
