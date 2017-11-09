package com.gala.video.app.epg.ui.ucenter.account;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.SupportFragment;
import com.gala.video.app.epg.ui.ucenter.account.event.ILoginEvent;
import com.gala.video.app.epg.ui.ucenter.account.ui.fragment.BaseLoginFragment;
import com.gala.video.app.epg.ui.ucenter.account.ui.fragment.CommLoginFragment;
import com.gala.video.app.epg.ui.ucenter.account.ui.fragment.ScanLoginFragment;
import com.gala.video.app.epg.ui.ucenter.account.ui.fragment.VipRightsScanLoginFragment;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.webview.utils.WebSDKConstants;

public class LoginActivity extends QMultiScreenActivity implements ILoginEvent {
    private static final String LOG_TAG = "EPG/login/LoginActivity";
    private Context mContext;
    private int mEnterType = -1;
    private int mIntentFlag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activity_login);
        this.mContext = AppRuntimeEnv.get().getApplicationContext();
        this.mIntentFlag = getIntent().getIntExtra(LoginConstant.LOGIN_SUCC_TO, -1);
        this.mEnterType = getIntent().getIntExtra(WebSDKConstants.PARAM_KEY_ENTER_TYPE, -1);
        initUserInfo();
    }

    public Drawable getContainerDrawable() {
        return findViewById(C0508R.id.epg_login_container).getBackground();
    }

    private void initUserInfo() {
        if (this.mContext != null) {
            CharSequence account = GetInterfaceTools.getIGalaAccountManager().getUserAccount();
            CharSequence cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
            CharSequence name = GetInterfaceTools.getIGalaAccountManager().getUserName();
            if (StringUtils.isEmpty(cookie) || StringUtils.isEmpty(name) || StringUtils.isEmpty(account)) {
                BaseLoginFragment fragment;
                Bundle bundle = new Bundle();
                bundle.putInt(LoginConstant.LOGIN_SUCC_TO, this.mIntentFlag);
                if (GetInterfaceTools.getIGalaVipManager().needShowActivationPage() && (this.mEnterType == 7 || this.mEnterType == 14)) {
                    fragment = new VipRightsScanLoginFragment();
                } else {
                    fragment = new ScanLoginFragment();
                }
                if (Project.getInstance().getBuild().getCustomerName().contains("qianhuanmojing")) {
                    switchFragment(new CommLoginFragment(), bundle);
                } else {
                    switchFragment(fragment, bundle);
                }
            }
        }
    }

    public void onSwitchFragment(BaseLoginFragment fragment, Bundle bundle) {
        int stackSize = getSupportFragmentManager().getBackStackEntryCount();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (stackSize <= 0) {
            transaction.add(C0508R.id.epg_login_main_frame, (Fragment) fragment);
        } else {
            transaction.replace(C0508R.id.epg_login_main_frame, fragment);
        }
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
    }

    private void switchFragment(final BaseLoginFragment fragment, final Bundle bundle) {
        if (!isFinishing()) {
            runOnUiThread(new Runnable() {
                public void run() {
                    LoginActivity.this.onSwitchFragment(fragment, bundle);
                }
            });
        }
    }

    public void onPlaySound() {
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        if (event.getKeyCode() != 4) {
            return super.handleKeyEvent(event);
        }
        int size = getSupportFragmentManager().getBackStackEntryCount();
        LogUtils.m1574i(LOG_TAG, "TEST - " + size);
        if (size > 1) {
            LogUtils.m1574i(LOG_TAG, "TEST ---- mCurFragment.updateItem --- onBackPressed");
            SupportFragment f = (SupportFragment) getSupportFragmentManager().getFragments().get(size - 1);
            if (f == null) {
                return true;
            }
            f.setBack(true);
            getSupportFragmentManager().popBackStack();
            return true;
        }
        PageIOUtils.activityOut(this);
        return true;
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_login_container);
    }
}
