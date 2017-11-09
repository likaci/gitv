package com.gala.video.app.epg.ui.albumlist.widget.cardtype;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public interface ICard {
    void releaseData();

    void setImageData(IData iData);

    void setTextData(IData iData);
}
