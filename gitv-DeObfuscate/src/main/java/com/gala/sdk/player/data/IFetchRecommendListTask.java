package com.gala.sdk.player.data;

import com.gala.tvapi.tv2.model.Album;
import java.util.List;

public interface IFetchRecommendListTask {
    List<Album> getRecommendList(String str, int i, String str2);
}
