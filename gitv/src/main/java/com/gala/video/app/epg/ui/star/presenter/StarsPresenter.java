package com.gala.video.app.epg.ui.star.presenter;

import android.os.Handler;
import android.os.Looper;
import com.gala.albumprovider.logic.set.search.SearchPeopleSet.IStarDetailCallback;
import com.gala.albumprovider.model.Tag;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.ui.star.data.IDataSource.IStarAlbumCallback;
import com.gala.video.app.epg.ui.star.domain.StarsDataSource;
import com.gala.video.app.epg.ui.star.model.StarTaskParams;
import com.gala.video.app.epg.ui.star.model.StarsInfoModel;
import com.gala.video.app.epg.ui.star.presenter.StarsContract.Presenter;
import com.gala.video.app.epg.ui.star.presenter.StarsContract.View;
import com.gala.video.app.epg.ui.star.utils.StarsPingbackUtil;
import com.gala.video.app.epg.ui.star.utils.StarsPingbackUtil.PageShowModel;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class StarsPresenter implements Presenter {
    private static final long LOADING_DELAY_MILLIS = 1500;
    protected static final String TAG = "EPG/StarsPresenter";
    private boolean isFirstEntry = true;
    private boolean isMapData = false;
    private boolean isStarData = false;
    private long mConsumedTime;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsDestroy;
    private NetworkPrompt mNetworkStatePrompt;
    private PageShowModel mPageShowModel;
    private Runnable mShowLoadingRunnable = new Runnable() {
        public void run() {
            View outer = (View) StarsPresenter.this.mStarsView.get();
            if (outer != null) {
                outer.showProgressBar();
            }
        }
    };
    private StarsInfoModel mStarsInfoModel;
    private final WeakReference<View> mStarsView;
    private final StarsDataSource mTasksRepository;

    private class NetworkListener implements INetworkStateListener {
        private NetworkListener() {
        }

        public void onConnected(boolean isChanged) {
            LogUtils.e(StarsPresenter.TAG, "onConnected() isChanged：" + isChanged);
            if (isChanged) {
                if (!StarsPresenter.this.isStarData) {
                    LogUtils.e(StarsPresenter.TAG, "onConnected() isStarData：" + StarsPresenter.this.isStarData);
                    StarsPresenter.this.getDetails();
                }
                if (!StarsPresenter.this.isMapData) {
                    StarsPresenter.this.removeLoadingCallbacks();
                    if (StarsPresenter.this.mHandler != null) {
                        StarsPresenter.this.mHandler.post(StarsPresenter.this.mShowLoadingRunnable);
                    }
                    StarsPresenter.this.getDatas();
                }
            }
        }
    }

    public StarsPresenter(StarsDataSource tasksRepository, View statisticsView, StarsInfoModel infoModel) {
        this.mTasksRepository = (StarsDataSource) ActivityUtils.checkNotNull(tasksRepository, "tasksRepository cannot be null");
        statisticsView = (View) ActivityUtils.checkNotNull(statisticsView, "View cannot be null!");
        this.mStarsView = new WeakReference(statisticsView);
        this.mStarsInfoModel = (StarsInfoModel) ActivityUtils.checkNotNull(infoModel, "starsInfoModel cannot be null!");
        statisticsView.setPresenter(this);
        this.mTasksRepository.initApi(this.mStarsInfoModel);
    }

    public void start() {
        startTask();
    }

    public void onResume() {
        if (this.mNetworkStatePrompt == null) {
            this.mNetworkStatePrompt = new NetworkPrompt(AppRuntimeEnv.get().getApplicationContext());
        }
        this.mNetworkStatePrompt.registerNetworkListener(new NetworkListener());
        if (!this.isFirstEntry) {
            sendShowPingback();
        }
        this.isFirstEntry = false;
    }

    public void onPause() {
        if (this.mNetworkStatePrompt != null) {
            this.mNetworkStatePrompt.unregisterNetworkListener();
        }
    }

    public void onDestroy() {
        this.mIsDestroy = true;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        this.mNetworkStatePrompt = null;
    }

    private void showLoadingDelayed() {
        if (this.mHandler != null) {
            this.mHandler.postDelayed(this.mShowLoadingRunnable, LOADING_DELAY_MILLIS);
        }
    }

    private void removeLoadingCallbacks() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mShowLoadingRunnable);
        }
    }

    private void startTask() {
        showLoadingDelayed();
        getDatas();
        getDetails();
    }

    private void getDatas() {
        this.mTasksRepository.getTasks(new IStarAlbumCallback() {
            public void onSuccess(Map<String, List<IData>> map, List<Tag> list, StarTaskParams params) {
                LogUtils.d(StarsPresenter.TAG, "getDatas() -> onSuccess() list:" + ListUtils.getCount((List) list) + ",map:" + ListUtils.getCount((Map) map));
                if (!StarsPresenter.this.mIsDestroy) {
                    StarsPresenter.this.onTaskSuccess(map, list, params);
                }
            }

            public void onFail(ApiException e) {
                if (!StarsPresenter.this.mIsDestroy) {
                    StarsPresenter.this.onTaskFail(e);
                }
            }
        });
    }

    private void onTaskSuccess(final Map<String, List<IData>> map, final List<Tag> list, StarTaskParams params) {
        Runnable mDataRunnable;
        removeLoadingCallbacks();
        if (ListUtils.isEmpty((List) list) || ListUtils.isEmpty((Map) map)) {
            this.isMapData = false;
            mDataRunnable = new Runnable() {
                public void run() {
                    StarsPresenter.this.showNoResultPanel(ErrorKind.NO_RESULT_AND_NO_MENU, null);
                }
            };
        } else {
            LogUtils.d(TAG, "getDatas() -> list is not null");
            this.isMapData = true;
            this.mConsumedTime = params.getGapTime();
            mDataRunnable = new Runnable() {
                public void run() {
                    View outer = (View) StarsPresenter.this.mStarsView.get();
                    if (outer != null) {
                        outer.showHasResultPanel();
                        outer.showDatas(map, list);
                        StarsPresenter.this.sendShowPingback();
                    }
                }
            };
        }
        onPost(mDataRunnable);
    }

    private void onTaskFail(final ApiException e) {
        onPost(new Runnable() {
            public void run() {
                LogUtils.e(StarsPresenter.TAG, "getDatas() -> onFail() e:" + e);
                StarsPresenter.this.removeLoadingCallbacks();
                StarsPresenter.this.showNoResultPanel(ErrorKind.NET_ERROR, e);
            }
        });
    }

    private void showNoResultPanel(ErrorKind kind, ApiException e) {
        View outer = (View) this.mStarsView.get();
        if (outer != null) {
            outer.showNoResultPanel(kind, e);
        }
    }

    private void onPost(Runnable r) {
        if (this.mHandler != null) {
            this.mHandler.post(r);
        }
    }

    private void getDetails() {
        this.mTasksRepository.getDetails(new IStarDetailCallback() {
            public void onSuccess(final Star star, String arg1) {
                if (!StarsPresenter.this.mIsDestroy) {
                    if (star != null) {
                        LogUtils.d(StarsPresenter.TAG, "getDetails() -> star != null");
                        StarsPresenter.this.isStarData = true;
                    }
                    StarsPresenter.this.onPost(new Runnable() {
                        public void run() {
                            View outer = (View) StarsPresenter.this.mStarsView.get();
                            if (outer != null) {
                                LogUtils.d(StarsPresenter.TAG, "getDetails() -> setDetails:" + star);
                                outer.setDetails(star);
                            }
                        }
                    });
                }
            }

            public void onFail(ApiException e) {
                StarsPresenter.this.isStarData = false;
                LogUtils.e(StarsPresenter.TAG, "getDetails() -> onFail() e:" + e);
            }
        });
    }

    private void sendShowPingback() {
        if (this.mPageShowModel == null) {
            this.mPageShowModel = new PageShowModel();
        }
        this.mPageShowModel.consumedTime = this.mConsumedTime;
        this.mPageShowModel.infoModel = this.mStarsInfoModel;
        StarsPingbackUtil.sendPageShow(this.mPageShowModel);
    }
}
