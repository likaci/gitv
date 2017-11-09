package com.gala.video.app.player;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView.ScaleType;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore.ALBUMLIST;
import com.gala.pingback.PingbackStore.AREA;
import com.gala.pingback.PingbackStore.BKT;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.CID;
import com.gala.pingback.PingbackStore.E;
import com.gala.pingback.PingbackStore.EC;
import com.gala.pingback.PingbackStore.EVENTID;
import com.gala.pingback.PingbackStore.HCDN;
import com.gala.pingback.PingbackStore.ISPLAYERSTART;
import com.gala.pingback.PingbackStore.LOCALTIME;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PFEC;
import com.gala.pingback.PingbackStore.PPUID;
import com.gala.pingback.PingbackStore.R;
import com.gala.pingback.PingbackStore.RANK;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.pingback.PingbackStore.ST;
import com.gala.pingback.PingbackStore.TAID;
import com.gala.pingback.PingbackStore.TCID;
import com.gala.pingback.PingbackStore.TD;
import com.gala.pingback.PingbackStore.TYPE;
import com.gala.pingback.PingbackStore.USRACT;
import com.gala.sdk.player.IGalaVideoPlayer;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnPlayerStateChangedListener;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.ScreenMode;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.WindowZoomRatio;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoItemFactory;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.DailyLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.multiscreen.MultiEventHelper;
import com.gala.video.app.player.provider.GalaPlayerPageProvider;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.app.player.ui.widget.listview.BaseDetailListAdapter;
import com.gala.video.app.player.ui.widget.listview.DetailListView;
import com.gala.video.app.player.ui.widget.listview.DetailListView.OnUserScrollListener;
import com.gala.video.app.player.ui.widget.listview.NewsListViewAdapter;
import com.gala.video.app.player.ui.widget.tabhost.MyTabHost;
import com.gala.video.app.player.ui.widget.tabhost.MyTabHost.TabChangeListener;
import com.gala.video.app.player.ui.widget.tabhost.MyTabWidget;
import com.gala.video.app.player.ui.widget.tabhost.MyTabWidget.TabFocusListener;
import com.gala.video.app.player.ui.widget.tabhost.VerticalTabHost;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.common.model.player.NewsParams;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IMultiEventHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.ErrorUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.pingback.PingbackContext;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.widget.util.AnimationUtils;
import com.gala.video.widget.util.HomeMonitorHelper;
import com.gala.video.widget.util.HomeMonitorHelper.OnHomePressedListener;
import com.tvos.appdetailpage.client.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import pl.droidsonroids.gif.GifImageView;

