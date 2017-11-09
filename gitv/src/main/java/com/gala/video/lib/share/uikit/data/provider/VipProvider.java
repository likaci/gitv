package com.gala.video.lib.share.uikit.data.provider;

import com.gala.tvapi.vrs.model.ChannelLabel;
import java.util.ArrayList;
import java.util.List;

public class VipProvider {
    private static final VipProvider sProvider = new VipProvider();
    private List<ChannelLabel> mData = new ArrayList(10);

    private VipProvider() {
    }

    public static VipProvider getInstance() {
        return sProvider;
    }

    public synchronized void add(ChannelLabel label) {
        this.mData.add(label);
    }

    public synchronized List<ChannelLabel> getList() {
        return this.mData;
    }

    public synchronized void clear() {
        this.mData.clear();
    }

    public synchronized void setList(List<ChannelLabel> list) {
        this.mData = list;
    }
}
