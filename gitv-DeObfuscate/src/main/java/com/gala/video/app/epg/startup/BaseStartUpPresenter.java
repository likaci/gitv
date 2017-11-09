package com.gala.video.app.epg.startup;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.ads.controller.AdStatusCallBack;
import com.gala.video.app.epg.home.ads.controller.StartScreenAdHandler;
import com.gala.video.app.epg.home.data.provider.StartOperateImageProvider;
import com.gala.video.app.epg.home.presenter.GuidePresenter;
import com.gala.video.app.epg.home.presenter.GuidePresenter.onGuideDismissListener;
import com.gala.video.app.epg.home.presenter.OperateImageShower;
import com.gala.video.app.epg.home.presenter.OperateImageShower.OperateImageStatusCallBack;
import com.gala.video.app.epg.home.widget.guidelogin.CheckInDialog;
import com.gala.video.app.epg.home.widget.guidelogin.CheckInHelper;
import com.gala.video.app.epg.preference.GuidePreference;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.io.File;

public class BaseStartUpPresenter {
    public static final int MSG_FINISH_STAGE_1 = 768;
    public static final int MSG_FINISH_STAGE_2 = 256;
    public static final int MSG_FINISH_STAGE_3 = 512;
    public static final long SHOW_TIME = 1000;
    private static final String TAG = "BaseStartUpPresenter";
    private RelativeLayout mAdContainer;
    private AdStatusCallBack mAdStatusCallback = new C07945();
    private FrameLayout mContainer;
    private Context mContext = null;
    private GuidePresenter mGuidePresenter;
    private boolean mHasAd = false;
    private boolean mHasGuideShown = false;
    private long mHomeBuildStart;
    private boolean mIsNextActiviy = false;
    private boolean mIsShowGuide = false;
    private boolean mIsShowPreviewCompleted = false;
    private onPreviewCompleteListener mListener;
    private OperateImageShower mOperateImageShower;
    private OperateImageStatusCallBack mOperateStatusCallback = new C07934();
    protected View mRootView;
    private boolean mShouldPresentSceneGuide = false;
    private boolean mShouldStartAd = false;
    private boolean mShouldStartOperate = false;
    private int mStartCount = 0;
    private Bitmap mStartOperateBitmap;
    private StartOperateImageModel mStartOperateImageModel;
    private Handler mStartUpHandler;
    private ViewPager mViewPager;

    public interface onPreviewCompleteListener {
        void onStartCompleted(boolean z);
    }

    class C07912 implements onGuideDismissListener {
        C07912() {
        }

        public void onGuideDismiss(FrameLayout rootView, ViewPager viewpager) {
            BaseStartUpPresenter.this.mViewPager = viewpager;
            GuidePreference.setShowGuideLoad(BaseStartUpPresenter.this.mContext, false);
            BaseStartUpPresenter.this.mStartUpHandler.sendEmptyMessage(256);
            BaseStartUpPresenter.this.mGuidePresenter = null;
        }
    }

    class C07923 implements OnDismissListener {
        C07923() {
        }

