package com.gala.video.app.epg.feedback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import com.gala.report.core.upload.IFeedbackResultListener;
import com.gala.report.core.upload.config.LogRecordConfigUtils;
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.sdk.player.ISdkError;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.GalaPlayerLogProvider;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.ui.netdiagnose.provider.NetDiagnoseUploader;
import com.gala.video.app.epg.widget.GlobalQRFeedBackDialog;
import com.gala.video.app.epg.widget.GlobalQRFeedBackDialog.StringModel;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.LogListener;
import com.gala.video.lib.share.ifimpl.logrecord.utils.CrashUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordDebugUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedbackData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.SourceType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.UploadResultControllerListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.ILogRecordProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.utils.QRUtils;
import com.mcto.ads.AdsClient;
import java.util.HashMap;
import java.util.Map;

public class FeedbackResultListener extends Wrapper implements IFeedbackResultListener {
    private static final String TAG = "FeedbackResultListener";
    private String feedbackString;
    private ApiException mApiException;
    private Context mContext;
    private GlobalDialog mDialog;
    private FeedbackData mFbData = new FeedbackData();
    Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsNormalReport = false;
    private int mPageType = 0;
    private RecorderType mRecorderType = RecorderType._FEEDBACK;
    private ISdkError mSdkError;
    private SourceType mSourceType;
    private UploadResultControllerListener mUploadResultControllerListener;
    private OnClickListener onRetryBtnListener = new OnClickListener() {
        public void onClick(View v) {
            ILogRecordProvider provider = GetInterfaceTools.getILogRecordProvider();
            provider.getUploadCore().resetFeedbackValue();
            FeedbackResultListener.this.dismissDialog();
            IFeedbackResultCallback feedbackResultCallback = CreateInterfaceTools.createFeedbackResultListener();
            feedbackResultCallback.init(FeedbackResultListener.this.mContext, FeedbackResultListener.this.mFbData, FeedbackResultListener.this.mUploadResultControllerListener, FeedbackResultListener.this.mSourceType);
            feedbackResultCallback.setRecorderType(FeedbackResultListener.this.mFbData.getRecorder().getRecorderType());
            if (FeedbackResultListener.this.mRecorderType == RecorderType._FEEDBACK) {
                provider.getUploadCore().sendRecorder(FeedbackResultListener.this.mFbData.getUploadExtraInfo(), FeedbackResultListener.this.mFbData.getUploadOption(), FeedbackResultListener.this.mFbData.getRecorder(), feedbackResultCallback.getFeedbackResultListener());
            }
        }
    };

    public void setRecorderType(RecorderType recorderType) {
        this.mRecorderType = recorderType;
    }

    public void setNormalReport(boolean isNormalReport) {
        this.mIsNormalReport = isNormalReport;
    }

    private FeedbackData createFeedbackModel(boolean isAdsInfo) {
        FeedbackData model = new FeedbackData();
        ILogRecordProvider provider = GetInterfaceTools.getILogRecordProvider();
        UploadExtraMap extraMap = new UploadExtraMap();
        UploadOptionMap optionMap = new UploadOptionMap();
        optionMap.setIsUploadtrace(true);
        extraMap.setClog(GalaPlayerLogProvider.getPumaLog());
        if (isAdsInfo) {
            optionMap.setIsUploadAdsLog(true);
            StringBuilder ads = new StringBuilder("");
            LogUtils.d(TAG, ">>>>> Ads LogC content - ", GalaPlayerLogProvider.getCupIdLog());
            LogUtils.d(TAG, ">>>>> Ads LogJ content - ", AdsClient.getFeedbackLog());
            extraMap.setAdsLog(ads.append(adsLogC).append(adsLogJ).toString());
        }
        model.setUploadExtraInfo(provider.getUploadExtraInfoAndParse(extraMap));
        model.setUploadOption(provider.getUploadOptionInfoAndParse(optionMap));
        model.setRecorder(new RecorderBuilder(RecorderType._FEEDBACK, RecorderLogType.LOGRECORD_MANUAL_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setIddRecord(LogRecordConfigUtils.getGlobalConfig().getString()).build());
        return model;
    }

    public FeedbackData makeFeedbackData(SourceType sourceType, boolean isSendAdsLog) {
        if (sourceType == null) {
            sourceType = SourceType.menu;
        }
        this.mSourceType = sourceType;
        setNormalReport(true);
        this.mFbData = createFeedbackModel(isSendAdsLog);
        return this.mFbData;
    }

    public void init(Context context) {
        this.mContext = context;
    }

    public void init(Context context, FeedbackData model, SourceType sourceType) {
        this.mContext = context;
        this.mFbData = model;
        this.mSourceType = sourceType;
    }

    public void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, SourceType sourceType) {
        this.mContext = context;
        this.mFbData.setUploadExtraInfo(uploadExtraInfo);
        this.mFbData.setUploadOption(uploadOption);
        this.mFbData.setRecorder(recorder);
        this.mSourceType = sourceType;
    }

