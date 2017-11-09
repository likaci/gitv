package com.gala.video.app.epg.ui.ucenter.account.ui.fragment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultTinyurl;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.PassportTVHelper;
import com.gala.tvapi.vrs.result.ApiResultQuickLogin;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserInfoBean;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.callback.ILoginCallback;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.utils.QRUtils;

public class VipRightsScanLoginFragment extends BaseLoginFragment implements OnClickListener, OnFocusChangeListener {
    private static final String LOG_TAG = "EPG/login/LoginFragment";
    String QR_TIP_TEXT = "<html><head></head><body><font color=\"#000000\">请使用手机扫码</font><font color=\"#19cc03\">领取VIP特权</font></body></html>";
    private boolean flag;
    private View mFocusView;
    private int mIntentFlag;
    private int mInvalidTime = -1;
    private boolean mIsCheckable;
    private boolean mIsThreadPost;
    private int mLastSecond = 0;
    private View mLoadingView;
    private Button mLoginBtn;
    private View mMainView;
    private View mQRBgView;
    private View mQRFailView;
    private View mQRFocusView;
    private ImageView mQRImage;
    private TextView mQRTipText;
    private TextView mQRTipText2;
    private Runnable mTimeRunnable = new C11952();
    private String mToken;
    private UserInfoBean mUserInfo;
    private boolean qrShowFlag;

    class C11941 implements Runnable {
        C11941() {
        }

        public void run() {
            VipRightsScanLoginFragment.this.callQRBitmap();
        }
    }

    class C11952 implements Runnable {
        C11952() {
        }

        public void run() {
            if (VipRightsScanLoginFragment.this.mInvalidTime != -1) {
                VipRightsScanLoginFragment.this.mLastSecond = VipRightsScanLoginFragment.this.mLastSecond + 1;
                if (VipRightsScanLoginFragment.this.mLastSecond >= VipRightsScanLoginFragment.this.mInvalidTime) {
                    VipRightsScanLoginFragment.this.loadQRBitmap();
                    VipRightsScanLoginFragment.this.mLastSecond = 0;
                    VipRightsScanLoginFragment.this.mInvalidTime = -1;
                    return;
                }
                VipRightsScanLoginFragment.this.mQRImage.postDelayed(VipRightsScanLoginFragment.this.mTimeRunnable, 1000);
                if (VipRightsScanLoginFragment.this.mLastSecond % 2 == 0) {
                    VipRightsScanLoginFragment.this.checkQRLoad();
                }
            }
        }
    }

    class C11993 implements IVrsCallback<ApiResultQuickLogin> {

        class C11972 implements Runnable {
            C11972() {
            }

            public void run() {
                if (VipRightsScanLoginFragment.this.mTimeRunnable != null) {
                    LogUtils.m1571e(VipRightsScanLoginFragment.LOG_TAG, "mQRImage.removeCallbacks(r) ----- PassportTVHelper.getTVLoginToken.call --- exception");
                    VipRightsScanLoginFragment.this.mQRImage.removeCallbacks(VipRightsScanLoginFragment.this.mTimeRunnable);
                }
                VipRightsScanLoginFragment.this.mLastSecond = 0;
                VipRightsScanLoginFragment.this.mInvalidTime = -1;
                VipRightsScanLoginFragment.this.mLoadingView.setVisibility(4);
                VipRightsScanLoginFragment.this.mQRBgView.setBackgroundColor(VipRightsScanLoginFragment.this.getColor(C0508R.color.transparent));
                VipRightsScanLoginFragment.this.mQRImage.setVisibility(4);
                VipRightsScanLoginFragment.this.mQRFailView.setVisibility(0);
                VipRightsScanLoginFragment.this.mQRFailView.requestFocus();
                if (VipRightsScanLoginFragment.this.mLoginBtn != null && VipRightsScanLoginFragment.this.mLoginBtn.getVisibility() == 0) {
                    VipRightsScanLoginFragment.this.mLoginBtn.setNextFocusUpId(C0508R.id.epg_view_failure);
                }
            }
        }

        C11993() {
        }

