package com.gala.video.lib.share.uikit.action.model;

import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.carousel.CarouselHistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;

public class CarouseHistoryActionModel extends BaseActionModel<CarouselHistoryInfo> {
    private ChannelLabel mLabel;

    public CarouseHistoryActionModel(ItemDataType itemDataType) {
        super(itemDataType);
    }

    public BaseActionModel buildActionModel(CarouselHistoryInfo dataSource) {
        ChannelLabel label = new ChannelLabel();
        label.tableNo = StringUtils.parse(dataSource.getCarouselChannelNo(), 0);
        label.itemId = dataSource.getCarouselChannelId();
        String name = dataSource.getCarouselChannelName();
        if (!StringUtils.isEmpty((CharSequence) name) && name.length() >= 6) {
            name = name.substring(0, 5) + "…";
        }
        label.name = name;
        this.mLabel = label;
        return this;
    }

    public ChannelLabel getLabel() {
        return this.mLabel;
    }
}
