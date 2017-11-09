package com.gala.video.lib.share.ifimpl.interaction;

import android.support.v4.util.ArrayMap;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.utils.IntentUtils;

public class ActionSetTool {
    static ArrayMap<String, String> mActionMap = new ArrayMap();

    public static String getActionByKey(String key) {
        if (StringUtils.isEmpty((CharSequence) key)) {
            return "";
        }
        return IntentUtils.getActionName((String) mActionMap.get(key));
    }

    static {
        mActionMap.put("HomeActivity", ActionSet.ACT_HOME);
        mActionMap.put("UpgradeActivity", ActionSet.ACT_UPGRADE);
        mActionMap.put("TabManagerActivity", ActionSet.ACT_TABMANAGER);
        mActionMap.put("SoloTabActivity", ActionSet.ACT_SOLO_TAB);
        mActionMap.put("AboutActivity", ActionSet.ACT_ABOUT);
        mActionMap.put("SettingAboutForLauncherActivity", ActionSet.ACT_ABOUT_4LAUNCHER);
        mActionMap.put("AppLauncherActivity", ActionSet.ACT_APP_LAUNCHER);
        mActionMap.put("AppListActivity", ActionSet.ACT_APPLIST);
        mActionMap.put("AppStoreDetailActivity", ActionSet.ACT_APPSTORE_DETAIL);
        mActionMap.put("DebugOptionsActivity", ActionSet.ACT_DEBUG_OPTIONS);
        mActionMap.put("AlbumDetailActivity", ActionSet.ACT_DETAIL);
        mActionMap.put("MultiProcAlbumDetailActivity", ActionSet.ACT_DETAIL_MULTIPROC);
        mActionMap.put("FeedbackActivity", ActionSet.ACT_FEEDBACK);
        mActionMap.put("FaultFeedbackActivity", ActionSet.ACT_FEEDBACK_FAULT);
        mActionMap.put("LoginActivity", ActionSet.ACT_LOGIN);
        mActionMap.put("MenuFloatLayerSettingActivity", ActionSet.ACT_MENU_FLOATLAYER);
        mActionMap.put("MsgCenterActivity", ActionSet.ACT_MSG);
        mActionMap.put("MsgCenterDetailActivity", ActionSet.ACT_MSGDETAIL);
        mActionMap.put("MultiSubjectActivity", ActionSet.ACT_MULTISUBJECT);
        mActionMap.put("NetDiagnoseActivity", ActionSet.ACT_NET_DIAGNOSE);
        mActionMap.put("QNetSpeedActivity", ActionSet.ACT_NETSPEED);
        mActionMap.put("NewsDetailActivity", ActionSet.ACT_NEWSDETAIL);
        mActionMap.put("MultiProcNewsDetailActivity", ActionSet.ACT_NEWSDETAIL_MULTIPROC);
        mActionMap.put("OpenApiLoadingActivity", ActionSet.ACT_OPENAPI_LOADING);
        mActionMap.put("PlayerActivity", ActionSet.ACT_PLAYER);
        mActionMap.put("MultiProcPlayerActivity", ActionSet.ACT_PLAYER_MULTIPROC);
        mActionMap.put("QPlayerSettingsActivity", ActionSet.ACT_PLAYER_SETTING);
        mActionMap.put("RecordFavouriteActivity", ActionSet.ACT_RECORD);
        mActionMap.put("QSearchActivity", ActionSet.ACT_SEARCH);
        mActionMap.put("SettingBGActivity", ActionSet.ACT_SETTING_BG);
        mActionMap.put("SettingMainActivity", ActionSet.ACT_SETTING_MAIN);
        mActionMap.put("StarsActivity", ActionSet.ACT_STARS);
        mActionMap.put("QSubjectReviewActivity", ActionSet.ACT_SUBJECT_REVIEW);
        mActionMap.put("UcenterActivity", ActionSet.ACT_UCENTER);
        mActionMap.put("WebCommonActivity", ActionSet.ACT_WEBCOMMON);
        mActionMap.put("AdImageShowActivity", ActionSet.ACT_AD_IMAGE_SHOW);
        mActionMap.put("WebSubjectActivity", ActionSet.ACT_WEBSUBJECT);
        mActionMap.put("ConcernWeChatActivity", ActionSet.ACT_WECHAT_CONCERN);
        mActionMap.put("QSpeedTestActivity", ActionSet.ACT_SPEEDTEST);
        mActionMap.put("AlbumActivity", ActionSet.ACT_ALBUM);
        mActionMap.put("ActivateActivity", ActionSet.ACT_ACTIVATE);
        mActionMap.put("VipRightsActivateActivity", ActionSet.ACT_VIPRIGHTS);
    }
}
