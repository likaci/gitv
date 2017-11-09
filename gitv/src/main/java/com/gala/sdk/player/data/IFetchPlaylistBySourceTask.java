package com.gala.sdk.player.data;

import com.gala.tvapi.tv2.model.Episode;
import java.util.List;

public interface IFetchPlaylistBySourceTask {
    List<Episode> getFirstEpisodes(String str, String str2);

    List<Episode> getSecondList();

    int getTotal();
}
