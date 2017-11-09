package com.gala.video.app.epg.ui.netspeed;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.speedrunner.netdoctor.TVNetDoctor;
import com.gala.speedrunner.speedrunner.IRunCheckCallback;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.netspeed.model.NetSpeedSeriesDataSet;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.netdoc.FileType;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class QNetSpeedActivity extends QMultiScreenActivity {
    private static final int MESSAGE_NETWORK_ERROR = 105;
    private static final int MESSAGE_NET_SPEED_CHECK_FAILED = 104;
    private static final int MESSAGE_NET_SPEED_CHECK_FINISHED = 103;
    private static final int MESSAGE_NET_SPEED_START = 106;
    private static final int MESSAGE_NO_NETWORK = 107;
    private static final int MESSAGE_UPDATE_SPEED_CHAT = 101;
    private static final int MESSAGE_UPDATE_SPEED_INFO = 102;
    private static final String TAG = "QNetSpeedActivity";
    private static final int TOAST_DURATION = 2000;
    private List<Integer> mAverageSpeedList = new CopyOnWriteArrayList();
    private Button mBtnRestart;
    private Button mBtnSetDefaultDefinition;
    private NetSpeedSeriesDataSet mDataSet;
    private boolean mIsRunSpeeder = false;
    private List<String> mLabelsTxt = new ArrayList();
    private Handler mMainHandler;
    private TVNetDoctor mNetDoctor = null;
    private NetSpeedChatView mNetSpeedChatView;
    private OnClickListener mOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            int i = v.getId();
            PingBackParams params;
            if (i == R.id.epg_btn_restart) {
                QNetSpeedActivity.this.mDataSet.clear();
                QNetSpeedActivity.this.mAverageSpeedList.clear();
                QNetSpeedActivity.this.mBtnRestart.setVisibility(4);
                QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(4);
                QNetSpeedActivity.this.mNetSpeedChatView.clear();
                int state = NetWorkManager.getInstance().getNetState();
                if (state == 3 || state == 4) {
                    QNetSpeedActivity.this.mMainHandler.sendEmptyMessage(105);
                } else if (state == 0) {
                    QNetSpeedActivity.this.mMainHandler.sendEmptyMessage(107);
                } else {
                    QNetSpeedActivity.this.setAverageSpeed(0, false);
                    QNetSpeedActivity.this.mMainHandler.sendEmptyMessage(106);
                }
                if (QNetSpeedActivity.this.mRecommendDefinition == null || QNetSpeedActivity.this.mRecommendDefinition.equals("")) {
                    params = new PingBackParams();
                    params.add(Keys.T, "20").add("block", "result_lowspeed").add("rt", "i").add("rseat", "retry").add("rpage", Keys.SPEED);
                    PingBack.getInstance().postPingBackToLongYuan(params.build());
                    return;
                }
                params = new PingBackParams();
                params.add(Keys.T, "20").add("block", "result_normal").add("rt", "i").add("rseat", "retry").add("rpage", Keys.SPEED);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            } else if (i == R.id.epg_btn_set_default_definition) {
                SettingPlayPreference.setStreamType(AppRuntimeEnv.get().getApplicationContext(), QNetSpeedActivity.this.mRecommendDefinitionType);
                QToast.makeTextAndShow(QNetSpeedActivity.this.getApplication(), QNetSpeedActivity.this.getResources().getString(R.string.txt_net_speed_recommend_toast) + QNetSpeedActivity.this.mTextRecommendDef.getText(), 2000);
                params = new PingBackParams();
                params.add(Keys.T, "20").add("block", "result_normal").add("rt", "i").add("rseat", "setstream").add("rpage", Keys.SPEED);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
        }
    };
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (!(v instanceof Button)) {
                return;
            }
            if (hasFocus) {
                ((Button) v).setTextColor(QNetSpeedActivity.this.getResources().getColor(R.color.gala_write));
            } else {
                ((Button) v).setTextColor(QNetSpeedActivity.this.getResources().getColor(R.color.long_history_title_color));
            }
        }
    };
    private String mRecommendDefinition = "";
    private int mRecommendDefinitionType = 2;
    private LinearLayout mSpeedInfoContainer;
    private IRunCheckCallback mSpeedRunnerCallback = new IRunCheckCallback() {
        public void onDownloadProgress(String arg0, int percent, int speed) {
            LogUtils.d(QNetSpeedActivity.TAG, "net speed check is running, current percent = " + percent + ", current speed = " + speed);
            QNetSpeedActivity.this.mDataSet.add(percent, QNetSpeedActivity.KBToKb(speed));
            QNetSpeedActivity.this.mAverageSpeedList.add(Integer.valueOf(QNetSpeedActivity.KBToKb(speed)));
            QNetSpeedActivity.this.mMainHandler.sendEmptyMessage(101);
            Message msg = QNetSpeedActivity.this.mMainHandler.obtainMessage();
            msg.what = 102;
            msg.arg1 = QNetSpeedActivity.KBToKb(speed);
            msg.sendToTarget();
        }

        public void onFailed(String result) {
            LogUtils.e(QNetSpeedActivity.TAG, "net speed check faild info = " + result);
            QNetSpeedActivity.this.mMainHandler.sendEmptyMessage(104);
        }

        public void onReportStatus(String arg0, int arg1) {
        }

        public void onSendLogResult(int arg0) {
        }

        public void onSuccess(int step, int speed, String result) {
            Message msg = QNetSpeedActivity.this.mMainHandler.obtainMessage();
            msg.what = 103;
            msg.arg1 = QNetSpeedActivity.KBToKb(speed);
            msg.sendToTarget();
        }

        public void onTestResult(String arg0, String arg1) {
            LogUtils.d(QNetSpeedActivity.TAG, "speed check result arg0=" + arg0 + "arg1=" + arg1);
        }
    };
    private TextView mTextAve;
    private TextView mTextRecommendDef;
    private TextView mTextRecommendDefIndicator;
    private TextView mTextStatus;

    private class NetSpeedHandler extends Handler {
        public NetSpeedHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 101:
                    QNetSpeedActivity.this.mNetSpeedChatView.setDataSet(QNetSpeedActivity.this.mDataSet);
                    return;
                case 102:
                    QNetSpeedActivity.this.mTextStatus.setVisibility(4);
                    QNetSpeedActivity.this.mSpeedInfoContainer.setVisibility(0);
                    QNetSpeedActivity.this.setAverageSpeed(QNetSpeedActivity.this.getAverageSpeed(), false);
                    QNetSpeedActivity.this.setSharpness(QNetSpeedActivity.this.getAverageSpeed(), false);
                    return;
                case 103:
                    int result_average = msg.arg1;
                    QNetSpeedActivity.this.setSharpness(result_average, true);
                    QNetSpeedActivity.this.mNetSpeedChatView.setAllDataSet(QNetSpeedActivity.this.mDataSet, QNetSpeedActivity.this.mRecommendDefinition);
                    SettingSharepreference.saveNetSpeedResult(QNetSpeedActivity.this.getApplicationContext(), QNetSpeedActivity.this.getSpeedDisplayStrKb(result_average));
                    QNetSpeedActivity.this.setAverageSpeed(result_average, true);
                    QNetSpeedActivity.this.cancelSpeedRun();
                    PingBackParams params;
                    if (result_average < 400) {
                        QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(8);
                        QNetSpeedActivity.this.mBtnRestart.setVisibility(0);
                        QNetSpeedActivity.this.mBtnRestart.requestFocus();
                        params = new PingBackParams();
                        params.add(Keys.T, "11").add("ct", "150518_speed").add(Keys.RA, "0").add(Keys.SPEED, String.valueOf(result_average));
                        PingBack.getInstance().postPingBackToLongYuan(params.build());
                        return;
                    }
                    QNetSpeedActivity.this.mBtnRestart.setVisibility(0);
                    QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(0);
                    QNetSpeedActivity.this.mBtnSetDefaultDefinition.requestFocus();
                    params = new PingBackParams();
                    params.add(Keys.T, "11").add("ct", "150518_speed").add(Keys.RA, String.valueOf(QNetSpeedActivity.this.mRecommendDefinitionType)).add(Keys.SPEED, String.valueOf(result_average));
                    PingBack.getInstance().postPingBackToLongYuan(params.build());
                    return;
                case 104:
                    QNetSpeedActivity.this.mSpeedInfoContainer.setVisibility(4);
                    QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(8);
                    QNetSpeedActivity.this.mBtnRestart.setVisibility(0);
                    QNetSpeedActivity.this.mBtnRestart.requestFocus();
                    QNetSpeedActivity.this.mTextStatus.setVisibility(0);
                    QNetSpeedActivity.this.mTextStatus.setText(QNetSpeedActivity.this.getString(R.string.speed_test_server_error));
                    QNetSpeedActivity.this.cancelSpeedRun();
                    return;
                case 105:
                    QNetSpeedActivity.this.mTextStatus.setVisibility(0);
                    QNetSpeedActivity.this.mSpeedInfoContainer.setVisibility(4);
                    QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(8);
                    QNetSpeedActivity.this.mBtnRestart.setVisibility(0);
                    QNetSpeedActivity.this.mBtnRestart.requestFocus();
                    QNetSpeedActivity.this.mTextStatus.setText(QNetSpeedActivity.this.getString(R.string.speed_test_network_wire_error));
                    return;
                case 106:
                    QNetSpeedActivity.this.mSpeedInfoContainer.setVisibility(4);
                    QNetSpeedActivity.this.mTextStatus.setVisibility(0);
                    QNetSpeedActivity.this.mTextStatus.setText(QNetSpeedActivity.this.getString(R.string.speed_test_server_gitv));
                    QNetSpeedActivity.this.doSpeedRun();
                    return;
                case 107:
                    QNetSpeedActivity.this.mSpeedInfoContainer.setVisibility(4);
                    QNetSpeedActivity.this.mTextStatus.setVisibility(0);
                    QNetSpeedActivity.this.mBtnSetDefaultDefinition.setVisibility(8);
                    QNetSpeedActivity.this.mBtnRestart.setVisibility(0);
                    QNetSpeedActivity.this.mBtnRestart.requestFocus();
                    QNetSpeedActivity.this.mTextStatus.setText(QNetSpeedActivity.this.getString(R.string.result_no_net));
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.mNetDoctor = new TVNetDoctor();
        this.mNetDoctor.initNetDoctor(TVApiBase.getTVApiProperty().getPassportDeviceId(), Project.getInstance().getBuild().getDomainName());
        this.mMainHandler = new NetSpeedHandler(getMainLooper());
        init();
    }

    protected int getLayoutResId() {
        return R.layout.epg_activity_net_speed;
    }

    private void init() {
        this.mNetSpeedChatView = (NetSpeedChatView) findViewById(R.id.epg_net_speed_chat_view);
        this.mBtnSetDefaultDefinition = (Button) findViewById(R.id.epg_btn_set_default_definition);
        this.mBtnSetDefaultDefinition.setOnClickListener(this.mOnClickListener);
        this.mBtnRestart = (Button) findViewById(R.id.epg_btn_restart);
        this.mBtnRestart.setOnClickListener(this.mOnClickListener);
        this.mBtnSetDefaultDefinition.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mBtnRestart.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mSpeedInfoContainer = (LinearLayout) findViewById(R.id.epg_net_speed_info);
        this.mTextAve = (TextView) findViewById(R.id.epg_net_speed_avarage_value);
        this.mTextRecommendDef = (TextView) findViewById(R.id.epg_recommend_definition_value);
        this.mTextRecommendDefIndicator = (TextView) findViewById(R.id.epg_recommend_definition_txt);
        this.mTextStatus = (TextView) findViewById(R.id.epg_net_speed_status);
        this.mTextStatus.setText(getString(R.string.speed_test_server_gitv));
        this.mDataSet = new NetSpeedSeriesDataSet();
        this.mLabelsTxt.add(getResources().getString(R.string.definition_standard));
        this.mLabelsTxt.add(getResources().getString(R.string.definition_high));
        this.mLabelsTxt.add(getResources().getString(R.string.definition_720P));
        this.mLabelsTxt.add(getResources().getString(R.string.definition_1080P));
        this.mNetSpeedChatView.setLabels(this.mLabelsTxt);
        checkNetwork();
    }

    private static int KBToKb(int speed) {
        return speed * 8;
    }

    private static String creatLowSpeedTipForHtml() {
        return "<html><head></head><body><p><font color='#" + ResourceUtil.getColorLength6(R.color.skin_netspeed_status) + "'>你的网络速度</font>" + "<font color='#" + ResourceUtil.getColorLength6(R.color.skin_net_yellow_tip_txt) + "'>偏低</font>" + "<font color='#" + ResourceUtil.getColorLength6(R.color.skin_netspeed_status) + "'>，可能无法流畅观看在线视频</font><P>" + "</body></html>";
    }

    private void setSharpness(int kb, boolean isfinished) {
        LogUtils.d(TAG, "setSharpness, iFinished = " + isfinished);
        if (kb < 400) {
            this.mTextRecommendDefIndicator.setVisibility(8);
            this.mTextRecommendDef.setText(Html.fromHtml(creatLowSpeedTipForHtml()));
            this.mRecommendDefinition = "";
            if (isfinished) {
                PingBackParams params = new PingBackParams();
                params.add(Keys.T, "21").add("bstp", "1").add("qtcurl", Keys.SPEED).add("block", "result_lowspeed");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                return;
            }
            return;
        }
        this.mTextRecommendDefIndicator.setVisibility(0);
        if (isfinished) {
            if (kb >= 400) {
                params = new PingBackParams();
                params.add(Keys.T, "21").add("bstp", "1").add("qtcurl", Keys.SPEED).add("block", "result_normal");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
            this.mTextRecommendDef.setTextColor(getResources().getColor(R.color.net_chat_bottom_description_hight_light));
        } else {
            this.mTextRecommendDef.setTextColor(getResources().getColor(R.color.net_chat_bottom_description_normal));
        }
        if (kb >= 400 && kb < 1024) {
            this.mRecommendDefinitionType = 1;
            this.mRecommendDefinition = getString(R.string.definition_standard);
        } else if (kb >= 1024 && kb < 3072) {
            this.mRecommendDefinitionType = 2;
            this.mRecommendDefinition = getString(R.string.definition_high);
        } else if (kb >= 3072 && kb < 5120) {
            this.mRecommendDefinitionType = 4;
            this.mRecommendDefinition = getString(R.string.definition_720P);
        } else if (kb < 5120 || kb >= NetSpeedSeriesDataSet.P4K_DEFINITION) {
            this.mRecommendDefinitionType = 5;
            this.mRecommendDefinition = getString(R.string.definition_1080P);
        } else {
            this.mRecommendDefinitionType = 5;
            this.mRecommendDefinition = getString(R.string.definition_1080P);
        }
        this.mTextRecommendDef.setText(this.mRecommendDefinition);
    }

    private void checkNetwork() {
        NetWorkManager.getInstance().checkNetWork(new StateCallback() {
            public void getStateResult(int state) {
                QNetSpeedActivity.this.onNetworkState(state);
            }
        });
    }

    private void onNetworkState(int state) {
        switch (state) {
            case 0:
                this.mMainHandler.sendEmptyMessage(107);
                return;
            case 1:
            case 2:
                this.mMainHandler.sendEmptyMessage(106);
                return;
            case 3:
            case 4:
                this.mMainHandler.sendEmptyMessage(105);
                return;
            default:
                return;
        }
    }

    private void setAverageSpeed(int kb, boolean isFinished) {
        if (isFinished) {
            this.mTextAve.setTextColor(getResources().getColor(R.color.net_chat_bottom_description_hight_light));
        } else {
            this.mTextAve.setTextColor(getResources().getColor(R.color.net_chat_bottom_description_normal));
        }
        this.mTextAve.setText(getSpeedDisplayStrKb(kb));
    }

    private String getSpeedDisplayStrKb(int kb) {
        if (kb < 1024) {
            return kb + "Kb/s";
        }
        return new DecimalFormat("0.0").format((double) (((float) kb) / 1024.0f)) + "Mb/s";
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_root);
    }

    protected void onDestroy() {
        super.onDestroy();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                QNetSpeedActivity.this.cancelSpeedRun();
            }
        });
    }

    private int getAverageSpeed() {
        int sum = 0;
        for (Integer intValue : this.mAverageSpeedList) {
            sum += intValue.intValue();
        }
        if (this.mAverageSpeedList.size() > 0) {
            return sum / this.mAverageSpeedList.size();
        }
        return 0;
    }

    private void doSpeedRun() {
        Log.d(TAG, "net speed check is started!");
        if (!this.mIsRunSpeeder) {
            this.mNetDoctor.setSpeedListener(this.mSpeedRunnerCallback);
            this.mIsRunSpeeder = true;
        }
        checkPlay();
    }

    private void checkPlay() {
        GetInterfaceTools.getIHistoryCacheManager().loadHistoryList(1, 1, 0, new IHistoryResultCallBack() {
            public void onSuccess(List<Album> list, int total) {
                QNetSpeedActivity.this.mNetDoctor.checkPlay(QNetSpeedActivity.this.getApplicationContext(), FileType.TYPE_F4V, 0, NetSpeedUtils.getOneAlbumProvider(list), "");
            }
        });
    }

    private void cancelSpeedRun() {
        if (this.mIsRunSpeeder) {
            this.mNetDoctor.stopPlay();
            this.mIsRunSpeeder = false;
        }
    }

    public void onBackPressed() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                QNetSpeedActivity.this.cancelSpeedRun();
            }
        });
        super.onBackPressed();
    }
}
