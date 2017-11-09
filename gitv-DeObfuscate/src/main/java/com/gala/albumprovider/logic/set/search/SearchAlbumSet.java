package com.gala.albumprovider.logic.set.search;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.set.SetTool;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchAlbumSet extends C0045h {
    private int f187a = 0;
    private Tag f188a;
    private String f189a = "";
    private List<Tag> f190a = new ArrayList();
    private boolean f191a = false;
    private int f192b = 0;
    private List<Album> f193b = new ArrayList();
    private int f194c = 0;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int f183a = 0;
        private IAlbumCallback f184a;
        final /* synthetic */ SearchAlbumSet f185a;
        private String f186a = "";

        public ApiCallback(SearchAlbumSet searchAlbumSet, int pageIndex, String tagId, IAlbumCallback callback) {
            this.f185a = searchAlbumSet;
            this.f184a = callback;
            this.f183a = pageIndex;
            this.f186a = tagId;
        }

        public void onException(ApiException ex) {
            this.f184a.onFailure(this.f183a, ex);
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (result != null) {
                this.f185a.f187a = result.total;
                if (result.docs == null || result.docs.equals("")) {
                    this.f185a.f192b = result.total;
                } else {
                    this.f185a.f192b = Integer.valueOf(result.docs).intValue();
                }
                this.f185a.f190a.clear();
                this.f185a.f190a.add(new Tag("0", LibString.DefaultTagName, SourceTool.SEARCH_TAG, QLayoutKind.LANDSCAPE));
                if (result.chnList != null) {
                    for (ChnList chnList : result.chnList) {
                        this.f185a.f190a.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.SEARCH_TAG, QLayoutKind.LANDSCAPE));
                    }
                }
                if (!this.f186a.equals(SourceTool.PEOPLE_TAG) || this.f185a.f190a || result.getAlbumList() == null) {
                    this.f184a.onSuccess(this.f183a, result.getAlbumList());
                    return;
                }
                if (this.f183a == 1) {
                    this.f185a.f193b.clear();
                }
                for (Album album : result.getAlbumList()) {
                    if (album.getType() != AlbumType.PEOPLE) {
                        this.f185a.f194c = this.f185a.f193b.size();
                        this.f185a.f191a = true;
                        break;
                    }
                    this.f185a.f193b.add(album);
                }
                this.f184a.onSuccess(this.f183a, this.f185a.f193b);
                return;
            }
            this.f184a.onFailure(this.f183a, new ApiException("ApiResult is null"));
        }
    }

    public SearchAlbumSet(String keyword, Tag tag) {
        this.f188a = tag;
        this.f189a = keyword;
        this.f191a = false;
    }

    public void loadDataAsync(final int pageIndex, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchAlbumSet f181a;

            public void run() {
                this.f181a.m72a(pageIndex, pageSize, albumCallback);
            }
        });
    }

    private void m72a(int i, int i2, IAlbumCallback iAlbumCallback) {
        if (!SetTool.isCorrectParams(iAlbumCallback, i, i2) || SetTool.isOutOfRange(iAlbumCallback, getAlbumCount(), i, i2)) {
            return;
        }
        if (AlbumProviderApi.getAlbumProvider().getProperty().isDebug()) {
            iAlbumCallback.onFailure(i, new ApiException("", "-100", ErrorEvent.HTTP_CODE_SUCCESS, "http://<TvServer>/itv/albumSearch/199/" + this.f189a + "/0/" + i + "/" + i2));
        } else if (this.f188a.getID().equals(SourceTool.PEOPLE_TAG)) {
            TVApi.albumSearch.call(new ApiCallback(this, i, this.f188a.getID(), iAlbumCallback), this.f189a, "0", String.valueOf(i), String.valueOf(i2));
        } else {
            TVApi.albumSearch.call(new ApiCallback(this, i, this.f188a.getID(), iAlbumCallback), this.f189a, this.f188a.getID(), String.valueOf(i), String.valueOf(i2));
        }
    }

    public String getTagId() {
        return this.f188a.getID();
    }

    public int getAlbumCount() {
        return this.f187a;
    }

    public int getSearchCount() {
        if (this.f188a.getID().equals(SourceTool.PEOPLE_TAG)) {
            return this.f194c;
        }
        return this.f192b;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f188a.getID().equals("0") || this.f188a.getID().equals("")) {
            return QLayoutKind.PORTRAIT;
        }
        return SetTool.setLayoutKind(this.f188a.getID());
    }

    public List<Tag> getTagList() {
        return this.f190a;
    }
}
