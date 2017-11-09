package com.gala.albumprovider.base;

import android.content.Context;
import android.util.SparseArray;
import com.gala.albumprovider.logic.AlbumProviderProperty;
import com.gala.albumprovider.model.QChannel;
import com.gala.tvapi.tv2.model.Channel;
import java.util.List;

public interface IAlbumProvider {
    IAlbumSource getChannelAlbumSource(String str, boolean z, String str2);

    IAlbumSource getChannelAlbumSource(String str, boolean z, String str2, boolean z2);

    IAlbumSource getChannelAlbumSource(String str, boolean z, String str2, boolean z2, boolean z3);

    SparseArray<QChannel> getChannels();

    IAlbumSource getFavouritesAlbumSource();

    AlbumProviderProperty getProperty();

    IAlbumSource getSearchSourceByChinese(String str);

    IAlbumSource getSubscribeSource();

    void isNeedChannelCache(boolean z);

    void setChannelCacheTime(long j);

    void setChannels(List<Channel> list);

    void setContext(Context context);
}
