package com.gala.video.app.epg.ui.albumlist.data.loader;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import java.lang.ref.WeakReference;
import java.util.List;

public class ChannelAlbumLoader extends BaseDataLoader {

    private static class AlbumDataCallback implements IAlbumCallback {
        private int localCurPageIndex;
        private OnAlbumFetchedListener localListener;
        private Tag localTag;
        private WeakReference<ChannelAlbumLoader> mOuter;

        public AlbumDataCallback(ChannelAlbumLoader outer, OnAlbumFetchedListener fetchingData, Tag tag, int curPageIndex) {
            this.mOuter = new WeakReference(outer);
            this.localListener = fetchingData;
            this.localTag = tag;
            this.localCurPageIndex = curPageIndex;
        }

        public void onSuccess(int index, List<Album> list) {
            ChannelAlbumLoader outer = (ChannelAlbumLoader) this.mOuter.get();
            if (outer != null) {
                if (outer.mLoadingTag == null || outer.mLoadingTag == this.localTag) {
                    outer.mOriginalList = list;
                    this.localListener.onFetchAlbumSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list, this.localTag.getLayout(), this.localCurPageIndex, outer.mInfoModel));
                    return;
                }
                String str;
                if (BaseDataLoader.NOLOG) {
                    str = null;
                } else {
                    str = "AlbumDataCallback---success--but tag is different, so return, 回调后 tag:" + outer.mLoadingTag.getName() + ", 回调前 tag:" + (this.localTag != null ? this.localTag.getName() : "null");
                }
                outer.log(str);
            }
        }

        public void onFailure(int index, ApiException e) {
            if (((ChannelAlbumLoader) this.mOuter.get()) != null) {
                this.localListener.onFetchAlbumFail(e);
            }
        }
    }

    public ChannelAlbumLoader(IAlbumSource albumSource, IAlbumSet albumSet, AlbumInfoModel model) {
        super(albumSource, albumSet, model);
    }

    public void fetchAlbumData(int eachPageCount, int index, OnAlbumFetchedListener albumFetchListener, Tag currentTag) {
        String str = null;
        logAndRecord(NOLOG ? null : "fetchAlbumData-- eachPageCount = " + eachPageCount + "--index = " + index + "--AlbumSet = " + this.mAlbumSet);
        this.mLoadingTag = currentTag;
        if (this.mAlbumSet != null) {
            if (!NOLOG) {
                str = "AlbumDataCallback---normal page ";
            }
            log(str);
            this.mAlbumSet.loadDataAsync(index, eachPageCount, new AlbumDataCallback(this, albumFetchListener, this.mLoadingTag, index));
        }
    }

    protected String getLogCatTag() {
        return "ChannelAlbumLoader";
    }
}
