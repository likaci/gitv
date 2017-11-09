package com.gala.video.app.epg.ui.setting;

import android.os.Bundle;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.ucenter.record.utils.ColorString;
import com.gala.video.app.epg.ui.ucenter.record.utils.ColorString.ColorStringItem;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedbackData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackResultCallback.SourceType;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ResourceUtil;

public class FaultFeedbackActivity extends QMultiScreenActivity {
    private OnClickListener mButtonOnClickListenr = new OnClickListener() {
        public void onClick(View v) {
            IFeedbackResultCallback callback = CreateInterfaceTools.createFeedbackResultListener();
            callback.init(FaultFeedbackActivity.this);
            FeedbackData model = callback.makeFeedbackData(SourceType.failfb, true);
            callback.setRecorderType(model.getRecorder().getRecorderType());
            callback.setPageType(4);
            GetInterfaceTools.getILogRecordProvider().sendRecorder(FaultFeedbackActivity.this, model.getUploadExtraInfo(), model.getUploadOption(), model.getRecorder(), callback.getFeedbackResultListener());
            FaultFeedbackActivity.this.sendClickPingBack();
        }
    };
    private Button mFeedBackButton;
    private TextView mMethod1Step1TextView;
    private TextView mMethod1Step2TextView;
    private TextView mMethod2Step1TextView;
    private TextView mMethod2Step2TextView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_setting_fault_feedback);
        initView();
        sendPageShowPingback();
    }

    private void initView() {
        this.mMethod1Step1TextView = (TextView) findViewById(R.id.epg_setting_method1_step1_text);
        this.mMethod1Step2TextView = (TextView) findViewById(R.id.epg_setting_method1_step2_text);
        this.mMethod2Step1TextView = (TextView) findViewById(R.id.epg_setting_method2_step1_text);
        this.mMethod2Step2TextView = (TextView) findViewById(R.id.epg_setting_method2_step2_text);
        this.mFeedBackButton = (Button) findViewById(R.id.epg_setting_feedback_button);
        this.mMethod1Step1TextView.setText(formatString(getString(R.string.fault_feedback_method1_step1), getString(R.string.fault_feedback_method1_step1_key)));
        this.mMethod1Step2TextView.setText(formatString(getString(R.string.fault_feedback_method1_step2), getString(R.string.fault_feedback_method1_step2_key)));
        this.mMethod2Step1TextView.setText(formatString(getString(R.string.fault_feedback_method2_step1), getString(R.string.fault_feedback_method2_step1_key)));
        this.mMethod2Step2TextView.setText(formatString(getString(R.string.fault_feedback_method1_step2), getString(R.string.fault_feedback_method1_step2_key)));
        this.mFeedBackButton.setOnClickListener(this.mButtonOnClickListenr);
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_setting_fault_feedback_layout);
    }

    private static Spanned formatString(String totalString, String specialString) {
        ColorString msp = new ColorString(totalString);
        msp.setColor(ResourceUtil.getColor(R.color.albumview_normal_color));
        msp.setColor(new ColorStringItem(specialString, ResourceUtil.getColor(R.color.albumview_yellow_color)));
        return msp.build();
    }

    private void sendPageShowPingback() {
        PingBackParams params = new PingBackParams();
        params.add("bstp", "1").add("qtcurl", "failfb").add("block", "failfb").add(Keys.T, "21");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private void sendClickPingBack() {
        PingBackParams params = new PingBackParams();
        params.add("rt", "i").add("block", "failfb").add("rseat", "failfb").add("rpage", "failfb").add(Keys.T, "20");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
