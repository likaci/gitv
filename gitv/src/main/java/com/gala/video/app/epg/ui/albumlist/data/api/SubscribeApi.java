package com.gala.video.app.epg.ui.albumlist.data.api;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumSource;
import com.gala.albumprovider.logic.set.AlbumSubscribeSet;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnAlbumFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi.OnLabelFetchedListener;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SubscribeApi extends BaseDataApi {

    private static class IAlbumCallbackImpl implements IAlbumCallback {
        private final OnAlbumFetchedListener albumListener;
        private WeakReference<SubscribeApi> mOuter;

        public IAlbumCallbackImpl(SubscribeApi outer, OnAlbumFetchedListener albumListener) {
            this.albumListener = albumListener;
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(int arg0, List<Album> list) {
            SubscribeApi outer = (SubscribeApi) this.mOuter.get();
            if (outer != null) {
                outer.handleOnDataSuccess(AlbumDataMakeupFactory.get().dataListMakeup(list, outer.getLayoutKind(), 0, null), this.albumListener);
            }
        }

        public void onFailure(int arg0, ApiException e) {
            SubscribeApi outer = (SubscribeApi) this.mOuter.get();
            if (outer != null) {
                outer.handleDataApiOnDataFail(e, this.albumListener);
            }
        }
    }

    public SubscribeApi(AlbumInfoModel infoModel) {
        super(infoModel);
    }

    public void loadLabelData(OnLabelFetchedListener labelListener) {
    }

    public void loadAlbumData(OnAlbumFetchedListener albumListener) {
        if (isNeedLoad()) {
            CharSequence authCookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            if (StringUtils.isEmpty(authCookie)) {
                albumListener.onFetchAlbumSuccess(new ArrayList());
            } else {
                ((AlbumSubscribeSet) this.mAlbumSet).loadDataNewAsync(authCookie, new IAlbumCallbackImpl(this, albumListener));
            }
        }
    }

    public int getSelectType() {
        return 1;
    }

    public int getRecommendType() {
        return 0;
    }

    protected IAlbumSource getAlbumSource() {
        return this.mAlbumProvider.getSubscribeSource();
    }

    protected void setTotalCount() {
    }

    protected int getOriginalPage() {
        return 1;
    }

    protected int getEachPageCount() {
        return 60;
    }

    protected void resetChildrenApi() {
    }

    protected String getLogCatTag() {
        return "SubscribleApi";
    }
}
