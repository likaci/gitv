package com.gala.video.lib.share.ifmanager.bussnessIF.epg.album;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;

public interface IData<T> {
    void click(Context context, Object obj);

    Album getAlbum();

    boolean getCornerStatus(int i);

    T getData();

    String getField(int i);

    String getImageUrl(int i);

    ResourceType getResourceType();

    String getText(int i);

    boolean isShowingCard();

    void setIndexOfCurPage(int i);

    void setShowingCard(boolean z);
}
