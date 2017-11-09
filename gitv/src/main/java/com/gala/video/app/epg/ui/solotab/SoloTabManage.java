package com.gala.video.app.epg.ui.solotab;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.uikit.item.DailyNewsItem;
import com.gala.video.app.epg.uikit.view.DailyNewsItemView;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.loader.IUikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitDataLoader;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class SoloTabManage {
    private static String TAG = "SoloTabManage";
    private boolean isFirstEntry = true;
    private BlocksView mBlocksView;
    private CardFocusHelper mCardFocusHelper;
    private Context mContext;
    private UIKitEngine mEngine;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SoloTabInfoModel mInfoModel;
    private IUikitDataLoader mLoader;
    private NetworkPrompt mNetworkStatePrompt;
    private SoloTabPingbackActionPolicy mPingbackActionPolicy;
    private View mRootView;
    private ScreenStatusListener mScreenStatusListener;
    private boolean mSuccessFetchedData;

    private static class NetworkListener implements INetworkStateListener {
        private WeakReference<SoloTabManage> mOuter;

        public NetworkListener(SoloTabManage out) {
            this.mOuter = new WeakReference(out);
        }

        public void onConnected(boolean isChanged) {
            SoloTabManage outer = (SoloTabManage) this.mOuter.get();
            if (outer != null) {
                Log.d(SoloTabManage.TAG, "onConnected() isChanged=" + isChanged + ",mSuccessFetchedData=" + outer.mSuccessFetchedData);
                if (isChanged) {
                    outer.loadData();
                }
            }
        }
    }

    private static class ScreenStatusListener implements IStatusListener {
        private WeakReference<SoloTabManage> mOuter;

        public ScreenStatusListener(SoloTabManage out) {
            this.mOuter = new WeakReference(out);
        }

        public void onStart() {
            SoloTabManage outer = (SoloTabManage) this.mOuter.get();
            if (outer != null && outer.mPingbackActionPolicy != null && outer.mEngine != null) {
                outer.mPingbackActionPolicy.onSendCardShowPingback(outer.mBlocksView, outer.mEngine.getPage(), false, false);
            }
        }

        public void onStop() {
            SoloTabManage outer = (SoloTabManage) this.mOuter.get();
            if (outer != null && outer.mPingbackActionPolicy != null) {
                outer.mPingbackActionPolicy.initTimestamp(outer.mBlocksView);
            }
        }
    }

    public SoloTabManage(Context context, View rootView, SoloTabInfoModel infoModel) {
        TAG = "SoloTabManage@" + Integer.toHexString(hashCode());
        this.mContext = context;
        this.mRootView = rootView;
        this.mInfoModel = infoModel;
        Log.d(TAG, this + ",newInstance SoloTabManage(), mContext=" + this.mContext + ",mInfoModel=" + this.mInfoModel);
    }

    public void initialize() {
        initEngine();
        loadData();
    }

    private void initEngine() {
        this.mBlocksView = (BlocksView) this.mRootView.findViewById(R.id.epg_solo_tab_blockview);
        this.mBlocksView.setPadding(0, ResourceUtil.getPx(43), 0, ResourceUtil.getPx(30));
        this.mCardFocusHelper = new CardFocusHelper(this.mRootView.findViewById(R.id.epg_solo_tab_card_focus));
        LayoutParams lp = this.mBlocksView.getLayoutParams();
        if (lp instanceof MarginLayoutParams) {
            this.mCardFocusHelper.setInvisiableMarginTop(((MarginLayoutParams) lp).topMargin);
        }
        EventBus.getDefault().register(this);
        this.mEngine = UIKitEngine.newInstance(this.mContext);
        this.mEngine.bindView(this.mBlocksView);
        this.mEngine.getUIKitBuilder().registerSpecialItem(216, DailyNewsItem.class, DailyNewsItemView.class);
        this.mEngine.getPage().registerActionPolicy(new SoloTabActionPolicy(this.mEngine));
        this.mPingbackActionPolicy = new SoloTabPingbackActionPolicy(this.mEngine.getPage(), this.mInfoModel);
        this.mEngine.getPage().registerActionPolicy(this.mPingbackActionPolicy);
        this.mScreenStatusListener = new ScreenStatusListener(this);
    }

    public UIKitEngine getEngine() {
        return this.mEngine;
    }

    private void sendPageShowPingback() {
        sendPageShow();
    }

    private void sendPageShow() {
        GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.SOLOTAB_PAGE_SHOW_PINGBACK).addItem("qtcurl", "solo_" + this.mInfoModel.getTabName()).addItem("block", "solo_" + this.mInfoModel.getTabName()).addItem("e", this.mInfoModel.getE()).addItem("tabsrc", this.mInfoModel.getTabSrc()).addItem("c1", this.mInfoModel.getChannelId() + "").post();
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.BACKGROUND)
    public void onUikitEvent(final UikitEvent event) {
        if (this.mEngine != null && this.mHandler != null) {
            Log.d(TAG, this + ",eventbus, uikitEngineId=" + event.uikitEngineId + ",mEngine.getId()=" + this.mEngine.getId());
            if (event.uikitEngineId == this.mEngine.getId()) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        Log.d(SoloTabManage.TAG, this + ", eventbus, receive loader event: " + event);
                        SoloTabManage.this.handleDataEvent(event);
                    }
                });
            }
        }
    }

    private void handleDataEvent(UikitEvent event) {
        switch (event.eventType) {
            case 32:
                Log.d(TAG, this + ",onUikitEvent loader_set_cards,sourceId=" + event.sourceId + ",pageNo=" + event.pageNo + ",card.size=" + ListUtils.getCount(event.cardList));
                if (ListUtils.isEmpty(event.cardList)) {
                    showLoading();
                    this.mSuccessFetchedData = false;
                    NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                        public void getStateResult(int state) {
                            boolean networkAvaliable = NetworkUtils.isNetworkAvaliable();
                            Log.e(SoloTabManage.TAG, this + ",onUikitEvent loader_set_cards,checkNetWork,state=" + state + "," + "networkAvaliable=" + networkAvaliable);
                            if (!networkAvaliable) {
                                QToast.makeTextAndShow(ResourceUtil.getContext(), R.string.result_no_net, (int) QToast.LENGTH_4000);
                            }
                        }
                    });
                    return;
                }
                this.mSuccessFetchedData = true;
                Log.d(TAG, this + ",setData, Engine id:" + this.mEngine.getId() + ",mSourceId=" + this.mInfoModel.getSourceId());
                this.mEngine.setData(event.cardList);
                sendPageShowPingback();
                return;
            case 33:
                Log.d(TAG, this + ",onUikitEvent loader_add_cards,sourceId=" + event.sourceId + ",pageNo=" + event.pageNo + ",card.size=" + ListUtils.getCount(event.cardList));
                this.mEngine.appendData(event.cardList);
                return;
            default:
                return;
        }
    }

    private synchronized void loadData() {
        Log.d(TAG, this + ",loadData, Engine id:" + this.mEngine.getId() + ",mSourceId=" + this.mInfoModel.getSourceId() + ",mSuccessFetchedData=" + this.mSuccessFetchedData);
        if (!this.mSuccessFetchedData) {
            showLoading();
            if (this.mLoader == null) {
                this.mLoader = new UikitDataLoader(3, this.mInfoModel.getSourceId(), this.mEngine.getId());
                this.mLoader.setVipLoader(this.mInfoModel.isVip());
                this.mLoader.register();
            }
            this.mLoader.firstCardList();
        }
    }

    private void showLoading() {
        Log.d(TAG, this + ",showLoading");
        this.mEngine.getPage().showLoading();
    }

    public void onResume() {
        if (this.mEngine != null) {
            this.mEngine.start();
        }
        if (this.mNetworkStatePrompt == null) {
            this.mNetworkStatePrompt = new NetworkPrompt(this.mContext);
        }
        this.mNetworkStatePrompt.registerNetworkListener(new NetworkListener(this));
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenStatusListener);
        this.mPingbackActionPolicy.initTimestamp(this.mBlocksView);
        if (!this.isFirstEntry) {
            sendPageShowPingback();
        }
        this.isFirstEntry = false;
    }

    public void onPause() {
        if (this.mNetworkStatePrompt != null) {
            this.mNetworkStatePrompt.unregisterNetworkListener();
        }
        this.mPingbackActionPolicy.onSendCardShowPingback(this.mBlocksView, this.mEngine.getPage(), false, false);
    }

    public void onStop() {
        this.mEngine.stop();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenStatusListener);
    }

    public void onDestroy() {
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
}
