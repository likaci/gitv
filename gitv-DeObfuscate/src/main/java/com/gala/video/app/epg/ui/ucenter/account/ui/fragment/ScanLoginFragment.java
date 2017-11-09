package com.gala.video.app.epg.ui.ucenter.account.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.download.DownloaderAPI;
import com.gala.download.base.FileRequest;
import com.gala.download.base.IFileCallback;
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
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.utils.QRUtils;
import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class ScanLoginFragment extends BaseLoginFragment implements OnClickListener, OnFocusChangeListener {
    private static final String LOG_TAG = "EPG/login/LoginFragment";
    private Runnable mCheckQRLoadRunnable = new CheckQRLoadRunnable(this);
    private CountDownTimer mCountDownTimer;
    private View mFocusView;
    private Runnable mGifAnimalRunnable = new C11917();
    private GifDrawable mGifDrawable;
    private GifImageView mGuideImageView;
    private int mIntentFlag;
    private View mLoadingView;
    private Button mLoginBtn;
    private Bitmap mLoginGuideBitmap;
    private View mMainView;
    private View mQRBgView;
    private View mQRFailView;
    private View mQRFocusView;
    private ImageView mQRImage;
    private TextView mQRTipText;
    private TextView mQRTipText2;
    private String mToken;
    private UserInfoBean mUserInfo;

    class C11831 implements Runnable {
        C11831() {
        }

        public void run() {
            ScanLoginFragment.this.loadDynamicQData();
        }
    }

    class C11863 implements Runnable {

        class C11851 implements Runnable {
            C11851() {
            }

            public void run() {
                ScanLoginFragment.this.mGuideImageView.setImageBitmap(ScanLoginFragment.this.mLoginGuideBitmap);
            }
        }

        C11863() {
        }

        public void run() {
            Options options = new Options();
            options.inPurgeable = true;
            options.inPreferredConfig = Config.RGB_565;
            ScanLoginFragment.this.mLoginGuideBitmap = BitmapFactory.decodeResource(ScanLoginFragment.this.getResources(), C0508R.drawable.epg_login_coupon_bg, options);
            ScanLoginFragment.this.mHandler.post(new C11851());
        }
    }

    class C11874 implements Runnable {
        C11874() {
        }

        public void run() {
            ScanLoginFragment.this.callQRBitmap();
        }
    }

    class C11885 implements Runnable {
        C11885() {
        }

        public void run() {
            LogUtils.m1571e(ScanLoginFragment.LOG_TAG, "doHasNoQRContent");
            ScanLoginFragment.this.mLoadingView.setVisibility(4);
            ScanLoginFragment.this.mQRBgView.setBackgroundColor(ScanLoginFragment.this.getColor(C0508R.color.transparent));
            ScanLoginFragment.this.mQRImage.setVisibility(4);
            ScanLoginFragment.this.mQRFailView.setVisibility(0);
            ScanLoginFragment.this.mQRFailView.requestFocus();
            if (ScanLoginFragment.this.mLoginBtn != null && ScanLoginFragment.this.mLoginBtn.getVisibility() == 0) {
                ScanLoginFragment.this.mLoginBtn.setNextFocusUpId(C0508R.id.epg_view_failure);
            }
        }
    }

    class C11917 implements Runnable {
        C11917() {
        }

        public void run() {
            if (ScanLoginFragment.this.mGifDrawable != null && ScanLoginFragment.this.mHandler != null) {
                LogUtils.m1568d(ScanLoginFragment.LOG_TAG, ">>>>> gif animation run");
                ScanLoginFragment.this.mGifDrawable.reset();
                ScanLoginFragment.this.mHandler.postDelayed(this, 10000);
            }
        }
    }

    static class ApiCallback implements IApiCallback<ApiResultTinyurl> {
        private WeakReference<ScanLoginFragment> mOuter;
        private ApiResultQuickLogin mResultQuickLogin;

        public ApiCallback(ScanLoginFragment outer, ApiResultQuickLogin resultQuickLogin) {
            this.mOuter = new WeakReference(outer);
            this.mResultQuickLogin = resultQuickLogin;
        }

        public void onSuccess(ApiResultTinyurl apiResultTinyurl) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1574i(ScanLoginFragment.LOG_TAG, "onSuccess --- TVApi.tinyurl.call：" + apiResultTinyurl.data.url);
                outer.doHasQRContent(this.mResultQuickLogin, apiResultTinyurl.data.url);
            }
        }

        public void onException(ApiException e) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1571e(ScanLoginFragment.LOG_TAG, "onException --- TVApi.tinyurl.call");
                LoginPingbackUtils.getInstance().errorPingback("315009", "login", "TVApi.tinyurl", e);
                outer.doHasNoQRContent();
            }
        }
    }

    static class CheckQRLoadRunnable implements Runnable {
        WeakReference<ScanLoginFragment> mOuter;

        public CheckQRLoadRunnable(ScanLoginFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void run() {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1574i(ScanLoginFragment.LOG_TAG, "checkQRLoad() --------- ");
                GetInterfaceTools.getIGalaAccountManager().loginByScan(outer.mToken, new LoginCallback(outer));
            }
        }
    }

    static class FileCallback implements IFileCallback {
        private WeakReference<ScanLoginFragment> mOuter;

        class C11921 implements Runnable {
            C11921() {
            }

            public void run() {
            }
        }

        public FileCallback(ScanLoginFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(FileRequest fileRequest, String path) {
            final ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null && !StringUtils.isEmpty((CharSequence) path) && path.length() >= 4) {
                if (path.substring(path.length() - 4, path.length()).equals(UIKitConfig.KEY_GIF)) {
                    try {
                        outer.mGifDrawable = new GifDrawable(new File(path));
                        outer.mGifDrawable.setLoopCount(1);
                        outer.mGuideImageView.setImageDrawable(outer.mGifDrawable);
                        outer.mHandler.post(outer.mGifAnimalRunnable);
                        outer.mHandler.post(new C11921());
                        return;
                    } catch (IOException e) {
                        LogUtils.m1571e(ScanLoginFragment.LOG_TAG, ">>>>> Login guide gifdrawable exception");
                        return;
                    }
                }
                Options options = new Options();
                options.inPurgeable = true;
                options.inPreferredConfig = Config.RGB_565;
                outer.mLoginGuideBitmap = BitmapFactory.decodeFile(path, options);
                outer.mHandler.post(new Runnable() {
                    public void run() {
                        outer.mGuideImageView.setImageBitmap(outer.mLoginGuideBitmap);
                    }
                });
            }
        }

        public void onFailure(FileRequest fileRequest, Exception e) {
            if (((ScanLoginFragment) this.mOuter.get()) != null) {
                LogUtils.m1573e(ScanLoginFragment.LOG_TAG, ">>>>> load guide pic fail, url - ", fileRequest.getUrl());
            }
        }
    }

    static class LoginCallback implements ILoginCallback {
        WeakReference<ScanLoginFragment> mOuter;

        public LoginCallback(ScanLoginFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onLoginSuccess(UserInfoBean bean) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LoginPingbackUtils.getInstance().logSucc("login_QR", outer.mS1);
                if (outer.mCountDownTimer != null) {
                    LogUtils.m1571e(ScanLoginFragment.LOG_TAG, "mCountDownTimer.cancel() ----- PassportTVHelper.checkTVLogin.call --- onSuccess");
                    outer.mCountDownTimer.cancel();
                }
                if (outer.mCheckQRLoadRunnable != null) {
                    outer.mQRImage.removeCallbacks(outer.mCheckQRLoadRunnable);
                }
                outer.mUserInfo = bean;
                LogUtils.m1571e(ScanLoginFragment.LOG_TAG, "gotoMyCenter");
                outer.gotoMyCenter(outer.mUserInfo, outer.mIntentFlag);
            }
        }

        public void onLoginFail(ApiException exception) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1571e(ScanLoginFragment.LOG_TAG, ">>>>> GetInterfaceTools.getIGalaAccountManager().loginByScan --- return onException");
                outer.mQRImage.postDelayed(outer.mCheckQRLoadRunnable, 2000);
            }
        }
    }

    static class VrsCallback implements IVrsCallback<ApiResultQuickLogin> {
        private WeakReference<ScanLoginFragment> mOuter;

        public VrsCallback(ScanLoginFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ApiResultQuickLogin resultQuickLogin) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1574i(ScanLoginFragment.LOG_TAG, "onSuccess --- PassportTVHelper.getTVLoginToken.call");
                IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
                String payeeType = "";
                String ab_test = "";
                String p2 = Project.getInstance().getBuild().getPingbackP2();
                String wd = Project.getInstance().getBuild().isSupportSmallWindowPlay() ? "0" : "1";
                String tvhwver = Build.MODEL.replace(" ", "-");
                if (dynamicQDataModel != null) {
                    ab_test = dynamicQDataModel.getABTest();
                }
                String qrContent = resultQuickLogin.getQuickMarkStringNoLogin(payeeType) + "&ab_test=" + ab_test + "&p2=" + p2 + "&wd=" + wd + "&hwver=" + tvhwver + "&isvipact=" + GetInterfaceTools.getIGalaVipManager().getPingBackVipAct() + "&s1=" + LoginConstant.S1_H5 + "&tvs1=" + outer.mS1;
                TVApi.tinyurl.call(new ApiCallback(outer, resultQuickLogin), qrContent);
            }
        }

        public void onException(ApiException e) {
            ScanLoginFragment outer = (ScanLoginFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.m1571e(ScanLoginFragment.LOG_TAG, "onException --- PassportTVHelper.getTVLoginToken.call");
                LoginPingbackUtils.getInstance().errorPingback("315009", "login", "PassportTVHelper.getTVLoginToken", e);
                outer.doHasNoQRContent();
            }
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
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_login, null);
        this.mLoadingView = this.mMainView.findViewById(C0508R.id.epg_view_loading);
        this.mQRBgView = this.mMainView.findViewById(C0508R.id.epg_login_qr_layout);
        this.mQRFailView = this.mMainView.findViewById(C0508R.id.epg_view_failure);
        this.mQRFocusView = this.mMainView.findViewById(C0508R.id.epg_login_qr_bg);
        this.mFocusView = this.mMainView.findViewById(C0508R.id.epg_login_qr_img);
        this.mQRTipText2 = (TextView) this.mMainView.findViewById(C0508R.id.epg_qr_tip_top);
        this.mQRTipText = (TextView) this.mMainView.findViewById(C0508R.id.epg_login_qr_tip);
        this.mQRImage = (ImageView) this.mMainView.findViewById(C0508R.id.epg_qr_bitmap);
        this.mLoginBtn = (Button) this.mMainView.findViewById(C0508R.id.epg_btn_comm_login);
        this.mGuideImageView = (GifImageView) this.mMainView.findViewById(C0508R.id.epg_login_guide_bg);
        this.mQRTipText2.setText(Html.fromHtml(LoginConstant.QR_TIP_TEXT));
        if (Project.getInstance().getBuild().isOpenKeyboardLogin()) {
            this.mLoginBtn.setVisibility(0);
        }
        this.mQRFocusView.requestFocus();
        this.mQRFailView.setOnClickListener(this);
        this.mLoginBtn.setOnClickListener(this);
        this.mLoginBtn.setOnFocusChangeListener(this);
        ThreadUtils.execute(new C11831());
        return this.mMainView;
    }

    private void loadDynamicQData() {
        String qrTip = "";
        CharSequence picUrl = "";
        IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicQDataModel != null) {
            qrTip = dynamicQDataModel.getLoginCode();
            picUrl = dynamicQDataModel.getLoginPageLeftPicUrl();
        }
        final String finalQrTip = qrTip;
        this.mHandler.post(new Runnable() {
            public void run() {
                ScanLoginFragment.this.mQRTipText.setText(StringUtils.isEmpty(finalQrTip) ? ScanLoginFragment.this.getStr(C0508R.string.login_qr_tip) : finalQrTip);
            }
        });
        if (this.mIntentFlag == 6) {
            this.mHandler.post(new C11863());
        } else if (!StringUtils.isEmpty(picUrl)) {
            DownloaderAPI.getDownloader().loadFile(new FileRequest(picUrl), new FileCallback(this));
        }
    }

    public void onResume() {
        super.onResume();
        loadQRBitmap();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onStop();
        } else {
            onResume();
        }
    }

    private void loadQRBitmap() {
        if (this.mCountDownTimer != null) {
            this.mCountDownTimer.cancel();
        }
        if (this.mCheckQRLoadRunnable != null) {
            this.mQRImage.removeCallbacks(this.mCheckQRLoadRunnable);
        }
        this.mQRBgView.setBackgroundColor(getColor(C0508R.color.transparent));
        this.mQRImage.setVisibility(4);
        this.mLoadingView.setVisibility(0);
        this.mQRFailView.setVisibility(8);
        ThreadUtils.execute(new C11874());
    }

    protected void callQRBitmap() {
        PassportTVHelper.getTVLoginToken.call(new VrsCallback(this), LoginConstant.LOGIN_QUICK_MARK);
    }

    private void doHasNoQRContent() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new C11885());
        }
    }

    private void doHasQRContent(ApiResultQuickLogin resultQuickLogin, String qrContent) {
        final int expireTime = resultQuickLogin.data.expire;
        this.mToken = resultQuickLogin.data.token;
        final Bitmap bitmap = QRUtils.createQRImage(qrContent, getDimen(C0508R.dimen.dimen_310dp), getDimen(C0508R.dimen.dimen_310dp));
        if (bitmap != null && getActivity() != null) {
            LogUtils.m1568d(LOG_TAG, ">>>>>二维码扫码成功展示pingback");
            LoginPingbackUtils.getInstance().pageDisplay("account", "login_QR", true, this.mS1);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    ScanLoginFragment.this.mLoadingView.setVisibility(4);
                    ScanLoginFragment.this.mQRBgView.setBackgroundColor(ScanLoginFragment.this.getColor(C0508R.color.gala_write));
                    ScanLoginFragment.this.mQRImage.setVisibility(0);
                    ScanLoginFragment.this.mQRImage.setImageBitmap(bitmap);
                    ScanLoginFragment.this.mCountDownTimer = new CountDownTimer((long) (expireTime * 1000), 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            LogUtils.m1568d(ScanLoginFragment.LOG_TAG, "onFinish -- 倒计时结束 , expireTime = " + expireTime);
                            ScanLoginFragment.this.loadQRBitmap();
                        }
                    };
                    if (!(ScanLoginFragment.this.mQRImage == null || ScanLoginFragment.this.mCountDownTimer == null)) {
                        LogUtils.m1574i(ScanLoginFragment.LOG_TAG, "mCountDownTimer.start() ----- ");
                        ScanLoginFragment.this.mCountDownTimer.start();
                    }
                    ScanLoginFragment.this.mQRImage.post(ScanLoginFragment.this.mCheckQRLoadRunnable);
                    if (ScanLoginFragment.this.mLoginBtn != null && ScanLoginFragment.this.mLoginBtn.getVisibility() == 0) {
                        ScanLoginFragment.this.mLoginBtn.setNextFocusUpId(C0508R.id.epg_login_qr_bg);
                    }
                }
            });
        }
    }

    public void onStop() {
        super.onStop();
        if (!(this.mQRImage == null || this.mCountDownTimer == null)) {
            LogUtils.m1571e(LOG_TAG, "mCountDownTimer.cancel() ----- onStop() ");
            this.mCountDownTimer.cancel();
        }
        if (this.mCheckQRLoadRunnable != null) {
            LogUtils.m1571e(LOG_TAG, "removeCallbacks(mCheckQRLoadRunnable) ----- onStop() ");
            this.mQRImage.removeCallbacks(this.mCheckQRLoadRunnable);
        }
        if (this.mGifAnimalRunnable != null && this.mHandler != null) {
            LogUtils.m1571e(LOG_TAG, "removeCallbacks(mGifAnimalRunnable) ----- onStop() ");
            this.mHandler.removeCallbacks(this.mGifAnimalRunnable);
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
            CommLoginFragment fragment = new CommLoginFragment();
            fragment.setArguments(getArguments());
            this.mLoginEvent.onSwitchFragment(fragment, getArguments());
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v.getId() == C0508R.id.epg_btn_comm_login) {
            this.mFocusView.setVisibility(hasFocus ? 4 : 0);
            AnimationUtil.zoomAnimation(v, hasFocus, 1.07f, 200);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginGuideBitmap != null) {
            this.mLoginGuideBitmap = null;
        }
        if (this.mCheckQRLoadRunnable != null) {
            this.mCheckQRLoadRunnable = null;
        }
        if (this.mGifAnimalRunnable != null) {
            this.mGifAnimalRunnable = null;
        }
    }
}
