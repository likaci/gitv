package com.gala.video.app.epg.home.widget.tabmanager;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;

public interface TabVisibilityListener {
    boolean addTab(TabModel tabModel, TabVisibilityItemView tabVisibilityItemView);

    boolean removeTab(TabModel tabModel, TabVisibilityItemView tabVisibilityItemView);
}
