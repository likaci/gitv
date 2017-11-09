package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.adapter;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.IActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;

public interface IMultiSubjectVAdapter {
    IActionListener getActionListener();

    IPingbackListener getPingbackListener();

    void reLoad();

    void setLastLoseFocusPosition(int i, int i2);
}
