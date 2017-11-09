package com.gala.video.lib.share.ifimpl.openplay.broadcast.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.common.configs.IntentConfig.BroadcastAction;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.BroadcastManager;
import com.gala.video.lib.share.ifimpl.openplay.broadcast.utils.OpenPlayIntentUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.BaseAction.LoadingCallback;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.webview.utils.WebSDKConstants;
import org.json.JSONObject;

public class LoadingActivity extends Activity {
    private final String TAG = "openplay/broadcast/LoadingActivity";
    Handler handler = new C17031();
    boolean isCancel = false;
    private boolean mIsExtraPageEnable = false;
    View mRootView;

    class C17031 extends Handler {
        C17031() {
        }

        public void handleMessage(Message msg) {
            LoadingActivity.this.mRootView.setVisibility(0);
        }
    }

    class MyLoadingCallback implements LoadingCallback {
        Intent mIntent;

        public MyLoadingCallback(Intent intent) {
            this.mIntent = intent;
        }

        private void sendPingback(boolean isSuccess, String currentPage) {
            String channel = "";
            String page = currentPage;
            String td = "";
            String st = "";
            String openmode = "";
            PingBackCollectionFieldUtils.setIncomeSrc("openapi");
            try {
                Bundle bundle = this.mIntent.getExtras();
                JSONObject playDict = OpenPlayIntentUtils.parsePlayInfo(bundle);
                channel = playDict.optString(WebSDKConstants.PARAM_KEY_CUSTOMER);
                String action = OpenPlayIntentUtils.splitAction(LoadingActivity.this, this.mIntent.getAction());
                if (StringUtils.isEmpty((CharSequence) page)) {
                    if (action.equals(BroadcastAction.ACTION_ALBUMLIST)) {
                        page = playDict.optString("listType");
                    } else {
                        page = action.replace("ACTION_", "").toLowerCase();
                    }
                }
                openmode = bundle.getBoolean("isFromBroadcast", false) ? IAlbumConfig.BUY_SOURCE_BROADCAST : "activity";
                td = (System.currentTimeMillis() - bundle.getLong("startTime", -1)) + "";
                st = isSuccess ? "1" : "0";
            } catch (Exception exception) {
                LogUtils.m1571e("openplay/broadcast/LoadingActivity", "[UNKNOWN-EXCEPTION] [reason:exception occurred when send pingback][Exception:" + exception.getMessage() + AlbumEnterFactory.SIGN_STR);
                exception.printStackTrace();
            }
            String entermode = page.equals("home") ? "0" : "1";
            PingBackInitParams initParams = PingBack.getInstance().getPingbackInitParams();
            initParams.sEnterMode = entermode;
            initParams.sChannel = channel;
            PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), initParams);
            LogUtils.m1568d("openplay/broadcast/LoadingActivity", "sendPingback---entermode = " + entermode + " ;channel =  " + channel + " ;openmode = " + openmode + " ;td = " + td + " ;page = " + page + " ;st = " + st);
            PingBackParams params = new PingBackParams();
            params.add(Keys.f2035T, "11").add("ct", "160718_out").add(Keys.PAGE, page).add("td", td).add("st", st).add(Keys.OPEN_MODE, openmode);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }

        public void onCancel() {
            LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity.this is Null ?" + LoadingActivity.this);
            if (LoadingActivity.this != null) {
                LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity.this.isFinishing() ?" + LoadingActivity.this.isFinishing());
                if (!LoadingActivity.this.isFinishing()) {
                    LoadingActivity.this.finish();
                    LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity.this.finish().");
                }
            }
        }

        public void onFail() {
            sendPingback(false, "");
            if (LoadingActivity.this != null) {
                LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity.this.isFinishing() ?" + LoadingActivity.this.isFinishing());
                if (!LoadingActivity.this.isFinishing()) {
                    LoadingActivity.this.finish();
                    LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity.this.finish().");
                }
            }
            if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.BROADCAST, HOSTSTATUS.FAIL);
            }
        }

        public boolean iscancel() {
            return LoadingActivity.this.isCancel;
        }

        public void onNetworkAvaliable() {
            if (LoadingActivity.this != null) {
                QToast.makeText(LoadingActivity.this, C1632R.string.no_network, 2000).show();
            }
        }

        public void onSuccess(String page) {
            sendPingback(true, page);
        }

        public void onSuccess() {
            sendPingback(true, "");
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mIsExtraPageEnable = Project.getInstance().getBuild().enableExtraPage();
        LogUtils.m1568d("openplay/broadcast/LoadingActivity", "onCreate--mIsExtraPageEnable= " + this.mIsExtraPageEnable);
        if (this.mIsExtraPageEnable) {
            setContentView(C1632R.layout.share_external_loading_page_activity);
        } else {
            setContentView(C1632R.layout.share_external_loading_dialog_activity);
            this.mRootView = findViewById(C1632R.id.share_activity_dialog_layout);
            this.handler.sendEmptyMessageDelayed(0, 500);
        }
        this.isCancel = false;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            LogUtils.m1571e("openplay/broadcast/LoadingActivity", "[INVALID-PARAMTER] [reason:missing playInfo bundle] [the bundle of the intent is null.]");
            finish();
            return;
        }
        if (!bundle.getBoolean("isFromBroadcast", false)) {
            bundle.putLong("startTime", System.currentTimeMillis());
            intent.putExtras(bundle);
        }
        process(this, intent);
    }

    protected void onPause() {
        super.onPause();
        this.isCancel = true;
        finish();
    }

    void process(final Context context, final Intent intent) {
        new Thread8K(new Runnable() {
            public void run() {
                if (LoadingActivity.this.mIsExtraPageEnable) {
                    try {
                        Thread.currentThread();
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        LogUtils.m1571e("openplay/broadcast/LoadingActivity", "[UNKNOWN-EXCEPTION] [reason:exception occurred when LoadingActivity sleep.][Exception:" + e.getMessage() + AlbumEnterFactory.SIGN_STR);
                        e.printStackTrace();
                    }
                }
                String action = intent != null ? intent.getAction() : "";
                LogUtils.m1568d("openplay/broadcast/LoadingActivity", "onReceive action : " + action);
                String actionSuffix = OpenPlayIntentUtils.splitAction(context, action);
                LogUtils.m1568d("openplay/broadcast/LoadingActivity", "onReceive actionSuffix : " + actionSuffix);
                BaseAction baseAcion = BroadcastManager.instance().findBroadcastActionByKey(actionSuffix);
                LogUtils.m1568d("openplay/broadcast/LoadingActivity", "baseAcion = " + baseAcion);
                if (baseAcion == null) {
                    LogUtils.m1571e("openplay/broadcast/LoadingActivity", "[NOT-AUTHORIZED] [action : " + actionSuffix + "] [supportList : " + BroadcastManager.instance().getSupportActionList() + AlbumEnterFactory.SIGN_STR);
                    LoadingActivity.this.finish();
                    return;
                }
                baseAcion.process(LoadingActivity.this, intent, new MyLoadingCallback(intent));
            }
        }, "LoadingActivity").start();
    }

    protected void onDestroy() {
        super.onDestroy();
        LogUtils.m1568d("openplay/broadcast/LoadingActivity", "LoadingActivity---onDestroy() ");
    }
}