        public void onSuccess(final ApiResultQuickLogin resultQuickLogin) {
            LogUtils.m1574i(VipRightsScanLoginFragment.LOG_TAG, "onSuccess --- PassportTVHelper.getTVLoginToken.call");
            IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            String payeeType = "";
            String ab_test = "";
            String p2 = Project.getInstance().getBuild().getPingbackP2();
            String wd = Project.getInstance().getBuild().isSupportSmallWindowPlay() ? "0" : "1";
            String tvhwver = Build.MODEL.replace(" ", "-");
            if (dynamicQDataModel != null) {
                ab_test = dynamicQDataModel.getABTest();
            }
            String qrContent = resultQuickLogin.getQuickMarkStringNoLogin(payeeType) + "&ab_test=" + ab_test + "&p2=" + p2 + "&wd=" + wd + "&hwver=" + tvhwver + "&isvipact=" + GetInterfaceTools.getIGalaVipManager().getPingBackVipAct();
            TVApi.tinyurl.call(new IApiCallback<ApiResultTinyurl>() {
                public void onSuccess(ApiResultTinyurl arg0) {
                    LogUtils.m1574i(VipRightsScanLoginFragment.LOG_TAG, "onSuccess --- TVApi.tinyurl.call：" + arg0.data.url);
                    C11993.this.doHasQRContent(resultQuickLogin, arg0.data.url);
                }

                public void onException(ApiException exception) {
                    LogUtils.m1571e(VipRightsScanLoginFragment.LOG_TAG, "onException --- TVApi.tinyurl.call");
                    LoginPingbackUtils.getInstance().errorPingback("315009", "login", "TVApi.tinyurl", exception);
                    C11993.this.doHasNoQRContent();
                }
            }, qrContent);
        }

        public void onException(ApiException exception) {
            LogUtils.m1571e(VipRightsScanLoginFragment.LOG_TAG, "onException --- PassportTVHelper.getTVLoginToken.call");
            LoginPingbackUtils.getInstance().errorPingback("315009", "login", "PassportTVHelper.getTVLoginToken", exception);
            doHasNoQRContent();
        }

        private void doHasNoQRContent() {
            if (VipRightsScanLoginFragment.this.getActivity() != null) {
                VipRightsScanLoginFragment.this.getActivity().runOnUiThread(new C11972());
            }
        }

