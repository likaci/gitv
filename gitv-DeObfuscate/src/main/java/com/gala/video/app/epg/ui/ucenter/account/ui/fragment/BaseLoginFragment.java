package com.gala.video.app.epg.ui.ucenter.account.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import com.gala.video.app.epg.SupportFragment;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.ucenter.account.event.ILoginEvent;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.AccountLoginHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.webview.utils.WebSDKConstants;

public class BaseLoginFragment extends SupportFragment {
    private static final String LOG_TAG = "EPG/login/BaseLoginFragment";
    protected Activity mContext;
    protected Handler mHandler;
    protected String mIncomSrc;
    public ILoginEvent mLoginEvent;
    protected String mS1;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mContext = activity;
            this.mHandler = new Handler(Looper.getMainLooper());
            this.mLoginEvent = (ILoginEvent) activity;
        } catch (Exception e) {
            throw new IllegalStateException("your activity must implements ISearchEvent  !");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null) {
            Intent intent = getActivity().getIntent();
            if (intent != null) {
                this.mS1 = intent.getStringExtra(LoginConstant.S1_TAB);
                this.mIncomSrc = intent.getStringExtra(LoginConstant.INCOMSRC_TAB);
                LogUtils.m1570d(LOG_TAG, ">>>>> s1 --- intent.getStringExtra(ILoginConstant.S1_TAB) --- ", this.mS1);
                LogUtils.m1570d(LOG_TAG, ">>>>> s1 --- intent.getStringExtra(ILoginConstant.INCOMSRC_TAB) --- ", this.mIncomSrc);
            }
        }
    }

    protected void setBackground(View view) {
        view.setBackgroundDrawable(this.mLoginEvent.getContainerDrawable());
    }

    protected int getDimen(int id) {
        if (this.mContext != null) {
            return (int) this.mContext.getResources().getDimension(id);
        }
        return 0;
    }

    protected int getColor(int id) {
        if (this.mContext != null) {
            return this.mContext.getResources().getColor(id);
        }
        return 0;
    }

    protected String getStr(int id) {
        if (this.mContext != null) {
            return this.mContext.getResources().getString(id);
        }
        return "";
    }

    protected String getStr(int id, String obj) {
        if (this.mContext == null) {
            return "";
        }
        return this.mContext.getResources().getString(id, new Object[]{obj});
    }

    protected void gotoMyCenter(UserInfoBean userBean, final int intentFlag) {
        final Activity activity = getActivity();
        if (activity != null) {
            AccountLoginHelper.hideAndRestartScreenSaver(activity);
            activity.runOnUiThread(new Runnable() {
                public void run() {
                    Intent intent;
                    int enter_type;
                    if (intentFlag == 1) {
                        intent = activity.getIntent();
                        CharSequence code = "";
                        String s2 = "";
                        enter_type = -1;
                        if (intent != null) {
                            s2 = intent.getStringExtra(LoginConstant.ACTIVATE_S2);
                            enter_type = intent.getIntExtra(WebSDKConstants.PARAM_KEY_ENTER_TYPE, -1);
                            code = intent.getStringExtra(LoginConstant.ACTIVATE_CODE);
                        }
                        if (StringUtils.isEmpty(code)) {
                            GetInterfaceTools.getLoginProvider().startActivateActivity(activity, s2, enter_type);
                        } else {
                            GetInterfaceTools.getLoginProvider().startActivateActivityOpenApi(activity, code, intentFlag);
                        }
                        activity.finish();
                    } else if (intentFlag == 2) {
                        activity.finish();
                    } else if (intentFlag == 3) {
                        AlbumUtils.startFootPlayhistoryPage(BaseLoginFragment.this.mContext);
                        activity.finish();
                    } else if (intentFlag == 4) {
                        LogUtils.m1568d(BaseLoginFragment.LOG_TAG, "LoginConstant.LOGIN_SUCC_TO_ALBUMDETAIL ");
                        activity.setResult(10, new Intent(IntentUtils.getActionName(ActionSet.ACT_DETAIL)));
                        activity.finish();
                    } else if (intentFlag == 5) {
                        LogUtils.m1568d(BaseLoginFragment.LOG_TAG, "LoginConstant.LOGIN_SUCC_TO_BITSTREAM ");
                        activity.setResult(22, new Intent(IntentUtils.getActionName(ActionSet.ACT_DETAIL)));
                        activity.finish();
                    } else if (intentFlag == 6) {
                        LogUtils.m1568d(BaseLoginFragment.LOG_TAG, "LoginConstant.LOGIN_SUCC_TO_COUPON");
                        intent = activity.getIntent();
                        String coupon_key = "";
                        String coupon_sign = "";
                        String from = "";
                        enter_type = 0;
                        if (intent != null) {
                            coupon_key = intent.getStringExtra(LoginConstant.COUPON_CODE);
                            coupon_sign = intent.getStringExtra(LoginConstant.COUPON_SIGN_KEY);
                            from = intent.getStringExtra(LoginConstant.S2_TAB);
                            enter_type = intent.getIntExtra(LoginConstant.COUPON_ENTER_TYPE, 0);
                        }
                        WebIntentParams params = new WebIntentParams();
                        params.from = from;
                        params.couponActivityCode = coupon_key;
                        params.couponSignKey = coupon_sign;
                        params.enterType = enter_type;
                        params.incomesrc = BaseLoginFragment.this.mIncomSrc;
                        LogUtils.m1570d(BaseLoginFragment.LOG_TAG, ">>>>> from, coupon_key, coupon_sign, enterType, incomsrc = ", from, ", ", coupon_key, ", ", coupon_sign, ", ", Integer.valueOf(enter_type), ", ", BaseLoginFragment.this.mIncomSrc);
                        GetInterfaceTools.getWebEntry().startCouponActivity(BaseLoginFragment.this.mContext, params);
                        activity.finish();
                    } else if (BaseLoginFragment.this.mLoginEvent != null) {
                        BaseLoginFragment.this.mLoginEvent.onPlaySound();
                        activity.finish();
                    }
                }
            });
        }
    }
}
