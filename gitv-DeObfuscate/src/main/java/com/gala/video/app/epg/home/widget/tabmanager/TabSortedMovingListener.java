package com.gala.video.app.epg.home.widget.tabmanager;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;

public interface TabSortedMovingListener {
    void moveBackward(TabModel tabModel);

    void moveForward(TabModel tabModel);
}
