package com.gala.video.app.player.controller.error;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Process;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.report.core.upload.tracker.TrackerRecord;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.ErrorDialogListener;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.IMedia;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnUserReplayListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.ErrorConstants;
import com.gala.sdk.player.error.IApiError;
import com.gala.sdk.player.error.IErrorStrategy;
import com.gala.sdk.player.error.IErrorStrategy.IDiagnoseInfoProvider;
import com.gala.sdk.player.error.IErrorStrategy.IDiagnoseListener;
import com.gala.sdk.player.error.IFeedbackCallback;
import com.gala.sdk.player.ui.IPlayerOverlay;
import com.gala.sdk.utils.MyLogUtils;
import com.gala.tvapi.tv2.constants.ApiCode;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.controller.error.ErrorDialogHelper.DialogType;
import com.gala.video.app.player.data.PlayerFeedbackModel;
import com.gala.video.app.player.utils.PlayerToastHelper;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController.OnFeedBackPrepareListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.errorcode.ErrorCodeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.IErrorHandler.ErrorType;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.ISuperPlayerOverlay;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.ErrorUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingPlayPreference;
import com.gala.video.lib.share.utils.DataUtils;

public abstract class AbsErrorStrategy implements IErrorStrategy {
    private static final String CAROUSEL_LIVE_C1 = "101221";
    private final String TAG = ("Player/Error/AbsErrorStrategy" + Integer.toHexString(hashCode()));
    protected Context mContext;
    protected IFeedbackDialogController mController;
    private IDiagnoseListener mDiagnoseListener;
    protected ISdkError mError;
    private ErrorDialogListener mErrorDialogListener;
    private OnClickListener mFeedBackBtnListener = new C13892();
    private IFeedbackCallback mFeedbackCallback;
    private OnFeedBackPrepareListener mFeedbackPrepareListener = new C13925();
    private boolean mIsNetworkDialogError = false;
    private OnDismissListener mOnDismissListener = new C13936();
    private OnUserReplayListener mOnUserReplayListener;
    protected ISuperPlayerOverlay mOverlay;
    private TrackerRecord mRecord;
    private OnClickListener mReplayBtnListener = new C13881();

    class C13881 implements OnClickListener {
        C13881() {
        }

