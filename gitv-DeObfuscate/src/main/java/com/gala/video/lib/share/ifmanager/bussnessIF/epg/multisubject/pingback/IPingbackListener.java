package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback;

import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;

public interface IPingbackListener {
    void sendCardShowPingback(int i, CardModel cardModel, int i2, MultiSubjectPingBackModel multiSubjectPingBackModel);

    void sendItemClickPingback(ViewHolder viewHolder, CardModel cardModel, int i, MultiSubjectPingBackModel multiSubjectPingBackModel);
}
