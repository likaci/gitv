package com.gala.video.app.epg.home.widget.guidelogin;

import android.content.Context;
import android.util.Log;
import com.gala.video.app.epg.preference.GuidePreference;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;

public class CheckInHelper {
    private static final String TAG = "CheckInHelper";

    public static boolean getIsShowActionBar() {
        boolean localFun = isBuildOpenCheckInFun();
        boolean dynamicFun = isDynamicOpenCheckInFun();
        Log.i(TAG, "getIsShowActionBar localFun: " + localFun + ",dynamicFun:" + dynamicFun);
        return localFun && dynamicFun;
    }

    public static void startCheckInActivity(Context context) {
        GetInterfaceTools.getWebEntry().startSignInActivity(context);
    }

    public static void setCloseSign() {
        GuidePreference.saveShowSignGuide(ResourceUtil.getContext(), false);
    }

    private static boolean isShouldShown() {
        return GuidePreference.shouldShowSignGuide(ResourceUtil.getContext());
    }

    public static boolean isShowCheckInDialog() {
        boolean localFun = isBuildOpenCheckInFun();
        boolean localRecommend = isBuildOpenCheckInRecommend();
        Log.i(TAG, "isShowCheckInDialog localFun: " + localFun);
        Log.i(TAG, "isShowCheckInDialog localRecommend: " + localRecommend);
        if (!localFun || !localRecommend) {
            return false;
        }
        boolean dynamicFun = isDynamicOpenCheckInFun();
        boolean dynamicRecommend = isDynamicOpenCheckInRecommend();
        boolean isShouldShown = isShouldShown();
        Log.i(TAG, "isShowCheckInDialog dynamicFun: " + dynamicFun + " dynamicRecommend: " + dynamicRecommend);
        Log.i(TAG, "isShowCheckInDialog isShouldShown: " + isShouldShown);
        if (dynamicFun && dynamicRecommend && isShouldShown) {
            return true;
        }
        return false;
    }

    private static boolean isBuildOpenCheckInFun() {
        return Project.getInstance().getBuild().isOpenCheckInFun();
    }

    private static boolean isBuildOpenCheckInRecommend() {
        return Project.getInstance().getBuild().isOpencheckInRecommend();
    }

    private static boolean isDynamicOpenCheckInFun() {
        return GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsCheckInFun();
    }

    private static boolean isDynamicOpenCheckInRecommend() {
        return GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsCheckInRecommend();
    }
}