        public void onClick(View view) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AbsErrorStrategy.this.TAG, "onReplayBtn clicked " + AbsErrorStrategy.this + " " + AbsErrorStrategy.this.mOnUserReplayListener);
            }
            IVideo video = (IVideo) AbsErrorStrategy.this.mController.getMeida();
            String c1 = String.valueOf(video.getChannelId());
            String now_qpid = String.valueOf(video.getTvId());
            if (!StringUtils.isEmpty(video.getLiveChannelId())) {
                c1 = AbsErrorStrategy.CAROUSEL_LIVE_C1;
                String channelId = video.getLiveChannelId();
                CharSequence programId = video.getLiveProgramId();
                if (StringUtils.isEmpty(programId)) {
                    now_qpid = channelId;
                } else {
                    CharSequence now_qpid2 = programId;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AbsErrorStrategy.this.TAG, "c1=" + c1);
            }
            PingBackParams params = new PingBackParams();
            params.add(Keys.f2035T, "20").add("rpage", "player").add("block", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).add("rseat", "retry").add("c1", c1).add("now_c1", c1).add("now_qpid", now_qpid).add("r", "");
            PingBack.getInstance().postPingBackToLongYuan(params.build());
            AbsErrorStrategy.this.mOnUserReplayListener.onReplay();
        }
    }

    class C13892 implements OnClickListener {
        C13892() {
        }

        public void onClick(View v) {
            IVideo video = (IVideo) AbsErrorStrategy.this.mController.getMeida();
            String c1 = String.valueOf(video.getChannelId());
            String now_qpid = String.valueOf(video.getTvId());
            if (!StringUtils.isEmpty(video.getLiveChannelId())) {
                c1 = AbsErrorStrategy.CAROUSEL_LIVE_C1;
                String channelId = video.getLiveChannelId();
                CharSequence programId = video.getLiveProgramId();
                if (StringUtils.isEmpty(programId)) {
                    now_qpid = channelId;
                } else {
                    CharSequence now_qpid2 = programId;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AbsErrorStrategy.this.TAG, "c1=" + c1);
            }
            PingBackParams params = new PingBackParams();
            params.add(Keys.f2035T, "20").add("rpage", "player").add("block", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).add("rseat", "fb").add("c1", c1).add("now_c1", c1).add("now_qpid", now_qpid).add("r", "");
            PingBack.getInstance().postPingBackToLongYuan(params.build());
            AbsErrorStrategy.this.mController.feedback();
        }
    }

    class C13903 implements OnDismissListener {
        C13903() {
        }

        public void onDismiss(DialogInterface arg0) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AbsErrorStrategy.this.TAG, "onDismiss");
            }
            Process.killProcess(Process.myPid());
        }
    }

    class C13914 implements OnClickListener {
        C13914() {
        }

        public void onClick(View v) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(AbsErrorStrategy.this.TAG, "right button.onClick");
            }
            Process.killProcess(Process.myPid());
        }
    }

    class C13925 implements OnFeedBackPrepareListener {
        C13925() {
        }

        public String onPrepare() {
            if (AbsErrorStrategy.this.mFeedbackCallback != null) {
                return AbsErrorStrategy.this.mFeedbackCallback.onPrepareFeedback();
            }
            return null;
        }
    }

    class C13936 implements OnDismissListener {
        C13936() {
        }

        public void onDismiss(DialogInterface dialog) {
            if (AbsErrorStrategy.this.mErrorDialogListener != null) {
                AbsErrorStrategy.this.mErrorDialogListener.onErrorFinished();
            }
        }
    }

    class C13947 implements OnClickListener {
        C13947() {
        }

        public void onClick(View v) {
            LogUtils.m1568d(AbsErrorStrategy.this.TAG, "cancel clicked");
            if (AbsErrorStrategy.this.mDiagnoseListener != null) {
                AbsErrorStrategy.this.mDiagnoseListener.onDiagnoseCanceled();
            }
        }
    }

    public AbsErrorStrategy(Context context, IFeedbackDialogController controller, IPlayerOverlay overlay) {
        this.mContext = context;
        this.mController = controller;
        this.mController.setPrepareListener(this.mFeedbackPrepareListener);
        this.mOverlay = (ISuperPlayerOverlay) overlay;
    }

    public void handleLiveProgramFinished(IVideo iBasicVideo) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleLiveProgramFinished called");
        }
        PlayerToastHelper.showToast(this.mContext, C1291R.string.live_program_finished, (int) QToast.LENGTH_LONG);
    }

    public void showErrorWithServerMsg(String content, String errLog, String errCode, String qrMessage) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "82 showErrorWithServerMsg errCode:" + errCode + ",content:" + content);
        }
        showQrDialog(content, errLog, errCode, qrMessage);
    }

    public void handleUserVipStatusIncorrectError(IVideo video) {
        if (video != null && video.getProvider() != null) {
            SourceType sourceType = video.getProvider().getSourceType();
            if (SourceType.PUSH != sourceType) {
                if (video.isFlower() && video.getProvider().getSourceType() == SourceType.LIVE) {
                    video = video.getProvider().getLiveVideo();
                }
                startPurchasePage(1, 5, video.getAlbum(), 2);
                return;
            }
            ErrorDialogHelper.createOpenVipDialog(this.mContext, false, this.mErrorDialogListener, sourceType);
        }
    }

    private void startPurchasePage(int pageType, int enterType, Album albumInfo, int requestCode) {
        if (this.mContext instanceof Activity) {
            Activity activity = this.mContext;
            String eventId = activity.getIntent().getStringExtra("eventId");
            String buySource = activity.getIntent().getStringExtra("buy_source");
            String from = activity.getIntent().getStringExtra("from");
            String state = "";
            if (albumInfo.isLive == 1) {
                if (StringUtils.parse(albumInfo.sliveTime, -1) < DeviceUtils.getServerTimeMillis()) {
                    state = WebConstants.STATE_ONAIR;
                } else {
                    state = WebConstants.STATE_COMING;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "goToBuy:pageType=" + pageType + ", enterType=" + enterType + ", buySource=" + buySource + ", album=" + DataUtils.albumInfoToString(albumInfo) + ", requestCode=" + 2 + ", state=" + state);
            }
            WebIntentParams params = new WebIntentParams();
            params.pageType = pageType;
            params.enterType = enterType;
            params.from = from;
            params.buySource = buySource;
            params.albumInfo = albumInfo;
            params.requestCode = requestCode;
            params.eventId = eventId;
            params.state = state;
            params.buyFrom = "vip_noplay_jump";
            GetInterfaceTools.getWebEntry().startPurchasePage(activity, params);
        }
    }

    private void showQrDialog(String content, String errLog, String errCode, String qrMessage) {
        showQrDialog(true, content, errLog, errCode, qrMessage);
    }

    private void showQrDialog(boolean needLogcat, String content, String errLog, String errCode, String qrMessage) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showQrDialog " + this.mController.getMeida() + " , " + this);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showQrDialog content=" + content + ",qrMessage=" + qrMessage + ",errCode=" + errCode + ", mError=" + this.mError);
        }
        PlayerFeedbackModel model = new PlayerFeedbackModel();
        model.setRecord(this.mRecord);
        model.setErrorMsg(content);
        model.setErrorLog(qrMessage + "\n" + GetInterfaceTools.getILogRecordProvider().getLogCore().getLogFromLogcatBuffer(300));
        if (this.mError != null) {
            model.setErrorCode(this.mError.toUniqueCode());
        } else {
            model.setErrorCode(errCode);
        }
        model.setSDKError(this.mError);
        model.setQrMessage(qrMessage);
        model.setNeedLogcat(needLogcat);
        if (!(this.mRecord == null || StringUtils.isEmpty(this.mRecord.getApiName().trim()))) {
            model.setApiName(this.mRecord.getApiName());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showQrDialog mMedia=" + this.mController.getMeida().getLiveChannelId());
        }
        IVideo video = (IVideo) this.mController.getMeida();
        String c1 = String.valueOf(video.getChannelId());
        String now_qpid = String.valueOf(video.getTvId());
        if (!StringUtils.isEmpty(video.getLiveChannelId())) {
            c1 = CAROUSEL_LIVE_C1;
            String channelId = video.getLiveChannelId();
            CharSequence programId = video.getLiveProgramId();
            if (StringUtils.isEmpty(programId)) {
                now_qpid = channelId;
            } else {
                CharSequence now_qpid2 = programId;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "c1=" + c1);
        }
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "21").add("qtcurl", "player").add("block", MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR).add("c1", c1).add("now_c1", c1).add("now_qpid", now_qpid).add("qpid", "");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        this.mController.showQRDialog(model, this.mOnDismissListener, this.mContext.getString(C1291R.string.popup_dialog_feedback_btn_text), this.mFeedBackBtnListener, this.mContext.getString(C1291R.string.error_dialog_retry), this.mReplayBtnListener);
    }

    public void handleVipAccountError(String errorCode) {
        ErrorDialogHelper.createVipAccountErrorDialog(this.mContext, errorCode, this.mOnDismissListener);
    }

    public void handleVideoOfflineError(String insideMsg, String log, String qRMsg) {
        showQrDialog(!StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.video_offline_error), log, null, qRMsg);
    }

    public void handleForeignIpError(String insideMsg, String log, String qRMsg) {
        showQrDialog(!StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.foreign_ip_error), log, null, qRMsg);
    }

    public void handleCopyrightRestrictionError(boolean needlobacat, String insideMsg, String log, String qRMsg) {
        showQrDialog(needlobacat, !StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.copy_restriction_error), log, null, qRMsg);
    }

    public void handleLiveCopyrightRestrictionError(String insideMsg, String log, String qRMsg) {
        showQrDialog(!StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.copy_restriction_live_error), log, null, qRMsg);
    }

    public void handleLiveCommonError(String errCode, String insideMsg, String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleLiveCommonError: errCode={" + errCode + "}");
        }
        showQrDialog(!StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.common_live_error), log, null, qRMsg);
    }

    public void handleNativePlayerBlockError(String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleNativePlayerBlockError: qrMsg={" + qRMsg + "}");
        }
        handleBlockError(log, qRMsg);
    }

    private void handleBlockError(String log, String qRMsg) {
        String rightText = this.mContext.getString(C1291R.string.restart);
        String content = this.mContext.getString(C1291R.string.native_player_block);
        PlayerFeedbackModel model = new PlayerFeedbackModel();
        model.setRecord(this.mRecord);
        model.setErrorMsg(content);
        model.setErrorLog(qRMsg + log + GetInterfaceTools.getILogRecordProvider().getLogCore().getLogFromLogcatBuffer(300));
        model.setQrMessage(qRMsg);
        model.setNeedLogcat(true);
        this.mController.showQRDialog(model, new C13903(), null, null, rightText, new C13914());
    }

    public void handleNativePlayer4016Error(String log, String qRMsg) {
        showQrDialog(false, this.mContext.getString(C1291R.string.nativeerror_4016), log, null, qRMsg);
    }

    public void handleNativePlayer4011And4012Error(String log, String qRMsg) {
        showQrDialog(false, this.mContext.getString(C1291R.string.nativeerror_4011_4012), log, null, qRMsg);
    }

    public void handleSystemPlayerOfflinePlaybackError(String log, String qRMsg) {
        showQrDialog(this.mContext.getString(C1291R.string.offline_player_error), log, null, qRMsg);
    }

    public void handleSystemPlayerCommonError(String errorCode, String log, String qRMsg) {
        showQrDialog(this.mContext.getString(C1291R.string.system_player_error, new Object[]{errorCode}), log, null, qRMsg);
    }

    public void handleNativePlayerCommonError(String errCode, String log, String qRMsg, int playerType) {
        String content = this.mContext.getString(C1291R.string.native_player_error, new Object[]{errCode});
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "playerType = " + playerType);
        }
        showQrDialog(true, content, log, null, qRMsg);
    }

    public void handlePreviewFinished(IVideo video) {
        if (video != null) {
            SourceType sourceType = video.getProvider().getSourceType();
            if (!ErrorDialogHelper.isDialogShowing(DialogType.OPEN_VIP)) {
                ErrorDialogHelper.createOpenVipDialog(this.mContext, true, this.mErrorDialogListener, sourceType);
            }
        }
    }

    public void handleCommonApiError(IApiError apiErr, String qRMsg, String log) {
        FeedBackModel model = CreateInterfaceTools.createFeedbackFactory().createFeedBack(apiErr.getApiException());
        showQrDialog(true, model.getErrorMsg(), qRMsg + "\n" + log, model.getErrorCode(), qRMsg);
    }

    public void onErrorClicked() {
    }

    public void skipError() {
        PlayerToastHelper.showToast(this.mContext, C1291R.string.jump_error_video, (int) QToast.LENGTH_LONG);
    }

    public void onDialogListenerRetryClicked(ErrorDialogListener listener) {
        if (listener != null) {
            listener.onRetryClicked();
            ErrorDialogHelper.clearCurrentDialog();
        }
    }

    public void handleNetworkConnected(int netState, boolean isPrepared) {
        String message = "";
        switch (netState) {
            case 0:
                showToast(this.mContext.getResources().getString(C1291R.string.result_no_net));
                return;
            case 1:
            case 2:
                if (this.mIsNetworkDialogError) {
                    if (ErrorDialogHelper.isDialogShowing(DialogType.NETWORK)) {
                        ErrorDialogHelper.clearCurrentDialog();
                    }
                    onDialogListenerRetryClicked(this.mErrorDialogListener);
                    this.mIsNetworkDialogError = false;
                    return;
                } else if (isPrepared) {
                    showToast(this.mContext.getResources().getString(C1291R.string.tip_connect_network));
                    return;
                } else {
                    return;
                }
            case 3:
            case 4:
                showToast(this.mContext.getResources().getString(Project.getInstance().getResProvider().getCannotConnInternet()));
                return;
            default:
                return;
        }
    }

    public void clearCurrentDialog() {
        ErrorDialogHelper.clearCurrentDialog();
        if (this.mController != null) {
            this.mController.clearCurrentDialog();
        }
    }

    public void showDiagnoseDialog(Context context, IVideo video, int lagSeconds, OnClickListener cancelListener, int playerType) {
        ErrorDialogHelper.createStartDiagnoseDialog(context, video, lagSeconds, cancelListener, playerType);
        PlayerToastHelper.hidePlayerToast();
    }

    public void showDiagnoseDialog(Context context, Album album, int diagnosePosition, BitStream bitStream, int playerType, OnClickListener cancelListener) {
        saveNetDoctorBitStream(bitStream);
        ErrorDialogHelper.createStartDiagnoseDialog(context, album, diagnosePosition, bitStream, cancelListener, playerType);
        PlayerToastHelper.hidePlayerToast();
    }

    private void saveNetDoctorBitStream(BitStream bitStream) {
        int definition = bitStream.getDefinition();
        int audioType = bitStream.getAudioType();
        int codecType = bitStream.getCodecType();
        int drType = bitStream.getDynamicRangeType();
        SettingPlayPreference.setKeyNetDoctorBitStreamDefinition(this.mContext, definition);
        SettingPlayPreference.setKeyNetDoctorAudioType(this.mContext, audioType);
        SettingPlayPreference.setKeyNetDoctorCodecType(this.mContext, codecType);
        SettingPlayPreference.setKeyNetDoctorDRType(this.mContext, drType);
    }

    public void setToastEnabled(boolean enabled) {
        PlayerToastHelper.setToastEnabled(enabled);
    }

    protected void showToast(String message) {
        PlayerToastHelper.showToast(this.mContext, message, (int) QToast.LENGTH_LONG);
    }

    public void setFeedbackCallback(IFeedbackCallback callback) {
        this.mFeedbackCallback = callback;
    }

    public void handleMediaPlayerError(int errorCode) {
        CharSequence errMsg;
        switch (errorCode) {
            case -1010:
                errMsg = this.mContext.getString(C1291R.string.unsupport_format);
                break;
            case -1004:
                errMsg = this.mContext.getString(C1291R.string.connection_error);
                break;
            default:
                errMsg = this.mContext.getString(C1291R.string.unknown_error);
                break;
        }
        QToast.makeTextAndShow(this.mContext, errMsg, 2000);
    }

    public void handleNetworkError(int netState) {
        switch (netState) {
            case 0:
                LogUtils.m1568d(this.TAG, "NetState NONE");
                ErrorDialogHelper.createNetworkErrorDialog(this.mContext, this.mContext.getResources().getString(C1291R.string.cannot_conn_internet), this.mErrorDialogListener);
                this.mIsNetworkDialogError = true;
                return;
            case 3:
            case 4:
                LogUtils.m1568d(this.TAG, "NetStata ERROR");
                ErrorDialogHelper.createNetworkErrorDialog(this.mContext, this.mContext.getResources().getString(Project.getInstance().getResProvider().getCannotConnInternet()), this.mErrorDialogListener);
                this.mIsNetworkDialogError = true;
                return;
            default:
                return;
        }
    }

    public void handleCarouselCommonError(String insideMsg, String log, String qRMsg) {
        showQrDialog(!StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(C1291R.string.common_live_error), log, null, qRMsg);
        this.mOverlay.showError(ErrorType.COMMON, "");
    }

    public void handleCarouselNativePlayerBlockError(String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handelCarouselNativePlayerBlockError: qrMsg={" + qRMsg + "}");
        }
        this.mOverlay.showError(ErrorType.COMMON, "");
        handleBlockError(log, qRMsg);
    }

    public void handleCarouselSpecialError(IVideo video, ISdkError error, String qRMsg) {
        SourceType sourceType = null;
        if (video.getProvider() != null) {
            sourceType = video.getProvider().getSourceType();
        }
        if (sourceType == SourceType.CAROUSEL) {
            this.mOverlay.showError(ErrorType.COMMON, "");
        } else if (sourceType == SourceType.LIVE) {
            this.mOverlay.showFullScreenHint(FullScreenHintType.LIVE);
        }
    }

    public boolean handleWithServerMsg(IVideo video, ISdkError error, String log) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleWithServerMsg: error=" + error + ", video=" + video);
        }
        if (error == null) {
            return false;
        }
        boolean needShowCarouselErrorLayer = false;
        if (video.getProvider().getSourceType() == SourceType.CAROUSEL) {
            if (!isVipAccountError(error)) {
                return false;
            }
            needShowCarouselErrorLayer = true;
        }
        if (video.isLive() && !isVipAccountError(error)) {
            return false;
        }
        if (error.getModule() == 101) {
            String errorCode = ErrorUtils.parseSecondCodeFromPumaError(error);
            if (video.isLive() && StringUtils.equals(errorCode, "A00005")) {
                return false;
            }
        }
        ErrorCodeModel errorModel = null;
        if (error.getModule() == 101) {
            if (error.getCode() == 10002) {
                return false;
            }
            String finalCode = error.getCode() + "_" + ErrorUtils.parseSecondCodeFromPumaError(error);
            if (error.getCode() == 10001) {
                finalCode = error.getServerCode();
                if (StringUtils.equals(finalCode, ApiCode.USER_INFO_CHANGED)) {
                    MyLogUtils.m462d(this.TAG, "user_info_changed: logout!");
                    GetInterfaceTools.getIGalaAccountManager().logOut(this.mContext, "", LoginConstant.LGTTYPE_EXCEPTION);
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "handleWithServerMsg: error type is NATIVE_PLAYER_ERROR, server_check_code is: " + finalCode);
            }
            errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(finalCode);
        }
        if (errorModel == null) {
            int errorModule = error.getModule();
            errorCode = String.valueOf(error.getCode());
            if (errorModule == 201 || errorModule == 203 || errorModule == ISdkError.MODULE_SERVER_TV || errorModule == 202) {
                errorCode = error.getServerCode();
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "handleWithServerMsg: error model is null, retry check with first code: " + errorCode);
            }
            errorModel = GetInterfaceTools.getErrorCodeProvider().getErrorCodeModel(errorCode);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleWithServerMsg: errorModel=" + errorModel + ", error=" + error + ", video=" + video);
        }
        if (errorModel == null) {
            return false;
        }
        String qRMsg = formatQrMsg(error.getString(), extractCommonVideoInfo(video));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleWithServerMsg: error=" + error + ", content=" + errorModel.getContent());
        }
        showErrorWithServerMsg(errorModel.getContent(), log, error.toUniqueCode(), qRMsg);
        if (needShowCarouselErrorLayer) {
            this.mOverlay.showError(ErrorType.COMMON, "");
        }
        return true;
    }

    private boolean isVipAccountError(ISdkError error) {
        boolean ret = false;
        if (error == null) {
            return false;
        }
        String serverCode = error.getServerCode();
        switch (error.getModule()) {
            case 101:
                if (error.getCode() == 10001) {
                    ret = true;
                } else {
                    ret = false;
                }
                break;
            case 201:
                if (ErrorConstants.API_ERRO_CODE_Q312.equals(serverCode) || ErrorConstants.API_ERRO_CODE_Q311.equals(serverCode)) {
                    ret = true;
                } else {
                    ret = false;
                }
                break;
            case 203:
                if (ErrorConstants.API_ERR_CODE_TOO_MANY_USERS.equals(serverCode) || ErrorConstants.API_ERR_CODE_VIP_ACCOUNT_BANNED.equals(serverCode) || ErrorConstants.API_ERR_CODE_PASSWORD_CHANGED.equals(serverCode)) {
                    ret = true;
                } else {
                    ret = false;
                }
                break;
        }
        return ret;
    }

    private static String extractCommonVideoInfo(IVideo video) {
        if (video == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        BitStream curDef = video.getCurrentBitStream();
        builder.append("video(").append("aid=").append(video.getAlbumId()).append(", tvid=").append(video.getTvId()).append(", aname=").append(video.getAlbumName()).append(", def=").append(curDef != null ? Integer.valueOf(curDef.getDefinition()) : "NULL").append(")");
        return builder.toString();
    }

    private static String formatQrMsg(String dbgMsg, String videoInfo) {
        return String.format("{\n%s, %s\n}\n\n", new Object[]{dbgMsg, videoInfo});
    }

    public void setErrorDialogListener(ErrorDialogListener listener) {
        this.mErrorDialogListener = listener;
    }

    public void handlePushLiveError() {
        ErrorDialogHelper.createCommonDialog(this.mContext, this.mContext.getResources().getString(C1291R.string.cannot_push_live_video), this.mOnDismissListener);
    }

    public void setEventId(String eventId) {
        this.mController.setEventID(eventId);
    }

    public void setMedia(IMedia media) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setMedia " + media + " , " + this);
        }
        this.mController.setMedia(media);
    }

    public void setErrorInfo(ISdkError error) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setErrorInfo error=" + error + " , " + this);
        }
        this.mError = error;
    }

    public void setTrackerRecord(TrackerRecord record) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setFeedBackRecord() record=" + record);
        }
        this.mRecord = record;
    }

    public void handleInvalidTvQidError() {
        ErrorDialogHelper.createCommonDialog(this.mContext, this.mContext.getResources().getString(C1291R.string.invalid_tvQid_error), this.mOnDismissListener);
    }

    public void handleDRMCommonError(String errorCode, String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "handleDRMCommonError: errorCode=" + errorCode);
        }
        String content = this.mContext.getString(C1291R.string.intertrust_drm_error, new Object[]{errorCode});
        if (StringUtils.equals(errorCode, String.valueOf(ISdkError.CODE_INTERTRUST_DRM_DEVICE_NOT_SUPPORT))) {
            content = this.mContext.getString(C1291R.string.common_player_error, new Object[]{errorCode});
        }
        showQrDialog(true, content, log, null, qRMsg);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!PlayerToastHelper.isLagToastShown() || event.getRepeatCount() != 0 || event.getAction() != 0 || (23 != event.getKeyCode() && 66 != event.getKeyCode())) {
            return false;
        }
        startDiagnose();
        return true;
    }

    public void setDiagnoseListener(IDiagnoseListener listener) {
        this.mDiagnoseListener = listener;
    }

    public void setOnUserReplayListener(OnUserReplayListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "OnUserReplayListener " + this + "  " + listener);
        }
        this.mOnUserReplayListener = listener;
    }

    private void startDiagnose() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "startDiagnose() + mDiagnoseListener=" + this.mDiagnoseListener);
        }
        if (this.mDiagnoseListener != null) {
            this.mDiagnoseListener.onDiagnosePreparing();
        }
        OnClickListener cancelListener = new C13947();
        if (this.mDiagnoseListener != null) {
            IDiagnoseInfoProvider provider = this.mDiagnoseListener.getDiagnoseInfoProvider();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "startDiagnose() + provider=" + provider);
            }
            if (provider != null) {
                showDiagnoseDialog(this.mContext, provider.getAlbum(), provider.getDiagnosePosition(), provider.getBitStream(), provider.getPlayerType(), cancelListener);
                if (this.mDiagnoseListener != null) {
                    this.mDiagnoseListener.onDiagnoseStarted();
                }
            }
        }
    }
}
