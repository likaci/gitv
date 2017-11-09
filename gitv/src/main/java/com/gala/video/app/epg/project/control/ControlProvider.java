package com.gala.video.app.epg.project.control;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.control.IControlInterface.Wrapper;
import com.gala.video.lib.share.project.Project;

public class ControlProvider extends Wrapper {
    private boolean mIsDetailGIFAnimDisable;
    private boolean mIsGIFDisablePropertySet = false;

    public boolean debugMode() {
        return true;
    }

    public Drawable getBackgroundDrawable() {
        return GetInterfaceTools.getIBackgroundManager().getBackgroundDrawable();
    }

    public boolean isOpenAnimation() {
        return true;
    }

    public boolean isUsingGalaSettingsOutSide() {
        return false;
    }

    public GlobalDialog getGlobalDialog(Context context) {
        return new GlobalDialog(context);
    }

    public Animation getLoadingViewAnimation() {
        return null;
    }

    public String getNetWorkSettingAction() {
        return CreateInterfaceTools.createNetworkProvider().getNetworkAction();
    }

    public boolean isOpenCrossWalk() {
        return true;
    }

    public boolean disableGifAnimForDetailPage() {
        if (!this.mIsGIFDisablePropertySet && GetInterfaceTools.isPlayerLoaded()) {
            this.mIsDetailGIFAnimDisable = GetInterfaceTools.getPlayerConfigProvider().disableGifAnimForDetailPage();
            this.mIsGIFDisablePropertySet = true;
        }
        return this.mIsDetailGIFAnimDisable;
    }

    public boolean isUsingGalaSettings() {
        return false;
    }

    public boolean releasePlayerOnStop() {
        return false;
    }

    public boolean isOpenCarousel() {
        return GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsSupportCarousel() && Project.getInstance().getBuild().isSupportCarousel();
    }
}
