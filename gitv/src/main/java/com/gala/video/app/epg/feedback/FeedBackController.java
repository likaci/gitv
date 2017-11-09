package com.gala.video.app.epg.feedback;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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
import com.gala.report.core.upload.config.UploadExtraInfo;
import com.gala.report.core.upload.config.UploadOption;
import com.gala.report.core.upload.feedback.FeedbackType;
import com.gala.report.core.upload.recorder.Recorder;
import com.gala.report.core.upload.recorder.Recorder.RecorderBuilder;
import com.gala.report.core.upload.recorder.RecorderLogType;
import com.gala.report.core.upload.recorder.RecorderType;
import com.gala.sdk.player.IMedia;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.app.epg.ui.netdiagnose.provider.NDBaseProvider.INetDiagnoseResultListener;
import com.gala.video.app.epg.ui.netdiagnose.provider.NetDiagnoseUploader;
import com.gala.video.app.epg.ui.netdiagnose.provider.PingNslookupProvider;
import com.gala.video.app.epg.widget.GlobalQRFeedBackDialog;
import com.gala.video.app.epg.widget.GlobalQRFeedBackDialog.StringModel;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.LogListener;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordDebugUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.InterfaceKey;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController.OnFeedBackPrepareListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.SourceType;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.utils.QRUtils;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

