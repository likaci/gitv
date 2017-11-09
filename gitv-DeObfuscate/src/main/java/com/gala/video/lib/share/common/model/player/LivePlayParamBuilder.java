package com.gala.video.lib.share.common.model.player;

import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;

public class LivePlayParamBuilder extends AbsPlayParamBuilder {
    public Album mAlbum;
    public ArrayList<Album> mFlowerList;

    public LivePlayParamBuilder setLiveAlbum(Album album) {
        this.mAlbum = album;
        return this;
    }

    public LivePlayParamBuilder setFlowerList(ArrayList<Album> flowerList) {
        this.mFlowerList = flowerList;
        return this;
    }
}
