package com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
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
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.SourceType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.UploadResultControllerListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadExtraMap;
import com.gala.video.lib.share.ifmanager.bussnessIF.logrecord.collection.UploadOptionMap;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.reflect.Field;

public class GlobalQRFeedbackPanel extends FrameLayout {
    private static final String LOG_TAG = "GlobalQRFeedbackPanel";
    private ApiException mApiException;
    private ViewGroup mBlankLayout;
    private final OnClickListener mClickListener = new OnClickListener() {
        public void onClick(View v) {
            if (GlobalQRFeedbackPanel.this.mIsSendingFeedback) {
                QToast.makeTextAndShow(GlobalQRFeedbackPanel.this.getContext(), R.string.feedback_sending, 2000);
            } else if (GlobalQRFeedbackPanel.this.mIsSendedFeedbackSuccess) {
                QToast.makeTextAndShow(GlobalQRFeedbackPanel.this.getContext(), R.string.feedback_retry, 2000);
            } else {
                Log.e(GlobalQRFeedbackPanel.LOG_TAG, "##################  click button ##################");
                if (GetInterfaceTools.getILogRecordProvider().isLogRecordFeatureReady()) {
                    UploadExtraMap extraMap = new UploadExtraMap();
                    UploadOptionMap optionMap = new UploadOptionMap();
                    optionMap.setIsUploadlogcat(true);
                    optionMap.setIsUploadtrace(false);
                    optionMap.setIsUploadGalabuffer(true);
                    UploadExtraInfo feedbackExtraInfo = GetInterfaceTools.getILogRecordProvider().getUploadExtraInfoAndParse(extraMap);
                    UploadOption feedbackOption = GetInterfaceTools.getILogRecordProvider().getUploadOptionInfoAndParse(optionMap);
                    String errorCode = GlobalQRFeedbackPanel.this.mApiException.getCode();
                    String errorMessage = GlobalQRFeedbackPanel.getStackTrace(GlobalQRFeedbackPanel.this.mApiException);
                    String errorApiname = GlobalQRFeedbackPanel.this.mApiException.getApiName();
                    Log.v(GlobalQRFeedbackPanel.LOG_TAG, "errorCode = " + errorCode);
                    Log.v(GlobalQRFeedbackPanel.LOG_TAG, "errorMessage = " + errorMessage);
                    Log.v(GlobalQRFeedbackPanel.LOG_TAG, "errorApiname = " + errorApiname);
                    if (errorMessage != null && errorMessage.length() >= 250) {
                        errorMessage = errorMessage.substring(0, 250);
                    }
                    Recorder recorder = new RecorderBuilder(RecorderType._FEEDBACK, RecorderLogType.LOGRECORD_SECOND_FEEDBACK, Project.getInstance().getBuild().getVersionString(), Build.MODEL.replace(" ", "-"), Project.getInstance().getBuild().getVrsUUID(), DeviceUtils.getMacAddr()).setQuesType(FeedbackType.COMMON).setErrorCode(errorCode).setErrorMessagec(errorMessage).setApiName(errorApiname).setIddRecord(LogRecordConfigUtils.getGlobalConfig().getString()).build();
                    IFeedbackResultCallback feedbackResultCallback = CreateInterfaceTools.createFeedbackResultListener();
                    feedbackResultCallback.setRecorderType(recorder.getRecorderType());
                    feedbackResultCallback.init(GlobalQRFeedbackPanel.this.getContext(), feedbackExtraInfo, feedbackOption, recorder, GlobalQRFeedbackPanel.this.mApiException, new UploadResultControllerListener() {
                        public void onSuccess() {
                            GlobalQRFeedbackPanel.this.mIsSendingFeedback = false;
                            GlobalQRFeedbackPanel.this.mIsSendedFeedbackSuccess = true;
                        }

                        public void onFailure() {
                            GlobalQRFeedbackPanel.this.mIsSendingFeedback = false;
                            GlobalQRFeedbackPanel.this.mIsSendedFeedbackSuccess = false;
                        }
                    }, SourceType.feedback);
                    GlobalQRFeedbackPanel.this.mIsSendingFeedback = true;
                    GlobalQRFeedbackPanel.this.mIsSendedFeedbackSuccess = false;
                    feedbackResultCallback.setPageType(5);
                    feedbackResultCallback.setSdkError(GlobalQRFeedbackPanel.this.mSdkError);
                    GetInterfaceTools.getILogRecordProvider().getUploadCore().sendRecorder(feedbackExtraInfo, feedbackOption, recorder, feedbackResultCallback.getFeedbackResultListener());
                    return;
                }
                LogRecordUtils.showLogRecordNotAlreadyToast(GlobalQRFeedbackPanel.this.getContext());
            }
        }
    };
    private TextView mErrorText;
    private Button mFeedBackBtn;
    private final OnFocusChangeListener mFocusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.06f, 200);
        }
    };
    private boolean mIsButton;
    private boolean mIsSendedFeedbackSuccess;
    private boolean mIsSendingFeedback;
    private ISdkError mSdkError;

    public GlobalQRFeedbackPanel(Context context) {
        super(context);
        init(context);
    }

    public GlobalQRFeedbackPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlobalQRFeedbackPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        try {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);
            layoutInflater.inflate(R.layout.share_global_panel_error_view, this);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        initView();
    }

    private void initView() {
        this.mErrorText = (TextView) findViewById(R.id.share_global_error_panel_tv);
        this.mFeedBackBtn = (Button) findViewById(R.id.share_global_error_panel_btn);
        this.mBlankLayout = (ViewGroup) findViewById(R.id.share_global_error_panel_layout);
        this.mFeedBackBtn.setOnClickListener(this.mClickListener);
        this.mFeedBackBtn.setOnFocusChangeListener(this.mFocusListener);
    }

    public static String getStackTrace(Exception exception) {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] frames = exception.getStackTrace();
        for (int j = 1; j < frames.length; j++) {
            sb.append(frames[j].toString() + "\n");
        }
        return sb.toString();
    }

    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == 0) {
            this.mIsSendingFeedback = false;
            this.mIsSendedFeedbackSuccess = false;
        }
    }

    public void addMessageView(View view) {
        if (view != null) {
            this.mBlankLayout.setVisibility(0);
            this.mErrorText.setVisibility(8);
            this.mFeedBackBtn.setVisibility(8);
            this.mIsButton = false;
            this.mBlankLayout.removeAllViewsInLayout();
            this.mBlankLayout.addView(view);
        }
    }

    public void setQRText(String errorText) {
        this.mBlankLayout.setVisibility(8);
        this.mErrorText.setVisibility(0);
        this.mFeedBackBtn.setVisibility(0);
        this.mIsButton = true;
        this.mErrorText.setText(ResourceUtil.getStr(R.string.feedback_tip, errorText));
    }

    public void setQRTextColor(int color) {
        if (this.mErrorText != null) {
            this.mErrorText.setTextColor(color);
        }
    }

    public synchronized boolean isShowButton() {
        return this.mIsButton;
    }

    public void setQRExcetion(ApiException e) {
        this.mApiException = e;
    }

    public Button getButton() {
        return this.mFeedBackBtn;
    }

    public void setSdkError(ISdkError error) {
        this.mSdkError = error;
    }
}
