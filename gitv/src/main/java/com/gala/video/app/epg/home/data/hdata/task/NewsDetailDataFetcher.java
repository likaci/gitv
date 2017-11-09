package com.gala.video.app.epg.home.data.hdata.task;

import android.os.AsyncTask;
import android.util.Log;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultDailyLabels;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class NewsDetailDataFetcher {
    private static final int MAX_TAB = 6;
    private static final String TAG = "NewsDetailDataFetcher";
    private OnTabListFetchedCallbackForHome mListFetchedCallback;
    private List<TabDataItem> mTabLabelList = new ArrayList();

    private class FetchDataTask extends AsyncTask<Object, Object, Object> {
        private FetchDataTask() {
        }

        protected Object doInBackground(Object... params) {
            int end = 6;
            LogUtils.d(NewsDetailDataFetcher.TAG, ">>doInBackground");
            NewsDetailDataFetcher.this.fetchTabList();
            if (NewsDetailDataFetcher.this.mTabLabelList.size() < 6) {
                end = NewsDetailDataFetcher.this.mTabLabelList.size();
            }
            for (int i = 0; i < end; i++) {
                NewsDetailDataFetcher.this.fetchEveryTabList(i);
            }
            LogUtils.d(NewsDetailDataFetcher.TAG, "<<doInBackground");
            return null;
        }

        protected void onPostExecute(Object result) {
            LogUtils.d(NewsDetailDataFetcher.TAG, "onPostExecute mAlbums=" + NewsDetailDataFetcher.this.mTabLabelList);
            if (NewsDetailDataFetcher.this.mListFetchedCallback != null) {
                NewsDetailDataFetcher.this.mListFetchedCallback.onListCallback(new ArrayList(NewsDetailDataFetcher.this.mTabLabelList));
            }
        }
    }

    public interface OnTabListFetchedCallbackForHome {
        void onListCallback(List<TabDataItem> list);
    }

    public NewsDetailDataFetcher(OnTabListFetchedCallbackForHome callback) {
        this.mListFetchedCallback = callback;
    }

    public void startLoad() {
        LogUtils.d(TAG, "startLoad");
        this.mTabLabelList.clear();
        new FetchDataTask().execute(new Object[0]);
    }

    public List<TabDataItem> startLoadByTabListSync(List<DailyTabInfo> infos) {
        LogUtils.d(TAG, ">>startLoadByTabListSync");
        this.mTabLabelList.clear();
        if (ListUtils.isEmpty((List) infos)) {
            LogUtils.d(TAG, "<<startLoadByTabListSync ListUtils.isEmpty(infos)");
            return null;
        }
        int i;
        for (i = 0; i < 6; i++) {
            TabDataItem tabDataItem = new TabDataItem();
            DailyLabel label = new DailyLabel();
            DailyTabInfo info = (DailyTabInfo) infos.get(i);
            if (info != null) {
                label.channelId = info.id;
                label.name = info.label;
            }
            tabDataItem.setLabel(label);
            this.mTabLabelList.add(tabDataItem);
        }
        for (i = 0; i < 6; i++) {
            fetchEveryTabList(i);
        }
        if (!isSuccess(this.mTabLabelList)) {
            return null;
        }
        LogUtils.e(TAG, "fetch daily news info is success");
        return new ArrayList(this.mTabLabelList);
    }

    private boolean isSuccess(List<TabDataItem> tabItems) {
        if (tabItems == null) {
            return false;
        }
        if (tabItems.isEmpty()) {
            return false;
        }
        if (tabItems.get(0) == null || ListUtils.isEmpty(((TabDataItem) tabItems.get(0)).getAlbumList())) {
            return false;
        }
        return true;
    }

    public List<TabDataItem> startLoadSync() {
        int end = 6;
        LogUtils.d(TAG, ">>startLoadSync");
        this.mTabLabelList.clear();
        fetchTabList();
        if (this.mTabLabelList.size() < 6) {
            end = this.mTabLabelList.size();
        }
        for (int i = 0; i < end; i++) {
            fetchEveryTabList(i);
        }
        LogUtils.d(TAG, "<<startLoadSync mTabLabelList=" + this.mTabLabelList);
        return new ArrayList(this.mTabLabelList);
    }

    private void fetchTabList() {
        LogUtils.d(TAG, "fetchTabList");
        VrsHelper.dailyLabels.callSync(new IVrsCallback<ApiResultDailyLabels>() {
            public void onSuccess(ApiResultDailyLabels result) {
                if (result == null || ListUtils.isEmpty(result.labels)) {
                    LogUtils.e(NewsDetailDataFetcher.TAG, "fetchTabList success null == result");
                    return;
                }
                List<DailyLabel> labels = result.labels;
                if (LogUtils.mIsDebug) {
                    LogUtils.e(NewsDetailDataFetcher.TAG, "fetchTabList success size = " + labels.size() + ", labels = " + labels);
                }
                for (DailyLabel label : labels) {
                    TabDataItem tabDataItem = new TabDataItem();
                    tabDataItem.setLabel(label);
                    NewsDetailDataFetcher.this.mTabLabelList.add(tabDataItem);
                }
            }

            public void onException(ApiException e) {
                if (LogUtils.mIsDebug) {
                    Log.e(NewsDetailDataFetcher.TAG, "fetchTabList exception code = " + e.getCode() + ", msg = " + e.getMessage());
                }
            }
        }, new String[0]);
    }

    private void fetchEveryTabList(final int index) {
        LogUtils.d(TAG, ">>fetchePlayList index = " + index);
        CharSequence channelId = ((TabDataItem) this.mTabLabelList.get(index)).getDailyLabel().channelId;
        CharSequence tagSet = ((TabDataItem) this.mTabLabelList.get(index)).getDailyLabel().tagSet;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fetchePlayList channelId = " + channelId + ", tagSet = " + tagSet);
        }
        String isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
        if (Project.getInstance().getBuild().isLitchi()) {
            VrsHelper.channelLabelsSize.callSync(new IVrsCallback<ApiResultChannelLabels>() {
                public void onException(ApiException e) {
                    if (LogUtils.mIsDebug) {
                        Log.d(NewsDetailDataFetcher.TAG, "fetch channelLabels onException exp code = " + e.getCode() + ", msg = " + e.getMessage());
                    }
                }

                public void onSuccess(ApiResultChannelLabels result) {
                    if (result == null) {
                        LogUtils.d(NewsDetailDataFetcher.TAG, "fetch channelLabels  onSuccess, but is null");
                        return;
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(NewsDetailDataFetcher.TAG, "fetchPlayList onSuccess list size = {" + result.data.getChannelLabelList().size() + "}");
                    }
                    List<ChannelLabel> labels = result.data.items;
                    List<Album> albums = new ArrayList();
                    for (ChannelLabel label : labels) {
                        Album album = label.getVideo();
                        album.area = "t_rose";
                        albums.add(album);
                    }
                    ((TabDataItem) NewsDetailDataFetcher.this.mTabLabelList.get(index)).setLabelImageUrl(((ChannelLabel) labels.get(0)).itemImageUrl);
                    ((TabDataItem) NewsDetailDataFetcher.this.mTabLabelList.get(index)).setLabelName(((ChannelLabel) labels.get(0)).itemName);
                    ((TabDataItem) NewsDetailDataFetcher.this.mTabLabelList.get(index)).getAlbumList().addAll(albums);
                }
            }, channelId, isFree, "1.0", "10");
        } else if (StringUtils.isEmpty(channelId) || StringUtils.isEmpty(tagSet)) {
            LogUtils.e(TAG, "invalid channelId or tagset");
        } else {
            VrsHelper.dailyInfo.callSync(new IVrsCallback<ApiResultRecommendListQipu>() {
                public void onSuccess(ApiResultRecommendListQipu result) {
                    if (result == null || result.getAlbumList() == null) {
                        LogUtils.d(NewsDetailDataFetcher.TAG, "fetchPlayList onSuccess, but is null");
                        return;
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(NewsDetailDataFetcher.TAG, "fetchPlayList onSuccess list = {" + result.getAlbumList() + "}");
                    }
                    ((TabDataItem) NewsDetailDataFetcher.this.mTabLabelList.get(index)).getAlbumList().addAll(result.getAlbumList());
                }

                public void onException(ApiException e) {
                    if (LogUtils.mIsDebug) {
                        Log.d(NewsDetailDataFetcher.TAG, "fetchPlayList onException exp code = " + e.getCode() + ", msg = " + e.getMessage());
                    }
                }
            }, "1", "10", channelId, tagSet, isFree);
        }
    }
}
