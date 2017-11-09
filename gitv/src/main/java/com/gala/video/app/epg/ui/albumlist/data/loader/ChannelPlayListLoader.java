package com.gala.video.app.epg.ui.albumlist.data.loader;

import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.logic.set.ChannelPlayListSet;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.result.ApiResultChannelPlayList;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import java.lang.ref.WeakReference;

public class ChannelPlayListLoader extends BaseDataLoader {

    private static class IVrsCallbackImpl implements IVrsCallback<ApiResultChannelPlayList> {
        private final OnAlbumFetchedListener mFetchingData;
        private final int mIndex;
        private WeakReference<ChannelPlayListLoader> mOuter;

        public IVrsCallbackImpl(ChannelPlayListLoader outer, int index, OnAlbumFetchedListener fetchingData) {
            this.mOuter = new WeakReference(outer);
            this.mIndex = index;
            this.mFetchingData = fetchingData;
        }

        public void onSuccess(ApiResultChannelPlayList list) {
            ChannelPlayListLoader outer = (ChannelPlayListLoader) this.mOuter.get();
            if (outer != null) {
                outer.mOriginalList = list.data;
                this.mFetchingData.onFetchAlbumSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list.data, outer.mLoadingTag.getLayout(), this.mIndex, outer.mInfoModel));
            }
        }

        public void onException(ApiException e) {
            ChannelPlayListLoader outer = (ChannelPlayListLoader) this.mOuter.get();
            if (outer != null) {
                outer.handleImplOnDataFail(e, this.mFetchingData);
            }
        }
    }

    public ChannelPlayListLoader(IAlbumSource albumSource, IAlbumSet albumSet, AlbumInfoModel model) {
        super(albumSource, albumSet, model);
    }

    public void fetchAlbumData(int eachPageCount, int index, OnAlbumFetchedListener fetchingData, Tag currentTag) {
        String str;
        if (NOLOG) {
            str = null;
        } else {
            str = "fetchAlbumData-- eachPageCount = 120,--index = " + index + "--AlbumSet = " + this.mAlbumSet;
        }
        logAndRecord(str);
        this.mLoadingTag = currentTag;
        if (this.mAlbumSet != null) {
            ((ChannelPlayListSet) this.mAlbumSet).loadDataAsync(index, 120, new IVrsCallbackImpl(this, index, fetchingData));
        }
    }

    protected String getLogCatTag() {
        return "ChannelPlayListLoader";
    }
}