class FeedBackController extends Wrapper {
    private final String TAG = "EPG/utils/FeedBackController";
    private OnClickListener mCancelBtnListener = new OnClickListener() {
        public void onClick(View v) {
            FeedBackController.this.mIsFeedBackClick = false;
            FeedBackController.this.dismissDialog();
        }
    };
    private OnClickListener mCancelUploadListener = new OnClickListener() {
        public void onClick(View v) {
            FeedBackController.this.dismissDialog();
        }
    };
    private String mContent;
    private Context mContext;
    private CountDownLatch mCountDownLatch;
    private GlobalDialog mDialog;
    private OnDismissListener mDismissListener;
    private String mEventID;
    private OnClickListener mFeedBackBtnListener = new OnClickListener() {
        public void onClick(View v) {
            FeedBackController.this.feedback();
        }
    };
    private FeedBackModel mFeedBackModel;
    private boolean mIsFeedBackClick = false;
    private boolean mIsRestryClick = false;
    private boolean mIsUploadCancel = true;
    private IMedia mMedia;
    private OnFeedBackPrepareListener mPrepareListener;
    private OnDismissListener mQRDialogDismisslistener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (!FeedBackController.this.mIsFeedBackClick && FeedBackController.this.mDismissListener != null) {
                FeedBackController.this.mDismissListener.onDismiss(FeedBackController.this.mDialog);
            }
        }
    };
    private OnDismissListener mResultDialogDismisslistener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            if (!FeedBackController.this.mIsRestryClick && FeedBackController.this.mDismissListener != null) {
                FeedBackController.this.mDismissListener.onDismiss(FeedBackController.this.mDialog);
            }
        }
    };
    private OnDismissListener mUploadingDismissListener = new OnDismissListener() {
        public void onDismiss(DialogInterface dialog) {
            GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
            if (FeedBackController.this.mIsUploadCancel && FeedBackController.this.mDismissListener != null) {
                FeedBackController.this.mDismissListener.onDismiss(FeedBackController.this.mDialog);
            }
        }
    };
    Handler mhandler = new Handler(Looper.getMainLooper());
    private OnClickListener onRetryBtnListener = new OnClickListener() {
        public void onClick(View v) {
            FeedBackController.this.mIsRestryClick = true;
            GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
            FeedBackController.this.dismissDialog();
            FeedBackController.this.feedBack(false);
        }
    };

    public void setEventID(String eventID) {
        this.mEventID = eventID;
    }

    public void setMedia(IMedia media) {
        this.mMedia = media;
    }

    public IMedia getMeida() {
        return this.mMedia;
    }

    public void init(Context context, OnFeedBackPrepareListener prepareListener) {
        this.mContext = context;
        this.mPrepareListener = prepareListener;
    }

    public void setPrepareListener(OnFeedBackPrepareListener prepareListener) {
        this.mPrepareListener = prepareListener;
    }

    public void showQRDialog(FeedBackModel model, OnDismissListener dismissListener) {
        FeedBackModel feedBackModel = model;
        OnDismissListener onDismissListener = dismissListener;
        showQRDialog(feedBackModel, onDismissListener, this.mContext.getString(R.string.popup_dialog_feedback_btn_text), this.mFeedBackBtnListener, this.mContext.getString(R.string.back), this.mCancelBtnListener);
    }

    public void showQRDialog(FeedBackModel model, OnDismissListener dismissListener, String leftText, OnClickListener leftListener, String rightText, OnClickListener rightListener) {
        if (model == null) {
            LogUtils.e("EPG/utils/FeedBackController", "showQRDialog()----FeedBackModel is null");
        } else if (this.mContext == null) {
            LogUtils.e("EPG/utils/FeedBackController", "feedBack()---mContext=" + this.mContext);
        } else {
            LogRecordUtils.setEventID(this.mEventID);
            this.mDismissListener = dismissListener;
            this.mFeedBackModel = model;
            this.mContent = this.mFeedBackModel.toString();
            this.mDialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
            this.mIsFeedBackClick = false;
            this.mDialog.setOnDismissListener(this.mQRDialogDismisslistener);
            if (StringUtils.isEmpty((CharSequence) leftText)) {
                leftText = this.mContext.getString(R.string.popup_dialog_feedback_btn_text);
                leftListener = this.mFeedBackBtnListener;
            }
            if (StringUtils.isEmpty((CharSequence) rightText)) {
                rightText = this.mContext.getString(R.string.back);
                rightListener = this.mCancelBtnListener;
            }
            this.mDialog.setParams(this.mContext.getString(R.string.feedback_tip, new Object[]{this.mFeedBackModel.getErrorMsg()}), leftText, leftListener, rightText, rightListener);
            this.mDialog.setGravity(3);
            this.mDialog.show();
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    FeedBackController.this.startUpload(true);
                }
            });
        }
    }

    private void dismissDialog() {
        LogUtils.d("EPG/utils/FeedBackController", "dismissDialog, mDialog=" + this.mDialog);
        if (this.mDialog != null) {
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    public void clearCurrentDialog() {
        LogUtils.d("EPG/utils/FeedBackController", "clearCurrentDialog: mDialog=" + this.mDialog);
        if (this.mDialog != null) {
            this.mDialog.setOnDismissListener(null);
            this.mDialog.dismiss();
            this.mDialog = null;
        }
    }

    public void feedback() {
        this.mIsFeedBackClick = true;
        dismissDialog();
        feedBack(true);
    }

    private void startUpload(boolean isErrorType) {
        if (GetInterfaceTools.getILogRecordProvider().isLogRecordFeatureReady()) {
            UploadExtraMap extraMap = new UploadExtraMap();
            UploadOptionMap optionMap = new UploadOptionMap();
            extraMap.setOtherInfo(this.mContent);
            LogUtils.d("EPG/utils/FeedBackController", "mFeedBackModel.isNeedLogcat() = " + this.mFeedBackModel.isNeedLogcat());
            optionMap.setIsUploadlogcat(this.mFeedBackModel.isNeedLogcat());
            UploadOption option = GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap);
            String iddRecord = "";
            Map<String, String> extroInfoKeyValues = null;
            if (this.mFeedBackModel.getRecord() != null) {
                iddRecord = this.mFeedBackModel.getRecord().getIDDRecord();
                extroInfoKeyValues = this.mFeedBackModel.getRecord().getKeyValues();
            }
            String errorCode = this.mFeedBackModel.getErrorCode();
            String errorMessage = this.mFeedBackModel.getErrorMsg();
            String errorApiname = this.mFeedBackModel.getApiName();
            String errorLog = this.mFeedBackModel.getErrorLog();
            String eventId = "";
            if (extroInfoKeyValues != null) {
                eventId = (String) extroInfoKeyValues.get("eventId");
            }
            String finalEventId = eventId;
            Log.v("EPG/utils/FeedBackController", "errorCode = " + errorCode);
            Log.v("EPG/utils/FeedBackController", "errorMessage = " + errorMessage);
            Log.v("EPG/utils/FeedBackController", "errorApiname = " + errorApiname);
            if (errorMessage != null && errorMessage.length() >= 250) {
                errorMessage = errorMessage.substring(0, 250);
            }
            if (isErrorType) {
                final Recorder errorRecorder = new RecorderBuilder(RecorderType._ERROR, RecorderLogType.LOGRECORD_CLICK_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setIddRecord(iddRecord).setLogContent(errorLog).setKeyValueMaps(extroInfoKeyValues).setErrorCode(errorCode).setErrorMessagec(errorMessage).setApiName(errorApiname).build();
                PingNslookupProvider provider = NetDiagnoseUploader.getInstance().getPingNsProvider(this.mFeedBackModel.getSDKError(), this.mFeedBackModel.getErrorCode(), this.mFeedBackModel.getUrl());
                if (provider == null) {
                    sendAutoTracker(GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap), option, errorRecorder, finalEventId);
                    return;
                }
                final UploadExtraMap uploadExtraMap = extraMap;
                final UploadOption uploadOption = option;
                final String str = finalEventId;
                NetDiagnoseUploader.getInstance().doNetDiagnoseToAutoTracker(provider, new INetDiagnoseResultListener() {
                    public void onReslut(String info) {
                        LogUtils.d("EPG/utils/FeedBackController", ">>>>> net diagnose successs, start upload to Tracker");
                        NetDiagnoseUploader.getInstance().setFilterFlag(false);
                        uploadExtraMap.setOtherInfo(FeedBackController.this.mContent + "\n ********* NET DIAGNOSE INFO *********** \n" + info);
                        FeedBackController.this.sendAutoTracker(GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(uploadExtraMap), uploadOption, errorRecorder, str);
                    }
                });
                return;
            }
            Recorder recorder = new RecorderBuilder(RecorderType._FEEDBACK, RecorderLogType.LOGRECORD_CLICK_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setIddRecord(iddRecord).setKeyValueMaps(extroInfoKeyValues).setErrorCode(errorCode).setErrorMessagec(errorMessage).setApiName(errorApiname).build();
            UploadExtraInfo extraInfo = GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap);
            GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(extraInfo, option, recorder, new IFeedbackResultListener() {
                public void beginsendLog() {
                }

                public void lastsendNotComplete() {
                    QToast.makeTextAndShow(FeedBackController.this.mContext, LogListener.MSG_ISRUNNING_RETRYLATER, 1);
                }

                public void sendReportSuccess(String feedbackId, String ip) {
                    if (LogRecordDebugUtils.testLogRecordExceptionForF00001()) {
                        sendReportFailed(LogRecordUtils.EXCEPTION_F00001);
                    } else if (LogRecordDebugUtils.testLogRecordExceptionForF00002()) {
                        sendReportFailed(LogRecordUtils.EXCEPTION_F00002);
                    } else if (LogRecordDebugUtils.testLogRecordExceptionForF00003()) {
                        sendReportFailed(LogRecordUtils.EXCEPTION_F00003);
                    } else if (LogRecordDebugUtils.testLogRecordExceptionForF10000()) {
                        sendReportFailed(LogRecordUtils.EXCEPTION_F10000);
                    } else {
                        PingBackParams params = new PingBackParams();
                        params.add(Keys.T, "11").add("st", "0").add("ct", "150721_feedback").add("ec", "").add("pfec", "").add("e", LogRecordUtils.getEventID()).add(Keys.FEEDBACK_ID, feedbackId).add(Keys.FBTYPE, InterfaceKey.EPG_FB);
                        PingBack.getInstance().postPingBackToLongYuan(params.build());
                        FeedBackController.this.feedBackSuccess(feedbackId, ip);
                    }
                    LogUtils.d("EPG/utils/FeedBackController", ">>>>> send feedback success, start upload net diagnose info");
                    NetDiagnoseUploader.getInstance().doPingNs(NetDiagnoseUploader.getInstance().getPingNsProvider(FeedBackController.this.mFeedBackModel.getSDKError(), FeedBackController.this.mFeedBackModel.getErrorCode(), FeedBackController.this.mFeedBackModel.getUrl()));
                }

                public void sendReportFailed(String e) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e("EPG/utils/FeedBackController", "feedBack---onFail---error=" + e);
                    }
                    PingBackParams params = new PingBackParams();
                    params.add(Keys.T, "11").add("st", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).add("ct", "150721_feedback").add("ec", "315011").add("pfec", "").add("e", LogRecordUtils.getEventID()).add(Keys.FEEDBACK_ID, "").add(Keys.FBTYPE, InterfaceKey.EPG_FB);
                    PingBack.getInstance().postPingBackToLongYuan(params.build());
                    FeedBackController.this.feedBackFail(e);
                }
            });
            return;
        }
        LogRecordUtils.showLogRecordNotAlreadyToast(this.mContext);
    }

    private void sendAutoTracker(UploadExtraInfo extraInfo, UploadOption option, Recorder errorRecorder, final String finalEventId) {
        GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(extraInfo, option, errorRecorder, new IFeedbackResultListener() {
            public void beginsendLog() {
            }

            public void lastsendNotComplete() {
            }

            public void sendReportSuccess(String feedbackId, String ip) {
                LogUtils.d("EPG/utils/FeedBackController", ">>>>> logrecord 'error_type' send pingback, eventid=", finalEventId);
                PingBackParams params = new PingBackParams();
                params.add(Keys.T, "11").add("st", "0").add("ct", "150721_feedback").add("ec", "").add("pfec", "").add("e", finalEventId).add(Keys.FEEDBACK_ID, feedbackId).add(Keys.FBTYPE, MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                NetDiagnoseUploader.getInstance().shutDownExecutor();
            }

            public void sendReportFailed(String s) {
                NetDiagnoseUploader.getInstance().shutDownExecutor();
            }
        });
    }

    private void prepare() {
        this.mCountDownLatch = new CountDownLatch(1);
        ThreadUtils.execute(new Runnable() {
            public void run() {
                FeedBackController.this.mContent = FeedBackController.this.mContent + FeedBackController.this.mPrepareListener.onPrepare();
                FeedBackController.this.mCountDownLatch.countDown();
            }
        });
        try {
            this.mCountDownLatch.await(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void feedBack(boolean needPrepare) {
        dismissDialog();
        this.mIsUploadCancel = true;
        this.mDialog = Project.getInstance().getControl().getGlobalDialog(this.mContext);
        String cancelFeedback = this.mContext.getString(R.string.global_feedback_cancel);
        this.mDialog.setParams(this.mContext.getString(R.string.global_feedback_uploading), cancelFeedback, this.mCancelUploadListener);
        this.mDialog.setOnDismissListener(this.mUploadingDismissListener);
        this.mDialog.show();
        if (needPrepare) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    if (FeedBackController.this.mPrepareListener != null) {
                        LogUtils.d("EPG/utils/FeedBackController", "feedBack()----prepare start");
                        FeedBackController.this.prepare();
                    }
                    LogUtils.d("EPG/utils/FeedBackController", "feedBack()----prepare end");
                    FeedBackController.this.startUpload(false);
                }
            });
        } else {
            startUpload(false);
        }
    }

    private void feedBackFail(final String error) {
        if (this.mDialog == null || !this.mDialog.isShowing()) {
            LogUtils.d("EPG/utils/FeedBackController", "feedBackFail()----the uploading dialog is not show, so break!");
            return;
        }
        this.mIsUploadCancel = false;
        this.mhandler.post(new Runnable() {
            public void run() {
                try {
                    if (((Activity) FeedBackController.this.mContext).isFinishing()) {
                        LogUtils.e("EPG/utils/FeedBackController", "EPG/utils/FeedBackController--->>feedBackFail()----activity is finish");
                        return;
                    }
                } catch (Exception e) {
                }
                FeedBackController.this.dismissDialog();
                Bitmap qRBitmap = BitmapFactory.decodeResource(FeedBackController.this.mContext.getResources(), R.drawable.share_btn_transparent);
                String message = FeedBackController.this.mContext.getString(R.string.logrecordFailed_with_qr);
                String rptBtnText = FeedBackController.this.mContext.getString(R.string.logrecordRetry);
                String cancelBtnText = FeedBackController.this.mContext.getString(R.string.Cancel);
                FeedBackController.this.mDialog = EpgAppConfig.getGlobalQRDialog(FeedBackController.this.mContext);
                StringModel stringModel = new StringModel();
                stringModel.mIsFeedbackSuccess = false;
                stringModel.mContentString = message;
                ((GlobalQRFeedBackDialog) FeedBackController.this.mDialog).setParams(stringModel, qRBitmap, rptBtnText, FeedBackController.this.onRetryBtnListener, cancelBtnText, new OnClickListener() {
                    public void onClick(View v) {
                        FeedBackController.this.mIsRestryClick = false;
                        GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                        FeedBackController.this.dismissDialog();
                    }
                });
                FeedBackController.this.mIsRestryClick = false;
                FeedBackController.this.mDialog.setOnDismissListener(FeedBackController.this.mResultDialogDismisslistener);
                FeedBackController.this.mDialog.show();
                FeedBackController.this.sendShowPingBack();
                FeedBackController.this.loadQRBitmap(error);
            }
        });
    }

    private void feedBackSuccess(final String feedbackId, final String ip) {
        if (this.mDialog == null || !this.mDialog.isShowing()) {
            LogUtils.d("EPG/utils/FeedBackController", "feedBackSuccess()----the uploading dialog is not show, so break!");
            return;
        }
        this.mIsUploadCancel = false;
        this.mhandler.post(new Runnable() {
            public void run() {
                try {
                    if (((Activity) FeedBackController.this.mContext).isFinishing()) {
                        LogUtils.e("EPG/utils/FeedBackController", "EPG/utils/FeedBackController--->>feedBackSuccess()----activity is finish");
                        return;
                    }
                } catch (Exception e) {
                }
                FeedBackController.this.dismissDialog();
                Bitmap qRBitmap = BitmapFactory.decodeResource(FeedBackController.this.mContext.getResources(), R.drawable.share_btn_transparent);
                String mIp = ip;
                if (StringUtils.isEmpty((CharSequence) mIp)) {
                    mIp = LogRecordUtils.getPublicIp(FeedBackController.this.mContext);
                }
                String time = DeviceUtils.getCurrentTime();
                String rightTopMessage = FeedBackController.this.mContext.getString(R.string.logrecordSuccess_with_qr_right_top);
                String rightBottomMessage = FeedBackController.this.mContext.getString(R.string.logrecordSuccess_with_qr_right_bottom, new Object[]{mIp, DeviceUtils.getMacAddr(), LogRecordUtils.getVersionCode(), time});
                String leftBottomMessage = FeedBackController.this.mContext.getString(R.string.logrecordSuccess_with_qr_left_bottom, new Object[]{feedbackId});
                StringModel stringModel = new StringModel();
                stringModel.mIsFeedbackSuccess = true;
                stringModel.mRightTopString = rightTopMessage;
                stringModel.mRightBottomString = rightBottomMessage;
                stringModel.mLeftBottomString = leftBottomMessage;
                FeedBackController.this.mDialog = EpgAppConfig.getGlobalQRDialog(FeedBackController.this.mContext);
                ((GlobalQRFeedBackDialog) FeedBackController.this.mDialog).setParams(stringModel, qRBitmap, null, null, null, null);
                FeedBackController.this.mIsRestryClick = false;
                FeedBackController.this.mDialog.setOnDismissListener(FeedBackController.this.mResultDialogDismisslistener);
                FeedBackController.this.mDialog.show();
                GetInterfaceTools.getILogRecordProvider().getUploadCore().resetFeedbackValue();
                FeedBackController.this.sendShowPingBack();
                FeedBackController.this.loadQRBitmap(feedbackId, time, mIp, FeedBackController.this.mContext);
            }
        });
    }

    private void sendShowPingBack() {
        PingBackParams params = new PingBackParams();
        params.add("qtcurl", "failfb_dlg").add(Keys.T, "21").add("block", SourceType.feedback.toString()).add("bstp", "1");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private void loadQRBitmap(String feedbackid, String time, String ip, Context mContext) {
        ImageView qrImageView = ((GlobalQRFeedBackDialog) this.mDialog).getQRImageView();
        if (qrImageView != null) {
            qrImageView.setBackgroundColor(871494129);
            ((GlobalQRFeedBackDialog) this.mDialog).setLoadingVisible(0);
            final String mHUrl = LogRecordUtils.getFeedbackUrl(this.mFeedBackModel.getQRMap(feedbackid, time, ip, mContext));
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    FeedBackController.this.displayQR(QRUtils.createQRImage(mHUrl));
                }
            });
        }
    }

    private void loadQRBitmap(String info) {
        ImageView qrImageView = ((GlobalQRFeedBackDialog) this.mDialog).getQRImageView();
        if (qrImageView != null) {
            qrImageView.setBackgroundColor(871494129);
            ((GlobalQRFeedBackDialog) this.mDialog).setLoadingVisible(0);
            this.mFeedBackModel.setFeedbackErrorCode(info);
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    FeedBackController.this.displayQR(QRUtils.createQRImage(FeedBackController.this.mFeedBackModel.getQRString()));
                }
            });
        }
    }

    private void displayQR(final Bitmap bitmap) {
        this.mhandler.post(new Runnable() {
            public void run() {
                if (FeedBackController.this.mDialog != null && FeedBackController.this.mDialog.isShowing() && (FeedBackController.this.mDialog instanceof GlobalQRFeedBackDialog)) {
                    ImageView qrImageView = ((GlobalQRFeedBackDialog) FeedBackController.this.mDialog).getQRImageView();
                    if (!(qrImageView == null || bitmap == null)) {
                        qrImageView.setBackgroundColor(-1);
                        qrImageView.setImageBitmap(bitmap);
                    }
                    ((GlobalQRFeedBackDialog) FeedBackController.this.mDialog).setLoadingVisible(8);
                    if (bitmap == null) {
                        ((GlobalQRFeedBackDialog) FeedBackController.this.mDialog).showQRFail();
                    }
                }
            }
        });
    }
}