public class NewsDetailActivity extends QMultiScreenActivity implements TabChangeListener, TabFocusListener, IPingbackContext {
    private static final String KEY_BUNDLE = "KEY_BUNDLE";
    private static final int LIST_MAX_COUNT = 6;
    private static final int MAX_TAB = 6;
    private static final int MSG_SUCCESS = 10;
    private static final String PAGE_STATE_DETAIL_ERROR = "detailError";
    private static final String PAGE_STATE_PLAYER_ADPLAYING = "playerAdPlayling";
    private static final String PAGE_STATE_PLAYER_ERROR = "playerError";
    private static final String PAGE_STATE_PLAYER_LOADING = "playerLoading";
    private static final String PAGE_STATE_PLAYER_START = "playerStart";
    private static final String PAGE_STATE_UNKNOWN = "unknown";
    private static final String TABTAG = "tab";
    private String TAG = "AlbumDetail/NewsDetailActivity";
    private boolean isFinished = false;
    private boolean mActivityPaused;
    private long mCalltime;
    private String mChannelName;
    private TextView mChannelNameTextView;
    private View mContentView;
    private int mCurPlayListIndex;
    private int mCurPlayTabIndex;
    private String mCurPlayTvId;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private String mE = "";
    private String mEc = "";
    private View mFooterView;
    private String mFrom;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NewsDetailActivity.this.TAG, "mHandler what=" + msg.what);
            }
            switch (msg.what) {
                case 10:
                    NewsDetailActivity.this.setPlayList(msg.obj, msg.arg1);
                    return;
                default:
                    return;
            }
        }
    };
    private String mHcdn;
    private HomeMonitorHelper mHomeMonitorHelper;
    private AtomicInteger mIdIndicator = new AtomicInteger(5592405);
    private boolean mIsError = false;
    private boolean mIsFirstPlay = true;
    private boolean mIsFirstPlayStarted;
    private boolean mIsRegisterHomeMonitor = false;
    private boolean mIsWindowFocused;
    private long mLastKayTime = 0;
    private long mMarkTimeToken;
    private IMultiEventHelper mMultiEventHelper = null;
    private final OnNetStateChangedListener mNetStateChangedListener = new OnNetStateChangedListener() {
        public void onStateChanged(int oldState, int newState) {
            switch (newState) {
                case 1:
                case 2:
                    LogRecordUtils.logd(NewsDetailActivity.this.TAG, "onNetworkState- change -state" + oldState + " >>> newState " + newState);
                    if (oldState != newState && NewsDetailActivity.this.mVideoPlayer == null) {
                        NewsDetailActivity.this.restartPlayer();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    };
    private ArrayDeque<Integer> mNextPageRequestDeque = new ArrayDeque();
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            int currentTab = NewsDetailActivity.this.mTabHost.getCurrentTab();
            if (LogUtils.mIsDebug) {
                LogUtils.d(NewsDetailActivity.this.TAG, "onItemClick position = " + position + ", currentTab = " + currentTab + ", mCurrentPlayTabIndex = " + NewsDetailActivity.this.mCurPlayTabIndex);
            }
            NewsDetailActivity.this.mIsFirstPlay = false;
            NewsDetailActivity.this.sendPageClickPingback(position);
            NewsDetailActivity.this.sendDailyInfoClickPingback(((TabDataItem) NewsDetailActivity.this.mTabDatas.get(currentTab)).getAlbumList(), ((TabDataItem) NewsDetailActivity.this.mTabDatas.get(currentTab)).getDailyLabel().channelId, position);
            if (currentTab == NewsDetailActivity.this.mCurPlayTabIndex) {
                NewsDetailActivity.this.mCurPlayListIndex = position;
                ((TabView) NewsDetailActivity.this.mTabViewsList.get(currentTab)).getListView().setPlayingIndex(position);
                Album album = (Album) ((TabDataItem) NewsDetailActivity.this.mTabDatas.get(currentTab)).getAlbumList().get(position);
                LogUtils.d(NewsDetailActivity.this.TAG, "onItemClick change video album = " + album);
                if (!(StringUtils.isEmpty(NewsDetailActivity.this.mCurPlayTvId) || album == null)) {
                    if (NewsDetailActivity.this.mCurPlayTvId.equals(album.tvQid)) {
                        LogUtils.e(NewsDetailActivity.this.TAG, "the same video go fullscreen");
                        NewsDetailActivity.this.goFullScreen();
                        return;
                    }
                    NewsDetailActivity.this.mCurPlayTvId = album.tvQid;
                }
                if (NewsDetailActivity.this.enableSwitch()) {
                    IVideoItemFactory videoItemFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory();
                    if (NewsDetailActivity.this.mVideoPlayer != null) {
                        NewsDetailActivity.this.mVideoPlayer.switchVideo(videoItemFactory.createVideoItem(NewsDetailActivity.this.mVideoPlayer.getVideo().getSourceType(), album, new MyPlayerProfile()), NewsDetailActivity.this.mFrom);
                    }
                }
            } else {
                ((TabView) NewsDetailActivity.this.mTabViewsList.get(NewsDetailActivity.this.mCurPlayTabIndex)).getListView().setPlayingIndex(-1);
                NewsDetailActivity.this.mCurPlayTabIndex = currentTab;
                NewsDetailActivity.this.mCurPlayListIndex = position;
                NewsDetailActivity.this.updateCurPlayTabIndicator(false);
                ((TabView) NewsDetailActivity.this.mTabViewsList.get(currentTab)).getListView().setPlayingIndex(position);
                NewsDetailActivity.this.changePlaylistByLabel(((TabDataItem) NewsDetailActivity.this.mTabDatas.get(currentTab)).getAlbumList(), ((TabDataItem) NewsDetailActivity.this.mTabDatas.get(currentTab)).getDailyLabel(), position);
            }
            NewsDetailActivity.this.clearErrorState();
        }
    };
    private String mPageState = "unknown";
    private String mPfec = "";
    private final IPingbackContext mPingbackContext = new PingbackContext();
    private boolean mPlayAfterAdjust = false;
    private LayoutParams mPlayerViewLayoutParams;
    private FrameLayout mPlayerViewParent;
    private View mPositionView;
    private int mResultCode;
    private View mRootView;
    private ArrayList<TabDataItem> mTabDatas = new ArrayList(6);
    private VerticalTabHost mTabHost;
    private ArrayList<TabView> mTabViewsList = new ArrayList();
    private TextView mTimeDateView;
    private TextView mTimeWeekView;
    private View mVideoBackView;
    private IGalaVideoPlayer mVideoPlayer;
    private OnPlayerStateChangedListener mVideoStateListener = new OnPlayerStateChangedListener() {
        public void onVideoSwitched(IVideo video, int type) {
            NewsDetailActivity.this.mIsFirstPlay = false;
            if (video != null) {
                NewsDetailActivity.this.clearErrorState();
                NewsDetailActivity.this.mCurPlayTvId = video.getTvId();
                final int playListIndex = DataHelper.findAlbumIndexByTvId(NewsDetailActivity.this.mCurPlayTvId, ((TabDataItem) NewsDetailActivity.this.mTabDatas.get(NewsDetailActivity.this.mCurPlayTabIndex)).getAlbumList());
                NewsDetailActivity.this.mCurPlayListIndex = playListIndex;
                boolean videoHasFocusBeforchange = NewsDetailActivity.this.mPositionView.hasFocus();
                final DetailListView listView = ((TabView) NewsDetailActivity.this.mTabViewsList.get(NewsDetailActivity.this.mCurPlayTabIndex)).getListView();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NewsDetailActivity.this.TAG, "onVideoSwitched mCurPlayTabIndex=" + NewsDetailActivity.this.mCurPlayTabIndex + ", currentTabIndex=" + NewsDetailActivity.this.mTabHost.getCurrentTab() + ", playListIndex=" + playListIndex + ", video = " + video);
                }
                if (NewsDetailActivity.this.mCurPlayTabIndex != NewsDetailActivity.this.mTabHost.getCurrentTab()) {
                    int duration = (int) ((SystemClock.elapsedRealtime() - NewsDetailActivity.this.mLastKayTime) / 1000);
                    LogUtils.d(NewsDetailActivity.this.TAG, "onVideoSwitched duration = " + duration);
                    if (duration > 20) {
                        NewsDetailActivity.this.mTabHost.setCurrentTab(NewsDetailActivity.this.mCurPlayTabIndex);
                        if (videoHasFocusBeforchange) {
                            NewsDetailActivity.this.mPositionView.requestFocus();
                        } else {
                            LogUtils.d(NewsDetailActivity.this.TAG, "mVideoParentLayout.hasFocus false");
                            listView.post(new Runnable() {
                                public void run() {
                                    listView.setPlayingIndex(playListIndex, true);
                                }
                            });
                            return;
                        }
                    }
                }
                listView.post(new Runnable() {
                    public void run() {
                        listView.setPlayingIndex(playListIndex);
                    }
                });
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(NewsDetailActivity.this.TAG, "<<onVideoSwitched null == video");
            }
        }

        public void onPlaybackFinished() {
            LogUtils.d(NewsDetailActivity.this.TAG, "onPlaybackFinished mIsError:" + NewsDetailActivity.this.mIsError);
            if (!NewsDetailActivity.this.mIsError) {
                Album album = (Album) ((TabDataItem) NewsDetailActivity.this.mTabDatas.get(NewsDetailActivity.this.mCurPlayTabIndex)).getAlbumList().get(0);
                LogUtils.d(NewsDetailActivity.this.TAG, "onPlaybackFinished start play from 0 album = " + album);
                if (NewsDetailActivity.this.enableSwitch()) {
                    IVideoItemFactory videoItemFactory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory();
                    if (NewsDetailActivity.this.mVideoPlayer != null) {
                        NewsDetailActivity.this.mVideoPlayer.switchVideo(videoItemFactory.createVideoItem(NewsDetailActivity.this.mVideoPlayer.getVideo().getSourceType(), album, new MyPlayerProfile()), NewsDetailActivity.this.mFrom);
                    }
                }
            }
        }

        public void onScreenModeSwitched(ScreenMode newMode) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NewsDetailActivity.this.TAG, "onScreenModeSwitched newMode = " + newMode);
            }
            NewsDetailActivity.this.mCurScreenMode = newMode;
            if (ScreenMode.WINDOWED == newMode) {
                NewsDetailActivity.this.mContentView.setVisibility(0);
                NewsDetailActivity.this.mTabHost.setCurrentTab(NewsDetailActivity.this.mCurPlayTabIndex);
                NewsDetailActivity.this.mRootView.setBackgroundResource(R.drawable.player_detail_news_bg);
            } else {
                NewsDetailActivity.this.mRootView.setBackgroundResource(0);
                NewsDetailActivity.this.mContentView.setVisibility(4);
            }
            if (NewsDetailActivity.this.mIsWindowFocused) {
                NewsDetailActivity.this.mPositionView.requestFocus();
            } else {
                ((TabView) NewsDetailActivity.this.mTabViewsList.get(NewsDetailActivity.this.mCurPlayTabIndex)).getListView().requestFocus();
            }
        }

        public boolean onError(IVideo video, ISdkError error) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NewsDetailActivity.this.TAG, "onError: error=" + error + ", video = " + video + ",mCurScreenMode=" + NewsDetailActivity.this.mCurScreenMode);
            }
            NewsDetailActivity.this.updatePageState("playerError");
            NewsDetailActivity.this.mEc = "";
            NewsDetailActivity.this.mPfec = ErrorUtils.getPfEc(error);
            NewsDetailActivity.this.mIsError = true;
            NewsDetailActivity.this.setErrorState();
            return false;
        }

        public void onVideoStarted(IVideo video) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NewsDetailActivity.this.TAG, "onVideoStarted");
            }
            NewsDetailActivity.this.updatePageState("playerStart");
            NewsDetailActivity.this.mIsFirstPlay = false;
            NewsDetailActivity.this.loadRestDataForTab(NewsDetailActivity.this.mCurPlayTabIndex);
            NewsDetailActivity.this.updateCurPlayTabIndicator(false);
        }

        public void onAdStarted() {
            NewsDetailActivity.this.updatePageState("playerAdPlayling");
        }

        public void onAdEnd() {
        }

        public void onPrepared() {
        }
    };

    private class FetchResList {
        private int mIndex;
        private DailyLabel mLabel;

        public FetchResList(DailyLabel label, int index) {
            this.mLabel = label;
            this.mIndex = index;
        }

        public void execute() {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(NewsDetailActivity.this.TAG, "execute() run");
                    }
                    List list = new FetchRestListLoader(FetchResList.this.mLabel, FetchResList.this.mIndex).getList();
                    if (!ListUtils.isEmpty(list)) {
                        NewsDetailActivity.this.mHandler.obtainMessage(10, FetchResList.this.mIndex, 0, list).sendToTarget();
                    }
                }
            });
        }
    }

    private class FetchRestListLoader {
        private int mIndex;
        private DailyLabel mLabel;

        public FetchRestListLoader(DailyLabel label, int index) {
            this.mLabel = label;
            this.mIndex = index;
        }

        private List<Album> getList() {
            final List<Album> fetchedList = new ArrayList();
            String isFree = GetInterfaceTools.getIDynamicQDataProvider().isSupportVip() ? "0" : "1";
            String channelId = this.mLabel.channelId;
            CharSequence tagSet = this.mLabel.tagSet;
            LogRecordUtils.logd(NewsDetailActivity.this.TAG, "FetchRestListLoader.getList, mLabel=[" + this.mLabel.name + ", " + this.mLabel.tagSet + ", " + this.mLabel.channelId + ", " + this.mLabel.id + ", " + this.mLabel.desc + AlbumEnterFactory.SIGN_STR);
            if (StringUtils.isEmpty(tagSet)) {
                VrsHelper.channelLabelsSize.callSync(new IVrsCallback<ApiResultChannelLabels>() {
                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(NewsDetailActivity.this.TAG, "fetch channelLabels onException exp code = " + e.getCode() + ", msg = " + e.getMessage());
                        }
                        NewsDetailActivity.this.updatePageState("playerError");
                        NewsDetailActivity.this.mEc = "315008";
                        NewsDetailActivity.this.mPfec = e.getCode();
                    }

                    public void onSuccess(ApiResultChannelLabels result) {
                        if (result != null) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(NewsDetailActivity.this.TAG, "fetchPlayList onSuccess list size = {" + result.data.getChannelLabelList().size() + "}");
                            }
                            List<ChannelLabel> labels = result.data.items;
                            List<Album> albums = new ArrayList();
                            for (ChannelLabel label : labels) {
                                albums.add(label.getVideo());
                            }
                            fetchedList.addAll(albums);
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.d(NewsDetailActivity.this.TAG, "fetch channelLabels onSuccess but null");
                        }
                    }
                }, channelId, isFree, "1.0", "60");
            } else {
                VrsHelper.recommendListQipu.callSync(new IVrsCallback<ApiResultRecommendListQipu>() {
                    public void onSuccess(ApiResultRecommendListQipu result) {
                        if (result != null) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(NewsDetailActivity.this.TAG, "VrsHelper.recommendListQipu.call onSuccess list size = {" + result.getAlbumList().size() + "}");
                            }
                            fetchedList.addAll(result.getAlbumList());
                        } else if (LogUtils.mIsDebug) {
                            LogUtils.d(NewsDetailActivity.this.TAG, "VrsHelper.recommendListQipu.call, onSuccess but null");
                        }
                    }

                    public void onException(ApiException e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(NewsDetailActivity.this.TAG, "VrsHelper.recommendListQipu.call onException exp code = " + e.getCode() + ", msg = " + e.getMessage());
                        }
                        NewsDetailActivity.this.updatePageState("playerError");
                        NewsDetailActivity.this.mEc = "315008";
                        NewsDetailActivity.this.mPfec = e.getCode();
                    }
                }, GetInterfaceTools.getIGalaAccountManager().getUID(), AppRuntimeEnv.get().getDefaultUserId(), "60", "-4", tagSet, isFree);
            }
            return fetchedList;
        }
    }

    interface ListFetchedCallback {
        void onListFetched(List<Album> list);
    }

    class TabView {
        private GifImageView mGifView;
        private int mIndicatorTitleResId;
        private View mIndicatorView;
        private int mLoadingViewId;
        private ProgressBarItem mProgress = ((ProgressBarItem) this.mTabContentView.findViewById(this.mLoadingViewId));
        private DetailListView mTabContentListView;
        private View mTabContentView;
        private String mTag;

        public TabView(String tag, View indicatorView, int indicatorTitleResId, View contenView, int loadingViewId, DetailListView listView, GifImageView gifView) {
            this.mTag = tag;
            this.mIndicatorView = indicatorView;
            this.mIndicatorTitleResId = indicatorTitleResId;
            this.mTabContentView = contenView;
            this.mLoadingViewId = loadingViewId;
            this.mTabContentListView = listView;
            this.mGifView = gifView;
        }

        public void addTabToHost(Context context, MyTabHost host) {
            this.mIndicatorView.setId(NewsDetailActivity.this.mIdIndicator.getAndIncrement());
            this.mTabContentView.setId(NewsDetailActivity.this.mIdIndicator.getAndIncrement());
            host.addNewTab(this.mTag, this.mIndicatorView, this.mTabContentView);
            for (ViewParent parent = ((TextView) this.mIndicatorView.findViewById(this.mIndicatorTitleResId)).getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                ((ViewGroup) parent).setClipChildren(false);
                ((ViewGroup) parent).setClipToPadding(false);
            }
        }

        public void showLoading() {
            this.mProgress.setVisibility(0);
        }

        public void hideLoading() {
            this.mProgress.setVisibility(8);
        }

        public DetailListView getListView() {
            return this.mTabContentListView;
        }

        public GifImageView getGifView() {
            return this.mGifView;
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            if (savedInstanceState != null) {
                getIntent().putExtras(savedInstanceState.getBundle("KEY_BUNDLE"));
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "onCreate :  getIntent().getExtras() = " + getIntent().getExtras());
                }
            } else if (!GalaPlayerPageProvider.restoreIntentExtras(getIntent())) {
                return;
            }
        }
        this.TAG += "@" + Integer.toHexString(hashCode());
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "[PERF-LOADING]tm_activity.create");
        }
        String eventId = getIntent().getStringExtra("eventId");
        GlobalPerformanceTracker.instance().recordPerformanceStepEnd(eventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(eventId, GlobalPerformanceTracker.PLAYER_PREF_INIT_STEP);
        sendInitPingback();
        registerHomeKeyForLauncher();
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(R.style.AppTheme);
            LogUtils.d(this.TAG, "onCreate: setTheme for home version");
        }
        setContentView(PlayerAppConfig.getNewsDetailLayoutId());
        this.mRootView = getWindow().getDecorView();
        this.mFrom = getIntent().getStringExtra("from");
        this.mChannelName = getIntent().getStringExtra("channelName");
        LogUtils.e(this.TAG, "onCreate mChannelName=" + this.mChannelName + ", mFrom=" + this.mFrom);
        initViews();
        this.mMarkTimeToken = SystemClock.elapsedRealtime();
        PingBackParams params1 = new PingBackParams();
        params1.add(Keys.T, "11").add("r", Values.value00001).add("s1", "0").add("ct", "150413_gettablist").add(Keys.CLIENTTM, getCurrentFormatTime());
        PingBack.getInstance().postPingBackToLongYuan(params1.build());
        NewsParams params = (NewsParams) getIntent().getExtras().getSerializable(IntentConfig2.INTENT_PARAM_NEWS);
        if (params != null && !ListUtils.isEmpty(params.getTabLabelList())) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onCreate, have params, and tab label list passed in.");
            }
            this.mTabDatas = DataHelper.getNewTabItemInstance(params.getTabLabelList());
            int listItemIndex = params.getPlaytingItemIndex();
            if (listItemIndex >= 0 && listItemIndex < 6) {
                this.mCurPlayListIndex = params.getPlaytingItemIndex();
            }
            this.mTabHost.setCurrentTab(0);
            List albums = ((TabDataItem) params.getTabLabelList().get(this.mCurPlayTabIndex)).getAlbumList();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onCreate playList:" + albums);
            }
            if (!ListUtils.isEmpty(albums)) {
                showContent(true);
                fillTabViews();
                for (int i = 0; i < this.mTabDatas.size(); i++) {
                    if (i != this.mCurPlayTabIndex) {
                        fillTabListView(i, false);
                    } else if (ListUtils.isEmpty(albums)) {
                        onTabSelected(this.mCurPlayTabIndex, 0, true);
                    } else {
                        int position;
                        if (albums.size() < 6) {
                            position = albums.size();
                        } else {
                            position = 5;
                        }
                        fillTabListView(this.mCurPlayTabIndex, true);
                        sendDailyInfoShowPingback(albums, this.mCurPlayTabIndex, position);
                    }
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "onCreate, current tab has no albums, mCurPlayTabIndex=" + this.mCurPlayTabIndex);
            }
        }
    }

    private String getCurrentFormatTime() {
        String clientTime = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.US).format(new Date());
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "getCurrentFormatTime ret=" + clientTime);
        }
        return clientTime;
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("KEY_BUNDLE", getIntent().getExtras());
    }

    protected void onStart() {
        super.onStart();
        NetWorkManager.getInstance().registerStateChangedListener(this.mNetStateChangedListener);
    }

    protected void onResume() {
        super.onResume();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onResume");
        }
        if (this.mActivityPaused) {
            getIntent().putExtra(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, SystemClock.elapsedRealtime());
            this.mActivityPaused = false;
            if (this.mVideoPlayer == null) {
                return;
            }
            if (!this.mVideoPlayer.isSleeping() || this.mResultCode == 1 || this.mResultCode == 22) {
                this.mVideoPlayer.release();
                restartPlayer();
                return;
            }
            this.mVideoPlayer.wakeUp();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.mResultCode = resultCode;
    }

    protected void onStop() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onStop");
        }
        super.onStop();
        NetWorkManager.getInstance().unRegisterStateChangedListener(this.mNetStateChangedListener);
    }

    private void initViews() {
        this.mRootView.setBackgroundResource(R.drawable.player_detail_news_bg);
        this.mPlayerViewParent = (FrameLayout) findViewById(R.id.fl_player_view_parent_news);
        this.mContentView = findViewById(R.id.detail_content);
        this.mVideoBackView = findViewById(R.id.view_videoview_back);
        this.mPositionView = findViewById(R.id.news_playwindow_position);
        this.mPositionView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NewsDetailActivity.this.TAG, "onLayoutChange");
                }
                NewsDetailActivity.this.mPositionView.removeOnLayoutChangeListener(this);
                NewsDetailActivity.this.updatePlaywindowParams();
                if (NewsDetailActivity.this.mPlayAfterAdjust) {
                    NewsDetailActivity.this.onListDataReady();
                }
            }
        });
        this.mPositionView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                NewsDetailActivity.this.goFullScreen();
            }
        });
        ((ProgressBarItem) findViewById(R.id.detail_loading_list)).setText(getString(R.string.album_list_loading));
        this.mTimeDateView = (TextView) findViewById(R.id.tv_detail_news_date);
        this.mTimeWeekView = (TextView) findViewById(R.id.tv_detail_news_week);
        this.mChannelNameTextView = (TextView) findViewById(R.id.txt_title);
        if (!TextUtils.isEmpty(this.mChannelName)) {
            this.mChannelNameTextView.setText(this.mChannelName);
        }
        initFooterView();
        initTabhost();
        adjustViews();
    }

    private void goFullScreen() {
        LogUtils.d(this.TAG, "goFullScreen");
        if (this.mIsError) {
            LogUtils.d(this.TAG, "startPlayerForFullScreen return mIsError");
            if (this.mVideoPlayer != null) {
                this.mVideoPlayer.onErrorClicked();
                return;
            }
            return;
        }
        this.mIsWindowFocused = this.mPositionView.hasFocus();
        if (this.mVideoPlayer != null) {
            this.mVideoPlayer.changeScreenMode(ScreenMode.FULLSCREEN);
        }
        this.mContentView.setVisibility(8);
        this.mCurScreenMode = ScreenMode.FULLSCREEN;
    }

    private void changePlaylistByLabel(List<Album> list, DailyLabel label, int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "changePlaylistByLabel: index=" + index + "list={ " + list + " }");
            if (label != null) {
                LogUtils.d(this.TAG, "changePlaylistByLabel label id= " + label.id + ", label name=" + label.name);
            }
        }
        if (this.mVideoPlayer != null) {
            PlayParams params = new PlayParams();
            params.sourceType = SourceType.DAILY_NEWS;
            params.continuePlayList = new ArrayList(list);
            params.playIndex = index;
            params.from = this.mFrom;
            if (label != null) {
                params.playListId = label.channelId;
                params.playListName = label.name;
            }
            if (!ListUtils.isEmpty((List) list) && index < list.size() && index >= 0) {
                Album album = (Album) list.get(index);
                if (album != null) {
                    this.mCurPlayTvId = album.tvQid;
                    LogUtils.d(this.TAG, "changePlaylistByLabel  mCurPlayTvId =" + this.mCurPlayTvId);
                }
            }
            if (this.mVideoPlayer != null) {
                this.mVideoPlayer.switchPlaylist(params);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "changePlaylistByLabel: null");
        }
    }

    private boolean enableSwitch() {
        boolean should = true;
        if (this.mVideoPlayer == null || this.mVideoPlayer.getVideo() == null || this.mVideoPlayer.getVideo().getProvider() == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "no provider!");
            }
            should = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "shouldSwitch ret=" + should);
        }
        return should;
    }

    private void adjustViews() {
        Rect bp = UiHelper.getBgDrawablePaddings(this.mPositionView.getBackground());
        MarginLayoutParams params = (MarginLayoutParams) this.mPositionView.getLayoutParams();
        LogUtils.d(this.TAG, "adjustViews before w/h/lm/tm=" + params.width + "," + params.height + "," + params.leftMargin + "," + params.topMargin);
        params.width += bp.left + bp.right;
        params.height += bp.top + bp.bottom;
        params.leftMargin -= bp.left;
        params.topMargin -= bp.top;
        this.mPositionView.setLayoutParams(params);
        LogUtils.d(this.TAG, "adjustViews l/r/t/b=" + bp.left + "," + bp.right + "," + bp.top + "," + bp.bottom);
        LogUtils.d(this.TAG, "adjustViews after w/h/lm/tm=" + params.width + "," + params.height + "," + params.leftMargin + "," + params.topMargin);
    }

    private void updatePlaywindowParams() {
        Rect bgPadding = UiHelper.getBgDrawablePaddings(this.mPositionView.getBackground());
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) this.mPositionView.getLayoutParams();
        this.mPlayerViewLayoutParams = new LayoutParams(params.width, params.height);
        this.mPlayerViewLayoutParams.width = (params.width - bgPadding.left) - bgPadding.right;
        this.mPlayerViewLayoutParams.height = (params.height - bgPadding.top) - bgPadding.bottom;
        int[] location = new int[2];
        this.mPositionView.getLocationInWindow(location);
        LogUtils.d(this.TAG, "updatePlaywindowParams location[0]=" + location[0] + ", location[1]=" + location[1]);
        LogUtils.d(this.TAG, "updatePlaywindowParams lbgPadding.left=" + bgPadding.left + ", bgPadding.top=" + bgPadding.top);
        this.mPlayerViewLayoutParams.leftMargin = location[0] + bgPadding.left;
        this.mPlayerViewLayoutParams.topMargin = location[1] + bgPadding.top;
    }

    private void showContent(boolean show) {
        int i = 4;
        LogUtils.d(this.TAG, "showContent: " + show);
        View contentView = findViewById(R.id.detail_content);
        if (show && contentView.getVisibility() != 0) {
            sendLoadPagePingback();
        }
        contentView.setVisibility(show ? 0 : 4);
        View findViewById = findViewById(R.id.detail_loading_list);
        if (!show) {
            i = 0;
        }
        findViewById.setVisibility(i);
        if (show) {
            this.mTimeDateView.setVisibility(0);
            this.mTimeWeekView.setVisibility(0);
            String[] time = AlbumTextHelper.getDateAndWeekForNews();
            this.mTimeDateView.setText(time[0]);
            this.mTimeWeekView.setText(time[1]);
            return;
        }
        this.mTimeDateView.setVisibility(8);
        this.mTimeWeekView.setVisibility(8);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogUtils.d(this.TAG, "onKeyDown");
        this.mLastKayTime = SystemClock.elapsedRealtime();
        return super.onKeyDown(keyCode, event);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleKeyEvent" + event);
        }
        if (this.mVideoPlayer != null && this.mVideoPlayer.handleKeyEvent(event)) {
            return true;
        }
        if (event.getKeyCode() == 82) {
            ScreenMode screenMode = this.mCurScreenMode;
            ScreenMode screenMode2 = this.mCurScreenMode;
            if (screenMode == ScreenMode.WINDOWED) {
                return true;
            }
        }
        try {
            return super.handleKeyEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    protected void onPause() {
        super.onPause();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onPause()");
        }
        if (isFinishing()) {
            if (this.mVideoPlayer != null) {
                this.mVideoPlayer.release();
            }
            this.mVideoPlayer = null;
            PingBackParams params1 = new PingBackParams();
            params1.add(Keys.T, "11").add("r", Values.value00001).add("s1", "0").add("ct", "150413_exit").add(Keys.CLIENTTM, getCurrentFormatTime()).add("rpage", "news");
            PingBack.getInstance().postPingBackToLongYuan(params1.build());
        } else if (this.mVideoPlayer != null) {
            this.mVideoPlayer.sleep();
        }
        this.mActivityPaused = true;
    }

    protected void onDestroy() {
        super.onDestroy();
        sendExitPingback();
        this.isFinished = true;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onDestroy()");
        }
        if (this.mVideoPlayer != null) {
            this.mVideoPlayer.release();
        }
        synchronized (this) {
            if (this.mHomeMonitorHelper != null) {
                this.mHomeMonitorHelper.onDestory();
            }
            this.mIsRegisterHomeMonitor = false;
        }
    }

    public String onActionNotifyEvent(RequestKind kind, String message) {
        LogUtils.i(this.TAG, "onActionNotifyEvent" + kind);
        if (kind == RequestKind.PULLVIDEO) {
            pullVideo();
        }
        return null;
    }

    private void pullVideo() {
        LogUtils.d(this.TAG, ">>pullVideo()");
        if ((this.mPositionView == null || !this.mPositionView.hasFocus()) && ScreenMode.FULLSCREEN != this.mCurScreenMode) {
            int curTabIndex = this.mTabHost.getCurrentTab();
            if (curTabIndex < 0 || curTabIndex >= this.mTabDatas.size()) {
                LogUtils.e(this.TAG, "pullVideo() invalid curTabIndex:" + curTabIndex);
                return;
            }
            LogUtils.d(this.TAG, "pullVideo() listView.pullVideo()");
            pullFocusedVideo(((TabView) this.mTabViewsList.get(curTabIndex)).getListView(), ((TabDataItem) this.mTabDatas.get(curTabIndex)).getAlbumList());
        }
    }

    private void pullFocusedVideo(DetailListView listView, List<Album> list) {
    }

    private void onTabSelected(int tabIndex, int playListIndex, boolean needGoPlay) {
        LogUtils.d(this.TAG, "onTabSelected tabIndex=" + tabIndex + ", playListIndex=" + playListIndex + ", needGoPlay=" + needGoPlay);
        if (!ListUtils.isEmpty(this.mTabViewsList) && !ListUtils.isEmpty(this.mTabDatas) && this.mTabViewsList.size() == this.mTabDatas.size() && tabIndex < this.mTabDatas.size() && tabIndex < this.mTabViewsList.size()) {
            TabDataItem tabItem = (TabDataItem) this.mTabDatas.get(tabIndex);
            int position = -1;
            if (tabIndex == 0) {
                if (((TabDataItem) this.mTabDatas.get(tabIndex)).getAlbumList().size() < 6) {
                    position = ((TabDataItem) this.mTabDatas.get(tabIndex)).getAlbumList().size();
                } else {
                    position = ((TabView) this.mTabViewsList.get(tabIndex)).getListView().getLastVisiblePosition() - 1;
                }
            }
            LogUtils.d(this.TAG, "onTabSelected position=" + position);
            sendDailyInfoShowPingback(tabItem.getAlbumList(), tabIndex, position);
        }
    }

    private void fillTabListView(final int tabIndex, final boolean needGoPlay) {
        if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, ">>fillTabListView tabIndex" + tabIndex);
        }
        final TabDataItem tabItem = (TabDataItem) this.mTabDatas.get(tabIndex);
        runOnUiThread(new Runnable() {
            public void run() {
                BaseDetailListAdapter adapter = new NewsListViewAdapter(new ArrayList(tabItem.getAlbumList()), NewsDetailActivity.this);
                DetailListView listView = ((TabView) NewsDetailActivity.this.mTabViewsList.get(tabIndex)).getListView();
                if (tabItem.getAlbumList().size() > 6) {
                    listView.addFooterView(NewsDetailActivity.this.mFooterView, null, false);
                }
                listView.setDetailListViewAdapter(adapter);
                if (needGoPlay) {
                    NewsDetailActivity.this.showContent(true);
                    NewsDetailActivity.this.onListDataReady();
                }
                ((TabView) NewsDetailActivity.this.mTabViewsList.get(tabIndex)).hideLoading();
            }
        });
    }

    private void initFooterView() {
        this.mFooterView = LayoutInflater.from(this).inflate(R.layout.player_view_news_footer, null);
    }

    private void onListDataReady() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">>onPlayDataReady");
        }
        if (this.mPlayerViewLayoutParams == null) {
            this.mPlayAfterAdjust = true;
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "<<onPlayDataReady null == mPlayerViewLayoutParams");
                return;
            }
            return;
        }
        updateCurPlayTabIndicator(false);
        this.mTabHost.setCurrentTab(this.mCurPlayTabIndex);
        showContent(true);
        this.mIsError = false;
        ((TabView) this.mTabViewsList.get(this.mCurPlayTabIndex)).getListView().setPlayingIndex(this.mCurPlayListIndex);
        ((TabView) this.mTabViewsList.get(this.mCurPlayTabIndex)).getListView().requestFocus();
        goPlay();
    }

    private void goPlay() {
        if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, ">> goPlay");
        }
        if (!this.mActivityPaused) {
            clearErrorState();
            TabDataItem currentItem = (TabDataItem) this.mTabDatas.get(this.mCurPlayTabIndex);
            List list = currentItem.getAlbumList();
            if (ListUtils.isEmpty(list)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(this.TAG, "<< goPlay list is invalid");
                }
                updatePageState("detailError");
                this.mEc = "";
                this.mPfec = "playLis invalid";
                return;
            }
            if (this.mCurPlayListIndex < 0 && this.mCurPlayListIndex >= list.size()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(this.TAG, "index is invalid change to 0");
                }
                this.mCurPlayListIndex = 0;
            }
            sendDailyInfoClickPingback(currentItem.getAlbumList(), currentItem.getDailyLabel().channelId, this.mCurPlayListIndex);
            updatePageState("playerLoading");
            Bundle bundle = getIntent().getExtras();
            bundle.putSerializable("videoType", SourceType.DAILY_NEWS);
            PlayParams params = new PlayParams();
            params.continuePlayList = list;
            params.playIndex = this.mCurPlayListIndex;
            params.playListId = currentItem.getDailyLabel().channelId;
            params.playListName = currentItem.getDailyLabel().name;
            bundle.putSerializable("play_list_info", params);
            Album album = (Album) list.get(this.mCurPlayListIndex);
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "goPlay album=" + DataUtils.albumInfoToString(album));
            }
            this.mCurPlayTvId = album.tvQid;
            bundle.putSerializable("albumInfo", album);
            bundle.putString(PlayerIntentConfig2.INTENT_PARAM_PAGENAME, "news");
            bundle.putInt(PlayerIntentConfig2.INTENT_PARAM_RESULT_CODE, this.mResultCode);
            this.mMultiEventHelper = new MultiEventHelper();
            this.mVideoPlayer = GetInterfaceTools.getGalaVideoPlayerGenerator().createVideoPlayer(this, this.mPlayerViewParent, bundle, this.mVideoStateListener, ScreenMode.WINDOWED, this.mPlayerViewLayoutParams, new WindowZoomRatio(true, WindowZoomRatio.WINDOW_ZOOM_RATIO_4_BY_3_BIG), this.mMultiEventHelper, null);
            this.mVideoStateListener.onScreenModeSwitched(ScreenMode.WINDOWED);
            if (LogUtils.mIsDebug) {
                LogUtils.e(this.TAG, "<< goPlay");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "<< goPlay return mActivityPaused");
        }
    }

    private void restartPlayer() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">>restartPlayer, mCurPlayTabIndex=" + this.mTabHost + this.mCurPlayTabIndex + ", mCurScreenMode=" + this.mCurScreenMode);
        }
        if (this.mTabHost != null) {
            this.mTabHost.setCurrentTab(this.mCurPlayTabIndex);
            this.mIsError = false;
            goPlay();
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, ">>restartPlayer error, mCurPlayTabIndex=" + this.mTabHost + this.mCurPlayTabIndex + ", mCurScreenMode=" + this.mCurScreenMode);
        }
    }

    private void setErrorState() {
        LogUtils.d(this.TAG, "setErrorState");
        runOnUiThread(new Runnable() {
            public void run() {
                NewsDetailActivity.this.mCurPlayTvId = null;
                NewsDetailActivity.this.updateCurPlayTabIndicator(true);
            }
        });
    }

    private void clearErrorState() {
        LogUtils.d(this.TAG, "clearErrorState");
        runOnUiThread(new Runnable() {
            public void run() {
                NewsDetailActivity.this.mIsError = false;
                NewsDetailActivity.this.mVideoBackView.setBackgroundResource(0);
            }
        });
    }

    private void fillTabViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">>fillTabViews mTabsList.size()" + this.mTabDatas.size());
        }
        this.mTabViewsList.clear();
        int length = this.mTabDatas.size();
        for (int i = 0; i < length; i++) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "newTabSpec");
            }
            final int index = i;
            FrameLayout indicatorView = (FrameLayout) LayoutInflater.from(this).inflate(R.layout.player_news_detail_tab_indicator, null);
            indicatorView.setTag("tab" + i);
            indicatorView.setBackgroundResource(R.drawable.player_news_tab_item_bg_normal);
            ((TextView) indicatorView.findViewById(R.id.txt_indicator)).setText(((TabDataItem) this.mTabDatas.get(i)).getDailyLabel().name);
            GifImageView playingGifView = new GifImageView(this);
            LayoutParams params = new LayoutParams(getResources().getDimensionPixelSize(R.dimen.dimen_25dp), getResources().getDimensionPixelSize(R.dimen.dimen_25dp));
            params.gravity = 85;
            indicatorView.addView(playingGifView, params);
            playingGifView.setFocusable(false);
            playingGifView.setScaleType(ScaleType.FIT_XY);
            playingGifView.setVisibility(8);
            View tabContentView = LayoutInflater.from(this).inflate(R.layout.player_news_detail_tab_content, null);
            DetailListView listView = (DetailListView) tabContentView.findViewById(R.id.tab_content_listview);
            listView.setId(this.mIdIndicator.getAndIncrement());
            initListView(listView);
            listView.setOnUserScrollListener(new OnUserScrollListener() {
                public void onLastItemVisibile() {
                    LogUtils.d(NewsDetailActivity.this.TAG, "onLastItemVisibile" + NewsDetailActivity.this.mTabHost.getCurrentTab());
                    NewsDetailActivity.this.loadRestDataForTab(NewsDetailActivity.this.mTabHost.getCurrentTab());
                }

                public void onScrollStop(int position, int total) {
                    LogUtils.d(NewsDetailActivity.this.TAG, "onScrollStop position=" + position + ", total=" + total + ", index=" + index);
                    if (index == 0 && !ListUtils.isEmpty(NewsDetailActivity.this.mTabDatas)) {
                        NewsDetailActivity.this.sendDailyInfoShowPingback(((TabDataItem) NewsDetailActivity.this.mTabDatas.get(index)).getAlbumList(), index, position);
                    }
                }
            });
            TabView tabs = new TabView("tab" + i, indicatorView, R.id.txt_indicator, tabContentView, R.id.loading_list, listView, playingGifView);
            tabs.addTabToHost(this, this.mTabHost);
            this.mTabViewsList.add(tabs);
        }
        updateTabFocusPath();
    }

    private void initListView(DetailListView listView) {
        listView.setNextFocusLeftId(R.id.news_playwindow_position);
        listView.setUI(new int[]{R.dimen.dimen_84dp, R.dimen.dimen_0dp, R.drawable.share_btn_focus});
        listView.setMaxVisibleCount(6);
        listView.setOnItemClickListener(this.mOnItemClickListener);
    }

    private void sendPageClickPingback(int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">>sendPageClickPingback pos=" + position);
        }
        int currentTab = this.mTabHost.getCurrentTab();
        List list = ((TabDataItem) this.mTabDatas.get(currentTab)).getAlbumList();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendPageClickPingback currentTab=" + currentTab);
        }
        if (!ListUtils.isEmpty(list) && position < list.size()) {
            String rId;
            Album clickedAlbum = (Album) list.get(position);
            if (clickedAlbum.getType() == AlbumType.ALBUM) {
                rId = clickedAlbum.qpId;
            } else {
                rId = clickedAlbum.tvQid;
            }
            String block = "list_" + ((TabDataItem) this.mTabDatas.get(currentTab)).getDailyLabel().name;
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "<<sendPageClickPingback rId=" + rId + ", block" + block);
            }
            PingbackFactory.instance().createPingback(33).addItem(R.R_TYPE(rId)).addItem(BLOCK.BLOCK_TYPE(block)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(position + 1))).addItem(this.mPingbackContext.getItem("rpage")).addItem(C1.C1_TYPE(String.valueOf(clickedAlbum.chnId))).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<<sendPageClickPingback invalid pos");
        }
    }

    private void initTabhost() {
        this.mTabHost = (VerticalTabHost) findViewById(R.id.tab_news);
        this.mTabHost.clearAllTabs();
        this.mTabHost.setTabChangeListener(this);
        this.mTabHost.setTabFocusListener(this);
        MyTabWidget tabWidget = (MyTabWidget) this.mTabHost.getTabWidget();
        tabWidget.setOrientation(1);
        this.mTabHost.getTabContentView().setBackgroundResource(R.drawable.player_detail_tabcontent_bg);
        tabWidget.setBackgroundColor(getResources().getColor(R.color.transparent));
        this.mTabHost.setTabWidgetWidth(getResources().getDimensionPixelSize(R.dimen.dimen_97dp));
        this.mTabHost.setTabWidgetHeight(getResources().getDimensionPixelSize(R.dimen.dimen_512dp));
        this.mTabHost.setTabContentWidth(getResources().getDimensionPixelSize(R.dimen.dimen_437dp));
    }

    private void updateCurPlayTabIndicator(boolean clear) {
        LogUtils.d(this.TAG, "updateCurPlayTabIndicator" + this.mCurPlayTabIndex);
        int i = 0;
        int size = this.mTabHost.getIndicatorCount();
        while (i < size) {
            if (this.mTabViewsList.isEmpty() || i >= this.mTabViewsList.size()) {
                LogUtils.d(this.TAG, "updateCurPlayTabIndicator mTabViewsList has correct index");
                return;
            }
            GifImageView gifView = ((TabView) this.mTabViewsList.get(i)).getGifView();
            if (gifView != null) {
                if (this.mCurPlayTabIndex != i || clear) {
                    gifView.setImageResource(0);
                    gifView.setVisibility(8);
                } else {
                    gifView.setImageResource(R.drawable.player_detail_news_playing_icon);
                    gifView.setVisibility(0);
                    clearErrorState();
                }
                i++;
            } else {
                return;
            }
        }
    }

    public void onTabFocusChange(View tabView, boolean hasFocus) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onTabFocusChange: tab tag=" + tabView.getTag() + ", hasFocus=" + hasFocus);
        }
        int colorSelected = getResources().getColor(R.color.news_detail_tab_indicator_selected);
        int colorDefault = getResources().getColor(R.color.news_detail_tab_indicator_default);
        int colorFocused = getResources().getColor(R.color.news_detail_tab_indicator_focused);
        if (hasFocus) {
            tabView.setBackgroundResource(R.drawable.share_btn_bg_focused);
            AnimationUtils.zoomIn(tabView);
        } else {
            View curTab = this.mTabHost.getCurrentIndicator();
            int tabCount = this.mTabHost.getIndicatorCount();
            for (int i = 0; i < tabCount; i++) {
                View tab = this.mTabHost.getIndicatorAt(i);
                if (tab.equals(curTab)) {
                    tab.setBackgroundResource(R.drawable.player_news_item_selected);
                } else {
                    tab.setBackgroundResource(R.drawable.player_news_tab_item_bg_normal);
                }
            }
            AnimationUtils.zoomOut(tabView);
        }
        this.mTabHost.getTabWidget().invalidate();
        TextView tabText = (TextView) tabView.findViewById(R.id.txt_indicator);
        int colorToSet = hasFocus ? colorFocused : this.mTabHost.getCurrentIndicator() == tabView ? colorSelected : colorDefault;
        tabText.setTextColor(colorToSet);
    }

    public void onTabChanged(String tag) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onTabChanged: " + tag);
        }
        View curTab = this.mTabHost.getCurrentIndicator();
        int tabCount = this.mTabHost.getIndicatorCount();
        int colorSelected = getResources().getColor(R.color.news_detail_tab_indicator_selected);
        int colorDefault = getResources().getColor(R.color.news_detail_tab_indicator_default);
        for (int i = 0; i < tabCount; i++) {
            int i2;
            View tab = this.mTabHost.getIndicatorAt(i);
            boolean isCurTab = tab.equals(curTab);
            TextView textView = (TextView) tab.findViewById(R.id.txt_indicator);
            if (isCurTab) {
                i2 = colorSelected;
            } else {
                i2 = colorDefault;
            }
            textView.setTextColor(i2);
            if (!isCurTab) {
                tab.setBackgroundResource(R.drawable.player_news_tab_item_bg_normal);
            } else if (tab.hasFocus()) {
                tab.setBackgroundResource(R.drawable.share_btn_bg_focused);
            } else {
                tab.setBackgroundResource(R.drawable.player_news_item_selected);
            }
        }
        updateTabFocusPath();
        onTabSelected(this.mTabHost.getCurrentTab(), 0, false);
    }

    public void onTabCountChanged(int newCount) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> onTabCountChanged() newCount = " + newCount);
        }
    }

    private void updateTabFocusPath() {
        LogUtils.d(this.TAG, ">> updateTabFocusPath()");
        if (!ListUtils.isEmpty(this.mTabViewsList) && this.mTabViewsList.size() == this.mTabDatas.size()) {
            View curTabWidgetChildView = this.mTabHost.getCurrentTabView();
            LogUtils.d(this.TAG, "updateTabFocusPath curTabWidgetChildView" + curTabWidgetChildView.getId());
            int currentTabIndex = this.mTabHost.getCurrentTab();
            LogUtils.d(this.TAG, "updateTabFocusPath currentTabIndex" + currentTabIndex);
            DetailListView listView = ((TabView) this.mTabViewsList.get(currentTabIndex)).getListView();
            curTabWidgetChildView.setNextFocusLeftId(listView.getId());
            listView.setNextFocusRightId(curTabWidgetChildView.getId());
            listView.setNextFocusDownId(listView.getId());
            listView.setNextFocusUpId(listView.getId());
            listView.setNextFocusLeftId(R.id.news_playwindow_position);
            this.mPositionView.setNextFocusRightId(listView.getId());
        }
    }

    private void loadRestDataForTab(int tabIndex) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">>loadRestDataForTab index=" + tabIndex);
        }
        if (!this.mNextPageRequestDeque.contains(Integer.valueOf(tabIndex))) {
            this.mNextPageRequestDeque.add(Integer.valueOf(tabIndex));
            new FetchResList(((TabDataItem) this.mTabDatas.get(tabIndex)).getDailyLabel(), tabIndex).execute();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "already loaded");
        }
    }

    private void setPlayList(List<Album> list, int tabIndex) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setPlayList() tabIndex=" + tabIndex);
        }
        if (!ListUtils.isEmpty((List) list)) {
            BaseDetailListAdapter baseDetailListAdapter;
            DetailListView listView = ((TabView) this.mTabViewsList.get(tabIndex)).getListView();
            listView.removeFooterView(this.mFooterView);
            List<Album> curList = ((TabDataItem) this.mTabDatas.get(tabIndex)).getAlbumList();
            curList.addAll(DataHelper.getUniqItemAlbums(curList, list));
            if (tabIndex == this.mCurPlayTabIndex && this.mVideoPlayer != null) {
                this.mVideoPlayer.addToPlayList(curList);
            }
            ((TabDataItem) this.mTabDatas.get(tabIndex)).setTabContentAlbumsList(curList);
            ListAdapter adapter = listView.getAdapter();
            if (adapter instanceof HeaderViewListAdapter) {
                baseDetailListAdapter = (BaseDetailListAdapter) ((HeaderViewListAdapter) adapter).getWrappedAdapter();
            } else {
                baseDetailListAdapter = (BaseDetailListAdapter) adapter;
            }
            baseDetailListAdapter.updateData(((TabDataItem) this.mTabDatas.get(tabIndex)).getAlbumList());
            listView.onAdapterUpdate();
        }
    }

    public Notify onPhoneSync() {
        if (this.mMultiEventHelper != null) {
            return this.mMultiEventHelper.onPhoneSync();
        }
        return null;
    }

    public boolean onKeyChanged(int keycode) {
        if (this.mMultiEventHelper != null) {
            return this.mMultiEventHelper.onKeyChanged(keycode);
        }
        return false;
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        if (this.mMultiEventHelper != null) {
            this.mMultiEventHelper.onDlnaKeyEvent(DlnaKeyEvent.SCROLL, keyKind);
        }
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "NewsDetailActivity/List<AbsVoiceAction> getSupportedVoices()");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        if (this.mMultiEventHelper != null) {
            return this.mMultiEventHelper.getSupportedVoices(actions);
        }
        return actions;
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.onResolutionChanged(newRes) : false;
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.onSeekChanged(newPosition) : false;
    }

    public long getPlayPosition() {
        return this.mMultiEventHelper != null ? this.mMultiEventHelper.getPlayPosition() : 0;
    }

    private synchronized void registerHomeKeyForLauncher() {
        if (!this.mIsRegisterHomeMonitor) {
            this.mHomeMonitorHelper = new HomeMonitorHelper(new OnHomePressedListener() {
                public void onHomePressed() {
                    LogRecordUtils.logd(NewsDetailActivity.this.TAG, "HomeMonitor home key pressed");
                    NewsDetailActivity.this.finish();
                }
            }, this);
            this.mIsRegisterHomeMonitor = true;
        }
    }

    private void sendInitPingback() {
        this.mE = getIntent().getStringExtra("eventId");
        this.mCalltime = getIntent().getLongExtra(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, -1);
        this.mHcdn = SystemConfigPreference.isOpenHCDN(this) ? "1" : "0";
        setItem("rpage", RPAGE.RPAGE_ID("news"));
        if (this.mCalltime > 0) {
            PingbackFactory.instance().createPingback(1).addItem(getItem("rpage")).addItem(E.E_ID(this.mE)).addItem(TD.TD_TYPE(String.valueOf(SystemClock.elapsedRealtime() - this.mCalltime))).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(HCDN.HCDN_TYPE(this.mHcdn)).post();
        }
    }

    private void sendExitPingback() {
        long td = -1;
        if (this.mCalltime > 0) {
            td = SystemClock.elapsedRealtime() - this.mCalltime;
        }
        PingbackFactory.instance().createPingback(14).addItem(getItem("rpage")).addItem(E.E_ID(this.mE)).addItem(TD.TD_TYPE(String.valueOf(td))).addItem(ST.ST_TYPE(this.mPageState)).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).addItem(EC.ST_TYPE(this.mEc)).addItem(PFEC.ST_TYPE(this.mPfec)).addItem(ISPLAYERSTART.ST_TYPE(this.mIsFirstPlayStarted ? "1" : "0")).addItem(HCDN.HCDN_TYPE(this.mHcdn)).post();
    }

    private void sendDailyInfoShowPingback(List<Album> albumList, int tabIndex, int latestPosition) {
        int i;
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendDailyInfoShowPingback()");
        }
        if (!ListUtils.isEmpty((List) albumList)) {
            Album album = null;
            String cid = "";
            String qpIdList = "";
            String type = "";
            String usract = "";
            int length;
            Album item;
            if (tabIndex == 0) {
                int startPosition;
                type = Constants.RECOMMEND_PINGBACK_TYPE_SHOW;
                usract = "1";
                if (latestPosition < 6) {
                    length = latestPosition + 1;
                    startPosition = 0;
                } else {
                    length = 6;
                    startPosition = latestPosition - 5;
                }
                if (latestPosition < albumList.size()) {
                    album = (Album) albumList.get(latestPosition);
                }
                for (i = 0; i < length; i++) {
                    try {
                        item = (Album) albumList.get(startPosition + i);
                        cid = cid + item.chnId + ",";
                        qpIdList = qpIdList + item.tvQid + ",";
                    } catch (Exception e) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(this.TAG, "<<sendDailyInfoShowPingback exception!" + e.toString() + ", i " + i + ",albumList.size() " + albumList.size());
                        }
                    }
                }
            } else {
                type = Constants.RECOMMEND_PINGBACK_TYPE_CLICK;
                usract = DBColumns.IS_NEED_SHOW;
                int size = albumList.size();
                if (size >= 0) {
                    album = (Album) albumList.get(size - 1);
                    length = Math.min(size, 10);
                    for (i = 0; i < length; i++) {
                        try {
                            item = (Album) albumList.get(i);
                            cid = cid + item.chnId + ",";
                            qpIdList = qpIdList + item.tvQid + ",";
                        } catch (Exception e2) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(this.TAG, "<<sendDailyInfoShowPingback exception!" + e2.toString() + ", i " + i + ",albumList.size() " + albumList.size());
                            }
                        }
                    }
                } else {
                    return;
                }
            }
            if (album != null) {
                PingbackFactory.instance().createPingback(30).addItem(PPUID.PPUID_TYPE(getUid())).addItem(EVENTID.EVENT_ID(album.eventId)).addItem(CID.CID_TYPE(cid)).addItem(BKT.BKT_TYPE(album.bkt)).addItem(AREA.AREA_TYPE(album.area)).addItem(ALBUMLIST.ALBUMLIST_TYPE(qpIdList)).addItem(TYPE.ITEM(type)).addItem(USRACT.USRACT_TYPE(usract)).post();
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<<sendDailyInfoShowPingback albumlist is empty");
        }
    }

    private void sendDailyInfoClickPingback(List<Album> albumList, String labelId, int position) {
        if (ListUtils.isEmpty((List) albumList)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "<<sendDailyInfoClickPingback albumlist is empty");
            }
        } else if (position >= 0 && position < albumList.size()) {
            Album clickedAlbum = (Album) albumList.get(position);
            PingbackFactory.instance().createPingback(29).addItem(USRACT.USRACT_TYPE(Constants.RECOMMEND_PINGBACK_USERACT_CLICK)).addItem(PPUID.PPUID_TYPE(getUid())).addItem(EVENTID.EVENT_ID(clickedAlbum.eventId)).addItem(CID.CID_TYPE(labelId)).addItem(BKT.BKT_TYPE(clickedAlbum.bkt)).addItem(AREA.AREA_TYPE(clickedAlbum.area)).addItem(RANK.RANK_TYPE(String.valueOf(position))).addItem(TAID.TAID_TYPE(clickedAlbum.tvQid)).addItem(TCID.TCID_TYPE(String.valueOf(clickedAlbum.chnId))).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<<sendDailyInfoClickPingback invalid position!");
        }
    }

    private void sendLoadPagePingback() {
        if (this.mCalltime > 0) {
            PingbackFactory.instance().createPingback(28).addItem(getItem("rpage")).addItem(E.E_ID(this.mE)).addItem(TD.TD_TYPE(String.valueOf(SystemClock.elapsedRealtime() - this.mCalltime))).addItem(LOCALTIME.LOCALTIME_TYPE(DataHelper.getFormatTime())).post();
        }
    }

    public PingbackItem getItem(String key) {
        return this.mPingbackContext.getItem(key);
    }

    public void setItem(String key, PingbackItem item) {
        this.mPingbackContext.setItem(key, item);
    }

    public void updatePageState(String pageState) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "updatePageState mIsFirstPlay=" + this.mIsFirstPlay);
        }
        if (this.mIsFirstPlay) {
            this.mPageState = pageState;
            if ("playerStart".equals(pageState)) {
                this.mIsFirstPlayStarted = true;
            }
        }
    }

    private String getUid() {
        String uid;
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(this)) {
            uid = GetInterfaceTools.getIGalaAccountManager().getUID();
        } else {
            uid = "NA";
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<<getUid" + uid);
        }
        return uid;
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mPingbackContext.setPingbackValueProvider(provider);
    }
}
