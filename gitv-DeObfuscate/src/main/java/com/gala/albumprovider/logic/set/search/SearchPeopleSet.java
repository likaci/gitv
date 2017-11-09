package com.gala.albumprovider.logic.set.search;

import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.logic.set.SetTool;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.albumprovider.p001private.C0045h;
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

public class SearchPeopleSet extends C0045h {
    private int f206a;
    private String f207a;
    private List<Tag> f208a = new ArrayList();
    private int f209b;
    private String f210b;

    private class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        private int f201a = 0;
        private IAlbumCallback f202a;
        final /* synthetic */ SearchPeopleSet f203a;

        public ApiCallback(SearchPeopleSet searchPeopleSet, int pageIndex, IAlbumCallback callback) {
            this.f203a = searchPeopleSet;
            this.f202a = callback;
            this.f201a = pageIndex;
        }

        public void onException(ApiException ex) {
            this.f202a.onFailure(this.f201a, ex);
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (result != null) {
                this.f203a.f206a = result.total;
                if (result.docs == null || result.docs.equals("")) {
                    this.f203a.f209b = result.total;
                } else {
                    this.f203a.f209b = Integer.valueOf(result.docs).intValue();
                }
                if (result.chnList != null) {
                    for (ChnList chnList : result.chnList) {
                        this.f203a.f210b.add(new Tag(String.valueOf(chnList.chnId), chnList.chnName, SourceTool.PEOPLE_TAG, SetTool.setLayoutKind(String.valueOf(chnList.chnId))));
                    }
                }
                this.f202a.onSuccess(this.f201a, result.data);
                return;
            }
            this.f202a.onFailure(this.f201a, new ApiException("ApiResult is null"));
        }
    }

    public interface IStarDetailCallback {
        void onFail(ApiException apiException);

        void onSuccess(Star star, String str);
    }

    private class StarCallback implements IApiCallback<ApiResultStars> {
        private IStarDetailCallback f204a;
        final /* synthetic */ SearchPeopleSet f205a;

        StarCallback(SearchPeopleSet searchPeopleSet, IStarDetailCallback callback) {
            this.f205a = searchPeopleSet;
            this.f204a = callback;
        }

        public void onException(ApiException e) {
            this.f204a.onFail(e);
        }

        public void onSuccess(ApiResultStars stars) {
            if (stars != null) {
                List list = stars.data;
                if (list == null || list.size() <= 0) {
                    this.f204a.onSuccess(null, stars.msg);
                    return;
                } else {
                    this.f204a.onSuccess((Star) list.get(0), stars.msg);
                    return;
                }
            }
            this.f204a.onSuccess(null, null);
        }
    }

    public SearchPeopleSet(String id, String tagId) {
        this.f207a = tagId;
        this.f210b = id;
    }

    public String getTagId() {
        return this.f207a;
    }

    public int getAlbumCount() {
        return this.f206a;
    }

    public QLayoutKind getLayoutKind() {
        if (this.f207a.split(",").length > 1) {
            return QLayoutKind.PORTRAIT;
        }
        return SetTool.setLayoutKind(this.f207a);
    }

    public void loadDataAsync(final int pageNo, final int pageSize, final IAlbumCallback albumCallback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchPeopleSet f197a;

            public void run() {
                TVApi.searchPersonAlbums.call(new ApiCallback(this.f197a, pageNo, albumCallback), this.f197a.f210b, this.f197a.f207a, String.valueOf(pageNo), String.valueOf(pageSize));
            }
        });
    }

    public void loadStarDetailData(final IStarDetailCallback callback) {
        ThreadUtils.execute(new Runnable(this) {
            final /* synthetic */ SearchPeopleSet f200a;

            public void run() {
                TVApi.stars.call(new StarCallback(this.f200a, callback), this.f200a.f210b);
            }
        });
    }

    public List<Tag> getTagList() {
        return this.f208a;
    }

    public int getSearchCount() {
        return this.f209b;
    }
}
