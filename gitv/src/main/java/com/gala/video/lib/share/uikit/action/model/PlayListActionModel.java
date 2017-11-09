package com.gala.video.lib.share.uikit.action.model;

import com.gala.sdk.player.PlayParams;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;

public class PlayListActionModel extends BaseActionModel<ChannelLabel> {
    private String mFrom;
    private ChannelLabel mLabel;
    private PlayParams mPlayParams;
    private String mTitle;

    public PlayListActionModel(ItemDataType itemDataType, ChannelLabel dataSource) {
        super(itemDataType);
        this.mLabel = dataSource;
    }

    public BaseActionModel buildActionModel(ChannelLabel label) {
        PlayParams playParams = new PlayParams();
        playParams.playListId = label.id;
        String title = DataBuildTool.getPrompt(label);
        String from = PingBackUtils.getTabName() + "_rec";
        this.mPlayParams = playParams;
        this.mTitle = title;
        this.mFrom = from;
        return this;
    }

    public PlayParams getPlayParams() {
        return this.mPlayParams;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getFrom() {
        return this.mFrom;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }
}
