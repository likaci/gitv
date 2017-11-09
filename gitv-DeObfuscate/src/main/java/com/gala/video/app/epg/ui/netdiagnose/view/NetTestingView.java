package com.gala.video.app.epg.ui.netdiagnose.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.netdiagnose.NetDiagnoseActivity.DiagnoseStatus;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.text.DecimalFormat;

public class NetTestingView extends RelativeLayout {
    private final int HIGH_DEFINITION = 1024;
    private final int P1080_DEFINITION = 5120;
    private final int STANDARD_DEFINITION = 512;
    private final int SUPER_DEFINITION = 3072;
    private String TAG = "NetTestingView";
    private boolean isCdnConnectFail = false;
    private int mAvgNetSpeed;
    private ImageView mCDN;
    private ImageView mCDNCross;
    private NetDiagnoseOvalView mCDNOval;
    private Context mContext;
    private ImageView mInternet;
    private ImageView mInternetCross;
    private NetDiagnoseOvalView mInternetOval;
    private View mLoadingView;
    private boolean mNeedShowNetSpeedResult;
    private TextView mResult;
    private ImageView mRouter;
    private ImageView mRouterCross;
    private NetDiagnoseOvalView mRouterOval;
    private ImageView mStatusCND;
    private ImageView mStatusInternet;
    private ImageView mStatusRouter;

