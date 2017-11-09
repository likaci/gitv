package com.gala.video.app.epg.netdiagnose;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.netspeed.NetSpeedUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.netdiagnose.IGetAlbumProvider.Callback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.netdiagnose.IGetAlbumProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import java.util.List;

public class GetAlbumProvider extends Wrapper {
    private static final String TAG = "GetAlbumProvider";

    public void getAlbumProviderAsync(final Callback callback) {
        GetInterfaceTools.getIHistoryCacheManager().loadHistoryList(1, 1, 0, new IHistoryResultCallBack() {
            public void onSuccess(List<Album> list, int total) {
                callback.onResult(NetSpeedUtils.getOneAlbumProvider(list));
            }
        });
    }
}
