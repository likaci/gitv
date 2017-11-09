package com.gala.video.app.epg.home.component.item;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.component.item.CarouseContract.Presenter;
import com.gala.video.app.epg.home.component.item.CarouseContract.View;
import com.gala.video.app.epg.home.component.play.NCarouselPlayer;
import com.gala.video.app.epg.home.component.play.NCarouselPlayer.onWindowModeSwitchListener;
import com.gala.video.app.epg.home.controller.HomeController;
import com.gala.video.app.epg.home.controller.UIEventType;
import com.gala.video.app.epg.home.controller.activity.ActivityLifeCycleDispatcher;
import com.gala.video.app.epg.home.controller.activity.IActivityLifeCycle;
import com.gala.video.app.epg.home.controller.key.IKeyDispatcher;
import com.gala.video.app.epg.home.controller.key.KeyEventDispatcher;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.app.epg.utils.UtilsPreference;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.databus.HomeEvent;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.action.model.CarouselActionModel;
import com.gala.video.lib.share.uikit.item.Item;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NCarouselItem extends Item implements Presenter, IKeyDispatcher, OnClickListener, IActivityLifeCycle {
    private static final int ANIMATE = 101;
    private static final int ANIMATION_INTERVAL = 500;
    private static final int DELAY_PLAY = 200;
    private static final int FULL_SCREEN = 1;
    private static int HORIZONTAL_PADDING = 1;
    private static final int PAUSE = 104;
    private static final int PLAY = 102;
    private static final int PREPARE = 100;
    private static final int RESUME = 105;
    private static final int STAY_INTERVAL = 500;
    private static final int STOP = 103;
    private static int VERTICAL_PADDING = 0;
    private static final int WINDOW_MODE_SWITCH = 106;
    private static boolean showPreviewCompleted = false;
    private final int REMOVE_VIEW_WHEN_STOP = 1;
    private String TAG = "NCarouselItem";
    private boolean isItemShow = false;
    private Animator mAnimator;
    private CarouseObserver mCarouseObserver = new CarouseObserver();
    private Context mContext;
    private Drawable mCoverImage;
    private int mCurrentScreenMode = 0;
    private boolean mIsPaused = false;
    private boolean mIsStopped = false;
    private PlayHandler mPlayHandler = new PlayHandler(Looper.getMainLooper());
    private FrameLayout mPlayViewParent;
    private int[] mPlayWindowLocation = new int[2];
    private NCarouselPlayer mPlayer;
    private View mView;

    private class CarouseObserver implements MyObserver {
        private CarouseObserver() {
        }

        public void update(String event) {
            NCarouselItem.showPreviewCompleted = true;
            if (!ThreadUtils.isUIThread()) {
                NCarouselItem.this.mPlayHandler.post(new Runnable() {
                    public void run() {
                        if (NCarouselItem.this.onBindSuccess() && NCarouselItem.this.isVisible(true)) {
                            NCarouselItem.this.onPlayItemVisible();
                        }
                    }
                });
            } else if (NCarouselItem.this.onBindSuccess() && NCarouselItem.this.isVisible(true)) {
                NCarouselItem.this.onPlayItemVisible();
            }
        }
    }

    private class MyPluginStateListener implements OnStateChangedListener {
        private MyPluginStateListener() {
        }

        public void onSuccess() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NCarouselItem.this.TAG, "onSuccess: player plugin loaded success.");
            }
            if (NCarouselItem.this.onBindSuccess() && NCarouselItem.this.mView.isCoverAttached() && NCarouselItem.this.isPlayItemVisible()) {
                LogUtils.d(NCarouselItem.this.TAG, "view is attached to window");
                NCarouselItem.this.mPlayHandler.sendEmptyMessageDelayed(102, 1200);
                return;
            }
            LogUtils.d(NCarouselItem.this.TAG, "view is detached from window");
            NCarouselItem.this.mPlayHandler.removeCallbacksAndMessages(null);
        }

        public void onFailed() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NCarouselItem.this.TAG, "player plugin loaded failed.");
            }
        }

        public void onCanceled() {
        }

        public void onLoading() {
        }
    }

    private class PlayHandler extends Handler {
        public PlayHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            boolean z = true;
            LogUtils.d(NCarouselItem.this.TAG, "on recieve message  = " + msg.what);
            switch (msg.what) {
                case 100:
                    NCarouselItem.this.prepare();
                    return;
                case 101:
                    NCarouselItem.this.animate();
                    return;
                case 102:
                    NCarouselItem.this.start();
                    return;
                case 103:
                    NCarouselItem nCarouselItem = NCarouselItem.this;
                    if (msg.arg1 != 1) {
                        z = false;
                    }
                    nCarouselItem.stop(z);
                    return;
                case 104:
                    NCarouselItem.this.pause();
                    return;
                case 106:
                    NCarouselItem.this.switchWindowMode(msg.arg1);
                    return;
                default:
                    return;
            }
        }
    }

    public NCarouselItem() {
        this.TAG += "@" + Integer.toHexString(hashCode());
        Log.i(this.TAG, "create NCarouselItem");
    }

    public int getType() {
        return 218;
    }

    public void addObserver() {
        ActivityLifeCycleDispatcher.get().register(this);
        EventBus.getDefault().register(this);
    }

    public void removeObserver() {
        EventBus.getDefault().unregister(this);
        ActivityLifeCycleDispatcher.get().unregister(this);
    }

    public void setView(Context context, View view) {
        this.mContext = context;
        this.mView = view;
    }

    public void onShow() {
        this.isItemShow = true;
        LogUtils.d(this.TAG, "onShow");
        if (onBindSuccess() && isPlayItemVisible()) {
            onPlayItemVisible();
        }
        if (onBindSuccess() && isHalfScreen() && showPreviewCompleted) {
            addPreview();
        }
    }

    private boolean isHalfScreen() {
        return !isVisible(true) && isVisible(false);
    }

    public void requestFocus() {
        if (this.mView != null) {
            this.mView.coverRequestFocus();
        }
    }

    public void onUnBind() {
        LogUtils.d(this.TAG, "onUnBind");
        stopAfterUnBind();
    }

    private void stopAfterUnBind() {
        LogUtils.d(this.TAG, "stopAfterUnBind");
        if (onBindSuccess()) {
            AppRuntimeEnv.get().setIsPlayInHome(false);
            if (this.mPlayHandler.hasMessages(103)) {
                LogUtils.d(this.TAG, "handler has stop message,ignore current messsage!");
            } else {
                this.mPlayHandler.removeCallbacksAndMessages(null);
                this.mPlayHandler.sendEmptyMessage(104);
                this.mPlayHandler.sendEmptyMessage(103);
            }
            unRegisterKeyDispatcher();
        }
    }

    public void onDetachedFromWindow() {
        if (onBindSuccess()) {
            stopAfterUnBind();
        }
    }

    private boolean isPlayItemVisible() {
        boolean isVisible = isVisible(true);
        LogUtils.d(this.TAG, "is visible = " + isVisible);
        if (isVisible && showPreviewCompleted) {
            return true;
        }
        return false;
    }

    private boolean isActivityVisible() {
        boolean isVisible = false;
        try {
            Object o = Activity.class.getDeclaredMethod("isResumed", new Class[0]).invoke(this.mContext, new Object[0]);
            if (o instanceof Boolean) {
                isVisible = ((Boolean) o).booleanValue();
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        return isVisible;
    }

    public void onPlayItemVisible() {
        LogUtils.i(this.TAG, "onPlayItemVisible");
        AppRuntimeEnv.get().setIsPlayInHome(true);
        if (this.mPlayer == null || !this.mPlayer.isPlay() || this.mPlayHandler.hasMessages(103)) {
            registerKeyDispatcher();
            if (this.mCurrentScreenMode == 1) {
                this.mPlayHandler.removeMessages(102);
                this.mPlayHandler.sendEmptyMessage(102);
                KeyEventDispatcher.get().resisterKeyDispatcher(this);
                return;
            }
            this.mPlayHandler.removeMessages(100);
            this.mPlayHandler.sendEmptyMessage(100);
        }
    }

    public void onPlayItemInVisible() {
        AppRuntimeEnv.get().setIsPlayInHome(true);
        if (this.mPlayHandler.hasMessages(103)) {
            LogUtils.d(this.TAG, "handler has stop message,ignore current messsage!");
        } else {
            this.mPlayHandler.removeCallbacksAndMessages(null);
            this.mPlayHandler.sendEmptyMessage(104);
            this.mPlayHandler.sendEmptyMessageDelayed(103, 500);
        }
        unRegisterKeyDispatcher();
    }

    private void registerKeyDispatcher() {
        if (!KeyEventDispatcher.get().hasInDispatcher(this)) {
            KeyEventDispatcher.get().resisterKeyDispatcher(this);
        }
    }

    private void unRegisterKeyDispatcher() {
        if (KeyEventDispatcher.get().hasInDispatcher(this)) {
            KeyEventDispatcher.get().unResisterKeyDispatcher(this);
        }
    }

    public boolean onKeyEvent(KeyEvent event) {
        return this.mPlayer != null && this.mPlayer.onKeyEvent(event);
    }

    public void switchWindowMode(int windowMode) {
        if (windowMode == 1 && this.mPlayer != null) {
            this.mPlayer.switchToFullScreen();
        }
    }

    public void onClick(android.view.View v) {
        Log.i(this.TAG, "onClickMe ");
        if (this.mPlayer != null) {
            Log.i(this.TAG, "coverClick: ");
            Message msg = Message.obtain(this.mPlayHandler);
            msg.what = 106;
            msg.arg1 = 1;
            this.mPlayHandler.sendMessage(msg);
        }
    }

    public void carouseItemClick() {
        LogUtils.i(this.TAG, "carouseItemClick: ");
        if (this.mPlayer != null) {
            Message msg = Message.obtain(this.mPlayHandler);
            msg.what = 106;
            msg.arg1 = 1;
            this.mPlayHandler.sendMessage(msg);
        }
    }

    public void onActivityStart() {
    }

    public void onActivityResume() {
        this.mIsPaused = false;
        if (!this.mIsStopped && onBindSuccess() && isPlayItemVisible()) {
            onPlayItemVisible();
        }
        this.mIsStopped = false;
    }

    public void onActivityPause() {
        this.mIsPaused = true;
        if (onBindSuccess() && isPlayItemVisible()) {
            this.mPlayHandler.removeCallbacksAndMessages(null);
            this.mPlayHandler.sendEmptyMessage(104);
            this.mPlayHandler.sendEmptyMessage(103);
        }
    }

    public void onActivityStop() {
        this.mIsStopped = true;
    }

    public void onActivityDestroy() {
    }

    private void start() {
        if (isPlayItemVisible() && !this.mIsPaused) {
            updateIncomeSrcForPingback();
            this.mPlayViewParent = (FrameLayout) ((Activity) this.mContext).findViewById(R.id.epg_carousel_window);
            LogUtils.d(this.TAG, "cover view width = " + this.mView.getCoverWidth());
            if (this.mView.getCoverWidth() != 0 && this.mView.getCoverHeight() != 0) {
                this.mPlayer = new NCarouselPlayer(this.mPlayViewParent, this, this.mContext, createPlayLayout());
                this.mPlayer.setOnWindowChangeListener(new onWindowModeSwitchListener() {
                    public void onWindowModeSwitched(int current) {
                        NCarouselItem.this.mCurrentScreenMode = current;
                        LogUtils.d(NCarouselItem.this.TAG, "onWindowModeSwitched current screen mode = " + NCarouselItem.this.mCurrentScreenMode);
                    }
                });
                this.mView.setCoverImage(null);
                LogUtils.d(this.TAG, "start current mode = " + this.mCurrentScreenMode);
                this.mPlayer.startPlay(this.mCurrentScreenMode, getPlayInfo());
                if (this.mContext instanceof Activity) {
                    ((Activity) this.mContext).getWindow().addFlags(128);
                }
            }
        }
    }

    private ChannelCarousel getPlayInfo() {
        ChannelCarousel PlayInfo = new ChannelCarousel();
        String playId = UtilsPreference.getCarouselChannelIdFromHistory(this.mContext);
        if (StringUtils.isEmpty((CharSequence) playId)) {
            BaseActionModel actionModel = getModel().getActionModel();
            if (actionModel != null && (actionModel instanceof CarouselActionModel)) {
                ChannelLabel label = ((CarouselActionModel) actionModel).getLabel();
                if (label != null) {
                    PlayInfo.id = StringUtils.parse(label.itemId, 0);
                    PlayInfo.tableNo = (long) label.tableNo;
                    PlayInfo.name = DataBuildTool.getPrompt(label);
                }
            }
        } else {
            PlayInfo.id = StringUtils.parse(playId, 0);
            PlayInfo.name = UtilsPreference.getCarouselChannelNameFromHistory(this.mContext);
            PlayInfo.tableNo = (long) StringUtils.parse(UtilsPreference.getCarouselChannelTableNoFromHistory(this.mContext), -1);
        }
        LogUtils.d(this.TAG, "carousel playInfo : " + PlayInfo);
        return PlayInfo;
    }

    private LayoutParams createPlayLayout() {
        int[] location = new int[2];
        this.mView.getLocation(location);
        LayoutParams videoLayoutParams = new LayoutParams(this.mView.getCoverWidth() + 0, this.mView.getCoverHeight() + 0);
        videoLayoutParams.leftMargin = location[0] + 0;
        videoLayoutParams.topMargin = location[1] + 0;
        LogUtils.d(this.TAG, "video player layout params (" + videoLayoutParams.width + "," + videoLayoutParams.height + "," + videoLayoutParams.leftMargin + "," + videoLayoutParams.topMargin + ")" + ",horizontal padding = " + 0 + ",vertical padding = " + 0);
        return videoLayoutParams;
    }

    private int getBgPadding(Context context, int backgroundResource, int orientation) {
        if (backgroundResource > 0) {
            StateListDrawable stateListDrawable = (StateListDrawable) context.getResources().getDrawable(backgroundResource);
            if (stateListDrawable != null) {
                Drawable drawable = stateListDrawable.getCurrent();
                Rect rect = new Rect();
                drawable.getPadding(rect);
                if (VERTICAL_PADDING == orientation) {
                    return rect.top;
                }
                return rect.left;
            }
        }
        return 0;
    }

    private void updateIncomeSrcForPingback() {
        String tabName = PingBackCollectionFieldUtils.getTabName();
        PingBackCollectionFieldUtils.setIncomeSrc(tabName + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_c_1_item_1");
        LogUtils.d(this.TAG, "updateIncomeSrcForPingback, incomesrc = " + PingBackCollectionFieldUtils.getIncomeSrc());
    }

    private void loadPlayerFeature() {
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(this.mContext, new MyPluginStateListener(), false);
    }

    private void prepare() {
        loadPlayerFeature();
        this.mView.setCoverClickListener(this);
        this.mCoverImage = getCoverDrawable();
        this.mView.setCoverImage(this.mCoverImage);
        this.mView.setCoverAlpha(1.0f);
        this.mView.getPlayCoverView().setAlpha(1.0f);
        this.mPlayHandler.sendEmptyMessageDelayed(101, 500);
    }

    private void pause() {
        addPreview();
        if (this.mPlayer != null) {
            this.mPlayer.pause();
        }
    }

    private void animate() {
        this.mAnimator = this.mView.alphaAnimationPlayCover();
    }

    private void addPreview() {
        if (this.mCoverImage == null) {
            this.mCoverImage = getCoverDrawable();
        }
        if (this.mAnimator != null) {
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        this.mView.setCoverImage(this.mCoverImage);
        this.mView.setCoverAlpha(1.0f);
    }

    private Drawable getCoverDrawable() {
        List<String> urls = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getDefaultCarouselUrl();
        if (urls != null && urls.size() > 0) {
            String filePath = (String) urls.get(0);
            if (!TextUtils.isEmpty(filePath)) {
                File cacheFile = new File(filePath);
                if (cacheFile.exists()) {
                    return new BitmapDrawable(this.mContext.getResources(), BitmapFactory.decodeFile(cacheFile.getAbsolutePath()));
                }
            }
        }
        return this.mContext.getResources().getDrawable(R.drawable.epg_home_carousel_cover_image);
    }

    private void stop(boolean removePreView) {
        AppRuntimeEnv.get().setIsPlayInHome(false);
        if (this.mPlayer != null) {
            this.mPlayer.stopPlay(removePreView);
            if (removePreView) {
                this.mCurrentScreenMode = 0;
            }
            HomeController.sUIEvent.post(UIEventType.CAROUSEL_CARD_UPDATE, null);
            this.mPlayer = null;
        }
        if (this.mContext instanceof Activity) {
            ((Activity) this.mContext).getWindow().clearFlags(128);
        }
    }

    public void sendStopMsg() {
        LogUtils.d(this.TAG, "receive stop cmd is paused = " + this.mIsPaused);
        if (onBindSuccess() && !this.mIsPaused) {
            onPlayItemInVisible();
        }
    }

    public void sendStartMsg() {
        if (onBindSuccess() && isPlayItemVisible()) {
            onPlayItemVisible();
        }
    }

    public boolean onBindSuccess() {
        return (this.mContext == null || this.mView == null) ? false : true;
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void previewCompleted(HomeEvent event) {
        showPreviewCompleted = true;
        if (onBindSuccess() && isVisible(true)) {
            onPlayItemVisible();
        }
        if (onBindSuccess() && isHalfScreen()) {
            addPreview();
        }
    }
}
