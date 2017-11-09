package com.gala.video.app.epg.uikit.ui.multisubject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.multisubject.model.MultiSubjectInfoModel;
import com.gala.video.app.epg.ui.multisubject.widget.MultiSubjectErrorView;
import com.gala.video.app.epg.ui.multisubject.widget.view.MultiSubjectBgView;
import com.gala.video.app.epg.uikit.item.DailyNewsItem;
import com.gala.video.app.epg.uikit.view.DailyNewsItemView;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.loader.IUikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MultiSubjectActivity extends QMultiScreenActivity {
    private static final String TAG = "MultiSubjectActivity";
    private long LOADING_DELAY_MILLIS = 1500;
    private boolean isFirstEntry = true;
    public ActionPolicy mActionPolicy;
    private MultiSubjectBgView mBgView;
    private BlocksView mBlocksView;
    private CardFocusHelper mCardFocusHelper;
    private UIKitEngine mEngine;
    private MultiSubjectErrorView mErrorView;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private MultiSubjectInfoModel mInfoModel;
    private IUikitDataLoader mLoader;
    private NetworkPrompt mNetworkStatePrompt;
    private MultiSubjectPingbackActionPolicy mPingbackActionPolicy;
    private View mRootView;
    private ScreenStatusListener mScreenStatusListener;
    private final Runnable mShowLoadingRunnable = new C12265();
    private boolean mSuccessFetchedData = false;

    class C12221 implements Runnable {
        C12221() {
        }

        public void run() {
            MultiSubjectActivity.this.showNoResultPanel(ErrorKind.NO_RESULT, null);
        }
    }

    class C12232 implements Runnable {
        C12232() {
        }

        public void run() {
            MultiSubjectActivity.this.hideProgressBar();
        }
    }

    class C12265 implements Runnable {
        C12265() {
        }

        public void run() {
            MultiSubjectActivity.this.showProgressBar();
        }
    }

    class C12276 implements Runnable {
        C12276() {
        }

        public void run() {
            MultiSubjectActivity.this.sendPageShow();
        }
    }

    private class ImageCallback implements IImageLoadCallback {
        List<CardInfoModel> mCardInfoModels = null;

        ImageCallback(List<CardInfoModel> cards) {
            this.mCardInfoModels = cards;
        }

        public void onSuccess(Bitmap bitmap) {
            LogUtils.m1577w(MultiSubjectActivity.TAG, "mImageLoadCallback onSuccess. bitmap ");
            MultiSubjectActivity.this.showData(bitmap, this.mCardInfoModels);
        }

        public void onFailed(String url) {
            LogUtils.m1577w(MultiSubjectActivity.TAG, "mImageLoadCallback onFailed. url = " + url);
            MultiSubjectActivity.this.showData(null, this.mCardInfoModels);
        }
    }

    private class NetworkListener implements INetworkStateListener {
        private NetworkListener() {
        }

        public void onConnected(boolean isChanged) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(MultiSubjectActivity.TAG, "onConnected() isChanged：" + isChanged);
            }
            if (isChanged && !MultiSubjectActivity.this.mSuccessFetchedData) {
                if (MultiSubjectActivity.this.mLoader == null) {
                    MultiSubjectActivity.this.mLoader = new UikitDataLoader(3, MultiSubjectActivity.this.mInfoModel.getItemId(), MultiSubjectActivity.this.mEngine.getId());
                    MultiSubjectActivity.this.mLoader.register();
                }
                MultiSubjectActivity.this.mLoader.firstCardList();
            }
        }
    }

    private class ScreenStatusListener implements IStatusListener {
        private ScreenStatusListener() {
        }

        public void onStart() {
            MultiSubjectActivity.this.mPingbackActionPolicy.onSendMultiSubjectCardShowPingback(MultiSubjectActivity.this.mBlocksView, MultiSubjectActivity.this.mEngine.getPage(), false);
        }

        public void onStop() {
            MultiSubjectActivity.this.mPingbackActionPolicy.initTimestamp(MultiSubjectActivity.this.mBlocksView);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPingbackPage(PingbackPage.MultiSubject);
        stopImageProvider();
        setContentView(C0508R.layout.epg_activity_mutil_subject_uikit);
        init(this);
        showLoadingDelayed();
        loadData();
    }

    protected void onResume() {
        super.onResume();
        if (this.mEngine != null) {
            this.mEngine.start();
        }
        if (this.mNetworkStatePrompt == null) {
            this.mNetworkStatePrompt = new NetworkPrompt(ResourceUtil.getContext());
        }
        this.mNetworkStatePrompt.registerNetworkListener(new NetworkListener());
        this.mPingbackActionPolicy.initTimestamp(this.mBlocksView);
        if (!this.isFirstEntry) {
            sendPageShowPingback();
        }
        this.isFirstEntry = false;
    }

    protected void onPause() {
        super.onPause();
        if (this.mNetworkStatePrompt != null) {
            this.mNetworkStatePrompt.unregisterNetworkListener();
        }
        this.mPingbackActionPolicy.onSendMultiSubjectCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false);
    }

    protected void onStop() {
        super.onStop();
        this.mEngine.stop();
    }

    protected void onDestroy() {
        super.onDestroy();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenStatusListener);
        this.mNetworkStatePrompt = null;
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
        EventBus.getDefault().unregister(this);
        this.mCardFocusHelper.destroy();
        this.mLoader.unregister();
        this.mLoader = null;
        this.mEngine.destroy();
        this.mEngine = null;
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 4 || keyCode == 111) {
            stopImageProvider();
        }
        return super.handleKeyEvent(event);
    }

    protected View getBackgroundContainer() {
        return getRootView();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onUikitEvent(UikitEvent event) {
        if (this.mEngine != null) {
            LogUtils.m1568d(TAG, this + "event.uikitEngineId = " + event.uikitEngineId + "  ;mEngine.getId() = " + this.mEngine.getId());
            if (event.uikitEngineId == this.mEngine.getId()) {
                LogUtils.m1568d(TAG, "receive loader event: " + event);
                switch (event.eventType) {
                    case 32:
                        LogUtils.m1568d(TAG, "onUikitEvent LOADER_SET_CARDS-" + event.sourceId);
                        removeLoadingCallbacks();
                        if (event.cardList == null || event.cardList.isEmpty()) {
                            this.mSuccessFetchedData = false;
                            this.mHandler.post(new C12221());
                        } else {
                            this.mSuccessFetchedData = true;
                            if (event.background == null || event.background.isEmpty()) {
                                LogUtils.m1568d(TAG, "onUikitEvent LOADER_SET_CARDS- event.background = " + event.background);
                                showData(null, event.cardList);
                            } else {
                                loadImage(event.background, new ImageCallback(event.cardList));
                            }
                            sendPageShowPingback();
                        }
                        this.mHandler.post(new C12232());
                        return;
                    case 33:
                        LogUtils.m1568d(TAG, "onUikitEvent LOADER_ADD_CARDS-" + event.sourceId + "-pageNo-" + event.pageNo);
                        appendPageData(event.cardList);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    public void init(Context context) {
        this.mBlocksView = (BlocksView) findViewById(C0508R.id.epg_multi_subject_gridview_id);
        this.mBgView = (MultiSubjectBgView) findViewById(C0508R.id.epg_multi_subject_bg_view_id);
        this.mCardFocusHelper = new CardFocusHelper(findViewById(C0508R.id.card_focus));
        LayoutParams lp = this.mBlocksView.getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            this.mCardFocusHelper.setInvisiableMarginTop(((MarginLayoutParams) lp).topMargin);
        }
        Intent intent = getIntent();
        if (intent != null) {
            try {
                this.mInfoModel = (MultiSubjectInfoModel) intent.getSerializableExtra("intent_model");
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
            EventBus.getDefault().register(this);
            this.mBlocksView.setPadding(0, ResourceUtil.getPx(43), 0, ResourceUtil.getPx(30));
            this.mEngine = UIKitEngine.newInstance(context);
            this.mEngine.bindView(this.mBlocksView);
            this.mEngine.getUIKitBuilder().registerSpecialItem(216, DailyNewsItem.class, DailyNewsItemView.class);
            setActionPolicy(new MultiSubjectActionPolicy(this.mEngine));
            setPingbackActionPolicy(new MultiSubjectPingbackActionPolicy(this.mEngine.getPage(), this.mInfoModel));
            this.mScreenStatusListener = new ScreenStatusListener();
            GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenStatusListener);
            return;
        }
        throw new IllegalArgumentException("MultiSubjectActivity.onCreate(), need a themeId");
    }

    public void setActionPolicy(ActionPolicy actionPolicy) {
        if (this.mActionPolicy != null) {
            this.mEngine.getPage().unregisterActionPolicy(this.mActionPolicy);
        }
        this.mActionPolicy = actionPolicy;
        if (this.mEngine != null) {
            this.mEngine.getPage().registerActionPolicy(this.mActionPolicy);
        }
    }

    public void setPingbackActionPolicy(MultiSubjectPingbackActionPolicy actionPolicy) {
        if (this.mPingbackActionPolicy != null) {
            this.mEngine.getPage().unregisterActionPolicy(this.mPingbackActionPolicy);
        }
        this.mPingbackActionPolicy = actionPolicy;
        if (this.mEngine != null) {
            this.mEngine.getPage().registerActionPolicy(this.mPingbackActionPolicy);
        }
    }

    public void loadData() {
        LogUtils.m1568d(TAG, "loadData Engine id:" + this.mEngine.getId());
        if (this.mLoader == null) {
            this.mLoader = new UikitDataLoader(3, this.mInfoModel.getItemId(), this.mEngine.getId());
            this.mLoader.register();
        }
        this.mLoader.firstCardList();
    }

    public void appendPageData(List<CardInfoModel> cards) {
        this.mEngine.appendData(cards);
    }

    public void bindDataSource(List<CardInfoModel> cards) {
        LogUtils.m1568d(TAG, "bindDataSource Engine id " + this.mEngine.getId());
        this.mEngine.setData(cards);
    }

    private MultiSubjectErrorView getErrorView() {
        if (this.mErrorView == null) {
            this.mErrorView = new MultiSubjectErrorView(getRootView());
        }
        return this.mErrorView;
    }

    private View getRootView() {
        if (this.mRootView == null) {
            this.mRootView = getWindow().getDecorView().findViewById(16908290);
        }
        return this.mRootView;
    }

    private void showData(final Bitmap bitmap, final List<CardInfoModel> cards) {
        removeLoadingCallbacks();
        if (this.mHandler != null) {
            this.mHandler.postDelayed(new Runnable() {
                public void run() {
                    MultiSubjectActivity.this.bindDataSource(cards);
                }
            }, 700);
            this.mHandler.post(new Runnable() {
                public void run() {
                    MultiSubjectActivity.this.hideNoResultPanel();
                    MultiSubjectActivity.this.mBgView.setBitmap(bitmap);
                }
            });
        }
    }

    public void loadImage(String url, IImageLoadCallback imageLoadCallback) {
        ImageLoader loader = new ImageLoader();
        loader.setImageLoadCallback(imageLoadCallback);
        loader.loadImage(url, null);
    }

    private void showLoadingDelayed() {
        if (this.mHandler != null) {
            this.mHandler.postDelayed(this.mShowLoadingRunnable, this.LOADING_DELAY_MILLIS);
        }
    }

    private void removeLoadingCallbacks() {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mShowLoadingRunnable);
        }
    }

    private void stopImageProvider() {
        ImageProviderApi.getImageProvider().stopAllTasks();
    }

    private void showProgressBar() {
        getErrorView().showProgressBar();
    }

    private void hideProgressBar() {
        getErrorView().hideProgressBar();
    }

    private void showNoResultPanel(ErrorKind kind, ApiException e) {
        getErrorView().showNoResultPanel();
        GetInterfaceTools.getUICreator().maketNoResultView(ResourceUtil.getContext(), getErrorView().getNoResultPanel(), kind, e);
    }

    private void hideNoResultPanel() {
        getErrorView().hideNoResultPanel();
    }

    private void sendPageShowPingback() {
        if (ThreadUtils.isUIThread()) {
            ThreadUtils.execute(new C12276());
        } else {
            sendPageShow();
        }
    }

    void sendPageShow() {
        GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.MULTISUJECT_PAGE_SHOW_PINGBACK).addItem("qtcurl", MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI).addItem("block", MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI).addItem("e", this.mInfoModel.getE()).addItem("s2", this.mInfoModel.getFrom()).addItem("tabsrc", PingBackUtils.getTabSrc()).addItem("plid", this.mInfoModel.getItemId()).post();
    }
}
