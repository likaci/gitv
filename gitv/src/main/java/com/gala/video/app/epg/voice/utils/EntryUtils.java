package com.gala.video.app.epg.voice.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.tv.voice.core.VoiceUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.HomeActivity;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.setting.QPlayerSettingsActivity;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.applist.AppListActivity;
import com.gala.video.app.epg.ui.background.SettingBGActivity;
import com.gala.video.app.epg.ui.netspeed.QNetSpeedActivity;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.search.QSearchActivity;
import com.gala.video.app.epg.ui.setting.AboutActivity;
import com.gala.video.app.epg.ui.setting.UpgradeActivity;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.app.epg.ui.ucenter.account.LoginActivity;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;

public class EntryUtils {
    public static final String ACTION_SETTING_ABOUT = "gala.settings.about.setting";
    public static final String ACTION_SETTING_ACCOUNT = "internal.settings.user.setting";
    public static final String ACTION_SETTING_AUDIO = "gala.settings.audio.setting";
    public static final String ACTION_SETTING_BG = "gala.settings.wallpaper.setting";
    public static final String ACTION_SETTING_BRIGHTNESS = "gala.settings.brightness.setting";
    public static final String ACTION_SETTING_FEEDBACK = "internal.settings.feedback.setting";
    public static final String ACTION_SETTING_HELP = "internal.settings.help.setting";
    public static final String ACTION_SETTING_MULTISCREEN = "internal.settings.multiscreen.setting";
    public static final String ACTION_SETTING_NETWORK = "gala.settings.network.setting";
    public static final String ACTION_SETTING_NET_SPEED = "internal.settings.netspeed.setting";
    public static final String ACTION_SETTING_PLAYER = "gala.settings.player.setting";
    public static final String ACTION_SETTING_UPGRADE = "gala.settings.upgrade.setting";
    public static final String ACTION_SYSTEM_SETTING = "com.cvte.androidsetting.intent.action.Setting";
    private static final int GLOBAL_INTENT_FLAG = 272629760;
    private static final boolean LOG = true;
    private static final String TAG = "EntryUtils";

    private static void configStartFlag(Context context, int flag) {
        if (!(context instanceof Activity)) {
        }
    }

