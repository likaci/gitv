package com.gala.video.app.player.utils;

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
import com.gala.video.app.player.MultiProcPlayerActivity;
import com.gala.video.app.player.PlayerActivity;
import com.gala.video.lib.framework.core.cache.LoopCache;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;

public class PlayerUtils {
    private static final String EXTRA_INDEX = "EXTRA_PLAYER_INDEX";
    private static final String TAG = "PlayerUtils";
    private static String sEventId;
    private static final LoopCache<Bundle> sIntentBundle = new LoopCache(5);

    private static class MyPluginStateListener implements OnStateChangedListener {
        private Context mContext;
        private Intent mIntent;

        public MyPluginStateListener(Context context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        public void onSuccess() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(PlayerUtils.TAG, "[PERF-LOADING]tm_plugin.load");
            }
            GlobalPerformanceTracker.instance().recordPerformanceStepEnd(PlayerUtils.sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
            GlobalPerformanceTracker.instance().recordPerformanceStepStart(PlayerUtils.sEventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
            this.mIntent.addCategory("android.intent.category.DEFAULT");
            PageIOUtils.activityIn(this.mContext, this.mIntent);
        }

        public void onFailed() {
        }

        public void onCanceled() {
        }

        public void onLoading() {
        }
    }

    public static class VideoPlayParamsBuilder {
        Album mAlbumInfo = null;
        String mBuySource = "";
        boolean mClearTaskFlag = false;
        boolean mContinueNextVideo = true;
        String mFrom = "";
        int mPlayOrder = 0;
        PlayParams mPlayParams = null;
        String mTabSource = "";

        public VideoPlayParamsBuilder setAlbumInfo(Album albumInfo) {
            this.mAlbumInfo = albumInfo;
            return this;
        }

        public VideoPlayParamsBuilder setPlayOrder(int playOrder) {
            this.mPlayOrder = playOrder;
            return this;
        }

        public VideoPlayParamsBuilder setPlayParams(PlayParams playParams) {
            this.mPlayParams = playParams;
            return this;
        }

        public VideoPlayParamsBuilder setClearTaskFlag(boolean clearTaskFlag) {
            this.mClearTaskFlag = clearTaskFlag;
            return this;
        }

        public VideoPlayParamsBuilder setFrom(String from) {
            this.mFrom = from;
            return this;
        }

        public VideoPlayParamsBuilder setBuySource(String buySource) {
            this.mBuySource = buySource;
            return this;
        }

        public VideoPlayParamsBuilder setTabSource(String tabSource) {
            this.mTabSource = tabSource;
            return this;
        }

        public VideoPlayParamsBuilder setContinueNextVideo(boolean continueNextVideo) {
            this.mContinueNextVideo = continueNextVideo;
            return this;
        }
    }

    public static void fillOutsideInfoIfNeed(Bundle extras, Album albumInfo, String from) {
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
                LogUtils.d(TAG, "fillOutsideInfoIfNeed() fill out album info!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fillOutsideInfoIfNeed() ignore!");
        }
    }

    public static void fillOutsideIntentInfoIfNeed(Intent intent, Album albumInfo, String from) {
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
                LogUtils.d(TAG, "fillOutsideIntentInfoIfNeed() fill out album info!");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fillOutsideIntentInfoIfNeed() ignore!");
        }
    }

    private static void startPlayForPush(Context context, MultiScreenParams msParams, String from, String se, String buySource, String tabSource) {
        LogUtils.d(TAG, ">> startPlayForPush: ms params=" + msParamsToString(msParams) + ", from=" + from + ", se=" + se + ", buySource=" + buySource + ", tabSource=" + tabSource);
        Intent intent = new Intent();
        intent.addFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        sEventId = PingBackUtils.createEventId();
        String className = getPlayerActivityClass().getName();
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
            intent.putExtra(PlayerIntentConfig.PUSH_V_FLAG, msParams.v);
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
            extras.putString(PlayerIntentConfig.PUSH_V_FLAG, msParams.v);
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

    public static void startNetDiagnoseActivity(Context context, NetDiagnoseInfo info, int playerType) {
        LogUtils.d(TAG, ">> startNetDiagnoseActivity: info=" + info);
        Intent intent = new Intent(IntentUtils.getActionName(ActionSet.ACT_NET_DIAGNOSE));
        intent.putExtra("intent_key_video_info", info);
        intent.putExtra(IntentConfig2.INTENT_PARAM_NETDIAGNOSE_SOURCE, 1);
        intent.putExtra("playerType", playerType);
        PageIOUtils.activityIn(context, intent);
    }

    private static void recordCallTimeAndDelayPingback(Bundle extras) {
        extras.putLong(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, GlobalPerformanceTracker.instance().recordRoutineStart("pageInitToStarted"));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[PERF-LOADING]page.click");
        }
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
    }

    public static synchronized Bundle wrapIntentExtras(Intent intent) {
        Bundle bundle;
        synchronized (PlayerUtils.class) {
            bundle = new Bundle();
            int index = sIntentBundle.add(bundle);
            intent.putExtra(EXTRA_INDEX, index);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "wrapIntentExtras(), index=" + index);
            }
        }
        return bundle;
    }

    public static synchronized boolean restoreIntentExtras(Intent intent) {
        boolean z;
        synchronized (PlayerUtils.class) {
            int index = intent.getIntExtra(EXTRA_INDEX, -1);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "restoreIntentExtras(), index=" + index);
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

    private static String msParamsToString(MultiScreenParams params) {
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

    private static Class<?> getPlayerActivityClass() {
        return Project.getInstance().getBuild().supportPlayerMultiProcess() ? MultiProcPlayerActivity.class : PlayerActivity.class;
    }
}
