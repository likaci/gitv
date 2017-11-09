package com.gala.albumprovider.logic.source;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.logic.set.AlbumFavoritesSet;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.i;

public class AlbumFavoritesSource extends i {
    private String a = AlbumProviderApi.getLanguages().getFavouritesName();

    public String getChannelName() {
        return this.a;
    }

    public Tag getDefaultTag() {
        return new Tag("0", LibString.DefaultTagName, SourceTool.COLLECT_TAG, QLayoutKind.PORTRAIT);
    }

    public IAlbumSet getAlbumSet(Tag tag) {
        return new AlbumFavoritesSet(false);
    }
}
