package com.gala.video.app.player.controller;

import android.content.Context;
import com.gala.sdk.player.data.IHistoryFetcher;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.data.LoopPlayPreference;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;

public class HistoryFetcher implements IHistoryFetcher {
    public Album getTvHistory(String tvId) {
        HistoryInfo info = GetInterfaceTools.getIHistoryCacheManager().getTvHistory(tvId);
        if (info != null) {
            return info.getAlbum();
        }
        return null;
    }

    public int getLoopPlayOffset(Context context, int position) {
        return LoopPlayPreference.getLoopPlayIndex(context, position);
    }

    public int getLoopPlayIndex(Context context, int position) {
        return LoopPlayPreference.getLoopPlayIndex(context, position);
    }

    public String getLoopPlayAlbumId(Context context, int position) {
        return LoopPlayPreference.getLoopPlayAlbumId(context, position);
    }

    public Album getAlbumHistory(String albumId) {
        HistoryInfo info = GetInterfaceTools.getIHistoryCacheManager().getAlbumHistory(albumId);
        if (info != null) {
            return info.getAlbum();
        }
        return null;
    }
}