    public void init(Context context, FeedbackData model, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType) {
        init(context, model, sourceType);
        this.mUploadResultControllerListener = uploadResultControllerListener;
    }

    public void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType) {
        init(context, uploadExtraInfo, uploadOption, recorder, sourceType);
        this.mUploadResultControllerListener = uploadResultControllerListener;
    }

    public void init(Context context, UploadExtraInfo uploadExtraInfo, UploadOption uploadOption, Recorder recorder, ApiException exception, UploadResultControllerListener uploadResultControllerListener, SourceType sourceType) {
        init(context, uploadExtraInfo, uploadOption, recorder, uploadResultControllerListener, sourceType);
        this.mApiException = exception;
    }

    public IFeedbackResultListener getFeedbackResultListener() {
        return this;
    }

    public void setUploadOption(UploadOption uploadOption) {
        this.mFbData.setUploadOption(uploadOption);
    }

    public void beginsendLog() {
        this.mHandler.post(new Runnable() {
            public void run() {
                QToast.makeTextAndShow(FeedbackResultListener.this.mContext, LogListener.MSG_LOG_BEGIN_SEND, 1);
            }
        });
    }

    public void lastsendNotComplete() {
        this.mHandler.post(new Runnable() {
            public void run() {
                QToast.makeTextAndShow(FeedbackResultListener.this.mContext, LogListener.MSG_ISRUNNING_RETRYLATER, 1);
            }
        });
    }

    public void sendReportSuccess(final String feedbackId, final String ip) {
        if (LogRecordDebugUtils.testLogRecordExceptionForF00001()) {
            sendReportFailed(LogRecordUtils.EXCEPTION_F00001);
        } else if (LogRecordDebugUtils.testLogRecordExceptionForF00002()) {
            sendReportFailed(LogRecordUtils.EXCEPTION_F00002);
        } else if (LogRecordDebugUtils.testLogRecordExceptionForF00003()) {
            sendReportFailed(LogRecordUtils.EXCEPTION_F00003);
        } else if (LogRecordDebugUtils.testLogRecordExceptionForF10000()) {
            sendReportFailed(LogRecordUtils.EXCEPTION_F10000);
        } else {
            if (!this.mIsNormalReport) {
                LogUtils.d(TAG, ">>>>> logrecord pingback --- type = ", this.mRecorderType.toString());
                switch (this.mRecorderType) {
                    case _FEEDBACK:
                        sendUploadFeedbackPingback("0", "", "", LogRecordUtils.getEventID(), feedbackId, InterfaceKey.EPG_FB);
                        break;
                    case _CRASH:
                        sendUploadFeedbackPingback("0", "", "", LogRecordUtils.getEventID(), feedbackId, "tracker_crash");
                        break;
                    case _FEEDBACK_AUTO:
                        sendUploadFeedbackPingback("0", "", "", LogRecordUtils.getEventID(), feedbackId, "tracker_auto");
                        break;
                    case _ERROR:
                        sendUploadFeedbackPingback("0", "", "", LogRecordUtils.getEventID(), feedbackId, "tracker_feedback");
                        break;
                    default:
                        break;
                }
            }
            LogUtils.d(TAG, ">>>>> logrecord pingback --- mIsNormalReport");
            sendUploadFeedbackPingback("0", "", "", "", "", "");
            if (this.mRecorderType == RecorderType._FEEDBACK) {
                this.mHandler.post(new Runnable() {
                    public void run() {
                        try {
                            if (((Activity) FeedbackResultListener.this.mContext).isFinishing()) {
                                LogUtils.e(FeedbackResultListener.TAG, "FeedbackResultListener--->>feedBackSuccess()----activity is finish");
                                return;
                            }
                        } catch (Exception e) {
                        }
                        Bitmap qRBitmap = BitmapFactory.decodeResource(FeedbackResultListener.this.mContext.getResources(), R.drawable.share_btn_transparent);
                        String mIp = ip;
                        if (StringUtils.isEmpty((CharSequence) mIp)) {
                            mIp = LogRecordUtils.getPublicIp(FeedbackResultListener.this.mContext);
                        }
                        String mTime = DeviceUtils.getCurrentTime();
                        FeedbackResultListener.this.mDialog = EpgAppConfig.getGlobalQRDialog(FeedbackResultListener.this.mContext);
                        FeedbackResultListener.this.feedbackString = FeedbackResultListener.this.getSuccessString(feedbackId, mTime, mIp);
                        String rightTopMessage = FeedbackResultListener.this.mContext.getString(R.string.logrecordSuccess_with_qr_right_top);
                        String rightBottomMessage = FeedbackResultListener.this.mContext.getString(R.string.logrecordSuccess_with_qr_right_bottom, new Object[]{mIp, DeviceUtils.getMacAddr(), LogRecordUtils.getVersionCode(), mTime});
                        String leftBottomMessage = FeedbackResultListener.this.mContext.getString(R.string.logrecordSuccess_with_qr_left_bottom, new Object[]{feedbackId});
                        StringModel stringModel = new StringModel();
                        stringModel.mIsFeedbackSuccess = true;
                        stringModel.mRightTopString = rightTopMessage;
                        stringModel.mRightBottomString = rightBottomMessage;
                        stringModel.mLeftBottomString = leftBottomMessage;
                        ((GlobalQRFeedBackDialog) FeedbackResultListener.this.mDialog).setParams(stringModel, qRBitmap, null, null, null, null);
                        FeedbackResultListener.this.mDialog.show();
                        FeedbackResultListener.this.sendShowPingBack(FeedbackResultListener.this.mSourceType.toString());
                        if (FeedbackResultListener.this.mUploadResultControllerListener != null) {
                            FeedbackResultListener.this.mUploadResultControllerListener.onSuccess();
                        }
                        Log.v(FeedbackResultListener.TAG, "feedbackString = " + FeedbackResultListener.this.feedbackString);
                        FeedbackResultListener.this.loadQRBitmap(FeedbackResultListener.this.feedbackString);
                        GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                        switch (FeedbackResultListener.this.mPageType) {
                            case 0:
                            case 1:
                                LogUtils.d(FeedbackResultListener.TAG, ">>>>> feedback success, no need net diagnose");
                                return;
                            case 3:
                                LogUtils.d(FeedbackResultListener.TAG, ">>>>> feedback success [FEEDBACK_KEY], start net diagnose");
                                NetDiagnoseUploader.getInstance().doTotalNetDiagnose();
                                return;
                            case 4:
                                LogUtils.d(FeedbackResultListener.TAG, ">>>>> feedback success [FEEDBACK_ACTIVITY], start net diagnose");
                                NetDiagnoseUploader.getInstance().doTotalNetDiagnose();
                                return;
                            case 5:
                                LogUtils.d(FeedbackResultListener.TAG, ">>>>> feedback success [GLOABAL_FEEDBACK_PANEL], start net diagnose");
                                String errorCode = null;
                                String url = null;
                                if (FeedbackResultListener.this.mApiException != null) {
                                    if (StringUtils.isEmpty(FeedbackResultListener.this.mApiException.getHttpCode())) {
                                        errorCode = FeedbackResultListener.this.mApiException.getCode();
                                    } else {
                                        errorCode = FeedbackResultListener.this.mApiException.getHttpCode();
                                    }
                                    url = FeedbackResultListener.this.mApiException.getUrl();
                                } else {
                                    LogUtils.d(FeedbackResultListener.TAG, ">>>>> ApiException is null, apiCode & httpcode & url is null!!!");
                                }
                                NetDiagnoseUploader.getInstance().doPingNs(NetDiagnoseUploader.getInstance().getPingNsProvider(FeedbackResultListener.this.mSdkError, errorCode, url));
                                return;
                            default:
                                return;
                        }
                    }
                });
            } else {
                CrashUtils.clearCrashFile();
            }
        }
    }

    public void sendReportFailed(final String code) {
        if (this.mIsNormalReport) {
            sendUploadFeedbackPingback(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR, "315011", code, "", "", "");
        } else if (this.mRecorderType == RecorderType._FEEDBACK) {
            sendUploadFeedbackPingback(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR, "315011", code, LogRecordUtils.getEventID(), "", InterfaceKey.EPG_FB);
        } else {
            sendUploadFeedbackPingback(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR, "315011", code, LogRecordUtils.getEventID(), "", "tracker");
        }
        if (this.mRecorderType == RecorderType._FEEDBACK) {
            this.mHandler.post(new Runnable() {
                public void run() {
                    try {
                        if (((Activity) FeedbackResultListener.this.mContext).isFinishing()) {
                            LogUtils.e(FeedbackResultListener.TAG, "FeedbackResultListener--->>feedBackFail()----activity is finish");
                            return;
                        }
                    } catch (Exception e) {
                    }
                    Bitmap qRBitmap = BitmapFactory.decodeResource(FeedbackResultListener.this.mContext.getResources(), R.drawable.share_btn_transparent);
                    String message = FeedbackResultListener.this.mContext.getString(R.string.logrecordFailed_with_qr);
                    String rptBtnText = FeedbackResultListener.this.mContext.getString(R.string.logrecordRetry);
                    String cancelBtnText = FeedbackResultListener.this.mContext.getString(R.string.Cancel);
                    FeedbackResultListener.this.mDialog = EpgAppConfig.getGlobalQRDialog(FeedbackResultListener.this.mContext);
                    FeedbackResultListener.this.feedbackString = FeedbackResultListener.this.getFailedString(code);
                    StringModel stringModel = new StringModel();
                    stringModel.mIsFeedbackSuccess = false;
                    stringModel.mContentString = message;
                    ((GlobalQRFeedBackDialog) FeedbackResultListener.this.mDialog).setParams(stringModel, qRBitmap, rptBtnText, FeedbackResultListener.this.onRetryBtnListener, cancelBtnText, new OnClickListener() {
                        public void onClick(View v) {
                            GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                            FeedbackResultListener.this.dismissDialog();
                        }
                    });
                    FeedbackResultListener.this.mDialog.show();
                    FeedbackResultListener.this.sendShowPingBack(FeedbackResultListener.this.mSourceType.toString());
                    if (FeedbackResultListener.this.mUploadResultControllerListener != null) {
                        FeedbackResultListener.this.mUploadResultControllerListener.onFailure();
                    }
                    FeedbackResultListener.this.loadQRBitmap(FeedbackResultListener.this.feedbackString);
                }
            });
        } else {
            CrashUtils.clearCrashFile();
        }
    }

    private void sendUploadFeedbackPingback(String st, String ec, String pfec, String e, String feedbackid, String fbtype) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "11").add("st", st).add("ct", "150721_feedback").add("ec", ec).add("pfec", pfec).add("e", e).add(Keys.FEEDBACK_ID, feedbackid).add(Keys.FBTYPE, fbtype);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private String getFailedString(String code) {
        StringBuffer sb = new StringBuffer();
        if (this.mApiException != null) {
            sb.append("ApiException:code=").append(this.mApiException.getCode()).append(",httpCode=").append(this.mApiException.getHttpCode()).append(",\n");
        }
        if (code != null) {
            sb.append("feedback server return  error code is ").append(code).append(",");
            if (code.equals("-100")) {
                sb.append(this.mContext.getString(R.string.logrecordFailed_not_connect_server));
            } else if (code.equals(LogRecordUtils.EXCEPTION_F10000)) {
                sb.append(this.mContext.getString(R.string.logrecordfailed_internal_error_code));
            } else if (code.equals(LogRecordUtils.EXCEPTION_F00001)) {
                sb.append(this.mContext.getString(R.string.logrecordfailed_null_request_body_code));
            } else if (code.equals(LogRecordUtils.EXCEPTION_F00002)) {
                sb.append(this.mContext.getString(R.string.logrecordfailed_ungzip_error_code));
            } else if (code.equals(LogRecordUtils.EXCEPTION_F00003)) {
                sb.append(this.mContext.getString(R.string.logrecordfailed_json_invalid_code));
            } else if (code.equals("-300")) {
                sb.append(this.mContext.getString(R.string.logrecordfailed_inter_know));
            } else {
                sb.append(this.mContext.getString(R.string.logrecordfailed_unknow));
            }
            sb.append("\n");
        }
        sb.append(LogRecordUtils.getDevicesInfoForQR(this.mContext));
        return sb.toString();
    }

    private String getSuccessString(String code, String time, String ip) {
        Map<String, String> map = new HashMap();
        map.put("fbid", code);
        map.put("time", time);
        map.put(WebConstants.IP, ip);
        map.put(Keys.PLATFORM, InterfaceKey.EPG_FB);
        if (this.mApiException != null) {
            CharSequence errCode = this.mApiException.getCode();
            String errUrl = this.mApiException.getUrl();
            CharSequence httpCode = this.mApiException.getHttpCode();
            if (!StringUtils.isEmpty(errCode)) {
                map.put("errcode", errCode);
            }
            if (!StringUtils.isEmpty((CharSequence) errUrl)) {
                Log.v(TAG, "mApiException.getUrl() length = " + errUrl.length());
                Log.v(TAG, "mApiException.getUrl() length = " + errUrl);
                if (errUrl.length() > LogRecordUtils.errUrlLength) {
                    errUrl = errUrl.substring(0, LogRecordUtils.errUrlLength);
                }
                Log.v(TAG, "url subString = " + errUrl);
                map.put(Keys.ERRURL, errUrl);
            }
            if (!StringUtils.isEmpty(httpCode)) {
                map.put("httpco", httpCode);
            }
        }
        LogRecordUtils.getDevicesInfoForQR(map, this.mContext);
        return LogRecordUtils.getFeedbackUrl(map);
    }

    private void dismissDialog() {
        LogUtils.d(TAG, "dismissDialog, mDialog=" + this.mDialog);
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    private void loadQRBitmap(final String str) {
        ImageView qrImageView = ((GlobalQRFeedBackDialog) this.mDialog).getQRImageView();
        if (qrImageView != null) {
            qrImageView.setBackgroundColor(871494129);
            ((GlobalQRFeedBackDialog) this.mDialog).setLoadingVisible(0);
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    final Bitmap bitmap = QRUtils.createQRImage(str);
                    FeedbackResultListener.this.mHandler.post(new Runnable() {
                        public void run() {
                            if (FeedbackResultListener.this.mDialog != null && FeedbackResultListener.this.mDialog.isShowing() && (FeedbackResultListener.this.mDialog instanceof GlobalQRFeedBackDialog)) {
                                ImageView qrImageView = ((GlobalQRFeedBackDialog) FeedbackResultListener.this.mDialog).getQRImageView();
                                if (!(qrImageView == null || bitmap == null)) {
                                    qrImageView.setBackgroundColor(-1);
                                    qrImageView.setImageBitmap(bitmap);
                                }
                                ((GlobalQRFeedBackDialog) FeedbackResultListener.this.mDialog).setLoadingVisible(8);
                                if (bitmap == null) {
                                    ((GlobalQRFeedBackDialog) FeedbackResultListener.this.mDialog).showQRFail();
                                }
                            }
                        }
                    });
                }
            });
        }
    }

    private void sendShowPingBack(String block) {
        Log.v(TAG, "sendShowPingBack block = " + block);
        PingBackParams params = new PingBackParams();
        params.add("qtcurl", "failfb_dlg").add(Keys.T, "21").add("block", block).add("bstp", "1");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void setPageType(int pageType) {
        this.mPageType = pageType;
    }

    public void setSdkError(ISdkError error) {
        this.mSdkError = error;
    }
}
