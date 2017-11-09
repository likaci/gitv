package com.gala.video.lib.share.ifimpl.ucenter.account.vipRight;

import android.content.Context;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBack.PingBackInitParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.IGalaVipManager;
import com.gala.video.lib.share.project.Project;

public class GalaVipRightsManager extends GalaVipRightsManagerBase {
    private static IGalaVipManager mInstance;

    public /* bridge */ /* synthetic */ int getAccountActivationState() {
        return super.getAccountActivationState();
    }

    public /* bridge */ /* synthetic */ String getActivationAccount() {
        return super.getActivationAccount();
    }

    public /* bridge */ /* synthetic */ int getActivationFeedbackState() {
        return super.getActivationFeedbackState();
    }

    public /* bridge */ /* synthetic */ void setActivationFeedbackState(int i) {
        super.setActivationFeedbackState(i);
    }

    GalaVipRightsManager() {
    }

    public int getActivationState() {
        Context context = AppRuntimeEnv.get().getApplicationContext();
        int feedbackState = this.mVipPreference.getActivationFeedbackState(context);
        if (-1 == feedbackState) {
            setAccountActivationState(0);
            LogUtils.m1568d("GalaVipRightsManager", "getActivationState()=-1");
            return -1;
        } else if (1 == feedbackState) {
            LogUtils.m1568d("GalaVipRightsManager", "getActivationState()=1");
            return 1;
        } else if (1 == this.mVipPreference.getAccountActivationState(context)) {
            return 1;
        } else {
            LogUtils.m1568d("GalaVipRightsManager", "getActivationState()=0");
            return 0;
        }
    }

    public boolean needQueryActivationStateFromServer() {
        return getActivationState() == -1;
    }

    public boolean needShowActivationPage() {
        if (!Project.getInstance().getBuild().isSupportVipRightsActivation()) {
            return false;
        }
        Context context = AppRuntimeEnv.get().getApplicationContext();
        int feedbackState = this.mVipPreference.getActivationFeedbackState(context);
        int accountState = this.mVipPreference.getAccountActivationState(context);
        if (1 == feedbackState || 1 == accountState) {
            return false;
        }
        return true;
    }

    public void setPingBackVipAct() {
        PingBackInitParams params = PingBack.getInstance().getPingbackInitParams();
        if (Project.getInstance().getBuild().isSupportVipRightsActivation()) {
            switch (getActivationState()) {
                case 0:
                    params.sIsVipAct = "1";
                    break;
                case 1:
                    params.sIsVipAct = "0";
                    break;
                default:
                    params.sIsVipAct = "";
                    break;
            }
        }
        params.sIsVipAct = "";
        PingBack.getInstance().initialize(AppRuntimeEnv.get().getApplicationContext(), params);
    }

    public String getPingBackVipAct() {
        String isvipact = "";
        if (!Project.getInstance().getBuild().isSupportVipRightsActivation()) {
            return isvipact;
        }
        switch (getActivationState()) {
            case 0:
                return "1";
            case 1:
                return "0";
            default:
                return "";
        }
    }
}
