package com.gala.video.app.epg.ui.setting;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.data.SettingDataProvider;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.ui.SettingBaseFragment;
import com.gala.video.app.epg.ui.setting.ui.SettingMainFragment;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.utils.PageIOUtils;
import java.util.List;

public class SettingMainActivity extends QMultiScreenActivity implements ISettingEvent {
    private final String LOG_TAG = "EPG/setting/SettingMainActivity";
    private SettingBaseFragment mCurFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activity_setting_main);
        SettingDataProvider.init(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            switchFragment(new SettingMainFragment(), bundle);
        } else {
            LogUtils.m1571e("EPG/setting/SettingMainActivity", "init Exception: onCreate --- bundle is null");
        }
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        if (this.mCurFragment == null || !this.mCurFragment.dispatchKeyEvent(event)) {
            return super.handleKeyEvent(event);
        }
        return true;
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_setting_main_layout);
    }

    private void switchFragment(SettingBaseFragment fragment, Bundle bundle) {
        int stackSize = getSupportFragmentManager().getBackStackEntryCount();
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (stackSize <= 0) {
            transaction.replace(C0508R.id.epg_setting_main_frame, fragment);
        } else {
            transaction.add(C0508R.id.epg_setting_main_frame, (Fragment) fragment);
            transaction.hide(this.mCurFragment);
        }
        transaction.addToBackStack(fragment.getClass().getName());
        transaction.commit();
        LogUtils.m1571e("EPG/setting/SettingMainActivity", "switchFragment," + (stackSize <= 0 ? "replace " : "add ") + fragment);
    }

    private void onBackPressed(SettingItem settingItem) {
        int size = getSupportFragmentManager().getBackStackEntryCount();
        LogUtils.m1574i("EPG/setting/SettingMainActivity", "TEST - " + size);
        if (size > 1) {
            this.mCurFragment = getPreFragment(this.mCurFragment);
            if (this.mCurFragment != null && getSupportFragmentManager().findFragmentById(this.mCurFragment.getId()) != null) {
                LogUtils.m1574i("EPG/setting/SettingMainActivity", "TEST ---- mCurFragment.updateItem --- onBackPressed");
                getSupportFragmentManager().popBackStack();
                this.mCurFragment.updateItem(settingItem);
                return;
            }
            return;
        }
        PageIOUtils.activityOut(this);
    }

    public SettingBaseFragment getPreFragment(Fragment fragment) {
        List<Fragment> fragmentList = fragment.getFragmentManager().getFragments();
        if (fragmentList == null) {
            return null;
        }
        for (int i = fragmentList.indexOf(fragment) - 1; i >= 0; i--) {
            Fragment preFragment = (Fragment) fragmentList.get(i);
            if (preFragment instanceof SettingBaseFragment) {
                return (SettingBaseFragment) preFragment;
            }
        }
        return null;
    }

    public void onSwitchFragment(SettingBaseFragment fragment, Bundle bundle) {
        switchFragment(fragment, bundle);
    }

    public void onPopStackFragment(SettingItem item) {
        onBackPressed(item);
    }

    public void onAttachActivity(SettingBaseFragment fragment) {
        this.mCurFragment = fragment;
    }

    public void onDetachActivity(SettingBaseFragment fragment) {
        if (this.mCurFragment == fragment) {
            this.mCurFragment = null;
        }
    }
}
