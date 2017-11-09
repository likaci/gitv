package com.gala.video.app.epg.ui.imsg.fetch;

import com.gala.albumprovider.model.Tag;
import java.util.List;

public interface IMsgDataSource {
    List<Tag> getLabels();

    void getMsgList(int i, IMsgCallback iMsgCallback);
}