        private void doHasQRContent(ApiResultQuickLogin resultQuickLogin, String qrContent) {
            VipRightsScanLoginFragment.this.mInvalidTime = resultQuickLogin.data.expire;
            VipRightsScanLoginFragment.this.mToken = resultQuickLogin.data.token;
            final Bitmap bitmap = QRUtils.createQRImage(qrContent, VipRightsScanLoginFragment.this.getDimen(C0508R.dimen.dimen_310dp), VipRightsScanLoginFragment.this.getDimen(C0508R.dimen.dimen_310dp));
            if (bitmap != null && VipRightsScanLoginFragment.this.getActivity() != null) {
                LogUtils.m1568d(VipRightsScanLoginFragment.LOG_TAG, ">>>>>二维码扫码成功展示pingback");
                if (VipRightsScanLoginFragment.this.qrShowFlag) {
                    VipRightsScanLoginFragment.this.qrShowFlag = false;
                    LoginPingbackUtils.getInstance().pageDisplay("account", "login_QR", true, VipRightsScanLoginFragment.this.mS1);
                }
                VipRightsScanLoginFragment.this.getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        VipRightsScanLoginFragment.this.mLoadingView.setVisibility(4);
                        VipRightsScanLoginFragment.this.mQRBgView.setBackgroundColor(VipRightsScanLoginFragment.this.getColor(C0508R.color.gala_write));
                        VipRightsScanLoginFragment.this.mQRImage.setVisibility(0);
                        VipRightsScanLoginFragment.this.mQRImage.setImageBitmap(bitmap);
                        VipRightsScanLoginFragment.this.mQRImage.postDelayed(VipRightsScanLoginFragment.this.mTimeRunnable, 1000);
                        if (VipRightsScanLoginFragment.this.mLoginBtn != null && VipRightsScanLoginFragment.this.mLoginBtn.getVisibility() == 0) {
                            VipRightsScanLoginFragment.this.mLoginBtn.setNextFocusUpId(C0508R.id.epg_login_qr_bg);
                        }
                    }
                });
            }
        }
    }

    class C12004 implements ILoginCallback {
        C12004() {
        }

        public void onLoginSuccess(UserInfoBean bean) {
            VipRightsScanLoginFragment.this.mIsCheckable = false;
            LoginPingbackUtils.getInstance().logSucc("login_QR", VipRightsScanLoginFragment.this.mS1);
            if (VipRightsScanLoginFragment.this.mTimeRunnable != null) {
                LogUtils.m1571e(VipRightsScanLoginFragment.LOG_TAG, "mQRImage.removeCallbacks(r) ----- PassportTVHelper.checkTVLogin.call --- onSuccess");
                VipRightsScanLoginFragment.this.mQRImage.removeCallbacks(VipRightsScanLoginFragment.this.mTimeRunnable);
            }
            VipRightsScanLoginFragment.this.mUserInfo = bean;
            VipRightsScanLoginFragment.this.gotoMyCenter(VipRightsScanLoginFragment.this.mUserInfo, VipRightsScanLoginFragment.this.mIntentFlag);
        }

        public void onLoginFail(ApiException exception) {
            LogUtils.m1571e(VipRightsScanLoginFragment.LOG_TAG, ">>>>> GetInterfaceTools.getIGalaAccountManager().loginByScan --- return onException");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.mIntentFlag = bundle.getInt(LoginConstant.LOGIN_SUCC_TO, -1);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_vip_rights_login, null);
        this.mLoadingView = this.mMainView.findViewById(C0508R.id.epg_view_loading);
        this.mQRBgView = this.mMainView.findViewById(C0508R.id.epg_login_qr_layout);
        this.mQRFailView = this.mMainView.findViewById(C0508R.id.epg_view_failure);
        this.mQRFocusView = this.mMainView.findViewById(C0508R.id.epg_login_qr_bg);
        this.mFocusView = this.mMainView.findViewById(C0508R.id.epg_login_qr_img);
        this.mQRTipText2 = (TextView) this.mMainView.findViewById(C0508R.id.epg_qr_tip_top);
        this.mQRTipText = (TextView) this.mMainView.findViewById(C0508R.id.epg_login_qr_tip);
        this.mQRImage = (ImageView) this.mMainView.findViewById(C0508R.id.epg_qr_bitmap);
        this.mLoginBtn = (Button) this.mMainView.findViewById(C0508R.id.epg_btn_comm_login);
        this.mQRTipText2.setText(Html.fromHtml(this.QR_TIP_TEXT));
        IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String qrTip = "";
        if (dynamicQDataModel != null) {
            qrTip = dynamicQDataModel.getLoginCode();
        }
        TextView textView = this.mQRTipText;
        if (StringUtils.isEmpty((CharSequence) qrTip)) {
            qrTip = getStr(C0508R.string.login_qr_tip);
        }
        textView.setText(qrTip);
        this.mIsCheckable = true;
        this.mQRFocusView.requestFocus();
        loadQRBitmap();
        this.mQRFailView.setOnClickListener(this);
        this.mLoginBtn.setOnClickListener(this);
        this.mLoginBtn.setOnFocusChangeListener(this);
        return this.mMainView;
    }

    public void onResume() {
        super.onResume();
        this.flag = true;
        this.qrShowFlag = true;
        if (!this.mIsThreadPost && this.mQRImage != null && this.mTimeRunnable != null) {
            LogUtils.m1574i(LOG_TAG, "mQRImage.post(r) ----- ");
            this.mIsThreadPost = true;
            this.mQRImage.post(this.mTimeRunnable);
        }
    }

    private void loadQRBitmap() {
        if (this.mTimeRunnable != null) {
            this.mQRImage.removeCallbacks(this.mTimeRunnable);
        }
        this.mQRBgView.setBackgroundColor(getColor(C0508R.color.transparent));
        this.mQRImage.setVisibility(4);
        this.mLoadingView.setVisibility(0);
        this.mQRFailView.setVisibility(8);
        ThreadUtils.execute(new C11941());
    }

    protected void callQRBitmap() {
        PassportTVHelper.getTVLoginToken.call(new C11993(), LoginConstant.LOGIN_QUICK_MARK);
    }

    protected void checkQRLoad() {
        if (this.mIsCheckable) {
            LogUtils.m1574i(LOG_TAG, "checkQRLoad() --------- ");
            GetInterfaceTools.getIGalaAccountManager().loginByScan(this.mToken, new C12004());
        }
    }

    public void onStop() {
        super.onStop();
        if (this.mQRImage != null && this.mTimeRunnable != null) {
            LogUtils.m1571e(LOG_TAG, "mQRImage.removeCallbacks(r) ----- onStop() ");
            this.mQRImage.removeCallbacks(this.mTimeRunnable);
            this.mIsThreadPost = false;
            this.mIsCheckable = true;
        }
    }

    public void onClick(View v) {
        int vId = v.getId();
        if (vId == C0508R.id.epg_view_failure) {
            if (this.mQRFailView.getVisibility() == 0) {
                loadQRBitmap();
            }
        } else if (vId == C0508R.id.epg_btn_comm_login && this.mLoginBtn.getVisibility() == 0 && this.mLoginEvent != null) {
            LoginPingbackUtils.getInstance().pageClick("login_QR", "tvlogin", "account", this.mS1);
            this.mLoginEvent.onSwitchFragment(new CommLoginFragment(), getArguments());
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == C0508R.id.epg_btn_comm_login) {
            this.mFocusView.setVisibility(hasFocus ? 4 : 0);
            AnimationUtil.zoomAnimation(v, hasFocus, 1.2f, 200);
        }
    }
}
