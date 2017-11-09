package com.gala.video.lib.share.ifimpl.ucenter.account.helper;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.UserHelper;
import com.gala.tvapi.vrs.result.ApiResultData;
import com.gala.tvapi.vrs.result.ApiResultKeepaliveInterval;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.account.bean.UserResponseBean;
import java.util.List;

public class AccountInfoTipHelper {
    private static final String TAG = "home/HomeTipInfoHelper";
    private static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void checkUserInfo(final Activity context) {
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    UserResponseBean respBean = GetInterfaceTools.getIGalaAccountManager().updateUserInfo();
                    if (respBean != null) {
                        if (respBean.getRespResult()) {
                            LogUtils.d(AccountInfoTipHelper.TAG, "check user info is success");
                            UserHelper.checkVipAccount.callSync(new IVrsCallback<ApiResultKeepaliveInterval>() {
                                public void onSuccess(ApiResultKeepaliveInterval result) {
                                    LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>UserHelper.checkVipAccount.callSync---onSuccess");
                                    AccountAdsHelper.handelCheckVipAccount(result);
                                }

                                public void onException(ApiException exception) {
                                    LogUtils.e(AccountInfoTipHelper.TAG, ">>>>>UserHelper.checkVipAccount.callSync---onException, code:", exception.getCode());
                                    LoginPingbackUtils.getInstance().errorPingback("315008", exception != null ? exception.getCode() : "", "UserHelper.checkVipAccount", exception);
                                    AccountAdsHelper.handelCheckVipAccountException(exception);
                                }
                            }, respBean.getCookie());
                            AccountInfoTipHelper.renewAuthCookie();
                            return;
                        }
                        AccountInfoTipHelper.handleLogOut(respBean.getException(), context);
                    }
                }
            });
        }
    }

    private static void handleLogOut(ApiException exception, Activity activity) {
        if (exception != null && !"-50".equals(exception.getHttpCode())) {
            CharSequence errorCode = exception.getCode();
            LogUtils.d(TAG, "PassportTVHelper.userInfo.call exception code is : " + errorCode);
            if (!"-100".equals(errorCode) && !StringUtils.isEmpty(errorCode)) {
                CharSequence warning = getAccountWarningStr(errorCode);
                if (StringUtils.isEmpty(warning) || activity == null) {
                    LogUtils.e(TAG, ">>>>> warning tip is empty");
                } else {
                    showAccountExceptionDialog(activity, warning);
                }
            }
        }
    }

    public static String getAccountWarningStr(String errorCode) {
        String warning = "";
        if (LoginConstant.ACCOUNT_ECODE_A00001.equals(errorCode)) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.account_error_a00001);
        }
        if ("A00005".equals(errorCode)) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.account_error_a00005);
        }
        if (LoginConstant.ACCOUNT_ECODE_A00055.equals(errorCode)) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.account_error_a00055);
        }
        if (LoginConstant.ACCOUNT_ECODE_A00056.equals(errorCode)) {
            return AppRuntimeEnv.get().getApplicationContext().getResources().getString(R.string.account_error_a00056);
        }
        return warning;
    }

    private static void showAccountExceptionDialog(final Activity activity, final String tip) {
        mHandler.post(new Runnable() {
            public void run() {
                if (!CreateInterfaceTools.createUpdateManager().isShowingDialog()) {
                    final Context context = AppRuntimeEnv.get().getApplicationContext();
                    final GlobalDialog confirmDialog = new GlobalDialog(activity);
                    OnClickListener okListener = new OnClickListener() {
                        public void onClick(View view) {
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountExceptionDialog --- OnClickListener -- ok");
                            confirmDialog.dismiss();
                            if (!StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
                                GetInterfaceTools.getIGalaAccountManager().logOut(context, "", LoginConstant.LGTTYPE_EXCEPTION);
                            }
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountExceptionDialog --- OnClickListener -- goto login page");
                            GetInterfaceTools.getLoginProvider().startLoginActivity(activity, "", 2);
                        }
                    };
                    OnClickListener cancelListener = new OnClickListener() {
                        public void onClick(View view) {
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountExceptionDialog --- OnClickListener -- cancel");
                            confirmDialog.dismiss();
                        }
                    };
                    confirmDialog.setOnDismissListener(new OnDismissListener() {
                        public void onDismiss(DialogInterface dialog) {
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountExceptionDialog --- OnDismissListener()");
                            if (!StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
                                GetInterfaceTools.getIGalaAccountManager().logOut(context, "", LoginConstant.LGTTYPE_EXCEPTION);
                            }
                        }
                    });
                    confirmDialog.setParams(tip, activity.getResources().getString(R.string.arefresh_login_ok), okListener, activity.getResources().getString(R.string.arefresh_login_cancel), cancelListener);
                    confirmDialog.show();
                }
            }
        });
    }

    public static void showAccountConflictDialog(final Activity activity, boolean isShowUI) {
        if (isShowUI) {
            mHandler.post(new Runnable() {
                public void run() {
                    final GlobalDialog confirmDialog = new GlobalDialog(activity);
                    confirmDialog.setParams(activity.getResources().getString(R.string.account_conflict_mess), activity.getResources().getString(R.string.account_conflict_ok), new OnClickListener() {
                        public void onClick(View view) {
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountConflictDialog --- OnClickListener -- ok");
                            confirmDialog.dismiss();
                            GetInterfaceTools.getLoginProvider().startUcenterActivity(activity);
                        }
                    }, activity.getResources().getString(R.string.arefresh_login_cancel), new OnClickListener() {
                        public void onClick(View view) {
                            LogUtils.d(AccountInfoTipHelper.TAG, ">>>>>showAccountConflictDialog --- OnClickListener -- cancel");
                            confirmDialog.dismiss();
                        }
                    });
                    confirmDialog.show();
                }
            });
        }
    }

    private static void renewAuthCookie() {
        Looper.prepare();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            public void run() {
                if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
                    GetInterfaceTools.getIGalaAccountManager().renewCookie(new IVrsCallback<ApiResultData>() {
                        public void onException(ApiException arg0) {
                            if (LoginConstant.ACCOUNT_ECODE_A00001.equals(arg0.getCode())) {
                                if (!StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
                                    GetInterfaceTools.getIGalaAccountManager().logOut(AppRuntimeEnv.get().getApplicationContext(), "", LoginConstant.LGTTYPE_EXCEPTION);
                                }
                                String msg = arg0.getMessage() == null ? "您已经很久没有登录爱奇艺了，请重新登录" : arg0.getMessage();
                                List<Activity> list = AppRuntimeEnv.get().getActivityList();
                                if (list != null && list.size() > 0) {
                                    AccountInfoTipHelper.showAccountExceptionDialog((Activity) list.get(list.size() - 1), msg);
                                }
                            }
                        }

                        public void onSuccess(ApiResultData arg0) {
                        }
                    });
                } else {
                    LogUtils.d(AccountInfoTipHelper.TAG, "isLogout,stop renew");
                }
                handler.postDelayed(this, 86400000);
            }
        });
        Looper.loop();
    }
}
