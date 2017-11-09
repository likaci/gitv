package com.gala.video.app.player.provider;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.constants.PlayerIntentConfig;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tv.voice.core.VoiceUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.app.player.PlayerActivity;
import com.gala.video.app.player.PlayerAdapterSettingActivity;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.cache.LoopCache;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.CarouselPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.LivePlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsParams;
import com.gala.video.lib.share.common.model.player.PushPlayParamBuilder;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IGalaPlayerPageProvider.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.qiyi.tv.client.impl.Utils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GalaPlayerPageProvider extends Wrapper {
    private static final int ALBUM_DETAIL_PAGE = 2;
    private static final int BASE_PLAYER_PAGE = 1;
    private static final String EXTRA_INDEX = "EXTRA_PLAYER_INDEX";
    private static final int NEWS_DETAIL_PAGE = 3;
    private static final String TAG = "GalaPlayerPageProvider";
    private static String sEventId;
    private static final LoopCache<Bundle> sIntentBundle = new LoopCache(5);

    private static class MyPluginStateListener implements OnStateChangedListener {
        private Context mContext;
        private Intent mIntent;
        private boolean mIsOut;

        public MyPluginStateListener(Context context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        public MyPluginStateListener(Context context, Intent intent, boolean isOut) {
            this.mContext = context;
            this.mIntent = intent;
            this.mIsOut = isOut;
        }

        public void onSuccess() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalaPlayerPageProvider.TAG, "[PERF-LOADING]tm_plugin.load");
            }
            GlobalPerformanceTracker.instance().recordPerformanceStepEnd(GalaPlayerPageProvider.sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
            GlobalPerformanceTracker.instance().recordPerformanceStepStart(GalaPlayerPageProvider.sEventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
            this.mIntent.addCategory("android.intent.category.DEFAULT");
            PageIOUtils.activityIn(this.mContext, this.mIntent);
            if (this.mIsOut) {
                ((Activity) this.mContext).finish();
            }
        }

        public void onFailed() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalaPlayerPageProvider.TAG, "[PERF-LOADING] playerplugin.Load onFailed");
            }
        }

        public void onCanceled() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalaPlayerPageProvider.TAG, "onCanceled");
            }
            if (this.mIsOut) {
                ((Activity) this.mContext).finish();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(GalaPlayerPageProvider.TAG, "finish");
                }
            }
        }

        public void onLoading() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(GalaPlayerPageProvider.TAG, "onLoading");
            }
        }
    }

    private void setDefaultTabSource(String from) {
        if (StringUtils.equals(from, "openAPI")) {
            PingBackUtils.setTabSrc("其他");
        }
    }

    private String getActivityName(int pageType) {
        String activityName = "";
        boolean isSupportPlayerMultiProcess = Project.getInstance().getBuild().supportPlayerMultiProcess();
        switch (pageType) {
            case 1:
                activityName = isSupportPlayerMultiProcess ? ActionSet.ACT_PLAYER_MULTIPROC : ActionSet.ACT_PLAYER;
                break;
            case 2:
                activityName = isSupportPlayerMultiProcess ? ActionSet.ACT_DETAIL_MULTIPROC : ActionSet.ACT_DETAIL;
                break;
            case 3:
                activityName = isSupportPlayerMultiProcess ? ActionSet.ACT_NEWSDETAIL_MULTIPROC : ActionSet.ACT_NEWSDETAIL;
                break;
        }
        return activityName;
    }

    private void recordCallTimeAndDelayPingback(Bundle extras) {
        extras.putLong(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, GlobalPerformanceTracker.instance().recordRoutineStart("pageInitToStarted"));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "[PERF-LOADING]page.click");
        }
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
    }

    public static synchronized Bundle wrapIntentExtras(Intent intent) {
        Bundle bundle;
        synchronized (GalaPlayerPageProvider.class) {
            bundle = new Bundle();
            int index = sIntentBundle.add(bundle);
            intent.putExtra(EXTRA_INDEX, index);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "wrapIntentExtras(), index=" + index);
            }
        }
        return bundle;
    }

    public static synchronized boolean restoreIntentExtras(Intent intent) {
        boolean z;
        synchronized (GalaPlayerPageProvider.class) {
            int index = intent.getIntExtra(EXTRA_INDEX, -1);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "restoreIntentExtras(), index=" + index);
            }
            Bundle bundle = null;
            if (index >= 0 && index < sIntentBundle.getCapacity()) {
                bundle = (Bundle) sIntentBundle.get(index);
            }
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            z = bundle != null;
        }
        return z;
    }

    private void fillOutsideInfoIfNeed(Bundle extras, Album albumInfo, String from) {
        if (extras != null && albumInfo != null && "openAPI".equals(from)) {
            extras.putString("vrsTvId", albumInfo.tvQid);
            extras.putString("vrsVid", albumInfo.vid);
            extras.putString("album_name", albumInfo.name);
            extras.putString("vrsAlbumId", albumInfo.qpId);
            extras.putString("history", Integer.toString(albumInfo.playTime));
            extras.putString("from", "openAPI");
            extras.putString("fromWhere", "openAPI");
            extras.putInt("videoType", SourceType.OUTSIDE.ordinal());
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "fillOutsideInfoIfNeed() fill out album info!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "fillOutsideInfoIfNeed() ignore!");
        }
    }

    private void fillOutsideIntentInfoIfNeed(Intent intent, Album albumInfo, String from) {
        if (intent != null && albumInfo != null && "openAPI".equals(from)) {
            intent.putExtra("vrsTvId", albumInfo.tvQid);
            intent.putExtra("vrsVid", albumInfo.vid);
            intent.putExtra("album_name", albumInfo.name);
            intent.putExtra("vrsAlbumId", albumInfo.qpId);
            intent.putExtra("history", Integer.toString(albumInfo.playTime));
            intent.putExtra("from", "openAPI");
            intent.putExtra("fromWhere", "openAPI");
            intent.putExtra("videoType", SourceType.OUTSIDE.ordinal());
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "fillOutsideIntentInfoIfNeed() fill out album info!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "fillOutsideIntentInfoIfNeed() ignore!");
        }
    }

    private void startBasePlay(Context context, Album albumInfo, int playOrder, PlayParams params, String from, boolean clearTaskFlag, String buySource, String tabSource, boolean continueNextVideo, Album detailOriAlbum) {
        Intent intent = new Intent();
        setDefaultTabSource(from);
        sEventId = PingBackUtils.createEventId();
        intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        if (clearTaskFlag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "add clear task flag");
            }
            intent.addFlags(32768);
        }
        String className = getActivityName(1);
        intent.setAction(IntentUtils.getActionName(className));
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            intent.putExtra(IntentConfig2.INTENT_PARAM_COMPONENT_NAME, className);
            intent.putExtra("continue_play_next_video", continueNextVideo);
            intent.putExtra("videoType", params.sourceType);
            intent.putExtra("albumInfo", albumInfo);
            intent.putExtra("from", from);
            intent.putExtra("episodePlayOrder", playOrder);
            intent.putExtra("play_list_info", params);
            intent.putExtra("buy_source", buySource);
            intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            intent.putExtra("eventId", sEventId);
            intent.putExtra("detailorigenalalbum", detailOriAlbum);
            fillOutsideIntentInfoIfNeed(intent, albumInfo, from);
            recordCallTimeAndDelayPingback(intent.getExtras());
        } else {
            Bundle extras = wrapIntentExtras(intent);
            extras.putSerializable("continue_play_next_video", Boolean.valueOf(continueNextVideo));
            extras.putSerializable("videoType", params.sourceType);
            extras.putSerializable("albumInfo", albumInfo);
            extras.putString("from", from);
            extras.putInt("episodePlayOrder", playOrder);
            extras.putSerializable("play_list_info", params);
            extras.putString("buy_source", buySource);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            extras.putString("eventId", sEventId);
            extras.putSerializable("detailorigenalalbum", detailOriAlbum);
            fillOutsideInfoIfNeed(extras, albumInfo, from);
            recordCallTimeAndDelayPingback(extras);
        }
        if ((context instanceof PlayerActivity) && !((PlayerActivity) context).isFinishing()) {
            ((PlayerActivity) context).finish();
        }
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }

    private void startPlayForCarousel(Context context, ChannelCarousel oldChannel, String from, String tabSource) {
        LogUtils.m1568d(TAG, ">> stratPlayForCarousel Channel" + oldChannel + "from=" + from + ", tabSource=" + tabSource);
        Intent intent = new Intent();
        if (!(context instanceof Activity)) {
            intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        }
        if (oldChannel != null) {
            TVChannelCarousel tvChannel = new TVChannelCarousel();
            Album album = new Album();
            album.live_channelId = String.valueOf(oldChannel.id);
            album.chnName = oldChannel.name;
            intent.putExtra("albumInfo", album);
            tvChannel.sid = oldChannel.tableNo;
            tvChannel.id = oldChannel.id;
            tvChannel.name = oldChannel.name;
            tvChannel.icon = oldChannel.logo;
            intent.putExtra("carouselChannel", tvChannel);
        }
        sEventId = PingBackUtils.createEventId();
        Bundle extras = wrapIntentExtras(intent);
        intent.setAction(IntentUtils.getActionName(getActivityName(1)));
        intent.putExtra("videoType", SourceType.CAROUSEL);
        intent.putExtra("from", from);
        intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
        intent.putExtra("eventId", sEventId);
        recordCallTimeAndDelayPingback(extras);
        if ((context instanceof PlayerActivity) && !((PlayerActivity) context).isFinishing()) {
            ((PlayerActivity) context).finish();
        }
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
        LogUtils.m1568d(TAG, "<< stratPlayForCarousel");
    }

    private void startPlayForLive(Context context, Album album, String from, ArrayList<Album> flowerList, String buySource, String tabSource) {
        LogUtils.m1568d(TAG, ">> startPlayForLive: album=" + DataUtils.albumInfoToString(album) + ", from=" + from + ", flowerList=" + flowerList + ", buySource=" + buySource + ", tabSource=" + tabSource);
        Intent intent = new Intent();
        if (!(context instanceof Activity)) {
            intent.addFlags(Utils.INTENT_FLAG_DEFAULT);
        }
        if (PlayerDebugUtils.testPlayerShowLive()) {
            album.sliveTime = String.valueOf(PlayerDebugUtils.getLiveStartTime() * 1000);
            ArrayList<Album> testFlower = new ArrayList();
            Album album1 = new Album();
            album1.qpId = "162494600";
            album1.tvQid = "162494600";
            album1.vid = "0c71734f004049858a9cb0efbb761aa0";
            album1.name = "EXO鹿晗节目现场热舞模仿Apink新曲《NoNoNo》 爆笑全场";
            album1.len = "41";
            album1.chnId = 5;
            album1.isFlower = 1;
            Album album2 = new Album();
            album2.qpId = "163705000";
            album2.tvQid = "163705000";
            album2.vid = "dd7371f10df9414ea78e9a67ff0bcfff";
            album2.name = "小老鼠上灯台 亲宝儿歌";
            album2.len = "73";
            album2.chnId = 5;
            album2.isFlower = 1;
            testFlower.add(album1);
            testFlower.add(album2);
            intent.putExtra("playlist", testFlower);
        } else {
            intent.putExtra("playlist", flowerList);
        }
        sEventId = PingBackUtils.createEventId();
        String className = getActivityName(1);
        intent.setAction(IntentUtils.getActionName(className));
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            intent.putExtra(IntentConfig2.INTENT_PARAM_COMPONENT_NAME, className);
            intent.putExtra("videoType", SourceType.LIVE);
            intent.putExtra("albumInfo", album);
            intent.putExtra("from", from);
            intent.putExtra("buy_source", buySource);
            intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            intent.putExtra("eventId", sEventId);
            recordCallTimeAndDelayPingback(intent.getExtras());
        } else {
            Bundle extras = wrapIntentExtras(intent);
            extras.putSerializable("videoType", SourceType.LIVE);
            extras.putSerializable("albumInfo", album);
            extras.putString("from", from);
            extras.putString("buy_source", buySource);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            extras.putString("eventId", sEventId);
            recordCallTimeAndDelayPingback(extras);
        }
        if (ListUtils.isEmpty((List) flowerList)) {
            LogUtils.m1568d(TAG, "startPlayForLive(), no flower");
        } else {
            LogUtils.m1568d(TAG, "flower for live, size=" + flowerList.size());
            Iterator it = flowerList.iterator();
            while (it.hasNext()) {
                Album flower = (Album) it.next();
                LogUtils.m1568d(TAG, "name = " + flower.name + "albumId=" + flower.qpId + ",tvId=" + flower.tvQid);
            }
        }
        LogUtils.m1568d(TAG, "startPlayForLive(), album[" + album + " isLive=" + album.isLive + " isFlower=" + album.isFlower + " liveChannelId=" + album.live_channelId + "], currentServerTime = " + new Date(DeviceUtils.getServerTimeMillis()) + ", starttime = " + new Date(StringUtils.parse(album.sliveTime, -1)));
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }

    private String msParamsToString(MultiScreenParams params) {
        if (params == null) {
            return "NULL";
        }
        StringBuilder builder = new StringBuilder();
        builder.append("MultiScreenParams@").append(Integer.toHexString(params.hashCode()) + "{");
        builder.append("aid=").append(params.aid);
        builder.append(", tvid=").append(params.tvid);
        builder.append(", vid=").append(params.vid);
        builder.append(", auth=").append(params.auth);
        builder.append(", type=").append(params.type);
        builder.append(", platform=").append(params.platform);
        builder.append(", mAlbumName=").append(params.mAlbumName);
        builder.append(", mIsExclusive=").append(params.mIsExclusive);
        builder.append(", mIsVip=").append(params.mIsVip);
        builder.append(", control=").append(params.control);
        builder.append(", from=").append(params.from);
        builder.append(", getVideoRequestkey=").append(params.getVideoRequestkey);
        builder.append(", history=").append(params.history);
        builder.append(", imei=").append(params.imei);
        builder.append(", key=").append(params.key);
        builder.append(", phone_tv_json=").append(params.phone_tv_json);
        builder.append(", speed=").append(params.speed);
        builder.append(", streamType=").append(params.streamType);
        builder.append(", value=").append(params.value);
        builder.append(", version=").append(params.version);
        builder.append(", keyKind=").append(params.keyKind);
        builder.append(", open_oversea_flag=").append(params.openForOversea);
        builder.append("}");
        return builder.toString();
    }

    private void startPlayForPush(Context context, MultiScreenParams msParams, String from, String se, String buySource, String tabSource) {
        LogUtils.m1568d(TAG, ">> startPlayForPush: ms params=" + msParamsToString(msParams) + ", from=" + from + ", se=" + se + ", buySource=" + buySource + ", tabSource=" + tabSource);
        Intent intent = new Intent();
        intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        sEventId = PingBackUtils.createEventId();
        String className = getActivityName(1);
        intent.setAction(IntentUtils.getActionName(className));
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            intent.putExtra(IntentConfig2.INTENT_PARAM_COMPONENT_NAME, className);
            intent.putExtra("action_is_multiscreen", true);
            intent.putExtra("vrsAlbumId", msParams.aid);
            intent.putExtra("vrsTvId", msParams.tvid);
            intent.putExtra("history", msParams.history);
            intent.putExtra("vrsVid", msParams.vid);
            intent.putExtra("albumvip", msParams.mIsVip);
            intent.putExtra("album_name", msParams.mAlbumName);
            intent.putExtra("album_exclusive", msParams.mIsExclusive);
            intent.putExtra("push_auth_cookie", msParams.auth);
            intent.putExtra("push_auth_vid", msParams.vid);
            intent.putExtra("push_auth_platform", msParams.platform);
            intent.putExtra(PlayerIntentConfig.PUSH_VIDEO_CTYPE, msParams.ctype);
            intent.putExtra(PlayerIntentConfig.PUSH_V_FLAG, msParams.f2019v);
            intent.putExtra("buy_source", buySource);
            intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            intent.putExtra("from", from);
            if (se != null) {
                intent.putExtra("se", se);
            }
            if (from.equals("openAPI")) {
                intent.putExtra("videoType", SourceType.OUTSIDE);
            } else if (from.equals("vod")) {
                intent.putExtra("videoType", SourceType.VOD);
            } else {
                intent.putExtra("videoType", SourceType.PUSH);
            }
            intent.putExtra("open_for_oversea", msParams.openForOversea);
            intent.putExtra("eventId", sEventId);
            recordCallTimeAndDelayPingback(intent.getExtras());
        } else {
            Bundle extras = wrapIntentExtras(intent);
            extras.putBoolean("action_is_multiscreen", true);
            extras.putString("vrsAlbumId", msParams.aid);
            extras.putString("vrsTvId", msParams.tvid);
            extras.putString("history", msParams.history);
            extras.putString("vrsVid", msParams.vid);
            extras.putBoolean("albumvip", msParams.mIsVip);
            extras.putString("album_name", msParams.mAlbumName);
            extras.putBoolean("album_exclusive", msParams.mIsExclusive);
            extras.putString("push_auth_cookie", msParams.auth);
            extras.putString("push_auth_vid", msParams.vid);
            extras.putString("push_auth_platform", msParams.platform);
            extras.putString(PlayerIntentConfig.PUSH_VIDEO_CTYPE, msParams.ctype);
            extras.putString(PlayerIntentConfig.PUSH_V_FLAG, msParams.f2019v);
            extras.putString("buy_source", buySource);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            extras.putString("from", from);
            if (se != null) {
                extras.putString("se", se);
            }
            if (from.equals("openAPI")) {
                extras.putSerializable("videoType", SourceType.OUTSIDE);
            } else if (from.equals("vod")) {
                extras.putSerializable("videoType", SourceType.VOD);
            } else {
                extras.putSerializable("videoType", SourceType.PUSH);
            }
            extras.putBoolean("open_for_oversea", msParams.openForOversea);
            extras.putString("eventId", sEventId);
            recordCallTimeAndDelayPingback(extras);
        }
        if ((context instanceof PlayerActivity) && !((PlayerActivity) context).isFinishing()) {
            ((PlayerActivity) context).finish();
        }
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }

    private void startAlbumDetailInner(Context context, Album album, PlayParams params, boolean isComplete, String from, String buySource, String tabSource, boolean clearTaskFlag, boolean continueNextVideo) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> startAlbumDetailInner context=" + context + JsonBundleConstants.A71_TRACKING_PARAMS + params + ", from=" + from + ", isComplete=" + isComplete + ", buySource=" + buySource + ", tabSource=" + tabSource + clearTaskFlag + continueNextVideo + ", album=" + DataUtils.albumInfoToString(album));
        }
        Intent intent = new Intent();
        String className = getActivityName(2);
        setDefaultTabSource(from);
        intent.setFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        if (clearTaskFlag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "add clear task flag");
            }
            intent.addFlags(32768);
        }
        sEventId = PingBackUtils.createEventId();
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            intent.putExtra("albumInfo", album);
            intent.putExtra("play_list_info", params);
            intent.putExtra("from", from);
            intent.putExtra("continue_play_next_video", continueNextVideo);
            intent.putExtra("buy_source", buySource);
            intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            intent.putExtra("eventId", sEventId);
            intent.putExtra(IntentConfig2.INTENT_PARAM_ALBUM_INFO_COMPLETE, isComplete);
            fillOutsideIntentInfoIfNeed(intent, album, from);
            recordCallTimeAndDelayPingback(intent.getExtras());
        } else {
            Bundle extras = wrapIntentExtras(intent);
            extras.putSerializable("albumInfo", album);
            extras.putSerializable("play_list_info", params);
            extras.putSerializable("continue_play_next_video", Boolean.valueOf(continueNextVideo));
            extras.putString("from", from);
            extras.putString("buy_source", buySource);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            extras.putString("eventId", sEventId);
            extras.putBoolean(IntentConfig2.INTENT_PARAM_ALBUM_INFO_COMPLETE, isComplete);
            fillOutsideInfoIfNeed(extras, album, from);
            recordCallTimeAndDelayPingback(extras);
        }
        intent.setAction(IntentUtils.getActionName(className));
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }

    private void startDetailForLoop(Context context, String from, String channelName, NewsParams params, String buySource, String tabSource) {
        if (context != null) {
            LogUtils.m1568d(TAG, ">> startDetailForLoop: context=" + context + ", from=" + from + ", channelName=" + channelName + ", params" + params + ", buySource=" + buySource + ", tabSource=" + tabSource);
            Intent intent = new Intent();
            if (!(context instanceof Activity)) {
                intent.setFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
            }
            sEventId = PingBackUtils.createEventId();
            String className = getActivityName(3);
            if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
                intent.putExtra(IntentConfig2.INTENT_PARAM_COMPONENT_NAME, className);
                intent.putExtra("from", from);
                intent.putExtra("videoType", SourceType.DAILY_NEWS);
                intent.putExtra(IntentConfig2.INTENT_PARAM_NEWS, params);
                intent.putExtra("channelName", channelName);
                intent.putExtra("buy_source", buySource);
                intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
                intent.putExtra("eventId", sEventId);
                recordCallTimeAndDelayPingback(intent.getExtras());
            } else {
                Bundle extras = wrapIntentExtras(intent);
                extras.putString("from", from);
                extras.putSerializable("videoType", SourceType.DAILY_NEWS);
                extras.putSerializable(IntentConfig2.INTENT_PARAM_NEWS, params);
                extras.putString("channelName", channelName);
                extras.putString("buy_source", buySource);
                extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
                extras.putString("eventId", sEventId);
                recordCallTimeAndDelayPingback(extras);
            }
            intent.setAction(IntentUtils.getActionName(className));
            GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
        }
    }

    public void startBasePlayerPage(Context context, BasePlayParamBuilder builder) {
        if (builder.mPlayParams == null) {
            LogUtils.m1571e(TAG, ">> startBasePlayerPage: mPlayParams" + builder.mPlayParams);
            throw new IllegalArgumentException("mPlayParams can't be null");
        }
        if (!ListUtils.isEmpty(builder.mPlayParams.continuePlayList)) {
            int size = builder.mPlayParams.continuePlayList.size();
            LogUtils.m1568d(TAG, "size=" + size + ", index=" + builder.mPlayParams.playIndex);
            if (builder.mPlayParams.playIndex < size) {
                builder.mAlbumInfo = (Album) builder.mPlayParams.continuePlayList.get(builder.mPlayParams.playIndex);
            }
        }
        if (!StringUtils.isEmpty(builder.mPlayParams.from)) {
            builder.setFrom(builder.mPlayParams.from);
        }
        startBasePlay(context, builder.mAlbumInfo, builder.mPlayOrder, builder.mPlayParams, builder.mFrom, builder.mClearTaskFlag, builder.mBuySource, builder.mTabSource, builder.mContinueNextVideo, builder.mDetailOriAlbum);
    }

    public void startLivePlayerPage(Context context, LivePlayParamBuilder builder) {
        startPlayForLive(context, builder.mAlbum, builder.mFrom, builder.mFlowerList, builder.mBuySource, builder.mTabSource);
    }

    public void startCarouselPlayerPage(Context context, CarouselPlayParamBuilder builder) {
        startPlayForCarousel(context, builder.mChannelCarousel, builder.mFrom, builder.mTabSource);
    }

    public void startAlbumDetailPlayerPage(Context context, AlbumDetailPlayParamBuilder builder) {
        startAlbumDetailInner(context, builder.mAlbumInfo, builder.mParam, builder.mIsComplete, builder.mFrom, builder.mBuySource, builder.mTabSource, builder.mClearTaskFlag, builder.mContinueNextVideo);
    }

    public void startNewsDetailPlayerPage(Context context, NewsDetailPlayParamBuilder builder) {
        startDetailForLoop(context, builder.mFrom, builder.mChannelName, builder.mParams, builder.mBuySource, builder.mTabSource);
    }

    public void startPushPlayerPage(Context context, PushPlayParamBuilder builder) {
        if (builder.mMultiScreenParams == null) {
            throw new NullPointerException("The command for outside video should not be null!");
        }
        LogUtils.m1568d(TAG, ">> startVideoPlayForPushVideo: command=" + builder.mMultiScreenParams + ", from=" + builder.mFrom + ", se=" + builder.mSe + ", buySource=" + builder.mBuySource + ", tabSource=" + builder.mTabSource);
        if (!PlayerAppConfig.handleStartPlayForPush(context, builder.mMultiScreenParams, builder.mFrom, builder.mSe)) {
            startPlayForPush(context, builder.mMultiScreenParams, builder.mFrom, builder.mSe, builder.mBuySource, builder.mTabSource);
        }
    }

    public void startPlayerAdapterSettingPage(Context context) {
        PageIOUtils.activityIn(context, new Intent(context, PlayerAdapterSettingActivity.class));
    }
}
