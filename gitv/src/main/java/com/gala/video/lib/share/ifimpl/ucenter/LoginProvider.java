package com.gala.video.lib.share.ifimpl.ucenter;

import android.content.Context;
import android.content.Intent;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.ILoginProvider.Wrapper;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import com.qiyi.tv.client.impl.Utils;

class LoginProvider extends Wrapper {
    private static final String LOGIN_ACTIVITY = "com.gala.video.app.epg.ui.ucenter.account.LoginActivity";
    private static final String LOG_TAG = "EPG/login/IntentUtils";
    private static final String UCENTER_ACTIVITY = "com.gala.video.app.epg.ui.ucenter.UcenterActivity";

    LoginProvider() {
    }

    public void startUcenterActivity(Context context) {
        Intent intent = new Intent();
        intent.setAction(IntentUtils.getActionName("com.gala.video.app.epg.ui.ucenter.UcenterActivity"));
        PageIOUtils.activityIn(context, intent);
    }

    public void startUcenterActivityFromCardSetting(Context context) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, LoginConstant.S1_FROM_SETTINGT_HOME);
        PageIOUtils.activityIn(context, intent);
    }

    public void startUcenterActivityFromSettingLayer(Context context) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, "menu");
        PageIOUtils.activityIn(context, intent);
    }

    public void startUcenterActivityFromSetting(Context context) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, "setting");
        PageIOUtils.activityIn(context, intent);
    }

    public void startUcenterActivityFromHomeTab(Context context) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, LoginConstant.S1_FROM_HOMEBAR);
        PageIOUtils.activityIn(context, intent);
    }

    public void startUcenterActivityFromSoloTab(Context context) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, LoginConstant.S1_FROM_SOLO);
        PageIOUtils.activityIn(context, intent);
    }

    public void startLoginForAlbum(Context context, int flag) {
        Intent intent = new Intent("com.gala.video.app.epg.ui.ucenter.UcenterActivity");
        intent.putExtra(LoginConstant.S1_TAB, LoginConstant.S1_FROM_ALBUMBAR);
        intent.setFlags(flag);
        PageIOUtils.activityIn(context, intent);
    }

    public void startLoginActivity(Context context, String s1, int loginSuccessTo) {
        startLoginActivity(context, s1, loginSuccessTo, -1);
    }

    public void startLoginActivity(Context context, String s1, int loginSuccessTo, int requestCode) {
        Intent intent = new Intent();
        intent.setAction(IntentUtils.getActionName("com.gala.video.app.epg.ui.ucenter.account.LoginActivity"));
        intent.putExtra(LoginConstant.S1_TAB, s1);
        intent.putExtra(LoginConstant.LOGIN_SUCC_TO, loginSuccessTo);
        PageIOUtils.activityIn(context, intent, requestCode);
    }

    public void startLoginActivityForCoupon(Context context, String code, String signKey, String from, String s1, String incomSrc, int enterType) {
        Intent intent = new Intent();
        intent.setAction(IntentUtils.getActionName("com.gala.video.app.epg.ui.ucenter.account.LoginActivity"));
        intent.putExtra(LoginConstant.S1_TAB, s1);
        intent.putExtra(LoginConstant.INCOMSRC_TAB, incomSrc);
        intent.putExtra(LoginConstant.S2_TAB, from);
        intent.putExtra(LoginConstant.COUPON_CODE, code);
        intent.putExtra(LoginConstant.COUPON_SIGN_KEY, signKey);
        intent.putExtra(LoginConstant.COUPON_ENTER_TYPE, enterType);
        intent.putExtra(LoginConstant.LOGIN_SUCC_TO, 6);
        PageIOUtils.activityIn(context, intent);
    }

    public void startLoginActivityOpenApi(Context context, int flags) {
        Intent intent = new Intent();
        intent.setAction(IntentUtils.getActionName("com.gala.video.app.epg.ui.ucenter.UcenterActivity"));
        intent.putExtra("source", "openAPI");
        intent.putExtra("fromWhere", "openAPI");
        if (-1 != flags) {
            intent.setFlags(flags);
        }
        intent.putExtra("from_openapi", true);
        PageIOUtils.activityIn(context, intent);
    }

    public void startVipGuideActivity(Context context, int tag) {
        Intent intent = new Intent(ActionSet.ACT_ACTIVATE);
        intent.putExtra(LoginConstant.FROM_TAG, tag);
        intent.addFlags(1073741824);
        PageIOUtils.activityIn(context, intent);
    }

    public void startActivateActivity(Context context, String s2, int enter_type) {
        Intent intent;
        if (StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
            LogUtils.e(LOG_TAG, "startActivateActivity -- no cookie -- need login first");
            intent = new Intent("com.gala.video.app.epg.ui.ucenter.account.LoginActivity");
            intent.putExtra(WebSDKConstants.PARAM_KEY_ENTER_TYPE, enter_type);
            intent.putExtra(LoginConstant.LOGIN_SUCC_TO, 1);
        } else if (GetInterfaceTools.getIGalaVipManager().needShowActivationPage() && (enter_type == 7 || enter_type == 3 || enter_type == 14)) {
            LogUtils.i(LOG_TAG, "start VIP rights ActivateActivity -- have cookie");
            intent = new Intent(ActionSet.ACT_VIPRIGHTS);
        } else {
            LogUtils.i(LOG_TAG, "startActivateActivity -- have cookie");
            intent = new Intent(ActionSet.ACT_ACTIVATE);
        }
        intent.putExtra(LoginConstant.ACTIVATE_S2, s2);
        if (14 == enter_type) {
            intent.setFlags(Utils.INTENT_FLAG_DEFAULT);
        }
        PageIOUtils.activityIn(context, intent);
    }

    public void startActivateActivityOpenApi(Context context, String code, int flag) {
        Intent intent;
        if (StringUtils.isEmpty(GetInterfaceTools.getIGalaAccountManager().getAuthCookie())) {
            LogUtils.e(LOG_TAG, "startActivateActivity -- no cookie -- need login first");
            intent = new Intent("com.gala.video.app.epg.ui.ucenter.account.LoginActivity");
            intent.putExtra(LoginConstant.LOGIN_SUCC_TO, 1);
        } else {
            LogUtils.i(LOG_TAG, "startActivateActivity -- have cookie");
            intent = new Intent(ActionSet.ACT_ACTIVATE);
        }
        intent.setFlags(flag);
        intent.putExtra(LoginConstant.ACTIVATE_CODE, code);
        PageIOUtils.activityIn(context, intent);
    }
}
