package com.gala.video.app.epg.ui.solotab;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher.IMessageNotification;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import java.lang.ref.WeakReference;

public class SoloTabActivity extends QMultiScreenActivity {
    private static final String TAG = "SoloTabActivity";
    private SoloTabTopPresenter mActionBarPresenter;
    private IMessageNotificationImp mMessageNotification;
    private View mRootView;
    private SoloTabManage mSoloTabManage;

    private static class IMessageNotificationImp implements IMessageNotification {
        WeakReference<SoloTabActivity> mOuter;

        public IMessageNotificationImp(SoloTabActivity outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onMessageReceive(IMsgContent content) {
            SoloTabActivity outer = (SoloTabActivity) this.mOuter.get();
            if (outer != null) {
                outer.mActionBarPresenter.onMessageReceive(content);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_solotab);
        getWindow().setFormat(-2);
        ImageProviderApi.getImageProvider().stopAllTasks();
        setPingbackPage(PingbackPage.SoloTab);
        SoloTabInfoModel infoModel = getInfoModel();
        this.mSoloTabManage = new SoloTabManage(this, getBackgroundContainer(), infoModel);
        this.mActionBarPresenter = new SoloTabTopPresenter(this, findViewById(R.id.epg_solo_tab_top_panel), infoModel, this.mSoloTabManage);
        this.mSoloTabManage.initialize();
        this.mMessageNotification = new IMessageNotificationImp(this);
    }

    private SoloTabInfoModel getInfoModel() {
        Intent intent = getIntent();
        if (intent != null) {
            try {
                SoloTabInfoModel infoModel = (SoloTabInfoModel) intent.getSerializableExtra("intent_model");
                if (infoModel != null && !TextUtils.isEmpty(infoModel.getSourceId())) {
                    return infoModel;
                }
                Log.e(TAG, "SoloTabActivity.onCreate(), finish, intent" + intent + ",infoModel=" + infoModel);
                finish();
                return infoModel;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    protected void onResume() {
        super.onResume();
        this.mSoloTabManage.onResume();
        this.mActionBarPresenter.startVipAnimation(false);
    }

    protected void onPause() {
        super.onPause();
        this.mSoloTabManage.onPause();
        this.mActionBarPresenter.stopVipAnimation();
    }

    protected void onStart() {
        super.onStart();
        MessagePromptDispatcher.get().register(this.mMessageNotification);
        this.mActionBarPresenter.onStart();
    }

    protected void onStop() {
        super.onStop();
        MessagePromptDispatcher.get().unregister(this.mMessageNotification);
        this.mSoloTabManage.onStop();
        this.mActionBarPresenter.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mSoloTabManage.onDestroy();
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 4 || keyCode == 111) {
            ImageProviderApi.getImageProvider().stopAllTasks();
        }
        return super.handleKeyEvent(event);
    }

    protected View getBackgroundContainer() {
        if (this.mRootView == null) {
            this.mRootView = getWindow().getDecorView().findViewById(16908290);
        }
        return this.mRootView;
    }
}
