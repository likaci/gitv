package com.gala.video.app.epg.ui.albumlist.data.loader;

import android.util.Log;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.model.Tag;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import java.util.List;

public abstract class BaseDataLoader {
    protected static boolean NOLOG = (!DebugUtils.ALBUM4_NEEDLOG);
    private String LOG_TAG;
    protected IAlbumSet mAlbumSet;
    protected IAlbumSource mAlbumSource;
    protected AlbumInfoModel mInfoModel;
    protected Tag mLoadingTag;
    protected List<?> mOriginalList;

    public abstract void fetchAlbumData(int i, int i2, OnAlbumFetchedListener onAlbumFetchedListener, Tag tag);

    protected abstract String getLogCatTag();

    public BaseDataLoader(IAlbumSource albumSource, IAlbumSet albumSet, AlbumInfoModel model) {
        this.LOG_TAG = "EPG/album4/BaseDataLoader";
        this.LOG_TAG = "EPG/album4/" + getLogCatTag();
        this.mAlbumSource = albumSource;
        this.mAlbumSet = albumSet;
        if (this.mAlbumSet == null) {
            log(NOLOG ? null : "BaseDataLoader---mAlbumSet == null---return");
        } else {
            this.mInfoModel = model;
        }
    }

    protected void handleImplOnDataFail(ApiException e, OnAlbumFetchedListener listener) {
        logAndRecord(NOLOG ? null : "BaseDataLoader ---- handleImplOnDataFail---e = " + e);
        listener.onFetchAlbumFail(e);
        QAPingback.error(this.LOG_TAG, String.valueOf(this.mInfoModel.getChannelId()), this.mInfoModel.getDataTagName(), e);
    }

    public List<?> getOriginalList() {
        return this.mOriginalList;
    }

    protected void log(String str) {
        if (str != null) {
            Log.e(this.LOG_TAG, "qdata//" + str);
        }
    }

    protected void logRecord(String str) {
        if (str != null) {
        }
    }

    protected void logAndRecord(String str) {
        if (str != null) {
            Log.e(this.LOG_TAG, "qdata//" + str);
        }
    }
}
