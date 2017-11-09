package com.gala.albumprovider.base;

import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import java.util.List;

public interface IAlbumSource {
    IAlbumSet getAlbumSet(Tag tag);

    String getChannelId();

    String getChannelName();

    Tag getDefaultTag();

    List<TwoLevelTag> getMultiTags();

    IAlbumSet getSearchAlbumSet(Tag tag);

    void getTags(ITagCallback iTagCallback);
}