    public NetTestingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public NetTestingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NetTestingView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        View child = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0508R.layout.epg_net_diagnose_testing_view, null);
        LayoutParams layoutParams = new LayoutParams(-2, -2);
        layoutParams.addRule(13);
        addView(child, layoutParams);
        this.mRouter = (ImageView) child.findViewById(C0508R.id.epg_icon_router);
        this.mInternet = (ImageView) child.findViewById(C0508R.id.epg_icon_internet);
        this.mCDN = (ImageView) child.findViewById(C0508R.id.icon_cdn);
        this.mStatusRouter = (ImageView) child.findViewById(C0508R.id.epg_status_router);
        this.mStatusInternet = (ImageView) child.findViewById(C0508R.id.epg_status_internet);
        this.mStatusCND = (ImageView) child.findViewById(C0508R.id.epg_status_cdn);
        this.mRouterCross = (ImageView) child.findViewById(C0508R.id.epg_status_router_cross);
        this.mInternetCross = (ImageView) child.findViewById(C0508R.id.epg_status_internet_cross);
        this.mCDNCross = (ImageView) child.findViewById(C0508R.id.epg_status_cdn_cross);
        this.mRouterOval = (NetDiagnoseOvalView) child.findViewById(C0508R.id.epg_icon_router_oval);
        this.mInternetOval = (NetDiagnoseOvalView) child.findViewById(C0508R.id.epg_icon_internet_oval);
        this.mCDNOval = (NetDiagnoseOvalView) child.findViewById(C0508R.id.epg_icon_cdn_oval);
        this.mLoadingView = child.findViewById(C0508R.id.progress_loading);
        this.mResult = (TextView) child.findViewById(C0508R.id.txt_result);
    }

    private void showResult(String msg) {
        LogUtils.m1568d(this.TAG, "showResult=" + msg);
        this.mLoadingView.setVisibility(8);
        this.mResult.setVisibility(0);
        this.mResult.setText(msg);
    }

    private void showResult(SpannableStringBuilder builder) {
        this.mLoadingView.setVisibility(8);
        this.mResult.setVisibility(0);
        this.mResult.setText(builder);
    }

    public void setAverageSpeed(int averageSpeed) {
        LogUtils.m1568d(this.TAG, "setAverageSpeed=" + averageSpeed);
        this.mAvgNetSpeed = averageSpeed;
    }

    public void setNetStatus(DiagnoseStatus status) {
        LogUtils.m1568d(this.TAG, "setNetStatus = " + status);
        switch (status) {
            case TESTING_BEGIN:
                this.mRouter.setImageResource(C0508R.drawable.epg_icon_router_grey);
                this.mInternet.setImageResource(C0508R.drawable.epg_icon_internet_grey);
                this.mCDN.setImageResource(C0508R.drawable.epg_icon_cdn_grey);
                this.mStatusRouter.setVisibility(8);
                this.mRouterOval.setVisibility(0);
                this.mRouterOval.startAnotation();
                this.mStatusRouter.setImageResource(C0508R.drawable.epg_net_connect_failed);
                this.mStatusInternet.setImageResource(C0508R.drawable.epg_net_connect_failed);
                this.mStatusCND.setImageResource(C0508R.drawable.epg_net_connect_failed);
                this.mRouterCross.setVisibility(4);
                this.mInternetCross.setVisibility(4);
                this.mCDNCross.setVisibility(4);
                this.mLoadingView.setVisibility(0);
                this.mResult.setVisibility(8);
                this.mStatusCND.setVisibility(0);
                this.mCDNOval.stopAnotation();
                this.mCDNOval.setVisibility(8);
                return;
            case ROUTER_CONNECTED:
                this.mStatusRouter.setVisibility(0);
                this.mRouterOval.stopAnotation();
                this.mRouterOval.setVisibility(8);
                this.mStatusInternet.setVisibility(8);
                this.mInternetOval.setVisibility(0);
                this.mInternetOval.startAnotation();
                this.mRouterCross.setVisibility(0);
                this.mRouterCross.setImageResource(C0508R.drawable.epg_net_connect_success_cross);
                this.mStatusRouter.setImageResource(C0508R.drawable.epg_net_connect_success);
                this.mRouter.setImageResource(C0508R.drawable.epg_icon_router_hl);
                return;
            case ROUTER_DISCONNECTED:
                this.mStatusRouter.setVisibility(0);
                this.mRouterOval.stopAnotation();
                this.mRouterOval.setVisibility(8);
                this.mRouterCross.setVisibility(0);
                this.mRouterCross.setImageResource(C0508R.drawable.epg_net_connect_fail_cross);
                this.mStatusRouter.setImageResource(C0508R.drawable.epg_net_connect_failed);
                this.mRouter.setImageResource(C0508R.drawable.epg_icon_router_grey);
                showResult(this.mContext.getResources().getString(C0508R.string.result_no_net));
                return;
            case INTERNET_CONNECTED:
                this.mStatusInternet.setVisibility(0);
                this.mInternetOval.stopAnotation();
                this.mInternetOval.setVisibility(8);
                this.mStatusCND.setVisibility(8);
                this.mCDNOval.setVisibility(0);
                this.mCDNOval.startAnotation();
                this.mInternetCross.setVisibility(0);
                this.mInternetCross.setImageResource(C0508R.drawable.epg_net_connect_success_cross);
                this.mStatusInternet.setImageResource(C0508R.drawable.epg_net_connect_success);
                this.mInternet.setImageResource(C0508R.drawable.epg_icon_internet_hl);
                return;
            case INTERNET_DISCONNECTED:
                this.mStatusInternet.setVisibility(0);
                this.mInternetOval.stopAnotation();
                this.mInternetOval.setVisibility(8);
                this.mInternetCross.setVisibility(0);
                this.mInternetCross.setImageResource(C0508R.drawable.epg_net_connect_fail_cross);
                this.mStatusInternet.setImageResource(C0508R.drawable.epg_net_connect_failed);
                this.mInternet.setImageResource(C0508R.drawable.epg_icon_internet_grey);
                showResult(this.mContext.getResources().getString(C0508R.string.result_no_internet));
                return;
            case CDN_CONNECTED:
                this.mStatusCND.setVisibility(0);
                this.mCDNOval.stopAnotation();
                this.mCDNOval.setVisibility(8);
                this.mCDNCross.setVisibility(0);
                this.mCDNCross.setImageResource(C0508R.drawable.epg_net_connect_success_cross);
                this.mStatusCND.setImageResource(C0508R.drawable.epg_net_connect_success);
                this.mCDN.setImageResource(C0508R.drawable.epg_icon_cdn_hl);
                this.isCdnConnectFail = false;
                return;
            case CDN_DISCONNECTED:
                this.mStatusCND.setVisibility(0);
                this.mCDNOval.stopAnotation();
                this.mCDNOval.setVisibility(8);
                this.mCDNCross.setVisibility(0);
                this.mCDNCross.setImageResource(C0508R.drawable.epg_net_connect_fail_cross);
                this.mStatusCND.setImageResource(C0508R.drawable.epg_net_connect_success);
                this.mCDN.setImageResource(C0508R.drawable.epg_icon_cdn_grey);
                this.isCdnConnectFail = true;
                return;
            case COLLECTON_SUCCESS:
                if (this.isCdnConnectFail) {
                    showResult(this.mContext.getString(C0508R.string.result_no_cdn) + this.mContext.getString(C0508R.string.feedback_guide));
                    return;
                } else if (this.mNeedShowNetSpeedResult) {
                    showResult(getSpeedTestResultString());
                    return;
                } else {
                    showResult(getCollectionResultString(status));
                    return;
                }
            case COLLECTON_FAIL:
                if (this.isCdnConnectFail) {
                    showResult(this.mContext.getString(C0508R.string.result_no_cdn) + this.mContext.getString(C0508R.string.feedback_guide));
                    return;
                } else if (this.mNeedShowNetSpeedResult) {
                    showResult(getSpeedTestResultString());
                    return;
                } else {
                    showResult(getCollectionResultString(status));
                    return;
                }
            default:
                return;
        }
    }

    public void setNeedShowNetSpeedResult(boolean need) {
        this.mNeedShowNetSpeedResult = need;
    }

    private String getCollectionResultString(DiagnoseStatus status) {
        switch (status) {
            case COLLECTON_SUCCESS:
            case COLLECTON_FAIL:
                return this.mContext.getString(C0508R.string.result_complete) + this.mContext.getString(C0508R.string.feedback_guide);
            default:
                return null;
        }
    }

    private SpannableStringBuilder getSpeedTestResultString() {
        SpannableStringBuilder sb;
        if (this.mAvgNetSpeed < 512) {
            String speedDescription = getSpeedDisplay(this.mAvgNetSpeed);
            String speedLowResult = this.mContext.getString(C0508R.string.net_speed_low, new Object[]{speedDescription});
            sb = new SpannableStringBuilder(speedLowResult);
            ForegroundColorSpan colorSpan = new ForegroundColorSpan(SettingConstants.ID_GROUP_MASK);
            int startResult = speedLowResult.indexOf(speedDescription);
            sb.setSpan(colorSpan, startResult, speedDescription.length() + startResult, 33);
        } else {
            String result = getSpeedDisplay(this.mAvgNetSpeed);
            String recommemdDefinition = getRecommendDefinition(this.mAvgNetSpeed);
            String speedResult = this.mContext.getString(C0508R.string.net_speed, new Object[]{result, recommemdDefinition});
            sb = new SpannableStringBuilder(speedResult);
            ForegroundColorSpan coloSpanSpeed = new ForegroundColorSpan(getResources().getColor(C0508R.color.skin_net_yellow_tip_txt));
            int index0 = speedResult.indexOf(result);
            sb.setSpan(coloSpanSpeed, index0, result.length() + index0, 33);
            ForegroundColorSpan colorSpanRecommendDefinition = new ForegroundColorSpan(SettingConstants.ID_GROUP_MASK);
            int index1 = speedResult.indexOf(recommemdDefinition);
            sb.setSpan(colorSpanRecommendDefinition, index1, recommemdDefinition.length() + index1, 33);
        }
        sb.append(this.mContext.getString(C0508R.string.feedback_guide));
        return sb;
    }

    private String getSpeedDisplay(int kb) {
        if (kb <= 1024) {
            return kb + "Kb/s";
        }
        return new DecimalFormat("0.0").format((double) (((float) kb) / 1024.0f)) + "Mb/s";
    }

    private String getRecommendDefinition(int kb) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "getRecommendDefinition, kB=" + kb);
        }
        String ret = "NULL";
        if (kb < 512) {
            if (!LogUtils.mIsDebug) {
                return ret;
            }
            LogUtils.m1568d(this.TAG, "getRecommendDefinition, speed too low, why reach here?");
            return ret;
        } else if (kb < 1024) {
            return this.mContext.getString(C0508R.string.definition_standard);
        } else {
            if (kb >= 1024 && kb < 3072) {
                return this.mContext.getString(C0508R.string.definition_high);
            }
            if (kb >= 3072 && kb < 5120) {
                return this.mContext.getString(C0508R.string.definition_720P);
            }
            if (kb >= 5120) {
                return this.mContext.getString(C0508R.string.definition_1080P);
            }
            return ret;
        }
    }
}
