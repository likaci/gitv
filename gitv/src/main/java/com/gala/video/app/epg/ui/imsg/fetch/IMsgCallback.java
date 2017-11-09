package com.gala.video.app.epg.ui.imsg.fetch;

import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.util.List;

public interface IMsgCallback {
    void onFail();

    void onSuccess(List<IMsgContent> list);
}
