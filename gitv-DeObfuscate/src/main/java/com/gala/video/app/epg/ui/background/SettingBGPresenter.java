package com.gala.video.app.epg.ui.background;

import com.gala.video.app.epg.ui.background.SettingBGContract.Presenter;
import com.gala.video.app.epg.ui.background.SettingBGContract.View;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper;
import com.gala.video.lib.share.ifmanager.bussnessIF.skin.IThemeZipHelper.BackgroundType;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingBGPresenter implements Presenter {
    protected static final String TAG = "EPG/StarsPresenter";
    private final View mBgSettingView;
    private IThemeZipHelper mThemeZipHelper = GetInterfaceTools.getIThemeZipHelper();
    List<String> mThumbBGList = new ArrayList();

    public SettingBGPresenter(View statisticsView) {
        this.mBgSettingView = (View) ActivityUtils.checkNotNull(statisticsView, "View cannot be null!");
        this.mBgSettingView.setPresenter(this);
    }

    public void start() {
        initSet();
        if (this.mThemeZipHelper.hasThemeZip()) {
            startTask();
        }
        this.mBgSettingView.setBgList(this.mThumbBGList);
        this.mBgSettingView.setSelectedPostion();
    }

    public void onDestroy() {
    }

    private void initSet() {
        this.mThumbBGList.clear();
        this.mThumbBGList.add(SystemConfigPreference.SETTING_BACKGROUND_NIGHT_DEFAULT);
    }

    private void startTask() {
        this.mThumbBGList.addAll(getDrawableListByPath(this.mThemeZipHelper.getBackground(BackgroundType.NIGHT_THUMB)));
    }

    private List<String> getDrawableListByPath(String path) {
        ArrayList<String> list = new ArrayList();
        LogUtils.m1568d(TAG, "getDrawableListByPath---path = " + path);
        try {
            File dir = new File(path);
            if (dir.exists() && dir.isDirectory()) {
                String[] fileNameList = dir.list();
                if (fileNameList != null) {
                    int i = 0;
                    while (i < fileNameList.length && i < 10) {
                        String tempFile = fileNameList[i];
                        LogUtils.m1568d(TAG, "tempFile = " + tempFile + " ; i = " + i);
                        list.add(path + tempFile);
                        i++;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.m1571e(TAG, "Exception ---getDrawableListByPath---path = " + e.getMessage());
        }
        return list;
    }
}
