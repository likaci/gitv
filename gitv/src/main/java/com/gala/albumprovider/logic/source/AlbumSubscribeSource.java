package com.gala.albumprovider.logic.source;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.logic.set.AlbumSubscribeSet;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.i;

public class AlbumSubscribeSource extends i {
    private String a = AlbumProviderApi.getLanguages().getSubscribeName();

    public String getChannelName() {
        return this.a;
    }

    public Tag getDefaultTag() {
        return new Tag("0", LibString.MySubscribe, SourceTool.SUBSCRIBE_TAG, QLayoutKind.PORTRAIT);
    }

    public IAlbumSet getAlbumSet(Tag tag) {
        return new AlbumSubscribeSet();
    }
}
