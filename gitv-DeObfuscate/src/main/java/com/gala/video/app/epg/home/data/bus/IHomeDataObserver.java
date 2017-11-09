package com.gala.video.app.epg.home.data.bus;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;

public interface IHomeDataObserver {
    void update(WidgetChangeStatus widgetChangeStatus, HomeModel homeModel);
}
