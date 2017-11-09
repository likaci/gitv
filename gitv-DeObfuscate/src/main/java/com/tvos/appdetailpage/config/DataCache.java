package com.tvos.appdetailpage.config;

import android.graphics.drawable.Drawable;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.tvos.appdetailpage.model.VersionInfo;
import com.tvos.appdetailpage.utils.ResourcesUtils;
import java.util.HashMap;

public class DataCache {
    public static HashMap<Integer, Integer> mRelations = new HashMap();
    private static boolean mandatoryUpdateFlag = false;
    private static int netState = 0;
    private static boolean updateCheckFlag = false;
    private static VersionInfo updateVersion;

    static {
        mRelations.put(Integer.valueOf(101), Integer.valueOf(getDrawableId("apps_category_101")));
        mRelations.put(Integer.valueOf(102), Integer.valueOf(getDrawableId("apps_category_102")));
        mRelations.put(Integer.valueOf(103), Integer.valueOf(getDrawableId("apps_category_103")));
        mRelations.put(Integer.valueOf(104), Integer.valueOf(getDrawableId("apps_category_104")));
        mRelations.put(Integer.valueOf(105), Integer.valueOf(getDrawableId("apps_category_105")));
        mRelations.put(Integer.valueOf(106), Integer.valueOf(getDrawableId("apps_category_106")));
        mRelations.put(Integer.valueOf(107), Integer.valueOf(getDrawableId("apps_category_107")));
        mRelations.put(Integer.valueOf(108), Integer.valueOf(getDrawableId("apps_category_108")));
        mRelations.put(Integer.valueOf(HistoryInfoHelper.MSG_CACHE_CLEAR_ALL), Integer.valueOf(getDrawableId("apps_category_114")));
        mRelations.put(Integer.valueOf(201), Integer.valueOf(getDrawableId("apps_category_201")));
        mRelations.put(Integer.valueOf(202), Integer.valueOf(getDrawableId("apps_category_202")));
        mRelations.put(Integer.valueOf(203), Integer.valueOf(getDrawableId("apps_category_203")));
        mRelations.put(Integer.valueOf(204), Integer.valueOf(getDrawableId("apps_category_204")));
        mRelations.put(Integer.valueOf(207), Integer.valueOf(getDrawableId("apps_category_207")));
        mRelations.put(Integer.valueOf(208), Integer.valueOf(getDrawableId("apps_category_208")));
        mRelations.put(Integer.valueOf(210), Integer.valueOf(getDrawableId("apps_category_210")));
        mRelations.put(Integer.valueOf(211), Integer.valueOf(getDrawableId("apps_category_211")));
        mRelations.put(Integer.valueOf(213), Integer.valueOf(getDrawableId("apps_category_213")));
        mRelations.put(Integer.valueOf(216), Integer.valueOf(getDrawableId("apps_category_216")));
    }

    public static VersionInfo getUpdateVersion() {
        return updateVersion;
    }

    public static void setUpdateVersion(VersionInfo updateVersion) {
        updateVersion = updateVersion;
    }

    public static boolean isMandatoryUpdateFlag() {
        return mandatoryUpdateFlag;
    }

    public static void setMandatoryUpdateFlag(boolean mandatoryUpdateFlag) {
        mandatoryUpdateFlag = mandatoryUpdateFlag;
    }

    public static Drawable getDrawable(String name) {
        return ContextHolder.getContext().getResources().getDrawable(ResourcesUtils.getResourceId(ContextHolder.getContext(), "drawable", name));
    }

    public static int getDrawableId(String name) {
        return ResourcesUtils.getResourceId(ContextHolder.getContext(), "drawable", name);
    }

    public static int getStringId(String name) {
        return ResourcesUtils.getResourceId(ContextHolder.getContext(), "string", name);
    }

    public static boolean getUpdateCheckFlag() {
        return updateCheckFlag;
    }

    public static void setUpdateCheckFlag(boolean updateCheckFlag) {
        updateCheckFlag = updateCheckFlag;
    }

    public static int getNetState() {
        return netState;
    }

    public static void setNetState(int netState) {
        netState = netState;
    }
}
