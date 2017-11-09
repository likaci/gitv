package com.gala.video.app.epg.ui.albumlist.data.loader;

import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.base.IChannelLabelsCallback;
import com.gala.albumprovider.logic.set.ChannelResourceSet;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import java.lang.ref.WeakReference;
import java.util.List;

public class ChannelLabelLoader extends BaseDataLoader {
    private Tag recommendTag;

    private static class IChannelLabelsCallbackImpl implements IChannelLabelsCallback {
        private final OnAlbumFetchedListener mFetchingData;
        private final int mIndex;
        private WeakReference<ChannelLabelLoader> mOuter;

        public IChannelLabelsCallbackImpl(ChannelLabelLoader outer, int index, OnAlbumFetchedListener fetchingData) {
            this.mOuter = new WeakReference(outer);
            this.mIndex = index;
            this.mFetchingData = fetchingData;
        }

        public void onSuccess(List<ChannelLabel> list) {
            ChannelLabelLoader outer = (ChannelLabelLoader) this.mOuter.get();
            if (outer != null) {
                outer.mOriginalList = list;
                outer.logAndRecord(BaseDataLoader.NOLOG ? null : "ChannelLabelLoader---success--资源位");
                this.mFetchingData.onFetchAlbumSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list, outer.mLoadingTag.getLayout(), this.mIndex, outer.mInfoModel));
            }
        }

        public void onFailure(ApiException e) {
            ChannelLabelLoader outer = (ChannelLabelLoader) this.mOuter.get();
            if (outer != null) {
                outer.logAndRecord(BaseDataLoader.NOLOG ? null : "ChannelLabelLoader---failed--资源位");
                outer.handleImplOnDataFail(e, this.mFetchingData);
            }
        }
    }

    public ChannelLabelLoader(IAlbumSource albumSource, IAlbumSet albumSet, AlbumInfoModel model) {
        super(albumSource, albumSet, model);
    }

    public void fetchAlbumData(int eachPageCount, int index, OnAlbumFetchedListener fetchingData, Tag currentTag) {
        String str;
        Tag[] tags;
        if (NOLOG) {
            str = null;
        } else {
            str = "fetchAlbumData-- eachPageCount = ∞, --index = " + index + "--AlbumSet = " + this.mAlbumSet;
        }
        logAndRecord(str);
        this.mLoadingTag = currentTag;
        if (this.recommendTag == null) {
            tags = new Tag[1];
        } else {
            tags = new Tag[2];
            tags[1] = this.recommendTag;
        }
        tags[0] = this.mLoadingTag;
        if (this.mAlbumSet != null) {
            ((ChannelResourceSet) this.mAlbumSet).loadDataAsync(new IChannelLabelsCallbackImpl(this, index, fetchingData), tags, DataInfoProvider.getLiveVersion());
        }
    }

    protected String getLogCatTag() {
        return "ChannelLabelLoader";
    }

    public void setRecTag(Tag recommendTag) {
        this.recommendTag = recommendTag;
    }
}
