package com.gala.albumprovider.logic.set.search;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.set.SetTool;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.private.h;
import com.gala.albumprovider.util.ThreadUtils;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.tv2.result.ApiResultStars;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import java.util.ArrayList;
import java.util.List;

public class SearchPeopleSet extends h {
    private int a;
    private String f157a;
    private List<Tag> f158a = new ArrayList();
    private int b;
    private String f159b;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int a = 0;
        private IAlbumCallback f163a;
        final /* synthetic */ SearchPeopleSet f164a;

        public ApiCallback(SearchPeopleSet searchPeopleSet, int pageIndex, IAlbumCallback callback) {
            this.f164a = searchPeopleSet;
            this.f163a = callback;
            this.a = pageIndex;
        }

        public void onException(ApiException ex) {
            this.f163a.onFailure(this.a, ex);
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (result != null) {
                this.f164a.a = result.total;
                if (result.docs == null || result.docs.equals("")) {
                    this.f164a.b = result.total;
                } else {
                    this.f164a.b = Integer.valueOf(result.docs).intValue();
                }
                if (result.chnList != null) {
                    for (ChnList chnList : result.chnList) {
                        this.f164a.f159b.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.PEOPLE_TAG, SetTool.setLayoutKind(String.valueOf(chnList.chnId))));
                    }
                }
                this.f163a.onSuccess(this.a, result.data);
                return;
            }
            this.f163a.onFailure(this.a, new ApiException("ApiResult is null"));
        }
    }

    public interface IStarDetailCallback {
        void onFail(ApiException apiException);

        void onSuccess(Star star, String str);
    }

    private class StarCallback implements IApiCallback<ApiResultStars> {
        private IStarDetailCallback a;
        final /* synthetic */ SearchPeopleSet f165a;

        StarCallback(SearchPeopleSet searchPeopleSet, IStarDetailCallback callback) {
            this.f165a = searchPeopleSet;
            this.a = callback;
        }

        public void onException(ApiException e) {
            this.a.onFail(e);
        }

        public void onSuccess(ApiResultStars stars) {
            if (stars != null) {
                List list = stars.data;
                if (list == null || list.size() <= 0) {
                    this.a.onSuccess(null, stars.msg);
                    return;
                } else {
                    this.a.onSuccess((Star) list.get(0), stars.msg);
                    return;
                }
            }
            this.a.onSuccess(null, null);
        }
    }

    public SearchPeopleSet(String id, String tagId) {
        this.f157a = tagId;
        this.f159b = id;
    }

    public String getTagId() {
        return this.f157a;
    }

    public int getAlbumCount() {
        return this.a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f157a.split(",").length > 1) {
            return QLayoutKind.PORTRAIT;
        }
        return SetTool.setLayoutKind(this.f157a);
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchPeopleSet f161a;

            public void run() {
                TVApi.searchPersonAlbums.call(new ApiCallback(this.f161a, pageNo, albumCallback), this.f161a.f159b, this.f161a.f157a, String.valueOf(pageNo), String.valueOf(pageSize));
            }
        });
    }

    public void loadStarDetailData(final IStarDetailCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchPeopleSet f162a;

            public void run() {
                TVApi.stars.call(new StarCallback(this.f162a, callback), this.f162a.f159b);
            }
        });
    }

    public List<Tag> getTagList() {
        return this.f158a;
    }

    public int getSearchCount() {
        return this.b;
    }
}
