package com.gala.video.app.epg.ui.ucenter.account.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.tvapi.data.TVApiHelper;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.tv2.result.ApiResultTinyurl;
import com.gala.tvapi.type.UserType;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.ucenter.account.ui.IUCenterView;
import com.gala.video.app.epg.widget.dialog.BitmapAlbum;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ImageSize;
import com.gala.video.lib.share.ifimpl.ucenter.account.helper.AccountInfoTipHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.AccountLoginHelper;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.utils.QRUtils;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UCenterPresenter {
    private final String LOG_TAG = "EPG/MyAccount/UCenterPresenter";
    private boolean flag;
    private List<BitmapAlbum> mBitmapAlbumList;
    private Context mContext;
    private Handler mHandler;
    private IImageProvider mImageProvider = ImageProviderApi.getImageProvider();
    private boolean mLoadingFlag;
    private IUCenterView mMyView;
    private String mPingbackE;
    private String mPingbackS1;
    private Runnable r = new Runnable() {
        public void run() {
            if (UCenterPresenter.this.mLoadingFlag) {
                UCenterPresenter.this.mMyView.showLoadingUI();
            }
        }
    };

    static class ApiCallback implements IApiCallback<ApiResultAlbumList> {
        WeakReference<UCenterPresenter> mOuter;

        public ApiCallback(UCenterPresenter outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ApiResultAlbumList result) {
            UCenterPresenter outer = (UCenterPresenter) this.mOuter.get();
            if (outer != null) {
                Album album;
                LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> TVApiHelper.get().vipChannelData --- succ");
                List<Album> albums = new ArrayList();
                for (Album album2 : result.data) {
                    if (!StringUtils.isEmpty(album2.pic)) {
                        albums.add(album2);
                        if (albums.size() == 3) {
                            break;
                        }
                    }
                }
                outer.mBitmapAlbumList = new ArrayList();
                for (int i = 0; i < albums.size(); i++) {
                    album2 = (Album) albums.get(i);
                    CharSequence url = album2.pic;
                    LogUtils.d("TEST", ">>>>> name,url ---", album2.name, ", ", album2.pic);
                    if (!StringUtils.isEmpty(url)) {
                        int index = url.lastIndexOf(".");
                        String pre = url.substring(0, index);
                        ImageRequest request = new ImageRequest(pre + ImageSize.IMAGE_SIZE_260_360 + url.substring(index, url.length()), album2);
                        request.setDecodeConfig(Config.ARGB_4444);
                        outer.mImageProvider.loadImage(request, new ImageCallback(outer));
                    }
                }
            }
        }

        public void onException(ApiException e) {
            if (((UCenterPresenter) this.mOuter.get()) != null) {
                LogUtils.e("EPG/MyAccount/UCenterPresenter", "Exception --- TVApiHelper.get().vipChannelData.call");
            }
        }
    }

    static class ApiTinyurlCallback implements IApiCallback<ApiResultTinyurl> {
        WeakReference<UCenterPresenter> mOuter;

        public ApiTinyurlCallback(UCenterPresenter outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ApiResultTinyurl result) {
            final UCenterPresenter outer = (UCenterPresenter) this.mOuter.get();
            if (outer != null) {
                final Bitmap qrBitmap = QRUtils.createQRImage(result.data.url, ResourceUtil.getDimen(R.dimen.dimen_404dp), ResourceUtil.getDimen(R.dimen.dimen_404dp));
                outer.mHandler.post(new Runnable() {
                    public void run() {
                        outer.mMyView.showQRImageUI(qrBitmap);
                    }
                });
            }
        }

        public void onException(ApiException exception) {
            final UCenterPresenter outer = (UCenterPresenter) this.mOuter.get();
            if (outer != null) {
                LogUtils.e("EPG/MyAccount/UCenterPresenter", "onException --- TVApi.tinyurl.call");
                LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "TVApi.tinyurl", exception);
                outer.mHandler.post(new Runnable() {
                    public void run() {
                        outer.mMyView.showQRFailUI();
                    }
                });
            }
        }
    }

    static class ImageCallback implements IImageCallback {
        WeakReference<UCenterPresenter> mOuter;

        public ImageCallback(UCenterPresenter outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            UCenterPresenter outer = (UCenterPresenter) this.mOuter.get();
            if (outer != null) {
                LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> mImageProvider.loadImage --- succ");
                BitmapAlbum bitmapAlbum = new BitmapAlbum();
                bitmapAlbum.setBitmap(bitmap);
                bitmapAlbum.setAlbum((Album) imageRequest.getCookie());
                LogUtils.d("TEST2", ">>>>> name, url----", bitmapAlbum.getAlbum().name, ", ", imageRequest.getUrl());
                outer.mBitmapAlbumList.add(bitmapAlbum);
            }
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            if (((UCenterPresenter) this.mOuter.get()) != null) {
                LogUtils.e("EPG/MyAccount/UCenterPresenter", "onFailure --- mImageProvider.loadImage ---", imageRequest.getUrl());
            }
        }
    }

    static class WorkRunnable implements Runnable {
        private static final int USER_INFO = 2;
        private static final int VIP_CHANNEL = 1;
        WeakReference<UCenterPresenter> mOuter;
        private WorkInfo mWorkInfo;

        static class WorkInfo {
            boolean isShowPingback;
            int type;

            public WorkInfo(int type, boolean isShowPingback) {
                this.type = type;
                this.isShowPingback = isShowPingback;
            }
        }

        public WorkRunnable(UCenterPresenter outer, int type) {
            this.mOuter = new WeakReference(outer);
            this.mWorkInfo = new WorkInfo(type, false);
        }

        public WorkRunnable(UCenterPresenter outer, WorkInfo info) {
            this.mOuter = new WeakReference(outer);
            this.mWorkInfo = info;
        }

        public void run() {
            UCenterPresenter outer = (UCenterPresenter) this.mOuter.get();
            if (outer != null || this.mWorkInfo == null) {
                switch (this.mWorkInfo.type) {
                    case 1:
                        loadVipChannel(outer);
                        return;
                    case 2:
                        checkUserInfo(outer);
                        return;
                    default:
                        return;
                }
            }
        }

        private void checkUserInfo(final UCenterPresenter outer) {
            UserResponseBean respBean = GetInterfaceTools.getIGalaAccountManager().updateUserInfo();
            if (respBean == null || respBean.getRespResult()) {
                outer.mMyView.refreshUserInfoUI();
                if (this.mWorkInfo.isShowPingback) {
                    LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> show pingback in 'My-Page'");
                    String s1 = outer.mPingbackS1;
                    String tabsrc = PingBackUtils.getTabSrc();
                    PingBackParams params = new PingBackParams();
                    params.add("bstp", "1").add("s1", s1).add("tabsrc", tabsrc).add("qtcurl", "mine_loggedin").add("block", "mine").add("e", outer.mPingbackE).add(Keys.T, "21");
                    PingBack.getInstance().postPingBackToLongYuan(params.build());
                    return;
                }
                return;
            }
            ApiException exception = respBean.getException();
            if (exception == null) {
                LogUtils.e("test", ">>>>> checkUserInfo - exception == null");
            } else if ("-50".equals(exception.getHttpCode())) {
                LogUtils.e("test", ">>>>> checkUserInfo - exception - NET_ERROR_CODE");
            } else {
                CharSequence errorCode = exception.getCode();
                LogUtils.d("EPG/MyAccount/UCenterPresenter", "PassportTVHelper.userInfo.call exception code is : " + errorCode);
                if ("-100".equals(errorCode)) {
                    LogUtils.e("test", ">>>>> checkUserInfo - exception - JSON_ERROR_CODE");
                } else if (StringUtils.isEmpty(errorCode)) {
                    LogUtils.e("test", ">>>>> checkUserInfo - exception - errorCode is empty");
                } else {
                    CharSequence warning = AccountInfoTipHelper.getAccountWarningStr(errorCode);
                    if (StringUtils.isEmpty(warning)) {
                        LogUtils.e("test", ">>>>> checkUserInfo - exception - warning tip is empty");
                        LogUtils.e("EPG/MyAccount/UCenterPresenter", ">>>>> warning tip is empty");
                        return;
                    }
                    final CharSequence tip = warning;
                    outer.mHandler.post(new Runnable() {
                        public void run() {
                            outer.mMyView.showExceptionUI(tip);
                        }
                    });
                }
            }
        }

        private void loadVipChannel(UCenterPresenter outer) {
            IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            if (dynamicQDataModel != null) {
                TVApiHelper.get().vipChannelData(new ApiCallback(outer), dynamicQDataModel.getVipResourceId(), 1, 10);
                return;
            }
            LogUtils.e("EPG/MyAccount/UCenterPresenter", ">>>>> loadAlbumPicture error, dynamicQDataModel is null");
        }
    }

    public UCenterPresenter(IUCenterView myView) {
        this.mMyView = myView;
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mHandler = new Handler(Looper.getMainLooper());
    }

    public void initHeadView() {
    }

    public void clearUserInfo() {
        if (!StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
            GetInterfaceTools.getIGalaAccountManager().logOut(this.mContext, "", LoginConstant.LGTTYPE_EXCEPTION);
        }
        LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>>showAccountExceptionDialog --- OnClickListener");
    }

    public boolean confirmLogout() {
        this.flag = true;
        sendClickPingBack("logout");
        return GetInterfaceTools.getIGalaAccountManager().logOut(this.mContext, this.mPingbackS1, LoginConstant.LGTTYPE_ACTIVE);
    }

    public void cancelLogout() {
        this.flag = true;
        sendClickPingBack(LoginConstant.CLICK_RESEAT_LOGOUT_CANCEL);
    }

    public void logoutDialogDismiss() {
        if (!this.flag) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> GlobalVipDialog --- OnDismissListener");
            sendClickPingBack("back");
        }
        this.flag = false;
    }

    private void sendClickPingBack(String rseat) {
        Log.v("EPG/MyAccount/UCenterPresenter", "sendClickPingBack rseat = " + rseat);
        PingBackParams params = new PingBackParams();
        params.add("rpage", "logout").add("block", "rec").add("rseat", rseat).add(Keys.T, "20").add("c1", "").add("r", "").add("s1", this.mPingbackS1).add("e", this.mPingbackE).add("rt", "i");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void clickLogout() {
        this.mMyView.showLogoutUI();
        LoginPingbackUtils.getInstance().pageDisplay("logout", "rec", false, this.mPingbackS1);
    }

    public void startSecurityCenter() {
        LoginPingbackUtils.getInstance().pageClick("usercenter", LoginConstant.CLICK_RESEAT_CHANGE_PASSWORD, "account", this.mPingbackS1);
        this.mMyView.showSecurityUI();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                UCenterPresenter.this.callSecurityQRBitmap();
            }
        });
    }

    public void hideSecurity() {
        this.mMyView.hideSecurityUI();
        checkUserInfo(false);
    }

    public void onDestory() {
        if (this.r != null) {
            this.r = null;
        }
    }

    private void callSecurityQRBitmap() {
        String qrContent = getChangePassUrl(GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), GetInterfaceTools.getIGalaAccountManager().getUserAccount());
        if (LogUtils.mIsDebug) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>>change password url:", qrContent);
        }
        TVApi.tinyurl.call(new ApiTinyurlCallback(this), qrContent);
    }

    public void checkUserInfo(boolean isShowPingback) {
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext)) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> checkUserInfo - user is login, check userinfo");
            ThreadUtils.execute(new WorkRunnable(this, new WorkInfo(2, isShowPingback)));
            return;
        }
        LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> checkUserInfo - user is no login");
        if (isShowPingback) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> show pingback in 'My-Page'");
            String s1 = this.mPingbackS1;
            String tabsrc = PingBackUtils.getTabSrc();
            PingBackParams params = new PingBackParams();
            params.add("bstp", "1").add("s1", s1).add("tabsrc", tabsrc).add("qtcurl", "mine_guest").add("block", "mine").add("e", this.mPingbackE).add(Keys.T, "21");
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    private String getUserName() {
        CharSequence phone = GetInterfaceTools.getIGalaAccountManager().getUserPhone();
        if (StringUtils.isEmpty(phone)) {
            return LoginConstant.NICKNAME_PRE + GetInterfaceTools.getIGalaAccountManager().getUserName();
        }
        return LoginConstant.NICKNAME_PRE + AccountLoginHelper.getUserPhone(phone);
    }

    @SuppressLint({"SimpleDateFormat"})
    private String getStatus() {
        UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
        if (userType == null) {
            return ResourceUtil.getStr(R.string.str_my_novip);
        }
        if (userType.isLitchi()) {
            return getVipDeadLine();
        }
        if (userType.isPlatinum()) {
            if (userType.isExpire()) {
                return ResourceUtil.getStr(R.string.vip_expire_tip);
            }
            return getVipDeadLine();
        } else if (userType.isLitchiOverdue()) {
            return ResourceUtil.getStr(R.string.my_overdue_vip);
        } else {
            return ResourceUtil.getStr(R.string.str_my_novip);
        }
    }

    private String getVipDeadLine() {
        long timeStamp = GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp();
        int vipOverdueDay = getVipOverDay(timeStamp);
        if (vipOverdueDay > 0 && vipOverdueDay < 10) {
            return vipOverdueDay + "天后过期";
        }
        Date date = new Date(timeStamp);
        return new SimpleDateFormat("yyyy年MM月dd日").format(date) + "过期";
    }

    private String getChangePassUrl(String cookie, String accountName) {
        IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String url = dynamicQDataModel.getModifyPwdQRCode();
        String ab_test = dynamicQDataModel.getABTest();
        return url + "?cok=" + cookie + "&u=" + accountName + "&agenttype=28&Code_type=1&device_id=" + TVApiBase.getTVApiProperty().getPassportDeviceId() + "&extra=" + getExtraJsonString() + "&ab_test=" + ab_test + "&hwver=" + Build.MODEL.replace(" ", "-");
    }

    private String getExtraJsonString() {
        StringBuilder s = new StringBuilder();
        s.append("ui:" + TVApiBase.getTVApiProperty().getUUID() + ";").append("ak:" + TVApiBase.getTVApiProperty().getApiKey() + ";").append("ai:" + TVApiBase.getTVApiProperty().getAuthId() + ";").append("av:" + TVApiBase.getTVApiProperty().getVersion() + ";").append("cv:" + Build.MODEL.toString().replaceAll(" ", ""));
        return s.toString();
    }

    public void loadAlbumPicture() {
        if (!GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext)) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> loadAlbumPicture return, no login");
        } else if (ListUtils.isEmpty(this.mBitmapAlbumList) || this.mBitmapAlbumList.size() != 3) {
            ThreadUtils.execute(new WorkRunnable(this, 1));
        } else {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>> loadAlbumPicture return, bitmapList has values already");
        }
    }

    public List<BitmapAlbum> getAlbumPics() {
        if (ListUtils.getCount(this.mBitmapAlbumList) != 3) {
            return null;
        }
        return this.mBitmapAlbumList;
    }

    public void setPingbackS1(String s1) {
        this.mPingbackS1 = s1;
    }

    public void setPingbackE(String e) {
        this.mPingbackE = e;
    }

    private int getVipOverDay(long timeStamp) {
        if (timeStamp <= 0) {
            return -1;
        }
        return (int) Math.ceil((double) (((float) (timeStamp - DeviceUtils.getServerTimeMillis())) / 8.64E7f));
    }

    public void showLoading() {
        this.mLoadingFlag = true;
        this.mHandler.postDelayed(this.r, 1500);
    }

    public void stopLoading() {
        this.mLoadingFlag = false;
        if (this.r != null) {
            this.mHandler.removeCallbacks(this.r);
        }
        this.mMyView.hideLoadingUI();
    }

    public void onPause() {
        if (this.r != null) {
            LogUtils.d("EPG/MyAccount/UCenterPresenter", ">>>>>UCenterPresenter.onPause");
            this.mHandler.removeCallbacks(this.r);
            this.mMyView.hideLoadingUI();
        }
    }
}
