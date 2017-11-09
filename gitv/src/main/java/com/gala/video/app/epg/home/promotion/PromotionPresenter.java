package com.gala.video.app.epg.home.promotion;

import android.content.Context;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.contract.PromotionContract.Presenter;
import com.gala.video.app.epg.home.contract.PromotionContract.View;
import com.gala.video.app.epg.home.promotion.local.PromotionCache;
import com.gala.video.app.epg.home.utils.PromotionUtil;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload.PromotionMessage;
import com.gala.video.lib.share.project.Project;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PromotionPresenter implements Presenter, MyObserver {
    private static final String TAG = "PromotionPresenter";
    private boolean focus = false;
    private AsyncTask mAppRecommendTask;
    private Context mContext;
    private long mLastAnimationX;
    private long mLastAnimationY;
    private int preFocusId = -1;
    private final View promotionView;

    private static class AppRecommendationCacheTask extends AsyncTask<Void, Void, Integer> {
        private WeakReference<Context> contextWeak;
        private WeakReference<Presenter> presenter;
        private HashMap<String, PromotionMessage> promotionMap;

        private AppRecommendationCacheTask(Context context, Presenter presenter) {
            this.contextWeak = new WeakReference(context);
            this.presenter = new WeakReference(presenter);
        }

        protected Integer doInBackground(Void... voids) {
            Context context = (Context) this.contextWeak.get();
            boolean support = Project.getInstance().getBuild().isSupportRecommendApp();
            if (context == null || !support) {
                return Integer.valueOf(-1);
            }
            this.promotionMap = PromotionCache.instance().get();
            if (this.promotionMap == null) {
                return Integer.valueOf(-1);
            }
            IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            String chinaPokerUrl = "";
            String childAppUrl = "";
            if (dynamicQDataModel != null) {
                chinaPokerUrl = dynamicQDataModel.getChinaPokerAppUrl();
                childAppUrl = dynamicQDataModel.getChildAppUrl();
            }
            LogUtils.i(PromotionPresenter.TAG, "childApp:" + childAppUrl + " chinaPoker:" + chinaPokerUrl);
            return appDisplay(context, PromotionUtil.isAppSupport(chinaPokerUrl), PromotionUtil.isAppSupport(childAppUrl));
        }

        private Integer appDisplay(Context context, boolean supportPoker, boolean supportChild) {
            if (supportPoker && supportChild) {
                if (PromotionUtil.judgementAppShouldShow(context, (PromotionMessage) this.promotionMap.get(PromotionCache.CHINAPOKER_APP_TAG), PromotionUtil.KEY_POKER_PROMOTION)) {
                    return Integer.valueOf(2);
                }
                return Integer.valueOf(1);
            } else if (supportPoker) {
                return Integer.valueOf(2);
            } else {
                if (supportChild) {
                    return Integer.valueOf(1);
                }
                return Integer.valueOf(-1);
            }
        }

        protected void onPostExecute(Integer integer) {
            Presenter presenter = (Presenter) this.presenter.get();
            LogUtils.i(PromotionPresenter.TAG, "GameType:" + integer);
            switch (integer.intValue()) {
                case 1:
                    PromotionMessage childMessage = (PromotionMessage) this.promotionMap.get(PromotionCache.CHILD_APP_TAG);
                    if (childMessage != null && presenter != null) {
                        LogUtils.i(PromotionPresenter.TAG, "show=>" + childMessage);
                        childMessage.setType(1);
                        presenter.showPromotion(childMessage);
                        return;
                    }
                    return;
                case 2:
                    PromotionMessage pokerMessage = (PromotionMessage) this.promotionMap.get(PromotionCache.CHINAPOKER_APP_TAG);
                    if (pokerMessage != null && presenter != null) {
                        LogUtils.i(PromotionPresenter.TAG, "show=>" + pokerMessage);
                        pokerMessage.setType(2);
                        presenter.showPromotion(pokerMessage);
                        return;
                    }
                    return;
                default:
                    if (presenter != null) {
                        presenter.invisiblePromotion();
                        return;
                    }
                    return;
            }
        }
    }

    public PromotionPresenter(Context context, View promotionView) {
        this.promotionView = promotionView;
        this.mContext = context;
    }

    public void start() {
        executeAppRecommendTask();
        GetInterfaceTools.getDataBus().registerStickySubscriber(IDataBus.UPDATE_PROMOTION_APP, this);
    }

    public void onStop() {
    }

    public boolean isPromotionViewVisibility() {
        return this.promotionView != null && this.promotionView.isVisibility();
    }

    private void executeAppRecommendTask() {
        if (this.mAppRecommendTask == null || this.mAppRecommendTask.getStatus() == Status.FINISHED) {
            this.mAppRecommendTask = new AppRecommendationCacheTask(this.mContext, this).execute(new Void[0]);
        }
    }

    public void update(String event) {
        if (IDataBus.UPDATE_PROMOTION_APP.equals(event)) {
            executeAppRecommendTask();
        }
    }

    public void destroy() {
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.UPDATE_PROMOTION_APP, this);
    }

    public void invisiblePromotion() {
        if (this.promotionView != null) {
            this.promotionView.invisiblePromotion();
        }
    }

    public void setNextFocusDownId(int id) {
        if (this.promotionView != null) {
            this.promotionView.setNextFocusDownId(id);
        }
    }

    public void setPreFocusId(int focusId) {
        this.preFocusId = focusId;
    }

    public void setOnFocus(boolean focus) {
        this.focus = focus;
    }

    public int getPreFocusId() {
        return this.preFocusId;
    }

    public boolean onKey(android.view.View v, int keyCode, KeyEvent event) {
        switch (event.getKeyCode()) {
            case 19:
                if (!this.focus) {
                    lastYAnimation(this.mContext, v);
                }
                this.focus = false;
                break;
            case 21:
                if (!(this.focus || this.preFocusId == -1)) {
                    this.preFocusId = -1;
                }
                this.focus = false;
                break;
            case 22:
                if (!this.focus) {
                    if (PromotionUtil.judgmentNextFocusIsSelf(2, v)) {
                        lastXAnimation(this.mContext, v);
                    } else {
                        this.preFocusId = -1;
                    }
                }
                this.focus = false;
                break;
        }
        return false;
    }

    private void lastXAnimation(Context context, android.view.View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationX > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.epg_shake));
            this.mLastAnimationX = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    private void lastYAnimation(Context context, android.view.View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationY > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.epg_shake_y));
            this.mLastAnimationY = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public void showPromotion(PromotionMessage message) {
        if (this.promotionView != null) {
            this.promotionView.showPromotion(message);
        }
    }
}
