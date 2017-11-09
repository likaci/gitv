package com.gala.video.app.epg.ui.netdiagnose;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.report.core.upload.config.LogRecordConfigUtils;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.sdk.player.BitStream;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.netdiagnose.view.NetTestingView;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDWrapperOperate;
import com.gala.video.lib.framework.coreservice.netdiagnose.INetDiagnoseController;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseController;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.SourceType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.UploadResultControllerListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaAccountManager;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.utils.DataUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetDiagnoseActivity extends QMultiScreenActivity {
    public static final int NETDIAGNOSE_TYPE_COMMON = 0;
    public static final int NETDIAGNOSE_TYPE_PLAYER = 1;
    private String TAG = "NetDiag/NetDiagnoseActivity";
    private Animation animation;
    private View mBtnCancel;
    private View mBtnReport;
    private View mBtnRetry;
    private INDDoneListener mCdnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseInfo result = (NetDiagnoseInfo) resultMap.get("data");
            NetDiagnoseActivity.this.setReslut(result);
            if (((Boolean) resultMap.get("success")).booleanValue()) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NetDiagnoseActivity.this.TAG, "onCdnDiagnoseSuccess( AvgSpeed:" + result.getCdnDiagnoseAvgSpeed() + ")");
                }
                NetDiagnoseActivity.this.mViewTesting.setAverageSpeed(result.getCdnDiagnoseAvgSpeed());
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.CDN_CONNECTED);
                return;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDiagnoseActivity.this.TAG, "onCdnDiagnoseFailed(" + result + ")");
            }
            NetDiagnoseActivity.this.updateUI(DiagnoseStatus.CDN_DISCONNECTED);
        }
    };
    private OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.epg_btn_dianose_cancel) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NetDiagnoseActivity.this.TAG, "finish");
                }
                NetDiagnoseActivity.this.finish();
            } else if (i == R.id.epg_btn_dianose_retry) {
                NetDiagnoseActivity.this.mIsSendedFeedbackSuccess = false;
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NetDiagnoseActivity.this.TAG, "btn_dianose_retry ");
                }
                NetDiagnoseActivity.this.mController.stopCheck();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NetDiagnoseActivity.this.TAG, "retry");
                }
                NetDiagnoseActivity.this.mBtnCancel.setVisibility(0);
                NetDiagnoseActivity.this.mBtnRetry.setVisibility(8);
                NetDiagnoseActivity.this.mBtnReport.setVisibility(8);
                NetDiagnoseActivity.this.initData();
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.TESTING_BEGIN);
                NetDiagnoseActivity.this.mController.startCheckEx(NetDiagnoseActivity.this.mWrapperList);
            } else if (i == R.id.epg_btn_dianose_report) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(NetDiagnoseActivity.this.TAG, "report");
                }
                if (NetDiagnoseActivity.this.mIsSendingFeedback) {
                    QToast.makeTextAndShow(NetDiagnoseActivity.this.mContext, R.string.feedback_sending, 2000);
                } else if (NetDiagnoseActivity.this.mIsSendedFeedbackSuccess) {
                    QToast.makeTextAndShow(NetDiagnoseActivity.this.mContext, R.string.feedback_retry, 2000);
                } else {
                    NetDiagnoseActivity.this.sendResult();
                }
            }
        }
    };
    private INDDoneListener mCollectListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseActivity.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            if (((Boolean) resultMap.get("success")).booleanValue()) {
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.COLLECTON_SUCCESS);
            } else {
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.COLLECTON_FAIL);
            }
        }
    };
    private Context mContext;
    private INetDiagnoseController mController;
    private int mCurrentNetDiagnoseType = 0;
    private INDDoneListener mDnsListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseActivity.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            NetDiagnoseActivity.this.updateUI(DiagnoseStatus.DNS_DONE);
        }
    };
    private boolean mIsSendedFeedbackSuccess;
    private boolean mIsSendingFeedback;
    private INDDoneListener mNetConnListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseInfo result = (NetDiagnoseInfo) resultMap.get("data");
            NetDiagnoseActivity.this.setReslut(result);
            boolean success = ((Boolean) resultMap.get("success")).booleanValue();
            int state = result.getNetConnDiagnoseResult();
            if (success) {
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.ROUTER_CONNECTED);
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.INTERNET_CONNECTED);
            } else if (state == 0) {
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.ROUTER_DISCONNECTED);
            } else {
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.ROUTER_CONNECTED);
                NetDiagnoseActivity.this.updateUI(DiagnoseStatus.INTERNET_DISCONNECTED);
            }
        }
    };
    private INDDoneListener mNsLookUp = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseActivity.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            NetDiagnoseActivity.this.updateUI(DiagnoseStatus.NSLOOKUP_DONE);
        }
    };
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            if (v instanceof LinearLayout) {
                View txtView = ((LinearLayout) v).getChildAt(0);
                if (txtView != null && (txtView instanceof TextView)) {
                    if (hasFocus) {
                        ((TextView) txtView).setTextColor(NetDiagnoseActivity.this.getResources().getColor(R.color.gala_write));
                    } else {
                        ((TextView) txtView).setTextColor(NetDiagnoseActivity.this.getResources().getColor(R.color.setting_white));
                    }
                }
            }
        }
    };
    private ImageView mProgressBar;
    private NetDiagnoseInfo mResult;
    private TextView mTextProgress;
    private INDDoneListener mThirdSpeedListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseActivity.this.setReslut((NetDiagnoseInfo) resultMap.get("data"));
            NetDiagnoseActivity.this.updateUI(DiagnoseStatus.SPEED_TEST_DONE);
        }
    };
    private INDDoneListener mTraceRouteListener = new INDDoneListener() {
        public void onFinish(Map<String, Object> resultMap) {
            NetDiagnoseInfo result = (NetDiagnoseInfo) resultMap.get("data");
            NetDiagnoseActivity.this.setReslut(result);
            LogUtils.d(NetDiagnoseActivity.this.TAG, "mTraceRouteListener: " + result.getDnsResult());
            NetDiagnoseActivity.this.updateUI(DiagnoseStatus.TRACEROUT_DONE);
        }
    };
    private INDUploadCallback mUploadcallback = new INDUploadCallback() {
        public void uploadNetDiagnoseDone() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(NetDiagnoseActivity.this.TAG, "uploadNetDiagnoseDone ");
            }
            NetDiagnoseActivity.this.mController.stopCheck();
        }
    };
    private NetTestingView mViewTesting;
    private List<INDWrapperOperate> mWrapperList = null;

    public enum DiagnoseStatus {
        TESTING_BEGIN,
        ROUTER_CONNECTED,
        ROUTER_DISCONNECTED,
        INTERNET_CONNECTED,
        INTERNET_DISCONNECTED,
        CDN_CONNECTED,
        CDN_DISCONNECTED,
        COLLECTON_SUCCESS,
        COLLECTON_FAIL,
        SPEED_TEST_DONE,
        TRACEROUT_DONE,
        DNS_DONE,
        NSLOOKUP_DONE
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onCreate ");
        }
        setContentView(R.layout.epg_activity_net_diagnose);
        this.mContext = this;
        initViews();
    }

    private void initViews() {
        if (getIntent().getExtras() != null) {
            this.mCurrentNetDiagnoseType = getIntent().getExtras().getInt(IntentConfig2.INTENT_PARAM_NETDIAGNOSE_SOURCE, 0);
        }
        this.mViewTesting = (NetTestingView) findViewById(R.id.epg_layout_testing_id);
        this.mViewTesting.setVisibility(0);
        this.mViewTesting.setNeedShowNetSpeedResult(this.mCurrentNetDiagnoseType == 1);
        this.mBtnCancel = this.mViewTesting.findViewById(R.id.epg_btn_dianose_cancel);
        this.mBtnCancel.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mBtnCancel.requestFocus();
        this.mBtnCancel.setOnClickListener(this.mClickListener);
        this.mBtnRetry = this.mViewTesting.findViewById(R.id.epg_btn_dianose_retry);
        this.mBtnRetry.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mBtnRetry.setVisibility(8);
        this.mBtnRetry.setOnClickListener(this.mClickListener);
        this.mBtnReport = this.mViewTesting.findViewById(R.id.epg_btn_dianose_report);
        this.mBtnReport.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mBtnReport.setVisibility(8);
        this.mBtnReport.setOnClickListener(this.mClickListener);
        this.mTextProgress = (TextView) this.mViewTesting.findViewById(R.id.epg_net_test_progressbar_text);
        this.mProgressBar = (ImageView) this.mViewTesting.findViewById(R.id.epg_net_test_progressbar);
        initAnimation();
    }

    protected void onResume() {
        LogUtils.d(this.TAG, "onResume start");
        super.onResume();
        updateUI(DiagnoseStatus.TESTING_BEGIN);
        initData();
        this.mIsSendingFeedback = false;
        this.mIsSendedFeedbackSuccess = false;
        this.mController.startCheckEx(this.mWrapperList);
        LogUtils.d(this.TAG, "onResume end");
    }

    protected void onPause() {
        super.onPause();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onPause ");
        }
        this.mController.stopCheck();
    }

    private void initData() {
        NetDiagnoseInfo netDiagnoseInfo = null;
        int playerType = 0;
        if (getIntent().getExtras() != null) {
            netDiagnoseInfo = (NetDiagnoseInfo) getIntent().getExtras().getSerializable("intent_key_video_info");
            playerType = getIntent().getExtras().getInt("playerType", 0);
        }
        if (netDiagnoseInfo == null) {
            IGalaAccountManager manager = GetInterfaceTools.getIGalaAccountManager();
            BitStream bitStream = new BitStream();
            bitStream.setDefinition(SettingPlayPreference.getNetDoctorBitStreamDefinition(this.mContext));
            bitStream.setAudioType(SettingPlayPreference.getNetDoctorAudioType(this.mContext));
            bitStream.setCodecType(SettingPlayPreference.getNetDoctorCodecType(this.mContext));
            bitStream.setDynamicRangeType(SettingPlayPreference.getNetDoctorDRType(this.mContext));
            netDiagnoseInfo = new CDNNetDiagnoseInfo(manager.getAuthCookie(), manager.getUID(), manager.getUserType(), 0, BitStream.getBid(bitStream), DataUtils.createReverForNetDoctor(bitStream));
        }
        this.mController = new NetDiagnoseController(this, netDiagnoseInfo);
        if (ListUtils.isEmpty(this.mWrapperList)) {
            this.mWrapperList = new ArrayList();
        }
        this.mWrapperList.clear();
        this.mWrapperList.add(NetDiagnoseCheckTools.getNetConnWrapper(netDiagnoseInfo, this.mNetConnListener));
        if (playerType == 0) {
            this.mWrapperList.add(NetDiagnoseCheckTools.getCdnWrapper(netDiagnoseInfo, 1, this.mCdnListener, this.mUploadcallback));
            this.mWrapperList.add(NetDiagnoseCheckTools.getCdnWrapper(netDiagnoseInfo, 2, this.mCdnListener, this.mUploadcallback));
        } else {
            this.mWrapperList.add(NetDiagnoseCheckTools.getCdnWrapper(netDiagnoseInfo, playerType, this.mCdnListener, this.mUploadcallback));
        }
        this.mWrapperList.add(NetDiagnoseCheckTools.getThirdSpeedWrapper(netDiagnoseInfo, this.mThirdSpeedListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getTracerouteWrapper(netDiagnoseInfo, this.mTraceRouteListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getDnsWrapper(netDiagnoseInfo, this.mDnsListener));
        this.mWrapperList.add(NetDiagnoseCheckTools.getNsLookupWrapper(netDiagnoseInfo, this.mNsLookUp));
        this.mWrapperList.add(NetDiagnoseCheckTools.getCollectInfoWrapper(netDiagnoseInfo, this.mCollectListener));
    }

    private void sendResult() {
        this.mIsSendingFeedback = true;
        if (GetInterfaceTools.getILogRecordProvider().isLogRecordFeatureReady()) {
            uploadResult();
        } else {
            LogRecordUtils.showLogRecordNotAlreadyToast(this.mContext);
        }
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.net_diagnose_layout);
    }

    private void switchView(DiagnoseStatus status) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "switchView(" + status + ")");
        }
        switch (status) {
            case TESTING_BEGIN:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_route));
                this.mBtnCancel.setVisibility(0);
                this.mBtnRetry.setVisibility(8);
                this.mBtnReport.setVisibility(8);
                this.mProgressBar.setVisibility(0);
                this.mBtnCancel.requestFocus();
                initAnimation();
                return;
            case ROUTER_CONNECTED:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_net));
                return;
            case ROUTER_DISCONNECTED:
                this.mTextProgress.setText(getResources().getText(R.string.result_no_internet));
                this.mBtnCancel.setVisibility(8);
                this.mBtnRetry.setVisibility(0);
                this.mBtnReport.setVisibility(8);
                this.mBtnRetry.requestFocus();
                return;
            case INTERNET_CONNECTED:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_cdn));
                return;
            case INTERNET_DISCONNECTED:
                this.mBtnCancel.setVisibility(8);
                this.mBtnRetry.setVisibility(0);
                this.mBtnReport.setVisibility(8);
                this.mBtnRetry.requestFocus();
                return;
            case CDN_CONNECTED:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_third_speed_test));
                return;
            case CDN_DISCONNECTED:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_third_speed_test));
                return;
            case SPEED_TEST_DONE:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_traceroute_test));
                return;
            case TRACEROUT_DONE:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_dns_test));
                return;
            case DNS_DONE:
                this.mTextProgress.setText(getResources().getText(R.string.net_diagnosis_collection_test));
                return;
            case COLLECTON_SUCCESS:
                showResultView();
                return;
            case COLLECTON_FAIL:
                showResultView();
                return;
            default:
                return;
        }
    }

    private void updateUI(final DiagnoseStatus status) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "updateUI, status=" + status);
        }
        runOnUiThread(new Runnable() {
            public void run() {
                NetDiagnoseActivity.this.switchView(status);
                NetDiagnoseActivity.this.mViewTesting.setNetStatus(status);
            }
        });
    }

    public void showResultView() {
        this.mBtnCancel.setVisibility(8);
        this.mBtnRetry.setVisibility(0);
        this.mBtnReport.setVisibility(0);
        this.mBtnReport.requestFocus();
        this.mProgressBar.clearAnimation();
        this.mProgressBar.setVisibility(8);
    }

    public void initAnimation() {
        this.animation = getProgressBarAnimation();
        if (this.animation != null) {
            this.mProgressBar.clearAnimation();
            this.mProgressBar.setAnimation(this.animation);
            this.animation.startNow();
            return;
        }
        ((AnimationDrawable) this.mProgressBar.getDrawable()).start();
    }

    private Animation getProgressBarAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this.mContext, R.anim.epg_litchi_anima_load_item);
        animation.setInterpolator(new LinearInterpolator());
        return animation;
    }

    private void setReslut(NetDiagnoseInfo result) {
        this.mResult = result;
    }

    private void uploadResult() {
        if (this.mResult != null) {
            GetInterfaceTools.getILogRecordProvider().notifySaveLogFile();
            String cdnResult = this.mResult.getCdnDiagnoseJsonResult();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "uploadResult jsonResult size=" + (cdnResult != null ? Integer.valueOf(cdnResult.length()) : "NULL"));
            }
            if (!(cdnResult == null || this.mController.getNetDoctor() == null)) {
                this.mController.getNetDoctor().sendLogInfo(cdnResult);
            }
            StringBuilder info = new StringBuilder();
            info.append("---------------3rd Speed Test---------------\r\n");
            info.append(this.mResult.getThirdSpeedTestResult());
            info.append("---------------\r\n\n");
            info.append("---------------gala CDN Test---------------\r\n");
            info.append("---------------CDN Result---------------\r\n");
            info.append("CDN Result " + cdnResult);
            info.append("\r\n---------------\r\n\n");
            info.append("---------------Trace Route Test---------------\r\n");
            info.append(this.mResult.getTracerouteResult());
            info.append("---------------\r\n\n");
            info.append("---------------DNS detect Test---------------\r\n");
            info.append(this.mResult.getDnsResult());
            info.append("\n---------------\r\n\n");
            info.append("---------------NS look up Test---------------\r\n");
            info.append(this.mResult.getNslookupResult());
            info.append("\n---------------\r\n\n");
            info.append(this.mResult.getCollectionResult());
            UploadExtraMap extraMap = new UploadExtraMap();
            UploadOptionMap optionMap = new UploadOptionMap();
            extraMap.setOtherInfo(info.toString());
            optionMap.setIsUploadtrace(true);
            UploadExtraInfo feedbackExtraInfo = GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap);
            UploadOption feedbackOption = GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap);
            Recorder recorder = new RecorderBuilder(RecorderType._FEEDBACK, RecorderLogType.LOGRECORD_NETDINOSE_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setIddRecord(LogRecordConfigUtils.getGlobalConfig().getString()).build();
            IFeedbackResultCallback feedbackResultCallback = CreateInterfaceTools.createFeedbackResultListener();
            feedbackResultCallback.setRecorderType(recorder.getRecorderType());
            feedbackResultCallback.init(this.mContext, feedbackExtraInfo, feedbackOption, recorder, new UploadResultControllerListener() {
                public void onSuccess() {
                    NetDiagnoseActivity.this.mIsSendingFeedback = false;
                    NetDiagnoseActivity.this.mIsSendedFeedbackSuccess = true;
                }

                public void onFailure() {
                    NetDiagnoseActivity.this.mIsSendingFeedback = false;
                    NetDiagnoseActivity.this.mIsSendedFeedbackSuccess = false;
                }
            }, SourceType.detector);
            feedbackResultCallback.setNormalReport(true);
            feedbackResultCallback.setPageType(2);
            GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(feedbackExtraInfo, feedbackOption, recorder, feedbackResultCallback.getFeedbackResultListener());
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(this.TAG, "uploadResult mResult is null!");
        }
    }
}
