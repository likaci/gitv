package com.gala.tvapi.vrs.model;

import com.gala.tvapi.log.a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.type.ResourceType;
import java.util.ArrayList;
import java.util.List;

public class ChannelLabels extends Model {
    private static final long serialVersionUID = 1;
    public String id = "";
    public List<ChannelLabel> items;
    public String name = "";
    public int viewNumber = 0;

    public void setItems(List<ChannelLabel> list) {
        this.items = list;
    }

    public List<ChannelLabel> getChannelLabelList() {
        List<ChannelLabel> arrayList = new ArrayList();
        if (TVApiBase.getTVApiProperty().isShowLive()) {
            for (ChannelLabel channelLabel : this.items) {
                if (!(channelLabel.getType() == ResourceType.DEFAULT || channelLabel.getType() == ResourceType.CHANNEL)) {
                    if (!channelLabel.getType().equals(ResourceType.LIVE)) {
                        arrayList.add(channelLabel);
                        if (TVApiBase.getTVApiTool().getRecommendAlbum() == null && channelLabel.getType() == ResourceType.VIDEO) {
                            a.a("ChannelLabels", "set video for speed runner" + channelLabel.getVideo().tvQid);
                            TVApiBase.getTVApiTool().setRecommednAlbum(channelLabel.getVideo());
                        }
                    } else if (channelLabel.getLiveAlbumList() != null) {
                        arrayList.add(channelLabel);
                    }
                }
            }
        } else {
            a.a("ChannelLabels", "filter live program");
            for (ChannelLabel channelLabel2 : this.items) {
                if (!(channelLabel2.getType() == ResourceType.DEFAULT || channelLabel2.getType() == ResourceType.LIVE || channelLabel2.getType() == ResourceType.CHANNEL)) {
                    arrayList.add(channelLabel2);
                    if (TVApiBase.getTVApiTool().getRecommendAlbum() == null && channelLabel2.getType() == ResourceType.VIDEO) {
                        a.a("ChannelLabels", "set video for speed runner" + channelLabel2.getVideo().tvQid);
                        TVApiBase.getTVApiTool().setRecommednAlbum(channelLabel2.getVideo());
                    }
                }
            }
        }
        return arrayList;
    }
}
