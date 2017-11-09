package com.gala.video.app.epg.home.controller;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.PageManage;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.lib.share.utils.TraceEx;
import java.util.List;

public class HomeActionPolicy extends ActionPolicy {
    private static final int CACHE_CARD_SIZE = 4;
    private static final int MAX_TOAST_COUNT = 3;
    public static String TAG = "HomeActionPolicy";
    private static final long TOAST_DELAY = 500;
    public static volatile boolean mToastHasShow = false;
    private PageManage mBindPage;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private HomeController mHomeController;
    private int mPageindex;
    private boolean mRequestDefaultFocus = true;
    private QToast mToast;
    private int mToastCount = -1;

    private class ToastRunnable implements Runnable {
        private ToastRunnable() {
        }

        public void run() {
            HomeActionPolicy.this.mToast = QToast.makeText(HomeActionPolicy.this.mHomeController.getContext(), ResourceUtil.getStr(R.string.back_key_toast), (int) QToast.LENGTH_4000);
            SystemConfigPreference.saveTabToastCount(HomeActionPolicy.this.mHomeController.getContext(), HomeActionPolicy.access$304(HomeActionPolicy.this));
            HomeActionPolicy.mToastHasShow = true;
            HomeActionPolicy.this.mToast.show();
            TraceEx.endSection();
        }
    }

    static /* synthetic */ int access$304(HomeActionPolicy x0) {
        int i = x0.mToastCount + 1;
        x0.mToastCount = i;
        return i;
    }

    public HomeActionPolicy(HomeController homeController, int index) {
        this.mHomeController = homeController;
        this.mPageindex = index;
        this.mBindPage = this.mHomeController.getUIController().mPageTabs[this.mPageindex];
        if (this.mToastCount < 0) {
            this.mToastCount = SystemConfigPreference.getTabToastCount(homeController.getContext());
        }
    }

    public void onFirstLayout(ViewGroup parent) {
        LogUtils.d(TAG, "onFirstLayout pageindex " + this.mPageindex + ",is started = " + this.mBindPage.mStarted + ",builded = " + this.mBindPage.mBuilded + ",is Selected = " + this.mBindPage.mSelected + "is Loading = " + this.mBindPage.mIsLoading + " mIsDefault = " + this.mBindPage.mIsDefault + "Engine id:" + this.mBindPage.getUIkitEngine().getId() + " parent = " + parent);
        if (!(this.mBindPage.mBuilded || this.mBindPage.mIsLoading)) {
            this.mBindPage.mBuilded = true;
            this.mHomeController.buildOnComplete(this.mPageindex);
            if (!this.mBindPage.mStarted && this.mBindPage.mSelected) {
                this.mBindPage.getUIkitEngine().start();
                this.mBindPage.mStarted = true;
            }
        }
        if (this.mBindPage.mBuilded && this.mBindPage.mSelected && LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[start performance] first layout cost " + (SystemClock.elapsedRealtime() - this.mBindPage.getLoadedTime()) + " ms" + " ,pageindex = " + this.mPageindex);
        }
        requestDefaultFocus(parent);
        postScrollTopEvent(parent);
    }

    public void onScrollStop(ViewGroup parent) {
        super.onScrollStop(parent);
        postScrollStopEvent(((BlocksView) parent).getFocusPosition());
        postScrollTopEvent(parent);
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        Page page = this.mBindPage.getUIkitEngine().getPage();
        Card card = page.getItem(cast(parent).getFocusPosition()).getParent();
        if (card != null && page.shouldLoadMore()) {
            synchronized (page) {
                List<Card> cardList = page.getCards();
                Card lastCard = (Card) cardList.get(cardList.size() - 1);
                if (lastCard != null && cardList.size() - cardList.indexOf(card) <= 4) {
                    UikitEvent uiEvent = new UikitEvent();
                    uiEvent.eventType = 17;
                    uiEvent.uikitEngineId = this.mBindPage.getUIkitEngine().getId();
                    uiEvent.cardInfoModel = lastCard.getModel();
                    this.mBindPage.onPostEvent(uiEvent);
                    Log.d(TAG, "UIKIT_ADD_CARDS");
                }
            }
        }
    }

    public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
        if (hasFocus && isShowBackTabGuide(position) && this.mToastCount <= 3) {
            handleBackTabGuide();
        }
    }

    public void onFocusLost(ViewGroup parent, ViewHolder holder) {
        this.mRequestDefaultFocus = false;
    }

    public void onNOData() {
        this.mHomeController.checkIfBuildAllCompleted();
    }

    public void updatePageindex(int pageindex) {
        this.mPageindex = pageindex;
    }

    public int getPageindex() {
        return this.mPageindex;
    }

    private boolean isShowBackTabGuide(int position) {
        int line = getLine(position);
        int direction = this.mBindPage.getBlocksView().getDirection();
        LogRecordUtils.logd(TAG, "isShowBackTabGuide: line -> " + line + ", direction -> " + direction);
        boolean isShowBackTabGuide = false;
        if (!mToastHasShow && line >= 3 && direction == 33) {
            isShowBackTabGuide = true;
        }
        LogRecordUtils.logd(TAG, "isShowBackTabGuide: isShowBackTabGuide -> " + isShowBackTabGuide);
        return isShowBackTabGuide;
    }

    private void handleBackTabGuide() {
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mToast != null && mToastHasShow) {
            this.mToast.hide();
            this.mToast = null;
            this.mToastCount = 4;
        } else if (this.mToastCount < 3) {
            this.mHandler.postDelayed(new ToastRunnable(), TOAST_DELAY);
        }
    }

    private int getLine(int position) {
        if (this.mBindPage == null || position < 0) {
            return 0;
        }
        Page page = this.mBindPage.getUIkitEngine().getPage();
        Item item = page.getItem(position);
        Card currentCard = item.getParent();
        int line = 0;
        List<Card> cardList = page.getCards();
        int size = cardList.size();
        for (int i = 0; i < size; i++) {
            Card card = (Card) cardList.get(i);
            if (card == null) {
                LogUtils.w(TAG, "getLine. card==null.");
            } else if (currentCard == card) {
                break;
            } else {
                line += card.getAllLine();
            }
        }
        return line + item.getLine();
    }

    private boolean isPageScrolled() {
        return this.mHomeController.getMainPagePresenter().isPageScrolled();
    }

    private void postScrollTopEvent(ViewGroup parent) {
        if (this.mBindPage.getUIkitEngine().getPage().isOnTop()) {
            UikitEvent uiEvent = new UikitEvent();
            uiEvent.eventType = 16;
            uiEvent.uikitEngineId = this.mBindPage.getUIkitEngine().getId();
            this.mBindPage.onPostEvent(uiEvent);
            Log.d(TAG, "UITKI_SCROLL_TOP");
        }
    }

    private void postScrollStopEvent(int position) {
        Card card = this.mBindPage.getUIkitEngine().getPage().getItem(position).getParent();
        UikitEvent uiEvent = new UikitEvent();
        uiEvent.eventType = 18;
        uiEvent.uikitEngineId = this.mBindPage.getUIkitEngine().getId();
        uiEvent.cardInfoModel = card.getModel();
        this.mBindPage.onPostEvent(uiEvent);
        Log.d(TAG, "UIKIT_SCROLL_PLACE");
    }

    private void requestDefaultFocus(ViewGroup parent) {
        if (this.mRequestDefaultFocus && this.mBindPage.mIsDefault && !isPageScrolled()) {
            parent.requestFocus();
        }
    }
}
