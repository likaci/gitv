package com.gala.sdk.player.data;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Episode;
import java.util.List;

public interface IFetchEpisodeListTask {

    public interface OnFullEpisodeListener {
        void onFullEpisodeReady();
    }

    List<Episode> getAllEpisodesFromCache();

    int getCacheEpisodesTotal();

    List<Episode> getCurrentEpisodeList(int i);

    List<Episode> getFullEpisodeList(Album album, boolean z);

    int getTotal();
}
