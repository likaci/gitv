package com.gala.albumprovider.logic.set.search;

import com.gala.albumprovider.AlbumProviderApi;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.set.SetTool;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.LibString;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.h;
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

public class SearchAlbumSet extends h {
    private int a = 0;
    private Tag f146a;
    private String f147a = "";
    private List<Tag> f148a = new ArrayList();
    private boolean f149a = false;
    private int b = 0;
    private List<Album> f150b = new ArrayList();
    private int c = 0;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int a = 0;
        private IAlbumCallback f153a;
        final /* synthetic */ SearchAlbumSet f154a;
        private String f155a = "";

        public ApiCallback(SearchAlbumSet searchAlbumSet, int pageIndex, String tagId, IAlbumCallback callback) {
            this.f154a = searchAlbumSet;
            this.f153a = callback;
            this.a = pageIndex;
            this.f155a = tagId;
        }

        public void onException(ApiException ex) {
            this.f153a.onFailure(this.a, ex);
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (result != null) {
                this.f154a.a = result.total;
                if (result.docs == null || result.docs.equals("")) {
                    this.f154a.b = result.total;
                } else {
                    this.f154a.b = Integer.valueOf(result.docs).intValue();
                }
                this.f154a.f148a.clear();
                this.f154a.f148a.add(new Tag("0", LibString.DefaultTagName, SourceTool.SEARCH_TAG, QLayoutKind.LANDSCAPE));
                if (result.chnList != null) {
                    for (ChnList chnList : result.chnList) {
                        this.f154a.f148a.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.SEARCH_TAG, QLayoutKind.LANDSCAPE));
                    }
                }
                if (!this.f155a.equals(SourceTool.PEOPLE_TAG) || this.f154a.f148a || result.getAlbumList() == null) {
                    this.f153a.onSuccess(this.a, result.getAlbumList());
                    return;
                }
                if (this.a == 1) {
                    this.f154a.f150b.clear();
                }
                for (Album album : result.getAlbumList()) {
                    if (album.getType() != AlbumType.PEOPLE) {
                        this.f154a.c = this.f154a.f150b.size();
                        this.f154a.f149a = true;
                        break;
                    }
                    this.f154a.f150b.add(album);
                }
                this.f153a.onSuccess(this.a, this.f154a.f150b);
                return;
            }
            this.f153a.onFailure(this.a, new ApiException("ApiResult is null"));
        }
    }

    public SearchAlbumSet(String keyword, Tag tag) {
        this.f146a = tag;
        this.f147a = keyword;
        this.f149a = false;
    }

    public void loadDataAsync(final int pageIndex, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchAlbumSet f152a;

            public void run() {
                this.f152a.a(pageIndex, pageSize, albumCallback);
            }
        });
    }

    private void a(int i, int i2, IAlbumCallback iAlbumCallback) {
        if (!SetTool.isCorrectParams(iAlbumCallback, i, i2) || SetTool.isOutOfRange(iAlbumCallback, getAlbumCount(), i, i2)) {
            return;
        }
        if (AlbumProviderApi.getAlbumProvider().getProperty().isDebug()) {
            iAlbumCallback.onFailure(i, new ApiException("", "-100", ErrorEvent.HTTP_CODE_SUCCESS, "http://<TvServer>/itv/albumSearch/199/" + this.f147a + "/0/" + i + "/" + i2));
        } else if (this.f146a.getID().equals(SourceTool.PEOPLE_TAG)) {
            TVApi.albumSearch.call(new ApiCallback(this, i, this.f146a.getID(), iAlbumCallback), this.f147a, "0", String.valueOf(i), String.valueOf(i2));
        } else {
            TVApi.albumSearch.call(new ApiCallback(this, i, this.f146a.getID(), iAlbumCallback), this.f147a, this.f146a.getID(), String.valueOf(i), String.valueOf(i2));
        }
    }

    public String getTagId() {
        return this.f146a.getID();
    }

    public int getAlbumCount() {
        return this.a;
    }

    public int getSearchCount() {
        if (this.f146a.getID().equals(SourceTool.PEOPLE_TAG)) {
            return this.c;
        }
        return this.b;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f146a.getID().equals("0") || this.f146a.getID().equals("")) {
            return QLayoutKind.PORTRAIT;
        }
        return SetTool.setLayoutKind(this.f146a.getID());
    }

    public List<Tag> getTagList() {
        return this.f148a;
    }
}
