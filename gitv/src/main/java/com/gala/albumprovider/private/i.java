package com.gala.albumprovider.private;

import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.base.ITagCallback;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import java.util.List;

public class i implements IAlbumSource {
    public String getChannelId() {
        return null;
    }

    public String getChannelName() {
        return null;
    }

    public void getTags(ITagCallback callback) {
    }

    public List<TwoLevelTag> getMultiTags() {
        return null;
    }

    public Tag getDefaultTag() {
        return null;
    }

    public IAlbumSet getAlbumSet(Tag tag) {
        return null;
    }

    public IAlbumSet getSearchAlbumSet(Tag tag) {
        return null;
    }
}
