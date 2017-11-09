package com.gala.albumprovider.base;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import java.util.List;

public interface IAlbumSet {
    int getAlbumCount();

    String getBackground();

    QLayoutKind getLayoutKind();

    int getSearchCount();

    String getTagId();

    List<Tag> getTagList();

    String getTagName();

    boolean isRunPlayList();

    void loadDataAsync(int i, int i2, IAlbumCallback iAlbumCallback);
}
