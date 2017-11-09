package com.gala.video.lib.share.ifimpl.ucenter.account.vipRight;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaVipManager.Wrapper;

abstract class GalaVipRightsManagerBase extends Wrapper {
    static final String LOG_TAG = "GalaVipRightsManager";
    VipRightsPreference mVipPreference = VipRightsPreference.get();

    GalaVipRightsManagerBase() {
    }

    public int getAccountActivationState() {
        return this.mVipPreference.getAccountActivationState(AppRuntimeEnv.get().getApplicationContext());
    }

    public int getActivationFeedbackState() {
        return this.mVipPreference.getActivationFeedbackState(AppRuntimeEnv.get().getApplicationContext());
    }

    public void setActivationFeedbackState(int state) {
        this.mVipPreference.setActivationFeedbackState(AppRuntimeEnv.get().getApplicationContext(), state);
    }

    public String getActivationAccount() {
        return this.mVipPreference.getActivationAccount(AppRuntimeEnv.get().getApplicationContext());
    }

    void setAccountActivationState(int state) {
        this.mVipPreference.setAccountActivationState(AppRuntimeEnv.get().getApplicationContext(), state);
    }
}
