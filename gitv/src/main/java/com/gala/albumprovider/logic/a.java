package com.gala.albumprovider.logic;

import android.content.Context;
import android.util.SparseArray;
import com.gala.albumprovider.base.IAlbumProvider;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.logic.source.AlbumChannelSource;
import com.gala.albumprovider.logic.source.AlbumFavoritesSource;
import com.gala.albumprovider.logic.source.AlbumSubscribeSource;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.logic.source.search.AlbumSearchSource;
import com.gala.albumprovider.model.QChannel;
import com.gala.albumprovider.private.d;
import com.gala.albumprovider.private.g;
import com.gala.albumprovider.util.DefaultMenus;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.model.Channel;
import java.util.List;

public class a implements IAlbumProvider {
    private final String a = "AlbumProvider";

    public a() {
        DefaultMenus.initData();
    }

    public void setChannels(List<Channel> channels) {
        g.a().a((List) channels);
    }

    public SparseArray<QChannel> getChannels() {
        return g.a().a();
    }

    public void isNeedChannelCache(boolean isNeedCache) {
        d.a().a(isNeedCache);
    }

    public IAlbumSource getChannelAlbumSource(String id, boolean isFree, String version) {
        if (g.a().a().size() == 0) {
            USALog.e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
        }
        return new AlbumChannelSource(id, isFree);
    }

    public IAlbumSource getSearchSourceByChinese(String keyword) {
        return new AlbumSearchSource(keyword);
    }

    public IAlbumSource getFavouritesAlbumSource() {
        return new AlbumFavoritesSource();
    }

    public IAlbumSource getSubscribeSource() {
        return new AlbumSubscribeSource();
    }

    public void setContext(Context context) {
        SourceTool.setContent(context);
    }

    public IAlbumSource getChannelAlbumSource(String id, boolean isFree, String version, boolean isNeedLive) {
        if (g.a().a().size() == 0) {
            USALog.e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
        }
        return new AlbumChannelSource(id, isFree, isNeedLive);
    }

    public IAlbumSource getChannelAlbumSource(String id, boolean isFree, String version, boolean isNeedLive, boolean isSupportInitMovie) {
        if (g.a().a().size() == 0) {
            USALog.e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
        }
        return new AlbumChannelSource(id, isFree, isNeedLive, isSupportInitMovie);
    }

    public AlbumProviderProperty getProperty() {
        return b.a();
    }

    public void setChannelCacheTime(long cacheTime) {
        d.a().a(cacheTime);
    }
}