    public static void setHomeActivity(Context context, int index) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        intent.putExtra("com.gala.video.extra.HOME_TAB_TYPE_INDEX", index);
        PageIOUtils.activityIn(context, intent);
    }

    private static int parse(String str, int defaultValue) {
        int value = defaultValue;
        try {
            value = Integer.parseInt(str);
        } catch (NumberFormatException e) {
            LogUtils.w(TAG, "parse(" + str + ") error!", e);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "parse(" + str + ", " + defaultValue + ") return " + value);
        }
        return value;
    }

    public static void startChannelListActivity(Context context, Channel channel) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        if (context instanceof Activity) {
            AlbumUtils.startChannelPage(context, channel);
        } else {
            AlbumUtils.startChannelPage(context, parse(channel.id, 0), channel.name, -1, (int) GLOBAL_INTENT_FLAG);
        }
    }

    public static void startDailyLoopActivity(Context context, String from) {
        NewsDetailPlayParamBuilder builder = new NewsDetailPlayParamBuilder();
        builder.setFrom(from);
        builder.setChannelName(10007 + "");
        GetInterfaceTools.getPlayerPageProvider().startNewsDetailPlayerPage(context, builder);
    }

    public static void startFootPlayhistoryPage(Context context) {
        if (context instanceof Activity) {
            AlbumUtils.startFootPlayhistoryPage(context);
        } else {
            AlbumUtils.startFootPlayhistoryPage(context, GLOBAL_INTENT_FLAG);
        }
    }

    public static void startNewLivePage(Context context) {
        if (context instanceof Activity) {
            AlbumUtils.startChannelPage(context, 100004, "新上线", 30);
        } else {
            AlbumUtils.startChannelPage(context, 100004, "新上线", 30, (int) GLOBAL_INTENT_FLAG);
        }
    }

    public static void startHotPlayPage(Context context) {
        if (context instanceof Activity) {
            AlbumUtils.startChannelPage(context, 10009, "热播榜", 30);
        } else {
            AlbumUtils.startChannelPage(context, 10009, "热播榜", 30, (int) GLOBAL_INTENT_FLAG);
        }
    }

    public static void startPlayerActivity(Context context, Album albumInfo, String from) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        PlayParams playParam = new PlayParams();
        playParam.sourceType = SourceType.COMMON;
        builder.setPlayParams(playParam);
        builder.setFrom(from);
        builder.setAlbumInfo(albumInfo);
        builder.setClearTaskFlag(false);
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(context, builder);
    }

    public static void startDetailActivity(Context context, ChannelLabel channel, String from) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        ItemUtils.openDetailOrPlay(context, channel, null, from, from, null);
    }

    public static void startHistoryActivity(Context context) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        if (context instanceof Activity) {
            AlbumUtils.startPlayhistoryActivity(context);
        } else {
            AlbumUtils.startPlayhistoryActivity(context, GLOBAL_INTENT_FLAG);
        }
    }

    public static void startTopicReviewPage(Context context) {
    }

    private static void searchAlbumInfoes(Context context, int model, int channelId, String keyWord, String channelName) {
        int intentFlag;
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        if (context instanceof Activity) {
            intentFlag = -1;
        } else {
            intentFlag = 268468224;
        }
        AlbumUtils.startSearchResultPageFromVoice(context, channelId, keyWord, 1, null, intentFlag, channelName, IAlbumConfig.FROM_VOICE);
    }

    public static void searchAlbumByAlbumName(Context context, int channelId, String keyWord, String channelName) {
        searchAlbumInfoes(context, 6, channelId, keyWord, channelName);
    }

    public static void startSearchActivity(Context context) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        Intent intent = new Intent();
        intent.putExtra("tvsrchsource", ISearchConstant.TVSRCHSOURCE_OTHER);
        intent.setClass(context, QSearchActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(GLOBAL_INTENT_FLAG);
        }
        PageIOUtils.activityIn(context, intent);
    }

    public static void startAppList(Context context) {
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        Intent intent = new Intent();
        intent.setClass(context, AppListActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(GLOBAL_INTENT_FLAG);
        }
        PageIOUtils.activityIn(context, intent);
    }

    private static void startLoginActivity(Context context, int tag, int flag) {
        Intent intent = new Intent(ActionSet.ACT_LOGIN);
        intent.putExtra(LoginConstant.FROM_TAG, tag);
        if (flag > 0) {
            intent.setFlags(flag);
        }
        PageIOUtils.activityIn(context, intent);
    }

    public static void startFavorite(Context context) {
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(context)) {
            AlbumUtils.startFavouriteActivity(context);
        } else {
            startLoginActivity(context, 3, -1);
        }
    }

    public static void startSettingActivity(final Context context, String action) {
        LogUtils.d(TAG, "startSettingActivity(" + action + ")");
        configStartFlag(context, GLOBAL_INTENT_FLAG);
        Intent intent = new Intent();
        if (ACTION_SETTING_PLAYER.equals(action)) {
            intent.setClass(context, QPlayerSettingsActivity.class);
        } else if (ACTION_SETTING_ABOUT.equals(action) && Project.getInstance().getControl().isUsingGalaSettings()) {
            intent.setClass(context, AboutActivity.class);
        } else if (ACTION_SETTING_MULTISCREEN.equals(action)) {
            GetInterfaceTools.getWebEntry().gotoMultiscreenActivity(context);
            return;
        } else if (!ACTION_SETTING_HELP.equals(action)) {
            if (ACTION_SETTING_ACCOUNT.equals(action)) {
                intent.setClass(context, LoginActivity.class);
            } else if (ACTION_SETTING_NET_SPEED.equals(action)) {
                intent.setClass(context, QNetSpeedActivity.class);
            } else if (ACTION_SETTING_FEEDBACK.equals(action)) {
                SettingUtils.starFeedbackSettingActivity(context, null);
                return;
            } else if (ACTION_SETTING_UPGRADE.equals(action)) {
                intent.setClass(context, UpgradeActivity.class);
            } else if (ACTION_SETTING_PLAYER.equals(action)) {
                intent.setClass(context, QPlayerSettingsActivity.class);
            } else if (ACTION_SETTING_BG.equals(action)) {
                intent.setClass(context, SettingBGActivity.class);
            } else {
                intent.setAction(action);
            }
        }
        if (!(context instanceof Activity)) {
            intent.addFlags(GLOBAL_INTENT_FLAG);
        }
        try {
            PageIOUtils.activityIn(context, intent);
        } catch (ActivityNotFoundException e) {
            new Handler(context.getMainLooper()).post(new Runnable() {
                public void run() {
                    QToast.makeText(context, R.string.setting_not_install, 3000).show();
                }
            });
        }
    }
}
