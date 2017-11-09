package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history;

import com.gala.tvapi.tv2.model.Album;
import java.util.List;

public interface IHistoryResultCallBack {
    void onSuccess(List<Album> list, int i);
}
