package com.gala.video.app.epg.ui.albumlist.data.api;

import android.annotation.SuppressLint;
import com.gala.albumprovider.base.IAlbumCallback;
import com.gala.albumprovider.base.IAlbumSet;
import com.gala.albumprovider.logic.set.search.SearchPeopleSet;
import com.gala.albumprovider.logic.set.search.SearchPeopleSet.IStarDetailCallback;
import com.gala.albumprovider.logic.source.search.AlbumSearchSource;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.data.factory.AlbumDataMakeupFactory;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchStarApi extends SearchResultApi {
    private static final String EPISODE_ID = "2";
    private static final String FILM_ID = "1";
    private static final int INVALID_INDEX = -1;

    private static class AlbumDataCallback implements IAlbumCallback {
        private IStarAlbumCallback fetchingDataListener;
        private long localLogTime1 = System.currentTimeMillis();
        private Tag localTag;
        private WeakReference<SearchStarApi> mOuter;

        public AlbumDataCallback(SearchStarApi outer, IStarAlbumCallback fetchingData, Tag tag) {
            this.mOuter = new WeakReference(outer);
            this.fetchingDataListener = fetchingData;
            this.localTag = tag;
        }

        public void onSuccess(int index, List<Album> albumlist) {
            String str = null;
            SearchStarApi outer = (SearchStarApi) this.mOuter.get();
            if (outer != null) {
                if (outer.mLoadingTag == null || outer.mLoadingTag == this.localTag) {
                    outer.mOriginalList = albumlist;
                    if (this.fetchingDataListener == null) {
                        if (!SearchStarApi.NOLOG) {
                            str = "OnDataFetchedImpl---success localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                        }
                        outer.log(str);
                        return;
                    }
                    String tagMsg;
                    if (SearchStarApi.NOLOG) {
                        tagMsg = null;
                    } else {
                        tagMsg = "OnDataFetchedImpl---success---tag.name=" + (this.localTag != null ? this.localTag.getName() : "null") + "--timeToken = " + (System.currentTimeMillis() - this.localLogTime1);
                    }
                    outer.logAndRecord(tagMsg);
                    outer.handleOnDataSuccess(albumlist, this.fetchingDataListener);
                    return;
                }
                if (!SearchStarApi.NOLOG) {
                    str = "AlbumDataCallback---success--but tag is different, so return, 回调后 tag:" + outer.mLoadingTag.getName() + ", 回调前 tag:" + (this.localTag != null ? this.localTag.getName() : "null");
                }
                outer.logAndRecord(str);
            }
        }

        public void onFailure(int index, ApiException e) {
            final SearchStarApi outer = (SearchStarApi) this.mOuter.get();
            if (outer != null) {
                if (this.fetchingDataListener == null) {
                    String str;
                    if (SearchStarApi.NOLOG) {
                        str = null;
                    } else {
                        str = "OnDataFetchedImpl---fail localDataListener=null--return--tag.name=" + (this.localTag != null ? this.localTag.getName() : "null");
                    }
                    outer.log(str);
                    return;
                }
                final long time1 = System.currentTimeMillis();
                outer.mIsLoading = false;
                outer.mLoadingTag = outer.mNewTag;
                outer.mPrePageIndex = outer.mCurPageIndex;
                final ApiException apiException = e;
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int state) {
                        String str;
                        SearchStarApi searchStarApi = outer;
                        if (SearchStarApi.NOLOG) {
                            str = null;
                        } else {
                            str = "BaseDataApi---handleDataApiOnDataFail---end netcheck timeToken=" + (System.currentTimeMillis() - time1);
                        }
                        searchStarApi.logAndRecord(str);
                        AlbumDataCallback.this.fetchingDataListener.onFail(apiException);
                    }
                });
                QAPingback.error(SearchStarApi.LOG_TAG, String.valueOf(outer.mInfoModel.getChannelId()), outer.mInfoModel.getDataTagName(), e);
            }
        }
    }

    public interface IStarAlbumCallback {
        void onFail(ApiException apiException);

        void onSuccess(Map<String, List<IData>> map, List<Tag> list);
    }

    public SearchStarApi(AlbumInfoModel model) {
        super(model);
    }

    public void loadStarAlbumData(IStarAlbumCallback fetchingDataListener) {
        if (isNeedLoad()) {
            this.mIsLoading = true;
            this.mLoadingTag = this.mNewTag;
            this.mAlbumSet.loadDataAsync(this.mCurPageIndex, getEachPageCount(), new AlbumDataCallback(this, fetchingDataListener, this.mLoadingTag));
        }
    }

    @SuppressLint({"UseSparseArrays"})
    private void handleOnDataSuccess(List<Album> list, IStarAlbumCallback fetchingData) {
        this.mIsLoading = false;
        this.mLoadingTag = this.mNewTag;
        List<Tag> tagList = this.mAlbumSet.getTagList();
        if (ListUtils.isEmpty((List) tagList) || ListUtils.isEmpty((List) list)) {
            fetchingData.onSuccess(null, tagList);
            return;
        }
        Map<String, List<Album>> map = new HashMap();
        extract2Map(list, map);
        order4Taglist(tagList, map);
        logAndRecord("handleOnDataSuccess--map.size = " + map.size() + ", tagList.size = " + tagList.size());
        Map<String, List<IData>> DataMap = new HashMap();
        for (Tag tag : tagList) {
            DataMap.put(tag.getID(), AlbumDataMakeupFactory.get().dataListMakeup((List) map.get(tag.getID()), tag.getLayout(), 1, this.mInfoModel));
        }
        fetchingData.onSuccess(DataMap, tagList);
    }

    private void order4Taglist(List<Tag> tagList, Map<String, List<Album>> map) {
        int filmIndex = -1;
        int epodIndex = -1;
        for (Tag tag : tagList) {
            if (tag.getID().equals("1")) {
                filmIndex = tagList.indexOf(tag);
            }
            if (tag.getID().equals("2")) {
                epodIndex = tagList.indexOf(tag);
            }
        }
        int filmCount = ListUtils.getCount((List) map.get("1"));
        int epodCount = ListUtils.getCount((List) map.get("2"));
        if (filmIndex >= 0 && epodIndex >= 0) {
            if (filmCount >= epodCount && epodIndex < filmIndex) {
                try {
                    tagList.add(epodIndex, tagList.remove(filmIndex));
                    tagList.add(filmIndex, tagList.remove(epodIndex + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (filmCount < epodCount && epodIndex > filmIndex) {
                try {
                    tagList.add(filmIndex, tagList.remove(epodIndex));
                    tagList.add(epodIndex, tagList.remove(filmIndex + 1));
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private void extract2Map(List<Album> list, Map<String, List<Album>> map) {
        for (Album data : list) {
            int chnId = data.chnId;
            if (chnId > 0 && chnId <= 100) {
                List<Album> list2 = (List) map.get(chnId + "");
                if (list2 == null) {
                    list2 = new ArrayList();
                    map.put(chnId + "", list2);
                }
                list2.add(data);
            }
        }
    }

    public IAlbumSet getAlbumSet() {
        return ((AlbumSearchSource) this.mAlbumSource).getSearchPeopleSet(this.mInfoModel.getSearchModel().getQpId(), this.mNewTag);
    }

    protected int getEachPageCount() {
        return 120;
    }

    public Tag getDefaultTag() {
        return ((AlbumSearchSource) this.mAlbumSource).getPeopleDefaultTag();
    }

    protected String getLogCatTag() {
        return "SearchStarApi";
    }

    public void loadStarDetailData(final IStarDetailCallback callback) {
        if (this.mAlbumSet == null || !(this.mAlbumSet instanceof SearchPeopleSet)) {
            String str;
            if (NOLOG) {
                str = null;
            } else {
                str = "loadStarDetailData---albumset= null ,return;";
            }
            log(str);
            return;
        }
        ((SearchPeopleSet) this.mAlbumSet).loadStarDetailData(new IStarDetailCallback() {
            public void onSuccess(Star star, String msg) {
                SearchStarApi.this.logAndRecord("onSuccess --- " + star);
                CharSequence bir = SearchStarApi.this.dealBirthday(star.birthday);
                if (!StringUtils.isEmpty(bir)) {
                    star.birthday = bir;
                }
                callback.onSuccess(star, msg);
            }

            public void onFail(ApiException e) {
                SearchStarApi.this.logAndRecord("onFail --- " + e.getMessage());
                callback.onFail(e);
            }
        });
    }

    private String dealBirthday(String birth) {
        if (StringUtils.isEmpty((CharSequence) birth)) {
            return null;
        }
        String birthday = "";
        String subStr = birth;
        int indexOf = subStr.indexOf(45);
        if (indexOf > 0) {
            birthday = (birthday + subStr.substring(0, indexOf)) + ResourceUtil.getStr(R.string.year);
            subStr = subStr.substring(indexOf + 1);
        }
        indexOf = subStr.indexOf(45);
        if (indexOf > 0) {
            birthday = (birthday + subStr.substring(0, indexOf)) + ResourceUtil.getStr(R.string.month);
            subStr = subStr.substring(indexOf + 1);
        }
        indexOf = subStr.indexOf(45);
        if (indexOf <= 0) {
            return birthday;
        }
        return (birthday + subStr.substring(0, indexOf)) + ResourceUtil.getStr(R.string.day);
    }
}
