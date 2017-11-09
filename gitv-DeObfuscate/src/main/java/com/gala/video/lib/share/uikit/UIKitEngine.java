package com.gala.video.lib.share.uikit;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalFocusChangeListener;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.albumlist.widget.LayoutManager.FocusPlace;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.contract.PageContract;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIKitEngine implements ServiceManager {
    private static final int EXTRA_PADDING = 500;
    private static final String TAG = "UIKitEngine";
    private Page mPage;
    private BlocksView mRootView;
    private Map<Class<?>, Object> mServices = new HashMap();
    public UIKitBuilder mUIKitBuilder;
    private PageView mView;

    class C17811 implements OnGlobalFocusChangeListener {
        C17811() {
        }

        public void onGlobalFocusChanged(View oldFocus, View newFocus) {
            if (BlocksView.containsView(UIKitEngine.this.mRootView, oldFocus) && !BlocksView.containsView(UIKitEngine.this.mRootView, newFocus)) {
                ViewHolder holder = UIKitEngine.this.mRootView.getViewHolderByPosition(UIKitEngine.this.mRootView.getFocusPosition());
                if (holder != null) {
                    UIKitEngine.this.mPage.getActionPolicy().onFocusLost(UIKitEngine.this.mRootView, holder);
                }
            }
        }
    }

    public class PageView implements PageContract.View {
        public void setLayouts(List<BlockLayout> layouts) {
            UIKitEngine.this.mRootView.getLayoutManager().setLayouts(layouts);
        }

        public void setFocusPosition(int position) {
            UIKitEngine.this.mRootView.setFocusPosition(position);
        }

        public BlocksView get() {
            return UIKitEngine.this.mRootView;
        }

        public boolean isOnTop() {
            if (UIKitEngine.this.mRootView.getFirstAttachedPosition() == 0) {
                return UIKitEngine.this.mRootView.getLayoutManager().isOnTop();
            }
            return false;
        }
    }

    public static UIKitEngine newInstance(Context context) {
        UIKitBuilder builder = UIKitBuilder.newInstance(context);
        UIKitEngine engine = builder.build();
        engine.initEngine(builder);
        return engine;
    }

    private void initEngine(UIKitBuilder builder) {
        this.mUIKitBuilder = builder;
        this.mPage = new Page(this);
    }

    public UIKitBuilder getUIKitBuilder() {
        return this.mUIKitBuilder;
    }

    public Page getPage() {
        return this.mPage;
    }

    public void bindView(BlocksView view) {
        this.mRootView = view;
        if (this.mRootView.getAdapter() == null) {
            this.mRootView.setAdapter(this.mPage.getAdapter());
        }
        this.mView = new PageView();
        this.mPage.setView(this.mView);
        initView();
        registerListener();
    }

    private void registerListener() {
        this.mRootView.setOnScrollListener(this.mPage.getActionPolicy());
        this.mRootView.setOnItemClickListener(this.mPage.getActionPolicy());
        this.mRootView.setOnItemFocusChangedListener(this.mPage.getActionPolicy());
        this.mRootView.setOnItemStateChangeListener(this.mPage.getActionPolicy());
        this.mRootView.setOnFirstLayoutListener(this.mPage.getActionPolicy());
        this.mRootView.setOnFocusPositionChangedListener(this.mPage.getActionPolicy());
        this.mRootView.setOnMoveToTheBorderListener(this.mPage.getActionPolicy());
        this.mRootView.setOnAttachStateChangeListener(this.mPage.getActionPolicy());
    }

    private void initView() {
        this.mRootView.setClipChildren(false);
        this.mRootView.setClipToPadding(false);
        this.mRootView.setFocusPlace(FocusPlace.FOCUS_EDGE);
        this.mRootView.setFocusMode(1);
        this.mRootView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mRootView.setQuickFocusLeaveForbidden(true);
        this.mRootView.setWillNotDraw(false);
        this.mRootView.setFocusLoop(true);
        this.mRootView.setFocusLeaveForbidden(83);
        this.mRootView.setExtraPadding(ResourceUtil.getPx(500));
        this.mRootView.getViewTreeObserver().addOnGlobalFocusChangeListener(new C17811());
    }

    public <T> void register(Class<T> type, T service) {
        this.mServices.put(type, service);
    }

    public <T> T getService(Class<T> type) {
        Object service = this.mServices.get(type);
        if (service == null) {
            return null;
        }
        return type.cast(service);
    }

    public void setData(List<CardInfoModel> cards) {
        this.mPage.setData(cards);
    }

    public void setDataSync(List<CardInfoModel> cards) {
        this.mPage.setDataSync(cards);
    }

    public void appendData(List<CardInfoModel> cards) {
        this.mPage.appendData(cards);
    }

    public void updateCaredModel(CardInfoModel cardModel) {
        this.mPage.updateCaredModel(cardModel);
    }

    public void changeCardModel(CardInfoModel cardModel) {
        this.mPage.changeCardModel(cardModel);
    }

    public void insertCardModel(int index, CardInfoModel cardModel) {
        this.mPage.insertCardModel(index, cardModel);
    }

    public void removeCardModel(int index, int count) {
        this.mPage.removeCardModel(index, count, true);
    }

    public void removeCardModel(int index, int count, boolean isAnimationRequired) {
        this.mPage.removeCardModel(index, count, isAnimationRequired);
    }

    public void registerActionPolicy(ActionPolicy actionPolicy) {
        this.mPage.registerActionPolicy(actionPolicy);
    }

    public void unregisterActionPolicy(ActionPolicy actionPolicy) {
        this.mPage.unregisterActionPolicy(actionPolicy);
    }

    public void start() {
        Log.d(TAG, "start");
        this.mPage.start();
    }

    public void stop() {
        Log.d(TAG, "stop");
        this.mPage.stop();
    }

    public void destroy() {
        Log.d(TAG, "destroy");
        this.mPage.destroy();
        this.mServices.clear();
        this.mServices = null;
    }

    public void hide() {
        this.mPage.hide();
    }

    public int getId() {
        return System.identityHashCode(this);
    }

    public void setIsDefaultPage(boolean isDefaultPage) {
        this.mPage.setIsDefaultPage(isDefaultPage);
    }
}