        public void onDismiss(DialogInterface dialog) {
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "initGuideCheckIn dialog dismiss");
            BaseStartUpPresenter.this.mStartUpHandler.removeMessages(512);
            BaseStartUpPresenter.this.mStartUpHandler.sendEmptyMessage(512);
        }
    }

    class C07934 implements OperateImageStatusCallBack {
        C07934() {
        }

        public void onFinished(boolean nextActivity) {
            if (!BaseStartUpPresenter.this.mIsNextActiviy) {
                BaseStartUpPresenter.this.mIsNextActiviy = nextActivity;
            }
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "start operate image is onFinished, nextActivity = " + nextActivity);
        }
    }

    class C07945 implements AdStatusCallBack {
        C07945() {
        }

        public void onAdPrepared() {
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "start screen ad data is prepared");
            BaseStartUpPresenter.this.mHasAd = true;
        }

        public void onError() {
            if (!BaseStartUpPresenter.this.mHasGuideShown) {
                BaseStartUpPresenter.this.mStartUpHandler.removeMessages(256);
                BaseStartUpPresenter.this.mStartUpHandler.sendEmptyMessage(256);
            }
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "start screen ad is onError");
        }

        public void onFinished() {
            if (!BaseStartUpPresenter.this.mHasGuideShown) {
                BaseStartUpPresenter.this.mStartUpHandler.removeMessages(256);
                BaseStartUpPresenter.this.mStartUpHandler.sendEmptyMessage(256);
            }
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "start screen ad is onFinished");
        }

        public void onTimeOut() {
            if (!BaseStartUpPresenter.this.mHasGuideShown) {
                BaseStartUpPresenter.this.mStartUpHandler.removeMessages(256);
                BaseStartUpPresenter.this.mStartUpHandler.sendEmptyMessage(256);
            }
            LogUtils.m1568d(BaseStartUpPresenter.TAG, "start screen ad is onTimeOut");
        }
    }

    public BaseStartUpPresenter(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "StartupPresent constructor class@ " + getClass().getName());
        }
        this.mContext = context;
        StartScreenAdHandler.instance().setRequestCallback(this.mAdStatusCallback);
        this.mStartCount = GuidePreference.getStartCount(context);
        GuidePreference.saveStartCount(context, this.mStartCount + 1);
    }

    public void start() {
    }

    protected void showAd() {
        StartScreenAdHandler.instance().showAd(this.mAdContainer);
        this.mShouldStartAd = true;
    }

    public void setOnShowPreViewListener(onPreviewCompleteListener listener) {
        this.mListener = listener;
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mIsShowPreviewCompleted || event.getKeyCode() == 25 || event.getKeyCode() == 24 || event.getKeyCode() == 91) {
            return false;
        }
        if (this.mGuidePresenter != null) {
            return this.mGuidePresenter.dispatchKeyEvent(event);
        }
        if (this.mShouldStartAd && dispatchKeyEventWhenAdShowing(event)) {
            return true;
        }
        if (this.mShouldStartOperate && dispatchKeyEventWhenStartOperateShowing(event)) {
            return true;
        }
        return true;
    }

    private boolean dispatchKeyWhenSceneGuider(KeyEvent event) {
        if (event.getAction() != 0 || event.getKeyCode() == 24 || event.getKeyCode() == 25) {
            return false;
        }
        if (event.getKeyCode() == 23 || event.getKeyCode() == 66) {
        }
        return true;
    }

    private boolean dispatchKeyEventWhenAdShowing(KeyEvent event) {
        LogUtils.m1568d(TAG, "dispatchKeyEventWhenAdShowing, keyEvent Action = " + event.getAction() + "keyEvent keyCode = " + event.getKeyCode());
        boolean isConsume = StartScreenAdHandler.instance().dispatchKeyEvent(event);
        if (isConsume) {
            this.mShouldPresentSceneGuide = false;
        }
        LogRecordUtils.logd(TAG, "dispatchKeyEventWhenAdShowing: isConsume -> " + isConsume + ", mShouldPresentSceneGuide -> " + this.mShouldPresentSceneGuide);
        return isConsume;
    }

    private boolean dispatchKeyEventWhenStartOperateShowing(KeyEvent event) {
        LogUtils.m1568d(TAG, "dispatchKeyEventWhenStartOperateShowing, keyEvent Action = " + event.getAction() + "keyEvent keyCode = " + event.getKeyCode());
        boolean isConsume = this.mOperateImageShower.dispatchKeyEvent(event);
        if (isConsume) {
            this.mShouldPresentSceneGuide = false;
        }
        LogRecordUtils.logd(TAG, "dispatchKeyEventWhenStartOperateShowing: isConsume -> " + isConsume + ", mShouldPresentSceneGuide -> " + this.mShouldPresentSceneGuide);
        return isConsume;
    }

    protected void init(Context context, Handler handler) {
        this.mRootView = LayoutInflater.from(context).inflate(C0508R.layout.epg_activity_welcome, null);
        this.mAdContainer = (RelativeLayout) this.mRootView.findViewById(C0508R.id.epg_ad_container);
        this.mStartUpHandler = handler;
        this.mHomeBuildStart = System.currentTimeMillis();
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "mHomeBuildStart = " + this.mHomeBuildStart);
        }
        if (!this.mIsShowGuide) {
            new Thread("dynamic") {
                public void run() {
                    BaseStartUpPresenter.this.mStartOperateBitmap = BaseStartUpPresenter.this.getStageTwoDynamic();
                }
            }.start();
        }
    }

    public synchronized Bitmap getStageTwoDynamic() {
        Bitmap bitmap;
        if (this.mStartOperateBitmap != null) {
            bitmap = this.mStartOperateBitmap;
        } else {
            LogUtils.m1568d(TAG, "getStageTwoDynamic");
            StartOperateImageModel dataModel = StartOperateImageProvider.getInstance().getStartOperateImageModel();
            this.mStartOperateImageModel = dataModel;
            if (dataModel != null) {
                String imagePath = dataModel.getImagePath();
                if (!TextUtils.isEmpty(imagePath)) {
                    File cacheFile = new File(imagePath);
                    if (cacheFile.exists()) {
                        this.mStartOperateBitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
                        bitmap = this.mStartOperateBitmap;
                    }
                }
            }
            bitmap = null;
        }
        return bitmap;
    }

    public void show(FrameLayout container) {
        LogUtils.m1568d(TAG, "show preview");
        this.mContainer = container;
        if (this.mContainer != null) {
            this.mContainer.addView(this.mRootView);
        }
    }

    private void dismiss() {
        LogUtils.m1568d(TAG, "dismiss");
        this.mShouldStartAd = false;
        if (this.mContainer != null) {
            this.mContainer.removeView(this.mRootView);
            StartScreenAdHandler.instance().setRequestCallback(null);
        }
    }

    protected void onPreviewFinished() {
        LogUtils.m1568d(TAG, "show preview completed");
        dismiss();
        if (this.mViewPager != null) {
            this.mContainer.removeView(this.mViewPager);
        }
        if (this.mAdContainer != null && this.mShouldStartAd) {
            this.mAdContainer.removeAllViews();
            StartScreenAdHandler.instance().stop();
        }
        if (this.mAdContainer != null && this.mShouldStartOperate) {
            this.mAdContainer.removeAllViews();
        }
        if (this.mListener != null) {
            this.mListener.onStartCompleted(this.mIsNextActiviy);
        }
        this.mIsShowPreviewCompleted = true;
        this.mContainer = null;
        this.mContext = null;
    }

    public void showOperateImage() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "mStartOperateBitmap =" + this.mStartOperateBitmap);
        }
        this.mOperateImageShower = new OperateImageShower();
        this.mOperateImageShower.showOperateImage(this.mContext, this.mAdContainer, this.mStartOperateBitmap, this.mStartOperateImageModel);
        this.mOperateImageShower.setCallBack(this.mOperateStatusCallback);
        this.mShouldStartOperate = true;
    }

    private void showGuide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "load Guide");
        }
        this.mGuidePresenter = new GuidePresenter(this.mContainer);
        this.mGuidePresenter.setOnGuideCompleteListener(new C07912());
        dismiss();
        this.mGuidePresenter.show();
    }

    protected void initGuideBoot() {
        this.mShouldPresentSceneGuide = CheckInHelper.isShowCheckInDialog();
        LogUtils.m1574i(TAG, "initGuideBoot: mShouldPresentSceneGuide -> " + this.mShouldPresentSceneGuide);
    }

    public void handleStageTow() {
        dismiss();
        if (this.mStartOperateBitmap != null) {
            this.mStartOperateBitmap.recycle();
            this.mStartOperateBitmap = null;
        }
        initGuideDialog();
    }

    private void initGuideDialog() {
        if (this.mShouldPresentSceneGuide) {
            this.mHasGuideShown = true;
            CheckInDialog checkInDialog = new CheckInDialog(this.mContext);
            checkInDialog.setStartCount(this.mStartCount);
            checkInDialog.show();
            checkInDialog.setOnDismissListener(new C07923());
            return;
        }
        this.mStartUpHandler.removeMessages(512);
        this.mStartUpHandler.sendEmptyMessage(512);
    }
}
