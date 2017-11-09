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
import com.gala.albumprovider.p001private.C0064d;
import com.gala.albumprovider.p001private.C0067g;
import com.gala.albumprovider.util.DefaultMenus;
import com.gala.albumprovider.util.USALog;
import com.gala.tvapi.tv2.model.Channel;
import java.util.List;

public class C0041a implements IAlbumProvider {
    private final String f63a = "AlbumProvider";

    public C0041a() {
        DefaultMenus.initData();
    }

    public void setChannels(List<Channel> channels) {
        C0067g.m139a().m142a((List) channels);
    }

    public SparseArray<QChannel> getChannels() {
        return C0067g.m139a().m139a();
    }

    public void isNeedChannelCache(boolean isNeedCache) {
        C0064d.m122a().m129a(isNeedCache);
    }

    public IAlbumSource getChannelAlbumSource(String id, boolean isFree, String version) {
        if (C0067g.m139a().m139a().size() == 0) {
            USALog.m150e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
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
        if (C0067g.m139a().m139a().size() == 0) {
            USALog.m150e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
        }
        return new AlbumChannelSource(id, isFree, isNeedLive);
    }

    public IAlbumSource getChannelAlbumSource(String id, boolean isFree, String version, boolean isNeedLive, boolean isSupportInitMovie) {
        if (C0067g.m139a().m139a().size() == 0) {
            USALog.m150e((Object) "getChannelAlbumSource()---MemoryCache.get().getChannelList().size() == 0");
        }
        return new AlbumChannelSource(id, isFree, isNeedLive, isSupportInitMovie);
    }

    public AlbumProviderProperty getProperty() {
        return C0042b.m24a();
    }

    public void setChannelCacheTime(long cacheTime) {
        C0064d.m122a().m128a(cacheTime);
    }
}
