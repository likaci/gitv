package com.gala.video.app.epg.home.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.ResourceOperatePingbackModel;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.app.epg.home.utils.ResourceOperateImageUtils;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.startup.StartOperateImageModel;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.uikit.data.data.Model.DeviceCheckModel;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.Locale;

public class OperateImageShower {
    private static final int ELAPSE = 1000;
    private static final int PLAY_SECOND = 3;
    public static final int SHOW_TIME = 3000;
    private static final String TAG = "home/ad/OperateImageShower";
    private OperateImageStatusCallBack mCallBack;
    private Context mContext;
    private boolean mEnableJump = false;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private StartOperateImageModel mStartOperateImageModel;

    public interface OperateImageStatusCallBack {
        void onFinished(boolean z);
    }

    public void showOperateImage(Context context, View adContainer, Bitmap drawable, StartOperateImageModel model) {
        if (model == null) {
            LogUtils.w(TAG, "showOperateImage, start operate image data is null");
            return;
        }
        this.mContext = context;
        this.mStartOperateImageModel = model;
        ((ImageView) adContainer.findViewById(R.id.epg_screen_ad)).setImageBitmap(drawable);
        adContainer.bringToFront();
        final View adBadge = adContainer.findViewById(R.id.epg_tv_ad_badge);
        adBadge.setVisibility(4);
        TextView jumpTipText = (TextView) adContainer.findViewById(R.id.epg_start_ad_jump_tip);
        ItemDataType itemDataType = DataBuildTool.getItemType(model.getChannelLabel());
        if (DeviceCheckModel.getInstance().isDevCheckPass() && ResourceOperateImageUtils.isSupportJump(model.getChannelLabel()) && ResourceOperateImageUtils.isSupportResType(model.getChannelLabel())) {
            this.mEnableJump = true;
            jumpTipText.setVisibility(0);
            String text = ResourceUtil.getStr(R.string.screen_saver_click_text);
            if (text == null) {
                text = "按\nOK\n键\n了\n解\n详\n情";
                LogUtils.d(TAG, " showOperateImage , read click tips text is null from  local Resource String");
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder(text);
            ForegroundColorSpan white = new ForegroundColorSpan(-921103);
            ForegroundColorSpan white1 = new ForegroundColorSpan(-921103);
            ForegroundColorSpan orange = new ForegroundColorSpan(-19456);
            ssb.setSpan(white, 0, 1, 33);
            ssb.setSpan(orange, 2, 4, 33);
            ssb.setSpan(white1, 5, text.length(), 33);
            jumpTipText.setText(ssb);
        } else {
            if (!ResourceOperateImageUtils.isSupportJump(model.getChannelLabel())) {
                LogUtils.d(TAG, "showOperateImage, not support jump , ");
            }
            if (!DeviceCheckModel.getInstance().isDevCheckPass()) {
                LogUtils.d(TAG, "showOperateImage, device check fail");
            }
            if (!ResourceOperateImageUtils.isSupportResType(model.getChannelLabel())) {
                LogUtils.d(TAG, "showOperateImage, not support type :" + DataBuildTool.getItemType(model.getChannelLabel()));
            }
            this.mEnableJump = false;
            jumpTipText.setVisibility(4);
        }
        final TextView mTimer = (TextView) adContainer.findViewById(R.id.epg_tv_adtime);
        mTimer.setTypeface(Typeface.createFromAsset(AppRuntimeEnv.get().getApplicationContext().getAssets(), "fonts/DS-DIGI.TTF"), 1);
        adContainer.setVisibility(0);
        this.mHandler.postAtFrontOfQueue(new Runnable() {
            int mCountDown = 3;

            public void run() {
                if (this.mCountDown > 0) {
                    LogUtils.d(OperateImageShower.TAG, "showOperateImage, showing count down: " + this.mCountDown + ", current time millis:" + System.currentTimeMillis());
                    TextView textView = mTimer;
                    Object[] objArr = new Object[1];
                    int i = this.mCountDown;
                    this.mCountDown = i - 1;
                    objArr[0] = Integer.valueOf(i);
                    textView.setText(String.format(Locale.CHINA, "%d", objArr));
                    OperateImageShower.this.mHandler.postDelayed(this, 1000);
                    return;
                }
                adBadge.setVisibility(0);
                if (OperateImageShower.this.mCallBack != null) {
                    OperateImageShower.this.mCallBack.onFinished(false);
                }
            }
        });
        HomePingbackFactory.instance().createPingback(ShowPingback.START_OPERATE_PAGE_SHOW_PINGBACK).addItem("qtcurl", "start").addItem("block", this.mEnableJump ? "start_pic_jump" : "start_pic").setOthersNull().post();
    }

    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        sendPageClickPingback(keyEvent);
        if ((keyEvent.getKeyCode() != 23 && keyEvent.getKeyCode() != 66) || keyEvent.getAction() != 0) {
            return false;
        }
        if (this.mEnableJump) {
            onClick();
        }
        return true;
    }

    private void onClick() {
        if (this.mStartOperateImageModel == null) {
            LogUtils.w(TAG, "onClick ,start operate image data is null is null");
            return;
        }
        PingBackCollectionFieldUtils.setIncomeSrc("others");
        ResourceOperatePingbackModel pingbackModel = new ResourceOperatePingbackModel();
        pingbackModel.setS2("startapk");
        pingbackModel.setEnterType(13);
        pingbackModel.setIncomesrc("others");
        ResourceOperateImageUtils.onClick(this.mContext, this.mStartOperateImageModel.getChannelLabel(), pingbackModel);
        if (this.mCallBack != null) {
            this.mCallBack.onFinished(true);
        }
    }

    private void sendPageClickPingback(KeyEvent keyEvent) {
        if (keyEvent != null && keyEvent.getAction() != 1) {
            String rseat = "";
            String block = this.mEnableJump ? "start_pic_jump" : "start_pic";
            String r = (this.mStartOperateImageModel == null || !this.mEnableJump) ? "" : ResourceOperateImageUtils.getRValue(this.mStartOperateImageModel.getChannelLabel());
            if (keyEvent.getKeyCode() == 22 && keyEvent.getAction() == 0) {
                rseat = ScreenSaverPingBack.SEAT_KEY_RIGHT;
            }
            if (keyEvent.getKeyCode() == 20 && keyEvent.getAction() == 0) {
                rseat = ScreenSaverPingBack.SEAT_KEY_DOWN;
            }
            if ((keyEvent.getKeyCode() == 23 || keyEvent.getKeyCode() == 66) && keyEvent.getAction() == 0) {
                rseat = ScreenSaverPingBack.SEAT_KEY_OK;
            }
            HomePingbackFactory.instance().createPingback(ClickPingback.START_OPERATE_PAGE_CLICK_PINGBACK).addItem("rpage", "start").addItem("block", block).addItem("rseat", rseat).addItem("r", r).setOthersNull().post();
        }
    }

    public void setCallBack(OperateImageStatusCallBack callBack) {
        this.mCallBack = callBack;
    }
}
