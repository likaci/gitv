package com.gala.video.app.epg.home.widget.guidelogin;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.widget.checkin.CheckInTimeHelper;
import com.gala.video.app.epg.home.widget.guidelogin.CheckInView.OnCheckClickListener;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ResourceUtil;

public class CheckInDialog extends GuideBaseDialog {
    private static final String TAG = "CheckInDialog";
    private CheckInView mCheckInView;
    private long mFirstShowTime;
    private int mStartCount = 0;

    class C07101 implements OnCheckClickListener {
        C07101() {
        }

        public void onClick(View v) {
            LogUtils.m1574i(CheckInDialog.TAG, "onClick: v" + v);
            if (v.getId() == C0508R.id.check_in) {
                CheckInTimeHelper.setShowedState();
                CheckInHelper.startCheckInActivity(CheckInDialog.this.mContext);
                CheckInDialog.this.sendClickPingback("sign");
                CheckInDialog.this.dismiss();
                return;
            }
            CheckInDialog.this.sendClickPingback("home");
            CheckInDialog.this.dismiss();
        }
    }

    public CheckInDialog(Context context) {
        super(context);
    }

    public void onCreate() {
        initializeParams();
        this.mCheckInView = new CheckInView(this.mContext);
        setContentView(this.mCheckInView);
        this.mCheckInView.setOnClickListener(new C07101());
    }

    private void initializeParams() {
        LayoutParams layoutParams = getWindow().getAttributes();
        getWindow().setGravity(51);
        layoutParams.x = ResourceUtil.getDimen(C0508R.dimen.dimen_279dp);
        layoutParams.y = ResourceUtil.getDimen(C0508R.dimen.dimen_2dp);
        getWindow().setAttributes(layoutParams);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == 4) {
            sendClickPingback("back");
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void show() {
        this.mFirstShowTime = System.currentTimeMillis();
        try {
            if (!(getContext() instanceof Activity)) {
                super.show();
                sendShowPingback();
                CheckInHelper.setCloseSign();
            } else if (!((Activity) getContext()).isFinishing()) {
                super.show();
                sendShowPingback();
                CheckInHelper.setCloseSign();
            }
        } catch (Exception e) {
            LogUtils.m1578w(TAG, "show CheckInDialog is failed.", e);
        }
    }

    public void dismiss() {
        try {
            if (!(getContext() instanceof Activity)) {
                destroy();
                super.dismiss();
            } else if (!((Activity) getContext()).isFinishing()) {
                destroy();
                super.dismiss();
            }
        } catch (Exception e) {
            LogUtils.m1578w(TAG, "dismiss CheckInDialog is failed.", e);
        }
    }

    private void destroy() {
        this.mContext = null;
        if (this.mCheckInView != null) {
            this.mCheckInView.setVisibility(8);
            this.mCheckInView.removeAllViews();
            this.mCheckInView = null;
        }
    }

    public void setStartCount(int startCount) {
        this.mStartCount = startCount;
    }

    private void sendShowPingback() {
        Log.i(TAG, "sendShowPingback: ");
        HomePingbackFactory.instance().createPingback(ShowPingback.SCENE_INSIDE_GUIDE_CHECK_IN_IMAGE_SHOW_PINGBACK).addItem(Keys.f2035T, "21").addItem("bstp", "1").addItem("qtcurl", "appstartsign").addItem("block", this.mStartCount > 0 ? ISearchConstant.TVSRCHSOURCE_OTHER : "first").addItem("c1", "").addItem("qpid", "").post();
    }

    private void sendClickPingback(String rseat) {
        LogUtils.m1574i(TAG, "sendClickPingback rseat: " + rseat);
        HomePingbackFactory.instance().createPingback(ClickPingback.SCENE_INSIDE_GUIDE_CHECK_IN_IMAGE_CLICK_PINGBACK).addItem(Keys.f2035T, "20").addItem("rpage", "appstartsign").addItem("block", this.mStartCount > 0 ? ISearchConstant.TVSRCHSOURCE_OTHER : "first").addItem("rseat", rseat).addItem("c1", "").addItem("r", "").addItem("td", String.valueOf(System.currentTimeMillis() - this.mFirstShowTime)).addItem("rt", "i").addItem("tabid", "").post();
    }
}
