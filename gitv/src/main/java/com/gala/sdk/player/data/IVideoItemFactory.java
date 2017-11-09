package com.gala.sdk.player.data;

import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.player.SourceType;
import com.gala.tvapi.tv2.model.Album;

public interface IVideoItemFactory {
    IStarData createStarData(String str, String str2, String str3);

    IVideo createVideoItem(SourceType sourceType, Album album, IPlayerProfile iPlayerProfile);
}
